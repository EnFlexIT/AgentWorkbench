package agentgui.core.sim.setup;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

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

	/**
	 * Standard name for an agent start list if the current start element was configured in the 
	 * tab 'Simulation-Setup' => 'Agent-Start'   
	 */
	@XmlTransient public static final String AGENT_LIST_ManualConfiguration = "01AgentStartManual";
	/**
	 *  Standard name for an agent start list if the current start element was configured for a predefined
	 *  environment model in the tab 'Simulation-Setup' => 'Simulation Environment'   
	 */
	@XmlTransient public static final String AGENT_LIST_EnvironmentConfiguration = "02AgentStartEnvironment";
	/**
	 * This Hash holds the instances of all agent start lists
	 */
	@XmlTransient private HashMap<String, DefaultListModel> agentSetupLists = new HashMap<String, DefaultListModel>();
	

	@XmlTransient private Project currProject = null;	
	
	@XmlElementWrapper(name = "agentSetup")
	@XmlElement(name="agent")
	private ArrayList<AgentClassElement4SimStart> agentList = new ArrayList<AgentClassElement4SimStart>();

	@XmlElement(name="distribution")
	public DistributionSetup distributionSetup = new DistributionSetup();
	
	private String environmentFileName = null;
	private String svgFileName = null;
	
	/**
	 * This field can be used in order to provide customised objects during
	 * the runtime of a project. This will be not stored within the file 'agentgui.xml' 
	 */
	@XmlTransient private Serializable userRuntimeObject = null;
	
	/**
	 * Constructor without arguments (This is first of all 
	 * for the JAXB-Context and should not be used by any
	 * other context)
	 */	
	public SimulationSetup() {
	}
	/**
	 * Default Constructor of this class
	 * @param project
	 */
	public SimulationSetup(Project project) {
		this.currProject = project;
	}
	/**
	 * @param currProject the currProject to set
	 */
	public void setCurrProject(Project currProject) {
		this.currProject = currProject;
	}
	/**
	 * Will merge all default list models to one array list
	 */
	private void mergeListModels(){
		//
		agentList = new ArrayList<AgentClassElement4SimStart>();
		
		// ------------------------------------------------
		// --- Write Data from GUI to the Model -----------
		Set<String> agentListNamesSet = agentSetupLists.keySet();
		Vector<String> agentListNames = new Vector<String>();
		agentListNames.addAll(agentListNamesSet);
		Collections.sort(agentListNames);
		
		for (int i = 0; i < agentListNames.size(); i++) {
			DefaultListModel dlm = agentSetupLists.get(agentListNames.get(i));
			this.setAgentList(dlm);
		}
	}
	
	/**
	 * This method saves the current Simulation-Setup
	 * @return true if save is successful
	 */
	public boolean save() {
		boolean saved = true;
		mergeListModels();
		
		// ------------------------------------------------
		// --- Save the current simulation setup ----------
		try {			
			// --- prepare context and Marshaller ---------
			JAXBContext pc = JAXBContext.newInstance( this.getClass() ); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
			pm.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 

			// --- Objektwerte in xml-Datei schreiben ----
			Writer pw = new FileWriter( currProject.simSetups.getCurrSimXMLFile() );
			pm.marshal( this, pw );
						
			// --- Save the userRuntimeObject in the Simsetup into a different file as a serializable binary object.
			FileOutputStream fos = null;
			ObjectOutputStream out = null;
		    try
		    {
		       fos = new FileOutputStream(currProject.getSubFolder4Setups(true)+currProject.simSetupCurrent+".bin");
		       out = new ObjectOutputStream(fos);
		       out.writeObject(this.userRuntimeObject);
		       out.close();
		    }
		    catch(IOException ex)
		    {
		      ex.printStackTrace();
		      saved = false;
		    }
		    
			currProject.setNotChangedButNotify(new SimulationSetupsChangeNotification(SimulationSetups.SIMULATION_SETUP_SAVED));
		} 
		catch (Exception e) {
			System.out.println("XML-Error while saving Setup-File!");
			e.printStackTrace();
			saved = false;
		}		
		return saved;		
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
		for (int i = 0; i < lm.size(); i++) {
			agentList.add((AgentClassElement4SimStart) lm.get(i));
		}		
	}
	
	/**
	 * This method can be used in order to add an individual agent start list to the SimulationSetup.<br>
	 * The list will be filled with elements of the type {@link AgentClassElement4SimStart} coming from
	 * the stored setup file and will be later on also stored in the file of the simulation setup.
	 * 
	 * @param newDefaultListModel4AgentStarts the new DefaultListModel to set
	 * @param listName the name of the list to be assigned
	 * @see AgentClassElement4SimStart
	 */
	public void registerAgentDefaultListModel(DefaultListModel newDefaultListModel4AgentStarts, String listName) {

		newDefaultListModel4AgentStarts.removeAllElements();
		for (int i = 0; i < agentList.size(); i++) {

			AgentClassElement4SimStart ac4s = agentList.get(i);
			if (ac4s.getListMembership()==null && listName.equals(SimulationSetup.AGENT_LIST_ManualConfiguration)) {
				newDefaultListModel4AgentStarts.addElement(ac4s);
			
			} else if (ac4s.getListMembership().equals(listName)) {
				newDefaultListModel4AgentStarts.addElement(ac4s);	
			}
			
		}
		agentSetupLists.put(listName, newDefaultListModel4AgentStarts);
	}
	/**
	 * This method can be used in order to get an agents start list for the 
	 * simulation, given by  
	 * @return the agentListModel
	 */
	public DefaultListModel getAgentDefaultListModel(String listName) {
		return agentSetupLists.get(listName);
	}
	/**
	 * Here a complete agent start list can be added to the simulation setup 
	 */
	public void setAgentDefaultListModel(DefaultListModel defaultListModel4AgentStarts, String listName) {
		agentSetupLists.put(listName, defaultListModel4AgentStarts);
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
	
	/**
	 * Checks whether the agent name already exists in the current agent configuration
	 * @param localAgentName
	 * @return true, if the agent name already exists
	 */
	public boolean isAgentNameExists(String localAgentName){
		mergeListModels();
		
		for (int i = 0; i < agentList.size(); i++) {
			if(agentList.get(i).getStartAsName().equals(localAgentName))
				return true;			
		}
		return false;
	}
	/**
	 * @param userRuntimeObject the userRuntimeObject to set
	 */
	public void setUserRuntimeObject(Serializable userRuntimeObject) {
		this.userRuntimeObject = userRuntimeObject;
	}
	/**
	 * @return the userRuntimeObject
	 */
	@XmlTransient
	public Serializable getUserRuntimeObject() {
		return userRuntimeObject;
	}
}
