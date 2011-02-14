package agentgui.core.calculation;

import org.nfunk.jep.JEP;

public class JEPTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JEP parser = new JEP();
		parser.addStandardFunctions();
		parser.addVariable("x", 3);
		parser.addVariable("y", 4);
		parser.parseExpression("tan(sin(x)+cos(y))");
		System.out.println(parser.getValue());
	}

}
