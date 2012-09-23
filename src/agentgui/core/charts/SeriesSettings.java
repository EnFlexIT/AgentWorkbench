package agentgui.core.charts;

import java.awt.Color;

public class SeriesSettings {
	private String label;
	private Color color;
	private float lineWIdth;

	/**
	 * Default constructor
	 */
	public SeriesSettings() {
	}
	/**
	 * @param label
	 * @param color
	 * @param lineWIdth
	 */
	public SeriesSettings(String label, Color color, float lineWIdth) {
		super();
		this.label = label;
		this.color = color;
		this.lineWIdth = lineWIdth;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * @return the lineWIdth
	 */
	public float getLineWIdth() {
		return lineWIdth;
	}
	/**
	 * @param lineWIdth the lineWIdth to set
	 */
	public void setLineWIdth(float lineWIdth) {
		this.lineWIdth = lineWIdth;
	}
}
