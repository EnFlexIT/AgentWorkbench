package de.enflexit.awb.core.update;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import de.enflexit.language.Language;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.update.repositoryModel.ProjectRepository;
import de.enflexit.common.http.WebResourcesAuthorization;
import de.enflexit.common.http.WebResourcesAuthorization.AuthorizationType;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * The Class AuthenticatationDialog.
 * 
 * @author Alexander Graute - SOFTEC - University of Duisburg-Essen
 */
public class AuthenticatationDialog extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = -6949198474833361353L;

	public enum StatusMessageType {
		NORMAL, ERROR, SUCCESSFUL
	}
	
	private JPanel jPanelContent;
	
	private JLabel jLabelAuthorizationType;
	private JComboBox<AuthorizationType> jComboBoxAuthrozationType;
	private JLabel jLabelUsername;
	private JTextField jTextFieldUsername;
	private JLabel jLabelPassword;
	private JButton jButtonSave;
	private JLabel jLabelStatusMessage;
	private JButton jButtonTestConnection;
	private JPasswordField JPasswordFieldPassword;
	private String updateSite;
	private WebResourcesAuthorization savedAuthorizationSettings;
	private String confirmButtonText = "Save";
	private Boolean showTestConnectionButton = true;
	
	private String statusMessage;
	private StatusMessageType statusMessageType;
	
	
	/**
	 * Instantiates a new project update settings dialog.
	 *
	 * @param frame the frame
	 * @param updateSite the update site
	 */
	public AuthenticatationDialog(Frame frame, String updateSite) {
		this(frame, null, updateSite);
	}
	/**
	 * Instantiates a new project update settings dialog.
	 *
	 * @param frame the frame
	 * @param authorization the update authorization
	 * @param updateSite the update site
	 */
	public AuthenticatationDialog(Frame frame, WebResourcesAuthorization authorization, String updateSite) {
		super(frame);
		
		this.updateSite = updateSite;
		
		this.setTitle(Language.translate("Authentisierungs-Einstellungen"));
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
		this.setModal(true);

		this.setSize(580, 240);
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	    
	    this.initialize();
		if (authorization != null && authorization.getType() == AuthorizationType.BASIC) {
			this.getJComboBoxAuthrozationType().setSelectedItem(authorization.getType());
			this.getJPasswordFieldPassword().setText(authorization.getPassword());
			this.getJTextFieldUsername().setText(authorization.getUsername());
		}
	}
	
	/**
	 * Initialize components.
	 */
	public void initialize() {
		
		
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.getJPanelContent().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelAuthorizationType = new GridBagConstraints();
		gbc_jLabelAuthorizationType.fill = GridBagConstraints.BOTH;
		gbc_jLabelAuthorizationType.insets = new Insets(15, 10, 5, 5);
		gbc_jLabelAuthorizationType.gridx = 0;
		gbc_jLabelAuthorizationType.gridy = 0;
		this.getJPanelContent().add(getJLabelAuthorizationType(), gbc_jLabelAuthorizationType);
		
		GridBagConstraints gbc_jComboBoxAuthrozationType = new GridBagConstraints();
		gbc_jComboBoxAuthrozationType.gridwidth = 2;
		gbc_jComboBoxAuthrozationType.fill = GridBagConstraints.BOTH;
		gbc_jComboBoxAuthrozationType.insets = new Insets(15, 0, 5, 10);
		gbc_jComboBoxAuthrozationType.gridx = 1;
		gbc_jComboBoxAuthrozationType.gridy = 0;
		this.getJPanelContent().add(getJComboBoxAuthrozationType(), gbc_jComboBoxAuthrozationType);
		
		GridBagConstraints gbc_jLabelUsername = new GridBagConstraints();
		gbc_jLabelUsername.fill = GridBagConstraints.BOTH;
		gbc_jLabelUsername.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelUsername.gridx = 0;
		gbc_jLabelUsername.gridy = 1;
		this.getJPanelContent().add(getJLabelUsername(), gbc_jLabelUsername);
		
		GridBagConstraints gbc_jTextFieldUsername = new GridBagConstraints();
		gbc_jTextFieldUsername.gridwidth = 2;
		gbc_jTextFieldUsername.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldUsername.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldUsername.gridx = 1;
		gbc_jTextFieldUsername.gridy = 1;
		this.getJPanelContent().add(getJTextFieldUsername(), gbc_jTextFieldUsername);
		
		GridBagConstraints gbc_jLabelPassword = new GridBagConstraints();
		gbc_jLabelPassword.anchor = GridBagConstraints.WEST;
		gbc_jLabelPassword.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelPassword.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelPassword.gridx = 0;
		gbc_jLabelPassword.gridy = 2;
		this.getJPanelContent().add(getJLabelPassword(), gbc_jLabelPassword);
		GridBagConstraints gbc_JPasswordFieldPassword = new GridBagConstraints();
		gbc_JPasswordFieldPassword.gridwidth = 2;
		gbc_JPasswordFieldPassword.insets = new Insets(0, 0, 5, 10);
		gbc_JPasswordFieldPassword.fill = GridBagConstraints.BOTH;
		gbc_JPasswordFieldPassword.gridx = 1;
		gbc_JPasswordFieldPassword.gridy = 2;
		getJPanelContent().add(getJPasswordFieldPassword(), gbc_JPasswordFieldPassword);
		
		GridBagConstraints gbc_jLabelStatusMessage = new GridBagConstraints();
		gbc_jLabelStatusMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelStatusMessage.gridwidth = 2;
		gbc_jLabelStatusMessage.insets = new Insets(0, 0, 10, 10);
		gbc_jLabelStatusMessage.gridx = 1;
		gbc_jLabelStatusMessage.gridy = 4;
		getJPanelContent().add(getJLabelStatusMessage(), gbc_jLabelStatusMessage);
		
		GridBagConstraints gbc_jButtonSave = new GridBagConstraints();
		gbc_jButtonSave.insets = new Insets(0, 0, 10, 5);
		gbc_jButtonSave.anchor = GridBagConstraints.WEST;
		gbc_jButtonSave.gridx = 1;
		gbc_jButtonSave.gridy = 3;
		this.getJPanelContent().add(getJButtonSave(), gbc_jButtonSave);

		GridBagConstraints gbc_jButtonTestConnection = new GridBagConstraints();
		gbc_jButtonTestConnection.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButtonTestConnection.insets = new Insets(0, 0, 0, 10);
		gbc_jButtonTestConnection.anchor = GridBagConstraints.NORTH;
		gbc_jButtonTestConnection.gridx = 2;
		gbc_jButtonTestConnection.gridy = 3;
		this.getJPanelContent().add(getJButtonTestConnection(), gbc_jButtonTestConnection);
				
		this.setContentPane(this.getJPanelContent());
		
	}
	
	private JPanel getJPanelContent() {
		if (jPanelContent==null) {
			jPanelContent = new JPanel();
			
		}
		return jPanelContent;
	}
	
	private JLabel getJLabelAuthorizationType() {
		if (jLabelAuthorizationType == null) {
			jLabelAuthorizationType = new JLabel();
			jLabelAuthorizationType.setText(Language.translate("Authentisierungs-Methode"));
			jLabelAuthorizationType.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelAuthorizationType;
	}
	private JComboBox<AuthorizationType> getJComboBoxAuthrozationType() {
		if (jComboBoxAuthrozationType == null) {
			jComboBoxAuthrozationType = new JComboBox<AuthorizationType>();
			jComboBoxAuthrozationType.setModel(new DefaultComboBoxModel<AuthorizationType>(AuthorizationType.values()));
			jComboBoxAuthrozationType.addActionListener(this);
		}
		return jComboBoxAuthrozationType;
	}

	private JLabel getJLabelUsername() {
		if (jLabelUsername == null) {
			jLabelUsername = new JLabel();
			jLabelUsername.setLabelFor(getJTextFieldUsername());
			jLabelUsername.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelUsername.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelUsername.setText(Language.translate("Benutzername"));
		}
		return jLabelUsername;
	}
	private JTextField getJTextFieldUsername() {
		if (jTextFieldUsername == null) {
			jTextFieldUsername = new JTextField();
			jTextFieldUsername.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldUsername.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldUsername.setColumns(10);
		}
		return jTextFieldUsername;
	}
	private JLabel getJLabelPassword() {
		if (jLabelPassword == null) {
			jLabelPassword = new JLabel();
			jLabelPassword.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelPassword.setText(Language.translate("Passwort"));
			jLabelPassword.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return jLabelPassword;
	}
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton(Language.translate(this.getConfirmButtonText(), Language.EN));
			jButtonSave.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSave.setForeground(new Color(0, 153, 0));
			jButtonSave.setPreferredSize(new Dimension(120, 26));
			jButtonSave.addActionListener(this);
		}
		return jButtonSave;
	}
	
	private JLabel getJLabelStatusMessage() {
		if(jLabelStatusMessage == null) {
			jLabelStatusMessage = new JLabel(this.getStatusMessage());
			if(this.getStatusMessageType() == StatusMessageType.ERROR) {
				jLabelStatusMessage.setForeground(new Color(153, 0, 0));
			} else if(this.getStatusMessageType() == StatusMessageType.SUCCESSFUL) {
				jLabelStatusMessage.setForeground(new Color(0, 153, 0));
			} else {
				jLabelStatusMessage.setForeground(new Color(0, 0, 0));
			}
			jLabelStatusMessage.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelStatusMessage.setPreferredSize(new Dimension(60,39));
		}
		return jLabelStatusMessage;
	}
	private JButton getJButtonTestConnection() {
		if (jButtonTestConnection == null) {
			jButtonTestConnection = new JButton("Test Connection");
			jButtonTestConnection.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonTestConnection.setVerticalAlignment(SwingConstants.TOP);
			jButtonTestConnection.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonTestConnection.setVisible(this.getShowTestConnectionButton());
			getJButtonTestConnection().addActionListener(this);
		}
		return jButtonTestConnection;
	}
	private JPasswordField getJPasswordFieldPassword() {
		if (JPasswordFieldPassword == null) {
			JPasswordFieldPassword = new JPasswordField();
			JPasswordFieldPassword.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return JPasswordFieldPassword;
	}
	
	
	
	/**
	 * @return the savedAuthorizationSettings
	 */
	public WebResourcesAuthorization getSavedAuthorizationSettings() {
		return savedAuthorizationSettings;
	}

	/**
	 * Action performed.
	 *
	 * @param ae the ae
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == this.getJComboBoxAuthrozationType()) {
			this.changeAuthorizationType((AuthorizationType) this.getJComboBoxAuthrozationType().getSelectedItem());
			
		} else if (ae.getSource() == this.getJButtonSave() ) {
			if (this.testConnection()==true) {
				this.saveAuthorizationSettings();
				this.dispose();
			} else {
				String title = Language.translate("Save Credentials", Language.EN);
				String msg = Language.translate("Errors occurred during the connection. Save anyway?", Language.EN);
				int userAnswer = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null);
				if (userAnswer==JOptionPane.YES_OPTION) {
					this.saveAuthorizationSettings();
					this.dispose();
				} else if (userAnswer==JOptionPane.NO_OPTION) {
					this.dispose();
				}
			}
		} else if(ae.getSource() == this.getJButtonTestConnection()) {
			this.testConnection();
			
		} else {
			System.err.println("Unknown: action => " + ae.getSource().toString());
		}
	}
	
	/**
	 * Show all necessary form elements and hide all others
	 * @param AuthorizationType type
	 */
	private void changeAuthorizationType(AuthorizationType type) {
		boolean setEnabled = (type!=AuthorizationType.NONE);
		this.getJLabelUsername().setEnabled(setEnabled);
		this.getJLabelPassword().setEnabled(setEnabled);
		this.getJTextFieldUsername().setEnabled(setEnabled);
		this.getJPasswordFieldPassword().setEnabled(setEnabled);
	}
	
	/**
	 * Gets the project repository authorization from fields.
	 * @return the project repository authorization from fields
	 */
	private WebResourcesAuthorization getProjectRepositoryAuthorizationFromFields() {
		
		WebResourcesAuthorization authorizationSettings = null;
		
		AuthorizationType type = (AuthorizationType) this.getJComboBoxAuthrozationType().getSelectedItem();
		switch (type) {
		case NONE:
			authorizationSettings = new WebResourcesAuthorization(type, null, null);
			break;

		case BASIC:
			authorizationSettings = new WebResourcesAuthorization(AuthorizationType.BASIC, this.getJTextFieldUsername().getText(), new String(this.getJPasswordFieldPassword().getPassword()));
			break;
			
		case BEARER:
			authorizationSettings = new WebResourcesAuthorization(type, null, null);;
			break;
		}
		return authorizationSettings;
	}
	
	/**
	 * Save authorization settings.
	 */
	private void saveAuthorizationSettings() {
		this.savedAuthorizationSettings = this.getProjectRepositoryAuthorizationFromFields();
	}

	/**
	 * Test connection.
	 * @return true, if successful
	 */
	private boolean testConnection() {
		boolean successfulConnection = true;
		WebResourcesAuthorization authorizationSettings = this.getProjectRepositoryAuthorizationFromFields();
		this.setStatusMessageType(StatusMessageType.NORMAL);	
		this.getJLabelStatusMessage().setVisible(true);

		try {
			this.setStatusMessage(Language.translate("Connecting...", Language.EN));
			ProjectRepository.openConnectionToUpdateSite(updateSite, authorizationSettings);
			this.setStatusMessageType(StatusMessageType.SUCCESSFUL);
			this.setStatusMessage(Language.translate("Verbindung erfolgreich"));
			
		} catch (IOException | URISyntaxException | ProjectRepositoryUpdateException e) {
			successfulConnection = false;
			setStatusMessageType(StatusMessageType.ERROR);
			setStatusMessage(e.getLocalizedMessage());
		}
		return successfulConnection;
	}
	
	/**
	 * Reset JlabelStatusMessage.
	 */
	public void resetJLabelStatusMessage() {
		if (this.jLabelStatusMessage != null) {
			this.remove(jLabelStatusMessage);
			this.jLabelStatusMessage = null;
		}
	}


	/**
	 * Gets the status message.
	 *
	 * @return the status message
	 */
	public String getStatusMessage() {
		return statusMessage;
	}


	/**
	 * Sets the status message.
	 *
	 * @param statusMessage the new status message
	 */
	public void setStatusMessage(String statusMessage) {
		this.getJLabelStatusMessage().setText(statusMessage);
		this.statusMessage = statusMessage;
	}

	/**
	 * Gets the status message type.
	 *
	 * @return the statusMessageType
	 */
	public StatusMessageType getStatusMessageType() {
		return statusMessageType;
	}

	/**
	 * Sets the status message type.
	 *
	 * @param statusMessageType the statusMessageType to set
	 */
	public void setStatusMessageType(StatusMessageType statusMessageType) {
		if(statusMessageType == StatusMessageType.ERROR) {
			getJLabelStatusMessage().setForeground(new Color(153, 0, 0));
		} else if(statusMessageType == StatusMessageType.SUCCESSFUL) {
			getJLabelStatusMessage().setForeground(new Color(0, 153, 0));
		} else {
			getJLabelStatusMessage().setForeground(new Color(0, 0, 0));
		}
		this.statusMessageType = statusMessageType;
	}

	/**
	 * Gets the confirm button text.
	 *
	 * @return the confirmButtonText
	 */
	public String getConfirmButtonText() {
		return confirmButtonText;
	}

	/**
	 * Sets the confirm button text.
	 *
	 * @param confirmButtonText the confirmButtonText to set
	 */
	public void setConfirmButtonText(String confirmButtonText) {
		this.getJButtonSave().setText(confirmButtonText);
		this.confirmButtonText = confirmButtonText;
	}

	/**
	 * Gets the show test connection button.
	 *
	 * @return the showTestConnectionButton
	 */
	public Boolean getShowTestConnectionButton() {
		return showTestConnectionButton;
	}

	/**
	 * Sets the show test connection button.
	 *
	 * @param showTestConnectionButton the showTestConnectionButton to set
	 */
	public void setShowTestConnectionButton(Boolean showTestConnectionButton) {
		this.showTestConnectionButton = showTestConnectionButton;
	}
	
	
	
}
