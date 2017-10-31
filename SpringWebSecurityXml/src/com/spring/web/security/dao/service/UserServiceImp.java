package com.spring.web.security.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.security.dao.UserDao;
import com.spring.web.security.entity.User;

@Service("userService")
public class UserServiceImp implements UserService {
	@Autowired
	private UserDao userDao;
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	@Override
	public User getUser(String username) {
		User user = userDao.getUser(username);
		return user;
	}

}
