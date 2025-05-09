package de.enflexit.common.swing;

import java.awt.Color;

/**
 * The enumeration AwbThemeColor.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public enum AwbThemeColor {

	Canvas_Background(Color.white, Color.black),
	
	ButtonTextRed(new Color(153, 0, 0), new Color(255, 0, 0)),
	ButtonTextGreen(new Color(0, 153, 0), new Color(0, 189, 0)),
	ButtonTextBlue(new Color(0, 0, 153), new Color(76, 135, 200)),
	
	LinkTextColor(new Color(0, 0, 153), new Color(76, 135, 200));
	
	
	private final Color lightColor;
	private final Color darkColor;
	
	/**
	 * Instantiates a new AwbThemeColor.
	 *
	 * @param lightColor the light color
	 * @param darkColor the dark color
	 */
	AwbThemeColor(Color lightColor, Color darkColor) {
		this.darkColor = darkColor;
		this.lightColor = lightColor;
	}
	/**
	 * Returns the light color.
	 * @return the light color
	 */
	public Color getLightColor() {
		return lightColor;
	}
	/**
	 * Returns the dark color.
	 * @return the dark color
	 */
	public Color getDarkColor() {
		return darkColor;
	}
	/**
	 * With respect to the current theme (light, dark, legacy), return the color to be used.
	 * @return the color
	 */
	public Color getColor() {
		if (AwbLookAndFeelAdjustments.isDarkLookAndFeel()==true) {
			return this.getDarkColor();
		}
		return this.getLightColor();
	}
}
