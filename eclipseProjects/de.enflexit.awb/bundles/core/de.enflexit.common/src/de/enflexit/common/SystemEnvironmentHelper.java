package de.enflexit.common;

import java.awt.GraphicsEnvironment;
import java.lang.reflect.Method;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * The Class SystemEnvironmentHelper provides some (hopeful) useful methods to information .
 * about the execution environment.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SystemEnvironmentHelper {

	
	/**
	 * Returns the operating system by calling <code>System.getProperty("os.name")</code>.
	 * @return the operating system
	 */
	public static String getOperatingSystem() {
		return System.getProperty("os.name");
	}
	/**
	 * Checks if the current operating system is windows.
	 * @return true, if is windows operating system
	 */
	public static boolean isWindowsOperatingSystem() {
		return getOperatingSystem().toLowerCase().contains("windows");
	}
	/**
	 * Checks if the current operating system is Linux.
	 * @return true, if is Linux operating system
	 */
	public static boolean isLinuxOperatingSystem() {
		return getOperatingSystem().toLowerCase().contains("linux");
	}
	/**
	 * Checks if the current operating system is Mac OS X.
	 * @return true, if is Mac OS X operating system
	 */
	public static boolean isMacOperatingSystem() {
		return getOperatingSystem().toLowerCase().contains("mac");
	}

	/**
	 * Return the version of the operating systems by calling <code>System.getProperty("os.version")</code>.
	 * @return the operating systems version
	 */
	public static String getOperatingSystemsVersion() {
		return System.getProperty("os.version");
	}
	/**
	 * Return the architecture of the operating systems by calling <code>System.getProperty("os.arch")</code>.
	 * @return the operating systems version
	 */
	public static String getOperatingSystemsArchitecture() {
		return System.getProperty("os.arch");
	}

	
	/**
	 * Checks if the current system environment is executed headless (without any GUI).
	 * @return true, if is headless operation
	 */
	public static boolean isHeadlessOperation() {
		return GraphicsEnvironment.isHeadless();
	}

	// --- Cached JNA objects for frequent calls (pumpMainRunLoop) ---
	private static boolean _jnaPumpInit = false;
	private static boolean _jnaPumpAvail = false;
	private static Object _pumpFnGetClass, _pumpFnSelRegister, _pumpFnObjcMsgSend;
	private static Method _pumpInvokePointer, _pumpInvokeNoReturn;
	private static Object _pumpRunLoop, _pumpSelRunUntil, _pumpNsDateCls, _pumpSelDateWith;

	/**
	 * Pumps the macOS main thread's {@code NSRunLoop} for the given number of seconds.
	 * <p>
	 * This is used by {@code AwbIApplication.waitForApplicationTermination()} on macOS
	 * (when started with {@code -XstartOnFirstThread}) instead of {@code Thread.sleep()}
	 * so that {@code performSelectorOnMainThread:} calls &mdash; used by
	 * {@link #requestForeground()} and {@link #closeSplashScreen()} &mdash; are actually
	 * processed. Without pumping the run loop, those calls would queue indefinitely.
	 * </p>
	 * <p>
	 * Must be called on the main thread. No-op on non-macOS or if JNA is unavailable
	 * (falls back to {@code Thread.sleep}).
	 * </p>
	 * @param seconds the time to run the loop before returning
	 */
	public static void pumpMainRunLoop(double seconds) {
		if (isMacOperatingSystem() == false) return;
		if (ensurePumpJna() == false) {
			try { Thread.sleep((long)(seconds * 1000)); } catch (InterruptedException ie) {}
			return;
		}
		try {
			// --- [NSDate dateWithTimeIntervalSinceNow: seconds] ---
			Object date = _pumpInvokePointer.invoke(_pumpFnObjcMsgSend, new Object[]{ new Object[]{ _pumpNsDateCls, _pumpSelDateWith, Double.valueOf(seconds) } });
			// --- [runLoop runUntilDate: date] ---
			_pumpInvokeNoReturn.invoke(_pumpFnObjcMsgSend, new Object[]{ new Object[]{ _pumpRunLoop, _pumpSelRunUntil, date } });
		} catch (Throwable ex) {
			try { Thread.sleep((long)(seconds * 1000)); } catch (InterruptedException ie) {}
		}
	}

	/**
	 * One-time JNA initialization for {@link #pumpMainRunLoop(double)}.
	 * Caches the NSRunLoop instance and selectors so the per-250ms call is cheap.
	 * @return {@code true} if JNA is available and the run loop objects were cached
	 */
	private static boolean ensurePumpJna() {
		if (_jnaPumpInit) return _jnaPumpAvail;
		_jnaPumpInit = true;
		try {
			BundleContext context = FrameworkUtil.getBundle(SystemEnvironmentHelper.class).getBundleContext();
			if (context == null) return false;
			Class<?> nativeLibraryCls = null;
			for (Bundle b : context.getBundles()) {
				try {
					nativeLibraryCls = b.loadClass("com.sun.jna.NativeLibrary");
					break;
				} catch (Throwable ignore) {}
			}
			if (nativeLibraryCls == null) return false;

			Method getInstance = nativeLibraryCls.getMethod("getInstance", String.class);
			Object objcLib = getInstance.invoke(null, "objc");
			Method getFunction = nativeLibraryCls.getMethod("getFunction", String.class);
			_pumpFnGetClass    = getFunction.invoke(objcLib, "objc_getClass");
			_pumpFnSelRegister = getFunction.invoke(objcLib, "sel_registerName");
			_pumpFnObjcMsgSend = getFunction.invoke(objcLib, "objc_msgSend");

			Class<?> functionCls = Class.forName("com.sun.jna.Function", true, nativeLibraryCls.getClassLoader());
			_pumpInvokePointer  = functionCls.getMethod("invokePointer", Object[].class);
			_pumpInvokeNoReturn = functionCls.getMethod("invoke", Object[].class);

			// --- NSRunLoop.currentRunLoop ---
			Object nsRunLoopCls = _pumpInvokePointer.invoke(_pumpFnGetClass, new Object[]{ new Object[]{"NSRunLoop"} });
			Object selCurrentRL = _pumpInvokePointer.invoke(_pumpFnSelRegister, new Object[]{ new Object[]{"currentRunLoop"} });
			_pumpRunLoop        = _pumpInvokePointer.invoke(_pumpFnObjcMsgSend, new Object[]{ new Object[]{ nsRunLoopCls, selCurrentRL } });

			// --- NSDate class + dateWithTimeIntervalSinceNow: selector ---
			_pumpNsDateCls   = _pumpInvokePointer.invoke(_pumpFnGetClass, new Object[]{ new Object[]{"NSDate"} });
			_pumpSelDateWith = _pumpInvokePointer.invoke(_pumpFnSelRegister, new Object[]{ new Object[]{"dateWithTimeIntervalSinceNow:"} });

			// --- runUntilDate: selector ---
			_pumpSelRunUntil = _pumpInvokePointer.invoke(_pumpFnSelRegister, new Object[]{ new Object[]{"runUntilDate:"} });

			_jnaPumpAvail = true;
		} catch (Throwable ex) {
			System.err.println("[AWB-MAC-DIAG] pumpMainRunLoop JNA init FAILED: " + ex.getMessage());
		}
		return _jnaPumpAvail;
	}

	/**
	 * Requests to bring the current application to the foreground on macOS.
	 * <p>
	 * This is a no-op on non-macOS systems. On macOS it first tries the modern
	 * {@code -[NSApplication activate]} API (macOS 14+, the replacement for the
	 * deprecated {@code activateIgnoringOtherApps:}) via JNA and the Objective-C
	 * runtime, then falls back to the legacy {@code com.apple.eawt.Application}
	 * reflection (effective on macOS&nbsp;&le;&nbsp;13). Everything is done via
	 * reflection so there is no compile-time dependency on JNA or the macOS-only
	 * {@code com.apple.eawt} package.
	 * </p>
	 * <p>
	 * <b>Why:</b> on macOS Tahoe&nbsp;26 (and 14+/15+) the legacy
	 * {@code requestForeground(true)} call reaches the now-deprecated
	 * {@code activateIgnoringOtherApps:}, which is a no-op, so the process is
	 * never activated and all windows stay in the background. The new
	 * {@code activate} API is required.
	 * </p>
	 * @return {@code true} if a foreground request was issued successfully
	 */
	public static boolean requestForeground() {
		if (isMacOperatingSystem() == false) return false;
		if (requestForegroundViaJna() == true) {
			return true;
		}
		return requestForegroundViaEawt();
	}

	/**
	 * Calls {@code -[NSApplication activate]} (macOS 14+) through JNA and the
	 * Objective-C runtime, loaded entirely via reflection so that no compile-time
	 * dependency on {@code com.sun.jna} is introduced. JNA is expected to be
	 * present on the runtime classpath (it is part of the Eclipse platform that
	 * AWB runs on); if it is absent this method prints a diagnostic and returns
	 * {@code false} so the caller can fall back to the legacy EAWT path.
	 * @return {@code true} if the {@code activate} message was sent successfully
	 */
	private static boolean requestForegroundViaJna() {
		try {
			// --- OSGi workaround: de.enflexit.common does not import com.sun.jna,
			//     so Class.forName fails. Locate the JNA bundle in the running
			//     framework and load the class THROUGH that bundle's classloader.
			BundleContext context = FrameworkUtil.getBundle(SystemEnvironmentHelper.class).getBundleContext();
			if (context == null) {
				System.err.println("[AWB-MAC-DIAG] JNA: no BundleContext available");
				return false;
			}
			Class<?> nativeLibraryCls = null;
			for (Bundle b : context.getBundles()) {
				try {
					nativeLibraryCls = b.loadClass("com.sun.jna.NativeLibrary");
					System.err.println("[AWB-MAC-DIAG] JNA found in bundle: " + b.getSymbolicName() + " " + b.getVersion());
					break;
				} catch (Throwable ignore) {
					// this bundle can't see JNA - try the next
				}
			}
			if (nativeLibraryCls == null) {
				System.err.println("[AWB-MAC-DIAG] JNA: no bundle could load com.sun.jna.NativeLibrary");
				return false;
			}

			Method getInstance = nativeLibraryCls.getMethod("getInstance", String.class);
			Object objcLib = getInstance.invoke(null, "objc");

			Method getFunction = nativeLibraryCls.getMethod("getFunction", String.class);
			Object fnGetClass    = getFunction.invoke(objcLib, "objc_getClass");
			Object fnSelRegister = getFunction.invoke(objcLib, "sel_registerName");
			Object fnObjcMsgSend = getFunction.invoke(objcLib, "objc_msgSend");

			Class<?> functionCls = Class.forName("com.sun.jna.Function", true, nativeLibraryCls.getClassLoader());
			Method invokePointer  = functionCls.getMethod("invokePointer", Object[].class);
			Method invokeNoReturn = functionCls.getMethod("invoke", Object[].class);   // void variant

			// --- [NSApplication sharedApplication] -> NSApp ---
			Object nsAppClass   = invokePointer.invoke(fnGetClass,    new Object[]{ new Object[]{"NSApplication"} });
			Object selSharedApp = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"sharedApplication"} });
			Object nsApp        = invokePointer.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ nsAppClass, selSharedApp } });

			// --- Diagnostic: are we on the AppKit main thread? ---
			Object nsThreadCls  = invokePointer.invoke(fnGetClass,    new Object[]{ new Object[]{"NSThread"} });
			Object selIsMain    = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"isMainThread"} });
			Method invokeInt    = functionCls.getMethod("invokeInt", Object[].class);
			int isMain          = (Integer) invokeInt.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ nsThreadCls, selIsMain } });
			System.err.println("[AWB-MAC-DIAG] isMainThread=" + (isMain != 0));

			// --- @selector(activate) (macOS 14+ replacement of activateIgnoringOtherApps:) ---
			Object selActivate = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"activate"} });

			// --- [NSApp setActivationPolicy: NSApplicationActivationPolicyRegular] (value=0) ---
			//     Ensures the app is a Regular app that can be activated.
			Object selSetPolicy = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"setActivationPolicy:"} });
			invokeNoReturn.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ nsApp, selSetPolicy, Long.valueOf(0) } });

			// --- Call [NSApp activate] on the main thread ---
			if (isMain != 0) {
				// --- We're on the main thread: call activate directly ---
				invokeNoReturn.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ nsApp, selActivate } });
			} else {
				// --- We're on a non-main thread (e.g. EDT): dispatch to main thread ---
				//     waitUntilDone:YES blocks until activate finishes. This works
				//     because the main thread pumps the NSRunLoop in
				//     AwbIApplication.waitForApplicationTermination().
				Object selPerfOnMain = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"performSelectorOnMainThread:withObject:waitUntilDone:"} });
				invokeNoReturn.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ nsApp, selPerfOnMain, selActivate, null, Boolean.TRUE } });
			}

			System.err.println("[AWB-MAC-DIAG] requestForegroundViaJna() SUCCEEDED ([NSApp activate])");
			return true;
		} catch (Throwable ex) {
			System.err.println("[AWB-MAC-DIAG] requestForegroundViaJna() FAILED: "
					+ ex.getClass().getName() + ": " + ex.getMessage());
			return false;
		}
	}

	/**
	 * Force-closes the Eclipse native splash screen on macOS by finding the
	 * borderless {@code NSWindow} in {@code [NSApp windows]} and sending it
	 * {@code orderOut:} on the main thread.
	 * <p>
	 * This is needed because the Eclipse launcher's splash-close mechanism
	 * relies on the native main thread running an {@code NSApplication} event
	 * loop (which only happens with {@code -XstartOnFirstThread}). Since the
	 * Swing variant must NOT use that flag, the launcher cannot dismiss the
	 * splash itself, so we do it manually via JNA.
	 * </p>
	 * <p>
	 * The splash is identified as a visible window with {@code styleMask == 0}
	 * (borderless) &mdash; the standard Eclipse launcher splash is a borderless
	 * {@code NSWindow} with the splash image as its content view. If no
	 * borderless window is found, a diagnostic dump of all windows is printed.
	 * </p>
	 * @return {@code true} if at least one splash window was closed
	 */
	public static boolean closeSplashScreen() {
		if (isMacOperatingSystem() == false) return false;
		try {
			// --- OSGi: locate JNA bundle and load objc functions ---
			BundleContext context = FrameworkUtil.getBundle(SystemEnvironmentHelper.class).getBundleContext();
			if (context == null) {
				System.err.println("[AWB-MAC-DIAG] closeSplashScreen: no BundleContext");
				return false;
			}
			Class<?> nativeLibraryCls = null;
			for (Bundle b : context.getBundles()) {
				try {
					nativeLibraryCls = b.loadClass("com.sun.jna.NativeLibrary");
					break;
				} catch (Throwable ignore) {
				}
			}
			if (nativeLibraryCls == null) {
				System.err.println("[AWB-MAC-DIAG] closeSplashScreen: JNA not found");
				return false;
			}

			Method getInstance = nativeLibraryCls.getMethod("getInstance", String.class);
			Object objcLib = getInstance.invoke(null, "objc");
			Method getFunction = nativeLibraryCls.getMethod("getFunction", String.class);
			Object fnGetClass    = getFunction.invoke(objcLib, "objc_getClass");
			Object fnSelRegister = getFunction.invoke(objcLib, "sel_registerName");
			Object fnObjcMsgSend = getFunction.invoke(objcLib, "objc_msgSend");

			Class<?> functionCls = Class.forName("com.sun.jna.Function", true, nativeLibraryCls.getClassLoader());
			Method invokePointer  = functionCls.getMethod("invokePointer", Object[].class);
			Method invokeLong     = functionCls.getMethod("invokeLong", Object[].class);
			Method invokeInt      = functionCls.getMethod("invokeInt", Object[].class);
			Method invokeNoReturn = functionCls.getMethod("invoke", Object[].class);

			// --- Get NSApp ---
			Object nsAppClass   = invokePointer.invoke(fnGetClass,    new Object[]{ new Object[]{"NSApplication"} });
			Object selSharedApp = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"sharedApplication"} });
			Object nsApp        = invokePointer.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ nsAppClass, selSharedApp } });

			// --- [NSApp windows] -> NSArray ---
			Object selWindows     = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"windows"} });
			Object windowsArray   = invokePointer.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ nsApp, selWindows } });

			// --- [array count] ---
			Object selCount       = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"count"} });
			Long count            = (Long) invokeLong.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ windowsArray, selCount } });
			System.err.println("[AWB-MAC-DIAG] closeSplashScreen: " + count + " window(s) in [NSApp windows]");

			// --- Prepare selectors for iteration ---
			Object selObjectAtIndex = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"objectAtIndex:"} });
			Object selStyleMask     = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"styleMask"} });
			Object selIsVisible     = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"isVisible"} });
			Object selOrderOut      = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"orderOut:"} });
			Object selPerfOnMain    = invokePointer.invoke(fnSelRegister, new Object[]{ new Object[]{"performSelectorOnMainThread:withObject:waitUntilDone:"} });

			// --- Iterate: close visible borderless windows (= splash) ---
			int closedCount = 0;
			for (long i = 0; i < count; i++) {
				Object win = invokePointer.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ windowsArray, selObjectAtIndex, Long.valueOf(i) } });
				if (win == null) continue;

				Long styleMask = (Long) invokeLong.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ win, selStyleMask } });
				int visible    = (Integer) invokeInt.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ win, selIsVisible } });

				if (visible != 0 && styleMask == 0L) {
					// --- Dispatch [win orderOut: nil] to the main thread ---
					invokeNoReturn.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ win, selPerfOnMain, selOrderOut, null, Boolean.FALSE } });
					closedCount++;
					System.err.println("[AWB-MAC-DIAG] closeSplashScreen: closed borderless window at index " + i);
				}
			}

			if (closedCount == 0) {
				// --- Diagnostic: dump all windows if no splash was found ---
				System.err.println("[AWB-MAC-DIAG] closeSplashScreen: no borderless window found, dumping all windows:");
				for (long i = 0; i < count; i++) {
					Object win = invokePointer.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ windowsArray, selObjectAtIndex, Long.valueOf(i) } });
					if (win == null) continue;
					Long styleMask = (Long) invokeLong.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ win, selStyleMask } });
					int visible    = (Integer) invokeInt.invoke(fnObjcMsgSend, new Object[]{ new Object[]{ win, selIsVisible } });
					System.err.println("[AWB-MAC-DIAG]   window[" + i + "]: styleMask=" + styleMask + " visible=" + (visible != 0));
				}
			} else {
				System.err.println("[AWB-MAC-DIAG] closeSplashScreen: closed " + closedCount + " splash window(s)");
			}
			return closedCount > 0;
		} catch (Throwable ex) {
			System.err.println("[AWB-MAC-DIAG] closeSplashScreen FAILED: "
					+ ex.getClass().getName() + ": " + ex.getMessage());
			return false;
		}
	}

	/**
	 * via reflection. Effective on macOS&nbsp;&le;&nbsp;13; a no-op on macOS&nbsp;14+
	 * where the underlying {@code activateIgnoringOtherApps:} is deprecated.
	 * <p>
	 * Requires {@code --add-opens java.desktop/com.apple.eawt=ALL-UNNAMED} in the
	 * launch configuration, otherwise the reflective invoke throws
	 * {@code InaccessibleObjectException} (caught here).
	 * </p>
	 * @return {@code true} if the legacy call was issued successfully
	 */
	private static boolean requestForegroundViaEawt() {
		try {
			Class<?> appClass = Class.forName("com.apple.eawt.Application");
			Method getApplication = appClass.getMethod("getApplication");
			getApplication.setAccessible(true);
			Object app = getApplication.invoke(null);
			Method requestForeground = appClass.getMethod("requestForeground", boolean.class);
			requestForeground.setAccessible(true);
			requestForeground.invoke(app, Boolean.TRUE);
			System.err.println("[AWB-MAC-DIAG] requestForegroundViaEawt() SUCCEEDED (legacy)");
			return true;
		} catch (Throwable ex) {
			System.err.println("[AWB-MAC-DIAG] requestForegroundViaEawt() FAILED: "
					+ ex.getClass().getName() + ": " + ex.getMessage());
			return false;
		}
	}
	
}
