package com.rw.velocity;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by ravindu on 5/07/17.
 */

@SuppressWarnings("WeakerAccess")
class OAuthHandler implements Velocity.ResponseListener
{
    private final int REQUEST_TOKEN = 0;
    private OAuthBuilder builder;


    OAuthHandler()
    {
    }

    void init(OAuthBuilder builder)
    {
        this.builder = builder;

        getAccessToken();
    }

    private void getAccessToken()
    {
        String authHeader;

        try
        {
            if (builder.clientIdSecretHash != null && builder.clientIdSecretHash.length() > 1)
                authHeader = builder.clientIdSecretHash;
            else
            {
                authHeader = builder.clientId + ":" + builder.clientSecret;

                byte[] bytes = authHeader.getBytes("UTF-8");
                authHeader = "Basic " + Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP);
            }


            Velocity.post(builder.url)
                    .withHeader("Authorization", authHeader)
                    .withFormData("password", builder.pass)
                    .withFormData("username", builder.user)
                    .withFormData("grant_type", builder.grantType)
                    .withFormData("scope", builder.scope)
                    .withBodyContentType(Velocity.ContentType.FORM_DATA_URLENCODED)
                    .connect(REQUEST_TOKEN, this);
        }
        catch (UnsupportedEncodingException ue)
        {
            handleException(ue);
        }

    }

    private void handleException(Exception e)
    {
        if (builder.callback != null)
        {
            final Velocity.Response response = new Velocity.Response(0, e.toString(), 0, null, null, null, new RequestBuilder(builder.url, Velocity.RequestType.Text));
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    builder.callback.onOAuthError(response);
                }
            };

            ThreadPool.getThreadPool().postToUiThread(r);
        }
    }

    @Override
    public void onVelocitySuccess(Velocity.Response response)
    {
        switch (response.requestId)
        {
            case REQUEST_TOKEN:
                TokenResponse token = new Deserializer().deserialize(response.body, TokenResponse.class);
                NetLog.d("got Auth Token");
                if (builder.callback != null)
                    builder.callback.onOAuthToken(token.access_token);
                break;
        }
    }

    @Override
    public void onVelocityFailed(Velocity.Response error)
    {
        if (builder.callback != null)
            builder.callback.onOAuthError(error);
    }

    private class TokenResponse
    {
        private String access_token = "";
        private long expires_in = 0;
        private String token_type = "";
    }
}
