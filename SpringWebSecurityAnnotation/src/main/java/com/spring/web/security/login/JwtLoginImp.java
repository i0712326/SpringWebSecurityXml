package com.spring.web.security.login;

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

import com.spring.web.security.jwt.JwtRequest;
import com.spring.web.security.jwt.JwtResponse;
import com.spring.web.security.jwt.JwtUser;
import com.spring.web.security.jwt.util.JwtTokenUtil;

@Controller
@RequestMapping("/auth")
public class JwtLoginImp implements JwtLogin {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody @Override
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request, Device device){
		String username = request.getUsername();
		String password = request.getPassword();
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		String token = jwtTokenUtil.generateToken(userDetails,device);
		
		return new ResponseEntity<JwtResponse>(new JwtResponse(token),HttpStatus.OK);
	}
	
	@Value("${jwt.header}")
	private String tokenHeader;
	@RequestMapping(value="/refresh",method=RequestMethod.GET)
	@ResponseBody @Override
	public ResponseEntity<JwtResponse> refresh(HttpServletRequest request){
		
		String token = request.getHeader(tokenHeader);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(username);
		
		if(jwtTokenUtil.canTokenBeRefreshed(token,jwtUser.getLastPasswordResetDate())){
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return new ResponseEntity<JwtResponse>(new JwtResponse(refreshedToken),HttpStatus.OK);
		}
		else{
			return new ResponseEntity<JwtResponse>(new JwtResponse(null),HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
}
