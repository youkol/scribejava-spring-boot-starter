package com.youkol.support.scribejava.oauth2.user;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public interface OAuth2User extends Serializable {

    Map<String, Object> getAttributes();

    default Object getAttribute(String name) {
        return Optional.ofNullable(getAttributes())
            .filter(t -> t.containsKey(name))
            .map(t -> t.get(name))
            .orElse(null);
    }

    String getName();
}
