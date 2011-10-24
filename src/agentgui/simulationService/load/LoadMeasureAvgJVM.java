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

import java.util.ArrayList;
import java.util.Hashtable;

public class LoadMeasureAvgJVM {

	private Integer useAVGCounter = 0;//maximum length of List
	private ArrayList<LoadMeasureJVM> measureList = new ArrayList<LoadMeasureJVM>();
	
	private String jvmPID = null;
	
	private long jvmMemoFree = 0;			// KB 
	private long jvmMemoTotal = 0;			// KB
	private long jvmMemoMax = 0;			// KB
   
    private long jvmHeapUsed = 0;  			// KB
    private long jvmHeapCommited = 0;  		// KB            
    private long jvmHeapMax = 0;  			// KB
    private long jvmHeapInit = 0;  			// KB

    private int jvmThreadCount = 0;
    private Hashtable<String, Long> jvmThreadTimes = null;
    
    
	public LoadMeasureAvgJVM(Integer avgCounter) {
		useAVGCounter = avgCounter;                     //maximum length of List
	}

	public void put(LoadMeasureJVM currentLoadMeasure) {

		this.jvmPID = currentLoadMeasure.getJvmPID();
		
		if (measureList.size() >= useAVGCounter) {
			measureList.remove(0);
		}
		measureList.add(currentLoadMeasure.clone()); //add new object in the list
		this.calculateLoadAverage();
	}
	
	/**
	 * 
	 */
	private void calculateLoadAverage() {
      
		int size = measureList.size();
		
		long tmpJvmMemoFree = 0;	 
		long tmpJvmMemoTotal = 0;	
		long tmpJvmMemoMax = 0;	
	   
	    long tmpJvmHeapUsed = 0;  	
	    long tmpJvmHeapCommited = 0;  		            
	    long tmpJvmHeapMax = 0;  			
	    long tmpJvmHeapInit = 0; 
	    
	    int tmpJvmThreadCount = 0;
	    
		// --- calculating JVM memory average values ----------------
		for (int i = 0; i < size; i++) {

			tmpJvmMemoFree += measureList.get(i).getJvmMemoFree();
			tmpJvmMemoTotal += measureList.get(i).getJvmMemoTotal();
			tmpJvmMemoMax += measureList.get(i).getJvmMemoMax();

			tmpJvmHeapUsed += measureList.get(i).getJvmHeapUsed();
			tmpJvmHeapCommited += measureList.get(i).getJvmHeapCommited();
			tmpJvmHeapMax += measureList.get(i).getJvmHeapMax();
			tmpJvmHeapInit += measureList.get(i).getJvmHeapInit();
			
			tmpJvmThreadCount += measureList.get(i).getJvmThreadCount();
			
		}	
	    
		jvmMemoFree = tmpJvmMemoFree / size;
		jvmMemoTotal = tmpJvmMemoTotal / size;
		jvmMemoMax = tmpJvmMemoMax / size;

		jvmHeapUsed = tmpJvmHeapUsed / size;
		jvmHeapCommited = tmpJvmHeapCommited / size;
		jvmHeapMax = tmpJvmHeapMax / size;
		jvmHeapInit = tmpJvmHeapInit / size;

		jvmThreadCount = tmpJvmThreadCount / size;
	}

	/**
	 * @param jvmPID the jvmPID to set
	 */
	public void setJvmPID(String jvmPID) {
		this.jvmPID = jvmPID;
	}
	/**
	 * @return the jvmPID
	 */
	public String getJvmPID() {
		return jvmPID;
	}

	/**
	 * @return the jvmMemoFree
	 */
	public long getJvmMemoFree() {
		return jvmMemoFree;
	}
	/**
	 * @param jvmMemoFree the jvmMemoFree to set
	 */
	public void setJvmMemoFree(long jvmMemoFree) {
		this.jvmMemoFree = jvmMemoFree;
	}

	/**
	 * @return the jvmMemoTotal
	 */
	public long getJvmMemoTotal() {
		return jvmMemoTotal;
	}
	/**
	 * @param jvmMemoTotal the jvmMemoTotal to set
	 */
	public void setJvmMemoTotal(long jvmMemoTotal) {
		this.jvmMemoTotal = jvmMemoTotal;
	}

	/**
	 * @return the jvmMemoMax
	 */
	public long getJvmMemoMax() {
		return jvmMemoMax;
	}
	/**
	 * @param jvmMemoMax the jvmMemoMax to set
	 */
	public void setJvmMemoMax(long jvmMemoMax) {
		this.jvmMemoMax = jvmMemoMax;
	}

	/**
	 * @return the jvmHeapUsed
	 */
	public long getJvmHeapUsed() {
		return jvmHeapUsed;
	}
	/**
	 * @param jvmHeapUsed the jvmHeapUsed to set
	 */
	public void setJvmHeapUsed(long jvmHeapUsed) {
		this.jvmHeapUsed = jvmHeapUsed;
	}

	/**
	 * @return the jvmHeapCommited
	 */
	public long getJvmHeapCommited() {
		return jvmHeapCommited;
	}
	/**
	 * @param jvmHeapCommited the jvmHeapCommited to set
	 */
	public void setJvmHeapCommited(long jvmHeapCommited) {
		this.jvmHeapCommited = jvmHeapCommited;
	}

	/**
	 * @return the jvmHeapMax
	 */
	public long getJvmHeapMax() {
		return jvmHeapMax;
	}
	/**
	 * @param jvmHeapMax the jvmHeapMax to set
	 */
	public void setJvmHeapMax(long jvmHeapMax) {
		this.jvmHeapMax = jvmHeapMax;
	}

	/**
	 * @return the jvmHeapInit
	 */
	public long getJvmHeapInit() {
		return jvmHeapInit;
	}
	/**
	 * @param jvmHeapInit the jvmHeapInit to set
	 */
	public void setJvmHeapInit(long jvmHeapInit) {
		this.jvmHeapInit = jvmHeapInit;
	}
	

	/**
	 * @return the jvmThreadCount
	 */
	public int getJvmThreadCount() {
		return jvmThreadCount;
	}
	/**
	 * @param jvmThreadCount the jvmThreadCount to set
	 */
	public void setJvmThreadCount(int jvmThreadCount) {
		this.jvmThreadCount = jvmThreadCount;
	}

	/**
	 * @return the jvmThreadTimes
	 */
	public Hashtable<String, Long> getJvmThreadTimes() {
		return jvmThreadTimes;
	}
	/**
	 * @param jvmThreadTimes the jvmThreadTimes to set
	 */
	public void setJvmThreadTimes(Hashtable<String, Long> jvmThreadTimes) {
		this.jvmThreadTimes = jvmThreadTimes;
	}


}
