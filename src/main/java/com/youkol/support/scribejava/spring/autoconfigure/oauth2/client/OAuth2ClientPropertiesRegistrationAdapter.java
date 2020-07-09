package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client;

import java.util.HashMap;
import java.util.Map;

import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistration;

import org.springframework.boot.context.properties.PropertyMapper;

public class OAuth2ClientPropertiesRegistrationAdapter {

    private OAuth2ClientPropertiesRegistrationAdapter() {
    }

    public static Map<String, ClientRegistration> getClientRegistrations(OAuth2ClientProperties properties) {
        Map<String, ClientRegistration> clientRegistrations = new HashMap<>();
        properties.getRegistration()
            .forEach((key, value) -> clientRegistrations.put(key, getClientRegistration(key, value)));

        return clientRegistrations;
    }

    private static ClientRegistration getClientRegistration(String registrationId, OAuth2ClientProperties.Registration registration) {
        ClientRegistration clientRegistration = new ClientRegistration();
        clientRegistration.setRegistrationId(registrationId);

        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(registration::getClientId).to(clientRegistration::setClientId);
        map.from(registration::getClientSecret).to(clientRegistration::setClientSecret);
        map.from(registration::getRedirectUri).to(clientRegistration::setRedirectUriTemplate);
        map.from(registration::getScope).to(clientRegistration::setScopes);
        map.from(registration::getClientName).to(clientRegistration::setClientName);

        return clientRegistration;
    }
}
