package de.enflexit.awb.core.update;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.core.ui.AgentWorkbenchUiManager;
import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.awb.core.ui.AwbProgressMonitor;
import de.enflexit.common.ExecutionEnvironment;
import de.enflexit.common.p2.P2OperationsHandler;
import de.enflexit.language.Language;

/**
 * The Class AWBUpdater provides a thread that can be used to update
 * Agent.Workbench and its features.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class AWBUpdater extends Thread {

	public static final long UPDATE_CHECK_PERIOD = 1000 * 60 * 60 * 24; // - once a day -
	
	public static final int UPDATE_MODE_AUTOMATIC = 0;
	public static final int UPDATE_MODE_ASK = 1;
	public static final int UPDATE_MODE_DISABLED = 2;
	
	// --- For the debugging of this class set true -------------
	private boolean debuggingInIDE = false;
	private boolean debug = false;
	private boolean updateDone = false;

	private GlobalInfo globalInfo;
	private ExecutionMode executionMode;

	private Integer updateAutoConfiguration;
	private long updateDateLastChecked = 0;

	private boolean manualyExecutedByUser = false;
	private boolean enforceUpdate = false;
	private boolean doUpdateProcedure = true;
	private boolean askBeforeDownload = false;
	
	private boolean doUpdateCheckOnly = false;
	
	private Object synchronizationObject;
	private AwbProgressMonitor progressMonitor;
	

	
	/**
	 * Instantiates a new Agent.Workbench updater process.
	 */
	public AWBUpdater() {
		this(false);
	}
	/**
	 * Instantiates a new Agent.Workbench updater process.
	 * @param userExecuted indicates that execution was manually chosen by user
	 */
	public AWBUpdater(boolean userExecuted) {
		this(userExecuted, false);
	}
	/**
	 * Instantiates a new Agent.Workbench updater process.
	 * @param userExecuted indicates that execution was manually chosen by user
	 * @param enforceUpdate the enforce update
	 */
	public AWBUpdater(boolean userExecuted, boolean enforceUpdate) {
		this.manualyExecutedByUser = userExecuted;
		this.enforceUpdate = enforceUpdate;
		
		this.initialize();
	}
	
	/**
	 * Initialize and set needed local variables.
	 */
	private void initialize() {
		
		this.synchronizationObject = new Object();

		this.setName(Application.getGlobalInfo().getApplicationTitle() + "-Updater");

		this.globalInfo = Application.getGlobalInfo();
		this.executionMode = this.globalInfo.getExecutionMode();
		this.updateAutoConfiguration = this.globalInfo.getUpdateAutoConfiguration();
		this.updateDateLastChecked = this.globalInfo.getUpdateDateLastChecked();

		this.setUpdateConfiguration();
	}

	/**
	 * Sets the update configuration.
	 */
	private void setUpdateConfiguration() {

		if (this.manualyExecutedByUser==false) {
			long timeNow = System.currentTimeMillis();
			long time4NextCheck = this.updateDateLastChecked + AWBUpdater.UPDATE_CHECK_PERIOD;
			if (this.enforceUpdate==false && timeNow < time4NextCheck) {
				this.doUpdateProcedure = false;
				return;
			} else {
				Application.getGlobalInfo().setUpdateDateLastChecked(timeNow);
				Application.getGlobalInfo().doSaveConfiguration();
			}
		}
		
		switch (this.executionMode) {
		case APPLICATION:
			// --------------------------------------------
			switch (this.updateAutoConfiguration) {
			case AWBUpdater.UPDATE_MODE_AUTOMATIC:
				this.askBeforeDownload = false;
				break;
			case AWBUpdater.UPDATE_MODE_ASK:
				this.askBeforeDownload = true;
				break;
			default:
				doUpdateProcedure = false;
				break;
			}
			// --------------------------------------------
			break;

		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
		case DEVICE_SYSTEM:
			// --------------------------------------------
			switch (this.updateAutoConfiguration) {
			case AWBUpdater.UPDATE_MODE_AUTOMATIC:
				this.askBeforeDownload = false;
				break;
			default:
				System.out.println("[" + this.getClass().getSimpleName() + "] Automatic updates are disabled.");
				doUpdateProcedure = false;
				break;
			}
			// --------------------------------------------
			break;
		}

		// ------------------------------------------------
		// --- Manual execution? --------------------------
		if (this.manualyExecutedByUser==true) {
			this.askBeforeDownload=true;
			this.doUpdateProcedure=true;
		}

		// ------------------------------------------------
		// --- Execution out of the IDE? ------------------
		if (this.globalInfo.getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE) {
			this.askBeforeDownload = true;
			this.doUpdateProcedure = false; // set to 'true' for further developments of the AgentGuiUpdater class
			this.doUpdateCheckOnly = true;
//			System.out.println("[" + this.getClass().getSimpleName() + "] P2 updates are not possible when starting from the IDE.");
			
		}

		// ------------------------------------------------
		// --- Currently debugging this class ? -----------
		if (this.debuggingInIDE == true) {
			this.doUpdateProcedure = true;
			this.askBeforeDownload = true;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		synchronized (this.getSynchronizationObject()) {
			
			if (this.doUpdateProcedure==true) {

				this.debugPrint("Doing full update procedure");
				// --- Wait for the end of the benchmark ------------
				this.waitForTheEndOfBenchmark();
				// --- Perform the update ---------------------------
				this.startP2Updates();
				
			} else if (this.doUpdateCheckOnly==true) {

				this.debugPrint("Doing update check only");
				// --- Wait for the end of the benchmark ------------
				this.waitForTheEndOfBenchmark();
				// --- Check the repo for newer versions ------------
				this.checkForAvailableUpdates();
			}
			
			// --- Notify waiting threads ---------------------------
			this.debugPrint("Update done, notifying");
			this.updateDone = true;
			this.getSynchronizationObject().notify();
		}
	}
	
	/**
	 * Checks if updates for the locally installed software components are available.
	 */
	private void checkForAvailableUpdates() {
		
		boolean updatesAvailable = P2OperationsHandler.getInstance().checkForNewerBundles();
		
		if (updatesAvailable==true) {
			String infoString = "Newer versions of local AWB components are available, please update your local AWB installation (if working against one) and the Target Platform!";
			System.err.println("[" + this.getClass().getSimpleName() + "] " + infoString);
			
			if (Application.isOperatingHeadless()==false) {
				AwbMessageDialog.showMessageDialog(Application.getMainWindow(), infoString, "Updates available", AwbMessageDialog.WARNING_MESSAGE);
			}
		} else {
			String infoString = "Your target platform is up to date!";
			System.out.println("[" + this.getClass().getSimpleName() + "] " + infoString);
			
			if (this.manualyExecutedByUser==true && Application.isOperatingHeadless()==false) {
				AwbMessageDialog.showMessageDialog(Application.getMainWindow(), infoString, "No Updates found", AwbMessageDialog.INFORMATION_MESSAGE);
			}
		}
		
	}
	/**
	 * Wait for the end of the benchmark.
	 */
	private void waitForTheEndOfBenchmark() {
		while (Application.isBenchmarkRunning()==true) {
			try {
				sleep(250);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	// ------------------------------------
	// --- p2-based update ----------------
	// ------------------------------------

	/**
	 * Starts a p2-based update procedure
	 */
	public void startP2Updates() {
		
		// --- Check for available updates -------
		System.out.println("[" + this.getClass().getSimpleName() + "] P2 Update: Check for updates ...");
		
		if (Application.isOperatingHeadless() == false) {
			this.getProgressMonitor().setVisible(true);
			P2OperationsHandler.getInstance().setProgressMonitor(this.getProgressMonitor());
		}
		
		IStatus status = P2OperationsHandler.getInstance().checkForUpdates();
		if (status.getSeverity()!=IStatus.ERROR) {

			if (status.getCode()==UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
				// --- No updates found --------------
				System.out.println("[" + this.getClass().getSimpleName() + "] P2 Update: No updates found!");
				
				if (Application.isOperatingHeadless() == false) {
					this.getProgressMonitor().setProgress(100);
					this.getProgressMonitor().setVisible(false);
					this.getProgressMonitor().dispose();
				}
				
				if (Application.isOperatingHeadless()==false && this.manualyExecutedByUser==true) {
					AwbMessageDialog.showMessageDialog(null, Language.translate("Keine Updates gefunden") + "!", Language.translate("Keine Updates gefunden"), AwbMessageDialog.INFORMATION_MESSAGE);
				}

			} else {
				
				// --- Ask for user confirmation if specified in the settings -------
				boolean installUpdates = true;
				if (this.askBeforeDownload==true) {
					
					// --- Temporary hide the progress dialog, otherwise the confirmation dialog would not be shown-------- 
					if (this.executionMode == ExecutionMode.APPLICATION) {
						this.getProgressMonitor().setVisible(false);
					}
					
					// --- Show confirmation dialog ----------
					int userAnswer = AwbMessageDialog.showConfirmDialog(null, Language.translate("Updates verfügbar, installieren?"), Application.getGlobalInfo().getApplicationTitle() + " Update", AwbMessageDialog.YES_NO_OPTION);
					if (userAnswer == AwbMessageDialog.NO_OPTION) {
						installUpdates = false;
						System.out.println("[" + this.getClass().getSimpleName() + "] P2 Update: Update canceled by user.");
						if (Application.isOperatingHeadless() == false) {
							this.getProgressMonitor().setVisible(false);
							this.getProgressMonitor().dispose();
						}
					}
				}
				

				if (installUpdates==true) {
					// --- Change progress dialog texts ----------------
					if (Application.isOperatingHeadless() == false) {
						this.getProgressMonitor().setHeaderText(Language.translate("Installiere Updates"));
						this.getProgressMonitor().setProgressText(Language.translate("Installiere") + "...");
						this.getProgressMonitor().setVisible(true);
						P2OperationsHandler.getInstance().setProgressMonitor(this.getProgressMonitor());
					}
					status = P2OperationsHandler.getInstance().installAvailableUpdates();
					if (status.isOK()) {
						System.out.println("[" + this.getClass().getSimpleName() + "] P2 Update: Updates sucessfully installed, restarting...");
						Application.restart();
					} else {
						System.err.println("[" + this.getClass().getSimpleName() + "] P2 Update: Error installing updates.");
					}
				}
				
				if (Application.isOperatingHeadless() == false) {
					this.getProgressMonitor().setProgress(100);
					this.getProgressMonitor().setVisible(false);
					this.getProgressMonitor().dispose();
				}

			}
		}
		
		// --- Finally remove the local progress monitor ------------
		P2OperationsHandler.getInstance().setProgressMonitor(null);
		
	}
	
	/**
	 * Gets the progress monitor.
	 * @return the progress monitor
	 */
	private AwbProgressMonitor getProgressMonitor() {
		if (this.progressMonitor == null) {
			String title = Language.translate("Aktualisierung");
			String header = Language.translate("Suche nach Updates");
			String progressText = Language.translate("Suche") + "...";
			this.progressMonitor = AgentWorkbenchUiManager.getInstance().getProgressMonitor(title, header, progressText);
		}
		return this.progressMonitor;
	}
	
	/**
	 * Checks if the update is done.
	 * @return true, if is update done
	 */
	public boolean isUpdateDone() {
		return updateDone;
	}
	/**
	 * Wait until the update is done.
	 */
	public void waitForUpdate() {

		// --- Only wait if the update is not done yet --------------
		if (this.isUpdateDone()==false) {
		
			synchronized (this.getSynchronizationObject()) {
				try {
					this.debugPrint("Waiting for the update process");
					this.getSynchronizationObject().wait();
					this.debugPrint("Update process finished, proceeding");
				} catch (InterruptedException e) {
					System.err.println("Waiting for update interrupted");
					e.printStackTrace();
				}
			}
			
		} else {
			this.debugPrint("Update already done, no need to wait");
		}
	}
	
	/**
	 * Gets the synchronization object.
	 * @return the synchronization object
	 */
	private Object getSynchronizationObject() {
		if (this.synchronizationObject==null) {
			this.synchronizationObject = new Object();
		}
		return this.synchronizationObject;
	}
	private void debugPrint(String text) {
		if (this.debug==true) {
			System.out.println("[" + this.getClass().getSimpleName() + "] " + text);
		}
	}

}
