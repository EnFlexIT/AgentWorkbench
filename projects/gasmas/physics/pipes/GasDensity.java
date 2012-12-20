package gasmas.physics.pipes;

import agentgui.math.calculation.CalcConstant;
import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcExpression;
import agentgui.math.calculation.CalcParameterNotSetException;
/**
 * Calculates the gas density using Cerbe formula 2.1.15b 
 * @author Nils
 *
 */
public class GasDensity extends CalcExpression {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 8409274717122261798L;
	// Parameters
	private CalcExpression normGasDensity;
	private CalcExpression normPreasure;
	private CalcExpression realPreasure;
	private CalcExpression normTemperature;
	private CalcExpression realTemperature;
	 
	 /**
	  * Constructor
	  * @param normGasDensity		// Gas density under norm conditions
	  * @param realPreasure			// The real preasure
	  * @param realTemperature		// The real temperature
	  */
	 public GasDensity(CalcExpression normGasDensity, CalcExpression realPreasure, CalcExpression realTemperature){
		 this.normGasDensity = normGasDensity;
		 this.realPreasure = realPreasure;
		 this.realTemperature = realTemperature;
		 this.normTemperature = new CalcConstant(273);		// Norm temperature 273 K
		 this.normPreasure = new CalcConstant(1.01325); 	// Norm preasure 1.01325 bar 
	 }
	
	 

	 
	/**
	 * @return the normGasDensity
	 */
	public CalcExpression getNormGasDensity() {
		return normGasDensity;
	}




	/**
	 * @param normGasDensity the normGasDensity to set
	 */
	public void setNormGasDensity(CalcExpression normGasDensity) {
		this.normGasDensity = normGasDensity;
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
		if( normGasDensity == null || realPreasure == null || realTemperature == null){
			throw new CalcParameterNotSetException();
		}else{
			return normGasDensity.getValue() * realPreasure.getValue() / normPreasure.getValue() * normTemperature.getValue() / realTemperature.getValue();
		}
	}

}
