package de.enflexit.awb.core.update;


import de.enflexit.common.p2.P2OperationsHandler;

public class UpdateCheckCoordinator {
	
	
	private UpdateCheckStatus status = new UpdateCheckStatus();
	private volatile Thread workerThread;
	private static final long CACHE_TIME = 60000;
	
	private static UpdateCheckCoordinator instance;
	
	public static UpdateCheckCoordinator getInstance() {
		if (instance == null) {
			instance = new UpdateCheckCoordinator();
		}
		return instance;
	}
	
	private UpdateCheckCoordinator() {}
	
    public UpdateCheckStatus getStatus() {
        return status;
    }

    /**
     * Starts a thread which checks for newer bundles if not already started.
     */
    public synchronized void triggerCheck(boolean forceNewCheck) {
    	if (forceNewCheck == false) {
    		// --- workerThread is already running but not finished yet -------------------------------
    		if (status.isPending() == true) {
    			return;
    		}
    		long now = System.currentTimeMillis();
    		if (status.getLastCheck() != 0 && now - status.getLastCheck() < CACHE_TIME) {
    			return;
    		}
    	}
    	status.setPending(true);
    	workerThread = new Thread(this::runCheck,"P2-Update-Check-Thread");
    	workerThread.start();
    }
    
    /**
     * Checks for newer bundles.
     */
    private void runCheck() {
		boolean isUpdateAvailable = P2OperationsHandler.getInstance().checkForNewerBundles();
		status.setAvailable(isUpdateAvailable);
		status.setLastCheck(System.currentTimeMillis());
		status.setPending(false);
	}
}