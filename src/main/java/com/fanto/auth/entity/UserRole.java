package com.fanto.auth.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "app_user_role")

@NamedQueries({
		@NamedQuery(name = "SELECT_ROLES", query = "select ur  FROM UserRole ur where  ur.primaryKey.login=:login"), 
})
public class UserRole {

	public static final String DELETE_ROLES_FOR_LOGIN = "delete  FROM UserRole ur where  ur.primaryKey.login=:login";

	@EmbeddedId
	private UserRolePK primaryKey;

	@Column(name = "description")
	private String description;

	public UserRolePK getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(UserRolePK primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
