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
import java.awt.Dimension;

import agentgui.core.application.Language;

/**
 * The Class TimeModelStrokeExecutionElements.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelStrokeExecutionElements extends TimeModelBaseExecutionElements {

	private final String toolBarTitle = Language.translate("Zähler");
	private TimeModelStroke timeModelStroke = null;
	
		
	/**
	 * Instantiates a new time model stroke execution elements.
	 */
	public TimeModelStrokeExecutionElements() {
		this.getJLabelTimeDisplay().setPreferredSize(new Dimension(60, 26));
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
		
		this.timeModelStroke = (TimeModelStroke) timeModel;
		if (this.timeModelStroke==null) {
			this.getJLabelTimeDisplay().setText("");			

		} else {
			
			Integer counter = this.timeModelStroke.getCounter();
			Integer counterStop = this.timeModelStroke.getCounterStop();
			// --- Set color of time display --------------
			if (counter<=counterStop) {
				this.getJLabelTimeDisplay().setForeground(new Color(0, 153, 0));
			} else {
				this.getJLabelTimeDisplay().setForeground(new Color(255, 51, 0));	
			}
			
			switch (this.view) {
			case ViewCOUNTDOWN:
				// --- countdown view ---------------------
				counter = counterStop-counter;
				if (counter<=0) {
					this.view = ViewTIMER;
					this.setTimeModel(timeModel);
					return;
				}
				this.getJLabelTimeDisplay().setText((counter.toString()));
				this.getJButtonTimeConfig().setText(this.getJMenuItemViewCountdown().getText());
				break;
				
			default:
				// --- timer view -------------------------
				this.getJLabelTimeDisplay().setText(counter.toString());
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
		return this.timeModelStroke;
	}

}
