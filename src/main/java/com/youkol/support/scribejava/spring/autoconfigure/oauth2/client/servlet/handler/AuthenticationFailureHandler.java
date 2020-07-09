package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.youkol.support.scribejava.service.delegate.OAuth2AuthenticationException;

public interface AuthenticationFailureHandler {

    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            OAuth2AuthenticationException exception) throws IOException, ServletException;
}
