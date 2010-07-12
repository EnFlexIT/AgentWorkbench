package mas.agents;

import java.text.DecimalFormat;

import jade.core.Agent;

public class AgentClassElement4SimStart {
	
	private Class<? extends Agent> agentClass = null;
	private String startAsName = "";
	
	private Integer postionNo = 0;
	private DecimalFormat df = new DecimalFormat("00000");
	
	private boolean mobileAgent = false;
	
	/**
	 * Constructor of this class by using the Class which extends Agent 
	 * @param agentClass
	 */
	public AgentClassElement4SimStart(Class<? extends Agent> agentClass){
		this.agentClass=agentClass;
		this.setDefaultAgentName();
	}
	/**
	 * Constructor of this class by using an AgentClassElement-Object
	 * @param agentClass
	 */
	public AgentClassElement4SimStart(AgentClassElement agentClass){
		this.agentClass=agentClass.getElementClass();
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
	@Override
	public String toString(){
		return df.format(postionNo) + ": " + startAsName + " [" + agentClass.getName() + "]";
	}
	
	/**
	 * Returns teh class of the Agent
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
