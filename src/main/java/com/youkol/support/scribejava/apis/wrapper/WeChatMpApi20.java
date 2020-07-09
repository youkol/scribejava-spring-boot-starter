package com.youkol.support.scribejava.apis.wrapper;

import java.io.OutputStream;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.youkol.support.scribejava.service.wapper.AbstractOAuth2ServiceWrapper;
import com.youkol.support.scribejava.service.wapper.WeChatMpOAuth2ServiceWrapper;

/**
 * WeChat OAuth 2.0 api. For WeChat Official Account.
 * 
 * @author jackiea
 * @see <a href=
 *      "https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html">微信开放文档
 *      - 网页授权</a>
 */
public class WeChatMpApi20 extends WeChatApi20 {

    protected WeChatMpApi20() {
    }

    private static class InstanceHolder {
        private static final WeChatMpApi20 INSTANCE = new WeChatMpApi20();
    }

    public static WeChatMpApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://open.weixin.qq.com/connect/oauth2/authorize";
    }

    @Override
    public AbstractOAuth2ServiceWrapper createService(String apiKey, String apiSecret, String callback,
            String defaultScope, String responseType, OutputStream debugStream, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new WeChatMpOAuth2ServiceWrapper(this, apiKey, apiSecret, callback, defaultScope, responseType,
                debugStream, userAgent, httpClientConfig, httpClient);
    }
}
