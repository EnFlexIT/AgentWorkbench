package mas.display;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import javax.swing.JFrame;

import mas.environment.ontology.Physical2DObject;
import mas.environment.provider.EnvironmentProviderHelper;
import mas.environment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;
/**
 * This type of agent controls a visualization of a Physical2DEnvironment 
 * @author Nils
 *
 */
public class DisplayAgent extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8613715346940866246L;
	
	private static final int PERIOD = 50;
	/**
	 * The DisplayAgent's GUI
	 */
	private DisplayAgentGUI myGUI = null;
	
	private JFrame frame = null;
	
	public void setup(){
		try {
			EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
			this.myGUI = new DisplayAgentGUI(helper.getSVGDoc(), helper.getEnvironment());
			
			frame = new JFrame();
			frame.setTitle("DisplayAgent "+getLocalName()+" - GUI");
			frame.setContentPane(myGUI);
			frame.pack();
			frame.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent we){
					DisplayAgent.this.doDelete();
				}
			});
			
			frame.setVisible(true);

			addBehaviour(new UpdateSVGBehaviour());
			
		} catch (ServiceException e) {
			System.err.println(getLocalName()+" - Error: Could not retrieve EnvironmentProviderHelper, shutting down!");
			doDelete();
		}
	}
	
	public void takeDown(){
		if(frame != null){
			frame.dispose();
		}
	}
	
	private class UpdateSVGBehaviour extends TickerBehaviour{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4205541330627054644L;
		private EnvironmentProviderHelper helper;

		public UpdateSVGBehaviour() {
			super(DisplayAgent.this, PERIOD);
			try {
				helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				System.err.println(getLocalName()+" - EnvironmentProviderHelper not found, shutting down");
				doDelete();
			}
			
		}

		@Override
		protected void onTick() {
			HashSet<Physical2DObject> movingObjects = helper.getCurrentlyMovingObjects();
			myGUI.updatePositions(movingObjects);
		}
		
	}

}
