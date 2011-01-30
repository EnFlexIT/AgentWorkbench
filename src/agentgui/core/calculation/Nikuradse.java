package agentgui.core.calculation;

/**
 * This class implements the Nikuradse Formula (see Cerbe Gl 4.2.9)
 * @author Nils
 *
 */
public class Nikuradse implements CalcExpression {
	
	/**
	 * @param reynolds the reynolds to set
	 */
	public void setReynolds(CalcExpression reynolds) {
		this.reynolds = reynolds;
	}

	CalcExpression reynolds;
	
	@Override
	public double getValue() throws ParameterNotSetException {
		if(reynolds == null){
			throw new ParameterNotSetException();
		}
		return (0.0032 + 0.221 * Math.pow(reynolds.getValue(), -0.237));
	}

}
