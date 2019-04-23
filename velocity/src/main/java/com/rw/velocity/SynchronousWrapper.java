package com.rw.velocity;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ravindu on 10/07/17.
 */

public class SynchronousWrapper implements Velocity.ResponseListener
{
    private CountDownLatch latch = new CountDownLatch(1);
    private Velocity.Response response = null;

    Velocity.Response connect(RequestBuilder builder)
    {
        builder.connect(this);
        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public void onVelocityResponse(Velocity.Response response)
    {
        this.response = response;
        latch.countDown();
    }
}
