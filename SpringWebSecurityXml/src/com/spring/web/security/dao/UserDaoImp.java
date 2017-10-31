package com.spring.web.security.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.web.security.entity.User;

@Repository("userDao")
public class UserDaoImp implements UserDao {
	private HibernateTemplate hibernateTemplate;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}
	@Transactional(propagation=Propagation.SUPPORTS) @Override
	public User getUser(final String username) {
		return hibernateTemplate.execute(new HibernateCallback<User>(){

			@Override
			public User doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "from User u where u.username = :username";
				Query query = session.createQuery(hql);
				query.setString("username", username);
				return (User) query.uniqueResult();
			}
			
		});
	}

}
