package agentgui.core.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import agentgui.core.ontologies.OntologySingleClassSlotDescription;

/**
 * 
 * @author Christian Derksen
 */
public class Sorter {

	/**
	 * Method for sorting a DefaultTableModel at a specified colum index and in
	 * ascending direction. The column which will be taken should be filled
	 * with String values 
	 * @param currentModel
	 * @param sortByColum
	 * @return
	 */
	public static void sortTableModel(DefaultTableModel currentModel, int sortByColum) {
		Sorter.sortTableModel(currentModel, sortByColum, true);
	}
	
	/**
	 * Method for sorting a DefaultTableModel at a specified colum index and in
	 * a specified direction. The column which will be taken should be filled
	 * with String values
	 * 
	 * @param currentModel
	 * @param sortByColum
	 * @param ascending
	 * @return
	 */
	public static void sortTableModel(DefaultTableModel currentModel, int sortByColum, boolean ascending) {
		
		Vector<String> keyVector = new Vector<String>();
		HashMap<String, Object> keyHash = new HashMap<String, Object>();
		
		Vector<?> dataVector = currentModel.getDataVector();
		int maxIndex = dataVector.size();
		for (int i = 0; i < maxIndex; i++) {
			// --------------------------------------------
			Vector<?> rowVector = (Vector<?>) dataVector.get(0);
			String key = (String) rowVector.get(sortByColum);
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
	 * @param array2Sort
	 * @return
	 */
	public static void sortSlotDescriptionArray(ArrayList<OntologySingleClassSlotDescription> array2Sort) {
		Sorter.sortSlotDescriptionArray(array2Sort, true);
	}
	
	/**
	 * Sorts a ArrayList of the Type ArrayList<OntologySingleClassSlotDescription> in a 
	 * given order. This will be used through the reflective access to an ontology
	 * @param array2Sort
	 * @param ascending
	 * @return
	 */
	public static void sortSlotDescriptionArray(ArrayList<OntologySingleClassSlotDescription> array2Sort, boolean ascending) {

		Vector<String> keyVector = new Vector<String>();
		HashMap<String, OntologySingleClassSlotDescription> keyHash = new HashMap<String, OntologySingleClassSlotDescription>();

		int maxIndex = array2Sort.size();
		for (int i = 0; i < maxIndex; i++) {
			// --------------------------------------------
			OntologySingleClassSlotDescription oscsd = array2Sort.get(0);
			String key = oscsd.getSlotName();
			// --------------------------------------------
			keyVector.add(key);
			keyHash.put(key, oscsd);
			// --------------------------------------------
			array2Sort.remove(0);
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
			OntologySingleClassSlotDescription oscsd = keyHash.get(key);
			array2Sort.add(oscsd);
		}		

	}
	
	
}
