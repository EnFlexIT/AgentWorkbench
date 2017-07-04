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

import java.io.Serializable;

/**
 * The Class TimeUnit describes the relationship between
 * a time increment and .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeUnit implements Serializable {

	private static final long serialVersionUID = 6691784575006563592L;

	private String unit = null;
	private int numberOfDigits = 1;
	private int minValue = 0;
	private int maxValue = 0;
	private long factorToMilliseconds = new Long(0);
	
	/**
	 * Instantiates a new time unit.
	 *
	 * @param unit the unit
	 * @param numberOfDigits the number of digits
	 */
	public TimeUnit(String unit, int numberOfDigits, int minValue, int maxValue, long factorToMilliseconds) {
		this.unit = unit;
		this.numberOfDigits = numberOfDigits;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.factorToMilliseconds = factorToMilliseconds;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.unit;
	}

	/**
	 * Sets the unit.
	 * @param unit the new unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * Returns the unit.
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the number of digits.
	 * @param numberOfDigits the new number of digits
	 */
	public void setNumberOfDigits(int numberOfDigits) {
		this.numberOfDigits = numberOfDigits;
	}
	/**
	 * Returns the number of digits.
	 * @return the number of digits
	 */
	public int getNumberOfDigits() {
		return numberOfDigits;
	}

	/**
	 * Sets the min value.
	 * @param minValue the new min value
	 */
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	/**
	 * Gets the min value.
	 * @return the min value
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * Sets the max value.
	 * @param maxValue the new max value
	 */
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	/**
	 * Gets the max value.
	 * @return the max value
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * Sets the factor to milliseconds.
	 * @param factorToMilliseconds the new factor to milliseconds
	 */
	public void setFactorToMilliseconds(long factorToMilliseconds) {
		this.factorToMilliseconds = factorToMilliseconds;
	}
	/**
	 * Gets the factor to milliseconds.
	 * @return the factor to milliseconds
	 */
	public long getFactorToMilliseconds() {
		return factorToMilliseconds;
	}
	
}
