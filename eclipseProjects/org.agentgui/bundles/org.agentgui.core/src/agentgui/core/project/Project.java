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

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.agentgui.gui.ProjectEditorWindow;
import org.agentgui.gui.ProjectEditorWindow.ProjectCloseUserFeedback;
import org.agentgui.gui.UiBridge;
import org.agentgui.gui.swing.project.ProjectWindow;
import org.agentgui.gui.swing.project.ProjectWindowTab;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.osgi.framework.Bundle;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.CommonComponentFactory;
import agentgui.core.config.GlobalInfo.ExecutionEnvironment;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.gui.components.JPanel4Visualisation;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.plugin.PlugIn;
import agentgui.core.plugin.PlugInLoadException;
import agentgui.core.plugin.PlugInNotification;
import agentgui.core.plugin.PlugInsLoaded;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import agentgui.core.project.setup.SimulationSetups;
import agentgui.core.project.transfer.ProjectExportController;
import agentgui.core.project.transfer.ProjectExportSettings;
import agentgui.core.update.VersionInformation;
import de.enflexit.common.classLoadService.ObjectInputStreamForClassLoadService;
import de.enflexit.common.ontology.AgentStartConfiguration;
import de.enflexit.common.ontology.OntologyVisualisationHelper;
import de.enflexit.common.p2.P2OperationsHandler;

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
	@XmlTransient private ProjectEditorWindow projectEditorWindow;
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
	@XmlElement(name = "projectResource")
	private ProjectResourceVector projectResources;
	@XmlElement(name = "reCreateProjectManifest")
	private boolean reCreateProjectManifest = true;

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
	@XmlElementWrapper(name = "plugins")
	@XmlElement(name = "className")
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
	@XmlElement(name = "subOntology")
	private Vector<String> subOntologies = new Vector<String>();

	/**
	 * This extended HashTable is used in order to save the relationship between an agent (agents class name)
	 * and the classes (also class names) which can be used as start-argument for the agents   
	 */
	@XmlElement(name = "agentStartConfiguration")
	private AgentStartConfiguration agentStartConfiguration;

	/**
	 * This field manages the configuration of JADE (e. g. JADE-Port 1099 etc.)
	 */
	@XmlElement(name = "jadeConfiguration")
	private PlatformJadeConfig jadeConfiguration = new PlatformJadeConfig();

	/** The distribution setup. */
	@XmlElement(name = "distributionSetup")
	private DistributionSetup distributionSetup = new DistributionSetup();

	/** The agent class load metrics. */
	@XmlElement(name = "agentClassLoadMetricsTable")
	private AgentClassLoadMetricsTable agentClassLoadMetricsTable;

	/**
	 * This field manages the configuration of remote container if needed
	 */
	@XmlElement(name = "remoteContainerConfiguration")
	private RemoteContainerConfiguration remoteContainerConfiguration = new RemoteContainerConfiguration();

	/**
	 * This field can be used in order to provide customised objects during
	 * the runtime of a project. This will be not stored within the file 'agentgui.xml' 
	 */
	@XmlTransient private Serializable userRuntimeObject;

	/**
	 * This attribute holds the instance of the currently selected SimulationSetup
	 */
	@XmlElement(name = "simulationSetupCurrent")
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
	@XmlTransient private DefaultComboBoxModel<EnvironmentType> environmentsComboBoxModel;

	/** The EnvironmentController of the project. */
	@XmlTransient private EnvironmentController environmentController;

	/** Configuration settings for the TimeModel used in this Project */
	@XmlElement(name="timeModelClass") 	private String timeModelClass = null;
	/** The TimeModelController controls the display of the selected TimModel. */
	@XmlTransient private TimeModelController timeModelController;


	@XmlTransient private MApplication eclipseMApplication;
	@XmlTransient private EPartService eclipseEPartService;
	@XmlTransient private EModelService eclipseEModelService;
	
	/**
	 * This Vector is used in order to store the class names of the used ontology's in the project file
	 */
	@XmlElementWrapper(name = "projectFeatures")
	@XmlElement(name = "projectFeature")
	private Vector<FeatureInfo> projectFeatures = new Vector<FeatureInfo>();
	
	

	/**
	 * Instantiates a new project.
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
	}

	/**
	 * Loads and returns the project from the specified project sub-directory. Both files will be loaded (agentgui.xml and agentgui.bin).
	 * By loading, this method will also load external jar-resources by using the ClassLoader.
	 *
	 * @param projectSubDirectory the project sub directory
	 * @return the project
	 */
	public static Project load(String projectSubDirectory) {
		String projectFolder = Application.getGlobalInfo().getPathProjects() + projectSubDirectory + File.separator;
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
		String projectFolder = Application.getGlobalInfo().getPathProjects() + projectSubDirectory + File.separator;
		return load(new File(projectFolder), loadResources);
	}

	/**
	 * Loads and returns the project from the specified directory. Both files will be loaded (agentgui.xml and agentgui.bin).
	 * By loading, this method will also load external jar-resources by using the ClassLoader.
	 *
	 * @param projectPath the project path
	 * @return the project
	 */
	public static Project load(File projectPath) {
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

		String projectSubDirectory = projectPath.getParentFile().toPath().relativize(projectPath.toPath()).toString();

		// --- Load the XML file of the project ----------
		Project project = loadProjectXml(projectPath);
		
		// --- Check/create default folders ---------------
		project.setProjectFolder(projectSubDirectory);
		project.checkAndCreateProjectsDirectoryStructure();
		
		// --- Install required features if necessary -----
		if (Application.getGlobalInfo().getExecutionEnvironment() == ExecutionEnvironment.ExecutedOverProduct) {
			// --- Only possible if not running from the IDE --
			loadRequiredFeatures(project);
		}
		
		// --- Load additional jar-resources --------------
		if (loadResources==true) {
			project.resourcesLoad();
		}
		
		// --- Load user data model -----------------------
		loadProjectUserDataModel(projectPath, project);
		
		return project;
	}
	
	/**
	 * Loads the projects XML file.
	 *
	 * @param projectPath the project path
	 * @return the project
	 */
	public static Project loadProjectXml(File projectPath) {
		
		Project project = null;

		// --- Get data model from file ---------------
		String xmlFileName = projectPath.getAbsolutePath() + File.separator + Application.getGlobalInfo().getFileNameProject();	

		// --- Does the file exists -------------------
		File xmlFile = new File(xmlFileName);
		if (xmlFile.exists() == false) {

			System.out.println(Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + " " + xmlFileName);
			Application.setStatusBar(Language.translate("Fertig"));

			String title = Language.translate("Projekt-Ladefehler!");
			String message = Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + "\n";
			message += xmlFileName;
			JOptionPane.showInternalMessageDialog(Application.getMainWindow().getJDesktopPane4Projects(), message, title, JOptionPane.WARNING_MESSAGE);
			return null;
		}

		// --- Read file 'agentgui.xml' ---------------
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(xmlFileName);
			JAXBContext pc = JAXBContext.newInstance(Project.class);
			Unmarshaller um = pc.createUnmarshaller();
			project = (Project) um.unmarshal(fileReader);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		} catch (JAXBException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (fileReader!=null) fileReader.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return project;
	}
	
	/**
	 * Loads features required by the project from the p2 repository if necessary.
	 *
	 * @param project the project
	 */
	private static void loadRequiredFeatures(Project project) {
		// --- Feature installation via p2 is only possible if running via product --------------
		if (Application.getGlobalInfo().getExecutionEnvironment() == ExecutionEnvironment.ExecutedOverProduct) {
			P2OperationsHandler p2handler = CommonComponentFactory.getNewP2OperationsHandler();
			boolean installedNewFeatures = false;
			
			for(FeatureInfo feature : project.getProjectFeatures()) {
				
				if(p2handler.checkIfInstalled(feature.getFeatureID()) == false) {
				
					boolean success = p2handler.installIU(feature.getFeatureID(), feature.getRepositoryURI());
					if (success == true) {
						installedNewFeatures = true;
					} else {
						System.err.println("Unnable to install required feature " + feature.getFeatureID());
						//TODO Figure out how to handle this
					}
					
				}
			}
			
			if(installedNewFeatures == true) {
				Application.restart();
			}
		}
	}

	/**
	 * Load the projects user data model that is stored in the file 'agentgui.bin'.
	 *
	 * @param projectPath the project path
	 * @param project the project
	 */
	private static void loadProjectUserDataModel(File projectPath, Project project) {

		String userObjectFileName = projectPath.getAbsolutePath() + File.separator + Application.getGlobalInfo().getFilenameProjectUserObject();

		File userObjectFile = new File(userObjectFileName);
		if (userObjectFile.exists()) {

			FileInputStream fis = null;
			ObjectInputStreamForClassLoadService inStream = null;
			try {
				fis = new FileInputStream(userObjectFileName);
				inStream = new ObjectInputStreamForClassLoadService(fis);
				Serializable userObject = (Serializable) inStream.readObject();
				project.setUserRuntimeObject(userObject);

			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
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
			pm.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// pm.marshal( this, System.out );
			// --- Write values to xml-File ---------------
			Writer pw = new FileWriter(projectPath + File.separator + Application.getGlobalInfo().getFileNameProject());
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

			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
		    	if (out!=null) out.close();
		    	if (fos!=null) fos.close();
			}

			// --- Save the current SimulationSetup -------
			if (saveSetup == true) {
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
	 * This method closes the current project. If necessary it will try to save it before.
	 * @return Returns true if saving was successful
	 */
	public boolean close() {
		return this.close(null);
	}

	/**
	 * This method closes the current project. If necessary it will try to save the before.
	 * @param parentComponent the parent component
	 * @return Returns true if saving was successful
	 */
	public boolean close(Component parentComponent) {

		// --- Close project? -----------------------------
		String msgHead = null;
		String msgText = null;

		Application.setStatusBar(Language.translate("Projekt schließen") + " ...");
		if (this.isUnsaved() == true) {

			if (Application.isOperatingHeadless() == false) {
				// --- Operation with an UI ---------------
				msgHead = Language.translate("Projekt '@' speichern?");
				msgHead = msgHead.replace("'@'", "'" + projectName + "'");
				msgText = Language.translate(
						"Das aktuelle Projekt '@' ist noch nicht gespeichert!" + Application.getGlobalInfo().getNewLineSeparator() + 
						"Möchten Sie es nun speichern ?");
				msgText = msgText.replace("'@'", "'" + projectName + "'");

				ProjectCloseUserFeedback userAnswer = this.getProjectEditorWindow().getUserFeedbackForClosingProject(msgHead, msgText);
				switch (userAnswer) {
				case CancelCloseAction:
					return false;
				case SaveProject:
					if (this.save()==false) return false;
					break;
				case DoNotSaveProject:
					// --- Nothing to do here ---
					break;
				}

			} else {
				// --- Headless operation -----------------
				if (this.save()==false) return false;
			}
		}
		// --- Shutdown Jade ? ----------------------------
		if (Application.getJadePlatform().stopAskUserBefore() == false) {
			return false;
		}

		// --- Clear/Dispose EnvironmentPanel -------------
		EnvironmentController envController = this.getEnvironmentController();
		if (envController != null) {
			EnvironmentPanel envPanel = envController.getEnvironmentPanel();
			if (envPanel != null) {
				envPanel.dispose();
			}
		}

		// --- Clear PlugIns ------------------------------
		this.plugInVectorRemove();
		// --- Remove external resources ------------------
		this.resourcesRemove();

		// --- Close Project ------------------------------
		ProjectsLoaded loadedProjects = Application.getProjectsLoaded();
		int projectIndex = loadedProjects.getIndexByName(this.projectName);
		if (Application.isOperatingHeadless() == false) {
			getProjectEditorWindow().dispose();
		}
		loadedProjects.remove(this);

		int nProjects = loadedProjects.count();
		if (nProjects > 0) {
			if (projectIndex+1>nProjects ) projectIndex=nProjects-1;  
			Application.setProjectFocused(loadedProjects.get(projectIndex));
			Application.getProjectFocused().setFocus(true);
			Application.setTitelAddition(Application.getProjectFocused().getProjectName());
		} else {
			Application.setProjectFocused(null);
			Application.setTitelAddition("");
			if (Application.getMainWindow() != null) {
				Application.getMainWindow().setCloseButtonPosition(false);
			}
		}
		Application.setStatusBar("");
		return true;
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

	// --- Methods for bundles that are loaded with the project -----
	/**
	 * Returns the projects {@link ProjectBundleLoader}.
	 * @return the bundle loader
	 */
	public ProjectBundleLoader getProjectBundleLoader() {
		if (projectBundleLoader == null) {
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
		if (bundleNames.size() == 0) {
			bundleNames = null;
		}
		return bundleNames;
	}

	/**
	 * Move project libraries to the specified destination directory.
	 *
	 * @param destinationDirectoryRootPath the destination directory root path
	 * @return the actual path of the resources for the file manager
	 */
	public String exportProjectRessurcesToDestinationDirectory(String destinationDirectoryRootPath) {

		// --- Define / create destination directory ----------------
		File destinationDir = new File(destinationDirectoryRootPath);
		if (destinationDir.exists()==false) destinationDir.mkdirs();

		// --- Define the target file -------------------------------
		File projectExportFile = new File(destinationDir.getAbsolutePath() + File.separator + this.getProjectFolder() + ".agui");

		// --- Define the export settings ---------------------------
		ProjectExportSettings pExSet = new ProjectExportSettings();
		pExSet.setTargetFile(projectExportFile);
		pExSet.setIncludeAllSetups(false);
		pExSet.setIncludeInstallationPackage(false);

		// --- Do the export ----------------------------------------
		ProjectExportController pExCon = new ProjectExportController(this);
		pExCon.exportProject(pExSet, false, false);

		// --- Return the root path for the file manager ------------
		String destinationDirPath = null;
		try {
			destinationDirPath = destinationDir.getCanonicalPath();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return destinationDirPath;
	}

	/**
	 * Sets the project resources.
	 * @param projectResources the projectResources to set
	 */
	public void setProjectResources(ProjectResourceVector projectResources) {
		this.projectResources = projectResources;
	}

	/**
	 * Returns the project resources.
	 * @return the projectResources
	 */
	@XmlTransient
	public ProjectResourceVector getProjectResources() {
		if (this.projectResources == null) {
			this.projectResources = new ProjectResourceVector();
		}
		return projectResources;
	}

	/**
	 * This method adds external project resources (*.jar-files) to the CLATHPATH
	 */
	public void resourcesLoad() {
		this.getProjectBundleLoader().installAndStartBundles();
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
		this.setChangedAndNotify(CHANGED_ProjectResources);
	}

	/**
	 * Indicates if the projects MANIFEST.mf has to be recreated when project is to be opened.
	 * @return true, if the projects MANIFEST.mf is to be recreated when project is to be opened
	 */
	@XmlTransient
	public boolean isReCreateProjectManifest() {
		return reCreateProjectManifest;
	}

	/**
	 * Sets to recreate the projects MANIFEST.mf, if the project will be opened.
	 * @param reCreateProjectManifest the new re create project manifest
	 */
	public void setReCreateProjectManifest(boolean reCreateProjectManifest) {
		if (reCreateProjectManifest != this.reCreateProjectManifest) {
			this.reCreateProjectManifest = reCreateProjectManifest;
			this.setChangedAndNotify(CHANGED_ProjectResources);
		}
	}

	// ----------------------------------------------------------------------------------
	// --- Here we come with methods for (un-) load ProjectPlugIns --- Start ------------
	// ----------------------------------------------------------------------------------
	/**
	 * This method will load the ProjectPlugIns, which are configured for the
	 * current project (plugins_Classes). It will be executed only one time during 
	 * the 'ProjectsLoaded.add()' execution. After this no further functionality 
	 * can be expected. 
	 */
	public void plugInVectorLoad() {
		if (this.plugInVectorLoaded == false) {
			// --- load all plugins configured in 'plugIns_Classes' -----------
			for (int i = 0; i < this.plugIns_Classes.size(); i++) {
				if (this.plugInLoad(this.plugIns_Classes.get(i), false) == false) {
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
		for (int i = this.getPlugInsLoaded().size(); i > 0; i--) {
			this.plugInRemove(this.getPlugInsLoaded().get(i - 1), false);
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

		try {
			if (this.getPlugInsLoaded().isLoaded(pluginReference) == true) {
				// --- PlugIn can't be loaded because it's already there ------
				PlugIn ppi = getPlugInsLoaded().getPlugIn(pluginReference);

				String msgHead = Language.translate("Fehler - PlugIn: ") + " " + ppi.getClassReference() + " !";
				String msgText = Language.translate("Das PlugIn wurde bereits in das Projekt integriert " +
						"und kann deshalb nicht erneut hinzugefügt werden!");
				this.getProjectEditorWindow().showErrorMessage(msgText, msgHead);
				return false;

			} else {
				// --- PlugIn can be loaded -----------------------------------
				PlugIn ppi = getPlugInsLoaded().loadPlugin(this, pluginReference);
				this.setNotChangedButNotify(new PlugInNotification(PlugIn.ADDED, ppi));
				if (add2PlugInReferenceVector == true) {
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
	// --- Here we come with methods for (un-) load ProjectPlugIns --- End --------------
	// ----------------------------------------------------------------------------------

	// --- Methods to access the simulation setup -------------------
	/**
	 * Gets the list of simulation setups.
	 * @return the simulation setups
	 */
	@XmlElementWrapper(name = "simulationSetups")
	public SimulationSetups getSimulationSetups() {
		if (this.simulationSetups == null) {
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

	/**
	 * Sets the download resources for this Project. This Vector represents the list of resources that 
	 * should be downloadable in case of distributed executions. The idea is, that for example external 
	 * jar-files can distributed to a remote location, where such jar-files will be added automatically
	 * to the ClassPath of the starting JVM.
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
	 * Controls and/or creates whether the sub-folder-Structure exists
	 * @return boolean true or false :-)
	 */
	public boolean checkAndCreateProjectsDirectoryStructure() {

		String newDirName = null;
		File file = null;
		boolean error = false;

		for (int i = 0; i < this.defaultSubFolders.length; i++) {
			// --- Does the default folder exists ---------
			newDirName = this.getProjectFolderFullPath() + defaultSubFolders[i];
			file = new File(newDirName);
			if (file.isDirectory() == false) {
				// --- create directors -------------------
				if (file.mkdir() == false) {
					error = true;
				}
			}
		};

		// --- Indicate if successful or not --------------
		if (error == true) {
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
			if (Application.getJadePlatform().stopAskUserBefore() == false) {
				return;
			}
			// --- unload ClassPath -----------------------
			this.resourcesRemove();
		}

		this.getProjectEditorWindow().moveToFront();
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
	 * Maximizes the ProjectEditorWindow within the Application
	 */
	public void setMaximized() {
		ProjectEditorWindow pew = this.getProjectEditorWindow();
		if (pew != null) {
			pew.setMaximized();
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

	// ---- The projects directory information ----------------------
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
		if (projectFolderFullPath == null) {
			projectFolderFullPath = Application.getGlobalInfo().getPathProjects() + projectFolder + File.separator;
		}
		return projectFolderFullPath;
	}

	/**
	 * Gets the projects temporary folder.
	 * @return the projects temporary folder
	 */
	public String getProjectTempFolderFullPath() {
		String tmpFolder = this.getProjectFolderFullPath() + "~tmp" + File.separator;
		File tmpFile = new File(tmpFolder);
		if (tmpFile.exists() == false) {
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
		if (versionInformation == null) {
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
		if (updateAutoConfiguration == null) {
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
		if (updateSite == null) {
			updateSite = "?";
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

	// --- Visualization instances ----------------------------------
	/**
	 * Creates / Returns the project editor window (Swing or SWT).
	 * @return the project editor window for the current project
	 */
	@XmlTransient
	public synchronized ProjectEditorWindow getProjectEditorWindow() {
		if (this.projectEditorWindow == null) {
			this.projectEditorWindow = UiBridge.getInstance().getProjectEditorWindow(this);
			if (this.projectEditorWindow != null) {
				this.projectEditorWindow.addDefaultTabs();
			}
		}
		return projectEditorWindow;
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
		ProjectEditorWindow pew = this.getProjectEditorWindow();
		if (pew != null && newProjectView.equals(this.projectView) == false) {

			// --- Change view ----------------------------
			this.projectView = newProjectView;
			setUnsaved(true);
			setChanged();
			notifyObservers(CHANGED_ProjectView);
			pew.validateStartTab();
		}
	}

	/**
	 * @return the projectView
	 */
	public String getProjectView() {
		if (this.projectView == null) {
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
		if (this.environmentModelName == null & newEnvironmentModelName == null) {
			setNewEnvironmentModel = false;
		} else {
			if (this.environmentModelName == null || newEnvironmentModelName == null) {
				setNewEnvironmentModel = true;
			} else {
				if (this.environmentModelName.equals(newEnvironmentModelName) == true) {
					setNewEnvironmentModel = false;
				} else {
					setNewEnvironmentModel = true;
				}
			}
		}

		// --- In case that a new environment model is to set -------
		if (setNewEnvironmentModel == true) {
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
	public String getEnvSetupPath() {
		return this.getEnvSetupPath(true);
	}

	/**
	 * Gets the subfolder for setup environment files
	 * @param fullPath If true, the absolute path is returned, otherwise relative to the project folder
	 * @return The subfolder for setup environment files
	 */
	public String getEnvSetupPath(boolean fullPath) {
		if (fullPath == true) {
			return this.getProjectFolderFullPath() + defaultSubFolderEnvSetups;
		} else {
			return defaultSubFolderEnvSetups;
		}
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
		if (fullPath == true) {
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
		if (this.getSubOntologies().contains(newSubOntology) == false) {
			if (this.getOntologyVisualisationHelper().addSubOntology(newSubOntology) == true) {
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
		this.setUnsaved(true);
		this.setChanged();
		this.notifyObservers(CHANGED_ProjectOntology);
	}

	/**
	 * Returns the ontology visualisation helper for this project.
	 * @return the OntologyVisualisationHelper
	 */
	public OntologyVisualisationHelper getOntologyVisualisationHelper() {
		if (this.ontologyVisualisationHelper == null) {
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
		if (this.agentStartConfiguration == null) {
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
		if (this.timeModelController == null) {
			this.timeModelController = new TimeModelController(this);
		}
		return timeModelController;
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
		if (this.jadeConfiguration == null) {
			this.jadeConfiguration = new PlatformJadeConfig();
		}
		if (this.jadeConfiguration.getProject() == null) {
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
		if (distributionSetup == null) {
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
		if (agentClassLoadMetricsTable == null) {
			agentClassLoadMetricsTable = new AgentClassLoadMetricsTable(this);
		}
		if (agentClassLoadMetricsTable.getProject() == null) {
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
		if (environmentController == null) {

			EnvironmentType envType = this.getEnvironmentModelType();
			Class<? extends EnvironmentController> envControllerClass = envType.getEnvironmentControllerClass();
			if (envControllerClass==null) return null;

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
		if (environmentsComboBoxModel == null) {
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
		if (this.visualisationTab4SetupExecution == null) {
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

	/**
	 * Sets the eclipse MApplication.
	 * @param eclipseMApplication the current eclipse M application
	 */
	public void setEclipseMApplication(MApplication eclipseMApplication) {
		this.eclipseMApplication = eclipseMApplication;
	}

	/**
	 * Gets the eclipse MApplication.
	 * @return the eclipse MApplication
	 */
	@XmlTransient
	public MApplication getEclipseMApplication() {
		return eclipseMApplication;
	}

	/**
	 * Sets the eclipse EPartService.
	 * @param eclipseEPartService the current eclipse EPartService
	 */
	public void setEclipseEPartService(EPartService eclipseEPartService) {
		this.eclipseEPartService = eclipseEPartService;
	}

	/**
	 * Gets the eclipse EPartService.
	 * @return the eclipse EPartService
	 */
	@XmlTransient
	public EPartService getEclipseEPartService() {
		return eclipseEPartService;
	}

	/**
	 * Sets the eclipse EModelService.
	 * @param eclipseEModelService the current eclipse EModelService
	 */
	public void setEclipseEModelService(EModelService eclipseEModelService) {
		this.eclipseEModelService = eclipseEModelService;
	}

	/**
	 * Gets the eclipse EModelService.
	 * @return the eclipse EModelService
	 */
	@XmlTransient
	public EModelService getEclipseEModelService() {
		return eclipseEModelService;
	}

	/**
	 * Gets the project features.
	 *
	 * @return the project features
	 */
	@XmlTransient
	public Vector<FeatureInfo> getProjectFeatures() {
		return projectFeatures;
	}

	/**
	 * Sets the project features.
	 *
	 * @param projectFeatures the new project features
	 */
	public void setProjectFeatures(Vector<FeatureInfo> projectFeatures) {
		this.projectFeatures = projectFeatures;
	}

	/**
	 * Adds a project feature.
	 *
	 * @param projectFeature the project feature to add
	 */
	public void addProjectFeature(FeatureInfo projectFeature) {
		if (this.projectFeatures.contains(projectFeature) == false) {
			this.projectFeatures.add(projectFeature);
			this.setUnsaved(true);
			this.setChanged();
			this.notifyObservers(CHANGED_ProjectResources);
		}
	}

	/**
	 * Removes a project feature.
	 *
	 * @param projectFeature the project feature to remove
	 */
	public void removeProjectFeature(FeatureInfo projectFeature) {
		this.projectFeatures.remove(projectFeature);
		this.setUnsaved(true);
		this.setChanged();
		this.notifyObservers(CHANGED_ProjectResources);
	}

	/**
	 * Adds a list of features to the project, optionally clears the list before.
	 *
	 * @param projectFeatures the features to be added
	 * @param clear the clear if true, the feature list will be cleared before adding the features
	 */
	public void addAllProjectFeatures(List<FeatureInfo> projectFeatures, boolean clear) {
		if (clear == true) {
			this.projectFeatures.clear();
		}
		for (FeatureInfo feature : projectFeatures) {
			this.projectFeatures.addElement(feature);
		}
		this.setUnsaved(true);
		this.setChanged();
		this.notifyObservers(CHANGED_ProjectResources);
	}

}
