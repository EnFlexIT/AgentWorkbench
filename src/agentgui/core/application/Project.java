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
import agentgui.core.gui.ProjectWindow;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.jade.PlatformJadeConfig;
import agentgui.core.ontologies.Ontologies4Project;
import agentgui.core.sim.setup.SimulationSetups;
import agentgui.physical2Denvironment.EnvironmentController;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;



@XmlRootElement public class Project extends Observable {

	// --- Konstanten ------------------------------------------
	@XmlTransient private String defaultSubFolderSetups    = "setups";
	@XmlTransient private String defaultSubFolderEnvSetups = "svgEnvSetups";
	@XmlTransient private String[] defaultSubFolders	   = { defaultSubFolderSetups,
															   defaultSubFolderEnvSetups, 
															  };
	
	@XmlTransient private static final String NewLine = Application.RunInfo.AppNewLineString();	
	@XmlTransient private static final String PathSep = Application.RunInfo.AppPathSeparatorString();
	
	// --- GUI der aktuellen Projekt-Instanz -------------------
	@XmlTransient public ProjectWindow ProjectGUI = null;
	@XmlTransient public JPanel ProjectVisualizationPanel = null;
	@XmlTransient public JDesktopPane ProjectDesktop = null;
	
	// --- Objekt- / Projektvariablen --------------------------
	@XmlTransient public boolean ProjectUnsaved = false;
	@XmlTransient private String ProjectFolder;
	@XmlTransient private String ProjectFolderFullPath;
	@XmlTransient public Ontologies4Project ontologies4Project;

	//	@XmlTransient private Physical2DEnvironment environment;
	@XmlTransient private EnvironmentController environmentController;
	
	// --- Speichervariablen der Projektdatei ------------------ 
	@XmlElement(name="projectName")
	private String ProjectName;
	@XmlElement(name="projectDescription")
	private String ProjectDescription;

	@XmlElementWrapper(name = "projectResources")
	@XmlElement(name="projectResource")
	public Vector<String> projectResources = new Vector<String>();
	@XmlTransient 
	public Vector<String> downloadResources = new Vector<String>();
	
	@XmlElementWrapper(name = "subOntologies")
	@XmlElement(name="subOntology")
	public Vector<String> subOntologies = new Vector<String>();
	
	@XmlElementWrapper(name = "agentConfiguration")
	public AgentConfiguration AgentConfig = new AgentConfiguration(this);
	
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
	};
	
	/**
	 * Save the current MAS-Project
	 */
	public boolean save() {
		// --- Speichern des aktuellen Projekts ------------
		Application.MainWindow.setStatusBar( ProjectName + ": " + Language.translate("speichern") + " ... ");
		try {			
			// --- Kontext und Marshaller vorbereiten ------
			JAXBContext pc = JAXBContext.newInstance( this.getClass() ); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
			pm.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 
			//pm.marshal( this, System.out );
			// --- Objektwerte in xml-Datei schreiben -----
			Writer pw = new FileWriter( ProjectFolderFullPath + Application.RunInfo.getFileNameProject() );
			pm.marshal( this, pw );
			
			// --- Speichern des aktuellen Sim-Setups -----
			this.simSetups.setupSave();
			
			// --- Speichern von Umgebung und SVG ---------
			this.environmentController.save();
			
			ProjectUnsaved = false;			
		} 
		catch (Exception e) {
			System.out.println("XML-Error while saving Project-File!");
			e.printStackTrace();
		}
		Application.MainWindow.setStatusBar("");
		return true;		
	}
	
	public boolean close() {
		// --- Projekt schließen ? -----------------------
		String MsgHead = null;
		String MsgText = null;
		Integer MsgAnswer = 0;
		
		Application.MainWindow.setStatusBar(Language.translate("Projekt schließen") + " ...");
		if ( ProjectUnsaved == true ) {
			MsgHead = Language.translate("Projekt '@' speichern?");
			MsgHead = MsgHead.replace( "'@'", "'" + ProjectName + "'");			
			MsgText = Language.translate(
						"Das aktuelle Projekt '@' ist noch nicht gespeichert!" + NewLine + 
						"Möchten Sie es nun speichern ?");
			MsgText = MsgText.replace( "'@'", "'" + ProjectName + "'");
			
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
		// --- Clear CLASSPATH ----------------------------
		this.resourcesRemove();
		
		// --- Close Project ------------------------------
		int Index = Application.Projects.getIndexByName( ProjectName ); // --- Merker Index ---		
		ProjectGUI.dispose();
		Application.Projects.remove(this);
		
		int NProjects = Application.Projects.count();
		if ( NProjects > 0 ) {
			if ( Index+1 > NProjects ) Index = NProjects-1;  
			Application.ProjectCurr = Application.Projects.get(Index);
			Application.ProjectCurr.setFocus(true);
			Application.setTitelAddition( Application.ProjectCurr.ProjectName );
		} else {
			Application.ProjectCurr = null;
			Application.Projects.setProjectMenuItems();
			Application.MainWindow.setCloseButtonPosition( false );
			Application.setTitelAddition( "" );
		}
		Application.setStatusBar( "" );
		return true;
	}
	
	/**
	 * This Procedure creates the default Project-Structure  for a new project. It creates the 
	 * deault folders ('agents' 'ontology' 'envSetups' 'resources') and creates default fils
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
			NewDirName = this.ProjectFolderFullPath + defaultSubFolders[i];
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
		ProjectUnsaved = true;
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
		
		ProjectGUI.moveToFront();
		Application.setTitelAddition( ProjectName );
		Application.ProjectCurr = this;
		Application.Projects.setProjectMenuItems();
		setMaximized();
		
		if (forceClassPathReload) {
			// --- CLASSPATH laden ------------------------
			Application.ProjectCurr.resourcesLoad();	
		}
	}
	/**
	 * Maximze the Project-Window within the AgenGUI-Application
	 */
	public void setMaximized() {
		Application.MainWindow.validate();
		((BasicInternalFrameUI) Application.ProjectCurr.ProjectGUI.getUI()).setNorthPane(null);
		Application.MainWindow.ProjectDesktop.getDesktopManager().maximizeFrame( ProjectGUI );				
	}
	
	/**
	 * @param projectFolder the projectName to set
	 */
	public void setProjectName(String projectName) {
		ProjectName = projectName;
		ProjectUnsaved = true;
		setChanged();
		notifyObservers( "ProjectName" );
	}
	/**
	 * @return the projectName
	 */
	@XmlTransient
	public String getProjectName() {
		return ProjectName;
	}
	
	/**
	 * @param projectDescription the projectDescription to set
	 */
	
	public void setProjectDescription(String projectDescription) {
		ProjectDescription = projectDescription;
		ProjectUnsaved = true;
		setChanged();
		notifyObservers( "ProjectDescription" );
	}
	/**
	 * @return the projectDescription
	 */
	@XmlTransient
	public String getProjectDescription() {
		return ProjectDescription;
	}
	
	/**
	 * @param projectFolder the projectFolder to set
	 */
	@XmlTransient
	public void setProjectFolder(String projectFolder) {
		ProjectFolder = projectFolder;
		ProjectFolderFullPath = Application.RunInfo.PathProjects(true, false) + ProjectFolder + Application.RunInfo.AppPathSeparatorString();
		setChanged();
		notifyObservers( "ProjectFolder" );
	}
	/**
	 * @return the projectFolder
	 */
	public String getProjectFolder() {
		return ProjectFolder;
	}
	/**
	 * @return the ProjectFolderFullPath
	 */
	public String getProjectFolderFullPath() {
		return ProjectFolderFullPath;
	}
	
	/**
	 * Informs about changes at the AgentConfiguration 'AgentConfig'
	 */
	public void updateAgentReferences() {
		ProjectUnsaved = true;
		setChanged();
		notifyObservers("AgentReferences");
	}
	
	/**
	 * Gets the default environment setup folder
	 * @return The default environment setup folder
	 */
	public String getEnvSetupPath(){
		return ProjectFolderFullPath+defaultSubFolderEnvSetups;
	}
	/**
	 * @return the environment
	 */
	@XmlTransient
	public Physical2DEnvironment getEnvironment() {
		return this.environmentController.getEnvironment();
	}
	/**
	 * 
	 * @return The SVG document
	 */
	@XmlTransient
	public Document getSVGDoc(){
		return this.environmentController.getSvgDoc();
	}
	/**
	 * @param environment the environment to set
	 */
	public void setEnvironmentController(EnvironmentController ec) {
		this.environmentController = ec;
	}
	/**
	 * @return the current EnvironmentController
	 */
	@XmlTransient
	public EnvironmentController getEnvironmentController() {
		return this.environmentController;
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
			ProjectUnsaved = true;
			setChanged();
			notifyObservers("ProjectOntology");
		} 
	}
	/**
	 * Removes a new sub ontology from the current project onology 
	 * @param removableSubOntology
	 */
	public void subOntologyRemove(String removableSubOntology) {
		this.ontologies4Project.removeSubOntology(removableSubOntology);
		ProjectUnsaved = true;
		setChanged();
		notifyObservers("ProjectOntology");
	}
	
	/**
	 * This method removes adds external project resources (jars) to the CLATHPATH  
	 */
	public void resourcesLoad() {

		for(String jarFile : projectResources) {
			
			try {
				jarFile = ClassLoaderUtil.adjustPathForLoadin(jarFile, this.getProjectFolder(), this.getProjectFolderFullPath());
				File file = new File(jarFile);
				ClassLoaderUtil.addFile(file.getAbsoluteFile());
				ClassLoaderUtil.addJarToClassPath(jarFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// --- Nach Agent-, Ontology- und BaseService - Classes suchen ----
		if (Application.classDetector == null) {
			Application.classDetector = new ClassSearcher(this);
		} else {
			Application.classDetector.reStartSearch(this, null);
		}
		
		this.setChangedAndNotify("projectResources");
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
				jarFile = ClassLoaderUtil.adjustPathForLoadin(jarFile, this.getProjectFolder(), this.getProjectFolderFullPath());
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
		this.setChangedAndNotify("projectResources");
	}
	
}
