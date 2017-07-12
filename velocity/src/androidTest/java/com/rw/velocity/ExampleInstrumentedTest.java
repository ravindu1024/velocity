package com.rw.velocity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    private class HttpBinReply
    {
        Map<String, String> args;
        Map<String, String> headers;
        Map<String, String> form;
        Map<String, String> json;
        String url;
    }

    private Velocity.Response serverResponse;

    @Test
    public void useAppContext() throws Exception
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.rw.velocity.test", appContext.getPackageName());
    }

    @Test
    public void get() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/get";

        Velocity.initialize(3);
        Velocity.get(url)
                .withHeader("header1", "value1")
                .withHeader("header2", "value2")
                .connect(new Velocity.ResponseListener()
                {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response)
                    {
                        serverResponse = response;
                        latch.countDown();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error)
                    {
                        serverResponse = error;
                        latch.countDown();
                    }
                });

        latch.await();

        HttpBinReply reply = serverResponse.deserialize(HttpBinReply.class);

        assertEquals(reply.url, url);
        assertEquals(reply.headers.get("Header1"), "value1");
        assertEquals(reply.headers.get("Header2"), "value2");
        assertEquals(reply.headers.get("User-Agent"), Velocity.Settings.USER_AGENT);
    }

    @Test
    public void getPathParams() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/get";

        Velocity.initialize(3);
        Velocity.get(url)
                .withPathParam("path1", "value1")
                .withPathParam("path2", "value2")
                .connect(new Velocity.ResponseListener()
                {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response)
                    {
                        serverResponse = response;
                        latch.countDown();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error)
                    {
                        serverResponse = error;
                        latch.countDown();
                    }
                });

        latch.await();

        HttpBinReply reply = serverResponse.deserialize(HttpBinReply.class);

        assertEquals(reply.args.get("path1"), "value1");
        assertEquals(reply.args.get("path2"), "value2");
    }

    @Test
    public void postFormData() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/post";

        Velocity.initialize(3);
        Velocity.post(url)
                .withFormData("key1", "value1")
                .withFormData("key2", "value2")
                .connect(new Velocity.ResponseListener()
                {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response)
                    {
                        serverResponse = response;
                        latch.countDown();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error)
                    {
                        serverResponse = error;
                        latch.countDown();
                    }
                });

        latch.await();

        HttpBinReply reply = serverResponse.deserialize(HttpBinReply.class);

        assertEquals(reply.url, url);
        assertEquals(reply.form.get("key1"), "value1");
        assertEquals(reply.form.get("key2"), "value2");
    }

    @Test
    public void postFormDataJson() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/post";

        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Velocity.initialize(3);
        Velocity.post(url)
                .withJsonBody(map)
                .connect(new Velocity.ResponseListener()
                {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response)
                    {
                        serverResponse = response;
                        latch.countDown();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error)
                    {
                        serverResponse = error;
                        latch.countDown();
                    }
                });

        latch.await();

        HttpBinReply reply = serverResponse.deserialize(HttpBinReply.class);

        assertEquals(reply.url, url);
        assertEquals(reply.json.get("key1"), "value1");
        assertEquals(reply.json.get("key2"), "value2");
    }

    @Test
    public void putFormData() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/put";

        Velocity.initialize(3);
        Velocity.put(url)
                .withFormData("key1", "value1")
                .withFormData("key2", "value2")
                .connect(new Velocity.ResponseListener()
                {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response)
                    {
                        serverResponse = response;
                        latch.countDown();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error)
                    {
                        serverResponse = error;
                        latch.countDown();
                    }
                });

        latch.await();

        HttpBinReply reply = serverResponse.deserialize(HttpBinReply.class);

        assertEquals(reply.url, url);
        assertEquals(reply.form.get("key1"), "value1");
        assertEquals(reply.form.get("key2"), "value2");
    }

    @Test
    public void deleteFormData() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/delete";

        Velocity.initialize(3);
        Velocity.delete(url)
                .withFormData("key1", "value1")
                .withFormData("key2", "value2")
                .connect(new Velocity.ResponseListener()
                {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response)
                    {
                        serverResponse = response;
                        latch.countDown();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error)
                    {
                        serverResponse = error;
                        latch.countDown();
                    }
                });

        latch.await();

        HttpBinReply reply = serverResponse.deserialize(HttpBinReply.class);

        assertEquals(reply.url, url);
        assertEquals(reply.form.get("key1"), "value1");
        assertEquals(reply.form.get("key2"), "value2");
    }
}
