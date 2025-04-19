package com.eos.admin.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eos.admin.jwt.JWTUtilsImpl;
import com.eos.admin.serviceImpl.OurUserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtilsImpl jwtUtilsImpl;

	@Autowired
	private OurUserDetailsServiceImpl ourUserDetailsServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String userEmail;

		if (authHeader == null || authHeader.isBlank()) {
			filterChain.doFilter(request, response);
			return;
		}

		jwtToken = authHeader.substring(7);
		userEmail = jwtUtilsImpl.extractUsername(jwtToken);

		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = ourUserDetailsServiceImpl.loadUserByUsername(userEmail);

			if (jwtUtilsImpl.isTokenValid(jwtToken, userDetails)) {
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				securityContext.setAuthentication(token);
				SecurityContextHolder.setContext(securityContext);
			}
		}

		filterChain.doFilter(request, response);

	}

}
