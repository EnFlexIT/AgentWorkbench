package de.enflexit.df.core.workbook.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;
import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;
import de.enflexit.db.hibernate.gui.DatabaseConnectionSettingsDialog;
import de.enflexit.db.hibernate.gui.DatabaseSettings;
import de.enflexit.db.hibernate.gui.DatabaseSettingsPanel;
import de.enflexit.db.hibernate.gui.HibernateStateVisualizationService;
import de.enflexit.db.hibernate.gui.HibernateStateVisualizer;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.data.DatabaseHelper;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook4DB;


/**
 * The Class JPanelDataWorkbookInDB.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelDataWorkbookInDB extends JPanel implements DocumentListener, ActionListener, HibernateStateVisualizationService {

	private static final long serialVersionUID = -3104101192788727464L;
	
	private DataController dataController;
	private DataWorkbook4DB dataWorkbook;
	
	private boolean pauseDocumentListener;
	
	private JLabel jLabelCaptionID;
	private JLabel jLabelID;
	private JLabel jLableCaptionName;
	private JTextField jTextFieldName;
	private JLabel jLabelDescription;
	private JScrollPane jScrollPaneDescription;
	private JTextArea jTextAreaDescription;
	
	private JSeparator jSeparatorToDbSettings;
	private DatabaseSettingsPanel jPanelDbSettings;

	private JPanel jPanelFactorySettings;
	private JLabel jLabelFactoryID;
	private JLabel jLabelFactroyState;
	private DefaultComboBoxModel<String> comboBoxModelFactoryID;
	private JComboBox<String> jComboBoxFactoryID;

	private JToggleButton jToggleButtonSettingsManual;
	private JToggleButton jToggleButtonSettingsFactory;
	private JButton jButtonTestConnection;
	private JButton jButtonApply;
	private JButton jButtonEditFactorySettings;

	
	/**
	 * Instantiates a new j panel data workbook as file.
	 */
	public JPanelDataWorkbookInDB() {
		this(null, null);
	}
	/**
	 * Instantiates a  JPanelDataWorkbookInFile.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 */
	public JPanelDataWorkbookInDB(DataController dataController, DataWorkbook4DB dataWorkbook) {
		this.initialize();
		this.setDataController(dataController);
		this.setDataWorkbook(dataWorkbook);
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelCaptionID = new GridBagConstraints();
		gbc_jLabelCaptionID.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelCaptionID.anchor = GridBagConstraints.WEST;
		gbc_jLabelCaptionID.gridx = 0;
		gbc_jLabelCaptionID.gridy = 0;
		this.add(this.getJLabelCaptionID(), gbc_jLabelCaptionID);
		
		GridBagConstraints gbc_jLabelID = new GridBagConstraints();
		gbc_jLabelID.insets = new Insets(5, 5, 0, 5);
		gbc_jLabelID.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelID.gridx = 1;
		gbc_jLabelID.gridy = 0;
		this.add(this.getJLabelID(), gbc_jLabelID);
		
		GridBagConstraints gbc_jLableCaptionName = new GridBagConstraints();
		gbc_jLableCaptionName.anchor = GridBagConstraints.WEST;
		gbc_jLableCaptionName.insets = new Insets(5, 5, 0, 0);
		gbc_jLableCaptionName.gridx = 0;
		gbc_jLableCaptionName.gridy = 1;
		this.add(this.getJLableCaptionName(), gbc_jLableCaptionName);
		
		GridBagConstraints gbc_jTextFieldName = new GridBagConstraints();
		gbc_jTextFieldName.insets = new Insets(5, 5, 0, 5);
		gbc_jTextFieldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldName.gridx = 1;
		gbc_jTextFieldName.gridy = 1;
		this.add(this.getJTextFieldName(), gbc_jTextFieldName);
		
		GridBagConstraints gbc_jLabelDescription = new GridBagConstraints();
		gbc_jLabelDescription.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelDescription.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelDescription.gridx = 0;
		gbc_jLabelDescription.gridy = 2;
		this.add(this.getJLabelDescription(), gbc_jLabelDescription);
		
		GridBagConstraints gbc_jScrollPaneDescription = new GridBagConstraints();
		gbc_jScrollPaneDescription.insets = new Insets(5, 5, 0, 5);
		gbc_jScrollPaneDescription.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneDescription.gridx = 1;
		gbc_jScrollPaneDescription.gridy = 2;
		this.add(this.getJScrollPaneDescription(), gbc_jScrollPaneDescription);
		
		GridBagConstraints gbc_jSeparatorToDbSettings = new GridBagConstraints();
		gbc_jSeparatorToDbSettings.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparatorToDbSettings.gridwidth = 2;
		gbc_jSeparatorToDbSettings.insets = new Insets(10, 5, 0, 5);
		gbc_jSeparatorToDbSettings.gridx = 0;
		gbc_jSeparatorToDbSettings.gridy = 3;
		this.add(this.getJSeparatorToDbSettings(), gbc_jSeparatorToDbSettings);
		
		GridBagConstraints gbc_jLabelFactoryID = new GridBagConstraints();
		gbc_jLabelFactoryID.anchor = GridBagConstraints.WEST;
		gbc_jLabelFactoryID.insets = new Insets(10, 5, 0, 0);
		gbc_jLabelFactoryID.gridx = 0;
		gbc_jLabelFactoryID.gridy = 4;
		add(getJLabelFactoryID(), gbc_jLabelFactoryID);
		
		GridBagConstraints gbc_jPanelFactorySettings = new GridBagConstraints();
		gbc_jPanelFactorySettings.insets = new Insets(10, 5, 0, 5);
		gbc_jPanelFactorySettings.fill = GridBagConstraints.BOTH;
		gbc_jPanelFactorySettings.gridx = 1;
		gbc_jPanelFactorySettings.gridy = 4;
		add(this.getJPanelFactorySettings(), gbc_jPanelFactorySettings);
		
		GridBagConstraints gbc_jPanelDbSettings = new GridBagConstraints();
		gbc_jPanelDbSettings.insets = new Insets(10, 0, 0, 0);
		gbc_jPanelDbSettings.gridwidth = 2;
		gbc_jPanelDbSettings.fill = GridBagConstraints.BOTH;
		gbc_jPanelDbSettings.gridx = 0;
		gbc_jPanelDbSettings.gridy = 5;
		this.add(this.getJPanelDbSettings(), gbc_jPanelDbSettings);
	
		ButtonGroup bgDBSource = new ButtonGroup();
		bgDBSource.add(this.getJToggleButtonSettingsManual());
		bgDBSource.add(this.getJToggleButtonSettingsFactory());
	}
	
	/**
	 * Returns the controller.
	 * @return the controller
	 */
	private DataController getDataController() {
		return this.dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	/**
	 * Informs the data controller about settings changes.
	 */
	protected void informDataWorkbookSettingChanged() {
		if (this.getDataController()!=null) {
			this.getDataController().firePropertyChange(DataController.DC_DATA_WORKBOOK_CONFIGURATION_CHANGED, null, AffectedDataObjects.create(this.getDataWorkbook()));	
		}
	}
	
	/**
	 * Returns the data workbook.
	 * @return the data workbook
	 */
	public DataWorkbook4DB getDataWorkbook() {
		return dataWorkbook;
	}
	/**
	 * Sets the data workbook.
	 * @param dataWorkbook the new data workbook
	 */
	public void setDataWorkbook(DataWorkbook4DB dataWorkbook) {
		this.dataWorkbook = dataWorkbook;
		if (this.dataWorkbook==null) {
			this.getJLabelID().setText("");
			this.getJTextFieldName().setText("");
			this.getJTextAreaDescription().setText("");
		} else {
			this.pauseDocumentListener = true;
			this.getJLabelID().setText(this.dataWorkbook.getID() + "");
			this.getJTextFieldName().setText(this.dataWorkbook.getName());
			this.getJTextAreaDescription().setText(this.dataWorkbook.getDescription());
			this.pauseDocumentListener = false;
			
			if (this.dataWorkbook.getFactoryID()==null) {
				this.switchSourceOfDatabaseSettings(true);
			} else {
				this.switchSourceOfDatabaseSettings(false);
			}
			// TODO
		}
	}
	
	
	private JLabel getJLabelCaptionID() {
		if (jLabelCaptionID == null) {
			jLabelCaptionID = new JLabel("ID:");
			jLabelCaptionID.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelCaptionID.setForeground(Color.GRAY);
		}
		return jLabelCaptionID;
	}
	private JLabel getJLabelID() {
		if (jLabelID == null) {
			jLabelID = new JLabel();
			jLabelID.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelID.setForeground(Color.GRAY);
			jLabelID.setPreferredSize(new Dimension(80, 26));
		}
		return jLabelID;
	}
	
	private JLabel getJLableCaptionName() {
		if (jLableCaptionName == null) {
			jLableCaptionName = new JLabel("Name:");
			jLableCaptionName.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableCaptionName;
	}
	private JTextField getJTextFieldName() {
		if (jTextFieldName == null) {
			jTextFieldName = new JTextField();
			jTextFieldName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldName.setPreferredSize(new Dimension(80, 26));
			jTextFieldName.getDocument().addDocumentListener(this);
		}
		return jTextFieldName;
	}
	
	private JLabel getJLabelDescription() {
		if (jLabelDescription == null) {
			jLabelDescription = new JLabel("Description:");
			jLabelDescription.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDescription;
	}
	private JScrollPane getJScrollPaneDescription() {
		if (jScrollPaneDescription == null) {
			jScrollPaneDescription = new JScrollPane((Component) null);
			jScrollPaneDescription.setPreferredSize(new Dimension(360, 52));
			jScrollPaneDescription.setMinimumSize(new Dimension(360, 52));
			jScrollPaneDescription.setViewportView(getJTextAreaDescription());
		}
		return jScrollPaneDescription;
	}
	private JTextArea getJTextAreaDescription() {
		if (jTextAreaDescription == null) {
			jTextAreaDescription = new JTextArea();
			jTextAreaDescription.setText((String) null);
			jTextAreaDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAreaDescription.getDocument().addDocumentListener(this);
		}
		return jTextAreaDescription;
	}
	
	private JSeparator getJSeparatorToDbSettings() {
		if (jSeparatorToDbSettings == null) {
			jSeparatorToDbSettings = new JSeparator();
		}
		return jSeparatorToDbSettings;
	}
	
	
	private JPanel getJPanelFactorySettings() {
		if (jPanelFactorySettings == null) {
			jPanelFactorySettings = new JPanel();
			GridBagLayout gbl_jPanelFactorySettings = new GridBagLayout();
			gbl_jPanelFactorySettings.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelFactorySettings.rowHeights = new int[]{0, 0};
			gbl_jPanelFactorySettings.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelFactorySettings.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelFactorySettings.setLayout(gbl_jPanelFactorySettings);
			GridBagConstraints gbc_jLabelFactroyState = new GridBagConstraints();
			gbc_jLabelFactroyState.anchor = GridBagConstraints.EAST;
			gbc_jLabelFactroyState.gridx = 0;
			gbc_jLabelFactroyState.gridy = 0;
			jPanelFactorySettings.add(getJLabelFactroyState(), gbc_jLabelFactroyState);
			GridBagConstraints gbc_jComboBoxFactoryID = new GridBagConstraints();
			gbc_jComboBoxFactoryID.insets = new Insets(0, 5, 0, 0);
			gbc_jComboBoxFactoryID.fill = GridBagConstraints.HORIZONTAL;
			gbc_jComboBoxFactoryID.gridx = 1;
			gbc_jComboBoxFactoryID.gridy = 0;
			jPanelFactorySettings.add(getJComboBoxFactoryID(), gbc_jComboBoxFactoryID);
			GridBagConstraints gbc_jButtonEditFactorySettings = new GridBagConstraints();
			gbc_jButtonEditFactorySettings.insets = new Insets(0, 5, 0, 0);
			gbc_jButtonEditFactorySettings.gridx = 2;
			gbc_jButtonEditFactorySettings.gridy = 0;
			jPanelFactorySettings.add(getJButtonEditFactorySettings(), gbc_jButtonEditFactorySettings);
		}
		return jPanelFactorySettings;
	}
	private JLabel getJLabelFactoryID() {
		if (jLabelFactoryID == null) {
			jLabelFactoryID = new JLabel("Factory-ID:");
			jLabelFactoryID.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelFactoryID;
	}
	private JLabel getJLabelFactroyState() {
		if (jLabelFactroyState == null) {
			jLabelFactroyState = new JLabel("");
			jLabelFactroyState.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelFactroyState.setPreferredSize(new Dimension(26, 26));
			jLabelFactroyState.setBackground(Color.YELLOW);
			// --- Register as state visualizer -------------------------
			HibernateStateVisualizer.registerStateVisualizationService(this);
		}
		return jLabelFactroyState;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.HibernateStateVisualizationService#setSessionFactoryState(java.lang.String, de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState)
	 */
	@Override
	public void setSessionFactoryState(String factoryID, SessionFactoryState sessionFactoryState) {
		if (factoryID.equals(this.getJComboBoxFactoryID().getSelectedItem())==true) {
			this.getJLabelFactroyState().setIcon(sessionFactoryState.getIconImage());
			this.getJLabelFactroyState().setToolTipText(sessionFactoryState.getDescription());
		}
	}
	/**
	 * Updates the factory status.
	 */
	private void updateFactoryStatus() {
		SessionFactoryState sfm = HibernateUtilities.getSessionFactoryMonitor((String)this.getJComboBoxFactoryID().getSelectedItem()).getSessionFactoryState();
		this.getJLabelFactroyState().setIcon(sfm.getIconImage());
		this.getJLabelFactroyState().setToolTipText(sfm.getDescription());
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
			if (cbm.getSize()!=0) {
				jComboBoxFactoryID.setSelectedItem(cbm.getElementAt(0));
				this.updateFactoryStatus();
			}
			jComboBoxFactoryID.addActionListener(this);
		}
		return jComboBoxFactoryID;
	}
	private JButton getJButtonEditFactorySettings() {
		if (jButtonEditFactorySettings == null) {
			jButtonEditFactorySettings = new JButton();
			jButtonEditFactorySettings.setToolTipText("Edit factory settings ...");
			jButtonEditFactorySettings.setIcon(BundleHelper.getImageIcon("DatabaseFactoryEdit.png"));
			jButtonEditFactorySettings.setPreferredSize(new Dimension(26, 26));
			jButtonEditFactorySettings.addActionListener(this);
		}
		return jButtonEditFactorySettings;
	}
	/**
	 * Show database dialog for the specified session factory.
	 * @param factoryID the factory ID
	 */
	private void showDatabaseDialog(String factoryID) {
		
		Window parentWindow = OwnerDetection.getOwnerWindowForComponent(this.getParent());
		DatabaseConnectionSettingsDialog databaseConnectionSettingsDialog = new DatabaseConnectionSettingsDialog(parentWindow, factoryID);
		databaseConnectionSettingsDialog.setVisible(true);
		// - - - Wait for user - - - - - - - - -  
		databaseConnectionSettingsDialog.dispose();
		databaseConnectionSettingsDialog = null;
	}
	
	
	private DatabaseSettingsPanel getJPanelDbSettings() {
		if (jPanelDbSettings == null) {
			jPanelDbSettings = new DatabaseSettingsPanel(null);
			jPanelDbSettings.setOuterMargin(5);
			jPanelDbSettings.removeHeaderText();
		}
		return jPanelDbSettings;
	}
	
	
	public  List<JComponent> getConfigurationToolbarComponents() {
		List<JComponent> tbComponents = new ArrayList<>();
		tbComponents.add(this.getJToggleButtonSettingsManual());
		tbComponents.add(this.getJToggleButtonSettingsFactory());
		tbComponents.add(new JToolBar.Separator());
		tbComponents.add(this.getJButtonTestConnection());
		tbComponents.add(new JToolBar.Separator());
		tbComponents.add(this.getJButtonSave());
		return tbComponents;
	}
	private JToggleButton getJToggleButtonSettingsManual() {
		if (jToggleButtonSettingsManual == null) {
			jToggleButtonSettingsManual = new JToggleButton();
			jToggleButtonSettingsManual.setToolTipText("Configure settings manual");
			jToggleButtonSettingsManual.setIcon(BundleHelper.getImageIcon("MBsaveSettings.png"));
			jToggleButtonSettingsManual.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jToggleButtonSettingsManual.addActionListener(this);
			jToggleButtonSettingsManual.setSelected(true);
		}
		return jToggleButtonSettingsManual;
	}
	private JToggleButton getJToggleButtonSettingsFactory() {
		if (jToggleButtonSettingsFactory == null) {
			jToggleButtonSettingsFactory = new JToggleButton();
			jToggleButtonSettingsFactory.setToolTipText("Get settings from a DB-Factory ...");
			jToggleButtonSettingsFactory.setIcon(BundleHelper.getImageIcon("DatabaseFactory.png"));
			jToggleButtonSettingsFactory.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jToggleButtonSettingsFactory.addActionListener(this);
		}
		return jToggleButtonSettingsFactory;
	}
	private JButton getJButtonTestConnection() {
		if (jButtonTestConnection == null) {
			jButtonTestConnection = new JButton();
			jButtonTestConnection.setToolTipText("Test Connection Settings ...");
			jButtonTestConnection.setIcon(BundleHelper.getImageIcon("MBcheckRed.png"));
			jButtonTestConnection.addActionListener(this);
		}
		return jButtonTestConnection;
	}
	private JButton getJButtonSave() {
		if (jButtonApply == null) {
			jButtonApply = new JButton();
			jButtonApply.setToolTipText("Apply settings");
			jButtonApply.setIcon(BundleHelper.getImageIcon("MBsave.png"));
			jButtonApply.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent de) {
		this.onChange(de);
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent de) {
		this.onChange(de);
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent de) {
		this.onChange(de);
	}
	/**
	 * Reacts on document events.
	 * @param de the DocumentEvent
	 */
	private void onChange(DocumentEvent de) {
		
		if (this.pauseDocumentListener==true) return;
		
		if (de.getDocument()==this.getJTextFieldName().getDocument()) {
			this.getDataWorkbook().setName(this.getJTextFieldName().getText());
			this.informDataWorkbookSettingChanged();
		} else if (de.getDocument()==this.getJTextAreaDescription().getDocument()) {
			this.getDataWorkbook().setDescription(this.getJTextAreaDescription().getText());
			this.informDataWorkbookSettingChanged();
		}
	}
	
	/**
	 * Switch source of database settings.
	 * @param toManualSettings the to manual settings
	 */
	private void switchSourceOfDatabaseSettings(boolean toManualSettings) {
		
		this.getJPanelDbSettings().setVisible(toManualSettings);
		this.getJLabelFactoryID().setVisible(!toManualSettings);
		this.getJPanelFactorySettings().setVisible(!toManualSettings);
		this.validate();
		this.repaint();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJToggleButtonSettingsManual()) {
			// ----------------------------------------------------------------
			// --- Switch to manual DB connection -----------------------------
			// ----------------------------------------------------------------
			this.switchSourceOfDatabaseSettings(true);
		} else if (ae.getSource()==this.getJToggleButtonSettingsFactory()) {
			// ----------------------------------------------------------------
			// --- Switch to factory usage for DB connection ------------------
			// ----------------------------------------------------------------
			this.switchSourceOfDatabaseSettings(false);
		
		} else if (ae.getSource()==this.getJComboBoxFactoryID()) {
			// ----------------------------------------------------------------
			// --- Changed factory ID -----------------------------------------
			// ----------------------------------------------------------------
			this.updateFactoryStatus();
			
		} else if (ae.getSource()==this.getJButtonEditFactorySettings()) {
			// ----------------------------------------------------------------
			// --- Open the actual factory settings ---------------------------
			// ----------------------------------------------------------------
			String factorySelected = (String)this.getJComboBoxFactoryID().getSelectedItem();
			this.showDatabaseDialog(factorySelected);
			
		} else if (ae.getSource()==this.getJButtonTestConnection()) {
			// ----------------------------------------------------------------
			// --- Check the database connection ------------------------------
			// ----------------------------------------------------------------
			DatabaseSettings dbSettings = this.getJPanelDbSettings().getDatabaseSettings();
			if (this.getJToggleButtonSettingsFactory().isSelected()==true) {
				String factoryID = (String)this.getJComboBoxFactoryID().getSelectedItem();
				dbSettings = DatabaseConnectionManager.getInstance().getDatabaseSettings(factoryID);
			}
			if (DatabaseHelper.providesValidDatabaseSettings(dbSettings, true, this)==true) {
				this.getJButtonTestConnection().setIcon(BundleHelper.getImageIcon("MBcheckGreen.png"));
			} else {
				this.getJButtonTestConnection().setIcon(BundleHelper.getImageIcon("MBcheckRed.png"));
			}
			
		} else if (ae.getSource()==this.getJButtonSave()) {
			// ----------------------------------------------------------------
			// --- Apply and save settings ------------------------------------
			// ----------------------------------------------------------------
			this.getDataWorkbook().setName(this.getJTextFieldName().getText());
			this.getDataWorkbook().setDescription(this.getJTextAreaDescription().getText());
			
			if (this.getJToggleButtonSettingsFactory().isSelected()==true) {
				// --- For factory settings -----------------------------------
				String factoryID = (String) this.getJComboBoxFactoryID().getSelectedItem();
				this.getDataWorkbook().setFactoryID(factoryID);
				this.getDataWorkbook().setWorkbookDataSource(null);
			} else {
				// --- For manual configured connections ---------------------- 
				DatabaseSettings dbSettings = this.getJPanelDbSettings().getDatabaseSettings();
				this.getDataWorkbook().setFactoryID(null);
				this.getDataWorkbook().setWorkbookDataSource(dbSettings.toDataSource());
			}
			this.getDataWorkbook().save();
			this.informDataWorkbookSettingChanged();
			
		}
	}
	
}
