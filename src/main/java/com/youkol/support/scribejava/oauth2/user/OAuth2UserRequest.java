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
package com.youkol.support.scribejava.oauth2.user;

import java.io.Serializable;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistration;

public class OAuth2UserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private ClientRegistration clientRegistration;

    private OAuth2AccessToken oAuth2AccessToken;

    public OAuth2UserRequest(ClientRegistration clientRegistration, OAuth2AccessToken oAuth2AccessToken) {
        this.clientRegistration = clientRegistration;
        this.oAuth2AccessToken = oAuth2AccessToken;
    }

    public ClientRegistration getClientRegistration() {
        return clientRegistration;
    }

    public OAuth2AccessToken getoAuth2AccessToken() {
        return oAuth2AccessToken;
    }
    
}
