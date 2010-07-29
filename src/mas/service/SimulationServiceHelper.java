/**
 * 
 */
package mas.service;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

import java.util.Date;

import mas.service.time.TimeModel;

/**
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface SimulationServiceHelper extends ServiceHelper {

	public static final String SERVICE_NAME = "mas.service.SimulationService";
	
	public static final String SERVICE_UPDATE_TIME_MODEL = "service-update-time-model";
	public static final String SERVICE_UPDATE_TIME_STEP = "service-update-time-step";
	public static final String SERVICE_UPDATE_ENVIRONMENT = "service-update-environment";
	public static final String SERVICE_UPDATE_SIMULATION = "service-update-simulation";
	
	public void notifySensors(String event) throws ServiceException;
	
	public void addSensor(Agent agentWithSensor) throws ServiceException;
	public void deleteSensor(Agent agentWithSensor) throws ServiceException;
	
	public void setManagerAgent(AID agentAddress) throws ServiceException; 
	public AID getManagerAgent() throws ServiceException;
	
	public void stepSimulation(Object envObjectInstance) throws ServiceException;
	
	public void setTimeModel(TimeModel newTimeModel) throws ServiceException;
	public TimeModel getTimeModel() throws ServiceException;		
	public void stepTimeModel() throws ServiceException;
	
	public void setEnvironmentInstance(Object envObjectInstance) throws ServiceException;
	public Object getEnvironmentInstance() throws ServiceException;
	
	public Date getTimeOfMainContainerAsDate();
	public Long getTimeOfMainContainerAsLong();
	
	
}
