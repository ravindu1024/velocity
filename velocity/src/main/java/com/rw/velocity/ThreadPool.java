package com.rw.velocity;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 15/12/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

class ThreadPool
{
    private static ThreadPool mInstance = null;
    private HandlerThread[] mThreads = null;
    private final ArrayList<Handler> mHandlers = new ArrayList<>();
    private int lastThread = 0;
    @SuppressWarnings("FieldCanBeLocal")
    private final Boolean syncObject = false;
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());


    private ThreadPool()
    {
    }


    @NonNull
    static ThreadPool getThreadPool()
    {
        if (mInstance == null || mInstance.mHandlers.isEmpty())
            throw new IllegalStateException("Threadpool not initialized. Please call Velocity.initialize(int)");

        return mInstance;
    }

    /**
     * Initialize the http thread pool
     *
     * @param num number of threads in thread pool
     */
    static void initialize(int num) throws InterruptedException
    {
        if (mInstance == null)
        {
            mInstance = new ThreadPool();
            mInstance.mThreads = new HandlerThread[num];

            for (int i = 0; i < num; i++)
            {
                mInstance.mThreads[i] = new HandlerThread("Velocity_workerThread_" + i);
                mInstance.mThreads[i].start();

                int count = 0;
                while (mInstance.mThreads[i].getLooper() == null && count++ < 10)
                {
                    Thread.sleep(50);
                }

                Looper l = mInstance.mThreads[i].getLooper();

                if (mInstance.mThreads[i].getLooper() != null)
                {
                    mInstance.mHandlers.add(new Handler(l));
                }
            }
            NetLog.d("initialized threadpool with size : " + num);
        }
    }

    void postRequestDelayed(final Request request, int delay)
    {
        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                request.execute();
            }
        };

        synchronized (syncObject)
        {
            if (lastThread == mHandlers.size() - 1)
                lastThread = 0;
            else
                lastThread++;


            if(delay > 0)
                mHandlers.get(lastThread).postDelayed(r, delay);
            else
                mHandlers.get(lastThread).post(r);
        }
    }

//    void postRequest(final Request request)
//    {
//        postRequestDelayed(request, 0);
//    }

    void postToUiThread(Runnable r)
    {
        mMainHandler.post(r);
    }
}
