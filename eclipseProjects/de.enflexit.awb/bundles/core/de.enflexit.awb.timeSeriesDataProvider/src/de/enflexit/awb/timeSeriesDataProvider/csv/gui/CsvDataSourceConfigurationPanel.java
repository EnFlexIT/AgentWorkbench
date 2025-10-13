package de.enflexit.awb.timeSeriesDataProvider.csv.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import java.awt.Dimension;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider.ConfigurationScope;
import de.enflexit.awb.timeSeriesDataProvider.csv.CsvDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.csv.CsvDataSourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel;
import de.enflexit.common.GlobalRuntimeValues;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JCheckBox;

/**
 * GUI panel for the configuration of a {@link CsvDataSeriesConfiguration}
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class CsvDataSourceConfigurationPanel extends AbstractDataSourceConfigurationPanel implements ActionListener, DocumentListener {

	private static final long serialVersionUID = 4169129076658770744L;
	
	private static final String DEFAULT_TIME_FORMAT = "dd.MM.yyyy HH:mm";
	
	public static final String TABLEMODEL_CHANGED = "TableModelChanged";
	
	private static final Dimension buttonSize = new Dimension(26, 26);
	
	private static final String ICON_PATH_CHECK_OKAY = "/icons/CheckGreen.png";
	private static final String ICON_PATH_CHECK_FAILED = "/icons/CheckRed.png";
	private static final String ICON_PATH_LOAD_FILE = "/icons/LoadFromFile.png";
	
	private static final String[] COLUMN_SEPARATORS = {";",",",":","."};
	
	private JLabel jLabelDataSourceName;
	private JTextField jTextFieldDataSourceName;
	private JLabel jLabelDateFormat;
	private JTextField jTextFieldDateFormat;
	private JButton jButtonFormatCheck;
	private JLabel jLabelFileName;
	private JTextField jTextFieldFileName;
	private JButton jButtonLoad;
	private JScrollPane jScrollPaneCsvDataTable;
	private JTable jTableCsvData;
	private JCheckBox jCheckBoxHasHeadline;
	private JLabel jLabelColumnSeparator;
	private JComboBox<String> jComboBoxColumnSeparator;

	private CsvDataSourceConfiguration currentSourceConfiguration;
	private DateTimeFormatter dateTimeFormatter;
	
	private File csvDataFile;
	private Vector<Vector<String>> csvData;
	private JLabel jLabelCsvDataSource;
	private CsvDataSeriesConfigurationPanel seriesConfigurationPanel;
	private JButton jButtonAutoGenerate;
	
	private boolean pauseListener;
	private JLabel jLabelConfigurationScope;
	private JComboBox<ConfigurationScope> jComboBoxConfigurationScope;
	
	/**
	 * Instantiates a new csv data source configuration panel.
	 */
	public CsvDataSourceConfigurationPanel() {
		this.initialize();
		this.setComponentsEnabled(false);
	}
	
	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelCsvDataSource = new GridBagConstraints();
		gbc_jLabelCsvDataSource.anchor = GridBagConstraints.WEST;
		gbc_jLabelCsvDataSource.gridwidth = 2;
		gbc_jLabelCsvDataSource.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelCsvDataSource.gridx = 0;
		gbc_jLabelCsvDataSource.gridy = 0;
		add(getJLabelCsvDataSource(), gbc_jLabelCsvDataSource);
		GridBagConstraints gbc_jLabelDataSourceName = new GridBagConstraints();
		gbc_jLabelDataSourceName.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelDataSourceName.anchor = GridBagConstraints.EAST;
		gbc_jLabelDataSourceName.gridx = 0;
		gbc_jLabelDataSourceName.gridy = 1;
		add(getJLabelDataSourceName(), gbc_jLabelDataSourceName);
		GridBagConstraints gbc_jTextFieldDataSourceName = new GridBagConstraints();
		gbc_jTextFieldDataSourceName.gridwidth = 2;
		gbc_jTextFieldDataSourceName.insets = new Insets(5, 0, 5, 5);
		gbc_jTextFieldDataSourceName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldDataSourceName.gridx = 1;
		gbc_jTextFieldDataSourceName.gridy = 1;
		add(getJTextFieldDataSourceName(), gbc_jTextFieldDataSourceName);
		GridBagConstraints gbc_jLabelConfigurationScope = new GridBagConstraints();
		gbc_jLabelConfigurationScope.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelConfigurationScope.gridx = 3;
		gbc_jLabelConfigurationScope.gridy = 1;
		add(getJLabelConfigurationScope(), gbc_jLabelConfigurationScope);
		GridBagConstraints gbc_jComboBoxConfigurationScope = new GridBagConstraints();
		gbc_jComboBoxConfigurationScope.gridwidth = 2;
		gbc_jComboBoxConfigurationScope.insets = new Insets(5, 0, 5, 5);
		gbc_jComboBoxConfigurationScope.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxConfigurationScope.gridx = 4;
		gbc_jComboBoxConfigurationScope.gridy = 1;
		add(getJComboBoxConfigurationScope(), gbc_jComboBoxConfigurationScope);
		GridBagConstraints gbc_jLabelFileName = new GridBagConstraints();
		gbc_jLabelFileName.anchor = GridBagConstraints.EAST;
		gbc_jLabelFileName.insets = new Insets(0, 5, 5, 5);
		gbc_jLabelFileName.gridx = 0;
		gbc_jLabelFileName.gridy = 2;
		add(getJLabelFileName(), gbc_jLabelFileName);
		GridBagConstraints gbc_jTextFieldFileName = new GridBagConstraints();
		gbc_jTextFieldFileName.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldFileName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldFileName.gridx = 1;
		gbc_jTextFieldFileName.gridy = 2;
		add(getJTextFieldFileName(), gbc_jTextFieldFileName);
		GridBagConstraints gbc_jButtonLoad = new GridBagConstraints();
		gbc_jButtonLoad.insets = new Insets(0, 0, 5, 5);
		gbc_jButtonLoad.gridx = 2;
		gbc_jButtonLoad.gridy = 2;
		add(getJButtonLoad(), gbc_jButtonLoad);
		GridBagConstraints gbc_jLabelColumnSeparator = new GridBagConstraints();
		gbc_jLabelColumnSeparator.anchor = GridBagConstraints.EAST;
		gbc_jLabelColumnSeparator.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelColumnSeparator.gridx = 3;
		gbc_jLabelColumnSeparator.gridy = 2;
		add(getJLabelColumnSeparator(), gbc_jLabelColumnSeparator);
		GridBagConstraints gbc_jComboBoxColumnSeparator = new GridBagConstraints();
		gbc_jComboBoxColumnSeparator.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBoxColumnSeparator.anchor = GridBagConstraints.WEST;
		gbc_jComboBoxColumnSeparator.gridx = 4;
		gbc_jComboBoxColumnSeparator.gridy = 2;
		add(getJComboBoxColumnSeparator(), gbc_jComboBoxColumnSeparator);
		GridBagConstraints gbc_jCheckBoxHasHeadline = new GridBagConstraints();
		gbc_jCheckBoxHasHeadline.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxHasHeadline.insets = new Insets(0, 0, 5, 5);
		gbc_jCheckBoxHasHeadline.gridx = 5;
		gbc_jCheckBoxHasHeadline.gridy = 2;
		add(getJCheckBoxHasHeadline(), gbc_jCheckBoxHasHeadline);
		GridBagConstraints gbc_jButtonAutoGenerate = new GridBagConstraints();
		gbc_jButtonAutoGenerate.anchor = GridBagConstraints.WEST;
		gbc_jButtonAutoGenerate.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonAutoGenerate.gridx = 6;
		gbc_jButtonAutoGenerate.gridy = 2;
		add(getJButtonAutoGenerate(), gbc_jButtonAutoGenerate);
		GridBagConstraints gbc_jLabelDateFormat = new GridBagConstraints();
		gbc_jLabelDateFormat.anchor = GridBagConstraints.EAST;
		gbc_jLabelDateFormat.insets = new Insets(0, 5, 5, 5);
		gbc_jLabelDateFormat.gridx = 0;
		gbc_jLabelDateFormat.gridy = 3;
		add(getJLabelDateFormat(), gbc_jLabelDateFormat);
		GridBagConstraints gbc_jTextFieldDateFormat = new GridBagConstraints();
		gbc_jTextFieldDateFormat.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldDateFormat.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldDateFormat.gridx = 1;
		gbc_jTextFieldDateFormat.gridy = 3;
		add(getJTextFieldDateFormat(), gbc_jTextFieldDateFormat);
		GridBagConstraints gbc_jButtonFormatCheck = new GridBagConstraints();
		gbc_jButtonFormatCheck.insets = new Insets(0, 0, 5, 5);
		gbc_jButtonFormatCheck.gridx = 2;
		gbc_jButtonFormatCheck.gridy = 3;
		add(getJButtonFormatCheck(), gbc_jButtonFormatCheck);
		GridBagConstraints gbc_jScrollPaneCsvDataTable = new GridBagConstraints();
		gbc_jScrollPaneCsvDataTable.insets = new Insets(5, 5, 5, 0);
		gbc_jScrollPaneCsvDataTable.gridwidth = 7;
		gbc_jScrollPaneCsvDataTable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneCsvDataTable.gridx = 0;
		gbc_jScrollPaneCsvDataTable.gridy = 4;
		add(getJScrollPaneCsvDataTable(), gbc_jScrollPaneCsvDataTable);
		GridBagConstraints gbc_seriesConfigurationPanel = new GridBagConstraints();
		gbc_seriesConfigurationPanel.gridwidth = 7;
		gbc_seriesConfigurationPanel.fill = GridBagConstraints.BOTH;
		gbc_seriesConfigurationPanel.gridx = 0;
		gbc_seriesConfigurationPanel.gridy = 5;
		add(getSeriesConfigurationPanel(), gbc_seriesConfigurationPanel);
	}

	/**
	 * Sets the data source configuration.
	 * @param dataSourceConfiguration the new data source configuration
	 */
	public void setCurrentDataSourceConfiguration(CsvDataSourceConfiguration dataSourceConfiguration) {
		this.currentSourceConfiguration = dataSourceConfiguration;
		
		if (dataSourceConfiguration!=null) {
			if (this.pauseListener==false) {
				this.setCsvDataFile(dataSourceConfiguration.getCsvFile());
			}
			
			// --- Set values to GUI components ---------------
			this.getJTextFieldDataSourceName().setText(dataSourceConfiguration.getName());
			this.getJTextFieldFileName().setText(dataSourceConfiguration.getCsvFile().getName());
			this.getJTextFieldDateFormat().setText(dataSourceConfiguration.getDateTimeFormat());
			this.getJCheckBoxHasHeadline().setSelected(dataSourceConfiguration.isHeadline());
			this.getJButtonAutoGenerate().setEnabled(dataSourceConfiguration.isHeadline());	// --- Headline required to generate series names
			this.getJComboBoxColumnSeparator().setSelectedItem(dataSourceConfiguration.getColumnSeparator());
			this.setComponentsEnabled(true);
		} else {
			this.setCsvDataFile(null);
			this.getJTextFieldDataSourceName().setText(null);
			this.getJTextFieldFileName().setText(null);
			this.getJTextFieldDateFormat().setText(null);
			this.getJCheckBoxHasHeadline().setSelected(false);
			this.getJButtonAutoGenerate().setEnabled(false);
			this.getJComboBoxColumnSeparator().setSelectedItem(0);
			this.setComponentsEnabled(false);
		}
		
//		this.getSeriesConfigurationPanel().setCurrentSeriesConfiguration(null);
	}
	
	/**
	 * Gets the series configuration.
	 * @return the series configuration
	 */
	public CsvDataSourceConfiguration getCurrentDataSourceConfiguration() {
		return currentSourceConfiguration;
	}
	
	// -------------------------------------------------------------- 
	// --- From here, methods to handle CSV files -------------------

	/**
	 * Gets the csv data file.
	 * @return the csv data file
	 */
	public File getCsvDataFile() {
		return csvDataFile;
	}

	/**
	 * Sets the CSV data file, tries to load it.
	 * @param csvDataFile the new csv data file
	 */
	public void setCsvDataFile(File csvDataFile) {
		this.csvDataFile = csvDataFile;
		if (csvDataFile!=null && csvDataFile.exists()) {
			this.csvData = this.readCsvData(csvDataFile);
			this.removeEmptyColumns();
			this.rebuildTableModel();
		} else {
			this.csvData=null;
			this.getJTableCsvData().setModel(new DefaultTableModel());
		}
	}
	
	/**
	 * Removes empty columns from the data set.
	 */
	private void removeEmptyColumns() {
		
		if (this.csvData!=null) {
			
			ArrayList<String> colsToRemove = new ArrayList<>();
			
			// --- Iterate over all columns -----------------------------------
			for (int colIndex=0; colIndex<this.csvData.get(0).size(); colIndex++) {
				boolean empty = true;
				
				for (int rowIndex=1; rowIndex<this.csvData.size(); rowIndex++) {
					String entry = this.csvData.get(rowIndex).get(colIndex);
					
					if (entry!=null && entry.isBlank()==false) {
						// --- One entry found -> not empty -------------------
						empty=false;
						break;
					}
				}
				if (empty==true) {
					// --- No entries found -> empty -> mark to remove --------
					colsToRemove.add(this.csvData.get(0).get(colIndex));
				}
			}
			
			// --- Remove the previously marked columns -----------------------
			for (String colToRemove : colsToRemove) {
				int colIndex = this.getIndexForColumn(colToRemove);
				if (colIndex>=0) {
					for (Vector<String> row : this.csvData) {
						row.remove(colIndex);
					}
				}
			}
			
		}
	}
	
	/**
	 * Gets the index for the column with the specified name.
	 * @param columnHeader the column header
	 * @return the index for column, -1 if not found
	 */
	private int getIndexForColumn(String columnHeader) {
		if (this.csvData!=null) {
			for (int i=0; i<this.getCsvData().get(0).size();i++) {
				if (this.getCsvData().get(0).get(i).equals(columnHeader)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Choose the csv file.
	 */
	private void chooseCsvFile() {
		boolean changeFile = true;
		
		// --- If the current data source has configured data series, 
		if (this.getCurrentDataSourceConfiguration().getDataSeriesConfigurations().size()>0) {
			String message = "By changing the CSV file, all configured data series for this data source\nwill be discarded! Are you sure?";
			int userResponse = JOptionPane.showConfirmDialog(this, message, "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (userResponse==JOptionPane.NO_OPTION) {
				changeFile = false;
			}
		}
		
		// --- Load the chosen file ------------
		if (changeFile==true) {
			JFileChooser jFileChooserImportCSV = new JFileChooser();
			jFileChooserImportCSV.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
			jFileChooserImportCSV.setFileFilter(new FileNameExtensionFilter("CSV-File", "csv"));
			jFileChooserImportCSV.setDialogTitle("Choose CSV-File");
			if(jFileChooserImportCSV.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File csvFile = jFileChooserImportCSV.getSelectedFile();
				Application.getGlobalInfo().setLastSelectedFolder(csvFile.getParent());
				if (this.isInProjectFolder(csvFile)==false) {
					JOptionPane.showMessageDialog(this, "Please choose a file that is located inside the project folder!", "Invalid location", JOptionPane.WARNING_MESSAGE);
				} else {
					if (csvFile!=null && csvFile.exists()) {
						for (int i=0; i<this.getCurrentDataSourceConfiguration().getDataSeriesConfigurations().size(); i++) {
							this.getCurrentDataSourceConfiguration().getDataSeriesConfigurations().remove(i);
						}
						this.setCsvDataFile(csvFile);
					}
				}
			}
		}
	}

	/**
	 * Checks if the specified file is inside the project folder (including subfolders).
	 * @param file the file
	 * @return true, if the file is inside the project folder
	 */
	private boolean isInProjectFolder(File file) {
		Path filePath = file.toPath();
		Path projectFolderPath = new File(Application.getProjectFocused().getProjectFolderFullPath()).toPath();
		return filePath.startsWith(projectFolderPath);
	}

	/**
	 * Reads the data from a csv file.
	 * @param csvFile the csv file
	 * @return the csv data
	 */
	private Vector<Vector<String>> readCsvData(File csvFile){
		Vector<Vector<String>> importedData = null;
		
		if(csvFile == null){
			System.err.println("No CSV file selected");
		}else{
			BufferedReader fileReader = null;
			try {
				
				importedData = new Vector<Vector<String>>();
				fileReader = new BufferedReader(new FileReader(csvFile));
				
				String line;
				while((line = fileReader.readLine()) != null){
					String[] tokens = line.split(this.currentSourceConfiguration.getColumnSeparator());
					Vector<String> lineData = new Vector<String>(Arrays.asList(tokens));
					importedData.addElement(lineData);
				}
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					fileReader.close();
				} catch (IOException e) {
					System.err.println("Error closing file reader");
					e.printStackTrace();
				}
			}
		}
		
		return importedData;
	}

	private Vector<Vector<String>> getCsvData() {
		if (csvData==null) {
			csvData = new Vector<>();
		}
		return csvData;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, private getter methods for the GUI components -----------

	private JLabel getJLabelCsvDataSource() {
		if (jLabelCsvDataSource == null) {
			jLabelCsvDataSource = new JLabel("CSV Data Source Configuration");
			jLabelCsvDataSource.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelCsvDataSource;
	}

	private JLabel getJLabelDataSourceName() {
		if (jLabelDataSourceName == null) {
			jLabelDataSourceName = new JLabel("Name:");
			jLabelDataSourceName.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDataSourceName;
	}
	private JTextField getJTextFieldDataSourceName() {
		if (jTextFieldDataSourceName == null) {
			jTextFieldDataSourceName = new JTextField();
			jTextFieldDataSourceName.setColumns(10);
			
			// --- Trigger rename when leaving the text field -------
			jTextFieldDataSourceName.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent fe) {
					if (pauseListener==false) {
						CsvDataSourceConfigurationPanel.this.renameDataSource();
					}
				}
			});
			
			// --- Trigger rename when enter was pressed ------------
			jTextFieldDataSourceName.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent ke) {
					if (ke.getKeyCode()==KeyEvent.VK_ENTER) {
						CsvDataSourceConfigurationPanel.this.renameDataSource();
					}
				}
			});
		}
		return jTextFieldDataSourceName;
	}
	private JLabel getJLabelConfigurationScope() {
		if (jLabelConfigurationScope == null) {
			jLabelConfigurationScope = new JLabel("Store at:");
			jLabelConfigurationScope.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelConfigurationScope;
	}

	private JComboBox<ConfigurationScope> getJComboBoxConfigurationScope() {
		if (jComboBoxConfigurationScope == null) {
			jComboBoxConfigurationScope = new JComboBox<>();
			jComboBoxConfigurationScope.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxConfigurationScope.setModel(new DefaultComboBoxModel<ConfigurationScope>(ConfigurationScope.values()));
			jComboBoxConfigurationScope.setToolTipText("Decide where to store the data source. If stored in the application, it will be available independently of the current project. If stored in the project, it is limited to that project, but can easily be shared together with it.");
			jComboBoxConfigurationScope.addActionListener(this);
		}
		return jComboBoxConfigurationScope;
	}

	private JLabel getJLabelDateFormat() {
		if (jLabelDateFormat == null) {
			jLabelDateFormat = new JLabel("Date Format:");
			jLabelDateFormat.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDateFormat;
	}
	private JTextField getJTextFieldDateFormat() {
		if (jTextFieldDateFormat == null) {
			jTextFieldDateFormat = new JTextField();
			jTextFieldDateFormat.setColumns(10);
			jTextFieldDateFormat.setText(DEFAULT_TIME_FORMAT);
			jTextFieldDateFormat.getDocument().addDocumentListener(this);
		}
		return jTextFieldDateFormat;
	}
	private JButton getJButtonFormatCheck() {
		if (jButtonFormatCheck == null) {
			jButtonFormatCheck = new JButton();
			jButtonFormatCheck.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_CHECK_FAILED)));
			jButtonFormatCheck.setToolTipText("Load Weather Data from File");
			jButtonFormatCheck.setPreferredSize(buttonSize);
			jButtonFormatCheck.addActionListener(this);
		}
		return jButtonFormatCheck;
	}
	private JLabel getJLabelFileName() {
		if (jLabelFileName == null) {
			jLabelFileName = new JLabel("CSV File:");
			jLabelFileName.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelFileName;
	}
	private JTextField getJTextFieldFileName() {
		if (jTextFieldFileName == null) {
			jTextFieldFileName = new JTextField();
			jTextFieldFileName.setColumns(10);
			jTextFieldFileName.setEditable(false);
		}
		return jTextFieldFileName;
	}
	private JButton getJButtonLoad() {
		if (jButtonLoad == null) {
			jButtonLoad = new JButton();
			jButtonLoad.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_LOAD_FILE)));
			jButtonLoad.setToolTipText("Select CSV file");
			jButtonLoad.setPreferredSize(buttonSize);
			jButtonLoad.addActionListener(this);
		}
		return jButtonLoad;
	}

	private JCheckBox getJCheckBoxHasHeadline() {
		if (jCheckBoxHasHeadline == null) {
			jCheckBoxHasHeadline = new JCheckBox("Has Headline");
			jCheckBoxHasHeadline.setToolTipText("Indicates if the CSV file has a headline");
			jCheckBoxHasHeadline.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxHasHeadline.addActionListener(this);
		}
		return jCheckBoxHasHeadline;
	}

	private JLabel getJLabelColumnSeparator() {
		if (jLabelColumnSeparator == null) {
			jLabelColumnSeparator = new JLabel("Separator:");
			jLabelColumnSeparator.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelColumnSeparator;
	}

	private JComboBox<String> getJComboBoxColumnSeparator() {
		if (jComboBoxColumnSeparator == null) {
			jComboBoxColumnSeparator = new JComboBox<String>();
			jComboBoxColumnSeparator.setModel(new DefaultComboBoxModel<>(COLUMN_SEPARATORS));
			jComboBoxColumnSeparator.addActionListener(this);
		}
		return jComboBoxColumnSeparator;
	}

	private JButton getJButtonAutoGenerate() {
		if (jButtonAutoGenerate == null) {
			jButtonAutoGenerate = new JButton("Generate Series");
			jButtonAutoGenerate.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonAutoGenerate.setToolTipText("Generates a new data series for each column of the file");
			jButtonAutoGenerate.addActionListener(this);
		}
		return jButtonAutoGenerate;
	}

	private JScrollPane getJScrollPaneCsvDataTable() {
		if (jScrollPaneCsvDataTable == null) {
			jScrollPaneCsvDataTable = new JScrollPane();
			jScrollPaneCsvDataTable.setViewportView(getJTableCsvData());
		}
		return jScrollPaneCsvDataTable;
	}
	protected JTable getJTableCsvData() {
		if (jTableCsvData == null) {
			jTableCsvData = new JTable();
		}
		return jTableCsvData;
	}
	private CsvDataSeriesConfigurationPanel getSeriesConfigurationPanel() {
		if (seriesConfigurationPanel == null) {
			seriesConfigurationPanel = new CsvDataSeriesConfigurationPanel();
			this.addPropertyChangeListener(seriesConfigurationPanel);
		}
		return seriesConfigurationPanel;
	}

	/**
	 * Sets the components enabled.
	 * @param enabled the new components enabled
	 */
	private void setComponentsEnabled(boolean enabled) {
		this.getJTextFieldDataSourceName().setEnabled(enabled);
		this.getJTextFieldFileName().setEnabled(enabled);
		this.getJButtonLoad().setEnabled(enabled);
		this.getJTextFieldDateFormat().setEnabled(enabled);
		this.getJButtonFormatCheck().setEnabled(enabled);
		this.getJComboBoxColumnSeparator().setEnabled(enabled);
		this.getJCheckBoxHasHeadline().setEnabled(enabled);
	}
	
	
	// --------------------------------------------------------------
	// --- From here, methods to handle actions and events ----------

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonFormatCheck()) {
			this.checkTimeFormat();
		} else if (ae.getSource()==this.getJCheckBoxHasHeadline()) {
			this.getCurrentDataSourceConfiguration().setHeadline(this.getJCheckBoxHasHeadline().isSelected());
			this.setCsvDataFile(csvDataFile);	// Trigger a reload
		} else if (ae.getSource()==this.getJButtonAutoGenerate()) {
			this.autoGenerateSeries();
		}
		
		else if (ae.getSource()==this.getJButtonLoad()) {
			this.chooseCsvFile();
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		this.checkTimeFormat();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		this.checkTimeFormat();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		this.checkTimeFormat();
	}
	
	/**
	 * Sets the data series configuration.
	 * @param seriesConfiguration the new data series configuration
	 */
	public void setDataSeriesConfiguration(CsvDataSeriesConfiguration seriesConfiguration) {
		this.getSeriesConfigurationPanel().setCurrentSeriesConfiguration(seriesConfiguration);
	}

	/**
	 * Gets the date time formatter.
	 * @return the date time formatter
	 */
	private DateTimeFormatter getDateTimeFormatter() {
		if (dateTimeFormatter==null) {
			dateTimeFormatter = DateTimeFormatter.ofPattern(this.getCurrentDataSourceConfiguration().getDateTimeFormat()).withZone(GlobalRuntimeValues.getZoneId());
		}
		return dateTimeFormatter;
	}

	/**
	 * Checks the time format, by trying to parse the first row's first entry. 
	 */
	private void checkTimeFormat() {
		// --- Reset the previous time format -------------
		this.dateTimeFormatter = null;
		
		if (this.currentSourceConfiguration!=null) {
			this.currentSourceConfiguration.setDateTimeFormat(this.getJTextFieldDateFormat().getText());
			this.dateTimeFormatter = null;
			// --- Try to parse the first date time entry -----
			if (this.getJTableCsvData().getModel().getRowCount()>0) {
				String firstTimeString = (String) this.getJTableCsvData().getValueAt(0, 0);
				try {
					this.getDateTimeFormatter().parse(firstTimeString);
					// --- Parsing successful -> format okay ------
					this.setTimeFormatCheckState(true);
				} catch (DateTimeParseException dtpe) {
					// --- Parsing failed -> format not okay ------
					this.setTimeFormatCheckState(false);
				}
			}
		} else {
			this.setTimeFormatCheckState(false);
		}
	}

	/**
	 * Sets the icon of the JButtonFormatCheck, to indicate if the current time format fits the data.
	 * @param success the new time format check state
	 */
	private void setTimeFormatCheckState(boolean success) {
		if (success) {
			this.getJButtonFormatCheck().setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_CHECK_OKAY)));
			this.getJButtonFormatCheck().setToolTipText("Test parsing successful, time format seems okay.");
		} else {
			this.getJButtonFormatCheck().setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_CHECK_FAILED)));
			this.getJButtonFormatCheck().setToolTipText("Test parsing failed, check you time format!");
		}
	}

	/**
	 * Rename the current data source.
	 */
	private void renameDataSource() {
		
		// --- Suppress unnecessary reloads of the CSV file.
		this.pauseListener = true;
		String newName = this.getJTextFieldDataSourceName().getText();
		if (super.renameDataSource(newName)==false) {
			// If the name was rejected, return to the textfield
			this.getJTextFieldDataSourceName().requestFocus();
		}
		this.pauseListener = false;
	}

	/**
	 * Rebuilds the table model.
	 */
	private void rebuildTableModel() {
		
		DefaultTableModel tableModel = null;
		
		if (this.getCsvData().size()>0) {
			int firstIndex = 0;
			tableModel = new DefaultTableModel(0, this.getCsvData().get(0).size());
			
			// --- If configured, use the first row as column headers ---------
			if (this.currentSourceConfiguration.isHeadline()) {
				tableModel.setColumnIdentifiers(this.getCsvData().get(0));
				firstIndex++;
			}
			
			// --- Read the actual data -------------------
			for (int i=firstIndex; i<csvData.size(); i++) {
				tableModel.addRow(csvData.get(i));
			}
		}
		
		this.getJTableCsvData().setModel(tableModel);
		this.firePropertyChange(TABLEMODEL_CHANGED, null, tableModel);
	}
	
	/**
	 * Generate a data series for every non-empty column of the CSV file.
	 */
	private void autoGenerateSeries() {
		for (int i=1; i<this.getJTableCsvData().getColumnCount(); i++) {
			String seriesName = this.getJTableCsvData().getColumnName(i);
			CsvDataSeriesConfiguration seriesConfiguration = new CsvDataSeriesConfiguration();
			seriesConfiguration.setName(seriesName);
			seriesConfiguration.setDataColumn(i);
			this.currentSourceConfiguration.addDataSeriesConfiguration(seriesConfiguration);
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel#getDataSourceConfiguraiton()
	 */
	@Override
	public AbstractDataSourceConfiguration getDataSourceConfiguration() {
		return this.getCurrentDataSourceConfiguration();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel#setDataSourceConfiguration(de.enflexit.awb.timeSeriesDataProvider.dataModel.AbstractDataSourceConfiguration)
	 */
	@Override
	public void setDataSourceConfiguration(AbstractDataSourceConfiguration dataSourceConfiguration) {
		if (dataSourceConfiguration==null || dataSourceConfiguration instanceof CsvDataSourceConfiguration) {
			this.setCurrentDataSourceConfiguration((CsvDataSourceConfiguration) dataSourceConfiguration);
		} else {
			throw new IllegalArgumentException("The provided data source configuration is not for a CSV data source!");
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel#setDataSeriesConfiguration(de.enflexit.awb.timeSeriesDataProvider.dataModel.AbstractDataSeriesConfiguration)
	 */
	@Override
	public void setDataSeriesConfiguration(AbstractDataSeriesConfiguration seriesConfiguraiton) {
		if (seriesConfiguraiton instanceof CsvDataSeriesConfiguration) {
			this.setDataSeriesConfiguration((CsvDataSeriesConfiguration)seriesConfiguraiton);
		}
	}
}