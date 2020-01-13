package com.authcode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ImplicitGrantBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.List;

import static com.google.common.base.Predicates.*;

import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.service.contexts.SecurityContext;
import static com.google.common.collect.Lists.*;
import static springfox.documentation.builders.PathSelectors.*;

@Configuration
@EnableSwagger2
//@Profile({"dev","qa","uat"}) - not needed since we have oauth with swagger now
public class SwaggerConfig {

  private static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

  private static final Logger log = Logger.getLogger(SwaggerConfig.class);
  
  //@Value("${swagger.clientid}")
  private String CLIENT_ID="23417740-b839-4e40-b563-99326215d0eb";
  
  //@Value("${swagger.oauth.uri}")
  private String OAUTH_URI="https://auth.login.run-np.homedepot.com/oauth/authorize";
		  
  @Bean
  public Docket api() {

    log.info("Starting Swagger.");
    StopWatch watch = new StopWatch();
    watch.start();

    Docket docket = new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .forCodeGeneration(true)
        .select()
        .paths(timsuiPaths())//regex(DEFAULT_INCLUDE_PATTERN))            
        .build()
        .securitySchemes(newArrayList(oauth()))
        .securityContexts(newArrayList(securityContext()))
        ;

    watch.stop();
    log.info(String.format("Started Swagger in {} ms %s", watch.getTotalTimeMillis()));

    return docket;
  }
  
  private Predicate<String> timsuiPaths() {
      return or(
              regex(DEFAULT_INCLUDE_PATTERN)
      );
  }
  
  

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Swagger OAuth Demo")
        .description("Swagger OAuth Demo")
        .build();
  }
  
  @Bean
  SecurityContext securityContext() {
      //AuthorizationScope readScope = new AuthorizationScope("uaa.resource", "");
      //AuthorizationScope readScope1 = new AuthorizationScope("openid", "");
      AuthorizationScope[] scopes = new AuthorizationScope[2];
      //scopes[0] = readScope;
      //scopes[1] = readScope1;
      SecurityReference securityReference = SecurityReference.builder()
              .reference("timsui_auth")
              .scopes(scopes)
              .build();

      return SecurityContext.builder()
              .securityReferences(newArrayList(securityReference))
              .forPaths(ant("/api/.*"))
              .build();
  }

  @Bean
  SecurityScheme oauth() {
      return new OAuthBuilder()
              .name("timsui_auth")
              .grantTypes(grantTypes())
              //.scopes(scopes())                
              .build();
  }
  
  @Bean
  SecurityScheme apiKey() {
      return new ApiKey("api_key", "api_key", "header");
  }

  
  List<AuthorizationScope> scopes() {
      return newArrayList(
              new AuthorizationScope("uaa.resource", ""),
              new AuthorizationScope("openid", ""));
  }
  
  List<GrantType> grantTypes() {
      GrantType grantType = new ImplicitGrantBuilder()
              .loginEndpoint(new LoginEndpoint(OAUTH_URI))
              .build();
      //GrantType grantType1 = new ClientCredentialsGrant("https://auth.login.run-np.homedepot.com/token_key");
      return newArrayList(grantType);//, grantType1);
  }

  @Bean
  public SecurityConfiguration securityInfo() {
      return new SecurityConfiguration(CLIENT_ID, "123", "public", "TIMSUI", "123", ApiKeyVehicle.HEADER, "", ",");
  }

}
