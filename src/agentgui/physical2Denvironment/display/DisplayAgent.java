package agentgui.physical2Denvironment.display;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.w3c.dom.Document;

import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
/**
 * This type of agent controls a visualization of a Physical2DEnvironment 
 * @author Nils
 *
 */
public class DisplayAgent extends Agent {
	
	private static final long serialVersionUID = 8613715346940866246L;
	
	private static final int PERIOD = 50;
	/**
	 * The DisplayAgent's GUI
	 */
	private DisplayAgentGUI myGUI = null;
	
	private JFrame useFrame = null;
	
	private JPanel usePanel = null;
	private Document svgDoc = null;
	private Physical2DEnvironment environment = null;
	
	
	public void setup(){
		
		int use4Visualization = 0;
		Object[] startArgs = getArguments();
		if (startArgs==null) {
			// --- started in a normal way ----------------  
			use4Visualization = 1;
			useFrame = getIndependentFrame();
			try {
				EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
				svgDoc = helper.getSVGDoc();
				environment = helper.getEnvironment();
			} catch (ServiceException e) {
				System.err.println(getLocalName()+" - Error: Could not retrieve EnvironmentProviderHelper, shutting down!");
				doDelete();
			}
			
		} else {
			// --- started from Agent.GUI -----------------
			use4Visualization = 2;
			usePanel = (JPanel) startArgs[0];
			svgDoc = (Document) startArgs[1];
			environment = (Physical2DEnvironment) startArgs[2];
		}

		// --- initiate the GUI - element -----------------
		this.myGUI = new DisplayAgentGUI(svgDoc, environment);

		// --- place it on right place --------------------
		switch (use4Visualization) {
		case 1:
			useFrame.setContentPane(myGUI);
			useFrame.setVisible(true);
			useFrame.pack();				
			break;

		case 2:
			usePanel.add(myGUI, BorderLayout.CENTER);
			usePanel.repaint();
			break;
		}
		
		addBehaviour(new UpdateSVGBehaviour());
	}
	
	public void takeDown(){
		if(useFrame != null){
			useFrame.dispose();
		}
		if (usePanel!=null) {
			usePanel.remove(myGUI);
		}
	}
	
	private JFrame getIndependentFrame() {
		
		JFrame frame = new JFrame();
		frame.setTitle("DisplayAgent "+getLocalName()+" - GUI");
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				DisplayAgent.this.doDelete();
			}
		});
		return frame;
	}
	
	private class UpdateSVGBehaviour extends TickerBehaviour{

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
