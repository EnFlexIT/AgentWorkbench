package de.enflexit.oidc.ui;

import de.enflexit.awb.baseUI.options.OptionDialogExtension;
import de.enflexit.oidc.OIDCAuthorization;
import de.enflexit.oidc.OIDCSettings;

/**
 * This implementation of {@link OptionDialogExtension} adds a panel for the configuration of OpenID Connect settings to the options dialog.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OIDCSettingsOptionDialogExtension extends OptionDialogExtension {

	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.options.AwbOptionDialogExtension#initialize()
	 */
	@Override
	public void initialize() {
		OIDCSettings oidcSettings = OIDCAuthorization.getInstance().getOIDCSettings();
		this.addOptionsTab(new OIDCSettingsOptionTab(oidcSettings, null));
	}

}
