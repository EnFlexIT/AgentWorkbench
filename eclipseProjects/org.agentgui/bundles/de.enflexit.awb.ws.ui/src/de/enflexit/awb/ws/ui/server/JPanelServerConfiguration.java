package de.enflexit.awb.ws.ui.server;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
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
	private JToolBarServer jToolBarServer;
	
	private JScrollPane jScrollPaneRight;
	private JPanelSettingsServer jPanelSettingsServer;
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
			splitPane.setDividerLocation(320);
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
			this.getJPanelSettingsServer().setDataModel(newServerTreeNodeServer);
		} else if (serverTreeNodeObject instanceof ServerTreeNodeHandler) {
			// --- Show handler panel ----------- 
			settingsPanel = this.getJPanelSettingsHandler();
			this.getJPanelSettingsHandler().setDataModel((ServerTreeNodeHandler) serverTreeNodeObject);
		}
		this.getJToolBarServer().setServerTreeNode(newServerTreeNodeServer);
		this.getJScrollPaneRight().setViewportView((JComponent)settingsPanel);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		if (this.editServerTreeNodeServer!=null && this.editServerTreeNodeServer.hasChangedJettySettings()==true) {
			return true;
		}
		return false;
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
			// --- TODO Reload view ???
			
		} else if (userAnswer==JOptionPane.CANCEL_OPTION) {
			// --- Return to previous selection ---------------------------
			return false;
		}
		return true;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, right part of the server configuration UI ---------------
	// ------------------------------------------------------------------------	
	public JPanel getjPanelRightBase() {
		if (jPanelRightBase==null) {
			jPanelRightBase = new JPanel();
			jPanelRightBase.setBorder(BorderFactory.createEmptyBorder());
			jPanelRightBase.setLayout(new BorderLayout());
			jPanelRightBase.add(this.getJToolBarServer(), BorderLayout.NORTH);
			jPanelRightBase.add(this.getJScrollPaneRight(), BorderLayout.CENTER);
		}
		return jPanelRightBase;
	}
	public JToolBarServer getJToolBarServer() {
		if (jToolBarServer==null) {
			jToolBarServer = new JToolBarServer();
		}
		return jToolBarServer;
	}
	private JScrollPane getJScrollPaneRight() {
		if (jScrollPaneRight == null) {
			jScrollPaneRight = new JScrollPane();
			jScrollPaneRight.setBorder(BorderFactory.createEmptyBorder());
		}
		return jScrollPaneRight;
	}
	private JPanelSettingsServer getJPanelSettingsServer() {
		if (jPanelSettingsServer==null) {
			jPanelSettingsServer = new JPanelSettingsServer();
		}
		return jPanelSettingsServer;
	}
	private JPanelSettingsHandler getJPanelSettingsHandler() {
		if (jPanelSettingsHandler==null) {
			jPanelSettingsHandler = new JPanelSettingsHandler();
		}
		return jPanelSettingsHandler;
	}
	
}
