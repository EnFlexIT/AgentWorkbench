package agentgui.graphEnvironment.controller;

import java.io.File;

/**
 * Classes that should work as import components for the GraphEnvironmentController must implement this interface. 
 * @author Nils
 *
 */
public interface GraphFileLoader2 {
	/**
	 * This method loads the graph graph from the file and translates it into a JUNG graph. 
	 * @param graphFile The file containing the graph definition.
	 * @return The JUNG graph.
	 */
	public GridModel2 loadGraphFromFile(File graphFile);
	/**
	 * Returns the extension of the file type the GraphFileLoader can handle
	 * @return The file extension
	 */
	public String getGraphFileExtension();
	/**
	 * Returns a type string used for GraphFileLoader selection
	 * @return The type String
	 */
	public String getTypeString();
}
