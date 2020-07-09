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
