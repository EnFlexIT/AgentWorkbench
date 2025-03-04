package de.enflexit.awb.baseUI.components;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import de.enflexit.awb.core.config.GlobalInfo.MtpProtocol;

/**
 * The Class JComboBoxMtpProtocol.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JComboBoxMtpProtocol extends JComboBox<MtpProtocol> {

	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<MtpProtocol> model;

	/**
	 * Instantiates a new JComboBoxMtpProtocol.
	 */
	public JComboBoxMtpProtocol(){
		setModel(this.getComboBoxModel());
	}
	/**
	 * Gets the combo box model.
	 * @return the combo box model
	 */
	private DefaultComboBoxModel<MtpProtocol> getComboBoxModel() {
		if (model==null) {
			model = new DefaultComboBoxModel<>();
			model.addElement(MtpProtocol.HTTP);
			model.addElement(MtpProtocol.HTTPS);
		}
		return model;
	}

	/**
	 * Gets the selected protocol.
	 * @return the selected protocol
	 */
	public MtpProtocol getSelectedProtocol(){
		return (MtpProtocol) this.getComboBoxModel().getSelectedItem();
	}
	/**
	 * Sets the selected protocol.
	 * @param protocolToSelect the new selected protocol
	 */
	public void setSelectedProtocol(MtpProtocol protocolToSelect){
		this.getComboBoxModel().setSelectedItem(protocolToSelect);
	}
}
