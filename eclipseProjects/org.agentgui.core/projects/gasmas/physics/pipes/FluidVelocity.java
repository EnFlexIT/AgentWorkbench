package gasmas.physics.pipes;

import agentgui.math.calculation.CalcConstant;
import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcExpression;
import agentgui.math.calculation.CalcParameterNotSetException;

/**
 * Calculates the fluid velocity using Cerbe formula 4.2.2
 * @author Nils
 *
 */
public class FluidVelocity extends CalcExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = 896839078700630853L;
	
	// Parameters
	private CalcExpression diameter;
	private CalcExpression volumeFlow;
	private CalcExpression normPreasure;
	private CalcExpression realPreasure;
	private CalcExpression normTemperature;
	private CalcExpression realTemperature;
	
	public FluidVelocity(CalcExpression diameter, CalcExpression volumeStream, CalcExpression realPreasure, CalcExpression realTemperature){
		this.diameter = diameter;
		this.volumeFlow = volumeStream;
		this.realPreasure = realPreasure;
		this.realTemperature = realTemperature;
		this.normTemperature = new CalcConstant(273);		// Norm temperature 273 K
		this.normPreasure = new CalcConstant(1.01325e5); 	// Norm preasure 1.01325 bar
	}
	
	/**
	 * @return the diameter
	 */
	public CalcExpression getDiameter() {
		return diameter;
	}

	/**
	 * @param diameter the diameter to set
	 */
	public void setDiameter(CalcExpression diameter) {
		this.diameter = diameter;
	}

	/**
	 * @return the volumeStream
	 */
	public CalcExpression getVolumeFlow() {
		return volumeFlow;
	}

	/**
	 * @param volumeFlow the volumeStream to set
	 */
	public void setVolumeFlow(CalcExpression volumeFlow) {
		this.volumeFlow = volumeFlow;
	}

	/**
	 * @return the realPreasure
	 */
	public CalcExpression getRealPreasure() {
		return realPreasure;
	}

	/**
	 * @param realPreasure the realPreasure to set
	 */
	public void setRealPreasure(CalcExpression realPreasure) {
		this.realPreasure = realPreasure;
	}

	/**
	 * @return the realTemperature
	 */
	public CalcExpression getRealTemperature() {
		return realTemperature;
	}

	/**
	 * @param realTemperature the realTemperature to set
	 */
	public void setRealTemperature(CalcExpression realTemperature) {
		this.realTemperature = realTemperature;
	}

	@Override
	public double getValue() throws CalcExeption {
		if( volumeFlow == null  || realPreasure == null || realTemperature == null){
			throw new CalcParameterNotSetException();
		}else{
			double circleArea = Math.pow(diameter.getValue(),2) * Math.PI / 4;
			return (volumeFlow.getValue() * normPreasure.getValue() * realTemperature.getValue())/ (circleArea * realPreasure.getValue() * normTemperature.getValue() * 3600);
		}
	}

}
