package de.enflexit.common;

import java.awt.HeadlessException;

import javax.swing.JDialog;

/**
 * The Class SystemEnvironmentHelper provides some (hopeful) useful methods to information .
 * about the execution environment.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SystemEnvironmentHelper {

	/**
	 * Checks if the current system environment is executed headless (without any GUI).
	 * @return true, if is headless operation
	 */
	public static boolean isHeadlessOperation() {

		boolean headlessOperation=false;
		// --- Do headless check ----------------------
		JDialog jDialog;
		try {
			jDialog = new JDialog();
			jDialog.validate();
//			jDialog.dispose();
			jDialog = null;
			headlessOperation = false;
			
		} catch (HeadlessException he) {
			headlessOperation = true;
		}
		return headlessOperation;
	}
	
}
