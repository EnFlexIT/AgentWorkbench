package de.enflexit.awb.ws.ui.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.ListSelectionModel;

import agentgui.core.config.GlobalInfo;
import de.enflexit.awb.ws.client.CredentialType;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;
import de.enflexit.awb.ws.ui.client.credentials.JDialogCredentialCreation;
import de.enflexit.common.swing.OwnerDetection;

/**
 * The Class JPanelCredentials for the listing and creation of credentials .
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class JPanelCredentials extends JPanel implements ActionListener,MouseListener,WindowListener,WsConfigurationInterface {
	
	private static final long serialVersionUID = -1252151357398930115L;
	
	private JLabel jLabelCredentialList;
	
	private JScrollPane jScrollPaneCredentialList;
	
	private JList<AbstractCredential> jListCredentials;
	
	private GridBagConstraints gbc_jPanelCredentials;
	
	private JPanel jPanelHeader;
	
	private JButton jButtonCreateNewCredential;
	
	private JButton jButtonEditACredential;
	
	private JButton jButtonDeleteACredential;
	
	private JDialogCredentialCreation jDialogCredCreate;

	private List<AbstractCredential> deletedCredentialsCache;
	private List<AbstractCredential> addedCredentialCache;
	private List<AbstractCredential> modifiedCredentialCache;

	/**
	 * Instantiates a new j panel credentials.
	 */
	public JPanelCredentials() {
		initialize();
	}
	
	/**
	 * Initialize the panel.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jPanelHeader = new GridBagConstraints();
		gbc_jPanelHeader.insets = new Insets(5, 0, 0, 0);
		gbc_jPanelHeader.fill = GridBagConstraints.BOTH;
		gbc_jPanelHeader.gridx = 0;
		gbc_jPanelHeader.gridy = 0;
		add(getJPanelHeader(), gbc_jPanelHeader);
		GridBagConstraints gbc_jScrollPaneCredentialList = new GridBagConstraints();
		gbc_jScrollPaneCredentialList.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneCredentialList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneCredentialList.gridx = 0;
		gbc_jScrollPaneCredentialList.gridy = 1;
		add(getJScrollPaneCredentialList(), gbc_jScrollPaneCredentialList);
		gbc_jPanelCredentials = new GridBagConstraints();
		gbc_jPanelCredentials.fill = GridBagConstraints.BOTH;
		gbc_jPanelCredentials.gridx = 0;
		gbc_jPanelCredentials.gridy = 3;
		this.fillCredentialJListAndRepaint();
	}

	/**
	 * Gets the j label credential list.
	 *
	 * @return the j label credential list
	 */
	private JLabel getJLabelCredentialList() {
		if (jLabelCredentialList == null) {
			jLabelCredentialList = new JLabel("Credentials");
			jLabelCredentialList.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelCredentialList.setMinimumSize(new Dimension(150, 26));
			jLabelCredentialList.setPreferredSize(new Dimension(150, 26));
		}
		return jLabelCredentialList;
	}
	
	/**
	 * Gets the j scroll pane credential list.
	 *
	 * @return the j scroll pane credential list
	 */
	private JScrollPane getJScrollPaneCredentialList() {
		if (jScrollPaneCredentialList == null) {
			jScrollPaneCredentialList = new JScrollPane();
			jScrollPaneCredentialList.setViewportView(getJListCredentials());
		}
		return jScrollPaneCredentialList;
	}
	
	/**
	 * Gets the j list credentials.
	 *
	 * @return a Jlist of all available credentials
	 */
	public JList<AbstractCredential> getJListCredentials() {
		if (jListCredentials == null) {
			jListCredentials = new JList<AbstractCredential>();
			jListCredentials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListCredentials.addMouseListener(this);
			jListCredentials.setCellRenderer(new ListCellRendererCredentials());
			fillCredentialJListAndRepaint();
		}
		return jListCredentials;
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
			gbl_jPanelHeader.rowHeights = new int[]{0, 0};
			gbl_jPanelHeader.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelHeader.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelHeader.setLayout(gbl_jPanelHeader);
			GridBagConstraints gbc_jLabelCredentialList = new GridBagConstraints();
			gbc_jLabelCredentialList.anchor = GridBagConstraints.WEST;
			gbc_jLabelCredentialList.insets = new Insets(0, 0, 0, 5);
			gbc_jLabelCredentialList.gridx = 0;
			gbc_jLabelCredentialList.gridy = 0;
			jPanelHeader.add(getJLabelCredentialList(), gbc_jLabelCredentialList);
			GridBagConstraints gbc_jButtonCreateNewCredential = new GridBagConstraints();
			gbc_jButtonCreateNewCredential.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonCreateNewCredential.gridx = 1;
			gbc_jButtonCreateNewCredential.gridy = 0;
			jPanelHeader.add(getJButtonCreateNewCredential(), gbc_jButtonCreateNewCredential);
			GridBagConstraints gbc_jButtonEditACredential = new GridBagConstraints();
			gbc_jButtonEditACredential.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonEditACredential.gridx = 2;
			gbc_jButtonEditACredential.gridy = 0;
			jPanelHeader.add(getJButtonEditACredential(), gbc_jButtonEditACredential);
			GridBagConstraints gbc_jButtonDeleteACredential = new GridBagConstraints();
			gbc_jButtonDeleteACredential.gridx = 3;
			gbc_jButtonDeleteACredential.gridy = 0;
			jPanelHeader.add(getJButtonDeleteACredential(), gbc_jButtonDeleteACredential);
		}
		return jPanelHeader;
	}
	
	/**
	 * Gets the j button create new credential.
	 *
	 * @return the j button create new credential
	 */
	private JButton getJButtonCreateNewCredential() {
		if (jButtonCreateNewCredential == null) {
			jButtonCreateNewCredential = new JButton(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonCreateNewCredential.setToolTipText("Create a new credential");
			jButtonCreateNewCredential.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCreateNewCredential.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
			jButtonCreateNewCredential.addActionListener(this);
		}
		return jButtonCreateNewCredential;
	}
	
	/**
	 * Gets the j button edit A credential.
	 *
	 * @return the j button edit A credential
	 */
	private JButton getJButtonEditACredential() {
		if (jButtonEditACredential == null) {
			jButtonEditACredential = new JButton(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonEditACredential.setToolTipText("Edit a new credential");
			jButtonEditACredential.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonEditACredential.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
			jButtonEditACredential.addActionListener(this);
		}
		return jButtonEditACredential;
	}
	
	/**
	 * Gets the j button delete A credential.
	 *
	 * @return the j button delete A credential
	 */
	protected JButton getJButtonDeleteACredential() {
		if (jButtonDeleteACredential == null) {
			jButtonDeleteACredential = new JButton(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDeleteACredential.setToolTipText("Delete a credential");
			jButtonDeleteACredential.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonDeleteACredential.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
		}
		return jButtonDeleteACredential;
	}
	
	
	/**
	 * Gets the j dialog cred create.
	 *
	 * @return the {@link JDialogCredentialCreation}
	 */
	public JDialogCredentialCreation getJDialogCredCreate() {
		if (this.jDialogCredCreate==null) {
			Window owner = OwnerDetection.getOwnerFrameForComponent(this);
	        jDialogCredCreate=new JDialogCredentialCreation(owner);
	        jDialogCredCreate.addWindowListener(this);
		}
		return jDialogCredCreate;
	}

	/**
	 * Sets the {@link JDialogCredentialCreation}.
	 *
	 * @param jDialogCredCreate the new j dialog cred create
	 */
	public void setjDialogCredCreate(JDialogCredentialCreation jDialogCredCreate) {
		jDialogCredCreate.addWindowListener(this);
		this.jDialogCredCreate = jDialogCredCreate;
	}
	
	/**
	 * Gets the deleted credentials cache.
	 *
	 * @return the deleted credentials cache
	 */
	public List<AbstractCredential> getDeletedCredentialsCache() {
		if(this.deletedCredentialsCache==null) {
			this.deletedCredentialsCache=new ArrayList<AbstractCredential>();
		}
		return deletedCredentialsCache;
	}

	/**
	 * Sets the deleted credentials cache.
	 *
	 * @param deletedCredentialsCache the new deleted credentials cache
	 */
	public void setDeletedCredentialsCache(List<AbstractCredential> deletedCredentialsCache) {
		this.deletedCredentialsCache = deletedCredentialsCache;
	}

	/**
	 * Gets the added credential cache.
	 *
	 * @return the added credential cache
	 */
	public List<AbstractCredential> getAddedCredentialCache() {
		if(this.addedCredentialCache==null) {
			this.addedCredentialCache=new ArrayList<AbstractCredential>();
		}
		return addedCredentialCache;
	}

	/**
	 * Sets the added credential cache.
	 *
	 * @param addedCredentialCache the new added credential cache
	 */
	public void setAddedCredentialCache(List<AbstractCredential> addedCredentialCache) {
		this.addedCredentialCache = addedCredentialCache;
	}

	/**
	 * Gets the modified credential cache.
	 *
	 * @return the modified credential cache
	 */
	public List<AbstractCredential> getModifiedCredentialCache() {
		if(this.modifiedCredentialCache==null) {
			this.modifiedCredentialCache=new ArrayList<AbstractCredential>();
		}
		return modifiedCredentialCache;
	}

	/**
	 * Sets the modified credential cache.
	 *
	 * @param modifiedCredentialCache the new modified credential cache
	 */
	public void setModifiedCredentialCache(List<AbstractCredential> modifiedCredentialCache) {
		this.modifiedCredentialCache = modifiedCredentialCache;
	}

    //---------------------------------------------------------------
	//----------From here Methods to fill/control Components---------
	//---------------------------------------------------------------
	
	/**
     * Fill the list with credential of a given type.
     *
     * @param type the type
     */
	public void fillCredentialJListWithType(CredentialType type) {
		List<AbstractCredential> credentials=WsCredentialStore.getInstance().getAllCredentialsOfaType(type);
		DefaultListModel<AbstractCredential> listModelRegisteredApis=new DefaultListModel<AbstractCredential>();
		listModelRegisteredApis.addAll(credentials);
		getJListCredentials().setModel(listModelRegisteredApis);
	}
	
	/**
	 * Fill the list with all possible credentials.
	 */
	public void fillCredentialJList() {
		List<AbstractCredential> credentials=WsCredentialStore.getInstance().getCredentialList();
		DefaultListModel<AbstractCredential> listModelRegisteredApis=new DefaultListModel<AbstractCredential>();
		listModelRegisteredApis.addAll(credentials);
		getJListCredentials().setModel(listModelRegisteredApis);
	}
	
	/**
	 * Fill the list with all possible credentials and repaint the panel.
	 */
	public void fillCredentialJListAndRepaint() {
		List<AbstractCredential> credentials=WsCredentialStore.getInstance().getCredentialList();
		DefaultListModel<AbstractCredential> listModelRegisteredApis=new DefaultListModel<AbstractCredential>();
		listModelRegisteredApis.addAll(credentials);
		getJListCredentials().setModel(listModelRegisteredApis);
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Opens JDialog credential creation.
	 *
	 * @param cred the cred, null if new credential should be create. For editing a credential add the credential as a parameter
	 */
	private void openJDialogCredentialCreation(AbstractCredential cred) {
		Window owner = OwnerDetection.getOwnerFrameForComponent(this);
		//If dialog ist used to edit a credential
		if (cred!=null) {
			JDialogCredentialCreation credEdit=new JDialogCredentialCreation(owner, cred);
			credEdit.setTitle("Edit a credential");
			this.setjDialogCredCreate(credEdit);
		} else {
			this.setjDialogCredCreate(new JDialogCredentialCreation(owner)); 
		}
		this.getJDialogCredCreate().setVisible(true);
		this.getJDialogCredCreate().addWindowListener(this);
	}
	
	
    //---------------------------------------------------------------
	//----------From here overridden Methods---------
	//---------------------------------------------------------------
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {

       if(this.getModifiedCredentialCache().size()>0) {
    	   return true;
       }
       
       if(this.getAddedCredentialCache().size()>0) {
    	   return true;
       }
       
       if(this.getDeletedCredentialsCache().size()>0) {
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
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(this.getJButtonCreateNewCredential())) {
			this.openJDialogCredentialCreation(null);
		} else if (ae.getSource().equals(this.getJButtonEditACredential())) {
			AbstractCredential cred = this.getJListCredentials().getSelectedValue();
			if (cred != null) {
				this.openJDialogCredentialCreation(cred);
			} else {
				JOptionPane.showConfirmDialog(this, "Please select a credential!",null,JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	/* (non-Javadoc)
	* @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	*/
	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.getJListCredentials().equals(e.getSource())) {
			if(e.getClickCount()==2) {
				int index = this.getJListCredentials().locationToIndex(e.getPoint());
				AbstractCredential cred=this.getJListCredentials().getModel().getElementAt(index);
				this.openJDialogCredentialCreation(cred);
			}
		}
	}

	/* (non-Javadoc)
	* @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	*/
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	/* (non-Javadoc)
	* @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	*/
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	/* (non-Javadoc)
	* @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	*/
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	

	/* (non-Javadoc)
	* @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	*/
	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	/* (non-Javadoc)
	* @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	*/
	@Override
	public void windowClosing(WindowEvent e) {
		if (e.getSource() == this.getJDialogCredCreate()) {
			if (this.getJDialogCredCreate().getModifiedCredential() != null) {
				this.getModifiedCredentialCache().add(this.getJDialogCredCreate().getModifiedCredential());
			}else if(this.getJDialogCredCreate().hasUnsavedChanges()) {
				try {
					if (this.getJDialogCredCreate().getCreatedCredential() != null) {
						this.getModifiedCredentialCache().add(this.getJDialogCredCreate().getModifiedCredential());
					}
				} catch (Exception e1) {
					//Do nothing, just check if the cred is there could be also because of created cred
				}
			}
			this.fillCredentialJListAndRepaint();
			try {
				if (this.getJDialogCredCreate().getCreatedCredential() != null) {
					this.getAddedCredentialCache().add(this.getJDialogCredCreate().getCreatedCredential());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

}