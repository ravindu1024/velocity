package com.rw.velocity.builder;

import android.support.annotation.NonNull;

import com.rw.velocity.Velocity;

import java.util.HashMap;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 13/12/16.
 */
@SuppressWarnings("WeakerAccess")
public class RequestBuilder extends IBuilder
{
    @NonNull
    @Override
    public RequestBuilder withHeaders(HashMap<String, String> headers)
    {
        return this;
    }

    @NonNull
    @Override
    public IBuilder addHeader(String key, String value)
    {
        return this;
    }

    @NonNull
    @Override
    public RequestBuilder withBody(HashMap<String, String> headers)
    {
        return this;
    }

    @NonNull
    @Override
    public RequestBuilder withBody(String params)
    {
        return this;
    }

    @NonNull
    @Override
    public IBuilder withBody(String params, String paramType)
    {
        return this;
    }

    @NonNull
    @Override
    public IBuilder addParam(String key, String value)
    {
        return this;
    }

    @NonNull
    @Override
    public IBuilder withMethodGet()
    {
        return this;
    }

    @NonNull
    @Override
    public IBuilder withMethodPost()
    {
        return this;
    }

    @NonNull
    @Override
    public IBuilder withMethodPut()
    {
        return this;
    }

    @NonNull
    @Override
    public IBuilder withMethodDelete()
    {
        return this;
    }

    @NonNull
    @Override
    public IBuilder withData(Object data)
    {
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
