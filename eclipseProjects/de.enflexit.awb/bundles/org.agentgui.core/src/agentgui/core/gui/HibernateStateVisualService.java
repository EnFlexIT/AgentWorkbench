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
package agentgui.core.gui;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;
import de.enflexit.db.hibernate.gui.HibernateStateVisualizationService;

/**
 * The Class HibernateStateVisualService.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class HibernateStateVisualService implements HibernateStateVisualizationService {

	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.HibernateStateVisualizationService#setSessionFactoryState(java.lang.String, de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState)
	 */
	@Override
	public void setSessionFactoryState(String factoryID, SessionFactoryState sessionFactoryState) {
		if (Application.isOperatingHeadless()==false) {
			GlobalInfo gInfo = Application.getGlobalInfo();
			boolean isApplication = gInfo.getExecutionMode()==ExecutionMode.APPLICATION;
			boolean isDeviceApplication = gInfo.getExecutionMode()==ExecutionMode.DEVICE_SYSTEM & gInfo.getDeviceServiceExecutionMode()==DeviceSystemExecutionMode.SETUP; 
			if (isApplication || isDeviceApplication) {
				MainWindow mw = Application.getMainWindow();
				if (mw!=null) {
					mw.getStatusBar().setSessionFactoryState(factoryID, sessionFactoryState);
				}
			}
		}
	}
	
}
