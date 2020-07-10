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
package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet;

import com.youkol.support.scribejava.oauth2.client.OAuth2ClientServiceDelegate;
import com.youkol.support.scribejava.oauth2.client.OAuth2ClientServiceWrapper;
import com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.OAuth2ClientAutoConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@AutoConfigureAfter(OAuth2ClientAutoConfiguration.class)
@EnableConfigurationProperties(OAuth2LoginProperties.class)
@ConditionalOnProperty(name = "youkol.oauth2.web.enabled", matchIfMissing = true)
public class OAuth2MvcAutoConfiguration {

    // @Bean
    // @ConditionalOnBean(OAuth2ClientServiceWrapper.class)
    // @ConditionalOnMissingBean(value = OAuth2LoginController.class, search = SearchStrategy.CURRENT)
    // public BasicOAuth2LoginController oAuth2LoginController(OAuth2LoginProperties properties, OAuth2ClientServiceWrapper oAuth2ClientService) {
    //     return new BasicOAuth2LoginController(properties, oAuth2ClientService);
    // }

    @Bean
    @ConditionalOnBean(OAuth2ClientServiceWrapper.class)
    @ConditionalOnMissingBean(value = OAuth2LoginController.class, search = SearchStrategy.CURRENT)
    public BasicOAuth2LoginController oAuth2LoginController(OAuth2LoginProperties properties, OAuth2ClientServiceDelegate oAuth2ClientServiceDelegate) {
        return new BasicOAuth2LoginController(properties, oAuth2ClientServiceDelegate);
    }
}
