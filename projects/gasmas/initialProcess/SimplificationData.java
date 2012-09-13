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

import java.util.HashSet;

/**
 * The Class SimplificationData is used for the third and fourth step as a message class of the initial process
 * 
 * @author Benjamin Schwartz - University of Duisburg - Essen
 */
public class SimplificationData extends GenericMesssageData {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 827774764094560195L;

	/** String, which holds the initiator of the actual asking round. */
	private String initiator = "";

	/** String, which holds the initiator of the initial request. */
	private String urInitiator = "";
	
	/** HashSet, which holds the way, how the message went. */
	private HashSet<String> way = new HashSet<String>();
	
	/** Shows, if the message is an answer. */
	private boolean answer = false;
	
	/** The step. */
	private int step;

	/**
	 * Gets the step.
	 *
	 * @return the step
	 */
	public int getStep() {
		return step;
	}

	/**
	 * Checks if is answer.
	 *
	 * @return true, if is answer
	 */
	public boolean isAnswer() {
		return answer;
	}

	/**
	 * Sets the answer.
	 *
	 * @param answer the new answer
	 */
	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	/**
	 * Sets the ur initiator.
	 *
	 * @param urInitiator the new ur initiator
	 */
	public void setUrInitiator(String urInitiator) {
		this.urInitiator = urInitiator;
	}

	/**
	 * Gets the ur initiator.
	 *
	 * @return the ur initiator
	 */
	public String getUrInitiator() {
		return urInitiator;
	}

	/**
	 * Sets the initiator.
	 *
	 * @param initiator the new initiator
	 */
	public void setInitiator(String initiator) {
		this.initiator = initiator;

	}

	/**
	 * Gets the initiator.
	 *
	 * @return the initiator
	 */
	public String getInitiator() {
		return initiator;
	}

	/**
	 * Gets the way.
	 *
	 * @return the way
	 */
	public HashSet<String> getWay() {
		return way;
	}
	
	/**
	 * Adds the station.
	 *
	 * @param station the station
	 */
	public void addStation(String station) {
		this.way.add(station);
	}
	
	/**
	 * Gets a copy of these data.
	 *
	 * @return the a copy
	 */
	public SimplificationData getCopy() {
		SimplificationData temp = new SimplificationData(this.initiator, 2);
		temp.setUrInitiator(this.urInitiator);
		temp.getWay().clear();
		temp.getWay().addAll(this.way);
		temp.setAnswer(this.isAnswer());
		return temp;
	}

	/**
	 * Instantiates a new simplification data.
	 *
	 * @param initiator the initiator
	 * @param step the step
	 */
	public SimplificationData(String initiator, int step) {
		this.initiator = initiator;
		this.urInitiator = initiator;
		this.way.add(initiator);
		this.step = step;
	}

	/**
	 * Instantiates a new simplification data.
	 *
	 * @param initiator the initiator
	 * @param b the b
	 * @param step the step
	 */
	public SimplificationData(String initiator, boolean b, int step) {
		this.initiator = initiator;
		if (b) {
			this.urInitiator = initiator;
		}
		this.step = step;
	}

	/**
	 * Instantiates a new simplification data.
	 *
	 * @param initiator the initiator
	 * @param way the way
	 * @param b the b
	 * @param urInitiator the ur initiator
	 * @param step the step
	 */
	public SimplificationData(String initiator, HashSet<String> way, boolean b, String urInitiator, int step) {
		this.initiator = initiator;
		this.way = way;
		this.answer = b;
		this.urInitiator = urInitiator;
		this.step = step;
	}
	
}
