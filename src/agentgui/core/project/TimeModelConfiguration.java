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
package agentgui.core.project;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import agentgui.simulationService.time.TimeModel;

/**
 * The Class TimeModelConfiguration holds the TimeModel settings for a projects.
 * 
 * @see Project
 * @see TimeModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelConfiguration implements Serializable {

	private static final long serialVersionUID = 6617541726643624421L;

	@XmlAttribute
	private boolean useTimeModel;
	@XmlAttribute
	private String timeModelClass;

	
	/**
	 * Instantiates a new time model configuration.
	 */
	public TimeModelConfiguration() {
	}
	
	/**
	 * Instantiates a new time model configuration.
	 *
	 * @param useTimeModel the use time model
	 * @param timeModelClass the time model class
	 */
	public TimeModelConfiguration(boolean useTimeModel, String timeModelClass) {
		this.setUseTimeModel(useTimeModel);
		this.setTimeModelClass(timeModelClass);
	}

	/**
	 * Sets the use time model.
	 * @param useTimeModel the new use time model
	 */
	public void setUseTimeModel(boolean useTimeModel) {
		this.useTimeModel = useTimeModel;
	}
	/**
	 * Checks if is use time model.
	 * @return true, if is use time model
	 */
	@XmlTransient
	public boolean isUseTimeModel() {
		return useTimeModel;
	}

	/**
	 * Sets the time model class.
	 * @param timeModelClass the new time model class
	 */
	public void setTimeModelClass(String timeModelClass) {
		this.timeModelClass = timeModelClass;
	}
	/**
	 * Gets the time model class.
	 * @return the time model class
	 */
	@XmlTransient
	public String getTimeModelClass() {
		return timeModelClass;
	}
	
}
