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
