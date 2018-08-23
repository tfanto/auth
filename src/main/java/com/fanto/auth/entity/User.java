package com.fanto.auth.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "app_user", indexes = { @Index(columnList = "login", name = "app_user00", unique = true), })
@NamedQueries({ @NamedQuery(name = User.USER_GET_ALL, query = "SELECT c FROM User c"), })
public class User {

	public static final String USER_GET_ALL = "user.getall";

	public static final String DELETE_LOGIN = "delete  FROM User u where  u.login=:login";
	public static final String SELECT_HASH = "select u  FROM User u where  u.login=:login";

	@PreUpdate
	@PrePersist
	public void preStored() {
		this.lastlogin = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	@Id
	@Column(name = "login")
	@Email(message="Not a valid email format")
	private String login;

	@Column(name = "password")
	@NotNull
	private String passsword;

	@Column(name = "lastlogin")
	private ZonedDateTime lastlogin;

	@Column(name = "confirmed")
	private Boolean confirmed;

	@Column(name = "blocked")
	private Boolean blocked;

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

	public ZonedDateTime getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(ZonedDateTime lastlogin) {
		this.lastlogin = lastlogin;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blocked == null) ? 0 : blocked.hashCode());
		result = prime * result + ((confirmed == null) ? 0 : confirmed.hashCode());
		result = prime * result + ((lastlogin == null) ? 0 : lastlogin.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((passsword == null) ? 0 : passsword.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (blocked == null) {
			if (other.blocked != null)
				return false;
		} else if (!blocked.equals(other.blocked))
			return false;
		if (confirmed == null) {
			if (other.confirmed != null)
				return false;
		} else if (!confirmed.equals(other.confirmed))
			return false;
		if (lastlogin == null) {
			if (other.lastlogin != null)
				return false;
		} else if (!lastlogin.equals(other.lastlogin))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (passsword == null) {
			if (other.passsword != null)
				return false;
		} else if (!passsword.equals(other.passsword))
			return false;
		return true;
	}

}
