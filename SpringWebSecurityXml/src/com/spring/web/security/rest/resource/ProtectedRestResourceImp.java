package com.spring.web.security.rest.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/protected")
public class ProtectedRestResourceImp implements ProtectedRestResource {
	
	@RequestMapping(value="/user/resource", method=RequestMethod.GET)
	@ResponseBody @Override
	public ResponseEntity<String> hello() {
		return new ResponseEntity<String>("protected data", HttpStatus.OK);
	}
	
	@RequestMapping(value="/admin/resource", method=RequestMethod.GET)
	@ResponseBody @Override
	public ResponseEntity<String> admin() {
		return new ResponseEntity<String>("protected admin data", HttpStatus.OK);
	}

	
}
