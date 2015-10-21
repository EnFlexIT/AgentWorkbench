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

import java.awt.Color;

import org.jfree.data.xy.XYSeries;
/**
 * Storage class for storing Thread-Load-Information of (physical)machine
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageMachine extends ThreadInfoStorageSeries{
	/**
	* The available series keys as constants
	*/
	public final String TOTAL_CPU_USER_TIME   = "TOTAL_CPU_USER_TIME";
	public final String DELTA_CPU_USER_TIME   = "DELTA_CPU_USER_TIME";
	public final String TOTAL_CPU_SYSTEM_TIME = "TOTAL_CPU_SYSTEM_TIME";
	public final String DELTA_CPU_SYSTEM_TIME = "DELTA_CPU_SYSTEM_TIME";
	public final String LOAD_CPU 			  = "LOAD_CPU";
	
	/** The MFLOPS of that (physical) machine. */
	private double mflops;
	
	/**
	 * Instantiates a new thread info storage machine.
	 * @param name the name
	 */
	public ThreadInfoStorageMachine(String name) {
		super(name);
		
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		getXYSeriesMap().put(TOTAL_CPU_USER_TIME, new XYSeries(TOTAL_CPU_USER_TIME+DELIMITER+name));
		getXYSeriesMap().put(DELTA_CPU_USER_TIME, new XYSeries(DELTA_CPU_USER_TIME+DELIMITER+name));
		getXYSeriesMap().put(TOTAL_CPU_SYSTEM_TIME, new XYSeries(TOTAL_CPU_SYSTEM_TIME+DELIMITER+name));
		getXYSeriesMap().put(DELTA_CPU_SYSTEM_TIME, new XYSeries(DELTA_CPU_SYSTEM_TIME+DELIMITER+name));
		getXYSeriesMap().put(LOAD_CPU, new XYSeries(LOAD_CPU+DELIMITER+name));	
		
		// --- default: total series RED, delta series BLACK , load BLUE---
		getXySeriesLineColorMap().put(TOTAL_CPU_USER_TIME+DELIMITER+name, Color.RED);
		getXySeriesLineColorMap().put(DELTA_CPU_USER_TIME+DELIMITER+name, Color.BLACK);
		getXySeriesLineColorMap().put(TOTAL_CPU_SYSTEM_TIME+DELIMITER+name, Color.RED);
		getXySeriesLineColorMap().put(DELTA_CPU_SYSTEM_TIME+DELIMITER+name, Color.BLACK);
		getXySeriesLineColorMap().put(LOAD_CPU+DELIMITER+name, Color.BLUE);
	}

	/**
	 * @return the MFLOPS
	 */
	public double getMflops() {
		return mflops;
	}
	/**
	 * @param mflops the MFLOPS to set
	 */
	public void setMflops(double mflops) {
		this.mflops = mflops;
	}

}
