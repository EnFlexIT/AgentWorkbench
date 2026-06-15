package de.enflexit.awb.core;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.common.p2.P2OperationsHandler;

/**
 * The Class MaintenanceScheduler.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class MaintenanceScheduler {
	
	private final ScheduledExecutorService scheduler;
	
	private final long UPDATE_CHECK_PERIOD = 1; // TODO actual interval
	static Logger LOGGER = LoggerFactory.getLogger(MaintenanceScheduler.class);
	
	public MaintenanceScheduler() {
		scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread maintenanceThread = new Thread(r);
			maintenanceThread.setName("maintenance-thread");
			maintenanceThread.setDaemon(true);
			return maintenanceThread;
		});
	}
	
	
	/**
	 * Start the maintenance thread which checks for updates
	 * on fixed intervals defined by UPDATE_CHECK_PERIOD.
	 */
	public void start() {
		System.out.println("Starting maintenance thread");
		scheduler.scheduleAtFixedRate(this::checkForUpdate, 0, UPDATE_CHECK_PERIOD, TimeUnit.MINUTES);
	}
	
	public void stop() {
		System.out.println("Stopping maintenance thread");
		scheduler.shutdown();
	}

	/**
	 * Check whether an update is available.
	 *
	 * @return true, if successful
	 */
	private void checkForUpdate() {
		
		if (P2OperationsHandler.getInstance().checkForNewerBundles() == true) {
			LOGGER.info("Update available");
			System.out.println("Update available");
		} else {
			LOGGER.info("No update found");
			System.out.println("No update found");
		}
		Application.getGlobalInfo().setUpdateDateLastChecked(System.currentTimeMillis());
		Application.getGlobalInfo().doSaveConfiguration();
	}

	
}