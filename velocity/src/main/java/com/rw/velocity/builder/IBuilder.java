package com.rw.velocity.builder;

import android.support.annotation.NonNull;

import com.rw.velocity.Velocity;

import java.util.HashMap;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 13/12/16.
 */

public abstract class IBuilder
{
    /**
     * Add HTTP request headers as a HashMap<String, String>
     * @param headers request headers
     * @return request builder
     */
    @NonNull
    public abstract IBuilder withHeaders(HashMap<String, String> headers);

    /**
     * Add a single request header
     * @param key request header key
     * @param value request header value
     * @return request builder
     */
    @NonNull
    public abstract IBuilder addHeader(String key, String value);

    /**
     * Add HTTP form data as a Hashmap<String, String>
     * @param headers form data
     * @return request builder
     */
    @NonNull
    public abstract IBuilder withBody(HashMap<String, String> headers);

    /**
     * Add HTTP params as a String (raw ot JSON string). "Content type" will be set to
     * "application/json" by default when calling this method
     * @param params raw parameter String
     * @return request builder
     */
    @NonNull
    public abstract IBuilder withBody(String params);

    /**
     * Add HTTP params as a String and set the content type. This is added as a request header specifying
     * the parameter type for the call
     * @param params raw parameter String
     * @param paramType eg: "application/json" or "text/xml"
     * @return
     */
    @NonNull
    public abstract IBuilder withBody(String params, String paramType);

    /**
     * Add a single HTTP parameter
     * @param key param key
     * @param value param value
     * @return request Builder
     */
    @NonNull
    public abstract IBuilder addParam(String key, String value);

    /**
     * Set the http request method as GET
     * @return request builder
     */
    @NonNull
    public abstract IBuilder withMethodGet();

    /**
     * Set the http request method as POST
     * @return request builder
     */
    @NonNull
    public abstract IBuilder withMethodPost();

    /**
     * Set the http request method as PUT
     * @return request builder
     */
    @NonNull
    public abstract IBuilder withMethodPut();

    /**
     * Set the http request method as DELETE
     * @return request builder
     */
    @NonNull
    public abstract IBuilder withMethodDelete();

    /**
     * Set any object to this field to get it returned through the data callback
     * @param data user data
     * @return request builder
     */
    @NonNull
    public abstract IBuilder withData(Object data);

    /**
     * Make a network request to recieve data in the callback.
     * See also {@link IBuilder#connect(int, Velocity.DataCallback)}
     * @param callback data callback
     */
    public abstract void connect(Velocity.DataCallback callback);

    /**
     * Make a network request to recieve data in the callback. The request id is returned so that
     * the same callback can be used for multiple calls.
     * @param requestId unique request id
     * @param callback data callback
     */
    public abstract void connect(int requestId, Velocity.DataCallback callback);
}
