package com.spring.web.security.dao;

import com.spring.web.security.entity.User;

public interface UserDao {
	public User getUser(String username);
}
