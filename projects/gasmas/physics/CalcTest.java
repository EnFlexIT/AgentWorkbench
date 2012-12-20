package gasmas.physics;

import gasmas.physics.pipes.PipeFrictionCoefficient;
import gasmas.physics.pipes.Reynolds;
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
		
		CalcConstant fluidVelocity = new CalcConstant(10.0);
		CalcConstant pipeDiameter = new CalcConstant(0.6);
		CalcConstant kinematicViscosity = new CalcConstant(0.0000002135);
		CalcConstant pipeRoughness = new CalcConstant(0.02);
		
		Reynolds reynolds = new Reynolds(fluidVelocity, pipeDiameter, kinematicViscosity);
		PipeFrictionCoefficient pfc0 = new PipeFrictionCoefficient(reynolds, pipeRoughness, pipeDiameter);

		try {
			System.out.println( System.currentTimeMillis());
			System.out.println("Reynolds " + reynolds.getValue() + "	" + System.currentTimeMillis());
			System.out.println("Lambda " + pfc0.getValue() + "			" + System.currentTimeMillis());
			
		} catch (CalcExeption e) {
			e.printStackTrace();
		}
		
		

	}

}
