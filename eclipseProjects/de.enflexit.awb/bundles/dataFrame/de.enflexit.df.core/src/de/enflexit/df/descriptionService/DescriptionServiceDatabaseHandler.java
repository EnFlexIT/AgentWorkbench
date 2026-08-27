package de.enflexit.df.descriptionService;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class DescriptionServiceDatabaseHandler {
	private Session session;

	public DescriptionServiceDatabaseHandler(Session session) {
		this.setSession(session);
	}
	
	/**
	 * Saves or updates the specified {@link entityInstance}.
	 * @param entityInstance the JettySession instance to save or update
	 */
	public <EntityInstance> boolean dbSaveOrUpdateEntityInstance(EntityInstance entityInstance, boolean doUpdate) {
		
		boolean successful= false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				if (doUpdate==false) {
					session.persist(entityInstance);
				} else {
					session.merge(entityInstance);
				}
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
	
	/**
	 * Disposes this database handler by closing the database session.
	 */
	public void dispose() {
		this.setSession(null);
	}
	
	/**
	 * Returns the current session instance.
	 * @return the session
	 */
	public Session getSession() {
		return session;
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
	
}
