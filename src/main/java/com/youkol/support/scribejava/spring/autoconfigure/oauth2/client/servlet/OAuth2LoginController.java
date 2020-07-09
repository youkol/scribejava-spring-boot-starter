package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet;

public interface OAuth2LoginController {

    String getAuthorizePath();
    
    String getCallbackPath();
}
