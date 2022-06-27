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

import java.io.Serializable;
import java.util.Vector;

/**
 * The Class TimeFormatVector is a Vector that holds different TimeFormats.<br>
 * When initiating this class the Vector will already be filled with TimeFormats.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeFormatVector extends Vector<TimeFormat> implements Serializable {

	private static final long serialVersionUID = 1736528558614878985L;

	/**
	 * Instantiates a new TimeFormatVector and fills it already
	 * with values of the type String.
	 * 
	 * @see TimeUnit
	 */
	public TimeFormatVector() {
		
		this.add(new TimeFormat("dd.MM.yyyy HH:mm:ss,SSS"));
		
		this.add(new TimeFormat("dd.MM.yyyy HH:mm:ss"));
		this.add(new TimeFormat("dd.MM.yyyy HH:mm"));
		this.add(new TimeFormat("dd.MM.yy HH:mm"));
		this.add(new TimeFormat("dd.MM.yy"));
		
		this.add(new TimeFormat("HH:mm:ss,SSS"));
		this.add(new TimeFormat("HH:mm:ss"));
		this.add(new TimeFormat("HH:mm"));
		
	}
	
}
