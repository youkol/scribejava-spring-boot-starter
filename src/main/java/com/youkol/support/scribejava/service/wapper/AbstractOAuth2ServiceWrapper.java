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
