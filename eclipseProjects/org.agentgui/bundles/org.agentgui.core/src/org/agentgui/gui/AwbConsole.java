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

import java.util.Vector;

/**
 * The Interface AwbConsole defines the required methods for a console.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface AwbConsole {


	/**
	 * Return if the current console is a local console (from current JVM).
	 * @return true, if is local console
	 */
	public boolean isLocalConsole();
	
	/**
	 * Sets if the current console is a local console.
	 * @param isLocalConsole the new checks if is local console
	 */
	public void setLocalConsole(boolean isLocalConsole);

	
	/**
	 * Appends a Vector<String> of lines to this console  .
	 * @param lines2transfer the lines to print
	 */
	public void appendText(Vector<String> linesToPrint);
	
	/**
	 * This method can be used in order to append the console output from a remote container.
	 * A line may start with '[SysOut]' or '[SysErr]' to indicate the type of output.
	 * 
	 * @param text the text to print out
	 */
	public void appendText(String text);
	
	/**
	 * This method can be used in order to append text to the current console
	 * @param text the text to print
	 * @param isError specifies if the text is an error or not
	 */
	public void appendText(String text, boolean isError);
	
	
}
