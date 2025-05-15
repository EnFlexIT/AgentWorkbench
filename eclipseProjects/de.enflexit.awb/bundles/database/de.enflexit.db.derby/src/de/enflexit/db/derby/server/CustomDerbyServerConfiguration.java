package de.enflexit.db.derby.server;

import de.enflexit.db.hibernate.gui.CustomDatabaseConfiguration;

/**
 * The Class CustomDerbyServerConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class CustomDerbyServerConfiguration implements CustomDatabaseConfiguration {

	private CustomDerbyServerConfigurationPanel configPanel;
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.CustomDatabaseConfiguration#getName()
	 */
	@Override
	public String getName() {
		return "Derby Network Server";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.CustomDatabaseConfiguration#getUIComponent()
	 */
	@Override
	public CustomDerbyServerConfigurationPanel getUIComponent() {
		if (configPanel==null) {
			configPanel = new CustomDerbyServerConfigurationPanel();
		}
		return configPanel;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.CustomDatabaseConfiguration#disposeUIComponent()
	 */
	@Override
	public void disposeUIComponent() {
		configPanel = null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.CustomDatabaseConfiguration#hasChangedConfiguration()
	 */
	@Override
	public boolean hasChangedConfiguration() {
		DerbyNetworkServerProperties dnsConfigCurr = new DerbyNetworkServerProperties();
		DerbyNetworkServerProperties dnsConfigNew  = this.getUIComponent().getDerbyNetworkServerProperties();
		return !dnsConfigNew.equals(dnsConfigCurr);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.CustomDatabaseConfiguration#saveConfiguration()
	 */
	@Override
	public boolean saveConfiguration() {
		
		boolean success = false;
		DerbyNetworkServerProperties dnsConfigCurr = new DerbyNetworkServerProperties();
		DerbyNetworkServerProperties dnsConfigNew  = this.getUIComponent().getDerbyNetworkServerProperties();
		if (dnsConfigNew.equals(dnsConfigCurr)==false) {
			// --- Save and apply the new configuration -------------
			success = dnsConfigNew.save();
			if (dnsConfigNew.isStartDerbyNetworkServer()==true) {
				// --- Start the server with the new configuration --
				if (DerbyNetworkServer.isExecuted()==true) {
					DerbyNetworkServer.terminate();
				}
				DerbyNetworkServer.execute();
				
			} else {
				// --- If executed, stop the server------------------
				DerbyNetworkServer.terminate();
				
			}
		}
		return success;
	}

}
