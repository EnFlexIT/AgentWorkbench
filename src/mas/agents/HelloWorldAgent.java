package mas.agents;

import java.util.Date;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import mas.service.AgentGUIService;
import mas.service.AgentGUIServiceHelper;
import mas.service.time.TimeModel;
import mas.service.time.TimeModelDiscrete;
import mas.service.time.TimeModelStroke;
/**
 * @version 1.0
 */ 
public class HelloWorldAgent extends Agent { 

	private static final long serialVersionUID = 1L;
	
	private AgentGUIServiceHelper agentGUIHelper = null;
	
	protected void setup() { 

		//System.out.println("Local - Name:" + getAID().getLocalName() );
		//System.out.println("GUID - Name:" + getAID().getName() );
		try {
			agentGUIHelper = (AgentGUIServiceHelper) getHelper(AgentGUIService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		Long stepInTime = new Long(1000);
		TimeModelDiscrete tmd = new TimeModelDiscrete(stepInTime);
		agentGUIHelper.setTimeModel(tmd);
		
		// ---- Add Cyclic Behaviour -----------
		this.addBehaviour(new HelloBehaviour());
		
	} 

	class HelloBehaviour extends CyclicBehaviour { 

		private static final long serialVersionUID = 1L;
		private Integer loop = 0;
		
		public void action() { 

			loop++;
			agentGUIHelper.stepTimeModel();
			TimeModelDiscrete tmd = (TimeModelDiscrete) agentGUIHelper.getTimeModel();
			Date date = new Date(tmd.getTime());
			System.out.println( "Loop: " + loop + " " + date );
			
			
		} 
	} 

} 
