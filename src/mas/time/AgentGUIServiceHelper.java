/**
 * 
 */
package mas.time;

import java.util.Date;

import jade.core.ServiceHelper;

/**
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface AgentGUIServiceHelper extends ServiceHelper {

	public Date getWorldTimeLocalAsDate();
	public Long getWorldTimeLocalAsLong();
	
	
}
