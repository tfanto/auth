package com.fanto.auth.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.mindrot.jbcrypt.BCrypt;

import com.fanto.auth.entity.User;
import com.fanto.auth.entity.UserRole;
import com.fanto.auth.entity.UserRolePK;
import com.fnt.dto.UserDto;

@Stateless
public class UserService {

	@PersistenceContext(name = "simple_PU")
	private EntityManager em;

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

	public UserDto create(UserDto user) {

		User usr = new User();
		usr.setLogin(user.getLogin());
		
		String pwd = user.getLogin();
		usr.setPasssword(BCrypt.hashpw(pwd, BCrypt.gensalt()));

		em.persist(usr);
		for (String role : user.getRoles()) {
			UserRole userRole = new UserRole();
			UserRolePK primaryKey = new UserRolePK();
			primaryKey.setLogin(user.getLogin());
			primaryKey.setRole(role);
			userRole.setPrimaryKey(primaryKey);
			userRole.setDescription("Description for " + user.getLogin());
			em.persist(userRole);
		}
		return user;
	}

	public UserDto update(UserDto user) {
		User theUser = em.find(User.class, user.getLogin());
		if (theUser == null) {
			return user;
		}

		Query query = em.createQuery(UserRole.DELETE_ROLES_FOR_LOGIN);
		query.setParameter("login", user.getLogin());
		query.executeUpdate();

		for (String role : user.getRoles()) {
			UserRole userRole = new UserRole();
			UserRolePK primaryKey = new UserRolePK();
			primaryKey.setLogin(user.getLogin());
			primaryKey.setRole(role);
			userRole.setPrimaryKey(primaryKey);
			userRole.setDescription("Description for " + user.getLogin());
			em.persist(userRole);
		}
		em.merge(theUser);
		return user;

	}

	public void delete(String login) {

		Query query = em.createQuery(UserRole.DELETE_ROLES_FOR_LOGIN);
		query.setParameter("login", login);
		query.executeUpdate();

		Query query2 = em.createQuery(User.DELETE_LOGIN);
		query2.setParameter("login", login);
		query2.executeUpdate();

		// nice trix em.remove(em.contains(user) ? user : em.merge(user));

	}

}
