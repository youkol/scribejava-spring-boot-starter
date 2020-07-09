package com.youkol.support.scribejava.apis.wrapper;

import java.io.OutputStream;
import java.util.Map;

import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.youkol.support.scribejava.apis.wechat.WeChatAccessTokenJsonExtractor;
import com.youkol.support.scribejava.apis.wechat.WeChatConstants;
import com.youkol.support.scribejava.apis.wechat.WeChatRequestBodyAuthenticationScheme;
import com.youkol.support.scribejava.service.wapper.AbstractOAuth2ServiceWrapper;
import com.youkol.support.scribejava.service.wapper.WeChatOAuth2ServiceWrapper;

/**
 * WeChat OAuth 2.0 api.
 * 
 * @author jackiea
 * @see <a href=
 *      "https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html">微信开放文档
 *      - 网站应用</a>
 */
public class WeChatApi20 extends WrapperDefaultApi20 {

    protected WeChatApi20() {
    }

    private static class InstanceHolder {
        private static final WeChatApi20 INSTANCE = new WeChatApi20();
    }

    public static WeChatApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.weixin.qq.com/sns/oauth2/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://open.weixin.qq.com/connect/qrconnect";
    }

    @Override
    public String getRefreshTokenEndpoint() {
        return "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    }

    @Override
    public String getAuthorizationUrl(String responseType, String apiKey, String callback, String scope, String state,
            Map<String, String> additionalParams) {
        ParameterList parameters = new ParameterList(additionalParams);
        parameters.add(WeChatConstants.RESPONSE_TYPE, responseType);
        parameters.add(WeChatConstants.CLIENT_ID, apiKey);

        if (callback != null) {
            parameters.add(WeChatConstants.REDIRECT_URI, callback);
        }

        if (scope != null) {
            parameters.add(WeChatConstants.SCOPE, scope);
        }

        if (state != null) {
            parameters.add(WeChatConstants.STATE, state);
        }

        parameters = parameters.sort();

        String authorizationUrl = parameters.appendTo(getAuthorizationBaseUrl());
        return authorizationUrl + "#wechat_redirect";
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return WeChatRequestBodyAuthenticationScheme.instance();
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return WeChatAccessTokenJsonExtractor.instance();
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public AbstractOAuth2ServiceWrapper createService(String apiKey, String apiSecret, String callback,
            String defaultScope, String responseType, OutputStream debugStream, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new WeChatOAuth2ServiceWrapper(this, apiKey, apiSecret, callback, defaultScope, responseType,
                debugStream, userAgent, httpClientConfig, httpClient);
    }

}
