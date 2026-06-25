package de.enflexit.awb.core.update;

import java.time.LocalTime;

/**
 * The Interface MaintenanceTask defines the necessary methods of a task
 * to be registered at the {@link MaintenanceScheduler}.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public interface MaintenanceTask {
	
	/**
	 * Should return a Runnable containing the task
	 * to be executed by the MaintenanceScheduler.
	 *
	 * @return the runnable to be executed
	 */
	public Runnable getTask();
	
	/**
	 * Should return a unique identifier for the submitted
	 * task.
	 *
	 * @return the id of the task
	 */
	public String getId();
	
	/**
	 * Returns the interval, at which the task should be performed.
	 * Default value once per day.
	 *
	 * @return the interval in hours
	 */
	public default int getIntervalInHours() {
		return 24;
	}
	
	/**
	 * Should return the time at which the task should be performed.
	 * Default is 02:00 O'clock.
	 *
	 * @return the start time
	 */
	public default LocalTime getStartTime() {
		return LocalTime.of(2, 0);
	}
	
	/**
	 * Returns the amout of minutes that the actual starting time
	 * may differ from getStartTime(). Default is 60, meaning +/- 30 minutes.
	 *
	 * @return the minutes to randomize
	 */
	public default int getMinutesToRandomize() {
		return 60;
	}
	
	/**
	 * Return true if the task should be executed in a separate thread.
	 *
	 * @return default true, can be overridden if the task doesn't require it's own Thread.
	 */
	public default boolean requiresDedicatedThread() {
		return true;
	}
	
}