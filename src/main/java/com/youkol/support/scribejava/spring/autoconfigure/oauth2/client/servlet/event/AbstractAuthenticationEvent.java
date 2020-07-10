package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.event;

import com.github.scribejava.core.model.OAuth2AccessToken;

import org.springframework.context.ApplicationEvent;

/**
 * Note: OAuth2AccessToken may be null, for example: when on failure. 
 * 
 * @author jackiea
 */
public class AbstractAuthenticationEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public AbstractAuthenticationEvent(OAuth2AccessToken accessToken) {
        super(accessToken);
    }

    public OAuth2AccessToken getOAuth2AccessToken() {
        return (OAuth2AccessToken) super.getSource();
    }
    
}
