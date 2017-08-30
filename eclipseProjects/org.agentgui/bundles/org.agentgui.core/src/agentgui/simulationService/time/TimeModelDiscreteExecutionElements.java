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

import java.awt.Color;

import agentgui.core.application.Language;

/**
 * The Class TimeModelDiscreteExecutionElements.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelDiscreteExecutionElements extends TimeModelBaseExecutionElements {

	private final String toolBarTitle = Language.translate("Zeit");
	private TimeModelDiscrete timeModelDiscrete = null;
	
	
	/**
	 * Instantiates a new time model discrete execution elements.
	 */
	public TimeModelDiscreteExecutionElements() {
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModelBaseExecutionElements#getToolBarTitle()
	 */
	@Override
	public String getToolBarTitle() {
		return this.toolBarTitle;
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModelBaseExecutionElements#setTimeModel(agentgui.simulationService.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		this.timeModelDiscrete = (TimeModelDiscrete) timeModel;
		if (this.timeModelDiscrete==null) {
			this.getJLabelTimeDisplay().setText("");			

		} else {
			
			long time = this.timeModelDiscrete.getTime();
			long timeStop = this.timeModelDiscrete.getTimeStop();
			String timeFormat = this.timeModelDiscrete.getTimeFormat();
			
			// --- Set color of time display --------------
			if (time<=timeStop) {
				this.getJLabelTimeDisplay().setForeground(new Color(0, 153, 0));
			} else {
				this.getJLabelTimeDisplay().setForeground(new Color(255, 51, 0));	
			}
			
			switch (this.view) {
			case ViewCOUNTDOWN:
				// --- countdown view ---------------------
				long timeDiff = timeStop-time;
				if (timeDiff<=0) {
					this.view = ViewTIMER;
					this.setTimeModel(timeModel);
					return;
				}

				// --- Set display for the current time --- 
				this.getJLabelTimeDisplay().setText(this.getTimeDurationFormatted(timeDiff, timeFormat));
				this.getJButtonTimeConfig().setText(this.getJMenuItemViewCountdown().getText());
				break;
				
			default:
				// --- timer view -------------------------
				this.getJLabelTimeDisplay().setText(this.getTimeFormatted(time, timeFormat));
				this.getJButtonTimeConfig().setText(this.getJMenuItemViewTimer().getText());
				break;
			}
			
		}

	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModelBaseExecutionElements#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {
		return this.timeModelDiscrete;
	}
	

}
