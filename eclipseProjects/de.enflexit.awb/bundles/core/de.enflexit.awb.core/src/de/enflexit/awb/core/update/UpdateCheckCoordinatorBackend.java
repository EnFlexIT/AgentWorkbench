package de.enflexit.awb.core.update;


import de.enflexit.common.p2.P2OperationsHandler;

/**
 * The Class UpdateCheckCoordinator.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UpdateCheckCoordinatorBackend {
	
	private static final long EXECUTION_WAIT_TIME = 60_000L;
	private Thread workerThread;
	private UpdateCheckStatusBackend status;
	private long lastExecution;
	
	private static UpdateCheckCoordinatorBackend instance;
	
	/**
	 * Gets the single instance of UpdateCheckCoordinator.
	 * @return single instance of UpdateCheckCoordinator
	 */
	public static UpdateCheckCoordinatorBackend getInstance() {
		if (instance == null) {
			instance = new UpdateCheckCoordinatorBackend();
		}
		return instance;
	}
	
	/**
	 * Instantiates a new update check coordinator.
	 */
	private UpdateCheckCoordinatorBackend() {}
	
    /**
     * Returns the UpdateCheckStatus.
     * @return the UpdateCheckStatus
     */
    public UpdateCheckStatusBackend getUpdateCheckStatusBackend() {
    	if (status == null) {
    		status = new UpdateCheckStatusBackend();
    	}
        return status;
    }

    /**
     * Starts a thread which checks for newer bundles.
     * @param forceNewCheck the force new check
     */
    public synchronized void triggerCheck(boolean forceNewCheck) {
    	
    	if (workerThread != null) return;
    	
    	long now = System.currentTimeMillis();
    	if (forceNewCheck == false) {
    		// --- workerThread is already running but not finished yet ---------------------------
    		if (now < this.getUpdateCheckStatusBackend().getLastCheck() + AWBUpdater.UPDATE_CHECK_PERIOD) {
    			return;
    		}
    	}
    	
    	// --- Avoid to frequent checks ----------------------------------------------------------
    	if (now >= lastExecution + EXECUTION_WAIT_TIME) {
    		this.getUpdateCheckStatusBackend().setPending(true);
    		workerThread = new Thread(this::runBackendCheck,"P2-Update-Check-Thread");
    		lastExecution = now;
    		workerThread.start();
    	}
    }
    		
    /**
     * Checks for newer bundles.
     */
    private void runBackendCheck() {
		boolean isUpdateAvailable = P2OperationsHandler.getInstance().checkForNewerBundles();
		this.getUpdateCheckStatusBackend().setAvailable(isUpdateAvailable);
		this.getUpdateCheckStatusBackend().setLastCheck(System.currentTimeMillis());
		this.getUpdateCheckStatusBackend().setPending(false);
		workerThread = null;
	}

}