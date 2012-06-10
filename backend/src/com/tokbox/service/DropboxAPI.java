package com.tokbox.service;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 6/9/12
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DropboxAPI extends DefaultApi10a {
    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.dropbox.com/1/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.dropbox.com/1/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(Token token) {
        return "https://www.dropbox.com/1/oauth/authorize?oauth_token="+token.getToken();
    }
}
