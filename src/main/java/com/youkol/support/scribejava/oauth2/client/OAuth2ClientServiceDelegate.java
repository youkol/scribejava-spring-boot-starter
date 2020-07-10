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
package com.youkol.support.scribejava.oauth2.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.youkol.support.scribejava.service.delegate.OAuth2ServiceDelegate;

public class OAuth2ClientServiceDelegate {

    private final Map<String, OAuth2ServiceDelegate> oAuth2ServiceDelegates;

    public OAuth2ClientServiceDelegate(OAuth2ServiceDelegate... oAuth2ServiceDelegates) {
        this(Arrays.asList(oAuth2ServiceDelegates));
    }

    public OAuth2ClientServiceDelegate(List<OAuth2ServiceDelegate> oAuth2ServiceDelegates) {
        if (oAuth2ServiceDelegates == null || oAuth2ServiceDelegates.isEmpty()) {
            throw new IllegalArgumentException("OAuth2ServiceDelegate cannot be empty");
        }

        Collector<OAuth2ServiceDelegate, ?, ConcurrentMap<String, OAuth2ServiceDelegate>> collector = Collectors
                .toConcurrentMap(OAuth2ServiceDelegate::getName, Function.identity());

        this.oAuth2ServiceDelegates = oAuth2ServiceDelegates.stream()
                .collect(Collectors.collectingAndThen(collector, Collections::unmodifiableMap));
    }

    public OAuth2ServiceDelegate getDelegate(String registrationId) {
        if (registrationId == null) {
            throw new IllegalArgumentException("registrationId cannot be null");
        }
        return this.oAuth2ServiceDelegates.get(registrationId);
    }
}
