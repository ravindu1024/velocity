package com.rw.velocity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ravindu on 5/09/17.
 */

@SuppressWarnings("WeakerAccess")
public abstract class Logger
{
    final String TAG;

    protected Logger(String tag)
    {
        TAG = tag;
    }

    protected void logDebug(String log)
    {
        android.util.Log.d(TAG, log);
    }

    protected void logError(String log)
    {
        android.util.Log.e(TAG, log);
    }

    protected abstract void logConnectionError(@NonNull Velocity.Response error, @Nullable Exception systemError);
}
