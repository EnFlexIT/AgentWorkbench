package de.enflexit.expression.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * The Class ExpressionFunctions.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public enum ExpressionFunctions {
	
	MIN("MIN(<ARRAY>)", "Array-Functions"),
	MAX("MAX(<ARRAY>)", "Array-Functions"),
	AVG("AVG(<ARRAY>)", "Array-Functions")

	;
	
	private final String mask;
	private final String group;
	/**
	 * Instantiates a new expression functions.
	 * @param mask the mask
	 */
	private ExpressionFunctions(String mask, String group) {
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
	
	
	private static final String ALL_FUNCTIONS = "All Functions";

	private static TreeMap<String, ArrayList<String>> functionTreeMap;
	
	/**
	 * Returns all known expression functions in a tree map, structured by groups.
	 * @return the function tree map
	 */
	public static TreeMap<String, ArrayList<String>> getFunctionTreeMap() {
		if (functionTreeMap==null) {
			functionTreeMap = new TreeMap<>();
			ExpressionFunctions.fillFunctionTreeMap();
		}
		return functionTreeMap;
	}
	
	/**
	 * Return the list of function groups in an ordered manner.
	 * @return the function groups
	 */
	public static List<String> getFunctionGroups() {
		List<String> groupList = new ArrayList<>();
		for (ExpressionFunctions exFx : ExpressionFunctions.values()) {
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
	public static List<ExpressionFunctions> getGroupFunctions(String group) {
		
		List<ExpressionFunctions> groupList = new ArrayList<>();
		for (ExpressionFunctions exFx : ExpressionFunctions.values()) {
			if (exFx.getGroup().equals(group)==true) {
				groupList.add(exFx);
			}
		}
		Collections.sort(groupList, new Comparator<ExpressionFunctions>() {
			@Override
			public int compare(ExpressionFunctions ef1, ExpressionFunctions ef2) {
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
		
		List<String> groupList = ExpressionFunctions.getFunctionGroups();
		for (String group : groupList) {
			
			ArrayList<String> groupExpressions = new ArrayList<>();
			// --- Get all members of the Group -----------
			List<ExpressionFunctions> exFcktList = getGroupFunctions(group);
			for (ExpressionFunctions exFckt : exFcktList) {
				groupExpressions.add(exFckt.getMask());
			}
			if (groupExpressions.size()>0) {
				ExpressionFunctions.getFunctionTreeMap().put(group, groupExpressions);
				allExpressions.addAll(groupExpressions);
			}
		}
		if (allExpressions.size()>0) {
			getFunctionTreeMap().put(ALL_FUNCTIONS, allExpressions);
		}
	}

	/**
	 * Returns all functions know as list.
	 * @return the function list
	 */
	public static List<String> getFunctionList() {
		return ExpressionFunctions.getFunctionTreeMap().get(ALL_FUNCTIONS);
	}
	
	
}
