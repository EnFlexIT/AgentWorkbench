package de.enflexit.db.hibernate.relocation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.SessionFactoryMonitor;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;
import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;
import de.enflexit.db.hibernate.connection.HibernateDatabaseConnectionService;

/**
 * The Class DatabaseRelocator manages to switch the database for storing 
 * the experimental data of a single AWB setup during MAS runtime.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseRelocator {

	private boolean verbose;

	private HashMap<String, Properties> tmpPropertiesHashMap;
	private HashMap<String, Properties> prevPropertiesHashMap;

	
	/**
	 * Sets the verbose flag.
	 * @param verbose the new verbose
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	/**
	 * Return the state of the verbose flag.
	 * @return true, if is verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}
	
	/**
	 * Applies the specified temporary hibernate properties within the current session factory settings.
	 *
	 * @param tmpPropertiesHashMap the temporary properties hash map to apply
	 * @param isCreateNonExistingDatabases the indicator to create not existing databases
	 * @param isDropExistingDatabase the indicator to drop and recreate existing databases
	 */
	public void applyTemporaryHibernateProperties(HashMap<String, Properties> tmpPropertiesHashMap, boolean isCreateNonExistingDatabases, boolean isDropExistingDatabase) {
		
		// --- Early exit? ----------------------
		if (tmpPropertiesHashMap==null || tmpPropertiesHashMap.size()==0) {
			this.verbosePrint("Exit because temporary properties HashMap is null or empty.", false);
			return;
		}
		
		// --- Create databases? ----------------
		if (isCreateNonExistingDatabases==true) {
			if (this.createNonExistingDatabases(tmpPropertiesHashMap, isDropExistingDatabase)==true) {
				this.verbosePrint("Successfully created required databases.", false);
			} else {
				this.verbosePrint("Exit because the creation of non-exisiting databases was NOT successful.", false);
				return;
			}
		}
		
		// --- Remind temporary settings --------
		this.tmpPropertiesHashMap  = tmpPropertiesHashMap;
		this.prevPropertiesHashMap = this.exchangePropertiesSessionFactory(this.tmpPropertiesHashMap);
		this.print("Successfully exchanged database location.", false);
	}

	/**
	 * Creates the non existing databases.
	 *
	 * @param tmpPropertiesHashMap the temporary properties hash map
	 * @param dropExistingDatabase the indicator to drop and recreate existing database
	 * @return true, if successful
	 */
	private boolean createNonExistingDatabases(HashMap<String, Properties> tmpPropertiesHashMap, boolean dropExistingDatabase) {
		
		String taskDescription = "Create database: ";
		DatabaseConnectionManager dbConnManager = DatabaseConnectionManager.getInstance();
		List<String> dbListCreated = new ArrayList<>();
		
		List<String> factoryIDList = new ArrayList<>(tmpPropertiesHashMap.keySet());
		for (String factoryID : factoryIDList) {
			// --- Get the properties to apply for the current session factory ----------
			Properties connPropsToApply = tmpPropertiesHashMap.get(factoryID);
			if (connPropsToApply==null) continue;

			String dbName = connPropsToApply.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog);
			if (dbName==null || dbName.isEmpty() || dbName.isBlank()) {
				this.verbosePrint(taskDescription + "New Database name was null or empty.", false);
				continue;
			}
			if (dbListCreated.contains(dbName)==true) {
				this.verbosePrint(taskDescription + "New Database '" + dbName + "' was already created.", false);
				continue;
			}
			
			// --- Get the connection service to use ------------------------------------
			HibernateDatabaseConnectionService hdbcs = dbConnManager.getHibernateDatabaseConnectionService(factoryID);
			if (hdbcs==null) {
				this.verbosePrint(taskDescription + "Could not find HibernateDatabaseConnectionService for factory '" + factoryID + "'.", false);
				continue;
			}

			// --- Get the original configuration ---------------------------------------
			Configuration conf = hdbcs.getConfiguration();
			dbConnManager.loadDatabaseConfigurationProperties(factoryID, conf);
			
			// --- Get a new database connection ----------------------------------------
			Connection dbConn = HibernateUtilities.getDatabaseConnection(conf, false);
			if (dbConn!=null) {
				// --- Get corresponding database service -------------------------------
				String driverClass = (String) conf.getProperties().get(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass);
				HibernateDatabaseService dbService = HibernateUtilities.getDatabaseServiceByDriverClassName(driverClass);
				if (dbService.databaseExists(dbConn, dbName)==false) {
					// --- Create the database ------------------------------------------
					if (dbService.createDatabase(dbConn, dbName)==false) {
						this.verbosePrint(taskDescription + "Creation of database '" + dbName + "' failed.", false);
						return false;
					}
					dbListCreated.add(dbName);
					this.verbosePrint(taskDescription + "Created database '" + dbName + "'.", false);
					
				} else {
					// --- Database already exists --------------------------------------
					if (dropExistingDatabase==true) {
						// --- Re-Create database ---------------------------------------
						if (dbService.dropDatabase(dbConn, dbName)==true && dbService.createDatabase(dbConn, dbName)==true) {
							dbListCreated.add(dbName);
							this.verbosePrint(taskDescription + "Recreated database '" + dbName + "'.", false);
						} else {
							this.verbosePrint(taskDescription + "Recreation of database '" + dbName + "' failed.", false);
							return false;
						}
					}
				}
				
				// --- Close DB connection ----------------------------------------------
				try {
					dbConn.close();
				} catch (SQLException sqlEx) {
					sqlEx.printStackTrace();
				}
				
			} else {
				this.verbosePrint(taskDescription + "Could not establish database connection for factory '" + factoryID + "'.", false);
			}
		}
		return true;
	}
	
	/**
	 * Restores the temporary applied hibernate properties to the old settings.
	 */
	public void restoreTemporaryHibernateProperties() {

		if (this.prevPropertiesHashMap==null || this.prevPropertiesHashMap.size()==0) return;
		
		this.exchangePropertiesSessionFactory(this.prevPropertiesHashMap);
		this.print("Restored database configuration.", false);
	}
	
	/**
	 * Checks if the temporary hibernate properties are applied.
	 *
	 * @param timeOutMillis the time out in milliseconds
	 * @return true, if the temporary hibernate properties are applied
	 */
	public boolean isAppliedTemporaryHibernateProperties(long timeOutMillis) {
		
		if (this.tmpPropertiesHashMap ==null || tmpPropertiesHashMap.size()==0) return false;
		if (this.prevPropertiesHashMap==null) return false;
		
		String taskDescription = "Check SessionFactory creation: ";
		List<String> factoryIDList = new ArrayList<>(this.tmpPropertiesHashMap.keySet());

		
		// --- Wait for the recreation of the DB connections ------------------ 
		long timeOutWork = timeOutMillis<=500 ? 500 : timeOutMillis; // As minimum: 500 ms
		long timeOutEnd  = System.currentTimeMillis() + timeOutWork;
		long sleepTime   = timeOutWork / 10;
		if (this.isVerbose()==true) {
			StringBuilder sb = new StringBuilder();
			sb.append("Time-Out specified: " + timeOutMillis + "ms, ");
			sb.append("Time-Out work: " + timeOutWork + "ms, ");
			sb.append("Sleep-Time: " + sleepTime + "ms");
			this.verbosePrint(taskDescription + sb.toString(), false);
		}
		
		int sleepCounter = 0;
		while (sleepCounter==0 || this.isPropertyAdjustmentInProgress(factoryIDList)==true) {
			
			this.verbosePrint(taskDescription + "Applied-Check => sleep counter: " + sleepCounter + ")", false);
			try {
				if (System.currentTimeMillis()>timeOutEnd) {
					this.print("=> The time out of " + timeOutWork + " milliseconds was reached to check for an established, relocated database connection!", true);
					break;
				}
				Thread.sleep(sleepTime);
				sleepCounter++;
				
			} catch (InterruptedException iEx) {
				iEx.printStackTrace();
			}
		}
		if (System.currentTimeMillis()<=timeOutEnd) {
			this.verbosePrint(taskDescription + "Exit waiting task since no further SessionFactory is in creation.", false);
		}
		
		// --- Check each state of the connections ----------------------------
		boolean isApplied = true;
		for (String factoryID : factoryIDList) {
			// --- Get the corresponding SessionFactoryMonitor ----------------
			SessionFactoryMonitor sfMonitor = HibernateUtilities.getSessionFactoryMonitor(factoryID);
			if (sfMonitor.getSessionFactoryState()!=SessionFactoryState.Created) {
				isApplied = false;
				this.verbosePrint(taskDescription + "Will return FALSE for applied-check, since SessionFactory state for '" + factoryID + "' is '" + sfMonitor.getSessionFactoryState().getDescription() + "'.", false);
				break;
			}
		}
		
		// --- Connection failed - restore previous settings ------------------
		if (isApplied==false) {
			this.restoreTemporaryHibernateProperties();
		}
		this.verbosePrint(taskDescription + "Applied-Check returns '" + isApplied + "'.", false);
		return isApplied;
	}
	
	/**
	 * Checks if the property adjustments are still in progress.
	 *
	 * @param factoryIDList the factory ID list
	 * @return true, if is property adjustment in progress
	 */
	private boolean isPropertyAdjustmentInProgress(List<String> factoryIDList) {
		for (String factoryID : factoryIDList) {
			if (HibernateUtilities.isSessionFactoryInCreation(factoryID)==true) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * Exchanges the specified properties in the corresponding {@link SessionFactory}s
	 * and will return the previously defined values.
	 *
	 * @param phmToApply the property hash map (factoryID to Properties) to apply
	 * @return the hash map with the previous settings
	 */
	private HashMap<String, Properties> exchangePropertiesSessionFactory(HashMap<String, Properties> phmToApply) {
		
		if (phmToApply==null || phmToApply.size()==0) return null;
		
		DatabaseConnectionManager dbConnManager = DatabaseConnectionManager.getInstance();		
		HashMap<String, Properties> phmPrevious = new HashMap<>();
		
		List<String> factoryIDList = new ArrayList<>(phmToApply.keySet());
		for (String factoryID : factoryIDList) {
			// --- Get the properties to apply for the current session factory ----------
			Properties connPropsToApply = phmToApply.get(factoryID);
			if (connPropsToApply==null) continue;
			
			// --- Get the connection service to use ------------------------------------
			HibernateDatabaseConnectionService hdbcs = dbConnManager.getHibernateDatabaseConnectionService(factoryID);
			if (hdbcs==null) continue;

			// --- Get the original configuration ---------------------------------------
			Configuration conf = hdbcs.getConfiguration();
			dbConnManager.loadDatabaseConfigurationProperties(factoryID, conf);
			Properties connPropsCurr = conf.getProperties();

			// --- Remind current settings ----------------------------------------------
			phmPrevious.put(factoryID, (Properties) connPropsCurr.clone());
			
			// --- Apply the temporary properties ---------------------------------------
			Set<Object> propKeyListToApply = connPropsToApply.keySet();
			for (Object propKeyToApply  : propKeyListToApply) {
				String propKey = (String) propKeyToApply;
				connPropsCurr.setProperty(propKey, connPropsToApply.getProperty(propKey));
			}
			
			// --- (Re-)Start the session factory with new settings ---------------------
			HibernateUtilities.startSessionFactoryInThread(factoryID, conf, true, false);
			
		}
		return phmPrevious;
	}

	/**
	 * Prints the specified text to the console if the verbose option was set true.
	 *
	 * @param message the message
	 * @param isError the is error
	 */
	private void verbosePrint(String message, boolean isError) {
		if (this.isVerbose()==true) {
			this.print(message, isError);
		}
	}
	
	/**
	 * Prints the specified text to the console.
	 *
	 * @param message the message
	 * @param isError the is error
	 */
	private void print(String message, boolean isError) {
		
		if (message==null || message.isEmpty()==true) return;
		
		String msg = "[" + this.getClass().getSimpleName() + "] " + message.trim();
		if (isError==true) {
			System.err.println(msg);
		} else {
			System.out.println(msg);
		}
	}
	
}
