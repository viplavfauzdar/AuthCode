package com.authcode;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.authcode.grps.UserInfo;

import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class Resource {
	
	private static final Logger logger = LoggerFactory.getLogger(Resource.class);
	
	@CrossOrigin
	@ApiOperation(value = "userinfo", nickname = "userinfo")
	@RequestMapping(value = "/userinfo", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Principal user(Principal principal, HttpServletRequest request, HttpServletResponse response) {
		logger.info("REMOTE USER: {}", request.getRemoteUser());
		response.setHeader("Access-Control-Allow-Origin","*");	
		return principal;
	}
	
	@CrossOrigin
	@ApiOperation(value = "hello", nickname = "hello")
	@RequestMapping(value = "/hello", method = RequestMethod.GET, produces=MediaType.TEXT_HTML_VALUE)
	public String hello(Principal principal, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin","*");	
		return "Hello, <b><u>"+ request.getRemoteUser() +"</u></b>! I am a resource :)";
	}
	
	@CrossOrigin
	@ApiOperation(value = "groups", nickname = "groups")
	@RequestMapping(value = "/groups", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UserInfo getUserGroups(HttpServletRequest request1) throws Exception{
		RestTemplate rt = new RestTemplate();		
		rt.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request,body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });
		return rt.getForObject("https://dapper.apps-np.homedepot.com/users/" + request1.getRemoteUser(), UserInfo.class);
	}
	
	/*@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "Successfully Logged Out!";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}*/

}
