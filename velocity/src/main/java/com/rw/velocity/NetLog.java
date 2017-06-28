package com.rw.velocity;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 20/12/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

class NetLog
{
    private final static String TAG = "Velocity";

    static void d(String message)
    {
        if (Velocity.Settings.LOGS_ENABLED)
            android.util.Log.d(TAG, message);
    }
}
