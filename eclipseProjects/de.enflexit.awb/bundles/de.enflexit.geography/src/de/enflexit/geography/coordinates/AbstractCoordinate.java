package de.enflexit.geography.coordinates;

import java.awt.geom.Point2D;
import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for AbstractGeoCoordinate complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractCoordinate"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractCoordinate")
public abstract class AbstractCoordinate extends Point2D implements Serializable {

    private final static long serialVersionUID = 201404191434L;

    /**
     * Has to serialize the current instance into a string.
     * 
     * @return the string
     */
    public abstract String serialize();

    /**
     * Has to convert the specified string into the specific coordinates for the
     * current instance.
     * 
     * @param coordinateString the coordinate as string
     * @throws CoordinateParseException if parsing of a coordinate fails
     */
    public abstract void deserialize(String coordinateString) throws NullPointerException, CoordinateParseException;

    /**
     * The Class CoordinateParseException.
     * 
     * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
     */
    public class CoordinateParseException extends Exception {

	private static final long serialVersionUID = 7134537764979177071L;

	/**
	 * Instantiates a new coordinate parse exception.
	 * 
	 * @param message the message
	 */
	public CoordinateParseException(String message) {
	    super(message);
	}
    }

}
