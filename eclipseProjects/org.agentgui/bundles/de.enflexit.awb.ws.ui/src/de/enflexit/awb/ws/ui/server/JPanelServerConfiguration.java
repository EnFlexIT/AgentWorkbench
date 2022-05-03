package de.enflexit.awb.ws.ui.server;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject;
import de.enflexit.awb.ws.core.model.ServerTreeNodeHandler;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;

/**
 * The Class JPanelServerConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelServerConfiguration extends JPanel {

	private static final long serialVersionUID = -1935493940628529036L;
	
	private JSplitPane splitPane;
	private JScrollPane jScrollPaneLeft;
	private ServerTree jTreeServer;
	private TreeSelectionListener  treeSelectionListener;
	
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
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(10, 10, 10, 10);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		add(getSplitPane(), gbc_splitPane);
	}
	
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setDividerLocation(320);
			splitPane.setResizeWeight(0.5);
			splitPane.setBorder(BorderFactory.createEmptyBorder());
			splitPane.setLeftComponent(this.getJScrollPaneLeft());
			splitPane.setRightComponent(this.getJScrollPaneRight());
		}
		return splitPane;
	}
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
			jTreeServer.addTreeSelectionListener(this.getTreeSelectionListener());
		}
		return jTreeServer;
	}
	/**
	 * Returns the tree selection listener for the local {@link ServerTree}.
	 * @return the tree selection listener
	 */
	private TreeSelectionListener getTreeSelectionListener() {
		if (treeSelectionListener==null) {
			treeSelectionListener = new TreeSelectionListener() {
				
				private boolean isDisabledTreeSelectionListener;
				
				/* (non-Javadoc)
				 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
				 */
				@Override
				public void valueChanged(TreeSelectionEvent tse) {
					
					if (this.isDisabledTreeSelectionListener==true) return;
					
					// --- Check to save the changes in the current view ------
					Component currView = JPanelServerConfiguration.this.getJScrollPaneRight().getViewport().getView();
					if (currView instanceof AbstractJPanelSettings<?>) {
						// --- Check if is unsaved ----------------------------
						AbstractJPanelSettings<?> jPanelSettings = (AbstractJPanelSettings<?>) currView;
						if (jPanelSettings.isUnsaved()==true) {
							// --- Ask the user to save or discard settings --- 
							String title = "Save changes?";
							String message = "Would you like to save the current changes?";
							int userAnswer = JOptionPane.showConfirmDialog(JPanelServerConfiguration.this.getParent(), message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null);
							if (userAnswer==JOptionPane.YES_OPTION) {
								// --- Save the changes -----------------------
								System.out.println("Save changes !");
								// TODO
								
							} else if (userAnswer==JOptionPane.CANCEL_OPTION) {
								// --- Return to previous selection -----------
								this.isDisabledTreeSelectionListener = true;
								JPanelServerConfiguration.this.getJTreeServer().setSelectionPath(tse.getOldLeadSelectionPath());
								this.isDisabledTreeSelectionListener = false;
								return;
							}
						}
					} 
					
					// --- Set view to new selection--------------------------- 
					AbstractServerTreeNodeObject stnSelectionNew = JPanelServerConfiguration.this.getServerTreeNode(tse.getNewLeadSelectionPath());
					JPanelServerConfiguration.this.setViewToTreeNodeSelection(stnSelectionNew);
				}
			};
		}
		return treeSelectionListener;
	}
	/**
	 * Returns the server tree node.
	 *
	 * @param path the TreePath
	 * @return the server tree node
	 */
	private AbstractServerTreeNodeObject getServerTreeNode(TreePath path) {
		if (path==null) return null;
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		return (AbstractServerTreeNodeObject) treeNode.getUserObject();
	}

	/**
	 * Sets the view to the specified tree node selection.
	 * @param serverTreeNodeObject the new view to tree node selection
	 */
	private void setViewToTreeNodeSelection(AbstractServerTreeNodeObject serverTreeNodeObject) {
		
		// --- Set the view to the new selection ------------------------------ 
		AbstractJPanelSettings<?> settingsPanel = null;
		if (serverTreeNodeObject instanceof ServerTreeNodeServer) {
			settingsPanel = this.getJPanelSettingsServer();
			this.getJPanelSettingsServer().setDataModel((ServerTreeNodeServer) serverTreeNodeObject);
		} else if (serverTreeNodeObject instanceof ServerTreeNodeHandler) {
			settingsPanel = this.getJPanelSettingsHandler();
			this.getJPanelSettingsHandler().setDataModel((ServerTreeNodeHandler) serverTreeNodeObject);
		}
		this.getJScrollPaneRight().setViewportView(settingsPanel);
		
	}
	
	private JScrollPane getJScrollPaneRight() {
		if (jScrollPaneRight == null) {
			jScrollPaneRight = new JScrollPane();
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
