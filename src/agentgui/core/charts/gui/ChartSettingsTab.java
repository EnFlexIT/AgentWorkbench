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
import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.SeriesSettings;
import agentgui.envModel.graph.components.TableCellEditor4Color;
import agentgui.envModel.graph.components.TableCellRenderer4Color;
import agentgui.ontology.DataSeries;

public class ChartSettingsTab extends JPanel implements ActionListener, TableModelListener, FocusListener{
	
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
	
	protected DataModel model;
	
//	protected ChartSettings settings;

	// -------------------------------------------------------------------------------------------------
	// --- Only activate this constructor if you want to redesign this dialog with a visual editor !! --
	// -------------------------------------------------------------------------------------------------
//	public ChartSettingsTab() {
//		this.initialize(); 
//	}
	// -------------------------------------------------------------------------------------------------
	// --- Only activate this constructor if you want to redesign this dialog with a visual editor !! --
	// -------------------------------------------------------------------------------------------------
	
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
			tfChartTitle.setText(model.getChartSettings().getChartTitle());
			tfChartTitle.addActionListener(this);
			tfChartTitle.addFocusListener(this);
		}
		return tfChartTitle;
	}
	protected JTextField getTfXAxisLabel() {
		if (tfXAxisLabel == null) {
			tfXAxisLabel = new JTextField();
			tfXAxisLabel.setColumns(10);
			tfXAxisLabel.setText(model.getChartSettings().getxAxisLabel());
			tfXAxisLabel.addActionListener(this);
			tfXAxisLabel.addFocusListener(this);
		}
		return tfXAxisLabel;
	}
	protected JTextField getTfYAxisLabel() {
		if (tfYAxisLabel == null) {
			tfYAxisLabel = new JTextField();
			tfYAxisLabel.setColumns(10);
			tfYAxisLabel.setText(model.getChartSettings().getyAxisLabel());
			tfYAxisLabel.addActionListener(this);
			tfYAxisLabel.addFocusListener(this);
		}
		return tfYAxisLabel;
	}
	protected JComboBox getCbRendererType() {
		if (cbRendererType == null) {
			cbRendererType = new JComboBox();
			cbRendererType.setModel(new DefaultComboBoxModel(ChartTab.RENDERER_TYPES));
			cbRendererType.setSelectedItem(model.getChartSettings().getRendererType());
			cbRendererType.addActionListener(this);
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
			tblSeriesSettings = new JTable(){

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
			tblSeriesSettings.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		}
		return tblSeriesSettings;
	}
	
	/**
	 * Creates the table model for the settings table
	 * @return The table model
	 */
	private DefaultTableModel initTableModel(){
		// --- Initialize model and columns ---------------
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn(Language.translate("Name"));
		tableModel.addColumn(Language.translate("Farbe"));
		tableModel.addColumn(Language.translate("Liniendicke"));
		
		// --- Add rows containing the series specific settings
		for(int i=0; i < model.getSeriesCount(); i++){
			SeriesSettings settings;
			try {
				settings = model.getChartSettings().getSeriesSettings(i);
				String seriesLabel = settings.getLabel();
				Color seriesColor = settings.getColor();
				Float seriesLineWidth = settings.getLineWIdth();
				tableModel.addRow(new Object[]{seriesLabel, seriesColor, seriesLineWidth});
			} catch (NoSuchSeriesException e) {
				System.err.println("Error: No settings for data series "+i+" found!");
				e.printStackTrace();
			}
			
			
		}
		
		tableModel.addTableModelListener(this);
		return tableModel;
	}
	
	public void addSeries(DataSeries series){
		
		String label = series.getLabel();
		Color color = new Color(Integer.parseInt((String) model.getOntologyModel().getChartSettings().getYAxisColors().get(model.getSeriesCount()-1)));
		Float lineWidth = (Float) model.getOntologyModel().getChartSettings().getYAxisLineWidth().get(model.getSeriesCount()-1);
		
		Object[] newRow = {label, color, lineWidth};
		
		((DefaultTableModel)getTblSeriesSettings().getModel()).addRow(newRow);
		
		model.getChartSettings().getSeriesSettings().add(new SeriesSettings(label, color, lineWidth));
		
	}
	
	@Override
	public void tableChanged(TableModelEvent tme) {
		int seriesIndex = tme.getFirstRow();
		try{
			if(tme.getColumn() == 0){
				model.getChartSettings().setSeriesLabel(seriesIndex, (String) tblSeriesSettings.getModel().getValueAt(seriesIndex, 0));
			}else if(tme.getColumn() == 1){
				model.getChartSettings().setSeriesColor(seriesIndex, (Color) tblSeriesSettings.getModel().getValueAt(seriesIndex, 1));
			}else if(tme.getColumn() == 2){
				model.getChartSettings().setSeriesLineWidth(seriesIndex, (Float) tblSeriesSettings.getModel().getValueAt(seriesIndex, 2));
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
			if(!getTfChartTitle().getText().equals(model.getChartSettings().getChartTitle())){
				model.getChartSettings().setChartTitle(getTfChartTitle().getText());
			}
		}else if(e.getSource() == getTfXAxisLabel()){
			if(!getTfXAxisLabel().equals(model.getChartSettings().getxAxisLabel())){
				model.getChartSettings().setxAxisLabel(getTfXAxisLabel().getText());
			}
		}else if(e.getSource() == getTfYAxisLabel()){
			if(!getTfYAxisLabel().equals(model.getChartSettings().getyAxisLabel())){
				model.getChartSettings().setyAxisLabel(getTfYAxisLabel().getText());
			}
		}else if(e.getSource() == getCbRendererType()){
			if(!getCbRendererType().getSelectedItem().equals(model.getChartSettings().getRendererType())){
				model.getChartSettings().setRendererType((String) getCbRendererType().getSelectedItem());
			}
		}
	}
	
	public void replaceModel(DataModel newModel){
		this.model = newModel;
		this.getTblSeriesSettings().setModel(initTableModel());
	}
}
