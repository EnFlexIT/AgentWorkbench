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
package jade.debugging;

import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.BaseService;
import jade.core.Filter;
import jade.core.HorizontalCommand;
import jade.core.IMTPException;
import jade.core.MainContainer;
import jade.core.Node;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.Service;
import jade.core.ServiceException;
import jade.core.ServiceHelper;
import jade.core.VerticalCommand;
import jade.core.management.AgentManagementSlice;
import jade.debugging.components.JFrame4Consoles;
import jade.debugging.components.JPanelConsole;
import jade.debugging.components.JTabbedPane4Consoles;
import jade.debugging.components.PrintStreamListener;
import jade.debugging.components.SysOutBoard;
import jade.debugging.components.SysOutScanner;
import jade.util.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * This Service can be used in order to direct (as a copy) the system output generated<br>
 * by commands like System.out and System.err to the Main-Container of a platform.<br> 
 * <br>
 * For this several components were developed, like a PrintStreamListener, which<br>  
 * listens to the current PrintStrem without disturbing it.<br>
 * Covering the circumstance, that in one JVM several containers can run, the<br>
 * Services comes with a Singleton class, which holds the needed information<br>
 * for one JVM.<br>
 * 
 * @see PrintStreamListener
 * @see SysOutBoard
 * @see #boot(Profile)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class DebugService extends BaseService {

	public static final String NAME = DebugServiceHelper.SERVICE_NAME;
	
	private AgentContainer myContainer;
	private MainContainer myMainContainer;
	
	private Filter incFilter;
	private Filter outFilter;
	private ServiceComponent localSlice;

	
	/* (non-Javadoc)
	 * @see jade.core.BaseService#init(jade.core.AgentContainer, jade.core.Profile)
	 */
	public void init(AgentContainer ac, Profile p) throws ProfileException {
		
		super.init(ac,p);
		myContainer = ac;
		myMainContainer = ac.getMain();		

		// --- Create filters -----------------------------
		outFilter = new CommandOutgoingFilter();
		incFilter = new CommandIncomingFilter();
		// --- Create local slice -------------------------
		localSlice = new ServiceComponent();
		
		if (myContainer!=null) {
			if (myLogger.isLoggable(Logger.FINE)) {
				myLogger.log(Logger.FINE, "Starting DebugService: My-Container: " + myContainer.toString());
			}
		}
		
	}
	/**
	 * Depending on the execution location the service configures independently
	 */
	public void boot(Profile p) throws ServiceException {
		
		if (myMainContainer==null) {
			// -----------------------------------------------------------------------
			// --- We are in a remote/satellite container ----------------------------
			// -----------------------------------------------------------------------
			if (SysOutBoard.isLocationOfMainContainer()==false) {
				
				if (SysOutBoard.isRunningSysOutScanner()==false) {
					// --- Start the console Scanner --------------
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Satellite container '" + localSlice.getNode().getName() + "': Attaching SystemOutputScanner");
					}
					SysOutBoard.setSysOutScanner(new SysOutScanner(this));
					
				}
				
			} else {
				// --- Remove such local consoles because the output ---------- 
				// --- will come in the local console				 ----------
				String localSliceName = localSlice.getNode().getName();
				JPanelConsole jpc = SysOutBoard.getHashMapJPanelConsoles().get(localSliceName);
				if (jpc!=null) {
					SysOutBoard.getJTabbedPane4Consoles().remove(jpc);
					SysOutBoard.getHashMapJPanelConsoles().remove(localSliceName);
				}
				
			}
			
		} else {
			// -----------------------------------------------------------------------
			// --- Is !=null, if the Service will start at the Main-Container !!! ----
			// --- In other words: We are in the Main-Container !!!               ----
			// -----------------------------------------------------------------------
			if (myLogger.isLoggable(Logger.FINE)) {
				myLogger.log(Logger.FINE, "Main-Container: " + myMainContainer.toString());
			}
			
			// --- Get the JTabbedPane, where the consoles can be shown ------- 
			JTabbedPane4Consoles tp4c = SysOutBoard.getJTabbedPane4Consoles();
			if (tp4c==null) {
				// --- Create the Frame, where the consoles can be displayed --
				JFrame4Consoles displayFrame = new JFrame4Consoles();
				tp4c = displayFrame.getJTabbedPaneRemoteConsoles();
				SysOutBoard.setJFrame4Display(displayFrame);
				SysOutBoard.setJTabbedPane4Consoles(tp4c);
				
				// --- Create a console window for the local output -----------
				JPanelConsole localConsole = new JPanelConsole(true);
				SysOutBoard.getJTabbedPane4Consoles().addTab("Local", localConsole);
				SysOutBoard.setHashMapJPanelConsoles(new HashMap<String, JPanelConsole>());
				SysOutBoard.getHashMapJPanelConsoles().put("Local", localConsole);

				// --- Show the dialog for the system output ------------------
				displayFrame.setVisible(true);
				
			}
			
			// --- If there are old consoles, remove them from tab ------------
			HashMap<String, JPanelConsole> consoleHash = SysOutBoard.getHashMapJPanelConsoles();
			if (consoleHash != null) {
				if (consoleHash.size()>0) {
					Set<String> consoleKeys = consoleHash.keySet();
					for (Iterator<String> iterator = consoleKeys.iterator(); iterator.hasNext();) {
						String consoleKey = (String) iterator.next();
						JPanelConsole currConsole = consoleHash.get(consoleKey);
						if (currConsole.isLocalConsole()==false) {
							tp4c.remove(consoleHash.get(consoleKey));
							SysOutBoard.getHashMapJPanelConsoles().remove(consoleKey);
							
						}
					}	
				}
			}
			
			// --- Configure the SysOutBoard for the current environment ------
			if (SysOutBoard.getHashMapJPanelConsoles() ==null) {
				SysOutBoard.setHashMapJPanelConsoles(new HashMap<String, JPanelConsole>());
			}
			SysOutBoard.setIsLocationOfMainContainer(true);
			
		}
		
	}
	public String getName() {
		return NAME;
	}
	public ServiceHelper getHelper (Agent ag) {
		return new DebugServiceImpl();
	}
	public Filter getCommandFilter(boolean direction) {
		if(direction == Filter.INCOMING) {
			return incFilter;
		}
		else {
			return outFilter;
		}
	}
	public Class<?> getHorizontalInterface() {
		return DebugServiceSlice.class;
	}
	/**
	 * Retrieve the locally installed slice of this service.
	 */
	public Service.Slice getLocalSlice() {
		return localSlice;
	}

	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'DebugServiceImpl' ---- Start ---------------
	// --------------------------------------------------------------
	/**
	 * Sub-Class to provide interaction between Agents and this Service
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
	 */
	public class DebugServiceImpl implements DebugServiceHelper {

		@Override
		public void init(Agent arg0) {
		}		

		public void sendLocalConsoleOutput() throws ServiceException {
			Vector<String> lines2transfer = null;
			synchronized (SysOutBoard.getSysOutScanner()) {
				lines2transfer = SysOutBoard.getSysOutScanner().getStack();	
			}
			sendLocalConsoleOutput2Main(lines2transfer);
		}

	}
	// --------------------------------------------------------------	
	// ---- Inner-Class 'DebugServiceImpl' ---- End -----------------
	// --------------------------------------------------------------
	
	/**
	 * Sends local console output to the MainContainer.
	 *
	 * @param lines2transfer the lines2transfer
	 * @throws ServiceException the service exception
	 */
	public void sendLocalConsoleOutput2Main(Vector<String> lines2transfer) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Send console output to main slice!");
		}
		String sliceName = null;
		try {
			DebugServiceSlice slice = (DebugServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Try to send console output to " + sliceName);
			}
			slice.sendLocalConsoleOutput2Main(localSlice.getNode().getName(), lines2transfer);
		}
		catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while trying to send console output to " + sliceName, t);
		}
	}
	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'ServiceComponent' ---- Start ---------------
	// --------------------------------------------------------------
	/**
	 * Inner class ServiceComponent. Will receive Commands, which 
	 * are coming from the LoadServiceProxy 
	 */
	private class ServiceComponent implements Service.Slice {
		
		private static final long serialVersionUID = 1776886375724997808L;

		public Service getService() {
			return DebugService.this;
		}
		
		public Node getNode() throws ServiceException {
			try {
				return DebugService.this.getLocalNode();
			}
			catch(IMTPException imtpe) {
				throw new ServiceException("Error retrieving local node", imtpe);
			}
		}
		
		public VerticalCommand serve(HorizontalCommand cmd) {
			
			if (cmd==null) return null;
			try {
				String cmdName = cmd.getName();
				Object[] params = cmd.getParams();
				
				//System.out.println( "=> DebugService ServiceComponent " + cmd.getService() + " " +  cmdName);
				if (cmdName.equals(DebugServiceSlice.DEBUG_SEND_LOCAL_OUTPUT)) {
					String containerName = (String) params[0];
					@SuppressWarnings("unchecked")
					Vector<String> lines2transfer = (Vector<String>) params[1];
					this.addConsoleLines(containerName, lines2transfer);
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Got new console output from " + containerName);
					}
				}
				
			} catch (Throwable t) {
				cmd.setReturnValue(t);
			}
			return null;
		}

		
		// -----------------------------------------------------------------
		// --- The real functions for the Service Component --- Start ------ 
		// -----------------------------------------------------------------
		private void addConsoleLines(String containerName, Vector<String> lines2transfer) {

			// --- If console for container does not exists, create it ----- 
			JPanelConsole currConsole = SysOutBoard.getHashMapJPanelConsoles().get(containerName);
			if (currConsole==null) {
				
				currConsole = new JPanelConsole();
				SysOutBoard.getHashMapJPanelConsoles().put(containerName, currConsole);
				
				JTabbedPane4Consoles tp4c = SysOutBoard.getJTabbedPane4Consoles();
				if (tp4c!=null) {
					// --- show JFrame if defined ---------
					if (SysOutBoard.getJFrame4Display()!=null) {
						SysOutBoard.getJFrame4Display().setVisible(true);
					}
					tp4c.addTab(containerName, currConsole);
					// --- set focus ----------------------
					tp4c.setSelectedComponent(currConsole);
				}
			}
			currConsole.appendText(lines2transfer);
			
		}
		// -----------------------------------------------------------------
		// --- The real functions for the Service Component --- Stop ------- 
		// -----------------------------------------------------------------
	} 
	// --------------------------------------------------------------	
	// ---- Inner-Class 'ServiceComponent' ---- End -----------------
	// --------------------------------------------------------------
	
	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandOutgoingFilter' ---- Start ----------
	// --------------------------------------------------------------
	/**
	 * Inner class CommandOutgoingFilter.
	 */
	private class CommandOutgoingFilter extends Filter {
		public CommandOutgoingFilter() {
			super();
		}
		public final boolean accept(VerticalCommand cmd) {
			
			if (cmd==null) return true;

			String cmdName = cmd.getName();
			if (cmdName.equals(AgentManagementSlice.KILL_CONTAINER)) {
//				Object[] params = cmd.getParams();
//				ContainerID id = (ContainerID) params[0];
//				String containerName = id.getName();
//				System.out.println("Kill " + containerName);
			}
			// Never veto other commands
			return true;
		}
	}
	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandOutgoingFilter' ---- End ------------
	// --------------------------------------------------------------


	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandIncomingFilter' ---- Start ----------
	// --------------------------------------------------------------
	/**
	 * Inner class CommandIncomingFilter.
	 */
	private class CommandIncomingFilter extends Filter {
		
		public boolean accept(VerticalCommand cmd) {
			
			if (cmd==null) return true;
			String cmdName = cmd.getName();
			//System.out.println( "=> in " + cmdName + " - " + cmd.getService());
			
			if (myMainContainer != null) {
				if (cmdName.equals(Service.NEW_SLICE)) {
					// --- If the new slice is a DebugServiceSlice, notify it about the current state ---
					handleNewSlice(cmd);
				}
				
			} else {
				if (cmdName.equals(Service.REATTACHED)) {
					// The Main lost all information related to this container --> Notify it again
				}
			}
			// Never veto a Command
			return true;
		}
	} 
	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandIncomingFilter' ---- End ------------
	// --------------------------------------------------------------

	/**
	 * If the new slice is a LoadServiceSlice notify it about the current state
	 */
	private void handleNewSlice(VerticalCommand cmd) {
		
		if (cmd.getService().equals(NAME)) {
			// --- We ARE in the Main-Container !!! -----------------
			Object[] params = cmd.getParams();
			String newSliceName = (String) params[0];

			String localSliceName = null; 
			try {
				localSliceName = localSlice.getNode().getName();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			if (newSliceName.equals(localSliceName)==false) {
				Vector<String> lineOutput= new Vector<String>();
				lineOutput.add(PrintStreamListener.SystemError + "INFO: --- Container <" + newSliceName + "> was added to the platform! ---");
				localSlice.addConsoleLines(newSliceName, lineOutput);
				
			}
			
		}
	}

	
}
