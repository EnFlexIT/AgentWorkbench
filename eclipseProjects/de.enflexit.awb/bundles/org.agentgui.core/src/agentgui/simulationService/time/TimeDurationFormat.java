/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.simulationService.time;

import java.text.DecimalFormat;
import java.util.Vector;

/**
 * The Class TimeDurationFormat.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeDurationFormat {

	private String format=null;
	
	private final long factorSecond = 1000;
	private final long factorMinute = factorSecond * 60;
	private final long factorHour   = factorMinute * 60;
	private final long factorDay    = factorHour * 24;
	private final long factorMonth  = factorDay * 30;
	private final long factorYear   = factorMonth * 12;

	private Vector<Long> factorVector = null;
	
	/**
	 * Instantiates a new time difference format.
	 * @param format the format
	 */
	public TimeDurationFormat(String format) {
		this.format = format;
		
		this.factorVector = new Vector<Long>();
		this.factorVector.add(factorYear);
		this.factorVector.add(factorMonth);
		this.factorVector.add(factorDay);
		this.factorVector.add(factorHour);
		this.factorVector.add(factorMinute);
		this.factorVector.add(factorSecond);
		
	}
	
	/**
	 * Format a given timely duration of milliseconds.
	 *
	 * @param duration the time difference
	 * @return the string
	 */
	public String format(long duration) {
		
		String durationFormatted = this.format;
		String workString = null;
		DecimalFormat twoDigits = new DecimalFormat("00");
		DecimalFormat threeDigits = new DecimalFormat("000");
		DecimalFormat fourDigits = new DecimalFormat("0000");
		
		Vector<Integer> fragVector = this.calculateTimeFragments(duration);
		
		if (this.format.contains("d")) {
			workString = twoDigits.format(fragVector.get(2));
			durationFormatted = durationFormatted.replace("dd", "d");
			durationFormatted = durationFormatted.replace("d", workString);
		}
		if (this.format.contains("M")) {
			workString = twoDigits.format(fragVector.get(1));
			durationFormatted = durationFormatted.replace("MM", "M");
			durationFormatted = durationFormatted.replace("M", workString);
		}
		if (this.format.contains("yyyy")) {
			workString = fourDigits.format(fragVector.get(0));
			durationFormatted = durationFormatted.replace("yyyy", "y");
			durationFormatted = durationFormatted.replace("y", workString);
		}
		if (this.format.contains("y")) {
			workString = twoDigits.format(fragVector.get(0));
			durationFormatted = durationFormatted.replace("yy", "y");
			durationFormatted = durationFormatted.replace("y", workString);
		}
		if (this.format.contains("H")) {
			workString = twoDigits.format(fragVector.get(3));
			durationFormatted = durationFormatted.replace("HH", "H");
			durationFormatted = durationFormatted.replace("H", workString);
		}
		if (this.format.contains("m")) {
			workString = twoDigits.format(fragVector.get(4));
			durationFormatted = durationFormatted.replace("mm", "m");
			durationFormatted = durationFormatted.replace("m", workString);
		}
		if (this.format.contains("s")) {
			workString = twoDigits.format(fragVector.get(5));
			durationFormatted = durationFormatted.replace("ss", "s");
			durationFormatted = durationFormatted.replace("s", workString);
		}
		if (this.format.contains("S")) {
			workString = threeDigits.format(fragVector.get(6));
			durationFormatted = durationFormatted.replace("SSS", "S");
			durationFormatted = durationFormatted.replace("SS", "S");
			durationFormatted = durationFormatted.replace("S", workString);
		}
		return durationFormatted;
	}
	
	
	/**
	 * Calculates the time fragments for years, month etc.
	 * @param duration the duration
	 */
	private Vector<Integer> calculateTimeFragments(long duration) {
		
		Vector<Integer> countingVector = new Vector<Integer>();
		long workDuration = duration;
		
		for (int fragment=0; fragment<this.factorVector.size(); fragment++) {
			
			// --- Count the number of years, months, days etc. in this duration -------- 
			int noOfFragments = this.calculateTimeFragment(workDuration, this.factorVector.get(fragment));
			countingVector.add(noOfFragments);
			
			// --- Reduce the amount of milliseconds in the duration -------------------- 
			workDuration = workDuration - (noOfFragments * this.factorVector.get(fragment));
			
		}
		
		// --- End of the factor vector (seconds) reached: finalise ---------------------
		countingVector.add((int) workDuration);
		
		return countingVector;
		
	}
	
	/**
	 * Calculates the number of time fragments for a special factor.
	 *
	 * @param duration the duration
	 * @param factor the factor
	 * @return the number of time fragments for a given factor 
	 */
	private int calculateTimeFragment(long duration, long factor) {
		if (duration<factor) {
			return 0;
		} else {
			float nFrags = (float) duration / (float) factor;
			return (int) nFrags;
		}
	}
	
}
