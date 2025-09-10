package de.enflexit.ml.pmml.evaluator.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.dmg.pmml.DataType;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ProbabilityDistribution;
import org.jpmml.evaluator.TargetField;

import de.enflexit.awb.core.Application;
import de.enflexit.common.swing.AwbLookAndFeelAdjustments;
import de.enflexit.ml.pmml.evaluator.PMMLEvaluator;

import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTable;


/**
 * The Class PMMLEvaluatorPanel provides a simple UI to load and evaluate PMML models.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class PMMLEvaluatorPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 441763226872389945L;
	
	private static final String ICON_PATH_LOAD = "/icons/Load.png";
	private static final String ICON_PATH_PREDICT_SINGLE = "/icons/LightBulb.png";
	private static final String ICON_PATH_PREDICT_BATCH_CSV = "/icons/CSVFile.png";
	
	private static final int COLUMN_INDEX_FIELD_NAME = 0;
	private static final int COLUMN_INDEX_DATA_TYPE = 1;
	private static final int COLUMN_INDEX_FIELD_VALUE = 2;
	
	private String[] FIELDS_TABLE_COLUMNS = {"Name", "Type", "Value"}; 
	
	private JToolBar pmmlEvaluatorToolBar;
	private JPanel pmmlEvaluatorMainPanel;
	private JButton jButtonLoadModel;
	private JButton jButtonPredictSingle;
	private JButton jButtonPredictBatchCSV;
	private JLabel jLabelModelState;
	private JLabel jLabelInputValues;
	private JScrollPane jScrollPaneInputValuesTable;
	private JLabel jLabelTargetValues;
	private JScrollPane jScrollPaneOutputValuesTable;
	private JTable jTableOutputValues;
	private JTable jTableInputValues;
	
	private JFileChooser pmmlFileChooser;
	private PMMLEvaluator pmmlEvaluator;
	
	public PMMLEvaluatorPanel() {
		initialize();
	}
	
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getPmmlEvaluatorToolBar(), BorderLayout.NORTH);
		add(getPmmlEvaluatorMainPanel(), BorderLayout.CENTER);
	}

	private JToolBar getPmmlEvaluatorToolBar() {
		if (pmmlEvaluatorToolBar == null) {
			pmmlEvaluatorToolBar = new JToolBar();
			pmmlEvaluatorToolBar.setFloatable(false);
			pmmlEvaluatorToolBar.add(getJButtonLoadModel());
			pmmlEvaluatorToolBar.add(getJButtonPredictSingle());
			pmmlEvaluatorToolBar.add(getJButtonPredictBatchCSV());
		}
		return pmmlEvaluatorToolBar;
	}
	
	private JPanel getPmmlEvaluatorMainPanel() {
		if (pmmlEvaluatorMainPanel == null) {
			pmmlEvaluatorMainPanel = new JPanel();
			GridBagLayout gbl_pmmlEvaluatorMainPanel = new GridBagLayout();
			gbl_pmmlEvaluatorMainPanel.columnWidths = new int[]{0, 0};
			gbl_pmmlEvaluatorMainPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
			gbl_pmmlEvaluatorMainPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_pmmlEvaluatorMainPanel.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
			pmmlEvaluatorMainPanel.setLayout(gbl_pmmlEvaluatorMainPanel);
			GridBagConstraints gbc_jLabelModelState = new GridBagConstraints();
			gbc_jLabelModelState.anchor = GridBagConstraints.WEST;
			gbc_jLabelModelState.insets = new Insets(10, 10, 5, 0);
			gbc_jLabelModelState.gridx = 0;
			gbc_jLabelModelState.gridy = 0;
			pmmlEvaluatorMainPanel.add(getJLabelModelState(), gbc_jLabelModelState);
			GridBagConstraints gbc_jLabelInputValues = new GridBagConstraints();
			gbc_jLabelInputValues.insets = new Insets(5, 10, 5, 0);
			gbc_jLabelInputValues.anchor = GridBagConstraints.WEST;
			gbc_jLabelInputValues.gridx = 0;
			gbc_jLabelInputValues.gridy = 1;
			pmmlEvaluatorMainPanel.add(getJLabelInputValues(), gbc_jLabelInputValues);
			GridBagConstraints gbc_jScrollPaneInputValuesTable = new GridBagConstraints();
			gbc_jScrollPaneInputValuesTable.weighty = 0.75;
			gbc_jScrollPaneInputValuesTable.insets = new Insets(0, 0, 5, 0);
			gbc_jScrollPaneInputValuesTable.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneInputValuesTable.gridx = 0;
			gbc_jScrollPaneInputValuesTable.gridy = 2;
			pmmlEvaluatorMainPanel.add(getJScrollPaneInputValuesTable(), gbc_jScrollPaneInputValuesTable);
			GridBagConstraints gbc_jLabelTargetValues = new GridBagConstraints();
			gbc_jLabelTargetValues.insets = new Insets(5, 10, 5, 0);
			gbc_jLabelTargetValues.anchor = GridBagConstraints.WEST;
			gbc_jLabelTargetValues.gridx = 0;
			gbc_jLabelTargetValues.gridy = 3;
			pmmlEvaluatorMainPanel.add(getJLabelTargetValues(), gbc_jLabelTargetValues);
			GridBagConstraints gbc_jScrollPaneOutputValuesTable = new GridBagConstraints();
			gbc_jScrollPaneOutputValuesTable.weighty = 0.25;
			gbc_jScrollPaneOutputValuesTable.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneOutputValuesTable.gridx = 0;
			gbc_jScrollPaneOutputValuesTable.gridy = 4;
			pmmlEvaluatorMainPanel.add(getJScrollPaneOutputValuesTable(), gbc_jScrollPaneOutputValuesTable);
		}
		return pmmlEvaluatorMainPanel;
	}
	
	private JButton getJButtonLoadModel() {
		if (jButtonLoadModel == null) {
			jButtonLoadModel = new JButton();
			jButtonLoadModel.setIcon(this.getImageIcon(ICON_PATH_LOAD, false));
			jButtonLoadModel.setToolTipText("Load a PMML model from file");
			jButtonLoadModel.addActionListener(this);
		}
		return jButtonLoadModel;
	}
	
	private JButton getJButtonPredictSingle() {
		if (jButtonPredictSingle == null) {
			jButtonPredictSingle = new JButton();
			jButtonPredictSingle.setIcon(this.getImageIcon(ICON_PATH_PREDICT_SINGLE, true));
			jButtonPredictSingle.setToolTipText("Make a single prediction, using the input values from the table");
			jButtonPredictSingle.setEnabled(false);
			jButtonPredictSingle.addActionListener(this);
		}
		return jButtonPredictSingle;
	}
	
	private JButton getJButtonPredictBatchCSV() {
		if (jButtonPredictBatchCSV == null) {
			jButtonPredictBatchCSV = new JButton();
			jButtonPredictBatchCSV.setIcon(this.getImageIcon(ICON_PATH_PREDICT_BATCH_CSV, true));
			jButtonPredictBatchCSV.setToolTipText("Make a batch prediction, reading the input values from a CSV file");
			jButtonPredictBatchCSV.setEnabled(false);
			jButtonPredictBatchCSV.addActionListener(this);
		}
		return jButtonPredictBatchCSV;
	}
	
	private ImageIcon getImageIcon(String iconPath, boolean modeDependant) {
		String resourcePath = null;
		if (modeDependant==true) {
			// --- Insert a file name suffix for light or dark mode
			int lastPointIndex = iconPath.lastIndexOf(".");
			String pathWithoutSuffix = iconPath.substring(0, lastPointIndex);
			String suffix = iconPath.substring(lastPointIndex);
			resourcePath = pathWithoutSuffix + this.getIconFileModeSuffix() + suffix;
		} else {
			// --- Same icon for both modes -> no adaption needed
			resourcePath = iconPath;
		}
		return new ImageIcon(this.getClass().getResource(resourcePath));
	}
	private String getIconFileModeSuffix() {
		return AwbLookAndFeelAdjustments.isDarkLookAndFeel() ? "_DarkMode" : "_LightMode";
	}
	
	
	private JLabel getJLabelModelState() {
		if (jLabelModelState == null) {
			jLabelModelState = new JLabel("No model loaded!");
			jLabelModelState.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelModelState;
	}
	
	private JLabel getJLabelInputValues() {
		if (jLabelInputValues == null) {
			jLabelInputValues = new JLabel("Input Values:");
			jLabelInputValues.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelInputValues;
	}
	
	private JScrollPane getJScrollPaneInputValuesTable() {
		if (jScrollPaneInputValuesTable == null) {
			jScrollPaneInputValuesTable = new JScrollPane();
			jScrollPaneInputValuesTable.setViewportView(getJTableInputValues());
		}
		return jScrollPaneInputValuesTable;
	}
	
	private JLabel getJLabelTargetValues() {
		if (jLabelTargetValues == null) {
			jLabelTargetValues = new JLabel("Output Values:");
			jLabelTargetValues.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelTargetValues;
	}
	
	private JScrollPane getJScrollPaneOutputValuesTable() {
		if (jScrollPaneOutputValuesTable == null) {
			jScrollPaneOutputValuesTable = new JScrollPane();
			jScrollPaneOutputValuesTable.setViewportView(getJTableOutputValues());
		}
		return jScrollPaneOutputValuesTable;
	}
	
	private JTable getJTableOutputValues() {
		if (jTableOutputValues == null) {
			jTableOutputValues = new JTable();
			this.setEmtpyOutputValuesTableModel();
			jTableOutputValues.setAutoCreateRowSorter(true);
		}
		return jTableOutputValues;
	}
	
	private void setEmtpyOutputValuesTableModel() {
		DefaultTableModel emptyTableModel = new DefaultTableModel(FIELDS_TABLE_COLUMNS, 0);
		this.getJTableOutputValues().setModel(emptyTableModel);
	}
	
	private JTable getJTableInputValues() {
		if (jTableInputValues == null) {
			jTableInputValues = new JTable();
			DefaultTableModel initialTableModel = new DefaultTableModel(FIELDS_TABLE_COLUMNS, 0);
			jTableInputValues.setModel(initialTableModel);
			jTableInputValues.setAutoCreateRowSorter(true);
		}
		return jTableInputValues;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == this.getJButtonLoadModel()) {
			this.loadModelFromFile();
		} else if (ae.getSource() == this.getJButtonPredictSingle()) {
			this.makeSinglePrediction();
		}
	}
	
	private void loadModelFromFile() {
		if (this.getPmmlFileChooser().showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File pmmlFile = this.getPmmlFileChooser().getSelectedFile();
			this.pmmlEvaluator = new PMMLEvaluator(pmmlFile);
			
			if (this.pmmlEvaluator!=null) {
				this.getJLabelModelState().setText("Loaded PMML model from " + pmmlFile.getName());
				DefaultTableModel fieldsTableModel = this.createInputFieldsTableModel(this.pmmlEvaluator.getInputFields());
				this.getJTableInputValues().setModel(fieldsTableModel);
				this.getJTableInputValues().getRowSorter().toggleSortOrder(COLUMN_INDEX_FIELD_NAME);
				this.setEmtpyOutputValuesTableModel();			// Clear old outputs 
				this.getJButtonPredictSingle().setEnabled(true);
			} else {
				this.getJLabelModelState().setText("Loading model from " + pmmlFile.getName() + " failed!");
				this.getJButtonPredictBatchCSV().setEnabled(false);
				this.getJButtonPredictBatchCSV().setEnabled(false);
			}
		}
	}
	
	private JFileChooser getPmmlFileChooser() {
		if (pmmlFileChooser==null) {
			pmmlFileChooser = new JFileChooser();
			pmmlFileChooser.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
			pmmlFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PMML File", "pmml"));
			pmmlFileChooser.setDialogTitle("Choose a PMML file");
		}
		return pmmlFileChooser;
	}
	
	private DefaultTableModel createInputFieldsTableModel(List<InputField> inputFields) {
		
		// --- Prepare the base model ---------------------
		DefaultTableModel fieldsTableModel = new DefaultTableModel(FIELDS_TABLE_COLUMNS, 0) {
			private static final long serialVersionUID = 5927561793542501086L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// --- Only the value column is editable --
				return column==COLUMN_INDEX_FIELD_VALUE;
			}
			
		};
		
		// --- Add a row for each field -------------------
		for (InputField modelField : inputFields) {
			Vector<Object> fieldRow = new Vector<Object>();
			fieldRow.add(modelField.getName());
			fieldRow.add(modelField.getDataType());
			fieldRow.add(null);
			fieldsTableModel.addRow(fieldRow);
		}
		
		return fieldsTableModel;
	}
	
	private DefaultTableModel createOutputFieldsTableModel(List<TargetField> outputFields, Map<String, ?> values) {
		// --- Prepare the base model ---------------------
		DefaultTableModel fieldsTableModel = new DefaultTableModel(FIELDS_TABLE_COLUMNS, 0) {
			private static final long serialVersionUID = 5927561793542501086L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
		};
		
		for (TargetField outputField : outputFields) {
			String fieldName = outputField.getName();
			Vector<Object> fieldRow = new Vector<Object>();
			fieldRow.add(fieldName);
			fieldRow.add(outputField.getDataType());
			
			Object targetValue = values.get(fieldName);

			//TODO more relevant cases?
			if (targetValue instanceof ProbabilityDistribution<?>) {
				// If the result is a probability distribution, show the prediction with its' probability
				ProbabilityDistribution<?> probDist = (ProbabilityDistribution<?>) targetValue;
				Object prediction = probDist.getPrediction();
				Double probability = probDist.getProbability(prediction);
				
				fieldRow.add(prediction + " (p=" + probability + ")"); 
			} else {
				// Otherwise, just show the prediction value
				fieldRow.add(values.get(fieldName));
			}
			
			fieldsTableModel.addRow(fieldRow);
		}
		return fieldsTableModel;
	}
	
	
	private void makeSinglePrediction() {
		HashMap<String, Object> inputValues = this.getInputValues();
		Map<String, ?> prediction = this.pmmlEvaluator.makePrediction(inputValues);
		if (prediction!=null) {
			DefaultTableModel outputTableModel = this.createOutputFieldsTableModel(this.pmmlEvaluator.getTargetFields(), prediction);
			this.getJTableOutputValues().setModel(outputTableModel);
			this.getJTableOutputValues().getRowSorter().toggleSortOrder(COLUMN_INDEX_FIELD_NAME);
		}
	}
	
	private HashMap<String, Object> getInputValues(){
		HashMap<String, Object> inputValues = new HashMap<String, Object>();
		for (int i=0; i<this.getJTableInputValues().getRowCount(); i++) {
			String fieldName = (String) this.getJTableInputValues().getValueAt(i, COLUMN_INDEX_FIELD_NAME);
			DataType dataType = (DataType) this.getJTableInputValues().getValueAt(i, COLUMN_INDEX_DATA_TYPE);
			String fieldValueString = (String) this.getJTableInputValues().getValueAt(i, COLUMN_INDEX_FIELD_VALUE);
			
			if (fieldValueString == null || fieldValueString.isBlank()==true) continue;

			// --- Convert the input string to the correct data type
			//TODO implement relevant date/time types! Maybe also other relevant types?
			//TODO catch parsing exceptions
			Object fieldValue=null;
			switch(dataType) {
			case FLOAT:
				fieldValue = Float.parseFloat(fieldValueString);
				break;
			case DOUBLE:
				fieldValue = Double.parseDouble(fieldValueString);
				break;
			case INTEGER:
				fieldValue = Integer.parseInt(fieldValueString);
				break;
			case BOOLEAN:
				fieldValue = Boolean.parseBoolean(fieldValueString);
				break;
			default:
				System.err.println("[" + this.getClass().getSimpleName() + "] Unimplemented data type: " + dataType);
			}
			inputValues.put(fieldName, fieldValue);
		}
		return inputValues;
	}
}
