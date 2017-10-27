/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.charts.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import agentgui.core.application.Language;
import agentgui.core.charts.ChartSettingModel;
import agentgui.core.charts.ChartSettingModelListener;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.envModel.graph.components.TableCellEditor4Color;
import agentgui.envModel.graph.components.TableCellRenderer4Color;

/**
 * GUI component for editing the settings for a chart. This component provides means to edit 
 * the settings that are applicable for all kinds of charts. If your chart type requires
 * additional settings, subclass this component and add the required GUI elements.
 *  
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class ChartSettingsTab extends JPanel implements DocumentListener, ChartSettingModelListener, TableModelListener {
	
	private static final long serialVersionUID = 2476599044804448243L;
	
	protected ChartSettingModel chartSettingModel;
	protected ChartEditorJPanel parent;
	
	// Swing components
	private JLabel lblChartTitle;
	private JTextField tfChartTitle;
	private JLabel lblXAxisLabel;
	private JLabel lblYAxisLabel;
	private JLabel lblRendererType;
	private JTextField tfXAxisLabel;
	private JTextField tfYAxisLabel;
	private JComboBox<String> cbRendererType;
	private JScrollPane spTblSeriesSettings;

	private JTable tblSeriesSettings;
	private TableCellRenderer4Color cellRendererColor = null;
	private TableCellEditor4Color cellEditorColor = null;
	private TableCellSpinnerEditor4FloatObject cellEditorSpinner = null;
	

	/**
	 * Instantiates a new chart settings tab.
	 *
	 * @param chartSettingModel the chart setting model
	 * @param parent the parent
	 */
	public ChartSettingsTab(ChartSettingModel chartSettingModel, ChartEditorJPanel parent) {
		this.chartSettingModel = chartSettingModel;
		this.chartSettingModel.addChartSettingModelListener(this);
		this.chartSettingModel.addObserver(parent.getChartTab());
		this.parent = parent;
		this.initialize(); 
		this.setChartSettingModelData();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartSettingModelListener#replaceModel(agentgui.core.charts.ChartSettingModel)
	 */
	@Override
	public void replaceModel(ChartSettingModel newChartSettingModel) {
		if (newChartSettingModel!=this.chartSettingModel) {
			this.chartSettingModel.removeChartSettingModelListener(this);
			this.chartSettingModel.deleteObserver(parent.getChartTab());
			this.chartSettingModel = newChartSettingModel;
			this.chartSettingModel.addChartSettingModelListener(this);
			this.chartSettingModel.addObserver(parent.getChartTab());
			this.chartSettingModel.getTableModelSeriesSettings().addTableModelListener(this);
		}
		
		this.chartSettingModel.setNotificationsEnabled(false);
		this.setChartSettingModelData();
		this.chartSettingModel.setNotificationsEnabled(true);
	}
	
	/**
	 * Sets the chart setting model data to the form.
	 */
	public void setChartSettingModelData() {
		
		this.getTfChartTitle().setText(this.chartSettingModel.getChartTitle());
		this.getTfXAxisLabel().setText(this.chartSettingModel.getChartXAxisLabel());
		this.getTfYAxisLabel().setText(this.chartSettingModel.getChartYAxisLabel());
		this.getCbRendererType().setSelectedItem(this.chartSettingModel.getRenderType());
		
		DefaultTableModel tbModel = this.chartSettingModel.getTableModelSeriesSettings();
		if (tbModel!=this.getTblSeriesSettings().getModel()) {
			this.getTblSeriesSettings().setModel(tbModel);
			this.refreshRenderEditorTblSeriesSettings();	
		}
	}
	
	/**
	 * Initializes the Swing components that are the same for all chart types
	 */
	protected void initialize(){
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
	protected JLabel getLblChartTitle() {
		if (lblChartTitle == null) {
			lblChartTitle = new JLabel(Language.translate("Titel"));
		}
		return lblChartTitle;
	}
	protected JLabel getLblXAxisLabel() {
		if (lblXAxisLabel == null) {
			lblXAxisLabel = new JLabel(Language.translate("Beschriftung X Achse"));
		}
		return lblXAxisLabel;
	}
	protected JLabel getLblYAxisLabel() {
		if (lblYAxisLabel == null) {
			lblYAxisLabel = new JLabel(Language.translate("Beschriftung Y Achse"));
		}
		return lblYAxisLabel;
	}
	protected JLabel getLblRendererType() {
		if (lblRendererType == null) {
			lblRendererType = new JLabel("Renderer");
		}
		return lblRendererType;
	}
	protected JTextField getTfChartTitle() {
		if (tfChartTitle == null) {
			tfChartTitle = new JTextField();
			tfChartTitle.setColumns(10);
			tfChartTitle.setText(chartSettingModel.getChartTitle());
			tfChartTitle.getDocument().addDocumentListener(this);
		}
		return tfChartTitle;
	}
	protected JTextField getTfXAxisLabel() {
		if (tfXAxisLabel == null) {
			tfXAxisLabel = new JTextField();
			tfXAxisLabel.setColumns(10);
			tfXAxisLabel.setText(chartSettingModel.getChartXAxisLabel());
			tfXAxisLabel.getDocument().addDocumentListener(this);
		}
		return tfXAxisLabel;
	}
	protected JTextField getTfYAxisLabel() {
		if (tfYAxisLabel == null) {
			tfYAxisLabel = new JTextField();
			tfYAxisLabel.setColumns(10);
			tfYAxisLabel.setText(chartSettingModel.getChartYAxisLabel());
			tfYAxisLabel.getDocument().addDocumentListener(this);
		}
		return tfYAxisLabel;
	}
	protected JComboBox<String> getCbRendererType() {
		if (cbRendererType == null) {
			cbRendererType = new JComboBox<String>();
			cbRendererType.setModel(new DefaultComboBoxModel<String>(ChartTab.RENDERER_TYPES));
			cbRendererType.setSelectedItem(chartSettingModel.getRenderType());
			cbRendererType.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					setRenderType((String) getCbRendererType().getSelectedItem());
				}
			});
		}
		return cbRendererType;
	}
	protected JScrollPane getSpTblSeriesSettings() {
		if (spTblSeriesSettings == null) {
			spTblSeriesSettings = new JScrollPane();
			spTblSeriesSettings.setViewportView(getTblSeriesSettings());
		}
		return spTblSeriesSettings;
	}
	protected JTable getTblSeriesSettings() {
		if (tblSeriesSettings == null) {
			tblSeriesSettings = new JTable(this.chartSettingModel.getTableModelSeriesSettings());
			tblSeriesSettings.setFillsViewportHeight(true);
			
			this.chartSettingModel.getTableModelSeriesSettings().addTableModelListener(this);
			this.chartSettingModel.updateSeriesList();
			this.refreshRenderEditorTblSeriesSettings();
		}
		return tblSeriesSettings;
	}
	/**
	 * Refreshes renderer and editors of the series table.
	 */
	private void refreshRenderEditorTblSeriesSettings() {
		
		TableColumnModel tcm = this.getTblSeriesSettings().getColumnModel();
		
		TableColumn colorColumn = tcm.getColumn(1);
		colorColumn.setCellEditor(this.getCellEditorColor());
		colorColumn.setCellRenderer(this.getCellRenderer4Color());
		
		TableColumn columnWidth = tcm.getColumn(2);
		columnWidth.setCellEditor(this.getCellEditorSpinner());
	}
	
	private TableCellRenderer4Color getCellRenderer4Color() {
		if(cellRendererColor==null) {
			cellRendererColor = new TableCellRenderer4Color(true);
		}
		return cellRendererColor;
	}
	private TableCellEditor4Color getCellEditorColor(){
		if(cellEditorColor == null){
			cellEditorColor = new TableCellEditor4Color();
		}
		return cellEditorColor;
	}
	private TableCellSpinnerEditor4FloatObject getCellEditorSpinner() {
		if (cellEditorSpinner==null) {
			cellEditorSpinner = new TableCellSpinnerEditor4FloatObject();
		}
		return cellEditorSpinner;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent tme) {

		int seriesIndex = tme.getFirstRow();
		try{
			if(tme.getColumn() == 0){
				this.setSeriesLabel(seriesIndex, (String) this.chartSettingModel.getTableModelSeriesSettings().getValueAt(seriesIndex, 0));
			}else if(tme.getColumn() == 1){
				this.setSeriesColor(seriesIndex, (Color) this.chartSettingModel.getTableModelSeriesSettings().getValueAt(seriesIndex, 1));
			}else if(tme.getColumn() == 2){
				this.setSeriesLineWidth(seriesIndex, (Float) this.chartSettingModel.getTableModelSeriesSettings().getValueAt(seriesIndex, 2));
			}
			
		} catch (NoSuchSeriesException ex) {
			System.err.println("Error changing settings for series "+seriesIndex);
			ex.printStackTrace();
		}
	}
	
	@Override
	public void insertUpdate(DocumentEvent de) {
		this.handleTextFieldUpdate(de);
	}
	@Override
	public void removeUpdate(DocumentEvent de) {
		this.handleTextFieldUpdate(de);
	}
	@Override
	public void changedUpdate(DocumentEvent de) {
		this.handleTextFieldUpdate(de);		
	}
	private void handleTextFieldUpdate(DocumentEvent de) {
		if (de.getDocument()==getTfChartTitle().getDocument()) {
			setChartTitle(getTfChartTitle().getText());
		}else if(de.getDocument() == getTfXAxisLabel().getDocument()){
			setXAxisLabel(getTfXAxisLabel().getText());
		}else if(de.getDocument() == getTfYAxisLabel().getDocument()){
			setYAxisLabel(getTfYAxisLabel().getText());
		}	
	}
	
	/**
	 * Applies chart title changes
	 * @param newTitle The new chart title
	 */
	private void setChartTitle(String newTitle){
		chartSettingModel.setChartTitle(newTitle);
//		parent.getChartTab().getChart().setTitle(newTitle);
	}
	/**
	 * Sets the x axis label for the chart
	 * @param newYAxisLabel The new x axis label
	 */
	private void setXAxisLabel(String newXAxisLabel){
		chartSettingModel.setChartXAxisLabel(newXAxisLabel);
//		parent.getChartTab().setXAxisLabel(newXAxisLabel);
	}
	/**
	 * Sets the y axis label for the chart
	 * @param newYAxisLabel The new y axis label
	 */
	private void setYAxisLabel(String newYAxisLabel){
		chartSettingModel.setChartYAxisLabel(newYAxisLabel);
//		parent.getChartTab().setYAxisLabel(newYAxisLabel);
	}
	/**
	 * Sets the renderer type for the chart
	 * @param newRendererType The new renderer type
	 */
	private void setRenderType(String newRendererType){
		chartSettingModel.setRenderType(newRendererType);
//		parent.getChartTab().setRenderer(newRendererType);
	}
	/**
	 * Sets the series label for a data series, specified by its index 
	 * @param seriesIndex The series index
	 * @param newLabel The new series label
	 * @throws NoSuchSeriesException Invalid series index
	 */
	private void setSeriesLabel(int seriesIndex, String newLabel) throws NoSuchSeriesException{
		chartSettingModel.setSeriesLabel(seriesIndex, newLabel);
//		parent.getChartTab().getChart().fireChartChanged();		// Trigger repaint 
	}
	/**
	 * Sets the plot color for a data series, specified by its index 
	 * @param seriesIndex The series index
	 * @param newLabel The new plot color
	 * @throws NoSuchSeriesException Invalid series index
	 */
	private void setSeriesColor(int seriesIndex, Color newColor) throws NoSuchSeriesException{
		chartSettingModel.setSeriesColor(seriesIndex, newColor);
//		parent.getChartTab().setSeriesColor(seriesIndex, newColor);
	}
	/**
	 * Sets the plot line width for a data series, specified by its index 
	 * @param seriesIndex The series index
	 * @param newLabel The new plot line width
	 * @throws NoSuchSeriesException Invalid series index
	 */
	private void setSeriesLineWidth(int seriesIndex, Float newWidth) throws NoSuchSeriesException{
		chartSettingModel.setSeriesLineWidth(seriesIndex, newWidth);
//		parent.getChartTab().setSeriesLineWidth(seriesIndex, newWidth);
	}
	
	/**
	 * Can be used to notify underlying elements to stop edit actions.
	 */
	public void stopEditing() {
		if (getTblSeriesSettings()!=null && getTblSeriesSettings().isEditing()==true) {
			getTblSeriesSettings().getCellEditor().stopCellEditing();
		}
	}

}
