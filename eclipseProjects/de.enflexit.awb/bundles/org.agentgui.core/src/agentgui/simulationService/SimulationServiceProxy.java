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
package agentgui.simulationService;

import jade.core.AID;
import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.Node;
import jade.core.ServiceException;
import jade.core.SliceProxy;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.transaction.EnvironmentManagerDescription;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This class provides the reals functionalities for the {@link SimulationServiceSlice}.
 */
public class SimulationServiceProxy extends SliceProxy implements SimulationServiceSlice {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7016240061703852319L;

	// ----------------------------------------------------------
	// --- Methods to synchronise the Time --- S T A R T --------
	// ----------------------------------------------------------
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#getRemoteTime()
	 */
	@Override
	public long getRemoteTime() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SYNCH_GET_REMOTE_TIME, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Long) result;
			
		} catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#setRemoteTimeDiff(long)
	 */
	@Override
	public void setRemoteTimeDiff(long timeDifference) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SYNCH_SET_TIME_DIFF, SimulationService.NAME, null);
			cmd.addParam(timeDifference);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}		
	}
	// ----------------------------------------------------------
	// --- Methods to synchronise the Time --- S T O P ----------
	// ----------------------------------------------------------

	
	// ----------------------------------------------------------
	// --- Methods on the Manager-Agent --- S T A R T -----------
	// ----------------------------------------------------------
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#setManagerAgent(agentgui.simulationService.transaction.EnvironmentManagerDescription)
	 */
	public void setManagerAgent(EnvironmentManagerDescription envManager) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_MANAGER_AGENT, SimulationService.NAME, null);
			cmd.addParam(envManager);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	// ----------------------------------------------------------
	// --- Methods on the Manager-Agent --- S T O P -------------
	// ----------------------------------------------------------
	
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#getEnvironmentModelFromSetup()
	 */
	@Override
	public EnvironmentModel getEnvironmentModelFromSetup() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_GET_ENVIRONMENT_MODEL_FROM_SETUP, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if ((result!=null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (EnvironmentModel) result;
			
		} catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#setEnvironmentModel(agentgui.simulationService.environment.EnvironmentModel)
	 */
	@Override
	public void setEnvironmentModel(EnvironmentModel envModel, boolean notifySensorAgents) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_ENVIRONMENT_MODEL, SimulationService.NAME, null);
			cmd.addParam(envModel);
			cmd.addParam(notifySensorAgents);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			
		} catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}			
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#stepSimulation(agentgui.simulationService.environment.EnvironmentModel, boolean)
	 */
	@Override
	public void stepSimulation(EnvironmentModel envModel, boolean aSynchron) throws IMTPException {

		try {
			// --- Notify the LoadService -----------------
			GenericCommand cmd = new GenericCommand(SIM_STEP_SIMULATION, LoadService.NAME, null);
			getNode().accept(cmd);

			// --- Notify the SimulationService -----------
			cmd = new GenericCommand(SIM_STEP_SIMULATION, SimulationService.NAME, null);
			cmd.addParam(envModel);
			cmd.addParam(aSynchron);
			
			Object result = getNode().accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#setAnswersExpected(int)
	 */
	@Override
	public void setAnswersExpected(int answersExpected) throws IMTPException {
	
		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_ANSWERS_EXPECTED, SimulationService.NAME, null);
			cmd.addParam(answersExpected);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#notifyAgent(jade.core.AID, agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	public boolean notifyAgent(AID agentAID, EnvironmentNotification notification) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_NOTIFY_AGENT, SimulationService.NAME, null);
			cmd.addParam(agentAID);
			cmd.addParam(notification);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Boolean) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#setPauseSimulation(boolean)
	 */
	@Override
	public void setPauseSimulation(boolean pauseSimulation) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_PAUSE_SIMULATION, SimulationService.NAME, null);
			cmd.addParam(pauseSimulation);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#setEnvironmentInstanceNextPart(java.util.Hashtable)
	 */
	@Override
	public void setEnvironmentInstanceNextPart(Hashtable<AID, Object> nextPartsLocal) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_ENVIRONMENT_NEXT_PART, SimulationService.NAME, null);
			cmd.addParam(nextPartsLocal);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#notifyManager(agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	public boolean notifyManager(EnvironmentNotification notification) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_NOTIFY_MANAGER, SimulationService.NAME, null);
			cmd.addParam(notification);

			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Boolean) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#notifyManagerPutAgentAnswers(java.util.Hashtable)
	 */
	@Override
	public void notifyManagerPutAgentAnswers(Hashtable<AID, Object> allAgentAnswers) throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(SIM_NOTIFY_MANAGER_PUT_AGENT_ANSWERS, SimulationService.NAME, null);
			cmd.addParam(allAgentAnswers);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#getEnvironmentInstanceNextParts()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Hashtable<AID, Object> getEnvironmentInstanceNextParts() throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(SIM_GET_ENVIRONMENT_NEXT_PARTS, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			Hashtable<AID, Object> nextParts = (Hashtable<AID, Object>) result;
			return nextParts;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#resetEnvironmentInstanceNextParts()
	 */
	@Override
	public void resetEnvironmentInstanceNextParts() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_RESET_ENVIRONMENT_NEXT_PARTS, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	// ----------------------------------------------------------
	// --- Methods on the Environment --- E N D -----------------
	// ----------------------------------------------------------
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#stopSimulationAgents()
	 */
	@Override
	public void stopSimulationAgents() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_STOP_SIMULATION_AGENTS, SimulationService.NAME, null);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#setAgentMigration(java.util.Vector)
	 */
	@Override
	public void setAgentMigration(Vector<AID_Container> transferAgents) throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SET_AGENT_MIGRATION, SimulationService.NAME, null);
			cmd.addParam(transferAgents);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}			
	}
	// ----------------------------------------------------------
	// --- Method to get the Load-Informations of all ----------- 
	// --- containers ----------------------------- E N D -------
	// ----------------------------------------------------------

	// ----------------------------------------------------------
	// --- Methods for updating display agents ------------------
	// ----------------------------------------------------------
	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#displayAgentNotify(agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	public void displayAgentSetEnvironmentModel(EnvironmentModel envModel) throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_DISPLAY_AGENT_SET_ENVIRONMENT_MODEL, SimulationService.NAME, null);
			cmd.addParam(envModel);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			
		} catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}		
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#displayAgentNotification(agentgui.simulationService.transaction.EnvironmentNotification)
	 */
	@Override
	public void displayAgentNotification(EnvironmentNotification notification) throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_DISPLAY_AGENT_NOTIFICATION, SimulationService.NAME, null);
			cmd.addParam(notification);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			
		} catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}	
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.SimulationServiceSlice#doDisplayAgentContainerUnRegister(java.lang.String, boolean)
	 */
	@Override
	public void doDisplayAgentContainerUnRegister(String containerName, boolean isRegisterContainer) throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_DISPLAY_CONTAINER_UN_REGISTRATION, SimulationService.NAME, null);
			cmd.addParam(containerName);
			cmd.addParam(isRegisterContainer);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			
		} catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}	
	}

}
