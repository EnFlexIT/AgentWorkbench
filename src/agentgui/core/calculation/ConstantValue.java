package agentgui.core.calculation;

/**
 * This class implements konstant values
 * @author Nils
 *
 */
public class ConstantValue implements CalcExpression {
	/**
	 * The value of the constant.
	 */
	double value;
	
	public ConstantValue(double value){
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.gui.calculation.CalcExpression#getValue()
	 */
	@Override
	public double getValue() {
		return this.value;
	}
}
