package org.awb.env.networkModel.maps;

/**
 * The listener interface for receiving events from a {@link MapSettingsPanel}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * 
 * @see MapSettingsPanel
 */
public interface MapSettingsPanelListener {

	public enum MapSettingsChanged {
		UTM_Longitude,
		UTM_Latitude,
		MapScale,
		MapService,
	    MapTileTransparency
	}
	
	/**
	 * On changed MapSettings.
	 * @param valueChangedInMapSetting the value changed in map setting
	 */
	public void onChangedMapSettings(MapSettingsChanged valueChangedInMapSetting);
	
}
