package de.enflexit.common.swing;

import java.awt.Color;

/**
 * The enumeration AwbThemeColor.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public enum AwbThemeColor {

	RegularText(Color.BLACK, new Color(221, 221, 221)),
	
	ButtonTextRed(new Color(153, 0, 0), new Color(255, 0, 0)),
	ButtonTextGreen(new Color(0, 153, 0), new Color(0, 189, 0)),
	ButtonTextBlue(new Color(0, 0, 153), new Color(76, 135, 200)),
	
	LinkTextColor(new Color(0, 0, 153), new Color(76, 135, 200)),
	
	Canvas_Background(Color.white, new Color(70, 73, 75)),
	
	Chart_Background(Color.white, new Color(70, 73, 75)),
	Chart_GridLine(Color.BLACK, Color.WHITE),
	Chart_Text(Color.BLACK, new Color(221, 221, 221)),
	
	
	Graph_Node(new Color(230, 230, 230), new Color(230, 230, 230)),
	Graph_Edge(new Color(80, 80, 80), new Color(230, 230, 230)),
	
	;
	
	
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
		return new AwbColor(this.getLightColor(), this.getDarkColor());
	}
	
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
	
}
