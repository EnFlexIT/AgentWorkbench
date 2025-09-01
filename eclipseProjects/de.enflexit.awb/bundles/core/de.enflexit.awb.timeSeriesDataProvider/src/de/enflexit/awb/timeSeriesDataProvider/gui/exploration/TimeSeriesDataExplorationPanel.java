package de.enflexit.awb.timeSeriesDataProvider.gui.exploration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.awb.timeSeriesDataProvider.TimeValuePair;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * A UI panel to explore the configured data sources and series.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesDataExplorationPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -6759695015854586392L;
	private static final String COMBOBOX_ENTRY_NOTHING_SELECTED = " --- Please Select ---";
	
	private static final String ICON_PATH_SOURCE_AVAILABLE = "/icons/TrafficLightGreen.png";
	private static final String ICON_PATH_SOURCE_NOT_AVAILABLE = "/icons/TrafficLightRed.png";
	private static final String ICON_PATH_SOURCE_NOT_SELECTED = "/icons/TrafficLightGrey.png";
	
	private static final String LABEL_STRING_SOURCE_AVAILABLE = "Available";
	private static final String LABEL_STRING_SOURCE_NOT_AVAILABLE = "Not available";
	private static final String LABEL_STRING_SOURCE_NOT_SELECTED = "Not selected";
	
	private JLabel jLabelDataSource;
	private JComboBox<String> jComboBoxDataSource;
	private DefaultComboBoxModel<String> dataSourceComboBoxModel;
	private JLabel jLabelAvailability;
	private ImageIcon imageIconAvailable;
	private ImageIcon imageIconNotAvailable;
	private ImageIcon imageIconNotSelected;
	private JLabel jLabelDataSeries;
	private JComboBox<String> jComboBoxDataSeries;
	private DefaultComboBoxModel<String> dataSeriesComboBoxModel;
	private JRadioButton jRadioButtonSingleValue;
	private JRadioButton jRadioButtonValueRange;
	private ButtonGroup singleOrRangeButtonGroup;
	private JLabel jLabelRequest;
	private JLabel jLabelFrom;
	private DateTimeWidget dateTimeWidgetFrom;
	private JLabel jLabelTo;
	private DateTimeWidget dateTimeWidgetTo;
	private JButton jButtonRequestData;
	private JLabel jLabelResultInfo;
	private JScrollPane jScrollPaneResultsTable;
	private JTable jTableResults;
	
	private AbstractDataSource selectedDataSource;
	private AbstractDataSeries selectedDataSeries;
	
	private DateTimeFormatter dateTimeFormatter;
	
	/**
	 * Instantiates a new time series data exploration panel.
	 */
	public TimeSeriesDataExplorationPanel() {
		this.initialize();
		this.setSelectedDataSource(null);
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelDataSource = new GridBagConstraints();
		gbc_jLabelDataSource.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataSource.insets = new Insets(10, 10, 5, 5);
		gbc_jLabelDataSource.gridx = 0;
		gbc_jLabelDataSource.gridy = 0;
		add(getJLabelDataSource(), gbc_jLabelDataSource);
		GridBagConstraints gbc_jComboBoxDataSource = new GridBagConstraints();
		gbc_jComboBoxDataSource.gridwidth = 2;
		gbc_jComboBoxDataSource.insets = new Insets(10, 0, 5, 5);
		gbc_jComboBoxDataSource.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxDataSource.gridx = 1;
		gbc_jComboBoxDataSource.gridy = 0;
		add(getJComboBoxDataSource(), gbc_jComboBoxDataSource);
		GridBagConstraints gbc_jLabelAvailability = new GridBagConstraints();
		gbc_jLabelAvailability.anchor = GridBagConstraints.WEST;
		gbc_jLabelAvailability.insets = new Insets(10, 5, 5, 0);
		gbc_jLabelAvailability.gridx = 3;
		gbc_jLabelAvailability.gridy = 0;
		add(getJLabelAvailability(), gbc_jLabelAvailability);
		GridBagConstraints gbc_jLabelDataSeries = new GridBagConstraints();
		gbc_jLabelDataSeries.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataSeries.insets = new Insets(5, 10, 5, 5);
		gbc_jLabelDataSeries.gridx = 0;
		gbc_jLabelDataSeries.gridy = 1;
		add(getJLabelDataSeries(), gbc_jLabelDataSeries);
		GridBagConstraints gbc_jComboBoxDataSeries = new GridBagConstraints();
		gbc_jComboBoxDataSeries.gridwidth = 2;
		gbc_jComboBoxDataSeries.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBoxDataSeries.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxDataSeries.gridx = 1;
		gbc_jComboBoxDataSeries.gridy = 1;
		add(getJComboBoxDataSeries(), gbc_jComboBoxDataSeries);
		GridBagConstraints gbc_jLabelRequest = new GridBagConstraints();
		gbc_jLabelRequest.anchor = GridBagConstraints.WEST;
		gbc_jLabelRequest.insets = new Insets(5, 10, 5, 5);
		gbc_jLabelRequest.gridx = 0;
		gbc_jLabelRequest.gridy = 2;
		add(getJLabelRequest(), gbc_jLabelRequest);
		GridBagConstraints gbc_jRadioButtonSingleValue = new GridBagConstraints();
		gbc_jRadioButtonSingleValue.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonSingleValue.insets = new Insets(5, 5, 5, 5);
		gbc_jRadioButtonSingleValue.gridx = 1;
		gbc_jRadioButtonSingleValue.gridy = 2;
		add(getJRadioButtonSingleValue(), gbc_jRadioButtonSingleValue);
		GridBagConstraints gbc_jRadioButtonValueRange = new GridBagConstraints();
		gbc_jRadioButtonValueRange.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonValueRange.insets = new Insets(5, 5, 5, 5);
		gbc_jRadioButtonValueRange.gridx = 2;
		gbc_jRadioButtonValueRange.gridy = 2;
		add(getJRadioButtonValueRange(), gbc_jRadioButtonValueRange);
		GridBagConstraints gbc_jLabelFrom = new GridBagConstraints();
		gbc_jLabelFrom.anchor = GridBagConstraints.WEST;
		gbc_jLabelFrom.insets = new Insets(5, 10, 5, 5);
		gbc_jLabelFrom.gridx = 0;
		gbc_jLabelFrom.gridy = 3;
		add(getJLabelFrom(), gbc_jLabelFrom);
		GridBagConstraints gbc_dateTimeWidgetFrom = new GridBagConstraints();
		gbc_dateTimeWidgetFrom.gridwidth = 2;
		gbc_dateTimeWidgetFrom.insets = new Insets(5, 0, 5, 5);
		gbc_dateTimeWidgetFrom.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateTimeWidgetFrom.gridx = 1;
		gbc_dateTimeWidgetFrom.gridy = 3;
		add(getDateTimeWidgetFrom(), gbc_dateTimeWidgetFrom);
		GridBagConstraints gbc_jLabelTo = new GridBagConstraints();
		gbc_jLabelTo.anchor = GridBagConstraints.WEST;
		gbc_jLabelTo.insets = new Insets(5, 10, 5, 5);
		gbc_jLabelTo.gridx = 0;
		gbc_jLabelTo.gridy = 4;
		add(getJLabelTo(), gbc_jLabelTo);
		GridBagConstraints gbc_dateTimeWidgetTo = new GridBagConstraints();
		gbc_dateTimeWidgetTo.gridwidth = 2;
		gbc_dateTimeWidgetTo.insets = new Insets(5, 0, 5, 5);
		gbc_dateTimeWidgetTo.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateTimeWidgetTo.gridx = 1;
		gbc_dateTimeWidgetTo.gridy = 4;
		add(getDateTimeWidgetTo(), gbc_dateTimeWidgetTo);
		GridBagConstraints gbc_jButtonRequestData = new GridBagConstraints();
		gbc_jButtonRequestData.gridwidth = 2;
		gbc_jButtonRequestData.insets = new Insets(5, 10, 5, 5);
		gbc_jButtonRequestData.gridx = 0;
		gbc_jButtonRequestData.gridy = 5;
		add(getJButtonRequestData(), gbc_jButtonRequestData);
		GridBagConstraints gbc_jLabelResultInfo = new GridBagConstraints();
		gbc_jLabelResultInfo.anchor = GridBagConstraints.WEST;
		gbc_jLabelResultInfo.gridwidth = 2;
		gbc_jLabelResultInfo.insets = new Insets(5, 5, 5, 0);
		gbc_jLabelResultInfo.gridx = 2;
		gbc_jLabelResultInfo.gridy = 5;
		add(getJLabelResultInfo(), gbc_jLabelResultInfo);
		GridBagConstraints gbc_jScrollPaneResultsTable = new GridBagConstraints();
		gbc_jScrollPaneResultsTable.gridwidth = 4;
		gbc_jScrollPaneResultsTable.insets = new Insets(5, 10, 10, 10);
		gbc_jScrollPaneResultsTable.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneResultsTable.gridx = 0;
		gbc_jScrollPaneResultsTable.gridy = 6;
		add(getJScrollPaneResultsTable(), gbc_jScrollPaneResultsTable);
	}

	private JLabel getJLabelDataSource() {
		if (jLabelDataSource == null) {
			jLabelDataSource = new JLabel("Data Source:");
			jLabelDataSource.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDataSource;
	}
	private JComboBox<String> getJComboBoxDataSource() {
		if (jComboBoxDataSource == null) {
			jComboBoxDataSource = new JComboBox<String>();
			jComboBoxDataSource.setModel(this.getDataSourceComboBoxModel());
			jComboBoxDataSource.addActionListener(this);
		}
		return jComboBoxDataSource;
	}
	private JLabel getJLabelAvailability() {
		if (jLabelAvailability == null) {
			jLabelAvailability = new JLabel("<availability>");
			jLabelAvailability.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelAvailability;
	}
	private ImageIcon getImageIconAvailable() {
		if (imageIconAvailable==null) {
			imageIconAvailable = new ImageIcon(this.getClass().getResource(ICON_PATH_SOURCE_AVAILABLE));
		}
		return imageIconAvailable;
	}
	private ImageIcon getImageIconNotAvailable() {
		if (imageIconNotAvailable==null) {
			imageIconNotAvailable = new ImageIcon(this.getClass().getResource(ICON_PATH_SOURCE_NOT_AVAILABLE));
		}
		return imageIconNotAvailable;
	}
	private ImageIcon getImageIconNotSelected() {
		if (imageIconNotSelected==null) {
			imageIconNotSelected = new ImageIcon(this.getClass().getResource(ICON_PATH_SOURCE_NOT_SELECTED));
		}
		return imageIconNotSelected;
	}
	private JLabel getJLabelDataSeries() {
		if (jLabelDataSeries == null) {
			jLabelDataSeries = new JLabel("Data Series:");
			jLabelDataSeries.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelDataSeries;
	}
	private JComboBox<String> getJComboBoxDataSeries() {
		if (jComboBoxDataSeries == null) {
			jComboBoxDataSeries = new JComboBox<String>();
			jComboBoxDataSeries.setModel(this.getDataSeriesComboBoxModel());
			jComboBoxDataSeries.addActionListener(this);
		}
		return jComboBoxDataSeries;
	}
	private JRadioButton getJRadioButtonSingleValue() {
		if (jRadioButtonSingleValue == null) {
			jRadioButtonSingleValue = new JRadioButton("SingleValue");
			jRadioButtonSingleValue.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonSingleValue.setSelected(true);
			jRadioButtonSingleValue.addActionListener(this);
			this.getSingleValueOrRangeGroup().add(jRadioButtonSingleValue);
		}
		return jRadioButtonSingleValue;
	}
	private JRadioButton getJRadioButtonValueRange() {
		if (jRadioButtonValueRange == null) {
			jRadioButtonValueRange = new JRadioButton("Value Range");
			jRadioButtonValueRange.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonValueRange.addActionListener(this);
			this.getSingleValueOrRangeGroup().add(jRadioButtonValueRange);
		}
		return jRadioButtonValueRange;
	}
	private ButtonGroup getSingleValueOrRangeGroup() {
		if (singleOrRangeButtonGroup==null) {
			singleOrRangeButtonGroup = new ButtonGroup();
		}
		return singleOrRangeButtonGroup;
	}
	private JLabel getJLabelRequest() {
		if (jLabelRequest == null) {
			jLabelRequest = new JLabel("Request:");
			jLabelRequest.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelRequest;
	}
	private JLabel getJLabelFrom() {
		if (jLabelFrom == null) {
			jLabelFrom = new JLabel("At / From:");
			jLabelFrom.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelFrom;
	}
	private DateTimeWidget getDateTimeWidgetFrom() {
		if (dateTimeWidgetFrom == null) {
			dateTimeWidgetFrom = new DateTimeWidget();
		}
		return dateTimeWidgetFrom;
	}
	private JLabel getJLabelTo() {
		if (jLabelTo == null) {
			jLabelTo = new JLabel("To:");
			jLabelTo.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelTo;
	}
	private DateTimeWidget getDateTimeWidgetTo() {
		if (dateTimeWidgetTo == null) {
			dateTimeWidgetTo = new DateTimeWidget();
			dateTimeWidgetTo.setEnabled(false);
		}
		return dateTimeWidgetTo;
	}
	private JButton getJButtonRequestData() {
		if (jButtonRequestData == null) {
			jButtonRequestData = new JButton("Request Data");
			jButtonRequestData.setEnabled(false);
			jButtonRequestData.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonRequestData.addActionListener(this);
		}
		return jButtonRequestData;
	}
	private JLabel getJLabelResultInfo() {
		if (jLabelResultInfo == null) {
			jLabelResultInfo = new JLabel("");
			jLabelResultInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelResultInfo;
	}
	private JScrollPane getJScrollPaneResultsTable() {
		if (jScrollPaneResultsTable == null) {
			jScrollPaneResultsTable = new JScrollPane();
			jScrollPaneResultsTable.setViewportView(getJTableResults());
		}
		return jScrollPaneResultsTable;
	}
	private JTable getJTableResults() {
		if (jTableResults == null) {
			jTableResults = new JTable();
		}
		return jTableResults;
	}
	
	/**
	 * Gets a combo box model containing all available data sources.
	 * @return the data source combo box model
	 */
	private DefaultComboBoxModel<String> getDataSourceComboBoxModel() {
		if (dataSourceComboBoxModel==null) {
			dataSourceComboBoxModel = new DefaultComboBoxModel<String>();
			dataSourceComboBoxModel.addElement(COMBOBOX_ENTRY_NOTHING_SELECTED);
			dataSourceComboBoxModel.addAll(TimeSeriesDataProvider.getInstance().getAvailableDataSourceNames());
		}
		return dataSourceComboBoxModel;
	}
	
	/**
	 * Gets a combo box model containing all available data series for the selected data source.
	 * @return the data series combo box model
	 */
	private DefaultComboBoxModel<String> getDataSeriesComboBoxModel() {
		if (dataSeriesComboBoxModel==null) {
			dataSeriesComboBoxModel = new DefaultComboBoxModel<String>();
			dataSeriesComboBoxModel.addElement(COMBOBOX_ENTRY_NOTHING_SELECTED);
			if (this.selectedDataSource!=null) {
				dataSeriesComboBoxModel.addAll(this.selectedDataSource.getSourceConfiguration().getDataSeriesNames());
			}
		}
		return dataSeriesComboBoxModel;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJComboBoxDataSource()) {
			String selectedSourceName = (String) this.getJComboBoxDataSource().getSelectedItem();
			if (selectedSourceName.equals(COMBOBOX_ENTRY_NOTHING_SELECTED)==false) {
				AbstractDataSource sourceConfig = TimeSeriesDataProvider.getInstance().getDataSource(selectedSourceName);
				this.setSelectedDataSource(sourceConfig);
			}
		} else if (ae.getSource()==this.getJComboBoxDataSeries()) {
			String selectedSeriesName = (String) this.getJComboBoxDataSeries().getSelectedItem();
			AbstractDataSeries selectedSeries = TimeSeriesDataProvider.getInstance().getDataSource(this.selectedDataSource.getName()).getDataSeries(selectedSeriesName);
			this.setSelectedDataSeries(selectedSeries);
		} else if (ae.getSource()==this.getJRadioButtonSingleValue()) {
			this.getDateTimeWidgetTo().setEnabled(false);
		} else if (ae.getSource()==this.getJRadioButtonValueRange()) {
			this.getDateTimeWidgetTo().setEnabled(true);
		} else if (ae.getSource()==this.getJButtonRequestData()) {
			requestData();
		}
	}

	/**
	 * Requests and visualizes data as specified in the UI.
	 */
	private void requestData() {
		long firstTimeStamp = this.getDateTimeWidgetFrom().getDate().getTime();
		
		if (this.getJRadioButtonSingleValue().isSelected()==true) {
			TimeValuePair value = this.selectedDataSeries.getValueForTime(firstTimeStamp);
			if (value!=null) {
				ArrayList<TimeValuePair> resultList = new ArrayList<TimeValuePair>();
				resultList.add(value);
				this.buildTableModel(resultList);
				this.getJLabelResultInfo().setText("Value retreived");
			} else {
				this.getJLabelResultInfo().setText("No value found for the specified time!");
			}
		} else if (this.getJRadioButtonValueRange().isSelected()==true) {
			long lastTimestamp = this.getDateTimeWidgetTo().getDate().getTime();
			List<TimeValuePair> rangeValues = this.selectedDataSeries.getValuesForTimeRange(firstTimeStamp, lastTimestamp);
			this.getJLabelResultInfo().setText("Retrieved " + rangeValues.size() + " values for the specified time range");
			this.buildTableModel(rangeValues);
		}
	}
	
	/**
	 * Builds a table model.
	 * @param resultsList the results list
	 */
	private void buildTableModel(List<TimeValuePair> resultsList) {
		
		String[] columns = {"Time", this.selectedDataSeries.getName()};
		Vector<String> columnsVector = new Vector<String>(Arrays.asList(columns));
		
		Vector<Vector<Object>> dataVector = new Vector<Vector<Object>>();
		
		for (TimeValuePair tvp : resultsList) {
			Instant instant = Instant.ofEpochMilli(tvp.getTimestamp());
			Vector<Object> rowVector = new Vector<Object>();
			rowVector.add(this.getDateTimeFormatter().format(instant));
			rowVector.add(tvp.getValue());
			dataVector.add(rowVector);
		}
		
		DefaultTableModel tableModel = new DefaultTableModel(dataVector, columnsVector);
		this.getJTableResults().setModel(tableModel);
	}

	/**
	 * Sets the selected data source, updates depending UI elements.
	 * @param selectedDataSource the selected data source
	 */
	private void setSelectedDataSource(AbstractDataSource selectedDataSource) {
		this.selectedDataSource = selectedDataSource;
		this.setAvailabilityLabelState();
		
		// --- Rebuild the data series combo box model with the series from the selected source.
		this.setSelectedSeries(null);
		this.dataSeriesComboBoxModel = null;
		this.getJComboBoxDataSeries().setModel(this.getDataSeriesComboBoxModel());
	}
	
	/**
	 * Sets the selected data series, updates depending UI elements.
	 * @param dataSeries the new selected data series
	 */
	private void setSelectedDataSeries(AbstractDataSeries dataSeries) {
		this.selectedDataSeries = dataSeries;
		// --- Clear the results table model
		this.getJTableResults().setModel(new DefaultTableModel());
		
		if (dataSeries!=null) {
			// --- Set the time values according to the series range
			long firstTimeStamp = dataSeries.getFirstTimeStamp();
			long lastTimeStamp = dataSeries.getLastTimeStamp();
			this.getDateTimeWidgetFrom().setDate(firstTimeStamp);
			this.getDateTimeWidgetTo().setDate(lastTimeStamp);

			// --- Enable data requests
			this.getJButtonRequestData().setEnabled(true);
		} else {
			this.getDateTimeWidgetFrom().setDate(0);
			this.getDateTimeWidgetTo().setDate(0);
			this.getJButtonRequestData().setEnabled(false);
		}
	}
	
	/**
	 * Sets the availability label according to the current source's state.
	 */
	private void setAvailabilityLabelState() {
		if (this.selectedDataSource==null) {
			this.getJLabelAvailability().setIcon(this.getImageIconNotSelected());
			this.getJLabelAvailability().setText(LABEL_STRING_SOURCE_NOT_SELECTED);
		} else if (this.selectedDataSource.isAvailable()==true) {
			this.getJLabelAvailability().setIcon(this.getImageIconAvailable());
			this.getJLabelAvailability().setText(LABEL_STRING_SOURCE_AVAILABLE);
		} else {
			this.getJLabelAvailability().setIcon(this.getImageIconNotAvailable());
			this.getJLabelAvailability().setText(LABEL_STRING_SOURCE_NOT_AVAILABLE);
		}
	}

	/**
	 * Sets the selected series.
	 * @param selectedSeries the new selected series
	 */
	private void setSelectedSeries(AbstractDataSeries selectedSeries) {
		if (selectedSeries!=null) {
			long firstTimeStamp = selectedSeries.getFirstTimeStamp();
			long lastTimeStamp = selectedSeries.getLastTimeStamp();
			
			this.getDateTimeWidgetFrom().setDate(firstTimeStamp);
			this.getDateTimeWidgetTo().setDate(lastTimeStamp);
			
			this.getJButtonRequestData().setEnabled(true);
		} else {
			this.getDateTimeWidgetFrom().setDate(null);
			this.getDateTimeWidgetTo().setDate(null);
			this.getJButtonRequestData().setEnabled(false);
		}
	}
	
	/**
	 * Gets the date time formatter.
	 * @return the date time formatter
	 */
	private DateTimeFormatter getDateTimeFormatter() {
		if (dateTimeFormatter==null) {
			dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneId.systemDefault());
		}
		return dateTimeFormatter;
	}
}
