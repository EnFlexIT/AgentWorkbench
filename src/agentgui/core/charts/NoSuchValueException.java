package agentgui.core.charts;

public class NoSuchValueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4772210832834082586L;
	
	public NoSuchValueException(){
		super("The value you're trying to access could not be found in the series");
	}

}
