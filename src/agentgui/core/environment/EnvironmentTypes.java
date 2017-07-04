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
package agentgui.core.environment;

import java.util.Iterator;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;

/**
 * This extended Vector holds all known environment types of Agent.GUI.<br>
 * Its concrete instance is stored in the class GlobalInfo 
 * (accessible at runtime by using Application.RunInfo)
 *
 * @see Application#getGlobalInfo()
 * @see GlobalInfo
 * @see GlobalInfo#getKnownEnvironmentTypes()
 * @see GlobalInfo#setKnownEnvironmentTypes(EnvironmentTypes)
 * @see EnvironmentType
 *   
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentTypes extends Vector<EnvironmentType>{

	private static final long serialVersionUID = 1887651840189227293L;

	/**
	 * Constructor of this class
	 */
	public EnvironmentTypes() {
		super();
	}
	
	/**
	 * This method returns the EnvironmentType instance searched by its key
	 * @param key
	 * @return The instance of the EnvironmentType
	 * @see EnvironmentType	
	 */
	public EnvironmentType getEnvironmentTypeByKey(String key) {
		
		for (Iterator<EnvironmentType> it = this.iterator(); it.hasNext();) {
			EnvironmentType envTyp = it.next();
			if (envTyp.getInternalKey().equals(key)) {
				return envTyp;
			}
		}
		return this.get(0); // --- the default value ---
	}
	
}
