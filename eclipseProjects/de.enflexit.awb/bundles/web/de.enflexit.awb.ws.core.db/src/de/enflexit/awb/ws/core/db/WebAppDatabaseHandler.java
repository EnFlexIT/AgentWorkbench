package de.enflexit.awb.ws.core.db;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import de.enflexit.awb.ws.core.db.dataModel.JettySession;


/**
 * The Class WebAppDatabaseHandler can be used to .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class WebAppDatabaseHandler {
	
	private Session session;
	
	/**
	 * Instantiates a new database handler.
	 */
	public WebAppDatabaseHandler() { }
	/**
	 * Instantiates a new database handler.
	 * @param session the session instance to use
	 */
	public WebAppDatabaseHandler(Session session) {
		this.setSession(session);
	}
	
	/**
	 * Returns the current session instance.
	 * @return the session
	 */
	public Session getSession() {
		if (session==null) {
			session = WebAppDatabaseConnectionService.getInstance().getNewDatabaseSession();
		}
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
	
	
	// --------------------------------------------------------------
	// --- From here, working on concrete data ----------------------
	// --------------------------------------------------------------	
	/**
	 * Saves or updates the specified {@link BgSystemPlatform}.
	 * @param jettySession the JettySession instance to save or update
	 */
	public boolean saveOrUpdateJettySession(JettySession jettySession) {
		
		boolean successful= false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.persist(jettySession);
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
	 * Returns the JettySession with the specified contact agent.
	 * @param sessionId the session Id as String
	 * @return the JettySession found
	 */
	public JettySession getJettySession(String sessionId) {
		
		JettySession jettySession =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				jettySession = session.get(JettySession.class, sessionId);
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return jettySession;
	}
	
	/**
	 * Deletes the specified JettySession.
	 *
	 * @param jettySession the JettySession to delete
	 * @return true, if successful
	 */
	public boolean deleteJettySession(JettySession jettySession) {
		
		boolean successful = false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.remove(jettySession);
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
	 * Returns the list of all background system platforms.
	 * @return the JettySession list
	 */
	public List<JettySession> getJettySessionList() {
		
		List<JettySession> bgSysPlatformList =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				
				Query<JettySession> query = session.createQuery("from JettySessions", JettySession.class);
				bgSysPlatformList = query.list();
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return bgSysPlatformList;
	}
	
	/**
	 * Exchanges the list of {@link JettySession} in the database table by the specified list <br>
	 * (which is a 'clear table' + 'add all' action).
	 *
	 * @param jettySessionList the jetty session list
	 * @return true, if successful
	 */
	public boolean setJettySessionList(List<JettySession> jettySessionList) {
		
		boolean successful= false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				// --- Clear the table first ------------------------
				MutationQuery query = session.createMutationQuery("DELETE FROM JettySessions");
				query.executeUpdate();
				
				// --- Save all platforms out of the list -----------  
				for (int i = 0; i < jettySessionList.size(); i++) {
					session.persist(jettySessionList.get(i));
			        if (i % 100 == 0) {
			            session.flush();
			            session.clear();
			        }
				}
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
}
