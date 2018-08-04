package com.fanto.auth.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.fanto.auth.dao.UserDao;
import com.fanto.auth.entity.User;
import com.fanto.auth.entity.UserRole;
import com.fanto.auth.entity.UserRolePK;
import com.fnt.dto.UserDto;

public class UserService {

	@PersistenceContext(name = "simple_PU")
	private EntityManager em;

	@Inject
	private UserDao dao;

	@Deprecated
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

	@Deprecated
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

	@Deprecated
	public void delete(String login) {
		if ((login == null) || login.trim().length() < 1) {
			throw new IllegalArgumentException("Login error");
		}
		dao.delete(login);
	}

	// NEW CODE USE em HERE we will skip DaoCrap

	public List<UserDto> getAll() {

		TypedQuery<User> qry = em.createNamedQuery(User.USER_GET_ALL, User.class);
		List<User> users = qry.getResultList();
		List<UserDto> usersDto = new ArrayList<>();
		for (User user : users) {
			usersDto.add(new UserDto(user.getLogin()));
		}
		return usersDto;
	}

	public UserDto get(String login) {
		User theUser = em.find(User.class, login);
		if (theUser == null)
			return null;
		UserDto userDto = new UserDto(theUser.getLogin());
		TypedQuery<UserRole> qry = em.createNamedQuery(UserRole.SELECT_ROLES_FOR_LOGIN, UserRole.class);
		qry.setParameter("login", login);
		List<UserRole> rs = qry.getResultList();
		for (UserRole role : rs) {
			userDto.addRole(role.getPrimaryKey().getRole());
		}
		return userDto;
	}

}
