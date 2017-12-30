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
package org.agentgui.gui;

/**
 * The Interface AwbBenchmarkMonitor defines the required methods for a visual benchmark monitor.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface AwbBenchmarkMonitor {

	
	/**
	 * Enables/disable the skip button.
	 * @param isEnabled the new enabled skip
	 */
	public void setEnableSkipButton(boolean isEnabled);
	/**
	 * Checks if is skip.
	 * @return true, if is skip
	 */
	public boolean isSkip();

	
	/**
	 * Enables/disable the skip always button.
	 * @param isEnabled the new enabled skip always
	 */
	public void setEnableSkipAlwaysButton(boolean isEnabled);
	/**
	 * Checks if is skip always.
	 * @return true, if is skip always
	 */
	public boolean isSkipAlways();

	
	/**
	 * Sets the Benchmark Monitor visible or not.
	 * @param isVisible the new visible
	 */
	public void setVisible(boolean isVisible);
	
	/**
	 * Has to dispose the benchmark monitor
	 */
	public void dispose();
	
	/**
	 * Has to set the progress minimum.
	 * @param pMin the new progress minimum
	 */
	public void setProgressMinimum(int pMin);

	/**
	 * Has to set the progress maximum.
	 * @param pMax the new progress maximum
	 */
	public void setProgressMaximum(int pMax);

	/**
	 * Has to set the progress value.
	 * @param newValue the new progress value
	 */
	public void setProgressValue(int newValue);
	
	
	/**
	 * Sets the new benchmark value / result.
	 * @param newBenchkarkResult the new benchmark value
	 */
	public void setBenchmarkValue(float newBenchkarkResult);

	
}
