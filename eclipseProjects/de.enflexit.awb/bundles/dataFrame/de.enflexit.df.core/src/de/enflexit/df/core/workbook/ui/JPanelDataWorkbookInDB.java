package de.enflexit.df.core.workbook.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.db.hibernate.gui.DatabaseSettingsPanel;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;
import javax.swing.JSeparator;


/**
 * The Class JPanelDataWorkbookInDB.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelDataWorkbookInDB extends JPanel implements DocumentListener, ActionListener {

	private static final long serialVersionUID = -3104101192788727464L;
	
	private DataController dataController;
	private DataWorkbook dataWorkbook;
	
	private JLabel jLabelCaptionID;
	private JLabel jLabelID;
	private JLabel jLableCaptionName;
	private JTextField jTextFieldName;
	private JLabel jLabelDescription;
	private JScrollPane jScrollPaneDescription;
	private JTextArea jTextAreaDescription;
	
	private JSeparator jSeparatorToDbSettings;
	private DatabaseSettingsPanel jPanelDbSettings;

	private JToggleButton jToggleButtonSettingsManual;
	private JToggleButton jToggleButtonSettingsFactory;
	private JButton jButtonTestConnection;
	private JButton jButtonApply;

	
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
	public JPanelDataWorkbookInDB(DataController dataController, DataWorkbook dataWorkbook) {
		this.initialize();
		this.setDataWorkbook(dataWorkbook);
		this.setDataController(dataController);
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
		gbc_jLabelDescription.anchor = GridBagConstraints.NORTH;
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
		
		GridBagConstraints gbc_jPanelDbSettings = new GridBagConstraints();
		gbc_jPanelDbSettings.insets = new Insets(10, 0, 0, 0);
		gbc_jPanelDbSettings.gridwidth = 2;
		gbc_jPanelDbSettings.fill = GridBagConstraints.BOTH;
		gbc_jPanelDbSettings.gridx = 0;
		gbc_jPanelDbSettings.gridy = 4;
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
			this.getDataController().firePropertyChange(DataController.DC_DATA_WORKBOOK_CONFIGURATION_CHANGED, null, null);	
		}
	}
	
	/**
	 * Returns the data workbook.
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook() {
		return dataWorkbook;
	}
	/**
	 * Sets the data workbook.
	 * @param dataWorkbook the new data workbook
	 */
	public void setDataWorkbook(DataWorkbook dataWorkbook) {
		this.dataWorkbook = dataWorkbook;
		if (this.dataWorkbook==null) {
			this.getJLabelID().setText("");
			this.getJTextFieldName().setText("");
			this.getJTextAreaDescription().setText("");
		} else {
			this.getJLabelID().setText(this.dataWorkbook.getID() + "");
			this.getJTextFieldName().setText(this.dataWorkbook.getName());
			this.getJTextAreaDescription().setText(this.dataWorkbook.getDescription());
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
		tbComponents.add(this.getJButtonTestConnection());
		tbComponents.add(new JToolBar.Separator());
		tbComponents.add(this.getJButtonApply());
		return tbComponents;
	}
	private JToggleButton getJToggleButtonSettingsManual() {
		if (jToggleButtonSettingsManual == null) {
			jToggleButtonSettingsManual = new JToggleButton();
			jToggleButtonSettingsManual.setToolTipText("Configure manual");
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
	private JButton getJButtonApply() {
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
		
		if (de.getDocument()==this.getJTextFieldName().getDocument()) {
			this.getDataWorkbook().setName(this.getJTextFieldName().getText());
			this.informDataWorkbookSettingChanged();
		} else if (de.getDocument()==this.getJTextAreaDescription().getDocument()) {
			this.getDataWorkbook().setDescription(this.getJTextAreaDescription().getText());
			this.informDataWorkbookSettingChanged();
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJToggleButtonSettingsManual()) {
			
		} else if (ae.getSource()==this.getJToggleButtonSettingsFactory()) {
			
		} else if (ae.getSource()==this.getJButtonTestConnection()) {
			
		} else if (ae.getSource()==this.getJButtonApply()) {
			
		}
	}
}
