package de.enflexit.awb.bgSystem;

import org.osgi.framework.BundleContext;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.jade.Platform.SystemAgent;

/**
 * The Class BundleActivator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BundleActivator implements org.osgi.framework.BundleActivator {

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		
		// --- Register server.master & server.slave as system agents ---------
		Application.getJadePlatform().getSystemAgentClasses().put(SystemAgent.BackgroundSystemAgentServerMaster, de.enflexit.awb.bgSystem.agents.ServerMasterAgent.class.getName());
		Application.getJadePlatform().getSystemAgentClasses().put(SystemAgent.BackgroundSystemAgentServerSlave, de.enflexit.awb.bgSystem.agents.ServerSlaveAgent.class.getName());
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
