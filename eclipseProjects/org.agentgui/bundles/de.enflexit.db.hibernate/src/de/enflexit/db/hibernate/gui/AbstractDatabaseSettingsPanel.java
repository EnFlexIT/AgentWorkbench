package de.enflexit.db.hibernate.gui;

import java.util.Properties;

import javax.swing.JPanel;

/**
 * The Class AbstractDatabaseSettingsPanel must be extended by database 
 * driver bundle in order to allow a visual configuration of the 
 * hibernate database connection by end user.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractDatabaseSettingsPanel extends JPanel {
	
	private static final long serialVersionUID = 6782158636214715068L;

	/**
	 * Sets the hibernate configuration properties to be visualized in the panel.
	 * 
	 * @param properties the new hibernate configuration properties
	 */
	public abstract void setHibernateConfigurationProperties(Properties properties);
	
	/**
	 * Has to return the hibernate configuration properties that were configured
	 * with the panel.
	 * 
	 * @return the hibernate configuration properties
	 */
	public abstract Properties getHibernateConfigurationProperties();
	
	
}
