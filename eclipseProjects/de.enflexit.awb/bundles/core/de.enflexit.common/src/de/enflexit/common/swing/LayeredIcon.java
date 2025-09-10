package de.enflexit.common.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.enflexit.common.images.ImageHelper;

/**
 * An icon that is made up of a collection of Icons.
 * They are rendered in layers starting with the first
 * Icon added (from the constructor).
 * 
 * @author Tom Nelson (Copied from JUNG Framework)
 */
public class LayeredIcon extends ImageIcon {

	private static final long serialVersionUID = -4107095079072967310L;

	private Set<Icon> iconSet = new LinkedHashSet<Icon>();
	
	/**
	 * Instantiates a new layered icon.
	 * @param image the image
	 */
	public LayeredIcon(Image image) {
	    super(image);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ImageIcon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
        super.paintIcon(c, g, x, y);
		for (Icon icon : iconSet) {
			Dimension id = new Dimension(icon.getIconWidth(), icon.getIconHeight());
			int dx = (this.getIconWidth()  - id.width)/2;
			int dy = (this.getIconHeight() - id.height)/2;
			icon.paintIcon(c, g, x+dx, y+dy);
		}
	}

	/**
	 * Adds a new Icon layer.
	 * @param icon the icon
	 */
	public void add(Icon icon) {
		if (icon!=null) {
			iconSet.add(icon);
		}
	}
	/**
	 * Removes the specified icon and its layer.
	 *
	 * @param icon the icon
	 * @return true, if successful
	 */
	public boolean remove(Icon icon) {
		if (icon==null) return false;
		return iconSet.remove(icon);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ImageIcon#getImage()
	 */
	@Override
	public Image getImage() {
	
		BufferedImage imageOut = ImageHelper.convertToBufferedImage(super.getImage());
		
		for (Icon icon : iconSet) {
			
			if (icon instanceof ImageIcon) {
				
				Dimension id = new Dimension(icon.getIconWidth(), icon.getIconHeight());
				int dx = (this.getIconWidth()  - id.width)/2;
				int dy = (this.getIconHeight() - id.height)/2;
				
				ImageIcon iiLayer = (ImageIcon)icon;

				Graphics2D g2d = imageOut.createGraphics();
				g2d.drawImage(iiLayer.getImage(), dx, dy, null);
				g2d.dispose();
			}
		}
		return imageOut;
	}
	
}
