package de.enflexit.db.hibernate;

import javax.swing.ImageIcon;

import de.enflexit.db.hibernate.gui.HibernateStateVisualizer;

/**
 * The Class SessionFactoryMonitor serves as synchronization object to initialize 
 * and monitor the state of a hibernate SessionFactory.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class SessionFactoryMonitor {

	/**
	 * The enumeration SessionFactoryState.
	 */
	public enum SessionFactoryState {
		NotAvailableYet("Not available yet", "DB_State_Grey.png"),
		Destroyed("SessionFactory was destroyed", "DB_State_Grey.png"),
		CheckDBConnection("Checking database connection ...", "DB_State_Blue.png"),
		CheckDBConectionFailed("Database connection test failed!", "DB_State_Red.png"),
		InitializationProcessStarted("Initialize SessionFactory", "DB_State_Turquoise.png"),
		InitializationProcessFailed("Initialization of SessionFactory failed", "DB_State_Red.png"),
		Created("Successfully Initialized", "DB_State_Green.png");
		
		private String description;
		private String iconImageName;
		
		/**
		 * The private constructor.
		 *
		 * @param description the description
		 * @param iconImageName the icon image name
		 */
		private SessionFactoryState(String description, String iconImageName) {
			this.description = description;
			this.iconImageName = iconImageName;
		}
		/**
		 * Provides a textual description of the state.
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * Returns the icon image name.
		 * @return the icon image name
		 */
		public String getIconImageName() {
			return iconImageName;
		}
		/**
		 * Returns the icon image for the state.
		 * @return the icon image
		 */
		public ImageIcon getIconImage() {
			return HibernateStateVisualizer.getImageIcon(this.getIconImageName());
		}
	}

	
	private String factoryID;
	private SessionFactoryState sessionFactoryState;
	
	/**
	 * Instantiates a new session factory monitor.
	 */
	public SessionFactoryMonitor(String factoryID) {
		this.setFactoryID(factoryID);
		this.setSessionFactoryState(SessionFactoryState.NotAvailableYet); // the default or start value
	}

	/**
	 * Return the session factory ID.
	 * @return the factory ID
	 */
	public String getFactoryID() {
		return factoryID;
	}
	/**
	 * Sets the session factory ID.
	 * @param factoryID the new factory ID
	 */
	public void setFactoryID(String factoryID) {
		this.factoryID = factoryID;
	}

	/**
	 * Returns the session factory state.
	 * @return the session factory state
	 */
	public SessionFactoryState getSessionFactoryState() {
		return sessionFactoryState;
	}
	/**
	 * Sets the session factory state.
	 * @param sessionFactoryState the new session factory state
	 */
	public void setSessionFactoryState(SessionFactoryState sessionFactoryState) {
		this.sessionFactoryState = sessionFactoryState;
		HibernateStateVisualizer.setConnectionState(this.getFactoryID(), this.sessionFactoryState);
	}
	
}

