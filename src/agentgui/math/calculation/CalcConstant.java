package agentgui.math.calculation;

/**
 * This class implements konstant values
 * @author Nils
 *
 */
public class CalcConstant extends CalcExpression {
	
	/**
	 * The value of the constant.
	 */
	double value;
	
	public CalcConstant(double value){
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
