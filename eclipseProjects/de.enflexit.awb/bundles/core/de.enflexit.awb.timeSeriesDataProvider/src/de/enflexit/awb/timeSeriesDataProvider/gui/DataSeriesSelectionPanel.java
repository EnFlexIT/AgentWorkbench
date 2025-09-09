package de.enflexit.awb.timeSeriesDataProvider.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.common.swing.JComboBoxWide;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class DataSeriesSelectionPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 3299893828069758534L;
	
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
	private JComboBoxWide<String> jComboBoxDataSeries;
	private DefaultComboBoxModel<String>  dataSeriesComboBoxModel;
	
	private AbstractDataSource selectedDataSource;
	private AbstractDataSeries selectedDataSeries;
	
	private boolean pauseListener;
	
	public DataSeriesSelectionPanel() {
		initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelDataSource = new GridBagConstraints();
		gbc_jLabelDataSource.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataSource.insets = new Insets(10, 10, 5, 5);
		gbc_jLabelDataSource.gridx = 0;
		gbc_jLabelDataSource.gridy = 0;
		add(getJLabelDataSource(), gbc_jLabelDataSource);
		GridBagConstraints gbc_jComboBoxDataSource = new GridBagConstraints();
		gbc_jComboBoxDataSource.insets = new Insets(10, 5, 5, 5);
		gbc_jComboBoxDataSource.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxDataSource.gridx = 1;
		gbc_jComboBoxDataSource.gridy = 0;
		add(getJComboBoxDataSource(), gbc_jComboBoxDataSource);
		GridBagConstraints gbc_jLabelAvailability = new GridBagConstraints();
		gbc_jLabelAvailability.anchor = GridBagConstraints.WEST;
		gbc_jLabelAvailability.insets = new Insets(10, 5, 5, 0);
		gbc_jLabelAvailability.gridx = 2;
		gbc_jLabelAvailability.gridy = 0;
		add(getJLabelAvailability(), gbc_jLabelAvailability);
		GridBagConstraints gbc_jLabelDataSeries = new GridBagConstraints();
		gbc_jLabelDataSeries.anchor = GridBagConstraints.WEST;
		gbc_jLabelDataSeries.insets = new Insets(5, 10, 0, 5);
		gbc_jLabelDataSeries.gridx = 0;
		gbc_jLabelDataSeries.gridy = 1;
		add(getJLabelDataSeries(), gbc_jLabelDataSeries);
		GridBagConstraints gbc_jComboBoxDataSeries = new GridBagConstraints();
		gbc_jComboBoxDataSeries.insets = new Insets(5, 5, 5, 5);
		gbc_jComboBoxDataSeries.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxDataSeries.gridx = 1;
		gbc_jComboBoxDataSeries.gridy = 1;
		add(getJComboBoxDataSeries(), gbc_jComboBoxDataSeries);
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
	private DefaultComboBoxModel<String> getDataSourceComboBoxModel() {
		if (dataSourceComboBoxModel==null) {
			dataSourceComboBoxModel = new DefaultComboBoxModel<String>();
			dataSourceComboBoxModel.addElement(COMBOBOX_ENTRY_NOTHING_SELECTED);
			dataSourceComboBoxModel.addAll(TimeSeriesDataProvider.getInstance().getAvailableDataSourceNames());
		}
		return dataSourceComboBoxModel;
	}
	private JLabel getJLabelAvailability() {
		if (jLabelAvailability == null) {
			jLabelAvailability = new JLabel("");
			jLabelAvailability.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelAvailability.setIcon(this.getImageIconNotSelected());
			jLabelAvailability.setText(LABEL_STRING_SOURCE_NOT_SELECTED);
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
	private JComboBoxWide<String> getJComboBoxDataSeries() {
		if (jComboBoxDataSeries == null) {
			jComboBoxDataSeries = new JComboBoxWide<String>(this.getDataSeriesComboBoxModel());
			jComboBoxDataSeries.setMaximumRowCount(18);
			jComboBoxDataSeries.setPreferredSize(new Dimension(205, 26));
			jComboBoxDataSeries.setMaximumSize(new Dimension(205, 26));
			jComboBoxDataSeries.addActionListener(this);
		}
		return jComboBoxDataSeries;
	}
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
	/**
	 * Gets the selected data source.
	 * @return the selected data source
	 */
	public AbstractDataSource getSelectedDataSource() {
		return selectedDataSource;
	}
	/**
	 * Sets the selected data source.
	 * @param dataSource the new selected data source
	 */
	public void setSelectedDataSource(AbstractDataSource dataSource) {
		this.selectedDataSource = dataSource;
		
		// --- If the source was not set from the combo box, update the selection
		this.pauseListener = true;
		String currentSelection = (String) this.getJComboBoxDataSource().getSelectedItem();
		if (dataSource!=null) {
			if (currentSelection.equals(dataSource.getName())==false) {
				this.getJComboBoxDataSource().setSelectedItem(dataSource.getName());
			}
		} else {
			this.getJComboBoxDataSource().setSelectedItem(COMBOBOX_ENTRY_NOTHING_SELECTED);
		}
		this.pauseListener = false;
		
		this.setAvailabilityLabelState();
		
		// --- Rebuild the data series combo box model with the series from the selected source.
		this.setSelectedDataSeries(null);
		this.dataSeriesComboBoxModel = null;
		this.getJComboBoxDataSeries().setModel(this.getDataSeriesComboBoxModel());
	}
	
	/**
	 * Gets the selected data series.
	 * @return the selected data series
	 */
	public AbstractDataSeries getSelectedDataSeries() {
		return selectedDataSeries;
	}
	
	/**
	 * Sets the selected data series.
	 * @param dataSeries the new selected data series
	 */
	public void setSelectedDataSeries(AbstractDataSeries dataSeries) {
		this.selectedDataSeries = dataSeries;
		
		// --- If the series was not set from the combo box, update the selection   
		String currentSelection = (String) this.getJComboBoxDataSeries().getSelectedItem();
		if (dataSeries!=null) {
			if (currentSelection.equals(dataSeries.getName())==false) {
				this.getJComboBoxDataSeries().setSelectedItem(dataSeries.getName());
			}
		} else {
			this.getJComboBoxDataSeries().setSelectedItem(COMBOBOX_ENTRY_NOTHING_SELECTED);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJComboBoxDataSource() && this.pauseListener==false) {
			AbstractDataSource selectedSource = null;
			String selectedSourceName = (String) this.getJComboBoxDataSource().getSelectedItem();
			if (selectedSourceName.equals(COMBOBOX_ENTRY_NOTHING_SELECTED)==false) {
				selectedSource = TimeSeriesDataProvider.getInstance().getDataSource(selectedSourceName);
			}
			this.setSelectedDataSource(selectedSource);
		} else if(ae.getSource()==this.getJComboBoxDataSeries()){
			AbstractDataSeries selectedSeries = null;
			String selectedSeriesName = (String) this.getJComboBoxDataSeries().getSelectedItem();
			if (selectedSeriesName.equals(COMBOBOX_ENTRY_NOTHING_SELECTED)==false) {
				selectedSeries = this.getSelectedDataSource().getDataSeries(selectedSeriesName);
			}
			this.setSelectedDataSeries(selectedSeries);
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
}

