package application;

import gui.ProjectWindow;
import jade.core.Agent;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import mas.PlatformJadeConfig;
import mas.agents.AgentConfiguration;
import mas.display.ontology.Environment;
import mas.environment.EnvironmentController;
import mas.onto.Ontologies4Project;
import rollout.TextFileRessourceRollOut;
import sim.setup.SimulationSetups;

@XmlRootElement public class Project extends Observable {

	// --- Konstanten ------------------------------------------
	@XmlTransient private String defaultSubFolderAgents 	= "agents";
	@XmlTransient private String defaultSubFolderOntology 	= "ontology";
	@XmlTransient private String defaultSubFolderSetups 	= "setups";
	@XmlTransient private String defaultSubFolderEnvSetups 	= "envSetups";
	@XmlTransient private String defaultSubFolderResources 	= "resources";
	@XmlTransient private String[] defaultSubFolders	= { defaultSubFolderAgents, 
															defaultSubFolderOntology, 
															defaultSubFolderSetups,
															defaultSubFolderEnvSetups, 
															defaultSubFolderResources
															};
	
	@XmlTransient private static final String NewLine = Application.RunInfo.AppNewLineString();	
	
	// --- GUI der aktuellen Projekt-Instanz -------------------
	@XmlTransient public ProjectWindow ProjectGUI = null;
	@XmlTransient public JDesktopPane ProjectDesktop = null;
	
	// --- Objekt- / Projektvariablen --------------------------
	@XmlTransient public boolean ProjectUnsaved = false;
	@XmlTransient private String ProjectFolder;
	@XmlTransient private String ProjectFolderFullPath;
	@XmlTransient private String MainPackage;
	@XmlTransient private Vector<Class<? extends Agent>> ProjectAgents;
	@XmlTransient public Ontologies4Project ontologies4Project;

	//	@XmlTransient private Environment environment;
	@XmlTransient private EnvironmentController environmentController;
	
	// --- Speichervariablen der Projektdatei ------------------ 
	@XmlElement(name="projectName")
	private String ProjectName;
	@XmlElement(name="projectDescription")
	private String ProjectDescription;
	
	private String svgFile;		// SVG-Datei
	private String envFile;		// Umgebungsdatei

	@XmlElementWrapper(name = "subOntologies")
	@XmlElement(name="subOntology")
	public Vector<String> subOntologies = new Vector<String>();
	
	@XmlElementWrapper(name = "agentConfiguration")
	public AgentConfiguration AgentConfig = new AgentConfiguration(this);
	
	@XmlElement(name="simulationSetupCurrent")
	public String simSetupCurrent = "default";
	@XmlElementWrapper(name = "simulationSetups")
	public SimulationSetups simSetups = new SimulationSetups(this, simSetupCurrent);
	
	@XmlElement(name="jadeConfiguration")
	public PlatformJadeConfig JadeConfiguration = new PlatformJadeConfig();
	private String JarFileName;
	
	
	
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
			if (this.simSetups != null)
				this.simSetups.setupSave();
			
			// --- Verzeichnis der Sim.-Setups aufräumen --
			this.cleanUpSubFolderSimulationSetup();
			
			// --- Speichern von Umgebung und SVG ---------
			if(this.svgFile != null){
				this.environmentController.saveSVG();
			}
			if(this.envFile != null){
				this.environmentController.saveEnvironment();
			}
			
			ProjectUnsaved = false;			
		} 
		catch (Exception e) {
			System.out.println("XML-Error while saving Project-File!");
			e.printStackTrace();
		}
//		this.saveEnvironment();
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
		
		// --- Projekt kann geschlossen werden ------------
		int Index = Application.Projects.getIndexByName( ProjectName ); // --- Merker Index ---		
		ProjectGUI.dispose();
		Application.Projects.remove(this);
		
		int NProjects = Application.Projects.count();
		if ( NProjects > 0 ) {
			if ( Index+1 > NProjects ) Index = NProjects-1;  
			Application.ProjectCurr = Application.Projects.get(Index);
			Application.ProjectCurr.setFocus();
			Application.setTitelAddition( Application.ProjectCurr.ProjectName );
		}
		else {
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
		// --- create Project-Ontolgy ---------------------
		this.createDefaultProjectOntology();
	}
	/**
	 * Controls and/or Creates wether the Subfolder-Structure exists 
	 * @return boolean true or false
	 */	
	private boolean checkCreateSubFolders() {
		
		String NewDirName = null;
		File f = null;
		boolean Error = false;
		
		for (int i=0; i< defaultSubFolders.length; i++  ) {
			// --- ggf. Verzeichnis anlegen ---------------
			NewDirName = this.ProjectFolderFullPath + defaultSubFolders[i];
			f = new File(NewDirName);
			if ( f.isDirectory() ) {
				// => Do nothing (yet)
			} 
			else {
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
	 * This Method scans the Folder of the Simulation-Setups and
	 * deletes all file, which are not used in this project
	 */
	public void cleanUpSubFolderSimulationSetup() {
		
		String pathSimXML  = this.getProjectFolderFullPath() + this.getSubFolderSetups() + Application.RunInfo.AppPathSeparatorString();
		File[] files = new File(pathSimXML).listFiles();
		if (files != null) {
			// --- Auflistung der Dateien durchlaufen -----
			for (int i = 0; i < files.length; i++) {
				// --- Nur xml-Dateien beachten -----------
				if (files[i].getName().endsWith(".xml")) {
					if (simSetups.containsValue(files[i].getName())==false) {
						files[i].delete();
					}
				}
			}
			// --------------------------------------------
		}
		
	}
	
	/**
	 * Here the default project-Ontolgy will be created (copied)
	 */
	private void createDefaultProjectOntology() {
			
		String srcReference 	 = "mas/onto/" + Application.RunInfo.getFileNameProjectOntology() + ".txt"; 
		String destPath 		 = this.ProjectFolderFullPath + defaultSubFolderOntology + "\\" + Application.RunInfo.getFileNameProjectOntology() + ".java";
		String destPackageString = "package " + this.ProjectFolder + "." + defaultSubFolderOntology;
		String fileContent		 = null;
		
		TextFileRessourceRollOut txtRollOut = new TextFileRessourceRollOut(srcReference, destPath, false);
		fileContent = txtRollOut.getFileString();
		fileContent = fileContent.replace("[DestinationPackage]", destPackageString);
		txtRollOut.writeFile(fileContent);

	}
	
	public void setChangedAndNotify(String reason) {
		ProjectUnsaved = true;
		setChanged();
		notifyObservers(reason);		
	}
	
	/**
	 * Moves the requested Project-Window to the front
	 */
	public void setFocus() {
		ProjectGUI.moveToFront();
		Application.setTitelAddition( ProjectName );
		Application.ProjectCurr = this;
		Application.Projects.setProjectMenuItems();
		setMaximized();
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
		if ( Application.RunInfo.AppExecutedOver() == "IDE" ) {
			ProjectFolderFullPath = Application.RunInfo.PathProjects(true, false) + ProjectFolder + Application.RunInfo.AppPathSeparatorString();
		} else {
			ProjectFolderFullPath = ProjectFolder;
		}
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
	 * @return the MainPackage
	 */
	public String getMainPackage() {
		return MainPackage;
	}
	/**
	 * @param mainPackage the mainPackage to set
	 */
	public void setMainPackage(String mainPackage) {
		MainPackage = mainPackage;
		ProjectUnsaved = true;
		setChanged();
		notifyObservers( "MainPackage" );
	}
	
	/**
	 * @return the JarFileName
	 */
	public String getJarFileName() {
		return JarFileName;
	}
	/**
	 * @param mainPackage the mainPackage to set
	 */
	public void setJarFileName(String jarFileName) {
		JarFileName = jarFileName;
		ProjectUnsaved = true;
		setChanged();
		notifyObservers( "JarFileName" );
	}
	
	/**
	 * @param projectAgents the projectAgents to set
	 */
	public void filterProjectAgents() {
		String jarPathName=null;
		if(getJarFileName()!=null && !getJarFileName().equals("")){
			jarPathName=getProjectFolderFullPath()+getJarFileName();
		}
		ProjectAgents = Application.JadePlatform.jadeGetAgentClasses( this.getMainPackage(), jarPathName);
		setChanged();
		notifyObservers("ProjectAgents");
	}
	
	/**
	 * @return the projectAgents
	 */
	public Vector<Class<? extends Agent>> getProjectAgents() {
		return ProjectAgents;
	}
	/**
	 * Informs about changes at the AgentConfiguration 'AgentConfig'
	 */
	public void updateAgentReferences() {
		ProjectUnsaved = true;
		setChanged();
		notifyObservers("AgentReferences");
	}
	
	public String getSvgFile(){
		return svgFile;
	}
	
	
	/**
	 * Gets the SVG file path
	 * @return The SVG file path
	 */
	public String getSvgPath(){
		if(this.svgFile != null){
			return getEnvSetupPath()+File.separator+this.svgFile;
		}else{
			return null;
		}
	}

	/**
	 * Setter for svgFile 
	 * @param fileName Name of the SVG file
	 */
	public void setSvgFile(String fileName){
		this.svgFile = fileName;	
		this.ProjectUnsaved = true;
	}
	public String getEnvFile(){
		return envFile;
	}
	/**
	 * Gets the environment file path
	 * @return The environment file path
	 */
	public String getEnvPath(){
		if(this.envFile != null){
			return getEnvSetupPath()+File.separator+this.envFile;
		}else{
			return null;
		}		
	}
	
	/**
	 * Setter für envFile
	 * @param fileName Dateiname der Umgebungsdatei
	 */
	public void setEnvFile(String fileName){
		this.envFile = fileName;
		this.ProjectUnsaved = true;
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
	public Environment getEnvironment() {
		return this.environmentController.getEnvironment();
	}
	/**
	 * @param environment the environment to set
	 */
	public void setEnvironmentController(EnvironmentController ec) {
		this.environmentController = ec;
	}

	
	/**
	 * @param defaultSubFolderAgents the defaultSubFolderAgents to set
	 */
	public void setSubFolderAgents(String newSubFolderAgents) {
		defaultSubFolderAgents = newSubFolderAgents;
	}
	/**
	 * @return the defaultSubFolderAgents
	 */
	public String getSubFolderAgents() {
		return defaultSubFolderAgents;
	}

	/**
	 * @param defaultSubFolderOntology the defaultSubFolderOntology to set
	 */
	public void setSubFolderOntology(String newSubFolderOntology) {
		defaultSubFolderOntology = newSubFolderOntology;
	}
	/**
	 * @return the defaultSubFolderOntology
	 */
	public String getSubFolderOntology() {
		return defaultSubFolderOntology;
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
	public String getSubFolderSetups() {
		return defaultSubFolderSetups;
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
	 * @param defaultSubFolderResources the defaultSubFolderResources to set
	 */
	public void setSubFolderResources(String newSubFolderResources) {
		defaultSubFolderResources = newSubFolderResources;
	}
	/**
	 * @return the defaultSubFolderResources
	 */
	public String getSubFolderResources() {
		return defaultSubFolderResources;
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

	
	
}
