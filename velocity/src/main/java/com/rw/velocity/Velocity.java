package com.rw.velocity;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
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

    public enum DownloadFileType
    {
        Automatic, Base64toPdf, Base64toJpg
    }

    public static class Data
    {
        @NonNull public final String body;
        @NonNull public final Map<String, List<String>> responseHeaders;
        @Nullable public final Bitmap image;
        @Nullable public final Object userData;
        public final int status;
        public final int requestId;

        Data(int requestId, @NonNull String body, int status, @NonNull Map<String, List<String>> responseHeaders, @Nullable Bitmap image, @Nullable Object userData)
        {
            this.requestId = requestId;
            this.body = body;
            this.status = status;
            this.responseHeaders = responseHeaders;
            this.image = image;
            this.userData = userData;
        }

    }


    public interface DataCallback
    {
        void onVelocitySuccess(Data response);
        void onVelocityFailed(Data error);
    }



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


    public static class Settings
    {
        static int TIMEOUT = 5000;

        /**
         * Sets a specified timeout value, in milliseconds, to be used when opening a connection to the specified URL.
         * A timeout of zero is interpreted as an infinite timeout.
         * @param timeout
         */
        public static void setTimeout(int timeout)
        {
            TIMEOUT = timeout;
        }
    }


}
