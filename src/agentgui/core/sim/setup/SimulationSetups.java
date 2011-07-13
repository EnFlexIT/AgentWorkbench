package agentgui.core.sim.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
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
import agentgui.core.application.Project;
import agentgui.core.common.FileCopier;

public class SimulationSetups extends Hashtable<String, String> {

	private static final long serialVersionUID = -9078535303459653695L;

	public static final String CHANGED = "SimSetups";
	public static final int SIMULATION_SETUP_LOAD = 0;
	public static final int SIMULATION_SETUP_ADD_NEW = 1;
	public static final int SIMULATION_SETUP_COPY = 2;
	public static final int SIMULATION_SETUP_REMOVE = 3;
	public static final int SIMULATION_SETUP_RENAME = 4;
	public static final int SIMULATION_SETUP_SAVED = 5;
	
	public final String XML_FilePostfix = ".xml";
	private Project currProject = Application.ProjectCurr;
	
	private String currSimSetupName = null;
	private String currSimXMLFile = null;
	private SimulationSetup currSimSetup = null;
	
	/**
	 * Constructor of this class
	 * @param project
	 */
	public SimulationSetups(Project project, String simSetupCurrent) {
		currProject = project;
		currSimSetupName = simSetupCurrent;
	}
	
	/**
	 * This Method creates the 'default' - Simulation-Setup 
	 */
	public void setupCreateDefault() {
		
		String xmlFile = null;
		String XMLPathName = null;
						
		this.currSimSetupName = "default";
		xmlFile = currProject.simSetups.getSuggestSetupFile(currSimSetupName);
		
		this.put(currSimSetupName, xmlFile);
		currProject.simSetupCurrent = currSimSetupName;
		
		currSimSetup = new SimulationSetup(currProject); 
		
		XMLPathName = currProject.getSubFolder4Setups(true) + xmlFile;		
		currSimXMLFile = XMLPathName;

		this.setupSave();
		
	}
	
	/**
	 * Adds a new Setup to this Hashtable
	 * @param name
	 * @param newFileName
	 */
	public void setupAddNew(String name, String newFileName) {
		
		// --- Aktuelles Setup speichern ------------------
		this.setupSave();
		// --- Name und Dateiname hinzufügen --------------
		this.put(name, newFileName);
		// --- Fokus auf das aktuelle Setup ---------------
		this.setupLoadAndFocus(SIMULATION_SETUP_ADD_NEW,name, true);
		// --- Projekt speichern --------------------------
		currProject.save();
	}

	/**
	 * Removes a Setup form this Hashtable
	 * @param name
	 */
	public void setupRemove(String name) {
		
		if (this.containsKey(name)==false) return;

		// --- Setup rausschmeissen------------------------
		this.remove(name);
		new File(currSimXMLFile).delete();
		// --- Groesse des Rests berücksichtigen ----------
		if (this.size() == 0) {
			// --- add default - Setup --------------------
			currSimSetupName = "default";
			// --- Noch keine Setups gespeichert ----------
			String newFileName = getSuggestSetupFile(currSimSetupName);
			this.setupAddNew(currSimSetupName, newFileName);

		} else {
			// --- Select first Setup ---------------------
			this.setupLoadAndFocus(SIMULATION_SETUP_REMOVE,this.getFirstSetup(), false);
		}
		// --- Projekt speichern --------------------------
		currProject.save();
	}
	
	/**
	 * Renames a Setup and the associated file
	 * @param nameOld
	 * @param nameNew
	 * @param fileNameNew
	 */
	public void setupRename(String nameOld, String nameNew, String fileNameNew) {

		if (this.containsKey(nameOld)==false) return;

		// --- Verzeichnis-Info zusammenbauen -------------
		String pathSimXML  = currProject.getSubFolder4Setups(true);
		String fileNameXMLNew = pathSimXML + fileNameNew; 
		// --- Datei kopieren -----------------------------
		FileCopier fc = new FileCopier();
		fc.copyFile(currSimXMLFile, fileNameXMLNew);
		// --- Alte Datei und alten Eintrag raus ----------
		new File(currSimXMLFile).delete();
		this.remove(nameOld);
		// --- Neuen Eintrag rein -------------------------
		this.put(nameNew, fileNameNew);
		this.setupLoadAndFocus(SIMULATION_SETUP_RENAME,nameNew, false);
		// --- Projekt speichern --------------------------
		currProject.save();
	}

	/**
	 * Copies a Setup and the associated file
	 * @param nameOld
	 * @param nameNew
	 * @param fileNameNew
	 */
	public void setupCopy(String nameOld, String nameNew, String fileNameNew) {

		if (this.containsKey(nameOld)==false) return;
		
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
	 * Set the current Setup-File to the one given by name
	 * @param name
	 */
	public void setupLoadAndFocus(int action, String name, boolean isAddedNew) {
		
		if (this.containsKey(name)==false) return;
		
		// --- Aktuelles Setup auf Input 'name' -----------
		currSimSetupName = name;
		currProject.simSetupCurrent = name;
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
	 * Finds and returns the first Setup name using an alphabetic order  
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
	 * This method returns a Suggestion for the Name of a Setup-File
	 * @param inputText
	 * @return returns a Suggestion for the Name of a Setup-File
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
	 * used in the current List of Setups (Hashmap)
	 * @param setupName2Test
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
	 * This Method saves the current
	 */
	public void setupSave() {
		if (currSimSetup!=null) {
			currSimSetup.save();
		}
		this.setupCleanUpSubFolder();
	}
	
	/**
	 * This Method loads the current Simulation-Setup to the local
	 * variable 'currSimSetup' which can be get and set by using
	 * 'getCurrSimSetup' or 'setCurrSimSetup'
	 */
	private void setupOpen() {
		
		String head=null, msg =null;;
		Integer answer = 0;
		JAXBContext pc = null;
		Unmarshaller um = null;
		
		try {
			pc = JAXBContext.newInstance( currSimSetup.getClass() );
			um = pc.createUnmarshaller();
			// --- 'SimulationSetup'-Objekt neu "instanziieren" -
			currSimSetup = (SimulationSetup) um.unmarshal( new FileReader( currSimXMLFile ) );
			currSimSetup.setCurrProject(currProject);
			
			//--- Reading the serializable user object of the simsetup from the 'agentgui_userobject.bin' ---
			String userObjectFileName = currProject.getSubFolder4Setups(true)+ currSimSetupName +".bin" ;
			Serializable userObject = null;
			FileInputStream fis = null;
			ObjectInputStream in = null;
	    	  try
	    	  {
	    	    fis = new FileInputStream(userObjectFileName);
	    	    in = new ObjectInputStream(fis);
	    	    userObject = (Serializable)in.readObject();
	    	    in.close();
	    	  }
	    	  catch(IOException ex)
	    	  {
	    		 ex.printStackTrace();
	    	  }
	    	  catch(ClassNotFoundException ex)
	    	  {
	    	    ex.printStackTrace();
	    	  }
	    	  currSimSetup.setUserRuntimeObject(userObject);

		} catch (FileNotFoundException e) {

			// --- Die Datei wurde nicht gefunden ---------
			head = Language.translate("Setup-Datei nicht gefunden!");
			msg  = Language.translate("Die Datei") + " '" + this.get(currSimSetupName) + "' " + Language.translate("für das Setup") + " '" + currSimSetupName + "' " + Language.translate("wurde nicht gefunden.");
			msg += Language.translate("<br>Kann der Name aus der Liste der Setups entfernt werden?");
			msg += Language.translate("<br>Falls nicht, wird eine neue Setup-Datei erzeugt.");
			answer = JOptionPane.showConfirmDialog(Application.MainWindow, msg, head, JOptionPane.YES_NO_OPTION);
			if ( answer == JOptionPane.YES_OPTION  ) {
				this.setupRemove(currSimSetupName);
			}			
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * This Method scans the Folder of the Simulation-Setups and
	 * deletes all file, which are not used in this project
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
					}
				}
			}
			// --------------------------------------------
		}
	}
	
	/**
	 * @return the currSimSetup
	 */
	public SimulationSetup getCurrSimSetup() {
		return currSimSetup;
	}
	/**
	 * @param currSimSetup the currSimSetup to set
	 */
	public void setCurrSimSetup(SimulationSetup currSimSetup) {
		this.currSimSetup = currSimSetup;
	}

	/**
	 * @param currSimXMLFile the currSimXMLFile to set
	 */
	public void setCurrSimXMLFile(String currSimXMLFile) {
		this.currSimXMLFile = currSimXMLFile;
	}
	/**
	 * @return the currSimXMLFile
	 */
	public String getCurrSimXMLFile() {
		return currSimXMLFile;
	}

	
}
