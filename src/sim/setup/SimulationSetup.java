package sim.setup;

import java.io.FileWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import application.Application;
import application.Project;


@XmlRootElement public class SimulationSetup {

	@XmlTransient private String currSetupName;
	@XmlTransient private String currSetupFile;
	@XmlTransient private Project currProject;	
	@XmlTransient private boolean currSetupUnsaved = false;
	
	/**
	 * Constructor without arguments (This is first of all 
	 * for the JAXB-Context and should not be used by any
	 * other context)
	 */
	public SimulationSetup() {
	}
	/**
	 * Default constructor !
	 */
	public SimulationSetup(String stpName, String stpFile, Project project) {
		this.currSetupName = stpName;
		this.currSetupFile = stpFile;
		this.currProject = project;
	}
	
	/**
	 * This method saves the current Simulation-Setup
	 * @return
	 */
	public boolean save() {
		
		// ------------------------------------------------
		// --- Folgendes passiert gerne -------------------
		// --- beim Öffnen eines Projekts -----------------
		if (currProject==null) return false;
		
		// ------------------------------------------------
		// --- Speichern des aktuellen Setups -------------
		try {			
			String XMLFileName = currProject.getProjectFolderFullPath() + currProject.getSubFolderSetups();
			XMLFileName += Application.RunInfo.AppPathSeparatorString();
			XMLFileName += currSetupFile;
			currProject.simSetups.setCurrSimXMLFile(XMLFileName);
			
			// --- Kontext und Marshaller vorbereiten -----
			JAXBContext pc = JAXBContext.newInstance( this.getClass() ); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
			pm.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 

			// --- Objektwerte in xml-Datei schreiben ----
			Writer pw = new FileWriter( XMLFileName );
			pm.marshal( this, pw );
						
			currSetupUnsaved = false;			
		} 
		catch (Exception e) {
			System.out.println("XML-Error while saving Setup-File!");
			e.printStackTrace();
		}
		return true;		
	}
	
	
}
