package de.enflexit.awb.ws.ui.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import de.enflexit.awb.ws.client.CredentialAssignment;
import de.enflexit.awb.ws.client.ServerURL;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;

/**
 * The Class JPanelServerURL.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class JPanelServerURL extends JPanel implements ActionListener,WsConfigurationInterface,WindowListener{

	private static final long serialVersionUID = -4683248868011024312L;
	private JLabel jLableServer;
	private JScrollPane jScrollPaneServerUrl;
	private JList<ServerURL> jListServerUrl;
	private JButton jButtonCreateNewServer;
	private JButton jButtonDeleteServerUrl;
	private JButton jButtonEditAServerUrl;
	private JDialogCreateServerURL jDialogCreateServer;
	private JPanel jPanelHeader;
	
	private List<ServerURL> deletedServerURLCache;
	private List<ServerURL> addedServerURLCache;
	private List<ServerURL> modifiedServerURLCache;
	

	/**
	 * Instantiates a new j panel server URL.
	 */
	public JPanelServerURL() {
	
		
		this.initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] { 26, 125, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jPanelHeader = new GridBagConstraints();
		gbc_jPanelHeader.insets = new Insets(5, 0, 0, 0);
		gbc_jPanelHeader.fill = GridBagConstraints.BOTH;
		gbc_jPanelHeader.gridx = 0;
		gbc_jPanelHeader.gridy = 0;
		add(getJPanelHeader(), gbc_jPanelHeader);
		GridBagConstraints gbc_jScrollPaneServerUrl = new GridBagConstraints();
		gbc_jScrollPaneServerUrl.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneServerUrl.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneServerUrl.gridx = 0;
		gbc_jScrollPaneServerUrl.gridy = 1;
		add(getJScrollPaneServerUrl(), gbc_jScrollPaneServerUrl);
	}
	
    //----------------------------------------------------------------------
	//------------------ From here Getter and Setter------------------------
	//----------------------------------------------------------------------
	
	/**
     * Gets the j lable server.
     *
     * @return the j lable server
     */
    private JLabel getJLableServer() {
		if (jLableServer == null) {
			jLableServer = new JLabel("Server");
			jLableServer.setFont(new Font("Dialog", Font.BOLD, 12));
			jLableServer.setMinimumSize(new Dimension(150, 26));
			jLableServer.setPreferredSize(new Dimension(150, 26));

		}
		return jLableServer;
	}
	
	/**
     * Gets the j scroll pane server url.
     *
     * @return the j scroll pane server url
     */
    private JScrollPane getJScrollPaneServerUrl() {
		if (jScrollPaneServerUrl == null) {
			jScrollPaneServerUrl = new JScrollPane();
			jScrollPaneServerUrl.setViewportView(getJListServerUrl());
		}
		return jScrollPaneServerUrl;
	}
    
	/**
	 * Gets the j list server url.
	 *
	 * @return the j list server url
	 */
	public JList<ServerURL> getJListServerUrl() {
		if (jListServerUrl == null) {
			jListServerUrl = new JList<ServerURL>();
			DefaultListModel<ServerURL> serverURLs = new DefaultListModel<ServerURL>();
			serverURLs.addAll(WsCredentialStore.getInstance().getServerURLList());
			jListServerUrl.setModel(serverURLs);
		}
		return jListServerUrl;
	}
	
	/**
	 * Gets the j button create new server.
	 *
	 * @return the j button create new server
	 */
	private JButton getJButtonCreateNewServer() {
		if (jButtonCreateNewServer == null) {
			jButtonCreateNewServer = new JButton(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonCreateNewServer.setToolTipText("Create a new ServerUrl");
			jButtonCreateNewServer.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCreateNewServer.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
			jButtonCreateNewServer.addActionListener(this);
		}
		return jButtonCreateNewServer;
	}
	
	/**
	 * Gets the j button delete server url.
	 *
	 * @return the j button delete server url
	 */
	public JButton getJButtonDeleteServerUrl() {
		if (jButtonDeleteServerUrl == null) {
			jButtonDeleteServerUrl = new JButton(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDeleteServerUrl.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonDeleteServerUrl.setToolTipText("Delete a Server-URL");
			jButtonDeleteServerUrl.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
			jButtonDeleteServerUrl.addActionListener(this);
		}
		return jButtonDeleteServerUrl;
	}
	
	/**
	 * Gets the j button edit A server url.
	 *
	 * @return the j button edit A server url
	 */
	private JButton getJButtonEditAServerUrl() {
		if (jButtonEditAServerUrl == null) {
			jButtonEditAServerUrl = new JButton(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonEditAServerUrl.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonEditAServerUrl.setToolTipText("Edit an existing Server-URL");
			jButtonEditAServerUrl.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
			jButtonEditAServerUrl.addActionListener(this);
		}
		return jButtonEditAServerUrl;
	}
	
	/**
	 * Gets the j dialog create server.
	 *
	 * @return the j dialog create server
	 */
	public JDialogCreateServerURL getJDialogCreateServer() {
		if(this.jDialogCreateServer==null) {
			Window owner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
			jDialogCreateServer=new JDialogCreateServerURL(owner, false);
		}
		return jDialogCreateServer;
	}

	/**
	 * Sets the j dialog create server.
	 *
	 * @param jDialogCreateServer the new j dialog create server
	 */
	public void setjDialogCreateServer(JDialogCreateServerURL jDialogCreateServer) {
		this.jDialogCreateServer = jDialogCreateServer;
	}
	
	/**
	 * Gets the j panel header.
	 *
	 * @return the j panel header
	 */
	private JPanel getJPanelHeader() {
		if (jPanelHeader == null) {
			jPanelHeader = new JPanel();
			GridBagLayout gbl_jPanelHeader = new GridBagLayout();
			gbl_jPanelHeader.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_jPanelHeader.rowHeights = new int[]{26, 0};
			gbl_jPanelHeader.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelHeader.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelHeader.setLayout(gbl_jPanelHeader);
			GridBagConstraints gbc_jLableServer = new GridBagConstraints();
			gbc_jLableServer.anchor = GridBagConstraints.SOUTHWEST;
			gbc_jLableServer.insets = new Insets(0, 0, 0, 5);
			gbc_jLableServer.gridx = 0;
			gbc_jLableServer.gridy = 0;
			jPanelHeader.add(getJLableServer(), gbc_jLableServer);
			GridBagConstraints gbc_jButtonCreateNewServer = new GridBagConstraints();
			gbc_jButtonCreateNewServer.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonCreateNewServer.gridx = 1;
			gbc_jButtonCreateNewServer.gridy = 0;
			jPanelHeader.add(getJButtonCreateNewServer(), gbc_jButtonCreateNewServer);
			GridBagConstraints gbc_jButtonEditAServerUrl = new GridBagConstraints();
			gbc_jButtonEditAServerUrl.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonEditAServerUrl.gridx = 2;
			gbc_jButtonEditAServerUrl.gridy = 0;
			jPanelHeader.add(getJButtonEditAServerUrl(), gbc_jButtonEditAServerUrl);
			GridBagConstraints gbc_jButtonDeleteServerUrl = new GridBagConstraints();
			gbc_jButtonDeleteServerUrl.gridx = 3;
			gbc_jButtonDeleteServerUrl.gridy = 0;
			jPanelHeader.add(getJButtonDeleteServerUrl(), gbc_jButtonDeleteServerUrl);
		}
		return jPanelHeader;
	}
	
	/**
	 * Fill J list server url and repaint.
	 */
	public void fillJListServerUrlAndRepaint() {
		DefaultListModel<ServerURL> serverURLs = new DefaultListModel<ServerURL>();
		List<ServerURL> serverURLsAsList=WsCredentialStore.getInstance().getServerURLList();
		serverURLs.addAll(serverURLsAsList);
		this.getJListServerUrl().setModel(serverURLs);
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Show J dialog server url.
	 */
	private void showJDialogServerUrl() {
		Window owner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
		JDialogCreateServerURL createServerDialog = new JDialogCreateServerURL(owner,false);
		createServerDialog.addWindowListener(this);
		this.setjDialogCreateServer(createServerDialog);
        createServerDialog.setVisible(true);
	}
	
	/**
	 * Show J dialog for editing A server url.
	 */
	private void showJDialogForEditingAServerUrl() {
		if (getJListServerUrl().getSelectedValue() != null) {
			Window owner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
			JDialogCreateServerURL createServerDialog = new JDialogCreateServerURL(owner,false,getJListServerUrl().getSelectedValue());
			createServerDialog.addWindowListener(this);
			this.setjDialogCreateServer(createServerDialog);
            createServerDialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Please selected a Server-URL to edit!");
		}
	}
	
	/**
	 * Gets the deleted server URL cache.
	 *
	 * @return the deleted server URL cache
	 */
	public List<ServerURL> getDeletedServerURLCache() {
		if(this.deletedServerURLCache==null) {
			deletedServerURLCache= new ArrayList<ServerURL>();
		}
		return deletedServerURLCache;
	}

	/**
	 * Sets the deleted server URL cache.
	 *
	 * @param deletedServerURLCache the new deleted server URL cache
	 */
	public void setDeletedServerURLCache(List<ServerURL> deletedServerURLCache) {
		this.deletedServerURLCache = deletedServerURLCache;
	}

	/**
	 * Gets the added server URL cache.
	 *
	 * @return the added server URL cache
	 */
	public List<ServerURL> getAddedServerURLCache() {
		if(this.addedServerURLCache==null) {
			addedServerURLCache= new ArrayList<ServerURL>();
		}
		return addedServerURLCache;
	}

	/**
	 * Sets the added server URL cache.
	 *
	 * @param addedServerURLCache the new added server URL cache
	 */
	public void setAddedServerURLCache(List<ServerURL> addedServerURLCache) {
		this.addedServerURLCache = addedServerURLCache;
	}
	
	/**
	 * Gets the modified server URL cache.
	 *
	 * @return the modified server URL cache
	 */
	public List<ServerURL> getModifiedServerURLCache() {
		if(this.modifiedServerURLCache==null) {
			this.modifiedServerURLCache=new ArrayList<ServerURL>();
		}
		return modifiedServerURLCache;
	}

	/**
	 * Sets the modified server URL cache.
	 *
	 * @param modifiedServerURLCache the new modified server URL cache
	 */
	public void setModifiedServerURLCache(List<ServerURL> modifiedServerURLCache) {
		this.modifiedServerURLCache = modifiedServerURLCache;
	}
	
	//--------------------------------------------------------------------------------------
	//-------------------------Methods to fill or manipulated components--------------------
    //--------------------------------------------------------------------------------------

	private void deleteServerURL(ServerURL serverURL) {
		if (serverURL != null) {
			int option = JOptionPane.showConfirmDialog(this, "Do you want to delete the Server with the following URL "+ serverURL.getServerURL()+"and all its corresponding CredentialAssignments?","Deletion of a Server-URL", JOptionPane.YES_NO_CANCEL_OPTION);
			if(option==JOptionPane.YES_OPTION) {
			  
				// Remove all linked CredentialAssignments before deleting the credential
			   List<CredentialAssignment> credAssgns=WsCredentialStore.getInstance().getCredentialAssignmentWithServer(serverURL);
			   WsCredentialStore.getInstance().getCredentialAssignmentList().removeAll(credAssgns);
		       
			   // Remove ServerURL
			   WsCredentialStore.getInstance().getServerURLList().remove(serverURL);
			   this.fillJListServerUrlAndRepaint();
			   this.getDeletedServerURLCache().add(serverURL);
			}
		} else {
			JOptionPane.showConfirmDialog(this, "Please select a Server-URL!","Select a Server-URL", JOptionPane.YES_OPTION);
		}
	}
	
	//--------------------------------------------------------------------------------------
	//-------------------------Overidden Methods--------------------------------------------
    //--------------------------------------------------------------------------------------
	
	/* (non-Javadoc)
	    * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	    */
	    //-------------------------------------
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(getJButtonCreateNewServer())) {
				showJDialogServerUrl();
			} else if(e.getSource().equals(getJButtonEditAServerUrl())) {
			    showJDialogForEditingAServerUrl();
			} else if(e.getSource().equals(getJButtonDeleteServerUrl())) {
				ServerURL serverURL = this.getJListServerUrl().getSelectedValue();
				deleteServerURL(serverURL);
			}
		}
		

	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	*/
	@Override
	public boolean hasUnsavedChanges() {
		if(this.getAddedServerURLCache().size()>0) {
			return true;
		}
		
		if(this.getDeletedServerURLCache().size()>0) {
			return true;
		}
		
		if(this.getModifiedServerURLCache().size()>0) {
			return true;
		}		
		return false;
	}

	/* (non-Javadoc)
	* @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	*/
	@Override
	public boolean userConfirmedToChangeView() {	
		return false;
	}

	/* (non-Javadoc)
	* @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	*/
	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	/* (non-Javadoc)
	* @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	*/
	@Override
	public void windowClosing(WindowEvent e) {
		if(e.getSource().equals(this.getJDialogCreateServer())) {
			fillJListServerUrlAndRepaint();
		}
		
	}

	/* (non-Javadoc)
	* @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	*/
	@Override
	public void windowClosed(WindowEvent e) {
		if(e.getSource().equals(this.getJDialogCreateServer())) {
			if(this.getJDialogCreateServer().getServerURL()!=null){
				//Check if it is in cached Change list
				if(!this.getAddedServerURLCache().contains(this.getJDialogCreateServer().getServerURL())) {
				this.getAddedServerURLCache().add(this.getJDialogCreateServer().getServerURL());
				}
			}else if(this.getJDialogCreateServer().getModifiedServerURL()!=null) {
				//Check if it is in cached Change list
				if(!this.getModifiedServerURLCache().contains(this.getJDialogCreateServer().getModifiedServerURL())) {
					this.getModifiedServerURLCache().add(this.getJDialogCreateServer().getModifiedServerURL());
				}
			}
			this.fillJListServerUrlAndRepaint();
		}	
	}

	/* (non-Javadoc)
	* @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	*/
	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	/* (non-Javadoc)
	* @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	*/
	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	/* (non-Javadoc)
	* @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	*/
	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	/* (non-Javadoc)
	* @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	*/
	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}
}
