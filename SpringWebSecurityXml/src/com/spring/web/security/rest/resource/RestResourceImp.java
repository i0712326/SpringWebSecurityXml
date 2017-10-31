package com.spring.web.security.rest.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest")
public class RestResourceImp implements RestResource{
	@RequestMapping(value="/resource",method=RequestMethod.GET)
	@ResponseBody	@Override
	public ResponseEntity<String> hello(){
		String data =  "hello world restful webservice";
		return new ResponseEntity<String>(data,HttpStatus.OK);
	}

	@RequestMapping(value="/data",method=RequestMethod.GET)
	@ResponseBody	@Override
	public ResponseEntity<String> rest() {
		return new ResponseEntity<String>("Rest Resource",HttpStatus.OK);
	}
	
}
