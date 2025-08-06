package de.enflexit.common.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JProgressBar;
import javax.swing.Painter;

/**
 * The Class AwbProgressBarPainter.
 */
public class AwbProgressBarPainter implements Painter<JProgressBar> {

	private Color light, dark, border;
	private GradientPaint gradPaint;

	/**
	 * Instantiates a new progress painter.
	 *
	 * @param light the light color
	 * @param dark the dark color
	 * @param border the border color
	 */
	public AwbProgressBarPainter(Color light, Color dark, Color border) {
		this.light = light;
		this.dark = dark;
		this.border = border; 
	}

	/* (non-Javadoc)
	 * @see javax.swing.Painter#paint(java.awt.Graphics2D, java.lang.Object, int, int)
	 */
	@Override
	public void paint(Graphics2D g, JProgressBar progressBar, int w, int h) {

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.gradPaint = new GradientPaint((w / 2.0f), 0, this.light, (w / 2.0f), (h / 2.0f), this.dark, true);
		g.setPaint(gradPaint);
		g.fillRect(2, 2, (w - 5), (h - 5));

		Color outline = this.border;
		g.setColor(outline);
		g.drawRect(2, 2, (w - 5), (h - 5));
		Color trans = new Color(outline.getRed(), outline.getGreen(), outline.getBlue(), 100);
		g.setColor(trans);
		g.drawRect(1, 1, (w - 3), (h - 3));
	}

}