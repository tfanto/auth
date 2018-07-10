package com.fanto.auth.service;

import javax.inject.Inject;

import com.fanto.auth.dao.UserDao;
import com.fanto.auth.entity.User;
import com.fanto.auth.entity.UserRole;
import com.fanto.auth.entity.UserRolePK;

public class UserService {

	@Inject
	private UserDao dao;

	public void add(String login, String password) {

		if ((login == null) || login.trim().length() < 10) {
			throw new IllegalArgumentException("Login error");
		}
		if ((password == null) || password.trim().length() < 10) {
			throw new IllegalArgumentException("Password error");
		}

		User user = new User();
		user.setLogin(login);
		user.setPasssword(password);
		dao.add(user);
	}

	public void addUserRole(String login, String role, String description) {

		if ((login == null) || login.trim().length() < 10) {
			throw new IllegalArgumentException("Login error");
		}
		if ((role == null) || role.trim().length() < 3) {
			throw new IllegalArgumentException("Role error");
		}

		UserRolePK primaryKey = new UserRolePK();
		primaryKey.setLogin(login);
		primaryKey.setRole(role);
		UserRole userRole = new UserRole();
		userRole.setPrimaryKey(primaryKey);
		userRole.setDescription(description);

		dao.addUserRole(userRole);
	}

	public void delete(String login) {
		if ((login == null) || login.trim().length() < 1) {
			throw new IllegalArgumentException("Login error");
		}
		dao.delete(login);
	}

}
