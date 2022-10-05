package de.enflexit.jade.behaviour;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

/**
 * Abstract superclass for timed behaviours. Unlike JADE's TickerBEhaviour, this implementation considers
 * the duration of the task, i.E. reduces the waiting interval accordingly when rescheduling. The task can
 * be configured either to start at or to be finished at the specified time.
 * 
 * IMPORTANT: TimingBehaviours must not be added like regular behaviours, since they will block the agent
 * thread then! Use the behaviour's start method again, and call its' stop method from the executing agent's 
 * takeDown method for proper termination! 
 *
 * @author Alexander Graute - SOFTEC - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractTimingBehaviour extends Behaviour {

	private static final long serialVersionUID = -8912815985096298239L;

	public enum ExecutionTiming {
		StartFrom,
		FinishUntil
	}
	
	private ThreadedBehaviourFactory threadedBehaviourFactory;
	
	private boolean debug = false;
	
	private Duration tickInterval;
	private ExecutionTiming executionTiming;	
	
	private Instant nextTick;
	private Instant previousTick;
	private ArrayList<Integer> durations;
	private Duration totalDuration;
	private int tickCount;
	private long bufferMillis = 15;
	private DateTimeFormatter formatter;
	
	private boolean done = false;

	/**
	 * Instantiates a new timing behaviour, starting at the specified instant and repeated with the specified interval length.
	 *
	 * @param agent the agent executing the behaviour
	 * @param startAt the instant to start at with interval ticks
	 * @param interval the interval duration
	 * @param executionTiming specifies if the should start at or be finished until the interval times.
	 */
	public AbstractTimingBehaviour(Agent agent, Instant startAt, Duration interval, ExecutionTiming executionTiming) {
		super(agent);
		this.setExecutionTiming(executionTiming);
		this.setTickInterval(interval);
		this.nextTick = startAt;
		this.durations = new ArrayList<Integer>();
	}
	
	/**
	 * Instantiates a new abstract timing behaviour, based on the specified interval and offset durations. For example
	 * an interval of 10 and an offset 2 minutes means the action is scheduled every ten minutes at xx:02, xx:12 etc.
	 * Attention: Currently only works with real time! Use the constructor above for simulation time support.
	 * //TODO Modify the calculation of the starting point (Method calcStartAtByInterval()) to support simulation time.
	 *
	 * @param agent the agent
	 * @param interval the tick interval
	 * @param offset the duration offset
	 * @param executionTiming specifies if the should start at or be finished until the interval times.
	 */
	public AbstractTimingBehaviour(Agent agent, Duration interval, Duration offset, ExecutionTiming executionTiming) {
		this(agent, calcStartAtByInterval(interval, offset), interval, executionTiming);
	}
	
	/**
	 * Calculate instant for next start by interval.
	 *
	 * @param interval the interval
	 * @param offset the offset
	 * @return the instant
	 */
	protected static Instant calcStartAtByInterval(Duration interval, Duration offset) {
		//TODO mögliche Fehler hinsichtlich offset vs interval Duration abfangen (z.B. > 24h Exception) 
		ZonedDateTime startAt = ZonedDateTime.now().withNano(0);
		if(interval.getSeconds() >= 1 ) {
			startAt = startAt.withSecond(0);
		}
		if(interval.toMinutes()>= 1) {
			startAt = startAt.withMinute(0);
		}
		if(interval.toHours()>= 1) {
			startAt = startAt.withHour(0);
		}
		if(offset != null) {
			startAt = startAt.plusNanos(offset.toNanos());
		}
		return startAt.toInstant();
	}
	
	/* (non-Javadoc)
	* @see jade.core.behaviours.Behaviour#action()
	*/
	@Override
	public final void action() {
		
		// --- Determine waiting time before the next execution -----
		this.nextTick = this.calculateNextTick(this.getNextTick(), this.getTickInterval());
		this.printDebugMessage("Next execution scheduled for " + (this.executionTiming==ExecutionTiming.StartFrom ? "start time" : "end time ") + this.getDateFormatter().format(nextTick));
		this.printDebugMessage("Current time: " + this.getDateFormatter().format(this.getCurrentTime()));
		long waitingTime = this.calculateWaitingTime(nextTick, this.getTickInterval(), this.getTickCount()).toMillis();
		this.printDebugMessage("Waiting " + waitingTime + " ms or " + waitingTime /1000  + " s");
		
		if (this.debug==true) {
			// --- Blank line to mark the end of the debug outputs of one execution
			System.out.println();
		}
		
		try {
			Thread.sleep(waitingTime);
		} catch (InterruptedException e) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Thread interrupted, terminating behaviour");
			this.done = true;
			return;
		}
		
		// --- Perform action ---------------------------------------
		Instant startTime = this.getCurrentTime();
		this.printDebugMessage("Starting execution...");
		this.performAction();
		Instant endTime = this.getCurrentTime();
		Duration duration = Duration.between(startTime, endTime);
		this.printDebugMessage("Done at " + this.getDateFormatter().format(endTime) + ", execution took " + duration.toMillis() + " ms");
		this.durations.add((int) duration.toMillis());
		this.totalDuration = this.getTotalDuration().plus(duration);
		if(duration.toNanos() > this.getTickInterval().toNanos()) {
			System.err.println("[" + this.getClass().getSimpleName() + "] - execution time (" + duration.toMillis() + ") exceeds the specified repetition interval ("+this.getTickInterval().toMillis()+")");
		}

		// --- Wait unit tick ends ----------------------------------
		waitingTime = Duration.between(this.getCurrentTime(),nextTick).toMillis();
		if(waitingTime < 0) {
			waitingTime = 0;
		}
		if(waitingTime>0) {
			
			printDebugMessage("Done before the specified time, waiting another " + waitingTime + " ms or " + waitingTime / 1000 + " s");
			if (this.debug==true) {
				// --- Blank line to mark the end of the debug outputs of one execution
				System.out.println();
			}
			
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Thread interrupted, terminating behaviour");
				this.done = true;
				return;
			}
		}
		this.previousTick = nextTick;
		this.tickCount++;
		
	}

	
	/**
	 * Perform the scheduled action.
	 */
	protected abstract void performAction();

	/**
	 * Calculate next tick and return instant.
	 *
	 * @param nextTick the last tick
	 * @param interval the interval
	 * @return the instant
	 */
	protected Instant calculateNextTick(Instant lastTick, Duration interval) {
		while(lastTick.isBefore(this.getCurrentTime()) || lastTick==this.previousTick) {
			lastTick = lastTick.plusMillis(interval.toMillis());
		}
		
		return lastTick;		
	}
	
	/**
	 * Calculate the waiting time.
	 *
	 * @param startAt the start at
	 * @param interval the interval
	 * @param tickCount the tick count
	 * @return the duration
	 */
	protected Duration calculateWaitingTime(Instant startAt, Duration interval, int tickCount) {
		Duration waitingTime = Duration.between(this.getCurrentTime(), startAt);
		if ((this.getExecutionTiming() == ExecutionTiming.FinishUntil && tickCount != 0)) {
			Duration standardDevitation = calculateStandardDeviation(durations).multipliedBy(2);
			Duration averageDuration = getAverageDuration(getTickCount());
			waitingTime = waitingTime.minus(averageDuration).minus(standardDevitation).minusMillis(bufferMillis);
			printDebugMessage("Average execution time: " + averageDuration.toMillis() + " ms");
			printDebugMessage("Standard deviation: " + standardDevitation.toMillis() + " ms");
			printDebugMessage("Calculated waiting time: " + waitingTime.toMillis() + " ms");
		}
		if(waitingTime.isNegative()) {
			return Duration.ofMillis(0);
		}
		return waitingTime;
	}

	/**
	 * Calculate the standard deviation for all previous execution times.
	 * 
	 * @param durations the durations
	 * @return the duration
	 */
	private Duration calculateStandardDeviation(ArrayList<Integer> durations) {
		
		int length = durations.size();
		if (length == 0) {
			return Duration.ofMillis(0);
		}
		double sum = getTotalDuration().toMillis();
		double standardDeviation = 0.0;
		double average = sum / length;

		for (int i = 0; i < length; i++) {
			standardDeviation += Math.pow(durations.get(i) - average, 2);
		}
		return Duration.ofMillis((long) Math.sqrt(standardDeviation / length));
	}

	/**
	 * Prints the debug message.
	 * @param message the message
	 */
	protected void printDebugMessage(String message) {
		if(this.debug==true) {
			System.out.println("[" + this.getClass().getSimpleName() + "] - " + message);
		}
	}
	
	/**
	 * Gets the date formatter.
	 * @return the date formatter
	 */
	protected DateTimeFormatter getDateFormatter() {
		if(this.formatter == null) {
			formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS").withLocale(Locale.GERMAN).withZone(ZoneId.systemDefault());
		}
		return formatter;
	}

	/**
	 * Gets the tick interval.
	 * @return the tickInterval
	 */
	public Duration getTickInterval() {
		return tickInterval;
	}

	/**
	 * Sets the tick interval.
	 * @param tickInterval the tickInterval to set
	 */
	public void setTickInterval(Duration tickInterval) {
		this.tickInterval = tickInterval;
	}

	/**
	 * Gets the total duration.
	 * @return the total duration
	 */
	public Duration getTotalDuration() {
		if (totalDuration == null) {
			this.totalDuration = Duration.ofNanos(0);
		}
		return totalDuration;
	}

	/**
	 * Gets the average duration.
	 * @param length the length
	 * @return the average duration
	 */
	public Duration getAverageDuration(int length) {
		if (length == 0) {
			return Duration.ofMillis(0);
		}
		return getTotalDuration().dividedBy(length);
	}

	/**
	 * Gets the last tick.
	 * @return the startAt
	 */
	public Instant getNextTick() {
		return nextTick;
	}
	
	/**
	 * Sets the next tick.
	 * @param nextTick the nextTick to set
	 */
	public void setNextTick(Instant nextTick) {
		this.nextTick = nextTick;
	}

	/**
	 * Gets the tick count.
	 * @return the tick count
	 */
	public int getTickCount() {
		return tickCount;
	}
	
	/**
	 * Gets the execution timing.
	 * @return the execution timing
	 */
	public ExecutionTiming getExecutionTiming() {
		return executionTiming;
	}
	/**
	 * Sets the execution timing.
	 * @param executionTiming the new execution timing
	 */
	private void setExecutionTiming(ExecutionTiming executionTiming) {
		this.executionTiming = executionTiming;
	}
	
	/**
	 * Checks if is debug.
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}
	/**
	 * Sets the debug.
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * Gets the current buffer in milliseconds.
	 * @return the buffer in milliseconds
	 */
	public long getBufferMillis() {
		return bufferMillis;
	}
	
	/**
	 * Sets the buffer in milliseconds.
	 * @param bufferMillis the new buffer in milliseconds
	 */
	public void setBufferMillis(long bufferMillis) {
		this.bufferMillis = bufferMillis;
	}
	
	/**
	 * Gets the current time. This default implementation returns the current system time.
	 * Override this method to return for example the current simulation time.
	 * @return the current time
	 */
	protected Instant getCurrentTime() {
		return Instant.now();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.behaviours.CyclicBehaviour#done()
	 */
	@Override
	public boolean done() {
		return this.done;
	}
	
	/**
	 * Starts this TimingBehaviour as a threaded behaviour.
	 */
	public void start() {
		if (this.myAgent!=null) {
			Behaviour threadedBehaviour = this.getThreadedBehaviourFactory().wrap(this);
			this.myAgent.addBehaviour(threadedBehaviour);
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Cannot start, no valid agent was passed to the constructor!");
		}
	}
	
	/**
	 * Stops this TimingBehaviour.
	 */
	public void stop() {
		Thread behaviourThread = this.getThreadedBehaviourFactory().getThread(this);
		if (behaviourThread!=null) {
			behaviourThread.interrupt();
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] " + this.myAgent.getLocalName() + " - Error terminating, behaviour thread not found!");
		}
		this.myAgent.removeBehaviour(this);
	}
	
	/**
	 * Gets the threaded behaviour factory.
	 * @return the threaded behaviour factory
	 */
	private ThreadedBehaviourFactory getThreadedBehaviourFactory() {
		if (threadedBehaviourFactory==null) {
			threadedBehaviourFactory = new ThreadedBehaviourFactory(); 
		}
		return threadedBehaviourFactory;
	}
	
}
