package de.enflexit.awb.core;

import java.lang.reflect.Method;

class MacRunLoopJNA {

	private static boolean available;
	private static Object modeRef;
	private static Object cFRunLoopGetMainResult;
	private static Object cFRunLoopRunInModeFn;
	private static Object cFRunLoopStopFn;
	private static Method invokeVoid;
	private static Method invokeInt;
	private static Method invokePointer;
	private static Object objcMsgSendFn;
	private static Object objcGetClassFn;
	private static Object selRegisterNameFn;

	static {
		try {
			Class<?> nativeLibraryClass = Class.forName("com.sun.jna.NativeLibrary");
			Class<?> functionClass = Class.forName("com.sun.jna.Function");
			Class<?> pointerClass = Class.forName("com.sun.jna.Pointer");

			Method getInstance = nativeLibraryClass.getMethod("getInstance", String.class);
			Method getGlobalVarAddr = nativeLibraryClass.getMethod("getGlobalVariableAddress", String.class);
			Method getFunction = nativeLibraryClass.getMethod("getFunction", String.class);
			Method getPointer = pointerClass.getMethod("getPointer", long.class);
			invokePointer = functionClass.getMethod("invokePointer", Object[].class);
			invokeVoid = functionClass.getMethod("invokeVoid", Object[].class);
			invokeInt = functionClass.getMethod("invokeInt", Object[].class);

			// --- CoreFoundation ------------------------------------------------
			Object cf = getInstance.invoke(null, "CoreFoundation");
			Object modeVar = getGlobalVarAddr.invoke(cf, "kCFRunLoopDefaultMode");
			modeRef = getPointer.invoke(modeVar, 0L);
			if (modeRef == null) {
				Object commonVar = getGlobalVarAddr.invoke(cf, "kCFRunLoopCommonModes");
				modeRef = getPointer.invoke(commonVar, 0L);
			}
			Object getMainFn = getFunction.invoke(cf, "CFRunLoopGetMain");
			cFRunLoopGetMainResult = invokePointer.invoke(getMainFn, new Object[] { new Object[0] });
			cFRunLoopRunInModeFn = getFunction.invoke(cf, "CFRunLoopRunInMode");
			cFRunLoopStopFn = getFunction.invoke(cf, "CFRunLoopStop");

			// --- Objective-C runtime (for NSApplication activation) ------------
			Object objc = getInstance.invoke(null, "objc");
			objcGetClassFn = getFunction.invoke(objc, "objc_getClass");
			selRegisterNameFn = getFunction.invoke(objc, "sel_registerName");
			objcMsgSendFn = getFunction.invoke(objc, "objc_msgSend");

			available = (modeRef != null && cFRunLoopGetMainResult != null
					&& cFRunLoopRunInModeFn != null && cFRunLoopStopFn != null
					&& objcGetClassFn != null && selRegisterNameFn != null && objcMsgSendFn != null);

		} catch (Exception e) {
			System.err.println("[MacRunLoopJNA] Init failed: " + e.getMessage());
		}
	}

	static boolean isAvailable() {
		return available;
	}

	static void pumpRunLoop(double seconds) {
		if (available) {
			try {
				invokeInt.invoke(cFRunLoopRunInModeFn, new Object[] { new Object[] { modeRef, seconds, Boolean.TRUE } });
			} catch (Exception e) {
				System.err.println("[MacRunLoopJNA] pumpRunLoop failed: " + e.getMessage());
			}
		}
	}

	static void stopMainRunLoop() {
		if (available) {
			try {
				invokeVoid.invoke(cFRunLoopStopFn, new Object[] { new Object[] { cFRunLoopGetMainResult } });
			} catch (Exception e) {
				System.err.println("[MacRunLoopJNA] stopMainRunLoop failed: " + e.getMessage());
			}
		}
	}

	/**
	 * Activates the application on macOS: sets the activation policy to Regular
	 * (shows dock icon) and brings the app to the foreground.
	 */
	static void activateApplication() {
		if (!available) return;
		try {
			// --- Get NSApplication class and sharedApplication instance --------
			Object nsAppClass = invokePointer.invoke(objcGetClassFn,
					new Object[] { new Object[] { "NSApplication" } });

			Object sharedAppSel = invokePointer.invoke(selRegisterNameFn,
					new Object[] { new Object[] { "sharedApplication" } });
			Object nsApp = invokePointer.invoke(objcMsgSendFn,
					new Object[] { new Object[] { nsAppClass, sharedAppSel } });

			// --- setActivationPolicy:NSApplicationActivationPolicyRegular (0) --
			Object setPolicySel = invokePointer.invoke(selRegisterNameFn,
					new Object[] { new Object[] { "setActivationPolicy:" } });
			invokeVoid.invoke(objcMsgSendFn,
					new Object[] { new Object[] { nsApp, setPolicySel, 0 } });

			// --- activateIgnoringOtherApps:YES --------------------------------
			Object activateSel = invokePointer.invoke(selRegisterNameFn,
					new Object[] { new Object[] { "activateIgnoringOtherApps:" } });
			invokeVoid.invoke(objcMsgSendFn,
					new Object[] { new Object[] { nsApp, activateSel, (byte) 1 } });

		} catch (Exception e) {
			System.err.println("[MacRunLoopJNA] activateApplication failed: " + e.getMessage());
		}
	}
}
