package com.rw.velocity;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 13/12/16.
 */
@SuppressWarnings("WeakerAccess")
public class DefaultRequestBuilder extends RequestBuilder
{
    @NonNull
    @Override
    public DefaultRequestBuilder withHeaders(HashMap<String, String> headers)
    {
        super.withHeaders(headers);
        return this;
    }

    @NonNull
    @Override
    public DefaultRequestBuilder addHeader(String key, String value)
    {
        super.addHeader(key, value);
        return this;
    }

    @NonNull
    @Override
    public DefaultRequestBuilder withBody(HashMap<String, String> params)
    {
        super.withBody(params);
        return this;
    }

    @NonNull
    @Override
    public DefaultRequestBuilder withBody(String params)
    {
        super.withBody(params);
        return this;
    }

    @NonNull
    @Override
    public DefaultRequestBuilder withBody(String params, String paramType)
    {
        super.withBody(params, paramType);
        return this;
    }

    @NonNull
    @Override
    public DefaultRequestBuilder addParam(String key, String value)
    {
        super.addParam(key, value);
        return this;
    }

    @NonNull
    @Override
    public DefaultRequestBuilder withMethodGet()
    {
        super.withMethodGet();
        return this;
    }

    @NonNull
    @Override
    public RequestBuilder withMethodPost()
    {
        super.withMethodPost();
        return this;
    }

    @NonNull
    @Override
    public RequestBuilder withMethodPut()
    {
        super.withMethodPut();
        return this;
    }

    @NonNull
    @Override
    public RequestBuilder withMethodDelete()
    {
        super.withMethodDelete();
        return this;
    }

    @NonNull
    @Override
    public RequestBuilder withData(Object data)
    {
        super.withData(data);
        return this;
    }

    @Override
    public void connect(Velocity.DataCallback callback)
    {

    }

    @Override
    public void connect(int requestId, Velocity.DataCallback callback)
    {

    }


}
