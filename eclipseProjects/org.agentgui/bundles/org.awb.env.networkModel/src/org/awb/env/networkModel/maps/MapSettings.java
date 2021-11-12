package org.awb.env.networkModel.maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.settings.LayoutSettings;

import de.enflexit.common.ServiceFinder;

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
    "mapServiceName",
    "mapTileTransparency"
})
public class MapSettings implements Serializable, Cloneable {

	private static final long serialVersionUID = 760729234063485505L;
	
	public enum MapScale {
		s1x(1, "1 x"),
		s2x(2, "2 x"),
		s3x(3, "3 x"),
		s4x(4, "4 x"),
		s5x(5, "5 x"),
		s10x(10, "10 x"),
		s15x(15, "15 x"),
		s20x(20, "20 x"),
		s25x(25, "25 x");
		
		private final int scaleMultiplier;
		private final String scaleDescription;
		
		private MapScale(int scaleMultiplier, String scaleDescription) {
			this.scaleMultiplier = scaleMultiplier;
			this.scaleDescription = scaleDescription;
		}
		public int getScaleMultiplier() {
			return scaleMultiplier;
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
	private MapScale mapScale;
	
	private String mapServiceName;
	private int mapTileTransparency = 0;
	
	
	public static final String NO_MAP_SERVICE_SELECTION = "Don't use Map Service";
	private transient MapService mapService;
	
	
	
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
		if (mapScale==null) {
			mapScale = MapScale.s1x;
		}
		return mapScale;
	}
	public void setMapScale(MapScale mapScale) {
		this.mapScale = mapScale;
	}
	
	
	public String getMapServiceName() {
		return mapServiceName;
	}
	public void setMapServiceName(String mapServiceName) {
		this.mapServiceName = mapServiceName;
		this.setMapService(getMapService(mapServiceName));
	}
	
	
	public int getMapTileTransparency() {
		return mapTileTransparency;
	}
	public void setMapTileTransparency(int mapTileTransparency) {
		this.mapTileTransparency = mapTileTransparency;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected MapSettings clone() throws CloneNotSupportedException {
		return (MapSettings) super.clone();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==this) return true;
		if (!(compObj instanceof MapSettings)) return false;
		
		MapSettings msComp = (MapSettings) compObj;
		
		if (msComp.getUTMLongitudeZone()!=this.getUTMLongitudeZone()) return false;
		if (msComp.getUTMLatitudeZone().equals(this.getUTMLatitudeZone())==false) return false;
		
		if (msComp.getMapServiceName().equals(this.getMapServiceName())==false) return false;
		if (msComp.getMapScale().equals(this.getMapScale())==false) return false;
		if (msComp.getMapTileTransparency()!=this.getMapTileTransparency()) return false;
		
		return true;
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here, handling of the currently selected MapService ---------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Return the current {@link MapService}. if <code>Null</code>, it will be tried to load the 
	 * currently configured MapsService from the list of available services
	 * @return the map service
	 * 
	 * @see MapSettings#getMapServiceList()
	 * @see MapSettings#getMapService(String)
	 * @see #getMapServiceName()
	 */
	public MapService getMapService() {
		if (mapService==null) {
			if (this.getMapServiceName()!=null && this.getMapServiceName().isEmpty()==false) {
				mapService = getMapService(this.getMapServiceName());	
			}
		}
		return mapService;
	}
	/**
	 * Sets the current {@link MapService}.
	 * @param mapService the new map service
	 */
	public void setMapService(MapService mapService) {
		this.mapService = mapService;
	}
	
	/**
	 * Returns the list of registered {@link MapService}s.
	 * @return the map service list
	 */
	public static List<MapService> getMapServiceList() {
		return ServiceFinder.findServices(MapService.class);
	}
	/**
	 * Returns the {@link MapService} specified by the map service name or <code>Null</code>.
	 *
	 * @param mapServiceName the map service name
	 * @return the MapService found or <code>Null</code>
	 */
	public static MapService getMapService(String mapServiceName) {
		if (mapServiceName!=null && mapServiceName.isEmpty()==false) {
			List<MapService> mServiceList = getMapServiceList();
			for (int i = 0; i < mServiceList.size(); i++) {
				MapService ms = mServiceList.get(i);
				if (ms.getMapServiceName().equals(mapServiceName)==true) {
					return ms;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the MapService names as sorted list, where the first element is the indicator
	 * for not using a MapService.
	 *
	 * @param isSortList the indicator to sort the list
	 * @param insertNoMapServiceIndicator the insert no map service indicator
	 * @return the map service name list
	 */
	public static List<String> getMapServiceNameList() {
		return getMapServiceNameList(true, true);
	}
	/**
	 * Returns the map service name list.
	 *
	 * @param isSortList the indicator to sort the list
	 * @param insertNoMapServiceIndicator the insert no map service indicator
	 * @return the map service name list
	 */
	public static List<String> getMapServiceNameList(boolean isSortList, boolean insertNoMapServiceIndicator) {
		
		// --- Extract service names ----------------------
		List<String> mServiceNameList =  new ArrayList<>();
		List<MapService> mServiceList = getMapServiceList();
		for (int i = 0; i < mServiceList.size(); i++) {
			mServiceNameList.add(mServiceList.get(i).getMapServiceName()); 
		}
		
		// --- Sort ascending -----------------------------
		if (isSortList==true) {
			Collections.sort(mServiceNameList);
		}

		// --- insert no selection indicator --------------
		if (insertNoMapServiceIndicator==true) {
			mServiceNameList.add(0, NO_MAP_SERVICE_SELECTION);
		}
		return mServiceNameList;
	}

	
	
	
}
