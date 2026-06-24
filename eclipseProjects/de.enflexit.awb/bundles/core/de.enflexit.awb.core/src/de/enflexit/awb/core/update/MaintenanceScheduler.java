package de.enflexit.awb.core.update;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class MaintenanceScheduler is used to register and execute {@link MaintenanceTask}
 * at their respective intervals. Stopping the scheduler using stop() will 
 * cancel and remove all running tasks, while registered tasks will be kept until unregistered.
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
			instance.start();
		}
		return instance;
	}
	/**
	 * Instantiates a new maintenance scheduledExecutorService.
	 */
	private MaintenanceScheduler() {}
	
	
	private boolean running;
	
	private ScheduledExecutorService scheduledExecutorService;
	private HashMap<String, MaintenanceTask> registeredTasks;
	private HashMap<String, ScheduledFuture<?>> runningTasks;

	/**
	 * Initializes the scheduledExecutorService and starts scheduling all registered tasks.
	 */
	public void start() {
		if (this.isRunning() == true) return;
		
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable task) {
				Thread maintenanceThread = new Thread(task);
				maintenanceThread.setName("maintenance-thread");
				maintenanceThread.setDaemon(true);
				return maintenanceThread;
			}
		});
		this.running = true;

		for (MaintenanceTask task : this.getRegisteredTasks().values()) {
			this.scheduleTask(task);
		}
	}
	
	/**
	 * Sets everything to null except for the singleton instance
	 * and the registered tasks.
	 */
	public void stop() {
		
		if (this.isRunning() == false) return;
		
		this.getRunningTasks().keySet().forEach(taskId -> this.cancelTask(taskId));
		this.runningTasks = null;
		this.scheduledExecutorService.shutdown();
		this.scheduledExecutorService = null;
		this.running = false;
	}
	
	/**
	 * Register a task which will be scheduled at fixed intervals if
	 * the MaintenanceScheduler is running.
	 *
	 * @param task2Register the task to register
	 */
	public void registerTask(MaintenanceTask task2Register) {
		
		this.getRegisteredTasks().put(task2Register.getId(), task2Register);
		
		if (this.isRunning() == true) {
			this.scheduleTask(task2Register);
		}
	}
	
	/**
	 * Unregisters the task corresponding to the specified id.
	 *
	 * @param taskId id of the task to unregister
	 */
	public void unregisterTask(String taskId) {
		
		this.getRegisteredTasks().remove(taskId);
		
		if (this.isRunning() == true) {
			this.cancelTask(taskId);
			this.getRunningTasks().remove(taskId);
			if (this.getRunningTasks().size() == 0) this.stop();
		}
	}
	
	/**
	 * Schedules the task to be executed periodically.
	 *
	 * @param task the task to schedule
	 */
	private void scheduleTask(MaintenanceTask task) {
		
		// --- Don't act if the task is already known or running ----
		if (this.getRunningTasks().get(task.getId()) == null || this.getRunningTasks().get(task.getId()).isCancelled()) {
			
			// --- Prepare delay and interval -----------------------
			long initialDelayInMillis = this.calculateExecutionDelay(task.getStartTime(), task.getIntervalInHours(), task.getMinutesToRandomize());
			long checkIntervalInMillis = task.getIntervalInHours() * 1000 * 60 * 60; 
			
			// --- Schedule the task --------------------------------
			ScheduledFuture<?> scheduledTask = scheduledExecutorService.scheduleAtFixedRate(task.getTask(), initialDelayInMillis, checkIntervalInMillis, TimeUnit.MILLISECONDS);
			this.getRunningTasks().put(task.getId(), scheduledTask);
		}
		
	}

	/**
	 * Cancels the task identified by the specified
	 * taskId.
	 */
	private void cancelTask(String taskId) {
		
		if (this.getRunningTasks().get(taskId) != null) {
			this.getRunningTasks().get(taskId).cancel(false);
		}
	}
	
	/**
	 * Calculates the delay before the first execution of the task.
	 *
	 * @param startingTime the starting time
	 * @param intervalInHours the interval in hours
	 * @param randomizerRangeInMinutes the randomizer range in minutes
	 * @return the milliseconds to wait before first execution.
	 */
	private long calculateExecutionDelay(LocalTime startingTime, int intervalInHours, int randomizerRangeInMinutes) {
		LocalTime targetTime;
		// --- Define the time to do the first check ----------------
		if (randomizerRangeInMinutes > 0) {
			targetTime = this.getRandomizedStartTime(startingTime, randomizerRangeInMinutes);
		} else {
			targetTime = startingTime;
		}
		
		// --- first check is Date of now at targetTime -------------
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime dateTimeFirstCheck = now.with(targetTime);
		
		// --- Add the interval until first check is in the future --
		while (now.isAfter(dateTimeFirstCheck)) {
			dateTimeFirstCheck = dateTimeFirstCheck.plusHours(intervalInHours);
		}
		
		// --- Calculate wait time before first check ---------------
		Duration timeToWait = Duration.between(now, dateTimeFirstCheck);
		return timeToWait.toMillis();
	}
	
	/**
	 * Returns a LocalTime object with time set to the 
	 * starting time +/- randomizerRange/2. 
	 *
	 * @param startingTime the starting time
	 * @param randomizerRangeInMinutes the randomizer range in minutes
	 * @return the randomized start time
	 */
	private LocalTime getRandomizedStartTime(LocalTime startingTime, int randomizerRangeInMinutes) {
		
		// --- Generate random offset -------------------------------
		int randomAmountOfMinutes = new Random().nextInt(0, randomizerRangeInMinutes);
		
		LocalTime randomizedResult;
		// --- Adjust the starting time -----------------------------
		if (randomAmountOfMinutes > randomAmountOfMinutes / 2) {
			randomizedResult = startingTime.plusMinutes(randomAmountOfMinutes);
		} else {
			randomizedResult = startingTime.minusMinutes(randomAmountOfMinutes);
		}
		return randomizedResult;
	}

	
	/**
	 * @return true if the the Scheduler is actively scheduling tasks.
	 */
	private boolean isRunning() {
		return running;
	}
	/**
	 * Returns the runningTasks, meaning tasks which have been scheduled.
	 *
	 * @return the runningTasks
	 */
	private HashMap<String, ScheduledFuture<?>> getRunningTasks() {
		if (runningTasks == null) {
			runningTasks = new HashMap<String, ScheduledFuture<?>>();
		}
		return runningTasks;
	}
	/**
	 * Returns the registered tasks.
	 *
	 * @return the registered tasks
	 */
	private HashMap<String, MaintenanceTask> getRegisteredTasks() {
		if (registeredTasks == null) {
			registeredTasks = new HashMap<String, MaintenanceTask>();
		}
		return registeredTasks;
	}

	
}