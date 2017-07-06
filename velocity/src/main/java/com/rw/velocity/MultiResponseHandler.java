package com.rw.velocity;

import android.annotation.SuppressLint;
import android.app.VoiceInteractor;
import android.service.voice.VoiceInteractionSession;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 17/01/17.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

//TODO : check request id when submitting request - and throw exception
class MultiResponseHandler implements Velocity.ResponseListener
{
    private Velocity.MultiResponseListener mCallback;
    private int mTotalRequestCount = 0;
    private boolean mIsFailed = false;
    private final static Boolean mLock = true;

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Velocity.Response> mResponses = new HashMap<>();

    private static ArrayList<RequestBuilder> mRequestList = new ArrayList<>();

    static void addToQueue(RequestBuilder request)
    {
        synchronized (mLock)
        {
            mRequestList.add(request);
        }
    }

    static void execute(Velocity.MultiResponseListener callback)
    {
        synchronized (mLock)
        {
            new MultiResponseHandler()._execute(callback);

            mRequestList.clear();
        }
    }

    private void _execute(Velocity.MultiResponseListener callback)
    {
        mCallback = callback;
        mTotalRequestCount = mRequestList.size();

        for(RequestBuilder builder : mRequestList)
        {
            builder.connect(builder.requestId, this);
        }

    }

    private void checkAndsendResponse(Velocity.Response response)
    {
        mResponses.put(response.requestId, response);

        if(mResponses.size() == mTotalRequestCount)
        {
            if (!mIsFailed)
                mCallback.onVelocityMultiResponseSuccess(mResponses);
            else
                mCallback.onVelocityMultiResponseError(mResponses);
        }
    }

    @Override
    public void onVelocitySuccess(Velocity.Response response)
    {
        checkAndsendResponse(response);
    }

    @Override
    public void onVelocityFailed(Velocity.Response error)
    {
        mIsFailed = true;

        checkAndsendResponse(error);
    }
}
