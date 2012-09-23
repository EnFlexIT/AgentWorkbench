package agentgui.core.charts;
/**
 * This type of exception is thrown when trying to access a data series that does not exist in the (sub) data model
 * @author Nils
 */
public class NoSuchSeriesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5200624191049455250L;
	
	public NoSuchSeriesException(){
		super("The series you're trying to access could not be found in the model");
	}

}
