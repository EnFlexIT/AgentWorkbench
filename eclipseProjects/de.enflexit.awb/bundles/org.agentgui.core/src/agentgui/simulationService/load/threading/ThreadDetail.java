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
 * The Class ThreadDetail.
 * Holds information about a single thread.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadDetail implements Serializable {

	private static final long serialVersionUID = 7920683110304631892L;
	
	public final static String UNKNOWN_THREAD_CLASSNAME = "system.class.-Regular-Thread-";
	public final static String UNKNOWN_AGENT_CLASSNAME = "agent.class.+Unknown-Agent-Class+";

	private String threadName;
	private String className;
	private boolean isAgent;
	
	private long systemTime;
	private long userTime;
	
	private double predictiveMetric;
	private double realMetric;
    
	
	/**
	 * Instantiates a new thread detail.
	 */
	public ThreadDetail() {
		this.setThreadName("");
    	this.setSystemTime(0);
    	this.setUserTime(0);
    	// --- default ---
    	this.setIsAgent(false);
    	this.setClassName(ThreadDetail.UNKNOWN_THREAD_CLASSNAME);
	}
    /**
     * Instantiates a new thread detail.
     *
     * @param threadName the thread name
     * @param systemTime the system time
     * @param userTime the user time
     */
    public ThreadDetail(String threadName, long systemTime, long userTime) {
    	this.setThreadName(threadName);
    	this.setSystemTime(systemTime);
    	this.setUserTime(userTime);
    	// --- default ---
    	this.setIsAgent(false);
    	this.setClassName(ThreadDetail.UNKNOWN_THREAD_CLASSNAME);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return getThreadName();
    }
    /**
     * Gets the thread name.
     * @return the thread name
     */
    public String getThreadName() {
		return threadName;
	}
	/**
	 * Sets the thread name.
	 * @param threadName the new thread name
	 */
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	/**
	 * Gets the class name.
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Sets the class name.
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Checks if is agent.
	 * @return true, if is agent
	 */
	public boolean isAgent() {
		return isAgent;
	}
	
	/**
	 * Sets the checks if is agent.
	 * @param isAgent the new checks if is agent
	 */
	public void setIsAgent(boolean isAgent) {
		this.isAgent = isAgent;
		if (this.isAgent==true && this.getClassName().equals(UNKNOWN_THREAD_CLASSNAME)) {
			this.setClassName(UNKNOWN_AGENT_CLASSNAME);
		}
	}
	
	/**
     * Gets the system cpu time.
     * @return the cpu time
     */
    public long getSystemTime() {
		return systemTime;
	}
    
	/**
	 * Sets the system cpu time.
	 * @param systemTime the new cpu time
	 */
	public void setSystemTime(long systemTime) {
		this.systemTime = systemTime;
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

	/**
	 * Gets the predictive metric.
	 * @return the predictive metric
	 */
	public double getPredictiveMetric() {
		return predictiveMetric;
	}

	/**
	 * Gets the real metric.
	 * @return the real metric
	 */
	public double getRealMetric() {
		return realMetric;
	}

	/**
	 * Sets the predictive metric.
	 * @param predictiveMetric the predictiveMetric to set
	 */
	public void setPredictiveMetric(double predictiveMetric) {
		this.predictiveMetric = predictiveMetric;
	}

	/**
	 * Sets the real metric.
	 * @param realMetric the realMetric to set
	 */
	public void setRealMetric(double realMetric) {
		this.realMetric = realMetric;
	}
}
