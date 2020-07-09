package com.youkol.support.scribejava.service.delegate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.youkol.support.scribejava.apis.QQApi20;
import com.youkol.support.scribejava.apis.wechat.WeChatAccessTokenErrorResponse;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistrationRepository;
import com.youkol.support.scribejava.oauth2.user.DefaultOAuth2User;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QQOAuth2ServiceDelegate extends AbstractOAuth2ServiceDelegate {

    private static final Logger log = LoggerFactory.getLogger(SinaWeiboOAuth2ServiceDelegate.class);

    private static final String DEFAULT_NAME = "qq_connect";

    private static final DefaultApi20 DEFAULT_API20 = QQApi20.instance();

    protected static final String USERINFO_RESOURCE_URL = "https://graph.qq.com/oauth2.0/me";

    private static final String NAME_PARAM_KEY = "openid";

    public QQOAuth2ServiceDelegate(ClientRegistrationRepository clientRegistrationRepository) {
        this(DEFAULT_NAME, clientRegistrationRepository);
    }

    public QQOAuth2ServiceDelegate(String registrationId, ClientRegistrationRepository clientRegistrationRepository) {
        super(registrationId, DEFAULT_API20, clientRegistrationRepository, Optional.empty());
    }

    public QQOAuth2ServiceDelegate(String registrationId, ClientRegistrationRepository clientRegistrationRepository,
            Optional<ObjectMapper> objectMapper) {
        super(registrationId, DEFAULT_API20, clientRegistrationRepository, objectMapper);
    }

    @Override
    public OAuth2User getOAuth2User(OAuth2AccessToken accessToken) throws OAuth2AuthenticationException {
        final OAuthRequest request = new OAuthRequest(Verb.GET, USERINFO_RESOURCE_URL);
        OAuth20Service oAuth20Service = this.getOAuth20Service();
        oAuth20Service.signRequest(accessToken, request);

        try (Response response = oAuth20Service.execute(request)) {

            log.debug("Response code: {}, body: {}", response.getCode(), response.getBody());

            String body = response.getBody();
            if (body.startsWith("callback(")) {
                body = body.substring(9, body.length() - 2).trim();
            }

            if (!response.isSuccessful()) {
                try {
                    OAuth2AccessTokenJsonExtractor.instance().generateError(response.getBody());
                } catch (WeChatAccessTokenErrorResponse ex) {
                    throw new OAuth2AuthenticationException(ex.getErrorCode(), ex.getErrorMessage(), ex.getRawResponse(), ex);
                }
            }

            final Map<String, Object> map = this.getLazyObjectMapper().readValue(body, mapType);
            final DefaultOAuth2User oAuth2User = new DefaultOAuth2User(NAME_PARAM_KEY, map);
            return oAuth2User;
        } catch (JsonProcessingException ex) {
            throw new OAuth2AuthenticationException("Parse response body to json error", ex);
        } catch (InterruptedException | ExecutionException | IOException ex) {
            throw new OAuth2AuthenticationException("Get userInfo error", ex);
        }
    }

}
