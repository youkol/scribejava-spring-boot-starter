### scribejava-spring-boot-starter

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.youkol.support.scribejava/scribejava-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.youkol.support.scribejava/scribejava-spring-boot-starter)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.youkol.support.scribejava/scribejava-spring-boot-starter?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/snapshots/com/youkol/support/scribejava/scribejava-spring-boot-starter/)
[![License](https://img.shields.io/badge/license-apache-brightgreen)](http://www.apache.org/licenses/LICENSE-2.0.html)

scribejava for spring boot autoconfigure.

#### Features
 [x] OAuth2ClientServiceWrapper. You can customize your own wrapper implementation. For example: [OAuth2ClientServiceWrapper](https://github.com/youkol/scribejava-spring-boot-starter/blob/master/src/main/java/com/youkol/support/scribejava/oauth2/client/OAuth2ClientServiceWrapper.java)   
 [x] Support for basic oauth2 authorization operations. For more information, please see
 [BasicOAuth2LoginController](https://github.com/youkol/scribejava-spring-boot-starter/blob/master/src/main/java/com/youkol/support/scribejava/spring/autoconfigure/oauth2/client/servlet/BasicOAuth2LoginController.java)   
 [x] Support for Authentication Success Handler and Failure Handler.

 #### Usage
For Maven
```xml
<dependency>
  <groupId>com.youkol.support.scribejava</groupId>
  <artifactId>scribejava-spring-boot-starter</artifactId>
  <version>${scribejava.spring.version}</version>
</dependency>
```
Spring-boot application.yml
```yml
youkol:
  oauth2:
    web: 
      enabled: true # default true
      authorize:
        path: "/oauth2/authorize/{registrationId}" # default
      callback:
        path: "/oauth2/callback/{registrationId}" # default
    client:
      enabled: true
      registration:
        wechat_offical: # => spring WrapperDefaultApi20 bean name
          client-id: [your apikey]
          client-secret: [your apiSecret]
          scope: [default scope]
          redirect-uri: "{baseUrl}/oauth2/callback/{registrationId}?redirect_uri={redirect_uri}" # default
```
For your project
```java 
@Configuration
public class ScribejavaConfig {
    
    @Bean("sina_weibo")
    public SinaWeiboApi20 sinaWeiboApi20() {
        return SinaWeiboApi20.instance();
    }

    @Bean("wechat_offical")
    public WeChatMpApi20 weChatMpApi20() {
        return WeChatMpApi20.instance();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CunstomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CunstomAuthenticationFailureHandler();
    }
}
```
