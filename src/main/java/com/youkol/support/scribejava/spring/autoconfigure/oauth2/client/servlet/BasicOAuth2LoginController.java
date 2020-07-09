package com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.youkol.support.scribejava.oauth2.client.OAuth2ClientServiceDelegate;
import com.youkol.support.scribejava.oauth2.user.OAuth2User;
import com.youkol.support.scribejava.service.delegate.OAuth2AuthenticationException;
import com.youkol.support.scribejava.service.delegate.OAuth2ServiceDelegate;
import com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.handler.AuthenticationFailureHandler;
import com.youkol.support.scribejava.spring.autoconfigure.oauth2.client.servlet.handler.AuthenticationSuccessHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Validated
@Controller
public class BasicOAuth2LoginController implements OAuth2LoginController {

    public static final Logger log = LoggerFactory.getLogger(BasicOAuth2LoginController.class);

    private OAuth2LoginProperties oAuth2LoginProperties;

    // private OAuth2ClientServiceWrapper oAuth2ClientService;

    private OAuth2ClientServiceDelegate oAuth2ClientServiceDelegate;

    @Autowired
    private ObjectProvider<AuthenticationSuccessHandler> successHandler;

    @Autowired
    private ObjectProvider<AuthenticationFailureHandler> failureHandler;

    // public BasicOAuth2LoginController(OAuth2LoginProperties
    // oAuth2LoginProperties, OAuth2ClientServiceWrapper oAuth2ClientService) {
    // this.oAuth2LoginProperties = oAuth2LoginProperties;
    // this.oAuth2ClientService = oAuth2ClientService;
    // }
    public BasicOAuth2LoginController(OAuth2LoginProperties oAuth2LoginProperties,
            OAuth2ClientServiceDelegate oAuth2ClientServiceDelegate) {
        this.oAuth2LoginProperties = oAuth2LoginProperties;
        this.oAuth2ClientServiceDelegate = oAuth2ClientServiceDelegate;
    }

    @RequestMapping(value = "${youkol.web.oauth2.authorize.path:/oauth2/authorize/{registrationId}}")
    public void authenticate(@PathVariable String registrationId,
            @RequestParam(name = "redirect_uri", required = false) String successRedirectUri, // 成功授权之后，需要返回的地址
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // AbstractOAuth2ServiceWrapper oAuth20Service =
        // oAuth2ClientService.getOAuth2Service(registrationId);
        OAuth2ServiceDelegate oAuth20Service = oAuth2ClientServiceDelegate.getDelegate(registrationId);

        String redirectUriTemplate = oAuth20Service.getAuthorizationUrl(UUID.randomUUID().toString());
        redirectUriTemplate = URLDecoder.decode(redirectUriTemplate, StandardCharsets.UTF_8.displayName());

        String redirectUri = this.expandRedirectUri(request, registrationId, redirectUriTemplate, successRedirectUri);

        log.debug("OAuth2 redirectUri: " + redirectUri);

        response.sendRedirect(redirectUri);
    }

    @RequestMapping(value = "${youkol.web.oauth2.callback.path:/oauth2/callback/{registrationId}}")
    public void callback(@PathVariable String registrationId,
            @RequestParam(name = "redirect_uri", required = false) String successRedirectUri,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "state", required = false) String state, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        log.debug("OAuth2 code: {}, state: {}, redirect_uri: {}", code, state, successRedirectUri);

        if (!StringUtils.hasText(code)) {
            throw new OAuth2AuthenticationException("INVALID_REQUEST");
        }

        try {
            // AbstractOAuth2ServiceWrapper oAuth20Service =
            // oAuth2ClientService.getOAuth2Service(registrationId);
            OAuth2ServiceDelegate oAuth20Service = oAuth2ClientServiceDelegate.getDelegate(registrationId);

            OAuth2AccessToken accessToken = oAuth20Service.getAccessToken(code);

            log.debug("OAuth2 accessToken: " + accessToken);

            OAuth2User oAuth2User = oAuth20Service.getOAuth2User(accessToken);

            if (oAuth2User != null) {
                log.debug(oAuth2User.getName());
                log.debug(oAuth2User.getAttributes().toString());
            }

            // on success
            this.onAuthenticationSuccess(request, response, accessToken, oAuth2User);
            if (StringUtils.hasText(successRedirectUri)) {
                response.sendRedirect(successRedirectUri);
            } else {
                response.sendRedirect("/");
            }

        } catch (OAuth2AuthenticationException ex) {
            // on failure
            this.onAuthenticationFailure(request, response, ex);
            throw ex;
        } catch (Exception ex) {
            OAuth2AuthenticationException exception = new OAuth2AuthenticationException("OAuth2 callback error", ex);
            // on failure
            this.onAuthenticationFailure(request, response, exception);
            throw exception;
        }
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            OAuth2AccessToken accessToken, OAuth2User oAuth2User) throws IOException, ServletException {
        Iterator<AuthenticationSuccessHandler> iterator = successHandler.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAuthenticationSuccess(request, response, accessToken, oAuth2User);
        }
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            OAuth2AuthenticationException exception) throws IOException, ServletException {
        Iterator<AuthenticationFailureHandler> iterator = failureHandler.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAuthenticationFailure(request, response, exception);
        }
    }

    private String expandRedirectUri(HttpServletRequest request, String registrationId, String redirectUriTemplate, String successRedirectUri) {
        String contextPath = request.getContextPath();
        String fullRequestUrl = this.buildFullRequestUrl(request);
        String baseUrl = UriComponentsBuilder.fromHttpUrl(fullRequestUrl)
            .replaceQuery(null)
            .replacePath(contextPath)
            .build()
            .toUriString();

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("baseUrl", baseUrl);
        uriVariables.put("registrationId", registrationId);
        uriVariables.put("redirect_uri", successRedirectUri);

        return UriComponentsBuilder.fromUriString(redirectUriTemplate)
            .buildAndExpand(uriVariables)
            .toUriString();
    }

    private String buildFullRequestUrl(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            return requestUrl + "?" + queryString;
        }

        return requestUrl;
    }

    @Override
    public String getAuthorizePath() {
        return oAuth2LoginProperties.getAuthorize().getPath();
    }

    @Override
    public String getCallbackPath() {
        return oAuth2LoginProperties.getCallback().getPath();
    }

    public ObjectProvider<AuthenticationSuccessHandler> getSuccessHandler() {
        return successHandler;
    }

    public void setSuccessHandler(ObjectProvider<AuthenticationSuccessHandler> successHandler) {
        this.successHandler = successHandler;
    }

    public ObjectProvider<AuthenticationFailureHandler> getFailureHandler() {
        return failureHandler;
    }

    public void setFailureHandler(ObjectProvider<AuthenticationFailureHandler> failureHandler) {
        this.failureHandler = failureHandler;
    }

    
}
