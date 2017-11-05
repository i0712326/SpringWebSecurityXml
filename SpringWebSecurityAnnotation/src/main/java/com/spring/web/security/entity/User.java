package com.spring.web.security.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="usrtbl")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5505957207237694947L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private long id;
	@Column(name="USERNAME", unique=true,nullable=false)
	private String username;
	@Column(name="FIRSTNAME")
	private String firstname;
	@Column(name="LASTNAME")
	private String lastname;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="EMAIL")
	private String email;
	@Column(name="ENABLED")
	private boolean enabled;
	@Column(name="LASTPASSWORDRESETDATE")
	private Date lastPasswordResetDate;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(
			name = "usrtbl_has_authtbl", 
	        joinColumns = { @JoinColumn(name = "USRTBL_ID") }, 
	        inverseJoinColumns = { @JoinColumn(name = "AUTHTBL_ID") }
	)
	private List<Authority> authorities;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public List<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}
	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}
	
}
