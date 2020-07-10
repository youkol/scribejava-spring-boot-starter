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

import java.util.Map;

import com.youkol.support.scribejava.apis.wrapper.WrapperDefaultApi20;
import com.youkol.support.scribejava.oauth2.client.OAuth2ClientServiceWrapper;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistration;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistrationRepository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ClientRegistration.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "youkol.oauth2.client.enabled", matchIfMissing = true)
@Import({ OAuth2ClientRegistrationRepositoryConfiguration.class })
public class OAuth2ClientAutoConfiguration {

    @Bean
    @ConditionalOnBean(WrapperDefaultApi20.class)
    @ConditionalOnMissingBean
    public OAuth2ClientServiceWrapper oAuth2ClientService(ClientRegistrationRepository clientRegistrationRepository, Map<String, WrapperDefaultApi20> apis) {
        return new OAuth2ClientServiceWrapper(clientRegistrationRepository, apis);
    }

}
