package com.rw.velocity;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.RequiresApi;
import android.support.annotation.WorkerThread;

import com.google.gson.Gson;

import java.io.InputStream;
import java.util.HashMap;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 13/12/16.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class RequestBuilder
{
    HashMap<String, String> headers = new HashMap<>();
    HashMap<String, String> params = new HashMap<>();
    HashMap<String, String> pathParams = new HashMap<>();
    String rawParams;
    String requestMethod = "GET";
    Object userData;
    String uploadFile;
    String uploadParamName;
    String uploadMimeType;
    InputStream uploadStream;
    String downloadFile;
    Velocity.DownloadType downloadType;
    String downloadUiTitle = "";
    String downloadUiDescr = "";
    Context context;
    int requestId = 0;
    String contentType;
    Velocity.ResponseListener callback;
    String url;
    final String originUrl;
    Velocity.ProgressListener progressListener;
    boolean mocked = false;
    String mockResponse = "Global Mock is enabled. Velovity will mock all calls and return this message.";

    private Velocity.RequestType requestType = Velocity.RequestType.Text;

    private RequestBuilder()
    {
        //will not be called from outside
        this.url = null;
        this.originUrl = null;
    }

    public RequestBuilder(String url, Velocity.RequestType type)
    {
        this.url = url;
        this.originUrl = url;
        this.requestType = type;
    }

    /**
     * Add HTTP request headers as a HashMap<String, String>
     *
     * @param headers request headers
     * @return request builder
     */
    public RequestBuilder withHeaders(HashMap<String, String> headers)
    {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * Add a single request header
     *
     * @param key   request header key
     * @param value request header value
     * @return request builder
     */
    public RequestBuilder withHeader(String key, String value)
    {
        this.headers.put(key, value);
        return this;
    }

    /**
     * Add a single path encoded parameter
     * @param key path parameter key
     * @param value path parameter value
     * @return request builder
     */
    public RequestBuilder withPathParam(String key, String value)
    {
        this.pathParams.put(key, value);
        return this;
    }


    /**
     * Add a list of path parameters
     * @param pathParams map containing path parameters and key values
     * @return request builder
     */
    public RequestBuilder withPathParams(HashMap<String, String> pathParams)
    {
        this.pathParams.putAll(pathParams);
        return this;
    }

    /**
     * Add HTTP form data as a Hashmap<String, String>. Content type: application/x-www-form-urlencoded-data
     *
     * @param params form data
     * @return request builder
     */
    public RequestBuilder withBody(HashMap<String, String> params)
    {
        this.params.putAll(params);
        this.contentType = Velocity.ContentType.FORM_DATA_URLENCODED.toString();
        return this;
    }

    /**
     * Add HTTP params as a String (raw JSON string). "Content type" will not be set in request headers.
     *
     * @param params raw parameter String
     * @return request builder
     */
    public RequestBuilder withBody(String params)
    {
        this.rawParams = params;
        this.contentType = Velocity.ContentType.TEXT.toString();
        return this;
    }

    /**
     * Add HTTP body as a json object. Content type: application/json
     * @param toJsonObect body
     * @return this builder
     */
    public RequestBuilder withJsonBody(Object toJsonObect)
    {
        this.rawParams = new Gson().toJson(toJsonObect);
        this.contentType = Velocity.ContentType.JSON.toString();

        return this;
    }

    /**
     * Add HTTP params as a String and set the content type. This is added as a request header specifying
     * the parameter type for the call
     *
     * @param params    raw parameter String
     * @param paramType from {@link Velocity.ContentType}
     * @return request builder
     */
    public RequestBuilder withBody(String params, Velocity.ContentType paramType)
    {
        this.rawParams = params;
        this.contentType = paramType.toString();
        return this;
    }


    /**
     * Add a key value pair as encoded form data. Content type: application/form-x-www-form-urlencoded
     * @param key parameter key
     * @param value parameter value
     * @return request builder
     */
    public RequestBuilder withFormData(String key, String value)
    {
        this.params.put(key, value);
        this.contentType = Velocity.ContentType.FORM_DATA_URLENCODED.toString();
        return this;
    }

    /**
     * Set HTTP body content type
     * @param contentType select from {@link Velocity.ContentType}
     * @return this builder
     */
    public RequestBuilder withBodyContentType(Velocity.ContentType contentType)
    {
        this.contentType = contentType.toString();
        return this;
    }


    public RequestBuilder withRequestMethod(String method)
    {
        this.requestMethod = method;
        return this;
    }

    /**
     * Set any object to this field to get it returned through the data callback
     *
     * @param data user data
     * @return request builder
     */
    public RequestBuilder withData(Object data)
    {
        this.userData = data;
        return this;
    }

    /**
     * Set this request as a mock request that will return the given response
     * @param mockResponse the response to be returned
     * @return request builder
     */
    public RequestBuilder setMocked(String mockResponse)
    {
        this.mocked = true;
        this.mockResponse = mockResponse;
        return this;
    }

    /**
     * Set the download location and filename
     * @param downloadFile the name of the file to be saved
     * @return this builder
     */
    public RequestBuilder setDownloadFile(String downloadFile)
    {
        this.downloadFile = downloadFile;
        return this;
    }

    /**
     * Set the download file type
     * 'Automatic' : set the type from the response headers
     * 'Base64toPdf' : convert the base64 response into a pdf file and save
     * 'Base64toJpg' : convert the base64 response into a jpeg and save
     * @param type download file type
     * @return this builder
     */
    public RequestBuilder setDownloadFileType(Velocity.DownloadType type)
    {
        this.downloadType = type;
        return this;
    }

    /**
     * set the details of the file to be uploaded
     * @param paramName form parameter name(or field) corresponding to this file (eg: "profile-picture" or "id-scan" etc)
     * @param mimeType mime type of the file. eg: "text/plain" , "image/jpeg" or "video/mp4"
     * @param uploadFile path to the file to be uploaded
     * @return RequestBuilder
     */
    public RequestBuilder setUploadSource(String paramName, String mimeType, String uploadFile)
    {
        this.uploadMimeType = mimeType;
        this.uploadFile = uploadFile;
        this.uploadParamName = paramName;
        return this;
    }

    /**
     *
     * @see RequestBuilder#setUploadSource(String, String, String)
     * @param uploadStream an open file as an {@link InputStream}
     */
    public RequestBuilder setUploadSource(String paramName, String mimeType, InputStream uploadStream)
    {
        this.uploadMimeType = mimeType;
        this.uploadParamName = paramName;
        this.uploadStream = uploadStream;
        return this;
    }

    /**
     * @see RequestBuilder#setUploadSource(String, String, String)
     * @param stream an opened data stream
     * @return RequestBuilder
     */
    public RequestBuilder setUploadSource(InputStream stream)
    {
        this.uploadMimeType = "application/octet-stream";
        this.uploadStream = stream;

        return this;
    }

    /**
     * Add downloaded file to the system Downloads Ui
     * @param context calling context
     * @param title title to be displayed in Downloads
     * @param description description of the downloaded file to be displayed in Downloads
     * @return RequestBuilder
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    public RequestBuilder addToDownloadsFolder(Context context, String title, String description)
    {
        this.context = context;
        this.downloadUiTitle = title;
        this.downloadUiDescr = description;

        return this;
    }



    /**
     * Receive progress notifications on a file download or upload.
     * This parameter will be ignored for text or image requests
     *
     * @param listener file progress listener
     * @return request builder
     */
    public RequestBuilder withProgressListener(Velocity.ProgressListener listener)
    {
        this.progressListener = listener;
        return this;
    }

    /**
     * Make a network request to recieve data in the callback.
     * See also {@link RequestBuilder#connect(int, Velocity.ResponseListener)}
     *
     * @param callback data callback
     */
    public void connect(Velocity.ResponseListener callback)
    {
        this.callback = callback;
        this.url += getPathParams();

        ThreadPool.getThreadPool().postRequestDelayed(resolveRequest(), Velocity.Settings.GLOBAL_NETWORK_DELAY);
    }

    /**
     * Make a network request to recieve data in the callback. The request id is returned so that
     * the same callback can be used for multiple calls.
     *
     * @param requestId unique request id
     * @param callback  data callback
     */
    public void connect(int requestId, Velocity.ResponseListener callback)
    {
        this.requestId = requestId;
        this.callback = callback;
        this.url += getPathParams();

        ThreadPool.getThreadPool().postRequestDelayed(resolveRequest(), Velocity.Settings.GLOBAL_NETWORK_DELAY);
    }


    /**
     * Add the calling RequestBuilder to the request queue
     * @param requestId identifier of the request
     */
    public void queue(int requestId)
    {
        this.requestId = requestId;

        MultiResponseHandler.addToQueue(this);
    }

    @WorkerThread
    public Velocity.Response connectBlocking()
    {
        if(Looper.myLooper() == Looper.getMainLooper())
            throw new NetworkOnMainThreadException();

        return new SynchronousWrapper().connect(this);
    }





    private String getPathParams()
    {
        String path = "";

        if(!pathParams.isEmpty())
        {
            for(String key : pathParams.keySet())
            {
                String value = pathParams.get(key);

                path += "&" + key + "=" + value;
            }
        }

        if(path.startsWith("&"))
            path = "?" + path.substring(1);

        return path;
    }


    private Request resolveRequest()
    {
        Request request;
        if (requestType == Velocity.RequestType.Download)
            request = new DownloadRequest(this);
        else if (requestType == Velocity.RequestType.Upload)
            request = new UploadRequest(this);
        else
            request = new Request(this);

        return request;
    }
}
