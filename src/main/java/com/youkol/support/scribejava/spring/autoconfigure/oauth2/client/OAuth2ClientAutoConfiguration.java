package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client;

import java.util.Map;

import com.youkol.support.scribejava.apis.wrapper.WrapperDefaultApi20;
import com.youkol.support.scribejava.oauth2.client.OAuth2ClientServiceDelegate;
import com.youkol.support.scribejava.oauth2.client.OAuth2ClientServiceWrapper;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistration;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistrationRepository;
import com.youkol.support.scribejava.service.delegate.OAuth2ServiceDelegate;

import org.springframework.beans.factory.ObjectProvider;
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

    @Bean
    @ConditionalOnBean(OAuth2ServiceDelegate.class)
    @ConditionalOnMissingBean
    public OAuth2ClientServiceDelegate oAuth2ClientServiceDelegate(ObjectProvider<OAuth2ServiceDelegate> oAuth2ServiceDelegate) {
        return new OAuth2ClientServiceDelegate(oAuth2ServiceDelegate.orderedStream().toArray(OAuth2ServiceDelegate[]::new));
    }
}
