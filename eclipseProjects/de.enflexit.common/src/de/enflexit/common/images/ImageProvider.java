package de.enflexit.common.images;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;


/**
 * The Class ImageProvider enables static access to the images 
 * located in the current package.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ImageProvider {

	/**
	 * This enumeration 'knows' all images within the current package and can.
	 * be used as a selection tool within the class {@link ImageProvider}.
	 */
	public enum ImageFile {
		
		MB_Export_PNG("MB_Export.png"),
		MB_Import_PNG("MB_Import.png");
		
		private final String imageFileName;
		private ImageFile(final String imageFileName) {
			this.imageFileName = imageFileName;
		}
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return imageFileName;
		}
	}
	
	private static String packageName;
	/**
	 * Returns the current package name.
	 * @return the current package name
	 */
	private static String getCurrentPackageName() {
		if (packageName==null) {
			String pName = ImageProvider.class.getPackage().getName();
			packageName = "/" + pName.replaceAll("\\.", "/") + "/";
		}
		return packageName;
	}
	
	/**
	 * Returns an image out of the current package as ImageIcon.
	 * @param imageURL the image URL
	 * @return the internal image icon
	 */
	public static ImageIcon getImageIcon(URL imageURL) {
		return new ImageIcon(imageURL);
	}
	/**
	 * Returns an image out of the current package as ImageIcon.
	 * @param simpleImageFileName the simple image file name
	 * @return the internal image icon
	 */
	public static ImageIcon getImageIcon(String simpleImageFileName) {
		return getImageIcon(ImageProvider.class.getResource(getCurrentPackageName() + simpleImageFileName));
	}
	/**
	 * Returns an image out of the current package as ImageIcon.
	 * @param imageFileName the image file name
	 * @return the internal image icon
	 */
	public static ImageIcon getImageIcon(ImageFile imageFileName) {
		return getImageIcon(imageFileName.toString());
	}
	
	
	/**
	 * Returns an image out of the current package as {@link ImageIcon}.
	 * @param simpleImageFileName the simple image file name
	 * @return the internal image
	 */
	public static Image getImage(URL imageURL) {
		return getImageIcon(imageURL).getImage();
	}
	/**
	 * Returns an image out of the current package as {@link ImageIcon}.
	 * @param simpleImageFileName the simple image file name
	 * @return the internal image
	 */
	public static Image getImage(String simpleImageFileName) {
		return getImageIcon(simpleImageFileName).getImage();
	}
	/**
	 * Returns an image out of the current package as {@link ImageIcon}.
	 * @param imageFileName the image file name
	 * @return the internal image
	 */
	public static Image getImage(ImageFile imageFileName) {
		return getImageIcon(imageFileName).getImage();
	}

	
}
