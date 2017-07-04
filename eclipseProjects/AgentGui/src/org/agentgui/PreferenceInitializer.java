package org.agentgui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

/**
 * The Class AbstractPreferenceInitializer.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * Instantiates a new abstract preference initializer.
	 */
	public PreferenceInitializer() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences prefs = DefaultScope.INSTANCE.getNode("org.agentgui");
		prefs.put("agentgui", "test");
	}

}
