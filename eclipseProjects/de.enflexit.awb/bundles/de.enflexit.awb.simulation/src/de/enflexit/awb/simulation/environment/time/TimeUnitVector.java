package de.enflexit.awb.simulation.environment.time;

import java.io.Serializable;
import java.util.Vector;

import de.enflexit.language.Language;

/**
 * The Class TimeUnitVector is a Vector that holds different TimeUnits with
 * different parameters, like textual description, number of digits etc.<br>
 * When initiating this class the Vector will already be filled with TimeUntits
 * for Milliseconds, Seconds, Minutes, Hours, Days, Weeks, Month and Years.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeUnitVector extends Vector<TimeUnit> implements Serializable {

	private static final long serialVersionUID = 1736528558614878985L;

	/**
	 * Instantiates a new time unit vector and fills it already
	 * with values of the type TimeUnit.
	 * 
	 * @see TimeUnit
	 */
	public TimeUnitVector() {
		
		this.add(new TimeUnit(Language.translate("Millisekunden"), 3, 0, 999, 1));
		this.add(new TimeUnit(Language.translate("Sekunden"), 2, 0, 60, 1000));
		this.add(new TimeUnit(Language.translate("Minuten"), 2, 0, 60, 1000*60));
		this.add(new TimeUnit(Language.translate("Stunden"), 2, 0, 24, 1000*60*60));
		this.add(new TimeUnit(Language.translate("Tage"), 2, 0, 31, 1000*60*60*24));
		this.add(new TimeUnit(Language.translate("Wochen"), 2, 0, 52, 1000*60*60*24*7));
		this.add(new TimeUnit(Language.translate("Monate"), 2, 0, 12, 1000*60*60*24*30));
		this.add(new TimeUnit(Language.translate("Jahre"), 4, 0, 999999, 1000*60*60*24*365));
		
	}
	
}
