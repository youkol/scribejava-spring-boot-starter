/**
 * Copyright (C) 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    @Override
    public String toString() {
        return "DefaultOAuth2User [attributes=" + attributes + ", nameAttributeKey=" + nameAttributeKey + "]";
    }
    
}
