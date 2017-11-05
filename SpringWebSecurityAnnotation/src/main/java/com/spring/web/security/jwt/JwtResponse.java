package com.spring.web.security.jwt;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -464848290609746021L;
	private String token;
	public JwtResponse(){
		super();
	}
	public JwtResponse(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
