package com.mbt.usermanagement.service;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class defines the filter applied to {@code /api/secure/*}.
 *
 */

public class JWTFilter extends AbstractAuthenticationProcessingFilter {

	private AuthorityService authorityService;
	private JWTService jwtTokenService;
	private String authHeader;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public JWTFilter(String defaultFilterProcessesUrl, JWTService jwtTokenService, String authHeader,
			AuthorityService authorityService) {
		super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
		this.jwtTokenService = jwtTokenService;
		this.authHeader = authHeader;
		this.authorityService = authorityService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		final String authHeaderVal = httpRequest.getHeader(authHeader);


		if (authHeaderVal == null) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		Authentication authentication = null;
		try {
			authentication = jwtTokenService.getAuthorization(authHeaderVal);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		if (authentication == null) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		try {
			if (jwtTokenService.getAuthorization(authHeaderVal) == null) {
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (JwtException e) {
			httpResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		if (!authorityService.hasAuthority(authentication.getName(), httpRequest.getRequestURI(),
				httpRequest.getMethod())) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		chain.doFilter(httpRequest, httpResponse);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws AuthenticationException, IOException {
		return null;
	}
}