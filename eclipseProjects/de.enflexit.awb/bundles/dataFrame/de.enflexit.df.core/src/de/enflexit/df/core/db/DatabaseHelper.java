package de.enflexit.df.core.db;

import java.awt.Window;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.gui.DatabaseSettings;

/**
 * The Class DatabaseHelper provides helper methods to work on database connections.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseHelper {

	/**
	 * Valid database settings.
	 *
	 * @param dbSettings the DatabaseSettings to check
	 * @param showUserInformation the show user information
	 * @param ownerComponent the owner component
	 * @return true, if successful
	 */
	public static boolean providesValidDatabaseSettings(DatabaseSettings dbSettings, boolean showUserInformation, JComponent ownerComponent) {
		Window owner = OwnerDetection.getOwnerWindowForComponent(ownerComponent);
		return DatabaseHelper.providesValidDatabaseSettings(dbSettings, showUserInformation, owner);
	}
	/**
	 * Valid database settings.
	 *
	 * @param dbSettings the DatabaseSettings to check
	 * @param showUserInformation the show user information
	 * @param owner the owner
	 * @return true, if successful
	 */
	public static boolean providesValidDatabaseSettings(DatabaseSettings dbSettings, boolean showUserInformation, Window owner) {
		
		boolean validDbSettings = false;
		
		Vector<String> msgVector = new Vector<>();
		HibernateDatabaseService hds = HibernateUtilities.getDatabaseService(dbSettings.getDatabaseSystemName());
		if (hds!=null) {
			
			// --- Get the connection properties from the DatabaseSettings ----
			Properties props = dbSettings.getHibernateDatabaseSettings();
			validDbSettings = hds.isDatabaseAccessible(props, msgVector, true);
			if (validDbSettings==true) {
				if (showUserInformation==true) {
					JOptionPane.showMessageDialog(owner, "Connection test successful!", "Connection Test", JOptionPane.INFORMATION_MESSAGE);
				}
				
			} else {
				String message = "Connection test failed!";
				if (msgVector.isEmpty()==false) {
					message += "\n\n";
					for (String singelMessage : msgVector) {
						message += singelMessage + "\n";
					}
				}
				if (showUserInformation==true) {
					JOptionPane.showMessageDialog(owner, message, "Connection Test", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		}
		return validDbSettings;
	}
	
}
