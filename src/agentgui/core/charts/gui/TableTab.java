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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

/**
 * Generic superclass for a Swing component showing the table representation of chart data and 
 * providing means for editing the data.
 * @author Nils
 *
 */
public abstract class TableTab extends JPanel implements ActionListener, ListSelectionListener{

	private static final long serialVersionUID = -8682335989453677658L;

	/** The path for icon images */
	protected final String PathImage = Application.getGlobalInfo().PathImageIntern();

	// Swing components
	protected JScrollPane scrollPane;
	protected JTable table;
	protected JToolBar toolBar;
	protected JButton btnAddRow;
	protected JButton btnAddColumn;
	protected JButton btnRemoveRow;
	protected JButton btnRemoveColumn;
	protected JSeparator separator;
	
	protected DataModel model;
	
	protected ChartEditorJPanel parentChartEditor;
	
	
	/**
	 * Creates a JTable with the correct renderer and editor classes for the type of chart data
	 * @return The table
	 */
	protected abstract JTable getTable();
	
	/**
	 * Creates a JTable with the correct renderer and editor classes for the type of chart data
	 * @return The table
	 */
	protected abstract JTable getTable(boolean forceRebuild);
	
	public TableTab(ChartEditorJPanel parentChartEditor){
		this.parentChartEditor = parentChartEditor;
	}
	
	
	/**
	 * Initialize.
	 */
	protected void initialize(){
		getTable().setModel(this.model.getTableModel());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(getScrollPane(), gbc_scrollPane);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.anchor = GridBagConstraints.NORTH;
		gbc_toolBar.gridx = 1;
		gbc_toolBar.gridy = 0;
		add(getToolBar(), gbc_toolBar);
	}
	
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setOrientation(SwingConstants.VERTICAL);
			toolBar.add(getBtnAddRow());
			toolBar.add(getBtnAddColumn());
			toolBar.add(getSeparator());
			toolBar.add(getBtnRemoveRow());
			toolBar.add(getBtnRemoveColumn());
		}
		return toolBar;
	}
	private JButton getBtnAddRow() {
		if (btnAddRow == null) {
			btnAddRow = new JButton();
			btnAddRow.setIcon(new ImageIcon(this.getClass().getResource(PathImage + "AddRow.png")));
			btnAddRow.setToolTipText(Language.translate("Zeile hinzufügen"));
			btnAddRow.addActionListener(this);
		}
		return btnAddRow;
	}
	private JButton getBtnAddColumn() {
		if (btnAddColumn == null) {
			btnAddColumn = new JButton();
			btnAddColumn.setIcon(new ImageIcon(this.getClass().getResource( PathImage + "AddCol.png")));
			btnAddColumn.setToolTipText(Language.translate("Spalte hinzufügen"));
			btnAddColumn.addActionListener(this);
		}
		return btnAddColumn;
	}
	private JButton getBtnRemoveRow() {
		if (btnRemoveRow == null) {
			btnRemoveRow = new JButton();
			btnRemoveRow.setIcon(new ImageIcon(this.getClass().getResource( PathImage + "RemoveRow.png")));
			btnRemoveRow.setToolTipText(Language.translate("Zeile entfernen"));
			btnRemoveRow.setEnabled(false);
			btnRemoveRow.addActionListener(this);
		}
		return btnRemoveRow;
	}
	private JButton getBtnRemoveColumn() {
		if (btnRemoveColumn == null) {
			btnRemoveColumn = new JButton();
			btnRemoveColumn.setIcon(new ImageIcon(this.getClass().getResource( PathImage + "RemoveCol.png")));
			btnRemoveColumn.setToolTipText(Language.translate("Spalte entfernen"));
			btnRemoveColumn.setEnabled(false);
			btnRemoveColumn.addActionListener(this);
		}
		return btnRemoveColumn;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == getBtnAddColumn()){
			
			String seriesLabel = JOptionPane.showInputDialog(this, Language.translate("Label"), Language.translate("Neue Datenreihe"), JOptionPane.QUESTION_MESSAGE);
//			String seriesLabel = model.getDefaultSeriesLabel();
			
			DataSeries newSeries = model.createNewDataSeries(seriesLabel);

			// As JFreeChart doesn't work with empty data series, one value pair must be added
			ValuePair initialValuePair;
			if(model.getSeriesCount() > 0){
				// If there is already data in the model, use the first existing key 
				initialValuePair = model.createNewValuePair((Number) model.getTableModel().getValueAt(0, 0), 0);
			}else{
				// If not, use 0 as time stamp
				initialValuePair = model.createNewValuePair(0, 0);
			}
			
			model.getValuePairsFromSeries(newSeries).add(initialValuePair);
			model.addSeries(newSeries);
			
			parentChartEditor.setOntologyClassInstance(parentChartEditor.getOntologyClassInstance());
		} else if(e.getSource() == getBtnAddRow()){
			model.getTableModel().addEmptyRow(getTable(false));

		} else if(e.getSource() == getBtnRemoveColumn()){
			
			if(table.getSelectedColumn() > 0){
				int seriesIndex = table.getSelectedColumn()-1;
				try {
					this.model.removeSeries(seriesIndex);	
					getBtnRemoveRow().setEnabled(false);
					getBtnRemoveColumn().setEnabled(false);

				} catch (NoSuchSeriesException e1) {
					System.err.println("Error removing series " + seriesIndex);
					e1.printStackTrace();
				}
			}
			
		} else if(e.getSource() == getBtnRemoveRow()){
			if(table.isEditing()){
				table.getCellEditor().cancelCellEditing();
			}
			Number key = (Number) table.getValueAt(table.getSelectedRow(), 0);
			model.removeValuePairsFromAllSeries(key);
			if(table.getRowCount() == 0){
				for(int i=0; i<model.getSeriesCount(); i++){
					try {
						model.removeSeries(i);
					} catch (NoSuchSeriesException e1) {
						System.err.println("The data series you tried to remove does not exist");
						e1.printStackTrace();
					}
				}
			}
			getBtnRemoveColumn().setEnabled(false);
			getBtnRemoveRow().setEnabled(false);
			
			parentChartEditor.setOntologyClassInstance(parentChartEditor.getOntologyClassInstance());
			
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// Enable btnRemoveRow if a row is selected
		getBtnRemoveRow().setEnabled(table.getSelectedRow() >= 0);
		
		// Enable btnRemoveColumn if a non-key column is selected
		getBtnRemoveColumn().setEnabled(table.getSelectedColumn() > 0);
	}
	
	/**
	 * Replaces the data model.
	 * @param newModel the new DataModel model
	 */
	public void replaceModel(DataModel newModel){

		this.model = newModel;
//		this.getTable().setModel(this.model.getTableModel());
		if(this.model.getSeriesCount() > 0){
			this.getScrollPane().setViewportView(getTable(true));
		}
		
		
	}

}
