package com.spring.web.security.dao.service;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.security.dao.UserDao;
import com.spring.web.security.entity.User;

@Service("userService")
public class UserServiceImp implements UserService {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	@Override
	public User getUser(String username) {
		try {
			return userDao.getUser(username);
		} catch (HibernateException | SQLException e) {
			logger.error("Exception occured while try to get user from database", e);
			return null;
		}

	}

}
