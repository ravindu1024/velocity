package com.rw.velocity;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 13/12/16.
 */

@SuppressWarnings("WeakerAccess")
public class Velocity
{
    static Settings mSettings = new Settings();


    private Velocity()
    {
        //blocked constructor
    }

    /**
     * IMPORTANT: Please initialize the background thread(s) before making any requests
     * @param numThreads number of background threads to handle network connections.
     *                   The recommended value is 3, but could be increased based on application
     *                   complexity.
     */
    public static void initialize(int numThreads)
    {
        try
        {
            ThreadPool.initialize(numThreads);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Prepare a builder to make a network call. Http request type will be "GET" by default
     * Minimum argument call looks like this: Velocity.load(url).connect(callback)
     *
     * If the return type is an image, it will be in Velocity.Data.image as a Bitmap
     * If it is any other type, the reply will be in Velocity.Data.body as raw text
     * @param url request address
     * @return request builder
     */
    public static RequestBuilder load(String url)
    {
        return new RequestBuilder(url);
    }

    /**
     * Prepare a builder to make a file download request. Http request type will be "GET" by default
     * @param url request address
     * @return request builder
     */
    public static RequestBuilder download(String url)
    {
        return new RequestBuilder(url);
    }

    /**
     * Prepare a builder to make an upload request. Http request type will be "POST" by default
     * @param url request address
     * @return request builder
     */
    public static RequestBuilder upload(String url)
    {
        return new RequestBuilder(url);
    }




    public enum DownloadType
    {
        Automatic, Base64toPdf, Base64toJpg
    }

    public static class Response
    {
        @NonNull public final String body;
        @NonNull public final Map<String, List<String>> responseHeaders;
        @Nullable public final Bitmap image;
        @Nullable public final Object userData;
        public final int status;
        public final int requestId;

        Response(int requestId, @NonNull String body, int status, @NonNull Map<String, List<String>> responseHeaders, @Nullable Bitmap image, @Nullable Object userData)
        {
            this.requestId = requestId;
            this.body = body;
            this.status = status;
            this.responseHeaders = responseHeaders;
            this.image = image;
            this.userData = userData;
        }

    }


    public interface ResponseListener
    {
        void onVelocitySuccess(Response response);
        void onVelocityFailed(Response error);
    }


    public static class Settings
    {
        int TIMEOUT = 5000;
        int READ_TIMEOUT = 0;

        /**
         * Sets a specified timeout value, in milliseconds, to be used when opening a connection to the specified URL.
         * A timeout of zero is interpreted as an infinite timeout.
         * @param timeout connection timeout in milliseconds
         */
        public void setTimeout(int timeout)
        {
            TIMEOUT = timeout;
        }

        /**
         * Sets the read timeout to a specified timeout, in milliseconds. A non-zero value specifies the timeout when
         * reading from Input stream once a connection is established to a resource.
         * A timeout of zero is interpreted as an infinite timeout.
         * @param readTimeout read timeout in milliseconds
         */
        public void setReadTimeout(int readTimeout)
        {
            READ_TIMEOUT = readTimeout;
        }
    }

    public static Settings getSettings()
    {
        return mSettings;
    }


}
