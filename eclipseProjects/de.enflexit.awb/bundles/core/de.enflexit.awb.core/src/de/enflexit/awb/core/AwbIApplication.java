package de.enflexit.awb.core;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.config.GlobalInfo.AWBProduct;
import de.enflexit.awb.core.ui.AgentWorkbenchUiManager;
import de.enflexit.common.SystemEnvironmentHelper;
import de.enflexit.language.Language;

/**
 * This class controls all aspects of the application's execution
 * and therefore also the different execution modes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AwbIApplication implements AwbIApplicationInterface {
	
	private IApplicationContext iApplicationContext;
	private Integer appReturnValue = IApplication.EXIT_OK;
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#getIApplicationContext()
	 */
	@Override
	public IApplicationContext getIApplicationContext() {
		return this.iApplicationContext;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#setIApplicationContext(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public void setIApplicationContext(IApplicationContext iApplicationContext) {
		this.iApplicationContext = iApplicationContext;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#getApplicationReturnValue()
	 */
	@Override
	public Integer getApplicationReturnValue() {
		return appReturnValue;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#setAppReturnValue(java.lang.Integer)
	 */
	@Override
	public void setApplicationReturnValue(Integer appReturnValue) {
		this.appReturnValue = appReturnValue;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#setApplicationIsRunning()
	 */
	@Override
	public void setApplicationIsRunning() {
		System.err.println("[AWB-MAC-DIAG] setApplicationIsRunning() called");
		this.getIApplicationContext().applicationRunning();
		// --- Fallback: close the AWT splash if Eclipse's applicationRunning() ---
		//     failed to dismiss it (e.g. when called from a non-main thread on macOS).
		try {
			java.awt.SplashScreen splash = java.awt.SplashScreen.getSplashScreen();
			if (splash != null) {
				splash.close();
				System.err.println("[AWB-MAC-DIAG] AWT SplashScreen closed as fallback");
			}
		} catch (Throwable ignore) {
		}
	}
	
	/**
	 * Waits for the termination of the application.
	 */
	protected void waitForApplicationTermination() throws Exception {
		// --- Wait for termination of the application ----
		while (Application.isQuitJVM()==false) {
			Thread.sleep(250);
		}
	}

	/** The Swing splash window (macOS only, replaces the native Eclipse splash). */
	private static JWindow swingSplash;

	/**
	 * Shows a Swing-based splash screen on macOS, loading the {@code splash.png}
	 * from the {@code de.enflexit.awb.core} bundle. This replaces the native
	 * Eclipse splash (disabled with {@code -nosplash}) which deadlocks the
	 * launcher when used with {@code -XstartOnFirstThread} on macOS Tahoe 26.
	 * <p>
	 * Must be called on the main thread (after AWT is initialized).
	 * </p>
	 */
	private static void showSwingSplash() {
		try {
			Bundle bundle = FrameworkUtil.getBundle(AwbIApplication.class);
			if (bundle == null) {
				System.err.println("[AWB-MAC-DIAG] showSwingSplash: bundle not found");
				return;
			}
			URL splashUrl = bundle.getEntry("/splash.png");
			if (splashUrl == null) {
				System.err.println("[AWB-MAC-DIAG] showSwingSplash: splash.png not found in bundle");
				return;
			}
			splashUrl = FileLocator.resolve(splashUrl);
			BufferedImage image = ImageIO.read(splashUrl);
			if (image == null) {
				System.err.println("[AWB-MAC-DIAG] showSwingSplash: could not read splash image");
				return;
			}
			swingSplash = new JWindow();
			javax.swing.JLabel label = new javax.swing.JLabel(new javax.swing.ImageIcon(image));
			swingSplash.getContentPane().add(label);
			swingSplash.pack();
			swingSplash.setLocationRelativeTo(null);
			swingSplash.setVisible(true);
			System.err.println("[AWB-MAC-DIAG] showSwingSplash: visible (" + image.getWidth() + "x" + image.getHeight() + ")");
		} catch (Throwable ex) {
			System.err.println("[AWB-MAC-DIAG] showSwingSplash FAILED: " + ex.getMessage());
		}
	}

	/**
	 * Closes and disposes the Swing splash screen if it is currently visible.
	 * Called from {@code MainWindow}'s {@code invokeLater} after the main window
	 * is activated.
	 */
	public static void closeSwingSplash() {
		if (swingSplash != null) {
			try {
				swingSplash.setVisible(false);
				swingSplash.dispose();
				System.err.println("[AWB-MAC-DIAG] closeSwingSplash: disposed");
			} catch (Throwable ex) {
				System.err.println("[AWB-MAC-DIAG] closeSwingSplash FAILED: " + ex.getMessage());
			} finally {
				swingSplash = null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#getAwbProduct()
	 */
	@Override
	public AWBProduct getAwbProduct() {
		return AWBProduct.DESKTOP_SWING;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {

		// --- Preparations for MAC environment -----------
		if (SystemEnvironmentHelper.isMacOperatingSystem()==true) {
			// --- Set the macOS application name before AWT loads ---
			//     so the menu bar / dock show the right name.
			System.setProperty("apple.awt.application.name", "Agent.Workbench");
			// --- Ensure to start AWT --------------------
		    Toolkit.getDefaultToolkit();
			// --- Activate the process as the foreground ---
			//     application before any window is created.
			//     requestForeground() is asynchronous on the
			//     AppKit thread, so doing it early gives the
			//     activation a head start over window show.
			SystemEnvironmentHelper.requestForeground();
			// --- Show a Swing splash (the native Eclipse splash ---
			//     is disabled with -nosplash on macOS to avoid a
			//     deadlock in the launcher's _update_splash native
			//     call when -XstartOnFirstThread is used).
			showSwingSplash();
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
		
		// --- Wait for termination of application --------
		this.waitForApplicationTermination();

		// --- Stop the Application class -----------------
		System.out.println(Language.translate("Programmende... "));
		this.stop();

		return this.appReturnValue;
	}
	
	/**
	 * Starts the application.
	 * @throws Exception the exception
	 * @see Application#start(AwbIApplication)
	 */
	private void startApplication() throws Exception {
		Application.start(this);
	}
	
	/**
	 * Starts the application in a separate thread.
	 */
	private void startApplicationInOwnThread() {
		Thread awbStarter = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							try {
								AwbIApplication.this.startApplication();
							} catch(Exception ex) {
								ex.printStackTrace();
							}
						}
					});
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		awbStarter.setName("AWB-Starter");
		awbStarter.start();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#stop(java.lang.Integer)
	 */
	@Override
	public void stop(Integer returnValue) {
		this.appReturnValue = returnValue;
		this.stop();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#stop()
	 */
	@Override
	public void stop() {

		// --- Check for open projects ------
		if (Application.stopAgentWorkbench()==false) return;
		// --- Stop Eclipse workbench -------
		AgentWorkbenchUiManager.getInstance().disposeEclipseWorkbench();
		// --- Stop LogFileWriter -----------
		Application.stopLoggingWriter();
		// --- ShutdownExecuter -------------
		Application.setShutdownThread(null);
		// --- Indicate to stop the JVM -----
		Application.setQuitJVM(true);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#startEndUserApplication(java.lang.Runnable)
	 */
	@Override
	public Integer startEndUserApplication(Runnable postWindowOpenRunnable) {
		
		try {
			this.appReturnValue = this.startMainWindow(postWindowOpenRunnable);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.appReturnValue;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#startMainWindow(java.lang.Runnable)
	 */
	@Override
	public Integer startMainWindow(Runnable postWindowOpenRunnable) throws Exception {
		
		Integer appReturnValue = IApplication.EXIT_OK;
		if (Application.isOperatingHeadless()==false) {
			// --- Start UI -------------------------------
			Application.getMainWindow();
			Application.getProjectsLoaded().setProjectView();
		}
		
		// --- Execute the post window open runnable ------
		if (postWindowOpenRunnable!=null) {
			Thread postAction = new Thread(postWindowOpenRunnable, "AWB-Swing-PostWindowOpen-Action");
			postAction.setPriority(Thread.MAX_PRIORITY);
			postAction.start();
		}

		// --- Remove splash screen -----------------------
		// For headless mode, close immediately. For non-headless, the splash
		// is closed from MainWindow's invokeLater (after macOS activation),
		// so the process is activated before the splash is dismissed.
		if (Application.isOperatingHeadless()) {
			this.setApplicationIsRunning();
		}
		
		return appReturnValue;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationInterface#startJadeStandalone(java.lang.String[])
	 */
	@Override
	public Integer startJadeStandalone(String[] arguments) {
		
		// --- Remove splash screen -----------------------
		this.setApplicationIsRunning();
		
		// --- Boot JADE as from command line ------------- 
		jade.Boot.main(arguments);
		jade.core.Runtime.instance().invokeOnTermination(new Runnable() {
			@Override
			public void run() {
				Application.setQuitJVM(true);
			}
		});
		return IApplication.EXIT_OK;
	}
	
}
