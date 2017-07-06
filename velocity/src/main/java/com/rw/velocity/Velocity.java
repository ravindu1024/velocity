package com.rw.velocity;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 13/12/16.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Velocity
{
    static Settings mSettings = new Settings();


    private Velocity()
    {
        //blocked constructor
    }

    /**
     * IMPORTANT: Please initialize the background thread(s) before making any requests
     *
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
     * <p>
     * If the return type is an image, it will be in Velocity.Data.image as a Bitmap
     * If it is any other type, the reply will be in Velocity.Data.body as raw text
     *
     * @param url request address
     * @return request builder
     */
    public static RequestBuilder load(String url)
    {
        return new RequestBuilder(url, RequestBuilder.RequestType.Text);
    }

    /**
     * Prepare a builder to make a file download request. Http request type will be "GET" by default
     *
     * @param url request address
     * @return request builder
     */
    public static RequestBuilder download(String url)
    {
        return new RequestBuilder(url, RequestBuilder.RequestType.Download);
    }

    /**
     * Prepare a builder to make an upload request. Http request type will be "POST" by default
     *
     * @param url request address
     * @return request builder
     */
    public static RequestBuilder upload(String url)
    {
        return new RequestBuilder(url, RequestBuilder.RequestType.Upload);
    }


    /**
     * Execute all queued requests. The results is given in a single callback.
     * @param callback {@link MultiResponseListener} callback
     */
    public static void executeQueue(MultiResponseListener callback)
    {
        MultiResponseHandler.execute(callback);
    }


    /**
     * This is only partially functional. Right now it only supports the resource owner password flow.
     *
     * @param publicUrl token url
     * @return OAuthBuilder
     */
    public static OAuthBuilder loginWithOAuth(String publicUrl)
    {
        return new OAuthBuilder(publicUrl);
    }


    public enum DownloadType
    {
        Automatic, Base64toPdf, Base64toJpg
    }

    public static class Response
    {
        @NonNull
        public final String body;
        public final Map<String, List<String>> responseHeaders;
        @Nullable
        public final Bitmap image;
        @Nullable
        public final Object userData;
        public final int responseCode;
        public final int requestId;
        @NonNull
        public final String requestUrl;

        Response(int requestId, @NonNull String body, int status, @Nullable Map<String, List<String>> responseHeaders, @Nullable Bitmap image, @Nullable Object userData, @NonNull String url)
        {
            this.requestId = requestId;
            this.body = body;
            this.responseCode = status;
            this.image = image;
            this.userData = userData;
            this.requestUrl = url;

            if(responseHeaders == null)
            {
                this.responseHeaders = new HashMap<>();
                this.responseHeaders.put("header-error", new ArrayList<String>());
            }
            else
                this.responseHeaders = responseHeaders;
        }

    }


    public interface ResponseListener
    {
        void onVelocitySuccess(Response response);

        void onVelocityFailed(Response error);
    }

    public interface MultiResponseListener
    {
        void onVelocityMultiResponseSuccess(HashMap<Integer, Response> responseMap);

        void onVelocityMultiResponseError(HashMap<Integer, Response> errorMap);
    }

    public interface ProgressListener
    {
        void onFileProgress(int percentage);
    }

    public interface OAuthListener
    {
        void onOAuthToken(String token);
        void onOAuthError(Velocity.Response error);
    }


    public static class Settings
    {
        static int TIMEOUT = 15000;
        static int READ_TIMEOUT = 30000;
        static int MOCK_RESPONSE_TIME = 1000;
        static boolean GLOBAL_MOCK = false;
        static int GLOBAL_NETWORK_DELAY = 0;
        static int MAX_REDIRECTS = 10;
        static boolean LOGS_ENABLED = false;

        //upload settings
        static final String LINEEND = "\r\n";
        static final String TWOHYPHENS = "--";
        static String BOUNDARY = "*****";
        static int MAX_BUFFER = 4096;

        /**
         * Sets a specified timeout value, in milliseconds, to be used when opening a connection to the specified URL.
         * A timeout of zero is interpreted as an infinite timeout.
         *
         * @param timeout connection timeout in milliseconds
         */
        public void setTimeout(int timeout)
        {
            NetLog.d("set connection timeout: " + timeout);
            TIMEOUT = timeout;
        }

        /**
         * Sets the read timeout to a specified timeout, in milliseconds. A non-zero value specifies the timeout when
         * reading from Input stream once a connection is established to a resource.
         * A timeout of zero is interpreted as an infinite timeout.
         *
         * @param readTimeout read timeout in milliseconds
         */
        public void setReadTimeout(int readTimeout)
        {
            NetLog.d("set read timeout: " + readTimeout);
            READ_TIMEOUT = readTimeout;
        }

        /**
         * Set the boundary String for multi-part uploads.
         * The default is "*****"
         *
         * @param boundary multi-part upload boundary string
         */
        public void setMultipartBoundary(String boundary)
        {
            NetLog.d("set multipart boundary: " + boundary);
            BOUNDARY = boundary;
        }

        /**
         * Set the buffer size for multi-part uploads
         * Default size is 1024KB
         *
         * @param bufferSize upload buffer size
         */
        public void setMaxTransferBufferSize(int bufferSize)
        {
            NetLog.d("set max transfer buffer size: " + bufferSize);
            MAX_BUFFER = bufferSize;
        }

        /**
         * Set the global response time for mocked api calls
         * @param waitTime response time
         */
        public void setMockResponseTime(int waitTime)
        {
            NetLog.d("Set mock response delay to: "+waitTime);
            MOCK_RESPONSE_TIME = waitTime;
        }

        public void setGloballyMocked(boolean mocked)
        {
            NetLog.d("set global mock : " + mocked);
            GLOBAL_MOCK = mocked;
        }

        /**
         * Set the global network simulation delay
         * @param delay delay in milliseconds
         */
        public void setGlobalNetworkDelay(int delay)
        {
            NetLog.d("set global network delay: " + delay);
            GLOBAL_NETWORK_DELAY = delay;
        }

        public void setMaxRedirects(int redirects)
        {
            NetLog.d("Set max redirects: " + redirects);
            MAX_REDIRECTS = redirects;
        }

        public void setLoggingEnabled(boolean enabled)
        {
            android.util.Log.d("Velocity", "Log enabled: " + enabled);
            LOGS_ENABLED = enabled;
        }
    }

    /**
     * Get the current Settings instance. It is best to leave them unchanged
     * unless you really want to customize the values
     *
     * @return Velocity Global settings
     */
    public static Settings getSettings()
    {
        return mSettings;
    }


}
