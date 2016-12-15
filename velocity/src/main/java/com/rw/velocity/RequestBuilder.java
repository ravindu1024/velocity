package com.rw.velocity;

import android.net.Uri;

import java.io.File;
import java.util.HashMap;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 13/12/16.
 */

public class RequestBuilder
{
    HashMap<String, String> headers = new HashMap<>();
    HashMap<String, String> params = new HashMap<>();
    String rawParams = null;
    String requestMethod = "GET";
    Object userData = null;
    Uri uploadFile;
    Uri downloadFile;
    Velocity.DownloadFileType downloadType;
    String uploadServerFileName = "";
    int requestId = 0;
    Velocity.DataCallback callback;

    RequestBuilder()
    {

    }

    /**
     * Add HTTP request headers as a HashMap<String, String>
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
     * @param key request header key
     * @param value request header value
     * @return request builder
     */
    public RequestBuilder addHeader(String key, String value)
    {
        this.headers.put(key, value);
        return this;
    }

    /**
     * Add HTTP form data as a Hashmap<String, String>
     * @param params form data
     * @return request builder
     */
    public RequestBuilder withBody(HashMap<String, String> params)
    {
        this.params.putAll(params);
        return this;
    }

    /**
     * Add HTTP params as a String (raw ot JSON string). "Content type" will be set to
     * "application/json" by default when calling this method
     * @param params raw parameter String
     * @return request builder
     */
    public RequestBuilder withBody(String params)
    {
        this.rawParams = params;
        this.headers.put("Content-Type", "application/json");
        return this;
    }

    /**
     * Add HTTP params as a String and set the content type. This is added as a request header specifying
     * the parameter type for the call
     * @param params raw parameter String
     * @param paramType eg: "application/json" or "text/xml"
     * @return
     */
    public RequestBuilder withBody(String params, String paramType)
    {
        this.rawParams = params;
        this.headers.put("Content-Type", paramType);
        return this;
    }

    /**
     * Add a single HTTP parameter
     * @param key param key
     * @param value param value
     * @return request Builder
     */
    public RequestBuilder addParam(String key, String value)
    {
        this.params.put(key, value);
        return this;
    }

    /**
     * Set the http request method as GET
     * @return request builder
     */
    public RequestBuilder withMethodGet()
    {
        this.requestMethod = "GET";
        return this;
    }

    /**
     * Set the http request method as POST
     * @return request builder
     */
    public RequestBuilder withMethodPost()
    {
        this.requestMethod = "POST";
        return this;
    }

    /**
     * Set the http request method as PUT
     * @return request builder
     */
    public RequestBuilder withMethodPut()
    {
        this.requestMethod = "PUT";
        return this;
    }

    /**
     * Set the http request method as DELETE
     * @return request builder
     */
    public RequestBuilder withMethodDelete()
    {
        this.requestMethod = "DELETE";
        return this;
    }

    /**
     * Set any object to this field to get it returned through the data callback
     * @param data user data
     * @return request builder
     */
    public RequestBuilder withData(Object data)
    {
        this.userData = data;
        return this;
    }

    public RequestBuilder setDownloadFile(Uri downloadFile)
    {
        this.downloadFile = downloadFile;
        return this;
    }

    public RequestBuilder setDownloadFileType(Velocity.DownloadFileType type)
    {
        this.downloadType = type;
        return this;
    }

    public RequestBuilder setUploadFile(Uri uploadFile)
    {
        this.uploadFile = uploadFile;
        return this;
    }

    public RequestBuilder setUploadDestinationFileName(String name)
    {
        this.uploadServerFileName = name;
        return this;
    }

    /**
     * Make a network request to recieve data in the callback.
     * See also {@link RequestBuilder#connect(int, Velocity.DataCallback)}
     * @param callback data callback
     */
    public void connect(Velocity.DataCallback callback)
    {
        this.callback = callback;
    }

    /**
     * Make a network request to recieve data in the callback. The request id is returned so that
     * the same callback can be used for multiple calls.
     * @param requestId unique request id
     * @param callback data callback
     */
    public void connect(int requestId, Velocity.DataCallback callback)
    {
        this.requestId = requestId;
        this.callback = callback;
    }
}
