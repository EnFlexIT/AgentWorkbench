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
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;

import agentgui.core.application.Language;
import agentgui.core.charts.DataModel;
import agentgui.core.config.GlobalInfo;

/**
 * Generic superclass for a Swing component showing the table representation of chart data and 
 * providing means for editing the data.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class TableTab extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = -8682335989453677658L;

	private JScrollPane scrollPane;
	private JToolBar toolBar;
	private JButton btnAddRow;
	private JButton btnAddColumn;
	private JButton btnRemoveRow;
	private JButton btnRemoveColumn;
	
	protected JTable jTable;
	
	protected ChartEditorJPanel parentChartEditor;
	protected DataModel dataModelLocal;
	
	
	/**
	 * Instantiates a new table tab.
	 * @param parentChartEditor the parent chart editor
	 */
	public TableTab(ChartEditorJPanel parentChartEditor){
		this.parentChartEditor = parentChartEditor;
	}
	
	/** Initializes this. */
	protected void initialize(){
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 0);
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
	
	/**
	 * Creates a JTable with the correct renderer and editor classes for the type of chart data
	 * @return The table
	 */
	protected abstract JTable getTable();
	/**
	 * Sets the buttons enabled to the current situation.
	 */
	public abstract void setButtonsEnabledToSituation();

	
	protected JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(this.getTable());
		}
		return scrollPane;
	}
	protected JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.setOrientation(SwingConstants.VERTICAL);
			toolBar.add(getBtnAddRow());
			toolBar.add(getBtnAddColumn());
			toolBar.addSeparator();
			toolBar.add(getBtnRemoveRow());
			toolBar.add(getBtnRemoveColumn());
			toolBar.addSeparator();
		}
		return toolBar;
	}
	protected JButton getBtnAddRow() {
		if (btnAddRow == null) {
			btnAddRow = new JButton();
			btnAddRow.setIcon(GlobalInfo.getInternalImageIcon("AddRow.png"));
			btnAddRow.setToolTipText(Language.translate("Zeile hinzufügen"));
			btnAddRow.addActionListener(this);
		}
		return btnAddRow;
	}
	protected JButton getBtnAddColumn() {
		if (btnAddColumn == null) {
			btnAddColumn = new JButton();
			btnAddColumn.setIcon(GlobalInfo.getInternalImageIcon("AddCol.png"));
			btnAddColumn.setToolTipText(Language.translate("Spalte hinzufügen"));
			btnAddColumn.addActionListener(this);
		}
		return btnAddColumn;
	}
	protected JButton getBtnRemoveRow() {
		if (btnRemoveRow == null) {
			btnRemoveRow = new JButton();
			btnRemoveRow.setIcon(GlobalInfo.getInternalImageIcon("RemoveRow.png"));
			btnRemoveRow.setToolTipText(Language.translate("Zeile entfernen"));
			btnRemoveRow.setEnabled(false);
			btnRemoveRow.addActionListener(this);
		}
		return btnRemoveRow;
	}
	protected JButton getBtnRemoveColumn() {
		if (btnRemoveColumn == null) {
			btnRemoveColumn = new JButton();
			btnRemoveColumn.setIcon(GlobalInfo.getInternalImageIcon("RemoveCol.png"));
			btnRemoveColumn.setToolTipText(Language.translate("Spalte entfernen"));
			btnRemoveColumn.setEnabled(false);
			btnRemoveColumn.addActionListener(this);
		}
		return btnRemoveColumn;
	}

	/**
	 * Can be used to notify underlying elements to stop edit actions.
	 */
	public void stopEditing() {
		if (this.getTable().isEditing()==true) {
			this.getTable().getCellEditor().stopCellEditing();
		}
	}
	
}
