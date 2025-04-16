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
package org.awb.env.networkModel.controller.ui.timeModel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.event.ChangeListener;

import de.enflexit.awb.core.environment.TimeModelController;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.awb.simulation.environment.time.TimeModelPresent;

/**
 * The Class JPanelTimeModelPresent is the JPanel used for configuration of the TimeModelPresent.
 */
public class JPanelTimeModelPresent extends JPanelTimeModelContinuous implements ChangeListener {

    private static final long serialVersionUID = -7897380427330081422L;
    
    /**
     * Instantiates a new time model present configuration.
     *
     * @param project the project
     * @param timeModelController the time model controller
     */
    public JPanelTimeModelPresent(Project project, TimeModelController timeModelController) {
        super(project, timeModelController);
    }
    
    /* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(900, 65);
	}
    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelContinuousConfiguration#initialize()
     */
    @Override
    protected void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		this.setLayout(gridBagLayout);
		this.setSize(900, 60);

		GridBagConstraints gbcTimeFormarter = new GridBagConstraints();
		gbcTimeFormarter.gridx = 0;
		gbcTimeFormarter.gridy = 0;
		gbcTimeFormarter.anchor = GridBagConstraints.WEST;
		gbcTimeFormarter.insets = new Insets(5, 5, 0, 0);
		this.add(getJPanelTimeFormatter(), gbcTimeFormarter);
    }

    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelContinuousConfiguration#setTimeModel(agentgui.simulationService.time.TimeModel)
     */
    @Override
    public void setTimeModel(TimeModel timeModel) {
        TimeModelPresent timeModelPresent = null;
        if (timeModel == null) {
            timeModelPresent = new TimeModelPresent();
        } else {
            timeModelPresent = (TimeModelPresent) timeModel;
        }

        // --- Settings for the time format -------------------------
        this.enabledChangeListener = false;
        this.getJPanelTimeFormatter().setTimeFormat(timeModelPresent.getTimeFormat());
        this.enabledChangeListener = true;
    }

    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelContinuousConfiguration#getTimeModel()
     */
    @Override
    public TimeModel getTimeModel() {
        // --- Getting the time format ------------------------------
        String timeFormat = this.getJPanelTimeFormatter().getTimeFormat();

        // --- Set TimeModel ----------------------------------------
        TimeModelPresent tmp = new TimeModelPresent();
        tmp.setTimeFormat(timeFormat);
        return tmp;
    }

}
