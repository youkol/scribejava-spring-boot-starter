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

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.AuthorizationUrlBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.pkce.PKCE;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistration;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;
import com.youkol.support.scribejava.service.OAuth2AuthenticationException;

/**
 * 
 * @author jackiea
 */
public interface OAuth2ServiceDelegate extends Nameable, Closeable {

    ClientRegistration getClientRegistration();

    OAuth20Service getOAuth20Service();

    OAuth2User getOAuth2User(OAuth2AccessToken accessToken) throws OAuth2AuthenticationException;

    default DefaultApi20 getApi() {
        return this.getOAuth20Service().getApi();
    }

    default String getVersion() {
        return this.getOAuth20Service().getVersion();
    }

    default String getResponseType() {
        return this.getOAuth20Service().getResponseType();
    }

    default String getDefaultScope() {
        return this.getOAuth20Service().getDefaultScope();
    }

    @Override
    default void close() throws IOException {
        this.getOAuth20Service().close();
    }

    default String getApiKey() {
        return this.getOAuth20Service().getApiKey();
    }

    default String getApiSecret() {
        return this.getOAuth20Service().getApiSecret();
    }

    default String getCallback() {
        return this.getOAuth20Service().getCallback();
    }

    default String getAuthorizationUrl() {
        return this.getOAuth20Service().getAuthorizationUrl();
    }

    default String getAuthorizationUrl(String state) {
        return this.getOAuth20Service().getAuthorizationUrl(state);
    }

    default String getAuthorizationUrl(Map<String, String> additionalParams) {
        return this.getOAuth20Service().getAuthorizationUrl(additionalParams);
    }

    default String getAuthorizationUrl(PKCE pkce) {
        return this.getOAuth20Service().getAuthorizationUrl(pkce);
    }

    default AuthorizationUrlBuilder createAuthorizationUrlBuilder() {
        return this.getOAuth20Service().createAuthorizationUrlBuilder();
    }

    default Future<OAuth2AccessToken> getAccessTokenAsync(String code) {
        return this.getOAuth20Service().getAccessTokenAsync(code);
    }

    default Future<OAuth2AccessToken> getAccessTokenAsync(AccessTokenRequestParams params) {
        return this.getOAuth20Service().getAccessTokenAsync(params);
    }

    default OAuth2AccessToken getAccessToken(String code) throws IOException, InterruptedException, ExecutionException {
        return this.getOAuth20Service().getAccessToken(code);
    }

    default OAuth2AccessToken getAccessToken(AccessTokenRequestParams params)
            throws IOException, InterruptedException, ExecutionException {
        return this.getOAuth20Service().getAccessToken(params);
    }

    default Future<OAuth2AccessToken> getAccessToken(AccessTokenRequestParams params,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return this.getOAuth20Service().getAccessToken(params, callback);
    }

    default Future<OAuth2AccessToken> getAccessToken(String code,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return this.getOAuth20Service().getAccessToken(code, callback);
    }

    default void signRequest(String accessToken, OAuthRequest request) {
        this.getOAuth20Service().signRequest(accessToken, request);
    }

    default void signRequest(OAuth2AccessToken accessToken, OAuthRequest request) {
        this.getOAuth20Service().signRequest(accessToken, request);
    }

    default Future<Response> executeAsync(OAuthRequest request) {
        return this.getOAuth20Service().executeAsync(request);
    }

    default Future<Response> execute(OAuthRequest request, OAuthAsyncRequestCallback<Response> callback) {
        return this.getOAuth20Service().execute(request, callback);
    }

    default <R> Future<R> execute(OAuthRequest request, OAuthAsyncRequestCallback<R> callback,
            OAuthRequest.ResponseConverter<R> converter) {
        return this.getOAuth20Service().execute(request, callback, converter);
    }

    default Response execute(OAuthRequest request) throws InterruptedException, ExecutionException, IOException {
        return this.getOAuth20Service().execute(request);
    }
}
