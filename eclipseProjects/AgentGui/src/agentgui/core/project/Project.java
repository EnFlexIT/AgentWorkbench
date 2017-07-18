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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.osgi.framework.Bundle;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.gui.ProjectWindow;
import agentgui.core.gui.components.JPanel4Visualisation;
import agentgui.core.gui.projectwindow.AgentClassLoadMetricsPanel;
import agentgui.core.gui.projectwindow.BaseAgents;
import agentgui.core.gui.projectwindow.Distribution;
import agentgui.core.gui.projectwindow.JadeSetupMTP;
import agentgui.core.gui.projectwindow.JadeSetupServices;
import agentgui.core.gui.projectwindow.OntologyTab;
import agentgui.core.gui.projectwindow.ProjectDesktop;
import agentgui.core.gui.projectwindow.ProjectInfo;
import agentgui.core.gui.projectwindow.ProjectResources;
import agentgui.core.gui.projectwindow.ProjectWindowTab;
import agentgui.core.gui.projectwindow.TabForSubPanels;
import agentgui.core.gui.projectwindow.simsetup.EnvironmentModelSetup;
import agentgui.core.gui.projectwindow.simsetup.StartSetup;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.ontologies.OntologyVisualisationHelper;
import agentgui.core.plugin.PlugIn;
import agentgui.core.plugin.PlugInLoadException;
import agentgui.core.plugin.PlugInNotification;
import agentgui.core.plugin.PlugInsLoaded;
import agentgui.core.resources.VectorOfProjectResources;
import agentgui.core.sim.setup.SimulationSetupNotification;
import agentgui.core.sim.setup.SimulationSetupNotification.SimNoteReason;
import agentgui.core.sim.setup.SimulationSetups;
import agentgui.core.update.VersionInformation;
import agentgui.core.webserver.DownloadServer;

/**
 * This is the class, which holds all necessary informations about a project.<br> 
 * In order to allow multiple access to the instance of Project, we designed it <br>
 * in the common <b>MVC pattern</b> (Model-View-Controller)<br>
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement public class Project extends Observable {

	// --- public statics --------------------------------------
	@XmlTransient public static final String PREPARE_FOR_SAVING = "ProjectPrepare4Saving";
	@XmlTransient public static final String SAVED = "ProjectSaved";
	@XmlTransient public static final String CHANGED_ProjectName = "ProjectName";
	@XmlTransient public static final String CHANGED_ProjectDescription = "ProjectDescription";
	@XmlTransient public static final String CHANGED_ProjectView = "ProjectView";
	@XmlTransient public static final String CHANGED_ProjectStartTab = "ProjectStartTab";
	@XmlTransient public static final String CHANGED_ProjectFolder= "ProjectFolder";
	
	@XmlTransient public static final String CHANGED_VersionInformation= "VersionInformation";
	@XmlTransient public static final String CHANGED_UpdateAutoConfiguration= "UpdateAutoConfiguration";
	@XmlTransient public static final String CHANGED_UpdateSite = "UpdateSite";
	@XmlTransient public static final String CHANGED_UpdateDateLastChecked= "UpdateDateLastChecked";
	
	@XmlTransient public static final String CHANGED_EnvironmentModelType= "EnvironmentModelType";
	@XmlTransient public static final String CHANGED_StartArguments4BaseAgent = "StartArguments4BaseAgents";
	@XmlTransient public static final String CHANGED_TimeModelClass = "TimeModelConfiguration";
	@XmlTransient public static final String CHANGED_ProjectOntology = "ProjectOntology";
	@XmlTransient public static final String CHANGED_ProjectResources = "ProjectResources";
	@XmlTransient public static final String CHANGED_JadeConfiguration = "JadeConfiguration";
	@XmlTransient public static final String CHANGED_DistributionSetup = "DistributionSetup";
	@XmlTransient public static final String CHANGED_RemoteContainerConfiguration = "RemoteContainerConfiguration";
	@XmlTransient public static final String CHANGED_UserRuntimeObject = "UserRuntimeObject";
	
	// --- Constant value in order to set the project view ----------
	@XmlTransient public static final String VIEW_Developer = "Developer";
	@XmlTransient public static final String VIEW_User = "User";
	@XmlTransient public static final String VIEW_Maximize = "MaximizeView";
	@XmlTransient public static final String VIEW_Restore = "RestoreView";
	@XmlTransient public static final String VIEW_TabsLoaded = "TabsLoaded";
	
	// --- Constants for the the agent distribution metric ----------
	@XmlTransient public static final String AGENT_METRIC_Reset = "AgentMetric_Reset";
	@XmlTransient public static final String AGENT_METRIC_ChangedDataSource = "AgentMetric_ChangedDataSource";
	@XmlTransient public static final String AGENT_METRIC_AgentDescriptionAdded = "AgentMetric_AgentDescriptionAdded";
	@XmlTransient public static final String AGENT_METRIC_AgentDescriptionEdited = "AgentMetric_AgentDescriptionEdited";
	@XmlTransient public static final String AGENT_METRIC_AgentDescriptionRemoved = "AgentMetric_AgentDescriptionRemoved";
	
	// --- Constants -------------------------------------------
	@XmlTransient private String defaultSubFolder4Setups   = "setups";
	@XmlTransient private String defaultSubFolderEnvSetups = "setupsEnv";
	@XmlTransient private final String[] defaultSubFolders = {defaultSubFolder4Setups, defaultSubFolderEnvSetups};
	

	/** The OSGI-bundle of the current project */  
	@XmlTransient private ProjectBundleLoader projectBundleLoader;
	
	/** This is the 'view' in the context of the mentioned MVC pattern */
	@XmlTransient private ProjectWindow projectWindow;
	/** This panel holds the instance of environment model display */
	@XmlTransient private JPanel4Visualisation visualisationTab4SetupExecution;
	/** This JDesktopPane that can be used as project desktop. */
	@XmlTransient private JDesktopPane projectDesktop;
	
	/** Indicates that the project is unsaved or not */
	@XmlTransient private boolean isUnsaved = false;

	@XmlTransient private String projectFolder;
	@XmlTransient private String projectFolderFullPath;
	
	// --- Variables saved within the project file -------------
	@XmlElement(name="projectName")				private String projectName;
	@XmlElement(name="projectDescription")		private String projectDescription;
	@XmlElement(name="projectStartTab")			private String projectStartTab;	
	@XmlElement(name="projectView")				private String projectView;			// --- View for developer or end-user ---

	@XmlElement(name="versionInformation")		private VersionInformation versionInformation;
	@XmlElement(name="updateAutoConfiguration")	private Integer updateAutoConfiguration;
	@XmlElement(name="updateSite")				private String updateSite;
	@XmlElement(name="updateDateLastChecked")	private Long updateDateLastChecked;
	
	@XmlElement(name="environmentModel")		private String environmentModelName;	
	
	
	/**
	 * This Vector holds the additional resources which are used for the current project 
	 * (external jar files or the binary folder of a development project)  
	 */
	@XmlElementWrapper(name = "projectResources")
	@XmlElement(name="projectResource")
	private VectorOfProjectResources projectResources;
	
	/**
	 * This Vector handles the list of resources which should be loadable in case of 
	 * distributed simulations. The idea is, that for example external jar-files can
	 * be distributed to a remote location, where such jar-files will be added automatically 
	 * to the ClassPath of the starting JVM.         
	 */
	@XmlTransient private Vector<String> downloadResources = new Vector<String>();
	
	/**
	 * This Vector will store the class names of the PlugIns which are used within the project
	 */
	@XmlElementWrapper(name="plugins")
	@XmlElement(name="className")
	private Vector<String> plugIns_Classes = new Vector<String>();
	/**
	 * This extended Vector will hold the concrete instances of the PLugIns loaded in this project 
	 */
	@XmlTransient private PlugInsLoaded plugInsLoaded = new PlugInsLoaded();
	@XmlTransient private boolean plugInVectorLoaded = false;
	
	/**
	 * This class is used for the management of the used Ontology's inside a project.
	 * It handles the concrete instances.
	 */
	@XmlTransient private OntologyVisualisationHelper ontologyVisualisationHelper;
	
	/**
	 * This Vector is used in order to store the class names of the used ontology's in the project file
	 */
	@XmlElementWrapper(name = "subOntologies")
	@XmlElement(name="subOntology")
	private Vector<String> subOntologies = new Vector<String>();
	
	/**
	 * This extended HashTable is used in order to save the relationship between an agent (agents class name)
	 * and the classes (also class names) which can be used as start-argument for the agents   
	 */
	@XmlElement(name="agentStartConfiguration")
	private AgentStartConfiguration agentStartConfiguration;
	
	/**
	 * This field manages the configuration of JADE (e. g. JADE-Port 1099 etc.)
	 */
	@XmlElement(name="jadeConfiguration")
	private PlatformJadeConfig jadeConfiguration = new PlatformJadeConfig();
	
	/** The distribution setup. */
	@XmlElement(name="distributionSetup")
	private DistributionSetup distributionSetup = new DistributionSetup();
	
	/** The agent class load metrics. */
	@XmlElement(name="agentClassLoadMetricsTable")
	private AgentClassLoadMetricsTable agentClassLoadMetricsTable;
	
	/**
	 * This field manages the configuration of remote container if needed
	 */
	@XmlElement(name="remoteContainerConfiguration")
	private RemoteContainerConfiguration remoteContainerConfiguration = new RemoteContainerConfiguration();
	
	/**
	 * This field can be used in order to provide customised objects during
	 * the runtime of a project. This will be not stored within the file 'agentgui.xml' 
	 */
	@XmlTransient 
	private Serializable userRuntimeObject;
	
	/**
	 * This attribute holds the instance of the currently selected SimulationSetup
	 */
	@XmlElement(name="simulationSetupCurrent")
	private String simulationSetupCurrent;
	/**
	 * This extended HashTable is used in order to store the SimulationsSetup's names 
	 * and their file names 
	 */
	private SimulationSetups simulationSetups;

	/**
	 * This model contains all known environment types of the application 
	 * and can be also used for tailored environment models / types
	 */
	@XmlTransient
	private DefaultComboBoxModel<EnvironmentType> environmentsComboBoxModel;
	
	/** The EnvironmentController of the project. */
	@XmlTransient 
	private EnvironmentController environmentController;
	
	/** Configuration settings for the TimeModel used in this Project */
	@XmlElement(name="timeModelClass")
	private String timeModelClass = null;
	/** The TimeModelController controls the display of the selected TimModel. */
	@XmlTransient
	private TimeModelController timeModelController;
	
	
	
	/**
	 * Default constructor for Project
	 */
	public Project() {
	
		// ----------------------------------------------------------
		// --- Fill the projects ComboBoxModel for environments ----- 
		Vector<EnvironmentType> appEnvTypes = Application.getGlobalInfo().getKnownEnvironmentTypes();
		for (int i = 0; i < appEnvTypes.size(); i++) {
			EnvironmentType envType = (EnvironmentType) appEnvTypes.get(i);
			this.getEnvironmentsComboBoxModel().addElement(envType);
		}
		// ----------------------------------------------------------
		
	};
	
	/**
	 * Loads and returns the project from the specified project sub-directory. Both files will be loaded (agentgui.xml and agentgui.bin).
	 * By loading, this method will also load external jar-resources by using the ClassLoader.
	 *
	 * @param projectSubDirectory the project sub directory
	 * @return the project
	 */
	public static Project load(String projectSubDirectory) {
		String projectFolder = Application.getGlobalInfo().getPathProjects(true) + projectSubDirectory + File.separator;
		return load(new File(projectFolder));
	}
	
	/**
	 * Loads and returns the project from the specified project sub-directory. Both files will be loaded (agentgui.xml and agentgui.bin).
	 * External jar resources will optionally be loaded.
	 *
	 * @param projectSubDirectory the project sub directory
	 * @param loadResources load external jar resources?
	 * @return the project
	 */
	public static Project load(String projectSubDirectory, boolean loadResources) {
		String projectFolder = Application.getGlobalInfo().getPathProjects(true) + projectSubDirectory + File.separator;
		return load(new File(projectFolder), loadResources);
	}
	/**
	 * Loads and returns the project from the specified directory. Both files will be loaded (agentgui.xml and agentgui.bin).
	 * By loading, this method will also load external jar-resources by using the ClassLoader.
	 *
	 * @param projectPath the project path
	 * @return the project
	 */
	public static Project load(File projectPath){
		return load(projectPath, true);
	}
	
	/**
	 * Loads and returns the project from the specified directory. Both files will be loaded (agentgui.xml and agentgui.bin).
	 * External jar resources will optionally be loaded.
	 *
	 * @param projectPath the project path
	 * @param loadResources load external jar resources?
	 * @return the project
	 */
	public static Project load(File projectPath, boolean loadResources) {
		
		Project project = null;
		
		// --- Get data model from file ---------------
		String XMLFileName = projectPath.getAbsolutePath() + File.separator + Application.getGlobalInfo().getFileNameProject();	
		String userObjectFileName = projectPath.getAbsolutePath() + File.separator + Application.getGlobalInfo().getFilenameProjectUserObject();
		String projectSubDirectory = projectPath.getParentFile().toPath().relativize(projectPath.toPath()).toString();
		
		// --- Does the file exists -------------------
		File xmlFile = new File(XMLFileName);
		if (xmlFile.exists()==false) {
			
			System.out.println(Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + " " + XMLFileName);
			Application.setStatusBar(Language.translate("Fertig"));
			
			String title = Language.translate("Projekt-Ladefehler!");
			String message = Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + "\n";
			message += XMLFileName;
			JOptionPane.showInternalMessageDialog(Application.getMainWindow().getJDesktopPane4Projects(), message, title, JOptionPane.WARNING_MESSAGE);
			return null;
		}
		
		// --- Read file 'agentgui.xml' ---------------
		FileReader fr = null;
		try {
			fr = new FileReader(XMLFileName);
			JAXBContext pc = JAXBContext.newInstance(Project.class);
			Unmarshaller um = pc.createUnmarshaller();
			project = (Project) um.unmarshal(fr);
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		} catch (JAXBException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (fr!=null) fr.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		// --- check/create default folders -----------
		project.setProjectFolder(projectSubDirectory);
		project.checkCreateSubFolders();
		
		// --- Load additional jar-resources ----------
		if (loadResources == true) {
			project.resourcesLoad();
		}
		
		// --- Reading the serializable user object of - 
		// --- the Project from the 'agentgui.bin' -----
		File userObjectFile = new File(userObjectFileName);
		if (userObjectFile.exists()) {
			
			FileInputStream fis = null;
			ObjectInputStream inStream = null;
			try {
				fis = new FileInputStream(userObjectFileName);
				inStream = new ObjectInputStream(fis);
				Serializable userObject = (Serializable) inStream.readObject();
				project.setUserRuntimeObject(userObject);
				
			} catch(IOException ex) {
				ex.printStackTrace();
			} catch(ClassNotFoundException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (inStream!=null) inStream.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				try {
					if (fis!=null) fis.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		
		return project;
	}
	
	/**
	 * Saves the current Project to the files 'agentgui.xml' and agentgui.bin.
	 * @return true, if successful
	 */
	public boolean save() {
		return this.save(new File(this.getProjectFolderFullPath()), true);
	}
	/**
	 * Saves the current Project to the files 'agentgui.xml' and agentgui.bin.
	 * @param projectPath the project path where the files have to be stored
	 * @return true, if successful
	 */
	public boolean save(File projectPath, boolean saveSetup) {
		// --- Save the current project -------------------
		Application.setStatusBar(this.projectName + ": " + Language.translate("speichern") + " ... ");
		this.setNotChangedButNotify(Project.PREPARE_FOR_SAVING);		
		
		try {			
			// --------------------------------------------
			// --- Prepare Context and Marshaller ---------
			JAXBContext pc = JAXBContext.newInstance(this.getClass()); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
			pm.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 
			//pm.marshal( this, System.out );
			// --- Write values to xml-File ---------------
			Writer pw = new FileWriter(projectPath + File.separator + Application.getGlobalInfo().getFileNameProject() );
			pm.marshal(this, pw);
			pw.close();
			
			// --- Save the userRuntimeObject into a different 
			// --- file as a serializable binary object.
			FileOutputStream fos = null;
			ObjectOutputStream out = null;
		    try {
		       fos = new FileOutputStream(projectPath + File.separator + Application.getGlobalInfo().getFilenameProjectUserObject());
		       out = new ObjectOutputStream(fos);
		       out.writeObject(this.userRuntimeObject);
		       
		    } catch(IOException ex) {
		    	ex.printStackTrace();
		    } finally {
		    	if (out!=null) out.close();
		    	if (fos!=null) fos.close();
		    }
		    
			// --- Save the current SimulationSetup -------
		    if (saveSetup==true) {
		    	this.getSimulationSetups().setupSave();
		    }
			
			this.setUnsaved(false);			

			// --- Notification ---------------------------
			this.setNotChangedButNotify(Project.SAVED);

		} catch (Exception e) {
			System.out.println("XML-Error while saving Project-File!");
			e.printStackTrace();
		}
		Application.setStatusBar("");
		return true;		
	}
	
	/**
	 * This method closes the current project. If necessary it will try to save the before.
	 * @return Returns true if saving was successful
	 */
	public boolean close() {
		
		// --- Close project? -----------------------------
		String msgHead = null;
		String msgText = null;
		Integer msgAnswer = 0;
		
		Application.setStatusBar(Language.translate("Projekt schließen") + " ...");
		if (this.isUnsaved()==true) {
			msgHead = Language.translate("Projekt '@' speichern?");
			msgHead = msgHead.replace( "'@'", "'" + projectName + "'");			
			msgText = Language.translate(
						"Das aktuelle Projekt '@' ist noch nicht gespeichert!" + Application.getGlobalInfo().getNewLineSeparator() + 
						"Möchten Sie es nun speichern ?");
			msgText = msgText.replace( "'@'", "'" + projectName + "'");
			
			if (Application.getMainWindow()!=null) {
				msgAnswer = JOptionPane.showInternalConfirmDialog (Application.getMainWindow().getContentPane(), msgText, msgHead, JOptionPane.YES_NO_CANCEL_OPTION );
				if (msgAnswer == JOptionPane.CANCEL_OPTION) {
					return false;
				} else if (msgAnswer == JOptionPane.YES_OPTION) {
					if (this.save()==false) return false;
				}
			} else {
				if (this.save()==false) return false;
			}
		}
		// --- ggf. noch Jade beenden ---------------------
		if (Application.getJadePlatform().stopAskUserBefore()==false) {
			return false;
		}

		// --- Clear/Dispose EnvironmentPanel -------------
		EnvironmentController envController = this.getEnvironmentController();
		if (envController!=null) {
			EnvironmentPanel envPanel = envController.getEnvironmentPanel();
			if (envPanel!=null) {
				envPanel.dispose();
			}
		}
		
		// --- Clear PlugIns ------------------------------
		this.plugInVectorRemove();
		// --- Clear ClassPath ----------------------------
		this.resourcesRemove();
		
		// --- Close Project ------------------------------
		ProjectsLoaded loadedProjects = Application.getProjectsLoaded();
		int Index = loadedProjects.getIndexByName(projectName); // --- Merker Index ---	
		if(Application.isOperatingHeadless() == false){
			getProjectWindow().dispose();
		}
		loadedProjects.remove(this);
		
		int nProjects = loadedProjects.count();
		if (nProjects > 0) {
			if ( Index+1 > nProjects ) Index = nProjects-1;  
			Application.setProjectFocused(loadedProjects.get(Index));
			Application.getProjectFocused().setFocus(true);
			Application.setTitelAddition( Application.getProjectFocused().projectName );
		} else {
			Application.setProjectFocused(null);
			Application.setTitelAddition("");
			if (Application.getMainWindow()!=null) {
				Application.getMainWindow().setCloseButtonPosition(false);
			}
		}
		Application.setStatusBar("");
		return true;
	}
	
	/**
	 * Returns the projects {@link ProjectBundleLoader}.
	 * @return the bundle loader
	 */
	private ProjectBundleLoader getProjectBundleLoader() {
		if (projectBundleLoader==null) {
			projectBundleLoader = new ProjectBundleLoader(this);
		}
		return projectBundleLoader;
	}
	/**
	 * Returns the vector of bundles that belong to this project.
	 * @return the bundles of the current project
	 */
	public Vector<Bundle> getBundles() {
		return this.getProjectBundleLoader().getBundleVector();
	}
	/**
	 * Returns the bundle names that belong to the this project.
	 * @return the bundle names
	 */
	public Vector<String> getBundleNames() {
		Vector<String> bundleNames = new Vector<>();
		for (Bundle bundle : this.getBundles()) {
			bundleNames.add(bundle.getSymbolicName());
		}
		if (bundleNames.size()==0) {
			bundleNames=null;
		}
		return bundleNames;
	}
	
	/**
	 * Sets the the Project configuration to be saved or unsaved.
	 * @param isUnsaved the new unsaved
	 */
	public void setUnsaved(boolean isUnsaved) {
		this.isUnsaved = isUnsaved;
	}
	/**
	 * Checks if the Project is unsaved.
	 * @return true, if is unsaved
	 */
	public boolean isUnsaved() {
		return isUnsaved;
	}

	/**
	 * Gets the list of simulation setups.
	 * @return the simulation setups
	 */
	@XmlElementWrapper(name = "simulationSetups")
	public SimulationSetups getSimulationSetups() {
		if (this.simulationSetups==null) {
			this.simulationSetups = new SimulationSetups(this, this.getSimulationSetupCurrent());
		}
		return this.simulationSetups;
	}

	/**
	 * Sets the simulation setup current.
	 * @param simulationSetupCurrent the new simulation setup current
	 */
	public void setSimulationSetupCurrent(String simulationSetupCurrent) {
		this.simulationSetupCurrent = simulationSetupCurrent;
	}
	/**
	 * Gets the simulation setup current.
	 * @return the simulation setup current
	 */
	@XmlTransient
	public String getSimulationSetupCurrent() {
		return simulationSetupCurrent;
	}

	// ----------------------------------------------------------------------------------
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	// --- Here we come with methods for (un-) load ProjectPlugIns --- Start ------------ 
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ----------------------------------------------------------------------------------	
	/**
	 * This method will load the ProjectPlugIns, which are configured for the
	 * current project (plugins_Classes). It will be executed only one time during 
	 * the 'ProjectsLoaded.add()' execution. After this no further functionality 
	 * can be expected. 
	 */
	public void plugInVectorLoad() {
		if (this.plugInVectorLoaded==false) {
			// --- load all plugins configured in 'plugIns_Classes' -----------
			for (int i=0; i < this.plugIns_Classes.size(); i++) {
				if (this.plugInLoad(this.plugIns_Classes.get(i), false)== false ) {
					System.err.println("Removed Plug-In entry for: '" + this.plugIns_Classes.get(i) + "'");
					this.plugIns_Classes.remove(i);
				}
			}
			this.plugInVectorLoaded = true;
		}
	}
	/**
	 * This method will remove/unload the plugins in 
	 * descending order of the Vector 'plugins_Loaded'.  
	 */
	private void plugInVectorRemove() {
		// --- unload/remove all plugins configured in 'plugIns_Loaded' -----------
		for (int i = this.getPlugInsLoaded().size(); i>0; i--) {
			this.plugInRemove(this.getPlugInsLoaded().get(i-1), false);
		}
		this.plugInVectorLoaded = false;
	}
	/**
	 * This method will reload the configured PlugIns
	 */
	public void plugInVectorReload() {
		// --- remove all loaded PlugIns ------------------
		this.plugInVectorRemove();
		// --- re-initialise the 'PlugInsLoaded'-Vector ---
		setPlugInsLoaded(new PlugInsLoaded());
		// --- load all configured PlugIns to the project -
		this.plugInVectorLoad();
		
	}
	/**
	 * Informs all PlugIn's that the setup was loaded.
	 */
	public void plugInVectorInformSetupLoaded() {
		for (PlugIn plugIn : this.getPlugInsLoaded()) {
			plugIn.update(this, new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_LOAD));
		}
	}
	/**
	 * This method loads a single PlugIn, given by its class reference
	 * @param pluginReference
	 */
	public boolean plugInLoad(String pluginReference, boolean add2PlugInReferenceVector) {
		
		String MsgHead = "";
		String MsgText = "";
			
		try {
			if (getPlugInsLoaded().isLoaded(pluginReference)==true) {
				// --- PlugIn can't be loaded because it's already there ------
				PlugIn ppi = getPlugInsLoaded().getPlugIn(pluginReference);
				
				MsgHead = Language.translate("Fehler - PlugIn: ") + " " + ppi.getClassReference() + " !" ;
				MsgText = Language.translate("Das PlugIn wurde bereits in das Projekt integriert " +
						"und kann deshalb nicht erneut hinzugefügt werden!");
				JOptionPane.showInternalMessageDialog( this.getProjectWindow(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
				return false;
				
			} else {
				// --- PlugIn can be loaded -----------------------------------
				PlugIn ppi = getPlugInsLoaded().loadPlugin(this, pluginReference);
				this.setNotChangedButNotify(new PlugInNotification(PlugIn.ADDED, ppi));
				if (add2PlugInReferenceVector==true) {
					this.plugIns_Classes.add(pluginReference);	
				}
			}
			
		} catch (PlugInLoadException err) {
			err.printStackTrace();
			return false;
		}		
		return true;
	}
	/**
	 * This method will unload and remove a single PlugIn. If removeFromProjectReferenceVector is set
	 * to true, the PlugIn-reference will be also removed from the list of PlugIns', which has to be 
	 * loaded with the project.
	 *  
	 * @param plugIn The PlugIn instance to be removed
	 * @param removeFromProjectReferenceVector 
	 */
	public void plugInRemove(PlugIn plugIn, boolean removeFromProjectReferenceVector) {
		this.getPlugInsLoaded().removePlugIn(plugIn);
		this.setNotChangedButNotify(new PlugInNotification(PlugIn.REMOVED, plugIn));
		if (removeFromProjectReferenceVector) {
			this.plugIns_Classes.remove(plugIn.getClassReference());
		}
	}
	
	/**
	 * Sets PlugInsLoaded, a Vector<PlugIn> that describes, which PlugIn's has to loaded.
	 * @param plugInsLoaded the PlugInsLoaded
	 */
	public void setPlugInsLoaded(PlugInsLoaded plugInsLoaded) {
		this.plugInsLoaded = plugInsLoaded;
	}
	/**
	 * Returns PlugInsLoaded, a Vector<PlugIn> that describes, which PlugIn's were loaded.
	 * @return the PlugInsLoaded
	 */
	@XmlTransient
	public PlugInsLoaded getPlugInsLoaded() {
		return plugInsLoaded;
	}
	// ----------------------------------------------------------------------------------
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// --- Here we come with methods for (un-) load ProjectPlugIns --- End --------------
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ----------------------------------------------------------------------------------
	

	/**
	 * Sets the download resources for this Project. This Vector represents the list of resources that 
	 * should be downloadable in case of distributed executions. The idea is, that for example external 
	 * jar-files can distributed to a remote location, where such jar-files will be added automatically
	 * to the ClassPath of the starting JVM.
	 * @see DownloadServer#setProjectDownloadResources(Project)
	 * @param downloadResources the new download resources
	 */
	public void setDownloadResources(Vector<String> downloadResources) {
		this.downloadResources = downloadResources;
	}
	/**
	 * Returns the download resources for the project. This Vector represents the list of resources that 
	 * should be downloadable in case of distributed executions. The idea is, that for example external 
	 * jar-files can distributed to a remote location, where such jar-files will be added automatically
	 * to the ClassPath of the starting JVM.
	 *
	 * @return the download resources
	 */
	public Vector<String> getDownloadResources() {
		return downloadResources;
	}

	/**
	 * This Procedure creates the default Project-Structure  for a new project. It creates the 
	 * default folders ('agents' 'ontology' 'envSetups' 'resources') and creates default files
	 * like the project ontology main class 'AgentGUIProjectOntology'
	 */
	public void createDefaultProjectStructure() {
		// --- create default folders --------------------- 
		this.checkCreateSubFolders();
	}
	/**
	 * Controls and/or creates whether the sub-folder-Structure exists 
	 * @return boolean true or false :-)
	 */	
	public boolean checkCreateSubFolders() {
		
		String newDirName = null;
		File file = null;
		boolean error = false;
		
		for (int i=0; i< this.defaultSubFolders.length; i++  ) {
			// --- Does the default folder exists ---------
			newDirName = this.getProjectFolderFullPath() + defaultSubFolders[i];
			file = new File(newDirName);
			if (file.isDirectory()==false) {
				// --- create directors -------------------
				if (file.mkdir() == false) {
					error = true;	
				}				
			}
		};
		
		// --- Indicate if successful or not --------------
		if (error==true) {
			return false;
		} else {
			return true;	
		}		
	}
	
	/**
	 * Allow change notifications within the observer pattern without necessarily saving such changes
	 * @param reason
	 */
	public void setNotChangedButNotify(Object reason) {
		setChanged();
		notifyObservers(reason);		
	}
	/**
	 * To prevent the closing of the project without saving
	 * @param reason
	 */
	public void setChangedAndNotify(Object reason) {
		setUnsaved(true);
		setChanged();
		notifyObservers(reason);		
	}
	
	/**
	 * Moves the requested Project-Window to the front
	 */
	public void setFocus(boolean forceClassPathReload) {
		
		if (forceClassPathReload) {
			// --- stop Jade ------------------------------
			if (Application.getJadePlatform().stopAskUserBefore()==false) {
				return;
			}
			// --- unload ClassPath -----------------------
			this.resourcesRemove();
		}
		
		this.getProjectWindow().moveToFront();
		Application.setTitelAddition(projectName);
		Application.setProjectFocused(this);
		Application.getProjectsLoaded().setProjectView();
		this.setMaximized();
		
		if (forceClassPathReload) {
			// --- ClassPath laden ------------------------
			this.resourcesLoad();	
		}
	}
	/**
	 * Maximise the Project-Window within the AgenGUI-Application
	 */
	public void setMaximized() {
		if (Application.getMainWindow()!=null) {
			// --- Validate the main application window -----------------
			Application.getMainWindow().validate();
			// --- Be sure that everything is there as needed ----------- 
			if (this.getProjectWindow()!=null && this.getProjectWindow().getParent()!=null) {
				// --- Maximise now -------------------------------------
				((BasicInternalFrameUI) getProjectWindow().getUI()).setNorthPane(null);
				DesktopManager dtm = Application.getMainWindow().getJDesktopPane4Projects().getDesktopManager();
				if (dtm!=null) {
					dtm.maximizeFrame(getProjectWindow());	
				}
			}
		}
	}
	
	/**
	 * Set method for the project name
	 * @param newProjectName the projectName to set
	 */
	public void setProjectName(String newProjectName) {
		projectName = newProjectName;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_ProjectName);
	}
	
	/**
	 * Returns the project name.
	 * @return the projectName
	 */
	@XmlTransient
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * Sets the project description.
	 * @param newProjectDescription the projectDescription to set
	 */
	public void setProjectDescription(String newProjectDescription) {
		projectDescription = newProjectDescription;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_ProjectDescription);
	}
	/**
	 * @return the projectDescription
	 */
	@XmlTransient
	public String getProjectDescription() {
		return projectDescription;
	}
	/**
	 * Sets the project start tab.
	 * @param projectStartTab the new project start tab
	 */
	public void setProjectStartTab(String projectStartTab) {
		this.projectStartTab = projectStartTab;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_ProjectStartTab);
	}
	/**
	 * Gets the project start tab.
	 * @return the project start tab
	 */
	@XmlTransient
	public String getProjectStartTab() {
		return projectStartTab;
	}
	
	// ---- The Projects directory information ----------------------
	/**
	 * Sets the current project folder.
	 * @param newProjectFolder the projectFolder to set
	 */
	@XmlTransient
	public void setProjectFolder(String newProjectFolder) {
		projectFolder = newProjectFolder;
		setChanged();
		notifyObservers(CHANGED_ProjectFolder);
	}
	/**
	 * Returns the current project folder that is one sub-directory name of the Agent.GUI's project directory.
	 * @return the projectFolder
	 */
	public String getProjectFolder() {
		return projectFolder;
	}
	/**
	 * Returns the absolute location of project directory as full path.
	 * @return the ProjectFolderFullPath
	 */
	public String getProjectFolderFullPath() {
		if (projectFolderFullPath==null) {
			projectFolderFullPath = Application.getGlobalInfo().getPathProjects(true) + projectFolder + File.separator;
		}
		return projectFolderFullPath;
	}
	/**
	 * Gets the projects temporary folder.
	 * @return the projects temporary folder
	 */
	public String getProjectTempFolderFullPath(){
		String tmpFolder = this.getProjectFolderFullPath() + "~tmp" + File.separator;
		File tmpFile = new File(tmpFolder);
		if (tmpFile.exists()==false) {
			tmpFile.mkdir();
		}
		tmpFile.deleteOnExit();
		return tmpFolder;
	}
	
	// --- Version and Update information ---------------------------
	/**
	 * Sets the projects {@link VersionInformation}.
	 * @param versionInformation the new project version information
	 */
	public void setVersionInformation(VersionInformation versionInformation) {
		this.versionInformation = versionInformation;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_VersionInformation);
	}
	/**
	 * Gets the projects {@link VersionInformation}.
	 * @return the project version information
	 */
	@XmlTransient
	public VersionInformation getVersionInformation() {
		if (versionInformation==null) {
			versionInformation = new VersionInformation();
			versionInformation.setMajorRevision(0);
			versionInformation.setMinorRevision(1);
			versionInformation.setBuild(1);
		}
		return versionInformation;
	}
	/**
	 * Sets the update auto configuration (1-3).
	 * @param updateAutoConfiguration the new update auto configuration
	 */
	public void setUpdateAutoConfiguration(Integer updateAutoConfiguration) {
		this.updateAutoConfiguration = updateAutoConfiguration;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_UpdateAutoConfiguration);

	}
	/**
	 * Returns the current auto-update configuration.
	 * @return the auto-update configuration
	 */
	@XmlTransient
	public Integer getUpdateAutoConfiguration() {
		if (updateAutoConfiguration==null) {
			updateAutoConfiguration = 0;
		}
		return updateAutoConfiguration;
	}
	/**
	 * Sets the update site.
	 * @param updateSite the new update site
	 */
	public void setUpdateSite(String updateSite) {
		this.updateSite = updateSite;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_UpdateSite);
	}
	/**
	 * Returns the update site.
	 * @return the update site
	 */
	@XmlTransient
	public String getUpdateSite() {
		if (updateSite==null) {
			updateSite = GlobalInfo.DEFAULT_UPDATE_SITE;
		}
		return updateSite;
	}
	/**
	 * Sets the date of the last update check.
	 * @param updateDateLastChecked the new date for the last update check
	 */
	public void setUpdateDateLastChecked(Long updateDateLastChecked) {
		this.updateDateLastChecked = updateDateLastChecked;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_UpdateDateLastChecked);
	}
	/**
	 * Returns the date of the last update check.
	 * @return the date of the last update check
	 */
	@XmlTransient
	public Long getUpdateDateLastChecked() {
		return updateDateLastChecked;
	}
	
	// --- Visualisation instances ----------------------------------
	/**
	 * Creates / Returns the project window with all {@link ProjectWindowTab}'s.
	 * @return the project window for the current project
	 */
	@XmlTransient
	public ProjectWindow getProjectWindow() {
		if (this.projectWindow==null) {
			this.projectWindow = new ProjectWindow(this);
			
			ProjectWindowTab pwt = null;
			// ------------------------------------------------
			// --- General Informations -----------------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Info"), null, null, new ProjectInfo(this), null);
			pwt.add();
			
			// ------------------------------------------------
			// --- Configuration ------------------------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Konfiguration"), null, null, new TabForSubPanels(this), null);
			pwt.add();
			this.projectWindow.registerTabForSubPanels(ProjectWindowTab.TAB_4_SUB_PANES_Configuration, pwt);
			
				// --- External Resources ---------------------
				pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Ressourcen"), null, null, new ProjectResources(this), Language.translate("Konfiguration"));
				pwt.add();
				// --- Used Ontologies ------------------------
				pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Ontologien"), null, null, new OntologyTab(this), Language.translate("Konfiguration"));
				pwt.add();
				// --- Project Agents -------------------------
				pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Agenten"), null, null, new BaseAgents(this), Language.translate("Konfiguration"));
				pwt.add();
				// --- JADE-Services --------------------------
				pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("JADE-Services"), null, null, new JadeSetupServices(this), Language.translate("Konfiguration"));
				pwt.add();
				// --- JADE-MTP configuration -----------------
				pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("JADE-Settings"), null, null, new JadeSetupMTP(this), Language.translate("Konfiguration"));
				pwt.add();
				// --- Distribution + Thresholds --------------
				pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Verteilung + Grenzwerte"), null, null, new Distribution(this), Language.translate("Konfiguration"));
				pwt.add();
				// --- Agent Load Metrics ---------------------
				pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, Language.translate("Agenten-Lastmetrik"), null, null, new AgentClassLoadMetricsPanel(this), Language.translate("Konfiguration"));
				pwt.add();
			
				
			// ------------------------------------------------
			// --- Simulations-Setup --------------------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate(ProjectWindowTab.TAB_4_SUB_PANES_Setup), null, null, new TabForSubPanels(this), null);
			pwt.add();
			this.projectWindow.registerTabForSubPanels(ProjectWindowTab.TAB_4_SUB_PANES_Setup, pwt);
			
				// --- start configuration for agents ---------
				pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Agenten-Start"), null, null, new StartSetup(this), Language.translate(ProjectWindowTab.TAB_4_SUB_PANES_Setup));
				pwt.add();
				// --- simulation environment -----------------
				pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION, Language.translate("Umgebungsmodell"), null, null, new EnvironmentModelSetup(this), Language.translate(ProjectWindowTab.TAB_4_SUB_PANES_Setup));
				pwt.add();
				

			// ------------------------------------------------
			// --- Visualisation ------------------------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION, Language.translate(ProjectWindowTab.TAB_4_RUNTIME_VISUALISATION), null, null, this.getVisualisationTab4SetupExecution(), null);
			pwt.add();
			
			// ------------------------------------------------
			// --- Project Desktop ----------------------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER, Language.translate("Projekt-Desktop"), null, null, new ProjectDesktop(this), null);
			pwt.add();
			
			// --- Expand the tree view -----------------------
			this.projectWindow.projectTreeExpand2Level(3, true);
			
		}
		return projectWindow;
	}

	/**
	 * Sets the project desktop in the ProjectWindow.
	 * @param projectDesktop the JDesktopPane usable as project desktop
	 */
	public void setProjectDesktop(JDesktopPane projectDesktop) {
		this.projectDesktop = projectDesktop;
	}
	/**
	 * Returns the project desktop for the current project. This JDesktopPane can be used in order 
	 * to allow further user interactions within the project by using individual JInternalFrames. 
	 * If frames are added to this desktop, the focus will be set to it.
	 * @return the project desktop
	 */
	@XmlTransient
	public JDesktopPane getProjectDesktop() {
		return projectDesktop;
	}

	/**
	 * Sets the new project view that is either {@link Project#VIEW_Developer} or {@link Project#VIEW_User}.
	 * @param newProjectView the new project view to set
	 */
	@XmlTransient
	public void setProjectView(String newProjectView) {
		if (newProjectView.equals(this.projectView)==false) {
			// --- Remind the start tab for the project ---
			DefaultMutableTreeNode oldStartNode = this.getProjectWindow().getStartTabNode();
			// --- Change view ----------------------------
			this.projectView = newProjectView;
			setUnsaved(true);
			setChanged();
			notifyObservers(CHANGED_ProjectView);	
			// --- Find and set the new start tab ---------
			DefaultMutableTreeNode newStartNode = this.getProjectWindow().getTreeNode(oldStartNode.getUserObject().toString());
			this.getProjectWindow().setStartTabNode(newStartNode);
		}
	}
	/**
	 * @return the projectView
	 */
	public String getProjectView() {
		if (this.projectView==null) {
			return "";
		} else {
			return this.projectView;	
		}		
	}

	/**
	 * Sets the new environment model name.
	 * @param newEnvironmentModelName the new environment model name
	 */
	@XmlTransient
	public void setEnvironmentModelName(String newEnvironmentModelName) {
		// --- Check if a new model is to set -----------------------
		boolean setNewEnvironmentModel = false;
		if (this.environmentModelName==null & newEnvironmentModelName==null) {
			setNewEnvironmentModel = false;
		} else {
			if (this.environmentModelName==null || newEnvironmentModelName==null) {
				setNewEnvironmentModel = true;
			} else {
				if (this.environmentModelName.equals(newEnvironmentModelName)==true) {
					setNewEnvironmentModel = false;
				} else {
					setNewEnvironmentModel = true;
				}
			}
		}
		
		// --- In case that a new environment model is to set -------
		if (setNewEnvironmentModel==true) {
			this.environmentModelName = newEnvironmentModelName;
			this.resetEnvironmentController();
			this.setUnsaved(true);
			setChanged();
			notifyObservers(CHANGED_EnvironmentModelType);	
		}
	}
	/**
	 * @return the environmentModel
	 */
	public String getEnvironmentModelName() {
		return environmentModelName;
	}
	/**
	 * @return the 'EnvironmentType' of the current EnvironmentModel
	 */
	public EnvironmentType getEnvironmentModelType() {
		return Application.getGlobalInfo().getKnownEnvironmentTypes().getEnvironmentTypeByKey(this.environmentModelName);
	}
	
	/**
	 * Gets the default environment setup folder
	 * @return The default environment setup folder
	 */
	public String getEnvSetupPath(){
		return this.getProjectFolderFullPath() + defaultSubFolderEnvSetups;
	}		
	
	/**
	 * @param newSubFolder4Setups the defaultSubFolderOntology to set
	 */
	public void setSubFolder4Setups(String newSubFolder4Setups) {
		this.defaultSubFolder4Setups = newSubFolder4Setups;
	}
	/**
	 * @return the defaultSubFolderSetups
	 */
	public String getSubFolder4Setups(boolean fullPath) {
		if (fullPath==true) {
			return getProjectFolderFullPath() + defaultSubFolder4Setups + File.separator;
		} else {
			return defaultSubFolder4Setups;	
		}	
	}
	
	/**
	 * @param newSubFolderEnvSetups the defaultSubFolderEnvSetups to set
	 */
	public void setSubFolderEnvSetups(String newSubFolderEnvSetups) {
		defaultSubFolderEnvSetups = newSubFolderEnvSetups;
	}
	/**
	 * @return the defaultSubFolderEnvSetups
	 */
	public String getSubFolderEnvSetups() {
		return defaultSubFolderEnvSetups;
	}

	
	/**
	 * Sets the reference Vector for the sub ontologies.
	 * @param subOntologies the new sub ontologies
	 */
	public void setSubOntologies(Vector<String> subOntologies) {
		this.subOntologies = subOntologies;
	}
	/**
	 * Returns the Vector of references to the used sub ontologies.
	 * @return the sub ontologies
	 */
	@XmlTransient
	public Vector<String> getSubOntologies() {
		return subOntologies;
	}
	/**
	 * Adds a new sub ontology to the current project  
	 * @param newSubOntology
	 */
	public void subOntologyAdd(String newSubOntology) {
		if (this.getSubOntologies().contains(newSubOntology)==false) {
			if (this.getOntologyVisualisationHelper().addSubOntology(newSubOntology)==true) {
				setUnsaved(true);
				setChanged();
				notifyObservers(CHANGED_ProjectOntology);
			}
		} 
	}
	/**
	 * Removes a new sub ontology from the current project ontology 
	 * @param subOntology2Remove
	 */
	public void subOntologyRemove(String subOntology2Remove) {
		this.getSubOntologies().remove(subOntology2Remove);
		this.getOntologyVisualisationHelper().removeSubOntology(subOntology2Remove);
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_ProjectOntology);
	}
	
	/**
	 * Returns the ontology visualisation helper for this project.
	 * @return the OntologyVisualisationHelper
	 */
	public OntologyVisualisationHelper getOntologyVisualisationHelper() {
		if (this.ontologyVisualisationHelper==null) {
			this.ontologyVisualisationHelper = new OntologyVisualisationHelper(this.getSubOntologies());
			this.setSubOntologies(this.ontologyVisualisationHelper.getSubOntologies());
		}
		return ontologyVisualisationHelper;
	}

	/**
	 * Sets the agent configuration.
	 * @param agentStartConfiguration the new agent configuration
	 */
	public void setAgentStartConfiguration(AgentStartConfiguration agentStartConfiguration) {
		this.agentStartConfiguration = agentStartConfiguration;
		this.setAgentStartConfigurationUpdated();
	}
	/**
	 * Returns the agent configuration.
	 * @return the agent configuration
	 */
	@XmlTransient
	public AgentStartConfiguration getAgentStartConfiguration() {
		if (this.agentStartConfiguration==null) {
			this.agentStartConfiguration = new AgentStartConfiguration();
			this.setAgentStartConfigurationUpdated();
		}
		return agentStartConfiguration;
	}
	/**
	 * Informs all observers about changes at the AgentConfiguration 'AgentConfig'
	 */
	public void setAgentStartConfigurationUpdated() {
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_StartArguments4BaseAgent);
	}

	/**
	 * Sets the time model class and configuration for this project.
	 * @param timeModelClass the new time model class
	 */
	public void setTimeModelClass(String timeModelClass) {
		this.timeModelClass = timeModelClass;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_TimeModelClass);
	}
	/**
	 * Returns the time model class and configuration for this Project.
	 * @return the time model class
	 */
	@XmlTransient
	public String getTimeModelClass() {
		return timeModelClass;
	}

	/**
	 * Returns the TimeModelController of this Project.
	 * @return the TimeModelController
	 */
	@XmlTransient
	public TimeModelController getTimeModelController() {
		if (this.timeModelController==null) {
			this.timeModelController = new TimeModelController(this);
		}
		return timeModelController;
	}

	/**
	 * Sets the project resources.
	 * @param projectResources the projectResources to set
	 */
	public void setProjectResources(VectorOfProjectResources projectResources) {
		this.projectResources = projectResources;
	}
	/**
	 * Returns the project resources.
	 * @return the projectResources
	 */
	@XmlTransient
	public VectorOfProjectResources getProjectResources() {
		if (this.projectResources==null) {
			this.projectResources = new VectorOfProjectResources();
		}
		return projectResources;
	}
	
	/**
	 * Tries to retrieve a bin-folder location given form an external resource to load for a project.
	 * @param resourcePath the resource path
	 * @return the same string if nothing is found, otherwise the new resource path
	 */
	private String retrievBinResourceFromPath(String resourcePath) {
		
		// --- Get Agent.GUI base directory and walk up to parent folders ----
		File searchDir = new File(Application.getGlobalInfo().getPathBaseDir());
		for (int i=0; i<2; i++) {
			if (searchDir!=null) {
				searchDir = searchDir.getParentFile();
			} else {
				break;
			}
		}
		
		// --- Directory found? -----------------------------------------------
		if (searchDir!=null) {
			// --- Set the string for the search path -------------------------
			String searchPath = searchDir.getAbsolutePath() + File.separator;
			// --- Examine the give resource path -----------------------------			
			String checkAddition = null;
			String[] jarFileCorrectedFragments = resourcePath.split("\\"+File.separator);
			// --- Try to rebuild the resource path ---------------------------
			for (int i=(jarFileCorrectedFragments.length-1); i>0; i--) {
				String fragment = jarFileCorrectedFragments[i];
				if (fragment.equals("")==false) {
					if (i==(jarFileCorrectedFragments.length-1)) {
						checkAddition = fragment;	
					} else {
						checkAddition = fragment + File.separator + checkAddition;
					}
					 
					File checkPath = new File(searchPath + checkAddition);	
					if (checkPath.exists()==true) {
						// --- Path found ! -----------------------------------
						return checkPath.getAbsolutePath();
					}	
				}
			}
		}
		return resourcePath;
	}
	
	
	/**
	 * This method adds external project resources (*.jar-files) to the CLATHPATH  
	 */
	public void resourcesLoad() {

		try {
			this.getProjectBundleLoader().installAndStartBundles();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
//		// TODO
//		for (int i=0; i<this.getProjectResources().size(); i++) {
//			
//			String jarFile4Display = this.getProjectResources().get(i);
//			jarFile4Display = PathHandling.getPathName4LocalSystem(jarFile4Display);
//			
//			String prefixText = null;
//			String suffixText = null;
//			try {
//				
//				String jarFileCorrected = ClassLoaderUtil.adjustPathForLoadIn(jarFile4Display, this.getProjectFolderFullPath());
//				File file = new File(jarFileCorrected);
//				if (file.exists()==false && jarFileCorrected.toLowerCase().endsWith("jar")==false) {
//					// --- Try to find / retrieve bin resource ------ 
//					String jarFileCorrectedNew = this.retrievBinResourceFromPath(jarFileCorrected);
//					if (jarFileCorrectedNew.equals(jarFileCorrected)==false) {
//						// --- Found new bin resource ---------------
//						System.out.println("=> Retrieved new location for resource path '" + jarFileCorrected + "'");
//						System.out.println("=> Corrected it to '" + jarFileCorrectedNew + "'");
//						this.getProjectResources().set(i, jarFileCorrectedNew);
//						
//						jarFile4Display = jarFileCorrectedNew;
//						jarFileCorrected = jarFileCorrectedNew;
//						file = new File(jarFileCorrected);	
//					}
//				}
//				
//				if (file.exists()==false) {
//					// --- Definitely no file found -----------------
//					prefixText = "ERROR";
//					suffixText = Language.translate("Datei nicht gefunden!");
//					
//				} else if (file.isDirectory()) {
//					// --- Folder found: Build a temporary *.jar ----
//					prefixText = "";
//					suffixText = "proceeding started";
//					
//					// --- Prepare the path variables -----
//					String pathBin = file.getAbsolutePath();
//					String pathBinHash = ((Integer)pathBin.hashCode()).toString();
//					String jarArchiveFileName = "BIN_DUMP_" + pathBinHash + ".jar";
//					String jarArchivePath = this.getProjectTempFolderFullPath() + jarArchiveFileName;
//					
//					// --- Create the jar-file ------------
//					JarFileCreator jarCreator = new JarFileCreator(pathBin);
//					File jarArchiveFile = new File(jarArchivePath);
//					jarCreator.createJarArchive(jarArchiveFile);
//					jarArchiveFile.deleteOnExit();
//					
//					// --- Add to the class loader --------
////					URL url = new URL("file:/" + jarArchiveFile.getAbsolutePath());
////					this.getProjectClassLoader().addURL(url);
//					
//					ClassLoaderUtil.addFile(jarArchiveFile.getAbsoluteFile());
//					ClassLoaderUtil.addJarToClassPath(jarArchivePath);
//					
//					// --- prepare the notification -------
//					SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
//					String dateText = dateFormat.format(new Date());
//					
//					prefixText = "OK";
//					suffixText = Language.translate("Verzeichnis gepackt zu") + " '" + File.separator + "~tmp" + File.separator + jarArchiveFileName + "' " + Language.translate("um") + " " + dateText;
//					
//				} else {
//					// --- Load the given jar-file ------------------
////					URL url = new URL("file:/" + jarFile4Display);
////					this.getProjectClassLoader().addURL(url);
////					
//					ClassLoaderUtil.addFile(file.getAbsoluteFile());
//					ClassLoaderUtil.addJarToClassPath(jarFileCorrected);
//					
//					prefixText = "OK";
//					suffixText = null;
//					
//				}
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//				prefixText = "ERROR";
//				suffixText = e.getMessage();
//			}
//			
//			// --- On Error print it to console -----------
//			if (prefixText.equals("ERROR")==true) {
//				System.err.println("=> " + suffixText + " - " + jarFile4Display);
//			}
//			// --- Set the additional text ----------------
//			this.getProjectResources().setPrefixText(jarFile4Display, prefixText);
//			this.getProjectResources().setSuffixText(jarFile4Display, suffixText);
//			
//		}

		this.setChangedAndNotify(CHANGED_ProjectResources);
	}
	/**
	 * This Method reloads the project resources in the ClassPath
	 */
	public void resourcesReLoad() {
		this.resourcesRemove();
		this.resourcesLoad();
	}
	
	/**
	 * This method removes all external project resources (jars) from the ClassPath  
	 */
	public void resourcesRemove() {
		
		this.getProjectBundleLoader().stopAndUninstallBundles();
		
//		for(String jarFile : getProjectResources()) {
//			
//			try {
//				String jarFileCorrected = ClassLoaderUtil.adjustPathForLoadIn(jarFile, this.getProjectFolderFullPath());
//				File file = new File(jarFileCorrected);
//				if (file.isDirectory()) {
//					// --- Prepare the path variables -----
//					String pathBin = file.getAbsolutePath();
//					String pathBinHash = ((Integer)pathBin.hashCode()).toString();
//					String jarArchiveFileName = "BIN_DUMP_" + pathBinHash + ".jar";
//					String jarArchivePath = this.getProjectTempFolderFullPath() + jarArchiveFileName;
//					
//					ClassLoaderUtil.removeFile(jarArchivePath);
//
//					File jarArchiveFile = new File(jarArchivePath);
//					jarArchiveFile.delete();
//					
//				} else {
//					ClassLoaderUtil.removeFile(jarFileCorrected);
//					
//				}
//				
//			} catch (RuntimeException e1) {
//				e1.printStackTrace();
//			} catch (NoSuchFieldException e1) {
//				e1.printStackTrace();
//			} catch (IllegalAccessException e1) {
//				e1.printStackTrace();
//			}
//		}
		this.setChangedAndNotify(CHANGED_ProjectResources);
	}

	/**
	 * Sets the jade configuration.
	 * @param jadeConfiguration the new jade configuration
	 */
	public void setJadeConfiguration(PlatformJadeConfig jadeConfiguration) {
		this.jadeConfiguration = jadeConfiguration;
	}
	/**
	 * Gets the jade configuration.
	 * @return the jade configuration
	 */
	@XmlTransient
	public PlatformJadeConfig getJadeConfiguration() {
		if (this.jadeConfiguration==null) {
			this.jadeConfiguration = new PlatformJadeConfig();
		}
		if (this.jadeConfiguration.getProject()==null) {
			this.jadeConfiguration.setProject(this);
		}
		return this.jadeConfiguration;
	}

	/**
	 * Gets the distribution setup.
	 * @return the distributionSetup
	 */
	@XmlTransient
	public DistributionSetup getDistributionSetup() {
		if (distributionSetup==null) {
			distributionSetup = new DistributionSetup();
		}
		return distributionSetup;
	}
	/**
	 * Sets the distribution setup.
	 * @param distributionSetup the distributionSetup to set
	 */
	public void setDistributionSetup(DistributionSetup distributionSetup) {
		this.distributionSetup = distributionSetup;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_DistributionSetup);
	}

	/**
	 * @return the remoteContainerConfiguration
	 */
	@XmlTransient
	public RemoteContainerConfiguration getRemoteContainerConfiguration() {
		return remoteContainerConfiguration;
	}	
	/**
	 * @param remoteContainerConfiguration the remoteContainerConfiguration to set
	 */
	public void setRemoteContainerConfiguration(RemoteContainerConfiguration remoteContainerConfiguration) {
		this.remoteContainerConfiguration = remoteContainerConfiguration;
		this.setChangedAndNotify(CHANGED_RemoteContainerConfiguration);
	}
	
	/**
	 * Gets the agent class load metrics.
	 * @return the agent class load metrics
	 */
	@XmlTransient
	public AgentClassLoadMetricsTable getAgentClassLoadMetrics() {
		if (agentClassLoadMetricsTable==null) {
			agentClassLoadMetricsTable = new AgentClassLoadMetricsTable(this);
		}
		if (agentClassLoadMetricsTable.getProject()==null) {
			agentClassLoadMetricsTable.setProject(this);
		}
		return agentClassLoadMetricsTable;
	}
	/**
	 * Sets the agent load metrics.
	 * @param agentClassLoadMetricsTable the new agent load metrics
	 */
	public void setAgentClassLoadMetrics(AgentClassLoadMetricsTable agentClassLoadMetricsTable) {
		this.agentClassLoadMetricsTable = agentClassLoadMetricsTable;
		this.setChangedAndNotify(AGENT_METRIC_Reset);
	}
	
	/**
	 * Returns the user runtime object.
	 * @return the userRuntimeObject, any kind of serializable object
	 */
	@XmlTransient
	public Object getUserRuntimeObject() {
		return userRuntimeObject;
	}
	/**
	 * Can be used in order to set any kind of runtime object that it is serializable by Java.
	 * @param userRuntimeObject the userRuntimeObject to set
	 */
	public void setUserRuntimeObject(Serializable userRuntimeObject) {
		this.userRuntimeObject = userRuntimeObject;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_UserRuntimeObject);
	}

	/**
	 * Returns the EnvironmentController of the project - an extended class of {@link EnvironmentController}.
	 * @return the current environment controller 
	 */
	@XmlTransient
	public EnvironmentController getEnvironmentController() {
		if (environmentController==null) {

			EnvironmentType envType = this.getEnvironmentModelType();
			Class<? extends EnvironmentController> envControllerClass = envType.getEnvironmentControllerClass();
			if (envControllerClass==null) {
				// --- If NO environment is specified -------------------
				return null;
			} 

			// --- If an environment IS specified -----------------------
			try {
				
				Class<?>[] conParameter = new Class[1];
				conParameter[0] = Project.class;
				Constructor<?> envControllerConstructor = envControllerClass.getConstructor(conParameter);

				// --- Define the argument for the newInstance call ----- 
				Object[] args = new Object[1];
				args[0] = this;
				environmentController = (EnvironmentController) envControllerConstructor.newInstance(args);
				
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			
		}
		return environmentController;
	}
	/**
	 * Resets the current EnvironmentController of the project - an extended {@link EnvironmentController}. 
	 */
	public void resetEnvironmentController() {
		this.environmentController = null;
		this.getEnvironmentController();
	}

	/**
	 * Returns the current ComboBoxModel for environment types.
	 * @return the environmentsComboBoxModel
	 */
	@XmlTransient
	public DefaultComboBoxModel<EnvironmentType> getEnvironmentsComboBoxModel() {
		if (environmentsComboBoxModel==null) {
			environmentsComboBoxModel = new DefaultComboBoxModel<EnvironmentType>();
		}
		return environmentsComboBoxModel;
	}
	/**
	 * Sets the ComboBoxModel for environment types.
	 * @param environmentsComboBoxModel the environmentsComboBoxModel to set
	 */
	public void setEnvironmentsComboBoxModel(DefaultComboBoxModel<EnvironmentType> environmentsComboBoxModel) {
		this.environmentsComboBoxModel = environmentsComboBoxModel;
	}

	/**
	 * Sets the visualisation tab of the {@link ProjectWindow} for an executed setup.
	 * @param visualisationTab4SetupExecution the new visualisation tab4 setup execution
	 */
	public void setVisualisationTab4SetupExecution(JPanel4Visualisation visualisationTab4SetupExecution) {
		this.visualisationTab4SetupExecution = visualisationTab4SetupExecution;
	}
	/**
	 * Returns the visualisation tab of the {@link ProjectWindow} for an executed setup.
	 * @return the visualisation tab4 setup execution
	 */
	@XmlTransient
	public JPanel4Visualisation getVisualisationTab4SetupExecution() {
		if (this.visualisationTab4SetupExecution==null) {
			this.visualisationTab4SetupExecution = new JPanel4Visualisation(this, Language.translate(ProjectWindowTab.TAB_4_RUNTIME_VISUALISATION)); 
		}
		return this.visualisationTab4SetupExecution;
	}

	/**
	 * Does the project update.
	 */
	public void doProjectUpdate() {
		// TODO Auto-generated method stub
	}


}
