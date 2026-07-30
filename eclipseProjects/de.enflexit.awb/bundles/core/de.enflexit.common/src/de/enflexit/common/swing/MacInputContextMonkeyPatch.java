package de.enflexit.common.swing;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import sun.misc.Unsafe;

import de.enflexit.common.SystemEnvironmentHelper;

/**
 * Monkey-patches InputContext.getInstance() on macOS JDK 21 to prevent
 * a native hang in CInputMethod.getNativeLocale().
 * 
 * This is a workaround for a known JDK bug where InputContext.getInstance()
 * calls into native code that hangs on macOS. The fix uses Unsafe.allocateInstance()
 * to create a no-op InputContext without calling the constructor, then replaces
 * the static icMap with a map that always returns this no-op instance.
 * 
 * Tradeoff: CJK/IME text input will not work. For Latin-based locales (de_DE,
 * en_US, etc.) this is acceptable.
 * 
 * Must be called early in application startup, before any Swing UI is created.
 * 
 * Requires JVM flags:
 *   --add-opens java.desktop/java.awt=ALL-UNNAMED
 *   --add-opens java.base/java.lang=ALL-UNNAMED
 */
public final class MacInputContextMonkeyPatch {

	private MacInputContextMonkeyPatch() {
		// prevent instantiation
	}

	/**
	 * Applies the monkey-patch if running on macOS.
	 * Must be called early in application startup, before any Swing UI is created.
	 */
	public static void applyIfMac() {
		if (SystemEnvironmentHelper.isMacOperatingSystem() == false) {
			return;
		}

		try {
			// Get the InputContext class via system class loader (bypasses OSGi).
			Class<?> icClass = ClassLoader.getSystemClassLoader().loadClass("java.awt.InputContext");

			// Use Unsafe.allocateInstance() to create an InputContext without
			// calling the constructor (which hangs in native code).
			// The resulting instance has all fields set to default values (null, 0, false).
			Unsafe unsafe = getUnsafe();
			Object noOpInstance = unsafe.allocateInstance(icClass);

			// Replace the static icMap in InputContext with a map that always
			// returns the no-op instance for get(), preventing new InputContext
			// instances from being created via the hanging constructor.
			Field icMapField = findIcMapField(icClass);
			if (icMapField != null) {
				icMapField.setAccessible(true);
				icMapField.set(null, new NoOpIcMap(noOpInstance));
			}

			// Clear the inputContext field in all existing Component instances.
			// This prevents existing components from having a reference to a
			// real InputContext that might have been created before the patch.
			clearComponentInputContexts();

		} catch (Exception e) {
			System.err.println("[MacInputContextMonkeyPatch] Failed to apply monkey-patch: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
		Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
		unsafeField.setAccessible(true);
		return (Unsafe) unsafeField.get(null);
	}

	private static Field findIcMapField(Class<?> icClass) {
		for (Field field : icClass.getDeclaredFields()) {
			field.setAccessible(true);
			String name = field.getName();
			if (name.equals("icMap") || name.equals("icmap") || name.equals("instances")) {
				Class<?> type = field.getType();
				if (Map.class.isAssignableFrom(type)) {
					return field;
				}
			}
		}
		return null;
	}

	private static void clearComponentInputContexts() {
		try {
			Class<?> componentClass = ClassLoader.getSystemClassLoader().loadClass("java.awt.Component");
			Field compInputContextField = componentClass.getDeclaredField("inputContext");
			compInputContextField.setAccessible(true);

			// Clear inputContext for all top-level windows
			Class<?> windowClass = ClassLoader.getSystemClassLoader().loadClass("java.awt.Window");
			Object[] windows = (Object[]) windowClass.getMethod("getWindows").invoke(null);

			for (Object window : windows) {
				try {
					compInputContextField.set(window, null);
				} catch (Exception ignored) {
				}
			}

			// Also clear for all components in existing windows
			for (Object window : windows) {
				clearInputContextFromChildren(window, compInputContextField);
			}

		} catch (NoSuchFieldException | ClassNotFoundException e) {
			// Field name may vary across JDK versions; ignore.
		} catch (Exception e) {
			// Other reflection errors (NoSuchMethodException, etc.); ignore.
		}
	}

	private static void clearInputContextFromChildren(Object parent, Field field) {
		try {
			field.set(parent, null);
		} catch (Exception ignored) {
		}
		try {
			Object components = parent.getClass().getMethod("getComponents").invoke(parent);
			if (components instanceof Object[]) {
				for (Object child : (Object[]) components) {
					clearInputContextFromChildren(child, field);
				}
			}
		} catch (Exception ignored) {
		}
	}

	/**
	 * A Map implementation that always returns the same no-op InputContext
	 * for get(), preventing InputContext.getInstance() from calling the
	 * hanging constructor.
	 */
	private static class NoOpIcMap extends java.util.AbstractMap<java.awt.Component, Object> {
		private final Object noOpInstance;

		NoOpIcMap(Object noOpInstance) {
			this.noOpInstance = noOpInstance;
		}

		@Override
		public Object get(Object key) {
			return noOpInstance;
		}

		@Override
		public Object put(java.awt.Component key, Object value) {
			return null;
		}

		@Override
		public Set<java.util.Map.Entry<java.awt.Component, Object>> entrySet() {
			return Collections.emptySet();
		}
	}
}
