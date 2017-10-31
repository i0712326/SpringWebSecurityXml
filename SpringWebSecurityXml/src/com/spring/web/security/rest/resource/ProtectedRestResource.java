package com.spring.web.security.rest.resource;

import org.springframework.http.ResponseEntity;

public interface ProtectedRestResource {
	public ResponseEntity<String> hello();
	public ResponseEntity<String> admin();
}
