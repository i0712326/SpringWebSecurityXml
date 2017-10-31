package com.spring.web.security.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Entity;


@Entity
@Table(name="authtbl")
public class Authority implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5559170281229207759L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="authid")
	private int id;
	@Column(name="authname")
	private String name;
	@ManyToMany(mappedBy = "authorities")
	private Set<User> users = new HashSet<User>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	
}
