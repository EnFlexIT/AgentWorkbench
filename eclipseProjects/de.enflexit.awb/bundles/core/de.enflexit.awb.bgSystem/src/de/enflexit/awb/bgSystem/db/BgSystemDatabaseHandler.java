package de.enflexit.awb.bgSystem.db;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import de.enflexit.awb.bgSystem.db.dataModel.BgSystemPlatform;


/**
 * The Class BgSystemDatabaseHandler can be used to .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class BgSystemDatabaseHandler {
	
	private Session session;
	
	/**
	 * Instantiates a new database handler.
	 */
	public BgSystemDatabaseHandler() { }
	/**
	 * Instantiates a new database handler.
	 * @param session the session instance to use
	 */
	public BgSystemDatabaseHandler(Session session) {
		this.setSession(session);
	}
	
	/**
	 * Returns the current session instance.
	 * @return the session
	 */
	public Session getSession() {
		if (session==null) {
			session = BgSystemDatabaseConnectionService.getInstance().getNewDatabaseSession();
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
	// --- From here, working on data -------------------------------
	// --------------------------------------------------------------	
	/**
	 * Saves or updates the specified {@link BgSystemPlatform}.
	 * @param bgSystemPlatform the BgSystemPlatform instance to save or update
	 */
	public boolean saveOrUpdateBgSystemPlatform(BgSystemPlatform bgSystemPlatform) {
		
		boolean successful= false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.persist(bgSystemPlatform);
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
	 * Returns the BgSystemPlatform with the specified contact agent.
	 * @param contactAgent the agents AID as String
	 * @return the BgSystemPlatform found
	 */
	public BgSystemPlatform getBgSystemPlatform(String contactAgent) {
		
		BgSystemPlatform bgSysPlatforms =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				bgSysPlatforms = session.get(BgSystemPlatform.class, contactAgent);
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return bgSysPlatforms;
	}
	
	/**
	 * Deletes the specified BgSystemPlatform.
	 *
	 * @param bgSystemPlatform the BgSystemPlatform to delete
	 * @return true, if successful
	 */
	public boolean deleteBgSystemPlatform(BgSystemPlatform bgSystemPlatform) {
		
		boolean successful = false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				session.remove(bgSystemPlatform);
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
	 * Clears the table for the BgSystemPlatform.
	 * @return the int
	 */
	public int clearBgSystemPlatformTable(){
	    
	    int noOfDeletations = -1;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				MutationQuery query = session.createMutationQuery("DELETE FROM BgSystemPlatform");
			    noOfDeletations = query.executeUpdate();
				transaction.commit();
				
			} catch (Exception ex) {
				this.doTransactionRollBack(transaction);
				ex.printStackTrace();
			} finally {
				session.clear();
			}
		}
		return noOfDeletations;
	}
	
	
	/**
	 * Returns the list of all background system platforms.
	 * @return the bg system platform list
	 */
	public List<BgSystemPlatform> getBgSystemPlatformList() {
		
		List<BgSystemPlatform> bgSysPlatformList =  null;
		Session session = this.getSession();
		if (session!=null) {
		
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				
				Query<BgSystemPlatform> query = session.createQuery("from BgSystemPlatform", BgSystemPlatform.class);
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
	 * Exchanges the list of {@link BgSystemPlatform} in the database table by the specified list <br>
	 * (which is a 'clear table' + 'add all' action).
	 * @param bgSystemPlatformList the list of BgSystemPlatform to save or update
	 */
	public boolean setBgSystemPlatformList(List<BgSystemPlatform> bgSystemPlatformList) {
		
		boolean successful= false;
		Session session = this.getSession();
		if (session!=null) {
			
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				// --- Clear the table first ------------------------
				MutationQuery query = session.createMutationQuery("DELETE FROM BgSystemPlatform");
				query.executeUpdate();
				
				// --- Save all platforms out of the list -----------  
				for (int i = 0; i < bgSystemPlatformList.size(); i++) {
					session.persist(bgSystemPlatformList.get(i));
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
