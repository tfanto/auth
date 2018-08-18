package com.fanto.auth.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.mindrot.jbcrypt.BCrypt;

import com.fanto.auth.entity.User;
import com.fanto.auth.entity.UserRole;
import com.fanto.auth.entity.UserRolePK;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fnt.dto.UserDto;
import com.nimbusds.jose.JOSEException;

@Stateless
public class UserService {

	@PersistenceContext(name = "simple_PU")
	private EntityManager em;

	@Inject
	LoginService loginService;

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
		userDto.setBlocked(theUser.getBlocked());
		userDto.setConfirmed(theUser.getConfirmed());
		userDto.setLastlogin(theUser.getLastlogin());
		return userDto;
	}

	public UserDto create(UserDto user) {

		User usr = new User();
		usr.setLogin(user.getLogin());
		usr.setBlocked(user.getBlocked());
		usr.setConfirmed(user.getConfirmed());
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

	// update everything but NOT password
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
		theUser.setBlocked(user.getBlocked());
		theUser.setConfirmed(user.getConfirmed());
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

	public static final String SET_PWD = "update User u set  u.password=:password where u.login=:login";

	// used from admin UI
	public Boolean update(String login, String pwd) {
		if (login == null)
			return false;
		if (pwd == null)
			return false;
		String encrypted = BCrypt.hashpw(pwd, BCrypt.gensalt());
		Query query = em.createQuery(SET_PWD);
		query.setParameter("password", encrypted);
		query.setParameter("login", login);
		int n = query.executeUpdate();
		return n == 1;
	}

	/** used from user page
	 * 
	 * @param currentUser
	 * @param login
	 * @param oldpwd
	 * @param newpwd
	 * @return
	 */
	public boolean updatePwd(String currentUser, String login, String oldpwd, String newpwd) {

		if (currentUser == null)
			return false;
		if (login == null)
			return false;
		if (!login.equals(currentUser))
			return false;
		if (oldpwd == null)
			return false;
		if (newpwd == null)
			return false;

		User theUser = em.find(User.class, login);
		if (theUser == null)
			return false;

		// here login in with oldpassword as well
		try {
			loginService.login(login, oldpwd);
		} catch (JsonProcessingException | JOSEException e) {
			return false;
		}
		theUser.setPasssword(BCrypt.hashpw(newpwd, BCrypt.gensalt()));
		em.merge(theUser);
		return true;
	}

}
