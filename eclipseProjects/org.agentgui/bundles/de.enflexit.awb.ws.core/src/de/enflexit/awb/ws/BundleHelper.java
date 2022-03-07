package de.enflexit.awb.ws;

import javax.swing.ImageIcon;


/**
 * The Class BunldeHelper provides static help methods.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BundleHelper {

private static final String imagePackage = "/icons/";
	
	/**
	 * Gets the image package location as String.
	 * @return the image package
	 */
	public static String getImagePackage() {
		return imagePackage;
	}
	/**
	 * Gets the image icon for the specified image.
	 *
	 * @param fileName the file name
	 * @return the image icon
	 */
	public static ImageIcon getImageIcon(String fileName) {
		String imagePackage = getImagePackage();
		ImageIcon imageIcon=null;
		try {
			imageIcon = new ImageIcon(BundleHelper.class.getResource((imagePackage + fileName)));
		} catch (Exception err) {
			System.err.println("Error while searching for image file '" + fileName + "' in " + imagePackage);
			err.printStackTrace();
		}	
		return imageIcon;
	}
	

	// ----------------------------------------------------
	// --- From here, debug print methods -----------------
	// ----------------------------------------------------
	/**
	 * System println.
	 *
	 * @param messenger the messenger object
	 * @param message the message
	 * @param isError the indicator, if the message describes an error
	 */
	public static void systemPrintln(Object messenger, String message, boolean isError) {
		message = "[" + messenger.getClass().getSimpleName() + "] " + message;
		if (isError==true) {
			System.err.println(message);
		} else {
			System.out.println(message);
		}
	}
	
}
