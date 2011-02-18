package agentgui.core.calculation;

/**
 * Interface for calculable expressions.
 * @author Nils
 *
 */
public interface CalcExpression {
	
	/**
	 * Gets the constant's value.
	 * @return the value
	 */
	public double getValue() throws CalculationException;
}
