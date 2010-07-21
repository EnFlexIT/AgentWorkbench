package mas.service;

import jade.core.AID;
import jade.core.Agent;
import jade.core.BaseService;
import jade.core.Profile;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

import java.util.Date;
import java.util.Hashtable;

import mas.service.time.TimeModel;
import mas.service.time.TimeModelDiscrete;
import mas.service.time.TimeModelStroke;

/**
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class AgentGUIService extends BaseService {

	public static final String NAME = AgentGUIService.class.getPackage().getName() + ".AgentGUI";
	private AgentGUIServiceHelper helper = new AgentGUIImpl();
	
	// --- The List of Agents, which are registered to this service ----------- 
	private Hashtable<String,AID> agentList = new Hashtable<String,AID>();
	
	// --- The current TimeModel ----------------------------------------------
	private TimeModel timeModel = null;
	
	
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
			// --- Store the Agent in the agentList -----------------
			agentList.put(ag.getName(), ag.getAID());			
		}

		public void setTimeModel(TimeModel newTimeModel) {
			timeModel = newTimeModel;
		}
		public TimeModel getTimeModel() {
			return timeModel;
		}
		public void stepTimeModel() {
			// --- Fallunterscheidung nach TimeModel ----------------
			switch (timeModel.typeOfTimeModel) {
			case TimeModel.STROKE:
				TimeModelStroke tms = (TimeModelStroke) timeModel;
				tms.step();
				break;
			case TimeModel.DISCRETE_TIME:
				TimeModelDiscrete tmd = (TimeModelDiscrete) timeModel;
				tmd.step();
				break;
			}
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
