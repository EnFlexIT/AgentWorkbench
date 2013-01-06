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
package agentgui.core.ontologies.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;

/**
 * The Class DynTableJPanel.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynTableJPanel extends JPanel {

	private static final long serialVersionUID = 6175657447251980498L;

	private DynForm dynForm = null;
	private JScrollPane jScrollPaneDynTable = null;
	private DynTable dynTable = null;

	
	/**
	 * This is the default constructor
	 */
	public DynTableJPanel(DynForm dynForm) {
		super();
		this.dynForm = dynForm;
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(this.getJScrollPaneDynTable(), BorderLayout.CENTER);
	}

	/**
	 * This method initialises jScrollPaneDynTable.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneDynTable() {
		if (jScrollPaneDynTable == null) {
			jScrollPaneDynTable = new JScrollPane();
			jScrollPaneDynTable.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jScrollPaneDynTable.setViewportView(this.getDynTable());
		}
		return jScrollPaneDynTable;
	}
	/**
	 * Returns the current DynTable instance.
	 * @return the dyn table
	 */
	private DynTable getDynTable() {
		if (dynTable==null) {
			dynTable = new DynTable(this.dynForm);
		}
		return dynTable;
	}
	/**
	 * Stops the DynTable cell editing.
	 */
	public void stopDynTableCellEditing() {
		DynTable dynTable = this.getDynTable();
		TableCellEditor cellEditor = dynTable.getCellEditor();
		if (cellEditor!=null) {
			cellEditor.stopCellEditing();
		}
	}
	/**
	 * Refreshes the TableModel.
	 */
	public void refreshTableModel() {
		this.getDynTable().refreshTableModel();
	}

}
