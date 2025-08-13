package de.enflexit.common.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import de.enflexit.common.images.ImageHelper;

/**
 * The Class AwbThemeImageIcon enable to specify two ImageIcon instances as theme-dependent 
 * image icons. One for a light, the other for a dark theme.<br>
 * If no dark-theme image is specified, the specified light theme image will be converted into a negative
 * of it and will be used as dark-thme image.   
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbThemeImageIcon extends ImageIcon {

	private static final long serialVersionUID = 3546265878162117304L;
	
	private ImageIcon darkThemeImageIcon;

	/**
	 * Instantiates a new AWB theme image icon by generating a negative for the specified light theme image icon. 
	 *
	 * @param lightThemeImageIcon the light theme image icon
	 */
	public AwbThemeImageIcon(ImageIcon lightThemeImageIcon) {
		this(lightThemeImageIcon, null);
	}
	/**
	 * Instantiates a new AWB theme image icon.
	 *
	 * @param lightThemeImageIcon the light theme image icon
	 * @param darkThemeImageIcon the dark theme image icon
	 */
	public AwbThemeImageIcon(ImageIcon lightThemeImageIcon, ImageIcon darkThemeImageIcon) {
		super(lightThemeImageIcon.getImage(), lightThemeImageIcon.getDescription());
		if (darkThemeImageIcon!=null) {
			this.darkThemeImageIcon = darkThemeImageIcon;
		} else {
			this.darkThemeImageIcon = ImageHelper.getNegativeImage(lightThemeImageIcon);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ImageIcon#getImage()
	 */
	@Override
	public Image getImage() {
		if (AwbLookAndFeelAdjustments.isDarkLookAndFeel()==true) {
			return darkThemeImageIcon.getImage();
		}
		return super.getImage();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ImageIcon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		//super.paintIcon(c, g, x, y);
		if (this.getImageObserver() == null) {
			g.drawImage(this.getImage(), x, y, c);
		} else {
			g.drawImage(this.getImage(), x, y, this.getImageObserver());
		}
	}
	
}
