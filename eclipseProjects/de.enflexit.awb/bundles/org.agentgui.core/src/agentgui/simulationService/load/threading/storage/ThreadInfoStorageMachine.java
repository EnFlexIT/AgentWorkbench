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
package agentgui.simulationService.load.threading.storage;

/**
 * Storage class for storing Thread-Load-Information of (physical)machine
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageMachine extends ThreadInfoStorageXYSeries{
	
	/** The MFLOPS of that (physical) machine. */
	private double mflops;
	
	/** The actual average load cpu. */
	private double actualAverageLoadCPU;
	
	/**
	 * Instantiates a new thread info storage machine.
	 * @param name the name
	 */
	public ThreadInfoStorageMachine(String name) {
		super(name);
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

	/**
	 * @return the actualAverageLoadCPU
	 */
	public double getActualAverageLoadCPU() {
		return actualAverageLoadCPU;
	}

	/**
	 * @param actualAverageLoadCPU the actualAverageLoadCPU to set
	 */
	public void setActualAverageLoadCPU(double actualAverageLoadCPU) {
		this.actualAverageLoadCPU = actualAverageLoadCPU;
	}

}
