package de.enflexit.awb.ws.ui.server;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject;
import de.enflexit.awb.ws.core.model.ServerTreeNodeHandler;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServerSecurity;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;

/**
 * The Class JPanelServerConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelServerConfiguration extends JPanel implements WsConfigurationInterface, TreeSelectionListener {

	private static final long serialVersionUID = -1935493940628529036L;
	
	private ServerTreeNodeServer editServerTreeNodeServer;
	
	private JSplitPane splitPane;
	
	private JScrollPane jScrollPaneLeft;
	private ServerTree jTreeServer;
	private boolean isDisabledTreeSelectionListener;
	
	private JPanel jPanelRightBase;
		private JPanel jPanelRightTop;
			private JLabel jLabelServerName;
			private JToolBarServer jToolBarServer;
		
		private JPanelSettingsServer jPanelSettingsServer;
		private JPanelSettingsSecurity jPanelSettingsSecurity;
		private JPanelSettingsHandler jPanelSettingsHandler;	
		
	/**
	 * Instantiates a new j panel server configuration.
	 */
	public JPanelServerConfiguration() {
		this.initialize();
	}
	private void initialize() {
		
		this.setBorder(BorderFactory.createEmptyBorder());

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(10, 10, 10, 10);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		this.add(this.getSplitPane(), gbc_splitPane);
		
		// --- Select first server node ----------------------
		this.getJTreeServer().selectFirstServerNode();
		
	}
	
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setDividerLocation(300);
			splitPane.setResizeWeight(0.5);
			splitPane.setBorder(BorderFactory.createEmptyBorder());
			splitPane.setLeftComponent(this.getJScrollPaneLeft());
			splitPane.setRightComponent(this.getjPanelRightBase());
		}
		return splitPane;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, left part of the server configuration UI ----------------
	// ------------------------------------------------------------------------	
	private JScrollPane getJScrollPaneLeft() {
		if (jScrollPaneLeft == null) {
			jScrollPaneLeft = new JScrollPane();
			jScrollPaneLeft.setViewportView(this.getJTreeServer());
		}
		return jScrollPaneLeft;
	}
	
	private ServerTree getJTreeServer() {
		if (jTreeServer == null) {
			jTreeServer = new ServerTree();
			jTreeServer.addTreeSelectionListener(this);
		}
		return jTreeServer;
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent tse) {
		
		if (this.isDisabledTreeSelectionListener==true) return;
		
		// --- Check for empty tree selection ---------------------
		if (tse.getNewLeadSelectionPath()==null) return;
		
		// --- Set view to new selection--------------------------- 
		AbstractServerTreeNodeObject stnSelectionNew = this.getJTreeServer().getServerTreeNodeObject(tse.getNewLeadSelectionPath());
		this.setViewToTreeNodeSelection(stnSelectionNew, tse.getOldLeadSelectionPath());
	}
	/**
	 * Sets the view to the specified tree node selection.
	 * @param serverTreeNodeObject the new view to tree node selection
	 */
	private void setViewToTreeNodeSelection(AbstractServerTreeNodeObject serverTreeNodeObject, TreePath oldSelectionPath) {
		
		// --------------------------------------------------------------------
		// --- Get current or parent server node ------------------------------
		// --------------------------------------------------------------------
		ServerTreeNodeServer newServerTreeNodeServer = null;
		if (serverTreeNodeObject instanceof ServerTreeNodeServer) {
			newServerTreeNodeServer = (ServerTreeNodeServer) serverTreeNodeObject;
		} else if (serverTreeNodeObject instanceof ServerTreeNodeHandler) {
			// --- Show handler panel ----------------------------------------- 
			newServerTreeNodeServer = this.getJTreeServer().getParentServerNode((ServerTreeNodeHandler) serverTreeNodeObject);
		} else if (serverTreeNodeObject instanceof ServerTreeNodeServerSecurity) {
			// --- Show security panel ---------------------------------------- 
			newServerTreeNodeServer = this.getJTreeServer().getParentServerNode((ServerTreeNodeServerSecurity) serverTreeNodeObject);
		}

		// --------------------------------------------------------------------
		// --- Check for server change and setting changes --------------------
		// --------------------------------------------------------------------
		if (this.editServerTreeNodeServer!=null && newServerTreeNodeServer!=this.editServerTreeNodeServer && this.editServerTreeNodeServer.hasChangedJettySettings()==true) {
			// --- Ask the user to save or discard settings -------------------
			if (this.userConfirmedToChangeView()==false) {
				// --- Return to previous selection ---------------------------
				this.isDisabledTreeSelectionListener = true;
				JPanelServerConfiguration.this.getJTreeServer().setSelectionPath(oldSelectionPath);
				this.isDisabledTreeSelectionListener = false;
				return;
			}
		}
		// --- Set as current server to edit ----------------------------------
		this.editServerTreeNodeServer = newServerTreeNodeServer;
		
		// --------------------------------------------------------------------
		// --- Set the view to the new selection ------------------------------
		// --------------------------------------------------------------------
		JettyConfigurationInterface<?> settingsPanel = null;
		if (serverTreeNodeObject instanceof ServerTreeNodeServer) {
			// --- Show server panel ------------
			settingsPanel = this.getJPanelSettingsServer();
			settingsPanel.setServerTreeNodeServer(this.editServerTreeNodeServer);
			this.getJPanelSettingsServer().setDataModel(newServerTreeNodeServer);
			
		} else if (serverTreeNodeObject instanceof ServerTreeNodeHandler) {
			// --- Show handler panel ----------- 
			settingsPanel = this.getJPanelSettingsHandler();
			settingsPanel.setServerTreeNodeServer(this.editServerTreeNodeServer);
			this.getJPanelSettingsHandler().setDataModel((ServerTreeNodeHandler) serverTreeNodeObject);
			
		} else if (serverTreeNodeObject instanceof ServerTreeNodeServerSecurity) {
			// --- Show security panel ----------
			settingsPanel = this.getJPanelSettingsSecurity();
			settingsPanel.setServerTreeNodeServer(this.editServerTreeNodeServer);
			this.getJPanelSettingsSecurity().setDataModel((ServerTreeNodeServerSecurity) serverTreeNodeObject);
			
		}
		this.getJLabelServerName().setText(this.editServerTreeNodeServer.getJettyConfiguration().getServerName());
		this.getJToolBarServer().setServerTreeNode(this.editServerTreeNodeServer);
		this.exchangeRightCenterComponent((JComponent) settingsPanel);
	}
	
	/**
	 * Reloads the current view.
	 */
	public void reloadView() {
		
		// --- Get current selections ---------------------
		ServerTreeNodeServer stnServer = null;
		ServerTreeNodeHandler stnHandler = null;
		ServerTreeNodeServerSecurity stnSecurity = null;
		
		AbstractServerTreeNodeObject stnoSelected = this.getJTreeServer().getServerTreeNodeObjectSelected();
		if (stnoSelected instanceof ServerTreeNodeServer) {
			stnServer = (ServerTreeNodeServer) stnoSelected;
		} else if (stnoSelected instanceof ServerTreeNodeHandler) {
			stnHandler = (ServerTreeNodeHandler) stnoSelected;
			stnServer = this.getJTreeServer().getParentServerNode(stnHandler);
		} else if (stnoSelected instanceof ServerTreeNodeServerSecurity) {
			stnSecurity = (ServerTreeNodeServerSecurity) stnoSelected;
			stnServer = this.getJTreeServer().getParentServerNode(stnSecurity);
		}

		// --- Reload tree --------------------------------
		this.getJTreeServer().refreshTreeModel();
		
		// --- Reset to previous selection ----------------
		this.getJTreeServer().selectServerNode(stnServer.getJettyConfiguration().getServerName());
		if (stnHandler!=null)  this.getJTreeServer().selectHandlerNode(stnHandler.getServiceClassName());
		if (stnSecurity!=null) this.getJTreeServer().selectSecurityNode(stnServer.getJettyConfiguration().getServerName());
		
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		if (this.editServerTreeNodeServer==null) return false;
		return this.editServerTreeNodeServer.hasChangedJettySettings();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {

		if (this.hasUnsavedChanges()==false) return true;
		
		String title = "Save server settings?";
		String message = "Would you like to save the changes in the server settings for server '" + this.editServerTreeNodeServer.getJettyConfiguration().getServerName() + "'?";
		int userAnswer = JOptionPane.showConfirmDialog(JPanelServerConfiguration.this.getParent(), message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null);
		if (userAnswer==JOptionPane.YES_OPTION) {
			// --- Save the changes ---------------------------------------
			this.editServerTreeNodeServer.save();
			
		} else if (userAnswer==JOptionPane.NO_OPTION) {
			// --- Revert to last saved settings --------------------------
			this.editServerTreeNodeServer.revertJettyConfigurationToPropertiesFile();
			this.reloadView();
			
		} else if (userAnswer==JOptionPane.CANCEL_OPTION) {
			// --- Return to previous selection ---------------------------
			return false;
		}
		return true;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, right part of the server configuration UI ---------------
	// ------------------------------------------------------------------------	
	private JPanel getjPanelRightBase() {
		if (jPanelRightBase==null) {
			jPanelRightBase = new JPanel();
			jPanelRightBase.setBorder(BorderFactory.createEmptyBorder());
			jPanelRightBase.setLayout(new BorderLayout());
			jPanelRightBase.add(this.getjPanelRightTop(), BorderLayout.NORTH);
			jPanelRightBase.add(new JPanel(), BorderLayout.CENTER);
		}
		return jPanelRightBase;
	}
	private JPanel getjPanelRightTop() {
		if (jPanelRightTop==null) {
			jPanelRightTop = new JPanel();
			
			GridBagLayout gbl_jPanelRightTop = new GridBagLayout();
			gbl_jPanelRightTop.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelRightTop.rowHeights = new int[]{28, 0};
			gbl_jPanelRightTop.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelRightTop.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelRightTop.setLayout(gbl_jPanelRightTop);
			
			GridBagConstraints gbc_jLabelServerName = new GridBagConstraints();
			gbc_jLabelServerName.insets = new Insets(0, 10, 0, 0);
			gbc_jLabelServerName.anchor = GridBagConstraints.WEST;
			gbc_jLabelServerName.gridx = 0;
			gbc_jLabelServerName.gridy = 0;
			jPanelRightTop.add(this.getJLabelServerName(), gbc_jLabelServerName);
			
			GridBagConstraints gbc_jToolBarServer = new GridBagConstraints();
			gbc_jToolBarServer.anchor = GridBagConstraints.EAST;
			gbc_jToolBarServer.gridx = 1;
			gbc_jToolBarServer.gridy = 0;
			jPanelRightTop.add(this.getJToolBarServer(), gbc_jToolBarServer);
		}
		return jPanelRightTop;
	}
	private JLabel getJLabelServerName() {
		if (jLabelServerName==null) {
			jLabelServerName = new JLabel("Server Name");
			jLabelServerName.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelServerName;
	}
	private JToolBarServer getJToolBarServer() {
		if (jToolBarServer==null) {
			jToolBarServer = new JToolBarServer(this);
		}
		return jToolBarServer;
	}	
	
	private JPanelSettingsServer getJPanelSettingsServer() {
		if (jPanelSettingsServer==null) {
			jPanelSettingsServer = new JPanelSettingsServer();
		}
		return jPanelSettingsServer;
	}
	private JPanelSettingsSecurity getJPanelSettingsSecurity() {
		if (jPanelSettingsSecurity==null) {
			jPanelSettingsSecurity = new JPanelSettingsSecurity();
		}
		return jPanelSettingsSecurity;
	}
	private JPanelSettingsHandler getJPanelSettingsHandler() {
		if (jPanelSettingsHandler==null) {
			jPanelSettingsHandler = new JPanelSettingsHandler();
		}
		return jPanelSettingsHandler;
	}
	private void exchangeRightCenterComponent(JComponent configUINew) {
		
		BorderLayout bLayout = (BorderLayout) this.getjPanelRightBase().getLayout();
		Component configUIOld = bLayout.getLayoutComponent(BorderLayout.CENTER);
		if (configUIOld==null) {
			this.getjPanelRightBase().add(configUINew, BorderLayout.CENTER);
			this.getjPanelRightBase().validate();
			this.getjPanelRightBase().repaint();
			
		} else {
			// --- Same type of component? ----------------
			if (configUINew!=configUIOld) {
				this.getjPanelRightBase().remove(configUIOld);
				this.getjPanelRightBase().add(configUINew, BorderLayout.CENTER);
				this.getjPanelRightBase().validate();
				this.getjPanelRightBase().repaint();
			}
		}
	}
	
	/**
	 * Stops the edit action in the current view (e.g. in table cells).
	 */
	public void stopEditing() {
		this.getJPanelSettingsServer().stopEditing();
		this.getJPanelSettingsHandler().stopEditing();
		this.getJPanelSettingsSecurity().stopEditing();
	}
	
}
