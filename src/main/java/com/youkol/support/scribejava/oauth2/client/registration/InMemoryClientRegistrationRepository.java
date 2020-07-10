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
package com.youkol.support.scribejava.oauth2.client.registration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class InMemoryClientRegistrationRepository implements ClientRegistrationRepository {

    private final Map<String, ClientRegistration> registrations;

    public InMemoryClientRegistrationRepository(ClientRegistration... registrations) {
        this(Arrays.asList(registrations));
    }

    public InMemoryClientRegistrationRepository(List<ClientRegistration> registrations) {
        if (registrations.isEmpty()) {
            throw new IllegalArgumentException("registrations cannot be empty");
        }

        Collector<ClientRegistration, ?, ConcurrentMap<String, ClientRegistration>> collector =
            Collectors.toConcurrentMap(ClientRegistration::getRegistrationId, Function.identity());

        this.registrations = registrations.stream()
            .collect(Collectors.collectingAndThen(collector, Collections::unmodifiableMap));
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        if (registrationId == null) {
            throw new IllegalArgumentException("registrationId cannot be null");
        }
        
        return this.registrations.get(registrationId);
    }

}
