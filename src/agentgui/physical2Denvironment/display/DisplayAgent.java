package agentgui.physical2Denvironment.display;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.omg.CORBA.Environment;
import org.w3c.dom.Document;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
import agentgui.physical2Denvironment.utils.EnvironmentHelper;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelDiscrete;
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
	private EnvironmentProviderHelper envHelper = null;
	private int lastMaximumValue=-2;
	private int lastCurrentValue=-1;
	private long initialTimeStep=0;
	private boolean play=true;

	public void setup(){
	
		int use4Visualization = 0;
		Object[] startArgs = getArguments();
		if (startArgs==null || startArgs.length==0) {
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
			useFrame.setSize(800,600);
			useFrame.setVisible(true);
			useFrame.pack();				
			break;

		case 2:
			usePanel.add(myGUI, BorderLayout.CENTER);
			usePanel.repaint();
			break;
		}
		try
		{
			myGUI.jPanelSimuInfos.setVisible(true);
			SimulationServiceHelper simHelper=(SimulationServiceHelper) getHelper(SimulationServiceHelper.SERVICE_NAME);
			
			EnvironmentModel envModel= simHelper.getEnvironmentModel();
		
			while(envModel==null)
			{
				Thread.sleep(10);
				envModel= simHelper.getEnvironmentModel();
			}
			TimeModelDiscrete model=(TimeModelDiscrete) envModel.getTimeModel();
			initialTimeStep=model.getStep();	
			String unit= Language.translate("Sekunden");
			myGUI.jLabelSpeedFactor.setText( (initialTimeStep/1000.0)+" " + unit);
			System.out.println("Sollte eigentlich darstelle");
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		
		myGUI.jButtonPlay.addMouseListener(new java.awt.event.MouseAdapter() {
			
			public void mouseClicked(java.awt.event.MouseEvent e) {
				play=!play;
				envHelper.setRunning(play);
				String pathImage=Application.RunInfo.PathImageIntern();
				Icon icon=null;
				if(play)
				{
					 icon=new ImageIcon(getClass().getResource(pathImage + "MBLoadPlay.png"));
				}
				else
				{
					 icon=new ImageIcon(getClass().getResource(pathImage + "MBLoadPause.png"));		
				}
				myGUI.jButtonPlay.setIcon(icon);
			
			}
		});
		
		
		
		
				
		// the user change the status of the simulation
		myGUI.jSliderTime.addChangeListener(new javax.swing.event.ChangeListener() {
	
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				// Call simu service and tell him that something is changed!
;
				try
				{
					if(	envHelper!=null)
					{
						
						envHelper.setCurrentPos(myGUI.jSliderTime.getValue());
						if(initialTimeStep==0)
						{
							SimulationServiceHelper simHelper=(SimulationServiceHelper) getHelper(SimulationServiceHelper.SERVICE_NAME);
							TimeModelDiscrete model=(TimeModelDiscrete) simHelper.getEnvironmentModel().getTimeModel();
							initialTimeStep=model.getStep();	
							
						}
					
						String unit= Language.translate("Sekunden");
						myGUI.jLabelTimeDisplay.setText((initialTimeStep*myGUI.jSliderTime.getValue())/1000.0 +" "+unit);
					}
					else
					{
						envHelper=(EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		
		// User adjusted the simulation time steps
		myGUI.jSliderVisualation.addChangeListener(new javax.swing.event.ChangeListener() {
			SimulationServiceHelper simHelper=null;
			
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				// Call simu service and tell him that something is changed!
			
				try
				{
				
				if(simHelper==null)
				{
						simHelper=(SimulationServiceHelper) getHelper(SimulationServiceHelper.SERVICE_NAME);
						TimeModelDiscrete model=(TimeModelDiscrete) simHelper.getEnvironmentModel().getTimeModel();
						initialTimeStep=model.getStep();
				}
				String unit= Language.translate("Sekunden");
				myGUI.jLabelSpeedFactor.setText( (((myGUI.jSliderVisualation.getValue()*initialTimeStep))/1000.0) +" "+unit);
				TimeModelDiscrete model=(TimeModelDiscrete) simHelper.getEnvironmentModel().getTimeModel();
				model.setStep(initialTimeStep*myGUI.jSliderVisualation.getValue());
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		
		
		
		
		
		
		
		addBehaviour(new UpdateSVGBehaviour());
	}
	
	public void takeDown(){
		if(useFrame != null){
			useFrame.dispose();
		}
		if (usePanel!=null) {
			myGUI = null;
			usePanel.removeAll();
			usePanel.repaint();
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
				System.err.println(getLocalName()+" - EnvironmentProviderHelper not found, shutting down");
				doDelete();
			}
			
		}

		@Override
		protected void onTick() {
			try
			{
						
					HashSet<Physical2DObject> movingObjects = helper.getCurrentlyMovingObjects();
					synchronized (movingObjects) {
						myGUI.updatePositions(movingObjects);
					}
			
				if(envHelper!=null)
				{
					if(lastCurrentValue!=envHelper.getCurrentPos()) // only change if necessary
					{
						myGUI.setCurrentTimePos(envHelper.getCurrentPos());
					}
					if(lastMaximumValue!=envHelper.getTransactionSize())
					{
						myGUI.setMaximum(envHelper.getTransactionSize()); // Set the maximum sliding position
					}
			
				}
				else
				{
				envHelper=(EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);	
				}
				lastCurrentValue=envHelper.getCurrentPos(); // Use for comparing
				lastMaximumValue=envHelper.getTransactionSize(); // use for comparing
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
		
		
	}
}
}
