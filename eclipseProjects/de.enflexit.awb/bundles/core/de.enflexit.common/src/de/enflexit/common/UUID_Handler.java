package de.enflexit.common;

import java.security.SecureRandom;

/**
 * The Class UUID_Handler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class UUID_Handler {

	private static final SecureRandom RANDOM = new SecureRandom();
	private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

	/**
	 * Generate a short UUID ID with the length of 10 characters.
	 * @return the string
	 */
	public static String generateShortUUID() {
	    return UUID_Handler.generateShortUUID(10);
	}
	/**
	 * Generate an String ID with a length of the number of the specified characters.
	 * @return the string
	 */
	public static String generateShortUUID(int numberOfChars) {
		if (numberOfChars<=0) return null;
		return RANDOM.ints(numberOfChars, 0, CHARS.length()).collect(StringBuilder::new,(sb, i) -> sb.append(CHARS.charAt(i)),StringBuilder::append).toString();
	}
	
}
