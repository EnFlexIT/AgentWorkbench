package de.enflexit.logging.db.dataModel;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * The Class LoggingEventPropertyId represents the composite primary key
 * for the table logging_event_property
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@Embeddable
public class LoggingEventPropertyId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "mapped_key")
    private String mappedKey;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o instanceof LoggingEventPropertyId) == false) {
            return false;
        }

        LoggingEventPropertyId compObj = (LoggingEventPropertyId) o;

        return Objects.equals(this.getEventId(), compObj.getEventId()) && Objects.equals(this.getMappedKey(), compObj.getMappedKey());

    }

    /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        return Objects.hash(eventId, mappedKey);
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
	 * @return the mappedKey
	 */
	public String getMappedKey() {
		return mappedKey;
	}

	/**
	 * @param mappedKey the mappedKey to set
	 */
	public void setMappedKey(String mappedKey) {
		this.mappedKey = mappedKey;
	}
}