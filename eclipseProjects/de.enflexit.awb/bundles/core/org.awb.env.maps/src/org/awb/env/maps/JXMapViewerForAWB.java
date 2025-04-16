package org.awb.env.maps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * The Class JXMapViewerForAWB.
 */
public class JXMapViewerForAWB extends JXMapViewer {

	private static final long serialVersionUID = -4089096441137343303L;

	/**
	 * Instantiates a new JXMapViewer for AWB.
	 */
	public JXMapViewerForAWB() {

		// --- Configure local file cache -------------------------------------
		File fileCacheDir = this.getFileCacheDirectory();
		FileBasedLocalCache localFileCache = new FileBasedLocalCache(fileCacheDir, true);

		// --- Initialize and configure tile factory --------------------------
		DefaultTileFactory tileFactory = new DefaultTileFactory(new OSMTileFactoryInfo());
		tileFactory.setThreadPoolSize(8);
		tileFactory.setLocalCache(localFileCache);
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
	
	/**
	 * Returns the file cache directory.
	 * @return the cache file directory
	 */
	private File getFileCacheDirectory() {
		
		// --- Get the applications configuration directory ---------
		File configDirectory = null;
		// --- Catch possible NullPointer in ConfigurationScope -----
		try {
			IScopeContext scopeContext = ConfigurationScope.INSTANCE;
			configDirectory = scopeContext.getLocation().toFile();
		} catch (Exception ex) { }
		
		if (configDirectory==null) return null;
		
		// --- Prepare cache directory path ------------------------- 
		Bundle myBundle = FrameworkUtil.getBundle(this.getClass());
		String cacheFilePath = configDirectory.getAbsolutePath() + File.separator + myBundle.getSymbolicName() + File.separator;
		File fileCacheDir = new File(cacheFilePath);
		if (fileCacheDir.exists()==false) {
			// --- Create the directory -----------------------------
			fileCacheDir.mkdir();
		}
		return fileCacheDir; 
	}
	
}
