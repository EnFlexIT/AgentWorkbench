package gasmas.physics.pipes;

import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcExpression;
import agentgui.math.calculation.CalcParameterNotSetException;
/**
 * Calculates the preasure loss for a pipe 
 * @author Nils
 *
 */
public class PipePreasureLoss extends CalcExpression{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 9077387906331047779L;
	
	// Parameters
	private CalcExpression initialPreasure;
	private CalcExpression pipeFrictionCoefficient;
	private CalcExpression pipeLength;
	private CalcExpression pipeDiameter;
	private CalcExpression realGasDensity;
	private CalcExpression fluidVelocity;
	private CalcExpression compressibilityNumber;
	
	/**
	 * @return All parameters set?
	 */
	private boolean checkParams(){
		if(
				initialPreasure == null
				|| pipeFrictionCoefficient == null
				|| pipeLength == null
				|| pipeDiameter == null
				|| realGasDensity == null
				|| fluidVelocity == null
				|| compressibilityNumber == null
			){
			return false;
		}
		return true;
	}
	
	


	/**
	 * @return the initialPreasure
	 */
	public CalcExpression getInitialPreasure() {
		return initialPreasure;
	}




	/**
	 * @param initialPreasure the initialPreasure to set
	 */
	public void setInitialPreasure(CalcExpression initialPreasure) {
		this.initialPreasure = initialPreasure;
	}




	/**
	 * @return the pipeFrictionCoefficient
	 */
	public CalcExpression getPipeFrictionCoefficient() {
		return pipeFrictionCoefficient;
	}




	/**
	 * @param pipeFrictionCoefficient the pipeFrictionCoefficient to set
	 */
	public void setPipeFrictionCoefficient(CalcExpression pipeFrictionCoefficient) {
		this.pipeFrictionCoefficient = pipeFrictionCoefficient;
	}




	/**
	 * @return the pipeLength
	 */
	public CalcExpression getPipeLength() {
		return pipeLength;
	}




	/**
	 * @param pipeLength the pipeLength to set
	 */
	public void setPipeLength(CalcExpression pipeLength) {
		this.pipeLength = pipeLength;
	}




	/**
	 * @return the pipeDiameter
	 */
	public CalcExpression getPipeDiameter() {
		return pipeDiameter;
	}




	/**
	 * @param pipeDiameter the pipeDiameter to set
	 */
	public void setPipeDiameter(CalcExpression pipeDiameter) {
		this.pipeDiameter = pipeDiameter;
	}




	/**
	 * @return the realGasDensity
	 */
	public CalcExpression getRealGasDensity() {
		return realGasDensity;
	}




	/**
	 * @param realGasDensity the realGasDensity to set
	 */
	public void setRealGasDensity(CalcExpression realGasDensity) {
		this.realGasDensity = realGasDensity;
	}




	/**
	 * @return the fluidVelocity
	 */
	public CalcExpression getFluidVelocity() {
		return fluidVelocity;
	}




	/**
	 * @param fluidVelocity the fluidVelocity to set
	 */
	public void setFluidVelocity(CalcExpression fluidVelocity) {
		this.fluidVelocity = fluidVelocity;
	}




	/**
	 * @return the compressibilityNumber
	 */
	public CalcExpression getCompressibilityNumber() {
		return compressibilityNumber;
	}




	/**
	 * @param compressibilityNumber the compressibilityNumber to set
	 */
	public void setCompressibilityNumber(CalcExpression compressibilityNumber) {
		this.compressibilityNumber = compressibilityNumber;
	}




	@Override
	public double getValue() throws CalcExeption {
		if(checkParams()){
			double lambda = pipeFrictionCoefficient.getValue();
			double l = pipeLength.getValue();
			double d = pipeDiameter.getValue();
			double ro = realGasDensity.getValue();
			double p = initialPreasure.getValue();
			double w = fluidVelocity.getValue();
			double k = compressibilityNumber.getValue();
			
			double radicand = 1 - lambda * ( l / d ) * ( ro / p ) * Math.pow(w, 2) * k;
			
			return p * ( 1 - Math.pow(radicand, 0.5) );
		}else{
			throw new CalcParameterNotSetException();
		}
	}
	
}

