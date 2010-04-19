package application;

import gui.ProjectWindow;

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

import sma.ontology.Environment;

import mas.agents.AgentConfiguration;
import mas.onto.OntologyClass;

@XmlRootElement public class Project extends Observable {

	// --- Konstanten ------------------------------------------
	@XmlTransient private static String[] DefaultSubFolders = {"agents", "ontology", "env-setups", "resources"};
	@XmlTransient private static final String NewLine = Application.RunInfo.AppNewLineString();	
	
	// --- GUI der aktuellen Projekt-Instanz -------------------
	@XmlTransient public ProjectWindow ProjectGUI = null;
	@XmlTransient public JDesktopPane ProjectDesktop = null;
	
	// --- Objekt- / Projektvariablen --------------------------
	@XmlTransient public boolean ProjectUnsaved = false;
	@XmlTransient private String ProjectFolder;
	@XmlTransient private String ProjectFolderFullPath;
	@XmlTransient private Vector<Class<?>> ProjectAgents;
	@XmlTransient public OntologyClass Ontology = new OntologyClass(this);
	@XmlTransient private Environment environment;
	
	
	/**
	 * @return the environment
	 */
	@XmlTransient
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	// --- Speichervariablen der Projektdatei ------------------ 
	@XmlElement(name="projectName")
	private String ProjectName;
	@XmlElement(name="projectDescription")
	private String ProjectDescription;
	
	private String svgFile;		// SVG-Datei
	private String envFile;		// Umgebungsdatei

	@XmlElementWrapper(name = "agentConfiguration")
	public AgentConfiguration AgentConfig = new AgentConfiguration(this);
		
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
			Writer pw = new FileWriter( ProjectFolderFullPath + Application.RunInfo.MASFile() );
			pm.marshal( this, pw );
			ProjectUnsaved = false;
		} 
		catch (Exception e) {
			System.out.println("XML - Fehler !");
			e.printStackTrace();
		}
//		this.saveEnvironment();
		Application.MainWindow.setStatusBar("");
		return true;		
	}
	
	public boolean close() {
		// --- Projekt schliessen ? -----------------------
		String MsgHead = null;
		String MsgText = null;

		Application.MainWindow.setStatusBar(Language.translate("Projekt schliessen") + " ...");
		if ( ProjectUnsaved == true ) {
			MsgHead = Language.translate("Projekt '@' speichern?");
			MsgHead = MsgHead.replace( "'@'", "'" + ProjectName + "'");			
			MsgText = Language.translate(
						"Das aktuelle Projekt '@' ist noch nicht gespeichert!" + NewLine + 
						"Möchten Sie es nun speichern ?");
			MsgText = MsgText.replace( "'@'", "'" + ProjectName + "'");
			
			Integer MsgAnswer = JOptionPane.showInternalConfirmDialog ( 
					Application.MainWindow.getContentPane(), 
					MsgText, MsgHead, JOptionPane.YES_NO_CANCEL_OPTION );
			if ( MsgAnswer == JOptionPane.CANCEL_OPTION ) {
				return false;
			}
			else if ( MsgAnswer == JOptionPane.YES_OPTION ) {
				if ( save()== false ) return false;
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
	 * Controls and/or Creates wether the Subfolder-Structure exists 
	 * @return boolean true or false
	 */	
	public boolean CheckCreateSubFolders() {
		
		String NewDirName = null;
		File f = null;
		boolean Error = false;
		
		for (int i=0; i< DefaultSubFolders.length; i++  ) {
			// --- ggf. Verzeichnis anlegen ---------------
			NewDirName = this.ProjectFolderFullPath + DefaultSubFolders[i];
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
	 * Moves the requested Projectwindow to the front
	 */
	public void setFocus() {
		ProjectGUI.moveToFront();
		Application.setTitelAddition( ProjectName );
		Application.ProjectCurr = this;
		Application.Projects.setProjectMenuItems();
		setMaximized();
	}

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
	 * @param projectAgents the projectAgents to set
	 */
	public void filterProjectAgents() {
		ProjectAgents = Application.JadePlatform.jadeGetAgentClasses( getProjectFolder() );
		setChanged();
		notifyObservers("ProjectAgents");
	}
	/**
	 * @return the projectAgents
	 */
	public Vector<Class<?>> getProjectAgents() {
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
	
	
	/**
	 * Getter für svgFile
	 * @return Dateiname der SVG-Datei
	 */
	public String getSvgFile(){
		return svgFile;
	}
	
	/**
	 * 
	 * @return Pfad zur SVG-Datei (Ressources-Ordner + Dateiname)
	 */
	public String getSvgPath(){
		return this.getProjectFolderFullPath()+"resources"+File.separator+this.svgFile;
	}

	/**
	 * Setter für svgFile 
	 * @param fileName Dateiname der SVG-Datei
	 */
	public void setSvgFile(String fileName){
		this.svgFile = fileName;	
		this.ProjectUnsaved = true;
	}	
	/**
	 * Getter für envFile
	 * @return Dateiname der Umgebungsdatei 
	 */
	public String getEnvFile(){
		return envFile;
	}
	
	/**
	 * 
	 * @return Pfad zur Umgebungsdatei (Ressources-Ordner + Dateiname)
	 */
	public String getEnvPath(){
		return this.getProjectFolderFullPath()+"resources"+File.separator+this.envFile;
	}
	
	/**
	 * Setter für envFile
	 * @param fileName Dateiname der Umgebungsdatei
	 */
	public void setEnvFile(String fileName){
		this.envFile = fileName;
		this.ProjectUnsaved = true;
	}

}
