package de.enflexit.common.linearization;

import java.util.TreeMap;

/**
 * The Class LinearFormulaMatch is used to weight the match of a formula when the
 * result value of a linearization is to be evaluated.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LinearFormulaMatch {

	public static final int PERFECT_MATCH = 2;
	public static final int MATCH = 1;
	public static final int NO_MATCH = -1;
	
	private LinearFormula linearFormula;
	private int matchWeight;

	
	/**
	 * Instantiates a new linear formula match.
	 * @param linearFormula the LinearFormula
	 */
	public LinearFormulaMatch(LinearFormula linearFormula) {
		this.linearFormula = linearFormula;
	}
	
	/**
	 * Returns the linear formula.
	 * @return the linear formula
	 */
	public LinearFormula getLinearFormula() {
		return linearFormula;
	}
	
	/**
	 * Returns the match weight of the local {@link LinearFormula}.
	 * @return the match weight
	 */
	public Integer getMatchWeight() {
		return matchWeight;
	}
	/**
	 * Sets the match weight.
	 * @param newMatchWeight the new match weight
	 */
	public void setMatchWeight(int newMatchWeight) {
		this.matchWeight = newMatchWeight;
	}
	/**
	 * Adds the specified value to the match weight.
	 * @param matchWeightToAdd the match weight to add
	 */
	public void addMatchWeight(int matchWeightToAdd) {
		this.matchWeight += matchWeightToAdd;
	}
	
	/**
     * Returns the calculated linearization result value for the specified variable values.
     *
     * @param variableValueTreeMap the variable values as TreeMap
     * @return the calculated result value
     */
	public Double getResult(TreeMap<String, Double> variableValueTreeMap) {
		return this.getLinearFormula().getResult(variableValueTreeMap);
	}
	
}
