package com.spring.web.security.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.web.security.entity.JwtAuthenticationRequest;
import com.spring.web.security.entity.JwtAuthenticationResponse;
import com.spring.web.security.entity.JwtUser;
import com.spring.web.security.rest.jwt.JwtTokenUtil;

@Controller
@RequestMapping("/auth")
public class LoginController {
	@Value("${jwt.header}")
	private String tokenHeader;
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody JwtAuthenticationRequest request, Device device){
		// Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPasswd()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails,device);
		return new ResponseEntity<JwtAuthenticationResponse>(new JwtAuthenticationResponse(token),HttpStatus.OK);
	}
	
	@RequestMapping(value="/refresh",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<JwtAuthenticationResponse> refreshAndGetAuthenticationToken(HttpServletRequest request) {
	        String token = request.getHeader(tokenHeader);
	        String username = jwtTokenUtil.getUsernameFromToken(token);
	        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

	        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
	            String refreshedToken = jwtTokenUtil.refreshToken(token);
	            return new ResponseEntity<JwtAuthenticationResponse>(new JwtAuthenticationResponse(refreshedToken),HttpStatus.OK);
	        } else {
	            return new ResponseEntity<JwtAuthenticationResponse>(new JwtAuthenticationResponse(null),HttpStatus.BAD_REQUEST);
	        }
	    }
}
