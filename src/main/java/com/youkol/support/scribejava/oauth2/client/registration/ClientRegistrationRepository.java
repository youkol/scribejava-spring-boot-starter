package com.youkol.support.scribejava.oauth2.client.registration;

public interface ClientRegistrationRepository {
    
    ClientRegistration findByRegistrationId(String registrationId);
}
