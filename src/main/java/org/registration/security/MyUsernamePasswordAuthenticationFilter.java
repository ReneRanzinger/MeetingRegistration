package org.registration.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.springframework.security.core.userdetails.User;
import org.registration.config.SecurityConstants;
import org.registration.view.Confirmation;
import org.registration.view.ErrorCodes;
import org.registration.view.ErrorMessage;
import org.registration.view.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Value("${admin.token-secret}")
	String tokenSecret;
	
	long tokenExpiration = SecurityConstants.EXPIRATION_TIME;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		
		try {
            LoginRequest creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
        	try {
        		sendError(request, response, e);
        		return null;
        	} catch (IOException e1) {
        		throw new RuntimeException(e1);
        	}
        } 
	}
	
	 @Override
	    protected void successfulAuthentication(HttpServletRequest req,
	                                            HttpServletResponse res,
	                                            FilterChain chain,
	                                            Authentication auth) throws IOException, ServletException {

	    	String token = Jwts.builder()
	                .setSubject(((User) auth.getPrincipal()).getUsername())
	                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
	                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes())
	                .compact();
	        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
	        //IMPORTANT! Needed for CORS request, otherwise header will not be visible to the client
	        res.addHeader("Access-Control-Expose-Headers", SecurityConstants.HEADER_STRING);   
	        Confirmation confirmation = new Confirmation ("Signed in successfully", HttpStatus.OK.value());
	        String acceptString = req.getHeader("Accept");
			if (acceptString.contains("xml")) {
				res.setContentType("application/xml;charset=UTF-8");
				res.setStatus(HttpStatus.OK.value());           
				PrintWriter out = res.getWriter();    
				try {
					JAXBContext errorContext = JAXBContext.newInstance(Confirmation.class);
					Marshaller errorMarshaller = errorContext.createMarshaller();
					errorMarshaller.marshal(confirmation, out);  
				} catch (JAXBException jex) {
					// do something
				}
			}else if (acceptString.contains("json")) {
				ObjectMapper jsonMapper = new ObjectMapper();          
				res.setContentType("application/json;charset=UTF-8");         
				res.setStatus(HttpStatus.OK.value());           
				PrintWriter out = res.getWriter();         
				out.print(jsonMapper.writeValueAsString(confirmation));       
			}
	    }
	    
	 
	void sendError (HttpServletRequest request, HttpServletResponse response, Exception authEx) throws IOException {
	    	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			Throwable mostSpecificCause = authEx.getCause();
	    
		    ErrorMessage errorMessage;
		    if (mostSpecificCause != null) {
		    	//String exceptionName = mostSpecificCause.getClass().getName();
		    	String message = mostSpecificCause.getMessage();
		        errorMessage = new ErrorMessage();
		        List<String> errors = new ArrayList<>();
		        errors.add(message);
		        errorMessage.setErrors(errors);
		    } else {
		    	errorMessage = new ErrorMessage(authEx.getMessage());
		    }
		    errorMessage.setErrorCode(ErrorCodes.INVALID_INPUT_JSON);
		    errorMessage.setStatus(HttpStatus.BAD_REQUEST.value());
			String acceptString = request.getHeader("Accept");
			if (acceptString.contains("xml")) {
				response.setContentType("application/xml;charset=UTF-8");      
				PrintWriter out = response.getWriter();    
				try {
					JAXBContext errorContext = JAXBContext.newInstance(ErrorMessage.class);
					Marshaller errorMarshaller = errorContext.createMarshaller();
					errorMarshaller.marshal(errorMessage, out);  
				} catch (JAXBException jex) {
					//do something
				}
			} else if (acceptString.contains("json")) {
				ObjectMapper jsonMapper = new ObjectMapper();          
				response.setContentType("application/json;charset=UTF-8");              
				PrintWriter out = response.getWriter();         
				out.print(jsonMapper.writeValueAsString(errorMessage));       
			} else {
				response.sendError (HttpStatus.BAD_REQUEST.value(), "Login request is not valid. Reason: " + authEx.getMessage());
			}
	    }

	
}
