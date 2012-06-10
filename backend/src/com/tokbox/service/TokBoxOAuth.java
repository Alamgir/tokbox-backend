package com.tokbox.service;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DropBoxApi;
import org.scribe.oauth.OAuthService;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 6/9/12
 * Time: 10:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class TokBoxOAuth {
    public static OAuthService service;

    public static void init() {
        ServiceBuilder builder = new ServiceBuilder();
        builder.provider(DropboxAPI.class);
        builder.apiKey("6hwxqmcx8gomive");
        builder.apiSecret("111v9xkl9l3yo5p");
        builder.callback("http://tokboxapp.com/auth");
        service = builder.build();
    }

    public static OAuthService getService() {
        return service;
    }
}
