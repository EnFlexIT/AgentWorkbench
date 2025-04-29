package de.enflexit.db.hibernate.gui;

import java.awt.Component;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.event.DocumentListener;

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
	
	/**
	 * This method allows to add a {@link DocumentListener} to all text fields of the panel,
	 * allowing to react on changes of the database configuration.
	 * @param documentnListener the {@link DocumentListener} to add
	 */
	public abstract void addDocumentListenerToTextFields(DocumentListener documentnListener);
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (Component comp : this.getComponents()) {
			comp.setEnabled(enabled);
		}
	}
	
}
