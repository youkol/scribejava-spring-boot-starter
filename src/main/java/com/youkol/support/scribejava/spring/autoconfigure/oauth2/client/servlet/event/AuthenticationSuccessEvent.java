package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.event;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;

public class AuthenticationSuccessEvent extends AbstractAuthenticationEvent {

    private static final long serialVersionUID = 1L;

    private OAuth2User oAuth2User;

    public AuthenticationSuccessEvent(OAuth2AccessToken accessToken, OAuth2User oAuth2User) {
        super(accessToken);
        this.oAuth2User = oAuth2User;
    }

    public OAuth2User getOAuth2User() {
        return this.oAuth2User;
    }

}
