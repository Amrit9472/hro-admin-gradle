package com.eos.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.eos.admin.serviceImpl.OurUserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	

	private OurUserDetailsServiceImpl ourUserDetailsServiceImpl;
	private JWTAuthFilter jwtAuthFilter;
	
	
	public SecurityConfig(OurUserDetailsServiceImpl ourUserDetailsServiceImpl, JWTAuthFilter jwtAuthFilter) {
		super();
		this.ourUserDetailsServiceImpl = ourUserDetailsServiceImpl;
		this.jwtAuthFilter = jwtAuthFilter;
	}

	private static final String ROLE_ADMIN = "ADMIN";
	private static final String ROLE_USER = "USER";
	private static final String ROLE_MANAGER = "MANAGER";
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(AbstractHttpConfigurer::disable)
		.cors(Customizer.withDefaults())
		.authorizeHttpRequests(Request -> Request.requestMatchers("/auth/**", "/public/**").permitAll().
				requestMatchers("/api/employees/createEmployee").permitAll()
				.requestMatchers("/admin/**").hasAnyAuthority(ROLE_ADMIN)
				.requestMatchers("/user/**").hasAnyAuthority(ROLE_USER,ROLE_MANAGER)
				.requestMatchers("/api/employees/**").hasAnyAuthority(ROLE_USER,ROLE_ADMIN,ROLE_MANAGER)
				.requestMatchers("/api/loi/**").hasAnyAuthority(ROLE_USER,ROLE_ADMIN,ROLE_MANAGER)
				.requestMatchers("/adminuser/**").hasAnyAuthority(ROLE_USER,ROLE_ADMIN,ROLE_MANAGER)
				.anyRequest().authenticated())
		.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider())
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	
	}


	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(ourUserDetailsServiceImpl);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
}
