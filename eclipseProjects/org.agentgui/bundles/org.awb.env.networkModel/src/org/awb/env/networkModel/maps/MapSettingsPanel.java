package org.awb.env.networkModel.maps;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.awb.env.networkModel.maps.MapSettings.MapScale;
import org.awb.env.networkModel.maps.MapSettingsPanelListener.MapSettingsChanged;

import de.enflexit.geography.coordinates.ui.JComboBoxUTMZoneLatitude;
import de.enflexit.geography.coordinates.ui.JComboBoxUTMZoneLongitude;

/**
 * The Class MapSettingsPanel allows to configure current {@link MapSettings} within its Panel.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MapSettingsPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -1942227397185713890L;

	private MapSettings mapSettings;

	private JLabel jLabelUTMZone;
	private JComboBoxUTMZoneLongitude jComboBoxUTMLongitude;
	private JComboBoxUTMZoneLatitude jComboBoxUTMLatitude;
	
	private JLabel jLabelScale;
	private JComboBoxMapScale jComboBoxMapScale;
	
	private JSeparator jSeparatorFirst;

	private JLabel jLabelMapService;
	private DefaultComboBoxModel<String> cbmMapServiceName;
	private JComboBox<String> jComboBoxMapServiceName;
	
	private JLabel jLabelTransparency;
	private JSlider jSliderTransparency;
	
	private Timer timeSliderChangeEvent;
	private boolean pauseActionListener;
	private boolean pauseSliderListener;
	
	private List<MapSettingsPanelListener> listener;
	
	
	/**
	 * Instantiates a new MapSettingsPanel.
	 */
	public MapSettingsPanel() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelUTMZone = new GridBagConstraints();
		gbc_jLabelUTMZone.insets = new Insets(5, 10, 0, 0);
		gbc_jLabelUTMZone.anchor = GridBagConstraints.WEST;
		gbc_jLabelUTMZone.gridx = 0;
		gbc_jLabelUTMZone.gridy = 0;
		add(getJLabelUTMZone(), gbc_jLabelUTMZone);
		GridBagConstraints gbc_jComboBoxUTMLongitude = new GridBagConstraints();
		gbc_jComboBoxUTMLongitude.insets = new Insets(5, 5, 0, 0);
		gbc_jComboBoxUTMLongitude.gridx = 1;
		gbc_jComboBoxUTMLongitude.gridy = 0;
		add(getJComboBoxUTMLongitude(), gbc_jComboBoxUTMLongitude);
		GridBagConstraints gbc_jComboBoxUTMLatitude = new GridBagConstraints();
		gbc_jComboBoxUTMLatitude.insets = new Insets(5, 0, 0, 0);
		gbc_jComboBoxUTMLatitude.gridx = 2;
		gbc_jComboBoxUTMLatitude.gridy = 0;
		add(getJComboBoxUTMLatitude(), gbc_jComboBoxUTMLatitude);
		GridBagConstraints gbc_jLabelScale = new GridBagConstraints();
		gbc_jLabelScale.insets = new Insets(5, 0, 0, 0);
		gbc_jLabelScale.anchor = GridBagConstraints.WEST;
		gbc_jLabelScale.gridx = 4;
		gbc_jLabelScale.gridy = 0;
		add(getJLabelScale(), gbc_jLabelScale);
		GridBagConstraints gbc_jComboBoxMapScale = new GridBagConstraints();
		gbc_jComboBoxMapScale.anchor = GridBagConstraints.EAST;
		gbc_jComboBoxMapScale.insets = new Insets(5, 5, 0, 10);
		gbc_jComboBoxMapScale.gridx = 5;
		gbc_jComboBoxMapScale.gridy = 0;
		add(getJComboBoxMapScale(), gbc_jComboBoxMapScale);
		GridBagConstraints gbc_jSeparatorFirst = new GridBagConstraints();
		gbc_jSeparatorFirst.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparatorFirst.insets = new Insets(5, 10, 5, 10);
		gbc_jSeparatorFirst.gridwidth = 6;
		gbc_jSeparatorFirst.gridx = 0;
		gbc_jSeparatorFirst.gridy = 1;
		add(getJSeparatorFirst(), gbc_jSeparatorFirst);
		GridBagConstraints gbc_jLabelMapService = new GridBagConstraints();
		gbc_jLabelMapService.insets = new Insets(5, 10, 2, 0);
		gbc_jLabelMapService.gridx = 0;
		gbc_jLabelMapService.gridy = 2;
		add(getJLabelMapService(), gbc_jLabelMapService);
		GridBagConstraints gbc_jComboBoxMapService = new GridBagConstraints();
		gbc_jComboBoxMapService.insets = new Insets(5, 5, 0, 10);
		gbc_jComboBoxMapService.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxMapService.gridwidth = 5;
		gbc_jComboBoxMapService.gridx = 1;
		gbc_jComboBoxMapService.gridy = 2;
		add(getJComboBoxMapServiceName(), gbc_jComboBoxMapService);
		GridBagConstraints gbc_jLabelTransparency = new GridBagConstraints();
		gbc_jLabelTransparency.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelTransparency.gridwidth = 3;
		gbc_jLabelTransparency.anchor = GridBagConstraints.WEST;
		gbc_jLabelTransparency.gridx = 0;
		gbc_jLabelTransparency.gridy = 3;
		add(getJLabelTransparency(), gbc_jLabelTransparency);
		GridBagConstraints gbc_jSliderTransparency = new GridBagConstraints();
		gbc_jSliderTransparency.insets = new Insets(5, 10, 0, 10);
		gbc_jSliderTransparency.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSliderTransparency.gridwidth = 6;
		gbc_jSliderTransparency.gridx = 0;
		gbc_jSliderTransparency.gridy = 4;
		add(getJSliderTransparency(), gbc_jSliderTransparency);
	}
	
	private JLabel getJLabelUTMZone() {
		if (jLabelUTMZone == null) {
			jLabelUTMZone = new JLabel("UTM Zone:");
			jLabelUTMZone.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelUTMZone;
	}
	private JComboBoxUTMZoneLongitude getJComboBoxUTMLongitude() {
		if (jComboBoxUTMLongitude == null) {
			jComboBoxUTMLongitude = new JComboBoxUTMZoneLongitude();
			jComboBoxUTMLongitude.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxUTMLongitude.setPreferredSize(new Dimension(50, 26));
			jComboBoxUTMLongitude.addActionListener(this);
		}
		return jComboBoxUTMLongitude;
	}
	private JComboBoxUTMZoneLatitude getJComboBoxUTMLatitude() {
		if (jComboBoxUTMLatitude == null) {
			jComboBoxUTMLatitude = new JComboBoxUTMZoneLatitude();
			jComboBoxUTMLatitude.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxUTMLatitude.setPreferredSize(new Dimension(45, 26));
			jComboBoxUTMLatitude.addActionListener(this);
		}
		return jComboBoxUTMLatitude;
	}
	
	
	private JLabel getJLabelScale() {
		if (jLabelScale == null) {
			jLabelScale = new JLabel("Scale:");
			jLabelScale.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelScale;
	}
	private JComboBoxMapScale getJComboBoxMapScale() {
		if (jComboBoxMapScale == null) {
			jComboBoxMapScale = new JComboBoxMapScale();
			jComboBoxMapScale.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxMapScale.setPreferredSize(new Dimension(70, 26));
			jComboBoxMapScale.addActionListener(this);
		}
		return jComboBoxMapScale;
	}
	
	private JSeparator getJSeparatorFirst() {
		if (jSeparatorFirst == null) {
			jSeparatorFirst = new JSeparator();
		}
		return jSeparatorFirst;
	}
	
	
	private JLabel getJLabelMapService() {
		if (jLabelMapService == null) {
			jLabelMapService = new JLabel("Map Service:");
			jLabelMapService.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelMapService;
	}
	private DefaultComboBoxModel<String> getComboBoxModelMapServiceName() {
		if (cbmMapServiceName==null) {
			List<String> mapServiceNameList = MapSettings.getMapServiceNameList();
			String[] mapServiceNameArray = new String[mapServiceNameList.size()];
			mapServiceNameArray = mapServiceNameList.toArray(mapServiceNameArray);
			cbmMapServiceName = new DefaultComboBoxModel<>(mapServiceNameArray);
		}
		return cbmMapServiceName;
	}
	private JComboBox<String> getJComboBoxMapServiceName() {
		if (jComboBoxMapServiceName == null) {
			jComboBoxMapServiceName = new JComboBox<String>(this.getComboBoxModelMapServiceName());
			jComboBoxMapServiceName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxMapServiceName.addActionListener(this);
		}
		return jComboBoxMapServiceName;
	}

	
	private JLabel getJLabelTransparency() {
		if (jLabelTransparency == null) {
			jLabelTransparency = new JLabel("Map-Transparency:");
			jLabelTransparency.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTransparency;
	}
	private JSlider getJSliderTransparency() {
		if (jSliderTransparency == null) {
			jSliderTransparency = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
			jSliderTransparency.setFont(new Font("Dialog", Font.PLAIN, 12));
			jSliderTransparency.setPaintLabels(true);
			jSliderTransparency.setPaintTicks(true);
			jSliderTransparency.setMajorTickSpacing(10);
			jSliderTransparency.setMinorTickSpacing(1);
			jSliderTransparency.setSnapToTicks(true);
			jSliderTransparency.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                	if (MapSettingsPanel.this.pauseSliderListener==false) {
                		// --- Set percentage value to display ------
                		int transValue = MapSettingsPanel.this.getJSliderTransparency().getValue();
                		MapSettingsPanel.this.getJLabelTransparency().setText("Map-Transparency: " + transValue + " %");
                		// --- Inform listener with a small delay ---
                		if (MapSettingsPanel.this.getTimerSliderChangeEvent().isRunning()==true) {
                			MapSettingsPanel.this.getTimerSliderChangeEvent().restart();
        				} else {
        					MapSettingsPanel.this.getTimerSliderChangeEvent().start();
        				}
                	}
                }
            });
			
		}
		return jSliderTransparency;
	}
	/**
	 * Gets the timer slider event.
	 * @return the timer slider event
	 */
	private Timer getTimerSliderChangeEvent() {
		if (timeSliderChangeEvent==null) {
			timeSliderChangeEvent = new Timer(250, this);
			timeSliderChangeEvent.setRepeats(false);
		}
		return timeSliderChangeEvent;
	}
	
	/**
	 * Returns the current MapSettings.
	 * @return the map settings
	 */
	public MapSettings getMapSettings() {
		if (mapSettings==null) {
			mapSettings = new MapSettings();
		}
		mapSettings.setUTMLongitudeZone((int) this.getJComboBoxUTMLongitude().getSelectedItem());
		mapSettings.setUTMLatitudeZone((String) this.getJComboBoxUTMLatitude().getSelectedItem());
		mapSettings.setMapScale((MapScale) this.getJComboBoxMapScale().getSelectedItem());
		mapSettings.setMapServiceName(this.getMapServiceName());
		mapSettings.setMapTileTransparency(this.getJSliderTransparency().getValue());
		return mapSettings;
	}
	/**
	 * Sets the MapSettings for the visual representation.
	 * @param mapSettings the new map settings
	 */
	public void setMapSettings(MapSettings mapSettings) {
		this.mapSettings = mapSettings;
		if (this.mapSettings==null) {
			this.setMapSettingsEnabled(false);
		} else {
			this.pauseActionListener=true;
			this.pauseSliderListener=true;
			this.setMapSettingsEnabled(true);
			this.getJComboBoxUTMLongitude().setSelectedItem(this.mapSettings.getUTMLongitudeZone());
			this.getJComboBoxUTMLatitude().setSelectedItem(this.mapSettings.getUTMLatitudeZone());
			this.getJComboBoxMapScale().setSelectedItem(this.mapSettings.getMapScale());
			this.setMapServiceName(this.mapSettings.getMapServiceName());
			this.setTransparency(this.mapSettings.getMapTileTransparency());
			this.pauseSliderListener=false;
			this.pauseActionListener=false;
		}
	}
	
	/**
	 * Sets the map settings enabled (or not).
	 * @param isEnabled the new map settings enabled
	 */
	private void setMapSettingsEnabled(boolean isEnabled) {

		this.getJLabelUTMZone().setEnabled(isEnabled);
		this.getJComboBoxUTMLongitude().setEnabled(isEnabled);
		this.getJComboBoxUTMLatitude().setEnabled(isEnabled);
		
		this.getJLabelScale().setEnabled(isEnabled);
		this.getJComboBoxMapScale().setEnabled(isEnabled);
		
		this.getJLabelTransparency().setEnabled(isEnabled);
		this.getJComboBoxMapServiceName().setEnabled(isEnabled);
		this.getJSliderTransparency().setEnabled(isEnabled);
	}

	/**
	 * Sets the map service name.
	 * @param newMapServiceName the new map service name
	 */
	private void setMapServiceName(String newMapServiceName) {
		if (newMapServiceName==null || newMapServiceName.isEmpty()==true) {
			this.getJComboBoxMapServiceName().setSelectedItem(MapSettings.NO_MAP_SERVICE_SELECTION);
		} else {
			this.getJComboBoxMapServiceName().setSelectedItem(newMapServiceName);
		}
	}
	/**
	 * Return the currently selected map service name.
	 * @return the map service name
	 */
	private String getMapServiceName() {
		Object selectedItem = this.getJComboBoxMapServiceName().getSelectedItem();
		if (selectedItem instanceof String) {
			String mapServiceSelected = (String) selectedItem;
			if (mapServiceSelected!=null && mapServiceSelected.isEmpty()==false && mapServiceSelected.equals(MapSettings.NO_MAP_SERVICE_SELECTION)==false) {
				return mapServiceSelected;
			}
		}
		return null;
	}
	
	/**
	 * Sets the transparency.
	 * @param transValue the new transparency
	 */
	private void setTransparency(int transValue) {
		this.setTransparency(transValue, true);
	}
	/**
	 * Sets the transparency.
	 * @param transValue the transparency value
	 * @param setToSlider the set to slider
	 */
	private void setTransparency(int transValue, boolean setToSlider) {
		this.getJLabelTransparency().setText("Map-Transparency: " + transValue + " %");
		if (setToSlider==true) {
			this.getJSliderTransparency().setValue(transValue);
		}
		this.informListener(MapSettingsChanged.MapTileTransparency);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		// --- Paused action listener? --------------------------------------------------
		if (this.pauseActionListener==true) return;
		
		// --- Ensure that the local MapSetting correspond to the visualization ---------
		this.getMapSettings();
		
		// --- Inform listener about changes --------------------------------------------
		if (ae.getSource()==this.getJComboBoxUTMLongitude()) {
			this.informListener(MapSettingsChanged.UTM_Longitude);
		} else if (ae.getSource()==this.getJComboBoxUTMLatitude()) {
			this.informListener(MapSettingsChanged.UTM_Latitude);			
		} else if (ae.getSource()==this.getJComboBoxMapScale()) {
			this.informListener(MapSettingsChanged.MapScale);
		} else if (ae.getSource()==this.getJComboBoxMapServiceName()) {
			this.informListener(MapSettingsChanged.MapService);
		} else if (ae.getSource()==this.getTimerSliderChangeEvent()) {
			this.setTransparency(this.getJSliderTransparency().getValue(), false);
		}
	}
	
	
	/**
	 * Returns the map settings panel listener.
	 * @return the map settings panel listener
	 */
	private List<MapSettingsPanelListener> getMapSettingsPanelListener() {
		if (listener==null) {
			listener = new ArrayList<>();
		}
		return listener;
	}
	/**
	 * Adds the map settings panel listener.
	 * @param listener the listener to add
	 */
	public void addMapSettingsPanelListener(MapSettingsPanelListener listener) {
		if (listener!=null && this.getMapSettingsPanelListener().contains(listener)==false) {
			this.getMapSettingsPanelListener().add(listener);
		}
	}
	/**
	 * Removes the map settings panel listener.
	 * @param listener the listener to remove
	 */
	public void removeMapSettingsPanelListener(MapSettingsPanelListener listener) {
		if (listener!=null) this.getMapSettingsPanelListener().remove(listener);
	}
	/**
	 * Informs the registered  listener about changes in the MapSettings.
	 * @param valueChangedInMapSetting the value changed in map setting
	 */
	private void informListener(MapSettingsChanged valueChangedInMapSetting) {
		for (int i = 0; i < this.getMapSettingsPanelListener().size(); i++) {
			this.getMapSettingsPanelListener().get(i).onChangedMapSettings(valueChangedInMapSetting);
		}
	}
}
