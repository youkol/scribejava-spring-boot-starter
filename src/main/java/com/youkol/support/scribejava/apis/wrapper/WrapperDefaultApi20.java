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
