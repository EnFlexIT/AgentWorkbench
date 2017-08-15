 
package org.agentgui.core.handler;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Shell;

import com.opcoach.e4.preferences.E4PreferenceRegistry;

public class PreferencesHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, E4PreferenceRegistry prefReg) {
		PreferenceManager prefMan = prefReg.getPreferenceManager();
		PreferenceDialog dialog = new PreferenceDialog(shell, prefMan);
		dialog.create();
		dialog.getTreeViewer().setComparator(new ViewerComparator());
		dialog.getTreeViewer().expandAll();
		dialog.open();
	}
		
}