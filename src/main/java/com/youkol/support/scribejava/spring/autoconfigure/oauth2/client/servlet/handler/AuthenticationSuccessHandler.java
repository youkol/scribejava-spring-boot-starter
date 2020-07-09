package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;

public interface AuthenticationSuccessHandler {

    void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            OAuth2AccessToken accessToken, OAuth2User oAuth2User) throws IOException, ServletException;
}
