import org.nfunk.jep.*;

/**
 * An example class to test custom functions with JEP.
 */
class CustFunc {
	
	/**
	 * Constructor.
	 */
	public CustFunc() {

	}

	/**
	 * Main method. Create a new JEP object and parse an example expression
	 * that uses the SquareRoot function.
	 */
	public static void main(String args[]) {
		
		JEP parser = new JEP();        // Create a new parser
		String expr = "1 + half(2)";
		double value;
		
		System.out.println("Starting CustFunc...");
		parser.addStandardFunctions();
		parser.addStandardConstants();
		parser.addFunction("half", new Half()); // Add the custom function
		
		parser.parseExpression(expr);                 // Parse the expression
		if (parser.hasError()) {
			System.out.println("Error while parsing");
			System.out.println(parser.getErrorInfo());
			return;
		}
		
		value = parser.getValue();                    // Get the value
		if (parser.hasError()) {
			System.out.println("Error during evaluation");
			System.out.println(parser.getErrorInfo());
			return;
		}
		
		System.out.println(expr + " = " + value); // Print value
	}
}
