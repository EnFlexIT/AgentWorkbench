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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import agentgui.core.ontologies.OntologySingleClassSlotDescription;

/**
 * This class provides static access to methods which allows to sort {@link DefaultTableModel}
 * and {@link ArrayList} of the type {@link OntologySingleClassSlotDescription}.
 *
 * @see OntologySingleClassSlotDescription
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Sorter {

	/**
	 * Method for sorting a DefaultTableModel at a specified colum index and in
	 * ascending direction. The column which will be taken should be filled
	 * with String values
	 *
	 * @param currentModel the current model
	 * @param sortByColumn the sort by column
	 */
	public static void sortTableModel(DefaultTableModel currentModel, int sortByColumn) {
		Sorter.sortTableModel(currentModel, sortByColumn, true);
	}
	
	/**
	 * Method for sorting a DefaultTableModel at a specified column index and in
	 * a specified direction. The column which will be taken should be filled
	 * with String values
	 *
	 * @param currentModel the current model
	 * @param sortByColumn the sort by column
	 * @param ascending the ascending
	 */
	public static void sortTableModel(DefaultTableModel currentModel, int sortByColumn, boolean ascending) {
		
		Vector<String> keyVector = new Vector<String>();
		HashMap<String, Object> keyHash = new HashMap<String, Object>();
		
		Vector<?> dataVector = currentModel.getDataVector();
		int maxIndex = dataVector.size();
		for (int i = 0; i < maxIndex; i++) {
			// --------------------------------------------
			Vector<?> rowVector = (Vector<?>) dataVector.get(0);
			String key = (String) rowVector.get(sortByColumn);
			// --------------------------------------------
			keyVector.add(key);
			keyHash.put(key, rowVector);
			// --------------------------------------------
			currentModel.removeRow(0);
		}

		// --- Sort the keyVector -------------------------
		Collections.sort(keyVector);
		if (ascending==false) {
			Collections.reverse(keyVector);
		}

		// --- Build the TableModel newly ----------------- 
		for (int i = 0; i < keyVector.size(); i++) {
			// --------------------------------------------
			String key = keyVector.get(i);
			Vector<?> rowVector = (Vector<?>) keyHash.get(key);
			currentModel.addRow(rowVector);
		}		
		
	}

	/**
	 * Sorts a ArrayList of the Type ArrayList<OntologySingleClassSlotDescription> in a
	 * ascending order. This will be used through the reflective access to an ontology
	 *
	 * @param array2Sort the array2 sort
	 */
	public static void sortSlotDescriptionArray(ArrayList<OntologySingleClassSlotDescription> array2Sort) {
		Sorter.sortSlotDescriptionArray(array2Sort, true);
	}
	
	/**
	 * Sorts a ArrayList of the Type ArrayList<OntologySingleClassSlotDescription> in a
	 * given order. This will be used through the reflective access to an ontology
	 *
	 * @param array2Sort the array2 sort
	 * @param ascending the ascending
	 */
	public static void sortSlotDescriptionArray(ArrayList<OntologySingleClassSlotDescription> array2Sort, boolean ascending) {

		final boolean ascendingSort = ascending;
		
		Comparator<OntologySingleClassSlotDescription> comperator = new Comparator<OntologySingleClassSlotDescription>() {
			@Override
			public int compare(OntologySingleClassSlotDescription oscsd1, OntologySingleClassSlotDescription oscsd2) {
				
				int returnValue = 0;
				
				boolean isRawType1 = false; 
				if (oscsd1.getSlotVarType().startsWith("Instance of")) {
					isRawType1 = false;
				} else {
					isRawType1 = true;
				}
				boolean isRawType2 = false; 
				if (oscsd2.getSlotVarType().startsWith("Instance of")) {
					isRawType2 = false;
				} else {
					isRawType2 = true;
				}
				
				if (isRawType1==isRawType2) {
					// --- both true or both false --------
					String stringComp1 = oscsd1.getSlotName();
					String stringComp2 = oscsd2.getSlotName();
					returnValue = stringComp1.compareToIgnoreCase(stringComp2);
					
				} else {
					// --- one true and one false ---------
					if (isRawType1==true) {
						returnValue = -1;
					} else {
						returnValue=  1;
					}
					
				}
				
				// --- set return value -------------------
				if (ascendingSort==true) {
					return returnValue;
				} else {
					return returnValue * (-1);
				}
			
			}
		};
		Collections.sort(array2Sort, comperator);
	}
	
	
}
