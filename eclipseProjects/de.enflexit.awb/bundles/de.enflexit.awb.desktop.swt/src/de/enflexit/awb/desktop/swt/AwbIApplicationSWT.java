package de.enflexit.awb.desktop.swt;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.AwbIApplication;

/**
 * This class controls all aspects of the application's execution
 * and therefore also the different execution modes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AwbIApplicationSWT extends AwbIApplication {

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplication#startEndUserApplication(java.lang.Runnable)
	 */
	@Override
	public Integer startEndUserApplication(Runnable postWindowOpenRunnable) {
		
		try {
			// --- Visualization by Eclipse -----------
			this.setApplicationReturnValue(AwbIApplicationSWT.startEclipseUI(postWindowOpenRunnable));
			this.stop();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.getApplicationReturnValue();
	}
	

	/**
	 * Starts the eclipse UI.
	 * @param postWindowOpenRunnable the post window open runnable
	 * @return the integer that represents the application return value
	 */
	public static Integer startEclipseUI(Runnable postWindowOpenRunnable) {
		
		Integer eclipseReturnValue = IApplication.EXIT_OK;
		Display display = PlatformUI.createDisplay();
		try {
			// --- Returns if visualization was closed ---- 
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor(postWindowOpenRunnable));
			if (returnCode == PlatformUI.RETURN_RESTART) {
				eclipseReturnValue = IApplication.EXIT_RESTART;
			} else {
				eclipseReturnValue = IApplication.EXIT_OK;
			}
			
		} finally {
			display.dispose();
			// --- Just in case of the Eclipse UI ---------
			// --- usage or after an update + restart -----
			if (eclipseReturnValue==IApplication.EXIT_RESTART) {
				Application.getAwbIApplication().setApplicationReturnValue(eclipseReturnValue);
				Application.setQuitJVM(true);
			}
		}		
		return eclipseReturnValue;
	}
	
	 /**
 	 * Shows or hides the current Eclipse workbench.
 	 * @param setVisible the indicator to set the workbench window visible or not
 	 */
	public static void setVisibleEclipseUi(final boolean setVisible) {

		if (PlatformUI.isWorkbenchRunning()==false) return;
		
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				AwbIApplicationSWT.setDisplayVisible(display, setVisible);
			}
		});
	}
	/**
	 * Sets the specified display visible or not.
	 *
	 * @param display the display
	 * @param setVisible the set visible
	 */
	private static void setDisplayVisible(Display display, boolean setVisible) {
		
		if (display==null) return;
	
		if (display.getActiveShell()!=null) {
			display.getActiveShell().setVisible(setVisible);
			if (setVisible==true) {
				display.getActiveShell().forceActive();
			}
		} else {
			for (int i = 0; i < display.getShells().length; i++) {
				display.getShells()[i].setVisible(setVisible);
				if (setVisible==true) {
					display.getShells()[i].forceActive();
				}

			} 
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplication#stop()
	 */
	@Override
	public void stop() {
		super.stop();
		AwbIApplicationSWT.stopEclipseUI();
	}
	
	/**
	 * Stops the Eclipse UI.
	 */
	public static void stopEclipseUI() {
		
		if (PlatformUI.isWorkbenchRunning()==false) return;
		
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}
	
}
