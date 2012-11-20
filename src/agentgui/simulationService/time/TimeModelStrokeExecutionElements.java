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

import java.awt.Font;

import javax.swing.JLabel;

import agentgui.core.application.Language;

/**
 * The Class TimeModelStrokeExecutionElements.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelStrokeExecutionElements extends JToolBarElements4TimeModelExecution {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 385807623783469748L;

	private TimeModelStroke timeModelStroke = null;
	
	private JLabel jLabelIntro = null;
	private JLabel jLabelTimeDisplay = null;
	
	
	/**
	 * Instantiates a new time model stroke execution elements.
	 */
	public TimeModelStrokeExecutionElements() {
		this.add(this.getIntro());
		this.add(this.getTimeDisplay());
	}

	/**
	 * Gets the intro.
	 * @return the time intro
	 */
	private JLabel getIntro() {
		if (this.jLabelIntro==null) {
			jLabelIntro = new JLabel("Zähler");
			jLabelIntro.setText(" " + Language.translate(jLabelIntro.getText()) + ": ");
			jLabelIntro.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return this.jLabelIntro;
	}
	/**
	 * Returns the time display.
	 * @return the time display
	 */
	private JLabel getTimeDisplay() {
		if (this.jLabelTimeDisplay==null) {
			jLabelTimeDisplay = new JLabel("");
			jLabelTimeDisplay.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return this.jLabelTimeDisplay;
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.JToolBarElements4TimeModelExecution#setTimeModel(agentgui.simulationService.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		this.timeModelStroke = (TimeModelStroke) timeModel;
		if (this.timeModelStroke==null) {
			this.getTimeDisplay().setText("");			

		} else {
			this.getTimeDisplay().setText(this.timeModelStroke.getCounter().toString());
			
		}

	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.JToolBarElements4TimeModelExecution#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {
		return this.timeModelStroke;
	}

}
