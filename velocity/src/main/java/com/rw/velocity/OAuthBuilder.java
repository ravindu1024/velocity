package com.rw.velocity;

import java.io.UnsupportedEncodingException;

/**
 * Created by ravindu on 6/07/17.
 */
public class OAuthBuilder
{
    String user = "";
    String pass = "";
    String scope = "";
    String grantType = "";
    String clientId = "";
    String headerPrefix = "";
    String clientSecret = "";
    String clientIdSecretHash = "";
    String url = "";
    Velocity.OAuthListener callback;
    boolean clientInfoInHeader = false;

    public enum GrantType
    {
        password
    }

    private OAuthBuilder()
    {
        this.url = null;
    }


    OAuthBuilder(String url)
    {
        this.url = url;
    }

    /**
     * set resource owner username
     * @param username username
     * @return OAuthBuilder
     */
    public OAuthBuilder withUsername(String username)
    {
        this.user = username;
        return this;
    }

    /**
     * set resource owner password
     * @param password password
     * @return OAuthBuilder
     */
    public OAuthBuilder withPassword(String password)
    {
        this.pass = password;
        return this;
    }

    /**
     * set OAuth scope
     * @param scope required scope
     * @return OAuthBuilder
     */
    public OAuthBuilder withScope(String scope)
    {
        this.scope = scope;
        return this;
    }

    /**
     * set OAuth grant type. currently only "password" is supported
     * @param grantType grant type
     * @return OAuthBuilder
     */
    public OAuthBuilder withGrantType(GrantType grantType)
    {
        this.grantType = grantType.toString();
        return this;
    }

    /**
     * set the client details
     * @param id client id string (plaintext)
     * @param secret client secret string (plaintext)
     * @return OAuthBuilder
     */
    public OAuthBuilder withClient(String id, String secret)
    {
        this.clientSecret = secret;
        this.clientId = id;
        this.clientInfoInHeader = false;
        return this;
    }

    /**
     * set the client id and secret as an encoded Authorization header.
     * Format: 'Authorization':'headerPrefix Base64(id:secret)
     * @param id
     * @param secret
     * @param headerPrefix
     * @return
     */
    public OAuthBuilder withClientAsAuthHeader(String id, String secret, String headerPrefix)
    {
        this.clientSecret = secret;
        this.clientId = id;
        this.headerPrefix = headerPrefix;
        this.clientInfoInHeader = true;
        return this;
    }

    /**
     * set the hashed client secret : Base64("client_id:client_secret")
     * @param hash hashed client secret
     * @return OAuthBuilder
     */
    public OAuthBuilder withClientAsAuthHeader(String hash, String headerPrefix)
    {
        this.clientIdSecretHash = hash;
        this.headerPrefix = headerPrefix;
        this.clientInfoInHeader = true;
        return this;
    }


    /**
     * Initialize the login process
     * @param callback login token callback
     */
    public void initiate(Velocity.OAuthListener callback)
    {
        this.callback = callback;

        new OAuthHandler().init(this);
    }


}
