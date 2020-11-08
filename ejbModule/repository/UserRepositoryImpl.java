package repository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import repository.entity.Customer;
import repository.entity.Group;
import repository.entity.User;
import repository.utils.AuthenticationUtils;

@Stateless
public class UserRepositoryImpl implements UserRepository {
	@PersistenceContext(unitName = "Assignment-ejbPU")
	private EntityManager entityManager;

	@Override
	public User findUserByUsername(String username) {
		TypedQuery<User> query = entityManager.createNamedQuery(User.GET_BY_USERNAME, User.class);
		query.setParameter("username", username);
		User user = null;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			// getSingleResult throws NoResultException in case there is no user in DB
			// ignore exception and return NULL for user instead
		}
		return user;
	}

	@Override
	public User findUserByCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createUser(User user) {
		try {
			user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}
		Group group = new Group();
		group.setUsername(user.getUsername());
		group.setGroupname(Group.NORMAL_USER);
		entityManager.persist(user);
		entityManager.persist(group);

		return user;
	}

	@Override
	public User changePassword(User user) {
		try {
			user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		}
		entityManager.merge(user);
		return user;
	}

	@Override
	public List<User> findAllUsers() {
		return entityManager.createNamedQuery(User.GET_ALL).getResultList();
	}

	@Override
	public String findUserGroup(String username) {
		Query query = entityManager.createNamedQuery(User.GET_GROUP);
		query.setParameter("username", username);
		return (String) query.getSingleResult();
	}

	@Override
	public void changeUserGroup(User user) {

		Query query = entityManager.createNamedQuery(Group.GET_BY_USERNAME);
		query.setParameter("username", user.getUsername());
		Group userGroup = (Group) query.getSingleResult();

		if (userGroup.getGroupname().equals("admin")) {
			userGroup.setGroupname("normal");
		} else {
			userGroup.setGroupname("admin");
		}
		
		entityManager.merge(userGroup);
	}
	
	@Override
	public void removeUserGroup(User user) {
		Query query = entityManager.createNamedQuery(Group.GET_BY_USERNAME);
		query.setParameter("username", user.getUsername());
		Group userGroup = (Group) query.getSingleResult();
		entityManager.remove(userGroup);
		
	}
	
	@Override
	public void removeUser(String username) {
		User user = this.findUserByUsername(username);
		if (user != null) {
			entityManager.remove(user);
		}
		
	}

}
