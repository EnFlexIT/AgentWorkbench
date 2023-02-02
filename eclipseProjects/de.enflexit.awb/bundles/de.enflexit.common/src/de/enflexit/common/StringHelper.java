package de.enflexit.common;

/**
 * The Class StringHelper provides helper methods for string, such as isEqual.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class StringHelper {

	/**
	 * Checks if the specified strings are equal or equal strings.<br>
	 * Comparing <code>null</code> and <code>null</code> will return <code>true</code><br>.
	 * Comparing <code>null</code> and 'anyValue' will return <code>false</code><br>.
	 * Otherwise, the equal method of the {@link String} class will be executed and return accordingly.
	 *
	 * @param string1 the first string to compare
	 * @param string2 the second string to compare
	 * @return true, if is equal string
	 * 
	 * @see String#equals(Object)
	 */
	public static boolean isEqualString(String string1, String string2) {
		if (string1==null & string2==null) {
			return true;
		} else if (string1==null & string2!=null) {
			return false;
		} else if (string1!=null & string2==null) {
			return false;
		}
		return string1.equals(string2);
	}
	
}
