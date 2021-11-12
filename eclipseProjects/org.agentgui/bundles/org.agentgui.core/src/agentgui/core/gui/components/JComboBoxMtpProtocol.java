/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.gui.components;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import agentgui.core.config.GlobalInfo.MtpProtocol;

/**
 * The Class JComboBoxMtpProtocol is a JcomboBox 
 * that is used to select a MTP protocol.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 29-04-2016
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
			model = new DefaultComboBoxModel<MtpProtocol>();
			model.addElement(MtpProtocol.HTTP);
			model.addElement(MtpProtocol.HTTPS);
			model.addElement(MtpProtocol.PROXIEDHTTPS);
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
