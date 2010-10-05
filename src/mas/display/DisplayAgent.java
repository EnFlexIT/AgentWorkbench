package mas.display;

import javax.swing.JFrame;

import mas.environment.provider.EnvironmentProviderHelper;
import mas.environment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;

public class DisplayAgent extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8613715346940866246L;

	private DisplayAgentGUI myGUI = null;
	
	public void setup(){
		try {
			EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
			helper.getEnvironment();
			helper.getSVGDoc();
			this.myGUI = new DisplayAgentGUI(helper.getSVGDoc(), helper.getEnvironment());
			
			JFrame frame = new JFrame();
			frame.setTitle("DisplayAgent "+getLocalName()+" - GUI");
			frame.setContentPane(myGUI);
			frame.pack();
			frame.setVisible(true);
			
		} catch (ServiceException e) {
			System.err.println(getLocalName()+" - Error: Could not retrieve EnvironmentProviderHelper, shutting down!");
			doDelete();
		}
	}

}
