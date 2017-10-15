/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.update;

import java.net.URI;
import javax.swing.JOptionPane;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.RepositoryTracker;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.ui.ProvisioningUI;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.ExecutionEnvironment;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.gui.options.UpdateOptions;

/**
 * The Class AgentGuiUpdater.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentGuiUpdater extends Thread {

	// --- For the debugging of this class set true -------------
	private boolean debuggingInIDE = false;
	
	public static final long UPDATE_CHECK_PERIOD = 1000*60*60*24; 	// - once a day -
	public static final String UPDATE_SUB_FOLDER = "updates"; 		// - subfolder in the web server directory -
	public static final String UPDATE_VERSION_INFO_FILE = "latestVersion.xml";
	
	private GlobalInfo globalInfo;
	private ExecutionMode executionMode;
	
	private Integer updateAutoConfiguration;
	private long updateDateLastChecked = 0;
	
	
	private String alternativeInfoLink;
	
	private boolean manualyExecutedByUser = false;
	private boolean doUpdateProcedure = true;
	private boolean askBeforeDownload = false;
	/**
	 * Instantiates a new Agent.GUI updater process.
	 */
	public AgentGuiUpdater() {
		this.initialize();
	}
	/**
	 * Instantiates a new Agent.GUI updater process.
	 * @param userExecuted indicates that execution was manually chosen by user
	 */
	public AgentGuiUpdater(boolean userExecuted) {
		this.manualyExecutedByUser = userExecuted;
		this.initialize();
	}
	/**
	 * Instantiates a new Agent.GUI updater process.
	 * @param userExecuted indicates that execution was manually chosen by user
	 * @param alternativeInfoLink the alternative download link for the update
	 */
	public AgentGuiUpdater(boolean userExecuted, String alternativeInfoLink) {
		this.manualyExecutedByUser = userExecuted;
		this.alternativeInfoLink = alternativeInfoLink;
		this.initialize();
	}
	
	/**
	 * Initialize and set needed local variables.
	 */
	private void initialize() {
		
		this.setName("Agent.GUI-Updater");
		
		this.globalInfo = Application.getGlobalInfo();
		this.globalInfo.getVersionInfo();
		this.executionMode = this.globalInfo.getExecutionMode();

		this.globalInfo.getUpdateSite();
		this.updateAutoConfiguration = this.globalInfo.getUpdateAutoConfiguration();
		this.globalInfo.getUpdateKeepDictionary();
		this.updateDateLastChecked = this.globalInfo.getUpdateDateLastChecked();
		
		this.globalInfo.getPathDownloads();
		this.globalInfo.getPathWebServer();
		this.globalInfo.getPathProperty(true);
		
		this.setUpdateConfiguration();
	}
	
	/**
	 * Sets the update configuration.
	 */
	private void setUpdateConfiguration() {
		
		if (this.alternativeInfoLink==null && this.manualyExecutedByUser==false) {
			long timeNow = System.currentTimeMillis();
			long time4NextCheck = this.updateDateLastChecked + AgentGuiUpdater.UPDATE_CHECK_PERIOD;
			if (timeNow<time4NextCheck) {
				doUpdateProcedure=false;
			} else {
				Application.getGlobalInfo().setUpdateDateLastChecked(timeNow);
				Application.getGlobalInfo().doSavePersistedConfiguration();
			}
		}
		
		switch (this.executionMode) {
		case APPLICATION:
			// --------------------------------------------
			switch (this.updateAutoConfiguration) {
			case UpdateOptions.UPDATE_MODE_AUTOMATIC:
				this.askBeforeDownload=false;
				break;
			case UpdateOptions.UPDATE_MODE_ASK:
				this.askBeforeDownload=true;
				break;
			default:
				doUpdateProcedure = false;
				break;
			}
			// --------------------------------------------
			break;
			
		case SERVER:
			// --------------------------------------------
			doUpdateProcedure = false;
			// --------------------------------------------
			break;
			
		case SERVER_MASTER:
			// --------------------------------------------
			switch (this.updateAutoConfiguration) {
			case UpdateOptions.UPDATE_MODE_AUTOMATIC:
				this.askBeforeDownload=false;
				break;
			default:
				doUpdateProcedure = false;
				break;
			}
			// --------------------------------------------
			break;
			
		case SERVER_SLAVE:
			// --------------------------------------------
			switch (this.updateAutoConfiguration) {
			case UpdateOptions.UPDATE_MODE_AUTOMATIC:
				this.askBeforeDownload=false;
				break;
			default:
				doUpdateProcedure = false;
				break;
			}
			// --------------------------------------------
			break;
			
		case DEVICE_SYSTEM:
			// --- TODO -------------
			break;
		}
		
		// ------------------------------------------------
		// --- Manual execution? --------------------------
		if (this.manualyExecutedByUser==true) {
//			this.doUpdateProcedure=true;
//			this.askBeforeDownload=true;
		}
		
		// ------------------------------------------------
		// --- Execution out of the IDE? ------------------
		if (this.globalInfo.getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE) {
			this.doUpdateProcedure=false; // set to 'true' for further developments of the AgentGuiUpdater class 
			this.askBeforeDownload=true;
			System.out.println("Agent.GUI-Update: No updates in the IDE environment available.");
		}
		
		// ------------------------------------------------
		// --- Currently debugging this class ? -----------
		if (this.debuggingInIDE==true) {
			this.doUpdateProcedure=true;
			this.askBeforeDownload=true;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		if (this.doUpdateProcedure==false) {
			// --- No download, no update --------------------------- 
			return;
		}
		
		// --- If running, wait for the end of the benchmark --------
		this.waitForTheEndOfBenchmark();
		
		// --- P2-based headless update ---------------
		checkForP2Updates();

	}
	
	/**
	 * Wait for benchmark.
	 */
	private void waitForTheEndOfBenchmark() {
		while (Application.isBenchmarkRunning()==true) {
			try {
				sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	// ------------------------------------
	// --- p2-based update ----------------
	// ------------------------------------
	
	/**
	 * Checks for available p2 updates, starts the update process if found.
	 */
	public void checkForP2Updates() {
		
		// --- Some initializations -----------
		UpdateOperation operation = getUpdateOperation();
		SubMonitor subMonitor = SubMonitor.convert(getProgressMonitor(), "Checking for Updates...", 200);

		// --- Check for available updates -------
		IStatus status = operation.resolveModal(subMonitor.newChild(100));
		 
		if(status.getSeverity() != IStatus.ERROR) {

			if(status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
				// --- No updates found --------------
				System.out.println("P2 Update: No updates found!");
			}else{
				// --- Updates found, proceed --------
				System.out.println("P2 Update: Updates available.");
				performP2Update(operation, subMonitor);
			}
		}
	}
	
	
	/**
	 * Checks if the updates should be installed, triggers the actual installation if so
	 * @param operation the {@link UpdateOperation} to be used
	 * @param subMonitor the {@link SubMonitor} to be used
	 */
	private void performP2Update(UpdateOperation operation, SubMonitor subMonitor) {
		
		// --- Default - install available updates without asking -----------
		boolean installUpdates = true;

		// --- Ask for user confirmation if specified in the settings -------
		if (this.askBeforeDownload == true) {
			int userAnswer = JOptionPane.showConfirmDialog(null, Language.translate("Updates verfügbar, installieren?"), "Agent.GUI Update", JOptionPane.YES_NO_OPTION);
			if (userAnswer == JOptionPane.NO_OPTION) {
				installUpdates = false;
			}
		}
		
		
		if (installUpdates == true) {

			// --- Install updates ------------------
			IStatus updateResult = installP2Updates(operation, subMonitor.newChild(100));

			// --- Restart if successful ------------
			if (updateResult.getSeverity() == IStatus.OK) {
				System.out.println("P2 Update: Updates sucessfully installed, restarting...");
				Application.restart();
			} else {
				System.err.println("P2 Update: Error installing updates...");
			}
			
		}
	}
	
	/**
	 * Installs the updates.
	 * @param operation the {@link UpdateOperation} to be used
	 * @param monitor the monitor The progress monitor to be used
	 * @return the return status
	 */
	private static IStatus installP2Updates(UpdateOperation operation, IProgressMonitor monitor) {
		System.out.println("P2 update: Installing updates...");
		
		// --- Initialize the provisioning job -------
		ProvisioningJob job = operation.getProvisioningJob(null);
		if (job == null) {
	        System.err.println("Trying to update from the Eclipse IDE? This won't work!");
	        return Status.CANCEL_STATUS;
	    }
		
		// --- Run the update job --------------
		IStatus status = job.runModal(monitor);
		if(status.getSeverity() == IStatus.CANCEL) {
			throw new OperationCanceledException();
		}
		return status;
	}
	
	/**
	 * Gets the provisioning agent for the update process
	 * @return The provisioning agent
	 */
	private static IProvisioningAgent getProvisioningAgent() {
		
		BundleContext bundleContext = FrameworkUtil.getBundle(AgentGuiUpdater.class).getBundleContext();
		ServiceReference<?> serviceReference = bundleContext.getServiceReference(IProvisioningAgent.SERVICE_NAME);
		
		if(serviceReference == null) {
			return null;
		}
		
		IProvisioningAgent agent = (IProvisioningAgent) bundleContext.getService(serviceReference);
		return agent;
		
	}
	
	/**
	 * Gets a progress monitor to be used for the update process
	 * @return The progress monitor
	 */
	private static IProgressMonitor getProgressMonitor() {
		//TODO Add a case for non-headless execution providing a progress visualization
		return new NullProgressMonitor();
	}
	
	/**
	 * Create and initialize the {@link UpdateOperation}
	 * @param session the {@link ProvisioningSession} for this {@link UpdateOperation}
	 * @return The {@link UpdateOperation}
	 */
	private static UpdateOperation getUpdateOperation() {
		
		IProvisioningAgent provisioningAgent = getProvisioningAgent();
		ProvisioningSession provisioningSession = new ProvisioningSession(provisioningAgent);
		
		UpdateOperation operation = new UpdateOperation(provisioningSession);
		
		RepositoryTracker repoTracker = ProvisioningUI.getDefaultUI().getRepositoryTracker();
		URI[] repos = repoTracker.getKnownRepositories(provisioningSession);
		
		ProvisioningContext pc = operation.getProvisioningContext();
		
		pc.setArtifactRepositories(repos);
		pc.setMetadataRepositories(repos);
		
		return operation;
	}
	
}
