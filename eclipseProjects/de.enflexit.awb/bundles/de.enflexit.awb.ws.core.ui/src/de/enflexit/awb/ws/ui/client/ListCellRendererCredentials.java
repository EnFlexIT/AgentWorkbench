package de.enflexit.awb.ws.ui.client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import de.enflexit.awb.ws.credential.AbstractCredential;

/**
 * The Class ListCellRendererCredentials renders the cells of the {@link JList} of the {@link JPanelCredentials} bundle.
 *
 * @author Timo Brandhorst - SOFTEC - University Duisburg-Essen
 */
public class ListCellRendererCredentials extends DefaultListCellRenderer{

	private static final long serialVersionUID = 5579979447328378903L;

	/* (non-Javadoc)
	* @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	*/
	@Override
	public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (!isSelected) {
			if(value instanceof AbstractCredential) {
			AbstractCredential cred=(AbstractCredential) value;
				if (cred.isEmpty()) {
					c.setBackground(Color.ORANGE);
				}else {
					c.setBackground(Color.WHITE);
				}
			}
		}else {
			c.setBackground(new Color(-13014646));	
			c.setForeground(new Color(-1));
		}
		return this;
	}
}
