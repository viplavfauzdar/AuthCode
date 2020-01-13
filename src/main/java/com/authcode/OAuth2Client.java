package com.authcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OAuth2Client {
	
	private static final Logger logger = LoggerFactory.getLogger(OAuth2Client.class);
	
	public static String getAccessToken(String clientId, String clientSecret, String tokenUrl){
	
		logger.debug("OAuth2 Token Url: {}", tokenUrl);
		
		ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
	    resourceDetails.setClientId(clientId);
	    resourceDetails.setClientSecret(clientSecret);    
	    resourceDetails.setAccessTokenUri(tokenUrl);
	    
	    OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext());	   
	    
	    String accessToken = oauth2RestTemplate.getAccessToken().toString();
	    
	    return accessToken;
		
	}
	
	@RequestMapping(value = "/gettoken", method = RequestMethod.GET)
	public String getToken(){
		return getAccessToken("069bb985-df99-43ba-a7de-609d075e6423","3c12c652-729e-4e3b-b8c8-c2de0143838c","https://auth.login.run-np.homedepot.com/oauth/token");
	}
	
	public static void main(String[] args){
		String token = getAccessToken("a68afe67-26ad-4aac-8983-bab7766da14c","67a8e6bd-b329-4016-92cd-1c6fef649f58",
				"https://auth.login.run-np.homedepot.com/oauth/token");
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "Bearer " + token);
	    
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> resp = rt.exchange("http://localhost:8080/api/dc/findAll",
				HttpMethod.GET, new HttpEntity<String>(null,headers), String.class);
		logger.info("Response: {}",resp);
	}

}
