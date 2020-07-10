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
package com.youkol.support.scribejava.service.wapper;

import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

public abstract class AbstractOAuth2ServiceWrapper extends OAuth20Service implements OAuth2ServiceWrapper {

    private Optional<ObjectMapper> objectMapper = Optional.empty();

    private ObjectMapper lazyObjectMapper;

    protected static final TypeReference<Map<String, Object>> mapType = new TypeReference<Map<String, Object>>() {
    };

    public AbstractOAuth2ServiceWrapper(DefaultApi20 api, String apiKey, String apiSecret, String callback,
            String defaultScope, String responseType, OutputStream debugStream, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, defaultScope, responseType, debugStream, userAgent, httpClientConfig,
                httpClient);
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

}
