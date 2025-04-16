package de.enflexit.common.linearization;

/**
 * The Class LinearizationBoundaries.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LinearizationBoundaries {

	/**
	 * Returns the lower boundary value for the specified variable.
	 *
	 * @param variableID the variable ID
	 * @return the lower boundary value
	 */
	public double getLowerBoundaryValue(String variableID) {
		return Linearization.DEFAULT_DOUBLE_VALUE_MIN;
	}
	
	/**
	 * Returns the upper boundary value for the specified variable.
	 *
	 * @param variableID the variable ID
	 * @return the upper boundary value
	 */
	public double getUpperBoundaryValue(String variableID) {
		return Linearization.DEFAULT_DOUBLE_VALUE_MAX;
	}
	
}
