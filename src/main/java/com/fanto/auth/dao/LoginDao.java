package com.fanto.auth.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mindrot.jbcrypt.BCrypt;

import com.fanto.auth.entity.User;
import com.fanto.auth.entity.UserRole;

@Stateless
public class LoginDao {

	private static final String DEFAULT_ROLE = "GUEST";

	@PersistenceContext
	private EntityManager em;

	public List<String> login(String login, String password) {

		TypedQuery<User> query = em.createNamedQuery(User.USER_SELECT_HASH, User.class);
		query.setParameter("login", login);
		try {
			User user = (User) query.getSingleResult();

			String hashed = user.getPasssword();

			if (!BCrypt.checkpw(password, hashed)) {
				throw new IllegalArgumentException("Invalid login");
			}

			List<String> roles = new ArrayList<>();
			TypedQuery<UserRole> roles_query = em.createNamedQuery(UserRole.SELECT_ROLES_FOR_LOGIN, UserRole.class);
			roles_query.setParameter("login", login);
			List<UserRole> rs = roles_query.getResultList();

			if (rs.isEmpty()) {
				roles.add(DEFAULT_ROLE);
			} else {
				for (UserRole r : rs) {
					roles.add(r.getPrimaryKey().getRole());
				}
			}

			return roles;
		} catch (NoResultException e) {
			throw new IllegalArgumentException("Invalid login");
		}

	}

}
