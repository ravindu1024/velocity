package com.rw.velocity;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rw.velocity.builder.DownloadRequestBuilder;
import com.rw.velocity.builder.RequestBuilder;
import com.rw.velocity.builder.UploadRequestBuilder;

import java.util.HashMap;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 13/12/16.
 */

@SuppressWarnings("WeakerAccess")
public class Velocity
{
    public static class Data
    {
        @NonNull public final String body;
        @NonNull public final HashMap<String, String> responseHeaders;
        @Nullable public final Bitmap image;
        @Nullable public final Object userData;
        public final int status;


        Data(@NonNull String body, int status, @NonNull HashMap<String, String> responseHeaders, @Nullable Bitmap image, @Nullable Object userData)
        {
            this.body = body;
            this.status = status;
            this.responseHeaders = responseHeaders;
            this.image = image;
            this.userData = userData;
        }
    }


    public interface DataCallback
    {
        void onVelocitySuccess();
        void onVelocityFailed();
    }


    /**
     * IMPORTANT: Please initialize the background thread(s) before making any requests
     * @param numThreads number of background threads to handle network connections.
     *                   The recommended value is 3, but could be increased based on application
     *                   complexity.
     */
    public static void initialize(int numThreads)
    {

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
        return new RequestBuilder();
    }

    /**
     * Prepare a builder to make a file download request. Http request type will be "GET" by default
     * @param url request address
     * @return request builder
     */
    public static DownloadRequestBuilder download(String url)
    {
        return new DownloadRequestBuilder();
    }

    /**
     * Prepare a builder to make an upload request. Http request type will be "POST" by default
     * @param url request address
     * @return request builder
     */
    public static UploadRequestBuilder upload(String url)
    {
        return new UploadRequestBuilder();
    }

}
