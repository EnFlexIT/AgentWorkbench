package de.enflexit.df.core.ui.dataSource;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.common.dataSources.CsvDataSource;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.common.swing.TimeFormatSelection;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.FileSelection;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.DataTreeNodeDataSourceCsv;

/**
 * The Class JPanelDataSourceConfigurationCsv.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @param <DataTreeNodeDataSourceCsv> the generic type
 */
public class JPanelDataSourceConfigurationCsv extends JPanelDataSourceConfiguration<DataTreeNodeDataSourceCsv> implements ActionListener, DocumentListener {

	private static final long serialVersionUID = 2214513797513629518L;
	
	private JLabel lblCsvImportConfiguration;
	private JLabel jLabelDataSourceName;
	private JTextField jTextFieldDataSourceName;
	private JLabel jLabelDescription;
	private JScrollPane jScrollPaneDescription;
	private JTextArea jTextAreaDescription;
	private JSeparator jSeparator;

	private JLabel jLabelFileName;
	private JTextField jTextFieldCsvFile;
	private JButton jButtonSelectCsvFile;
	private JLabel jLabelColumnSeparator;
	private JComboBox<String> jComboBoxColumnSeparator;
	private JCheckBox jCheckBoxHasHeadline;
	private JLabel jLabelDateFormat;
	private JLabel jLabelHasHeadline;
	private TimeFormatSelection jPanelTimeFormater;

	
	/**
	 * Instantiates a new JPanelDataSourceConfigurationCsv.
	 *
	 * @param dataController the data controller
	 * @param dsTreeNode the DataTreeNodeDataSourceCsv
	 */
	public JPanelDataSourceConfigurationCsv(DataController dataController, DataTreeNodeDataSourceCsv dsTreeNode) {
		super(dataController, dsTreeNode);
		this.initialize();
		this.setCsvDataSourceToUI();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JComponent getConfigurationPanel() {
		return this;
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		GridBagConstraints gbc_lblCsvImportConfiguration = new GridBagConstraints();
		gbc_lblCsvImportConfiguration.insets = new Insets(5, 5, 0, 0);
		gbc_lblCsvImportConfiguration.anchor = GridBagConstraints.WEST;
		gbc_lblCsvImportConfiguration.gridwidth = 3;
		gbc_lblCsvImportConfiguration.gridx = 0;
		gbc_lblCsvImportConfiguration.gridy = 0;
		add(getLblCsvImportConfiguration(), gbc_lblCsvImportConfiguration);
		GridBagConstraints gbc_jLabelDataSourceName = new GridBagConstraints();
		gbc_jLabelDataSourceName.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelDataSourceName.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataSourceName.gridx = 0;
		gbc_jLabelDataSourceName.gridy = 1;
		add(getJLabelDataSourceName(), gbc_jLabelDataSourceName);
		GridBagConstraints gbc_jTextFieldDataSourceName = new GridBagConstraints();
		gbc_jTextFieldDataSourceName.insets = new Insets(5, 5, 0, 0);
		gbc_jTextFieldDataSourceName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldDataSourceName.gridx = 1;
		gbc_jTextFieldDataSourceName.gridy = 1;
		add(getJTextFieldDataSourceName(), gbc_jTextFieldDataSourceName);
		GridBagConstraints gbc_jLabelDescription = new GridBagConstraints();
		gbc_jLabelDescription.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelDescription.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelDescription.gridx = 0;
		gbc_jLabelDescription.gridy = 2;
		add(getJLabelDescription(), gbc_jLabelDescription);
		GridBagConstraints gbc_jTextAreaDescription = new GridBagConstraints();
		gbc_jTextAreaDescription.insets = new Insets(5, 5, 0, 0);
		gbc_jTextAreaDescription.fill = GridBagConstraints.BOTH;
		gbc_jTextAreaDescription.gridx = 1;
		gbc_jTextAreaDescription.gridy = 2;
		add(getJScrollPaneDescription(), gbc_jTextAreaDescription);
		GridBagConstraints gbc_jSeparator = new GridBagConstraints();
		gbc_jSeparator.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparator.insets = new Insets(5, 5, 0, 5);
		gbc_jSeparator.gridwidth = 3;
		gbc_jSeparator.gridx = 0;
		gbc_jSeparator.gridy = 3;
		add(getJSeparator(), gbc_jSeparator);
		GridBagConstraints gbc_jLabelFileName = new GridBagConstraints();
		gbc_jLabelFileName.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelFileName.anchor = GridBagConstraints.WEST;
		gbc_jLabelFileName.gridx = 0;
		gbc_jLabelFileName.gridy = 4;
		add(getJLabelFileName(), gbc_jLabelFileName);
		GridBagConstraints gbc_jTextFieldCsvFile = new GridBagConstraints();
		gbc_jTextFieldCsvFile.insets = new Insets(5, 5, 0, 0);
		gbc_jTextFieldCsvFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldCsvFile.gridx = 1;
		gbc_jTextFieldCsvFile.gridy = 4;
		add(getJTextFieldCsvFile(), gbc_jTextFieldCsvFile);
		GridBagConstraints gbc_jButtonSelectCsvFile = new GridBagConstraints();
		gbc_jButtonSelectCsvFile.fill = GridBagConstraints.BOTH;
		gbc_jButtonSelectCsvFile.insets = new Insets(5, 5, 0, 5);
		gbc_jButtonSelectCsvFile.gridx = 2;
		gbc_jButtonSelectCsvFile.gridy = 4;
		add(getJButtonSelectCsvFile(), gbc_jButtonSelectCsvFile);
		GridBagConstraints gbc_jLabelColumnSeparator = new GridBagConstraints();
		gbc_jLabelColumnSeparator.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelColumnSeparator.anchor = GridBagConstraints.WEST;
		gbc_jLabelColumnSeparator.gridx = 0;
		gbc_jLabelColumnSeparator.gridy = 5;
		add(getJLabelColumnSeparator(), gbc_jLabelColumnSeparator);
		GridBagConstraints gbc_jComboBoxColumnSeparator = new GridBagConstraints();
		gbc_jComboBoxColumnSeparator.anchor = GridBagConstraints.WEST;
		gbc_jComboBoxColumnSeparator.insets = new Insets(5, 5, 0, 0);
		gbc_jComboBoxColumnSeparator.gridx = 1;
		gbc_jComboBoxColumnSeparator.gridy = 5;
		add(getJComboBoxColumnSeparator(), gbc_jComboBoxColumnSeparator);
		GridBagConstraints gbc_jLabelHasHeadline = new GridBagConstraints();
		gbc_jLabelHasHeadline.anchor = GridBagConstraints.WEST;
		gbc_jLabelHasHeadline.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelHasHeadline.gridx = 0;
		gbc_jLabelHasHeadline.gridy = 6;
		add(getJLabelHasHeadline(), gbc_jLabelHasHeadline);
		GridBagConstraints gbc_jCheckBoxHasHeadline = new GridBagConstraints();
		gbc_jCheckBoxHasHeadline.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxHasHeadline.insets = new Insets(5, 5, 0, 0);
		gbc_jCheckBoxHasHeadline.gridx = 1;
		gbc_jCheckBoxHasHeadline.gridy = 6;
		add(getJCheckBoxHasHeadline(), gbc_jCheckBoxHasHeadline);
		GridBagConstraints gbc_jLabelDateFormat = new GridBagConstraints();
		gbc_jLabelDateFormat.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelDateFormat.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelDateFormat.gridx = 0;
		gbc_jLabelDateFormat.gridy = 7;
		add(getJLabelDateFormat(), gbc_jLabelDateFormat);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(5, 5, 0, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 7;
		add(getJPanelTimeFormater(), gbc_panel);
	}


	private JLabel getLblCsvImportConfiguration() {
		if (lblCsvImportConfiguration == null) {
			lblCsvImportConfiguration = new JLabel("CSV Import Configuration");
			lblCsvImportConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return lblCsvImportConfiguration;
	}
	private JLabel getJLabelDataSourceName() {
		if (jLabelDataSourceName == null) {
			jLabelDataSourceName = new JLabel("Data-Source Name:");
			jLabelDataSourceName.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDataSourceName;
	}
	private JTextField getJTextFieldDataSourceName() {
		if (jTextFieldDataSourceName == null) {
			jTextFieldDataSourceName = new JTextField();
			jTextFieldDataSourceName.setPreferredSize(new Dimension(120, 26));
			jTextFieldDataSourceName.getDocument().addDocumentListener(this);
			jTextFieldDataSourceName.addActionListener(this);
		}
		return jTextFieldDataSourceName;
	}
	
	private JLabel getJLabelDescription() {
		if (jLabelDescription == null) {
			jLabelDescription = new JLabel("Description:");
			jLabelDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDescription;
	}
	private JScrollPane getJScrollPaneDescription() {
		if (jScrollPaneDescription==null) {
			jScrollPaneDescription = new JScrollPane(this.getJTextAreaDescription());
			jScrollPaneDescription.setPreferredSize(new Dimension(360, 52));
			jScrollPaneDescription.setMinimumSize(new Dimension(360, 52));
		}
		return jScrollPaneDescription;
	}
	private JTextArea getJTextAreaDescription() {
		if (jTextAreaDescription == null) {
			jTextAreaDescription = new JTextArea();
			jTextAreaDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAreaDescription.getDocument().addDocumentListener(this);
		}
		return jTextAreaDescription;
	}
	private JSeparator getJSeparator() {
		if (jSeparator == null) {
			jSeparator = new JSeparator();
		}
		return jSeparator;
	}
	
	private JLabel getJLabelFileName() {
		if (jLabelFileName == null) {
			jLabelFileName = new JLabel("CSV File:");
			jLabelFileName.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelFileName;
	}
	private JTextField getJTextFieldCsvFile() {
		if (jTextFieldCsvFile == null) {
			jTextFieldCsvFile = new JTextField();
			jTextFieldCsvFile.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldCsvFile.setEditable(false);
			jTextFieldCsvFile.setPreferredSize(new Dimension(120, 26));
		}
		return jTextFieldCsvFile;
	}
	private JButton getJButtonSelectCsvFile() {
		if (jButtonSelectCsvFile == null) {
			jButtonSelectCsvFile = new JButton();
			jButtonSelectCsvFile.setIcon(BundleHelper.getImageIcon("LoadFromFile.png"));
			jButtonSelectCsvFile.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonSelectCsvFile.setToolTipText("Select CSV file");
			jButtonSelectCsvFile.setPreferredSize(new Dimension(26, 26));
			jButtonSelectCsvFile.addActionListener(this);
		}
		return jButtonSelectCsvFile;
	}
	private JLabel getJLabelColumnSeparator() {
		if (jLabelColumnSeparator == null) {
			jLabelColumnSeparator = new JLabel("Value Separator:");
			jLabelColumnSeparator.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelColumnSeparator;
	}
	private JComboBox<String> getJComboBoxColumnSeparator() {
		if (jComboBoxColumnSeparator == null) {
			jComboBoxColumnSeparator = new JComboBox<String>();
			jComboBoxColumnSeparator.setModel(new DefaultComboBoxModel<>(CsvDataSource.COLUMN_SEPARATORS));
			jComboBoxColumnSeparator.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxColumnSeparator.addActionListener(this);
		}
		return jComboBoxColumnSeparator;
	}
	private JLabel getJLabelHasHeadline() {
		if (jLabelHasHeadline == null) {
			jLabelHasHeadline = new JLabel("Has Headline");
			jLabelHasHeadline.setToolTipText("Indicates if the CSV file has a headline");
			jLabelHasHeadline.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelHasHeadline;
	}
	private JCheckBox getJCheckBoxHasHeadline() {
		if (jCheckBoxHasHeadline == null) {
			jCheckBoxHasHeadline = new JCheckBox("");
			jCheckBoxHasHeadline.setToolTipText("Indicates if the CSV file has a headline");
			jCheckBoxHasHeadline.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxHasHeadline.addActionListener(this);
		}
		return jCheckBoxHasHeadline;
	}
	private JLabel getJLabelDateFormat() {
		if (jLabelDateFormat == null) {
			jLabelDateFormat = new JLabel("Date Format:");
			jLabelDateFormat.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDateFormat;
	}
	
	private TimeFormatSelection getJPanelTimeFormater() {
		if (jPanelTimeFormater==null) {
			jPanelTimeFormater = new TimeFormatSelection(false, 5, new Font("Dialog", Font.PLAIN, 12));
			jPanelTimeFormater.setPreferredSize(new Dimension(360, 70));
			jPanelTimeFormater.setMinimumSize(new Dimension(360, 70));
			jPanelTimeFormater.addActionListener(this);
		}
		return jPanelTimeFormater;
	}
	
	
	/**
	 * Returns the current instance of {@link CsvDataSource}.
	 * @return the csv data source
	 */
	private CsvDataSource getCsvDataSource() {
		return this.getDataTreeNodeDataSource().getDataSource();
	}
	
	/**
	 * Sets the csv data source to UI.
	 */
	private void setCsvDataSourceToUI() {
		
		CsvDataSource csvDS = this.getCsvDataSource();
		
		this.getJTextFieldDataSourceName().setText(csvDS.getName());
		this.getJTextAreaDescription().setText(csvDS.getDescription());
		
		this.getJTextFieldCsvFile().setText(csvDS.getCsvFilePath());
		this.getJTextFieldCsvFile().setToolTipText(csvDS.getCsvFilePath());
		
		this.getJComboBoxColumnSeparator().setSelectedItem(csvDS.getColumnSeparator());
		this.getJCheckBoxHasHeadline().setSelected(csvDS.isHeadline());
		this.getJPanelTimeFormater().setTimeFormat(csvDS.getDateTimeFormat());
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJTextFieldDataSourceName()) {
			this.getCsvDataSource().setName(this.getJTextFieldDataSourceName().getText());
			this.getJTextAreaDescription().requestFocus();
			this.informDataSourceSettingChanged(CsvDataSource.CHANGED_NAME);
			
		} else if (ae.getSource()==this.getJTextAreaDescription()) {
			this.getCsvDataSource().setDescription(this.getJTextAreaDescription().getText());
			this.informDataSourceSettingChanged(CsvDataSource.CHANGED_DESCRIPTION);
			
		} else if (ae.getSource()==this.getJButtonSelectCsvFile()) {
			// --- Ask user to select an csv file ------------------- 
			String previousFilePath = this.getCsvDataSource().getCsvFilePath();
			File previousFile = previousFilePath==null ? null : new File(previousFilePath);
			
			File csvFile = FileSelection.selectCsvFile(OwnerDetection.getOwnerWindowForComponent(this), previousFile, null);
			if (csvFile==null) return;

			String csvFilePath = csvFile.getAbsolutePath();
			this.getCsvDataSource().setCsvFilePath(csvFilePath);
			this.getJTextFieldCsvFile().setText(csvFilePath);
			this.getJTextFieldCsvFile().setToolTipText(csvFilePath);
			
			String csvFileName = csvFile.getName(); 
			this.getCsvDataSource().setName(csvFileName);
			this.getJTextFieldDataSourceName().setText(csvFileName);
			this.informDataSourceSettingChanged(CsvDataSource.CHANGED_CSV_FILE);
			
			this.getDataTreeNodeDataSource().loadData();
			
		} else if (ae.getSource()==this.getJComboBoxColumnSeparator()) {
			this.getCsvDataSource().setColumnSeparator((String)this.getJComboBoxColumnSeparator().getSelectedItem());
			this.informDataSourceSettingChanged(CsvDataSource.CHANGED_CSV_COLUMN_SEPARATOR);
			
			this.getDataTreeNodeDataSource().loadData();
			
		} else if (ae.getSource()==this.getJCheckBoxHasHeadline()) {
			this.getCsvDataSource().setHeadline(this.getJCheckBoxHasHeadline().isSelected());
			this.informDataSourceSettingChanged(CsvDataSource.CHANGED_CSV_HAS_HEADLINE);
			
			this.getDataTreeNodeDataSource().loadData();
			
		} else if (ae.getSource()==this.getJPanelTimeFormater()) {
			this.getCsvDataSource().setDateTimeFormat(this.getJPanelTimeFormater().getTimeFormat());
			this.informDataSourceSettingChanged(CsvDataSource.CHANGED_CSV_DATE_TIME_FORMAT);
			
			this.getDataTreeNodeDataSource().loadData();			
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent de) {
		this.onDocumentEvent(de);
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent de) {
		this.onDocumentEvent(de);
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent de) {
		this.onDocumentEvent(de);		
	}
	/**
	 * React on a document event.
	 * @param de the actual DocumentEvent
	 */
	private void onDocumentEvent(DocumentEvent de) {
		
		if (de.getDocument()==this.getJTextFieldDataSourceName().getDocument()) {
			// --- Changed name ---------------------------
			this.getCsvDataSource().setName(this.getJTextFieldDataSourceName().getText());
			this.informDataSourceSettingChanged(CsvDataSource.CHANGED_NAME);
			
		} else if (de.getDocument()==this.getJTextAreaDescription().getDocument()) {
			// --- Changed description --------------------
			this.getCsvDataSource().setDescription(this.getJTextAreaDescription().getText());
			this.informDataSourceSettingChanged(CsvDataSource.CHANGED_DESCRIPTION);
		}
	}
	
}
