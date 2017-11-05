package com.spring.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/protected")
public class HelloResourceImp {

	@RequestMapping(value="/resource",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> hello() {
		return new ResponseEntity<String>("Hello World",HttpStatus.OK);
	}

}
