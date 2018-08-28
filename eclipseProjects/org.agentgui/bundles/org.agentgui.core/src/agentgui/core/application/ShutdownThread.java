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
package agentgui.core.application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.management.ManagementFactory;

import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.ExecutionMode;

/**
 * The ShutdownThread will be executed in case of an headless
 * execution of Agent.GUI and will shutdown the application, if
 * the local file <i>Shutdown[Sufix][No].txt</i> was deleted.
 * 
 * @see Application#isOperatingHeadless()
 * @see ExecutionMode#DEVICE_SYSTEM
 * @see DeviceSystemExecutionMode#AGENT
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ShutdownThread extends Thread {

	private final String ShutDownFileNameTemplate = "Shutdown_XXXX.txt";
	private File shutDownFile;
	
	private boolean stopObserving;
	
	/**
	 * Instantiates a new shutdown executer.
	 */
	public ShutdownThread() {
		this.setName("ShutdownThread");
		this.createShutDownFile();
	}

	/**
	 * Creates the shutdown file that will be observed by this behaviour.
	 */
	private void createShutDownFile() {
		
		// --- Get the root path to the file --------------
		String agentGuiRoot = Application.getGlobalInfo().getPathBaseDir();

		// --- Determine the local process ID (PID) -------
		String localProcessName = ManagementFactory.getRuntimeMXBean().getName();
		String pidChar = localProcessName.substring(0, localProcessName.indexOf("@"));
		Integer pid = Integer.parseInt(pidChar);
		
		// --- Determine the current execution mode -------
		String fileSufix = Application.getGlobalInfo().getExecutionModeDescription();
		fileSufix = fileSufix.replaceAll("/", "_");
		fileSufix = fileSufix.replaceAll("\\[", "");
		fileSufix = fileSufix.replaceAll("\\]", "");
		fileSufix = fileSufix.replaceAll(" ", "");
		fileSufix += "_" + pid;
		
		// --- Configure file location and name -----------
		String shutDownFile = ShutDownFileNameTemplate.replace("XXXX", fileSufix);
		File checkFile = new File(agentGuiRoot + shutDownFile);
		Integer oldNo = pid;
		while (checkFile.exists()) {
			shutDownFile = shutDownFile.replace(oldNo + ".txt", (oldNo+1) + ".txt");
			oldNo++;
			checkFile  = new File(agentGuiRoot + shutDownFile);
		}
		this.shutDownFile = checkFile;
		
		// --- Write the shutdown text file ---------------
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(this.shutDownFile));
			writer.write("=> Just delete this file in order to stop the execution of Agent.GUI! <=");
	        
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
		
		// --- Create output to inform the user -----------
		System.out.println("=> Observe file '" + this.shutDownFile.getName() + "' for the shutdown of " + Application.getGlobalInfo().getApplicationTitle() + " ...");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		while (true) {
			// --- Check if the shutdown file exists ------ 
			if (this.shutDownFile.exists()==false) {
				System.out.println("=> Observed file '" + this.shutDownFile.getName() + "' was deleted!");
				System.out.println("=> Shutdown " + Application.getGlobalInfo().getApplicationTitle() + " ...");
				Application.stop();
				return;
			}
			// --- Stop the observation of the file? ------
			if (this.stopObserving==true) {
				this.deleteShutDownFile();
				return;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		} // --- end while ---
	}

	/**
	 * Stop the observation of the current Agent.GUI shutdown file. 
	 * The observed file will be deleted and the thread will be terminated.
	 */
	public synchronized void stopObserving() {
		this.stopObserving = true;
		this.deleteShutDownFile();
	}
	
	/**
	 * Deletes the shut down file.
	 */
	private synchronized void deleteShutDownFile() {
		if (this.shutDownFile!=null && this.shutDownFile.exists()==true) {
			if (this.shutDownFile.delete()==false) {
				this.shutDownFile.deleteOnExit();
			}
			this.shutDownFile = null;
		}
	}
	
	
}
