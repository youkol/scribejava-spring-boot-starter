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
package com.youkol.support.scribejava.service.wrapper;

import java.io.OutputStream;
import java.util.Map;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.youkol.support.scribejava.apis.wechat.WeChatAccessTokenJsonExtractor;
import com.youkol.support.scribejava.apis.wechat.WeChatConstants;
import com.youkol.support.scribejava.apis.wechat.WeChatOAuth2AccessToken;
import com.youkol.support.scribejava.oauth2.user.DefaultOAuth2User;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeChatOAuth2ServiceWrapper extends AbstractOAuth2ServiceWrapper {

    private static final Logger log = LoggerFactory.getLogger(WeChatOAuth2ServiceWrapper.class);

    protected static final String USERINFO_RESOURCE_URL = "https://api.weixin.qq.com/sns/userinfo";

    private static final String NAME_PARAM_KEY = WeChatConstants.OPEN_ID;

    public WeChatOAuth2ServiceWrapper(DefaultApi20 api, String apiKey, String apiSecret, String callback,
            String defaultScope, String responseType, OutputStream debugStream, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, defaultScope, responseType, debugStream, userAgent, httpClientConfig,
                httpClient);
    }

    @Override
    public OAuth2User getOAuth2User(OAuth2AccessToken accessToken) throws Exception {
        OAuthRequest request = new OAuthRequest(Verb.GET, USERINFO_RESOURCE_URL);
        String openId = ( (WeChatOAuth2AccessToken) accessToken ).getOpenId();
        request.addParameter(WeChatConstants.OPEN_ID, openId);
        
        this.signRequest(accessToken, request);
        try (Response response = this.execute(request)) {

            log.debug("Response code: {}, body: {}", response.getCode(), response.getBody());

            if (!response.isSuccessful()) {
                WeChatAccessTokenJsonExtractor jsonExtractor = (WeChatAccessTokenJsonExtractor) this.getApi().getAccessTokenExtractor();
                jsonExtractor.generateError(response.getBody());
                return null; // not run always.
            }
            
            Map<String, Object> map = this.getLazyObjectMapper().readValue(response.getBody(), mapType);
            DefaultOAuth2User oAuth2User = new DefaultOAuth2User(NAME_PARAM_KEY, map);
            return oAuth2User;
        }
    }
    
}
