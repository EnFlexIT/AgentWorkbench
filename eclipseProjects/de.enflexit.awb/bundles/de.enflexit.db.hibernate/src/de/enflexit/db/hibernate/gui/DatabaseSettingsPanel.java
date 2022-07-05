package de.enflexit.db.hibernate.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentListener;

import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;

/**
 * The Class DatabaseSettingsPanel enables to edit database connection data
 * in an individual context.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DatabaseSettingsPanel extends JPanel {

	private static final long serialVersionUID = -5778880894988052682L;
	
	private String databaseSystem;
	private DatabaseSettings databaseSettings;
	private AbstractDatabaseSettingsPanel abstractDatabaseSettingsPanel;
	
	private JLabel jLabelHeader;
	private JLabel jLabelDatabaseType;
	private JComboBox<String> jComboBoxDbType;
	private DefaultComboBoxModel<String> comboBoxModel;

	private JScrollPane jScrollPaneSettings;

	private boolean canceled;
	private Vector<String> userMessages;
	
	/**
	 * Instantiates a new database settings dialog.
	 * @param databaseSettings the actual database settings to configure
	 */
	public DatabaseSettingsPanel(DatabaseSettings databaseSettings) {
		this.initialize();
		this.setDatabaseSettings(databaseSettings);
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setSize(new Dimension(600, 500));
	    
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.gridwidth = 2;
		gbc_jLabelHeader.insets = new Insets(15, 15, 10, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		this.add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jLabelDatabaseType = new GridBagConstraints();
		gbc_jLabelDatabaseType.insets = new Insets(0, 15, 5, 5);
		gbc_jLabelDatabaseType.anchor = GridBagConstraints.WEST;
		gbc_jLabelDatabaseType.gridx = 0;
		gbc_jLabelDatabaseType.gridy = 1;
		this.add(getJLabelDatabaseType(), gbc_jLabelDatabaseType);
		GridBagConstraints gbc_jComboBoxDbType = new GridBagConstraints();
		gbc_jComboBoxDbType.anchor = GridBagConstraints.WEST;
		gbc_jComboBoxDbType.insets = new Insets(0, 5, 5, 0);
		gbc_jComboBoxDbType.gridx = 1;
		gbc_jComboBoxDbType.gridy = 1;
		this.add(getJComboBoxDbType(), gbc_jComboBoxDbType);
		GridBagConstraints gbc_jPanelSettings = new GridBagConstraints();
		gbc_jPanelSettings.gridwidth = 2;
		gbc_jPanelSettings.insets = new Insets(10, 15, 15, 15);
		gbc_jPanelSettings.fill = GridBagConstraints.BOTH;
		gbc_jPanelSettings.gridx = 0;
		gbc_jPanelSettings.gridy = 2;
		this.add(getJScrollPaneSettings(), gbc_jPanelSettings);
		
	}
   
    /**
     * Sets the header text for the Dialog.
     * @param headerText the new header
     */
    public void setHeaderText(String headerText) {
    	if (headerText!=null && headerText.isEmpty()==false) {
    		this.getJLabelHeader().setText(headerText);
    	}
	}
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Database Settings");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelHeader;
	}
	private JLabel getJLabelDatabaseType() {
		if (jLabelDatabaseType == null) {
			jLabelDatabaseType = new JLabel("Database System:");
			jLabelDatabaseType.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelDatabaseType.setPreferredSize(new Dimension(120, 26));
		}
		return jLabelDatabaseType;
	}
	private JComboBox<String> getJComboBoxDbType() {
		if (jComboBoxDbType == null) {
			jComboBoxDbType = new JComboBox<String>(this.getComboBoxModel());
			jComboBoxDbType.setPreferredSize(new Dimension(260, 26));
			jComboBoxDbType.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent ie) {
					if (ie.getStateChange()==ItemEvent.SELECTED) {
						// --- Get new selection and update UI ----------------
						String newDatavabeSystem = String.valueOf(DatabaseSettingsPanel.this.getJComboBoxDbType().getSelectedItem());
						if (newDatavabeSystem.isBlank()==false) {
							DatabaseSettingsPanel.this.setDatabaseSystem(newDatavabeSystem, false);
						}
					}
				}
			});
		}
		return jComboBoxDbType;
	}
	private DefaultComboBoxModel<String> getComboBoxModel() {
		if (comboBoxModel==null) {
			comboBoxModel = new DefaultComboBoxModel<>();
			List<String> dbSystems = HibernateUtilities.getDatabaseSystemList();
			for (int i = 0; i < dbSystems.size(); i++) {
				comboBoxModel.addElement(dbSystems.get(i));
			}
		}
		return comboBoxModel;
	}
	
	private JScrollPane getJScrollPaneSettings() {
		if (jScrollPaneSettings == null) {
			jScrollPaneSettings = new JScrollPane();
			jScrollPaneSettings.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		}
		return jScrollPaneSettings;
	}
	
	/**
	 * Checks if the dialog action was canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	/**
	 * Sets the canceled.
	 * @param canceled the new canceled
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	/**
	 * Returns the database settings.
	 * @return the database settings
	 */
	public DatabaseSettings getDatabaseSettings() {
		if (databaseSettings==null) {
			// --- Get the first registered database system ---------
			String dbSystem = HibernateUtilities.getDatabaseSystemList().get(0);
			Properties dbProps = null;
			if (dbSystem.equals(HibernateUtilities.DB_SERVICE_REGISTRATION_ERROR)==false) {
				databaseSettings = new DatabaseSettings(dbSystem, dbProps);
				this.setDatabaseSystem(dbSystem);
				databaseSettings.setHibernateDatabaseSettings(this.getDatabaseSettingPanel().getHibernateConfigurationProperties());
			}
			
		} else {
			// --- Always get configuration from visualization ------
			AbstractDatabaseSettingsPanel settingsPanel = this.getDatabaseSettingPanel();
			databaseSettings.setDatabaseSystemName(this.getDatabaseSystem());
			if (settingsPanel!=null) {
				databaseSettings.setHibernateDatabaseSettings(settingsPanel.getHibernateConfigurationProperties());
			}
		}
		return databaseSettings;
	}
	/**
	 * Sets the database settings.
	 * @param databaseSettings the new database settings
	 */
	public void setDatabaseSettings(DatabaseSettings databaseSettings) {
		if (databaseSettings==null) {
			this.databaseSettings = null;
			databaseSettings = this.getDatabaseSettings();
		}
		if (databaseSettings!=null) {
			String dbSystem = databaseSettings.getDatabaseSystemName();
			Properties dbProperties = databaseSettings.getHibernateDatabaseSettings();
			this.setDatabaseSystem(dbSystem);
			AbstractDatabaseSettingsPanel settingsPanel = this.getDatabaseSettingPanel();
			if (settingsPanel!=null) {
				settingsPanel.setHibernateConfigurationProperties(dbProperties);
			}
			this.databaseSettings = databaseSettings;
		} 
	}

	/**
	 * Gets the database system.
	 * @return the database system
	 */
	private String getDatabaseSystem() {
		return databaseSystem;
	}
	/**
	 * Sets the database system.
	 * @param newDatabaseSystem the new database system
	 */
	private void setDatabaseSystem(String newDatabaseSystem) {
		this.setDatabaseSystem(newDatabaseSystem, true);
	}
	/**
	 * Sets the database system to be used with the current configuration.
	 *
	 * @param newDatabaseSystem the new database system
	 * @param isUpdateComboBox the indicator to update the local combo box for selection
	 */
	private void setDatabaseSystem(String newDatabaseSystem, boolean isUpdateComboBox) {
		if (this.databaseSystem==null || this.databaseSystem.equals(newDatabaseSystem)==false) {
			// --- Check if the DB system connector is available --------------
			HibernateDatabaseService dbService = HibernateUtilities.getDatabaseService(newDatabaseSystem);
			if (dbService==null) {
				// --- No corresponding service found -------------------------
				String newDatabaseSystemFallback = HibernateUtilities.getDatabaseSystemList().get(0);
				if (newDatabaseSystemFallback==null || newDatabaseSystemFallback.equals(HibernateUtilities.DB_SERVICE_REGISTRATION_ERROR)) {
					System.err.println("Not a single HibernateDatabaseService could be found. - Please, make sure to load and start the database connection bundles!");
					return;
				} else {
					System.err.println("No HibernateDatabaseService could be found for the database system '" + newDatabaseSystem + "'. Please check, if the corresponding database bundle was loaded.");
					newDatabaseSystem = newDatabaseSystemFallback;
				}
			}
			// --- Set the database system selection --------------------------
			this.databaseSystem = newDatabaseSystem;
			if (isUpdateComboBox) this.getJComboBoxDbType().setSelectedItem(newDatabaseSystem);
			this.setDatabaseSettingPanel(newDatabaseSystem);
		}
	}

	/**
	 * Sets the database setting panel according to the specified database system.
	 * @param dbSystem the database system to use
	 */
	private void setDatabaseSettingPanel(String dbSystem) {
		// --- Get setting panel for database system ------
		HibernateDatabaseService hds = HibernateUtilities.getDatabaseService(dbSystem);
		if (hds==null) return;
		
		AbstractDatabaseSettingsPanel dbConfigPanel = hds.getHibernateSettingsPanel();
		// --- Get the configuration properties -----------
		Properties dbConfig = this.getDatabaseSettings().getHibernateDatabaseSettings();
		if (dbConfig==null) {
			dbConfig = hds.getHibernateDefaultPropertySettings();
		}
		dbConfigPanel.setHibernateConfigurationProperties(dbConfig);
		// --- Integrate the configuration panel ----------
		this.setDatabaseSettingPanel(dbConfigPanel);
	}
	/**
	 * Sets the database setting panel.
	 * @param abstractDatabaseSettingsPanel the new database setting panel
	 */
	private void setDatabaseSettingPanel(AbstractDatabaseSettingsPanel abstractDatabaseSettingsPanel) {
		this.abstractDatabaseSettingsPanel = abstractDatabaseSettingsPanel;
		this.getJScrollPaneSettings().setViewportView(this.abstractDatabaseSettingsPanel);
	}
	/**
	 * Returns the current database setting panel.
	 * @return the database setting panel
	 */
	private AbstractDatabaseSettingsPanel getDatabaseSettingPanel() {
		if (abstractDatabaseSettingsPanel==null) {
			HibernateDatabaseService hds = HibernateUtilities.getDatabaseService(this.getDatabaseSystem());
			if (hds!=null) {
				abstractDatabaseSettingsPanel = hds.getHibernateSettingsPanel();
				abstractDatabaseSettingsPanel.setHibernateConfigurationProperties(hds.getHibernateDefaultPropertySettings());
				this.getJScrollPaneSettings().setViewportView(this.abstractDatabaseSettingsPanel);
			}
		}
		return abstractDatabaseSettingsPanel;
	}
	
	
	/**
	 * Returns a message vector that can be used to inform the user about 
	 * configuration states (e.g. errors). This vector is especially used 
	 * in case of the JDBC connection test, to allow to provide more detailed
	 * information about the result of the connection test.
	 * 
	 * @return the user messages
	 */
	public Vector<String> getUserMessages() {
		if (userMessages==null) {
			userMessages = new Vector<>();
		}
		return userMessages;
	}
	/**
	 * Adds or prints the specified user message.
	 *
	 * @param userMessage the user message
	 * @param isError the is error
	 * @param printToConsole the print to console
	 */
	protected void addUserMessage(String userMessage, boolean printToConsole, boolean isError) {
		
		if (userMessage==null || userMessage.isEmpty()==true) return;
		
		this.getUserMessages().addElement(userMessage);
		if (printToConsole==true) {
			if (isError==true) {
				System.err.println(userMessage);
			} else {
				System.out.println(userMessage);
			}
		}
	}
	/**
	 * Clears all user messages.
	 */
	protected void clearUserMessages() {
		this.getUserMessages().clear();
	}

	/**
	 * This method allows to add a {@link DocumentListener} to all text fields of the panel,
	 * allowing to react on changes of the database configuration.
	 * @param documentnListener the {@link DocumentListener} to add
	 */
	public void addDocumentListenerToTextFields(DocumentListener documentnListener) {
		this.getDatabaseSettingPanel().addDocumentListenerToTextFields(documentnListener);
	}
	
}
