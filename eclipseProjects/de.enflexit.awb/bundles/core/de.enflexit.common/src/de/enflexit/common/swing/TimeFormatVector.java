package de.enflexit.common.swing;

import java.io.Serializable;
import java.util.Vector;

/**
 * The Class TimeFormatVector is a Vector that holds different TimeFormats.<br>
 * When initiating this class the Vector will already be filled with TimeFormats.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeFormatVector extends Vector<TimeFormat> implements Serializable {

	private static final long serialVersionUID = 1736528558614878985L;

	/**
	 * Instantiates a new TimeFormatVector and fills it already
	 * with values of the type String.
	 * 
	 * @see TimeUnit
	 */
	public TimeFormatVector() {
		
		this.add(new TimeFormat("dd.MM.yyyy HH:mm:ss,SSS"));
		
		this.add(new TimeFormat("dd.MM.yyyy HH:mm:ss"));
		this.add(new TimeFormat("dd.MM.yyyy HH:mm"));
		this.add(new TimeFormat("dd.MM.yy HH:mm"));
		this.add(new TimeFormat("dd.MM.yy"));
		
		this.add(new TimeFormat("HH:mm:ss,SSS"));
		this.add(new TimeFormat("HH:mm:ss"));
		this.add(new TimeFormat("HH:mm"));
		
	}
	
}
