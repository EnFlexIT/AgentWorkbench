package de.enflexit.geography.coordinates.ui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * The Class JComboBoxUTMZoneLongitude.
 */
public class JComboBoxUTMZoneLongitude extends JComboBox<Integer> {

	private static final long serialVersionUID = 2461610502711620992L;

	private DefaultComboBoxModel<Integer> comboBoxModel;
	
	
	/**
	 * Instantiates a new JComboBoxUTMZoneLongitude.
	 */
	public JComboBoxUTMZoneLongitude() {
		super();
		this.setModel(getComboBoxModel());
		this.setMaximumRowCount(10);
	}
	/**
	 * Gets the combo box model.
	 * @return the combo box model
	 */
	public DefaultComboBoxModel<Integer> getComboBoxModel() {
		if (comboBoxModel==null) {
			comboBoxModel = new DefaultComboBoxModel<>();
			for (int i=0; i <= 60; i++) {
				comboBoxModel.addElement(i);
			}
		}
		return comboBoxModel;
	}
	
}
