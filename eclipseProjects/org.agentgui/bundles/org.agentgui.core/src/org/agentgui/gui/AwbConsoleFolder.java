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
package org.agentgui.gui;

/**
 * The Interface AwbConsoleFolder corresponds to a JTabedPane (or similar) that.
 * enables to host several {@link AwbConsole} panels or parts.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface AwbConsoleFolder {

	/**
	 * Has to add the specified console tab.
	 *
	 * @param title the title of the tab
	 * @param console the console
	 */
	public void addTab(String title, AwbConsole console);
	
	/**
	 * Removes the specified console.
	 * @param console the console
	 */
	public void remove(AwbConsole console);
	
	/**
	 * Sets the selected console component.
	 * @param console the new selected component
	 */
	public void setSelectedComponent(AwbConsole console);
	
}
