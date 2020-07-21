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
package com.youkol.support.scribejava.service.delegate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.youkol.support.scribejava.apis.WeChatMpApi20;
import com.youkol.support.scribejava.apis.wechat.WeChatAccessTokenErrorResponse;
import com.youkol.support.scribejava.apis.wechat.WeChatAccessTokenJsonExtractor;
import com.youkol.support.scribejava.apis.wechat.WeChatConstants;
import com.youkol.support.scribejava.apis.wechat.WeChatOAuth2AccessToken;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistrationRepository;
import com.youkol.support.scribejava.oauth2.user.DefaultOAuth2User;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;
import com.youkol.support.scribejava.service.OAuth2AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeChatMpOAuth2ServiceDelegate extends AbstractOAuth2ServiceDelegate {

    private static final Logger log = LoggerFactory.getLogger(WeChatMpOAuth2ServiceDelegate.class);

    private static final String DEFAULT_NAME = "wechat_offical";

    private static final DefaultApi20 DEFAULT_API20 = WeChatMpApi20.instance();

    protected static final String USERINFO_RESOURCE_URL = "https://api.weixin.qq.com/sns/userinfo";

    private static final String NAME_PARAM_KEY = WeChatConstants.OPEN_ID;

    public WeChatMpOAuth2ServiceDelegate(ClientRegistrationRepository clientRegistrationRepository) {
        this(DEFAULT_NAME, clientRegistrationRepository);
    }

    public WeChatMpOAuth2ServiceDelegate(String registrationId,
            ClientRegistrationRepository clientRegistrationRepository) {
        super(registrationId, clientRegistrationRepository, DEFAULT_API20, Optional.empty());
    }

    public WeChatMpOAuth2ServiceDelegate(String registrationId,
            ClientRegistrationRepository clientRegistrationRepository, Optional<ObjectMapper> objectMapper) {
        super(registrationId, clientRegistrationRepository, DEFAULT_API20, objectMapper);
    }

    @Override
    public OAuth2User getOAuth2User(OAuth2AccessToken accessToken) throws OAuth2AuthenticationException {
        OAuthRequest request = new OAuthRequest(Verb.GET, USERINFO_RESOURCE_URL);
        WeChatOAuth2AccessToken wechatAccessToken = (WeChatOAuth2AccessToken) accessToken;
        String openId = wechatAccessToken.getOpenId();
        request.addParameter(WeChatConstants.OPEN_ID, openId);

        OAuth20Service oAuth20Service = this.getOAuth20Service();

        oAuth20Service.signRequest(accessToken, request);
        try (Response response = oAuth20Service.execute(request)) {

            log.debug("Response code: {}, body: {}", response.getCode(), response.getBody());

            if (response.getCode() != 200) {
                try {
                    WeChatAccessTokenJsonExtractor jsonExtractor = (WeChatAccessTokenJsonExtractor) this.getApi()
                        .getAccessTokenExtractor();
                    jsonExtractor.generateError(response.getBody());
                } catch (WeChatAccessTokenErrorResponse ex) {
                    throw new OAuth2AuthenticationException(ex.getErrorCode(), ex.getErrorMessage(), ex.getRawResponse(), ex);
                }
            }

            Map<String, Object> map = this.getLazyObjectMapper().readValue(response.getBody(), mapType);
            if (map.containsKey("errcode") || map.containsKey("errmsg")) {
                throw new OAuth2AuthenticationException(map.get("errcode").toString(), map.get("errmsg").toString(), response.getBody());
            }
            
            DefaultOAuth2User oAuth2User = new DefaultOAuth2User(NAME_PARAM_KEY, map);

            return oAuth2User;
        } catch (JsonProcessingException ex) {
            throw new OAuth2AuthenticationException("Parse response body to json error", ex);
        } catch (InterruptedException | ExecutionException | IOException ex) {
            throw new OAuth2AuthenticationException("Get userInfo error", ex);
        }
    }
}
