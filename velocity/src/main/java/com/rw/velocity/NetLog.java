package com.rw.velocity;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 20/12/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

class NetLog
{
    private final static boolean ENABLED = true;
    private final static String TAG = "Velocity";

    static void d(String message)
    {
        if (ENABLED)
            android.util.Log.d(TAG, message);
    }
}
