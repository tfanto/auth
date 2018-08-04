package com.fanto.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "app_user")
@NamedQueries({ @NamedQuery(name = User.USER_GET_ALL, query = "SELECT c FROM User c"), })
public class User {

	public static final String USER_GET_ALL = "user.getall";

	public static final String DELETE_LOGIN = "delete  FROM User u where  u.login=:login";
	public static final String SELECT_HASH = "select u  FROM User u where  u.login=:login";

	@Id
	@Column(name = "login")
	private String login;

	@Column(name = "password")
	private String passsword;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasssword() {
		return passsword;
	}

	public void setPasssword(String passsword) {
		this.passsword = passsword;
	}

}
