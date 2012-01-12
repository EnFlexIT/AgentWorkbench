package gasmas.physics.pipes;

import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcExpression;
import agentgui.math.calculation.CalcParameterNotSetException;

/**
 * This class calculates the Reynolds number (Cerbe Gl 4.2.5)
 * @author Nils
 *
 */
public class Reynolds extends CalcExpression {
	
	private CalcExpression diameter;
	private CalcExpression kinematicViscosity;
	private CalcExpression fluidVelocity;

	
	
	@Override
	public double getValue() throws CalcExeption {
		if(diameter == null || kinematicViscosity == null || fluidVelocity == null){
			throw new CalcParameterNotSetException();
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
