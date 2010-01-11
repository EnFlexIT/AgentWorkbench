package application;

import gui.ProjectWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import mas.environment.Playground;

@XmlRootElement public class Project extends Observable {

	// --- Konstanten ------------------------------------------
	@XmlTransient private static String[] DefaultSubFolders = {"agents", "ontology", "ressources"};
	@XmlTransient private static String NewLine = Application.RunInfo.AppNewLineString();	
	
	// --- GUI der aktuellen Projekt-Instanz -------------------
	@XmlTransient public ProjectWindow ProjectGUI = null;
	
	// --- Objekt- / Projektvariablen --------------------------
	@XmlTransient public boolean ProjectUnsaved = false;
	@XmlTransient private String ProjectFolder;
	@XmlTransient private String ProjectFolderFullPath;
	@XmlTransient private Vector<Class<?>> ProjectAgents;
	@XmlTransient private Playground mainPlayground;	// Umgebung, enthält Agenten und Objekte 
	
	// --- Speichervariablen der Projektdatei ------------------ 
	private String ProjectName;
	private String ProjectDescription;
	private String svgFileName;		// Datei muss im Ordner Ressources liegen
	
	
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
		this.saveEnvironment();
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
	public String getProjectDescription() {
		return ProjectDescription;
	}
	
	/**
	 * @param projectFolder the projectFolder to set
	 */
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
	 * @param projectAgents the projectAgents to set
	 */
	public void filterProjectAgents() {
		String FolderFilter = Application.RunInfo.PathProjects(false, true) + getProjectFolder();
		ProjectAgents = Application.JadePlatform.jadeGetAgentClasses( FolderFilter );
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
	 * @return Dateiname der SVG-Datei
	 */
	public String getSvgFileName(){
		return this.svgFileName;
	}
	/**
	 * @return Pfad zur SVG Datei
	 */
	public String getSvgPath(){
		return this.ProjectFolderFullPath+"ressources"+File.separator+this.svgFileName;
	}
	/**
	 * Setzt die SVG-Datei (muss unterhalb von ressources liegen)
	 * @param fileName Dateiname
	 */
	public void setSvgFileName(String fileName){
		this.svgFileName = fileName;
	}
	/**
	 * @return Umgebung
	 */
	public Playground getEnvironment(){
		return mainPlayground;		
	}
	/**
	 * Läd die Umgebung, Pfad z.Zt. noch hartcodiert
	 */
	public void loadEnvironment(){
		File pgFile = new File(this.ProjectFolderFullPath+"ressources"+File.separator+"environment.ser");
				
		if(pgFile.exists()){
			try {
				System.out.println("Loading environment from "+pgFile.getPath());
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(pgFile));
				mainPlayground = (Playground) is.readObject();
				if(mainPlayground != null){
					System.out.println("Playground size"+mainPlayground.getWidth()+"x"+mainPlayground.getHeight());
					System.out.println("Objects: "+mainPlayground.getObjects().size()+" (including Agents)");
					System.out.println("Agents: "+mainPlayground.getAgents().size());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			
		}else{
			System.out.println("No environment file specified");
		}
		
	}
	/**
	 * Speichert die Umgebung, Pfad z.Zt. noch hartcodiert
	 */
	public void saveEnvironment(){
		File pgFile = new File(this.ProjectFolderFullPath+"ressources"+File.separator+"environment.ser");
		if(!pgFile.exists()){
			try {
				pgFile.createNewFile();				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream fs;
		try {
			fs = new FileOutputStream(pgFile);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(mainPlayground);
			System.out.println("Saving Playground, size"+mainPlayground.getWidth()+"x"+mainPlayground.getHeight());
			System.out.println("Objects: "+mainPlayground.getObjects().size()+" (including Agents)");
			System.out.println("Agents: "+mainPlayground.getAgents().size());
			os.close();
			fs.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
