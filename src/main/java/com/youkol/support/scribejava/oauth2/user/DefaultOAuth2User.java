package com.youkol.support.scribejava.oauth2.user;

import java.util.HashMap;
import java.util.Map;

public class DefaultOAuth2User implements OAuth2User {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> attributes = new HashMap<>();

    private String nameAttributeKey;

    public DefaultOAuth2User(String nameAttributeKey, Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException("attributes cannot be empty");
        }
        if (nameAttributeKey == null) {
            throw new IllegalArgumentException("nameAttributeKey cannot be empty");
        }
        if (!attributes.containsKey(nameAttributeKey)) {
            throw new IllegalArgumentException("Missing attribute '" + nameAttributeKey + "' in attributes");
        }

        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return getAttribute(nameAttributeKey).toString();
    }
    
}
