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
package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "youkol.oauth2.client")
public class OAuth2ClientProperties {

    private boolean enabled = true;

    private final Map<String, Registration> registration = new HashMap<>();

    @PostConstruct
    public void validate() {
        this.getRegistration().values().forEach(this::validateRegistration);
    }

    private void validateRegistration(OAuth2ClientProperties.Registration registration) {
        if (!StringUtils.hasText(registration.getClientId())) {
            throw new IllegalStateException("Client id must not be empty.");
        } else if (!StringUtils.hasText(registration.getClientSecret())) {
            throw new IllegalStateException("Client secret must not be empty.");
        }
    }

    public Map<String, Registration> getRegistration() {
        return registration;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class Registration {

        private String clientId;

        private String clientSecret;

        private String redirectUri;

        private Set<String> scope;

        private String clientName;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }

        public Set<String> getScope() {
            return scope;
        }

        public void setScope(Set<String> scope) {
            this.scope = scope;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

    }

}
