package de.enflexit.awb.core.update;


import de.enflexit.common.p2.P2OperationsHandler;

/**
 * The Class UpdateCheckCoordinator.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class UpdateCheckCoordinator {
	
	private static final long EXECUTION_WAIT_TIME = 60_000L;
	private Thread workerThread;
	private UpdateCheckStatus status;
	private long lastExecution;
	
	private static UpdateCheckCoordinator instance;
	
	/**
	 * Gets the single instance of UpdateCheckCoordinator.
	 * @return single instance of UpdateCheckCoordinator
	 */
	public static UpdateCheckCoordinator getInstance() {
		if (instance == null) {
			instance = new UpdateCheckCoordinator();
		}
		return instance;
	}
	
	private UpdateCheckCoordinator() {}
	
    /**
     * Gets the status.
     * @return the status
     */
    public UpdateCheckStatus getUpdateCheckStatus() {
    	if (status == null) {
    		status = new UpdateCheckStatus();
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
    		if (now < this.getUpdateCheckStatus().getLastCheck() + AWBUpdater.UPDATE_CHECK_PERIOD) {
    			return;
    		}
    	}
    	
    	// --- Avoid too frequent checks ----------------------------------------------------------
    	if (now >= lastExecution + EXECUTION_WAIT_TIME) {
    		this.getUpdateCheckStatus().setPending(true);
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
		this.getUpdateCheckStatus().setAvailable(isUpdateAvailable);
		this.getUpdateCheckStatus().setLastCheck(System.currentTimeMillis());
		this.getUpdateCheckStatus().setPending(false);
		workerThread = null;
	}

}