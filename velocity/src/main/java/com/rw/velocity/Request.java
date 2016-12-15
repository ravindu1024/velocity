package com.rw.velocity;

/**
 * velocity-android
 * <p>
 * Created by ravindu on 15/12/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

public class Request extends IRequest
{


    @Override
    void execute()
    {
        ThreadPool.getThreadPool().postToUiThread(new Runnable()
        {
            @Override
            public void run()
            {
            }
        });
    }
}
