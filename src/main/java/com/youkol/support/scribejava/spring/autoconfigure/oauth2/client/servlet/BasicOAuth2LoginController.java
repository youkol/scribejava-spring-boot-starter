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
package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.youkol.support.scribejava.apis.wechat.WeChatAccessTokenErrorResponse;
import com.youkol.support.scribejava.oauth2.client.OAuth2ClientServiceDelegate;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;
import com.youkol.support.scribejava.oauth2.user.OAuth2UserRequest;
import com.youkol.support.scribejava.service.OAuth2AuthenticationException;
import com.youkol.support.scribejava.service.delegate.OAuth2ServiceDelegate;
import com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.event.AuthenticationFailureEvent;
import com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.event.AuthenticationSuccessEvent;
import com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.handler.AuthenticationFailureHandler;
import com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.handler.AuthenticationSuccessHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Validated
@Controller
public class BasicOAuth2LoginController implements OAuth2LoginController, ApplicationEventPublisherAware {

    public static final Logger log = LoggerFactory.getLogger(BasicOAuth2LoginController.class);

    private static final String REDIRECT_URI_COOKIE_NAME = "redirect_uri";

    private OAuth2LoginProperties oAuth2LoginProperties;

    private OAuth2ClientServiceDelegate oAuth2ClientServiceDelegate;

    protected ApplicationEventPublisher eventPublisher;

    @Autowired
    private ObjectProvider<AuthenticationSuccessHandler> successHandler;

    @Autowired
    private ObjectProvider<AuthenticationFailureHandler> failureHandler;

    public BasicOAuth2LoginController(OAuth2LoginProperties oAuth2LoginProperties,
            OAuth2ClientServiceDelegate oAuth2ClientServiceDelegate) {
        this.oAuth2LoginProperties = oAuth2LoginProperties;
        this.oAuth2ClientServiceDelegate = oAuth2ClientServiceDelegate;
    }

    @RequestMapping(value = "${youkol.oauth2.web.authorize.path:/oauth2/authorize/{registrationId}}")
    public void authenticate(@PathVariable String registrationId,
            @RequestParam(name = "redirect_uri", required = false) String successRedirectUri, // 成功授权之后，需要返回的地址
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String encodedSuccessRedirectUri = null;
        if (StringUtils.hasText(successRedirectUri)) { // Base64 encode, avoid some url information missing. for example: http://127.0.0.1/#/oauth2/redirect
            encodedSuccessRedirectUri = Base64.getEncoder().encodeToString(successRedirectUri.getBytes());
        }

        // add to cookie
        // Avoid Error: java.lang.IllegalArgumentException: An invalid character [13] was present in the Cookie value
        Cookie cookie = new Cookie(REDIRECT_URI_COOKIE_NAME, encodedSuccessRedirectUri);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        OAuth2ServiceDelegate oAuth20Service = oAuth2ClientServiceDelegate.getDelegate(registrationId);
        if (oAuth20Service == null) {
            throw new OAuth2AuthenticationException("oAuth20Service cannot be null");
        }

        String redirectUriTemplate = oAuth20Service.getAuthorizationUrl(UUID.randomUUID().toString());
        redirectUriTemplate = URLDecoder.decode(redirectUriTemplate, StandardCharsets.UTF_8.displayName());

        String redirectUri = this.expandRedirectUri(request, registrationId, redirectUriTemplate, encodedSuccessRedirectUri);

        log.debug("OAuth2 redirectUri: " + redirectUri);

        response.sendRedirect(redirectUri);
    }

    @RequestMapping(value = "${youkol.oauth2.web.callback.path:/oauth2/callback/{registrationId}}")
    public void callback(@PathVariable String registrationId,
            @RequestParam(name = "redirect_uri", required = false) String successRedirectUri,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "state", required = false) String state, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        OAuth2AccessToken accessToken = null;
        OAuth2User oAuth2User;
        OAuth2UserRequest oAuth2UserRequest = null;
        String decodedSuccessRedirectUri = null;

        try {
            decodedSuccessRedirectUri = getDecodedLocalRedirectUri(request, "redirect_uri", successRedirectUri);

            // delete cookie
            Cookie cookie = new Cookie(REDIRECT_URI_COOKIE_NAME, "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        
            log.debug("OAuth2 code: {}, state: {}, redirect_uri: {}", code, state, decodedSuccessRedirectUri);

            if (!StringUtils.hasText(code)) {
                throw new OAuth2AuthenticationException("INVALID_REQUEST");
            }

            OAuth2ServiceDelegate oAuth20Service = oAuth2ClientServiceDelegate.getDelegate(registrationId);
            if (oAuth20Service == null) {
                throw new OAuth2AuthenticationException("oAuth20Service cannot be null");
            }

            accessToken = oAuth20Service.getAccessToken(code);

            log.debug("OAuth2 accessToken: " + accessToken);

            oAuth2User = oAuth20Service.getOAuth2User(accessToken);

            oAuth2UserRequest = new OAuth2UserRequest(oAuth20Service.getClientRegistration(), accessToken);

        } catch (OAuth2AuthenticationException ex) {
            // on failure
            this.onAuthenticationFailure(request, response, oAuth2UserRequest, ex);
            throw ex;
        } catch (OAuth2AccessTokenErrorResponse ex) {
            // on failure
            OAuth2AuthenticationException exception = new OAuth2AuthenticationException(ex.getError().name(),
                    ex.getErrorDescription(), ex.getRawResponse(), ex);
            this.onAuthenticationFailure(request, response, oAuth2UserRequest , exception);
            throw exception;
        } catch (WeChatAccessTokenErrorResponse ex) {
            // on failure
            OAuth2AuthenticationException exception = new OAuth2AuthenticationException(ex.getErrorCode(),
                    ex.getErrorMessage(), ex.getRawResponse(), ex);
            this.onAuthenticationFailure(request, response, oAuth2UserRequest, exception);
            throw exception;
        } catch (Exception ex) {
            OAuth2AuthenticationException exception = new OAuth2AuthenticationException("OAuth2 callback error", ex);
            // on failure
            this.onAuthenticationFailure(request, response, oAuth2UserRequest, exception);
            throw exception;
        }

        // on success
        this.onAuthenticationSuccess(request, response, oAuth2UserRequest, oAuth2User);

        // redirect uri.
        if (StringUtils.hasText(decodedSuccessRedirectUri)) {
            response.sendRedirect(decodedSuccessRedirectUri);
        }
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws IOException, ServletException {
        log.debug("OAuth2User: " + oAuth2User);

        // fire event
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new AuthenticationSuccessEvent(oAuth2UserRequest, oAuth2User));
        }

        Iterator<AuthenticationSuccessHandler> iterator = successHandler.orderedStream().iterator();
        while (iterator.hasNext()) {
            iterator.next().onAuthenticationSuccess(request, response, oAuth2UserRequest, oAuth2User);
        }
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            OAuth2UserRequest oAuth2UserRequest, OAuth2AuthenticationException exception)
            throws IOException, ServletException {
        // fire event
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new AuthenticationFailureEvent(oAuth2UserRequest, exception));
        }

        Iterator<AuthenticationFailureHandler> iterator = failureHandler.orderedStream().iterator();
        while (iterator.hasNext()) {
            iterator.next().onAuthenticationFailure(request, response, oAuth2UserRequest, exception);
        }
    }

    private String expandRedirectUri(HttpServletRequest request, String registrationId, String redirectUriTemplate,
            String successRedirectUri) {
        String contextPath = request.getContextPath();
        String fullRequestUrl = this.buildFullRequestUrl(request);
        String baseUrl = UriComponentsBuilder.fromHttpUrl(fullRequestUrl)
            .replaceQuery(null)
            .replacePath(contextPath)
            .build()
            .toUriString();

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("baseUrl", baseUrl);
        uriVariables.put("registrationId", registrationId);
        if (StringUtils.hasText(successRedirectUri)) {
            uriVariables.put("redirect_uri", successRedirectUri);
        }

        return UriComponentsBuilder.fromUriString(redirectUriTemplate)
            .encode()
            .buildAndExpand(uriVariables)
            .toUriString();
    }

    private String getDecodedLocalRedirectUri(HttpServletRequest request, String cookieName, String successRedirectUri) {
        Base64.Decoder decoder = Base64.getDecoder();
        // try find from cookie and decode value
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    String value = cookie.getValue();
                    return new String(decoder.decode(value));
                }
            }
        }

        // Didn't find it from cookie, try find it from successRedirectUri parameter and decode it.
        String decodedSuccessRedirectUri = null;
        if (StringUtils.hasText(successRedirectUri)) {
            decodedSuccessRedirectUri = new String(decoder.decode(successRedirectUri));
        }

        return decodedSuccessRedirectUri;
    }

    private String buildFullRequestUrl(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            return requestUrl + "?" + queryString;
        }

        return requestUrl;
    }

    @Override
    public String getAuthorizePath() {
        return oAuth2LoginProperties.getAuthorize().getPath();
    }

    @Override
    public String getCallbackPath() {
        return oAuth2LoginProperties.getCallback().getPath();
    }

    public ObjectProvider<AuthenticationSuccessHandler> getSuccessHandler() {
        return successHandler;
    }

    public void setSuccessHandler(ObjectProvider<AuthenticationSuccessHandler> successHandler) {
        this.successHandler = successHandler;
    }

    public ObjectProvider<AuthenticationFailureHandler> getFailureHandler() {
        return failureHandler;
    }

    public void setFailureHandler(ObjectProvider<AuthenticationFailureHandler> failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;

    }

    
}
