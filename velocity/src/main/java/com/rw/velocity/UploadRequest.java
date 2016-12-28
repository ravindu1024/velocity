package com.rw.velocity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 21/12/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

class UploadRequest extends Request
{
    UploadRequest(RequestBuilder builder)
    {
        super(builder);
    }


    @Override
    void setupRequestHeaders()
    {
        super.setupRequestHeaders();

        mConnection.setRequestProperty("Connection", "Keep-Alive");
        mConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + Velocity.Settings.BOUNDARY);
        mConnection.setRequestProperty("Cache-Control", "no-cache");
    }


    @Override
    void setupRequestBody() throws IOException
    {
        int bytesRead;
        int bytesAvailable;
        int bufferSize;
        byte[] buffer;
        int prevPercentage = 0;
        int maxBufferSize = Velocity.Settings.MAX_BUFFER;
        InputStream fileInputStream;


        mConnection.setDoInput(true);
        mConnection.setDoOutput(true);
        mConnection.setUseCaches(false);


        if (mBuilder.uploadStream != null)
            fileInputStream = mBuilder.uploadStream;
        else
            fileInputStream = new FileInputStream(new File(mBuilder.uploadFile));

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        int totalToWrite = bytesAvailable;
        int totalWritten = 0;


        DataOutputStream dos = new DataOutputStream(mConnection.getOutputStream());

        dos.writeBytes(Velocity.Settings.TWOHYPHENS + Velocity.Settings.BOUNDARY + Velocity.Settings.LINEEND);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + mBuilder.uploadParamName + "\"; filename=\"" + "uploadfile" + "\"" + Velocity.Settings.LINEEND);
        dos.writeBytes("Content-Type: " + mBuilder.uploadMimeType + Velocity.Settings.LINEEND);
        dos.writeBytes(Velocity.Settings.LINEEND);


        while (bytesRead > 0)
        {
            dos.write(buffer, 0, bufferSize);

            totalWritten += bufferSize;

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            if (mBuilder.progressListener != null)
            {
                int prog = totalWritten * 100 / totalToWrite;
                if (prog > prevPercentage)
                {
                    prevPercentage = prog;
                    mBuilder.progressListener.onFileProgress(prog);
                }
            }
        }
        dos.writeBytes(Velocity.Settings.LINEEND);


        //write other form data
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

        //end all post data
        dos.writeBytes(Velocity.Settings.TWOHYPHENS + Velocity.Settings.BOUNDARY + Velocity.Settings.TWOHYPHENS + Velocity.Settings.LINEEND);

        dos.flush();
        dos.close();
        fileInputStream.close();
    }

}
