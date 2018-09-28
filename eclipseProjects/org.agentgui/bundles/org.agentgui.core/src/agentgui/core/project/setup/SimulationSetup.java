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
package agentgui.core.project.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import agentgui.core.application.Application;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.common.AbstractUserObject;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import de.enflexit.common.classLoadService.ObjectInputStreamForClassLoadService;

/**
 * This is the model class for a simulation setup.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement 
public class SimulationSetup {

	public static final String XML_FileSuffix = ".xml";
	public static final String USER_MODEL_BIN_FileSuffix = ".bin";
	public static final String USER_MODEL_XML_FileSuffix = "-UserObject.xml";

	/** The possible indicator for setup file types. */
	public enum SetupFileType {
		BASE_XML_FILE,
		USER_OBJECT_XML,
		USER_OBJECT_BIN
	}
	
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
	private String environmentFileName;
	
	/** The time model settings. */
	@XmlElementWrapper(name = "timeModelSettings")
	private HashMap<String, String> timeModelSettings;
	
	/**
	 * This field can be used in order to provide customized objects during
	 * the runtime of a project. This will be not stored within the file 'agentgui.xml' 
	 */
	@XmlTransient 
	private Serializable userRuntimeObject;
	@XmlElement(name = "userObjectClassName")
	private String userRuntimeObjectClassName;

	
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
		this.setProject(project);
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
	 * This method saves the current Simulation-Setup to the default location.
	 * @return true, if saving was successful
	 */
	public boolean save() {
		File setupXmlFile = new File(this.currProject.getSimulationSetups().getCurrSimXMLFile());
		return this.save(setupXmlFile, true);
	}

	/**
	 * This method saves the current Simulation-Setup to the specified location.
	 * @param setupXmlFile The setup xml file (other file paths are derived)
	 * @return true, if saving was successful
	 */
	public boolean save(File setupXmlFile, boolean saveUserRuntimeObject) {
		
		boolean saved = true;
		this.mergeAgentListModels();
		
		try {			
			// --------------------------------------------
			// --- Save this instance as XML-file ---------
			JAXBContext pc = JAXBContext.newInstance(this.getClass()); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 

			Writer pw = new FileWriter(setupXmlFile);
			pm.marshal(this, pw);
			pw.close();
			
			// --------------------------------------------
			// --- Save the userRuntimeObject -------------
			if (saveUserRuntimeObject==true) {
				// --- ... as XML or bin file ? -----------
				boolean successXmlSave = this.saveUserObjectAsXmlFile(setupXmlFile);
				if (successXmlSave==false) {
					// --- Backup: save as bin file -------
					this.saveUserObjectAsBinFile(setupXmlFile);
				}
			}
			
			// --------------------------------------------
			// --- Notify about save action is done -------
			this.currProject.setNotChangedButNotify(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_SAVED));
		
		} catch (Exception ex) {
			System.out.println("[" + this.getClass().getSimpleName() + "] XML-Error while saving setup file!");
			ex.printStackTrace();
			saved = false;
		}		
		return saved;		
	}
	
	/**
	 * Saves the current user object as XML file.
	 *
	 * @param setupXmlFile the setup XML file
	 * @return true, if successful
	 */
	private boolean saveUserObjectAsXmlFile(File setupXmlFile) {
		File destinFile = SimulationSetup.getSetupFile(setupXmlFile, SetupFileType.USER_OBJECT_XML);
		return AbstractUserObject.saveUserObjectAsXmlFile(destinFile, this.getUserRuntimeObject());
	}
	
	/**
	 * Saves the current user object as bin file.
	 *
	 * @param setupXmlFile the setup XML file
	 * @return true, if successful
	 */
	private boolean saveUserObjectAsBinFile(File setupXmlFile) {
		
		boolean successfulSaved = false;
		
		if (setupXmlFile==null) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The path for saving the setups user runtime object is not allowed to be null!");
			return false;
		}
		
		if (this.getUserRuntimeObject()==null) {
			successfulSaved = true;
		
		} else {
			try {
				FileOutputStream fos = null;
				ObjectOutputStream out = null;
				try  {
					fos = new FileOutputStream(SimulationSetup.getSetupFile(setupXmlFile, SetupFileType.USER_OBJECT_BIN));
					out = new ObjectOutputStream(fos);
					out.writeObject(this.userRuntimeObject);
					successfulSaved = true;
					
				} catch(IOException ex) {
					ex.printStackTrace();
				} finally {
					if (out!=null) out.close();
					if (fos!=null) fos.close();
				}
				
			} catch (IOException ioEx) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Error while saving the setups user runtime object as bin file:");
				ioEx.printStackTrace();
			} 
		}
		return successfulSaved;
	}
	
	
	/**
	 * Loads a {@link SimulationSetup} from the specified XML file.
	 * @param setupXmlFile the setup xml file
	 * @return the simulation setup, or null if loading failed
	 */
	public static SimulationSetup load(File setupXmlFile, boolean loadUserRuntimeObject) {
		
		// --- Load the setup from XML file ---------------
		SimulationSetup setup = null;
		FileReader fileReader = null;
		try {
			JAXBContext pc = JAXBContext.newInstance(SimulationSetup.class);
			Unmarshaller um = pc.createUnmarshaller();
			fileReader = new FileReader(setupXmlFile);
			setup = (SimulationSetup) um.unmarshal(fileReader);
			setup.setProject(Application.getProjectFocused());
			
		} catch (JAXBException | IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fileReader!=null) fileReader.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		// --- If no setup was loaded return here -----------------------------
		if (setup==null) return null;
		
		// --- Load the user runtime object if specified ----------------------
		if (loadUserRuntimeObject==true) {
			SimulationSetup.loadSetupUserDataModel(setupXmlFile, setup);
		}
		
		// --- Initialize the agent lists -------------------------------------
		if (setup.initializeAgentLists()==true) {
			return setup;
		} else {
			// --- An exception occurred during initializing the agent lists
			return null;
		}
	}
	/**
	 * Loads the {@link SimulationSetup}'s user runtime object from the specified file.
	 *
	 * @param xmlBaseFile the xml base file
	 * @param setup the setup
	 * @return true, if successful
	 */
	private static void loadSetupUserDataModel(File xmlBaseFile, SimulationSetup setup) {
		
		// --- ... as XML or bin file ? -------------------
		boolean successXmlLoad = SimulationSetup.loadUserObjectFromXmlFile(xmlBaseFile, setup);
		if (successXmlLoad==false) {
			// --- Backup: load from bin file -------------
			SimulationSetup.loadUserObjectFromBinFile(xmlBaseFile, setup);
			
		} else {
			// --- Delete old bin file if available -------
			File binFileUserObject = SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_BIN);
			if (binFileUserObject.exists()==true) {
				boolean deleted = binFileUserObject.delete();
				if (deleted==false) {
					binFileUserObject.deleteOnExit();
				}
			}
		}
		
	}
	
	/**
	 * Loads the user object from a XML file.
	 *
	 * @param xmlBaseFile the xml base file
	 * @param setup the setup
	 * @return true, if successful
	 */
	private static boolean loadUserObjectFromXmlFile(File xmlBaseFile, SimulationSetup setup) {
		
		boolean successfulLoaded = false;
		if (setup!=null && setup.getUserRuntimeObjectClassName()!=null) {
			try {
				File userObjectFile = SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_XML);
				Class<?> userRuntimeClass = ClassLoadServiceUtility.forName(setup.getUserRuntimeObjectClassName());
				AbstractUserObject userObject = AbstractUserObject.loadUserObjectFromXmlFile(userObjectFile, userRuntimeClass);
				if (userObject!=null) {
					setup.setUserRuntimeObject(userObject);
					successfulLoaded = true;
				}
				
			} catch (ClassNotFoundException | NoClassDefFoundError cEx) {
				cEx.printStackTrace();
			}
		}
		return successfulLoaded;
	}
	/**
	 * Loads the user object from a bin file.
	 *
	 * @param xmlBaseFile the xml base file
	 * @param setup the setup
	 * @return true, if successful
	 */
	private static boolean loadUserObjectFromBinFile(File xmlBaseFile, SimulationSetup setup) {
		
		boolean successfulLoaded = false;
		
		File userRuntimeObjectFile = SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_BIN);
		FileInputStream fis = null;
		ObjectInputStreamForClassLoadService in = null;
		try {
			fis = new FileInputStream(userRuntimeObjectFile);
			in = new ObjectInputStreamForClassLoadService(fis, ClassLoadServiceUtility.class);
			Serializable userObject = (Serializable)in.readObject();
			setup.setUserRuntimeObject(userObject);
			successfulLoaded = true;
			
		} catch(IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (in!=null) in.close();
				if (fis!=null) fis.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return successfulLoaded;
	}
	
	/**
	 * Initializes the agent lists.
	 * @return true, if successful
	 */
	private boolean initializeAgentLists() {
		// --- Create the DefaultListModels for the current agent configuration ---- 
		this.createHashMap4AgentDefaulListModelsFromAgentList();
		
		// --- Set the agent classes in the agentSetup -----------------------------
		ArrayList<AgentClassElement4SimStart> agentList = this.getAgentList();
		for (int i = 0; i < agentList.size(); i++) {
			try {
				// --- The .toSting method will check if the class exists ----------
				agentList.get(i).toString();
				
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}
		return true;
	}
	/**
	 * Gets the agent list.
	 * @return the agentList
	 */
	@XmlTransient
	public ArrayList<AgentClassElement4SimStart> getAgentList() {
		this.mergeAgentListModels();
		return agentList;
	}
	
	/**
	 * Will merge all default list models to one array list.
	 */
	private void mergeAgentListModels(){

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
	 * Clear the agent default list models.
	 */
	public void clearAgentDefaultListModels() {
		Vector<String> agentListNames = new Vector<String>(this.hashMap4AgentDefaulListModels.keySet());
		for (int i=0; i<agentListNames.size(); i++) {
			this.hashMap4AgentDefaulListModels.get(agentListNames.get(i)).clear();
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
		if (this.userRuntimeObject==null) {
			this.setUserRuntimeObjectClassName(null);
		} else {
			this.setUserRuntimeObjectClassName(this.userRuntimeObject.getClass().getName());
		}
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
	 * Gets the user runtime object class name.
	 * @return the user runtime object class name
	 */
	private String getUserRuntimeObjectClassName() {
		if (userRuntimeObjectClassName==null && this.getUserRuntimeObject()!=null) {
			userRuntimeObjectClassName = this.getUserRuntimeObject().getClass().getName();
		}
		return userRuntimeObjectClassName;
	}
	/**
	 * Sets the user runtime object class name.
	 * @param userRuntimeObjectClassName the new user runtime object class name
	 */
	private void setUserRuntimeObjectClassName(String userRuntimeObjectClassName) {
		this.userRuntimeObjectClassName = userRuntimeObjectClassName;
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
			this.mergeAgentListModels();	
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
		this.mergeAgentListModels();
		
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

	
	// --------------------------------------------------------------
	// --- From here, some static help methods for file handling-----
	// --------------------------------------------------------------
	/**
	 * Returns, based on the specified base XML file, the specified setup file (especially for user runtime objects).
	 *
	 * @param xmlBaseFile the XML base file
	 * @param setupFileType the setup file type
	 * @return the setup file
	 */
	public static File getSetupFile(File xmlBaseFile, SetupFileType setupFileType) {
		
		if (xmlBaseFile==null) return null;
		if (setupFileType==null) return null;
		if (setupFileType==SetupFileType.BASE_XML_FILE) return xmlBaseFile;
		
		// --- Remove the file suffix ---------------------
		String baseFilename = xmlBaseFile.getAbsolutePath();
		int cut = baseFilename.lastIndexOf(".");
		baseFilename = baseFilename.substring(0, cut);
		
		switch (setupFileType) {
		case USER_OBJECT_XML:
			baseFilename += USER_MODEL_XML_FileSuffix; 	
			break;

		case USER_OBJECT_BIN:
			baseFilename += USER_MODEL_BIN_FileSuffix;
			break;
		default:
			break;
		}
		return new File(baseFilename);
	}
	/**
	 * Returns, based on the specified base XML file, all possible setup file (e.g. also for user runtime objects).
	 *
	 * @param xmlBaseFile the XML base file
	 * @return the setup file
	 */
	public static List<File> getSetupFiles(File xmlBaseFile) {
		List<File> setupFiles = new ArrayList<>();
		setupFiles.add(xmlBaseFile);
		setupFiles.add(SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_BIN));
		setupFiles.add(SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_XML));
		return setupFiles;
	}
	
	/**
	 * Returns the nominal base setup file from the specified file, but without garanty that 
	 * the file really exists.
	 *
	 * @param fileToCheck the file to check
	 * @return the base setup file
	 */
	public static File getSetupBaseFile(File fileToCheck) {
		
		String fileToCheckPath = fileToCheck.getAbsolutePath();
		
		if (fileToCheckPath.endsWith(USER_MODEL_XML_FileSuffix)) {
			fileToCheckPath = fileToCheckPath.substring(0, fileToCheckPath.length() - USER_MODEL_XML_FileSuffix.length()) + XML_FileSuffix;
		} else if (fileToCheckPath.endsWith(USER_MODEL_BIN_FileSuffix)) {
			fileToCheckPath = fileToCheckPath.substring(0, fileToCheckPath.length() - USER_MODEL_BIN_FileSuffix.length()) + XML_FileSuffix;
		} else if (fileToCheckPath.endsWith(XML_FileSuffix)) {
			// --- Nothing to exchange ----			
		} else {
			fileToCheckPath = null;
		}
		
		if (fileToCheckPath==null) {
			return null;
		}
		return new File(fileToCheckPath);
	}
	
}
