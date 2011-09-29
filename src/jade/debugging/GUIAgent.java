package jade.debugging;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.debugging.components.JFrame4Consoles;

public class GUIAgent extends Agent {

	private static final long serialVersionUID = -1267455969879282083L;

	@Override
	protected void setup() {
	
		try {
			DebugServiceHelper dh = (DebugServiceHelper) getHelper(DebugService.NAME);
			JFrame4Consoles displayFrame = dh.getRemoteConsolesDisplay();
			if (displayFrame!=null) {
				displayFrame.setVisible(true);	
			} else {
				System.out.println(this.getLocalName() + ": Found no JFrame for remote consoles"  );
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		this.doDelete();
	}
	
	
}
