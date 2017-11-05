package com.spring.web.security.rest.user.detail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

@Service("userDetailsService")
public class UserDetailsServiceImp implements UserDetailsService {
	@Autowired
	private UserService userService;
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUser(username);
		List<GrantedAuthority> authorities = buildUserAuthority(user.getAuthorities());
		return buildUserForAuthentication(user, authorities);
	}
	private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), user.isEnabled(), true, true, true, authorities);
	}
	private List<GrantedAuthority> buildUserAuthority(Set<Authority> authorities) {
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (Authority authority : authorities) {
			setAuths.add(new SimpleGrantedAuthority(authority.getName()));
		}

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

		return Result;
	}

	
}
