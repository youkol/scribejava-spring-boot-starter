package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.event;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.youkol.support.scribejava.service.OAuth2AuthenticationException;

public class AuthenticationFailureEvent extends AbstractAuthenticationEvent {

    private static final long serialVersionUID = 1L;

    private OAuth2AuthenticationException exception;

    public AuthenticationFailureEvent(OAuth2AccessToken accessToken, OAuth2AuthenticationException exception) {
        super(accessToken);
        this.exception = exception;
    }

    public OAuth2AuthenticationException getException() {
		return exception;
    }

}
