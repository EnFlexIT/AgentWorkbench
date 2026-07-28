package de.enflexit.logging.db.dataModel;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * The Class LoggingEventException.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@Entity
@Table(name = "logging_event_exception")
public class LoggingEventException {

    @EmbeddedId
    private LoggingEventExceptionId id;

    @MapsId("eventId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private LoggingEvent event;

    @Column(name = "trace_line", nullable = false)
    private String traceLine;

	/**
	 * Returns the id.
	 *
	 * @return the id
	 */
	public LoggingEventExceptionId getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId(LoggingEventExceptionId id) {
		this.id = id;
	}

	/**
	 * Returns the event.
	 *
	 * @return the event
	 */
	public LoggingEvent getEvent() {
		return event;
	}

	/**
	 * Sets the event.
	 *
	 * @param event the event to set
	 */
	public void setEvent(LoggingEvent event) {
		this.event = event;
	}

	/**
	 * Returns the trace line.
	 *
	 * @return the traceLine
	 */
	public String getTraceLine() {
		return traceLine;
	}

	/**
	 * Sets the trace line.
	 *
	 * @param traceLine the traceLine to set
	 */
	public void setTraceLine(String traceLine) {
		this.traceLine = traceLine;
	}

}