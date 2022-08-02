package de.enflexit.awb.ws.ui.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import agentgui.core.application.Application;
import de.enflexit.awb.ws.client.CredentialType;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.ui.WsConfigurationInterface;
import de.enflexit.awb.ws.ui.client.credentials.JDialogCredentialCreation;

public class JPanelCredentials extends JPanel implements ActionListener, WsConfigurationInterface {
	
	private static final long serialVersionUID = -1252151357398930115L;
	
	private JLabel jLabelCredentialList;
	private JScrollPane jScrollPaneCredentialList;
	private JList<AbstractCredential> jListCredentials;
	private GridBagConstraints gbc_jPanelCredentials;
	private JButton jButtonNewCredentials;
	
	public JPanelCredentials() {
		initialize();
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelCredentialList = new GridBagConstraints();
		gbc_jLabelCredentialList.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelCredentialList.anchor = GridBagConstraints.WEST;
		gbc_jLabelCredentialList.gridx = 0;
		gbc_jLabelCredentialList.gridy = 0;
		add(getJLabelCredentialList(), gbc_jLabelCredentialList);
		GridBagConstraints gbc_jScrollPaneCredentialList = new GridBagConstraints();
		gbc_jScrollPaneCredentialList.insets = new Insets(5, 0, 5, 0);
		gbc_jScrollPaneCredentialList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneCredentialList.gridx = 0;
		gbc_jScrollPaneCredentialList.gridy = 1;
		add(getJScrollPaneCredentialList(), gbc_jScrollPaneCredentialList);
		GridBagConstraints gbc_jButtonNewCredentials = new GridBagConstraints();
		gbc_jButtonNewCredentials.anchor = GridBagConstraints.WEST;
		gbc_jButtonNewCredentials.insets = new Insets(5, 0, 5, 0);
		gbc_jButtonNewCredentials.gridx = 0;
		gbc_jButtonNewCredentials.gridy = 2;
		add(getJButtonNewCredentials(), gbc_jButtonNewCredentials);
		gbc_jPanelCredentials = new GridBagConstraints();
		gbc_jPanelCredentials.fill = GridBagConstraints.BOTH;
		gbc_jPanelCredentials.gridx = 0;
		gbc_jPanelCredentials.gridy = 3;
	}

	private JLabel getJLabelCredentialList() {
		if (jLabelCredentialList == null) {
			jLabelCredentialList = new JLabel("Credentials");
			jLabelCredentialList.setFont(new Font("Dialog", Font.BOLD, 12));
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
	public JList<AbstractCredential> getJListCredentials() {
		if (jListCredentials == null) {
			jListCredentials = new JList<AbstractCredential>();
			jListCredentials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jListCredentials;
	}
	
	public JButton getJButtonNewCredentials() {
		if (jButtonNewCredentials == null) {
			jButtonNewCredentials = new JButton("New Credential");
			jButtonNewCredentials.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonNewCredentials.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonNewCredentials.addActionListener(this);
		}
		return jButtonNewCredentials;
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
		jListCredentials.setModel(listModelRegisteredApis);
	}
	
	/**
	 * Fill the list with all possible credentials 
	 */
	public void fillCredentialJList() {
		List<AbstractCredential> credentials=WsCredentialStore.getInstance().getCredentialList();
		DefaultListModel<AbstractCredential> listModelRegisteredApis=new DefaultListModel<AbstractCredential>();
		listModelRegisteredApis.addAll(credentials);
		jListCredentials.setModel(listModelRegisteredApis);
	}
	
	/**
	 * Fill the list with all possible credentials and repaint the panel
	 */
	public void fillCredentialJListAndRepaint() {
		List<AbstractCredential> credentials=WsCredentialStore.getInstance().getCredentialList();
		DefaultListModel<AbstractCredential> listModelRegisteredApis=new DefaultListModel<AbstractCredential>();
		listModelRegisteredApis.addAll(credentials);
		jListCredentials.setModel(listModelRegisteredApis);
		this.revalidate();
		this.repaint();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		
		if (this.jListCredentials == null) return false;

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
		
		if (ae.getSource()==this.getJButtonNewCredentials()) {
			this.openJDialogCredentialCreation();
		}
	}
	
	/**
	 * Opens JDialog credential creation.
	 */
	private void openJDialogCredentialCreation() {
		
		Window owner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
		JDialogCredentialCreation credCreate = new JDialogCredentialCreation(owner);
		credCreate.setVisible(true);
		credCreate.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fillCredentialJListAndRepaint();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				fillCredentialJListAndRepaint();
			}
		});
		
	}
	
}
