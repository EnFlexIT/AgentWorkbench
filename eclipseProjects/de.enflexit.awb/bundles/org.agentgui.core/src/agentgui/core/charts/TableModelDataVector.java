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
package agentgui.core.charts;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;


/**
 * The Class TableModelDataVector is used within the TableModel that can
 * be edited by the user.
 * 
 * @see TableModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TableModelDataVector extends Vector<Vector<Number>> {

	private static final long serialVersionUID = -5351438344725678570L;
	
	private String typeDescription = this.getClass().getSimpleName();
	private boolean activateRowNumber = false;
	private int noOfPrefixColumns = 1;
	private int keyColumnIndex = 0;
	
	private TreeMap<Number, Vector<Number>> keyRowVectorTreeMap = null;
	private HashMap<Number, Integer> keyIndexHashMap = null;
	
	
	/**
	 * Instantiates a new table model data vector.
	 *
	 * @param typeDescription the type description
	 * @param activateRowNumber the activate row number
	 * @param keyColumnIndex the key column index
	 */
	public TableModelDataVector(String typeDescription, boolean activateRowNumber, int keyColumnIndex) {
		if (typeDescription!=null) {
			this.typeDescription = typeDescription;
		}
		this.activateRowNumber = activateRowNumber;
		if (this.activateRowNumber==true) {
			this.noOfPrefixColumns = 2;
		} else {
			this.noOfPrefixColumns = 1;
		}
		this.keyColumnIndex = keyColumnIndex;
	}
	
	/**
	 * Checks if is activate row number appearance.
	 * @return true, if is activate row number
	 */
	public boolean isActivateRowNumber() {
		return activateRowNumber;
	}
	/**
	 * Gets the no of prefix columns.
	 * @return the no of prefix columns
	 */
	public int getNoOfPrefixColumns() {
		return noOfPrefixColumns;
	}
	/**
	 * Gets the key column index.
	 * @return the key column index
	 */
	public int getKeyColumnIndex() {
		return keyColumnIndex;
	}
	
	
	/**
	 * Returns the TreeMap of the key-to-row relationship.
	 * @return the key tree map
	 */
	public TreeMap<Number, Vector<Number>> getKeyRowVectorTreeMap() {
		if (keyRowVectorTreeMap==null) {
			keyRowVectorTreeMap = new TreeMap<Number, Vector<Number>>();
		}
		return keyRowVectorTreeMap;
	}
	/**
	 * Returns the HashMap of the key-to-index position .
	 * @return the key index hash map
	 */
	public HashMap<Number, Integer> getKeyIndexHashMap() {
		if (keyIndexHashMap==null) {
			keyIndexHashMap = new HashMap<Number, Integer>();
		}
		return keyIndexHashMap;
	}

	/**
	 * Resets reminder maps.
	 */
	public void resetRowNumberAndReminderMaps() {
		keyRowVectorTreeMap.clear();
		keyIndexHashMap.clear();
		for (int i = 0; i < this.size(); i++) {
			Vector<Number> rowVector = this.get(i);
			if (this.isActivateRowNumber()==true) {
				rowVector.set(0, i+1);
			}
			Number key = rowVector.get(this.getKeyColumnIndex());
			this.keyRowVectorTreeMap.put(key, rowVector);
			this.keyIndexHashMap.put(key, i);
		}
	}
	
	// ------------------------------------------------------------------------
	// ---- In the following the overwritten add and remove methods -----------
	// ------------------------------------------------------------------------	
	/* (non-Javadoc)
	 * @see java.util.Vector#add(java.lang.Object)
	 */
	@Override
	public synchronized boolean add(Vector<Number> rowVector) {
		Number key = rowVector.get(this.getKeyColumnIndex());
		if (this.getKeyRowVectorTreeMap().get(key)==null) {
			this.getKeyRowVectorTreeMap().put(key, rowVector);
			this.getKeyIndexHashMap().put(key, this.size());
			return super.add(rowVector);
		} else {
			System.err.println(this.typeDescription + ": Duplicate key value " + key + " - row data was not added!");	
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#addElement(java.lang.Object)
	 */
	@Override
	public synchronized void addElement(Vector<Number> rowVector) {
		Number key = rowVector.get(this.getKeyColumnIndex());
		if (this.getKeyRowVectorTreeMap().get(key)==null) {
			this.getKeyRowVectorTreeMap().put(key, rowVector);
			this.getKeyIndexHashMap().put(key, this.size());
			super.addElement(rowVector);
		} else {
			System.err.println(this.typeDescription + ": Duplicate key value " + key + " - row data was not added!");
		}
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, Vector<Number> rowVector) {
		Number key = rowVector.get(this.getKeyColumnIndex());
		if (this.getKeyColumnIndex()==0) {
			// --- Duplicate x values are allowed ---------
			super.add(index, rowVector);
			this.resetRowNumberAndReminderMaps();
			
		} else {
			// --- Duplicate x values are NOT allowed -----
			if (this.getKeyRowVectorTreeMap().get(key)==null) {
				super.add(index, rowVector);
				this.resetRowNumberAndReminderMaps();			
			}  else {
				System.err.println(this.typeDescription + ": Duplicate key value " + key + " - row data was not added!");
			}	
		}
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#addAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean addAll(Collection<? extends Vector<Number>> collection) {
		return this.addAll(this.size(), collection);
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#addAll(int, java.util.Collection)
	 */
	@Override
	public synchronized boolean addAll(int index, Collection<? extends Vector<Number>> collection) {

		boolean fullSuccess = true;
		int newIndexPos = index;
		for (Vector<Number> rowVector : collection) {
			Number key = rowVector.get(this.getKeyColumnIndex());
			if (this.getKeyRowVectorTreeMap().get(key)==null) {
				this.getKeyRowVectorTreeMap().put(key, rowVector);
				super.add(newIndexPos, rowVector);
				newIndexPos++;
				
			}  else {
				System.err.println(this.typeDescription + ": Duplicate key value " + key + " - row data was not added!");
				fullSuccess = false;
			}
		}
		
		this.resetRowNumberAndReminderMaps();
		return fullSuccess;
	}
	
	
	// --- Remove -------------------------------
	@Override
	public synchronized Vector<Number> remove(int index) {
		Vector<Number> rowRemoved = super.remove(index);
		this.resetRowNumberAndReminderMaps();
		return rowRemoved;
	}
	@Override
	public synchronized void removeElementAt(int index) {
		super.removeElementAt(index);
		this.resetRowNumberAndReminderMaps();
	}
	
	@Override
	public boolean remove(Object o) {
		// Nothing to do here: will call #removeElement
		return super.remove(o);
	}
	@Override
	public synchronized boolean removeElement(Object obj) {
		// Nothing to do here: will call #removeElementAt later on
		return super.removeElement(obj);
	}
	
	@Override
	public synchronized void removeAllElements() {
		super.removeAllElements();
		this.resetRowNumberAndReminderMaps();
	}
	@Override
	public synchronized boolean removeAll(Collection<?> collection) {
		boolean done = super.removeAll(collection);
		this.resetRowNumberAndReminderMaps();
		return done;
	}

	/**
	 * Sorts the current data model.
	 */
	public void sort() {
		Collections.sort(this, new Comparator<Vector<Number>>() {
			@Override
			public int compare(Vector<Number> v1, Vector<Number> v2) {
				if (isActivateRowNumber()==false) {
					// --- TimeStamp compare ----
					Long time1 = v1.get(0).longValue();
					Long time2 = v2.get(0).longValue();
					return time1.compareTo(time2);
				} 
				// --- XY-compare -----------
				Float x1 = v1.get(1).floatValue();
				Float x2 = v2.get(1).floatValue();
				if (x1.equals(x2)==true) {
					Float y1 = v1.get(2).floatValue();
					Float y2 = v2.get(2).floatValue();
					return y1.compareTo(y2);
				}
				return x1.compareTo(x2);
			}
		});
		this.resetRowNumberAndReminderMaps();
	}

	/**
	 * Returns the row specified by the column to search for and a given value.
	 *
	 * @param columnIndex the column index
	 * @param value the value
	 * @param skipRowIndex the row index that can be skip during search
	 * @return the row by value
	 */
	public Vector<Number> getRowByValue(int columnIndex, Number value, Integer skipRowIndex) {
		Vector<Number> rowFound = null;
		for (int i=0; i<this.size(); i++) {
			if (!(skipRowIndex!=null && i==skipRowIndex)) {
				Number compValue = this.get(i).get(columnIndex);
				if (compValue.equals(value)) {
					rowFound = this.get(i);
					break;
				}
			}
		}
		return rowFound;
	}
	
	
}
