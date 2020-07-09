package com.youkol.support.scribejava.service.wapper;

import java.io.Closeable;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;

public interface OAuth2ServiceWrapper extends Closeable {

    OAuth2User getOAuth2User(OAuth2AccessToken accessToken) throws Exception;

}
