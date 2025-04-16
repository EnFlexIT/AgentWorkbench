package de.enflexit.common.images;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * The Class ImageHelper provides selected static help methods to work with images and icons.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ImageHelper {

	/**
	 * Converts an {@link Image} into a {@link BufferedImage}.
	 * @param image the image to convert
	 * @return the converted buffered image
	 */
	public static BufferedImage convertToBufferedImage(Image image) {
		
		// --- Is a buffered image already? -----------------------------------
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		
		// --- Create a BufferedImage -----------------------------------------
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return bufferedImage;
	}

	/**
	 * Replaces a specified color with another one in an image.
	 * @param image    The image
	 * @param oldColor The color that will be replaced
	 * @param newColor The new color
	 * @return The image
	 */
	public static BufferedImage exchangeColor(BufferedImage image, Color oldColor, Color newColor) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color currentColor = new Color(image.getRGB(x, y), true);
				if (currentColor.equals(oldColor)) {
					image.setRGB(x, y, newColor.getRGB());
				}
			}
		}
		return image;
	}
	/**
	 * Scales the buffered image by its height and it width with the specified scale multiplier.
	 * @param scrImage        the buffered image
	 * @param scaleMultiplier the scale multiplier
	 * @return the buffered image
	 */
	public static BufferedImage scaleBufferedImage(BufferedImage scrImage, int scaleMultiplier) {
		
		if (scrImage==null || scaleMultiplier==1) return scrImage;
		
	    BufferedImage scaledImage = null;
	    try {
	    	// --- Resize the source image ----------------
	    	int newWidth  = scrImage.getWidth() * scaleMultiplier;
	    	int newHeight = scrImage.getHeight() * scaleMultiplier;

	    	scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TRANSLUCENT);
	    	Graphics2D g2d = scaledImage.createGraphics();
	    	g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    	g2d.drawImage(scrImage, 0, 0, newWidth, newHeight, null);
	    	g2d.dispose();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			// -- Return source image as backup -----------
			scaledImage = scrImage;
		}
	    return scaledImage;
	} 	
	
}
