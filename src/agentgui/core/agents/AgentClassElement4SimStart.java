package agentgui.core.agents;

import jade.core.Agent;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.codec.binary.Base64;

public class AgentClassElement4SimStart {
	
	@XmlTransient private DecimalFormat df = new DecimalFormat("00000");

	private Class<? extends Agent> agentClass = null;
	
	@XmlElement(name="postionNo")
	private Integer postionNo = 0;
	@XmlElement(name="agentClassReference")
	private String agentClassReference = null;
	@XmlElement(name="startAsName")
	private String startAsName = "";
	@XmlElement(name="mobileAgent")
	private boolean mobileAgent = true;
	
	@XmlElementWrapper(name = "startArguments")
	@XmlElement(name="argument")
	private String[] startArguments = null;
	
	
	
	
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
	 * @return the postionNo
	 */
	@XmlTransient
	public Integer getPostionNo() {
		return postionNo;
	}
	/**
	 * @param postionNo the postionNo to set
	 */
	public void setPostionNo(Integer postionNo) {
		this.postionNo = postionNo;
	}
	/**
	 * @return the startAsName
	 */
	@XmlTransient
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
	@XmlTransient
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
	@XmlTransient
	public boolean isMobileAgent() {
		return mobileAgent;
	}
	/**
	 * @param mobileAgent the mobileAgent to set
	 */
	public void setMobileAgent(boolean mobileAgent) {
		this.mobileAgent = mobileAgent;
	}
	
	/**
	 * @return the startInstances
	 */
	@XmlTransient
	public String[] getStartArguments() {
		
		if (this.startArguments==null) return null;
		
		String[] startArgumentsDecoded = new String[this.startArguments.length];
		String decodedArgument = null;
		try {
			for (int i = 0; i < startArguments.length; i++) {
				decodedArgument = new String(Base64.decodeBase64(startArguments[i].getBytes()), "UTF8");
				startArgumentsDecoded[i] = decodedArgument;
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return startArgumentsDecoded;
	}
	/**
	 * @param startInstances the startInstances to set
	 */
	public void setStartArguments(String[] startArgs) {
		
		if (startArgs.length==0) {
			this.startArguments = null;
			return;
		}
		String[] startArgumentsEncoded = new String[startArgs.length];
		String encodedArgument = null;
		try {
			for (int i = 0; i < startArgs.length; i++) {
				encodedArgument = new String(Base64.encodeBase64(startArgs[i].getBytes("UTF8")));
				startArgumentsEncoded[i] = encodedArgument;
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.startArguments = startArgumentsEncoded;
	}
	
	
}
