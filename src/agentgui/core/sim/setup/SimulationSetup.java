package agentgui.core.sim.setup;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Project;


@XmlRootElement public class SimulationSetup {

	@XmlTransient private Project currProject = null;	
	@XmlTransient private DefaultListModel agentListModel = null;	
	
	@XmlElementWrapper(name = "agentSetup")
	@XmlElement(name="agent")
	private ArrayList<AgentClassElement4SimStart> agentList = new ArrayList<AgentClassElement4SimStart>();
	
	public static final String ENVMODE_PHYSICAL2D = "physical2DEnvironment";
	public static final String ENVMODE_GRAPH = "gridEnvironment";
	
	
	private String environmentMode = ENVMODE_PHYSICAL2D;
	/**
	 * @return the environmentMode
	 */
	public String getEnvironmentMode() {
		return environmentMode;
	}
	/**
	 * @param environmentMode the environmentMode to set
	 */
	public void setEnvironmentMode(String environmentMode) {
		this.environmentMode = environmentMode;
	}
	/**
	 * The name of the environment file assigned to this SimulationSetup
	 */
	private String environmentFileName = null;
	/**
	 * The name of the svg file assigned to this SimulationSetup
	 */
	private String svgFileName = null;
	
	@XmlElement(name="distribution")
	public DistributionSetup distributionSetup = new DistributionSetup();
	
	
	/**
	 * Constructor without arguments (This is first of all 
	 * for the JAXB-Context and should not be used by any
	 * other context)
	 */
	public SimulationSetup() {
	}
	/**
	 * @param currProject the currProject to set
	 */
	public void setCurrProject(Project currProject) {
		this.currProject = currProject;
	}
	/**
	 * Default Constructor of this class
	 * @param project
	 */
	public SimulationSetup(Project project) {
		this.currProject = project;
	}

	/**
	 * This method saves the current Simulation-Setup
	 * @return
	 */
	public boolean save() {
		
		// ------------------------------------------------
		// --- Daten vom GUI in das Modell schreiben ------
		this.setAgentList(this.agentListModel);
		
		// ------------------------------------------------
		// --- Speichern des aktuellen Setups -------------
		try {			
			// --- Kontext und Marshaller vorbereiten -----
			JAXBContext pc = JAXBContext.newInstance( this.getClass() ); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
			pm.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 

			// --- Objektwerte in xml-Datei schreiben ----
			Writer pw = new FileWriter( currProject.simSetups.getCurrSimXMLFile() );
			pm.marshal( this, pw );
						
		} 
		catch (Exception e) {
			System.out.println("XML-Error while saving Setup-File!");
			e.printStackTrace();
		}
		return true;		
	}
	
	/**
	 * @return the agentList
	 */
	@XmlTransient
	public ArrayList<AgentClassElement4SimStart> getAgentList() {
		return agentList;
	}
	/**
	 * @param agentList the agentList to set
	 */
	public void setAgentList(ArrayList<AgentClassElement4SimStart> agentList) {
		this.agentList = agentList;
	}
	/**
	 * This Method transfers a DefaultListModel to 
	 * the localArrayList 'agentList' which is a 
	 * type of 'AgentClassElement4SimStart'
	 * @param lm
	 */
	public void setAgentList(DefaultListModel lm) {
		if (lm==null) return;
		agentList = new ArrayList<AgentClassElement4SimStart>();
		for (int i = 0; i < lm.size(); i++) {
			agentList.add((AgentClassElement4SimStart) lm.get(i));
		}		
	}
	
	/**
	 * @param agentListModel the agentListModel to set
	 */
	public void setAgentListModel(DefaultListModel agentListModel) {
		this.agentListModel = agentListModel;
		this.agentListModel.removeAllElements();
		for (int i = 0; i < agentList.size(); i++) {
			this.agentListModel.addElement(agentList.get(i));
		}
	}
	/**
	 * @return the agentListModel
	 */
	public DefaultListModel getAgentListModel() {
		return agentListModel;
	}
	
	
	/**
	 * @return the svgFileName
	 */
	public String getSvgFileName() {
		return svgFileName;
	}
	/**
	 * @param svgFileName the svgFileName to set
	 */
	public void setSvgFileName(String svgFileName) {
		this.svgFileName = svgFileName;
	}
	public String getEnvironmentFileName() {
		return environmentFileName;
	}
	public void setEnvironmentFileName(String environmentFile) {
		this.environmentFileName = environmentFile;
	}
	
	
	/**
	 * @return the distributionSetup
	 */
	@XmlTransient
	public DistributionSetup getDistributionSetup() {
		return distributionSetup;
	}
	/**
	 * @param distributionSetup the distributionSetup to set
	 */
	public void setDistributionSetup(DistributionSetup distributionSetup) {
		this.distributionSetup = distributionSetup;
	}
	
	
}
