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
package agentgui.simulationService.load.threading;

import java.io.Serializable;


/**
 * The Class ThreadTime.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadTime implements Serializable{

	private static final long serialVersionUID = 7920683110304631892L;
	
	public String threadName;
	public long cpuTime;
    public long userTime;
    
    /**
     * Instantiates a new thread time.
     *
     * @param cpuTime the CPU time
     * @param userTime the user time
     */
    public ThreadTime(String threadName, long cpuTime, long userTime) {
    	this.setThreadName(threadName);
    	this.setCpuTime(cpuTime);
    	this.setUserTime(userTime);
    }
    
    /**
     * Gets the thread name.
     * @return the thread name
     */
    public String getThreadName() {
		return this.threadName;
	}
	/**
	 * Sets the thread name.
	 * @param threadName the new thread name
	 */
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	/**
     * Gets the cpu time.
     * @return the cpu time
     */
    public long getCpuTime() {
		return cpuTime;
	}
	/**
	 * Sets the cpu time.
	 * @param cpuTime the new cpu time
	 */
	public void setCpuTime(long cpuTime) {
		this.cpuTime = cpuTime;
	}
	
	/**
	 * Gets the user time.
	 * @return the user time
	 */
	public long getUserTime() {
		return userTime;
	}
	/**
	 * Sets the user time.
	 * @param userTime the new user time
	 */
	public void setUserTime(long userTime) {
		this.userTime = userTime;
	}

}
