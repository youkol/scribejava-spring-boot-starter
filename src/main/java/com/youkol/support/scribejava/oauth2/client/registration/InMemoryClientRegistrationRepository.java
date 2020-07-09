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
