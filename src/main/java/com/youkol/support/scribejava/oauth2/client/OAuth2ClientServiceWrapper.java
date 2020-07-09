package com.youkol.support.scribejava.oauth2.client;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.youkol.support.scribejava.apis.wrapper.WrapperDefaultApi20;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistration;
import com.youkol.support.scribejava.oauth2.client.registration.ClientRegistrationRepository;
import com.youkol.support.scribejava.service.wapper.AbstractOAuth2ServiceWrapper;

public class OAuth2ClientServiceWrapper {

    private final Map<String, WrapperDefaultApi20> apis;

    private final ClientRegistrationRepository clientRegistrationRepository;

    // memory cache
    private Map<String, AbstractOAuth2ServiceWrapper> oAuth2Services = new HashMap<>();

    public OAuth2ClientServiceWrapper(ClientRegistrationRepository clientRegistrationRepository, Map<String, WrapperDefaultApi20> apis) {
        if (apis == null || apis.isEmpty()) {
            throw new IllegalArgumentException("apis cannot be empty");
        }
        if (clientRegistrationRepository == null) {
            throw new IllegalArgumentException("clientRegistrationRepository cannot be null");
        }

        this.apis = apis;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    public AbstractOAuth2ServiceWrapper getOAuth2Service(String registrationId) {
        // 1: find from memory.
        if (oAuth2Services.containsKey(registrationId)) {
            return oAuth2Services.get(registrationId);
        }

        // 2. find user config.
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration == null) {
            throw new IllegalArgumentException("Invalid Client Registration with Id: " + registrationId);
        }

        WrapperDefaultApi20 api = apis.get(registrationId);
        if (api == null) {
            throw new IllegalArgumentException("Invalid DefaultApi20 with Id: " + registrationId);
        }

        String scope = clientRegistration.getScopes().stream()
            .collect(Collectors.joining(" "));
        // 3. create service.
        OAuth20Service oAuth20Service = new ServiceBuilder(clientRegistration.getClientId())
            .apiSecret(clientRegistration.getClientSecret())
            .callback(clientRegistration.getRedirectUriTemplate())
            .defaultScope(scope)
            .build(api);
        
        // 4. put it to cache
        oAuth2Services.put(registrationId, (AbstractOAuth2ServiceWrapper) oAuth20Service);

        // 5. return
        return (AbstractOAuth2ServiceWrapper) oAuth20Service;
    }

    public void clearOAuth2ServiceCache() {
        this.oAuth2Services.clear();
    }

}
