package application;

import gui.ProjectNewOpen;
import gui.ProjectWindow;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import sim.setup.SimulationSetups;

import mas.onto.Ontologies4Project;

public class ProjectsLoaded {

	// --- Listing of the open projects -------------------------------
	private ArrayList<Project> ProjectsOpen = new ArrayList<Project>();
	
	/**
	 * Adding (Creating or Opening) a new Project to the Application
	 * @param addNew
	 * @return Project
	 */
	public Project add ( boolean addNew ) {
		
		String ActionTitel = null;
		String ProjectNameTest = null;
		String ProjectFolderTest = null;
		String LocalTmpProjectName = null;
		String LocalTmpProjectFolder = null;
		
		// ------------------------------------------------
		// --- Define a new Project-Instance -------------- 
		Project NewPro = new Project();
		
		// ------------------------------------------------
		// --- Startbedingenen für "New" oder "Open" ------
		// ------------------------------------------------
		if ( addNew == true ){
			// ------------------------------------------------
			// --- Anlegen eines neuen Projekts ---------------
			ActionTitel = Language.translate("Neues Projekt anlegen");
			
			// --- Neuen, allgemeinen Projektnamen finden -----		
			String ProjectNamePrefix = Language.translate("Neues Projekt");
			ProjectNameTest = ProjectNamePrefix;
			int Index = Application.Projects.getIndexByName(ProjectNameTest);
			int i = 2;
			while ( Index != -1 ) {
				ProjectNameTest = ProjectNamePrefix + " " + i;
				Index = Application.Projects.getIndexByName( ProjectNameTest );
				i++;
			}
			ProjectFolderTest = ProjectNameTest.toLowerCase().replace(" ", "_");
		}
		else {
			// ------------------------------------------------
			// --- Öffnen eine vorhandenen Projekts -----------
			ActionTitel = Language.translate("Projekt öffnen");			
		}
		Application.MainWindow.setStatusBar(ActionTitel + " ...");
		
		// ------------------------------------------------
		// --- Benutzer-Dialog öffnen ---------------------
		// ------------------------------------------------
		if ( Application.RunInfo.AppExecutedOver() == "IDE" ) {
			// -----------------------------------------------
			// --- IDE-Dialog für "Neues Projekt" öffnen -----
			ProjectNewOpen NewProDia = new ProjectNewOpen( Application.MainWindow,
														   Application.RunInfo.AppTitel() + ": " + ActionTitel,
														   true,
														   addNew
														   );
			NewProDia.setVarProjectName( ProjectNameTest );
			NewProDia.setVarProjectFolder( ProjectFolderTest );
			NewProDia.setVisible(true);
			// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
			if ( NewProDia.isCanceled() == true ) {
				Application.setStatusBar( Language.translate("Fertig") );
				return null;
			}
			else {
				LocalTmpProjectName = NewProDia.getVarProjectName();
				LocalTmpProjectFolder = NewProDia.getVarProjectFolder(); 
			}
			NewProDia.dispose();
			NewProDia = null;	
			// -----------------------------------------------
		}
		else {
			// -----------------------------------------------
			// --- Datei-Dialog für "Projekt öffnen" laden ---
			File DefaultFile = new File( Application.RunInfo.PathProjects(true, false) ); 

			JFileChooser FileSelector = new JFileChooser();
			FileSelector.setDialogTitle( Application.RunInfo.AppTitel() + " - " + ActionTitel );
			FileSelector.setCurrentDirectory( DefaultFile );
			FileSelector.setFileSelectionMode( JFileChooser.FILES_ONLY );
			FileSelector.setAcceptAllFileFilterUsed(false);
			FileSelector.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
				public String getDescription() {
					return "AgentGUI - Projektdatei (*.xml)";
				}
				public boolean accept(File f) {
					boolean RightFileType = false;
					if (f.getName().endsWith("agentgui.xml")) RightFileType = true;
					return RightFileType || f.isDirectory();					
				}
			});			
			// Disable "New-Folder" - Button ----------------- 
			((JPanel)FileSelector.getComponent(0)).getAccessibleContext().getAccessibleChild(0).getAccessibleContext().getAccessibleChild(4).getAccessibleContext().getAccessibleComponent().setEnabled(false);   
			FileSelector.setVisible(true);			
		
			// --- Dialog öffnen -----------------------------
			int SelectorResult = FileSelector.showOpenDialog(null);
			if ( SelectorResult != JFileChooser.APPROVE_OPTION) {
				// --- Abbruch / Falscher Dateityp -----------
				Application.setStatusBar( Language.translate("Fertig") );
				return null;				
			}	
			
			// --- Projektname und -verzeichnis festlegen 
			int Cut = FileSelector.getSelectedFile().getParent().lastIndexOf( Application.RunInfo.AppPathSeparatorString() ) + 1;
			LocalTmpProjectName =  FileSelector.getSelectedFile().getParent().substring(Cut);
			LocalTmpProjectFolder = FileSelector.getSelectedFile().getParent();
			if (LocalTmpProjectFolder.endsWith(Application.RunInfo.AppPathSeparatorString()) == false  )
				LocalTmpProjectFolder = LocalTmpProjectFolder.concat(Application.RunInfo.AppPathSeparatorString());	
		}				
		
		// --- Projektvariablen setzen -----------------------
		NewPro.setProjectName( LocalTmpProjectName );
		NewPro.setProjectFolder( LocalTmpProjectFolder );

		if ( addNew == true ) {			
			// --- Standardstruktur anlegen -------------------
			NewPro.createDefaultProjectStructure();
		}
		else {
			// --- XML-Datei einlesen -------------------------
			JAXBContext pc;
			Unmarshaller um = null;
			String XMLFileName = NewPro.getProjectFolderFullPath() + Application.RunInfo.getFileNameProject();			
			try {
				pc = JAXBContext.newInstance( NewPro.getClass() );
				um = pc.createUnmarshaller();
				NewPro = (Project) um.unmarshal( new FileReader( XMLFileName ) );
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			// --- Folder auf aktuellen Projektordner einstellen ---
			NewPro.setProjectFolder( LocalTmpProjectFolder );	
			
			// --- check/create default folders -------------------- 
			NewPro.checkCreateSubFolders();
		}
		
		// --- Die Agenten zu diesem Projekt ermitteln ----
		NewPro.filterProjectAgents();

		// --- Das Ontologie-Objekt beladen --------------- 
		NewPro.ontologies4Project = new Ontologies4Project(NewPro);

		// --- ggf. AgentGUI - DefaultProfile übernehmen --
		if( NewPro.JadeConfiguration.isUseDefaults()==true) {
			NewPro.JadeConfiguration = Application.RunInfo.getJadeDefaultPlatformConfig();
		}
		
		// --- Gibt es bereits ein Simulations-Setup? -----
		if (NewPro.simSetups.size()==0) {
			NewPro.simSetups = new SimulationSetups(NewPro, "default");
			NewPro.simSetups .setupCreateDefault();			
		}
		
		// --- Neues Projektfenster öffnen ----------------
		NewPro.ProjectGUI = new ProjectWindow( NewPro );
		
		// --- Projekt als aktuelle markieren ------------- 
		NewPro.ProjectUnsaved = false;
				
		// --- Objekt an die Projektauflistung hängen -----
		ProjectsOpen.add( NewPro );
		Application.ProjectCurr = NewPro;

		// --- Anzeige anpassen ---------------------------
		Application.Projects.setProjectMenuItems();		
		Application.MainWindow.setCloseButtonPosition( true );
		Application.setTitelAddition( NewPro.getProjectName() );
		Application.setStatusBar( Language.translate("Fertig") );	
		NewPro.setMaximized();
		if (addNew==true) {
			NewPro.save();   // --- Erstmalig speichern ---	
		}		
		
		// --- Ggf. noch nach Ontologien suchen -----------
		if (Application.JadePlatform.OntologyVector==null) {
			Application.JadePlatform.jadeFindOntologyClasse();
		}
		
		// --- Hier erfolgt gerade eine Testausgabe zu debugging - Zwecken. Wird bald wieder gelöscht ----
		System.out.println( "");
		System.out.println( "+++ Versuch ....  ohhjjjeeeee .... START +++");
//		@SuppressWarnings("unused")
//		DynForm df = new DynForm(NewPro, "monitormas.agents.ControlAgent");
		System.out.println( "+++ Versuch ....  ohhjjjeeeee .... ENDE +++");
		
		return NewPro;
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
		return ProjectsOpen.get(Index);
	}

	/**
	 * Removes a single Project
	 * @param String Project2Remove
	 */
	public void remove( Project Project2Remove ) {
		ProjectsOpen.remove(Project2Remove);		
	}
	/**
	 * Removes all Projects from the (Array) ProjectList
	 */
	public void removeAll( ) {
		ProjectsOpen.clear();
		Application.ProjectCurr = null;
		Application.Projects.setProjectMenuItems();		
	}

	/**
	 * Identifies a Project by his name and returns the Array-/Window-Index
	 * @param ProjectName
	 * @return
	 */
	public int getIndexByName ( String ProjectName ) {
		int Index = -1;
		for(int i=0; i<this.count(); i++) {
			if( ProjectsOpen.get(i).getProjectName().equalsIgnoreCase(ProjectName) ) {
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
			if( ProjectsOpen.get(i).getProjectFolder().toLowerCase().equalsIgnoreCase( ProjectName.toLowerCase() ) ) {
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
		return ProjectsOpen.size();		
	}

	/**
	 * Create's the Window=>MenuItems depending on the open projects 
	 */
	public void setProjectMenuItems() {
		
		boolean SetFontBold = true;
		
		JMenu WindowMenu = Application.MainWindow.jMenuMainWindow;
		WindowMenu.removeAll();
		if (this.count() == 0 ){
			WindowMenu.add( new JMenuItmen_Window( Language.translate("Kein Projekt geöffnet !"), -1, SetFontBold ) );
		}
		else {
			for(int i=0; i<this.count(); i++) {
				String ProjectName = ProjectsOpen.get(i).getProjectName();
				if ( ProjectName.equalsIgnoreCase( Application.ProjectCurr.getProjectName() ) ) 
					SetFontBold = true;
				else 
					SetFontBold = false;
				WindowMenu.add( new JMenuItmen_Window( ProjectName, i, SetFontBold) );
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
		this.get(Index).setFocus();		
	}


		
}
