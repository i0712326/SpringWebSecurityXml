package com.spring.web.security.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.web.security.dao.service.UserService;
import com.spring.web.security.entity.Authority;
import com.spring.web.security.entity.User;
import com.spring.web.security.jwt.JwtUser;

@Service("userDetailsService")
public class UserDetailsServiceImp implements UserDetailsService {
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUser(username);
		List<GrantedAuthority> authorities = buildUserAuthority(user.getAuthorities());
		return buildUserDetails(user,authorities);
	}
	private List<GrantedAuthority> buildUserAuthority(List<Authority> authorities){
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		for(Authority authority:authorities){
			auths.add(new SimpleGrantedAuthority(authority.getName()));
		}
		return auths;
	}
	private UserDetails buildUserDetails(User user,List<GrantedAuthority> authorities){
		Long id = user.getId();
		String username = user.getUsername();
		String password = user.getPassword();
		String firstname = user.getFirstname();
		String lastname = user.getLastname();
		String email = user.getEmail();
		boolean enabled = user.isEnabled();
		Date lastPasswordResetDate = user.getLastPasswordResetDate();
		java.util.Date date = new java.util.Date(lastPasswordResetDate.getTime());
		JwtUser jwtUser = new JwtUser(id,username,firstname,lastname, email, password, authorities,enabled,date);
		return jwtUser;
	}
	
}
