package gasmas.physics.pipes;

import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcExpression;
import agentgui.math.calculation.CalcFormula;
import agentgui.math.calculation.CalcParameterNotSetException;

/**
 * This class implements the Nikuradse Formula (see Cerbe Gl 4.2.9)
 * @author Nils
 *
 */
public class Nikuradse extends CalcFormula {

	private static final long serialVersionUID = -8141049028616495617L;
	
	private CalcExpression reynolds;
	
	
	
	/**
	 * @param reynolds the reynolds to set
	 */
	public void setReynolds(CalcExpression reynolds) {
		this.reynolds = reynolds;
	}
	
	@Override
	public double getValue() throws CalcExeption {
		if(reynolds == null){
			throw new CalcParameterNotSetException();
		}
		return (0.0032 + 0.221 * Math.pow(reynolds.getValue(), -0.237));
	}

}
