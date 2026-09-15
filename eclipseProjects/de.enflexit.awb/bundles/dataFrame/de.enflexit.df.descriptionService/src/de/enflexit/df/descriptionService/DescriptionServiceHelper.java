package de.enflexit.df.descriptionService;

/**
 * Some static helper methods used by the description service extension.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DescriptionServiceHelper {
	private static final String REGEX_REMOVE_ALSO_AVAILABLE = "\\s\\(also available in:\\s.+\\)";
	
	/**
	 * Removes the "also available in..." suffix from the provided table name.
	 * @param tableName the table name
	 * @return the string
	 */
	public static String removeAlsoAvailableFromTableName(String tableName) {
		if (tableName==null) {
			return null;
		} else {
			return tableName.replaceAll(REGEX_REMOVE_ALSO_AVAILABLE, "");
		}
	}
}
