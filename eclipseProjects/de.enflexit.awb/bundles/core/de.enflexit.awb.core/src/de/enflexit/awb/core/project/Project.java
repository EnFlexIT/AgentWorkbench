package de.enflexit.awb.core.project;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Vector;

import javax.swing.JDesktopPane;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.ApplicationListener.ApplicationEvent;
import de.enflexit.awb.core.classLoadService.ClassLoadServiceUtility;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.core.environment.EnvironmentController.PersistenceStrategy;
import de.enflexit.awb.core.environment.EnvironmentPanel;
import de.enflexit.awb.core.environment.EnvironmentType;
import de.enflexit.awb.core.environment.TimeModelController;
import de.enflexit.awb.core.project.plugins.PlugIn;
import de.enflexit.awb.core.project.plugins.PlugInLoadException;
import de.enflexit.awb.core.project.plugins.PlugInNotification;
import de.enflexit.awb.core.project.plugins.PlugInsLoaded;
import de.enflexit.awb.core.project.setup.SimulationSetupNotification;
import de.enflexit.awb.core.project.setup.SimulationSetupNotification.SimNoteReason;
import de.enflexit.awb.core.project.setup.SimulationSetups;
import de.enflexit.awb.core.project.transfer.ProjectExportController;
import de.enflexit.awb.core.project.transfer.ProjectExportControllerProvider;
import de.enflexit.awb.core.project.transfer.ProjectExportSettingsController;
import de.enflexit.awb.core.ui.AgentWorkbenchUiManager;
import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.awb.core.ui.AwbProjectWindow;
import de.enflexit.awb.core.ui.AwbProjectWindow.ProjectCloseUserFeedback;
import de.enflexit.awb.core.update.ProjectRepositoryExport;
import de.enflexit.awb.core.update.ProjectRepositoryUpdate;
import de.enflexit.common.AbstractUserObject;
import de.enflexit.common.ExecutionEnvironment;
import de.enflexit.common.Observable;
import de.enflexit.common.PathHandling;
import de.enflexit.common.StringHelper;
import de.enflexit.common.classLoadService.ObjectInputStreamForClassLoadService;
import de.enflexit.common.featureEvaluation.FeatureInfo;
import de.enflexit.common.http.WebResourcesAuthorization;
import de.enflexit.common.http.WebResourcesAuthorization.AuthorizationType;
import de.enflexit.common.ontology.AgentStartConfiguration;
import de.enflexit.common.ontology.OntologyVisualizationHelper;
import de.enflexit.common.p2.P2OperationsHandler;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertiesEvent;
import de.enflexit.common.properties.PropertiesListener;
import de.enflexit.language.Language;

/**
 * This is the class, which holds all necessary informations about a project.<br>
 * In order to allow multiple access to the instance of Project, we designed it <br>
 * in the common <b>MVC pattern</b> (Model-View-Controller)<br>
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement public class Project extends Observable implements PropertiesListener {

	// --- public statics --------------------------------------
	@XmlTransient public static final String PREPARE_FOR_SAVING = "ProjectPrepare4Saving";
	@XmlTransient public static final String SAVED = "ProjectSaved";
	@XmlTransient public static final String SAVED_EXCLUDING_SETUP = "ProjectSavedExcludingSetup";
	@XmlTransient public static final String CHANGED_ProjectName = "ProjectName";
	@XmlTransient public static final String CHANGED_ProjectDescription = "ProjectDescription";
	@XmlTransient public static final String CHANGED_ProjectView = "ProjectView";
	@XmlTransient public static final String CHANGED_ProjectStartTab = "ProjectStartTab";
	@XmlTransient public static final String CHANGED_ProjectFolder= "ProjectFolder";

	@XmlTransient public static final String CHANGED_VersionTag = "VersionTag";
	@XmlTransient public static final String CHANGED_Version = "Version";
	@XmlTransient public static final String CHANGED_UpdateAutoConfiguration= "UpdateAutoConfiguration";
	@XmlTransient public static final String CHANGED_UpdateSite = "UpdateSite";
	@XmlTransient public static final String CHANGED_UpdateDateLastChecked = "UpdateDateLastChecked";
	@XmlTransient public static final String CHANGED_UpdateAuthorization = "UpdateAuthorization";
	@XmlTransient public static final String CHANGED_UpdateSettings = "UpdateSettings";	// Summarizes UpdateSite, UpdateAutoConfiguration and UpdateAuthorization

	@XmlTransient public static final String CHANGED_EnvironmentModelType= "EnvironmentModelType";
	@XmlTransient public static final String CHANGED_StartArguments4BaseAgent = "StartArguments4BaseAgents";
	@XmlTransient public static final String CHANGED_TimeModelClass = "TimeModelConfiguration";
	@XmlTransient public static final String CHANGED_ProjectOntology = "ProjectOntology";
	@XmlTransient public static final String CHANGED_ProjectResources = "ProjectResources";
	@XmlTransient public static final String CHANGED_ProjectFeatures = "ProjectFeatures";
	@XmlTransient public static final String CHANGED_JadeConfiguration = "JadeConfiguration";
	@XmlTransient public static final String CHANGED_DistributionSetup = "DistributionSetup";
	@XmlTransient public static final String CHANGED_RemoteContainerConfiguration = "RemoteContainerConfiguration";
	@XmlTransient public static final String CHANGED_ProjectProperties = "ProjectProperties";
	@XmlTransient public static final String CHANGED_UserRuntimeObject = "UserRuntimeObject";

	// --- Constant value in order to set the project view ----------
	@XmlTransient public static final String VIEW_Developer = "Developer";
	@XmlTransient public static final String VIEW_User = "User";
	@XmlTransient public static final String VIEW_Maximize = "MaximizeView";
	@XmlTransient public static final String VIEW_Restore = "RestoreView";
	@XmlTransient public static final String VIEW_TabsLoaded = "TabsLoaded";

	// --- Constants for the Project Desktop ------------------------
	@XmlTransient public static final String PROJECT_DESKTOP_COMPONENT_ADDED   = "ProjectDesktopComponentAdded";
	@XmlTransient public static final String PROJECT_DESKTOP_COMPONENT_REMOVED = "ProjectDesktopComponentRemoved";
	
	// --- Constants for the the agent distribution metric ----------
	@XmlTransient public static final String AGENT_METRIC_Reset = "AgentMetric_Reset";
	@XmlTransient public static final String AGENT_METRIC_ChangedDataSource = "AgentMetric_ChangedDataSource";
	@XmlTransient public static final String AGENT_METRIC_AgentDescriptionAdded = "AgentMetric_AgentDescriptionAdded";
	@XmlTransient public static final String AGENT_METRIC_AgentDescriptionEdited = "AgentMetric_AgentDescriptionEdited";
	@XmlTransient public static final String AGENT_METRIC_AgentDescriptionRemoved = "AgentMetric_AgentDescriptionRemoved";

	// --- Constants -------------------------------------------
	@XmlTransient public static final String DEFAULT_SUB_FOLDER_4_SETUPS   = "setups";
	@XmlTransient public static final String DEFAULT_SUB_FOLDER_ENV_SETUPS = "setupsEnv";
	@XmlTransient public static final String DEFAULT_SUB_FOLDER_SECURITY = "security";
	@XmlTransient private final String[] DEFAULT_SUB_FOLDERS = {DEFAULT_SUB_FOLDER_4_SETUPS, DEFAULT_SUB_FOLDER_ENV_SETUPS};
	@XmlTransient public static final String DEFAULT_TEMP_FOLDER = "~tmp";
	@XmlTransient public static final String DEFAULT_AGENT_WORKING_DIRECTORY = "agentWorkingDirectory";

	/** The OSGI-bundle of the current project */
	@XmlTransient private ProjectBundleLoader projectBundleLoader;

	/** This is the 'view' in the context of the mentioned MVC pattern */
	@XmlTransient private AwbProjectWindow projectEditorWindow;
	/** This JDesktopPane that can be used as project desktop. */
	@XmlTransient private JDesktopPane projectDesktop;

	/** Indicates that the project is unsaved or not */
	@XmlTransient private boolean isUnsaved = false;
	
	@XmlTransient private String projectFolder;
	@XmlTransient private String projectFolderFullPath;
	@XmlTransient private String projectSecurtiyFolderFullPath;
	
	// --- Variables saved within the project file ------------------
	@XmlElement(name="projectName")				private String projectName;
	@XmlElement(name="projectDescription")		private String projectDescription;
	@XmlElement(name="projectStartTab")			private String projectStartTab;	
	@XmlElement(name="projectView")				private String projectView;			// --- View for developer or end-user ---
	@XmlElement(name="projectTreeVisible")		private boolean projectTreeVisible=true;
	@XmlElement(name="projectTabHeaderVisible")	private boolean projectTabHeaderVisible=true;
	
	// --- Variables for the update and version handling ------------
	@XmlTransient public static final String DEFAULT_VERSION_TAG = "Complete Project"; 
	@XmlElement(name="version")					private String version;
	@XmlElement(name="versionTag")				private String versionTag;
	@XmlElement(name="updateSite")				private String updateSite;
	@XmlElement(name="updateAutoConfiguration")	private Integer updateAutoConfiguration;
	@XmlElement(name="updateDateLastChecked")	private long updateDateLastChecked;

	// --- Project repository authorization ------------------------------
	@XmlElement(name = "updateAuthorization")
	private WebResourcesAuthorization updateAuthorization;
	
	// --- The environment model name to use ------------------------
	@XmlElement(name="environmentModel")		private String environmentModelName;	

	/**
	 * This Vector is used in order to store information about features required by the current project
	 */
	@XmlElementWrapper(name = "projectFeatures")
	@XmlElement(name = "projectFeature")
	private Vector<FeatureInfo> projectFeatures;
	
	/**
	 * This Vector will store the class names of the PlugIns which are used within the project
	 */
	@XmlElementWrapper(name = "plugins")
	@XmlElement(name = "className")
	private Vector<String> pluginClassNames;
	/**
	 * This extended Vector will hold the concrete instances of the PLugIns loaded in this project
	 */
	@XmlTransient private PlugInsLoaded plugInsLoaded = new PlugInsLoaded();
	@XmlTransient private boolean plugInVectorLoaded = false;

	/**
	 * This class is used for the management of the used Ontology's inside a project.
	 * It handles the concrete instances.
	 */
	@XmlTransient private OntologyVisualizationHelper ontologyVisualisationHelper;

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

	@XmlElement(name = "projectProperties")
	private Properties properties;
	
	/**
	 * This field can be used in order to provide customized objects during
	 * the runtime of a project. This will be not stored within the file 'agentgui.xml' 
	 */
	@XmlTransient 
	private Serializable userRuntimeObject;
	@XmlElement(name = "userObjectClassName")
	private String userRuntimeObjectClassName;
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

	/** The EnvironmentController of the project. */
	@XmlTransient private EnvironmentController environmentController;

	/** Configuration settings for the TimeModel used in this Project */
	@XmlElement(name="timeModelClass") 	private String timeModelClass;
	/** The TimeModelController controls the display of the selected TimModel. */
	@XmlTransient private TimeModelController timeModelController;

	

	/**
	 * Instantiates a new project.
	 */
	public Project() { }

	/**
	 * Loads and returns the project from the specified project sub-directory. Both files will be loaded (agentgui.xml and agentgui.bin).
	 * By loading, this method will also load external jar-resources by using the ClassLoader.
	 *
	 * @param projectSubDirectory the project sub directory
	 * @return the project
	 */
	public static Project load(String projectSubDirectory) {
		return Project.load(projectSubDirectory, true);
	}

	/**
	 * Loads and returns the project from the specified project sub-directory. Both files will be loaded (agentgui.xml and agentgui.bin).
	 * External jar resources will optionally be loaded.
	 *
	 * @param projectSubDirectory the project sub directory
	 * @param loadResources load external jar resources?
	 * @return the project
	 */
	public static Project load(final String projectSubDirectory, boolean loadResources) {
		
		// ----------------------------------------------------------
		// --- Check for the correct sub directory path -------------
		// ----------------------------------------------------------
		// --- If nothing will be found, error handling will be   ---
		// --- done in the later load()-method.                   ---
		// --- Thus, parameter can be used as default!            ---
		// ----------------------------------------------------------
		String projectSubDirectoryCaseSensitive = projectSubDirectory;
		
		// --- Check all sub directories of project base dir --------
		File projectsDir = new File(Application.getGlobalInfo().getPathProjects());
		File [] filesFound = projectsDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return (dir.isDirectory()==true && name.equalsIgnoreCase(projectSubDirectory)==true);
			}
		});
		// --- Substitute start argument with directory found -------
		if (filesFound!=null && filesFound.length > 0) {
			projectSubDirectoryCaseSensitive = filesFound[0].getName();
		}
		
		// --- Define projects directory and load project -----------
		String projectFolder = Application.getGlobalInfo().getPathProjects() + projectSubDirectoryCaseSensitive + File.separator;
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

		// --- Load the XML file of the project ---------------------
		Project project = loadProjectXml(projectPath);
		if (project==null) return null;
		
		// --- Check/create default folders -------------------------
		project.setProjectFolder(projectSubDirectory);
		project.checkAndCreateProjectsDirectoryStructure();
		
		// ----------------------------------------------------------
		// --- Install required features if necessary ---------------
		// ----------------------------------------------------------
		if (Application.getGlobalInfo().getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverProduct) {
			// --- Only possible if not running from the IDE --------
			boolean newFeaturesInstalled = false;
			try {
				// --- Install additional features if necessary -----
				newFeaturesInstalled = project.installRequiredFeatures();
				
			} catch (Exception ex) {
				System.err.println("[" + Project.class.getSimpleName() +  "] Not all required features have been installed successfully:");
				System.err.println(ex.getMessage());
			}
			
			// --- Restart the application if necessary -------------
			if (newFeaturesInstalled==true) {
				Application.relaunch("-project " + project.getProjectFolder());
				return null; // --- Skip the further loading of the project					
			}
		}
		
		// ----------------------------------------------------------
		// --- Load additional resources and data -------------------
		// ----------------------------------------------------------
		if (project!=null && loadResources==true) {
			// --- Load additional jar-resources --------------------
			project.resourcesLoad();
			// --- Load user data model -----------------------------
			Project.loadProjectUserDataModel(projectPath, project);
		}
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

		// --- Get data model from file -------------------
		String xmlFileName = projectPath.getAbsolutePath() + File.separator + Application.getGlobalInfo().getFileNameProject();	

		// --- Does the file exists -----------------------
		File xmlFile = new File(xmlFileName);
		if (xmlFile.exists()==false) {

			System.out.println(Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + " " + xmlFileName);
			Application.setStatusBarMessageReady();

			String title = Language.translate("Projekt-Ladefehler!");
			String message = Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + "\n";
			message += xmlFileName;
			if (Application.getGlobalInfo().getExecutionMode() == ExecutionMode.APPLICATION){
				AwbMessageDialog.showMessageDialog(Application.getMainWindow(), message, title, AwbMessageDialog.WARNING_MESSAGE);
			}
			return null;
		}

		// --- Read file 'agentgui.xml' -------------------
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(xmlFileName);
			JAXBContext pc = JAXBContext.newInstance(Project.class);
			Unmarshaller um = pc.createUnmarshaller();
			project = (Project) um.unmarshal(fileReader);
			// --- Fire application event -----------------
			Application.informApplicationListener(new ApplicationEvent(ApplicationEvent.PROJECT_LOADING_PROJECT_XML_FILE_LOADED, project));

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
	 * Load the projects user data model that is stored in the file 'agentgui.bin'.
	 *
	 * @param projectPath the project path
	 * @param project the project
	 */
	private static void loadProjectUserDataModel(File projectPath, Project project) {
		// --- ... as XML or bin file ? -------------------
		boolean successXmlLoad = Project.loadUserObjectFromXmlFile(projectPath, project);
		if (successXmlLoad==false) {
			// --- Backup: load from bin file -------------
			Project.loadUserObjectFromBinFile(projectPath, project);
			
		} else {
			// --- Delete old bin file if available -------
			File binFileUserObject = new File(projectPath.getAbsolutePath() + File.separator + Application.getGlobalInfo().getFilenameProjectUserObjectBinFile());
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
	 * @param projectPath the project path
	 * @param project the project
	 * @return true, if successful
	 */
	private static boolean loadUserObjectFromXmlFile(File projectPath, Project project) {
		
		boolean successfulLoaded = false;
		if (project!=null && project.getUserRuntimeObjectClassName()!=null) {
			try {
				File userObjectFile = new File(projectPath.getAbsolutePath() + File.separator + Application.getGlobalInfo().getFileNameProjectUserObjectXmlFile());
				Class<?> userRuntimeClass = ClassLoadServiceUtility.forName(project.getUserRuntimeObjectClassName());
				AbstractUserObject userObject = AbstractUserObject.loadUserObjectFromXmlFile(userObjectFile, userRuntimeClass);
				if (userObject!=null) {
					project.setUserRuntimeObject(userObject);
					successfulLoaded = true;
					// --- Fire application event ---------
					Application.informApplicationListener(new ApplicationEvent(ApplicationEvent.PROJECT_LOADING_PROJECT_USER_FILE_LOADED, project));
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
	 * @param projectPath the project path
	 * @param project the project
	 * @return true, if successful
	 */
	private static boolean loadUserObjectFromBinFile(File projectPath, Project project) {
		
		boolean successfulLoaded = false;
		
		File userObjectFile = new File(projectPath.getAbsolutePath() + File.separator + Application.getGlobalInfo().getFilenameProjectUserObjectBinFile());
		if (userObjectFile.exists()) {

			FileInputStream fis = null;
			ObjectInputStreamForClassLoadService inStream = null;
			try {
				fis = new FileInputStream(userObjectFile);
				inStream = new ObjectInputStreamForClassLoadService(fis, ClassLoadServiceUtility.class);
				Serializable userObject = (Serializable) inStream.readObject();
				project.setUserRuntimeObject(userObject);
				successfulLoaded = true;
				// --- Fire application event -------------
				Application.informApplicationListener(new ApplicationEvent(ApplicationEvent.PROJECT_LOADING_PROJECT_USER_FILE_LOADED, project));


			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (inStream!=null) inStream.close();
					if (fis!=null) fis.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return successfulLoaded;
	}
	
	/**
	 * Saves the current Project to the files 'agentgui.xml' and agentgui.bin.
	 * @return true, if successful
	 */
	public boolean save() {
		return this.save(new File(this.getProjectFolderFullPath()), true);
	}
	/**
	 * Saves the current Project to the default project files 'agentgui.xml' and agentgui.bin.
	 * @param saveSetup set to true if the current simulation setup should be saved
	 * @return true, if successful
	 */
	public boolean save(boolean saveSetup) {
		return this.save(new File(this.getProjectFolderFullPath()), saveSetup, true);
	}
	/**
	 * Saves the current Project to the files 'agentgui.xml' and agentgui.bin.
	 * @param projectPath the project path where the files have to be stored
	 * @param saveSetup set to true if the current simulation setup should be saved
	 * @return true, if successful
	 */
	public boolean save(File projectPath, boolean saveSetup) {
		return this.save(projectPath, saveSetup, true);
	}
	/**
	 * Saves the current Project to the files 'agentgui.xml', and optionally agentgui.bin.
	 * @param projectPath the project path where the files have to be stored
	 * @param saveSetup set to true if the current simulation setup should be saved
	 * @param saveUserDataModel set to true if the user data model (agentgui.bin) should be saved
	 * @return true, if successful
	 */
	public boolean save(File projectPath, boolean saveSetup, boolean saveUserDataModel) {

		Application.setStatusBarMessage(this.projectName + ": " + Language.translate("speichern") + " ... ");

		// ------------------------------------------------
		// --- Notify. prepare for saving -----------------
		this.setNotChangedButNotify(Project.PREPARE_FOR_SAVING);

		boolean successful = false;
		try {
			
			// --------------------------------------------
			// --- Save main project file -----------------
			this.saveProjectFile(projectPath);
			
			// --------------------------------------------
			// --- Save the userRuntimeObject -------------
			if (saveUserDataModel==true) {
				// --- ... as XML or bin file ? -----------
				boolean successXmlSave = this.saveUserObjectAsXmlFile(projectPath);
				if (successXmlSave==false) {
					// --- Backup: save as bin file -------
					this.saveUserObjectAsBinFile(projectPath);
				}
			}

			// --------------------------------------------
			// --- Save the current SimulationSetup -------
			if (saveSetup==true) {
				this.getSimulationSetups().setupSave();
			}

			// --------------------------------------------
			// --- Save the environment -------------------
			EnvironmentController envController = this.getEnvironmentController();
			if (envController!=null) {
				envController.callSaveEnvironment(PersistenceStrategy.HandleWithProjectOpenOrSave);
			}
			
			this.setUnsaved(false);
			successful = true;
			
			// --------------------------------------------
			// --- Notification ---------------------------
			if (saveSetup==true) {
				this.setNotChangedButNotify(Project.SAVED);
			} else {
				this.setNotChangedButNotify(Project.SAVED_EXCLUDING_SETUP);
			}

		} catch (Exception e) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Error while saving the project files!");
			e.printStackTrace();
		}
		Application.setStatusBarMessage("");
		return successful;
	}

	/**
	 * Just saves the main project file (agentgui.xml) of the current Project.
	 */
	public void saveProjectFile() {
		try {
			this.saveProjectFile(new File(this.getProjectFolderFullPath()));
		} catch (JAXBException | IOException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Saves the main project file (agentgui.xml).
	 *
	 * @param projectPath the actual projects path
	 * @throws JAXBException the JAXB exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void saveProjectFile(File projectPath) throws JAXBException, IOException {
					
		// --- Prepare Context and Marshaller ---------
		JAXBContext pc = JAXBContext.newInstance(this.getClass());
		Marshaller pm = pc.createMarshaller();
		pm.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		// --- Write values to xml-File ---------------
		Writer pw = new FileWriter(projectPath + File.separator + Application.getGlobalInfo().getFileNameProject());
		pm.marshal(this, pw);
		pw.close();
	}
	
	
	/**
	 * Saves the current user object as XML file.
	 *
	 * @param projectPath the project path
	 * @return true, if successful
	 */
	private boolean saveUserObjectAsXmlFile(File projectPath) {
		File destinFile = new File(projectPath + File.separator + Application.getGlobalInfo().getFileNameProjectUserObjectXmlFile());
		return AbstractUserObject.saveUserObjectAsXmlFile(destinFile, this.getUserRuntimeObject());
	}
	/**
	 * Saves the current user object as bin file.
	 *
	 * @param projectPath the project path
	 * @return true, if successful
	 */
	private boolean saveUserObjectAsBinFile(File projectPath) {
		
		boolean successfulSaved = false;
		
		if (projectPath==null) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The path for saving the projects user runtime object is not allowed to be null!");
			return false;
		}
		
		if (this.getUserRuntimeObject()==null) {
			successfulSaved = true;
		
		} else {
			try {
				FileOutputStream fos = null;
				ObjectOutputStream out = null;
				try {
					fos = new FileOutputStream(projectPath + File.separator + Application.getGlobalInfo().getFilenameProjectUserObjectBinFile());
					out = new ObjectOutputStream(fos);
					out.writeObject(this.getUserRuntimeObject());
					successfulSaved = true;
					
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					if (out!=null) out.close();
					if (fos!=null) fos.close();
				}
				
			} catch (IOException ioEx) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Error while saving the projects user runtime object as bin file:");
				ioEx.printStackTrace();
			} 
		}
		return successfulSaved;
	}
	
	
	/**
	 * Closes the current project. If necessary it will try to save it before.
	 * @return Returns true if saving was successful
	 */
	public boolean close() {
		return this.close(null, false);
	}
	/**
	 * Closes the current project. If necessary it will try to save the before.
	 * @param parentComponent the parent component
	 * @return true, if successful
	 */
	public boolean close(Component parentComponent) {
		return this.close(parentComponent, false);
	}
	/**
	 * Closes the current project. If necessary it will try to save the before.
	 * @param parentComponent the parent component
	 * @param isSkipSaving set true, if you want to skip saving the project
	 * @return Returns true if saving was successful
	 */
	public boolean close(Component parentComponent, boolean isSkipSaving) {

		// --- Close project? -----------------------------
		String msgHead = null;
		String msgText = null;

		Application.setStatusBarMessage(Language.translate("Projekt schließen") + " ...");
		if (isSkipSaving==false && this.isUnsaved()==true) {

			if (Application.isOperatingHeadless()==false) {
				// --- Operation with an UI ---------------
				msgHead = Language.translate("Projekt '@' speichern?");
				msgHead = msgHead.replace("'@'", "'" + projectName + "'");
				msgText = Language.translate(
						"Das aktuelle Projekt '@' ist noch nicht gespeichert!" + Application.getGlobalInfo().getNewLineSeparator() + 
						"Möchten Sie es nun speichern ?");
				msgText = msgText.replace("'@'", "'" + projectName + "'");

				ProjectCloseUserFeedback userAnswer = this.getProjectEditorWindow().getUserFeedbackForClosingProject(msgHead, msgText, parentComponent);
				switch (userAnswer) {
				case CancelCloseAction:
					return false;
				case SaveProject:
					if (this.save()==false) {
						return false;
					}
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
		if (Application.getJadePlatform().stopAskUserBefore()==false) {
			return false;
		}

		// --- Clear/Dispose EnvironmentPanel -------------
		if (this.isEnvironmentControllerInitiated()==true) {
			EnvironmentController envController = this.getEnvironmentController();
			if (envController!=null) {
				EnvironmentPanel envPanel = envController.getEnvironmentPanel();
				if (envPanel!=null) {
					envPanel.dispose();
				}
				envController.dispose();
			}
		}

		// --- Close Project ------------------------------
		ProjectsLoaded loadedProjects = Application.getProjectsLoaded();
		int projectIndex = loadedProjects.getIndexByName(this.projectName);
		if (Application.isOperatingHeadless()==false && this.isProjectEditorWindowOpen()==true) {
			this.getProjectEditorWindow().dispose();
		}
		loadedProjects.remove(this);

		// --- Clear PlugIns ------------------------------
		this.plugInVectorRemove();
		// --- Remove external resources ------------------
		this.resourcesRemove();

		
		int nProjects = loadedProjects.count();
		if (nProjects > 0) {
			if (projectIndex+1>nProjects ) projectIndex=nProjects-1;  
			Application.setProjectFocused(loadedProjects.get(projectIndex));
			Application.getProjectFocused().setFocus(true);
			Application.setTitleAddition(Application.getProjectFocused().getProjectName());
		} else {
			Application.setProjectFocused(null);
			Application.setTitleAddition("");
			if (Application.getMainWindow() != null) {
				Application.getMainWindow().setCloseProjectButtonVisible(false);
			}
		}
		Application.setStatusBarMessage("");
		Application.startGarbageCollection();
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
	@XmlTransient
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
		Vector<Bundle> bundleVector = this.getBundles();
		for (int i = 0; i < bundleVector.size(); i++) {
			bundleNames.add(bundleVector.get(i).getSymbolicName());
		}
		if (bundleNames.size()==0) {
			bundleNames = null;
		}
		return bundleNames;
	}

	/**
	 * Returns the features required for the current project.
	 * @return the project features
	 */
	@XmlTransient
	public Vector<FeatureInfo> getProjectFeatures() {
		if (projectFeatures==null) {
			projectFeatures = new Vector<>();
		}
		return projectFeatures;
	}
	/**
	 * Sets the project features.
	 * @param projectFeatures the new project features
	 */
	public void setProjectFeatures(Vector<FeatureInfo> projectFeatures) {
		this.projectFeatures = projectFeatures;
	}
	
	/**
	 * Automatically determines and refreshes the vector of required features.
	 */
	public void determineRequiredFeatures() {

		boolean isChaneged = false;
		
		// --- Get the list of required features --------
		Vector<FeatureInfo> reqFeatures = BundleFeatureMapper.getRequiredFeaturesForProject(this);
		if (reqFeatures!=null) {
			for (FeatureInfo fi : reqFeatures) {
				if (this.getProjectFeatures().contains(fi)==false) {
					this.getProjectFeatures().add(fi);
					isChaneged = true;
				}
			}
		}

		if (isChaneged==true) {
			this.setUnsaved(true);
			this.setChanged();
			this.notifyObservers(CHANGED_ProjectFeatures);
		}
	}
	
	/**
	 * Check project features availability.
	 * @return true, if all required features are available
	 */
	public boolean requiresFeatureInstallation() {
		for (int i=0; i<this.getProjectFeatures().size(); i++) {
			FeatureInfo feature = this.getProjectFeatures().get(i);
			if (P2OperationsHandler.getInstance().checkIfInstalled(feature.getId())==false) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Loads the features required by the project from the p2 repository if necessary.
	 * @return true if new features have been installed
	 * @throws Exception installation of a feature failed
	 */
	public boolean installRequiredFeatures() throws Exception {
		
		boolean somethingInstalled = false;
		Vector<FeatureInfo> projectFeatures = this.getProjectFeatures();
		
		if (projectFeatures!=null && projectFeatures.isEmpty()==false) {
			
			for (int i = 0; i < projectFeatures.size(); i++) {
				
				// --- Get the required feature ---------------------
				FeatureInfo feature = projectFeatures.get(i);
				// --- Check if the feature is already installed ---- 
				if (P2OperationsHandler.getInstance().checkIfInstalled(feature.getId())==false) {
					
					// --- Install if not ---------------------------
					System.out.print("=> Project '" + this.getProjectName() + "': Install '" + feature.getName()  + "' (" + feature.getId() + ") from " + feature.getRepositoryURI() + " ... ");
					if (P2OperationsHandler.getInstance().installIU(feature.getId(), feature.getRepositoryURI())==true) {
						// --- Installation successful --------------
						System.out.println("DONE!\n");
						somethingInstalled = true;
					} else {
						// --- Installation failed ------------------
						System.out.println("FAILED!");
						throw new Exception("=> Project '" + this.getProjectName() + "': Unnable to install feature " + feature.getId());
					}
				}
			}
		}
		return somethingInstalled;
	}
	/**
	 * Adds a project feature.
	 * @param projectFeature the project feature to add
	 */
	public void addProjectFeature(FeatureInfo projectFeature) {
		if (this.projectFeatures.contains(projectFeature)==false) {
			this.projectFeatures.add(projectFeature);
			this.setUnsaved(true);
			this.setChanged();
			this.notifyObservers(CHANGED_ProjectFeatures);
		}
	}
	/**
	 * Removes a project feature.
	 * @param projectFeature the project feature to remove
	 */
	public void removeProjectFeature(FeatureInfo projectFeature) {
		this.projectFeatures.remove(projectFeature);
		this.setUnsaved(true);
		this.setChanged();
		this.notifyObservers(CHANGED_ProjectFeatures);
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
		this.notifyObservers(CHANGED_ProjectFeatures);
	}
	
	/**
	 * Move project libraries to the specified destination directory.
	 *
	 * @param destinationDirectory the destination directory root path
	 * @param isExportEntireProject the indicator to export the entire project or not
	 * @param messageSuccess the optional message for successful export (is allowed to be <code>null</code>)
	 * @param messageFailure the optional message, if the export failed (is allowed to be <code>null</code>)
	 * @return the actual File that were exported to the destination directory
	 */
	public File exportProjectRessourcesToDestinationDirectory(String destinationDirectory, boolean isExportEntireProject, String messageSuccess, String messageFailure) {

		// --- Define / create destination directory ----------------
		File destinationDir = new File(destinationDirectory);
		if (destinationDir.exists()==false) destinationDir.mkdirs();

		// --- Define the target file -------------------------------
		File projectExportFile = new File(destinationDir.getAbsolutePath() + File.separator + this.getProjectFolder() + ".agui");

		// --- Define the export settings ---------------------------
		ProjectExportController pExCon = ProjectExportControllerProvider.getProjectExportController();
		ProjectExportSettingsController pesc = new ProjectExportSettingsController(this, pExCon);
		if (isExportEntireProject==false) {
			pesc.setIncludeAllSetups(false);
			pesc.includeSimulationSetup(this.getSimulationSetupCurrent());
			pesc.addDefaultsToExcludeList();
		}
		pesc.excludeInstallationPackage();
		pesc.setTargetFile(projectExportFile);

		// --- Do the export ----------------------------------------
		if (messageSuccess!=null) pExCon.setMessageSuccess(messageSuccess);	
		if (messageFailure!=null) pExCon.setMessageFailure(messageFailure);
		pExCon.exportProject(this, pesc.getProjectExportSettings(), false, false);

		// --- Return the root path for the file manager ------------
		return projectExportFile;
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
	

	// ----------------------------------------------------------------------------------
	// --- Here we come with methods for (un-) load ProjectPlugIns --- Start ------------
	// ----------------------------------------------------------------------------------
	/**
	 * Returns the plugin class names (this is also stored in the project file).
	 * @return the plugin class names
	 */
	public Vector<String> getPluginClassNames() {
		if (pluginClassNames==null) {
			pluginClassNames = new Vector<>();
		}
		return pluginClassNames;
	}
	/**
	 * This method will load the ProjectPlugIns, which are configured for the
	 * current project (plugins_Classes). It will be executed only one time during 
	 * the 'ProjectsLoaded.add()' execution. After this no further functionality 
	 * can be expected. 
	 */
	public void plugInVectorLoad() {
		if (this.plugInVectorLoaded == false) {
			// --- load all plugins configured in 'pluginClassNames' -----------
			for (int i = 0; i < this.getPluginClassNames().size(); i++) {
				if (this.plugInLoad(this.getPluginClassNames().get(i), false)==false) {
					System.err.println("Removed Plug-In entry for: '" + this.getPluginClassNames().get(i) + "'");
					this.getPluginClassNames().remove(i);
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
		this.setPlugInsLoaded(new PlugInsLoaded());
		// --- load all configured PlugIns to the project -
		this.plugInVectorLoad();

	}

	/**
	 * Informs all PlugIn's that the setup was loaded.
	 */
	public void plugInVectorInformSetupLoaded() {
		for (int i = 0; i < this.getPlugInsLoaded().size(); i++) {
			PlugIn plugIn = this.getPlugInsLoaded().get(i);
			plugIn.update(this, new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_LOAD));
		}
	}

	/**
	 * This method loads a single PlugIn, given by its class reference.
	 *
	 * @param pluginReference the plugin reference
	 * @param add2PlugInReferenceVector the add 2 plug in reference vector
	 * @return true, if successful
	 */
	public boolean plugInLoad(String pluginReference, boolean add2PlugInReferenceVector) {

		try {
			if (this.getPlugInsLoaded().isLoaded(pluginReference) == true) {
				// --- PlugIn can't be loaded because it's already there ------
				PlugIn ppi = getPlugInsLoaded().getPlugIn(pluginReference);

				String msgHead = Language.translate("Fehler - PlugIn: ") + " " + ppi.getClassReference() + " !";
				String msgText = Language.translate("Das PlugIn wurde bereits in das Projekt integriert und kann deshalb nicht erneut hinzugefügt werden!");
				this.getProjectEditorWindow().showErrorMessage(msgText, msgHead);
				return false;

			} else {
				// --- PlugIn can be loaded -----------------------------------
				PlugIn ppi = this.getPlugInsLoaded().loadPlugin(this, pluginReference);
				this.setNotChangedButNotify(new PlugInNotification(PlugIn.ADDED, ppi));
				if (add2PlugInReferenceVector==true) {
					this.getPluginClassNames().add(pluginReference);
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
	 * @param removeFromProjectReferenceVector the indicator to remove the references also from project reference vector
	 */
	public void plugInRemove(PlugIn plugIn, boolean removeFromProjectReferenceVector) {
		this.getPlugInsLoaded().removePlugIn(plugIn);
		this.setNotChangedButNotify(new PlugInNotification(PlugIn.REMOVED, plugIn));
		if (removeFromProjectReferenceVector) {
			this.getPluginClassNames().remove(plugIn.getClassReference());
		}
	}

	/**
	 * Sets PlugInsLoaded, a <code>Vector&lt;PlugIn&gt;</code> that describes, which PlugIn's has to loaded.
	 * @param plugInsLoaded the PlugInsLoaded
	 */
	public void setPlugInsLoaded(PlugInsLoaded plugInsLoaded) {
		this.plugInsLoaded = plugInsLoaded;
	}

	/**
	 * Returns PlugInsLoaded, a <code>Vector&lt;PlugIn&gt;</code> that describes, which PlugIn's were loaded.
	 * @return the PlugInsLoaded
	 */
	@XmlTransient
	public PlugInsLoaded getPlugInsLoaded() {
		return plugInsLoaded;
	}
	
	/**
	 * Removes a plug in class from the reference vector. If the plug in is currently loaded, it will be unloaded before. 
	 * @param classReference the class reference
	 */
	public void removePlugInClassReference(String classReference) {
		// --- Check if the PlugIn is currently loaded --------------
		PlugIn plugIn = this.getPlugInsLoaded().getPlugIn(classReference);
		
		if (plugIn!=null) {
			// --- If it is, unload it and remove it from the class reference vector ----
			this.plugInRemove(plugIn, true);
		} else {
			// --- If not, just remove it from the class reference vector ---------------
			this.getPluginClassNames().remove(classReference);
		}
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
	 * Sets the current simulation setup name (not file).
	 * @param simulationSetupCurrent the new simulation setup name
	 */
	public void setSimulationSetupCurrent(String simulationSetupCurrent) {
		this.simulationSetupCurrent = simulationSetupCurrent;
	}
	/**
	 * Returns the current simulation setup name.
	 * @return the current simulation setup name
	 */
	@XmlTransient
	public String getSimulationSetupCurrent() {
		return simulationSetupCurrent;
	}

	/**
	 * Controls and/or creates whether the sub-folder-Structure exists
	 * @return boolean true or false :-)
	 */
	public boolean checkAndCreateProjectsDirectoryStructure() {

		String newDirName = null;
		File file = null;
		boolean error = false;

		for (int i = 0; i < this.DEFAULT_SUB_FOLDERS.length; i++) {
			// --- Does the default folder exists ---------
			newDirName = this.getProjectFolderFullPath() + DEFAULT_SUB_FOLDERS[i];
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
	 * @param reason the notification reason object
	 */
	public void setNotChangedButNotify(Object reason) {
		this.setChanged();
		this.notifyObservers(reason);
	}

	/**
	 * To prevent the closing of the project without saving
	 * @param reason the notification reason object
	 */
	public void setChangedAndNotify(Object reason) {
		this.setUnsaved(true);
		this.setChanged();
		this.notifyObservers(reason);
	}

	/**
	 * Moves the requested Project-Window to the front.
	 * @param forceClassPathReload the indicator to force a reload resources
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
		Application.setTitleAddition(projectName);
		Application.setProjectFocused(this);
		Application.getProjectsLoaded().setProjectView();
		this.setMaximized();

		if (forceClassPathReload) {
			// --- ClassPath laden ------------------------
			this.resourcesLoad();
		}
	}

	/**
	 * Maximizes the AwbProjectWindow within the Application
	 */
	public void setMaximized() {
		AwbProjectWindow pew = this.getProjectEditorWindow();
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

		if (StringHelper.isEqualString(newProjectDescription, this.projectDescription)==true) return;
		
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
		this.setChanged();
		this.notifyObservers(CHANGED_ProjectFolder);
	}

	/**
	 * Returns the current project folder that is one sub-directory name of the Agent.Workbench projects directory.
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
			projectFolderFullPath = Application.getGlobalInfo().getPathProjects() + this.getProjectFolder() + File.separator;
		}
		return projectFolderFullPath;
	}
	/**
	 * Enables to set the full path of the project director.
	 * @param projectFolderFullPath the new project folder full path
	 */
	public void setProjectFolderFullPath(String projectFolderFullPath) {
		this.projectFolderFullPath = projectFolderFullPath;
	}
	
	/**
	 * Returns the projects security directory relative to Agent.Workbench projects directory (e.g. /myProject/security).
	 * @return the projects security folder
	 */
	public String getProjectSecurityFolder() {
		return projectFolder + File.separator + DEFAULT_SUB_FOLDER_SECURITY;
	}
	/**
	 * Returns the project security folder as full path. If the directory does not exists yet, it will be created.
	 * @return the project security folder full path
	 */
	public String getProjectSecurityFolderFullPath() {
		if (projectSecurtiyFolderFullPath==null) {
			projectSecurtiyFolderFullPath = Application.getGlobalInfo().getPathProjects() + this.getProjectSecurityFolder() + File.separator;
			File securityDir = new File(projectSecurtiyFolderFullPath);
			if (securityDir.exists()==false) {
				securityDir.mkdir();
			}
		}
		return projectSecurtiyFolderFullPath;
	}
	
	/**
	 * Returns the projects agent working directory relative to Agent.Workbench projects directory (e.g. /myProject/agentWorkingDirectory).
	 * @return the projectFolder
	 */
	public String getProjectAgentWorkingFolder() {
		String setupPath = PathHandling.getFileNameSuggestion(this.getSimulationSetupCurrent());
		return projectFolder + File.separator + DEFAULT_AGENT_WORKING_DIRECTORY + File.separator + setupPath;
	}
	/**
	 * Returns the project agent working folder as full path. If the directory does not exists yet, it will be created.
	 * @return the project agent working folder full path
	 */
	public String getProjectAgentWorkingFolderFullPath() {
		return getProjectAgentWorkingFolderFullPath(true);
	}
	/**
	 * Returns the project agent working folder as full path. Directory can be created, if it does'n exists.
	 *
	 * @param createDirIfNotExists set true if the directory should be created if not already done 
	 * @return the project agent working folder full path
	 */
	public String getProjectAgentWorkingFolderFullPath(boolean createDirIfNotExists) {
		String projectAgentWorkingFolderFullPath = Application.getGlobalInfo().getPathProjects() + this.getProjectAgentWorkingFolder() + File.separator;
		if (createDirIfNotExists==true) {
			File workingDir = new File(projectAgentWorkingFolderFullPath);
			if (workingDir.exists()==false) {
				workingDir.mkdirs();
			}
		}
		return projectAgentWorkingFolderFullPath;
	}
	
	/**
	 * Gets the projects temporary folder.
	 * @return the projects temporary folder
	 */
	public String getProjectTempFolderFullPath() {
		String tmpFolder = this.getProjectFolderFullPath() + DEFAULT_TEMP_FOLDER + File.separator;
		File tmpFile = new File(tmpFolder);
		if (tmpFile.exists()==false) {
			tmpFile.mkdir();
		}
		tmpFile.deleteOnExit();
		return tmpFolder;
	}

	// --- Version and Update information ---------------------------
	/**
	 * Sets the projects version as String.
	 * @param newVersion the new version string
	 */
	@XmlTransient
	public void setVersion(String newVersion) {
		if (newVersion==null || newVersion.isEmpty()) return;
		if (this.version!=null && this.version.equals(newVersion)==true) return;
		this.version = newVersion;
		this.setChangedAndNotify(CHANGED_Version);
	}
	/**
	 * Gets the projects {@link Version}.
	 * @return the project version information
	 */
	public Version getVersion() {
		Version versionInst = null;
		if (this.version==null) {
			String versionQualifier = ProjectRepositoryExport.getVersionQualifierForTimeStamp(System.currentTimeMillis());
			versionInst = Version.parseVersion("0.0.1." + versionQualifier);
			this.setChangedAndNotify(CHANGED_Version);
		} else {
			versionInst = Version.parseVersion(this.version);
		}
		return versionInst;
	}

	/**
	 * Sets the version tag.
	 * @param newVersionTag the new version tag
	 */
	public void setVersionTag(String newVersionTag) {
		if (newVersionTag==null || newVersionTag.isEmpty()) return;
		if (this.versionTag!=null && this.versionTag.equals(newVersionTag)==true) return;
		this.versionTag = newVersionTag;
		this.setChangedAndNotify(CHANGED_VersionTag);
	}
	/**
	 * Returns the version tag.
	 * @return the version tag
	 */
	@XmlTransient
	public String getVersionTag() {
		if (versionTag==null) {
			versionTag = DEFAULT_VERSION_TAG;
		}
		return versionTag;
	}
	
	/**
	 * Sets the update auto configuration (1-3), where <br> 
	 * 0 = automated update without further requests,<br> 
	 * 1 = do the download then ask and<br> 
	 * 2 = do nothing in an automated manner.
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
	 * @return the auto-update configuration, where <br> 
	 * 0 = automated update without further requests,<br> 
	 * 1 = do the download then ask and<br> 
	 * 2 = do nothing in an automated manner
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
		return this.getUpdateSite(false);
	}
	/**
	 * Return the update site. If the request is for the user visualization and the 
	 * update site is null, a '?' will be returned.
	 *
	 * @param isForVisualization the indicator if the return value is used for the end user visualization
	 * @return the update site
	 */
	public String getUpdateSite(boolean isForVisualization) {
		if (updateSite==null && isForVisualization==true) {
			return "NOT CONFIGURED YET";
		}
		return updateSite;
	}
	
	/**
	 * Sets the date of the last update check.
	 * @param updateDateLastChecked the new date for the last update check
	 */
	public void setUpdateDateLastChecked(long updateDateLastChecked) {
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
	public long getUpdateDateLastChecked() {
		return updateDateLastChecked;
	}

	/**
	 * Sets the update authentication.
	 * @param updateAuthorization the new update authorization
	 */
	public void setUpdateAuthorization(WebResourcesAuthorization updateAuthorization) {
		this.updateAuthorization = updateAuthorization;
		setUnsaved(true);
		setChanged();
		notifyObservers(CHANGED_UpdateAuthorization);
	}
	/**
	 * Returns the update authorization.
	 * @return the update authorization
	 */
	@XmlTransient
	public WebResourcesAuthorization getUpdateAuthorization() {
		if (updateAuthorization==null) {
			updateAuthorization = new WebResourcesAuthorization(AuthorizationType.NONE, null, null);
		}
		return updateAuthorization;
	}
	
	
	/**
	 * Does the project update check and installation.
	 *
	 * @param isManuallyExecutedByUser indicator that tells, if the call represents a user call
	 * @return the project in case that the calling method can continue
	 */
	public Project doProjectUpdate(boolean isManuallyExecutedByUser) {
		return doProjectUpdate(isManuallyExecutedByUser, false);
	}
	/**
	 * Does the project update check and installation.
	 *
	 * @param isManuallyExecutedByUser indicator that tells, if the call represents a user call
	 * @param isSkipTestOfLastDateChecked the is skip test of last date checked
	 * @return the project in case that the calling method can continue
	 */
	public Project doProjectUpdate(boolean isManuallyExecutedByUser, boolean isSkipTestOfLastDateChecked) {
		
		ProjectRepositoryUpdate pru = new ProjectRepositoryUpdate(this);
		
		ExecutionMode eMode = Application.getGlobalInfo().getExecutionMode();
		switch (eMode) {
		case APPLICATION:
			// --- Start update check after the project was opened --
			pru.setExecutedByUser(isManuallyExecutedByUser);
			if (isManuallyExecutedByUser==false) {
				pru.setUserRequestForDownloadAndInstallation(true);
				pru.setShowFinalUserMessage(false);
			}
			pru.start(); // seconds
			break;

		case DEVICE_SYSTEM:
			// --- Directly start the update check ------------------
			pru.setExecutedByUser(false);
			pru.setSkipTestOfLastDateChecked(isSkipTestOfLastDateChecked);
			pru.startInSameThread();
			if (pru.isSuccessfulUpdate()==true) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// --- Clean up & restart application -------
						Application.stopAgentWorkbench();
						Application.startAgentWorkbench();
					}
				}, "Project Update Restarter").start();
				return null;
			}
			break;
			
		default:
			// --- Nothing to do here -------------------------------
			break;
		}
		return this;
	}
	
	// --- Visualization instances ----------------------------------
	/**
	 * Checks if the project editor window is open.
	 * @return true, if the project editor window open
	 */
	public boolean isProjectEditorWindowOpen() {
		return !(this.projectEditorWindow==null);
	}
	/**
	 * Creates / Returns the project editor window (Swing or SWT).
	 * @return the project editor window for the current project
	 */
	@XmlTransient
	public synchronized AwbProjectWindow getProjectEditorWindow() {
		if (this.projectEditorWindow == null) {
			this.projectEditorWindow = AgentWorkbenchUiManager.getInstance().getProjectWindow(this);
			if (this.projectEditorWindow!=null) {
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
		AwbProjectWindow pew = this.getProjectEditorWindow();
		if (pew!=null && newProjectView.equals(this.projectView)==false) {

			// --- Change view ----------------------------
			this.projectView = newProjectView;
			this.setUnsaved(true);
			this.setChanged();
			this.notifyObservers(CHANGED_ProjectView);
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
	 * Toggles the view of the project tree.
	 */
	public void toggleViewProjectTree() {
		this.setProjectTreeVisible(!this.isProjectTreeVisible());
	}
	@XmlTransient
	public void setProjectTreeVisible(boolean isProjectTreeVisible) {
		this.projectTreeVisible = isProjectTreeVisible;
		this.setUnsaved(true);
		this.setChanged();
		if (Application.isOperatingHeadless()==false) {
			this.getProjectEditorWindow().setProjectTreeVisible(isProjectTreeVisible);
		}
	}
	public boolean isProjectTreeVisible() {
		return projectTreeVisible;
	}
	
	/**
	 * Toggles the view of the tab header of the {@link ProjectWindow}.
	 */
	public void toggleViewProjectTabHeader() {
		this.setProjectTabHeaderVisible(!this.isProjectTabHeaderVisible());		
	}
	@XmlTransient
	public void setProjectTabHeaderVisible(boolean isProjectTabHeaderVisible) {
		this.projectTabHeaderVisible = isProjectTabHeaderVisible;
		this.setUnsaved(true);
		this.setChanged();
		if (Application.isOperatingHeadless()==false) {
			this.getProjectEditorWindow().setProjectTabHeaderVisible(isProjectTabHeaderVisible);
		}
	}
	public boolean isProjectTabHeaderVisible() {
		return projectTabHeaderVisible;
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
	 * Gets the sub folder for setup environment files
	 * @param fullPath If true, the absolute path is returned, otherwise relative to the project folder
	 * @return The sub folder for setup environment files
	 */
	public String getEnvSetupPath(boolean fullPath) {
		if (fullPath == true) {
			return this.getProjectFolderFullPath() + DEFAULT_SUB_FOLDER_ENV_SETUPS;
		} else {
			return DEFAULT_SUB_FOLDER_ENV_SETUPS;
		}
	}

	/**
	 * Returns the sub folder for setups.
	 *
	 * @param fullPath the indicator to return the full path information
	 * @return the defaultSubFolderSetups
	 */
	public String getSubFolder4Setups(boolean fullPath) {
		if (fullPath == true) {
			return getProjectFolderFullPath() + DEFAULT_SUB_FOLDER_4_SETUPS + File.separator;
		} else {
			return DEFAULT_SUB_FOLDER_4_SETUPS;
		}
	}

	/**
	 * Gets the sub folder for environment setups.
	 * @return the defaultSubFolderEnvSetups
	 */
	public String getSubFolderEnvSetups() {
		return DEFAULT_SUB_FOLDER_ENV_SETUPS;
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
	 * Adds a new sub ontology to the current project.
	 * @param newSubOntology the new sub ontology
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
	 * Removes a new sub ontology from the current project ontology.
	 * @param subOntology2Remove the sub ontology to remove
	 */
	public void subOntologyRemove(String subOntology2Remove) {
		this.getSubOntologies().remove(subOntology2Remove);
		this.getOntologyVisualisationHelper().removeSubOntology(subOntology2Remove);
		this.setUnsaved(true);
		this.setChanged();
		this.notifyObservers(CHANGED_ProjectOntology);
	}

	/**
	 * Returns the ontology visualization helper for this project.
	 * @return the OntologyVisualisationHelper
	 */
	public OntologyVisualizationHelper getOntologyVisualisationHelper() {
		if (this.ontologyVisualisationHelper == null) {
			this.ontologyVisualisationHelper = new OntologyVisualizationHelper(this.getSubOntologies());
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
		this.setUnsaved(true);
		this.setChanged();
		this.notifyObservers(CHANGED_TimeModelClass);
	}
	/**
	 * Returns the time model class and configuration for this Project.
	 * @return the time model class
	 */
	@XmlTransient
	public String getTimeModelClass() {
		// --- Consider legacy package for TimeModels ---------------
		if (timeModelClass!=null && timeModelClass.startsWith("agentgui.simulationService.time.")==true) {
			timeModelClass = timeModelClass.replace("agentgui.simulationService.time.", "de.enflexit.awb.simulation.environment.time.");
			this.setUnsaved(true);
			this.setChanged();
		}
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
			this.timeModelController.initialize();
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
		this.setChangedAndNotify(CHANGED_ProjectProperties);
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
		if (this.userRuntimeObject==null) {
			this.setUserRuntimeObjectClassName(null);
		} else {
			this.setUserRuntimeObjectClassName(this.userRuntimeObject.getClass().getName());
		}
		this.setUnsaved(true);
		this.setChanged();
		this.notifyObservers(CHANGED_UserRuntimeObject);
	}
	
	/**
	 * Returns the user runtime object class name.
	 * @return the user runtime object class name
	 */
	@XmlTransient
	public String getUserRuntimeObjectClassName() {
		if (userRuntimeObjectClassName==null && this.getUserRuntimeObject()!=null) {
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
	
	
	/**
	 * Checks if a environment controller is initiated.
	 * @return true, if is environment controller initiated
	 */
	public boolean isEnvironmentControllerInitiated() {
		return !(this.environmentController==null);
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
	
}
