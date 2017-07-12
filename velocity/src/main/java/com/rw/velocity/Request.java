package com.rw.velocity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 15/12/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

class Request
{
    final RequestBuilder mBuilder;

    HttpURLConnection mConnection = null;
    StringBuilder mResponse = new StringBuilder();
    private Bitmap mResponseImage = null;
    int mResponseCode = 0;


    Request(RequestBuilder builder)
    {
        mBuilder = builder;
    }


    /**
     * DO NOT CALL THIS DIRECTLY
     * Execute the network requet and post the response.
     * This function will be called from a worker thread from within the Threadpool
     */
    void execute()
    {
        long t = SystemClock.elapsedRealtime();

        if(mBuilder.mocked || Velocity.Settings.GLOBAL_MOCK)
        {
            try
            {
                Thread.sleep(Velocity.Settings.MOCK_RESPONSE_TIME);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            NetLog.d(padRight("MOCKED/" + mBuilder.requestMethod, 10) + " : " + padLeft((SystemClock.elapsedRealtime() - t) + "ms : ", 10) + mBuilder.url);
            returnMockResponse();
        }
        else
        {

            boolean success = initializeConnection();

            if (success)
            {
                success = readResponse();

                if (!success)
                    readError();
            }

            NetLog.d(padRight(mResponseCode + "/" + mBuilder.requestMethod, 10) + " : " + padLeft((SystemClock.elapsedRealtime() - t) + "ms : ", 10) + mBuilder.url);
            returnResponse(success);
        }
    }

    private String padLeft(String s, int n)
    {
        return String.format("%1$" + n + "s", s);
    }

    private String padRight(String s, int n)
    {
        return String.format("%1$-" + n + "s", s);
    }

    void setupRequestHeaders()
    {
        mConnection.setRequestProperty("User-Agent", Velocity.Settings.USER_AGENT);

        if(mBuilder.contentType != null && !mBuilder.contentType.equalsIgnoreCase(Velocity.ContentType.TEXT.toString()))
            mConnection.setRequestProperty("Content-Type", mBuilder.contentType);


        if (!mBuilder.headers.isEmpty())
        {
            for (String key : mBuilder.headers.keySet())
                mConnection.setRequestProperty(key, mBuilder.headers.get(key));
        }
    }

    void setupRequestBody() throws IOException
    {
        if (mBuilder.requestMethod.equalsIgnoreCase("GET") || mBuilder.requestMethod.equalsIgnoreCase("COPY") || mBuilder.requestMethod.equalsIgnoreCase("HEAD")
                || mBuilder.requestMethod.equalsIgnoreCase("PURGE") || mBuilder.requestMethod.equalsIgnoreCase("UNLOCK"))
        {
            //do not send params for these request methods
            return;
        }

        mConnection.setDoInput(true);
        mConnection.setDoOutput(true);

        String params = null;

        if (mBuilder.rawParams != null && mBuilder.rawParams.length() > 0)
        {
            params = mBuilder.rawParams;
        }
        else if (!mBuilder.params.isEmpty())
        {
            params = getFormattedParams();
        }

        if (params != null)
        {
            if(mBuilder.contentType.equalsIgnoreCase(Velocity.ContentType.FORM_DATA_MULTIPART.toString()))
            {
                DataOutputStream dos = new DataOutputStream(mConnection.getOutputStream());
                for(String param : mBuilder.params.keySet())
                {
                    String val = mBuilder.params.get(param);

                    dos.writeBytes(Velocity.Settings.TWOHYPHENS + Velocity.Settings.BOUNDARY + Velocity.Settings.LINEEND);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + param + "\"" + Velocity.Settings.LINEEND);
                    dos.writeBytes("Content-Type: text/plain" + Velocity.Settings.LINEEND);
                    dos.writeBytes(Velocity.Settings.LINEEND);
                    dos.writeBytes(val);
                    dos.writeBytes(Velocity.Settings.LINEEND);
                }

                dos.writeBytes(Velocity.Settings.TWOHYPHENS + Velocity.Settings.BOUNDARY + Velocity.Settings.TWOHYPHENS + Velocity.Settings.LINEEND);

                dos.flush();
                dos.close();
            }
            else
            {
                OutputStream os = mConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(params);
                writer.flush();
                writer.close();
                os.close();
            }
        }
    }

    private String getFormattedParams() throws UnsupportedEncodingException
    {
        StringBuilder params = new StringBuilder();
        boolean first = true;

        for (String key : mBuilder.params.keySet())
        {
            if (first)
                first = false;
            else
                params.append("&");

            params.append(URLEncoder.encode(key, "UTF-8"));
            params.append("=");
            params.append(URLEncoder.encode(mBuilder.params.get(key), "UTF-8"));
        }

        return params.toString();
    }



    private boolean initializeConnection()
    {
        boolean ret;

        try
        {
            mResponseCode = makeConnection();

            int count = 0;
            while((mResponseCode == HttpURLConnection.HTTP_MOVED_PERM || mResponseCode == HttpURLConnection.HTTP_MOVED_TEMP) && count < Velocity.Settings.MAX_REDIRECTS)
            {
                count++;
                mBuilder.url = mConnection.getHeaderField("Location");
                NetLog.d("redirected : " + mBuilder.url);

                mResponseCode = makeConnection();
            }

            ret = true;
        }
        catch (IOException ioe)
        {
            ret = false;
            mResponse = new StringBuilder(ioe.getMessage());
        }

        return ret;
    }

    private int makeConnection() throws IOException
    {
        URL url = new URL(mBuilder.url);

        if (url.getProtocol().equalsIgnoreCase("https"))
            mConnection = (HttpsURLConnection) url.openConnection();
        else
            mConnection = (HttpURLConnection) url.openConnection();

        mConnection.setRequestMethod(mBuilder.requestMethod);
        mConnection.setConnectTimeout(Velocity.Settings.TIMEOUT);
        mConnection.setReadTimeout(Velocity.Settings.READ_TIMEOUT);

        setupRequestHeaders();

        setupRequestBody();

        mConnection.connect();

        return mConnection.getResponseCode();
    }

    boolean readResponse()
    {
        boolean ret;

        try
        {
            if (mResponseCode / 100 == 2) //all 2xx codes are OK
            {

                if (mConnection.getContentType() != null && mConnection.getContentType().startsWith("image"))
                {
                    mResponseImage = BitmapFactory.decodeStream(mConnection.getInputStream());
                    mResponse.append(mConnection.getContentType());
                }
                else
                {
                    InputStream in = new BufferedInputStream(mConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        mResponse.append(line);
                    }
                }

                ret = true;
            }
            else
                ret = false;
        }
        catch (IOException e)
        {
            ret = false;
            mResponse = new StringBuilder(e.getMessage());
        }

        return ret;
    }

    private void readError()
    {
        InputStream in = new BufferedInputStream(mConnection.getErrorStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                mResponse.append(line);
            }
        }
        catch (IOException e)
        {
            mResponse.append(e.getMessage());
        }

    }

    private void returnMockResponse()
    {
        final Velocity.Response reply = new Velocity.Response(mBuilder.requestId,
                mBuilder.mockResponse,
                200,
                null,
                null,
                mBuilder.userData,
                mBuilder);

        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                if (mBuilder.callback != null)
                {
                    mBuilder.callback.onVelocitySuccess(reply);
                }
                else
                    NetLog.d("Warning: No Data callback supplied");
            }
        };

        ThreadPool.getThreadPool().postToUiThread(r);
    }

    private void returnResponse(final boolean success)
    {
        final Velocity.Response reply = new Velocity.Response(mBuilder.requestId,
                mResponse.toString(),
                mResponseCode,
                (mConnection == null) ? null : mConnection.getHeaderFields(),
                mResponseImage,
                mBuilder.userData,
                mBuilder);

        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                if (mBuilder.callback != null)
                {
                    if (success)
                        mBuilder.callback.onVelocitySuccess(reply);
                    else
                        mBuilder.callback.onVelocityFailed(reply);
                }
                else
                    NetLog.d("Warning: No Data callback supplied");
            }
        };

        ThreadPool.getThreadPool().postToUiThread(r);
    }
}
