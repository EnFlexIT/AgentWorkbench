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

import agentgui.core.project.Project;
import agentgui.simulationService.time.JPanel4TimeModelConfiguration;
import agentgui.simulationService.time.TimeModelContinuous;

/**
 * TimeModel which uses the current system time of the system/platform, especially for monitoring use cases.
 */
public class TimeModelPresent extends TimeModelContinuous {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7524432551373560286L;

    /**
     * Instantiates a new time model present.
     */
    public TimeModelPresent() {
        setAccelerationFactor(1.0);
        setTimeStart(getTime());
        setTimeStop(Long.MAX_VALUE);
    }

    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelContinuous#getJPanel4Configuration(agentgui.core.project.Project)
     */
    @Override
    public JPanel4TimeModelConfiguration getJPanel4Configuration(Project project) {
        return new TimeModelPresentConfiguration(project);
    }

    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelContinuous#getTime()
     */
    @Override
    public long getTime() {
        return getSystemTimeSynchronized();
    }

    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelDateBased#getTimeStart()
     */
    @Override
    public long getTimeStart() {
        return getTime();
    }

    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelContinuous#logTookLocalTime()
     */
    @Override
    protected void logTookLocalTime() {
        // don't output message
    }

}
