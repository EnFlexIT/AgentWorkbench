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

import javax.swing.DefaultListModel;

import agentgui.core.agents.AgentClassElement;
import agentgui.core.application.Application;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.jade.ClassSearcherSingle;
import agentgui.core.project.Project;

/**
 * This class can be use like a JList, but it will show a progress, if
 * the corresponding search process is still running
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JListClassSearcher extends JListWithProgressBar {

	private static final long serialVersionUID = 6613469481292382381L;

	private Class<?> currSearchFor = null;
	private ClassSearcherSingle currClassSearcherSingle = null; 
	private Project currProject = null;
	private DefaultListModel currListModel = null;
	
	private String currTextSearch4 = null;
	private DefaultListModel tmpListModel = null;
	
	/**
	 * This constructor was only build to enable the use of the Visual Editor  
	 */
	@Deprecated
	public JListClassSearcher() {
		super();
	}
	
	/**
	 * Constructor for this class in case that we DON'T NEED project-specific
	 * Classes. Needs constants from the class 'ClassSearcher'
	 * @param class2Search4
	 */
	public JListClassSearcher(Class<?> class2Search4) {
		super();
		this.currSearchFor = class2Search4;
		this.reSetListModel();
	}

	/**
	 * Constructor for this class in case that we DON'T NEED project-specific
	 * Classes. Needs a specific ClassSearcherSingle, which looks for a specific class
	 * @param searchFor
	 */
	public JListClassSearcher(Class<?> searchFor, ClassSearcherSingle css) {
		super();
		this.currSearchFor = searchFor;
		this.currClassSearcherSingle = css;
		this.reSetListModel();
	}
	
	/**
	 * Constructor for this class in case that we NEED project-specific
	 * Classes. Needs constants from the class 'ClassSearcher'
	 * @param searchFor
	 */
	public JListClassSearcher(Class<?> searchFor, Project project) {
		super();
		this.currSearchFor = searchFor;
		this.currProject = project;
		this.reSetListModel();
	}
	
	/**
	 * Sets the DefaultListModel for the current JList to display
	 */
	public void reSetListModel() {

		// --- Select the required 'ClassSearcherSingle' --
		if (this.currSearchFor == ClassSearcher.CLASSES_AGENTS) {
			this.currClassSearcherSingle = Application.ClassDetector.csAgents;
		} else if (this.currSearchFor == ClassSearcher.CLASSES_ONTOLOGIES) {
			this.currClassSearcherSingle = Application.ClassDetector.csOntologies;
		} else if (this.currSearchFor == ClassSearcher.CLASSES_BASESERVICE) {
			this.currClassSearcherSingle = Application.ClassDetector.csBaseService;
		} 
		
		// --- Get the needed ListModel -------------------
		if (this.currProject==null) {
			this.currListModel = currClassSearcherSingle.getjListModelClassesFound();
		} else {
			this.currListModel = currClassSearcherSingle.getjListModelClassesFoundProject();
		}
		
		this.currClassSearcherSingle.registerJListWithProgress(this);
		this.setModel(this.currListModel);
	}

	
	/**
	 * Filters the displayed list, depending on the search String
	 * @param search4
	 */
	public void setModelFiltered(String search4) {
		this.setModelFiltered(search4, null);
	}
	/**
	 * Filters the displayed list, depending on the search String
	 * while excluding the alsoExclude-variable 
	 * @param search4
	 */
	public void setModelFiltered(String search4, String alsoExcludePackage) {
		
		currTextSearch4 = search4;
		if ( (currTextSearch4==null | currTextSearch4.equals("")) && alsoExcludePackage==null ) {
			this.setModel(currListModel);
			return;
		}
		
		// --- Start filtering the Classes ----------------
		tmpListModel = new DefaultListModel();
		for (int i =0; i<currListModel.getSize();i++) {
			Object currObject    = currListModel.get(i);
			Class<?> currClass   = null;
			String currClassName = null;
			if (currSearchFor == ClassSearcher.CLASSES_AGENTS) {
				AgentClassElement ace = (AgentClassElement) currObject;
				currClass = ace.getElementClass();
				currClassName = ace.toString();
			} else if (currSearchFor == ClassSearcher.CLASSES_ONTOLOGIES) {
				ClassElement2Display ce2d = (ClassElement2Display) currObject;
				currClass = ce2d.getElementClass();
				currClassName = currClass.getName();
			} else if (currSearchFor == ClassSearcher.CLASSES_BASESERVICE) {
				ClassElement2Display ce2d = (ClassElement2Display) currObject;
				currClass = ce2d.getElementClass();
				currClassName = currClass.getName();
			} else {
				ClassElement2Display ce2d = (ClassElement2Display) currObject;
				currClass = ce2d.getElementClass();
				currClassName = currClass.getName();
			}
			
			if (alsoExcludePackage==null) {
				if (currClassName.toLowerCase().contains(currTextSearch4.toLowerCase()) ) {
					currClassSearcherSingle.add2ListModel(tmpListModel, currClass);
				}	
			} else {
				if (currClassName.toLowerCase().startsWith(alsoExcludePackage.toLowerCase())==false) {
					if (currClassName.toLowerCase().contains(currTextSearch4.toLowerCase()) ) {
						currClassSearcherSingle.add2ListModel(tmpListModel, currClass);
					}	
				}
			}
					
		}// end for
		this.setModel(tmpListModel);
	}
	
	public void refreshModel() {
		if (currTextSearch4!=null && currTextSearch4.equals("")==false) {
			this.setModelFiltered(this.currTextSearch4);
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
