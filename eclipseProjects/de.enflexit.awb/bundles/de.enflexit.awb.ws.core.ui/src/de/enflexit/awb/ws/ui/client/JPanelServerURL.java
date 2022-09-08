package de.enflexit.awb.ws.ui.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import de.enflexit.awb.ws.client.ServerURL;
import de.enflexit.awb.ws.client.WsCredentialStore;

public class JPanelServerURL extends JPanel implements ActionListener{

	private static final long serialVersionUID = -4683248868011024312L;
	private JLabel jLableServer;
	private JScrollPane jScrollPaneServerUrl;
	private JList<ServerURL> jListServerUrl;
	private JButton jButtonCreateNewServer;
	private JButton jButtonDeleteServerUrl;
	private JButton jButtonEditAServerUrl;
	private JPanel jPanelHeader;

	
	public JPanelServerURL() {
		this.initialize();
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
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
	
	
	private JLabel getJLableServer() {
		if (jLableServer == null) {
			jLableServer = new JLabel("Server");
			jLableServer.setFont(new Font("Dialog", Font.BOLD, 12));
			jLableServer.setMinimumSize(new Dimension(150, 26));
			jLableServer.setPreferredSize(new Dimension(150, 26));

		}
		return jLableServer;
	}
	
    //-------------------------------------
	//----- From here overridden methods----
	//-------------------------------------
	
	private JScrollPane getJScrollPaneServerUrl() {
		if (jScrollPaneServerUrl == null) {
			jScrollPaneServerUrl = new JScrollPane();
			jScrollPaneServerUrl.setViewportView(getJListServerUrl());
		}
		return jScrollPaneServerUrl;
	}
	
	public JList<ServerURL> getJListServerUrl() {
		if (jListServerUrl == null) {
			jListServerUrl = new JList<ServerURL>();
			DefaultListModel<ServerURL> serverURLs = new DefaultListModel<ServerURL>();
			serverURLs.addAll(WsCredentialStore.getInstance().getServerURLList());
			jListServerUrl.setModel(serverURLs);
		}
		return jListServerUrl;
	}
	
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
	
	private JButton getJButtonDeleteServerUrl() {
		if (jButtonDeleteServerUrl == null) {
			jButtonDeleteServerUrl = new JButton(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDeleteServerUrl.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonDeleteServerUrl.setToolTipText("Delete a Server-URL");
			jButtonDeleteServerUrl.setPreferredSize(JPanelClientConfiguration.BUTTON_SIZE);
			jButtonDeleteServerUrl.addActionListener(this);
		}
		return jButtonDeleteServerUrl;
	}
	
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
	
	private void fillJListServerUrlAndRepaint() {
		DefaultListModel<ServerURL> serverURLs = new DefaultListModel<ServerURL>();
		List<ServerURL> serverURLsAsList=WsCredentialStore.getInstance().getServerURLList();
		serverURLs.addAll(serverURLsAsList);
		jListServerUrl.setModel(serverURLs);
		this.revalidate();
		this.repaint();
	}
	
	private void showJDialogServerUrl() {
		Window owner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
		JDialogCreateServerURL createServerDialog = new JDialogCreateServerURL(owner);
		createServerDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (createServerDialog.hasUnsavedChanges()) {
					WsCredentialStore.save(WsCredentialStore.getInstance());
					fillJListServerUrlAndRepaint();
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				if (createServerDialog.hasUnsavedChanges()) {
					WsCredentialStore.save(WsCredentialStore.getInstance());
					fillJListServerUrlAndRepaint();
				}
			}
		});

	}
	
	private void showJDialogForEditingAServerUrl() {
		if (getJListServerUrl().getSelectedValue() != null) {
			Window owner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
			JDialogCreateServerURL createServerDialog = new JDialogCreateServerURL(owner,getJListServerUrl().getSelectedValue());
			createServerDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (createServerDialog.hasUnsavedChanges()) {
						WsCredentialStore.save(WsCredentialStore.getInstance());
						fillJListServerUrlAndRepaint();
					}
				}

				@Override
				public void windowClosed(WindowEvent e) {
					if (createServerDialog.hasUnsavedChanges()) {
						WsCredentialStore.save(WsCredentialStore.getInstance());
						fillJListServerUrlAndRepaint();
					}
				}
			});
		} else {
			JOptionPane.showMessageDialog(this, "Please selected a Server-URL to edit!");
		}
	}

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
				if (serverURL != null) {
					int option =JOptionPane.showConfirmDialog(this, "Do you want to delete the Server with the following URL "+ serverURL.getServerURL()+"?","Deletion of a Server-URL", JOptionPane.YES_NO_CANCEL_OPTION);
					if(option==JOptionPane.YES_OPTION) {
					   WsCredentialStore.getInstance().getServerURLList().remove(serverURL);
					   this.fillJListServerUrlAndRepaint();
					   WsCredentialStore.getInstance().save();
					}
				} else {
					JOptionPane.showConfirmDialog(this, "Please select a Server-URL!","Select a Server-URL", JOptionPane.YES_OPTION);
				}
			}
		}
}
