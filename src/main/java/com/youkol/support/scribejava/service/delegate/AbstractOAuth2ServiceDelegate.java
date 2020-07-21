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

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistration;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistrationRepository;

public abstract class AbstractOAuth2ServiceDelegate implements OAuth2ServiceDelegate, Cacheable {

    private String name;

    private DefaultApi20 api;

    // memory cache
    private OAuth20Service oAuth20Service;

    private ClientRegistrationRepository clientRegistrationRepository;

    private Optional<ObjectMapper> objectMapper = Optional.empty();

    private ObjectMapper lazyObjectMapper;

    protected static final TypeReference<Map<String, Object>> mapType = new TypeReference<Map<String, Object>>() {
    };

    public AbstractOAuth2ServiceDelegate(String registrationId, ClientRegistrationRepository clientRegistrationRepository,
            DefaultApi20 api, Optional<ObjectMapper> objectMapper) {
        if (registrationId == null) {
            throw new IllegalArgumentException("registrationId cannot be null");
        }
        if (clientRegistrationRepository == null) {
            throw new IllegalArgumentException("clientRegistrationRepository cannot be null");
        }
        if (api == null) {
            throw new IllegalArgumentException("api cannot be null");
        }

        this.name = registrationId;
        this.api = api;
        this.clientRegistrationRepository = clientRegistrationRepository;
        if (objectMapper != null) {
            this.objectMapper = objectMapper;
        }
    }

    public ObjectMapper getLazyObjectMapper() {
        this.lazyObjectMapper = objectMapper.orElse(new ObjectMapper());
        return this.lazyObjectMapper;
    }

    public Optional<ObjectMapper> getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(final Optional<ObjectMapper> objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public OAuth20Service getOAuth20Service() {
        OAuth20Service oAuth20Service = Optional.ofNullable(this.oAuth20Service).orElseGet(() -> {
            ClientRegistration registration = this.getClientRegistration();
            String scope = registration.getScopes().stream()
                .collect(Collectors.joining(" "));

            this.oAuth20Service = new ServiceBuilder(registration.getClientId())
                .apiSecret(registration.getClientSecret())
                .callback(registration.getRedirectUriTemplate())
                .defaultScope(scope)
                .build(this.getApi());

            return this.oAuth20Service;
        });

        return oAuth20Service;
    }

    @Override
    public DefaultApi20 getApi() {
        return this.api;
    }

    @Override
    public ClientRegistration getClientRegistration() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(this.getName());
        if (clientRegistration == null) {
            throw new IllegalArgumentException("clientRegistration cannot be null");
        }

        return clientRegistration;
    }

    @Override
    public void clearCache() {
        this.oAuth20Service = null;
    }

}
