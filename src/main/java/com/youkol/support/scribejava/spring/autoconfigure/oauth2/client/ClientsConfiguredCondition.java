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

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ClientsConfiguredCondition extends SpringBootCondition {

    private static final Bindable<Map<String, OAuth2ClientProperties.Registration>> SPRING_REGISTRATION_MAP = Bindable
            .mapOf(String.class, OAuth2ClientProperties.Registration.class);

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth2 Clients Configured Condition");
        Map<String, OAuth2ClientProperties.Registration> registrations = getRegistrations(context.getEnvironment());
        if (!registrations.isEmpty()) {
            String clientIds = registrations.values().stream()
                .map(OAuth2ClientProperties.Registration::getClientId)
                .collect(Collectors.joining(", "));
            return ConditionOutcome.match(message.foundExactly("registered clients" + clientIds));
        }

        return ConditionOutcome.noMatch(message.notAvailable("registered clients"));
    }

    private Map<String, OAuth2ClientProperties.Registration> getRegistrations(Environment environment) {
        return Binder.get(environment)
            .bind("youkol.oauth2.client.registration", SPRING_REGISTRATION_MAP)
            .orElse(Collections.emptyMap());
    }

}
