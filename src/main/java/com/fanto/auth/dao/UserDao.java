package com.fanto.auth.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.mindrot.jbcrypt.BCrypt;

import com.fanto.auth.entity.User;
import com.fanto.auth.entity.UserRole;

@Stateless
public class UserDao {

	@PersistenceContext(name = "simple_PU")
	private EntityManager em;

	public void add(User user) {

		if (exists(user.getLogin())) {
			throw new IllegalArgumentException("Login already exists");
		}
		
		String pwd = user.getPasssword();
		String hashed = BCrypt.hashpw(pwd,  BCrypt.gensalt());
		user.setPasssword(hashed);
		em.persist(user);
	}

	public void addUserRole(UserRole userRole) {

		if (exists(userRole)) {
			throw new IllegalArgumentException("Role already exists for that login");
		}

		em.persist(userRole);
	}

	public void delete(String login) {

		Query query = em.createQuery(UserRole.DELETE_ROLES_FOR_LOGIN);
		query.setParameter("login", login);
		query.executeUpdate();
		
		Query query2 = em.createQuery(User.DELETE_LOGIN);
		query2.setParameter("login", login);
		query2.executeUpdate();

		// nice trix   em.remove(em.contains(user) ? user : em.merge(user));

	}

	private Boolean exists(String login) {
		User user = em.find(User.class, login);
		return user != null;
	}

	private Boolean exists(UserRole userRole) {
		UserRole role = em.find(UserRole.class, userRole.getPrimaryKey());
		return role != null;
	}

}
