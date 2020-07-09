package com.youkol.support.scribejava.oauth2.client.registration;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

public final class ClientRegistration implements Serializable {

    private static final long serialVersionUID = 1L;

    private String registrationId;

    private String clientId;

    private String clientSecret;

    private String redirectUriTemplate;

    private Set<String> scopes = Collections.emptySet();

	private String clientName;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUriTemplate() {
        return redirectUriTemplate;
    }

    public void setRedirectUriTemplate(String redirectUriTemplate) {
        this.redirectUriTemplate = redirectUriTemplate;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
