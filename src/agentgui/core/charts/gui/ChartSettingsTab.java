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
import java.util.Vector;

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
import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.envModel.graph.components.TableCellEditor4Color;
import agentgui.envModel.graph.components.TableCellRenderer4Color;
import agentgui.ontology.DataSeries;

/**
 * GUI component for editing the settings for a chart. This component provides means to edit 
 * the settings that are applicable for all kinds of charts. If your chart type requires
 * additional settings, subclass this component and add the required GUI elements.
 *  
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 *
 */
public class ChartSettingsTab extends JPanel implements TableModelListener, DocumentListener {
	
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
	
	protected DataModel model;
	
	private DefaultTableModel myTableModel;
	private JTable tblSeriesSettings;
	
	private TableCellRenderer4Color cellRendererColor = null;
	private TableCellEditor4Color cellEditorColor = null;
	private TableCellSpinnerEditor4FloatObject cellEditorSpinner = null;
	
	protected ChartEditorJPanel parent;
	
	
	public ChartSettingsTab(DataModel model, ChartEditorJPanel parent) {
		this.model = model;
		this.parent = parent;
		this.initialize(); 
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
			lblRendererType = new JLabel(Language.translate("Art der Darstellung"));
		}
		return lblRendererType;
	}
	protected JTextField getTfChartTitle() {
		if (tfChartTitle == null) {
			tfChartTitle = new JTextField();
			tfChartTitle.setColumns(10);
			tfChartTitle.setText(model.getOntologyModel().getChartSettings().getChartTitle());
			tfChartTitle.getDocument().addDocumentListener(this);
		}
		return tfChartTitle;
	}
	protected JTextField getTfXAxisLabel() {
		if (tfXAxisLabel == null) {
			tfXAxisLabel = new JTextField();
			tfXAxisLabel.setColumns(10);
			tfXAxisLabel.setText(model.getOntologyModel().getChartSettings().getXAxisLabel());
			tfXAxisLabel.getDocument().addDocumentListener(this);
		}
		return tfXAxisLabel;
	}
	protected JTextField getTfYAxisLabel() {
		if (tfYAxisLabel == null) {
			tfYAxisLabel = new JTextField();
			tfYAxisLabel.setColumns(10);
			tfYAxisLabel.setText(model.getOntologyModel().getChartSettings().getYAxisLabel());
			tfYAxisLabel.getDocument().addDocumentListener(this);
		}
		return tfYAxisLabel;
	}
	protected JComboBox getCbRendererType() {
		if (cbRendererType == null) {
			cbRendererType = new JComboBox();
			cbRendererType.setModel(new DefaultComboBoxModel(ChartTab.RENDERER_TYPES));
			cbRendererType.setSelectedItem(model.getOntologyModel().getChartSettings().getRendererType());
			cbRendererType.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					setRendererType((String) getCbRendererType().getSelectedItem());
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
			tblSeriesSettings = new JTable(this.getTableModel());
			tblSeriesSettings.setFillsViewportHeight(true);
			this.refreshTableModel();
			
			TableColumnModel tcm = tblSeriesSettings.getColumnModel();
			
			TableColumn colorColumn = tcm.getColumn(1);
			colorColumn.setCellEditor(this.getCellEditorColor());
			colorColumn.setCellRenderer(this.getCellRenderer4Color());
			
			TableColumn columnWidth = tcm.getColumn(2);
			columnWidth.setCellEditor(this.getCellEditorSpinner());
			
		}
		return tblSeriesSettings;
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
	
	
	/**
	 * Creates the table model for the settings table
	 * @return The table model
	 */
	private DefaultTableModel getTableModel(){
		// --- Initialize model and columns ---------------
		if (this.myTableModel==null) {
			myTableModel = new DefaultTableModel();
			myTableModel.addColumn(Language.translate("Name"));
			myTableModel.addColumn(Language.translate("Farbe"));
			myTableModel.addColumn(Language.translate("Liniendicke"));
			myTableModel.addTableModelListener(this);
		}
		return myTableModel;
	}

	private void refreshTableModel() {

		// --- Remove all elements first ----------------------------
		while(this.getTableModel().getRowCount()>0) {
			this.getTableModel().removeRow(0);
		}
		
		// --- Add rows containing the series specific settings -----
		for(int i=0; i < model.getSeriesCount(); i++){
				
			// Extract series settings from the ontology model
			DataSeries series = (DataSeries) model.getOntologyModel().getChartData().get(i);
			
			String rgb = null;
			if (model.getOntologyModel().getChartSettings().getYAxisColors().size() < (i+1)) {
				rgb = ((Integer) DataModel.DEFAULT_COLORS[i % DataModel.DEFAULT_COLORS.length].getRGB()).toString();
				model.getOntologyModel().getChartSettings().getYAxisColors().add(i, rgb);
			} else {
				rgb = (String) model.getOntologyModel().getChartSettings().getYAxisColors().get(i);
			}
			
			Float width = null; 
			if (model.getOntologyModel().getChartSettings().getYAxisLineWidth().size() < (i+1)) {
				width = DataModel.DEFAULT_LINE_WIDTH;
				model.getOntologyModel().getChartSettings().getYAxisLineWidth().add(i, width);
			} else {
				width = (Float) model.getOntologyModel().getChartSettings().getYAxisLineWidth().get(i);
			}
			
			// Create a table row for the series
			Vector<Object> rowVector = new Vector<Object>();
			rowVector.add(series.getLabel());
			rowVector.add(new Color(Integer.parseInt(rgb)));
			rowVector.add(width);
			
			this.getTableModel().addRow(rowVector);
		}
	}
	
	public void addSeries(DataSeries series){
		
		String label = series.getLabel();
		Color color = new Color(Integer.parseInt((String) model.getOntologyModel().getChartSettings().getYAxisColors().get(model.getSeriesCount()-1)));
		Float lineWidth = (Float) model.getOntologyModel().getChartSettings().getYAxisLineWidth().get(model.getSeriesCount()-1);
		
		Object[] newRow = {label, color, lineWidth};
		
		((DefaultTableModel)getTblSeriesSettings().getModel()).addRow(newRow);
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent tme) {
		// Handle changes of series-specific settings
		int seriesIndex = tme.getFirstRow();
		try{
			if(tme.getColumn() == 0){
				setSeriesLabel(seriesIndex, (String) tblSeriesSettings.getModel().getValueAt(seriesIndex, 0));
			}else if(tme.getColumn() == 1){
				setSeriesColor(seriesIndex, (Color) tblSeriesSettings.getModel().getValueAt(seriesIndex, 1));
			}else if(tme.getColumn() == 2){
				setSeriesLineWidth(seriesIndex, (Float) tblSeriesSettings.getModel().getValueAt(seriesIndex, 2));
			}
			
		}catch (NoSuchSeriesException ex) {
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
	
	public void seriesRemoved(int seriesIndex){
		((DefaultTableModel)getTblSeriesSettings().getModel()).removeRow(seriesIndex);
	}
	
	public void replaceModel(DataModel newModel){
		this.model = newModel;
		DefaultTableModel dtm = (DefaultTableModel) this.getTblSeriesSettings().getModel();
		if (dtm.getColumnCount()==0) {
			this.getTblSeriesSettings().setModel(getTableModel());
		}
		this.refreshTableModel();
	}
	
	/**
	 * Applies chart title changes
	 * @param newTitle The new chart title
	 */
	private void setChartTitle(String newTitle){
		model.getOntologyModel().getChartSettings().setChartTitle(newTitle);
		parent.getChartTab().getChart().setTitle(newTitle);
	}
	/**
	 * Sets the x axis label for the chart
	 * @param newYAxisLabel The new x axis label
	 */
	private void setXAxisLabel(String newXAxisLabel){
		model.getOntologyModel().getChartSettings().setXAxisLabel(newXAxisLabel);
		parent.getChartTab().setXAxisLabel(newXAxisLabel);
		model.getTableModel().setKeyColumnLabel(newXAxisLabel);
	}
	/**
	 * Sets the y axis label for the chart
	 * @param newYAxisLabel The new y axis label
	 */
	private void setYAxisLabel(String newYAxisLabel){
		model.getOntologyModel().getChartSettings().setYAxisLabel(newYAxisLabel);
		parent.getChartTab().setYAxisLabel(newYAxisLabel);
		model.getTableModel().setKeyColumnLabel(newYAxisLabel);
	}
	/**
	 * Sets the renderer type for the chart
	 * @param newRendererType The new renderer type
	 */
	private void setRendererType(String newRendererType){
		model.getOntologyModel().getChartSettings().setRendererType(newRendererType);
		parent.getChartTab().setRenderer(newRendererType);
	}
	/**
	 * Sets the series label for a data series, specified by its index 
	 * @param seriesIndex The series index
	 * @param newLabel The new series label
	 * @throws NoSuchSeriesException Invalid series index
	 */
	private void setSeriesLabel(int seriesIndex, String newLabel) throws NoSuchSeriesException{
		model.getOntologyModel().getSeries(seriesIndex).setLabel(newLabel);
		model.getChartModel().getSeries(seriesIndex).setKey(newLabel);
	}
	/**
	 * Sets the plot color for a data series, specified by its index 
	 * @param seriesIndex The series index
	 * @param newLabel The new plot color
	 * @throws NoSuchSeriesException Invalid series index
	 */
	private void setSeriesColor(int seriesIndex, Color newColor) throws NoSuchSeriesException{
		model.getOntologyModel().getChartSettings().getYAxisColors().remove(seriesIndex);
		model.getOntologyModel().getChartSettings().getYAxisColors().add(seriesIndex, ""+newColor.getRGB());
		parent.getChartTab().setSeriesColor(seriesIndex, newColor);
	}
	/**
	 * Sets the plot line width for a data series, specified by its index 
	 * @param seriesIndex The series index
	 * @param newLabel The new plot line width
	 * @throws NoSuchSeriesException Invalid series index
	 */
	private void setSeriesLineWidth(int seriesIndex, Float newWidth) throws NoSuchSeriesException{
		model.getOntologyModel().getChartSettings().getYAxisLineWidth().remove(seriesIndex);
		model.getOntologyModel().getChartSettings().getYAxisLineWidth().add(seriesIndex, newWidth);
		parent.getChartTab().setSeriesLineWidth(seriesIndex, newWidth);
	}

}
