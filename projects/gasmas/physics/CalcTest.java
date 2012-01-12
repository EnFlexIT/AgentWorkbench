package gasmas.physics;

import gasmas.physics.pipes.Nikuradse;
import gasmas.physics.pipes.Reynolds;
import agentgui.math.calculation.CalcConstant;
import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcFormula;

/**
 * Temporary class for the first CalcExpression tests.
 * @author Nils
 */
public class CalcTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CalcConstant velocity = new CalcConstant(20);
		CalcConstant diameter = new CalcConstant(0.9);
		CalcConstant kineticViscosity = new CalcConstant(14.9E-6);
		
		Reynolds re = new Reynolds();
		re.setDiameter(diameter);
		re.setFluidVelocity(velocity);
		re.setKinematicViscosity(kineticViscosity);
		
		Nikuradse ni = new Nikuradse();
		ni.setReynolds(re);
		
		CalcFormula test = new CalcFormula();
		test.setFormula("a+b/c");
		test.addParameter("a", new CalcConstant(5));
		test.addParameter("b", new CalcConstant(0.666));
		test.addParameter("c", ni);
		
		try {
			System.out.println(test.getValue());
		} catch (CalcExeption e) {
			e.printStackTrace();
		}

	}

}
