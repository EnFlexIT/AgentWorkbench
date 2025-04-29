package de.enflexit.db.hibernate.connection;

import de.enflexit.db.hibernate.gui.DatabaseSettings;

/**
 * The Class GeneralDatabaseSettings.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class GeneralDatabaseSettings extends DatabaseSettings {

	private static final long serialVersionUID = 2687238382500762550L;

	private boolean useForEveryFactory;

	/**
	 * Sets the use for every factory.
	 * @param useForEveryFactory the new use for every factory
	 */
	public void setUseForEveryFactory(boolean useForEveryFactory) {
		this.useForEveryFactory = useForEveryFactory;
	}
	/**
	 * Checks if is use for every factory.
	 * @return true, if is use for every factory
	 */
	public boolean isUseForEveryFactory() {
		return useForEveryFactory;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.DatabaseSettings#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		boolean isEqual = super.equals(compareObject);
		if (isEqual==false) return false;
		
		if (compareObject instanceof GeneralDatabaseSettings==false) return false;
		GeneralDatabaseSettings compGDS = (GeneralDatabaseSettings) compareObject;
		if (compGDS.isUseForEveryFactory()!=this.isUseForEveryFactory()) return false;
		
		return true;
	}
	
}