package agentgui.core.charts.timeseriesChart.gui;

import javax.swing.JPanel;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;

import agentgui.core.application.Language;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.SeriesSettings;
import agentgui.core.charts.SettingsInfo;
import agentgui.core.charts.timeseriesChart.TimeSeriesChartSettings;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.envModel.graph.components.TableCellEditor4Color;
import agentgui.envModel.graph.components.TableCellRenderer4Color;
import agentgui.ontology.TimeSeries;

public class TimeSeriesSettingsTab extends JPanel implements ActionListener, TableModelListener, FocusListener, Observer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2476599044804448243L;
	
	// Swing components
	private JLabel lblChartTitle;
	private JTextField tfChartTitle;
	private JLabel lblXAxisLabel;
	private JLabel lblYAxisLabel;
	private JLabel lblRendererType;
	private JTextField tfXAxisLabel;
	private JTextField tfYAxisLabel;
	private JComboBox cbRendererType;
	private JScrollPane spTblSeriesSettings;
	private JTable tblSeriesSettings;
	
	private TimeSeriesDataModel model;
	
	private TimeSeriesChartSettings settings;

	public TimeSeriesSettingsTab(TimeSeriesDataModel model) {
		
		this.model = model;
		
		this.settings = model.getChartSettings();
		this.settings.addObserver(this);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblChartTitle = new GridBagConstraints();
		gbc_lblChartTitle.insets = new Insets(5, 5, 5, 5);
		gbc_lblChartTitle.anchor = GridBagConstraints.WEST;
		gbc_lblChartTitle.gridx = 0;
		gbc_lblChartTitle.gridy = 0;
		add(getLblChartTitle(), gbc_lblChartTitle);
		GridBagConstraints gbc_tfChartTitle = new GridBagConstraints();
		gbc_tfChartTitle.weightx = 10.0;
		gbc_tfChartTitle.insets = new Insets(0, 0, 5, 0);
		gbc_tfChartTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfChartTitle.gridx = 1;
		gbc_tfChartTitle.gridy = 0;
		add(getTfChartTitle(), gbc_tfChartTitle);
		GridBagConstraints gbc_lblXAxisLabel = new GridBagConstraints();
		gbc_lblXAxisLabel.anchor = GridBagConstraints.WEST;
		gbc_lblXAxisLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblXAxisLabel.gridx = 0;
		gbc_lblXAxisLabel.gridy = 1;
		add(getLblXAxisLabel(), gbc_lblXAxisLabel);
		GridBagConstraints gbc_tfXAxisLabel = new GridBagConstraints();
		gbc_tfXAxisLabel.insets = new Insets(0, 0, 5, 0);
		gbc_tfXAxisLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfXAxisLabel.gridx = 1;
		gbc_tfXAxisLabel.gridy = 1;
		add(getTfXAxisLabel(), gbc_tfXAxisLabel);
		GridBagConstraints gbc_lblYAxisLabel = new GridBagConstraints();
		gbc_lblYAxisLabel.anchor = GridBagConstraints.WEST;
		gbc_lblYAxisLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblYAxisLabel.gridx = 0;
		gbc_lblYAxisLabel.gridy = 2;
		add(getLblYAxisLabel(), gbc_lblYAxisLabel);
		GridBagConstraints gbc_tfYAxisLabel = new GridBagConstraints();
		gbc_tfYAxisLabel.insets = new Insets(0, 0, 5, 0);
		gbc_tfYAxisLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfYAxisLabel.gridx = 1;
		gbc_tfYAxisLabel.gridy = 2;
		add(getTfYAxisLabel(), gbc_tfYAxisLabel);
		GridBagConstraints gbc_lblRendererType = new GridBagConstraints();
		gbc_lblRendererType.anchor = GridBagConstraints.WEST;
		gbc_lblRendererType.insets = new Insets(5, 5, 5, 5);
		gbc_lblRendererType.gridx = 0;
		gbc_lblRendererType.gridy = 3;
		add(getLblRendererType(), gbc_lblRendererType);
		GridBagConstraints gbc_cbRendererType = new GridBagConstraints();
		gbc_cbRendererType.insets = new Insets(0, 0, 5, 0);
		gbc_cbRendererType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbRendererType.gridx = 1;
		gbc_cbRendererType.gridy = 3;
		add(getCbRendererType(), gbc_cbRendererType);
		GridBagConstraints gbc_spTblSeriesSettings = new GridBagConstraints();
		gbc_spTblSeriesSettings.gridheight = 2;
		gbc_spTblSeriesSettings.gridwidth = 2;
		gbc_spTblSeriesSettings.fill = GridBagConstraints.BOTH;
		gbc_spTblSeriesSettings.gridx = 0;
		gbc_spTblSeriesSettings.gridy = 4;
		add(getSpTblSeriesSettings(), gbc_spTblSeriesSettings);
	}
	private JLabel getLblChartTitle() {
		if (lblChartTitle == null) {
			lblChartTitle = new JLabel(Language.translate("Titel"));
		}
		return lblChartTitle;
	}
	private JLabel getLblXAxisLabel() {
		if (lblXAxisLabel == null) {
			lblXAxisLabel = new JLabel(Language.translate("Beschriftung X Achse"));
		}
		return lblXAxisLabel;
	}
	private JLabel getLblYAxisLabel() {
		if (lblYAxisLabel == null) {
			lblYAxisLabel = new JLabel(Language.translate("Beschriftung Y Achse"));
		}
		return lblYAxisLabel;
	}
	private JLabel getLblRendererType() {
		if (lblRendererType == null) {
			lblRendererType = new JLabel(Language.translate("Art der Darstellung"));
		}
		return lblRendererType;
	}
	private JTextField getTfChartTitle() {
		if (tfChartTitle == null) {
			tfChartTitle = new JTextField();
			tfChartTitle.setColumns(10);
			tfChartTitle.setText(settings.getChartTitle());
			tfChartTitle.addActionListener(this);
			tfChartTitle.addFocusListener(this);
		}
		return tfChartTitle;
	}
	private JTextField getTfXAxisLabel() {
		if (tfXAxisLabel == null) {
			tfXAxisLabel = new JTextField();
			tfXAxisLabel.setColumns(10);
			tfXAxisLabel.setText(settings.getxAxisLabel());
			tfXAxisLabel.addActionListener(this);
			tfXAxisLabel.addFocusListener(this);
		}
		return tfXAxisLabel;
	}
	private JTextField getTfYAxisLabel() {
		if (tfYAxisLabel == null) {
			tfYAxisLabel = new JTextField();
			tfYAxisLabel.setColumns(10);
			tfYAxisLabel.setText(settings.getyAxisLabel());
			tfYAxisLabel.addActionListener(this);
			tfYAxisLabel.addFocusListener(this);
		}
		return tfYAxisLabel;
	}
	private JComboBox getCbRendererType() {
		if (cbRendererType == null) {
			cbRendererType = new JComboBox();
			cbRendererType.setModel(new DefaultComboBoxModel(TimeSeriesChartTab.RENDERER_TYPES));
			cbRendererType.setSelectedItem(settings.getRendererType());
			cbRendererType.addActionListener(this);
		}
		return cbRendererType;
	}
	private JScrollPane getSpTblSeriesSettings() {
		if (spTblSeriesSettings == null) {
			spTblSeriesSettings = new JScrollPane();
			spTblSeriesSettings.setViewportView(getTblSeriesSettings());
		}
		return spTblSeriesSettings;
	}
	private JTable getTblSeriesSettings() {
		if (tblSeriesSettings == null) {
			tblSeriesSettings = new JTable(){

				/**
				 * 
				 */
				private static final long serialVersionUID = -2409483712205505262L;

				/* (non-Javadoc)
				 * @see javax.swing.JTable#getCellEditor(int, int)
				 */
				@Override
				public TableCellEditor getCellEditor(int row, int column) {
					if(column == 1){
						return new TableCellEditor4Color();
					}else if(column == 2){
						return new TableCellSpinnerEditor4FloatObject();
					}else{
						return super.getCellEditor(row, column);
					}
				}

				/* (non-Javadoc)
				 * @see javax.swing.JTable#getCellRenderer(int, int)
				 */
				@Override
				public TableCellRenderer getCellRenderer(int row, int column) {
					if(column == 1){
						return new TableCellRenderer4Color(true);
					}else{
						return super.getCellRenderer(row, column);
					}
				}
				
				
			};
			tblSeriesSettings.setFillsViewportHeight(true);
			tblSeriesSettings.setModel(initTableModel());
		}
		return tblSeriesSettings;
	}
	
	private DefaultTableModel initTableModel(){
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn(Language.translate("Name"));
		tableModel.addColumn(Language.translate("Farbe"));
		tableModel.addColumn(Language.translate("Liniendicke"));
		
		tableModel.addTableModelListener(this);
		return tableModel;
	}
	
	public void addSeries(TimeSeries series){
		
		String label = series.getLabel();
		Color color = new Color(Integer.parseInt((String) model.getOntologyModel().getGeneralSettings().getYAxisColors().get(model.getSeriesCount()-1)));
		Float lineWidth = (Float) model.getOntologyModel().getGeneralSettings().getYAxisLineWidth().get(model.getSeriesCount()-1);
		
		Object[] newRow = {label, color, lineWidth};
		
		((DefaultTableModel)getTblSeriesSettings().getModel()).addRow(newRow);
		
		settings.getSeriesSettings().add(new SeriesSettings(label, color, lineWidth));
		
	}
	
	public void addObserver(Observer observer){
		settings.addObserver(observer);
	}
	@Override
	public void tableChanged(TableModelEvent tme) {
		int seriesIndex = tme.getFirstRow();
		try{
			if(tme.getColumn() == 0){
				settings.setSeriesLabel(seriesIndex, (String) tblSeriesSettings.getModel().getValueAt(seriesIndex, 0));
			}else if(tme.getColumn() == 1){
				settings.setSeriesColor(seriesIndex, (Color) tblSeriesSettings.getModel().getValueAt(seriesIndex, 1));
			}else if(tme.getColumn() == 2){
				settings.setSeriesLineWidth(seriesIndex, (Float) tblSeriesSettings.getModel().getValueAt(seriesIndex, 2));
			}
		}catch (NoSuchSeriesException ex) {
			System.err.println("Error changing settings for series "+seriesIndex);
			ex.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		handleEvent(e);
	}
	@Override
	public void focusGained(FocusEvent e) {
		// Nothing to be done here
	}
	@Override
	public void focusLost(FocusEvent e) {
		handleEvent(e);
	}
	
	/**
	 * ActionEvent and FocusEvent are handled the same way 
	 * @param e
	 */
	private void handleEvent(AWTEvent e){
		if(e.getSource() == getTfChartTitle()){
			if(!getTfChartTitle().getText().equals(settings.getChartTitle())){
				settings.setChartTitle(getTfChartTitle().getText());
			}
		}else if(e.getSource() == getTfXAxisLabel()){
			if(!getTfXAxisLabel().equals(settings.getxAxisLabel())){
				settings.setxAxisLabel(getTfXAxisLabel().getText());
			}
		}else if(e.getSource() == getTfYAxisLabel()){
			if(!getTfYAxisLabel().equals(settings.getyAxisLabel())){
				settings.setyAxisLabel(getTfYAxisLabel().getText());
			}
		}else if(e.getSource() == getCbRendererType()){
			if(!getCbRendererType().getSelectedItem().equals(settings.getRendererType())){
				settings.setRendererType((String) getCbRendererType().getSelectedItem());
			}
		}
	}
	@Override
	public void update(Observable o, Object arg) {
		if(o == this.settings && arg instanceof SettingsInfo){
			SettingsInfo info = (SettingsInfo) arg;
			if(info.getType() == SettingsInfo.SERIES_REMOVED){
				int seriesIndex = info.getSeriesIndex();
				DefaultTableModel tableModel = (DefaultTableModel) getTblSeriesSettings().getModel();
				tableModel.removeRow(seriesIndex);
			}else if(info.getType() == SettingsInfo.SERIES_ADDED){
				SeriesSettings settings = (SeriesSettings) info.getData();
				String label = settings.getLabel();
				Color color = settings.getColor();
				Float lineWidth = settings.getLineWIdth();
				Vector<Object> newRow = new Vector<Object>();
				newRow.add(label);
				newRow.add(color);
				newRow.add(lineWidth);
				DefaultTableModel tableModel = (DefaultTableModel) getTblSeriesSettings().getModel();
				tableModel.addRow(newRow);
			}
		}
	}
}
