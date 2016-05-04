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
package agentgui.core.sim.setup;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Application;
import agentgui.core.project.Project;
import agentgui.core.sim.setup.SimulationSetupNotification.SimNoteReason;

/**
 * This is the model class for a simulation setup.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement 
public class SimulationSetup {

	/** Lists the possible reasons why a SimulationSetup can be changed and unsaved  */
	public enum CHANGED {
		TimeModelSettings,
		AgentConfiguration,
		UserRuntimeObject
	}
	
	@XmlTransient 
	private Project currProject = null;
	
	@XmlTransient 
	public static final String AGENT_LIST_ManualConfiguration = "01 AgentStartManual";
	@XmlTransient 
	public static final String AGENT_LIST_EnvironmentConfiguration = "02 AgentStartEnvironment";
	
	/** This Hash holds the instances of all agent start lists. */
	@XmlTransient 
	private HashMap<String, DefaultListModel<AgentClassElement4SimStart>> hashMap4AgentDefaulListModels = new HashMap<String, DefaultListModel<AgentClassElement4SimStart>>();
	/** The ComboBoxModel for agent lists. */
	@XmlTransient 
	private DefaultComboBoxModel<String> comboBoxModel4AgentLists = new DefaultComboBoxModel<String>();
	
	
	/** The agent list to save. */
	@XmlElementWrapper(name = "agentSetup")
	@XmlElement(name="agent")
	private ArrayList<AgentClassElement4SimStart> agentList = new ArrayList<AgentClassElement4SimStart>();

	
	/** The environment file name. */
	private String environmentFileName = null;
	
	/** The time model settings. */
	@XmlElementWrapper(name = "timeModelSettings")
	private HashMap<String, String> timeModelSettings = null;
	
	/**
	 * This field can be used in order to provide customised objects during
	 * the runtime of a project. This will be not stored within the file 'agentgui.xml' 
	 */
	@XmlTransient 
	private Serializable userRuntimeObject = null;
	
	
	
	/**
	 * Constructor without arguments (This is first of all
	 * for the JAXB-Context and should not be used by any
	 * other context).
	 */	
	public SimulationSetup() {
	}
	/**
	 * Default Constructor of this class.
	 * @param project the project
	 */
	public SimulationSetup(Project project) {
		this.currProject = project;
	}
	
	/**
	 * Sets the current project.
	 * @param project the currProject to set
	 */
	public void setProject(Project project) {
		this.currProject = project;
	}
	/**
	 * Sets the current project to be unsaved.
	 * @see CHANGED
	 * @param reason the new project unsaved
	 */
	private void setProjectUnsaved(Object reason) {
		if (this.currProject!=null) {
			this.currProject.setChangedAndNotify(reason);
		}
	}

	/**
	 * This method saves the current Simulation-Setup.
	 *
	 * @return true, if saving was successful
	 */
	public boolean save() {
		
		boolean saved = true;
		this.mergeListModels();
		
		try {			
			// --------------------------------------------
			// --- Save this instance as XML-file ---------
			// --------------------------------------------
			JAXBContext pc = JAXBContext.newInstance(this.getClass()); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
			pm.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 

			Writer pw = new FileWriter( this.currProject.getSimulationSetups().getCurrSimXMLFile() );
			pm.marshal(this, pw);
			pw.close();
			
			// --------------------------------------------
			// --- Save the userRuntimeObject in the   ----
			// --- SimSetup into a different file as a ----
			// --- Serializable binary object.		   ----
			// --------------------------------------------
			FileOutputStream fos = null;
			ObjectOutputStream out = null;
		    try  {
		    	String binFileName = Application.getGlobalInfo().getBinFileNameFromXmlFileName(this.currProject.getSimulationSetups().getCurrSimXMLFile()); 
		    	fos = new FileOutputStream(binFileName);
		    	out = new ObjectOutputStream(fos);
		    	out.writeObject(this.userRuntimeObject);
		    } catch(IOException ex) {
		    	ex.printStackTrace();
		    	saved = false;
		    } finally {
		    	out.close();
		    	fos.close();
		    }
			this.currProject.setNotChangedButNotify(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_SAVED));
		
		} catch (Exception ex) {
			System.out.println("XML-Error while saving Setup-File!");
			ex.printStackTrace();
			saved = false;
		}		
		return saved;		
	}
	
	/**
	 * Gets the agent list.
	 * @return the agentList
	 */
	@XmlTransient
	public ArrayList<AgentClassElement4SimStart> getAgentList() {
		this.mergeListModels();
		return agentList;
	}
	
	/**
	 * Will merge all default list models to one array list.
	 */
	private void mergeListModels(){

		this.agentList = new ArrayList<AgentClassElement4SimStart>();

		// ------------------------------------------------
		// --- Write Data from GUI to the local variable --
		Set<String> agentListNamesSet = this.hashMap4AgentDefaulListModels.keySet();
		Vector<String> agentListNames = new Vector<String>();
		agentListNames.addAll(agentListNamesSet);
		Collections.sort(agentListNames);
		
		for (int i = 0; i < agentListNames.size(); i++) {
			DefaultListModel<AgentClassElement4SimStart> dlm = this.hashMap4AgentDefaulListModels.get(agentListNames.get(i));
			this.addToAgentList(dlm);
		}
	}
	
	/**
	 * This Method transfers a DefaultListModel to
	 * the localArrayList 'agentList' which is a
	 * type of 'AgentClassElement4SimStart'.
	 *
	 * @param lm the DefaultListModel that has to be added to the overall agent list
	 */
	private void addToAgentList(DefaultListModel<AgentClassElement4SimStart> lm) {
		if (lm==null) return;
		for (int i = 0; i < lm.size(); i++) {
			this.agentList.add((AgentClassElement4SimStart) lm.get(i));
		}		
	}
	
	/**
	 * Sets the agent list.
	 * @param agentList the agentList to set
	 */
	public void setAgentList(ArrayList<AgentClassElement4SimStart> agentList) {
		this.agentList = agentList;
		this.setProjectUnsaved(CHANGED.AgentConfiguration);
	}
	
	/**
	 * This method will create all DefaultListModels which will be used within the visible application<br> 
	 * as for example for the manual agent configuration (tab 'Simulation-Setup' => 'Agent-Start')<br>
	 * or the configuration of the environment model (tab 'Simulation-Setup' => 'Simulation Environment').<br>
	 * The resulting ListModels can be get by using {@link #getAgentDefaultListModel(String)}
	 */
	public void createHashMap4AgentDefaulListModelsFromAgentList() {

		if (this.hashMap4AgentDefaulListModels==null) {
			this.hashMap4AgentDefaulListModels = new HashMap<String, DefaultListModel<AgentClassElement4SimStart>>();
		}
		
		// --- Rebuild the ComboBoxModel for all start lists --------
		this.comboBoxModel4AgentLists = new DefaultComboBoxModel<String>();
		
		// --- Run through the list of all configured agent --------- 
		DefaultListModel<AgentClassElement4SimStart> dlm = null;
		for (int i = 0; i < agentList.size(); i++) {
			
			AgentClassElement4SimStart ace4ss = agentList.get(i);
			String memberOf = ace4ss.getListMembership();
			if (hashMap4AgentDefaulListModels.get(memberOf)==null) {
				dlm = new DefaultListModel<AgentClassElement4SimStart>();
				this.setAgentDefaultListModel(memberOf, dlm);
			} else {
				dlm = hashMap4AgentDefaulListModels.get(memberOf);
			}
			dlm.addElement(ace4ss);
		}
		
	}
	
	/**
	 * Here a complete agent start list (DefaultListModel) can be added to the simulation setup.
	 *
	 * @param listName the list name
	 * @param defaultListModel4AgentStarts the default list model4 agent starts
	 */
	public void setAgentDefaultListModel(String listName, DefaultListModel<AgentClassElement4SimStart> defaultListModel4AgentStarts) {
		if (listName!=null) {
			this.hashMap4AgentDefaulListModels.put(listName, defaultListModel4AgentStarts);
			if (this.comboBoxModel4AgentLists.getIndexOf(listName)==-1) {
				this.comboBoxModel4AgentLists.addElement(listName);	
				this.sortComboBoxModel4AgentLists();
			}
		}
	}
	
	/**
	 * Sets the ComboBoxModel for agent lists.
	 * @param comboBoxModel4AgentLists the comboBoxModel4AgentLists to set
	 */
	public void setComboBoxModel4AgentLists(DefaultComboBoxModel<String> comboBoxModel4AgentLists) {
		this.comboBoxModel4AgentLists = comboBoxModel4AgentLists;
	}
	/**
	 * Gets the combo box model4 agent lists.
	 * @return the comboBoxModel4AgentLists
	 */
	@XmlTransient
	public DefaultComboBoxModel<String> getComboBoxModel4AgentLists() {
		return comboBoxModel4AgentLists;
	}
	/**
	 * Sort ComboBoxModel for agent lists.
	 */
	private void sortComboBoxModel4AgentLists() {
		
		// --- Move the current entries to a Vector -----------------
		Vector<String> agentLists = new Vector<String>();
		DefaultComboBoxModel<String> dlm = this.comboBoxModel4AgentLists;
		for (int i = 0; i < dlm.getSize(); i++) {
			agentLists.add((String) dlm.getElementAt(i));
		}
		// --- If the default list is not there, create it ----------
		if (agentLists.contains(AGENT_LIST_ManualConfiguration)==false) {
			this.getAgentDefaultListModel(new DefaultListModel<AgentClassElement4SimStart>(), AGENT_LIST_ManualConfiguration);
			agentLists.add(AGENT_LIST_ManualConfiguration);
		}
		// --- Sort the list ----------------------------------------
		Collections.sort(agentLists);
		// --- Recreate the JComboBoxModel for agent lists ----------
		this.comboBoxModel4AgentLists = new DefaultComboBoxModel<String>();
		for (int i = 0; i < agentLists.size(); i++) {
			this.comboBoxModel4AgentLists.addElement(agentLists.get(i));
		}
	}
	
	/**
	 * This method can be used in order to get an agents start list for the
	 * simulation, given by.
	 *
	 * @param listName the list name
	 * @return the agentListModel
	 */
	public DefaultListModel<AgentClassElement4SimStart> getAgentDefaultListModel(String listName) {
		return hashMap4AgentDefaulListModels.get(listName);
	}
	
	/**
	 * This method can be used in order to add an individual agent start list to the SimulationSetup.<br>
	 * The list will be filled with elements of the type {@link AgentClassElement4SimStart} coming from
	 * the stored setup file and will be later on also stored in the file of the simulation setup.
	 *
	 * @see AgentClassElement4SimStart AgentClassElement4SimStart - The type to use within a concrete list model
	 * 
	 * @param newDefaultListModel4AgentStarts the new DefaultListModel to set
	 * @param listName the name of the list to be assigned.
	 * Consider the use of one of the constants {@link #AGENT_LIST_ManualConfiguration} or {@link #AGENT_LIST_EnvironmentConfiguration}
	 * or just use an individual name
	 * @return the DefaultListModel of agents to be started for the specified list
	 */
	public DefaultListModel<AgentClassElement4SimStart> getAgentDefaultListModel(DefaultListModel<AgentClassElement4SimStart> newDefaultListModel4AgentStarts, String listName) {

		DefaultListModel<AgentClassElement4SimStart> dlm = this.getAgentDefaultListModel(listName);
		if (dlm==null) {
			dlm = newDefaultListModel4AgentStarts;
			this.setAgentDefaultListModel(listName, dlm);			
		}
		return dlm;
	}
	
	/**
	 * Gets the environment file name.
	 * @return the environment file name
	 */
	public String getEnvironmentFileName() {
		return environmentFileName;
	}
	/**
	 * Sets the environment file name.
	 * @param environmentFile the new environment file name
	 */
	public void setEnvironmentFileName(String environmentFile) {
		this.environmentFileName = environmentFile;
	}
	
	/**
	 * Sets the user runtime object.
	 * @param userRuntimeObject the userRuntimeObject to set
	 */
	public void setUserRuntimeObject(Serializable userRuntimeObject) {
		this.userRuntimeObject = userRuntimeObject;
		this.setProjectUnsaved(CHANGED.UserRuntimeObject);
	}
	/**
	 * Gets the user runtime object.
	 * @return the userRuntimeObject
	 */
	@XmlTransient
	public Serializable getUserRuntimeObject() {
		return userRuntimeObject;
	}
	
	/**
	 * Checks if an agent name already exists in the current agent configuration.
	 * @param localAgentName The agent name to search for
	 * @return true, if the agent name already exists
	 */
	public boolean isAgentNameExists(String localAgentName){
		return isAgentNameExists(localAgentName, true);
	}
	
	/**
	 * Checks if an agent name already exists in the current agent configuration.
	 * @param agentName2Check The agent name to search for
	 * @param mergeListModels indicates if the over all {@link #agentList} has to be build new
	 * @return true, if the agent name already exists
	 */
	public boolean isAgentNameExists(String agentName2Check, boolean mergeListModels){
		
		if (mergeListModels==true) {
			// --- merge all list models to the complete list 'agentList' -----
			this.mergeListModels();	
		}
		// --- search for the agent name in the list 'agentList' --------------s  
		for (int i = 0; i < agentList.size(); i++) {
			if(agentList.get(i).getStartAsName().equals(agentName2Check))
				return true;			
		}
		return false;
	}
	
	/**
	 * Will find a new unique name for an agent, if the suggestion is not already unique.
	 *
	 * @param agentNameSuggestion the agent name suggestion
	 * @return unique agent name for the simulation setup
	 */
	public String getAgentNameUnique(String agentNameSuggestion) {
		
		int incrementNo = 1;
		String newAgentName = agentNameSuggestion;
		
		// --- merge the list models to a complete list ---
		this.mergeListModels();
		
		// --- find a new name ----------------------------
		while (isAgentNameExists(newAgentName, false)==true) {
			newAgentName = agentNameSuggestion + "_" + incrementNo;
			incrementNo++;
		}
		return newAgentName;
	}

	/**
	 * Sets the time model settings.
	 * @param newTimeModelSettings the new time model settings
	 */
	public void setTimeModelSettings(HashMap<String, String> newTimeModelSettings) {
		this.timeModelSettings = newTimeModelSettings;
		this.setProjectUnsaved(CHANGED.TimeModelSettings);
	}
	/**
	 * Gets the time model settings.
	 * @return the time model settings
	 */
	@XmlTransient
	public HashMap<String,String> getTimeModelSettings() {
		if (this.timeModelSettings==null) {
			this.timeModelSettings= new HashMap<String, String>();
		}
		return this.timeModelSettings;
	}
	
}
