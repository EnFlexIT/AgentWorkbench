package de.enflexit.expression.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import de.enflexit.expression.ExpressionService;

/**
 * The Class ExpressionFunction.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public enum ExpressionFunction {
	
	MIN("MIN(<Array>)", "Array Functions"),
	MAX("MAX(<Array>)", "Array Functions"),
	AVG("AVG(<Array>)", "Array Functions"),
	SUM("SUM(<Array>)", "Array Functions"),
	TimeSeriesDiscretization("TimeSeriesDiscretization(<TimeSeries>,<TimeFrom>,<TimeTo>,<NumSteps>)", "Time Series Functions")

	;
	
	private final String mask;
	private final String group;
	
	/**
	 * Instantiates a new expression functions.
	 * @param mask the mask
	 */
	private ExpressionFunction(String mask, String group) {
		this.mask  = mask;
		this.group = group;
	}
	/**
	 * Gets the mask.
	 * @return the mask
	 */
	public String getMask() {
		return mask;
	}
	/**
	 * Gets the group.
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}
	
	// --------------------------------------------------------------
	// --- From here, some static help methods ----------------------
	// --------------------------------------------------------------
	
	private static final String ALL_FUNCTIONS = "All Functions";

	private static TreeMap<String, ArrayList<String>> functionTreeMap;
	
	/**
	 * Returns all known expression functions in a tree map, structured by groups.
	 * @return the function tree map
	 */
	public static TreeMap<String, ArrayList<String>> getFunctionTreeMap() {
		if (functionTreeMap==null) {
			functionTreeMap = new TreeMap<>();
			ExpressionFunction.fillFunctionTreeMap();
		}
		return functionTreeMap;
	}
	
	/**
	 * Return the list of function groups in an ordered manner.
	 * @return the function groups
	 */
	public static List<String> getFunctionGroups() {
		List<String> groupList = new ArrayList<>();
		for (ExpressionFunction exFx : ExpressionFunction.values()) {
			if (groupList.contains(exFx.getGroup())==false) {
				groupList.add(exFx.getGroup());
			}
		}
		Collections.sort(groupList);
		return groupList;
	}
	
	/**
	 * Returns all functions of the specified group in an ordered manner.
	 *
	 * @param group the group
	 * @return the group functions
	 */
	public static List<ExpressionFunction> getGroupFunctions(String group) {
		
		List<ExpressionFunction> groupList = new ArrayList<>();
		for (ExpressionFunction exFx : ExpressionFunction.values()) {
			if (exFx.getGroup().equals(group)==true) {
				groupList.add(exFx);
			}
		}
		Collections.sort(groupList, new Comparator<ExpressionFunction>() {
			@Override
			public int compare(ExpressionFunction ef1, ExpressionFunction ef2) {
				return ef1.getMask().compareTo(ef2.getMask());
			}
		});
		return groupList;
	}
	
	/**
	 * Builds the templates tree map.
	 */
	private static void fillFunctionTreeMap(){
		
		ArrayList<String> allExpressions = new ArrayList<>();
		
		for (String group : ExpressionFunction.getFunctionGroups()) {
			
			ArrayList<String> groupExpressions = new ArrayList<>();
			// --- Get all members of the Group -----------
			List<ExpressionFunction> exFcktList = ExpressionFunction.getGroupFunctions(group);
			for (ExpressionFunction exFckt : exFcktList) {
				groupExpressions.add(exFckt.getMask());
			}
			if (groupExpressions.size()>0) {
				ExpressionFunction.getFunctionTreeMap().put(group, groupExpressions);
				allExpressions.addAll(groupExpressions);
			}
		}
		if (allExpressions.size()>0) {
			getFunctionTreeMap().put(ALL_FUNCTIONS, allExpressions);
		}
	}

	
	/**
	 * Returns all functions know as list.
	 * 
	 * @return the function list
	 */
	public static List<String> getFunctionList() {
		
		List<String> functionList = new ArrayList<>();
		for (ExpressionFunction exFx : ExpressionFunction.values()) {
			functionList.add(exFx.name());
		}
		Collections.sort(functionList);
		return functionList;
	}
	/**
	 * Returns the expression function prefix, if the specified string starts with a function description.
	 * 
	 * @param expressionString the expression string
	 */
	public static String getExpressionFunctionPrefix(String expressionString) {
		
		String expressionWork = expressionString.toLowerCase();
		
		List<String> fList = ExpressionFunction.getFunctionList();
		for (String function : fList) {
			String functionWork = function.toLowerCase();
			if (expressionWork.startsWith(functionWork)==true) {
				return function;
			}
		}
		return null;
	}
	/**
	 * Returns the expression function that is defined by the expression string.
	 *
	 * @param expressionString the expression string
	 * @return the expression function
	 */
	public static ExpressionFunction getExpressionFunction(String expressionString) {
		
		String expPrefix = ExpressionFunction.getExpressionFunctionPrefix(expressionString);
		if (expPrefix!=null && expPrefix.isBlank()==false) {
			return ExpressionFunction.valueOf(expPrefix);
		}
		return null;
	}
	/**
	 * Checks if the specified expression string contains an expression function.
	 *
	 * @param expressionString the expression string
	 * @return true, if the specified string contains an expression function
	 */
	public static boolean containsExpressionFunction(String expressionString) {
		
		String expressionWork = expressionString.toLowerCase();
		
		List<String> fList = ExpressionFunction.getFunctionList();
		for (String function : fList) {
			if (expressionWork.contains(function.toLowerCase())==true) {
				int cut = expressionWork.indexOf(function.toLowerCase()) + function.length(); 
				// --- Get the next character and check for function delimiter ----------
				boolean isFunction = false;
				for (int i = cut; i < expressionWork.length(); i++) {
					char character = expressionWork.charAt(i);
					if (character==' ') {
						continue;
					} else if (character==ExpressionService.EXPRESSION_FUNCTION_OPENING_DELIMITER) {
						isFunction = true;
						break;
					} else {
						isFunction = false;
						break;
					}
				}
				return isFunction;
			}
		}
		return false;
	}
	
}
