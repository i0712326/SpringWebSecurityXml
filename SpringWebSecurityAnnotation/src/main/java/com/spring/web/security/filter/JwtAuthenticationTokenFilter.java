package com.spring.web.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.web.security.jwt.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;


public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
	private Logger logger = Logger.getLogger(this.getClass());
	@Value("${jwt.header}")
	private String tokenHeader;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestHeader = request.getHeader(tokenHeader);
		
		String username = null;
		String authToken = null;

		if (requestHeader != null) {
			if (requestHeader.startsWith("Bearer ")) {
				authToken = requestHeader.substring(7);
				try {
					username = jwtTokenUtil.getUsernameFromToken(authToken);
				} catch (IllegalArgumentException e) {
					logger.error("an error occured during getting username from token", e);
				} catch (ExpiredJwtException e) {
					logger.warn("the token is expired and not valid anymore", e);
				}

			} else {
				logger.warn("could identify request header");
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (jwtTokenUtil.validateToken(authToken, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

}
