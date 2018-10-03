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
package agentgui.core.project.setup;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JOptionPane;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import de.enflexit.common.transfer.FileCopier;

/**
 * This class represents the list of setups available in a {@link Project}.
 *  
 * @see Project
 * @see Project#simulationSetups
 * @see Project#simulationSetupCurrent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SimulationSetups extends TreeMap<String, String> {

	private static final long serialVersionUID = -9078535303459653695L;

	public static final String DEFAULT_SETUP_NAME = "default";
	
	private Project currProject = Application.getProjectFocused();
	private SimulationSetup currSimSetup;
	
	private String currSimSetupName;
	private String currSimXMLFile;

	
	/**
	 * Constructor for this class.
	 * @param project the project
	 * @param currentSimulationSetup the current simulation setup 
	 */
	public SimulationSetups(Project project, String currentSimulationSetup) {
		this.currProject = project;
		this.currSimSetupName = currentSimulationSetup;
	}

	/**
	 * This Method creates the 'default' {@link SimulationSetup}.
	 */
	public void setupCreateDefault() {
		
		this.currSimSetupName = SimulationSetups.DEFAULT_SETUP_NAME;
		String xmlFile = this.getSuggestSetupFile(currSimSetupName);
		
		this.put(currSimSetupName, xmlFile);
		this.currProject.setSimulationSetupCurrent(currSimSetupName);
		
		this.currSimSetup = new SimulationSetup(this.currProject); 
		
		String xmlFileNameFullPath = this.currProject.getSubFolder4Setups(true) + xmlFile;		
		this.setCurrSimXMLFile(xmlFileNameFullPath);

		this.setupSave();
	}
	
	/**
	 * Adds a new Setup to the setups.
	 * @param newSetupName the name
	 * @param newFileName the new file name
	 */
	public void setupAddNew(String newSetupName, String newFileName) {
		// --- Set application status for user ------------
		Application.setStatusBarMessage("Adding new setup '" + newSetupName + "' ...");
		// --- Setup current setup ------------------------
		this.setupSave();
		// --- Add name and file --------------------------
		this.put(newSetupName, newFileName);
		// --- Set focus to the current setup -------------
		this.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_ADD_NEW, newSetupName, true);
		// --- Save project -------------------------------
		this.saveProject(true);
		Application.setStatusBarMessageReady();
	}

	/**
	 * Removes a setup form the list of setups.
	 * @param setupName the setup to remove
	 */
	public void setupRemove(String setupName) {
		
		if (this.containsKey(setupName)==false) return;

		// --- Set application status for user ------------
		Application.setStatusBarMessage("Remove setup '" + setupName + "' ...");
		// --- Remove Setup -------------------------------
		this.remove(setupName);
		
		// --- Delete setup files -------------------------
		List<File> setupFileList = SimulationSetup.getSetupFiles(new File(currSimXMLFile));
		for (int i = 0; i < setupFileList.size(); i++) {
			File setupFile = setupFileList.get(i);
			if (setupFile.exists()) setupFile.delete();
		}
		
		// --- Notify -------------------------------------
		this.currProject.setChangedAndNotify(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_REMOVE));
		
		if (this.size()==0) {
			// --- Add default setup ----------------------
			this.currSimSetupName = "default";
			// --- No setup found -------------------------
			String newFileName = getSuggestSetupFile(currSimSetupName);
			this.setupAddNew(currSimSetupName, newFileName);

		} else {
			// --- Load first Setup -----------------------
			this.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, this.getFirstSetup(), false);
		}
		// --- Save project -------------------------------
		this.saveProject(false);
		Application.setStatusBarMessageReady();
	}
	
	/**
	 * Renames a Setup and the associated file.
	 *
	 * @param nameOld the name old
	 * @param nameNew the name new
	 * @param fileNameNew the file name new
	 */
	public void setupRename(String nameOld, String nameNew, String fileNameNew) {

		if (this.containsKey(nameOld)==false) return;

		// --- Set application status for user ------------
		Application.setStatusBarMessage("Rename setup '" + nameOld + "' to '" + nameNew + "' ...");
		this.saveProject(true);
		
		// --- Rename folder setup files ------------------
		List<File> setupFileListOld = SimulationSetup.getSetupFiles(new File(this.currSimXMLFile));
		List<File> setupFileListNew = SimulationSetup.getSetupFiles(new File(this.currProject.getSubFolder4Setups(true) + fileNameNew));
		for (int i = 0; i < setupFileListOld.size(); i++) {
			File setupFileOld = setupFileListOld.get(i);
			File setupFileNew = setupFileListNew.get(i);
			if (setupFileOld.exists()) {
				setupFileOld.renameTo(setupFileNew);
			}
		}

		// --- Edit local entries -------------------------
		this.remove(nameOld);
		this.put(nameNew, fileNameNew);
		
		this.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_RENAME, nameNew, false);
		// --- Save Project -------------------------------
		this.saveProject(false);
		Application.setStatusBarMessageReady();
	}

	/**
	 * Copies a {@link SimulationSetup} and the associated files.
	 *
	 * @param nameOld the name of the {@link SimulationSetup} to copy
	 * @param nameNew the name of the copied {@link SimulationSetup}
	 * @param fileNameNew the file name for the new {@link SimulationSetup}
	 */
	public void setupCopy(String nameOld, String nameNew, String fileNameNew) {

		if (this.containsKey(nameOld)==false) return;
		
		// --- Set application status for user ------------
		Application.setStatusBarMessage("Copy setup '" + nameOld + "' to '" + nameNew + "' ...");
		
		// --- Save current state and project -------------
		this.saveProject(true);
		
		// --- Get new base setup file --------------------
		String fileNameXMLNew = this.currProject.getSubFolder4Setups(true) + fileNameNew; 
		
		// --- Copy setup files ---------------------------
		List<File> setupFileListOld = SimulationSetup.getSetupFiles(new File(this.currSimXMLFile));
		List<File> setupFileListNew = SimulationSetup.getSetupFiles(new File(fileNameXMLNew));
		for (int i = 0; i < setupFileListOld.size(); i++) {
			File setupFileOld = setupFileListOld.get(i);
			File setupFileNew = setupFileListNew.get(i);
			if (setupFileOld.exists()) {
				new FileCopier().copyFile(setupFileOld.getAbsolutePath(), setupFileNew.getAbsolutePath());
			}
		}
		
		// --- Insert new entry ----------------------------
		this.put(nameNew, fileNameNew);
		this.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_COPY, nameNew, false);
		// --- Save Project --------------------------
		this.saveProject(false);
		Application.setStatusBarMessageReady();
	}
	
	/**
	 * Set the current Setup-File to the one given by name.
	 *
	 * @param action the action
	 * @param name the name
	 * @param isAddedNew the is added new
	 */
	public boolean setupLoadAndFocus(SimNoteReason action, String name, boolean isAddedNew) {
		
		boolean done = true;
		
		if (name==null || name.isEmpty()) return false;
		if (this.containsKey(name)==false) return false;
		
		// --- Configure to the specified setup -----------
		this.currSimSetupName = name;
		this.currProject.setSimulationSetupCurrent(name);
		
		String xmlFileNameFullPath = this.currProject.getSubFolder4Setups(true) + this.get(currSimSetupName);
		this.setCurrSimXMLFile(xmlFileNameFullPath);
		
		// --- Create new instance of SimulationSetup -----
		this.currSimSetup = new SimulationSetup(this.currProject);
				
		// --- Read file if needed ------------------------ 
		if (isAddedNew==false) {
			done = this.setupOpen();
		}		
		this.currProject.setChangedAndNotify(new SimulationSetupNotification(action));
		return done;
		
	}
	
	/**
	 * This Method loads the current Simulation-Setup to the local
	 * variable 'currSimSetup' which can be get and set by using
	 * {@link #getCurrSimSetup()} or {@link #setCurrSimSetup(SimulationSetup)}.
	 */
	private boolean setupOpen() {
		
		File setupXmlFile = new File(currSimXMLFile);
		if (setupXmlFile.exists()==true) {
			this.currSimSetup = SimulationSetup.load(setupXmlFile, true);
			if (this.currSimSetup!=null) {
				this.currSimSetup.setProject(this.currProject);
			}
			
		} else {
			String head = Language.translate("Setup-Datei nicht gefunden!");
			String msg  = Language.translate("Die Datei") + " '" + this.get(currSimSetupName) + "' " + Language.translate("für das Setup") + " '" + currSimSetupName + "' " + Language.translate("wurde nicht gefunden.");
			msg += Language.translate("<br>Kann der Name aus der Liste der Setups entfernt werden?");
			msg += Language.translate("<br>Falls nicht, wird eine neue Setup-Datei erzeugt.");
			int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), msg, head, JOptionPane.YES_NO_OPTION);
			if (answer==JOptionPane.YES_OPTION) {
				this.setupRemove(currSimSetupName);
			}
		}
		return this.currSimSetup!=null;
	}
	
	/**
	 * Finds and returns the first setup name using an alphabetic order.
	 * @return the first Setup name using an alphabetic order
	 */
	public String getFirstSetup() {
		if (this.size()==0) return null;
		Vector<String> simSetupNames = new Vector<String>(this.keySet());
		Collections.sort(simSetupNames, String.CASE_INSENSITIVE_ORDER);
		this.currSimSetupName = simSetupNames.get(0); 
		return this.currSimSetupName;
	}
	
	/**
	 * This method returns a Suggestion for the Name of a Setup-File.
	 *
	 * @param inputText the input text
	 * @return a Suggestion for the Name of a Setup-File
	 */
	public String getSuggestSetupFile(String inputText) {
		
		String regExp = "[a-z;_;0-9]";
		String suggest = inputText;
		String suggestNew = "";
		
		// --- Preparations ---------------------
		suggest = suggest.toLowerCase();
		suggest = suggest.replaceAll("  ", " ");
		suggest = suggest.replace(" ", "_");
		suggest = suggest.replace("-", "_");
		suggest = suggest.replace("ä", "ae");
		suggest = suggest.replace("ö", "oe");
		suggest = suggest.replace("ü", "ue");
		
		// --- Examine all characters -----------
		for (int i = 0; i < suggest.length(); i++) {
			String SngChar = "" + suggest.charAt(i);
			if (SngChar.matches(regExp) == true ) {
				suggestNew = suggestNew + SngChar;	
			}						
	    }
		suggest = suggestNew;
		suggest = suggest.replaceAll("__", "_");
		return suggest + SimulationSetup.XML_FileSuffix;
	}
	
	/**
	 * This Method checks if the incoming Setup-Name is already
	 * used in the current List of Setups (HashMap).
	 *
	 * @param setupName2Test the setup name to test
	 * @return true, if the setup name is already used in the current list of setups
	 */
	public boolean containsSetupName(String setupName2Test) {
		
		String testSetupName = setupName2Test.toLowerCase();
		Iterator<String> it = this.keySet().iterator();
		while (it.hasNext()){
			String setupName = it.next().toLowerCase();
			if (setupName.equalsIgnoreCase(testSetupName)){
				return true;
			}
		}
		return false;		
	}
	
	/**
	 * Saves the current project.
	 */
	private void saveProject(boolean alsoSaveSetup) {
		if (this.currProject!=null) {
			if (this.currProject.isUnsaved()==true) {
				this.currProject.save(alsoSaveSetup);
			}
		}
	}
	
	/**
	 * This Method saves the current.
	 */
	public void setupSave() {
		if (this.currSimSetup!=null) {
			this.currProject.setChangedAndNotify(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_PREPARE_SAVING));
			this.currSimSetup.save();
			this.currProject.setChangedAndNotify(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_SAVED));
		}
		this.setupCleanUpSubFolder();
	}
	

	/**
	 * This Method scans the Folder of the Simulation-Setups and
	 * deletes all file, which are not used in this project.
	 */
	public void setupCleanUpSubFolder() {
		
		String pathSimXML  = this.currProject.getSubFolder4Setups(true);
		File[] files = new File(pathSimXML).listFiles();
		if (files!=null) {
			// --- Run through the list of files in folder ----------
			for (int i = 0; i < files.length; i++) {
				File checkFile = files[i];
				File setupBaseFile = SimulationSetup.getSetupBaseFile(checkFile);
				if (this.containsValue(setupBaseFile.getName())==false) {
					checkFile.delete();
				}
			} 
		} 
	}
	
	/**
	 * Returns the current {@link SimulationSetup}.
	 * @return {@link SimulationSetup}
	 */
	public SimulationSetup getCurrSimSetup() {
		if (this.currSimSetup==null && this.currSimSetupName!=null) {
			this.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, currSimSetupName, false);
		}
		return currSimSetup;
	}
	/**
	 * Sets the current {@link SimulationSetup}.
	 * @param currSimSetup the new current {@link SimulationSetup}
	 */
	public void setCurrSimSetup(SimulationSetup currSimSetup) {
		this.currSimSetup = currSimSetup;
	}

	/**
	 * Sets the current {@link SimulationSetup}'s xml file name as String (full path).
	 * @param xmlFileNameFullPath the new setup file name for the XML file (full path)
	 */
	private void setCurrSimXMLFile(String xmlFileNameFullPath) {
		this.currSimXMLFile = xmlFileNameFullPath;
	}
	/**
	 * Returns the current {@link SimulationSetup}'s xml file name as String (full path).
	 * @return the currSimXMLFile
	 */
	public String getCurrSimXMLFile() {
		return currSimXMLFile;
	}

	
}
