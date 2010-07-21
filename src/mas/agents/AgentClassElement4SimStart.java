package mas.agents;

import jade.core.Agent;

import java.text.DecimalFormat;

import javax.xml.bind.annotation.XmlTransient;

public class AgentClassElement4SimStart {
	
	private Class<? extends Agent> agentClass = null;
	private String agentClassReference = null;
	private String startAsName = "";
	
	private Integer postionNo = 0;
	@XmlTransient private DecimalFormat df = new DecimalFormat("00000");
	
	private boolean mobileAgent = true;
	
	/**
	 * Constructor without arguments (This is first of all 
	 * for the JAXB-Context and should not be used by any
	 * other context)
	 */
	public AgentClassElement4SimStart() {
	}
	/**
	 * Constructor of this class by using the Class which extends Agent 
	 * @param agentClass
	 */
	public AgentClassElement4SimStart(Class<? extends Agent> agentClass){
		this.agentClass=agentClass;
		this.agentClassReference = this.agentClass.getName();
		this.setDefaultAgentName();
	}
	/**
	 * Constructor of this class by using an AgentClassElement-Object
	 * @param agentClass
	 */
	public AgentClassElement4SimStart(AgentClassElement agentClass){
		this.agentClass=agentClass.getElementClass();
		this.agentClassReference = this.agentClass.getName();
		this.setDefaultAgentName();
	}
	
	/**
	 * Sets a default Name for the executed Agent in the Simulation-Experiment
	 */
	private void setDefaultAgentName() {
		
			// -----------------------------------------------------
			// --- Vorschlag für den Ausführungsnamen finden -------
			String StartAs = agentClass.getName();
			StartAs = StartAs.substring(StartAs.lastIndexOf(".")+1);
			// -----------------------------------------------------
			// --- Alle Großbuchstaben filtern ---------------------
			String RegExp = "[A-Z]";	
			String StartAsNew = ""; 
			for (int i = 0; i < StartAs.length(); i++) {
				String SngChar = "" + StartAs.charAt(i);
				if ( SngChar.matches( RegExp ) == true ) {
					StartAsNew = StartAsNew + SngChar;	
					// --- ggf. den zweiten Buchstaben mitnehmen ---
					if ( i < StartAs.length() ) {
						String SngCharN = "" + StartAs.charAt(i+1);
						if ( SngCharN.matches( RegExp ) == false ) {
							StartAsNew = StartAsNew + SngCharN;	
						}
					}	
					// ---------------------------------------------
				}						
		    }
			if ( StartAsNew != "" && StartAsNew.length() >= 4 ) {
				StartAs = StartAsNew;
			}
			// -----------------------------------------------------
			// --- Check, ob dieser Agent schon im Setup ist -------
			//TODO: Check, ob der Agent schon
			// -----------------------------------------------------
			// --- Vorschlagsnamen einstellen ----------------------	
			startAsName = StartAs;
		
	}
	
	/**
	 * Retruns the String-Descriptoin of this Object
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String toString(){
		
		// --- Existiert die Agenten-Klasseninstanz ? -----
		if (agentClass== null) {
			try {
				// --- Gibt es die Klasse überhaupt? ------
				agentClass = (Class<? extends Agent>) Class.forName(agentClassReference);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} 
		
		// --- Ausgabe zusammenstellen --------------------
		if (agentClass==null) {
			return "NotFound: " + df.format(postionNo) + ": " + startAsName + " [" + agentClassReference + "]";
		} else {
			return df.format(postionNo) + ": " + startAsName + " [" + agentClass.getName() + "]";	
		}			
		
	}
	
	/**
	 * Returns the class of the Agent
	 * @return
	 */
	public Class<? extends Agent> getElementClass(){
		return agentClass;
	}
	
	/**
	 * @param postionNo the postionNo to set
	 */
	public void setPostionNo(Integer postionNo) {
		this.postionNo = postionNo;
	}
	/**
	 * @return the postionNo
	 */
	public Integer getPostionNo() {
		return postionNo;
	}
	/**
	 * @return the startAsName
	 */
	public String getStartAsName() {
		return startAsName;
	}
	/**
	 * @param startAsName the startAsName to set
	 */
	public void setStartAsName(String startAsName) {
		this.startAsName = startAsName;
	}

	/**
	 * @return the agentClassReference
	 */
	public String getAgentClassReference() {
		return agentClassReference;
	}
	/**
	 * @param agentClassReference the agentClassReference to set
	 */
	public void setAgentClassReference(String agentClassReference) {
		this.agentClassReference = agentClassReference;
	}
	
	/**
	 * @return the mobileAgent
	 */
	public boolean isMobileAgent() {
		return mobileAgent;
	}
	/**
	 * @param mobileAgent the mobileAgent to set
	 */
	public void setMobileAgent(boolean mobileAgent) {
		this.mobileAgent = mobileAgent;
	}
	
	
	
}
