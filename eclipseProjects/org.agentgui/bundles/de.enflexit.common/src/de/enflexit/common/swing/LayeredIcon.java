package de.enflexit.common.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;

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
        Dimension d = new Dimension(getIconWidth(), getIconHeight());
		for (Icon icon : iconSet) {
			Dimension id = new Dimension(icon.getIconWidth(), icon.getIconHeight());
			int dx = (d.width - id.width)/2;
			int dy = (d.height - id.height)/2;
			icon.paintIcon(c, g, x+dx, y+dy);
		}
	}

	/**
	 * Adds a new Icon layer.
	 * @param icon the icon
	 */
	public void add(Icon icon) {
		iconSet.add(icon);
	}
	/**
	 * Removes the specified icon and its layer.
	 *
	 * @param icon the icon
	 * @return true, if successful
	 */
	public boolean remove(Icon icon) {
		return iconSet.remove(icon);
	}
	
}
