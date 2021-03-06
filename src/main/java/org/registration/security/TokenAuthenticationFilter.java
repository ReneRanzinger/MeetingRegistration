package org.registration.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.registration.view.ErrorCodes;
import org.registration.view.ErrorMessage;
import org.registration.config.SecurityConstants;
import org.registration.service.AdminDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

final public class TokenAuthenticationFilter extends GenericFilterBean {

	String tokenSecret;
	
	@Autowired
	AdminDetailService adminService;
	
	@Autowired
	RequestMatcher securedEndpoints;
	
	
	public TokenAuthenticationFilter (AdminDetailService adminService, final RequestMatcher securedEndpoints, String tokenSecret) {
		this.securedEndpoints = securedEndpoints;
		this.adminService = adminService;
		this.tokenSecret = tokenSecret;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		final HttpServletRequest httpRequest = (HttpServletRequest)request;
		
		String token = httpRequest.getHeader(SecurityConstants.HEADER_STRING);
        if (token != null && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            // parse the token
        	try {
	            String user = Jwts.parser()
	                    .setSigningKey(tokenSecret.getBytes())
	                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
	                    .getBody()
	                    .getSubject();
	            
	            UserDetails userDetails = adminService.loadUserByUsername(user);
	            if (userDetails != null) {
		            final UsernamePasswordAuthenticationToken authentication =
		                    new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
		            SecurityContextHolder.getContext().setAuthentication(authentication);
	            }
        	} catch (MalformedJwtException | SignatureException e) {
        		sendError (httpRequest, (HttpServletResponse)response, e);
        		return;
        	} catch (ExpiredJwtException e) {
        		sendError (httpRequest, (HttpServletResponse)response, e);
        		return;
        	}
        	
        }
        chain.doFilter(request, response);
	}
	
	void sendError (HttpServletRequest request, HttpServletResponse response, Exception authEx) throws IOException {
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
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
	    if (authEx instanceof ExpiredJwtException) 
	    	errorMessage.setErrorCode(ErrorCodes.EXPIRED);
	    else
	    	errorMessage.setErrorCode(ErrorCodes.UNAUTHORIZED);
	    
	    errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());
		String acceptString = request.getHeader("Accept");
		if (acceptString.contains("xml")) {
			response.setContentType("application/xml;charset=UTF-8");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());           
			PrintWriter out = response.getWriter();    
			try {
				JAXBContext errorContext = JAXBContext.newInstance(ErrorMessage.class);
				Marshaller errorMarshaller = errorContext.createMarshaller();
				errorMarshaller.marshal(errorMessage, out);  
			} catch (JAXBException jex) {
				// do something
			}
 		} else if (acceptString.contains("json")) {
			ObjectMapper jsonMapper = new ObjectMapper();          
			response.setContentType("application/json;charset=UTF-8");         
			response.setStatus(HttpStatus.UNAUTHORIZED.value());           
			PrintWriter out = response.getWriter();         
			out.print(jsonMapper.writeValueAsString(errorMessage));       
		} else {
			response.sendError (HttpStatus.UNAUTHORIZED.value(), request.getUserPrincipal() + " is not allowed to access " + request.getRequestURI() + ": " + authEx.getMessage());
		}
    }
	

}