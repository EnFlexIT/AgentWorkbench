package mas.service;

import jade.core.AID;
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
import jade.util.Logger;

import java.util.Date;
import java.util.Hashtable;

import mas.service.time.TimeModel;

/**
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class SimulationService extends BaseService {

	public static final String NAME = SimulationServiceHelper.SERVICE_NAME;

	private AgentContainer myContainer;
	private MainContainer myMainContainer;
	
	private Filter incFilter;
	private Filter outFilter;
	private ServiceComponent localSlice;
	
	// --- The List of Agents, which are registered to this service ----------- 
	private Hashtable<String,AID> agentList = new Hashtable<String,AID>();
	// --- The current TimeModel ----------------------------------------------
	private TimeModel timeModel = null;
	
	
	public void init(AgentContainer ac, Profile p) throws ProfileException {
		
		super.init(ac, p);
		myContainer = ac;
		myMainContainer = ac.getMain();
		// Create filters
		outFilter = new CommandOutgoingFilter();
		incFilter = new CommandIncomingFilter();
		// Create local slice
		localSlice = new ServiceComponent();
		
		if (myContainer!=null) {
			System.out.println( "Starting SimulationService: Me: " + myContainer.toString() );	
		}
		if (myMainContainer!=null) {
			System.out.println( "Main: " + myMainContainer.toString() );
		}
		
	}
	public void boot(Profile p) throws ServiceException {
		super.boot(p);
	}
	public String getName() {
		return NAME;
	}
	public ServiceHelper getHelper (Agent ag) {
		return new SimulationServiceImpl();
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
		return SimulationServiceSlice.class;
	}
	
	/**
	 * Retrieve the locally installed slice of this service.
	 */
	public Service.Slice getLocalSlice() {
		return localSlice;
	}

	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'AgentTimeImpl' ---- Start ------------------
	// --------------------------------------------------------------
	/**
	 * Sub-Class to provide interaction between Agents and this Service
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
	 */
	public class SimulationServiceImpl implements SimulationServiceHelper {

		private static final long serialVersionUID = 5741448121178289099L;

		public void init(Agent ag) {
			// --- Store the Agent in the agentList -----------------
			agentList.put(ag.getName(), ag.getAID());
			// --- Remind all Agents which are interested in time ---
		}

		public void setTimeModel(TimeModel newTimeModel) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastSetTimeModel(newTimeModel, slices);
		}
		public TimeModel getTimeModel() throws ServiceException {
			return timeModel;
		}
		public void stepTimeModel() throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastStepTimeModel(slices);
		}		
		
		public Date getWorldTimeLocalAsDate() {
			Date nowDate = new Date();
			return nowDate;
		}
		public Long getWorldTimeLocalAsLong() {
			return System.currentTimeMillis();
		}
	
	}
	// --------------------------------------------------------------	
	// ---- Inner-Class 'AgentTimeImpl' ---- End --------------------
	// --------------------------------------------------------------
	
	/**
	 * Broadcast the current TimeModel to all Slices
	 */
	private void broadcastSetTimeModel(TimeModel tm, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending current TimeModel!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending current TimeModel to " + sliceName);
				}
				slice.setTimeModel(tm);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error propagating current TimeModel to slice  " + sliceName, t);
			}
		}
	}
	
	/**
	 * Broadcast the current TimeModel to all Slices
	 */
	private void broadcastStepTimeModel(Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending current TimeModel!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending current TimeModel to " + sliceName);
				}
				slice.stepTimeModel();
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error propagating current TimeModel to slice  " + sliceName, t);
			}
		}
	}
	
	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'ServiceComponent' ---- Start ---------------
	// --------------------------------------------------------------
	/**
	 * Inner class ServiceComponent. Will receive Commands, which 
	 * are comming from the SimulationServiceProxy 
	 */
	private class ServiceComponent implements Service.Slice {
		
		private static final long serialVersionUID = 1776886375724997808L;

		public Service getService() {
			return SimulationService.this;
		}
		
		public Node getNode() throws ServiceException {
			try {
				return SimulationService.this.getLocalNode();
			}
			catch(IMTPException imtpe) {
				throw new ServiceException("Error retrieving local node", imtpe);
			}
		}
		
		public VerticalCommand serve(HorizontalCommand cmd) {
			
			try {
				if (cmd==null) return null;
				//if ( ! cmd.getService().equals(NAME) ) return null;
				
				String cmdName = cmd.getName();
				Object[] params = cmd.getParams();
				
				//System.out.println( "=> ServiceComponent " + cmdName);
				if (cmdName.equals(SimulationServiceSlice.SIM_SET_TIMEMODEL)) {
					TimeModel ntm = (TimeModel) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received TimeModel");
					}					
					setTimeModel(ntm);
				}
				else if(cmdName.equals(SimulationServiceSlice.SIM_GET_TIMEMODEL)) {
					AID aid = (AID) params[0];
					AID topic = (AID) params[1];
					//System.out.println("Received deregistration of agent "+aid.getName()+" from topic "+topic.getLocalName());
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received deregistration of agent "+aid.getName()+" from topic "+topic.getLocalName());
					}					
					//deregister(aid, topic);
				} else if (cmdName.equals(SimulationServiceSlice.SIM_STEP_TIMEMODEL)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Step the TimeModel");
					}					
					stepTimeModel();				
				}
			}
			catch (Throwable t) {
				cmd.setReturnValue(t);
			}
			return null;
			
		}
		
		private void setTimeModel(TimeModel newTimeModel) {
			timeModel = newTimeModel;
		}
		private void stepTimeModel() {
			timeModel.step(timeModel);	
		}
		
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
			//setPreferredPosition(2);  // Before the Messaging (encoding) filter and the security related ones
		}
		public final boolean accept(VerticalCommand cmd) {
			
			if (cmd==null) return true;
			//if ( ! cmd.getService().equals(NAME) ) return true;

			String name = cmd.getName();
			//System.out.println( "=> out " + name + " - " + cmd.getService());
			if (name.equals("nothing yet")) {
				// --- Here is nothing to do yet ---------------------

				// Veto the original SEND_MESSAGE command
				return false;
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
			String name = cmd.getName();
			//if ( ! name.equals(NAME) ) return true;
			
			//System.out.println( "=> in " + name + " - " + cmd.getService());

			if (myMainContainer != null) {
				if (name.equals(AgentManagementSlice.INFORM_KILLED)) {
					// If the dead agent was registered to some topic, deregister it
					//handleInformKilled(cmd);
				}
				if (name.equals(Service.NEW_SLICE)) {
					// --- If the new slice is a SimulationServiceSlice, notify it about the current state ---
					handleNewSlice(cmd);
				}
			}
			else {
				if (name.equals(Service.REATTACHED)) {
					// The Main lost all information related to this container --> Notify it again
					//handleReattached(cmd);
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
	 * If the new slice is a SimulationServiceSlice notify it about the current state
	 */
	private void handleNewSlice(VerticalCommand cmd) {
		
		if (cmd.getService().equals(NAME)) {
			Object[] params = cmd.getParams();
			String newSliceName = (String) params[0];
			try {
				// --- Be sure to get the new (fresh) slice --> Bypass the service cache ---
				SimulationServiceSlice newSlice = (SimulationServiceSlice) getFreshSlice(newSliceName);
				// --- set remote TimeModel ------------------------------------------------
				newSlice.setTimeModel(timeModel);			
				
			}
			catch (Throwable t) {
				myLogger.log(Logger.WARNING, "Error notifying new slice "+newSliceName+" about current SimulationService-State", t);
			}
		}
	}
	
	
}
