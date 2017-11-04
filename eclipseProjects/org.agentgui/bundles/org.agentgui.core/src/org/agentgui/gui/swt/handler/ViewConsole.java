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
 
package org.agentgui.gui.swt.handler;

import org.agentgui.gui.swt.AppModelId;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Handler class to show / hide the console view.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ViewConsole {

	@Execute 
	public void execute() {
		
		try {
			// --- Show or hide the console view ----------
			IWorkbenchPage wbPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart viewConsole = wbPage.findView(AppModelId.PART_ORG_AGENTGUI_CORE_PART_CONSOLE);
			if (viewConsole==null) {
				// --- Open console view ------------------
				wbPage.showView(AppModelId.PART_ORG_AGENTGUI_CORE_PART_CONSOLE);
				
			} else {
				// --- Close console view -----------------
				int currentState = wbPage.getPartState(wbPage.getReference(viewConsole));
				if (currentState==IWorkbenchPage.STATE_MAXIMIZED) {
					wbPage.activate(viewConsole);
					wbPage.setPartState(wbPage.getReference(viewConsole), IWorkbenchPage.STATE_RESTORED);
					// --- To be progressed! --------------
					wbPage.resetPerspective(); 
				}
				wbPage.hideView(viewConsole);
				
			}
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
	}
	
}