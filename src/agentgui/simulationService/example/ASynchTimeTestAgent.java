package agentgui.simulationService.example;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;


/**
 * @version 1.0
 */ 
public class ASynchTimeTestAgent extends SimulationAgent { 

	private static final long serialVersionUID = 1L;
	
	private String myName = null;
	private SynchTimeGUI gui = null;
	private Date curSynchDate = null;
	private long currSynchDiff = 0;

	protected void setup() { 
		
		this.myName = this.getLocalName();
		
		SimulationServiceHelper simHelper = null;
		// --- Setup the Simulation with the Simulation-Service ------------
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			if (simHelper.getManagerAgent()==null) {
				simHelper.setManagerAgent(this.getAID());
				// --- Start a Ticker -----------------------------
				this.addBehaviour(new ShowTimeBehaviour(this,1000));

			} else {
				// --- Wait for the Service-Trigger ---------------
				this.sensorPlugIn();
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		// --- Start and show the GUI ---------------------
		this.startGUI();
		
	} 
	
	protected void takeDown() {
		super.takeDown();
		stopGUI();
	}
	protected void beforeMove() {
		super.beforeMove();
		stopGUI();
	}
	protected void afterMove() {
		super.afterMove();
		startGUI();
		
		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			if (simHelper.getManagerAgent()==null) {
				simHelper.setManagerAgent(this.getAID());
				// --- Start a Ticker -----------------------------
				this.addBehaviour(new ShowTimeBehaviour(this,1000));
			} 	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	
	private void startGUI(){
		gui = new SynchTimeGUI(null);
		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			// --- Open the GUI ------------------------------------------------------------
			curSynchDate = simHelper.getSynchTimeDate();
			currSynchDiff = simHelper.getSynchTimeDifferenceMillis();
			gui.setTime(curSynchDate, currSynchDiff);
			gui.setVisible(true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	private void stopGUI() {
		if (gui!=null){
			gui.setVisible(false);
			gui = null;
		}
	}
	
	class ShowTimeBehaviour extends TickerBehaviour { 

		private static final long serialVersionUID = 1L;
		public ShowTimeBehaviour(Agent a, long period) {
			super(a, period);
		}
		
		protected void onTick() {
			SimulationServiceHelper simHelper = null;
			try {
				simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				curSynchDate = simHelper.getSynchTimeDate();
				currSynchDiff = simHelper.getSynchTimeDifferenceMillis();
				simHelper.stepSimulation(null, 0, true);				
				if (gui!=null) {
					gui.setTime(curSynchDate, currSynchDiff);	
				}
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		} 

	}

	@Override
	protected void onEnvironmentStimulus() {
		
		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			curSynchDate = simHelper.getSynchTimeDate();
			currSynchDiff = simHelper.getSynchTimeDifferenceMillis();
			if (gui!=null) {
				gui.setTime(curSynchDate, currSynchDiff);	
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	

	public class SynchTimeGUI extends JDialog {

		private static final long serialVersionUID = 1L;
		private JPanel jContentPane = null;
		
		private JLabel jLabelTime = null;
		private JLabel jLabelTimeLocal = null;
		private JLabel jLabelTimeDiff = null;
		
		private JLabel jLabelTimeLocalCaption = null;
		private JLabel jLabelTimeCaption = null;
		private JLabel jLabelTimeDiffCaption = null;
		
		
		public void setTime(Date currSynchTime, Long difference) {
			SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yy HH:mm:ss.S" );
			jLabelTimeLocal.setText(df.format(new Date(System.currentTimeMillis())));
			jLabelTime.setText(df.format(currSynchTime));
			jLabelTimeDiff.setText(difference.toString());
		}
		/**
		 * @param owner
		 */
		public SynchTimeGUI(Frame owner) {
			super(owner);
			initialize();
		}
		/**
		 * This method initializes this
		 * @return void
		 */
		private void initialize() {
			this.setSize(397, 191);
			this.setTitle("'" + myName + "' - AgentGUI: Container-Synch-Time-Example");
			this.setContentPane(getJContentPane());
			this.setAlwaysOnTop(true);
		}
		/**
		 * This method initializes jContentPane
		 * @return javax.swing.JPanel
		 */
		private JPanel getJContentPane() {
			if (jContentPane == null) {
				
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.gridx = 1;
				gridBagConstraints1.gridy = 0;
				gridBagConstraints1.ipadx = 200;
				gridBagConstraints1.ipady = 0;
				gridBagConstraints1.insets = new Insets(20, 20, 0, 0);
				gridBagConstraints1.fill = GridBagConstraints.NONE;
				gridBagConstraints1.weightx = 1.0;
				gridBagConstraints1.anchor = GridBagConstraints.WEST;
				

				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.gridx = 0;
				gridBagConstraints2.gridy = 0;
				gridBagConstraints2.anchor = GridBagConstraints.WEST;
				gridBagConstraints2.insets = new Insets(20, 20, 0, 0);
				

				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.gridx = 0;
				gridBagConstraints3.gridy = 1;
				gridBagConstraints3.anchor = GridBagConstraints.WEST;
				gridBagConstraints3.insets = new Insets(20, 20, 00, 0);
				
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 1;
				gridBagConstraints.gridy = 1;
				gridBagConstraints.ipadx = 200;
				gridBagConstraints.ipady = 0;
				gridBagConstraints.insets = new Insets(20, 20, 0, 0);
				gridBagConstraints.anchor = GridBagConstraints.WEST;
				
				GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
				gridBagConstraints4.gridx = 1;
				gridBagConstraints4.gridy = 2;
				//gridBagConstraints4.ipadx = 200;
				//gridBagConstraints4.ipady = 0;
				gridBagConstraints4.insets = new Insets(20, 20, 20, 0);
				gridBagConstraints4.anchor = GridBagConstraints.WEST;
				
				GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
				gridBagConstraints5.gridx = 0;
				gridBagConstraints5.gridy = 2;
				gridBagConstraints5.anchor = GridBagConstraints.WEST;
				gridBagConstraints5.insets = new Insets(20, 20, 20, 0);
				
				
				jLabelTimeCaption = new JLabel();
				jLabelTimeCaption.setText("Time synch:");
				jLabelTimeCaption.setFont(new Font("Dialog", Font.BOLD, 18));

				jLabelTime = new JLabel();
				jLabelTime.setText("...");
				jLabelTime.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelTime.setFont(new Font("Dialog", Font.BOLD, 18));

				jLabelTimeLocalCaption = new JLabel();
				jLabelTimeLocalCaption.setText("Time local:");
				jLabelTimeLocalCaption.setFont(new Font("Dialog", Font.BOLD, 18));
				
				jLabelTimeLocal = new JLabel();
				jLabelTimeLocal.setText("...");
				jLabelTimeLocal.setPreferredSize(new Dimension(15, 24));
				jLabelTimeLocal.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelTimeLocal.setFont(new Font("Dialog", Font.BOLD, 18));
				
				jLabelTimeDiffCaption = new JLabel();
				jLabelTimeDiffCaption.setText("Diff. [ms]:");
				jLabelTimeDiffCaption.setFont(new Font("Dialog", Font.BOLD, 14));
				
				jLabelTimeDiff = new JLabel();
				jLabelTimeDiff.setText("...");
				jLabelTimeDiff.setPreferredSize(new Dimension(15, 24));
				jLabelTimeDiff.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelTimeDiff.setFont(new Font("Dialog", Font.BOLD, 14));
				
				
				
				jContentPane = new JPanel();
				jContentPane.setFont(new Font("Dialog", Font.BOLD, 18));
				jContentPane.setLayout(new GridBagLayout());
				jContentPane.add(jLabelTime, gridBagConstraints);
				jContentPane.add(jLabelTimeCaption, gridBagConstraints3);
				jContentPane.add(jLabelTimeLocal, gridBagConstraints1);
				jContentPane.add(jLabelTimeLocalCaption, gridBagConstraints2);
				
				jContentPane.add(jLabelTimeDiffCaption, gridBagConstraints5);
				jContentPane.add(jLabelTimeDiff, gridBagConstraints4);
				
			}
			return jContentPane;
		}

	}
	
} 
