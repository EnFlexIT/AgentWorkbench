/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.gui.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.SimpleOIDCClient;
import agentgui.core.gui.options.https.HttpsConfigWindow;

import javax.swing.JCheckBox;
import javax.swing.JTextArea;

/**
 * On this JPanel the starting options of AgentGUI can be set.
 * 
 * @author Hanno - Felix Wagner - DAWIS - ICB - University of Duisburg - Essen
 */
public class AuthOptions extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = -6953230375139010927L;
	
	private JPanel jPanelTop;
	
	private JLabel lblOIDCValues;
	private JButton bConnect;

	private List<AbstractJPanelForOptions> optionPanels;
	private JTextField tfUsername;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblLicenseServer;
	private JLabel lblIdProvider;
	private JTextField tfPassword;
	private JFormattedTextField tfLicenseServer;
	private JFormattedTextField tfIdProvider;
	private JTextField tfClientId;
	private JLabel lblClientId;
	private JTextField tfClientSecret;
	private JLabel lblClientSecret;
	
	private SimpleOIDCClient oidcClient;
	private JCheckBox cbTrustEverybody;
	private JButton btnConfigTrust;
	private JScrollPane spLog;
	private JTextArea taLog;
	private JButton btnSave;
	
	private static final String DEBUG_ISSUER_URI = "https://se238124.zim.uni-due.de:8443/auth/realms/EOMID/";
	private static final String DEBUG_RESOURCE_URI = "https://se238124.zim.uni-due.de:18443/vanilla/profile.jsp";
	private static final String DEBUG_CLIENT_ID = "testclient";
	private static final String DEBUG_CLIENT_SECRET = "b3b651a0-66a7-435e-8f1c-b1460bbfe9e0";

	/**
	 * This is the Constructor
	 */
	public AuthOptions(OptionDialog optionDialog) {
		super(optionDialog);
		
		this.initialize();
		this.setGlobalData2Form();
		
		// --- Translate ----------------------------------
		lblOIDCValues.setText(Language.translate("Open-ID-Connect-Werte"));
		bConnect.setText(Language.translate("Verbinden"));
		GridBagConstraints gbc_spLog = new GridBagConstraints();
		gbc_spLog.fill = GridBagConstraints.BOTH;
		gbc_spLog.gridx = 0;
		gbc_spLog.gridy = 1;
		add(getSpLog(), gbc_spLog);
	}
	
	private SimpleOIDCClient getOIDCCLient(){
		if(oidcClient==null){
			oidcClient= new SimpleOIDCClient();
		}
		return oidcClient;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Authorisierung");
	}
	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Authorisierung");
	}

	/**
	 * Gets the option panels.
	 * @return the option panels
	 */
	public List<AbstractJPanelForOptions> getOptionPanels() {
		if (optionPanels==null) {
			optionPanels = new ArrayList<AbstractJPanelForOptions>();
		}
		return optionPanels;
	}
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.insets = new Insets(20, 20, 5, 20);
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.weighty = 0.0;
		gridBagConstraints21.gridy = 0;
		
		this.setSize(770, 440);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 1.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		this.setLayout(gridBagLayout);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.add(this.getJPanelTop(), gridBagConstraints21);
		
	}
	
	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.gridx = 1;
			gridBagConstraints18.gridy = 0;
			gridBagConstraints18.insets = new Insets(10, 20, 0, 10);
			GridBagConstraints gbc_lblOIDCValues = new GridBagConstraints();
			gbc_lblOIDCValues.insets = new Insets(2, 0, 5, 5);
			gbc_lblOIDCValues.gridy = 0;
			gbc_lblOIDCValues.ipadx = 0;
			gbc_lblOIDCValues.anchor = GridBagConstraints.EAST;
			gbc_lblOIDCValues.weightx = 0.0;
			gbc_lblOIDCValues.gridx = 0;
			
			lblOIDCValues = new JLabel();
			lblOIDCValues.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelTop = new JPanel();
			GridBagLayout gbl_jPanelTop = new GridBagLayout();
			gbl_jPanelTop.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0};
			jPanelTop.setLayout(gbl_jPanelTop);
			jPanelTop.add(lblOIDCValues, gbc_lblOIDCValues);
			GridBagConstraints gbc_lblUsername = new GridBagConstraints();
			gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
			gbc_lblUsername.anchor = GridBagConstraints.EAST;
			gbc_lblUsername.gridx = 1;
			gbc_lblUsername.gridy = 0;
			jPanelTop.add(getLblUsername(), gbc_lblUsername);
			GridBagConstraints gbc_tfUsername = new GridBagConstraints();
			gbc_tfUsername.insets = new Insets(0, 0, 5, 5);
			gbc_tfUsername.fill = GridBagConstraints.HORIZONTAL;
			gbc_tfUsername.gridx = 2;
			gbc_tfUsername.gridy = 0;
			jPanelTop.add(getTfUsername(), gbc_tfUsername);
			GridBagConstraints gbc_btnSave = new GridBagConstraints();
			gbc_btnSave.insets = new Insets(0, 0, 5, 0);
			gbc_btnSave.gridx = 4;
			gbc_btnSave.gridy = 0;
			jPanelTop.add(getBtnSave(), gbc_btnSave);
			GridBagConstraints gbc_lblPassword = new GridBagConstraints();
			gbc_lblPassword.anchor = GridBagConstraints.EAST;
			gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
			gbc_lblPassword.gridx = 1;
			gbc_lblPassword.gridy = 1;
			jPanelTop.add(getLblPassword(), gbc_lblPassword);
			GridBagConstraints gbc_tfPassword = new GridBagConstraints();
			gbc_tfPassword.insets = new Insets(0, 0, 5, 5);
			gbc_tfPassword.fill = GridBagConstraints.HORIZONTAL;
			gbc_tfPassword.gridx = 2;
			gbc_tfPassword.gridy = 1;
			jPanelTop.add(getTfPassword(), gbc_tfPassword);
			GridBagConstraints gbc_bConnect = new GridBagConstraints();
			gbc_bConnect.anchor = GridBagConstraints.EAST;
			gbc_bConnect.gridx = 4;
			gbc_bConnect.gridy = 1;
			gbc_bConnect.weightx = 0.0;
			gbc_bConnect.insets = new Insets(0, 20, 5, 0);
			jPanelTop.add(getBConnect(), gbc_bConnect);
			GridBagConstraints gbc_lblLicenseServer = new GridBagConstraints();
			gbc_lblLicenseServer.anchor = GridBagConstraints.EAST;
			gbc_lblLicenseServer.insets = new Insets(0, 0, 5, 5);
			gbc_lblLicenseServer.gridx = 1;
			gbc_lblLicenseServer.gridy = 2;
//			jPanelTop.add(getLblLicenseServer(), gbc_lblLicenseServer);
			
			GridBagConstraints gbc_tfLicenseServer = new GridBagConstraints();
			gbc_tfLicenseServer.insets = new Insets(0, 0, 5, 5);
			gbc_tfLicenseServer.fill = GridBagConstraints.HORIZONTAL;
			gbc_tfLicenseServer.gridx = 2;
			gbc_tfLicenseServer.gridy = 2;
//			jPanelTop.add(getTfLicenseServer(), gbc_tfLicenseServer);
			GridBagConstraints gbc_cbTrustEverybody = new GridBagConstraints();
			gbc_cbTrustEverybody.insets = new Insets(0, 0, 5, 0);
			gbc_cbTrustEverybody.gridx = 4;
			gbc_cbTrustEverybody.gridy = 2;
			jPanelTop.add(getCbTrustEverybody(), gbc_cbTrustEverybody);
			GridBagConstraints gbc_lblIdProvider = new GridBagConstraints();
			gbc_lblIdProvider.anchor = GridBagConstraints.EAST;
			gbc_lblIdProvider.insets = new Insets(0, 0, 5, 5);
			gbc_lblIdProvider.gridx = 1;
			gbc_lblIdProvider.gridy = 3;
//			jPanelTop.add(getLblIdProvider(), gbc_lblIdProvider);
			GridBagConstraints gbc_tfIdProvider = new GridBagConstraints();
			gbc_tfIdProvider.insets = new Insets(0, 0, 5, 5);
			gbc_tfIdProvider.fill = GridBagConstraints.HORIZONTAL;
			gbc_tfIdProvider.gridx = 2;
			gbc_tfIdProvider.gridy = 3;
//			jPanelTop.add(getTfIdProvider(), gbc_tfIdProvider);
			GridBagConstraints gbc_btnConfigTrust = new GridBagConstraints();
			gbc_btnConfigTrust.insets = new Insets(0, 0, 5, 0);
			gbc_btnConfigTrust.gridx = 4;
			gbc_btnConfigTrust.gridy = 3;
			jPanelTop.add(getBtnConfigTrust(), gbc_btnConfigTrust);
			GridBagConstraints gbc_lblClientId = new GridBagConstraints();
			gbc_lblClientId.insets = new Insets(0, 0, 5, 5);
			gbc_lblClientId.anchor = GridBagConstraints.EAST;
			gbc_lblClientId.gridx = 1;
			gbc_lblClientId.gridy = 4;
//			jPanelTop.add(getLblClientId(), gbc_lblClientId);
			GridBagConstraints gbc_tfClientId = new GridBagConstraints();
			gbc_tfClientId.insets = new Insets(0, 0, 5, 5);
			gbc_tfClientId.fill = GridBagConstraints.HORIZONTAL;
			gbc_tfClientId.gridx = 2;
			gbc_tfClientId.gridy = 4;
//			jPanelTop.add(getTfClientId(), gbc_tfClientId);
			GridBagConstraints gbc_lblClientSecret = new GridBagConstraints();
			gbc_lblClientSecret.insets = new Insets(0, 0, 0, 5);
			gbc_lblClientSecret.anchor = GridBagConstraints.EAST;
			gbc_lblClientSecret.gridx = 1;
			gbc_lblClientSecret.gridy = 5;
//			jPanelTop.add(getLblClientSecret(), gbc_lblClientSecret);
			GridBagConstraints gbc_tfClientSecret = new GridBagConstraints();
			gbc_tfClientSecret.insets = new Insets(0, 0, 0, 5);
			gbc_tfClientSecret.fill = GridBagConstraints.HORIZONTAL;
			gbc_tfClientSecret.gridx = 2;
			gbc_tfClientSecret.gridy = 5;
//			jPanelTop.add(getTfClientSecret(), gbc_tfClientSecret);
			
		}
		return jPanelTop;
	}
	/**
	 * This method initializes jButtonApply	
	 * @return javax.swing.JButton	
	 */
	private JButton getBConnect() {
		if (bConnect == null) {
			bConnect = new JButton();
			bConnect.setFont(new Font("Dialog", Font.BOLD, 12));
			bConnect.setForeground(new Color(0, 153, 0));
			bConnect.setPreferredSize(new Dimension(100, 26));
			bConnect.setActionCommand("connectOIDC");
			bConnect.addActionListener(this);
		}
		return bConnect;
	}
	/**
	 * Resets the jPanelConfig
	 */
	private void resetJPanelConfigReset() {
	}
	/**
	 * Adds the to config panel completion.
	 */
	private void addToConfigPanelCompletion() {
	}
	
	/**
	 * This method handles and refreshes the view 
	 */
	private void refreshView() {
		
		// --- Reset the configuration panel first --------
		this.resetJPanelConfigReset();
		


		// --- Refresh view of registered option panels ---
		for (AbstractJPanelForOptions optionPanel : this.getOptionPanels()) {
			optionPanel.refreshView();
		}
		
		// --- Add completion dummy JLabel ----------------
		this.addToConfigPanelCompletion();
	}
	
	/**
	 * This method sets the Data from the global Area to the Form.
	 */
	private void setGlobalData2Form(){
		getTfUsername().setText(Application.getGlobalInfo().getOIDCUsername());
		
		this.refreshView();
	}
	
	public void setFormData2Global() {
		Application.getGlobalInfo().setOIDCUsername(getTfUsername().getText());
	}
	
	public String processURL(URL requestURL, AccessToken accessToken) throws IOException {
		String redirectionURL = "";

//		log("requestURL=");
//		log(requestURL);

		HttpURLConnection.setFollowRedirects(false);

		HttpsURLConnection conn = (HttpsURLConnection) requestURL.openConnection();
		if(getTrustEverybody()){
			SimpleOIDCClient.trustEverybody(conn);
		}
		conn.setRequestMethod("GET");
		if (accessToken != null) {
			conn.setRequestProperty("Authorization", "bearer " + accessToken);
		}

		conn.connect();

		int responseCode = conn.getResponseCode();

		if (responseCode == 302) {
			redirectionURL = conn.getHeaderField("Location");
//			log("redirection to:");
//			log(redirectionURL);
			return redirectionURL;
		} else if (responseCode == 400) {
			log("400: General Error");
			return null;
		} else if (responseCode == 500) {
			log("500");
			return null;
		} else if (responseCode == 200) { //
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				log(inputLine);
			}
			in.close();
			return null;
		} else {
			log("responseCode =" + responseCode);
			return null;
		}
	}
	
	
	// Optional: UserInfo request
	// Alternatively: pass Access Token on to another client, use it to access a resource there
	public String doResourceAccess(AccessToken accessToken) throws ParseException, IOException {
		if (accessToken != null) {
			getOIDCCLient().dumpTokenInfo();
			// only for debugging
			oidcClient.requestUserInfo();
			log("UserInfoJSON:");
			log(oidcClient.getUserInfoJSON()+"");
		}
		return processURL(oidcClient.getRedirectURI().toURL(), accessToken);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		String actCMD = ae.getActionCommand();
	
		if (actCMD.equalsIgnoreCase("connectOIDC")) {
			try {				
				String authRedirection = "";
				AccessToken accessToken = null;
				
				SimpleOIDCClient oidcClient = getOIDCCLient();
				
				if(getTrustEverybody()){
					SimpleOIDCClient.trustEverybody(null);
				}
				
				oidcClient.setIssuerURI(getIssuerURI());
				oidcClient.retrieveProviderMetadata();
				oidcClient.setClientMetadata(getResourceURI());
				oidcClient.setClientID(getClientId(), getClientSecret());
				oidcClient.setRedirectURI(getResourceURI());
				
				
				log("try a direct access to the resource (EOM licenseer)");
				authRedirection = doResourceAccess(accessToken);

				if (authRedirection == null) { 	// no authentication required (or already authenticated?)
					log("resource available");
					return;
				}

				log("authentication redirection neccessary");
				
//				log("parse authentication parameters from redirection");
				oidcClient.parseAuthenticationDataFromRedirect(authRedirection, false); // don't override clientID
//				log("set USER credentials");
				oidcClient.setResourceOwnerCredentials(getTfUsername().getText(), getTfPassword().getText());

				oidcClient.requestToken();
				accessToken = oidcClient.getAccessToken();

				log("access the resource (licenseer) again, this time sending an access token");
				authRedirection = doResourceAccess(accessToken);
				if (authRedirection == null) { 	// no authentication required (or already authenticated?)
					log("resource available");
					log("the logged in resource should be shown");
					return;
				} else {
					log("Something went awfully wrong");
					return;
				}
				
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.refreshView();	
//			this.setGlobalData2Form();
			
		} else if (actCMD.equalsIgnoreCase("save")) {
			setFormData2Global();
		} else if (actCMD.equalsIgnoreCase("configureTrust")) {
			// --- Open the HttpsConfigWindow ----------------------------------
//			HttpsConfigWindow httpsConfigWindow = new HttpsConfigWindow(this.optionDialog, getKeyStore(), getKeyStorePassword(), getTrustStore(), getTrustStorePassword());
			HttpsConfigWindow httpsConfigWindow = new HttpsConfigWindow(this.optionDialog);

			// --- Wait for the user -------------------------------------------
			if (httpsConfigWindow.isCanceled() == false) {
				// ---- Return the TrustStore chosen by the user --
//				this.setTrustStore(httpsConfigWindow.getTrustStorefilepath());
//				this.setTrustStorePassword(httpsConfigWindow.getTrustStorePassword());
//				this.getJTextFieldTrustStorePath().setText(this.getTrustStore());
			} 
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actCMD);
		}
	}
	
	public void log(String s) {
		   try {
		      Document doc = getTaLog().getDocument();
		      doc.insertString(doc.getLength(), s+"\n", null);
		   } catch(BadLocationException exc) {
		      exc.printStackTrace();
		   }
		}
	
	private JTextField getTfUsername() {
		if (tfUsername == null) {
			tfUsername = new JTextField();
			tfUsername.setColumns(10);
		}
		return tfUsername;
	}
	private JLabel getLblUsername() {
		if (lblUsername == null) {
			lblUsername = new JLabel(Language.translate("Benutzername"));
		}
		return lblUsername;
	}
	private JLabel getLblPassword() {
		if (lblPassword == null) {
			lblPassword = new JLabel(Language.translate("Passwort"));
		}
		return lblPassword;
	}
	private JLabel getLblLicenseServer() {
		if (lblLicenseServer == null) {
			lblLicenseServer = new JLabel(Language.translate("Lizenz-Server"));
		}
		return lblLicenseServer;
	}
	private JLabel getLblIdProvider() {
		if (lblIdProvider == null) {
			lblIdProvider = new JLabel(Language.translate("ID-Provider"));
		}
		return lblIdProvider;
	}
	private JTextField getTfPassword() {
		if (tfPassword == null) {
			tfPassword = new JTextField();
//			tfPassword.setText("test");
			tfPassword.setColumns(10);
		}
		return tfPassword;
	}
	private JFormattedTextField getTfLicenseServer() {
		if (tfLicenseServer == null) {
			tfLicenseServer = new JFormattedTextField();
			tfLicenseServer.setText(DEBUG_RESOURCE_URI);
		}
		return tfLicenseServer;
	}
	private JFormattedTextField getTfIdProvider() {
		if (tfIdProvider == null) {
			tfIdProvider = new JFormattedTextField();
			tfIdProvider.setText(DEBUG_ISSUER_URI);
		}
		return tfIdProvider;
	}
	private JTextField getTfClientId() {
		if (tfClientId == null) {
			tfClientId = new JTextField();
			tfClientId.setText(DEBUG_CLIENT_ID);
			tfClientId.setColumns(10);
		}
		return tfClientId;
	}
	private JLabel getLblClientId() {
		if (lblClientId == null) {
			lblClientId = new JLabel(Language.translate("Client-ID"));
		}
		return lblClientId;
	}
	private JTextField getTfClientSecret() {
		if (tfClientSecret == null) {
			tfClientSecret = new JTextField();
			tfClientSecret.setText(DEBUG_CLIENT_SECRET);
			tfClientSecret.setColumns(10);
		}
		return tfClientSecret;
	}
	private JLabel getLblClientSecret() {
		if (lblClientSecret == null) {
			lblClientSecret = new JLabel(Language.translate("Client-Secret"));
		}
		return lblClientSecret;
	}
	private JCheckBox getCbTrustEverybody() {
		if (cbTrustEverybody == null) {
			cbTrustEverybody = new JCheckBox(Language.translate("Jedem vertrauen"));
		}
		return cbTrustEverybody;
	}
	private JButton getBtnConfigTrust() {
		if (btnConfigTrust == null) {
			btnConfigTrust = new JButton(Language.translate("Vertrauen konfigurieren"));
			btnConfigTrust.setActionCommand("configureTrust");
			btnConfigTrust.addActionListener(this);
		}
		return btnConfigTrust;
	}
	private JScrollPane getSpLog() {
		if (spLog == null) {
			spLog = new JScrollPane();
			spLog.setColumnHeaderView(getTaLog());
		}
		return spLog;
	}
	private JTextArea getTaLog() {
		if (taLog == null) {
			taLog = new JTextArea();
		}
		return taLog;
	}
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton(Language.translate("Speichern"));
			btnSave.setActionCommand("save");
			btnSave.addActionListener(this);
		}
		return btnSave;
	}
	
	private String getIssuerURI(){
//		return getTfIdProvider().getText();
		return DEBUG_ISSUER_URI;
	}
	
	private String getResourceURI(){
//		return getTfLicenseServer().getText();
		return DEBUG_RESOURCE_URI;
	}
	
	private String getClientId(){
//		return getTfClientId().getText();
		return DEBUG_CLIENT_ID;
	}
	
	private String getClientSecret(){
//		return getTfClientSecret().getText();
		return DEBUG_CLIENT_SECRET;
	}
		
	private boolean getTrustEverybody(){
		return getCbTrustEverybody().isSelected();
	}
}  //  @jve:decl-index=0:visual-constraint="-3,8"
