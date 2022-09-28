package de.enflexit.db.mySQL;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mysql.cj.jdbc.Driver;

import de.enflexit.db.hibernate.gui.AbstractDatabaseSettingsPanel;

/**
 * The Class MySQLSettingsPanel enables to configure the 
 * MySQL connection in a visual way.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class MySQLSettingsPanel extends AbstractDatabaseSettingsPanel {
	
	private static final long serialVersionUID = -736276260261466533L;
	
	private final Dimension textFieldSize = new Dimension(400, 26);

	private JLabel jLabelHostOrIP;
	private JLabel jLabelPort;
	private JLabel jLabelDatabase;
	private JLabel jLabelAddURLParameter;
	private JLabel jLabelURL;
	private JLabel jLabelUserName;
	private JLabel jLabelPassword;

	private JTextField jTextFieldHostOrIP;
	private JTextField jTextFieldPort;
	private JTextField jTextFieldDatabase;
	private JTextField jTextFieldAddUrlParams;
	private JTextField jTextFieldResultURL;
	private JTextField jTextFieldUserName;
	private JPasswordField jTextFieldPassword;

	private DocumentListener urlDocumentListener;
	
	/**
	 * Instantiates a new my SQL setting panel.
	 */
	public MySQLSettingsPanel() {
		this.initialize();
	}
	/**
	 * Initializes this panel.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{130, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHostOrIP = new GridBagConstraints();
		gbc_jLabelHostOrIP.anchor = GridBagConstraints.WEST;
		gbc_jLabelHostOrIP.insets = new Insets(10, 10, 5, 5);
		gbc_jLabelHostOrIP.gridx = 0;
		gbc_jLabelHostOrIP.gridy = 0;
		add(getJLabelHostOrIP(), gbc_jLabelHostOrIP);
		GridBagConstraints gbc_jTextFieldHostOrIP = new GridBagConstraints();
		gbc_jTextFieldHostOrIP.insets = new Insets(10, 0, 5, 0);
		gbc_jTextFieldHostOrIP.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldHostOrIP.gridx = 1;
		gbc_jTextFieldHostOrIP.gridy = 0;
		add(getJTextFieldHostOrIP(), gbc_jTextFieldHostOrIP);
		GridBagConstraints gbc_jLabelPort = new GridBagConstraints();
		gbc_jLabelPort.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelPort.anchor = GridBagConstraints.WEST;
		gbc_jLabelPort.gridx = 0;
		gbc_jLabelPort.gridy = 1;
		add(getJLabelPort(), gbc_jLabelPort);
		GridBagConstraints gbc_jTextFieldPort = new GridBagConstraints();
		gbc_jTextFieldPort.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldPort.gridx = 1;
		gbc_jTextFieldPort.gridy = 1;
		add(getJTextFieldPort(), gbc_jTextFieldPort);
		GridBagConstraints gbc_jLabelDatabase = new GridBagConstraints();
		gbc_jLabelDatabase.anchor = GridBagConstraints.WEST;
		gbc_jLabelDatabase.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelDatabase.gridx = 0;
		gbc_jLabelDatabase.gridy = 2;
		add(getJLabelDatabase(), gbc_jLabelDatabase);
		GridBagConstraints gbc_jTextFieldDatabase = new GridBagConstraints();
		gbc_jTextFieldDatabase.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldDatabase.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldDatabase.gridx = 1;
		gbc_jTextFieldDatabase.gridy = 2;
		add(getJTextFieldDatabase(), gbc_jTextFieldDatabase);
		GridBagConstraints gbc_jLabelURLParameter = new GridBagConstraints();
		gbc_jLabelURLParameter.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelURLParameter.anchor = GridBagConstraints.WEST;
		gbc_jLabelURLParameter.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelURLParameter.gridx = 0;
		gbc_jLabelURLParameter.gridy = 3;
		add(getJLabelURLParameter(), gbc_jLabelURLParameter);
		GridBagConstraints gbc_jTextFieldAddUrlParams = new GridBagConstraints();
		gbc_jTextFieldAddUrlParams.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldAddUrlParams.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldAddUrlParams.gridx = 1;
		gbc_jTextFieldAddUrlParams.gridy = 3;
		add(getJTextFieldAddUrlParams(), gbc_jTextFieldAddUrlParams);
		GridBagConstraints gbc_jLabelURL = new GridBagConstraints();
		gbc_jLabelURL.anchor = GridBagConstraints.WEST;
		gbc_jLabelURL.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelURL.gridx = 0;
		gbc_jLabelURL.gridy = 4;
		add(getJLabelURL(), gbc_jLabelURL);
		GridBagConstraints gbc_jLabelResultURL = new GridBagConstraints();
		gbc_jLabelResultURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelResultURL.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelResultURL.gridx = 1;
		gbc_jLabelResultURL.gridy = 4;
		add(getJTextFieldResultURL(), gbc_jLabelResultURL);
		GridBagConstraints gbc_jLabelUserName = new GridBagConstraints();
		gbc_jLabelUserName.anchor = GridBagConstraints.WEST;
		gbc_jLabelUserName.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelUserName.gridx = 0;
		gbc_jLabelUserName.gridy = 5;
		add(getJLabelUserName(), gbc_jLabelUserName);
		GridBagConstraints gbc_jTextFieldUserName = new GridBagConstraints();
		gbc_jTextFieldUserName.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldUserName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldUserName.gridx = 1;
		gbc_jTextFieldUserName.gridy = 5;
		add(getJTextFieldUserName(), gbc_jTextFieldUserName);
		GridBagConstraints gbc_jLabelPassword = new GridBagConstraints();
		gbc_jLabelPassword.anchor = GridBagConstraints.WEST;
		gbc_jLabelPassword.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelPassword.gridx = 0;
		gbc_jLabelPassword.gridy = 6;
		add(getJLabelPassword(), gbc_jLabelPassword);
		GridBagConstraints gbc_jTextFieldPassword = new GridBagConstraints();
		gbc_jTextFieldPassword.insets = new Insets(0, 0, 5, 0);
		gbc_jTextFieldPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldPassword.gridx = 1;
		gbc_jTextFieldPassword.gridy = 6;
		add(getJTextFieldPassword(), gbc_jTextFieldPassword);
	}
	
	private JLabel getJLabelHostOrIP() {
		if (jLabelHostOrIP == null) {
			jLabelHostOrIP = new JLabel("Host or IP");
			jLabelHostOrIP.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHostOrIP;
	}
	private JLabel getJLabelPort() {
		if (jLabelPort == null) {
			jLabelPort = new JLabel("Port (default: 3306)");
			jLabelPort.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPort;
	}
	private JLabel getJLabelDatabase() {
		if (jLabelDatabase == null) {
			jLabelDatabase = new JLabel("Database");
			jLabelDatabase.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDatabase;
	}
	private JLabel getJLabelURLParameter() {
		if (jLabelAddURLParameter == null) {
			jLabelAddURLParameter = new JLabel("Add. URL-Params.");
			jLabelAddURLParameter.setToolTipText("Additional URL Parameter");
			jLabelAddURLParameter.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelAddURLParameter;
	}
	private JLabel getJLabelURL() {
		if (jLabelURL == null) {
			jLabelURL = new JLabel("Resulting URL");
			jLabelURL.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelURL;
	}
	private JLabel getJLabelUserName() {
		if (jLabelUserName == null) {
			jLabelUserName = new JLabel("User Name");
			jLabelUserName.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelUserName;
	}
	private JLabel getJLabelPassword() {
		if (jLabelPassword == null) {
			jLabelPassword = new JLabel("Password");
			jLabelPassword.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelPassword;
	}
	// --- Value fields -----------------------------------
	private JTextField getJTextFieldHostOrIP() {
		if (jTextFieldHostOrIP == null) {
			jTextFieldHostOrIP = new JTextField();
			jTextFieldHostOrIP.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldHostOrIP.setPreferredSize(this.textFieldSize);
			jTextFieldHostOrIP.getDocument().addDocumentListener(this.getDocumentListenerForURL());
		}
		return jTextFieldHostOrIP;
	}
	private JTextField getJTextFieldPort() {
		if (jTextFieldPort == null) {
			jTextFieldPort = new JTextField();
			jTextFieldPort.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldPort.setPreferredSize(this.textFieldSize);
			jTextFieldPort.getDocument().addDocumentListener(this.getDocumentListenerForURL());
		}
		return jTextFieldPort;
	}
	private JTextField getJTextFieldDatabase() {
		if (jTextFieldDatabase == null) {
			jTextFieldDatabase = new JTextField();
			jTextFieldDatabase.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldDatabase.setPreferredSize(this.textFieldSize);
			jTextFieldDatabase.getDocument().addDocumentListener(this.getDocumentListenerForURL());
		}
		return jTextFieldDatabase;
	}
	private JTextField getJTextFieldAddUrlParams() {
		if (jTextFieldAddUrlParams == null) {
			jTextFieldAddUrlParams = new JTextField();
			jTextFieldAddUrlParams.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldAddUrlParams.setPreferredSize(this.textFieldSize);
			jTextFieldAddUrlParams.getDocument().addDocumentListener(this.getDocumentListenerForURL());
		}
		return jTextFieldAddUrlParams;
	}
	private JTextField getJTextFieldResultURL() {
		if (jTextFieldResultURL == null) {
			jTextFieldResultURL = new JTextField("");
			jTextFieldResultURL.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldResultURL.setPreferredSize(this.textFieldSize);
			jTextFieldResultURL.setEditable(false);
			jTextFieldResultURL.setOpaque(true);
			jTextFieldResultURL.setBackground(this.getBackground());
		}
		return jTextFieldResultURL;
	}
	private JTextField getJTextFieldUserName() {
		if (jTextFieldUserName == null) {
			jTextFieldUserName = new JTextField();
			jTextFieldUserName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldUserName.setPreferredSize(this.textFieldSize);
		}
		return jTextFieldUserName;
	}
	private JPasswordField getJTextFieldPassword() {
		if (jTextFieldPassword == null) {
			jTextFieldPassword = new JPasswordField();
			jTextFieldPassword.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldPassword.setPreferredSize(this.textFieldSize);
		}
		return jTextFieldPassword;
	}
	
	
	private DocumentListener getDocumentListenerForURL() {
		if (urlDocumentListener==null) {
			urlDocumentListener = new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent de) {
					this.setURLResult();
				}
				@Override
				public void insertUpdate(DocumentEvent de) {
					this.setURLResult();
				}
				@Override
				public void changedUpdate(DocumentEvent de) {
					this.setURLResult();
				}
				private void setURLResult() {
					getJTextFieldResultURL().setText(getUrlConfigured());
				}
			};
		}
		return urlDocumentListener;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.AbstractDatabaseSettingsPanel#setHibernateConfigurationProperties(java.util.Properties)
	 */
	@Override
	public void setHibernateConfigurationProperties(Properties properties) {
		
		if (properties==null) {
			// --- Get default properties -----------------
			properties = new MySQLDatabaseService().getHibernateDefaultPropertySettings();
		}

		String urlProperty = properties.getProperty(MySQLDatabaseService.HIBERNATE_PROPERTY_URL, "");

		// --- Remove the prefix of the URL ---------------
		String searchString = "//";
		int cutAt = urlProperty.indexOf(searchString);
		urlProperty = urlProperty.substring(cutAt + searchString.length());
		
		// --- Extract the additional parameter first -----
		String addParams = null;
		if (urlProperty.contains("?")==true) {
			addParams = urlProperty.substring(urlProperty.indexOf("?")+1);
			urlProperty = urlProperty.substring(0, urlProperty.indexOf("?"));
		}
		// --- Remove database from URL -------------------
		if (urlProperty.contains("/")==true) {
			urlProperty = urlProperty.substring(0, urlProperty.indexOf("/"));
		}
		// --- Try to get the port ------------------------
		String port = null;
		if (urlProperty.contains(":")==true) {
			port = urlProperty.substring(urlProperty.indexOf(":")+1);
			urlProperty = urlProperty.substring(0, urlProperty.indexOf(":"));
		}
		// --- Assign the URL or the port -----------------
		String urlOrIP = urlProperty;
		
		
		this.getJTextFieldHostOrIP().setText(urlOrIP);
		this.getJTextFieldPort().setText(port);
		this.getJTextFieldAddUrlParams().setText(addParams);
		
		this.getJTextFieldDatabase().setText(properties.getProperty(MySQLDatabaseService.HIBERNATE_PROPERTY_Catalog));
		this.getJTextFieldUserName().setText(properties.getProperty(MySQLDatabaseService.HIBERNATE_PROPERTY_UserName));
		this.getJTextFieldPassword().setText(properties.getProperty(MySQLDatabaseService.HIBERNATE_PROPERTY_Password));

	}

	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.AbstractDatabaseSettingsPanel#getHibernateConfigurationProperties()
	 */
	@Override
	public Properties getHibernateConfigurationProperties() {
		
		Properties props = new Properties();
		props.setProperty(MySQLDatabaseService.HIBERNATE_PROPERTY_DriverClass, Driver.class.getName()); 
		props.setProperty(MySQLDatabaseService.HIBERNATE_PROPERTY_URL, this.getUrlConfigured());
		props.setProperty(MySQLDatabaseService.HIBERNATE_PROPERTY_Catalog, this.getJTextFieldDatabase().getText());
		props.setProperty(MySQLDatabaseService.HIBERNATE_PROPERTY_UserName, this.getJTextFieldUserName().getText());
		props.setProperty(MySQLDatabaseService.HIBERNATE_PROPERTY_Password, new String(this.getJTextFieldPassword().getPassword()));
		return props;
	}
	
	/**
	 * Gets the current URL configured.
	 * @return the URL configured
	 */
	private String getUrlConfigured() {
		
		String url = "jdbc:mysql://[HostorIP]:[Port]/[Database][additionalParams]";
		
		String hostOrIP = this.getJTextFieldHostOrIP().getText().trim();
		String port = this.getJTextFieldPort().getText().trim();
		String database = this.getJTextFieldDatabase().getText().trim();
		String addParams = this.getJTextFieldAddUrlParams().getText().trim();
		
		url = url.replace("[HostorIP]", hostOrIP);
		url = url.replace("[Port]", port);
		url = url.replace("[Database]", database);
		if (addParams.isEmpty()==false) {
			url = url.replace("[additionalParams]", "?" + addParams);
		} else {
			url = url.replace("[additionalParams]", "");
		}
		return url;
	}
	@Override
	public void addDocumentListenerToTextFields(DocumentListener documentnListener) {
		this.getJTextFieldHostOrIP().getDocument().addDocumentListener(documentnListener);
		this.getJTextFieldPort().getDocument().addDocumentListener(documentnListener);
		this.getJTextFieldDatabase().getDocument().addDocumentListener(documentnListener);
		this.getJTextFieldAddUrlParams().getDocument().addDocumentListener(documentnListener);
		this.getJTextFieldUserName().getDocument().addDocumentListener(documentnListener);
		this.getJTextFieldPassword().getDocument().addDocumentListener(documentnListener);
	}
	
}
