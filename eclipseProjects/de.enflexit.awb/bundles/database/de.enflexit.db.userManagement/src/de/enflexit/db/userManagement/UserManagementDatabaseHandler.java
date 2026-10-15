package de.enflexit.db.userManagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import de.enflexit.db.userManagement.dataModel.GroupRole;
import de.enflexit.db.userManagement.dataModel.User;
import de.enflexit.db.userManagement.dataModel.UserRole;
import jakarta.persistence.TypedQuery;


/**
 * The Class ExampleDatabaseHandler can be used to .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class UserManagementDatabaseHandler {
	
	private Session session;
	
	/**
	 * Instantiates a new database handler.
	 * @param session the session instance to use
	 */
	public UserManagementDatabaseHandler(Session session) {
		this.setSession(session);
	}
	
	/**
	 * Sets the current session instance.
	 * @param session the new session
	 */
	public void setSession(Session session) {
		if (this.session!=null) {
			if (session==null) {
				this.session.close();
			} else {
				if (this.session!=session) {
					this.session.close();
				}
			}
		}
		this.session = session;
	}
	/**
	 * Returns the current session.
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}
	
	/**
	 * Disposes this database handler by closing the database session.
	 */
	public void dispose() {
		this.setSession(null);
	}
	
	/**
	 * Does a transaction roll back.
	 * @param transaction the transaction
	 */
	private void doTransactionRollBack(Transaction transaction) {
		
		try {
			if (transaction!=null) {
				transaction.rollback();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			// --- Dispose session to renew handler state - 
			this.dispose();
		}
	}

	// --------------------------------------------------------------
	// --- From here, generically working on concrete data ----------
	// --------------------------------------------------------------	
	/**
	 * Saves or updates the specified {@link entityInstance}.
	 * @param entityInstance the JettySession instance to save or update
	 */
	public <EntityInstance> boolean dbSaveOrUpdateEntityInstance(EntityInstance entityInstance) {
		
		boolean successful= false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.persist(entityInstance);
				session.flush();
				transaction.commit();
				successful = true;
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
				successful = false;
				
			} finally {
				session.clear();
			}
		}
		return successful;
	}
	
	/**
	 * Loads an entity instance by its ID from the database.
	 *
	 * @param <EntityClass> the generic entity instance to load
	 * @param entityClass the entity class
	 * @param entityID the entity ID
	 * @return the entity instance found in the database
	 */
	public <EntityInstance> EntityInstance dbLoadEntityInstance(Class<EntityInstance> entityClass, String entityID) {
		
		EntityInstance siteMenu =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				siteMenu = session.get(entityClass, entityID);
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return siteMenu;
	}
	
	/**
	 * Returns the list of all entity instance in the database table .
	 *
	 * @param <EntityInstance> the generic type
	 * @param entityClass the entity class
	 * @return the JettySession list
	 */
	public <EntityInstance> List<EntityInstance> dbLoadEntityInstanceList(Class<EntityInstance> entityClass) {
		return this.dbLoadEntityInstanceList("from " + entityClass.getSimpleName(), entityClass);
	}
	/**
	 * Returns the list of all entity instance in the database table.
	 *
	 * @param <EntityInstance> the generic type
	 * @param queryString the query string
	 * @param entityClass the entity class
	 * @return the JettySession list
	 */
	public <EntityInstance> List<EntityInstance> dbLoadEntityInstanceList(String queryString, Class<EntityInstance> entityClass) {
		
		List<EntityInstance> entityInstanceList =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				
				Query<EntityInstance> query = session.createQuery(queryString, entityClass);
				entityInstanceList = query.list();
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return entityInstanceList;
	}
	
	
	/**
	 * Deletes the specified entity instance.
	 *
	 * @param <EntityInstance> the generic type
	 * @param entityInstance the entity instance
	 * @return true, if successful
	 */
	public <EntityInstance> boolean dbDeleteEntityInstance(EntityInstance entityInstance) {
		
		boolean successful = false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.remove(entityInstance);
				session.flush();
				transaction.commit();
				successful = true;
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
				successful = false;
			} finally {
				session.clear();
			}
		}
		return successful;
	}

	
	public boolean addUser(User user) {
		return this.dbSaveOrUpdateEntityInstance(user);
	}
	
	public boolean addRole(UserRole userRole) {
		return this.dbSaveOrUpdateEntityInstance(userRole);
	}
	
	
	
	/**
	 * Returns the combined backend and frontend roles for the specified user.
	 *
	 * @param userName the user name
	 * @return the user roles
	 */
	public List<String> getUserRoles(String userName) {
		
		if (userName == null || userName.isBlank() == true) {
			return new ArrayList<>();
		}
	
		Set<UserRole> backendRoles = this.getBackendUserRoles(userName);
		Set<GroupRole> frontendRoles = this.getFrontendUserRoles(userName);

		List<String> combinedRoles = new ArrayList<>();
		for (UserRole role : backendRoles) {
			combinedRoles.add(role.getRole());
		}
		for (GroupRole role : frontendRoles) {
			combinedRoles.add(role.getRole());
		}
		
		return combinedRoles;
	}
	
	/**
	 * Returns the backend user roles.
	 *
	 * @param userName the user name
	 * @return the backend user roles
	 */
	public Set<UserRole> getBackendUserRoles(String userName) {
		
		if (userName == null || userName.isBlank() == true)
			return new HashSet<>();

		Session session = this.getSession();
		if (session != null) {

			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				String sql = "SELECT uur.role,"
						+ "uur.id_user_role "
						+ "FROM  um_user uu "
						+ "INNER JOIN  um_users_in_roles uuir "
						+ "ON uu.id_user = uuir.id_user "
						+ "INNER JOIN um_user_role uur "
						+ "ON uuir.id_role = uur.id_user_role "
						+ "WHERE uu.name = :userName"; 


				NativeQuery<UserRole> query = session.createNativeQuery(sql, UserRole.class);
				query.setParameter("userName", userName);

				List<UserRole> userRoles = query.getResultList();
				transaction.commit();

				return new HashSet<>(userRoles);

			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				return new HashSet<>();

			} finally {
				session.clear();
			}
		}
		
		return new HashSet<>();
	}
	
	/**
	 * Returns all unique group roles the specified user has in any group.
	 *
	 * @param userName the user name
	 * @return the group roles for user
	 */
	public Set<GroupRole> getFrontendUserRoles(String userName) {

		if (userName == null || userName.isBlank() == true)
			return new HashSet<>();

		Session session = this.getSession();
		if (session != null) {

			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				String sql = "SELECT DISTINCT ugr.id_group_role, " 
							+ "ugr.role " 
							+ "FROM um_group_role ugr "
							+ "INNER JOIN um_user_group_roles uugr " 
							+ "ON ugr.id_group_role = uugr.id_group_role "
							+ "INNER JOIN um_user uu " 
							+ "ON uugr.id_user = uu.id_user " 
							+ "WHERE uu.name = :userName ";

				NativeQuery<GroupRole> query = session.createNativeQuery(sql, GroupRole.class);
				query.setParameter("userName", userName);

				List<GroupRole> userGroupRoles = query.getResultList();
				transaction.commit();

				return new HashSet<>(userGroupRoles);

			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				return new HashSet<>();

			} finally {
				session.clear();
			}
		}
		return new HashSet<>();
	}
	
	/**
	 * Find group roles by names.
	 *
	 * @param roleNames the role names
	 * @return the sets the
	 */
	public Set<GroupRole> findGroupRolesByNames(List<String> roleNames) {
		
		if (roleNames == null || roleNames.isEmpty() == true) {
			return new HashSet<>();
		}

		Session session = this.getSession();
		
		if (session != null) {

			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				String jpql = "SELECT gr FROM GroupRole gr WHERE gr.role IN :roleNames";

				TypedQuery<GroupRole> query = session.createQuery(jpql, GroupRole.class);
				query.setParameter("roleNames", roleNames);
				List<GroupRole> userGroupRoles = query.getResultList();
				transaction.commit();
				return new HashSet<GroupRole>(userGroupRoles);

			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				return new HashSet<>();

			} finally {
				session.clear();
			}
		}
		return new HashSet<>();
	}
	
}