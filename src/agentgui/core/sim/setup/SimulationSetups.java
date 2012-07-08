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
package agentgui.core.sim.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
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


import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.FileCopier;
import agentgui.core.project.Project;

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

	public static final String CHANGED = "SimSetups";
	public static final int SIMULATION_SETUP_LOAD = 0;
	public static final int SIMULATION_SETUP_ADD_NEW = 1;
	public static final int SIMULATION_SETUP_COPY = 2;
	public static final int SIMULATION_SETUP_REMOVE = 3;
	public static final int SIMULATION_SETUP_RENAME = 4;
	public static final int SIMULATION_SETUP_SAVED = 5;
	
	private final String XML_FilePostfix = Application.getGlobalInfo().getXmlFilePostfix();
	
	private Project currProject = Application.ProjectCurr;
	private SimulationSetup currSimSetup = null;
	
	private String currSimSetupName = null;
	private String currSimXMLFile = null;

	
	/**
	 * Constructor of this class.
	 *
	 * @param project the project
	 * @param currentSimulationSetup the current simulation setup 
	 */
	public SimulationSetups(Project project, String currentSimulationSetup) {
		currProject = project;
		currSimSetupName = currentSimulationSetup;
	}
	
	/**
	 * This Method creates the 'default' - Simulation-Setup.
	 */
	public void setupCreateDefault() {
		
		String xmlFile = null;
		String XMLPathName = null;
						
		this.currSimSetupName = "default";
		xmlFile = currProject.simulationSetups.getSuggestSetupFile(currSimSetupName);
		
		this.put(currSimSetupName, xmlFile);
		currProject.simulationSetupCurrent = currSimSetupName;
		
		currSimSetup = new SimulationSetup(currProject); 
		
		XMLPathName = currProject.getSubFolder4Setups(true) + xmlFile;		
		currSimXMLFile = XMLPathName;

		this.setupSave();
		
	}
	
	/**
	 * Adds a new Setup to this Hashtable.
	 *
	 * @param name the name
	 * @param newFileName the new file name
	 */
	public void setupAddNew(String name, String newFileName) {
		
		// --- Aktuelles Setup speichern ------------------
		this.setupSave();
		// --- Name und Dateiname hinzufügen --------------
		this.put(name, newFileName);
		// --- Fokus auf das aktuelle Setup ---------------
		this.setupLoadAndFocus(SIMULATION_SETUP_ADD_NEW, name, true);
		// --- Projekt speichern --------------------------
		currProject.save();
	}

	/**
	 * Removes a Setup form this Hashtable.
	 *
	 * @param name the new up remove
	 */
	public void setupRemove(String name) {
		
		if (this.containsKey(name)==false) return;

		// --- Setup rausschmeissen------------------------
		this.remove(name);
		new File(currSimXMLFile).delete();
		String userObjectFileName = Application.getGlobalInfo().getBinFileNameFromXmlFileName(currSimXMLFile);
		new File(userObjectFileName).delete();
		
		// --- Interessenten informieren ------------------
		currProject.setChangedAndNotify(new SimulationSetupsChangeNotification(SIMULATION_SETUP_REMOVE));
		
		// --- Groesse des Rests berücksichtigen ----------
		if (this.size() == 0) {
			// --- add default - Setup --------------------
			currSimSetupName = "default";
			// --- Noch keine Setups gespeichert ----------
			String newFileName = getSuggestSetupFile(currSimSetupName);
			this.setupAddNew(currSimSetupName, newFileName);

		} else {
			// --- Load first Setup -----------------------
			this.setupLoadAndFocus(SIMULATION_SETUP_LOAD,this.getFirstSetup(), false);
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
		
		// --- Verzeichnis-Info zusammenbauen -------------
		String pathSimXML  = currProject.getSubFolder4Setups(true);
		String fileNameXMLNew = pathSimXML + fileNameNew; 
		// --- Datei kopieren -----------------------------
		FileCopier fc = new FileCopier();
		fc.copyFile(currSimXMLFile, fileNameXMLNew);
		
		// --- Copy user object file ----------------------
		String userObjectFileNameOld = Application.getGlobalInfo().getBinFileNameFromXmlFileName(currSimXMLFile);
		String userObjectFileNameNew = Application.getGlobalInfo().getBinFileNameFromXmlFileName(fileNameXMLNew);
		
		fc = new FileCopier();
		fc.copyFile(userObjectFileNameOld, userObjectFileNameNew);
		
		// --- Alte Datei und alten Eintrag raus ----------
		new File(currSimXMLFile).delete();
		new File(userObjectFileNameOld).delete();
		this.remove(nameOld);
		// --- Neuen Eintrag rein -------------------------
		this.put(nameNew, fileNameNew);
		this.setupLoadAndFocus(SIMULATION_SETUP_RENAME, nameNew, false);
		// --- Projekt speichern --------------------------
		currProject.save();
	}

	/**
	 * Copies a Setup and the associated file.
	 *
	 * @param nameOld the name old
	 * @param nameNew the name new
	 * @param fileNameNew the file name new
	 */
	public void setupCopy(String nameOld, String nameNew, String fileNameNew) {

		if (this.containsKey(nameOld)==false) return;
		// --- Aktuellen Zustand speichern ----------------
		currProject.save();
		
		// --- Verzeichnis-Info zusammenbauen -------------
		String pathSimXML  = currProject.getSubFolder4Setups(true);
		String fileNameXMLNew = pathSimXML + fileNameNew; 
		// --- Datei kopieren -----------------------------
		FileCopier fc = new FileCopier();
		fc.copyFile(currSimXMLFile, fileNameXMLNew);
		// --- Name und Dateiname hinzufügen --------------
		this.put(nameNew, fileNameNew);
		// --- Fokus auf das aktuelle Setup ---------------
		this.setupLoadAndFocus(SIMULATION_SETUP_COPY,nameNew, false);
		// --- Projekt speichern --------------------------
		currProject.save();
	}
	
	/**
	 * Set the current Setup-File to the one given by name.
	 *
	 * @param action the action
	 * @param name the name
	 * @param isAddedNew the is added new
	 */
	public void setupLoadAndFocus(int action, String name, boolean isAddedNew) {
		
		if (this.containsKey(name)==false) return;
		
		// --- Aktuelles Setup auf Input 'name' -----------
		currSimSetupName = name;
		currProject.simulationSetupCurrent = name;
		currSimXMLFile = currProject.getSubFolder4Setups(true) + this.get(currSimSetupName);
		
		// --- 'SimulationSetup'-Objekt neu instanziieren -
		currSimSetup = new SimulationSetup(currProject);
				
		// --- Datei lesen und currSimSetup setzen -------- 
		if (isAddedNew==false) {
			this.setupOpen();	
		}		
		
		// --- Interessenten informieren ------------------
		currProject.setChangedAndNotify(new SimulationSetupsChangeNotification(action));
		
	}
	
	/**
	 * Finds and returns the first Setup name using an alphabetic order.
	 *
	 * @return the first Setup name using an alphabetic order
	 */
	public String getFirstSetup() {

		if (this.size()==0) return null;
			
		Vector<String> v = new Vector<String>(this.keySet());
		Collections.sort(v, String.CASE_INSENSITIVE_ORDER);
		Iterator<String> it = v.iterator();
		while (it.hasNext()) {
			currSimSetupName = it.next();
			break;
		}
		return currSimSetupName;
	}
	
	/**
	 * This method returns a Suggestion for the Name of a Setup-File.
	 *
	 * @param inputText the input text
	 * @return a Suggestion for the Name of a Setup-File
	 */
	public String getSuggestSetupFile(String inputText) {
		
		String RegExp = "[a-z;_;0-9]";
		String suggest = inputText;
		String suggestNew = "";
		
		// --- Vorarbeiten ------------------------------
		suggest = suggest.toLowerCase();
		suggest = suggest.replaceAll("  ", " ");
		suggest = suggest.replace(" ", "_");
		suggest = suggest.replace("-", "_");
		suggest = suggest.replace("ä", "ae");
		suggest = suggest.replace("ö", "oe");
		suggest = suggest.replace("ü", "ue");
		
		// --- Alle Buchstaben untersuchen --------------
		for (int i = 0; i < suggest.length(); i++) {
			String SngChar = "" + suggest.charAt(i);
			if ( SngChar.matches( RegExp ) == true ) {
				suggestNew = suggestNew + SngChar;	
			}						
	    }
		suggest = suggestNew;
		suggest = suggest.replaceAll("__", "_");
		return suggest + XML_FilePostfix;
	}
	
	/**
	 * This Method checks if the incomming Setup-Name is already
	 * used in the current List of Setups (Hashmap).
	 *
	 * @param setupName2Test the setup name2 test
	 * @return true if the setup name is already used in the current list of setups
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
			currSimSetup.save();
			currProject.setChangedAndNotify(new SimulationSetupsChangeNotification(SIMULATION_SETUP_SAVED));
		}
		this.setupCleanUpSubFolder();
	}
	
	/**
	 * This Method loads the current Simulation-Setup to the local
	 * variable 'currSimSetup' which can be get and set by using
	 * 'getCurrSimSetup' or 'setCurrSimSetup'.
	 */
	private void setupOpen() {
		
		String head=null, msg =null;;
		Integer answer = 0;
		JAXBContext pc = null;
		Unmarshaller um = null;
		
		try {
			pc = JAXBContext.newInstance(currSimSetup.getClass());
			um = pc.createUnmarshaller();
			FileReader fr = new FileReader(currSimXMLFile);
			currSimSetup = (SimulationSetup) um.unmarshal(fr);
			fr.close();
			
		} catch (FileNotFoundException e) {

			// --- Die Datei wurde nicht gefunden ---------
			head = Language.translate("Setup-Datei nicht gefunden!");
			msg  = Language.translate("Die Datei") + " '" + this.get(currSimSetupName) + "' " + Language.translate("für das Setup") + " '" + currSimSetupName + "' " + Language.translate("wurde nicht gefunden.");
			msg += Language.translate("<br>Kann der Name aus der Liste der Setups entfernt werden?");
			msg += Language.translate("<br>Falls nicht, wird eine neue Setup-Datei erzeugt.");
			answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), msg, head, JOptionPane.YES_NO_OPTION);
			if ( answer == JOptionPane.YES_OPTION  ) {
				this.setupRemove(currSimSetupName);
			}			
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// --- Set the project to the simulation setup -----------------------------
		currSimSetup.setCurrProject(currProject);
		
		//--- Reading the serializable user object of the simsetup from the 'agentgui_userobject.bin' ---
		String userObjectFileName = Application.getGlobalInfo().getBinFileNameFromXmlFileName(currSimXMLFile);
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
				currSimSetup.setUserRuntimeObject(userObject);
				
			} catch(IOException ex) {
				ex.printStackTrace();
			} catch(ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		
		// --- Create the DefaultListModels for the current agent configuration ---- 
		currSimSetup.createHashMap4AgentDefaulListModelsFromAgentList();

		// --- Set the agent classes in the agentSetup -----------------------------
		ArrayList<AgentClassElement4SimStart> agentList = currSimSetup.getAgentList();
		for (int i = 0; i < agentList.size(); i++) {
			// --- The .toSting method will check if the class is ---------   
			// --- there and initialize the corresponding attribute -------
			agentList.get(i).toString();
		}
		currSimSetup.setCurrProject(currProject);
	}

	/**
	 * This Method scans the Folder of the Simulation-Setups and
	 * deletes all file, which are not used in this project.
	 */
	public void setupCleanUpSubFolder() {
		
		String pathSimXML  = currProject.getSubFolder4Setups(true);
		File[] files = new File(pathSimXML).listFiles();
		if (files != null) {
			// --- Auflistung der Dateien durchlaufen -----
			for (int i = 0; i < files.length; i++) {
				// --- Nur xml-Dateien beachten -----------
				if (files[i].getName().endsWith(XML_FilePostfix)) {
					if (this.containsValue(files[i].getName())==false) {
						files[i].delete();
						// --- bin File ebenfalls löschen -
						String binFileName = Application.getGlobalInfo().getBinFileNameFromXmlFileName(files[i].getAbsolutePath());
						if (binFileName!=null) {
							File binFile = new File(binFileName);
							if (binFile.exists()==true) {
								binFile.delete();
							}
						}
						// --------------------------------
					}
				}
			}
			// --------------------------------------------
		}
	}
	
	/**
	 * Gets the curr sim setup.
	 *
	 * @return the currSimSetup
	 */
	public SimulationSetup getCurrSimSetup() {
		return currSimSetup;
	}
	
	/**
	 * Sets the curr sim setup.
	 *
	 * @param currSimSetup the currSimSetup to set
	 */
	public void setCurrSimSetup(SimulationSetup currSimSetup) {
		this.currSimSetup = currSimSetup;
	}

	/**
	 * Sets the curr sim xml file.
	 *
	 * @param currSimXMLFile the currSimXMLFile to set
	 */
	public void setCurrSimXMLFile(String currSimXMLFile) {
		this.currSimXMLFile = currSimXMLFile;
	}
	
	/**
	 * Gets the curr sim xml file.
	 *
	 * @return the currSimXMLFile
	 */
	public String getCurrSimXMLFile() {
		return currSimXMLFile;
	}

	
}
