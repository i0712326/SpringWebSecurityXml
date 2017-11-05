package com.spring.web.security.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.web.security.entity.User;

@Repository("userDao")
public class UserDaoImp implements UserDao {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public User getUser(String username) throws HibernateException, SQLException {
		
		String hql = "from User u where u.username = :username";
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setString("username", username);
		User user = (User) query.uniqueResult();
		return user;
		
	}

}
