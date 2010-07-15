package sim.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import common.FileCopier;

import application.Application;
import application.Project;

public class SimulationSetups extends Hashtable<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9078535303459653695L;
	
	private Project currProject = null;
	
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
		
		if (this.size()==0) {
			// --- Noch keine Setups gespeichert --------------------
			String newFileName = getSuggestSetupFile(currSimSetupName) + ".xml";;
			this.setupAddNew(currSimSetupName, newFileName);
		} else {
			// --- Es gibt Setups -----------------------------------
			if (this.containsKey(currSimSetupName)) {
				// --- Es gibt auch ein Setup mit diesem Namen ------
				this.setupLoadAndFocus(currSimSetupName, false);
			} else {
				// --- Setup mit diesem Namen nicht gefunden --------
				this.setupLoadAndFocus(this.getFirstSetup(), false);				
			}
		}
		// --- Aktuelles SimulationSetup-Objekt instanziieren -------
		currSimSetup = new SimulationSetup(currSimSetupName, this.get(currSimSetupName), currProject);
	}

	/**
	 * Adds a new Setup to this Hashtable
	 * @param name
	 * @param newFileName
	 */
	public void setupAddNew(String name, String newFileName) {
		// --- Name und Dateiname hinzufügen --------------
		this.put(name, newFileName);
		// --- Fokus auf das aktuelle Setup ---------------
		this.setupLoadAndFocus(name, true);
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
			String newFileName = getSuggestSetupFile(currSimSetupName) + ".xml";
			this.setupAddNew(currSimSetupName, newFileName);

		} else {
			// --- Select first Setup ---------------------
			this.setupLoadAndFocus(this.getFirstSetup(), false);
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
		String pathSimXML  = currProject.getProjectFolderFullPath() + currProject.getSubFolderSetups() + Application.RunInfo.AppPathSeparatorString();
		String fileNameXMLNew = pathSimXML + fileNameNew; 
		// --- Datei umbenennen ---------------------------		
		File fileOld = new File(currSimXMLFile);
		File fileNew = new File(fileNameXMLNew);
		fileOld.renameTo(fileNew);
		// --- alten Eintrag raus, neuen rein -------------
		this.remove(nameOld);
		this.setupAddNew(nameNew, fileNameNew);
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
		String pathSimXML  = currProject.getProjectFolderFullPath() + currProject.getSubFolderSetups() + Application.RunInfo.AppPathSeparatorString();
		String fileNameXMLNew = pathSimXML + fileNameNew; 
		// --- Datei kopieren -----------------------------
		FileCopier fc = new FileCopier();
		fc.copyFile(currSimXMLFile, fileNameXMLNew);
		// --- Name und Dateiname hinzufügen --------------
		this.put(nameNew, fileNameNew);
		// --- Fokus auf das aktuelle Setup ---------------
		this.setupLoadAndFocus(nameNew, false);
		// --- Projekt speichern --------------------------
		currProject.save();
	}
		
	
	
	/**
	 * Set the current Setup-File to the one given by name
	 * @param name
	 */
	public void setupLoadAndFocus(String name, boolean isAddedNew) {
		
		if (this.containsKey(name)==false) return;
		
		// --- aktuelles Setup speichern ------------------
		this.setupSave();
		
		// --- Aktuelles Projekt auf Input 'name' ---------
		currSimSetupName = name;
		currProject.simSetupCurrent = name;
		
		// --- 'SimulationSetup'-Objekt neu instanziieren -
		currSimSetup = new SimulationSetup(currSimSetupName, this.get(currSimSetupName), currProject);
		
		// --- Datei lesen und currSimSetup setzen -------- 
		if (isAddedNew==false) {
			this.setupOpen();	
		}		
		
		// --- Interessenten informieren ------------------
		currProject.setChangedAndNotify("SimSetups");
		
	}
	
	/**
	 * Finds and returns the first Setup name using an alphabetic order  
	 * @return
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
	 * @return
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
		return suggest;
	}
	
	/**
	 * This Method saves the current
	 * @return Simulation-Setup
	 */
	public boolean setupSave() {
		if (currSimSetup==null) {
			return false;			
		} else {
			return currSimSetup.save();	
		}
	}
	
	/**
	 * This Method loads the current Simulation-Setup to the local
	 * variable 'currSimSetup' which can be get and set by using
	 * 'getCurrSimSetup' or 'setCurrSimSetup'
	 */
	private void setupOpen() {
		
		String XMLFileName = null;
		JAXBContext pc = null;
		Unmarshaller um = null;
		
		XMLFileName  = currProject.getProjectFolderFullPath() + currProject.getSubFolderSetups();
		XMLFileName += Application.RunInfo.AppPathSeparatorString();
		XMLFileName += this.get(currSimSetupName);
		currSimXMLFile = XMLFileName;
		
		try {
			pc = JAXBContext.newInstance( currSimSetup.getClass() );
			um = pc.createUnmarshaller();
			currSimSetup = (SimulationSetup) um.unmarshal( new FileReader( XMLFileName ) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
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
