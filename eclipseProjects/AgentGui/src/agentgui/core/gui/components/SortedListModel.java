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
package agentgui.core.gui.components;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;

/**
 * The Class SortedListModel extends an {@link AbstractListModel} 
 * and can be used as sorted model in a JList.
 * 
 * @param <E> the element type has to implement the {@link Comparable} interface
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SortedListModel<E> extends AbstractListModel<E> {

	private static final long serialVersionUID = 6786824098255218310L;

	private SortedSet<E> model;
	
	/**
	 * Returns the internal model.
	 * @return the model
	 */
	private SortedSet<E> getModel() {
		if (model==null) {
			model = new TreeSet<E>();	
		}
		return model;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		return this.getModel().size();
	}
	
	/**
	 * Returns the element at the specified index position.
	 *
	 * @param index the index
	 * @return the e
	 */
	public E get(int index) {
		return this.getElementAt(index);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public E getElementAt(int index) {
		@SuppressWarnings("unchecked")
		E element = (E) this.getModel().toArray()[index];
		return element;
	}
	/**
	 * Adds the specified element.
	 * @param element the element
	 */
	public void add(E element) {
		if (this.getModel().add(element)) {
			fireContentsChanged(this, 0, getSize());
		}
	}
	/**
	 * Adds the specified element.
	 * @param element the element
	 */
	public void addElement(E element) {
		this.add(element);
	}
	/**
	 * Adds all elements specified in the array.
	 * @param elements the elements
	 */
	public void addAll(E elements[]) {
		Collection<E> c = Arrays.asList(elements);
		this.getModel().addAll(c);
		fireContentsChanged(this, 0, getSize());
	}
	
	/**
	 * Removes the specified element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public boolean removeElement(Object element) {
		boolean removed = this.getModel().remove(element);
		if (removed) {
			fireContentsChanged(this, 0, getSize());
		}
		return removed;
	}
	
	/**
	 * Clear.
	 */
	public void clear() {
		this.getModel().clear();
		fireContentsChanged(this, 0, getSize());
	}
	/**
	 * Checks if the list contains the specified element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public boolean contains(Object element) {
		return this.getModel().contains(element);
	}
	/**
	 * Return the first element in the list.
	 * @return the object
	 */
	public Object firstElement() {
		return this.getModel().first();
	}
	/**
	 * Return the last element in the list.
	 * @return the object
	 */
	public Object lastElement() {
		return this.getModel().last();
	}
	
	/**
	 * Returns an Iterator of the list elements.
	 * @return the iterator
	 */
	public Iterator<E> iterator() {
		return this.getModel().iterator();
	}
	
}
