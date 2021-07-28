package org.awb.env.maps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * The Class JXMapViewerForAWB.
 */
public class JXMapViewerForAWB extends JXMapViewer {

	private static final long serialVersionUID = -4089096441137343303L;

	/**
	 * Instantiates a new JX map viewer for AWB.
	 */
	public JXMapViewerForAWB() {

		// --- Initialize and configure tile factory --------------------------
		DefaultTileFactory tileFactory = new DefaultTileFactory(new OSMTileFactoryInfo());
		tileFactory.setThreadPoolSize(8);
		// --- Caching is an open issue by now ------
		//tileFactory.setLocalCache(null);
		//tileFactory.setTileCache(null);
		this.setTileFactory(tileFactory);

		try {
			URL url = JXMapViewer.class.getResource("/images/loading.png");
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
	
	/**
	 * Sets the current bounds of the map visualization.
	 * @param dim the new bounds
	 */
	public void setBounds(Dimension dim) {
		this.setBounds(new Rectangle(dim));
	}

	/* (non-Javadoc)
	 * @see org.jxmapviewer.JXMapViewer#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
}
