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


public class SimulationServiceProxy extends SliceProxy implements SimulationServiceSlice {

	private static final long serialVersionUID = -7016240061703852319L;

	// ----------------------------------------------------------
	// --- Methods to synchronise the Time --- S T A R T --------
	// ----------------------------------------------------------
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
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
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
	@Override
	public void setEnvironmentModel(EnvironmentModel envModel) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_ENVIRONMENT_MODEL, SimulationService.NAME, null);
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
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}			
	}
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
	
}
