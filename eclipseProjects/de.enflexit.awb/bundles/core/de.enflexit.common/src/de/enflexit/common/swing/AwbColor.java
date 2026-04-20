package de.enflexit.common.swing;

import java.awt.Color;


/**
 * The Class AwbColor represents a color instance that enables to dynamically 
 * switch between a light and a dark color without exchanging the instance itself.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbColor extends Color {

	private static final long serialVersionUID = 2011285098965368891L;

	private final Color darkColor;
	
	/**
	 * Instantiates a new AwbColor.
	 *
	 * @param lightColor the light color
	 * @param darkColor the dark color
	 */
	public AwbColor(Color lightColor, Color darkColor) {
		super(lightColor.getRed(), lightColor.getGreen(), lightColor.getBlue());
		this.darkColor = darkColor;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Color#getAlpha()
	 */
	@Override
	public int getAlpha() {
		if (AwbLookAndFeelAdjustments.isDarkLookAndFeel()==true) {
			return this.darkColor.getAlpha();
		}
		return super.getAlpha();
	}
	/* (non-Javadoc)
	 * @see java.awt.Color#getRed()
	 */
	@Override
	public int getRed() {
		if (AwbLookAndFeelAdjustments.isDarkLookAndFeel()==true) {
			return this.darkColor.getRed();
		}
		return super.getRed();
	}
	/* (non-Javadoc)
	 * @see java.awt.Color#getGreen()
	 */
	@Override
	public int getGreen() {
		if (AwbLookAndFeelAdjustments.isDarkLookAndFeel()==true) {
			return this.darkColor.getGreen();
		}
		return super.getGreen();
	}
	/* (non-Javadoc)
	 * @see java.awt.Color#getBlue()
	 */
	@Override
	public int getBlue() {
		if (AwbLookAndFeelAdjustments.isDarkLookAndFeel()==true) {
			return this.darkColor.getBlue();
		}
		return super.getBlue();
	}
	/* (non-Javadoc)
	 * @see java.awt.Color#getRGB()
	 */
	@Override
	public int getRGB() {
		if (AwbLookAndFeelAdjustments.isDarkLookAndFeel()==true) {
			return this.darkColor.getRGB();
		}
		return super.getRGB();
	}
}
