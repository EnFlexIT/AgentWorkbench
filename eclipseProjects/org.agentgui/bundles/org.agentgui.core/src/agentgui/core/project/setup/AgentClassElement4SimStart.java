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

import jade.core.Agent;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.codec.binary.Base64;

import agentgui.core.classLoadService.ClassLoadServiceUtility;
import de.enflexit.common.classSelection.ClassElement2Display;


/**
 * Provides a container instance in order to configure an agents 
 * start configuration. This class is used in the simulation setup
 * of <b>Agent.GUI</b>
 *   
 * @see agentgui.core.project.setup.SimulationSetup
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen 
 */
public class AgentClassElement4SimStart {
	
	@XmlTransient private DecimalFormat df = new DecimalFormat("00000");

	private Class<? extends Agent> agentClass;
	
	@XmlElement(name="postionNo")
	private Integer postionNo = 0;
	
	@XmlElement(name="listMembership")
	private String listMembership = null;
	
	@XmlElement(name="agentClassReference")
	private String agentClassReference = null;
	
	@XmlElement(name="startAsName")
	private String startAsName = "";
	
	@XmlElementWrapper(name = "startArguments")
	@XmlElement(name="argument")
	private String[] startArguments = null;
	
	
	/**
	 * Constructor without arguments (This is first of all
	 * for the JAXB-context and should not be used by any
	 * other context).
	 *
	 * @deprecated because of the JAXB-context
	 */
	@Deprecated
	public AgentClassElement4SimStart() {
	}
	
	/**
	 * Constructor of this class by using the Class which extends Agent.
	 *
	 * @param agentClass the agent class
	 * @param listMembership the list membership
	 */
	public AgentClassElement4SimStart(Class<? extends Agent> agentClass, String listMembership){
		this.agentClassReference = agentClass.getName();
		this.listMembership = listMembership;
		this.setDefaultAgentName();
	}
	
	/**
	 * Constructor of this class by using an AgentClassElement-Object.
	 *
	 * @param classElement2Display a {@link ClassElement2Display}
	 * @param listMembership the list membership
	 */
	public AgentClassElement4SimStart(ClassElement2Display classElement2Display, String listMembership){
		this.agentClassReference = classElement2Display.getClassElement();
		this.listMembership = listMembership;
		this.setDefaultAgentName();
	}
	
	/**
	 * Sets a default name for the executed Agent in the Simulation-Experiment.
	 */
	private void setDefaultAgentName() {
		
			// -----------------------------------------------------
			// --- Find suggestion for the agent name -------------
			String startAs = this.agentClassReference;
			startAs = startAs.substring(startAs.lastIndexOf(".")+1);
			// -----------------------------------------------------
			// --- Filter for upper letters ------------------------
			String regExp = "[A-Z]";	
			String startAsNew = ""; 
			for (int i=0; i<startAs.length(); i++) {
				String sngChar = "" + startAs.charAt(i);
				if (sngChar.matches(regExp)==true) {
					startAsNew = startAsNew + sngChar;	
					// --- Maybe take also the next letter ----------
					if ((i+1)<=(startAs.length()-1)) {
						String sngCharN = "" + startAs.charAt(i+1);
						if (sngCharN.matches(regExp)==false) {
							startAsNew = startAsNew + sngCharN;	
						}
					}	
					// ----------------------------------------------
				}						
		    }
			if (startAsNew!="" && startAsNew.length()>=4) {
				startAs = startAsNew;
			}
			// -----------------------------------------------------
			// --- set agent name ----------------------------------	
			this.setStartAsName(startAs);
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		if (this.getElementClass()==null) {
			return "NotFound: " + df.format(this.postionNo) + ": " + this.startAsName + " [" + this.agentClassReference + "]";
		} else {
			return df.format(this.postionNo) + ": " + this.startAsName + " [" + this.agentClassReference  + "]";	
		}			
	}
	
	/**
	 * Returns the class of the current Agent.
	 * @return the Class<? extends Agent> instance of the agent
	 */
	public Class<? extends Agent> getElementClass(){
		if (agentClass==null) {
			try {
				agentClass = ClassLoadServiceUtility.getAgentClass(agentClassReference);
			} catch (NoClassDefFoundError exeption) {
				System.err.println("Agent class definition not found for '" + agentClassReference + "'!");
			} catch (ClassNotFoundException exeption) {
				System.err.println("Could not find agent class '" + agentClassReference + "'!");
			}
		}
		return agentClass;
	}
	
	/**
	 * Returns the position of the currently configured agent in the current list.
	 * @return Integer position number
	 */
	@XmlTransient
	public Integer getPostionNo() {
		return postionNo;
	}
	/**
	 * Sets the position number of the current agent.
	 * @param postionNo the postionNo to set
	 */
	public void setPostionNo(Integer postionNo) {
		this.postionNo = postionNo;
	}

	/**
	 * Returns the listType this entry belongs to.
	 * @return the listType
	 */
	@XmlTransient
	public String getListMembership() {
		return listMembership;
	}
	/**
	 * Sets the type of the list this entry belongs to.
	 * @param listMembership the listType to set
	 */
	public void setListMembership(String listMembership) {
		this.listMembership = listMembership;
	}

	/**
	 * Returns the local name of the agent like ((AID)agentAID).getLocalname() 
	 * @return the startAsName
	 */
	@XmlTransient
	public String getStartAsName() {
		return startAsName;
	}
	/**
	 * Here the local name for the agent can be set similar to ((AID)agentAID).getLocalname() 
	 * @param startAsName the startAsName to set
	 */
	public void setStartAsName(String startAsName) {
		this.startAsName = startAsName;
	}

	/**
	 * This method returns the class reference of the agent.
	 *
	 * @return String the agentClassReference
	 */
	@XmlTransient
	public String getAgentClassReference() {
		return agentClassReference;
	}
	
	/**
	 * Here the class reference of the agent can be set.
	 *
	 * @param agentClassReference the agentClassReference to set
	 */
	public void setAgentClassReference(String agentClassReference) {
		this.agentClassReference = agentClassReference;
	}
	
	/**
	 * This method will return the serialized start arguments of the current agent.
	 * Internally these arguments will be kept as Base64 encoded Strings in order to 
	 * store this configuration also in the SimulationSetup
	 *   
	 * @return the startInstances
	 * @see agentgui.core.project.setup.SimulationSetup
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
	 * This method can be used in order to save the start arguments of the current agent.
	 * Internally these arguments will be kept as Base64 encoded Strings in order to 
	 * store this configuration also in the SimulationSetup
	 *  
	 * @param startArguments the startInstances to set for the agent start up
	 * @see agentgui.core.project.setup.SimulationSetup
	 */
	public void setStartArguments(String[] startArguments) {
		
		if (startArguments.length==0) {
			this.startArguments = null;
			return;
		}
		String[] startArgumentsEncoded = new String[startArguments.length];
		String encodedArgument = null;
		try {
			for (int i = 0; i < startArguments.length; i++) {
				encodedArgument = new String(Base64.encodeBase64(startArguments[i].getBytes("UTF8")));
				startArgumentsEncoded[i] = encodedArgument;
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.startArguments = startArgumentsEncoded;
	}
	
	/**
	 * This method returns the agent's start arguments in Base64 encoded form.
	 * @return The Base64 encoded start arguments
	 */
	@XmlTransient
	public String[] getStartArgumentsBase64(){
		return this.startArguments;
	}
	
	
}
