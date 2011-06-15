package gasmas.physics;

import agentgui.math.calculation.CalculationException;
import agentgui.math.calculation.ConstantValue;
import agentgui.math.calculation.Formula;

/**
 * Temporary class for the first CalcExpression tests.
 * @author Nils
 */
public class CalcTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConstantValue velocity = new ConstantValue(20);
		ConstantValue diameter = new ConstantValue(0.9);
		ConstantValue kineticViscosity = new ConstantValue(14.9E-6);
		
		Reynolds re = new Reynolds();
		re.setDiameter(diameter);
		re.setFluidVelocity(velocity);
		re.setKinematicViscosity(kineticViscosity);
		
		Nikuradse ni = new Nikuradse();
		ni.setReynolds(re);
		
		Formula test = new Formula();
		test.setFormula("a+b/c");
		test.addParameter("a", new ConstantValue(5));
		test.addParameter("b", new ConstantValue(0.666));
		test.addParameter("c", ni);
		
		try {
			System.out.println(test.getValue());
		} catch (CalculationException e) {
			e.printStackTrace();
		}

	}

}
