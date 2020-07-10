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
package com.youkol.support.scribejava.service.delegate;

public class OAuth2AuthenticationException extends Exception {

    private static final long serialVersionUID = 1L;

    private String errorCode;

    private String errorMessage;

    private String rawResponse;

    public OAuth2AuthenticationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public OAuth2AuthenticationException(Throwable cause) {
        super(cause);
    }

    public OAuth2AuthenticationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
    }
    
    public OAuth2AuthenticationException(String errorCode, String errorMessage, String rawResponse) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.rawResponse = rawResponse;
    }

    public OAuth2AuthenticationException(String errorCode, String errorMessage, String rawResponse, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.rawResponse = rawResponse;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getRawResponse() {
        return rawResponse;
    }

}
