package com.spring.web.security.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Entity;

@Entity
@Table(name="usrtbl")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4887221359406887593L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="usrid")
	private int id;
	@Column(name="usrname")
	private String username;
	@Column(name="passwd")
	private String password;
	@Column(name="enable")
	private boolean enabled;
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "usrtbl_has_authtbl", 
        joinColumns = { @JoinColumn(name = "usrtbl_usrid") }, 
        inverseJoinColumns = { @JoinColumn(name = "authtbl_authid") }
    )
	private Set<Authority> authorities = new HashSet<Authority>(0);
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Set<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
}
