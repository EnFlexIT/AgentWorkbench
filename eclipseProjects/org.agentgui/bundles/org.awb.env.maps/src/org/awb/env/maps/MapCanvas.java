package org.awb.env.maps;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
//import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Tile;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.TileListener;
import org.jxmapviewer.viewer.empty.EmptyTileFactory;

public class MapCanvas extends JXMapViewer{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1695571506526902968L;

	private int zoomLevel = 1;

    private Point2D center = new Point2D.Double(0, 0);

    private static boolean DRAW_TILE_BORDERS = true;
    
    private TileFactory factory;
    
//    private Painter<? super MapPainter> overlay;
    
    private GeoPosition addressLocation;
    
    private boolean designTime;

    private Image loadingImage;

    private boolean restrictOutsidePanning = true;
    private boolean horizontalWrapped = true;
    private boolean infiniteMapRendering = true;

    /**
     * If true, panning with the mouse should take place. If false, panning should not happen. Does not disable
     * explicit setting of position via {@link setCenter}.
     */
    private boolean panningEnabled = true;
    
    
	

	private int height;
    
    private int width;

    private Insets insets;

    
    public MapCanvas(Rectangle dim) {
    	this(new Insets(0,0,0,0),(int) dim.getHeight(),(int) dim.getWidth());
    }
    
	public MapCanvas(Insets insets, int height,int width) {
        factory = new EmptyTileFactory();
        
        if(width < 0 && height < 0) {
        	throw new IllegalArgumentException("Height and width must be not negative");
        }
        setInsets(insets);
        setHeight(height);
        setWidth(width);
        setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));

        // make a dummy loading image
        try
        {
			URL url = new URL("file:D:\\Sonstiges\\Entwicklung\\personal-workspace\\MapTiles\\src\\resources\\loading.gif");
            this.setLoadingImage(ImageIO.read(url));
        }
        catch (Exception ex)
        {
            System.out.println("could not load 'loading.png'");
            BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setColor(Color.black);
            g2.fillRect(0, 0, 16, 16);
            g2.dispose();
            this.setLoadingImage(img);
        }
         setAddressLocation(new GeoPosition(51.46469597167675, 7.007373381386001)); // Essen UDE campus
	}
	
	
	public void paint(Graphics g) {
		this.paintMap(g);
	}
	
	private void paintMap(Graphics g) {
        if (isDesignTime())
        {
            // do nothing
        }
        else
        {
            int z = getZoom();
            Rectangle viewportBounds = getViewportBounds();
            drawMapTiles(g, z, viewportBounds);
//            drawOverlays(z, g, viewportBounds);
        }

	}
	
	
	 /**
     * Indicate that the component is being used at design time, such as in a visual editor like NetBeans' Matisse
     * @param b indicates if the component is being used at design time
     */
    
    public void setDesignTime(boolean b)
    {
        this.designTime = b;
    }

    /**
     * Indicates whether the component is being used at design time, such as in a visual editor like NetBeans' Matisse
     * @return boolean indicating if the component is being used at design time
     */
    public boolean isDesignTime()
    {
        return designTime;
    }
    
    
    
    
    /**
     * Draw the map tiles. This method is for implementation use only.
     * @param g Graphics
     * @param zoom zoom level to draw at
     * @param viewportBounds the bounds to draw within
     */
    protected void drawMapTiles(final Graphics g, final int zoom, Rectangle viewportBounds)
    {
        int size = getTileFactory().getTileSize(zoom);
        Dimension mapSize = getTileFactory().getMapSize(zoom);

        // calculate the "visible" viewport area in tiles
        int numWide = viewportBounds.width / size + 2;
        int numHigh = viewportBounds.height / size + 2;

        // TilePoint topLeftTile = getTileFactory().getTileCoordinate(
        // new Point2D.Double(viewportBounds.x, viewportBounds.y));
        TileFactory tileFactory = getTileFactory();
        TileFactoryInfo info = tileFactory.getInfo();

        // number of tiles in x direction
        int tpx = (int) Math.floor(viewportBounds.getX() / info.getTileSize(0));
        // number of tiles in y direction
        int tpy = (int) Math.floor(viewportBounds.getY() / info.getTileSize(0));
        // TilePoint topLeftTile = new TilePoint(tpx, tpy);

        Rectangle clipBounds = g.getClipBounds();
        // p("top tile = " + topLeftTile);
        // fetch the tiles from the factory and store them in the tiles cache
        // attach the tileLoadListener
        for (int x = 0; x <= numWide; x++)
        {
            for (int y = 0; y <= numHigh; y++)
            {
                int itpx = x + tpx;// topLeftTile.getX();
                int itpy = y + tpy;// topLeftTile.getY();
                // TilePoint point = new TilePoint(x + topLeftTile.getX(), y + topLeftTile.getY());
                // only proceed if the specified tile point lies within the area being painted
                if (clipBounds.intersects(
                        new Rectangle(itpx * size - viewportBounds.x, itpy * size - viewportBounds.y, size, size)))
                {
                    Tile tile = tileFactory.getTile(itpx, itpy, zoom);
                    int ox = ((itpx * tileFactory.getTileSize(zoom)) - viewportBounds.x);
                    int oy = ((itpy * tileFactory.getTileSize(zoom)) - viewportBounds.y);

                    // if the tile is off the map to the north/south, then just don't paint anything
                    if (!isTileOnMap(itpx, itpy, mapSize))
                    {
                        if (isOpaque())
                        {
                            g.setColor(getBackground());
                            g.fillRect(ox, oy, size, size);
                        }
                    }
                    else if (tile.isLoaded())
                    {
                        g.drawImage(tile.getImage(), ox, oy, null);
                    }
                    else
                    {
                        Tile superTile = null;

                        // Use tile at higher zoom level with 200% magnification and if we are not already at max resolution
                        if (zoom < info.getMaximumZoomLevel()) {
                            superTile = tileFactory.getTile(itpx / 2, itpy / 2, zoom + 1);
                        }

                        if ( superTile != null && superTile.isLoaded())
                        {
                            int offX = (itpx % 2) * size / 2;
                            int offY = (itpy % 2) * size / 2;
                            g.drawImage(superTile.getImage(), ox, oy, ox + size, oy + size, offX, offY, offX + size / 2, offY + size / 2, null);
                        }
                        else
                        {
                            int imageX = (tileFactory.getTileSize(zoom) - getLoadingImage().getWidth(null)) / 2;
                            int imageY = (tileFactory.getTileSize(zoom) - getLoadingImage().getHeight(null)) / 2;
                            g.setColor(Color.GRAY);
                            g.fillRect(ox, oy, size, size);
                           
                            g.drawImage(getLoadingImage(), ox + imageX, oy + imageY, null);
                        }
                    }
                    if (isDrawTileBorders())
                    {

                        g.setColor(Color.black);
                        g.drawRect(ox, oy, size, size);
                        g.drawRect(ox + size / 2 - 5, oy + size / 2 - 5, 10, 10);
                        g.setColor(Color.white);
                        g.drawRect(ox + 1, oy + 1, size, size);

                        String text = itpx + ", " + itpy + ", " + getZoom();
                        g.setColor(Color.BLACK);
                        g.drawString(text, ox + 10, oy + 30);
                        g.drawString(text, ox + 10 + 2, oy + 30 + 2);
                        g.setColor(Color.WHITE);
                        g.drawString(text, ox + 10 + 1, oy + 30 + 1);
                    }
                }
            }
        }
    }
    
    private boolean isTileOnMap(int x, int y, Dimension mapSize)
    {
        return (y >= 0 && y < mapSize.getHeight()) &&
                  (isInfiniteMapRendering() || x >= 0 && x < mapSize.getWidth());
    }
    
    /**
     * Returns the bounds of the viewport in pixels. This can be used to transform points into the world bitmap
     * coordinate space.
     * @return the bounds in <em>pixels</em> of the "view" of this map
     */
    public Rectangle getViewportBounds()
    {
        return calculateViewportBounds(getCenter());
    }

    private Rectangle calculateViewportBounds(Point2D centr)
    {
        Insets insets = getInsets();
//        calculate the "visible" viewport area in pixels
        int viewportWidth = getWidth() - insets.left - insets.right;
        int viewportHeight = getHeight() - insets.top - insets.bottom;
//         int viewportWidth = getWidth();
//         int viewportHeight = getHeight();
        double viewportX = (centr.getX() - viewportWidth / 2);
        double viewportY = (centr.getY() - viewportHeight / 2);
        return new Rectangle((int) viewportX, (int) viewportY, viewportWidth, viewportHeight);
    }
    
    
    /**
     * Set the current zoom level
     * @param zoom the new zoom level
     */
    public void setZoom(int zoom)
    {
        if (zoom == this.zoomLevel)
        {
            return;
        }

        TileFactory tileFactory = getTileFactory();
        TileFactoryInfo info = tileFactory.getInfo();
        // don't repaint if we are out of the valid zoom levels
        if (info != null && (zoom < info.getMinimumZoomLevel() || zoom > info.getMaximumZoomLevel()))
        {
            return;
        }

        // if(zoom >= 0 && zoom <= 15 && zoom != this.zoom) {
        int oldzoom = this.zoomLevel;
        Point2D oldCenter = getCenter();
        Dimension oldMapSize = tileFactory.getMapSize(oldzoom);
        this.zoomLevel = zoom;

        Dimension mapSize = tileFactory.getMapSize(zoom);

        setCenter(new Point2D.Double(oldCenter.getX() * (mapSize.getWidth() / oldMapSize.getWidth()), oldCenter.getY()
                * (mapSize.getHeight() / oldMapSize.getHeight())));

       	repaint();
    }
    
    /**
     * Gets the current zoom level
     * @return the current zoom level
     */
    public int getZoom()
    {
        return this.zoomLevel;
    }

    /**
     * Gets the current address location of the map. This property does not change when the user pans the map. This
     * property is bound.
     * @return the current map location (address)
     */
    public GeoPosition getAddressLocation()
    {
        return addressLocation;
    }

    /**
     * Gets the current address location of the map
     * @param addressLocation the new address location
     */
    public void setAddressLocation(GeoPosition addressLocation)
    {
        GeoPosition old = getAddressLocation();
        this.addressLocation = addressLocation;
        setCenter(getTileFactory().geoToPixel(addressLocation, getZoom()));

        firePropertyChange("addressLocation", old, getAddressLocation());
        repaint();
    }
    
    /**
     * Re-centers the map to have the current address location be at the center of the map, accounting for the map's
     * width and height.
     */
    public void recenterToAddressLocation()
    {
        setCenter(getTileFactory().geoToPixel(getAddressLocation(), getZoom()));
        repaint();
    }

    /**
     * Indicates if the tile borders should be drawn. Mainly used for debugging.
     * @return the value of this property
     */
    public boolean isDrawTileBorders()
    {
        return DRAW_TILE_BORDERS;
    }

    /**
     * A property indicating the center position of the map
     * @param geoPosition the new property value
     */
    public void setCenterPosition(GeoPosition geoPosition)
    {
        GeoPosition oldVal = getCenterPosition();
        setCenter(getTileFactory().geoToPixel(geoPosition, zoomLevel));
        repaint();
        GeoPosition newVal = getCenterPosition();
        firePropertyChange("centerPosition", oldVal, newVal);
    }

    
    /**
     * A property indicating the center position of the map
     * @return the current center position
     */
    public GeoPosition getCenterPosition()
    {
        return getTileFactory().pixelToGeo(getCenter(), zoomLevel);
    }

    /**
     * Get the current factory
     * @return the current property value
     */
    public TileFactory getTileFactory()
    {
        return factory;
    }
    
    /**
     * Set the current tile factory (must not be <code>null</code>)
     * @param factory the new property value
     */
    public void setTileFactory(TileFactory factory)
    {
        if (factory == null)
            throw new NullPointerException("factory must not be null");

        this.factory.removeTileListener(tileLoadListener);
        this.factory.dispose();

        this.factory = factory;
        this.setZoom(factory.getInfo().getDefaultZoomLevel());

        factory.addTileListener(tileLoadListener);

        repaint();
    }

    /**
     * A property for an image which will be display when an image is still loading.
     * @return the current property value
     */
    public Image getLoadingImage()
    {
        return loadingImage;
    }

    /**
     * A property for an image which will be display when an image is still loading.
     * @param loadingImage the new property value
     */
    public void setLoadingImage(Image loadingImage)
    {
        this.loadingImage = loadingImage;
    }

    /**
     * Gets the current pixel center of the map. This point is in the global bitmap coordinate system, not as lat/longs.
     * @return the current center of the map as a pixel value
     */
    public Point2D getCenter()
    {
        return center;
    }
    
    
    /**
     * Sets the new center of the map in pixel coordinates.
     * @param center the new center of the map in pixel coordinates
     */
    public void setCenter(Point2D center)
    {
        Point2D old = this.getCenter();

        double centerX = center.getX();
        double centerY = center.getY();

        Dimension mapSize = getTileFactory().getMapSize(getZoom());
        int mapHeight = (int) mapSize.getHeight() * getTileFactory().getTileSize(getZoom());
        int mapWidth = (int) mapSize.getWidth() * getTileFactory().getTileSize(getZoom());

        if (isRestrictOutsidePanning())
        {
            Insets insets = getInsets();
            int viewportHeight = getHeight() - insets.top - insets.bottom;
            int viewportWidth = getWidth() - insets.left - insets.right;

            // don't let the user pan over the top edge
            Rectangle newVP = calculateViewportBounds(center);
            if (newVP.getY() < 0)
            {
                centerY = viewportHeight / 2;
            }

            // don't let the user pan over the left edge
            if (!isHorizontalWrapped() && newVP.getX() < 0)
            {
                centerX = viewportWidth / 2;
            }

            // don't let the user pan over the bottom edge
            if (newVP.getY() + newVP.getHeight() > mapHeight)
            {
                centerY = mapHeight - viewportHeight / 2;
            }

            // don't let the user pan over the right edge
            if (!isHorizontalWrapped() && (newVP.getX() + newVP.getWidth() > mapWidth))
            {
                centerX = mapWidth - viewportWidth / 2;
            }

            // if map is to small then just center it vert
            if (mapHeight < newVP.getHeight())
            {
                centerY = mapHeight / 2;// viewportHeight/2;// - mapHeight/2;
            }

            // if map is too small then just center it horiz
            if (!isHorizontalWrapped() && mapWidth < newVP.getWidth())
            {
                centerX = mapWidth / 2;
            }
        }

        // If center is outside (0, 0,mapWidth, mapHeight)
        // compute modulo to get it back in.
        {
            centerX = centerX % mapWidth;
            centerY = centerY % mapHeight;

            if (centerX < 0)
                centerX += mapWidth;

            if (centerY < 0)
                centerY += mapHeight;
        }

        GeoPosition oldGP = this.getCenterPosition();
        this.center = new Point2D.Double(centerX, centerY);
        firePropertyChange("center", old, this.center);
        firePropertyChange("centerPosition", oldGP, this.getCenterPosition());
        repaint();
    }
    
    /**
     * Calculates a zoom level so that all points in the specified set will be visible on screen. This is useful if you
     * have a bunch of points in an area like a city and you want to zoom out so that the entire city and it's points
     * are visible without panning.
     * @param positions A set of GeoPositions to calculate the new zoom from
     */
    public void calculateZoomFrom(Set<GeoPosition> positions)
    {
        // u.p("calculating a zoom based on: ");
        // u.p(positions);
        if (positions.size() < 2)
        {
            return;
        }

        int zoom = getZoom();
        Rectangle2D rect = generateBoundingRect(positions, zoom);
        // Rectangle2D viewport = map.getViewportBounds();
        int count = 0;
        while (!getViewportBounds().contains(rect))
        {
            // u.p("not contained");
            Point2D centr = new Point2D.Double(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            GeoPosition px = getTileFactory().pixelToGeo(centr, zoom);
            // u.p("new geo = " + px);
            setCenterPosition(px);
            count++;
            if (count > 30)
                break;

            if (getViewportBounds().contains(rect))
            {
                // u.p("did it finally");
                break;
            }
            zoom = zoom + 1;
            if (zoom > 15) //TODO: use maxZoom of the tfInfo
            {
                break;
            }
            setZoom(zoom);
            rect = generateBoundingRect(positions, zoom);
        }
    }
    
    /**
     * Zoom and center the map to a best fit around the input GeoPositions.
     * Best fit is defined as the most zoomed-in possible view where both
     * the width and height of a bounding box around the positions take up
     * no more than maxFraction of the viewport width or height respectively.
     * @param positions A set of GeoPositions to calculate the new zoom from
     * @param maxFraction the maximum fraction of the viewport that should be covered
     */
    public void zoomToBestFit(Set<GeoPosition> positions, double maxFraction)
    {
        if (positions.isEmpty())
            return;

        if (maxFraction <= 0 || maxFraction > 1)
            throw new IllegalArgumentException("maxFraction must be between 0 and 1");

        TileFactory tileFactory = getTileFactory();
        TileFactoryInfo info = tileFactory.getInfo();

        if(info == null)
            return;

        // set to central position initially
        GeoPosition centre = computeGeoCenter(positions);
        setCenterPosition(centre);

        if (positions.size() == 1)
            return;

        // repeatedly zoom in until we find the first zoom level where either the width or height
        // of the points takes up more than the max fraction of the viewport

        // start with zoomed out at maximum
        int bestZoom = info.getMaximumZoomLevel();

        Rectangle2D viewport = getViewportBounds();

        Rectangle2D bounds = generateBoundingRect(positions, bestZoom);

        // is this zoom still OK?
        while (bestZoom >= info.getMinimumZoomLevel() &&
               bounds.getWidth() < viewport.getWidth() * maxFraction &&
               bounds.getHeight() < viewport.getHeight() * maxFraction)
        {
            bestZoom--;
            bounds = generateBoundingRect(positions, bestZoom);
        }

        setZoom(bestZoom + 1);
    }

    private Rectangle2D generateBoundingRect(final Set<GeoPosition> positions, int zoom)
    {
        Point2D point1 = getTileFactory().geoToPixel(positions.iterator().next(), zoom);
        Rectangle2D rect = new Rectangle2D.Double(point1.getX(), point1.getY(), 0, 0);

        for (GeoPosition pos : positions)
        {
            Point2D point = getTileFactory().geoToPixel(pos, zoom);
            rect.add(point);
        }
        return rect;
    }

    private GeoPosition computeGeoCenter(final Set<GeoPosition> positions)
    {
        double sumLat = 0;
        double sumLon = 0;

        for (GeoPosition pos : positions)
        {
            sumLat += pos.getLatitude();
            sumLon += pos.getLongitude();
        }
        double avgLat = sumLat / positions.size();
        double avgLon = sumLon / positions.size();
        return new GeoPosition(avgLat, avgLon);
    }

    // a property change listener which forces repaints when tiles finish loading
    private TileListener tileLoadListener = new TileListener()
    {
        public void tileLoaded(Tile tile)
        {
                if (tile.getZoom() == getZoom())
                {
                    repaint();
                }
            }

    };

    /**
     * @return true if panning is restricted or not
     */
    public boolean isRestrictOutsidePanning()
    {
        return restrictOutsidePanning;
    }

    /**
     * @param restrictOutsidePanning set if panning is restricted or not
     */
    public void setRestrictOutsidePanning(boolean restrictOutsidePanning)
    {
        this.restrictOutsidePanning = restrictOutsidePanning;
    }

    /**
     * @return true if horizontally wrapped or not
     */
    public boolean isHorizontalWrapped()
    {
        return horizontalWrapped;
    }

    /**
     * Side note: This setting is ignored when  horizontaklWrapped is set to true.
     *
     * @param infiniteMapRendering true when infinite map rendering should be enabled
     */
    public void setInfiniteMapRendering(boolean infiniteMapRendering)
    {
        this.infiniteMapRendering = infiniteMapRendering;
    }

    /**
     * @return true if infinite map rendering is enabled
     */
    public boolean isInfiniteMapRendering()
    {
        return horizontalWrapped || infiniteMapRendering;
    }

    /**
     * @param horizontalWrapped true if horizontal wrap is enabled
     */
    public void setHorizontalWrapped(boolean horizontalWrapped)
    {
        this.horizontalWrapped = horizontalWrapped;
    }

    /**
     * Converts the specified GeoPosition to a point in the JXMapViewer's local coordinate space. This method is
     * especially useful when drawing lat/long positions on the map.
     * @param pos a GeoPosition on the map
     * @return the point in the local coordinate space of the map
     */
    public Point2D convertGeoPositionToPoint(GeoPosition pos)
    {
        // convert from geo to world bitmap
        Point2D pt = getTileFactory().geoToPixel(pos, getZoom());
        // convert from world bitmap to local
        Rectangle bounds = getViewportBounds();
        return new Point2D.Double(pt.getX() - bounds.getX(), pt.getY() - bounds.getY());
    }

    /**
     * Converts the specified Point2D in the JXMapViewer's local coordinate space to a GeoPosition on the map. This
     * method is especially useful for determining the GeoPosition under the mouse cursor.
     * @param pt a point in the local coordinate space of the map
     * @return the point converted to a GeoPosition
     */
    public GeoPosition convertPointToGeoPosition(Point2D pt)
    {
        // convert from local to world bitmap
        Rectangle bounds = getViewportBounds();
        Point2D pt2 = new Point2D.Double(pt.getX() + bounds.getX(), pt.getY() + bounds.getY());

        // convert from world bitmap to geo
        GeoPosition pos = getTileFactory().pixelToGeo(pt2, getZoom());
        return pos;
    }


    /**
     * Enables or disables panning.
     * Useful for performing selections on the map.
     * @param enabled if true, panning is enabled (the default), if false, panning is disabled
     */
    public void setPanEnabled(boolean enabled)
    {
        this.panningEnabled = enabled;
    }

    /**
     * Returns whether panning is enabled. If it is disabled, panning should not occur. (Used primarily by {@link PanMouseInputListener}
     * @return true if panning is enabled
     */
    public boolean isPanningEnabled()
    {
        return this.panningEnabled;
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


	public void setInsets(Insets insets) {
		this.insets = insets;
	}



}
