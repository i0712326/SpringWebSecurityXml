package com.spring.web.rest;

import org.springframework.http.ResponseEntity;

public interface HelloResource {
	public ResponseEntity<String> hello();
}
