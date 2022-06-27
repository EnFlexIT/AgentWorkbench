package de.enflexit.awb.samples.plugIn;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * The Class MyExampleAgent is just an empty agent.
 */
public class MyExampleAgent extends Agent {

	private static final long serialVersionUID = -9151847508178320798L;

	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new SpeakBehaviour(this, 1000));
	}

	private class SpeakBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = -8499240481779430976L;
		
		private int counter = 0;
		
		/**
		 * Instantiates a new speak behaviour.
		 *
		 * @param agent the agent
		 * @param period the period
		 */
		public SpeakBehaviour(Agent agent, long period) {
			super(agent, period);
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {
			this.counter++;
			if (this.counter==1) {
				System.out.println("=> " + this.myAgent.getLocalName() + " says 'Helo' the first time!");
			} else {
				System.out.println("=> " + this.myAgent.getLocalName() + " says 'Helo' the " + this.counter + ". time!");
			}
		}
		
	}
	
}
