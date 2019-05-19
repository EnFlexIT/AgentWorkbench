package org.awb.env.networkModel.maps;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.settings.LayoutSettings;

/**
 * The Class MapSettings is used to adjust the graphical representation of a
 * {@link NetworkModel} if the {@link LayoutSettings} are set to a 'geographical layout'.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MapSettings", propOrder = {
    "utmLongitudeZone",
    "utmLatitudeZone",
    "mapScale",
    "showMapTiles",
    "mapTileTransparency"
})
public class MapSettings implements Serializable {

	private static final long serialVersionUID = 760729234063485505L;
	
	public enum MapScale {
		m(1, "1 m"),
		m10(10, "10 m"),
		m100(100, "100 m"),
		km(1000, "1 km");
		
		private final double scaleDivider;
		private final String scaleDescription;
		
		private MapScale(double scaleDivider, String scaleDescription) {
			this.scaleDivider = scaleDivider;
			this.scaleDescription = scaleDescription;
		}
		public double getScaleDivider() {
			return scaleDivider;
		}
		public double getScaleDividerInvers() {
			return 1.0 / ((double)scaleDivider);
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return this.scaleDescription;
		}
		
	}
	
	// --- Define variables and default values ------------
	private int utmLongitudeZone = 32;
	private String utmLatitudeZone = "U";
	private MapScale mapScale = MapScale.m;
	
	private boolean showMapTiles = true;
	private int mapTileTransparency = 0;
	
	
	public int getUTMLongitudeZone() {
		return utmLongitudeZone;
	}
	public void setUTMLongitudeZone(int lngZone) {
		this.utmLongitudeZone = lngZone;
	}

	
	public String getUTMLatitudeZone() {
		return utmLatitudeZone;
	}
	public void setUTMLatitudeZone(String latZone) {
		this.utmLatitudeZone = latZone;
	}

	
	public MapScale getMapScale() {
		return mapScale;
	}
	public void setMapScale(MapScale mapScale) {
		this.mapScale = mapScale;
	}
	
	
	public boolean isShowMapTiles() {
		return showMapTiles;
	}
	public void setShowMapTiles(boolean showMapTiles) {
		this.showMapTiles = showMapTiles;
	}
	

	public int getMapTileTransparency() {
		return mapTileTransparency;
	}
	public void setMapTileTransparency(int mapTileTransparency) {
		this.mapTileTransparency = mapTileTransparency;
	}
	
}
