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
package gasmas.initialProcess;

/**
 * The Class StatusData, which is used to send information about the status in the initial process.
 * 
 * @author Benjamin Schwartz - University of Duisburg - Essen
 */
public class StatusData extends GenericMesssageData {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 827774764094560195L;

	/** Shows the actual step. */
	private int phase = 0;

	/** Shows the name of a cluster, in which the receiver is. */
	private String clusterName = "";

	/** Shows the reason of the message. */
	private String reason = "";
	
	/**
	 * Gets the phase.
	 *
	 * @return the phase
	 */
	public int getPhase() {
		return phase;
	}

	/**
	 * Gets the cluster name.
	 *
	 * @return the cluster name
	 */
	public String getClusterName() {
		return clusterName;
	}
	
	/**
	 * Gets the reason.
	 *
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Instantiates a new status data.
	 *
	 * @param step the step
	 */
	public StatusData(int step) {
		this.phase = step;
	}

	/**
	 * Instantiates a new status data.
	 *
	 * @param clusterName the cluster name
	 */
	public StatusData(String clusterName) {
		this.phase = -1;
		this.clusterName = clusterName;
	}
	
	/**
	 * Instantiates a new status data.
	 *
	 * @param step the step
	 * @param reason the reason
	 */
	public StatusData(int step, String reason) {
		this.phase = step;
		this.reason = reason;
	}

}
