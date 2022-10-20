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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import de.enflexit.awb.ws.client.CredentialAssignment;
import de.enflexit.awb.ws.client.CredentialType;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;
import de.enflexit.awb.ws.ui.client.credentials.JDialogCredentialCreation;


public class JPanelCredentials extends JPanel implements ActionListener,MouseListener,WsConfigurationInterface {
	
	private static final long serialVersionUID = -1252151357398930115L;
	
	private JLabel jLabelCredentialList;
	private JScrollPane jScrollPaneCredentialList;
	private JList<AbstractCredential> jListCredentials;
	private GridBagConstraints gbc_jPanelCredentials;
	private JPanel jPanelHeader;
	private JButton jButtonCreateNewCredential;
	private JButton jButtonEditACredential;
	private JButton jButtonDeleteACredential;
	
	
	public JPanelCredentials() {
		initialize();
	}
	
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
	}

	private JLabel getJLabelCredentialList() {
		if (jLabelCredentialList == null) {
			jLabelCredentialList = new JLabel("Credentials");
			jLabelCredentialList.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelCredentialList.setMinimumSize(new Dimension(150, 26));
			jLabelCredentialList.setPreferredSize(new Dimension(150, 26));
		}
		return jLabelCredentialList;
	}
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
			getJListCredentials().setCellRenderer(new ListCellRendererCredentials());
			fillCredentialJList();
		}
		return jListCredentials;
	}

	
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
	
	private JButton getJButtonEditACredential() {
		if (jButtonEditACredential == null) {
			jButtonEditACredential = new JButton(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonEditACredential.setToolTipText("Edit a new credential");
			jButtonEditACredential.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
			jButtonEditACredential.addActionListener(this);
		}
		return jButtonEditACredential;
	}
	
	private JButton getJButtonDeleteACredential() {
		if (jButtonDeleteACredential == null) {
			jButtonDeleteACredential = new JButton(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDeleteACredential.setToolTipText("Delete a credential");
			jButtonDeleteACredential.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonDeleteACredential.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
			jButtonDeleteACredential.addActionListener(this);
		}
		return jButtonDeleteACredential;
	}
    //---------------------------------------------------------------
	//----------From here Methods to fill/control Components---------
	//---------------------------------------------------------------
	
	/**
	 * Fill the list with credential of a given type
	 * @param CredentialType type
	 */
	public void fillCredentialJListWithType(CredentialType type) {
		List<AbstractCredential> credentials=WsCredentialStore.getInstance().getAllCredentialsOfaType(type);
		DefaultListModel<AbstractCredential> listModelRegisteredApis=new DefaultListModel<AbstractCredential>();
		listModelRegisteredApis.addAll(credentials);
		getJListCredentials().setModel(listModelRegisteredApis);
	}
	
	/**
	 * Fill the list with all possible credentials 
	 */
	public void fillCredentialJList() {
		List<AbstractCredential> credentials=WsCredentialStore.getInstance().getCredentialList();
		DefaultListModel<AbstractCredential> listModelRegisteredApis=new DefaultListModel<AbstractCredential>();
		listModelRegisteredApis.addAll(credentials);
		getJListCredentials().setModel(listModelRegisteredApis);
	}
	
	/**
	 * Fill the list with all possible credentials and repaint the panel
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
		Window owner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
		JDialogCredentialCreation credCreate = new JDialogCredentialCreation(owner);
		if(cred!=null) {
			credCreate.setCreatedCredential(cred);
		}
		credCreate.setVisible(true);
		credCreate.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fillCredentialJListAndRepaint();
				if(credCreate.hasUnsavedChanges()) {
					WsCredentialStore.save(WsCredentialStore.getInstance());
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				fillCredentialJListAndRepaint();
				if(credCreate.hasUnsavedChanges()) {
					WsCredentialStore.save(WsCredentialStore.getInstance());
				}
			}
		});
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

		if (this.jListCredentials == null)return false;
		List<AbstractCredential> credentialList = WsCredentialStore.getInstance().getCredentialList();
		ArrayList<AbstractCredential> credArrayList = new ArrayList<AbstractCredential>();

		ListModel<AbstractCredential> credModel = jListCredentials.getModel();
		for (int i = 0; i < credModel.getSize(); i++) {
			AbstractCredential cred = credModel.getElementAt(i);
			credArrayList.add(cred);
		}

		boolean sameList = credArrayList.equals(credentialList);
		return sameList;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {
		return hasUnsavedChanges();
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
				openJDialogCredentialCreation(cred);
			} else {
				JOptionPane.showConfirmDialog(this, "Please select a credential!",null,JOptionPane.INFORMATION_MESSAGE);
			}
		}else if (ae.getSource().equals(this.getJButtonDeleteACredential())) {
			AbstractCredential cred = this.getJListCredentials().getSelectedValue();
			if (cred != null) {
				int option =JOptionPane.showConfirmDialog(this, "Do you want to delete the "+ cred.getName()+ " of the type " + cred.getCredentialType()+" and all is corresponding Assignments?","Deletion of a credential", JOptionPane.YES_NO_CANCEL_OPTION);
				if(option==JOptionPane.YES_OPTION) {
				     // Remove all linked CredentialAssignments before deleting the credential
					 List<CredentialAssignment> credAssgns=WsCredentialStore.getInstance().getCredentialAssignmentWithCredential(cred);
					 for (CredentialAssignment credentialAssignment : credAssgns) {
						  WsCredentialStore.getInstance().getCredentialAssignmentList().remove(credentialAssignment);
			         }
				     //Remove the credential afterwards
					  WsCredentialStore.getInstance().getCredentialList().remove(cred);
					  this.fillCredentialJListAndRepaint();
					  WsCredentialStore.getInstance().save();
				}
			} else {
				JOptionPane.showConfirmDialog(this, "Please select a credential!","Select a credential", JOptionPane.YES_OPTION);
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.getJListCredentials().equals(e.getSource())) {
			if(e.getClickCount()==2) {
				int index = this.getJListCredentials().locationToIndex(e.getPoint());
				AbstractCredential cred=this.getJListCredentials().getModel().getElementAt(index);
				openJDialogCredentialCreation(cred);
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

}