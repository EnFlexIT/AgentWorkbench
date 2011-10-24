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

import java.io.Serializable;

public class LoadThresholdLevels implements Serializable {

	private static final long serialVersionUID = 1795189499689460795L;
	
	private Integer thCpuL = 5;
	private Integer thCpuH = 80;
	
	private Integer thMemoL = 5;
	private Integer thMemoH = 80;
	
	private Integer thNoThreadsL = 50;
	private Integer thNoThreadsH = 1500;
	
		
	/**
	 * @return the thCpuL
	 */
	public Integer getThCpuL() {
		return thCpuL;
	}
	/**
	 * @param thCpuL the thCpuL to set
	 */
	public void setThCpuL(Integer thCpuL) {
		this.thCpuL = thCpuL;
	}
	/**
	 * @return the thCpuH
	 */
	public Integer getThCpuH() {
		return thCpuH;
	}
	/**
	 * @param thCpuH the thCpuH to set
	 */
	public void setThCpuH(Integer thCpuH) {
		this.thCpuH = thCpuH;
	}
	/**
	 * @return the thMemL
	 */
	public Integer getThMemoL() {
		return thMemoL;
	}
	/**
	 * @param thMemL the thMemL to set
	 */
	public void setThMemoL(Integer thMemoL) {
		this.thMemoL = thMemoL;
	}
	/**
	 * @return the thMemH
	 */
	public Integer getThMemoH() {
		return thMemoH;
	}
	/**
	 * @param thMemH the thMemH to set
	 */
	public void setThMemoH(Integer thMemoH) {
		this.thMemoH = thMemoH;
	}
	/**
	 * @param thNoThreadsL the thNoThreadsL to set
	 */
	public void setThNoThreadsL(Integer thNoThreadsL) {
		this.thNoThreadsL = thNoThreadsL;
	}
	/**
	 * @return the thNoThreadsL
	 */
	public Integer getThNoThreadsL() {
		return thNoThreadsL;
	}
	/**
	 * @param thNoThreadsH the thNoThreadsH to set
	 */
	public void setThNoThreadsH(Integer thNoThreadsH) {
		this.thNoThreadsH = thNoThreadsH;
	}
	/**
	 * @return the thNoThreadsH
	 */
	public Integer getThNoThreadsH() {
		return thNoThreadsH;
	}
	
}
