package agentgui.core.application;

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
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import agentgui.core.common.Zipper;
import agentgui.core.gui.ProjectNewOpen;
import agentgui.core.gui.ProjectWindow;
import agentgui.core.ontologies.Ontologies4Project;
import agentgui.core.sim.setup.SimulationSetups;

/**
 * 
 * @author Christian Derksen
 */
public class ProjectsLoaded {

	// --- Listing of the open projects -------------------
	private ArrayList<Project> projectsOpen = new ArrayList<Project>();
	
	/**
	 * Adding (Creating or Opening) a new Project to the Application
	 * @param addNew
	 * @return Project
	 */
	public Project add(boolean addNew) {
		return this.add(addNew, null);
	}
	/**
	 * Open a project correponding to specified project folder
	 * @param selectedProjectFolder
	 * @return
	 */
	public Project add(String selectedProjectFolder) {
		return this.add(false, selectedProjectFolder);
	}
	
	/**
	 * Adding (Creating or Opening) a new Project to the Application
	 * @param addNew
	 * @param selectedProjectFolder
	 * @return
	 */
	private Project add (boolean addNew, String selectedProjectFolder) {

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
		if (addNew == true){
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
		
		
		if (selectedProjectFolder==null) {
			// ------------------------------------------------
			// --- Benutzer-Dialog öffnen ---------------------
			ProjectNewOpen newProDia = new ProjectNewOpen( Application.MainWindow, Application.RunInfo.getApplicationTitle() + ": " + actionTitel, true, addNew );
			newProDia.setVarProjectName( projectNameTest );
			newProDia.setVarProjectFolder( projectFolderTest );
			newProDia.setVisible(true);
			// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
			if ( newProDia.isCanceled() == true ) {
				Application.setStatusBar( Language.translate("Fertig") );
				return null;
			} else {
				localTmpProjectName = newProDia.getVarProjectName();
				localTmpProjectFolder = newProDia.getVarProjectFolder(); 
			}
			newProDia.dispose();
			newProDia = null;	
			
		} else {
			// ------------------------------------------------
			// --- Projekt aus Startparameter übernehmen ------
			localTmpProjectName = null;
			localTmpProjectFolder = selectedProjectFolder;
		}

		// ------------------------------------------------
		// --- ClassLoader entladen -----------------------
		if(projectsOpen.size()!=0) {
			Application.ProjectCurr.resourcesRemove();
		}
		
		// ------------------------------------------------
		// --- Projektvariablen setzen --------------------
		newProject.setProjectName( localTmpProjectName );
		newProject.setProjectFolder( localTmpProjectFolder );

		if (addNew==true) {			
			// --- Standardstruktur anlegen ---------------
			newProject.createDefaultProjectStructure();
		} 
		else {
			// --- XML-Datei einlesen ---------------------
			JAXBContext pc;
			Unmarshaller um = null;
			String XMLFileName = newProject.getProjectFolderFullPath() + Application.RunInfo.getFileNameProject();	
			 
			// --- Gibt es diese Datei überhaupt? ---------
			File xmlFile = new File(XMLFileName);
			if (xmlFile.exists()==false) {
				System.out.println(Language.translate("Verzeichnis wurde nicht gefunden:") + " " + XMLFileName);
				return null;
			}
			// --- Einlesen der Datei 'agentgui.xml' ------
			try {
				pc = JAXBContext.newInstance( newProject.getClass() );
				um = pc.createUnmarshaller();
				newProject = (Project) um.unmarshal( new FileReader(XMLFileName) );
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
			newProject.simSetups.setupCreateDefault();			
		}
		
		// --- Projektfenster und Standard-Tabs anhängen --
		newProject.projectWindow = new ProjectWindow(newProject);
		newProject.addDefaultTabs();
		
		// --- Konfigurierte PlugIns laden ----------------
		newProject.plugInVectorLoad();
		
		// --- Projekt als aktuell markieren -------------- 
		newProject.isUnsaved = false;
				
		// --- Objekt an die Projektauflistung hängen -----
		projectsOpen.add(newProject);
		Application.ProjectCurr = newProject;

		// --- Anzeige anpassen ---------------------------
		Application.Projects.setProjectView();		
		Application.MainWindow.setCloseButtonPosition(true);
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
		
		JMenu WindowMenu = Application.MainWindow.jMenuMainWindows;
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
	/**
	 * Sets the focus to the project identified with the index number
	 * @param Index
	 */
	private void setFocus(int Index) {
		this.get(Index).setFocus(true);		
	}
	
	/**
	 * Imports a project, which is packed in Agent.GUI project file (*.agui)
	 */
	public void projectImport() {
		
		String optionMsg = null;
		String optionTitle = null;
		String newLine = Application.RunInfo.AppNewLineString(); 
		
		// --- Select a *.agui file -----------------------
		String fileEnd = Application.RunInfo.getFileEndProjectZip();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(Language.translate("Agent.GUI Projekt-Datei") + " (*." + fileEnd + ")", fileEnd);
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(Application.RunInfo.getLastSelectedFolder());
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		int answerChooser = chooser.showDialog(Application.MainWindow, Language.translate("Projekt importieren"));
		if (answerChooser==JFileChooser.CANCEL_OPTION) return;
		Application.RunInfo.setLastSelectedFolder(chooser.getCurrentDirectory());
		
		File projectFile = chooser.getSelectedFile();
		if (projectFile!=null && projectFile.exists()) {

			String destFolder = Application.RunInfo.PathProjects(true, false);
			String zipFolder = projectFile.getAbsolutePath();
			
			// --- Import project file as a new project ---
			Zipper zipper = new Zipper();
			zipper.setUnzipZipFolder(zipFolder);
			zipper.setUnzipDestinationFolder(destFolder);
			
			// --- Error-Handling -------------------------
			String rootFolder2Extract = zipper.getRootFolder2Extract();
			String testFolder = destFolder + rootFolder2Extract;
			File testFile = new File(testFolder);
			if (testFile.exists()) {
				optionTitle = rootFolder2Extract + ": " + Language.translate("Verzeichnis bereits vorhanden!");
				optionMsg = Language.translate("Verzeichnis") + ": " + testFolder + newLine;
				optionMsg+= Language.translate("Das Verzeichnis existiert bereits. Der Import wird unterbrochen.");
				JOptionPane.showMessageDialog(Application.MainWindow, optionMsg, optionTitle, JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// --- Finally unzip --------------------------
			zipper.doUnzipProject(rootFolder2Extract);
			zipper = null;
			
		}		
		
	}

	/**
	 * Exports a project to a Agent.GUI project file (*.agui)
	 */
	public void projectExport() {
		
		String optionMsg = null;
		String optionTitle = null;
		
		String actionTitel = Language.translate("Projekt zum Export auswählen");
		String projectFolder = null;
		
		// --- If a project is open, ask to export this project -----
		if (Application.ProjectCurr!=null) {
			optionTitle = "" + Application.ProjectCurr.getProjectName() + ": " + Language.translate("Projekt exportieren?");
			optionMsg = Language.translate("Möchten Sie das aktuelle Projekt exportieren?");
			int answer = JOptionPane.showConfirmDialog(Application.MainWindow, optionMsg, optionTitle, JOptionPane.YES_NO_OPTION);
			if (answer==JOptionPane.YES_OPTION) {
				projectFolder = Application.ProjectCurr.getProjectFolder();
			}
		}
		
		// --- If no projectFolder is specified yet ----------------- 
		if (projectFolder==null) {
			// --- Select the project to export ---------------------
			ProjectNewOpen newProDia = new ProjectNewOpen( Application.MainWindow, Application.RunInfo.getApplicationTitle() + ": " + actionTitel, true, false );
			newProDia.setOkButtonText("Export");
			newProDia.setVisible(true);
			// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
			if (newProDia.isCanceled()==true) return;
			projectFolder = newProDia.getVarProjectFolder(); 
			newProDia.dispose();
			newProDia = null;
		}

		// --- Select a *.agui file ---------------------------------
		String fileEnd = Application.RunInfo.getFileEndProjectZip();
		String proposedFileName = Application.RunInfo.getLastSelectedFolderAsString() + projectFolder + "." + fileEnd ;
		File proposedFile = new File(proposedFileName );
		FileNameExtensionFilter filter = new FileNameExtensionFilter(Language.translate("Agent.GUI Projekt-Datei") + " (*." + fileEnd + ")", fileEnd);
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(proposedFile);
		chooser.setCurrentDirectory(proposedFile);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setAcceptAllFileFilterUsed(false);
		
		int answerChooser = chooser.showDialog(Application.MainWindow, Language.translate("Projekt exportieren"));
		if (answerChooser==JFileChooser.CANCEL_OPTION) return;
		Application.RunInfo.setLastSelectedFolder(chooser.getCurrentDirectory());		
		
		
		File projectFile = chooser.getSelectedFile();
		if (projectFile!=null) {
			// --- Make sure that the file end is the correct one ---
			if (projectFile.getName().endsWith("." + fileEnd)==false) {
				projectFile = new File(projectFile.getAbsolutePath() + "." + fileEnd);
			}

			// --- Some Error-Handlings -----------------------------
			// --- File already there? ----------
			if (projectFile.exists()==true) {
				optionTitle = projectFile.getName() + ": " + Language.translate("Datei überschreiben?");
				optionMsg = Language.translate("Die Datei existiert bereits. Wollen Sie diese Datei überschreiben?");
				int answer = JOptionPane.showConfirmDialog(Application.MainWindow, optionMsg, optionTitle, JOptionPane.YES_NO_OPTION);
				if (answer==JOptionPane.YES_OPTION) {
					projectFile.delete();
				} else {
					return;
				}
			}
			
			// --- Export project file as a new project -------------
			String srcFolder = Application.RunInfo.PathProjects(true, false) + projectFolder;
			String zipFolder = projectFile.getAbsolutePath();
			
			Zipper zipper = new Zipper();
			zipper.setExcludePattern(".svn");
			zipper.setZipFolder(zipFolder);
			zipper.setZipSourceFolder(srcFolder);
			zipper.doZipFolder();
			zipper = null;
			
		}
		
	}
	
}
