/**
 * Copyright (C) 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youkol.support.scribejava.apis.wrapper;

import java.io.OutputStream;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;
import com.youkol.support.scribejava.service.wapper.AbstractOAuth2ServiceWrapper;
import com.youkol.support.scribejava.service.wapper.SinaWeiboOAuth2ServiceWrapper;

/**
 * SinaWeibo OAuth 2.0 api.
 */
public class SinaWeiboApi20 extends WrapperDefaultApi20 {

    protected SinaWeiboApi20() {
    }

    private static class InstanceHolder {
        private static final SinaWeiboApi20 INSTANCE = new SinaWeiboApi20();
    }

    public static SinaWeiboApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.weibo.com/oauth2/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.weibo.com/oauth2/authorize";
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }

    @Override
    public AbstractOAuth2ServiceWrapper createService(String apiKey, String apiSecret, String callback,
            String defaultScope, String responseType, OutputStream debugStream, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new SinaWeiboOAuth2ServiceWrapper(this, apiKey, apiSecret, callback, defaultScope, responseType,
                debugStream, userAgent, httpClientConfig, httpClient);
    }
}
