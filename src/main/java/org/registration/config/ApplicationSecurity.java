package org.registration.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.registration.persistence.dao.ConfigurationRepository;
import org.registration.security.MyUsernamePasswordAuthenticationFilter;
import org.registration.view.Confirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.applyPermitDefaultValues();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        source.registerCorsConfiguration("/**",config );
        return source;
    }
	
	@Bean
    public MyUsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
		
		MyUsernamePasswordAuthenticationFilter authenticationFilter
            = new MyUsernamePasswordAuthenticationFilter();
      //  authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		RequestMatcher PUBLIC_URLS = new OrRequestMatcher( 
				new AntPathRequestMatcher("/error"),
				new AntPathRequestMatcher("/conference/info/**"),
				new AntPathRequestMatcher("/conference/**"),
				new AntPathRequestMatcher("/participant/**"),
				new AntPathRequestMatcher("/registration/**"),
				new AntPathRequestMatcher("/abstract/**"),
				new AntPathRequestMatcher("/login**"));
		
		http.cors().and().authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/*").permitAll()
		    .requestMatchers(PUBLIC_URLS).permitAll()
		    .anyRequest().fullyAuthenticated()
            .and()
            	.csrf().disable()
            	.httpBasic().disable()
            	.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.logout()
	        	.permitAll();
	}	
	
//	@Override
//    public void configure(AuthenticationManagerBuilder auth) 
//      throws Exception {
// 
//        auth.inMemoryAuthentication()
//            .withUser(configRepository.findByName("admin.username").getValue())
//            .password(configRepository.findByName("admin.password").getValue())
//            .roles("ADMIN");
//    }
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("admin").password("password").roles("ADMIN").build());
		return manager;
		
	}
	
}
