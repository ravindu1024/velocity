package com.rw.velocity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 20/12/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

class NetLog
{
//    private final static String TAG = "Velocity";

    private static Logger logger = new Logger("Velocity")
    {
        @Override
        protected void logConnectionError(@NonNull Velocity.Response error, @Nullable Exception systemError)
        {

        }
    };

    static void setLogger(Logger logger)
    {
        NetLog.logger = logger;
    }

    static void d(String message)
    {
        if (Velocity.Settings.LOGS_ENABLED)
            logger.logDebug(message);
    }

    static void e(String message)
    {
        if (Velocity.Settings.LOGS_ENABLED)
            logger.logError(message);
    }

    static void conError(Velocity.Response response, Exception ex)
    {
        logger.logConnectionError(response, ex);
    }
}
