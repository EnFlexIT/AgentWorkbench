package de.enflexit.awb.simulation.distribution;

import de.enflexit.awb.simulation.ontology.RemoteContainerConfig;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

/**
 * The Class JadeRemoteStartAgent.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeRemoteStartAgent extends Agent {

	private static final long serialVersionUID = -880859677817836126L;

	private RemoteContainerConfig remoteContainerConfig;
	private JadeRemoteStartConfiguration remoteStartConfiguration;
	
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {

		Object[] startArg = this.getArguments();
		if (startArg!=null && startArg.length>0) {
			if (startArg[0] instanceof RemoteContainerConfig) {
				// --- Start the remote container ---------
				this.remoteContainerConfig = (RemoteContainerConfig) startArg[0];
				this.addBehaviour(new JadeRemoteStartBehaviour(this));

			} else if (startArg[0] instanceof JadeRemoteStartConfiguration) {
				// --- Retry to start remote container ----
				this.remoteStartConfiguration = (JadeRemoteStartConfiguration) startArg[0];
				this.addBehaviour(new JadeRemoteReStartBehaviour(this));
			}
		}
	}
	
	/**
	 * The Class JadeRemoteStartBehaviour is used in case that a remote container
	 * start was requested. It will organize the download of the required resources
	 * and starts the container if everything  was done successfully.
	 */
	private class JadeRemoteStartBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = 5986902939912984745L;

		/**
		 * Instantiates a new jade remote start.
		 * @param myAgent the my agent
		 */
		public JadeRemoteStartBehaviour(Agent myAgent) {
			this.setAgent(myAgent);
		}
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			// --- Initiate the starter -----------------------------
			JadeRemoteStart jrs = new JadeRemoteStart(remoteContainerConfig);
			System.out.println("[" + jrs.getClass().getSimpleName() + "] Prepare for remote container start ... ");
			if (jrs.isReadyToStartRemoteContainer()==true) {
				// --- Start the remote container -------------------
				System.out.println("[" + jrs.getClass().getSimpleName() + "] Starting remote container ... ");
				jrs.startJade();
			}
			// +++ Returns here after container shutdown ++++++++++++
			this.getAgent().doDelete();
		}
	}

	/**
	 * The Class JadeRemoteStartBehaviour is used in case that a remote container
	 * start was requested. It will organize the download of the required resources
	 * and starts the container if everything  was done successfully.
	 */
	private class JadeRemoteReStartBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = 5986902939912984745L;

		/**
		 * Instantiates a new jade remote restart.
		 * @param myAgent the my agent
		 */
		public JadeRemoteReStartBehaviour(Agent myAgent) {
			this.setAgent(myAgent);
		}
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			// --- Initiate the starter -----------------------------
			JadeRemoteStart jrs = new JadeRemoteStart(remoteStartConfiguration);
			System.out.println("[" + jrs.getClass().getSimpleName() + "] Re-Prepare for remote container start ... ");
			if (jrs.isReadyToStartRemoteContainer()==true) {
				// --- Start the remote container -------------------
				System.out.println("[" + jrs.getClass().getSimpleName() + "] Starting remote container ... ");
				jrs.startJade();
			}
			// +++ Returns here after container shutdown ++++++++++++
			this.getAgent().doDelete();
		}
	}

	
}
