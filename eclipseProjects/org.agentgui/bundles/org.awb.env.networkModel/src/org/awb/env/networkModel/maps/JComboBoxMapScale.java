package org.awb.env.networkModel.maps;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.awb.env.networkModel.maps.MapSettings.MapScale;

/**
 * The Class JComboBoxMapScale.
 */
public class JComboBoxMapScale extends JComboBox<MapScale> {

	private static final long serialVersionUID = 2461610502711620992L;

	private DefaultComboBoxModel<MapScale> comboBoxModel;
	
	
	/**
	 * Instantiates a new JComboBoxMapScale.
	 */
	public JComboBoxMapScale() {
		super();
		this.setModel(getComboBoxModel());
	}
	/**
	 * Gets the combo box model.
	 * @return the combo box model
	 */
	public DefaultComboBoxModel<MapScale> getComboBoxModel() {
		if (comboBoxModel==null) {
			comboBoxModel = new DefaultComboBoxModel<>();
			for (MapScale mapScale : MapScale.values()) {
				comboBoxModel.addElement(mapScale);
			}
		}
		return comboBoxModel;
	}
	
}
