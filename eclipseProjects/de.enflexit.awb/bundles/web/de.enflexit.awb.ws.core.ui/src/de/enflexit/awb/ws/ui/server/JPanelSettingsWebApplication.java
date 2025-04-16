package de.enflexit.awb.ws.ui.server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings.UpdateStrategy;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;
import de.enflexit.awb.ws.core.model.ServerTreeNodeWebAppSettings;
import de.enflexit.awb.ws.core.util.WebApplicationUpdate;
import de.enflexit.awb.ws.core.util.WebApplicationUpdateProcess;
import de.enflexit.awb.ws.core.util.WebApplicationUpdateProcessListener;
import de.enflexit.awb.ws.core.util.WebApplicationVersion;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import de.enflexit.common.swing.JHyperLink;
import de.enflexit.common.swing.OwnerDetection;

/**
 * The Class JPanelSettingsHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelSettingsWebApplication extends JPanel implements JettyConfigurationInterface<ServerTreeNodeWebAppSettings>, ActionListener, WebApplicationUpdateProcessListener {

	private static final long serialVersionUID = -4985161964727450005L;

	private ImageIcon imageGreen = BundleHelper.getImageIcon("MB_CheckGreen.png");
	private ImageIcon imageRed   = BundleHelper.getImageIcon("MB_CheckRed.png");
	
	private ServerTreeNodeServer serverTreeNodeServer; 					// --- Always set and thus available --------
	@SuppressWarnings("unused")
	private ServerTreeNodeWebAppSettings serverTreeNodeWebApplication; 	// --- Used only for server wide settings ---
	
	private boolean isPauseDocumentListener;
	private boolean isPauseActionListener;
	
	private JLabel jLabelWebAppURL;
	private DefaultComboBoxModel<UpdateStrategy>  comboBoxModelUpdateStrategy;
	private JComboBox<UpdateStrategy> jComboBoxUpdateStrategy;
	
	private JLabel jLabelUpdateStrategy;
	private JTextField jTextFieldWebAppURL;
	private JButton jButtonCheckDownloadPath;
	private JScrollPane jScrollPaneVersionsAvailable;
	
	private DefaultListModel<WebApplicationVersion> listModelVersionsAvailable;
	private JList<WebApplicationVersion> jListVersionsAvailable;
	private JButton jButtonCheckForUpdates;
	private JButton jButtonInstallSelected;
	private JLabel jLabelCurrentVersionLabel;
	private JLabel jLabelCurrentVersion;
	private JLabel jLabelWebAppAvailable;
	private JLabel jLabelHeader;
	private JLabel jLabelVisitWebApplication;
	private JHyperLink jHyperLinkWebApplication;

	
	/**
	 * Instantiates a new JPanel for the server settings.
	 */
	public JPanelSettingsWebApplication() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gbl_jPanelLeft = new GridBagLayout();
		gbl_jPanelLeft.columnWidths = new int[]{0, 220, 0, 0, 0, 0};
		gbl_jPanelLeft.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_jPanelLeft.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_jPanelLeft.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gbl_jPanelLeft);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.gridwidth = 2;
		gbc_jLabelHeader.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jLabelWebAppAvailable = new GridBagConstraints();
		gbc_jLabelWebAppAvailable.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelWebAppAvailable.anchor = GridBagConstraints.WEST;
		gbc_jLabelWebAppAvailable.gridx = 3;
		gbc_jLabelWebAppAvailable.gridy = 0;
		add(getJLabelWebAppAvailable(), gbc_jLabelWebAppAvailable);
		GridBagConstraints gbc_jButtonInstallSelected = new GridBagConstraints();
		gbc_jButtonInstallSelected.insets = new Insets(10, 5, 0, 10);
		gbc_jButtonInstallSelected.anchor = GridBagConstraints.NORTHWEST;
		gbc_jButtonInstallSelected.gridx = 4;
		gbc_jButtonInstallSelected.gridy = 0;
		add(getJButtonInstallSelected(), gbc_jButtonInstallSelected);
		GridBagConstraints gbc_jLabelWebAppURL = new GridBagConstraints();
		gbc_jLabelWebAppURL.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelWebAppURL.anchor = GridBagConstraints.WEST;
		gbc_jLabelWebAppURL.gridx = 0;
		gbc_jLabelWebAppURL.gridy = 1;
		this.add(getJLabelWebAppURL(), gbc_jLabelWebAppURL);
		GridBagConstraints gbc_jTextFieldWebAppURL = new GridBagConstraints();
		gbc_jTextFieldWebAppURL.anchor = GridBagConstraints.WEST;
		gbc_jTextFieldWebAppURL.insets = new Insets(10, 5, 0, 0);
		gbc_jTextFieldWebAppURL.gridx = 1;
		gbc_jTextFieldWebAppURL.gridy = 1;
		this.add(getJTextFieldWebAppURL(), gbc_jTextFieldWebAppURL);
		GridBagConstraints gbc_jButtonCheckDownloadPath = new GridBagConstraints();
		gbc_jButtonCheckDownloadPath.insets = new Insets(10, 5, 0, 10);
		gbc_jButtonCheckDownloadPath.gridx = 2;
		gbc_jButtonCheckDownloadPath.gridy = 1;
		add(getJButtonCheckDownloadPath(), gbc_jButtonCheckDownloadPath);
		GridBagConstraints gbc_jScrollPaneVersionsAvailable = new GridBagConstraints();
		gbc_jScrollPaneVersionsAvailable.gridwidth = 2;
		gbc_jScrollPaneVersionsAvailable.insets = new Insets(5, 10, 5, 14);
		gbc_jScrollPaneVersionsAvailable.gridheight = 6;
		gbc_jScrollPaneVersionsAvailable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneVersionsAvailable.gridx = 3;
		gbc_jScrollPaneVersionsAvailable.gridy = 1;
		add(getJScrollPaneVersionsAvailable(), gbc_jScrollPaneVersionsAvailable);
		GridBagConstraints gbc_jLabelUpdatePolicy = new GridBagConstraints();
		gbc_jLabelUpdatePolicy.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelUpdatePolicy.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelUpdatePolicy.gridx = 0;
		gbc_jLabelUpdatePolicy.gridy = 2;
		this.add(getJLabelUpdateStrategy(), gbc_jLabelUpdatePolicy);
		GridBagConstraints gbc_jComboBoxSecurityHandler = new GridBagConstraints();
		gbc_jComboBoxSecurityHandler.anchor = GridBagConstraints.WEST;
		gbc_jComboBoxSecurityHandler.insets = new Insets(10, 5, 0, 0);
		gbc_jComboBoxSecurityHandler.gridx = 1;
		gbc_jComboBoxSecurityHandler.gridy = 2;
		this.add(getJComboBoxUpdateStrategy(), gbc_jComboBoxSecurityHandler);
		GridBagConstraints gbc_jLabelCurrentVersionLabel = new GridBagConstraints();
		gbc_jLabelCurrentVersionLabel.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelCurrentVersionLabel.anchor = GridBagConstraints.WEST;
		gbc_jLabelCurrentVersionLabel.gridx = 0;
		gbc_jLabelCurrentVersionLabel.gridy = 3;
		add(getJLabelCurrentVersionLabel(), gbc_jLabelCurrentVersionLabel);
		GridBagConstraints gbc_jLabelCurrentVersion = new GridBagConstraints();
		gbc_jLabelCurrentVersion.insets = new Insets(10, 5, 0, 0);
		gbc_jLabelCurrentVersion.anchor = GridBagConstraints.WEST;
		gbc_jLabelCurrentVersion.gridx = 1;
		gbc_jLabelCurrentVersion.gridy = 3;
		add(getJLabelCurrentVersion(), gbc_jLabelCurrentVersion);
		GridBagConstraints gbc_jLabelVisitWebApplication = new GridBagConstraints();
		gbc_jLabelVisitWebApplication.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelVisitWebApplication.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelVisitWebApplication.gridx = 0;
		gbc_jLabelVisitWebApplication.gridy = 4;
		add(getJLabelVisitWebApplication(), gbc_jLabelVisitWebApplication);
		GridBagConstraints gbc_hprlnkHttplocalhost = new GridBagConstraints();
		gbc_hprlnkHttplocalhost.anchor = GridBagConstraints.WEST;
		gbc_hprlnkHttplocalhost.insets = new Insets(10, 5, 0, 0);
		gbc_hprlnkHttplocalhost.gridx = 1;
		gbc_hprlnkHttplocalhost.gridy = 4;
		add(getJHyperLinkWebApplication(), gbc_hprlnkHttplocalhost);
		GridBagConstraints gbc_jButtonCheckForUpdates = new GridBagConstraints();
		gbc_jButtonCheckForUpdates.insets = new Insets(10, 5, 0, 0);
		gbc_jButtonCheckForUpdates.anchor = GridBagConstraints.WEST;
		gbc_jButtonCheckForUpdates.gridx = 1;
		gbc_jButtonCheckForUpdates.gridy = 5;
		add(getJButtonCheckForUpdates(), gbc_jButtonCheckForUpdates);
	}
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Web-Application Settings");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	
	private JLabel getJLabelWebAppURL() {
		if (jLabelWebAppURL == null) {
			jLabelWebAppURL = new JLabel("Download-URL:");
			jLabelWebAppURL.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelWebAppURL;
	}
	private JTextField getJTextFieldWebAppURL() {
		if (jTextFieldWebAppURL == null) {
			jTextFieldWebAppURL = new JTextField();
			jTextFieldWebAppURL.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldWebAppURL.setPreferredSize(new Dimension(320, 28));
			jTextFieldWebAppURL.addActionListener(this);
			jTextFieldWebAppURL.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent de) {
					this.onUpdate();					
				}
				@Override
				public void insertUpdate(DocumentEvent de) {
					this.onUpdate();
				}
				@Override
				public void changedUpdate(DocumentEvent de) {
					this.onUpdate();
				}
				private void onUpdate() {
					if (JPanelSettingsWebApplication.this.isPauseDocumentListener==true) return;
					JPanelSettingsWebApplication.this.checkDownloadURL();
					JPanelSettingsWebApplication.this.setWebApplicationSettingsFromView();
				}
			});
			
		}
		return jTextFieldWebAppURL;
	}
	
	private JButton getJButtonCheckDownloadPath() {
		if (jButtonCheckDownloadPath == null) {
			jButtonCheckDownloadPath = new JButton();
			jButtonCheckDownloadPath.setPreferredSize(new Dimension(26, 26));
			jButtonCheckDownloadPath.setIcon(imageGreen);
			jButtonCheckDownloadPath.setToolTipText("Check specified Download-URL for the Web-Application");
			jButtonCheckDownloadPath.addActionListener(this);
		}
		return jButtonCheckDownloadPath;
	}
	
	private JLabel getJLabelCurrentVersionLabel() {
		if (jLabelCurrentVersionLabel == null) {
			jLabelCurrentVersionLabel = new JLabel("Current Version:");
			jLabelCurrentVersionLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelCurrentVersionLabel;
	}
	
	private JLabel getJLabelUpdateStrategy() {
		if (jLabelUpdateStrategy == null) {
			jLabelUpdateStrategy = new JLabel("Update Strategy:");
			jLabelUpdateStrategy.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelUpdateStrategy;
	}
	private DefaultComboBoxModel<UpdateStrategy> getComboBoxModelUpdateStrategy() {
		if (comboBoxModelUpdateStrategy==null) {
			comboBoxModelUpdateStrategy = new DefaultComboBoxModel<UpdateStrategy>();
			comboBoxModelUpdateStrategy.addElement(UpdateStrategy.Automatic);
			comboBoxModelUpdateStrategy.addElement(UpdateStrategy.AskUser);
			comboBoxModelUpdateStrategy.addElement(UpdateStrategy.Manual);
		}
		return comboBoxModelUpdateStrategy;
	}
	private JComboBox<UpdateStrategy> getJComboBoxUpdateStrategy() {
		if (jComboBoxUpdateStrategy == null) {
			jComboBoxUpdateStrategy = new JComboBox<>(this.getComboBoxModelUpdateStrategy());
			jComboBoxUpdateStrategy.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxUpdateStrategy.setPreferredSize(new Dimension(120, 28));
			jComboBoxUpdateStrategy.addActionListener(this);
		}
		return jComboBoxUpdateStrategy;
	}

	private JLabel getJLabelCurrentVersion() {
		if (jLabelCurrentVersion == null) {
			jLabelCurrentVersion = new JLabel("");
			jLabelCurrentVersion.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelCurrentVersion.setPreferredSize(new Dimension(320, 28));
		}
		return jLabelCurrentVersion;
	}
	private void updateCurrentVersionLabel() {
		
		WebApplicationVersion waVersion = WebApplicationUpdate.getCurrentWebApplicationVersion();
		if (waVersion==null) {
			this.getJLabelCurrentVersion().setText("UNKNOWN");
		} else {
			this.getJLabelCurrentVersion().setText(waVersion.getVersion().toString());
		}
	}
	
	private JLabel getJLabelVisitWebApplication() {
		if (jLabelVisitWebApplication == null) {
			jLabelVisitWebApplication = new JLabel("Visit:");
			jLabelVisitWebApplication.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelVisitWebApplication;
	}
	private JHyperLink getJHyperLinkWebApplication() {
		if (jHyperLinkWebApplication == null) {
			jHyperLinkWebApplication = new JHyperLink();
			jHyperLinkWebApplication.setFont(new Font("Dialog", Font.BOLD, 12));
			jHyperLinkWebApplication.setText("");
			jHyperLinkWebApplication.addActionListener(this);
		}
		return jHyperLinkWebApplication;
	}
	private void updateWebApplicationLink() {

		String httpAccessLink = this.serverTreeNodeServer.getJettyConfiguration().getWebApplicationLink();
		if (httpAccessLink==null || httpAccessLink.isBlank()==true) {
			this.getJHyperLinkWebApplication().setText("");
		} else {
			this.getJHyperLinkWebApplication().setText(httpAccessLink);
		}
	}
	
	private JButton getJButtonCheckForUpdates() {
		if (jButtonCheckForUpdates == null) {
			jButtonCheckForUpdates = new JButton("Check for Update");
			jButtonCheckForUpdates.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCheckForUpdates.setForeground(new Color(0, 153, 0));
			jButtonCheckForUpdates.setPreferredSize(new Dimension(180, 27));
			jButtonCheckForUpdates.addActionListener(this);
		}
		return jButtonCheckForUpdates;
	}
	
	
	private JLabel getJLabelWebAppAvailable() {
		if (jLabelWebAppAvailable == null) {
			jLabelWebAppAvailable = new JLabel("Online available Versions");
			jLabelWebAppAvailable.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelWebAppAvailable;
	}
	
	private JScrollPane getJScrollPaneVersionsAvailable() {
		if (jScrollPaneVersionsAvailable == null) {
			jScrollPaneVersionsAvailable = new JScrollPane();
			jScrollPaneVersionsAvailable.setViewportView(this.getJListVersionsAvailable());
		}
		return jScrollPaneVersionsAvailable;
	}
	private DefaultListModel<WebApplicationVersion> getListModelVersionsAvailable() {
		if (listModelVersionsAvailable==null) {
			listModelVersionsAvailable = new DefaultListModel<>();
			this.reFillListModelVersionsAvailable();
		}
		return listModelVersionsAvailable;
	}
	private void reFillListModelVersionsAvailable() {
		
		// --- Clear list first ---------------------------
		this.getListModelVersionsAvailable().clear();

		// --- Fill the version list ----------------------
		if (this.getWebApplicationSettings()==null) return;
		
		List<WebApplicationVersion> waVersionLIst = WebApplicationUpdate.getWebApplicationVersions(this.getJTextFieldWebAppURL().getText());
		if (waVersionLIst!=null) {
			Collections.sort(waVersionLIst);
			waVersionLIst.forEach(waVersion -> this.getListModelVersionsAvailable().addElement(waVersion));
		}
	}
	private JList<WebApplicationVersion> getJListVersionsAvailable() {
		if (jListVersionsAvailable == null) {
			jListVersionsAvailable = new JList<>(this.getListModelVersionsAvailable());
			jListVersionsAvailable.setToolTipText("Versions available from Download-URL");
			jListVersionsAvailable.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jListVersionsAvailable;
	}

	private JButton getJButtonInstallSelected() {
		if (jButtonInstallSelected == null) {
			jButtonInstallSelected = new JButton("Install selected Version");
			jButtonInstallSelected.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonInstallSelected.setForeground(new Color(0, 153, 0));
			jButtonInstallSelected.setPreferredSize(new Dimension(180, 27));
			jButtonInstallSelected.addActionListener(this);
		}
		return jButtonInstallSelected;
	}
	
	
	// ----------------------------------------------------------------------------------
	// --- From here some context variables to answer the question where we are ---------
	// ----------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.JettyConfigurationInterface#setServerTreeNodeServer(de.enflexit.awb.ws.core.model.ServerTreeNodeServer)
	 */
	@Override
	public void setServerTreeNodeServer(ServerTreeNodeServer serverTreeNodeServer) {
		// --------------------------------------------------------------------
		// --- Always set with an visualization -------------------------------
		// --------------------------------------------------------------------
		this.serverTreeNodeServer = serverTreeNodeServer;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.AbstractJPanelSettings#setDataModel(de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject)
	 */
	@Override
	public void setDataModel(ServerTreeNodeWebAppSettings dataModel) {
		// --------------------------------------------------------------------
		// --- Only used with the server wide settings !!! --------------------
		// --------------------------------------------------------------------
		this.serverTreeNodeWebApplication = dataModel;
		
		// --- Update the view ------------------------------------------------
		this.setWebApplicationSettingsToView();
		this.checkDownloadURL();
		this.updateCurrentVersionLabel();
		this.updateWebApplicationLink();
	}
	
	/**
	 * Returns the current server security settings.
	 * @return the security settings
	 */
	private JettyWebApplicationSettings getWebApplicationSettings() {
		if (this.serverTreeNodeServer==null) return null;
		return this.serverTreeNodeServer.getJettyConfiguration().getWebApplicationSettings();
	}
	/**
	 * Sets the web application settings to the view.
	 */
	private void setWebApplicationSettingsToView() {
		
		JettyWebApplicationSettings webAppSettings = this.getWebApplicationSettings();
		if (webAppSettings==null) return;
		
		this.isPauseDocumentListener = true;
		this.isPauseActionListener = true;
		this.getJTextFieldWebAppURL().setText(webAppSettings.getDownloadURL());
		this.getJComboBoxUpdateStrategy().setSelectedItem(webAppSettings.getUpdateStrategy());
		this.isPauseActionListener = false;
		this.isPauseDocumentListener = false;
	}
	/**
	 * Sets the web application settings from the view.
	 */
	private void setWebApplicationSettingsFromView() {
		
		JettyWebApplicationSettings webAppSettings = this.getWebApplicationSettings();
		if (webAppSettings==null) return;
		
		webAppSettings.setDownloadURL(this.getJTextFieldWebAppURL().getText());
		webAppSettings.setUpdateStrategy((UpdateStrategy) this.getJComboBoxUpdateStrategy().getSelectedItem());
	}
	
	/**
	 * Checks the download URL.
	 */
	private void checkDownloadURL() {
		this.checkDownloadURL(this.getJTextFieldWebAppURL().getText());
		this.reFillListModelVersionsAvailable();
	}
	/**
	 * Checks the download URL.
	 */
	private void checkDownloadURL(String webURL) {
		
		boolean hasError = false;
		try {
			// --- Check URL string first --------------------------- 
			hasError = WebApplicationUpdate.isValidUpdateURL(webURL)==false;
			if (hasError==true) return;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
		} finally {

			if (hasError==false) {
				this.getJButtonCheckDownloadPath().setIcon(this.imageGreen);
			} else {
				this.getJButtonCheckDownloadPath().setIcon(this.imageRed);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (this.isPauseActionListener==true) return;
		
		if (ae.getSource()==this.getJTextFieldWebAppURL()) {
			this.checkDownloadURL();
			this.setWebApplicationSettingsFromView();
			
		} else if (ae.getSource()==this.getJButtonCheckDownloadPath()) {
			this.checkDownloadURL();
			
		} else if (ae.getSource()==this.getJComboBoxUpdateStrategy()) {
			this.setWebApplicationSettingsFromView();
			
		} else if (ae.getSource()==this.getJHyperLinkWebApplication()) {
			Application.browseURI(ae.getActionCommand());
			
		} else if (ae.getSource()==this.getJButtonCheckForUpdates()) {
			Window owner = OwnerDetection.getOwnerWindowForComponent(this);
			WebApplicationVersion appVersionUpdate = WebApplicationUpdate.getWebApplicationUpdate(this.getJTextFieldWebAppURL().getText());
			if (appVersionUpdate==null) {
				JOptionPane.showMessageDialog(owner, "There is no update available!", "Check for Update", JOptionPane.INFORMATION_MESSAGE);
				
			} else {
				int userDecision = JOptionPane.showConfirmDialog(owner, "Version " + appVersionUpdate.getVersion().toString() + " of the web application is available!\nInstall this update?", "Check for Update", JOptionPane.YES_NO_OPTION);
				if (userDecision==JOptionPane.YES_OPTION) {
					new WebApplicationUpdateProcess(appVersionUpdate, this).start();
				}
			}
		
		} else if (ae.getSource()==this.getJButtonInstallSelected()) {
			// --- Get the selected WebApplicationVersion ---------------------
			WebApplicationVersion appVersion = this.getJListVersionsAvailable().getSelectedValue();
			if (appVersion==null) {
				System.err.println("[" + this.getClass().getSimpleName() + "] No web application was selected!");
			} else {
				new WebApplicationUpdateProcess(appVersion, this).start(); 
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.core.util.WebApplicationUpdateProcessListener#onUpdateProcessFinalized()
	 */
	@Override
	public void onUpdateProcessFinalized() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JPanelSettingsWebApplication.this.updateCurrentVersionLabel();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.JettyConfigurationInterface#stopEditing()
	 */
	@Override
	public void stopEditing() {

	}
	
}
