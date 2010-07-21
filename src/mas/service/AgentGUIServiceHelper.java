/**
 * 
 */
package mas.service;

import java.util.Date;

import mas.service.time.TimeModel;

import jade.core.ServiceHelper;

/**
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface AgentGUIServiceHelper extends ServiceHelper {

	public void setTimeModel(TimeModel newTimeModel);
	public TimeModel getTimeModel();		
	public void stepTimeModel();
	
	public Date getWorldTimeLocalAsDate();
	public Long getWorldTimeLocalAsLong();
	
	
}
