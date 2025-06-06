package de.enflexit.oidc;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.language.Language;

/**
 * The Class OIDCPanel.
 */
public class OIDCPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -169367444435859302L;

	public static final String DEBUG_RESOURCE_URI = "https://se238124.zim.uni-due.de:18443/enflex-licensor-1.0-SNAPSHOT/api/private/test";

	public static final String DEBUG_CLIENT_ID = "testclient";
	public static final String DEBUG_CLIENT_SECRET = "b3b651a0-66a7-435e-8f1c-b1460bbfe9e0";
//	private static final String COMMAND_CONNECT = "connectOIDC";

	private OIDCAuthorization owner;

	private JLabel jLabelHeader;
	private JLabel jLabelUsername;
	private JTextField jTextFieldUsername;
	private JLabel jLabelPassword;
	private JPasswordField jPasswordField;
	private ActionListener parentGUI;
	private JButton jButtonConnect;
	private JLabel jLabelResult;


	/**
	 * Instantiates a new OIDC panel.
	 * @param owner the owner that is the current OIDCAuthorization
	 */
	public OIDCPanel(OIDCAuthorization owner) {
		super();
		this.owner = owner;

		GridBagLayout gridBagLayout = new GridBagLayout();

		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 24, 22, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		this.setLayout(gridBagLayout);

		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.gridwidth = 3;
		gbc_jLabelHeader.insets = new Insets(10, 10, 10, 10);
		gbc_jLabelHeader.gridy = 0;
		gbc_jLabelHeader.ipadx = 0;
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.weightx = 0.0;
		gbc_jLabelHeader.gridx = 0;
		this.add(this.getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jLabelUsername = new GridBagConstraints();
		gbc_jLabelUsername.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelUsername.anchor = GridBagConstraints.WEST;
		gbc_jLabelUsername.gridx = 0;
		gbc_jLabelUsername.gridy = 1;
		this.add(this.getJLabelUsername(), gbc_jLabelUsername);
		GridBagConstraints gbc_jTextFieldUsername = new GridBagConstraints();
		gbc_jTextFieldUsername.gridwidth = 2;
		gbc_jTextFieldUsername.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldUsername.gridx = 1;
		gbc_jTextFieldUsername.gridy = 1;
		this.add(this.getJTextFieldUsername(), gbc_jTextFieldUsername);
		GridBagConstraints gbc_jLabelPassword = new GridBagConstraints();
		gbc_jLabelPassword.anchor = GridBagConstraints.WEST;
		gbc_jLabelPassword.insets = new Insets(0, 10, 10, 5);
		gbc_jLabelPassword.gridx = 0;
		gbc_jLabelPassword.gridy = 2;
		this.add(this.getJLabelPassword(), gbc_jLabelPassword);
		GridBagConstraints gbc_jPasswordField = new GridBagConstraints();
		gbc_jPasswordField.gridwidth = 2;
		gbc_jPasswordField.insets = new Insets(0, 0, 10, 10);
		gbc_jPasswordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPasswordField.gridx = 1;
		gbc_jPasswordField.gridy = 2;
		this.add(this.getJPasswordField(), gbc_jPasswordField);
		GridBagConstraints gbc_jButtonConnect = new GridBagConstraints();
		gbc_jButtonConnect.anchor = GridBagConstraints.WEST;
		gbc_jButtonConnect.gridx = 1;
		gbc_jButtonConnect.gridy = 3;
		gbc_jButtonConnect.weightx = 0.0;
		gbc_jButtonConnect.insets = new Insets(0, 0, 0, 10);
		this.add(this.getJButtonConnect(), gbc_jButtonConnect);
		GridBagConstraints gbc_jLabelResult = new GridBagConstraints();
		gbc_jLabelResult.insets = new Insets(0, 0, 0, 10);
		gbc_jLabelResult.gridx = 2;
		gbc_jLabelResult.gridy = 3;
		add(this.getJLabelResult(), gbc_jLabelResult);

	}

	/**
	 * Sets the parent.
	 *
	 * @param parent the parent
	 * @return the OIDC panel
	 */
	public OIDCPanel setParent(ActionListener parent) {
		this.parentGUI = parent;
		return this; // for chaining
	}
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader==null) {
			jLabelHeader = new JLabel();
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelHeader.setText("Agent.Workbench & EOM: " + Language.translate("Web Service Authentifizierung"));
		}
		return jLabelHeader;
	}
	/**
	 * Gets the JLabel user name.
	 * @return the JLabel user name
	 */
	private JLabel getJLabelUsername() {
		if (jLabelUsername == null) {
			jLabelUsername = new JLabel(Language.translate("Benutzername"));
			jLabelUsername.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelUsername;
	}
	/**
	 * Gets the JTextField user name.
	 * @return the JTextField user name
	 */
	public JTextField getJTextFieldUsername() {
		if (jTextFieldUsername == null) {
			jTextFieldUsername = new JTextField();
			jTextFieldUsername.setPreferredSize(new Dimension(100, 26));
			jTextFieldUsername.setColumns(10);
			jTextFieldUsername.addActionListener(this);
		}
		return jTextFieldUsername;
	}
	/**
	 * Gets the JLabel password.
	 * @return the JLabel password
	 */
	private JLabel getJLabelPassword() {
		if (jLabelPassword == null) {
			jLabelPassword = new JLabel(Language.translate("Passwort"));
			jLabelPassword.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPassword;
	}
	/**
	 * Gets the JPasswordField password.
	 * @return the JPasswordField password
	 */
	public JPasswordField getJPasswordField() {
		if (jPasswordField == null) {
			jPasswordField = new JPasswordField();
			jPasswordField.setPreferredSize(new Dimension(100, 26));
			jPasswordField.setColumns(10);
			jPasswordField.addActionListener(this);
		}
		return jPasswordField;
	}
	/**
	 * Gets the JButton connect.
	 * @return the JButton connect
	 */
	private JButton getJButtonConnect() {
		if (jButtonConnect == null) {
			jButtonConnect = new JButton(Language.translate("Verbinden"));
			jButtonConnect.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonConnect.setForeground(AwbThemeColor.ButtonTextBlue.getColor());
			jButtonConnect.addActionListener(this);
		}
		return jButtonConnect;
	}
	/**
	 * Gets the JLabel result.
	 * @return the JLabel result
	 */
	private JLabel getJLabelResult() {
		if (jLabelResult == null) {
			jLabelResult = new JLabel("Result");
			jLabelResult.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelResult.setVisible(false);
		}
		return jLabelResult;
	}

	/**
	 * Display result.
	 *
	 * @param successful the successful
	 * @param message the message
	 */
	public void displayResult(boolean successful, String message) {
		this.getJLabelResult().setVisible(true);
		if (successful==true) {
			this.getJLabelResult().setText(Language.translate("Erfolgreich"));
			this.getJLabelResult().setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			this.getParent().getParent().getParent().setVisible(false);
		} else {
			this.getJLabelResult().setText(Language.translate("Fehlgeschlagen") + ": "+message);
			this.getJLabelResult().setForeground(AwbThemeColor.ButtonTextRed.getColor());
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		char[] pswd = getJPasswordField().getPassword();
		String userName = getJTextFieldUsername().getText().trim();

		if (ae.getSource()==this.getJTextFieldUsername()) {
			this.getJPasswordField().requestFocus();
			
		} else if (ae.getSource()==this.getJPasswordField()) {
			// --- Enter in password field ----------------
			if (this.isValidEdit(userName, pswd)==true) {
				this.getJButtonConnect().doClick();
			}
			
		} else if (ae.getSource()==this.getJButtonConnect()) {
			// --- Do Connect action ----------------------
			if (this.isValidEdit(userName, pswd)==true) {
				try {
					this.displayResult(owner.authorizeByUserAndPW(userName, new String(pswd)), null);
				} catch (URISyntaxException | IOException | KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
					this.displayResult(false, ex.getLocalizedMessage());
					ex.printStackTrace();
				}
			}

		} else {
			if (parentGUI != null) {
				parentGUI.actionPerformed(ae);
			}
		}
	}

	/**
	 * Checks if the current edits are valid enough.
	 *
	 * @param userName the user name
	 * @param pswd the pswd
	 * @return true, if is valid edit
	 */
	private boolean isValidEdit(String userName, char[] pswd) {
		
		boolean isValid = true;
		if (userName==null || userName.isEmpty()==true) {
			this.displayResult(false, Language.translate("Fehlender Benutzer"));
			isValid = false;
		}
		
		if (isValid==true && pswd.length==0) {
			this.displayResult(false, Language.translate("Fehlendes Passwort"));
			isValid = false;
		}
		return isValid;
	}
	
}
