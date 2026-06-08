package de.enflexit.awb.ws.core.util;

import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings;
import de.enflexit.awb.ws.server.AwbServer;

public class UpdateCheckCoordinatorWebApp {
	
	private static final long EXECUTION_WAIT_TIME = 60_000L;
	private Thread workerThread;
	private UpdateCheckStatusWebApp status;
	private long lastExecution;
	
	private static UpdateCheckCoordinatorWebApp instance;
	
	/**
	 * Gets the single instance of UpdateCheckCoordinator.
	 * @return single instance of UpdateCheckCoordinator
	 */
	public static UpdateCheckCoordinatorWebApp getInstance() {
		if (instance == null) {
			instance = new UpdateCheckCoordinatorWebApp();
		}
		return instance;
	}
	
	/**
	 * Instantiates a new update check coordinator.
	 */
	private UpdateCheckCoordinatorWebApp() {}
	
    /**
     * Returns the UpdateCheckStatus.
     * @return the UpdateCheckStatus
     */
    public UpdateCheckStatusWebApp getUpdateCheckStatusWebApp() {
    	if (status == null) {
    		status = new UpdateCheckStatusWebApp();
    	}
        return status;
    }
    
    /**
     * Starts a thread which checks for newer bundles.
     * @param forceNewCheck the force new check
     */
    public synchronized void triggerCheck() {
    	
    	if (workerThread != null) return;
    	
    	long now = System.currentTimeMillis();
    	// --- Avoid to frequent checks --------------------------------------------------------------------
    	if (now >= lastExecution + EXECUTION_WAIT_TIME) {
    		this.getUpdateCheckStatusWebApp().setPending(true);
    		workerThread = new Thread(this::runWebAppCheck,"P2-Update-Check-Thread");
    		lastExecution = now;
    		workerThread.start();
    	}
    }
    
    /**
     * Checks whether a new version of the web app is available. Results are stored in 
     * the updateCheckStatusWebApp.
     */
    private void runWebAppCheck() {
		// --- Get the webAppSettings -----------------------------------------------------------------------
		JettyConfiguration jettyConfig = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerService(AwbServer.NAME).getJettyConfiguration();
		JettyWebApplicationSettings webAppSettings = jettyConfig.getWebApplicationSettings();
		// --- Search for a newer version -------------------------------------------------------------------
		WebApplicationVersion newVersion = WebApplicationUpdate.getWebApplicationUpdate(webAppSettings.getDownloadURL());
		// --- Set results. newVersion == null means no update available ------------------------------------
		if (newVersion != null) {
			this.getUpdateCheckStatusWebApp().setAvailable(true);
			this.getUpdateCheckStatusWebApp().setNewVersion(newVersion.getVersion().toString());
		} else {
			this.getUpdateCheckStatusWebApp().setAvailable(false);
			this.getUpdateCheckStatusWebApp().setNewVersion("-");
		}
		this.getUpdateCheckStatusWebApp().setLastCheck(System.currentTimeMillis());
		this.getUpdateCheckStatusWebApp().setPending(false);
		this.getUpdateCheckStatusWebApp().setCurrentVersion(WebApplicationUpdate.getCurrentWebApplicationVersion().getVersion().toString());		
		workerThread = null;
    }
	
}