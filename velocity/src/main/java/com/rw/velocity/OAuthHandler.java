package com.rw.velocity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by ravindu on 5/07/17.
 */

@SuppressWarnings("WeakerAccess")
public class OAuthHandler implements Velocity.ResponseListener
{
    private final int REQUEST_TOKEN = 0;

    private Builder builder = new Builder();
    private OAuthListener callback;
    private String url;

    public interface OAuthListener
    {
        void onOAuthToken(String token);
        void onOAuthError(String error);
    }

    public enum GrantType
    {
        password
    }

    /**
     * Warning : This class is not fully functional. "password" is the only resource type supported.
     * @param url identity server
     *
     */
    public static Builder login(String url)
    {
        OAuthHandler handler = new OAuthHandler();
        handler.url = url;

        return handler.builder;
    }

    private OAuthHandler()
    {
    }

    public class Builder
    {
        String user;
        String pass;
        String scope;
        String grantType;
        String clientId = "";
        String clientSecret = "";
        String clientIdSecretHash;

        private Builder()
        {

        }

        public Builder withUsername(String username)
        {
            this.user = username;
            return this;
        }

        public Builder withPassword(String password)
        {
            this.pass = password;
            return this;
        }

        public Builder withScope(String scope)
        {
            this.scope = scope;
            return this;
        }

        public Builder withGrantType(GrantType grantType)
        {
            this.grantType = grantType.toString();
            return this;
        }

        public Builder withClient(String id, String secret)
        {
            this.clientSecret = secret;
            this.clientId = id;
            return  this;
        }

        public Builder withClientSecretHash(String hash)
        {
            this.clientIdSecretHash = hash;
            return this;
        }

        public void init(OAuthListener listener)
        {
            callback = listener;
            try
            {
                getAccessToken();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                if(callback != null)
                    callback.onOAuthError(e.toString());
            }
        }
    }

    public void getAccessToken() throws UnsupportedEncodingException
    {
        String authHeader;
        if(builder.clientIdSecretHash != null)
            authHeader = builder.clientIdSecretHash;
        else
        {
            authHeader = builder.clientId + ":" + builder.clientSecret;

            byte[] bytes = authHeader.getBytes("UTF-8");
            authHeader = "Basic " + Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP);
        }


        Velocity.load(url)
                .withRequestMethodPost()
                .withHeader("Authorization", authHeader)
                .withFormData("password", builder.pass)
                .withFormData("username", builder.user)
                .withFormData("grant_type", builder.grantType)
                .withFormData("scope", builder.scope)
                .withBodyContentType(RequestBuilder.ContentType.FORM_DATA_URLENCODED)
                .connect(REQUEST_TOKEN, this);

    }

    @Override
    public void onVelocitySuccess(Velocity.Response response)
    {
        switch (response.requestId)
        {
            case REQUEST_TOKEN:
                Token token = new Deserializer().deserialize(response.body, Token.class);
                NetLog.d("got Auth Token");
                if(callback != null)
                    callback.onOAuthToken(token.access_token);
                break;
        }
    }

    @Override
    public void onVelocityFailed(Velocity.Response error)
    {
        if(callback != null)
            callback.onOAuthError(error.body);
    }

    private class Token
    {
        private String access_token;
        private String expires_in;
        private String token_type;
    }
}
