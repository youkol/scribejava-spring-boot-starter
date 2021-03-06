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
package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.event;

import com.youkol.support.scribejava.oauth2.user.OAuth2User;
import com.youkol.support.scribejava.oauth2.user.OAuth2UserRequest;

public class AuthenticationSuccessEvent extends AbstractAuthenticationEvent {

    private static final long serialVersionUID = 1L;

    private OAuth2User oAuth2User;

    public AuthenticationSuccessEvent(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        super(oAuth2UserRequest);
        this.oAuth2User = oAuth2User;
    }

    public OAuth2User getOAuth2User() {
        return this.oAuth2User;
    }

}
