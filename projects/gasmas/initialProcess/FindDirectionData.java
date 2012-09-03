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
 * The Class FindDirectionData is used for the first and second step as a message class of the initial process
 * 
 * @author Benjamin Schwartz - University of Duisburg - Essen
 */
public class FindDirectionData extends GenericMesssageData {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3438711840630347490L;

	/** String, which holds the way, how the message went. */
	protected String way = "";
	
	/** String, which holds the initiator of the actual asking round. */
	protected String flow;

	/**
	 * Sets the flow.
	 *
	 * @param reason the new flow
	 */
	public void setFlow(String reason) {
		this.flow = reason;
	}

	/**
	 * Gets the flow.
	 *
	 * @return the flow
	 */
	public String getFlow() {
		return flow;
	}

	/**
	 * Instantiates a new find direction data.
	 *
	 * @param way the way
	 * @param reason the reason
	 */
	public FindDirectionData(String way, String reason) {
		this.way = way;
		this.flow = reason;
	}

	/**
	 * Instantiates a new find direction data.
	 *
	 * @param reason the reason
	 */
	public FindDirectionData(String reason) {
		this.flow = reason;
	}

	/**
	 * Gets the way.
	 *
	 * @return the way
	 */
	public String getWay() {
		return way;
	}

	/**
	 * Sets the way.
	 *
	 * @param way the new way
	 */
	public void setWay(String way) {
		this.way = way;
	}

}
