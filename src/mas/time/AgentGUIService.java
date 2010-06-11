package mas.time;

import java.util.Date;

import jade.core.Agent;
import jade.core.BaseService;
import jade.core.Profile;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

/**
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class AgentGUIService extends BaseService {

	public static final String NAME = AgentGUIService.class.getPackage().getName() + ".AgentGUI";
	private AgentGUIServiceHelper helper = new AgentGUIImpl();
	
	
	
	@Override
	public String getName() {
		return NAME;
	}

	public void boot(Profile p) throws ServiceException {
		super.boot(p);
		System.out.println( NAME + " was started!");
		
	}
	public ServiceHelper getHelper (Agent ag) {
		return helper;
	}

	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'AgentTimeImpl' ---- Start ------------------
	// --------------------------------------------------------------
	/**
	 * Sub-Class to provide interaction between Agents and this Service
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
	 */
	public class AgentGUIImpl implements AgentGUIServiceHelper {

		public void init(Agent ag) {
			// --- Empty that time ---
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
	
}
