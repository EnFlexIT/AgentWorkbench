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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import agentgui.core.application.Application;
import agentgui.core.application.ApplicationListener.ApplicationEvent;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import de.enflexit.common.AbstractUserObject;
import de.enflexit.common.classLoadService.ObjectInputStreamForClassLoadService;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertiesEvent;
import de.enflexit.common.properties.PropertiesListener;

/**
 * This is the model class for a simulation setup.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement
public class SimulationSetup implements PropertiesListener {

	public static final String XML_FileSuffix = ".xml";
	public static final String USER_MODEL_BIN_FileSuffix = ".bin";
	public static final String USER_MODEL_XML_FileSuffix = "-UserObject.xml";

	public static final String AGENT_LIST_ManualConfiguration = "01 AgentStartManual";
	public static final String AGENT_LIST_EnvironmentConfiguration = "02 AgentStartEnvironment";

	/** The possible indicator for setup file types. */
	public enum SetupFileType {
		BASE_XML_FILE, USER_OBJECT_XML, USER_OBJECT_BIN
	}

	/**
	 * Lists the possible reasons why a SimulationSetup can be changed and unsaved
	 */
	public enum SetupChangeEvent {
		TimeModelSettings, AgentConfiguration, SetupProperties, UserRuntimeObject
	}

	private transient Project currProject = null;

	/** The ComboBoxModel for the agent list models. */
	@XmlTransient
	private DefaultComboBoxModel<String> comboBoxModel4AgentLists;
	/** This Hash holds the instances of all agent start lists. */
	@XmlTransient
	private HashMap<String, DefaultListModel<AgentClassElement4SimStart>> hashMap4AgentDefaultListModels;

	/** The list data listener for agent default list models. */
	@XmlTransient
	private ListDataListener listDataListener4AgentDefaulListModels;
	private transient boolean isDebugListDataListener = false;
	private transient boolean isPauseListDataListener;

	/** The agent list to save. */
	@XmlElementWrapper(name = "agentSetup")
	@XmlElement(name = "agent")
	private ArrayList<AgentClassElement4SimStart> agentList;
	private transient ArrayList<AgentClassElement4SimStart> agentListBeforeChanges;
	
	
	/** The time model settings. */
	@XmlElementWrapper(name = "timeModelSettings")
	private HashMap<String, String> timeModelSettings;

	@XmlElement(name = "setupProperties")
	private Properties properties;
	
	
	/**
	 * This field can be used in order to provide customized objects during the
	 * runtime of a project. This will be not stored within the file 'agentgui.xml'
	 */
	@XmlTransient
	private Serializable userRuntimeObject;
	@XmlElement(name = "userObjectClassName")
	private String userRuntimeObjectClassName;

	@XmlTransient
	private List<ApplicationEvent> applicationEventsToFire;

	
	/**
	 * Constructor without arguments (This is first of all for the JAXB-Context and
	 * should not be used by any other context).
	 */
	public SimulationSetup() { 
		this(null);
	}
	/**
	 * Default Constructor of this class.
	 * 
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
	 * Returns the current project.
	 * @return the project
	 */
	@XmlTransient
	public Project getProject() {
		return this.currProject;
	}

	/**
	 * Sets the current project to be unsaved.
	 * 
	 * @see SetupChangeEvent
	 * @param reason the new project unsaved
	 */
	private void setProjectUnsaved(Object reason) {
		if (this.currProject != null) {
			this.currProject.setChangedAndNotify(reason);
		}
	}

	/**
	 * Sets the time model settings.
	 * @param newTimeModelSettings the new time model settings
	 */
	public void setTimeModelSettings(HashMap<String, String> newTimeModelSettings) {
		this.timeModelSettings = newTimeModelSettings;
		this.setProjectUnsaved(SetupChangeEvent.TimeModelSettings);
	}
	/**
	 * Gets the time model settings.
	 * @return the time model settings
	 */
	@XmlTransient
	public HashMap<String, String> getTimeModelSettings() {
		if (this.timeModelSettings == null) {
			this.timeModelSettings = new HashMap<String, String>();
		}
		return this.timeModelSettings;
	}

	
	/**
	 * Returns the further project properties.
	 * @return the properties
	 */
	@XmlTransient
	public Properties getProperties() {
		if (properties==null) {
			properties = new Properties();
		}
		properties.addPropertiesListener(this);
		return properties;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.properties.PropertiesListener#onPropertiesEvent(de.enflexit.common.properties.PropertiesEvent)
	 */
	@Override
	public void onPropertiesEvent(PropertiesEvent propertiesEvent) {
		this.setProjectUnsaved(SetupChangeEvent.SetupProperties);
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, handling of the user runtime object ---------------------
	// ------------------------------------------------------------------------
	/**
	 * Sets the user runtime object.
	 * @param userRuntimeObject the userRuntimeObject to set
	 */
	public void setUserRuntimeObject(Serializable userRuntimeObject) {
		this.userRuntimeObject = userRuntimeObject;
		if (this.userRuntimeObject == null) {
			this.setUserRuntimeObjectClassName(null);
		} else {
			this.setUserRuntimeObjectClassName(this.userRuntimeObject.getClass().getName());
		}
		this.setProjectUnsaved(SetupChangeEvent.UserRuntimeObject);
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
	@XmlTransient
	public String getUserRuntimeObjectClassName() {
		if (userRuntimeObjectClassName == null && this.getUserRuntimeObject() != null) {
			userRuntimeObjectClassName = this.getUserRuntimeObject().getClass().getName();
		}
		return userRuntimeObjectClassName;
	}
	/**
	 * Sets the user runtime object class name.
	 * @param userRuntimeObjectClassName the new user runtime object class name
	 */
	public void setUserRuntimeObjectClassName(String userRuntimeObjectClassName) {
		this.userRuntimeObjectClassName = userRuntimeObjectClassName;
	}

	
	// ------------------------------------------------------------------------
	// --- From here, methods for saving and loading a SimulationSetup --------
	// ------------------------------------------------------------------------
	/**
	 * This method saves the current {@link SimulationSetup} and it files to the default location for setups.<br>
	 * <b>Note: </b> this methods only saves the files without further notification to the setups observer.
	 * @return true, if saving was successful
	 */
	public boolean saveSetupFiles() {
		File setupXmlFile = new File(this.currProject.getSimulationSetups().getCurrSimXMLFile());
		return this.saveSetupFiles(setupXmlFile, true);
	}

	/**
	 * This method saves the current {@link SimulationSetup} to the specified
	 * location. If the user runtime object should not be saved, set the according
	 * parameter.<br>
	 * <b>Note: </b> this methods only saves the files without further notification
	 * to the setups observer.
	 *
	 * @param setupXmlFile          The setup xml file (other file paths are
	 *                              derived)
	 * @param saveUserRuntimeObject set true, if user runtime object should be saved
	 *                              in its separate file
	 * @return true, if saving was successful
	 */
	public boolean saveSetupFiles(File setupXmlFile, boolean saveUserRuntimeObject) {

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
			if (saveUserRuntimeObject == true) {
				// --- ... as XML or bin file ? -----------
				boolean successXmlSave = this.saveUserObjectAsXmlFile(setupXmlFile);
				if (successXmlSave == false) {
					// --- Backup: save as bin file -------
					this.saveUserObjectAsBinFile(setupXmlFile);
				}
			}

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

		if (setupXmlFile == null) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The path for saving the setups user runtime object is not allowed to be null!");
			return false;
		}

		if (this.getUserRuntimeObject() == null) {
			successfulSaved = true;

		} else {
			try {
				FileOutputStream fos = null;
				ObjectOutputStream out = null;
				try {
					fos = new FileOutputStream(SimulationSetup.getSetupFile(setupXmlFile, SetupFileType.USER_OBJECT_BIN));
					out = new ObjectOutputStream(fos);
					out.writeObject(this.userRuntimeObject);
					successfulSaved = true;

				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					if (out != null)
						out.close();
					if (fos != null)
						fos.close();
				}

			} catch (IOException ioEx) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Error while saving the setups user runtime object as bin file:");
				ioEx.printStackTrace();
			}
		}
		return successfulSaved;
	}

	/**
	 * Returns the application events that are to be fired after a setup was loaded.
	 * @return the application events to fire
	 */
	public List<ApplicationEvent> getApplicationEventsToFire() {
		if (applicationEventsToFire == null) {
			applicationEventsToFire = new ArrayList<>();
		}
		return applicationEventsToFire;
	}

	/**
	 * Loads a {@link SimulationSetup} from the specified XML file.
	 *
	 * @param setupXmlFile          the setup xml file
	 * @param loadUserRuntimeObject the indicator to also load user runtime object
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
			// --- Fire application event ---------
			setup.getApplicationEventsToFire().add(new ApplicationEvent(ApplicationEvent.PROJECT_LOADING_SETUP_XML_FILE_LOADED, setup));

		} catch (JAXBException | IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		// --- If no setup was loaded return here -----------------------------
		if (setup == null)
			return null;

		// --- Load the user runtime object if specified ----------------------
		if (loadUserRuntimeObject == true) {
			SimulationSetup.loadSetupUserDataModel(setupXmlFile, setup);
		}

		// --- Initialize the agent lists -------------------------------------
		if (setup.initializeAgentStartListModels()==true) {
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
		if (successXmlLoad == false) {
			// --- Backup: load from bin file -------------
			SimulationSetup.loadUserObjectFromBinFile(xmlBaseFile, setup);

		} else {
			// --- Delete old bin file if available -------
			File binFileUserObject = SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_BIN);
			if (binFileUserObject.exists() == true) {
				boolean deleted = binFileUserObject.delete();
				if (deleted == false) {
					binFileUserObject.deleteOnExit();
				}
			}
		}
	}

	/**
	 * Loads the user object from a XML file.
	 *
	 * @param xmlBaseFile the xml base file
	 * @param setup       the setup
	 * @return true, if successful
	 */
	private static boolean loadUserObjectFromXmlFile(File xmlBaseFile, SimulationSetup setup) {

		boolean successfulLoaded = false;
		if (setup != null && setup.getUserRuntimeObjectClassName() != null) {
			try {
				File userObjectFile = SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_XML);
				Class<?> userRuntimeClass = ClassLoadServiceUtility.forName(setup.getUserRuntimeObjectClassName());
				AbstractUserObject userObject = AbstractUserObject.loadUserObjectFromXmlFile(userObjectFile,
						userRuntimeClass);
				if (userObject != null) {
					setup.setUserRuntimeObject(userObject);
					successfulLoaded = true;
					// --- Fire application event ---------
					setup.getApplicationEventsToFire().add(new ApplicationEvent(ApplicationEvent.PROJECT_LOADING_SETUP_USER_FILE_LOADED, setup));
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
	 * @param setup       the setup
	 * @return true, if successful
	 */
	private static boolean loadUserObjectFromBinFile(File xmlBaseFile, SimulationSetup setup) {

		boolean successfulLoaded = false;

		File userRuntimeObjectFile = SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_BIN);
		if (userRuntimeObjectFile.exists() == true) {

			FileInputStream fis = null;
			ObjectInputStreamForClassLoadService in = null;
			try {
				fis = new FileInputStream(userRuntimeObjectFile);
				in = new ObjectInputStreamForClassLoadService(fis, ClassLoadServiceUtility.class);
				Serializable userObject = (Serializable) in.readObject();
				setup.setUserRuntimeObject(userObject);
				successfulLoaded = true;
				// --- Fire application event ---------
				setup.getApplicationEventsToFire().add(new ApplicationEvent(ApplicationEvent.PROJECT_LOADING_SETUP_USER_FILE_LOADED, setup));

			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
					if (fis != null)
						fis.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return successfulLoaded;
	}

	// --------------------------------------------------------------
	// --- From here, some static help methods for file handling-----
	// --------------------------------------------------------------
	/**
	 * Returns, based on the specified base XML file, the specified setup file
	 * (especially for user runtime objects).
	 *
	 * @param xmlBaseFile   the XML base file
	 * @param setupFileType the setup file type
	 * @return the setup file
	 */
	public static File getSetupFile(File xmlBaseFile, SetupFileType setupFileType) {

		if (xmlBaseFile == null)
			return null;
		if (setupFileType == null)
			return null;
		if (setupFileType == SetupFileType.BASE_XML_FILE)
			return xmlBaseFile;

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
	 * Returns, based on the specified base XML file, all possible setup file (e.g.
	 * also for user runtime objects).
	 *
	 * @param xmlBaseFile the XML base file
	 * @return the setup file
	 */
	protected static List<File> getSetupFiles(File xmlBaseFile) {
		List<File> setupFiles = new ArrayList<>();
		setupFiles.add(xmlBaseFile);
		setupFiles.add(SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_BIN));
		setupFiles.add(SimulationSetup.getSetupFile(xmlBaseFile, SetupFileType.USER_OBJECT_XML));
		return setupFiles;
	}

	/**
	 * Returns the nominal base setup file from the specified file, but without
	 * guaranty that the file really exists.
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

		if (fileToCheckPath == null) {
			return null;
		}
		return new File(fileToCheckPath);
	}

	// ------------------------------------------------------------------------
	// --- From here, handling of different start lists for agents ------------
	// ------------------------------------------------------------------------
	/**
	 * Returns the combo box model agent start lists.
	 * 
	 * @return the comboBoxModel4AgentLists
	 */
	@XmlTransient
	public DefaultComboBoxModel<String> getComboBoxModel4AgentLists() {
		if (comboBoxModel4AgentLists == null) {
			comboBoxModel4AgentLists = new DefaultComboBoxModel<>();
		}
		return comboBoxModel4AgentLists;
	}

	/**
	 * Sets the ComboBoxModel for agent lists.
	 * 
	 * @param comboBoxModel4AgentLists the comboBoxModel4AgentLists to set
	 */
	public void setComboBoxModel4AgentLists(DefaultComboBoxModel<String> comboBoxModel4AgentLists) {
		this.comboBoxModel4AgentLists = comboBoxModel4AgentLists;
	}

	/**
	 * Sort ComboBoxModel for agent lists.
	 */
	private void sortComboBoxModel4AgentLists() {

		// --- Move the current entries to a Vector -----------------
		List<String> agentStartLists = new ArrayList<>();
		DefaultComboBoxModel<String> dlm = this.getComboBoxModel4AgentLists();
		for (int i = 0; i < dlm.getSize(); i++) {
			agentStartLists.add((String) dlm.getElementAt(i));
		}
		// --- If the default list is not there, create it ----------
		if (agentStartLists.contains(AGENT_LIST_ManualConfiguration) == false) {
			agentStartLists.add(AGENT_LIST_ManualConfiguration);
			this.getAgentDefaultListModel(new DefaultListModel<AgentClassElement4SimStart>(), AGENT_LIST_ManualConfiguration);
		}
		Collections.sort(agentStartLists);

		// --- Recreate JComboBoxModel for agent lists --------------
		this.setComboBoxModel4AgentLists(new DefaultComboBoxModel<String>());
		agentStartLists.forEach(agentStartList -> this.getComboBoxModel4AgentLists().addElement(agentStartList));
	}

	/**
	 * Returns the HashMap of {@link DefaultListModel}s for the agent start.
	 * 
	 * @return the agent list model hash map
	 */
	private HashMap<String, DefaultListModel<AgentClassElement4SimStart>> getAgentStartDefaultListModelHashMap() {
		if (hashMap4AgentDefaultListModels == null) {
			hashMap4AgentDefaultListModels = new HashMap<>();
		}
		return hashMap4AgentDefaultListModels;
	}

	/**
	 * This method can be used in order to get an agents start list for the
	 * simulation, given by.
	 *
	 * @param listName the list name
	 * @return the agentListModel
	 */
	public DefaultListModel<AgentClassElement4SimStart> getAgentDefaultListModel(String listName) {
		return this.getAgentStartDefaultListModelHashMap().get(listName);
	}

	/**
	 * This method can be used in order to add an individual agent start list to the SimulationSetup.<br>
	 * The list will be filled with elements of the type {@link AgentClassElement4SimStart} coming from the stored setup file and will
	 * be later on also stored in the file of the simulation setup.
	 *
	 * @see AgentClassElement4SimStart AgentClassElement4SimStart - The type to use within a list model
	 * 
	 * @param newDefaultListModel4AgentStarts the new {@link DefaultListModel} to use
	 * @param listName the name of the list to be assigned. Consider the use of one of the constants
	 *                 {@link #AGENT_LIST_ManualConfiguration} or {@link #AGENT_LIST_EnvironmentConfiguration}
	 *                 or just use an individual name
	 * @return the DefaultListModel of agents to be started for the specified list
	 */
	public DefaultListModel<AgentClassElement4SimStart> getAgentDefaultListModel(DefaultListModel<AgentClassElement4SimStart> newDefaultListModel4AgentStarts, String listName) {

		DefaultListModel<AgentClassElement4SimStart> dlm = this.getAgentDefaultListModel(listName);
		if (dlm == null) {
			dlm = newDefaultListModel4AgentStarts;
			this.setAgentDefaultListModel(listName, dlm);
		}
		this.checkForListDataListener(dlm);
		return dlm;
	}

	/**
	 * Here a complete agent start list (a DefaultListModel) can be added to the simulation setup.
	 * @param listName the list name
	 * @param defaultListModel4AgentStarts the default list model4 agent starts
	 */
	public void setAgentDefaultListModel(String listName, DefaultListModel<AgentClassElement4SimStart> defaultListModel4AgentStarts) {
		if (listName != null) {
			// --- Put to local HashMap -------------------
			this.getAgentStartDefaultListModelHashMap().put(listName, defaultListModel4AgentStarts);
			// --- Check for a listener -------------------
			this.checkForListDataListener(defaultListModel4AgentStarts);
			// --- Check combo box model ------------------
			if (this.getComboBoxModel4AgentLists().getIndexOf(listName) == -1) {
				this.getComboBoxModel4AgentLists().addElement(listName);
				this.sortComboBoxModel4AgentLists();
			}
		}
	}

	/**
	 * Clear the agent default list models.
	 */
	public void clearAgentDefaultListModels() {
		Vector<String> agentListNames = new Vector<String>(this.getAgentStartDefaultListModelHashMap().keySet());
		for (int i = 0; i < agentListNames.size(); i++) {
			this.getAgentStartDefaultListModelHashMap().get(agentListNames.get(i)).clear();
		}
	}

	/**
	 * Initializes the agent lists.
	 * 
	 * This method will fill all DefaultListModels that will be used within the visible application<br>
	 * as for example for the manual agent configuration (tab 'Simulation-Setup' =&gt; 'Agent-Start')<br>
	 * or the configuration of the environment model (tab 'Simulation-Setup' =&gt;
	 * 'Simulation Environment').<br>
	 * The resulting ListModels can be get by using {@link #getAgentDefaultListModel(String)}
	 * 
	 * @return true, if successful
	 */
	private boolean initializeAgentStartListModels() {

		try {
			// --- Rebuild the ComboBoxModel for all start lists --------------
			this.setComboBoxModel4AgentLists(new DefaultComboBoxModel<String>());
			
			// --- Pause local ListDataListener -------------------------------
			this.isPauseListDataListener = true;
			
			// --- Run through the list of all configured agents --------------
			ArrayList<AgentClassElement4SimStart> agentList = this.getAgentListBeforeChanges();
			for (int i = 0; i < agentList.size(); i++) {
				
				AgentClassElement4SimStart ace4ss = agentList.get(i);
				String memberOf = ace4ss.getListMembership();
				ace4ss.toString();  // .toSting will check if class exists ----
				
				DefaultListModel<AgentClassElement4SimStart> dlm = null;
				if (this.getAgentStartDefaultListModelHashMap().get(memberOf) == null) {
					dlm = new DefaultListModel<AgentClassElement4SimStart>();
					this.setAgentDefaultListModel(memberOf, dlm);
				} else {
					dlm = this.getAgentStartDefaultListModelHashMap().get(memberOf);
				}
				dlm.addElement(ace4ss);
			}
			// --- Check list data listener of all DefaultListModels ----------
			this.checkForListDataListener();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
			
		} finally {
			// --- Activate local ListDataListener again ----------------------
			this.isPauseListDataListener = false;
		}
		return true;
	}
	

	/**
	 * Returns the local List data listener for agent default list models.
	 * @return the list data listener for agent default list models
	 */
	private ListDataListener getListDataListenerForAgentDefaultListModels() {
		if (listDataListener4AgentDefaulListModels == null) {
			listDataListener4AgentDefaulListModels = new ListDataListener() {
				@Override
				public void intervalAdded(ListDataEvent lde) {
					if (isPauseListDataListener == true) return;
					if (isDebugListDataListener) System.out.println(SimulationSetup.this.getClass().getSimpleName() + " Addedd element ");
					SimulationSetup.this.fireSimulationSetupNotification(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_AGENT_ADDED));
				}

				@Override
				public void intervalRemoved(ListDataEvent lde) {
					if (isPauseListDataListener == true) return;
					if (isDebugListDataListener) System.out.println(SimulationSetup.this.getClass().getSimpleName() + " Removed element ");
					SimulationSetup.this.fireSimulationSetupNotification(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_AGENT_REMOVED));
				}

				@Override
				public void contentsChanged(ListDataEvent lde) {
					if (isPauseListDataListener == true) return;
					if (isDebugListDataListener) System.out.println(SimulationSetup.this.getClass().getSimpleName() + " Changed element ");
				}
			};
		}
		return listDataListener4AgentDefaulListModels;
	}
	/**
	 * Fires the specified simulation setup notification.
	 * @param simSetupNotification the SimulationSetupNotification to fire 
	 */
	private void fireSimulationSetupNotification(SimulationSetupNotification simSetupNotification) {

		if (simSetupNotification==null || simSetupNotification.getUpdateReason()==null) return;
		
		ArrayList<AgentClassElement4SimStart> newAgentList = this.getAgentList();
		
		ArrayList<AgentClassElement4SimStart> newAgentListWork = new ArrayList<>(newAgentList);
		ArrayList<AgentClassElement4SimStart> oldAgentListWork = new ArrayList<>(this.getAgentListBeforeChanges());
		switch (simSetupNotification.getUpdateReason()) {
		case SIMULATION_SETUP_AGENT_ADDED:
			// --- Find the elements added --------------------------
			newAgentListWork.removeAll(oldAgentListWork);
			simSetupNotification.setNotificationObject(newAgentList);
			this.setProjectUnsaved(simSetupNotification);
			break;

		case SIMULATION_SETUP_AGENT_REMOVED:
			// --- Find the elements removed ------------------------
			oldAgentListWork.removeAll(newAgentListWork);
			simSetupNotification.setNotificationObject(oldAgentListWork);
			this.setProjectUnsaved(simSetupNotification);
			break;

		default:
			break;
		}
		// --- Remind new state for later changes -----------------
		this.setAgentListBeforeChanges(newAgentList);
	}
	
	/**
	 * Checks in all available {@link DefaultListModel}s for the local {@link ListDataListener}. 
	 * If not added / registered yet, the method will add the local listener.
	 * 
	 * @see #getListDataListenerForAgentDefaultListModels()
	 */
	private void checkForListDataListener() {
		List<DefaultListModel<AgentClassElement4SimStart>> startListModels = new ArrayList<>(this.getAgentStartDefaultListModelHashMap().values());
		for (DefaultListModel<AgentClassElement4SimStart> startListModel : startListModels) {
			this.checkForListDataListener(startListModel);
		}
	}
	/**
	 * Checks in the specified {@link DefaultListModel}s for the local {@link ListDataListener}. 
	 * If not added / registered yet, the method will add the local listener.
	 * 
	 * @see #getListDataListenerForAgentDefaultListModels()
	 */
	private void checkForListDataListener(DefaultListModel<AgentClassElement4SimStart> defaultListModel) {
		if (Arrays.asList(defaultListModel.getListDataListeners()).contains(this.getListDataListenerForAgentDefaultListModels()) == false) {
			defaultListModel.addListDataListener(this.getListDataListenerForAgentDefaultListModels());
		}
	}

	
	/**
	 * Sets the agent list that is to be stored in the current simulation setup.
	 * @param agentList the agentList to set
	 */
	public void setAgentList(ArrayList<AgentClassElement4SimStart> agentList) {
		this.agentList = agentList;
		this.setProjectUnsaved(SetupChangeEvent.AgentConfiguration);
	}
	/**
	 * Return the current agent list that is to be stored in the simulation setup.
	 * @return the list of agents to be started
	 */
	@XmlTransient
	public ArrayList<AgentClassElement4SimStart> getAgentList() {
		// ----------------------------------------------------------
		// --- If start lists are defined, merge them to a ----------
		// --- new list. - Otherwise return what we have ------------
		// ----------------------------------------------------------
		if (this.getAgentStartDefaultListModelHashMap().size()>0) {
			this.mergeAgentListModels();
		}
		if (agentList == null) {
			agentList = new ArrayList<>();
		}
		return agentList;
	}
	/**
	 * Will merge all default list models to one array list.
	 */
	private void mergeAgentListModels() {

		this.agentList = new ArrayList<AgentClassElement4SimStart>();

		// ----------------------------------------------------------
		// --- Write Data from list models to local variable --------
		List<String> startListNames = new ArrayList<>(this.getAgentStartDefaultListModelHashMap().keySet());
		Collections.sort(startListNames);
		startListNames.forEach(startListName -> this.addToAgentList(this.getAgentStartDefaultListModelHashMap().get(startListName)));
	}
	/**
	 * This Method transfers a DefaultListModel to the localArrayList 'agentList'
	 * which is a type of 'AgentClassElement4SimStart'.
	 *
	 * @param lm the DefaultListModel that has to be added to the overall agent list
	 */
	private void addToAgentList(DefaultListModel<AgentClassElement4SimStart> lm) {
		if (lm == null) return;
		for (int i = 0; i < lm.size(); i++) {
			this.agentList.add((AgentClassElement4SimStart) lm.get(i));
		}
	}

	
	/**
	 * Return the agent class element for the simulation start that corresponds to the specified agent name or <code>null</code>.
	 *
	 * @param agentName the agent name
	 * @return the AgentClassElement4SimStart
	 */
	public AgentClassElement4SimStart getAgentClassElement4SimStart(String agentName) {
		return this.getAgentClassElement4SimStart(this.getAgentList(), agentName);
	}
	/**
	 * Return the agent class element for the simulation start that corresponds to the specified agent name or <code>null</code>.
	 *
	 * @param agentList the agent list to search in 
	 * @param agentName the agent name
	 * @return the AgentClassElement4SimStart
	 */
	public AgentClassElement4SimStart getAgentClassElement4SimStart(List<AgentClassElement4SimStart> agentList, String agentName) {
		if (agentList==null || agentName==null || agentName.isBlank()==true) return null;
		for (AgentClassElement4SimStart ace4ss : agentList) {
			if (ace4ss.getStartAsName().equals(agentName)==true) return ace4ss; 
		}
		return null;
	}
	
	/**
	 * Checks if an agent name already exists in the current agent configuration.
	 *
	 * @param agentName2Check the agent name to check
	 * @return true, if the agent name already exists
	 */
	public boolean isAgentNameExists(String agentName2Check) {
		// --- Search for the agent name in the list 'agentList' --------------
		List<AgentClassElement4SimStart> agentList = this.getAgentList();
		for (int i = 0; i < agentList.size(); i++) {
			if (agentList.get(i).getStartAsName().equals(agentName2Check))
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

		// --- Get the current agent list -----------------
		List<AgentClassElement4SimStart> ace4ssList = this.getAgentList();
		// --- find a new name ----------------------------
		while (this.getAgentClassElement4SimStart(ace4ssList, newAgentName)!=null) {
			newAgentName = agentNameSuggestion + "_" + incrementNo;
			incrementNo++;
		}
		return newAgentName;
	}

	
	/**
	 * Renames the agent with the specified old name to the new name.
	 *
	 * @param oldAgentName the old agent name
	 * @param newAgentName the new agent name
	 * @return true, if successful
	 */
	public boolean renameAgent(String oldAgentName, String newAgentName) {

		if (oldAgentName==null || oldAgentName.isBlank()==true) return false;
		if (newAgentName==null || newAgentName.isBlank()==true) return false;
		
		// --- Find the element to rename -------
		AgentClassElement4SimStart ace4ss = this.getAgentClassElement4SimStart(oldAgentName);
		// --- Check if the new name is unused --
		if (this.isAgentNameExists(newAgentName)==false) {
			// --- Rename in list element -------
			ace4ss.setStartAsName(newAgentName);
			// --- Prepare info for observer ----
			HashMap<String, String> changed = new HashMap<>();
			changed.put("oldAgentName", oldAgentName);
			changed.put("newAgentName", newAgentName);
			this.setProjectUnsaved(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_AGENT_RENAMED, changed)); 
			return true;
		}
		return false;
	}

	
	/**
	 * Returns the agent list before changes (serves as reminder for class internal actions).
	 * @return the agent list before changes
	 */
	private ArrayList<AgentClassElement4SimStart> getAgentListBeforeChanges() {
		if (agentListBeforeChanges==null) {
			agentListBeforeChanges = new ArrayList<AgentClassElement4SimStart>(this.getAgentList());
		}
		return agentListBeforeChanges;
	}
	/**
	 * Sets the agent list before changes.
	 * @param newReminderList the new agent list before changes reminder
	 */
	private void setAgentListBeforeChanges(ArrayList<AgentClassElement4SimStart> newReminderList) {
		this.agentListBeforeChanges = newReminderList;
	}
	
}
