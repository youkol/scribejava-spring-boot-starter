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

import java.util.ArrayList;
import java.util.List;

import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistration;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistrationRepository;
import com.youkol.support.scribejava.oauth2.client.registration.InMemoryClientRegistrationRepository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OAuth2ClientProperties.class)
@Conditional(ClientsConfiguredCondition.class)
public class OAuth2ClientRegistrationRepositoryConfiguration {

    private final OAuth2ClientProperties properties;

    public OAuth2ClientRegistrationRepositoryConfiguration(OAuth2ClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(ClientRegistrationRepository.class)
    public InMemoryClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = new ArrayList<>(OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(properties).values());
        return new InMemoryClientRegistrationRepository(registrations);
    }

}
