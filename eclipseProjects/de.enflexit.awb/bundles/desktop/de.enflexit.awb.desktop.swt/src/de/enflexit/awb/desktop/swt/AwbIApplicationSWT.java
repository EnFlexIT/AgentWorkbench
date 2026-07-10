package de.enflexit.awb.desktop.swt;

import java.awt.Toolkit;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.AwbIApplication;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.config.GlobalInfo.AWBProduct;
import de.enflexit.common.SystemEnvironmentHelper;

/**
 * This class controls all aspects of the application's execution
 * and therefore also the different execution modes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AwbIApplicationSWT extends AwbIApplication {
	
	private static Display display;
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplication#getAwbProduct()
	 */
	@Override
	public AWBProduct getAwbProduct() {
		return AWBProduct.DESKTOP_SWING;
	}
	
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		if(SystemEnvironmentHelper.isMacOperatingSystem()==true) {
			startEclipseUI(() -> { 
			try {			
				startSwingComponents(context);
				
				display.asyncExec(() -> {
					// --- Hide the display --------------
					AwbIApplicationSWT.this.eclipseUiHide(display);					
				});
			} catch (Exception e) {
				e.printStackTrace();
			} 
			});
		}
		return this.getApplicationReturnValue();
	}
	
	private void startSwingComponents(IApplicationContext context) throws Exception {
		// --- Preparations for MAC environment -----------
		if (SystemEnvironmentHelper.isMacOperatingSystem()==true) {
			// --- Ensure to start AWT --------------------
		    Toolkit.getDefaultToolkit();
		}
		
		// --- Set the product indicator ------------------
		GlobalInfo.catchProduct(this.getAwbProduct());
		
		// --- Remind application context -----------------
		this.setIApplicationContext(context);

		// --- OS-dependent system start ------------------
		if (SystemEnvironmentHelper.isMacOperatingSystem()==true) {
			// --- Start for MacOS ------------------------
			this.startApplicationInOwnThread();
		} else {
			// --- Regular start for Windows and Linux ----
			this.startApplication();
		}

	}
	
	/**
	 * Hides the Eclipse workbench.
	 * @param display the display
	 */
	private void eclipseUiHide(Display display) {
		if (display!=null) {
			if (display.getActiveShell()!=null) {
				display.getActiveShell().setVisible(false);
			} else {
				for (int i = 0; i < display.getShells().length; i++) {
					display.getShells()[i].setVisible(false);
				} 
			}
		}
	}	
	
	/**
	 * Starts the eclipse UI.
	 * @param postWindowOpenRunnable the post window open runnable
	 * @return the integer that represents the application return value
	 */
	public static Integer startEclipseUI(Runnable postWindowOpenRunnable) {
		
		Integer eclipseReturnValue = IApplication.EXIT_OK;
		try {			
			display = PlatformUI.createDisplay();
			// --- Returns if visualization was closed ---- 
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor(postWindowOpenRunnable));
			if (returnCode == PlatformUI.RETURN_RESTART) {
				eclipseReturnValue = IApplication.EXIT_RESTART;
			} else {
				eclipseReturnValue = IApplication.EXIT_OK;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(display != null) display.dispose();
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
