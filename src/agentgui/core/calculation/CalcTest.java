package agentgui.core.calculation;

/**
 * Temporary class for the first CalcExpression tests.
 * @author Nils
 */
public class CalcTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConstantValue velo = new ConstantValue(20);
		ConstantValue dia = new ConstantValue(0.9);
		ConstantValue kinVisc = new ConstantValue(14.9E-6);
		
		Reynolds re = new Reynolds();
		re.setDiameter(dia);
		re.setFluidVelocity(velo);
		re.setKinematicViscosity(kinVisc);
		
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
