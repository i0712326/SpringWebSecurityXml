package com.spring.web.security.rest.resource;

import org.springframework.http.ResponseEntity;

public interface RestResource {
	public ResponseEntity<String> hello();
	public ResponseEntity<String> rest();
}
