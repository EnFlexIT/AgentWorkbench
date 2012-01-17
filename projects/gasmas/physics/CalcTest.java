package gasmas.physics;

import gasmas.physics.pipes.PipeFrictionCoefficient;
import agentgui.math.calculation.CalcConstant;
import agentgui.math.calculation.CalcExeption;

/**
 * Temporary class for the first CalcExpression tests.
 * @author Nils
 */
public class CalcTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PipeFrictionCoefficient pfc0 = new PipeFrictionCoefficient(new CalcConstant(2500000));
		
		try {
			System.out.println("Result " + pfc0.getValue() );
			
		} catch (CalcExeption e) {
			e.printStackTrace();
		}
		
		

	}

}
