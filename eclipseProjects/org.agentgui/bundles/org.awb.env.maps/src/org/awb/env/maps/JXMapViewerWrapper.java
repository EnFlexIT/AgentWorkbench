package org.awb.env.maps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

public class JXMapViewerWrapper extends JXMapViewer {

	private static final long serialVersionUID = -4089096441137343303L;

	private int height;

	private int width;

	private Insets insets;

	
	public JXMapViewerWrapper() {
		this(new Insets(0, 0, 0, 0), null);
	}
	
	public JXMapViewerWrapper(Dimension dim) {
		this(new Insets(0, 0, 0, 0), dim);
	}

	/**
	 * Instantiates a new JX map viewer wrapper.
	 *
	 * @param insets the insets
	 * @param height the height
	 * @param width  the width
	 */
	public JXMapViewerWrapper(Insets insets, Dimension dim) {
		if (width < 0 && height < 0) {
			throw new IllegalArgumentException("Height and width must be not negative");
		}
		this.setInsets(insets);
		if (dim!=null) {
			this.setBounds(dim);
		}
		this.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
		this.setDrawTileBorders(true);
		try {
			URL url = new URL("file:D:\\Sonstiges\\Entwicklung\\personal-workspace\\MapTiles\\src\\resources\\loading.gif");
			this.setLoadingImage(ImageIO.read(url));
		} catch (Exception ex) {
			System.out.println("could not load 'loading.png'");
			BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			g2.setColor(Color.black);
			g2.fillRect(0, 0, 16, 16);
			g2.dispose();
			this.setLoadingImage(img);
		}
		this.setAddressLocation(new GeoPosition(51.46469597167675, 7.007373381386001)); // Essen UDE campus

	}
	
	public void setBounds(Rectangle rectangle) {
		super.setBounds(rectangle);
		setHeight((int)rectangle.getHeight());
		setWidth((int) rectangle.getWidth());
	}
	
	public void setBounds(Dimension dim) {
		setBounds(new Rectangle(dim));
	}

	public void paint(Graphics g) {
		this.paintComponent(g);
	}
	
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	
	public Insets getInsets() {
		return insets;
	}
	/**
	 * Sets the insets.
	 * @param insets the new insets
	 */
	public void setInsets(Insets insets) {
		this.insets = insets;
	}

}
