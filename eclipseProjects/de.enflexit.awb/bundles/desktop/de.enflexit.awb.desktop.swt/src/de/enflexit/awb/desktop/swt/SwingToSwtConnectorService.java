package de.enflexit.awb.desktop.swt;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * The Class SwingToSwtConnectorService enables to open and focus the Eclipse UI and other.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SwingToSwtConnectorService implements de.enflexit.awb.core.ui.SwingToSwtConnectorService {

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipseWorkbench()
	 */
	@Override
	public void showEclipseWorkbench() {
		this.startEclipseWorkbench(true);
	}
	/**
	 * Will activate Show eclipse workbench.
	 * @param setVisible the set visible
	 */
	private void startEclipseWorkbench(final boolean setVisible) {
		
		if (PlatformUI.isWorkbenchRunning()==false) {
			// --- Start workbench in dedicated thread --------------
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (setVisible==true) {
						AwbIApplicationSWT.startEclipseUI(null);
					} else {
						AwbIApplicationSWT.startEclipseUI(new Runnable() {
							@Override
							public void run() {
								AwbIApplicationSWT.setVisibleEclipseUi(setVisible);
							}
						});
					}
				}
			}, "Eclipse Workbench Thread").start();

			// --- Wait for the end of the workbench start phase ----
			while (PlatformUI.isWorkbenchRunning()==false) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} else {
			// --- Show and focus workbench window ------------------
			AwbIApplicationSWT.setVisibleEclipseUi(setVisible);			
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#disposeEclipseWorkbench()
	 */
	@Override
	public void disposeEclipseWorkbench() {
		AwbIApplicationSWT.stopEclipseUI();
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipsePreferences()
	 */
	@Override
	public void showEclipsePreferences() {
		
		// TODO Not implemented final
		
		this.startEclipseWorkbench(false);

		// --- Start workbench in dedicated thread --------------
		Display display = PlatformUI.getWorkbench().getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, null, null, null);
				dialog.getShell().forceActive();
				dialog.open();
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipseAbout()
	 */
	@Override
	public void showEclipseAbout() {
		// TODO Not implemented yet
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipseCheckForUpdates()
	 */
	@Override
	public void showEclipseCheckForUpdates() {
		// TODO Not implemented yet
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.SwingToSwtConnectorService#showEclipseInstallNewSoftware()
	 */
	@Override
	public void showEclipseInstallNewSoftware() {
		// TODO Not implemented yet
	}

}
