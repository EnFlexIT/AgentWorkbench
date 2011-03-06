package agentgui.core.application;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import agentgui.core.gui.ProjectNewOpen;
import agentgui.core.gui.ProjectWindow;
import agentgui.core.ontologies.Ontologies4Project;
import agentgui.core.sim.setup.SimulationSetups;


public class ProjectsLoaded {

	// --- Listing of the open projects -------------------
	private ArrayList<Project> projectsOpen = new ArrayList<Project>();
	
	/**
	 * Adding (Creating or Opening) a new Project to the Application
	 * @param addNew
	 * @return Project
	 */
	public Project add (boolean addNew) {

		String actionTitel = null;
		String projectNameTest = null;
		String projectFolderTest = null;
		String localTmpProjectName = null;
		String localTmpProjectFolder = null;
		
		// ------------------------------------------------
		// --- Define a new Project-Instance -------------- 
		Project newProject = new Project();
		
		// ------------------------------------------------
		// --- Startbedingenen für "New" oder "Open" ------
		if ( addNew == true ){
			// --------------------------------------------
			// --- Anlegen eines neuen Projekts -----------
			actionTitel = Language.translate("Neues Projekt anlegen");
			
			// --- Neuen, allgemeinen Projektnamen finden -----		
			String ProjectNamePrefix = Language.translate("Neues Projekt");
			projectNameTest = ProjectNamePrefix;
			int Index = Application.Projects.getIndexByName(projectNameTest);
			int i = 2;
			while ( Index != -1 ) {
				projectNameTest = ProjectNamePrefix + " " + i;
				Index = Application.Projects.getIndexByName( projectNameTest );
				i++;
			}
			projectFolderTest = projectNameTest.toLowerCase().replace(" ", "_");
		}
		else {
			// --------------------------------------------
			// --- Öffnen eine vorhandenen Projekts -------
			actionTitel = Language.translate("Projekt öffnen");			
		}
		Application.MainWindow.setStatusBar(actionTitel + " ...");
		
		// ------------------------------------------------
		// --- Benutzer-Dialog öffnen ---------------------
		ProjectNewOpen NewProDia = new ProjectNewOpen( Application.MainWindow, Application.RunInfo.getApplicationTitle() + ": " + actionTitel, true, addNew );
		NewProDia.setVarProjectName( projectNameTest );
		NewProDia.setVarProjectFolder( projectFolderTest );
		NewProDia.setVisible(true);
		// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
		if ( NewProDia.isCanceled() == true ) {
			Application.setStatusBar( Language.translate("Fertig") );
			return null;
		} else {
			localTmpProjectName = NewProDia.getVarProjectName();
			localTmpProjectFolder = NewProDia.getVarProjectFolder(); 
		}
		NewProDia.dispose();
		NewProDia = null;	

		// ------------------------------------------------
		// --- ClassLoader entladen -----------------------
		if(projectsOpen.size()!=0) {
			Application.ProjectCurr.resourcesRemove();
		}
		
		// ------------------------------------------------
		// --- Projektvariablen setzen --------------------
		newProject.setProjectName( localTmpProjectName );
		newProject.setProjectFolder( localTmpProjectFolder );

		if ( addNew == true ) {			
			// --- Standardstruktur anlegen ---------------
			newProject.createDefaultProjectStructure();
		} 
		else {
			// --- XML-Datei einlesen ---------------------
			JAXBContext pc;
			Unmarshaller um = null;
			String XMLFileName = newProject.getProjectFolderFullPath() + Application.RunInfo.getFileNameProject();			
			try {
				pc = JAXBContext.newInstance( newProject.getClass() );
				um = pc.createUnmarshaller();
				newProject = (Project) um.unmarshal( new FileReader( XMLFileName ) );
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			// --- Folder auf aktuellen Projektordner einstellen ---
			newProject.setProjectFolder( localTmpProjectFolder );	
			
			// --- check/create default folders -------------------- 
			newProject.checkCreateSubFolders();
		}
		
		// --- ClassLoader/CLASSPATH laden ----------------
		newProject.resourcesLoad();
		
		// --- Das Ontologie-Objekt beladen --------------- 
		newProject.ontologies4Project = new Ontologies4Project(newProject);

		// --- ggf. AgentGUI - DefaultProfile übernehmen --
		if( newProject.JadeConfiguration.isUseDefaults()==true) {
			newProject.JadeConfiguration = Application.RunInfo.getJadeDefaultPlatformConfig();
		}
		
		// --- Gibt es bereits ein Simulations-Setup? -----
		if (newProject.simSetups.size()==0) {
			newProject.simSetups = new SimulationSetups(newProject, "default");
			newProject.simSetups .setupCreateDefault();			
		}
		
		// --- Projektfenster und Standard-Tabs anhängen --
		newProject.projectWindow = new ProjectWindow( newProject );
		newProject.addDefaultTabs();
		
		// --- Projekt als aktuelle markieren ------------- 
		newProject.isUnsaved = false;
				
		// --- Objekt an die Projektauflistung hängen -----
		projectsOpen.add(newProject);
		Application.ProjectCurr = newProject;

		// --- Anzeige anpassen ---------------------------
		Application.Projects.setProjectView();		
		Application.MainWindow.setCloseButtonPosition( true );
		Application.setTitelAddition( newProject.getProjectName() );
		Application.setStatusBar( Language.translate("Fertig") );	
		newProject.setMaximized();
		if (addNew==true) {
			newProject.save();   // --- Erstmalig speichern ---	
		}		
		return newProject;
	}

	/**
	 * Trys to close all current opened projects
	 * @return
	 */
	public boolean closeAll() {		
		// --- Alle "aktuellen" Projekte schließen --------
		while ( Application.ProjectCurr != null ) {
			if ( Application.ProjectCurr.close() == false  ) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Returns the Project-Object
	 * @param String ProjectName
	 * @return
	 */
	public Project get ( String ProjectName ) {
		int Index = getIndexByName( ProjectName );
		if ( Index == -1 ) {
			// --- Falls der Verzeichnisname genommen wurde ----
			Index = getIndexByFolderName( ProjectName );
		}
		return get(Index);
	}
	/**
	 * Returns the Project-Object
	 * @param int Index
	 * @return
	 */
	public Project get( int Index ) {
		return projectsOpen.get(Index);
	}

	/**
	 * Removes a single Project
	 * @param String Project2Remove
	 */
	public void remove( Project Project2Remove ) {
		projectsOpen.remove(Project2Remove);
		this.setProjectView();
	}
	/**
	 * Removes all Projects from the (Array) ProjectList
	 */
	public void removeAll( ) {
		projectsOpen.clear();
		Application.ProjectCurr = null;
		Application.Projects.setProjectView();		
	}

	/**
	 * Identifies a Project by his name and returns the Array-/Window-Index
	 * @param ProjectName
	 * @return
	 */
	public int getIndexByName ( String ProjectName ) {
		int Index = -1;
		for(int i=0; i<this.count(); i++) {
			if( projectsOpen.get(i).getProjectName().equalsIgnoreCase(ProjectName) ) {
				Index = i;
				break;
			}	
		}
		return Index;
	}
	/**
	 * Identifies a Project by his Root-Folder-Name and returns the Array-/Window-Index
	 * @param ProjectName
	 * @return
	 */
	public int getIndexByFolderName ( String ProjectName ) {
		int Index = -1;
		for(int i=0; i<this.count(); i++) {
			if( projectsOpen.get(i).getProjectFolder().toLowerCase().equalsIgnoreCase( ProjectName.toLowerCase() ) ) {
				Index = i;
				break;
			}	
		}
		return Index;
	}
	/**
	 * Counts the actual open projects
	 */
	public int count() {
		return projectsOpen.size();		
	}

	/**
	 * Configures the apeareance of the application, depending on the project
	 */
	public void setProjectView() {
		
		// --- 1. Rebuild the view to the Items in MenuBar 'Window' -----------
		this.setProjectMenuItems();
		
		// --- 2. Set the right value to the MenueBar 'View' ------------------
		this.setProjectView4DevOrUser();
		
	}
	
	/**
	 * Configures the View for menue 'view' -> 'Developer' or 'End user' 
	 */
	private void setProjectView4DevOrUser() {
		
		JRadioButtonMenuItem viewDeveloper = Application.MainWindow.viewDeveloper; 
		JRadioButtonMenuItem viewEndUser = Application.MainWindow.viewEndUser; 
		
		if (this.count()==0) {
			// --- Disable both MenuItems -----------------
			viewDeveloper.setEnabled(false);
			viewEndUser.setEnabled(false);
		} else {
			// --- Enable both MenuItems ------------------
			viewDeveloper.setEnabled(true);
			viewEndUser.setEnabled(true);
			
			// --- select the right item in relation ------  
			// --- to the project 					 ------
			String viewConfigured = Application.ProjectCurr.getProjectView();
			if (viewConfigured.equalsIgnoreCase(Project.VIEW_User)) {
				viewDeveloper.setSelected(false);
				viewEndUser.setSelected(true);
			} else {
				viewEndUser.setSelected(false);
				viewDeveloper.setSelected(true);
			}
			Application.ProjectCurr.projectWindow.setView();
		}
	}
	
	
	/**
	 * Create's the Window=>MenuItems depending on the open projects 
	 */
	private void setProjectMenuItems() {
		
		boolean setFontBold = true;
		
		JMenu WindowMenu = Application.MainWindow.jMenuMainWindow;
		WindowMenu.removeAll();
		if (this.count()==0 ){
			WindowMenu.add( new JMenuItmen_Window( Language.translate("Kein Projekt geöffnet !"), -1, setFontBold ) );
		}
		else {
			for(int i=0; i<this.count(); i++) {
				String ProjectName = projectsOpen.get(i).getProjectName();
				if ( ProjectName.equalsIgnoreCase( Application.ProjectCurr.getProjectName() ) ) 
					setFontBold = true;
				else 
					setFontBold = false;
				WindowMenu.add( new JMenuItmen_Window( ProjectName, i, setFontBold) );
			}		
		}
	}	
	
	/**
	 * Creates a single MenueItem for the Window-Menu depending on the open projects  
	 * @author derksen
	 */
	// --- Unterklasse für die Menüelemente "Fenster" => Projekte --------
	private class JMenuItmen_Window extends JMenuItem  {
 
		private static final long serialVersionUID = 1L;
		
		private JMenuItmen_Window( String ProjectName, int WindowIndex, boolean setBold  ) {
			
			final int WinIdx = WindowIndex;
			int WinNo = WindowIndex + 1;
			
			if ( WinNo <= 0 ) {
				this.setText( ProjectName );
			}
			else {
				this.setText( WinNo + ": " + ProjectName );
			}		
			if ( setBold ) {
				Font cfont = this.getFont();
				if ( cfont.isBold() == true ) {
					this.setForeground( Application.RunInfo.ColorMenuHighLight() );	
				}
				else {
					this.setFont( cfont.deriveFont(Font.BOLD) );
				}
			}
			this.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Application.Projects.setFocus( WinIdx );							
				}
			});		
		}
	}
	private void setFocus( int Index ) {
		this.get(Index).setFocus(true);		
	}


		
}
