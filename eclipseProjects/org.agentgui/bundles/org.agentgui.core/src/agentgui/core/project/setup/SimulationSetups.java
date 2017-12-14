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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import de.enflexit.common.classLoadService.ObjectInputStreamForClassLoadService;
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
public class SimulationSetups extends Hashtable<String, String> {

	private static final long serialVersionUID = -9078535303459653695L;

	public static final String DEFAULT_SETUP_NAME = "default";
	
	private final String XML_FilePostfix = Application.getGlobalInfo().getXmlFilePostfix();
	
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
		
		String xmlFile = null;
		String XMLPathName = null;
						
		this.currSimSetupName = SimulationSetups.DEFAULT_SETUP_NAME;
		xmlFile = currProject.getSimulationSetups().getSuggestSetupFile(currSimSetupName);
		
		this.put(currSimSetupName, xmlFile);
		this.currProject.setSimulationSetupCurrent(currSimSetupName);
		
		this.currSimSetup = new SimulationSetup(this.currProject); 
		
		XMLPathName = this.currProject.getSubFolder4Setups(true) + xmlFile;		
		this.currSimXMLFile = XMLPathName;

		this.setupSave();
		
	}
	
	/**
	 * Adds a new Setup to this Hashtable.
	 * @param name the name
	 * @param newFileName the new file name
	 */
	public void setupAddNew(String name, String newFileName) {
		
		// --- Setup current setup ------------------------
		this.setupSave();
		// --- Add name and file --------------------------
		this.put(name, newFileName);
		// --- Set focus to the current setup -------------
		this.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_ADD_NEW, name, true);
		// --- Save project -------------------------------
		currProject.save();
	}

	/**
	 * Removes a Setup form this Hashtable.
	 * @param name the new up remove
	 */
	public void setupRemove(String name) {
		
		if (this.containsKey(name)==false) return;

		// --- Remove Setup -------------------------------
		this.remove(name);
		new File(currSimXMLFile).delete();
		String userObjectFileName = Application.getGlobalInfo().getBinFileNameFromXmlFileName(currSimXMLFile);
		new File(userObjectFileName).delete();
		
		// --- Notify -------------------------------------
		currProject.setChangedAndNotify(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_REMOVE));
		
		if (this.size() == 0) {
			// --- add default - Setup --------------------
			currSimSetupName = "default";
			// --- No setup found -------------------------
			String newFileName = getSuggestSetupFile(currSimSetupName);
			this.setupAddNew(currSimSetupName, newFileName);

		} else {
			// --- Load first Setup -----------------------
			this.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD,this.getFirstSetup(), false);
		}
		// --- Save project -------------------------------
		currProject.save();
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

		this.currSimSetup.save();
		
		// --- Prepare folder info and copy XML file ------
		String pathSimXML  = this.currProject.getSubFolder4Setups(true);
		String fileNameXMLNew = pathSimXML + fileNameNew; 
		FileCopier fc = new FileCopier();
		fc.copyFile(currSimXMLFile, fileNameXMLNew);
		
		// --- Copy user object file ----------------------
		String userObjectFileNameOld = Application.getGlobalInfo().getBinFileNameFromXmlFileName(this.currSimXMLFile);
		String userObjectFileNameNew = Application.getGlobalInfo().getBinFileNameFromXmlFileName(fileNameXMLNew);
		fc = new FileCopier();
		fc.copyFile(userObjectFileNameOld, userObjectFileNameNew);
		
		// --- Remove old file and old entry --------------
		new File(currSimXMLFile).delete();
		new File(userObjectFileNameOld).delete();
		this.remove(nameOld);
		
		// --- Insert new entry ----------------------------
		this.put(nameNew, fileNameNew);
		this.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_RENAME, nameNew, false);
		// --- Save Project -------------------------------
		this.currProject.save();
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
		// --- Save current state and project -------------
		this.currProject.save();
		
		// --- Prepare folder info and copy XML file ------
		String pathSimXML  = this.currProject.getSubFolder4Setups(true);
		String fileNameXMLNew = pathSimXML + fileNameNew; 
		FileCopier fc = new FileCopier();
		fc.copyFile(this.currSimXMLFile, fileNameXMLNew);
		
		// --- Copy user object file ----------------------
		String userObjectFileNameOld = Application.getGlobalInfo().getBinFileNameFromXmlFileName(this.currSimXMLFile);
		String userObjectFileNameNew = Application.getGlobalInfo().getBinFileNameFromXmlFileName(fileNameXMLNew);
		fc = new FileCopier();
		fc.copyFile(userObjectFileNameOld, userObjectFileNameNew);
		
		// --- Insert new entry ----------------------------
		this.put(nameNew, fileNameNew);
		this.setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_COPY, nameNew, false);
		// --- Save Project --------------------------
		this.currProject.save();
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
		if (this.containsKey(name)==false) return false;
		
		// --- Configure to the specified setup -----------
		this.currSimSetupName = name;
		this.currProject.setSimulationSetupCurrent(name);
		this.currSimXMLFile = this.currProject.getSubFolder4Setups(true) + this.get(currSimSetupName);
		
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
		return suggest + XML_FilePostfix;
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
	 * This Method saves the current.
	 */
	public void setupSave() {
		if (currSimSetup!=null) {
			this.currProject.setChangedAndNotify(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_PREPARE_SAVING));
			this.currSimSetup.save();
			this.currProject.setChangedAndNotify(new SimulationSetupNotification(SimNoteReason.SIMULATION_SETUP_SAVED));
		}
		this.setupCleanUpSubFolder();
	}
	
	/**
	 * This Method loads the current Simulation-Setup to the local
	 * variable 'currSimSetup' which can be get and set by using
	 * {@link #getCurrSimSetup()} or {@link #setCurrSimSetup(SimulationSetup)}.
	 */
	private boolean setupOpen() {
		
		boolean done = true;
		try {
			JAXBContext pc = JAXBContext.newInstance(currSimSetup.getClass());
			Unmarshaller um = pc.createUnmarshaller();
			FileReader fr = new FileReader(currSimXMLFile);
			this.currSimSetup = (SimulationSetup) um.unmarshal(fr);
			fr.close();
			
		} catch (FileNotFoundException e) {

			String head = Language.translate("Setup-Datei nicht gefunden!");
			String msg  = Language.translate("Die Datei") + " '" + this.get(currSimSetupName) + "' " + Language.translate("für das Setup") + " '" + currSimSetupName + "' " + Language.translate("wurde nicht gefunden.");
			msg += Language.translate("<br>Kann der Name aus der Liste der Setups entfernt werden?");
			msg += Language.translate("<br>Falls nicht, wird eine neue Setup-Datei erzeugt.");
			Integer answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), msg, head, JOptionPane.YES_NO_OPTION);
			if (answer==JOptionPane.YES_OPTION) {
				this.setupRemove(currSimSetupName);
			}			
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		// --- Set the project to the simulation setup -----------------------------
		this.currSimSetup.setProject(currProject);
		
		//--- Reading the serializable user object of the simsetup from the 'agentgui_userobject.bin' ---
		String userObjectFileName = Application.getGlobalInfo().getBinFileNameFromXmlFileName(currSimXMLFile);
		File userObjectFile = new File(userObjectFileName);
		if (userObjectFile.exists()==true) {
			Serializable userObject = null;
			FileInputStream fis = null;
			ObjectInputStreamForClassLoadService in = null;
			try {
				fis = new FileInputStream(userObjectFileName);
				in = new ObjectInputStreamForClassLoadService(fis, ClassLoadServiceUtility.class);
				userObject = (Serializable)in.readObject();
				in.close();
				this.currSimSetup.setUserRuntimeObject(userObject);
				
			} catch(IOException ex) {
				ex.printStackTrace();
				try {
					in.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				return false;
				
			} catch(ClassNotFoundException ex) {
				ex.printStackTrace();
				try {
					in.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				return false;
			
			}
		}
		
		// --- Create the DefaultListModels for the current agent configuration ---- 
		this.currSimSetup.createHashMap4AgentDefaulListModelsFromAgentList();
		
		// --- Set the agent classes in the agentSetup -----------------------------
		ArrayList<AgentClassElement4SimStart> agentList = currSimSetup.getAgentList();
		for (int i = 0; i < agentList.size(); i++) {
			try {
				// --- The .toSting method will check if the class exists ----------
				agentList.get(i).toString();
				
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}
		this.currSimSetup.setProject(currProject);
		
		return done;
	}

	/**
	 * This Method scans the Folder of the Simulation-Setups and
	 * deletes all file, which are not used in this project.
	 */
	public void setupCleanUpSubFolder() {
		
		String pathSimXML  = currProject.getSubFolder4Setups(true);
		File[] files = new File(pathSimXML).listFiles();
		if (files != null) {
			// --- Run through the list of files in folder ----------
			for (int i = 0; i < files.length; i++) {
				// --- Just work on files ending with xml -----------
				if (files[i].getName().endsWith(XML_FilePostfix)) {
					if (this.containsValue(files[i].getName())==false) {
						// --- If not in the list of setups, delete -
						files[i].delete();
						// --- Also delete the bin-file, if there ---
						String binFileName = Application.getGlobalInfo().getBinFileNameFromXmlFileName(files[i].getAbsolutePath());
						if (binFileName!=null) {
							File binFile = new File(binFileName);
							if (binFile.exists()==true) {
								binFile.delete();
							}
						}
					}
				} // end is xml-file
			} // end for
		} // end if
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
	 * Sets the current {@link SimulationSetup}'s xml file name as String.
	 * @param currSimXMLFile the currSimXMLFile to set
	 */
	public void setCurrSimXMLFile(String currSimXMLFile) {
		this.currSimXMLFile = currSimXMLFile;
	}
	/**
	 * Returns the current {@link SimulationSetup}'s xml file name as String.
	 * @return the currSimXMLFile
	 */
	public String getCurrSimXMLFile() {
		return currSimXMLFile;
	}

	
}
