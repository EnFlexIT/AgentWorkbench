package de.enflexit.db.hibernate.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;
import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;
import de.enflexit.db.hibernate.connection.GeneralDatabaseSettings;

import javax.swing.JCheckBox;

/**
 * The Class DatabaseSettingsDialog can be used to configure the 
 * connection settings for hibernate.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DatabaseConnectionSettingsDialog extends JDialog implements ActionListener, HibernateStateVisualizationService {

	private static final long serialVersionUID = -5778880894988052682L;
	
	private enum DatabaseSettingsType {
		GeneralSettings,
		FactorySettings
	}
	
	private String currentFactoryID;
	
	private JTabbedPane jTabbedPaneSettings;
	private JPanel jPanelGeneralSettings;
	private JLabel jLabelHeaderGeneral;
	private DatabaseSettingsPanel jPanelGeneralDatabaseSettings;
	
	private JPanel jPanelFactorySettings;
	private JLabel jLabelHeader;
	private JLabel jLabelFactoryID;
	private DefaultComboBoxModel<String> comboBoxModelFactoryID;
	private JComboBox<String> jComboBoxFactoryID;
	private JSeparator separator;

	private JPanel JPanelNoDatabaseConnections;
	private DatabaseSettingsPanel jPanelDbSettings;

	private JPanel jPanelButtons;
	private JButton jButtonSave;
	private JButton jButtonTestConnection;
	private JButton jButtonClose;

	private Vector<String> userMessages;
	private JLabel jLabelFactroyState;
	private JCheckBox jCheckBoxUseForEveryFactory;
	
	/**
	 * Instantiates a new database dialog.
	 *
	 * @param owner the owner
	 * @param factoryID the factory ID
	 */
	public DatabaseConnectionSettingsDialog(Window owner, String factoryID) {
		super(owner);
		this.initialize();
		if (factoryID!=null && factoryID.isBlank()==false) {
			// --- Set selection to argument factoryID ----
			this.getJTabbedPaneSettings().setSelectedIndex(1);
			this.getJComboBoxFactoryID().setSelectedItem(factoryID);
		} else {
			if (this.getComboBoxModelFactoryID().getSize()>0) {
				// --- Reset current selection ------------
				this.getJComboBoxFactoryID().setSelectedItem(this.getFactoryIdSelected());
			}
			if (this.getJCheckBoxUseForEveryFactory().isSelected()==false) {
				this.getJTabbedPaneSettings().setSelectedIndex(1);
			}
		}
		this.updateSettingsUI();
		this.updateButtonUI();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setTitle("Database Connections & Settings");
		this.setSize(new Dimension(600, 580));
		this.setModal(true);
		
		this.registerEscapeKeyStroke();
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if (DatabaseConnectionSettingsDialog.this.isDoClose()==false) return;
				HibernateStateVisualizer.unregisterStateVisualizationService(DatabaseConnectionSettingsDialog.this);
				DatabaseConnectionSettingsDialog.this.setVisible(false);
			}
		});
		
		// --- Set Dialog position ----------------------------------
	    WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	    
	    // --- Register as state visualizer -------------------------
		HibernateStateVisualizer.registerStateVisualizationService(this);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{584, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_jTabbedPaneSettings = new GridBagConstraints();
		gbc_jTabbedPaneSettings.fill = GridBagConstraints.BOTH;
		gbc_jTabbedPaneSettings.gridx = 0;
		gbc_jTabbedPaneSettings.gridy = 0;
		this.getContentPane().add(this.getJTabbedPaneSettings(), gbc_jTabbedPaneSettings);

		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.insets = new Insets(10, 15, 15, 15);
		gbc_jPanelButtons.fill = GridBagConstraints.VERTICAL;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 1;
		this.getContentPane().add(getJPanelButtons(), gbc_jPanelButtons);
		
	}
	
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	if (DatabaseConnectionSettingsDialog.this.isDoClose()==false) return;
            	DatabaseConnectionSettingsDialog.this.setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
    
    private JTabbedPane getJTabbedPaneSettings() {
    	if (jTabbedPaneSettings==null) {
    		jTabbedPaneSettings = new JTabbedPane();
    		jTabbedPaneSettings.setBorder(BorderFactory.createEmptyBorder());
    		jTabbedPaneSettings.setFont(new Font("Dialog", Font.PLAIN, 12));
    		jTabbedPaneSettings.addTab(" General Settings  ", this.getJPanelGeneralSettings());
    		jTabbedPaneSettings.addTab(" Factory Settings  ", this.getJPanelFactorySettings());
    		jTabbedPaneSettings.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ce) {
					DatabaseConnectionSettingsDialog.this.updateButtonUI();
				}
			});
    	}
    	return jTabbedPaneSettings;
    }
    
    /**
     * Returns the DatabaseSettingsType.
     * @return the database settings type
     */
    private DatabaseSettingsType getDatabaseSettingsType() {
    	
    	DatabaseSettingsType dbSettingsType = null;
    	switch (this.getJTabbedPaneSettings().getSelectedIndex()) {
    	case 0:
    		dbSettingsType = DatabaseSettingsType.GeneralSettings;
    		break;
    	case 1:
    		dbSettingsType = DatabaseSettingsType.FactorySettings;
    		break;
    	}
    	return dbSettingsType;
    }
    
    private JPanel getJPanelGeneralSettings() {
    	if (jPanelGeneralSettings==null) {
    		jPanelGeneralSettings = new JPanel();
    		
    		GridBagLayout gridBagLayout = new GridBagLayout();
    		gridBagLayout.columnWidths = new int[]{0, 0};
    		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
    		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
    		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
    		jPanelGeneralSettings.setLayout(gridBagLayout);
    		
    		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
    		gbc_jLabelHeader.insets = new Insets(10, 15, 0, 10);
    		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
    		gbc_jLabelHeader.gridx = 0;
    		gbc_jLabelHeader.gridy = 0;
    		jPanelGeneralSettings.add(getJLabelHeaderGeneral(), gbc_jLabelHeader);
    		GridBagConstraints gbc_jCheckBoxUseForEveryFactory = new GridBagConstraints();
    		gbc_jCheckBoxUseForEveryFactory.anchor = GridBagConstraints.WEST;
    		gbc_jCheckBoxUseForEveryFactory.insets = new Insets(10, 10, 0, 10);
    		gbc_jCheckBoxUseForEveryFactory.gridx = 0;
    		gbc_jCheckBoxUseForEveryFactory.gridy = 1;
    		jPanelGeneralSettings.add(getJCheckBoxUseForEveryFactory(), gbc_jCheckBoxUseForEveryFactory);
    		
    		GridBagConstraints gbc_jPanelGeneralDbSettings = new GridBagConstraints();
    		gbc_jPanelGeneralDbSettings.fill = GridBagConstraints.BOTH;
    		gbc_jPanelGeneralDbSettings.insets = new Insets(10, 0, 0, 0);
    		gbc_jPanelGeneralDbSettings.gridx = 0;
    		gbc_jPanelGeneralDbSettings.gridy = 2;
    		jPanelGeneralSettings.add(getJPanelGeneralDatabaseSettings(), gbc_jPanelGeneralDbSettings);
    		
    	}
    	return jPanelGeneralSettings;
    }
    
    private JLabel getJLabelHeaderGeneral() {
		if (jLabelHeaderGeneral == null) {
			jLabelHeaderGeneral = new JLabel("General Database Connection Settings");
			jLabelHeaderGeneral.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelHeaderGeneral;
	}
    private JCheckBox getJCheckBoxUseForEveryFactory() {
		if (jCheckBoxUseForEveryFactory == null) {
			jCheckBoxUseForEveryFactory = new JCheckBox("Use settings below for every database connection");
			jCheckBoxUseForEveryFactory.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxUseForEveryFactory.addActionListener(this);
		}
		return jCheckBoxUseForEveryFactory;
	}
    private DatabaseSettingsPanel getJPanelGeneralDatabaseSettings() {
		if (jPanelGeneralDatabaseSettings == null) {
			jPanelGeneralDatabaseSettings = new DatabaseSettingsPanel(null);
			this.loadGeneralDatabaseSettings();
		}
		return jPanelGeneralDatabaseSettings;
	}
    
    /**
     * Returns the GeneralDatabaseSettings, currently edited.
     * @return the general database settings
     */
    private GeneralDatabaseSettings getGeneralDatabaseSettings() {
    	
    	// --- Get the DatabaseSettings from the panel --------------
    	DatabaseSettings dbs = this.getJPanelGeneralDatabaseSettings().getDatabaseSettings();
    	// --- Create the GeneralDatabaseSettings-instance ----------
    	GeneralDatabaseSettings gdbs = new GeneralDatabaseSettings();
    	gdbs.setUseForEveryFactory(this.getJCheckBoxUseForEveryFactory().isSelected());
    	gdbs.setDatabaseSystemName(dbs.getDatabaseSystemName());
    	gdbs.setHibernateDatabaseSettings(dbs.getHibernateDatabaseSettings());
    	return gdbs;
    }
    /**
     * Load general database settings.
     */
    private void loadGeneralDatabaseSettings() {
    	GeneralDatabaseSettings gdbs = DatabaseConnectionManager.getInstance().getGeneralDatabaseSettings();
    	this.getJCheckBoxUseForEveryFactory().setSelected(gdbs.isUseForEveryFactory());
    	this.getJPanelGeneralDatabaseSettings().setDatabaseSettings(gdbs);
    }
    
    
    private JPanel getJPanelFactorySettings() {
    	if (jPanelFactorySettings==null) {
    		jPanelFactorySettings = new JPanel();
    		
    		GridBagLayout gridBagLayout = new GridBagLayout();
    		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
    		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
    		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
    		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    		jPanelFactorySettings.setLayout(gridBagLayout);
    		
    		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
    		gbc_jLabelHeader.gridwidth = 3;
    		gbc_jLabelHeader.insets = new Insets(10, 10, 0, 10);
    		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
    		gbc_jLabelHeader.gridx = 0;
    		gbc_jLabelHeader.gridy = 0;
    		jPanelFactorySettings.add(getJLabelHeader(), gbc_jLabelHeader);
    		
    		GridBagConstraints gbc_jLabelFactoryID = new GridBagConstraints();
    		gbc_jLabelFactoryID.insets = new Insets(10, 10, 0, 0);
    		gbc_jLabelFactoryID.anchor = GridBagConstraints.EAST;
    		gbc_jLabelFactoryID.gridx = 0;
    		gbc_jLabelFactoryID.gridy = 1;
    		jPanelFactorySettings.add(getJLabelFactoryID(), gbc_jLabelFactoryID);
    	
    		GridBagConstraints gbc_jLabelFactroyState = new GridBagConstraints();
    		gbc_jLabelFactroyState.insets = new Insets(10, 0, 0, 0);
    		gbc_jLabelFactroyState.gridx = 1;
    		gbc_jLabelFactroyState.gridy = 1;
    		jPanelFactorySettings.add(getJLabelFactroyState(), gbc_jLabelFactroyState);
    		
    		GridBagConstraints gbc_jComboBoxFactoryID = new GridBagConstraints();
    		gbc_jComboBoxFactoryID.anchor = GridBagConstraints.WEST;
    		gbc_jComboBoxFactoryID.insets = new Insets(10, 10, 0, 10);
    		gbc_jComboBoxFactoryID.gridx = 2;
    		gbc_jComboBoxFactoryID.gridy = 1;
    		jPanelFactorySettings.add(getJComboBoxFactoryID(), gbc_jComboBoxFactoryID);
    		
    		GridBagConstraints gbc_separator = new GridBagConstraints();
    		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
    		gbc_separator.insets = new Insets(10, 10, 0, 10);
    		gbc_separator.gridwidth = 3;
    		gbc_separator.gridx = 0;
    		gbc_separator.gridy = 2;
    		jPanelFactorySettings.add(getSeparator(), gbc_separator);
    		
    		GridBagConstraints gbc_jPanelDbSettings = new GridBagConstraints();
    		gbc_jPanelDbSettings.gridwidth = 3;
    		gbc_jPanelDbSettings.insets = new Insets(0, 0, 5, 0);
    		gbc_jPanelDbSettings.fill = GridBagConstraints.BOTH;
    		gbc_jPanelDbSettings.gridx = 0;
    		gbc_jPanelDbSettings.gridy = 3;
    		if (this.getComboBoxModelFactoryID().getSize()==0) {
    			jPanelFactorySettings.add(this.getJPanelNoDatabaseConnections(), gbc_jPanelDbSettings);
    		} else {
    			jPanelFactorySettings.add(this.getJPanelDbSettings(), gbc_jPanelDbSettings);
    		}
    		
    	}
    	return jPanelFactorySettings;
	}
    
    private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Individual Database Connections & Settings");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelHeader;
	}
	private JLabel getJLabelFactoryID() {
		if (jLabelFactoryID == null) {
			jLabelFactoryID = new JLabel("Factory-ID:");
			jLabelFactoryID.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelFactoryID.setPreferredSize(new Dimension(99, 26));
		}
		return jLabelFactoryID;
	}
	private JLabel getJLabelFactroyState() {
		if (jLabelFactroyState == null) {
			jLabelFactroyState = new JLabel("");
			jLabelFactroyState.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelFactroyState.setPreferredSize(new Dimension(26, 26));
			jLabelFactroyState.setBackground(Color.YELLOW);
		}
		return jLabelFactroyState;
	}
	
	private DefaultComboBoxModel<String> getComboBoxModelFactoryID() {
		if (comboBoxModelFactoryID==null) {
			comboBoxModelFactoryID = new DefaultComboBoxModel<>();
			// --- Fill the model ----
			HibernateUtilities.getSessionFactoryIDList().forEach((String factoryID) -> comboBoxModelFactoryID.addElement(factoryID));
		}
		return comboBoxModelFactoryID;
	}
	private JComboBox<String> getJComboBoxFactoryID() {
		if (jComboBoxFactoryID == null) {
			DefaultComboBoxModel<String> cbm = this.getComboBoxModelFactoryID();
			jComboBoxFactoryID = new JComboBox<>(cbm);
			jComboBoxFactoryID.setToolTipText("Factory-ID of database connection");
			jComboBoxFactoryID.setPreferredSize(new Dimension(260, 26));
			jComboBoxFactoryID.setFont(new Font("Dialog", Font.PLAIN, 12));
			if (cbm.getSize()!=0) jComboBoxFactoryID.setSelectedItem(cbm.getElementAt(0));
			jComboBoxFactoryID.addActionListener(this);
		}
		return jComboBoxFactoryID;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}
    
	private JPanel getJPanelNoDatabaseConnections() {
		if (JPanelNoDatabaseConnections==null) {
			JPanelNoDatabaseConnections = new JPanel();
			JPanelNoDatabaseConnections.setLayout(new BorderLayout());
			JLabel jLabelFactoryID = new JLabel("No database connection services could be found!");
			jLabelFactoryID.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabelFactoryID.setHorizontalAlignment(JLabel.CENTER);
			JPanelNoDatabaseConnections.add(jLabelFactoryID, BorderLayout.CENTER);
		}
		return JPanelNoDatabaseConnections;
	}
	
    private DatabaseSettingsPanel getJPanelDbSettings() {
		if (jPanelDbSettings == null) {
			jPanelDbSettings = new DatabaseSettingsPanel(null);
		}
		return jPanelDbSettings;
	}
    /**
     * Sets the header text for the Dialog.
     * @param headerText the new header
     */
    public void setHeaderInJPanelDbSettings(String headerText) {
    	if (headerText!=null && headerText.isEmpty()==false) {
    		this.getJPanelDbSettings().setHeaderText(headerText);
    	}
	}
    /**
	 * Returns the current factory database settings.
	 * @return the database settings
	 */
	private DatabaseSettings getFactoryDatabaseSettings() {
		return this.getJPanelDbSettings().getDatabaseSettings();
	}
	/**
	 * Sets the factory database settings to work on.
	 * @param databaseSettings the new database settings
	 */
	private void setFactoryDatabaseSettings(DatabaseSettings databaseSettings) {
		this.getJPanelDbSettings().setDatabaseSettings(databaseSettings);
	}
	
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonSave = new GridBagConstraints();
			gbc_jButtonSave.insets = new Insets(0, 0, 0, 60);
			gbc_jButtonSave.gridx = 0;
			gbc_jButtonSave.gridy = 0;
			jPanelButtons.add(getJButtonSave(), gbc_jButtonSave);
			GridBagConstraints gbc_jButtonTestConnection = new GridBagConstraints();
			gbc_jButtonTestConnection.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonTestConnection.gridx = 1;
			gbc_jButtonTestConnection.gridy = 0;
			jPanelButtons.add(getJButtonTestConnection(), gbc_jButtonTestConnection);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(0, 60, 0, 0);
			gbc_jButtonCancel.gridx = 2;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonClose(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton("Save");
			jButtonSave.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSave.setForeground(new Color(0, 153, 0));
			jButtonSave.setPreferredSize(new Dimension(80, 26));
			jButtonSave.addActionListener(this);
		}
		return jButtonSave;
	}
	private JButton getJButtonTestConnection() {
		if (jButtonTestConnection == null) {
			jButtonTestConnection = new JButton("Test Connection");
			jButtonTestConnection.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonTestConnection.addActionListener(this);
		}
		return jButtonTestConnection;
	}
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton("Close");
			jButtonClose.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonClose.setForeground(new Color(153, 0, 0));
			jButtonClose.setPreferredSize(new Dimension(80, 26));
			jButtonClose.addActionListener(this);
		}
		return jButtonClose;
	}
	
	private void updateButtonUI() {
		
		int selectedTabIndex = this.getJTabbedPaneSettings().getSelectedIndex();
		boolean isUseSettingsForEveryFactory = this.getJCheckBoxUseForEveryFactory().isSelected();
		
		boolean activateSaveAndTestButton = (isUseSettingsForEveryFactory==true & selectedTabIndex==0) ||(isUseSettingsForEveryFactory==false & selectedTabIndex==1);
		this.getJButtonTestConnection().setEnabled(activateSaveAndTestButton);
	}
	
	// --------------------------------------------------------------	
	// --- From here some handling and modification classes ---------
	// --------------------------------------------------------------
	/**
	 * Returns the currently selected factory ID.
	 * @return the currently selected factory ID
	 */
	private String getFactoryIdSelected() {
		return (String) this.getJComboBoxFactoryID().getSelectedItem();
	}
	/**
	 * Returns the DatabaseSettings for the specified factory ID.
	 *
	 * @param factoryID the factory ID
	 * @return the database settings for factory ID
	 */
	private DatabaseSettings getDatabaseSettings(String factoryID) {
		return DatabaseConnectionManager.getInstance().getDatabaseSettings(factoryID);
	}
	/**
	 * Loads the database settings for the specified factory.
	 * @param factoryID the factory ID
	 */
	private void loadDatabaseSettings(String factoryID) {
		// --- Get settings from connection manager -------
		this.setFactoryDatabaseSettings(this.getDatabaseSettings(factoryID));
		this.setHeaderInJPanelDbSettings("Database Settings for connection '" + factoryID + "'");
		this.currentFactoryID = factoryID;
		this.updateFactoryStatus();
		boolean isUseGeneralSettings = this.getJCheckBoxUseForEveryFactory().isSelected();
		this.getJPanelDbSettings().setEnabled(!isUseGeneralSettings);
	}
	/**
	 * Save the database settings for the current database connection / factoryID.
	 *
	 * @param factoryID the factory ID
	 * @return true, if successful or if no changes were made
	 */
	private boolean saveDatabaseSettings(String factoryID) {
		
		boolean success = true;
		if (factoryID==null) {
			// --- If general settings are edited -------------------
			if (this.hasChangedGeneralDatabaseSettings()==true) {
				success = DatabaseConnectionManager.getInstance().saveGeneralDatabaseSettings(this.getGeneralDatabaseSettings());
			}
			
		} else {
			// --- If a factory connection is edited ----------------
			if (this.hasChangedFactoryDatabaseSettings(factoryID)==true) {
				success = DatabaseConnectionManager.getInstance().saveDatabaseConfigurationProperties(factoryID, this.getFactoryDatabaseSettings().getHibernateDatabaseSettings());
			}
		}
		return success;
	}
	
	/**
	 * Checks for changed general database settings.
	 * @return true, if we have changed settings
	 */
	private boolean hasChangedGeneralDatabaseSettings() {
		DatabaseSettings dbSettingsGeneralNew = this.getGeneralDatabaseSettings();
		DatabaseSettings dbSettingsGeneralOld = DatabaseConnectionManager.getInstance().getGeneralDatabaseSettings();
		return dbSettingsGeneralNew.equals(dbSettingsGeneralOld)==false;
	}
	/**
	 * Checks for changed factory database settings.
	 *
	 * @param factoryID the factory ID
	 * @return true, if we have changed settings
	 */
	private boolean hasChangedFactoryDatabaseSettings(String factoryID) {
		DatabaseSettings dbSettingsNew = this.getFactoryDatabaseSettings();
		DatabaseSettings dbSettingsOld = this.getDatabaseSettings(factoryID);
		return dbSettingsNew.equals(dbSettingsOld)==false;
	}
	
	/**
	 * Checks if the database settings have changed.
	 * @return true, if is changed database settings
	 */
	private boolean isSaveChangedDatabaseSettings() {
		
		// --- If general settings are edited -----------------------
		if (this.hasChangedGeneralDatabaseSettings()==true) {
			// --- Save changes? ------------------------------------
			String title = "Save the changes of the general database settings?";
			String message = "Would you like to save the changes of the general database settings?";
			int userAnswer = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (userAnswer==JOptionPane.YES_OPTION) {
				// --- Save the current settings --------------------
				return true;
			}
		}
		
		// --- If a factory connection is edited --------------------
		String factoryID = this.getFactoryIdSelected();
		if (factoryID==null) return false;
		if (this.hasChangedFactoryDatabaseSettings(factoryID)==true) {
			// --- Save changes? ------------------------------------
			String title = "Save the changes of the database settings?";
			String message = "Would you like to save the changes for the \ndatabase connection of '" + factoryID + "'?";
			int userAnswer = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (userAnswer==JOptionPane.YES_OPTION) {
				// --- Save the current settings --------------------
				return true;
			}
		}
		return false;
		
	}
	/**
	 * Checks if the current factory ID has changed.
	 *
	 * @param newFactoryID the new factory ID
	 * @return true, if is changed factory ID
	 */
	private boolean isChangedFactoryID(String newFactoryID) {
		if (this.currentFactoryID==null || newFactoryID.equals(newFactoryID)) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.HibernateStateVisualizationService#setSessionFactoryState(java.lang.String, de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState)
	 */
	@Override
	public void setSessionFactoryState(String factoryID, SessionFactoryState sessionFactoryState) {
		if (factoryID.equals(this.currentFactoryID)==true) {
			this.getJLabelFactroyState().setIcon(sessionFactoryState.getIconImage());
			this.getJLabelFactroyState().setToolTipText(sessionFactoryState.getDescription());
		}
	}
	
	/**
	 * Updates the factory status.
	 */
	private void updateFactoryStatus() {
		SessionFactoryState sfm = HibernateUtilities.getSessionFactoryMonitor(this.currentFactoryID).getSessionFactoryState();
		this.getJLabelFactroyState().setIcon(sfm.getIconImage());
		this.getJLabelFactroyState().setToolTipText(sfm.getDescription());
	}
	
	/**
	 * Update settings UI.
	 */
	private void updateSettingsUI() {

		boolean isUseForEveryFactory = this.getJCheckBoxUseForEveryFactory().isSelected();
		
		this.getJPanelGeneralDatabaseSettings().setEnabled(isUseForEveryFactory);

		this.getJComboBoxFactoryID().setEnabled(!isUseForEveryFactory);
		this.getJPanelDbSettings().setEnabled(!isUseForEveryFactory);
		this.updateButtonUI();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJCheckBoxUseForEveryFactory() ) {
			// --- Use connection settings for every factory -------- 
			this.updateSettingsUI();
			
		} else if (ae.getSource()==this.getJComboBoxFactoryID()) {
			// --- Selection of new FactoryID -----------------------
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// --- A new factory was selected? --------------
					if (DatabaseConnectionSettingsDialog.this.isChangedFactoryID(DatabaseConnectionSettingsDialog.this.getFactoryIdSelected())==false) return;
					// --- Save changed database settings? ----------
					if (DatabaseConnectionSettingsDialog.this.isSaveChangedDatabaseSettings()==true) {
						switch (DatabaseConnectionSettingsDialog.this.getDatabaseSettingsType()) {
						case GeneralSettings: 
							DatabaseConnectionSettingsDialog.this.saveDatabaseSettings(null);
							break;
						case FactorySettings:
							DatabaseConnectionSettingsDialog.this.saveDatabaseSettings(DatabaseConnectionSettingsDialog.this.currentFactoryID);							
							break;
						}
					}
					// --- Change to selected factory ---------------
					DatabaseConnectionSettingsDialog.this.loadDatabaseSettings(DatabaseConnectionSettingsDialog.this.getFactoryIdSelected());
				}
			});

		} else if (ae.getSource()==this.getJButtonSave()) {
			// --- Save the current settings ------------------------
			this.saveDatabaseSettings(null);
			this.saveDatabaseSettings(this.getFactoryIdSelected());

		} else if (ae.getSource()==this.getJButtonClose()) {
			// --- Close editing ------------------------------------
			if (this.isDoClose()==false) return;
			this.setVisible(false);
			
		} else if (ae.getSource()==this.getJButtonTestConnection()) {
			// --- Test connection ----------------------------------
			DatabaseSettings dbSettings = null;
			switch (this.getDatabaseSettingsType()) {
			case GeneralSettings:
				dbSettings = this.getJPanelGeneralDatabaseSettings().getDatabaseSettings();
				break;
				
			case FactorySettings:
				dbSettings = this.getJPanelDbSettings().getDatabaseSettings();
				break;
			}
			
			HibernateDatabaseService hds = HibernateUtilities.getDatabaseService(dbSettings.getDatabaseSystemName());
			if (hds!=null) {
				Properties props = dbSettings.getHibernateDatabaseSettings();
				this.clearUserMessages();
				if (hds.isDatabaseAccessible(props, this.getUserMessages(), true)==true) {
					JOptionPane.showMessageDialog(this, "Connection test successful!", "Connection Test", JOptionPane.INFORMATION_MESSAGE);
				} else {
					String message = "Connection test failed!";
					if (this.getUserMessages().isEmpty()==false) {
						message += "\n\n";
						for (String singelMessage : this.getUserMessages()) {
							message += singelMessage + "\n";
						}
					}
					JOptionPane.showMessageDialog(this, message, "Connection Test", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
	}
	
	/**
	 * Checks if the current dialog can be closed.
	 * @return true, if is do close
	 */
	private boolean isDoClose() {
		
		if (this.isSaveChangedDatabaseSettings()==true) {

			// --- Save factory DB settings of current first --------
			String currFactoryID = this.getFactoryIdSelected();
			boolean isSavedFactorySettings = this.saveDatabaseSettings(currFactoryID);
			// --- Wait for session factory re-creation -------------
			try {
				while (HibernateUtilities.isSessionFactoryInCreation(currFactoryID)==true) {
					Thread.sleep(100);
				}
			} catch (InterruptedException intEx) {
				intEx.printStackTrace();
			}
			
			// --- Save general DB settings -------------------------
			boolean isSavedGeneralSettings = this.saveDatabaseSettings(null);
			if (isSavedGeneralSettings==false || isSavedFactorySettings==false) {
				// --- An error occurred ----------------------------
				return false;
			}
		}
		return true;
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
	
}
