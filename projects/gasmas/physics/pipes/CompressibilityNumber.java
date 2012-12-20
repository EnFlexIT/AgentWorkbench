package gasmas.physics.pipes;

import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcExpression;
import agentgui.math.calculation.CalcParameterNotSetException;
/**
 * Calculates the compressibility number using Cerbe formula 2.1.16a 
 * @author Nils
 *
 */
public class CompressibilityNumber extends CalcExpression {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -9023181353792373553L;
	/**
	 * The initial preasure
	 */
	private CalcExpression initialPreasure;
	
	/**
	 * Constructor
	 * @param initialPreasure The initial preasure
	 */
	public CompressibilityNumber(CalcExpression initialPreasure){
		this.initialPreasure = initialPreasure;
	}
	/**
	 * Getter
	 * @return The initial preasure
	 */
	public CalcExpression getInitialPreasure() {
		return initialPreasure;
	}
	/**
	 * Setter
	 * @param initialPreasure The initial preasure
	 */
	public void setInitialPreasure(CalcExpression initialPreasure) {
		this.initialPreasure = initialPreasure;
	}

	@Override
	public double getValue() throws CalcExeption {
		if(initialPreasure == null){
			throw new CalcParameterNotSetException();
		}else{
			return 1 - initialPreasure.getValue() / 450e5;
		}
	}

}
