package de.enflexit.expression.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * The Class ExpressionFunctions.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionFunctions {

	private static final String ALL_FUNCTIONS = "All Functions";
	
	private TreeMap<String, ArrayList<String>> functionTreeMap;
	
	/**
	 * Returns all known expression functions in a tree map, structured by groups.
	 * @return the function tree map
	 */
	public TreeMap<String, ArrayList<String>> getFunctionTreeMap() {
		if (functionTreeMap==null) {
			functionTreeMap = new TreeMap<>();
			this.fillFunctionTreeMap();
		}
		return functionTreeMap;
	}
	/**
	 * Builds the templates tree map.
	 */
	private void fillFunctionTreeMap(){
		
		ArrayList<String> timeSeriesExpressions = new ArrayList<>();
		timeSeriesExpressions.add("TS-Min");
		timeSeriesExpressions.add("TS-Max");
		timeSeriesExpressions.add("TS-Avg");
		this.getFunctionTreeMap().put("Time Series-Functions", timeSeriesExpressions);

		ArrayList<String> arrayExpressions = new ArrayList<>();
		arrayExpressions.add("Min");
		arrayExpressions.add("Max");
		arrayExpressions.add("Avg");
		this.getFunctionTreeMap().put("Array-Functions", arrayExpressions);
		
		// --- Concluding group that displays all functions / constants at once ---------
		ArrayList<String> allExpressions = new ArrayList<>();
		allExpressions.addAll(timeSeriesExpressions);
		allExpressions.addAll(arrayExpressions);
		this.getFunctionTreeMap().put(ALL_FUNCTIONS, allExpressions);
	}

	/**
	 * Returns all functions know as list.
	 * @return the function list
	 */
	public List<String> getFunctionList() {
		return this.getFunctionTreeMap().get(ALL_FUNCTIONS);
	}
	
	
}
