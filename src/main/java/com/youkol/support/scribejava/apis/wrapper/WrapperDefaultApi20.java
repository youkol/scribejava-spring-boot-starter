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
package com.youkol.support.scribejava.apis.wrapper;

import java.io.OutputStream;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.youkol.support.scribejava.service.wapper.AbstractOAuth2ServiceWrapper;

public abstract class WrapperDefaultApi20 extends DefaultApi20 {

    @Override
    public abstract AbstractOAuth2ServiceWrapper createService(String apiKey, String apiSecret, String callback,
            String defaultScope, String responseType, OutputStream debugStream, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient);
}
