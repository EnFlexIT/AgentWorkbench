package de.enflexit.awb.ws.dynSiteApi.content;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * The Class ImageHelper.
 */
public class ImageHelper {

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
			imageIcon = new ImageIcon(ImageHelper.class.getResource((imagePackage + fileName)));
		} catch (Exception err) {
			System.err.println("Error while searching for image file '" + fileName + "' in " + imagePackage);
			err.printStackTrace();
		}	
		return imageIcon;
	}
	
	
	/**
	 * Returns the specified image as base 64 encoded string.
	 *
	 * @param fileName the file name
	 * @return the image icon base 64 encoded
	 */
	public static String getImageIconBase64Encoded(String fileName) {
		
		ImageIcon iIcon = getImageIcon(fileName);
		if (iIcon==null) return null;		
		
		String fileExtension = null;
		int i = fileName.lastIndexOf('.');
		if (i > 0) fileExtension = fileName.substring(i+1);
		
		String base64Image = null;
		try {

			BufferedImage bufferedImage = ImageIO.read(ImageHelper.class.getResource((imagePackage + fileName)));

			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, fileExtension, os);
			base64Image = Base64.getEncoder().encodeToString(os.toByteArray());			
					
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return base64Image;
	}
	
}
