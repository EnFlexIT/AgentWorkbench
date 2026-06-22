package de.enflexit.awb.core.update;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.awb.core.Application;
import de.enflexit.common.p2.P2OperationsHandler;

/**
 * The Class MaintenanceScheduler is used to schedule update checks
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class MaintenanceScheduler {

	static Logger LOGGER = LoggerFactory.getLogger(MaintenanceScheduler.class);
	
	private static MaintenanceScheduler instance;
	
	/**
	 * Returns the single instance of MaintenanceScheduler.
	 *
	 * @return single instance of MaintenanceScheduler
	 */
	public static MaintenanceScheduler getInstance() {
		if (instance == null) {
			instance = new MaintenanceScheduler();
		}
		return instance;
	}
	/**
	 * Dispose.
	 */
	public static void dispose() {
		if (instance != null) {
			instance.stopSchedulingUpdateChecks();
			instance = null;
		}
	}
	/**
	 * Instantiates a new maintenance scheduledExecutorService. Configures the 
	 * Thread factory of ScheduledExecutorService to produce a
	 * Daemon thread with name "maintenance-thread"
	 */
	private MaintenanceScheduler() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread maintenanceThread = new Thread(r);
			maintenanceThread.setName("maintenance-thread");
			maintenanceThread.setDaemon(true);
			return maintenanceThread;
		});
	}
	
	
	private ScheduledExecutorService scheduledExecutorService;
	private ScheduledFuture<?> updateCheckTask;
	
	/**
	 * Start scheduling update checks at intervals defined by UPDATE_CHECK_PERIOD 
	 * defined in @see AWBUpdater.
	 */
	public void startSchedulingUpdateChecks() {

		if (updateCheckTask == null || updateCheckTask.isCancelled()) {
			updateCheckTask = scheduledExecutorService.scheduleAtFixedRate(this::checkForUpdate, 0, AWBUpdater.UPDATE_CHECK_PERIOD, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Checks whether an update is available.
	 */
	private void checkForUpdate() {

		LOGGER.info("Starting AWBUpdater...");
		AWBUpdater updater = new AWBUpdater();
		updater.start();
	}
			
	/**
	 * Stop scheduling update checks. Does not cancel an
	 * actively running update check.
	 */
	private void stopSchedulingUpdateChecks() {
		
		if (updateCheckTask != null && updateCheckTask.isCancelled() == false) {
			updateCheckTask.cancel(false);
			scheduledExecutorService.shutdown();
			updateCheckTask = null;
			scheduledExecutorService = null;
		}
	}
	
}