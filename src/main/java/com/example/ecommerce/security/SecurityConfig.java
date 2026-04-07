package com.example.ecommerce.security;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
@Configuration
public class SecurityConfig 
{
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Value("${app.cors.allowed-origins}")
	private String allowedOrigins;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter)	{
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.csrf(csrf -> csrf.disable())
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
				.requestMatchers("/api/cart/**", "/api/orders/**", "/api/payment/**").authenticated()
				.anyRequest().authenticated());
		
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
		
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception
	{
		return authConfig.getAuthenticationManager();
	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource()
	{
		CorsConfiguration configuration = new CorsConfiguration();
		
		List<String> origins= Arrays.asList(allowedOrigins.split(","));
		origins = origins.stream().map(String::trim).toList();
		
		if (origins.contains("*")) {
	        throw new IllegalArgumentException(
	            "CORS misconfiguration: '*' is not allowed when allowCredentials=true. " +
	            "Please specify explicit origins in application.properties"
	        );
	    }

		configuration.setAllowedOrigins(origins);
		configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));
		configuration.setAllowCredentials(false);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
		
	}
}