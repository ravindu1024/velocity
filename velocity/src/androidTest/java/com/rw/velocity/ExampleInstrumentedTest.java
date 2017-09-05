package com.rw.velocity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
        String data;
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
    public void postFormDataRaw() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/post";

        String postData = "raw_data_string";

        Velocity.initialize(3);
        Velocity.post(url)
                .withBody(postData)
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
        assertEquals(reply.form.containsKey(postData), true);
    }

    @Test
    public void postFormDataRawJson() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/post";

        String postData = "{\"key1\":\"value1\"}";

        Velocity.initialize(3);
        Velocity.post(url)
                .withBody(postData, Velocity.ContentType.JSON)
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
    }

    @Test
    public void postFormDataRawTextPlain() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/post";

        String postData = "plain_text";

        Velocity.initialize(3);
        Velocity.post(url)
                .withBody(postData, Velocity.ContentType.TEXT_PLAIN)
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
        assertEquals(reply.data, postData);
    }

    @Test
    public void postFormDataRawTextPlainAndForm() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/post";

        String postData = "plain_text";

        Velocity.initialize(3);
        Velocity.post(url)
                .withBody(postData, Velocity.ContentType.TEXT_PLAIN)
                .withFormData("key1", "value1")
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

        //raw data will be ignored if form data was given
        assertEquals(reply.url, url);
        assertEquals(reply.data, "");
        assertEquals(reply.form.get("key1"), "value1");
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

    @Test
    public void postMultiPart() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/post";

        Velocity.initialize(3);
        Velocity.post(url)
                .withFormData("key1", "value1")
                .withFormData("key2", "value2")
                .withBodyContentType(Velocity.ContentType.FORM_DATA_MULTIPART)
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
    public void absoluteRedirect() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/absolute-redirect/2";

        Velocity.initialize(3);
        Velocity.getSettings().setAutoRedirects(false);
        Velocity.getSettings().setLoggingEnabled(true);
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

        assertEquals(reply.headers.get("Header1"), "value1");
        assertEquals(reply.headers.get("Header2"), "value2");
    }

    @Test
    public void relativeRedirect() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/relative-redirect/2";

        Velocity.initialize(3);
        Velocity.getSettings().setAutoRedirects(false);
        Velocity.getSettings().setLoggingEnabled(true);
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

        assertEquals(reply.headers.get("Header1"), "value1");
        assertEquals(reply.headers.get("Header2"), "value2");
    }

    @Test
    public void imagePng() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="http://httpbin.org/image/png";

        Velocity.initialize(3);
        Velocity.get(url)
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

        assertNotNull(serverResponse.image);
    }

    @Test
    public void upload() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        String url ="https://www.posttestserver.com/post.php";

        String test = "hello world";
        InputStream stream = null;
        try
        {
            stream = new ByteArrayInputStream(test.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        Velocity.initialize(3);
        Velocity.getSettings().setLoggingEnabled(true);
        Velocity.upload(url)
                .withPathParam("dir", "velocity")
                .withPathParam("method", "post")
                .withPathParam("enctype", "multipart/form-data")
                .setUploadSource(stream)
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

        final CountDownLatch latch2 = new CountDownLatch(1);
        NetLog.d("response: " + serverResponse.body);

        int start = serverResponse.body.indexOf("http");
        int finish = serverResponse.body.indexOf("No Post");
        String sub = serverResponse.body.substring(start, finish);

        NetLog.d("file info: " + sub);
        Velocity.get(sub).connect(new Velocity.ResponseListener()
        {
            @Override
            public void onVelocitySuccess(Velocity.Response response)
            {
                serverResponse = response;
                latch2.countDown();
            }

            @Override
            public void onVelocityFailed(Velocity.Response error)
            {
                NetLog.d("error");
                latch2.countDown();
            }
        });

        //Thread.sleep(1000);

        latch2.await();

        String uploaded = serverResponse.body.substring(serverResponse.body.indexOf("Uploaded File: http"));
        uploaded = uploaded.substring(uploaded.indexOf("http"));

        NetLog.d("Uploaded file: " + uploaded);

        final CountDownLatch latch3 = new CountDownLatch(1);

        Velocity.get(uploaded).connect(new Velocity.ResponseListener()
        {
            @Override
            public void onVelocitySuccess(Velocity.Response response)
            {
                serverResponse = response;
                latch3.countDown();
            }

            @Override
            public void onVelocityFailed(Velocity.Response error)
            {
                NetLog.d("error");
                latch3.countDown();
            }
        });
        latch3.await();

        NetLog.d("upload file content: " + serverResponse.body);
        assertEquals(test, serverResponse.body);
    }


}
