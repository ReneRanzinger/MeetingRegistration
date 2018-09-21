package org.registration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter  {

	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		RequestMatcher PUBLIC_URLS = new OrRequestMatcher( 
				new AntPathRequestMatcher("/error"),
				new AntPathRequestMatcher("/conference/info/**"),
				new AntPathRequestMatcher("/registration/**"),
				new AntPathRequestMatcher("/admin/**"));
		
		http.cors().and().authorizeRequests()
		    .requestMatchers(PUBLIC_URLS).permitAll()
		    .anyRequest().fullyAuthenticated()
            .and().csrf().disable()
            .httpBasic().disable();
		
	}	
}
