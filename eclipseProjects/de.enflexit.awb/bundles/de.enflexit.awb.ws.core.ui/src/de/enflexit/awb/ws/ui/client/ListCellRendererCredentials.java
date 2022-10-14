package de.enflexit.awb.ws.ui.client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.enflexit.awb.ws.credential.AbstractCredential;
import de.enflexit.awb.ws.credential.ApiKeyCredential;
import de.enflexit.awb.ws.credential.BearerTokenCredential;
import de.enflexit.awb.ws.credential.UserPasswordCredential;

public class ListCellRendererCredentials extends JLabel implements ListCellRenderer<AbstractCredential> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5579979447328378903L;

	/* (non-Javadoc)
	* @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	*/
	@Override
	public Component getListCellRendererComponent(JList<? extends AbstractCredential> list, AbstractCredential value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof ApiKeyCredential) {
			ApiKeyCredential cred = (ApiKeyCredential) value;
			if (cred.isEmpty()) {
				super.setBackground(Color.magenta);
			}
		} else if (value instanceof BearerTokenCredential) {
			BearerTokenCredential cred = (BearerTokenCredential) value;
			if (cred.isEmpty()) {
				super.setBackground(Color.magenta);
			}
		} else if (value instanceof UserPasswordCredential) {
			UserPasswordCredential cred = (UserPasswordCredential) value;
			if (cred.isEmpty()) {
				super.setBackground(Color.magenta);
			}
		}
		return this;
	}

}
