package de.enflexit.geography.coordinates.ui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import de.enflexit.geography.coordinates.CoordinateConversion;

/**
 * The Class JComboBoxUTMZoneLatitude.
 */
public class JComboBoxUTMZoneLatitude extends JComboBox<String> {

	private static final long serialVersionUID = 2461610502711620992L;

	private DefaultComboBoxModel<String> comboBoxModel;
	
	/**
	 * Instantiates a new JComboBoxUTMZoneLatitude.
	 */
	public JComboBoxUTMZoneLatitude() {
		super();
		this.setModel(getComboBoxModel());
		this.setMaximumRowCount(10);
	}
	
	/**
	 * Gets the combo box model.
	 * @return the combo box model
	 */
	public DefaultComboBoxModel<String> getComboBoxModel() {
		if (comboBoxModel==null) {
			comboBoxModel = new DefaultComboBoxModel<>();
			
			char[] letters = new CoordinateConversion().getUTMLatZoneLetters();
			for (int i = 0; i < letters.length; i++) {
				comboBoxModel.addElement(String.valueOf(letters[i]));
			}
		}
		return comboBoxModel;
	}
	
}
