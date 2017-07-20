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
package agentgui.core.plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import agentgui.core.application.Language;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.project.Project;
import jade.core.ProfileImpl;

/**
 * This class represents the list of plug-in's, which are loaded in a single
 * project.
 * 
 * @see Project#plugInsLoaded
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PlugInsLoaded extends Vector<PlugIn> {

	private static final long serialVersionUID = 8827995777943963298L;
	
	/**
	 * This method will load a PlugIn given by its class-reference.
	 *
	 * @param project the project
	 * @param plugInReference the reference to the plug-in class 
	 * @return the plug-in
	 * @throws PlugInLoadException the plug in load exception
	 */
	public PlugIn loadPlugin(Project project, String plugInReference) throws PlugInLoadException {
		
		PlugIn plugIn = null;
		
		// ----------------------------------------------------------
		// --- If the plugIn-class WAS found  -----------------------
		try {
			plugIn = ClassLoadServiceUtility.getPlugInInstance(plugInReference, project);
			
		} catch (ClassNotFoundException e) {
			throw new PlugInLoadException(e);
		} catch (SecurityException e) {
			throw new PlugInLoadException(e);
		} catch (NoSuchMethodException e) {
			throw new PlugInLoadException(e);
		} catch (IllegalArgumentException e) {
			throw new PlugInLoadException(e);
		} catch (InstantiationException e) {
			throw new PlugInLoadException(e);
		} catch (IllegalAccessException e) {
			throw new PlugInLoadException(e);
		} catch (InvocationTargetException e) {
			throw new PlugInLoadException(e);
		}
		if (plugIn==null) return null;
		
		
		// ----------------------------------------------------------
		// --- Check some configurations of the PlugIn -------------- 
		if (plugIn.getName()==null || plugIn.getName().equals("")) {
			throw new PlugInLoadException(Language.translate("Es wurde kein Name für das PlugIn konfiguriert"));
		}
		
		// --- Remind the classReference in the PlugIn --------------
		plugIn.setClassReference(plugInReference);
		// --- Call the onPlugIn() method ---------------------------
		try {
			plugIn.onPlugIn();	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// --- add the PlugIn to the local Vector -------------------
		this.add(plugIn);		
		return plugIn;
	}
	
	/**
	 * This will unload and remove a single ProjectPlugin from the current project.
	 *
	 * @param plugIn the plug-in
	 */
	public void removePlugIn(PlugIn plugIn) {
		try {
			// --- Call the onPlugOut()method -----------------------
			plugIn.onPlugOut();
			// --- Call the afterPlugOut()method --------------------
			plugIn.afterPlugOut();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// --- remove the PlugIn from the local Vector --------------
		this.remove(plugIn);
	}
	
	
	/**
	 * This method returns a single PlugIn Instance
	 * given by its name or its class reference.
	 *
	 * @param plugInName_or_plugInReference the plug in name_or_plug in reference
	 * @return the plug in
	 */
	public PlugIn getPlugIn(String plugInName_or_plugInReference) {

		String search = plugInName_or_plugInReference;
		for (int i = 0; i < this.size(); i++) {
			PlugIn pi = this.get(i);
			if (pi.getName().equals(search)) {
				return pi;
			} else if (pi.getClassReference().equals(search)) {
				return pi;
			}
		}
		return null;
	}
	
	/**
	 * This method checks if a PlugInClass is already loaded.
	 *
	 * @param plugInName_or_plugInReference the plug-in name or the plug-in reference
	 * @return true, if is loaded
	 */
	public boolean isLoaded(String plugInName_or_plugInReference) {

		if (this.getPlugIn(plugInName_or_plugInReference)==null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method invokes all loaded PlugIn-Classes, so that it can set 
	 * further configurations for JADE container.
	 * 
	 * @param jadeContainerProfile The profile to change
	 * @return the configured JADE Profile
	 */
	public ProfileImpl getJadeProfile(ProfileImpl jadeContainerProfile) {

		for (int i = 0; i < this.size(); i++) {
			PlugIn pi = this.get(i);
			jadeContainerProfile = pi.getJadeProfile(jadeContainerProfile);
		}
		return jadeContainerProfile;
	}

	
	/**
	 * Checks, if the plugins return that they find valid preconditions before the execution of the MAS.
	 * @return true, if the preconditions are valid 
	 */
	public boolean haveValidPreconditions() {
		boolean validPrecondition = true;
		for (PlugIn plugin : this) {
			try {
				validPrecondition = plugin.hasValidPreconditionForMasExecution();
				if (validPrecondition==false) {
					System.err.println("PlugIn '" + plugin.getName() + "' found invalid preconditions for the MAS execution.");
					break;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return validPrecondition;
	}

	/**
	 * Notifies all loaded plugins for the upcoming agent start.
	 */
	public void notifyPluginsForStartMAS() {
		for (PlugIn plugin : this) {
			try {
				plugin.onMasWillBeExecuted();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * Notifies all project plugins for agent termination.
	 */
	public void notifyPluginsForTerminatedMAS() {
		for (PlugIn plugin : this) {
			try {
				plugin.onMasWasTerminated();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
