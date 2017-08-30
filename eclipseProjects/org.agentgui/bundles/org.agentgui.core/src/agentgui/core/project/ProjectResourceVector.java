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
package agentgui.core.project;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This extended Vector<String> is used as a data model for project resources.
 * As an extension its provides a DefaultListModel, which is synchronised with
 * the Vectors content and for the use in the tab 'Configuration' - 'Resources'.
 * 
 * @see Project
 * @see DefaultListModel
 * @see ProjectResource2Display
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectResourceVector extends Vector<String> {

	private static final long serialVersionUID = -6728505277548001391L;
	
	@XmlTransient 
	private DefaultListModel<ProjectResource2Display> resourcesListModel = new DefaultListModel<ProjectResource2Display>();
	
	/**
	 * Instantiates a new project resources.
	 */
	public ProjectResourceVector() {
		super();
	}

	// ----------------------------------------------------
	// --- ADD methods ------------------------------------ 
	// ----------------------------------------------------
	/* (non-Javadoc)
	 * @see java.util.Vector#addElement(java.lang.Object)
	 */
	@Override
	public synchronized void addElement(String element) {
		this.add2DefaultListModel(element);
		super.addElement(element);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#add(java.lang.Object)
	 */
	@Override
	public synchronized boolean add(String element) {
		this.add2DefaultListModel(element);
		return super.add(element);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#addAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean addAll(Collection<? extends String> collection) {
		for (Iterator<? extends String> iterator = collection.iterator(); iterator.hasNext();) {
			String listEntry = (String) iterator.next();
			this.add2DefaultListModel(listEntry);
		}
		return super.addAll(collection);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, String element) {
		this.add2DefaultListModel(index, element);	
		super.add(index, element);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#addAll(int, java.util.Collection)
	 */
	@Override
	public synchronized boolean addAll(int index, Collection<? extends String> c) {
		for (Iterator<? extends String> iterator = c.iterator(); iterator.hasNext();) {
			String listEntry = (String) iterator.next();
			this.add2DefaultListModel(index, listEntry);
			index++;
		}
		return super.addAll(index, c);
	}

	// ----------------------------------------------------	
	// --- SET methods ------------------------------------
	// ----------------------------------------------------
	@Override
	public synchronized String set(int index, String element) {
		String oldElement = super.set(index, element);
		this.set2DefaultListModel(index, element);
		return oldElement;
	}
	
	@Override
	public synchronized void setElementAt(String element, int index) {
		super.setElementAt(element, index);
		this.set2DefaultListModel(index, element);
	}
	
	// ----------------------------------------------------	
	// --- REMOVE methods ---------------------------------
	// ----------------------------------------------------
	/* (non-Javadoc)
	 * @see java.util.Vector#remove(int)
	 */
	@Override
	public synchronized String remove(int index) {
		String returnValue = this.get(index);
		this.removeElementAt(index);
		return returnValue;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		return this.removeElement(o);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#removeAllElements()
	 */
	@Override
	public synchronized void removeAllElements() {
		this.removeAllFromDefaultListModel();
		super.removeAllElements();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Vector#removeElementAt(int)
	 */
	@Override
	public synchronized void removeElementAt(int index) {
		this.removeFromDefaultListModel(index);
		super.removeElementAt(index);
	}
	
	/**
	 * Sets the resources list model.
	 * @param resourcesListModel the resourcesListModel to set
	 */
	public void setResourcesListModel(DefaultListModel<ProjectResource2Display> resourcesListModel) {
		this.resourcesListModel = resourcesListModel;
	}
	
	/**
	 * Gets the resources list model.
	 * @return the resourcesListModel
	 */
	@XmlTransient 
	public DefaultListModel<ProjectResource2Display> getResourcesListModel() {
		if (resourcesListModel==null) {
			resourcesListModel = new DefaultListModel<ProjectResource2Display>();
			for (int i = 0; i < this.size(); i++) {
				this.add2DefaultListModel(this.get(i));
			}
		}
		return resourcesListModel;
	}
	
	
	/**
	 * Add to default list model.
	 * @param resource the resource
	 */
	private void add2DefaultListModel(String resource) {
		this.add2DefaultListModel(resourcesListModel.size(), resource);
	}
	/**
	 * Add2 default list model.
	 * @param index the index
	 * @param resource the resource
	 */
	private void add2DefaultListModel(int index, String resource) {
		ProjectResource2Display r2d = new ProjectResource2Display(resource);
		this.resourcesListModel.add(index, r2d);
	}
	
	/**
	 * Set a new resource entry to the list model at the specified index position.
	 *
	 * @param index the index
	 * @param resource the resource
	 */
	private void set2DefaultListModel(int index, String resource) {
		ProjectResource2Display r2d = new ProjectResource2Display(resource);
		this.resourcesListModel.set(index, r2d);
	}
	
	/**
	 * Removes the from default list model.
	 * @param index the index
	 */
	private void removeFromDefaultListModel(int index) {
		this.resourcesListModel.remove(index);
	}
	/**
	 * Removes the all from default list model.
	 */
	private void removeAllFromDefaultListModel() {
		this.resourcesListModel.removeAllElements();
	}
	
	/**
	 * Sets the prefix text.
	 *
	 * @param resource the resource
	 * @param prefixText the prefix text
	 */
	public void setPrefixText(String resource, String prefixText){
		ProjectResource2Display r2d = this.getDefaultListModelElement(resource);
		if (r2d!=null) {
			r2d.setPrefixText(prefixText);	
		}
	}
	
	/**
	 * Sets the suffix text.
	 *
	 * @param resource the resource
	 * @param suffixText the additional text
	 */
	public void setSuffixText(String resource, String suffixText){
		ProjectResource2Display r2d = this.getDefaultListModelElement(resource);
		if (r2d!=null) {
			r2d.setSuffixText(suffixText);	
		}
	}
	
	/**
	 * Gets the default list model element.
	 *
	 * @param resource the resource
	 * @return the default list model element
	 */
	public ProjectResource2Display getDefaultListModelElement(String resource) {
		for (int i = 0; i < resourcesListModel.size(); i++) {
			ProjectResource2Display r2d = (ProjectResource2Display) resourcesListModel.get(i);
			if (r2d.getFileOrFolderResource().equals(resource)){
				return r2d;
			}
		}
		return null;
	}
	
}
