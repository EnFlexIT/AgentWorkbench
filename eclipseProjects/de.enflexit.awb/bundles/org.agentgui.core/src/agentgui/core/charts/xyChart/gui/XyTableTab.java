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
package agentgui.core.charts.xyChart.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Language;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.TableModel;
import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.charts.gui.TableCellEditor4FloatObject;
import agentgui.core.charts.gui.TableTab;
import agentgui.core.charts.xyChart.XyDataModel;
import agentgui.core.charts.xyChart.XyOntologyModel;
import agentgui.core.charts.xyChart.XyTableModel;
import agentgui.core.config.GlobalInfo;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;
import agentgui.ontology.XyDataSeries;

/**
 * TableTab-implementation for XY-charts.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class XyTableTab extends TableTab {

	private static final long serialVersionUID = -5737806366707646814L;
	
	private JButton btnSeriesSettings;
	private JButton btnNextSeries; 
	private JButton btnPrevSeries;
	private JButton btnMoveUp; 
	private JButton btnMoveDown;
	
	private JPopupMenu jPopupMenueSettings;
	private JCheckBox jCheckboxAutoSort; 
	private JCheckBox jCheckboxDuplicateXValues;
	private JButton jButtonApplySettings;
	
	
	/**
	 * Instantiates a new XyTableTab.
	 *
	 * @param model the model
	 * @param parentChartEditor the parent chart editor
	 */
	public XyTableTab(XyDataModel model, ChartEditorJPanel parentChartEditor){
		super(parentChartEditor);
		this.dataModelLocal = model;
		this.initialize();
		this.addFurtherXyButtons();
		this.validate();
	}

	/**
	 * Adds the further buttons for xy-Charts.
	 */
	private void addFurtherXyButtons() {
		
		JToolBar jToolBar = this.getToolBar();
		
		// --- Button for the series-settings -------------
		jToolBar.add(this.getBtnSeriesSettings(), 0);
		jToolBar.add(new JToolBar.Separator(),1);
		
		// --- Buttons for the series-navigation ----------
		jToolBar.add(this.getBtnPrevSeries(), 2);
		jToolBar.add(this.getBtnNextSeries(), 3);
		jToolBar.add(new JToolBar.Separator(),4);
		
		// --- Buttons for row-movement -------------------
		jToolBar.add(this.getBtnMoveUp());
		jToolBar.add(this.getBtnMoveDown());
		jToolBar.addSeparator();
		
	}
	
	private JButton getBtnSeriesSettings() {
		if (btnSeriesSettings == null) {
			btnSeriesSettings = new JButton();
			btnSeriesSettings.setIcon(GlobalInfo.getInternalImageIcon("Properties.png"));
			btnSeriesSettings.setToolTipText(Language.translate("Eigenschaften"));
			btnSeriesSettings.addActionListener(this);
		}
		return btnSeriesSettings;
	}
	private JButton getBtnPrevSeries() {
		if (btnPrevSeries == null) {
			btnPrevSeries = new JButton();
			btnPrevSeries.setIcon(GlobalInfo.getInternalImageIcon("ArrowLeft.png"));
			btnPrevSeries.setPreferredSize(new Dimension(26, 26));
			btnPrevSeries.setToolTipText(Language.translate("Vorherige Datenreihe"));
			btnPrevSeries.addActionListener(this);
		}
		return btnPrevSeries;
	}
	private JButton getBtnNextSeries() {
		if (btnNextSeries == null) {
			btnNextSeries = new JButton();
			btnNextSeries.setIcon(GlobalInfo.getInternalImageIcon("ArrowRight.png"));
			btnNextSeries.setPreferredSize(new Dimension(26, 26));
			btnNextSeries.setToolTipText(Language.translate("Nächste Datenreihe"));
			btnNextSeries.addActionListener(this);
		}
		return btnNextSeries;
	}
	private JButton getBtnMoveUp() {
		if (btnMoveUp == null) {
			btnMoveUp = new JButton();
			btnMoveUp.setIcon(GlobalInfo.getInternalImageIcon("ArrowUp.png"));
			btnMoveUp.setPreferredSize(new Dimension(26, 26));
			btnMoveUp.setToolTipText(Language.translate("Nach oben verschieben"));
			btnMoveUp.addActionListener(this);
		}
		return btnMoveUp;
	}
	private JButton getBtnMoveDown() {
		if (btnMoveDown == null) {
			btnMoveDown = new JButton();
			btnMoveDown.setIcon(GlobalInfo.getInternalImageIcon("ArrowDown.png"));
			btnMoveDown.setPreferredSize(new Dimension(26, 26));
			btnMoveDown.setToolTipText(Language.translate("Nach unten verschieben"));
			btnMoveDown.addActionListener(this);
		}
		return btnMoveDown;
	}
	
	private JPopupMenu getJPopupMenueSettings() {
		if(jPopupMenueSettings==null) {
			jPopupMenueSettings = new JPopupMenu(Language.translate("Eigenschaften"));
			jPopupMenueSettings.add(this.getJCheckboxAutoSort());
			jPopupMenueSettings.add(this.getJCheckboxDuplicateXValues());
			jPopupMenueSettings.addSeparator();
			jPopupMenueSettings.add(this.getJButtonApplySettings());
		}
		return jPopupMenueSettings;
	}
	private JCheckBox getJCheckboxAutoSort() {
		if (jCheckboxAutoSort==null) {
			jCheckboxAutoSort = new JCheckBox(Language.translate("Automatisch sortieren"));
			jCheckboxAutoSort.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckboxAutoSort.setMargin(new Insets(5, 5, 5, 5));
			jCheckboxAutoSort.addActionListener(this);
		}
		return jCheckboxAutoSort;
	}
	private JCheckBox getJCheckboxDuplicateXValues() {
		if(jCheckboxDuplicateXValues==null){
			jCheckboxDuplicateXValues = new JCheckBox(Language.translate("Doppelte X-Werte zulassen"));
			jCheckboxDuplicateXValues.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckboxDuplicateXValues.setMargin(new Insets(5, 5, 5, 10));
			jCheckboxDuplicateXValues.addActionListener(this);
		}
		return jCheckboxDuplicateXValues;
	}
	private JButton getJButtonApplySettings() {
		if(jButtonApplySettings==null) {
			jButtonApplySettings = new JButton(Language.translate("Anwenden"));
			jButtonApplySettings.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApplySettings.setForeground(new Color(0, 153, 0));
			jButtonApplySettings.setMargin(new Insets(0, 20, 0, 20));
			jButtonApplySettings.addActionListener(this);
		}
		return jButtonApplySettings;
	}

	@Override
	protected JTable getTable() {
		
		if(jTable == null){
			jTable = new JTable(dataModelLocal.getTableModel()){
				private static final long serialVersionUID = 3537626788187543327L;
				@Override
				public TableCellEditor getCellEditor(int row, int column) {
					// Same cell editor for all columns
					return new TableCellEditor4FloatObject();
				}
			};
			
			jTable.setShowGrid(false);
			jTable.setRowSelectionAllowed(true);
			jTable.getTableHeader().setReorderingAllowed(false);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.getSelectionModel().addListSelectionListener(this);
			
			if (dataModelLocal!=null && dataModelLocal.getTableModel()!=null && dataModelLocal.getTableModel().getColumnCount()>0) {
				
				TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(dataModelLocal.getTableModel());
				rowSorter.setSortsOnUpdates(true);
				rowSorter.setComparator(0, new Comparator<Integer>() {
					@Override
					public int compare(Integer value1, Integer value2) {
						return value1.compareTo(value2);
					}
				});	
				rowSorter.setComparator(1, new Comparator<Float>() {
					@Override
					public int compare(Float value1, Float value2) {
						return value1.compareTo(value2);
					}
				});
				rowSorter.setComparator(1, new Comparator<Float>() {
					@Override
					public int compare(Float value1, Float value2) {
						return value1.compareTo(value2);
					}
				});
				jTable.setRowSorter(rowSorter);
				
				java.util.List<SortKey> sortKeys = new ArrayList<SortKey>();
				for (int i = 0; i < this.jTable.getColumnCount(); i++) {
				    sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
				}
				jTable.getRowSorter().setSortKeys(sortKeys);
		 		
			}
		}
		return jTable;
		
	}

	/**
	 * Gets the current XyDataSeries.
	 * @return the current XyDataSeries
	 */
	private XyDataSeries getCurrentXySeries() {
		XyDataSeries dataSeries = null;
		try {
			int seriesIndex = ((XyTableModel) this.dataModelLocal.getTableModel()).getFocusedSeriesIndex();
			dataSeries = ((XyOntologyModel) this.dataModelLocal.getOntologyModel()).getSeries(seriesIndex);
		} catch (NoSuchSeriesException nsse) {
			nsse.printStackTrace();
		}
		return dataSeries;
	}
	/**
	 * Sets the JPopupMenueSettings for the settings visible or not.
	 * @param setVisible set true, if you want to show the popup
	 */
	private void setJPopupMenueSettingsVisible(boolean setVisible) {
		if (setVisible==true) {
			// --- Get current settings -------------------
			XyDataSeries xySeries = this.getCurrentXySeries();
			if (xySeries!=null) {
				// --- Set settings to visualisation ------
				this.getJCheckboxAutoSort().setSelected(xySeries.getAutoSort());
				this.getJCheckboxDuplicateXValues().setSelected(!xySeries.getAvoidDuplicateXValues());
				
				// --- Show popup -------------------------
				this.getJPopupMenueSettings().validate();
				int moveX = -(this.getJPopupMenueSettings().getWidth()-this.getBtnSeriesSettings().getWidth());
				int moveY = this.getBtnSeriesSettings().getHeight();
				this.getJPopupMenueSettings().show(this.getBtnSeriesSettings(), moveX, moveY);
				
				// --- Correct position -------------------
				int posX = (int) this.getBtnSeriesSettings().getLocationOnScreen().getX() - this.getJPopupMenueSettings().getWidth() + this.getBtnSeriesSettings().getWidth();
				int posY = (int) this.getJPopupMenueSettings().getLocationOnScreen().getY(); 
				this.getJPopupMenueSettings().setLocation(posX, posY);
			}
			
		} else {
			// --- Hide popup -----------------------------
			this.getJPopupMenueSettings().setVisible(false);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		// --- Stop Cell editing ----------------------------------------------
		if (this.getTable().isEditing()) {
			this.getTable().getCellEditor().stopCellEditing();	
		}
		
		XyTableModel xyTableModel = ((XyTableModel)this.dataModelLocal.getTableModel());
		// --- Case separation ActionEvent ------------------------------------
		if (ae.getSource()==getBtnSeriesSettings()) {
			this.setJPopupMenueSettingsVisible(true);
			
		} else if (ae.getSource()==getJButtonApplySettings()) {

			this.setJPopupMenueSettingsVisible(false);
			// --- Are there changes in the settings? -------------------------
			XyDataSeries dataSeries = this.getCurrentXySeries();
			if (!(this.jCheckboxAutoSort.isSelected()==dataSeries.getAutoSort() && this.getJCheckboxDuplicateXValues().isSelected()!=dataSeries.getAvoidDuplicateXValues())) {
				// --- Settings have changed ----------------------------------
				int userAnswer = JOptionPane.YES_OPTION;
				if (this.getJCheckboxDuplicateXValues().isSelected()==dataSeries.getAvoidDuplicateXValues() && this.getJCheckboxDuplicateXValues().isSelected()==false) {
					// --- Especially duplicate x values are disallowed now ---
					String title = Language.translate("Änderungen anwenden") +"?";
					String msg = "Das Anwenden der neuen Datenreiheneigenschaften ";
					msg += "\nkann dazu führen, dass Daten verloren gehen.";
					msg += "\n\nMöchten Sie fortfahren?"; 
					msg = Language.translate(msg) ;
					userAnswer = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
				} 
				
				if (userAnswer==JOptionPane.YES_OPTION) {
					// --- Apply settings to series ---------------------------
					dataSeries.setAutoSort(this.jCheckboxAutoSort.isSelected());
					dataSeries.setAvoidDuplicateXValues(!this.getJCheckboxDuplicateXValues().isSelected());
					xyTableModel.rebuildXyDataSeries(xyTableModel.getFocusedSeriesIndex());
				}
			}
			
		} else if (ae.getSource()==getBtnPrevSeries()) {
			xyTableModel.focusSeries(xyTableModel.getFocusedSeriesIndex()-1);
			
		} else if (ae.getSource()==getBtnNextSeries()) {
			xyTableModel.focusSeries(xyTableModel.getFocusedSeriesIndex()+1);
			
		} else if(ae.getSource()==getBtnAddColumn() || (ae.getSource()==getBtnAddRow() && getTable().getRowCount()==0)){
			
			String seriesLabel = (String) JOptionPane.showInputDialog(this, Language.translate("Label"), Language.translate("Neue Datenreihe"), JOptionPane.QUESTION_MESSAGE, null, null, dataModelLocal.getDefaultSeriesLabel());
			if(seriesLabel != null){
				// --- As JFreeChart doesn't work with empty data series, one value pair must be added ------ 
				DataSeries newSeries = this.dataModelLocal.createNewDataSeries(seriesLabel);
				ValuePair initialValuePair = this.dataModelLocal.createNewValuePair(0, 0);
				this.dataModelLocal.getValuePairsFromSeries(newSeries).add(initialValuePair);
				this.dataModelLocal.addSeries(newSeries);
			}
			
		} else if(ae.getSource()==getBtnRemoveColumn()){
			
			try {
				int seriesIndex = ((XyTableModel) this.dataModelLocal.getTableModel()).getFocusedSeriesIndex();
				if (seriesIndex>=0) {
					this.dataModelLocal.removeSeries(seriesIndex);	
				}
				
			} catch (NoSuchSeriesException e1) {
				System.err.println("Error removing series:");
				e1.printStackTrace();
			}
			
		} else if(ae.getSource()==getBtnAddRow()){
			this.dataModelLocal.getTableModel().addEmptyRow(this.getTable());
			
		} else if(ae.getSource()==getBtnRemoveRow()){
			this.dataModelLocal.getTableModel().removeRow(this.getTable());
			
		} else if (ae.getSource()==getBtnMoveUp()) {
			xyTableModel.move(this.getTable(), -1);
			
		} else if (ae.getSource()==getBtnMoveDown()) {
			xyTableModel.move(this.getTable(), 1);
			
		}
		this.setButtonsEnabledToSituation();
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		this.setButtonsEnabledToSituation();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.TableTab#setButtonsEnabledToSituation()
	 */
	@Override
	public void setButtonsEnabledToSituation() {
		
		try {
			int countSeries = this.dataModelLocal.getSeriesCount();
			int focusedSeries = ((XyTableModel)this.dataModelLocal.getTableModel()).getFocusedSeriesIndex();
			int countRows = this.dataModelLocal.getTableModel().getRowCount();
			int selectRowTable = this.getTable().getSelectedRow();
			XyDataSeries xySeries = null;
			if (focusedSeries>=0) {
				xySeries = ((XyOntologyModel) this.dataModelLocal.getOntologyModel()).getSeries(focusedSeries);	
			}
			
			// --- Settings -------------------------
			this.getBtnSeriesSettings().setEnabled(countSeries>0);	
			
			// --- Series Selection -----------------
			this.getBtnPrevSeries().setEnabled(countSeries>1 && focusedSeries>0);
			this.getBtnNextSeries().setEnabled(countSeries>1 && focusedSeries+1<countSeries);
			
			// --- Add functions --------------------
			this.getBtnAddRow().setEnabled(true);
			this.getBtnAddColumn().setEnabled(true);
			
			// --- Remove functions -----------------
			this.getBtnRemoveRow().setEnabled(selectRowTable!=-1);
			this.getBtnRemoveColumn().setEnabled(countSeries>0);
			
			this.getBtnMoveUp().setEnabled(xySeries!=null && xySeries.getAutoSort()==false && countRows>1 && selectRowTable>0);
			this.getBtnMoveDown().setEnabled(xySeries!=null && xySeries.getAutoSort()==false && countRows>1 && selectRowTable!=-1 && selectRowTable<countRows-1) ;
			
		} catch (NoSuchSeriesException nsse) {
			nsse.printStackTrace();
		} 
		
	}
	
}
