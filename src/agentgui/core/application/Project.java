package agentgui.core.application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.w3c.dom.Document;

import agentgui.core.agents.AgentConfiguration;
import agentgui.core.common.ClassLoaderUtil;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.gui.ProjectWindow;
import agentgui.core.gui.ProjectWindowTab;
import agentgui.core.gui.components.TabForSubPanes;
import agentgui.core.gui.projectwindow.BaseAgents;
import agentgui.core.gui.projectwindow.JadeSetup;
import agentgui.core.gui.projectwindow.OntologyTab;
import agentgui.core.gui.projectwindow.ProjectDesktop;
import agentgui.core.gui.projectwindow.ProjectInfo;
import agentgui.core.gui.projectwindow.ProjectResources;
import agentgui.core.gui.projectwindow.Visualization;
import agentgui.core.gui.projectwindow.simsetup.Distribution;
import agentgui.core.gui.projectwindow.simsetup.SimulationEnvironment;
import agentgui.core.gui.projectwindow.simsetup.StartSetup;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.jade.PlatformJadeConfig;
import agentgui.core.ontologies.Ontologies4Project;
import agentgui.core.plugin.PlugIn;
import agentgui.core.plugin.PlugInLoadException;
import agentgui.core.plugin.PlugInNotification;
import agentgui.core.plugin.PlugInsLoaded;
import agentgui.core.sim.setup.SimulationSetups;
import agentgui.physical2Denvironment.controller.Physical2DEnvironmentController;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;

@XmlRootElement public class Project extends Observable {

	// --- public statics --------------------------------------
	@XmlTransient public static final String SAVED = "ProjectSaved";
	
	@XmlTransient public static final String CHANGED_ProjectName = "ProjectName";
	@XmlTransient public static final String CHANGED_ProjectDescription = "ProjectDescription";
	@XmlTransient public static final String CHANGED_ProjectFolder= "ProjectFolder";
	@XmlTransient public static final String CHANGED_ProjectView = "ProjectView";
	@XmlTransient public static final String CHANGED_EnvironmentModel= "EnvironmentModel";
	@XmlTransient public static final String CHANGED_AgentStartConfiguration = "AgentConfiguration4StartArguments";
	@XmlTransient public static final String CHANGED_ProjectOntology = "ProjectOntology";
	@XmlTransient public static final String CHANGED_ProjectResources = "ProjectResources";
	
	@XmlTransient public static final String VIEW_Developer = "Developer";
	@XmlTransient public static final String VIEW_User = "User";
	
	// --- Constants -------------------------------------------
	@XmlTransient private String defaultSubFolderSetups    = "setups";
	@XmlTransient private String defaultSubFolderEnvSetups = "svgEnvSetups";
	@XmlTransient private String[] defaultSubFolders	   = { defaultSubFolderSetups,
															   defaultSubFolderEnvSetups, 
															  };
	
	@XmlTransient private static final String NewLine = Application.RunInfo.AppNewLineString();	
	@XmlTransient private static final String PathSep = Application.RunInfo.AppPathSeparatorString();
	
	// --- GUI der aktuellen Projekt-Instanz -------------------
	@XmlTransient public ProjectWindow projectWindow = null;
	@XmlTransient public JPanel projectVisualizationPanel = null;
	@XmlTransient public JDesktopPane projectDesktop = null;
	
	// --- Objekt- / Projektvariablen --------------------------
	@XmlTransient public boolean isUnsaved = false;
	@XmlTransient private String projectFolder;
	@XmlTransient private String projectFolderFullPath;
	@XmlTransient public Ontologies4Project ontologies4Project;

	//	@XmlTransient private Physical2DEnvironment environment;
	@XmlTransient private Physical2DEnvironmentController physical2DEnvironmentController;
	
	// --- Speichervariablen der Projektdatei -------------------------
	@XmlElement(name="projectName")
	private String projectName;
	@XmlElement(name="projectDescription")
	private String projectDescription;
	@XmlElement(name="projectView")
	private String projectView;			// --- Developer / End-User ---
	
	@XmlElement(name="environmentModel")
	private String environmentModel;	
	
	@XmlElementWrapper(name = "projectResources")
	@XmlElement(name="projectResource")
	public Vector<String> projectResources = new Vector<String>();

	@XmlElementWrapper(name="plugins")
	@XmlElement(name="className")
	public Vector<String> plugIns_Classes = new Vector<String>();
	@XmlTransient public PlugInsLoaded plugIns_Loaded = new PlugInsLoaded();
	@XmlTransient private boolean plugInVectorLoaded = false;
	
	@XmlTransient 
	public Vector<String> downloadResources = new Vector<String>();
	
	@XmlElementWrapper(name = "subOntologies")
	@XmlElement(name="subOntology")
	public Vector<String> subOntologies = new Vector<String>();
	
	@XmlElementWrapper(name = "agentConfiguration")
	public AgentConfiguration agentConfig = new AgentConfiguration(this);
	
	@XmlElement(name="simulationSetupCurrent")
	public String simSetupCurrent = null;
	@XmlElementWrapper(name = "simulationSetups")
	public SimulationSetups simSetups = new SimulationSetups(this, simSetupCurrent);
	
	@XmlElement(name="jadeConfiguration")
	public PlatformJadeConfig JadeConfiguration = new PlatformJadeConfig();
	
	/**
	 * Default-Constructor
	 */
	public Project() {
		// ---
	};
	
	/**
	 * This methods adds all default Agent.GUI Tabs to the ProjectWindow of the project
	 */
	public void addDefaultTabs() {
		
		ProjectWindowTab pwt = null;
		// ------------------------------------------------
		// --- General Informations -----------------------
		pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER, 
								   Language.translate("Info"), null, null, 
								   new ProjectInfo(this), null, null);
		pwt.add();
		
		// ------------------------------------------------
		// --- Configuration ------------------------------
		TabForSubPanes subPanes = new TabForSubPanes(); 
		pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, 
				   Language.translate("Konfiguration"), null, null, 
				   subPanes, null, subPanes.jTabbedPaneIntern);
		pwt.add();
		this.projectWindow.setProjectWindowTab4Configuration(pwt);
		
			// --- External Resources -------------------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, 
					   Language.translate("Ressourcen"), null, null, 
					   new ProjectResources(this), Language.translate("Konfiguration"), null);
			pwt.add();
			// --- Used Ontologies ----------------------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, 
					   Language.translate("Ontologien"), null, null, 
					   new OntologyTab(this), Language.translate("Konfiguration"), null);
			pwt.add();
			// --- Project Agents -----------------------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, 
					   Language.translate("Agenten"), null, null, 
					   new BaseAgents(this), Language.translate("Konfiguration"), null);
			pwt.add();
			// --- JADE-Configuration -------------------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_DEVELOPER, 
					   Language.translate("JADE-Konfiguration"), null, null, 
					   new JadeSetup(this), Language.translate("Konfiguration"), null);
			pwt.add();
		
		// ------------------------------------------------
		// --- Simulations-Setup --------------------------
		subPanes = new TabForSubPanes(); 
		pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER, 
				   Language.translate("Simulations-Setup"), null, null, 
				   subPanes, null, subPanes.jTabbedPaneIntern);
		pwt.add();
		this.projectWindow.setProjectWindowTab4SimulationSetup(pwt);
		
			// --- start configuration for agents ---------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER, 
					   Language.translate("Agenten-Start"), null, null, 
					   new StartSetup(this), Language.translate("Simulations-Setup"), null);
			pwt.add();
			// --- simulation environment -----------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION, 
					   Language.translate("Simulationsumgebung"), null, null, 
					   new SimulationEnvironment(this), Language.translate("Simulations-Setup"), null);
			pwt.add();
			// --- distribution + thresholds --------------
			pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER, 
					   Language.translate("Verteilung + Grenzwerte"), null, null, 
					   new Distribution(this), Language.translate("Simulations-Setup"), null);
			pwt.add();
			

		// --- SVG-visualisation --------------------------
		Visualization visualization = new Visualization(this);
		this.projectVisualizationPanel = visualization.getJPanel4Visualization();
		pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER_VISUALIZATION, 
				   Language.translate("Simulations-Visualisierung"), null, null, 
				   this.projectVisualizationPanel, null, null);
		pwt.add();
		// --- Project Desktop ----------------------------
		pwt = new ProjectWindowTab(this, ProjectWindowTab.DISPLAY_4_END_USER, 
				   Language.translate("Projekt-Desktop"), null, null, 
				   new ProjectDesktop(this), null, null);
		pwt.add();
		
		// --- Expand the tree view -----------------------
		this.projectWindow.projectTreeExpand2Level(3, true);
		
	}
	
	/**
	 * Save the current MAS-Project
	 */
	public boolean save() {
		// --- Save the current project -------------------
		Application.MainWindow.setStatusBar( projectName + ": " + Language.translate("speichern") + " ... ");
		try {			
			// --- prepare Context and Marshaller ---------
			JAXBContext pc = JAXBContext.newInstance( this.getClass() ); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
			pm.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 
			//pm.marshal( this, System.out );
			// --- Write values to xml-File ---------------
			Writer pw = new FileWriter( projectFolderFullPath + Application.RunInfo.getFileNameProject() );
			pm.marshal( this, pw );
			
			// --- Save the current SimulationSetup -------
			this.simSetups.setupSave();
			
			this.isUnsaved = false;			

			// --- Notification ---------------------------
			this.setNotChangedButNotify(Project.SAVED);

		} 
		catch (Exception e) {
			System.out.println("XML-Error while saving Project-File!");
			e.printStackTrace();
		}
		Application.MainWindow.setStatusBar("");
		return true;		
	}
	
	/**
	 * This method closes the current project and returns true if this was successful 
	 * @return
	 */
	public boolean close() {
		// --- Close project? -----------------------------
		String MsgHead = null;
		String MsgText = null;
		Integer MsgAnswer = 0;
		
		Application.MainWindow.setStatusBar(Language.translate("Projekt schließen") + " ...");
		if ( isUnsaved == true ) {
			MsgHead = Language.translate("Projekt '@' speichern?");
			MsgHead = MsgHead.replace( "'@'", "'" + projectName + "'");			
			MsgText = Language.translate(
						"Das aktuelle Projekt '@' ist noch nicht gespeichert!" + NewLine + 
						"Möchten Sie es nun speichern ?");
			MsgText = MsgText.replace( "'@'", "'" + projectName + "'");
			
			MsgAnswer = JOptionPane.showInternalConfirmDialog (Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_CANCEL_OPTION );
			if ( MsgAnswer == JOptionPane.CANCEL_OPTION ) {
				return false;
			} else if ( MsgAnswer == JOptionPane.YES_OPTION ) {
				if ( save()== false ) 
					return false;
			}
		}
		// --- ggf. noch Jade beenden ---------------------
		if (Application.JadePlatform.jadeStopAskUserBefore()==false) {
			return false;
		}

		// ------------------------------------------------		
		// --- Projekt kann geschlossen werden ------------
		// ------------------------------------------------
		// --- Clear PlugIns ------------------------------
		this.plugInVectorRemove();
		// --- Clear CLASSPATH ----------------------------
		this.resourcesRemove();
		
		// --- Close Project ------------------------------
		int Index = Application.Projects.getIndexByName( projectName ); // --- Merker Index ---		
		projectWindow.dispose();
		Application.Projects.remove(this);
		
		int NProjects = Application.Projects.count();
		if ( NProjects > 0 ) {
			if ( Index+1 > NProjects ) Index = NProjects-1;  
			Application.ProjectCurr = Application.Projects.get(Index);
			Application.ProjectCurr.setFocus(true);
			Application.setTitelAddition( Application.ProjectCurr.projectName );
		} else {
			Application.ProjectCurr = null;
			Application.MainWindow.setCloseButtonPosition( false );
			Application.setTitelAddition( "" );
		}
		Application.setStatusBar( "" );
		return true;
	}

	// ----------------------------------------------------------------------------------
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	// --- Here we come with methods for (un-) load ProjectPlugIns --- Start ------------ 
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ----------------------------------------------------------------------------------	
	/**
	 * This method will load the ProjectPlugIns, which are configured for the
	 * current project (plugins_Classes). It will be executed only one time during 
	 * the 'ProjectsLoaded.add()' execution. After this no further functionallity 
	 * can be expected. 
	 */
	public void plugInVectorLoad() {
		if (this.plugInVectorLoaded==false) {
			// --- load all plugins configured in 'plugIns_Classes' -----------
			for (int i = 0; i < this.plugIns_Classes.size(); i++) {
				if (this.plugInLoad(this.plugIns_Classes.get(i), false)== false ) {
					return;
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
		for (int i = this.plugIns_Loaded.size(); i>0; i--) {
			this.plugInRemove(this.plugIns_Loaded.get(i-1), false);
		}
		this.plugInVectorLoaded = false;
	}
	/**
	 * This method will reload the configured PlugIns
	 */
	public void plugInVectorReload() {
		// --- remove all loaded PlugIns ------------------
		this.plugInVectorRemove();
		// --- re-initialize the 'PlugInsLoaded'-Vector ---
		plugIns_Loaded = new PlugInsLoaded();
		// --- load all configured PlugIns to the project -
		this.plugInVectorLoad();
		
	}
	/**
	 * This method loads a single plugin given by its class reference
	 * @param pluginReference
	 */
	public boolean plugInLoad(String pluginReference, boolean add2ProjectReferenceVector) {
		
		String MsgHead = "";
		String MsgText = "";
			
		try {
			if (plugIns_Loaded.isLoaded(pluginReference)==true) {
				// --- PlugIn can't be loaded because it's already there ------
				PlugIn ppi = plugIns_Loaded.getPlugIn(pluginReference);
				
				MsgHead = Language.translate("Fehler - PlugIn: ") + ppi.getName() + " !" ;
				MsgText = Language.translate("Das PlugIn wurde bereits in das Projekt integriert " +
						"und kann deshalb nicht erneut hinzugefügt werden!");
				JOptionPane.showInternalMessageDialog( this.projectWindow, MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
				
			} else {
				// --- PlugIn can be loaded -----------------------------------
				PlugIn ppi = plugIns_Loaded.loadPlugin(this, pluginReference);
				this.setNotChangedButNotify(new PlugInNotification(PlugIn.ADDED, ppi));
				if (add2ProjectReferenceVector) {
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
	 * This method will unload and remove a single PlugIn 
	 * @param pluginReference
	 */
	public void plugInRemove(PlugIn plugIn, boolean removeFromProjectReferenceVector) {
		this.plugIns_Loaded.removePlugIn(plugIn);
		this.setNotChangedButNotify(new PlugInNotification(PlugIn.REMOVED, plugIn));
		if (removeFromProjectReferenceVector) {
			this.plugIns_Classes.remove(plugIn.getClassReference());
		}
	}
	// ----------------------------------------------------------------------------------
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// --- Here we come with methods for (un-) load ProjectPlugIns --- End --------------
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ----------------------------------------------------------------------------------
	
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
	 * Controls and/or Creates wether the Subfolder-Structure exists 
	 * @return boolean true or false
	 */	
	public boolean checkCreateSubFolders() {
		
		String NewDirName = null;
		File f = null;
		boolean Error = false;
		
		for (int i=0; i< defaultSubFolders.length; i++  ) {
			// --- ggf. Verzeichnis anlegen ---------------
			NewDirName = this.projectFolderFullPath + defaultSubFolders[i];
			f = new File(NewDirName);
			if ( f.isDirectory() == false) {
				// --- Verzeichnis anlegen ----------------
				if ( f.mkdir() == false ) {
					Error = true;	
				}				
			}
		};
		
		// --- Set Return-Value ---------------------------
		if ( Error == true ) {
			return false;
		}
		else {
			return true;	
		}		
	}
	
	/**
	 * To prevent to close the project without saving
	 * @param reason
	 */
	public void setNotChangedButNotify(Object reason) {
		setChanged();
		notifyObservers(reason);		
	}
	/**
	 * To prevent to close the project without saving
	 * @param reason
	 */
	public void setChangedAndNotify(Object reason) {
		isUnsaved = true;
		setChanged();
		notifyObservers(reason);		
	}
	
	/**
	 * Moves the requested Project-Window to the front
	 */
	public void setFocus(boolean forceClassPathReload) {
		
		if (forceClassPathReload) {
			// --- ggf. Jade beenden ----------------------
			if (Application.JadePlatform.jadeStopAskUserBefore()==false) {
				return;
			}
			// --- CLASSPATH entladen  --------------------
			Application.ProjectCurr.resourcesRemove();
		}
		
		projectWindow.moveToFront();
		Application.setTitelAddition( projectName );
		Application.ProjectCurr = this;
		Application.Projects.setProjectView();
		setMaximized();
		
		if (forceClassPathReload) {
			// --- CLASSPATH laden ------------------------
			Application.ProjectCurr.resourcesLoad();	
		}
	}
	/**
	 * Maximise the Project-Window within the AgenGUI-Application
	 */
	public void setMaximized() {
		Application.MainWindow.validate();
		((BasicInternalFrameUI) Application.ProjectCurr.projectWindow.getUI()).setNorthPane(null);
		Application.MainWindow.getJDesktopPane4Projects().getDesktopManager().maximizeFrame( projectWindow );				
	}
	
	/**
	 * @param projectFolder the projectName to set
	 */
	public void setProjectName(String newProjectName) {
		projectName = newProjectName;
		isUnsaved = true;
		setChanged();
		notifyObservers(CHANGED_ProjectName);
	}
	/**
	 * @return the projectName
	 */
	@XmlTransient
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * @param projectDescription the projectDescription to set
	 */
	
	public void setProjectDescription(String newProjectDescription) {
		projectDescription = newProjectDescription;
		isUnsaved = true;
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
	 * @param projectView the projectView to set
	 */
	@XmlTransient
	public void setProjectView(String projectView) {
		this.projectView = projectView;
		isUnsaved = true;
		setChanged();
		notifyObservers(CHANGED_ProjectView);
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
	 * @param environmentModel the environmentModel to set
	 */
	@XmlTransient
	public void setEnvironmentModel(String environmentModel) {
		this.environmentModel = environmentModel;
		this.isUnsaved = true;
		setChanged();
		notifyObservers(CHANGED_EnvironmentModel);
	}
	/**
	 * @return the environmentModel
	 */
	public String getEnvironmentModel() {
		return environmentModel;
	}
	/**
	 * @return the 'EnvironmentType' of the current EnvironmentModel
	 */
	public EnvironmentType getEnvironmentModelType() {
		return Application.RunInfo.getKnowEnvironmentTypes().getEnvironmentTypeByKey(this.environmentModel);
	}
	
	/**
	 * @param newProjectFolder the projectFolder to set
	 */
	@XmlTransient
	public void setProjectFolder(String newProjectFolder) {
		projectFolder = newProjectFolder;
		projectFolderFullPath = Application.RunInfo.PathProjects(true, false) + projectFolder + Application.RunInfo.AppPathSeparatorString();
		setChanged();
		notifyObservers(CHANGED_ProjectFolder);
	}
	/**
	 * @return the projectFolder
	 */
	public String getProjectFolder() {
		return projectFolder;
	}
	/**
	 * @return the ProjectFolderFullPath
	 */
	public String getProjectFolderFullPath() {
		return projectFolderFullPath;
	}
	
	/**
	 * Informs about changes at the AgentConfiguration 'AgentConfig'
	 */
	public void updateAgentReferences() {
		isUnsaved = true;
		setChanged();
		notifyObservers(CHANGED_AgentStartConfiguration);
	}
	
	/**
	 * Gets the default environment setup folder
	 * @return The default environment setup folder
	 */
	public String getEnvSetupPath(){
		return projectFolderFullPath+defaultSubFolderEnvSetups;
	}
	/**
	 * @return the environment
	 */
	@XmlTransient
	public Physical2DEnvironment getEnvironment() {
		if (this.physical2DEnvironmentController==null) {
			return null;
		} else {
			return this.physical2DEnvironmentController.getEnvironment();	
		}
	}
	public Physical2DEnvironment getEnvironmentCopy() {
		if (this.physical2DEnvironmentController==null) {
			return null;
		} else {
			return this.physical2DEnvironmentController.getEnvironmentCopy();	
		}
		
	}
	/**
	 * 
	 * @return The SVG document
	 */
	@XmlTransient
	public Document getSVGDoc(){
		if (this.physical2DEnvironmentController==null) {
			return null;
		} else {
			return this.physical2DEnvironmentController.getSvgDoc();	
		}
	}
	public Document getSVGDocCopy(){
		if (this.physical2DEnvironmentController==null) {
			return null;
		} else {
			return this.physical2DEnvironmentController.getSvgDocCopy();	
		}
	}	
	/**
	 * @param environment the environment to set
	 */
	public void setEnvironmentController(Physical2DEnvironmentController ec) {
		this.physical2DEnvironmentController = ec;
	}
	/**
	 * @return the current EnvironmentController
	 */
	@XmlTransient
	public Physical2DEnvironmentController getEnvironmentController() {
		return this.physical2DEnvironmentController;
	}
	/**
	 * @param defaultSubFolderSetups the defaultSubFolderOntology to set
	 */
	public void setSubFolderSetups(String newSubFolderSetups) {
		defaultSubFolderSetups = newSubFolderSetups;
	}
	/**
	 * @return the defaultSubFolderSetups
	 */
	public String getSubFolderSetups(boolean fullPath) {
		if (fullPath==true) {
			return getProjectFolderFullPath() + defaultSubFolderSetups + PathSep;
		} else {
			return defaultSubFolderSetups;	
		}
		
	}
	
	/**
	 * @param defaultSubFolderEnvSetups the defaultSubFolderEnvSetups to set
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
	 * Adds a new sub ontology to the current project onology 
	 * @param newSubOntology
	 */
	public void subOntologyAdd(String newSubOntology) {
		if (this.subOntologies.contains(newSubOntology)==false) {
			this.ontologies4Project.addSubOntology(newSubOntology);
			isUnsaved = true;
			setChanged();
			notifyObservers(CHANGED_ProjectOntology);
		} 
	}
	/**
	 * Removes a new sub ontology from the current project onology 
	 * @param removableSubOntology
	 */
	public void subOntologyRemove(String removableSubOntology) {
		this.ontologies4Project.removeSubOntology(removableSubOntology);
		isUnsaved = true;
		setChanged();
		notifyObservers(CHANGED_ProjectOntology);
	}
	
	/**
	 * This method removes adds external project resources (jars) to the CLATHPATH  
	 */
	public void resourcesLoad() {

		for(String jarFile : projectResources) {
			
			try {
				jarFile = ClassLoaderUtil.adjustPathForLoadin(jarFile, this.getProjectFolderFullPath());
				File file = new File(jarFile);
				ClassLoaderUtil.addFile(file.getAbsoluteFile());
				ClassLoaderUtil.addJarToClassPath(jarFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// --- Nach Agent-, Ontology- und BaseService - Classes suchen ----
		if (Application.ClassDetector == null) {
			Application.ClassDetector = new ClassSearcher(this);
		} else {
			Application.ClassDetector.reStartSearch(this, null);
		}
		
		this.setChangedAndNotify(CHANGED_ProjectResources);
	}
	/**
	 * This Method reloads the project resources in the CLASSPATH
	 */
	public void resourcesReLoad() {
		this.resourcesRemove();
		this.resourcesLoad();
	}
	
	/**
	 * This method removes all external project resources (jars) from the CLATHPATH  
	 */
	public void resourcesRemove() {
		
		for(String jarFile : projectResources) {
			
			try {
				jarFile = ClassLoaderUtil.adjustPathForLoadin(jarFile, this.getProjectFolderFullPath());
				ClassLoaderUtil.removeFile(jarFile);
				ClassLoaderUtil.removeJarFromClassPath(jarFile);
			} catch (RuntimeException e1) {
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
		this.setChangedAndNotify(CHANGED_ProjectResources);
	}

}
