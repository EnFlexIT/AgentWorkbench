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
package agentgui.simulationService.time;

import javax.swing.JPanel;

import agentgui.core.project.Project;
import agentgui.core.sim.setup.SimulationSetup;

/**
 * The Class TimeModelConfiguration has to be extended in order to
 * provide a specific JPanle for the configuration of a TimeModel.
 * 
 * @see TimeModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public abstract class JPanel4TimeModelConfiguration extends JPanel {

	private static final long serialVersionUID = 4966720402773236025L;

	private Project currProject = null;
	
	/**
	 * Instantiates a new display j panel4 configuration.
	 * @param project the project
	 */
	public JPanel4TimeModelConfiguration(Project project) {
		this.currProject = project;
	}
	
	/**
	 * Save the current TimeModel.
	 * @param timeModel the TimeModel
	 */
	protected void saveTimeModelInSimulationSetup(TimeModel timeModel) {
		SimulationSetup simSetup = this.currProject.getSimulationSetups().getCurrSimSetup();
		if (simSetup!=null) {
			simSetup.setTimeModelSettings(timeModel.getTimeModelSetting());	
		}
	}
	
	/**
	 * Sets the TimeModel.
	 * @param timeModel the new TimeModel
	 */
	public abstract void setTimeModel(TimeModel timeModel);
	/**
	 * Returns the TimeModel.
	 * @return the TimeModell
	 */
	public abstract TimeModel getTimeModel();
	
}
