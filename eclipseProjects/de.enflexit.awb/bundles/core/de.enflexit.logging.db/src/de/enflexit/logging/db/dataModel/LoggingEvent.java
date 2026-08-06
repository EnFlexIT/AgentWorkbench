package de.enflexit.logging.db.dataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

/**
 * The Class LoggingEvent.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@Entity
@Table(
		name = "logging_event",
		indexes = {
				@Index(name ="idx_logging_event_timestmp", columnList = "timestmp")
		})

public class LoggingEvent {

	@Column(name="timestmp", nullable=false)
	private long timestmp;
	
	@Column(name="formatted_message", nullable=false, columnDefinition = "Text")
	private String formattedMessage;
	
	@Column(name="logger_name", nullable=false)
	private String loggerName;
	
	@Column(name="level_string", nullable=false)
	private String levelString;
	
	@Column(name="thread_name")
	private String threadName;
	
	@Column(name="reference_flag")
	private Short referenceFlag;

	@Column(name="arg0")
	private String arg0;

	@Column(name="arg1")
	private String arg1;
	
	@Column(name="arg2")
	private String arg2;

	@Column(name="arg3")
	private String arg3;
	
	@Column(name="caller_filename", nullable=false)
	private String callerFileName;
	
	@Column(name="caller_class", nullable=false)
	private String callerClass;
	
	@Column(name="caller_method", nullable=false)
	private String callerMethod;

	@Column(name="caller_line", nullable=false, length = 4)
	private String callerLine;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="event_id", nullable=false)
	private long eventId;
	
    @ElementCollection
    @CollectionTable(name = "logging_event_property", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyColumn(name = "mapped_key")
    @Column(name = "mapped_value")
    private Map<String, String> properties = new HashMap<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoggingEventException> exceptions = new ArrayList<>();

	/**
	 * Adds the exception and sets the exceptions' event to 'this'
	 *
	 * @param exception the exception
	 */
	public void addException(LoggingEventException exception) {
		this.getExceptions().add(exception);
		exception.setEvent(this);
	}

	/**
	 * Removes the exception and sets the exceptions' event to null
	 *
	 * @param exception the exception
	 */
	public void removeException(LoggingEventException exception) {
		this.getExceptions().remove(exception);
		exception.setEvent(null);
	}

    
	/**
	 * Returns the timestamp.
	 *
	 * @return the timestmp
	 */
	public long getTimestmp() {
		return timestmp;
	}

	/**
	 * Sets the timestmp.
	 *
	 * @param timestmp the new timestmp
	 */
	public void setTimestmp(long timestmp) {
		this.timestmp = timestmp;
	}

	/**
	 * Returns the formatted message.
	 *
	 * @return the formatted message
	 */
	public String getFormattedMessage() {
		return formattedMessage;
	}

	/**
	 * Sets the formatted message.
	 *
	 * @param formattedMessage the new formatted message
	 */
	public void setFormattedMessage(String formattedMessage) {
		this.formattedMessage = formattedMessage;
	}

	/**
	 * Returns the logger name.
	 *
	 * @return the logger name
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * Sets the logger name.
	 *
	 * @param loggerName the new logger name
	 */
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	/**
	 * Returns the level string.
	 *
	 * @return the level string
	 */
	public String getLevelString() {
		return levelString;
	}

	/**
	 * Sets the level string.
	 *
	 * @param levelString the new level string
	 */
	public void setLevelString(String levelString) {
		this.levelString = levelString;
	}

	/**
	 * Returns the thread name.
	 *
	 * @return the thread name
	 */
	public String getThreadName() {
		return threadName;
	}

	/**
	 * Sets the thread name.
	 *
	 * @param threadName the new thread name
	 */
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	/**
	 * Returns the arg 0.
	 *
	 * @return the arg 0
	 */
	public String getArg0() {
		return arg0;
	}

	/**
	 * Sets the arg 0.
	 *
	 * @param arg0 the new arg 0
	 */
	public void setArg0(String arg0) {
		this.arg0 = arg0;
	}

	/**
	 * Returns the arg 1.
	 *
	 * @return the arg 1
	 */
	public String getArg1() {
		return arg1;
	}

	/**
	 * Sets the arg 1.
	 *
	 * @param arg1 the new arg 1
	 */
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}

	/**
	 * Returns the arg 2.
	 *
	 * @return the arg 2
	 */
	public String getArg2() {
		return arg2;
	}

	/**
	 * Sets the arg 2.
	 *
	 * @param arg2 the new arg 2
	 */
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}

	/**
	 * Returns the arg 3.
	 *
	 * @return the arg 3
	 */
	public String getArg3() {
		return arg3;
	}

	/**
	 * Sets the arg 3.
	 *
	 * @param arg3 the new arg 3
	 */
	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}

	/**
	 * Returns the caller file name.
	 *
	 * @return the caller file name
	 */
	public String getCallerFileName() {
		return callerFileName;
	}

	/**
	 * Sets the caller file name.
	 *
	 * @param callerFileName the new caller file name
	 */
	public void setCallerFileName(String callerFileName) {
		this.callerFileName = callerFileName;
	}

	/**
	 * Returns the caller class.
	 *
	 * @return the caller class
	 */
	public String getCallerClass() {
		return callerClass;
	}

	/**
	 * Sets the caller class.
	 *
	 * @param callerClass the new caller class
	 */
	public void setCallerClass(String callerClass) {
		this.callerClass = callerClass;
	}

	/**
	 * Returns the caller method.
	 *
	 * @return the caller method
	 */
	public String getCallerMethod() {
		return callerMethod;
	}

	/**
	 * Sets the caller method.
	 *
	 * @param callerMethod the new caller method
	 */
	public void setCallerMethod(String callerMethod) {
		this.callerMethod = callerMethod;
	}

	/**
	 * Returns the caller line.
	 *
	 * @return the caller line
	 */
	public String getCallerLine() {
		return callerLine;
	}

	/**
	 * Sets the caller line.
	 *
	 * @param callerLine the new caller line
	 */
	public void setCallerLine(String callerLine) {
		this.callerLine = callerLine;
	}

	/**
	 * Returns the event id.
	 *
	 * @return the event id
	 */
	public long getEventId() {
		return eventId;
	}

	/**
	 * Sets the event id.
	 *
	 * @param eventId the new event id
	 */
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	/**
	 * Returns the exceptions.
	 *
	 * @return the exceptions
	 */
	public List<LoggingEventException> getExceptions() {
		return exceptions;
	}

	/**
	 * Sets the exceptions.
	 *
	 * @param exceptions the new exceptions
	 */
	public void setExceptions(List<LoggingEventException> exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * Returns the properties.
	 *
	 * @return the properties
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * Sets the properties.
	 *
	 * @param properties the properties
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

}