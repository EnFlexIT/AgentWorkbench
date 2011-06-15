package gasmas.physics;

import agentgui.math.calculation.CalcExpression;
import agentgui.math.calculation.CalculationException;
import agentgui.math.calculation.ParameterNotSetException;

/**
 * This class calculates the Reynolds number (Cerbe Gl 4.2.5)
 * @author Nils
 *
 */
public class Reynolds implements CalcExpression {
	
	CalcExpression diameter;
	
	CalcExpression kinematicViscosity;
	
	CalcExpression fluidVelocity;

	@Override
	public double getValue() throws CalculationException {
		if(diameter == null || kinematicViscosity == null || fluidVelocity == null){
			throw new ParameterNotSetException();
		}
		return ((fluidVelocity.getValue() * diameter.getValue()) / kinematicViscosity.getValue());
	}

	/**
	 * @param diameter the diameter to set
	 */
	public void setDiameter(CalcExpression diameter) {
		this.diameter = diameter;
	}

	/**
	 * @param kinematicViscosity the kinematicViscosity to set
	 */
	public void setKinematicViscosity(CalcExpression kinematicViscosity) {
		this.kinematicViscosity = kinematicViscosity;
	}

	/**
	 * @param fluidVelocity the fluidVelocity to set
	 */
	public void setFluidVelocity(CalcExpression fluidVelocity) {
		this.fluidVelocity = fluidVelocity;
	}

}
