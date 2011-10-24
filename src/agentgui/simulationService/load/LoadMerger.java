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
package agentgui.simulationService.load;

import java.util.Vector;

import agentgui.simulationService.ontology.PlatformLoad;


public class LoadMerger {

	private String identifier = null;
	private Vector<String> containerList = new Vector<String>();
	private Vector<String> jvmList = new Vector<String>();
	private Float benchmarkValue = null;	
	private PlatformLoad machineLoad = null;
	private Integer numberOfAgents = 0;
	
	/**
	 * Default constructor
	 */
	public LoadMerger(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Cumlate the informations of the Container, so that 
	 * the informations are available per JVM
	 * @param container
	 * @param benchValue
	 * @param pLoad
	 * @param nAgents
	 */
	public void merge(String containerName, String jvm ,Float benchValue, PlatformLoad pLoad, Integer nAgents) {
		
		if (containerList.contains(containerName)==false) {
			containerList.addElement(containerName);	
		}
		if (jvmList.contains(jvm)==false) {
			jvmList.addElement(jvm);	
		}		
		if (this.benchmarkValue==null) {
			this.benchmarkValue = benchValue;
		}
		if (this.machineLoad==null) {
			this.machineLoad = pLoad;
		}
		if (nAgents!=null) {
			numberOfAgents += nAgents;	
		}
		
	}

	/**
	 * @return the identifier of this object
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * @return the containerList
	 */
	public Vector<String> getJvmList() {
		return jvmList;
	}
	/**
	 * @return the containerList
	 */
	public Vector<String> getContainerList() {
		return containerList;
	}
	/**
	 * @return the benchmarkValue
	 */
	public Float getBenchmarkValue() {
		return benchmarkValue;
	}
	/**
	 * @return the machineLoad
	 */
	public PlatformLoad getMachineLoad() {
		return machineLoad;
	}
	/**
	 * @return the numberOfAgents
	 */
	public Integer getNumberOfAgents() {
		return numberOfAgents;
	}
	
}
