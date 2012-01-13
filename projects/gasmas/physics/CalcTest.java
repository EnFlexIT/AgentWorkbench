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
		
		
		CalcFormula velo = new CalcFormula();
		velo.setFormula("v/3.6");
		velo.addParameter("v", new CalcConstant(72));
		
		
		
		Reynolds reynold = new Reynolds();
		reynold.addParameter("diameter", diameter);
		reynold.addParameter("kinematicViscosity", kineticViscosity);
		reynold.addParameter("fluidVelocity", velo);
		
		System.out.println(reynold.getParameters());
		
		try {
			System.out.println(reynold.getValue());
		} catch (CalcExeption e2) {
			e2.printStackTrace();
		}
		
				
		Reynolds re = new Reynolds();
		re.setDiameter(diameter);
		re.setFluidVelocity(velo);
		re.setKinematicViscosity(kineticViscosity);
		try {
			System.out.println(re.getValue());
		} catch (CalcExeption e2) {
			e2.printStackTrace();
		}
		
		Nikuradse ni = new Nikuradse();
		ni.setReynolds(re);
		try {
			System.out.println( ni.getValue() );
		} catch (CalcExeption e1) {
			e1.printStackTrace();
		}
		
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
