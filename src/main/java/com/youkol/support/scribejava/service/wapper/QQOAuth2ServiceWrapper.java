package com.youkol.support.scribejava.service.wapper;

import java.io.OutputStream;
import java.util.Map;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.youkol.support.scribejava.oauth2.user.DefaultOAuth2User;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QQOAuth2ServiceWrapper extends AbstractOAuth2ServiceWrapper {

    private static final Logger log = LoggerFactory.getLogger(QQOAuth2ServiceWrapper.class);

    protected static final String USERINFO_RESOURCE_URL = "https://graph.qq.com/oauth2.0/me";

    private static final String NAME_PARAM_KEY = "openid";

    public QQOAuth2ServiceWrapper(final DefaultApi20 api, final String apiKey, final String apiSecret, final String callback,
            final String defaultScope, final String responseType, final OutputStream debugStream, final String userAgent,
            final HttpClientConfig httpClientConfig, final HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, defaultScope, responseType, debugStream, userAgent, httpClientConfig,
                httpClient);
    }

    @Override
    public OAuth2User getOAuth2User(final OAuth2AccessToken accessToken) throws Exception {
        final OAuthRequest request = new OAuthRequest(Verb.GET, USERINFO_RESOURCE_URL);
        this.signRequest(accessToken, request);
        
        try (Response response = this.execute(request)) {

            log.debug("Response code: {}, body: {}", response.getCode(), response.getBody());

            String body = response.getBody();
            if (body.startsWith("callback(")) {
                body = body.substring(9, body.length() - 2).trim();
            }

            if (!response.isSuccessful()) {
                OAuth2AccessTokenJsonExtractor.instance().generateError(body);
                return null; // not run always.
            }

            final Map<String, Object> map = this.getLazyObjectMapper().readValue(body, mapType);
            final DefaultOAuth2User oAuth2User = new DefaultOAuth2User(NAME_PARAM_KEY, map);
            return oAuth2User;
        }
    }
}
