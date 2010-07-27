/**
 * 
 */
package mas.service;

import jade.core.ServiceException;
import jade.core.ServiceHelper;

import java.util.Date;

import mas.service.time.TimeModel;

/**
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface SimulationServiceHelper extends ServiceHelper {

	public static final String SERVICE_NAME = "mas.service.SimulationService";
	
	public void setTimeModel(TimeModel newTimeModel) throws ServiceException;
	public TimeModel getTimeModel() throws ServiceException;		
	public void stepTimeModel() throws ServiceException;
	
	public Date getWorldTimeLocalAsDate();
	public Long getWorldTimeLocalAsLong();
	
	
}
