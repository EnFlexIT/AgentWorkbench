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

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import agentgui.core.application.Application;

/**
 * The Class DynTableDataVector provides the data vector 
 * that is used in the TableModel of the DynTable.
 * 
 * @see DynTable
 * @see DynTable#getModel()
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynTableDataVector extends Vector<Object> {

	private static final long serialVersionUID = -6049438110666843131L;

	private DynForm dynForm = null;
	private int rowCounter = 0;
	private Vector<Integer> editableRows = null;
	
	
	/**
	 * Instantiates and initialises a new DynTableDataVector.
	 * @param dynForm the current DynForm
	 */
	public DynTableDataVector(DynForm dynForm) {
		this.dynForm = dynForm;
		this.initialize();
	}

	/** Initialize. */
	private void initialize(){
		
		this.rowCounter = 0;
		this.getEditableRowsVector().removeAllElements();
		
		DefaultTreeModel objectTree = this.dynForm.getObjectTree();
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) objectTree.getRoot();
		
		Vector<Object> rows = this.getChildNodeVector(rootNode, true);
		this.addAll(rows);
		
	}
	
	/**
	 * Gets the child node vector.
	 * @return the child node vector
	 */
	private Vector<Object> getChildNodeVector(DefaultMutableTreeNode parentNode, boolean visibleInTableView) {
		
		Vector<Object> childVector = new Vector<Object>();
		boolean childNodesVisible = true;
		for (int i=0; i<parentNode.getChildCount(); i++) {

			// --------------------------------------------
			// --- Create data row for this node ----------
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			DynType dynType = (DynType) childNode.getUserObject();
			dynType.setVisibleInTableView(visibleInTableView);
			
			if (visibleInTableView==true) {
				// --- Are child slots visible in the table ---
				if (Application.getGlobalInfo().isOntologyClassVisualisation(dynType.getClassName())) {
					childNodesVisible = false;
				} else {
					childNodesVisible = true;
				}
				
			} else {
				// --- nodes and sub node are invisible ---
				childNodesVisible = visibleInTableView;
			}
			
			// --- Create data row ------------------------
			Vector<Object> dataRow = new Vector<Object>();			
			dataRow.add(dynType);
			dataRow.add(dynType);
			// --- Add to mainVector ----------------------
			childVector.add(dataRow);
			
			// --------------------------------------------
			// --- Remind the row number as editable!? ----
			if (dynType.getTypeName().equals(DynType.typeRawType)) {
				this.getEditableRowsVector().add(this.rowCounter);
			}
			this.rowCounter++;
			
			// --------------------------------------------
			// --- Add the Child nodes, if available ------
			if (childNode.getChildCount()!=0) {
				// --- get child nodes first --------------
				childVector.addAll(this.getChildNodeVector(childNode, childNodesVisible));
			}
			
		}
		return childVector;
	}

	/**
	 * Gets the editable rows vector.
	 * @return the editable rows vector
	 */
	public Vector<Integer> getEditableRowsVector() {
		if (this.editableRows==null) {
			this.editableRows = new Vector<Integer>();
		}
		return this.editableRows;
	}
	
	/**
	 * Checks if the structure is equal.
	 *
	 * @param compareTo the compare to object
	 * @return true, if is equal structure
	 */
	public boolean isEqualStructure(Object compareToObject) {

		if (compareToObject==this) return true;
		if (!(compareToObject instanceof DynTableDataVector)) return false;
		
		DynTableDataVector compareTo = (DynTableDataVector) compareToObject; 
		if (this.size()!=compareTo.size()) return false;
		
		for (int i=0; i<this.size(); i++) {
			
			Object objectThisRow = this.get(i);
			Vector<?> rowThis = (Vector<?>) objectThisRow;
			DynType dynTypeThis = (DynType) rowThis.get(0);
			
			Object objectCompRow = compareTo.get(i);
			Vector<?> rowComp = (Vector<?>) objectCompRow;
			DynType dynTypeComp = (DynType) rowComp.get(0);
			
			if (dynTypeThis.equals(dynTypeComp)==false) return false;
			
		}
		return true;
	}
	
	
}
