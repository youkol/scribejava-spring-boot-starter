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

import com.github.scribejava.core.model.OAuth2AccessToken;

import org.springframework.context.ApplicationEvent;

/**
 * Note: OAuth2AccessToken may be null, for example: when on failure. 
 * 
 * @author jackiea
 */
public class AbstractAuthenticationEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public AbstractAuthenticationEvent(OAuth2AccessToken accessToken) {
        super(accessToken);
    }

    public OAuth2AccessToken getOAuth2AccessToken() {
        return (OAuth2AccessToken) super.getSource();
    }
    
}
