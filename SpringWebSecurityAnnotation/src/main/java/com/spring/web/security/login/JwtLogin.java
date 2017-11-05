package com.spring.web.security.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;

import com.spring.web.security.jwt.JwtRequest;
import com.spring.web.security.jwt.JwtResponse;

public interface JwtLogin {
	public ResponseEntity<JwtResponse> login(JwtRequest request,Device device);
	public ResponseEntity<JwtResponse> refresh(HttpServletRequest request);
}
