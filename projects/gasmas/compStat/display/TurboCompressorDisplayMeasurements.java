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
package gasmas.compStat.display;

import gasmas.compStat.CompressorStationModel;
import gasmas.ontology.CompStatAdiabaticEfficiency;
import gasmas.ontology.CompStatTcMeasurement;
import gasmas.ontology.TurboCompressor;
import gasmas.ontology.ValueType;
import jade.util.leap.List;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import agentgui.envModel.graph.GraphGlobals;

/**
 * The Class TurboCompressorDisplayMeasurments.
 */
public class TurboCompressorDisplayMeasurements extends ParameterDisplay implements ActionListener {

	private static final long serialVersionUID = 2985435239542481036L;

	private final String myParameterDescription = "Measurements";
	
	private final String pathImage = GraphGlobals.getPathImages();
	private final ImageIcon iconPlus  =	new ImageIcon(getClass().getResource(pathImage + "ListPlus.png"));  //  @jve:decl-index=0:
	private final ImageIcon iconMinus =	new ImageIcon(getClass().getResource(pathImage + "ListMinus.png"));

	private CompressorStationModel compressorStationModel = null;
	private String turboCompressorID = null;
	private TurboCompressor myTurboCompressor = null;  			//  @jve:decl-index=0:

	private String columnHeader1 = "Efficiency / Settleline";	//  @jve:decl-index=0:
	private String columnHeader2 = "Speed [1/min]";				//  @jve:decl-index=0:
	private String columnHeader3 = "Vol. Flow Rate [m³/s]";  	//  @jve:decl-index=0:
	private String columnHeader4 = "Ad. Head [kJ/kg]";			//  @jve:decl-index=0:

	private JScrollPane jScrollPaneMeasurements = null;
	private DefaultTableModel tableModel = null;
	private JTable jTableMeasurements = null;
	
	private JButton jButtonAddMeasurement = null;
	private JButton jButtonRemoveMeasurement = null;

	
	
	/**
	 * Instantiates a new turbo compressor display measurments.
	 *
	 * @param compressorStationModel the compressor station model
	 * @param turboCompressorID the turbo compressor id
	 */
	public TurboCompressorDisplayMeasurements(CompressorStationModel compressorStationModel, String turboCompressorID) {
		this.compressorStationModel = compressorStationModel;	
		this.turboCompressorID = turboCompressorID;
		this.myTurboCompressor = (TurboCompressor) this.compressorStationModel.getComponent(this.turboCompressorID);
		this.initialize();
		this.setMeasurements2Form();
	}

	private void initialize() {
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.anchor = GridBagConstraints.WEST;
        gridBagConstraints5.insets = new Insets(5, 10, 0, 0);
        gridBagConstraints5.gridy = 0;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.insets = new Insets(5, 5, 0, 0);
        gridBagConstraints4.gridy = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(439, 250));
        this.add(getJScrollPaneMeasurements(), gridBagConstraints);
        this.add(getJButtonAddMeasurement(), gridBagConstraints4);
        this.add(getJButtonRemoveMeasurement(), gridBagConstraints5);
		
	}

	/**
	 * Sets the measurements the current turbo compressor.
	 */
	public void setMeasurements2TurboCompressor() {
		
		// --- Reset current measurements -----------------
		List charDiaMeasurements = this.myTurboCompressor.getCharacteristicDiagramMeasurements();
		this.emptyList(charDiaMeasurements);
		List settlelineMeasurements = this.myTurboCompressor.getSettlelineMeasurements();
		this.emptyList(settlelineMeasurements);
		
		// --- Run  through the table model ---------------
		DefaultTableModel tableModel = this.getTableModel4Measurements();
		for (int row=0; row<tableModel.getRowCount(); row++) {
			
			String rowheader = (String) tableModel.getValueAt(row, 0);
			ValueType valueTypeSpeed = (ValueType) tableModel.getValueAt(row, 1);
			ValueType valueTypeFlowRate = (ValueType) tableModel.getValueAt(row, 2);
			ValueType valueTypeAdiabaticHeat = (ValueType) tableModel.getValueAt(row, 3);
			
			CompStatTcMeasurement cstm = new CompStatTcMeasurement();
			cstm.setSpeed(valueTypeSpeed);
			cstm.setVolumetricFlowrate(valueTypeFlowRate);
			cstm.setAdiabaticHead(valueTypeAdiabaticHeat);
			
			if (rowheader.equals(TurboCompressorMeasurmentEdit.rowHeaderSettleline)) {
				// --- Add to the settle line measurements ----------
				settlelineMeasurements.add(cstm);
				
			} else {
				// --- Add to characteristic diagram Measurements ---
				CompStatAdiabaticEfficiency adiabaticEfficiency = this.getAdiabaticEfficiency(charDiaMeasurements, rowheader);
				adiabaticEfficiency.getMeasurements().add(cstm);
			}
			
		}
		
		// --- Inform listener about changes --------------
		this.myTurboCompressor.setCharacteristicDiagramMeasurements(charDiaMeasurements);
		this.myTurboCompressor.setSettlelineMeasurements(settlelineMeasurements);
		this.informListener(myParameterDescription, this.myTurboCompressor);
	}
	
	
	/**
	 * Gets the adiabatic efficiency.
	 *
	 * @param charDiaMeasurements the char dia measurements
	 * @param efficiencyID the efficiency id
	 * @return the adiabatic efficiency
	 */
	private CompStatAdiabaticEfficiency getAdiabaticEfficiency(List charDiaMeasurements, String efficiencyID) {
		
		CompStatAdiabaticEfficiency adiabaticEfficiency = null;
		for (int i=0; i<charDiaMeasurements.size(); i++) {
			CompStatAdiabaticEfficiency currentAdiabaticEfficiency = (CompStatAdiabaticEfficiency) charDiaMeasurements.get(i);
			if (currentAdiabaticEfficiency.getAdiabaticEfficiency().equals(efficiencyID)) {
				adiabaticEfficiency = currentAdiabaticEfficiency;
				break;
			}
		}
		
		if (adiabaticEfficiency==null) {
			adiabaticEfficiency = new CompStatAdiabaticEfficiency();
			adiabaticEfficiency.setAdiabaticEfficiency(efficiencyID);
			// --- Add to list --------------------------------------
			charDiaMeasurements.add(adiabaticEfficiency);
		}
		return adiabaticEfficiency;
	}

	/**
	 * Empties a specified list.
	 * @param list2Empty the list2 empty
	 */
	private void emptyList(List list2Empty) {
		while (list2Empty.size()>0) {
			list2Empty.remove(0);
		}
	}
	
	/**
	 * Sets the measurements.
	 */
	private void setMeasurements2Form() {
		
		List charDiaMeasurements = this.myTurboCompressor.getCharacteristicDiagramMeasurements();
		for (int i = 0; i < charDiaMeasurements.size(); i++) {
			CompStatAdiabaticEfficiency adiabaticEff = (CompStatAdiabaticEfficiency) charDiaMeasurements.get(i);
			
			String efficiency = adiabaticEff.getAdiabaticEfficiency();
			if (efficiency!=null && efficiency.equals("")==false) {
				List measurements = adiabaticEff.getMeasurements();
				for (int j = 0; j < measurements.size(); j++) {
					CompStatTcMeasurement measurement = (CompStatTcMeasurement) measurements.get(j);
					
					Vector<Object> rowData = new Vector<Object>();
					rowData.add(efficiency);
					rowData.add(measurement.getSpeed());
					rowData.add(measurement.getVolumetricFlowrate());
					rowData.add(measurement.getAdiabaticHead());
					this.getTableModel4Measurements().addRow(rowData);
				}				
			}
		}
		
		List settlelineMeasurements = this.myTurboCompressor.getSettlelineMeasurements();
		for (int i = 0; i < settlelineMeasurements.size(); i++) {
			CompStatTcMeasurement measurement = (CompStatTcMeasurement) settlelineMeasurements.get(i);

			Vector<Object> rowData = new Vector<Object>();
			rowData.add("Settleline");
			if (!(measurement.getSpeed().getValue()==0 && measurement.getVolumetricFlowrate().getValue()==0 && measurement.getAdiabaticHead().getValue()==0)) {
				rowData.add(measurement.getSpeed());
				rowData.add(measurement.getVolumetricFlowrate());
				rowData.add(measurement.getAdiabaticHead());
				this.getTableModel4Measurements().addRow(rowData);	
			}
		}
		
	}
	
	/**
	 * This method initializes jScrollPaneMeasurements	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneMeasurements() {
		if (jScrollPaneMeasurements == null) {
			jScrollPaneMeasurements = new JScrollPane();
			jScrollPaneMeasurements.setViewportView(getJTableMeasurements());
		}
		return jScrollPaneMeasurements;
	}
	/**
	 * This method initializes jTableMeasurments	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTableMeasurements() {
		if (jTableMeasurements == null) {
			jTableMeasurements = new JTable();
			jTableMeasurements.setFillsViewportHeight(true);
			jTableMeasurements.setShowGrid(false);
			jTableMeasurements.setRowHeight(20);
			jTableMeasurements.setModel(this.getTableModel4Measurements());
			jTableMeasurements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableMeasurements.getTableHeader().setReorderingAllowed(false);
			
			// --- Set the alignment in the header ----------------------------
			final DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) jTableMeasurements.getTableHeader().getDefaultRenderer();
			jTableMeasurements.getTableHeader().setDefaultRenderer(new TableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component comp = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					if (column==0) {
						renderer.setHorizontalAlignment(JLabel.LEFT);
					} else if (column>0) {
						renderer.setHorizontalAlignment(JLabel.RIGHT);
					}
					return comp;
				}
			});
			
			// --- Set the Renderer and the Editor for the table cells --------
			TableColumnModel contentColumnModel = jTableMeasurements.getColumnModel();
			contentColumnModel.getColumn(0).setCellEditor(new TurboCompressorDisplayMeasurementRendererEditor(this));
			contentColumnModel.getColumn(0).setCellRenderer(new TurboCompressorDisplayMeasurementRendererEditor(this));
			contentColumnModel.getColumn(1).setCellEditor(new TurboCompressorDisplayMeasurementRendererEditor(this));
			contentColumnModel.getColumn(1).setCellRenderer(new TurboCompressorDisplayMeasurementRendererEditor(this));
			contentColumnModel.getColumn(2).setCellEditor(new TurboCompressorDisplayMeasurementRendererEditor(this));
			contentColumnModel.getColumn(2).setCellRenderer(new TurboCompressorDisplayMeasurementRendererEditor(this));
			contentColumnModel.getColumn(3).setCellEditor(new TurboCompressorDisplayMeasurementRendererEditor(this));
			contentColumnModel.getColumn(3).setCellRenderer(new TurboCompressorDisplayMeasurementRendererEditor(this));
			
		}
		return jTableMeasurements;
	}

	/**
	 * Gets the table model4 measurements.
	 * @return the table model4 measurements
	 */
	private DefaultTableModel getTableModel4Measurements() {
		if (tableModel==null){
			tableModel = new DefaultTableModel(this.getVectorTableData(), this.getVectorOfColumnNames());
		}
		return tableModel; 
	}
	
	/**
	 * Gets the vector table data.
	 * @return the vector table data
	 */
	private Vector<Object> getVectorTableData() {
		Vector<Object> data = new Vector<Object>();
		return data;
	}
	
	/**
	 * Gets the column names.
	 * @return the column names as Vector
	 */
	private Vector<String> getVectorOfColumnNames() {
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(columnHeader1);
		columnNames.add(columnHeader2);
		columnNames.add(columnHeader3);
		columnNames.add(columnHeader4);
		return columnNames;
	}

	/**
	 * This method initializes jButtonAddMeasurement	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAddMeasurement() {
		if (jButtonAddMeasurement == null) {
			jButtonAddMeasurement = new JButton();
			jButtonAddMeasurement.setSize(new Dimension(26, 26));
			jButtonAddMeasurement.setPreferredSize(new Dimension(26, 26));
			jButtonAddMeasurement.setIcon(iconPlus);
			jButtonAddMeasurement.addActionListener(this);
		}
		return jButtonAddMeasurement;
	}
	/**
	 * This method initializes jButtonRemoveMeasurement	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemoveMeasurement() {
		if (jButtonRemoveMeasurement == null) {
			jButtonRemoveMeasurement = new JButton();
			jButtonRemoveMeasurement.setSize(new Dimension(26, 26));
			jButtonRemoveMeasurement.setPreferredSize(new Dimension(26, 26));
			jButtonRemoveMeasurement.setIcon(iconMinus);
			jButtonRemoveMeasurement.addActionListener(this);
		}
		return jButtonRemoveMeasurement;
	}

	/**
	 * Adds a measurement row.
	 */
	private void addMeasurementRow() {
		
		// --- Create row vector --------------------------
		Vector<Object> newRow = new Vector<Object>();
		newRow.add(new String());
		newRow.add(new ValueType());
		newRow.add(new ValueType());
		newRow.add(new ValueType());
		// --- Set default values for unit ----------------
		((ValueType) newRow.get(1)).setUnit("PER_MIN");
		((ValueType) newRow.get(2)).setUnit("m_cube_per_s");
		((ValueType) newRow.get(3)).setUnit("kJ_per_kg");
		
		TurboCompressorMeasurmentEdit tcme = new TurboCompressorMeasurmentEdit(newRow);
		tcme.setVisible(true);
		// --- Wait until the end of the user interaction -
		if (tcme.isCanceled()==true) {
			tcme.dispose();
			tcme = null;
			return;
		}
		newRow = tcme.getMeasurement();
		tcme.dispose();
		tcme = null;
		
		this.getTableModel4Measurements().addRow(newRow);
		int newIndex = this.getTableModel4Measurements().getRowCount() - 1;
		newIndex = this.getJTableMeasurements().convertRowIndexToView(newIndex);
		
		int editCell = 0;
		this.getJTableMeasurements().changeSelection(newIndex, editCell, false, false);
		this.setMeasurements2TurboCompressor();
		
	}
	
	/**
	 * Removes a measurement row.
	 * @param rowNumTable the row num table
	 */
	private void removeMeasurementRow(int rowNumTable){
		int rowNumModel = this.getJTableMeasurements().convertRowIndexToModel(rowNumTable);
		((DefaultTableModel)getJTableMeasurements().getModel()).removeRow(rowNumModel);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object actor = ae.getSource();
		if (actor == this.getJButtonAddMeasurement()) {
			// --- Add Measurement ------------------------
			this.addMeasurementRow();
			
		} else if (actor == this.getJButtonRemoveMeasurement()) {
			// --- Remove Measurement ---------------------
			if(getJTableMeasurements().getSelectedRow() > -1){
				this.removeMeasurementRow(getJTableMeasurements().getSelectedRow());
			}

		}
		
	}

	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
