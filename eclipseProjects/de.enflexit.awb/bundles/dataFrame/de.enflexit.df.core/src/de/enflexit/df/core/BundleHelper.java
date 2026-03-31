package de.enflexit.df.core;

import javax.swing.ImageIcon;

import de.enflexit.common.swing.AwbThemeImageIcon;

/**
 * The Class BundleHelper provides some static help methods to be used within the bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
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
	
	/**
	 * Creates a themed icon, using the provided URLs for light and dark mode images
	 * 
	 * @param lightModeImageURL the light mode image name
	 * @param darkModeImageURL the dark mode image nema
	 * @return the themed icon
	 */
	public static  AwbThemeImageIcon getThemedIcon(String lightModeImage, String darkModeImage) {
		ImageIcon lightModeIcon = BundleHelper.getImageIcon(lightModeImage); 
		ImageIcon darkModeIcon = BundleHelper.getImageIcon(darkModeImage);
		return new AwbThemeImageIcon(lightModeIcon, darkModeIcon);
	}
}
