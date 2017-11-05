package com.spring.web.security.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;

import com.spring.web.security.entity.User;

public interface UserDao {
	public User getUser(final String user) throws HibernateException, SQLException;
}
