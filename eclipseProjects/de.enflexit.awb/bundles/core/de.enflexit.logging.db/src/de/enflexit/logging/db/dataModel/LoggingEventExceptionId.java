package de.enflexit.logging.db.dataModel;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * The Class LoggingEventExceptionId represents the composite primary key 
 * for the table logging_event_exception
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@Embeddable
public class LoggingEventExceptionId implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "i")
    private Short index;


    /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o instanceof LoggingEventExceptionId) == false) {
            return false;
        }

        LoggingEventExceptionId that = (LoggingEventExceptionId) o;

        return Objects.equals(eventId, that.eventId) && Objects.equals(index, that.index);
    }

    /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        return Objects.hash(eventId, index);
    }

	/**
	 * @return the eventId
	 */
	public Long getEventId() {
		return eventId;
	}

	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the index
	 */
	public Short getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Short index) {
		this.index = index;
	}

}