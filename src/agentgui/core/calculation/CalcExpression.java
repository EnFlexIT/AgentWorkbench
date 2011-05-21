package agentgui.core.calculation;

/**
 * Interface for calculable expressions.
 * @author Nils
 *
 */
public interface CalcExpression {
	
	/**
	 * Gets the result of the calculation.
	 * @return the value
	 */
	public double getValue() throws CalculationException;
}
