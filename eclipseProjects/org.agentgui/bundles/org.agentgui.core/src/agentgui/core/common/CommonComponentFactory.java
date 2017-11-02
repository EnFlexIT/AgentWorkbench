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
package agentgui.core.common;

import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import de.enflexit.common.csv.CsvDataControllerPanel;
import de.enflexit.common.swing.ProgressMonitor;
import de.enflexit.common.transfer.RecursiveFolderCopier;
import de.enflexit.common.transfer.RecursiveFolderDeleter;
import de.enflexit.common.transfer.Zipper;

/**
 * A factory for creating common components that are based  
 * in the bundle de.enflexit.common and their sub packages.
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class CommonComponentFactory {

	/**
	 * Return a new pre-configured {@link ProgressMonitor}.
	 *
	 * @param windowTitle the window title - may be <code>null</code>
	 * @param headerText the header text - may be <code>null</code>
	 * @param progressText the progress text - may be <code>null</code>
	 * @return the new progress monitor
	 */
	public static ProgressMonitor getNewProgressMonitor(String windowTitle, String headerText, String progressText) {
		
		// --- Try to get a JDesktopPane ----------------------------
		JDesktopPane desktop = null;
		if (Application.getMainWindow()!=null) {
			desktop = Application.getMainWindow().getJDesktopPane4Projects();
		}
		
		// --- Get the image icon for the progress monitor ----------
		ImageIcon imageIcon = GlobalInfo.getInternalImageIcon("AgentGUI.png");
		// --- Get the look and feel --------------------------------
		String lookAndFeelClassName = Application.getGlobalInfo().getAppLookAndFeelClassName();
		
		// --- Initiate new ProgressMonitor ------------------------- 
		ProgressMonitor pm = new ProgressMonitor(windowTitle, headerText, progressText, imageIcon, desktop, lookAndFeelClassName);
		
		return pm;
	}

	/**
	 * Returns a new pre-configured {@link Zipper}.
	 *
	 * @param owner the owner frame
	 * @return the new {@link Zipper} instance
	 */
	public static Zipper getNewZipper() {
		return getNewZipper(null);
	}
	/**
	 * Returns a new pre-configured {@link Zipper}.
	 *
	 * @param owner the owner frame - may be <code>null</code>
	 * @return the new {@link Zipper} instance
	 */
	public static Zipper getNewZipper(Frame owner) {
		
		Zipper zipper = new Zipper(owner);
		zipper.setIconImage(GlobalInfo.getInternalImage("AgentGUI.png"));
		zipper.setLookAndFeelClassName(Application.getGlobalInfo().getAppLookAndFeelClassName());
		zipper.setApplicationName(Application.getGlobalInfo().getApplicationTitle());
		return zipper;
	}
	
	/**
	 * Returns a new pre-configured {@link CsvDataControllerPanel}.
	 * @return the new {@link CsvDataControllerPanel}
	 */
	public static CsvDataControllerPanel getNewCsvDataControllerPanel() {
		CsvDataControllerPanel csvPanel = new CsvDataControllerPanel();
		csvPanel.setLastSelectedFolderReminder(Application.getGlobalInfo());
		return csvPanel;
	}
	
	/**
	 * Returns a new {@link RecursiveFolderCopier}
	 * @return the new {@link RecursiveFolderCopier}
	 */
	public static RecursiveFolderCopier getNewRecursiveFolderCopier() {
		return new RecursiveFolderCopier();
	}
	
	/**
	 * Returns a new {@link RecursiveFolderDeleter}
	 * @return the new {@link RecursiveFolderDeleter}
	 */
	public static RecursiveFolderDeleter getNewRecursiveFolderDeleter() {
		return new RecursiveFolderDeleter();
	}
	
}
