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
