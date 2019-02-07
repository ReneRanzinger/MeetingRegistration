package org.registration.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.registration.view.Confirmation;
import org.registration.view.ErrorCodes;
import org.registration.view.ErrorMessage;
import org.registration.persistence.dao.ConfigurationRepository;
import org.registration.security.MyUsernamePasswordAuthenticationFilter;
import org.registration.security.TokenAuthenticationFilter;
import org.registration.service.AdminDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;


@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter  {

	@Autowired
	ConfigurationRepository configRepository;
	
	@Autowired
	AdminDetailService adminService;
	
	@Value("${admin.token-secret}")
	String tokenSecret;
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.applyPermitDefaultValues();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**",config );
        return source;
    }
	
	@Bean
    public MyUsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
		
		MyUsernamePasswordAuthenticationFilter authenticationFilter
            = new MyUsernamePasswordAuthenticationFilter(this.authenticationManager());
		authenticationFilter.setAuthenticationSuccessHandler(this::loginSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(this::loginFailureHandler);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }
	
	private void loginSuccessHandler(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        Authentication authentication) throws IOException {
			response.setStatus(HttpStatus.OK.value());
			
			Confirmation confirmation = new Confirmation("User is authorized", HttpStatus.OK.value());
			String acceptString = request.getHeader("Accept");
			if (acceptString.contains("xml")) {
				response.setContentType("application/xml;charset=UTF-8");          
				PrintWriter out = response.getWriter();    
				try {
					JAXBContext errorContext = JAXBContext.newInstance(Confirmation.class);
					Marshaller errorMarshaller = errorContext.createMarshaller();
					errorMarshaller.marshal(confirmation, out);  
				} catch (JAXBException jex) {
					// do something
				}
			} else {
		        ObjectMapper jsonMapper = new ObjectMapper();          
				response.setContentType("application/json;charset=UTF-8");         
				PrintWriter out = response.getWriter();  
				out.print(jsonMapper.writeValueAsString(confirmation));   
			}
	    }
	 
	    private void loginFailureHandler(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        AuthenticationException e) throws IOException {
	    	
	    	ErrorMessage errorMessage = new ErrorMessage();
		    errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());
		    errorMessage.setErrorCode(ErrorCodes.UNAUTHORIZED);
		    
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
		        response.setStatus(HttpStatus.UNAUTHORIZED.value());
		        ObjectMapper jsonMapper = new ObjectMapper();          
				response.setContentType("application/json;charset=UTF-8");                 
				PrintWriter out = response.getWriter();   
				out.print(jsonMapper.writeValueAsString(errorMessage));  
			}
	    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		RequestMatcher PUBLIC_URLS = new OrRequestMatcher( 
				new AntPathRequestMatcher("/error"),
//				new AntPathRequestMatcher("/conference/info/**"),
//				new AntPathRequestMatcher("/conference/**"),
//				new AntPathRequestMatcher("/participant/**"),
				new AntPathRequestMatcher("/registration/**"),
				new AntPathRequestMatcher("/abstract/**"),
				new AntPathRequestMatcher("/login**"));
		
		final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
		final TokenAuthenticationFilter tokenFilter = new TokenAuthenticationFilter(adminService, PROTECTED_URLS, tokenSecret);
	//	tokenFilter.setValidators(accessTokenValidators());
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.cors().and().authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/*").permitAll()
		    .requestMatchers(PUBLIC_URLS).permitAll()
		    .anyRequest().fullyAuthenticated()
            .and()
            	.csrf().disable()
            	.httpBasic().disable()
            	.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
    	        .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.logout()
	        	.permitAll();
	}	
	
//	@Override
//    public void configure(AuthenticationManagerBuilder auth) 
//      throws Exception {
// 
//		auth.authenticationProvider(authenticationProvider);
//       
//    }
	
	
}
