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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.Zipper;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.gui.ProjectNewOpen;

/**
 * This class holds the list of the projects, that are currently open
 * within Agent.GUI and offers methods to deal with them.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectsLoaded {

	// --- Listing of the open projects -------------------
	private ArrayList<Project> projectsOpen = new ArrayList<Project>();
	
	/**
	 * Adding (Creating or Opening) a new Project to the Application
	 * @param addNew
	 * @return The Project-instance that was added here
	 */
	public Project add(boolean addNew) {
		return this.add(addNew, null);
	}
	/**
	 * Open a project corresponding to specified project folder
	 * @param selectedProjectFolder
	 * @return The Project-instance that was added here
	 */
	public Project add(String selectedProjectFolder) {
		return this.add(false, selectedProjectFolder);
	}
	
	/**
	 * Adding (Creating or Opening) a new Project to the Application
	 * @param addNew
	 * @param selectedProjectFolder
	 * @return The Project-instance that was added here
	 */
	private Project add(boolean addNew, String selectedProjectFolder) {

		int action = 0;
		String actionTitel = null;
		String projectNameTest = null;
		String projectFolderTest = null;
		String localTmpProjectName = null;
		String localTmpProjectFolder = null;
		
		// --- Define a new Project-Instance -------------- 
		Project newProject = new Project();
		
		// --- Start argument for "New" oder "Open" -------
		if (addNew == true){
			// --- New Project ----------------------------
			action = ProjectNewOpen.ACTION_NewProject;
			actionTitel = Language.translate("Neues Projekt anlegen");
			
			// --- Define an initial project name ---------		
			String ProjectNamePrefix = Language.translate("Neues Projekt");
			projectNameTest = ProjectNamePrefix;
			int index = Application.getProjectsLoaded().getIndexByName(projectNameTest);
			int i = 2;
			while (index!=-1) {
				projectNameTest = ProjectNamePrefix + " " + i;
				index = Application.getProjectsLoaded().getIndexByName(projectNameTest);
				i++;
			}
			projectFolderTest = projectNameTest.toLowerCase().replace(" ", "_");
		
		} else {
			// --- Open existing project ------------------
			action = ProjectNewOpen.ACTION_OpenProject;
			actionTitel = Language.translate("Projekt �ffnen");			
		}
		Application.getMainWindow().setStatusBar(actionTitel + " ...");
		
		
		if (selectedProjectFolder==null) {
			// --- Open user dialog -----------------------
			ProjectNewOpen newProDia = new ProjectNewOpen(Application.getMainWindow(), Application.getGlobalInfo().getApplicationTitle() + ": " + actionTitel, action);
			newProDia.setProjectName(projectNameTest);
			newProDia.setProjectFolder(projectFolderTest);
			newProDia.setVisible(true);
			// === Wait for user here =====================
			if (newProDia.isCanceled()==true) {
				Application.setStatusBar(Language.translate("Fertig"));
				return null;
			} else {
				localTmpProjectName = newProDia.getProjectName();
				localTmpProjectFolder = newProDia.getProjectFolder(); 
			}
			newProDia.dispose();
			newProDia = null;	
			
		} else {
			// --- project folder from start argument -----
			localTmpProjectName = null;
			localTmpProjectFolder = selectedProjectFolder;
		}

		// --- ClassLoader unload -------------------------
		if(projectsOpen.size()!=0) {
			Application.getProjectFocused().resourcesRemove();
		}
		
		// --- Set project variables ----------------------
		newProject.setProjectName(localTmpProjectName);
		newProject.setProjectFolder(localTmpProjectFolder);

		if (addNew==true) {			
			// --- Create default project structure -------
			newProject.createDefaultProjectStructure();
		} 
		else {
			// --- Get data model from file ---------------
			JAXBContext pc;
			Unmarshaller um = null;
			String XMLFileName = newProject.getProjectFolderFullPath() + Application.getGlobalInfo().getFileNameProject();	
			String userObjectFileName = newProject.getProjectFolderFullPath() + Application.getGlobalInfo().getFilenameProjectUserObject();
		
			// --- Does the file exists -------------------
			File xmlFile = new File(XMLFileName);
			if (xmlFile.exists()==false) {
				
				System.out.println(Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + " " + XMLFileName);
				Application.setStatusBar(Language.translate("Fertig"));
				
				String title = Language.translate("Projekt-Ladefehler!");
				String message = Language.translate("Datei oder Verzeichnis wurde nicht gefunden:") + "\n";
				message += XMLFileName;
				JOptionPane.showInternalMessageDialog(Application.getMainWindow().getJDesktopPane4Projects(), message, title, JOptionPane.WARNING_MESSAGE);
				return null;
			}
			
			// --- Read file 'agentgui.xml' --------------
			try {
				pc = JAXBContext.newInstance(newProject.getClass());
				um = pc.createUnmarshaller();
				newProject = (Project) um.unmarshal(new FileReader(XMLFileName));
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (JAXBException ex) {
				ex.printStackTrace();
			}
			
			// --- Reading the serializable user object of - 
			// --- the Project from the 'agentgui.bin' -----
			File userObjectFile = new File(userObjectFileName);
			if (userObjectFile.exists()) {
				
				Serializable userObject = null;
				FileInputStream fis = null;
				ObjectInputStream in = null;
				try {
					fis = new FileInputStream(userObjectFileName);
					in = new ObjectInputStream(fis);
					userObject = (Serializable)in.readObject();
					in.close();
				} catch(IOException ex) {
					ex.printStackTrace();
				} catch(ClassNotFoundException ex) {
					ex.printStackTrace();
				}
				newProject.setUserRuntimeObject(userObject);				
			}
			
			// --- check/create default folders --------------------
			newProject.setProjectFolder(localTmpProjectFolder);
			newProject.checkCreateSubFolders();
		}
		
		// --------------------------------------------------------------------
		// --- Load additional external resources with the ClassLoader --------
		// --------------------------------------------------------------------
		newProject.resourcesLoad();
		// --------------------------------------------------------------------
		
		// --- Remind the current name of the used environment model type -----
		boolean reloadEnvironmentType = false;
		String envModelName = newProject.getEnvironmentModelName();
		EnvironmentType envType = newProject.getEnvironmentModelType();
		if (envType.getInternalKey().equals("none")) {
			// --- The environment model was NOT FOUND YET ----------
			// --- => Maybe a PlugIn contains an environment model --
			reloadEnvironmentType = true;
		} else {
			// --- The environment model was ALREADY FOUND ----------
			reloadEnvironmentType = false;
		}
		
		// --- Maybe take over Agent.GUI default JADE configuration -----------
		if(addNew==true) {
			newProject.setJadeConfiguration(Application.getGlobalInfo().getJadeDefaultPlatformConfig());
		}
		
		// --- Is there already a simulation setup? ---------------------------
		if (newProject.getSimulationSetups().size()==0) {
			// --- Create default simulations setup ---------------------------
			newProject.getSimulationSetups().setupCreateDefault();			
		}

		// --- Instantiate project-window and the default tabs ----------------
		newProject.addDefaultTabs();

		// --- Load configured PlugIns ----------------------------------------
		newProject.plugInVectorLoad();
		
		// --- add project to the project-listing -----------------------------
		projectsOpen.add(newProject);
		Application.setProjectFocused(newProject);

		// --- Configure the project view in the main application -------------
		Application.getProjectsLoaded().setProjectView();		
		Application.getMainWindow().setCloseButtonPosition(true);
		Application.setTitelAddition(newProject.getProjectName());
		Application.setStatusBar(Language.translate("Fertig"));	
		newProject.setMaximized();
		
		if (addNew==true) {
			// --- Save project for the first time ---
			newProject.save();   	
		} else {
			// --- Set Project to unsaved ------------
			newProject.setUnsaved(false);
		}
		
		// --- In case of an environment model in a PlugIn --------------------
		if (envModelName!=null && reloadEnvironmentType==true) {
			newProject.setNotChangedButNotify(Project.CHANGED_EnvironmentModel);		
		}
		return newProject;
	}

	/**
	 * This method will try to close all open projects
	 * @return Returns true on success
	 */
	public boolean closeAll() {		
		while (Application.getProjectFocused()!=null) {
			if (Application.getProjectFocused().close()==false) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Returns the Project-instance given by its project name
	 * @param projectName
	 * @return A Project instance 
	 */
	public Project get(String projectName) {
		int index = this.getIndexByName(projectName);
		if (index == -1 ) {
			// --- if the folder name was used ---
			index = this.getIndexByFolderName(projectName);
		}
		return get(index);
	}
	/**
	 * Returns the Project-instance given by its index
	 * @param indexOfProject
	 * @return A Project instance
	 */
	public Project get(int indexOfProject) {
		return projectsOpen.get(indexOfProject);
	}

	/**
	 * Removes a single Project
	 * @param project2Remove
	 */
	public void remove(Project project2Remove) {
		projectsOpen.remove(project2Remove);
		if (projectsOpen.size()==0) {
			Application.setProjectFocused(null);
		}
		this.setProjectView();
	}
	/**
	 * Removes all Projects from the (Array) ProjectList
	 */
	public void removeAll() {
		projectsOpen.clear();
		Application.setProjectFocused(null);
		Application.getProjectsLoaded().setProjectView();		
	}

	/**
	 * Identifies a Project by its name and returns the Array-/Window-Index
	 * @param projectName
	 * @return The index position of a project 
	 */
	public int getIndexByName(String projectName) {
		int Index = -1;
		for(int i=0; i<this.count(); i++) {
			if( projectsOpen.get(i).getProjectName().equalsIgnoreCase(projectName) ) {
				Index = i;
				break;
			}	
		}
		return Index;
	}
	/**
	 * Identifies a Project by its Root-Folder-Name and returns the Array-/Window-Index
	 * @param projectFolderName
	 * @return The index position of a project
	 */
	public int getIndexByFolderName(String projectFolderName) {
		int index = -1;
		for(int i=0; i<this.count(); i++) {
			if( projectsOpen.get(i).getProjectFolder().toLowerCase().equalsIgnoreCase( projectFolderName.toLowerCase() ) ) {
				index = i;
				break;
			}	
		}
		return index;
	}
	/**
	 * Counts the actual open projects
	 */
	public int count() {
		return projectsOpen.size();		
	}

	/**
	 * Configures the appearance of the application, depending on the current project configuration
	 */
	public void setProjectView() {
		
		// --- 1. Set Setup-Selector and Tools --------------------------------
		Application.getMainWindow().getSetupSelectorToolbar().setProject(Application.getProjectFocused());
		
		// --- 2. Rebuild the view to the Items in MenuBar 'Window' -----------
		this.setProjectMenuItems();
		
		// --- 3. Set the right value to the MenueBar 'View' ------------------
		this.setProjectView4DevOrUser();
		
	}
	
	/**
	 * Configures the View for menue 'view' -> 'Developer' or 'End user' 
	 */
	private void setProjectView4DevOrUser() {
		
		JRadioButtonMenuItem viewDeveloper = Application.getMainWindow().viewDeveloper; 
		JRadioButtonMenuItem viewEndUser = Application.getMainWindow().viewEndUser; 
		
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
			String viewConfigured = Application.getProjectFocused().getProjectView();
			if (viewConfigured.equalsIgnoreCase(Project.VIEW_User)) {
				viewDeveloper.setSelected(false);
				viewEndUser.setSelected(true);
			} else {
				viewEndUser.setSelected(false);
				viewDeveloper.setSelected(true);
			}
			Application.getProjectFocused().getProjectWindow().setView();
		}
	}
	
	
	/**
	 * Create's the Window=>MenuItems depending on the open projects 
	 */
	private void setProjectMenuItems() {
		
		boolean setFontBold = true;
		
		JMenu WindowMenu = Application.getMainWindow().getJMenuMainWindow();
		WindowMenu.removeAll();
		if (this.count()==0 ){
			WindowMenu.add( new JMenuItmen_Window( Language.translate("Kein Projekt ge�ffnet !"), -1, setFontBold ) );
		} else {
			for(int i=0; i<this.count(); i++) {
				String ProjectName = projectsOpen.get(i).getProjectName();
				if ( ProjectName.equalsIgnoreCase( Application.getProjectFocused().getProjectName() ) ) 
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
	// --- Unterklasse f�r die Men�elemente "Fenster" => Projekte --------
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
					this.setForeground( Application.getGlobalInfo().ColorMenuHighLight() );	
				}
				else {
					this.setFont( cfont.deriveFont(Font.BOLD) );
				}
			}
			this.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Application.getProjectsLoaded().setFocus( WinIdx );							
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
		String newLine = Application.getGlobalInfo().getNewLineSeparator(); 
		
		// --- Select a *.agui file -----------------------
		String fileEnd = Application.getGlobalInfo().getFileEndProjectZip();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(Language.translate("Agent.GUI Projekt-Datei") + " (*." + fileEnd + ")", fileEnd);
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		int answerChooser = chooser.showDialog(Application.getMainWindow(), Language.translate("Projekt importieren"));
		if (answerChooser==JFileChooser.CANCEL_OPTION) return;
		Application.getGlobalInfo().setLastSelectedFolder(chooser.getCurrentDirectory());
		
		File projectFile = chooser.getSelectedFile();
		if (projectFile!=null && projectFile.exists()) {

			String destFolder = Application.getGlobalInfo().PathProjects(true);
			String zipFolder = projectFile.getAbsolutePath();
			
			// --- Import project file as a new project ---
			Zipper zipper = new Zipper(Application.getMainWindow());
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
				JOptionPane.showMessageDialog(Application.getMainWindow(), optionMsg, optionTitle, JOptionPane.WARNING_MESSAGE);
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
		this.projectExport(null);
	}
	
	/**
	 * Exports a project to a Agent.GUI project file (*.agui)
	 *
	 * @param projectFolder the project folder
	 * @return true, if successful
	 */
	public boolean projectExport(String projectFolder) {
		
		String optionMsg = null;
		String optionTitle = null;
		
		String actionTitel = Language.translate("Projekt zum Export ausw�hlen");
		
		if (projectFolder==null) {
			// --- If a project is open, ask to export this project -----
			if (Application.getProjectFocused()!=null) {
				optionTitle = "" + Application.getProjectFocused().getProjectName() + ": " + Language.translate("Projekt exportieren?");
				optionMsg = Language.translate("M�chten Sie das aktuelle Projekt exportieren?");
				int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), optionMsg, optionTitle, JOptionPane.YES_NO_OPTION);
				if (answer==JOptionPane.YES_OPTION) {
					projectFolder = Application.getProjectFocused().getProjectFolder();
				}
			}
			
			// --- If no projectFolder is specified yet ----------------- 
			if (projectFolder==null) {
				// --- Select the project to export ---------------------
				ProjectNewOpen newProDia = new ProjectNewOpen(Application.getMainWindow(), Application.getGlobalInfo().getApplicationTitle() + ": " + actionTitel, ProjectNewOpen.ACTION_OpenProject);
				newProDia.setOkButtonText("Export");
				newProDia.setVisible(true);
				// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
				if (newProDia.isCanceled()==true) {
					return false;
				}
				projectFolder = newProDia.getProjectFolder(); 
				newProDia.dispose();
				newProDia = null;
			}
		}

		// --- Select a *.agui file ---------------------------------
		String fileEnd = Application.getGlobalInfo().getFileEndProjectZip();
		String proposedFileName = Application.getGlobalInfo().getLastSelectedFolderAsString() + projectFolder + "." + fileEnd ;
		File proposedFile = new File(proposedFileName );
		FileNameExtensionFilter filter = new FileNameExtensionFilter(Language.translate("Agent.GUI Projekt-Datei") + " (*." + fileEnd + ")", fileEnd);
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(proposedFile);
		chooser.setCurrentDirectory(proposedFile);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setAcceptAllFileFilterUsed(false);
		
		int answerChooser = chooser.showDialog(Application.getMainWindow(), Language.translate("Projekt exportieren"));
		if (answerChooser==JFileChooser.CANCEL_OPTION) {
			return false;
		}
		Application.getGlobalInfo().setLastSelectedFolder(chooser.getCurrentDirectory());		
		
		File projectFile = chooser.getSelectedFile();
		if (projectFile==null) {
			return false;
		}
		
		// --- Make sure that the file end is the correct one ---
		if (projectFile.getName().endsWith("." + fileEnd)==false) {
			projectFile = new File(projectFile.getAbsolutePath() + "." + fileEnd);
		}

		// --- Some Error-Handlings -----------------------------
		// --- File already there? ----------
		if (projectFile.exists()==true) {
			optionTitle = projectFile.getName() + ": " + Language.translate("Datei �berschreiben?");
			optionMsg = Language.translate("Die Datei existiert bereits. Wollen Sie diese Datei �berschreiben?");
			int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), optionMsg, optionTitle, JOptionPane.YES_NO_OPTION);
			if (answer==JOptionPane.YES_OPTION) {
				projectFile.delete();
			} else {
				return false;
			}
		}
		
		// --- Export project file as a new project -------------
		String srcFolder = Application.getGlobalInfo().PathProjects(true) + projectFolder;
		String zipFolder = projectFile.getAbsolutePath();
		
		Zipper zipper = new Zipper(Application.getMainWindow());
		zipper.setExcludePattern(".svn");
		zipper.setZipFolder(zipFolder);
		zipper.setZipSourceFolder(srcFolder);
		zipper.doZipFolder();
		zipper = null;
		
		return true;
		
	}
	
	/**
	 * Can be used in order to delete a project.
	 */
	public void projectDelete() {
		
		String optionMsg = null;
		String optionTitle = null;
		String projectFolder = null;
		String projectFolderFullPath = null;
		boolean exportBeforeDelete = false;
		
		int action = ProjectNewOpen.ACTION_DeleteProject;
		String actionTitel = Language.translate("Projekt l�schen");
		
		Application.getMainWindow().setStatusBar(actionTitel + " ...");
		
		// ----------------------------------------------------------
		// --- Open project selection dialog ------------------------
		ProjectNewOpen newProDia = new ProjectNewOpen(Application.getMainWindow(), Application.getGlobalInfo().getApplicationTitle() + ": " + actionTitel, action);
		newProDia.setVisible(true);
		// === Waiting for closing dialog ===
		if ( newProDia.isCanceled() == true ) {
			Application.setStatusBar(Language.translate("Fertig"));
			return;
		} else {
			exportBeforeDelete = newProDia.isExportBeforeDelete();
			projectFolder = newProDia.getProjectFolder();
		}
		newProDia.dispose();
		newProDia = null;	
	
		// ----------------------------------------------------------
		// --- If a project is open, ask to close this project ------
		if (Application.getProjectFocused()!=null) {
			
			int iProject = this.getIndexByFolderName(projectFolder);
			if (iProject>=0) {
			
				// --- The selected project is open -----------------
				Project currProject = this.get(iProject);	
				optionTitle = "" + currProject.getProjectName() + " - " + Language.translate("Projekt schlie�en");
				optionMsg = currProject.getProjectName() + ": " + Language.translate("Das Projekt wird nun geschlossen!");
				int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), optionMsg, optionTitle, JOptionPane.OK_CANCEL_OPTION);
				if (answer==JOptionPane.CANCEL_OPTION) {
					Application.setStatusBar(Language.translate("Fertig"));
					return;
				}
				// --- Close the project window ---------------------
				while (iProject>=0) {
					currProject = this.get(iProject);
					if (currProject.close()==false) {
						Application.setStatusBar(Language.translate("Fertig"));
						return;
					}
					iProject = this.getIndexByFolderName(projectFolder);
				}
			}
		}
		
		// ----------------------------------------------------------
		// --- Export before delete ? -------------------------------
		if (exportBeforeDelete==true) {
			if (this.projectExport(projectFolder)==false){
				Application.setStatusBar(Language.translate("Fertig"));
				return;
			}
		}
		
		// ----------------------------------------------------------
		// --- Delete the folders of the project --------------------
		projectFolderFullPath = Application.getGlobalInfo().PathProjects(true) + projectFolder;
		System.out.println(Language.translate("L�sche Verzeichnis") +": " + projectFolderFullPath);

		// --- Get the files and folders in the project folder ------
		Vector<File> files = this.getFilesAndFolders(projectFolderFullPath);
		for (int i = files.size()-1; i>-1 ; i--) {
			File file = files.get(i);
			file.delete();
		}

		// --- Delete the project folder itself ---------------------
		File file = new File(projectFolderFullPath);
		file.delete();
		
		Application.setStatusBar(Language.translate("Fertig"));
	}
	
	/**
	 * This method will evaluate the given folder and it's sub-folder recursively.
	 * The containing File objects will be returned in the Vector.
	 *
	 * @param srcFolder the folder to evaluate
	 * @return the files and folders
	 */
	private Vector<File> getFilesAndFolders(String srcFolder) {
		
		Vector<File> filesFound = new Vector<File>();
		
		File folder = new File(srcFolder);
		String listOfFiles[] = folder.list();
		for (int i = 0; i < listOfFiles.length; i++) {
			
			// --- If the current file should be included -----------
			File sngFileObject = new File(srcFolder + File.separator + listOfFiles[i]);
			filesFound.add(sngFileObject);
			if (sngFileObject.isDirectory()) {
				filesFound.addAll(this.getFilesAndFolders(sngFileObject.getAbsolutePath()));
			}
		} // end for
		return filesFound;
	}
	
}
