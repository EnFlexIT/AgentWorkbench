package de.enflexit.df.core;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.BackingStoreException;


/**
 * The Class DataFramePreferences.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFramePreferences {

	// ----------------------------------------------------
	// --- Global constants area --------------------------
	// ----------------------------------------------------
	public static final String DATA_FRAME_PAGINATION_ACTIVATED = "DATA_FRAME_PAGINATION_ACTIVATED";
	public static final String DATA_FRAME_NUMBER_OF_RECORDS_PER_PAGE = "DATA_FRAME_NUMBER_OF_RECORDS_PER_PAGE";
	
	// ----------------------------------------------------
	// --- Singleton area ---------------------------------
	// ----------------------------------------------------
	private static DataFramePreferences instance;
	public static DataFramePreferences getInstance() {
		if (instance==null) {
			instance = new DataFramePreferences();
			instance.setDefaultValues();
		}
		return instance;
	}
	private DataFramePreferences() {}
	
	// ----------------------------------------------------
	// --- Runtime area -----------------------------------
	// ----------------------------------------------------
	private IEclipsePreferences eclipsePreferences;

	/**
	 * Returns the eclipse preferences.
	 * @return the eclipse preferences
	 */
	public IEclipsePreferences getEclipsePreferences() {
		if (eclipsePreferences==null) {
			Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			IScopeContext iScopeContext = ConfigurationScope.INSTANCE;
			eclipsePreferences = iScopeContext.getNode(bundle.getSymbolicName());
		}
		return eclipsePreferences;
	}
	/**
	 * Returns the preferences key list as snapshot.
	 * @return the key list snapshot
	 */
	private List<String> getKeyListSnapshot() {
		
		List<String> keyList = null;
		try {
			keyList = Arrays.asList(this.getEclipsePreferences().keys());
		} catch (BackingStoreException bsEx) {
			bsEx.printStackTrace();
		}
		return keyList;
	}
	
	/**
	 * Sets the default values.
	 */
	private void setDefaultValues() {
		
		boolean isOverwriteStoredPreferences = false;
		
		IEclipsePreferences prefs = this.getEclipsePreferences();
		List<String> keyList = this.getKeyListSnapshot();
		
		if (isOverwriteStoredPreferences==true || keyList.contains(DATA_FRAME_PAGINATION_ACTIVATED)==false) {
			prefs.putBoolean(DataFramePreferences.DATA_FRAME_PAGINATION_ACTIVATED, true);
		}
		if (isOverwriteStoredPreferences==true || keyList.contains(DATA_FRAME_NUMBER_OF_RECORDS_PER_PAGE)==false) {
			prefs.putInt(DataFramePreferences.DATA_FRAME_NUMBER_OF_RECORDS_PER_PAGE, 1000);
		}
		
		
		this.save();
	}
	
	
	/**
	 * Saves the bundle properties.
	 */
	public void save() {
		try {
			this.getEclipsePreferences().flush();
		} catch (BackingStoreException bsEx) {
			bsEx.printStackTrace();
		}
	}
	
	public void putString(String key, String value) {
		this.getEclipsePreferences().put(key, value);
		this.save();
	}
	public void putBoolean(String key, boolean value) {
		this.getEclipsePreferences().putBoolean(key, value);
		this.save();
	}
	public void putInt(String key, int value) {
		this.getEclipsePreferences().putInt(key, value);
		this.save();
	}
	public void putLong(String key, long value) {
		this.getEclipsePreferences().putLong(key, value);
		this.save();
	}
	public void putFloat(String key, float value) {
		this.getEclipsePreferences().putFloat(key, value);
		this.save();
	}
	public void putDouble(String key, double value) {
		this.getEclipsePreferences().putDouble(key, value);
		this.save();
	}
	public void putByteArry(String key, byte[] value) {
		this.getEclipsePreferences().putByteArray(key, value);
		this.save();
	}
	
	public String getString(String key, String defaultValue) {
		return this.getEclipsePreferences().get(key, defaultValue);
	}
	public boolean getBoolean(String key, boolean defaultValue) {
		return this.getEclipsePreferences().getBoolean(key, defaultValue);
	}
	public int getInt(String key, int defaultValue) {
		return this.getEclipsePreferences().getInt(key, defaultValue);
	}
	public long getLong(String key, long defaultValue) {
		return this.getEclipsePreferences().getLong(key, defaultValue);
	}
	public float getFloat(String key, float defaultValue) {
		return this.getEclipsePreferences().getFloat(key, defaultValue);
	}
	public double getDouble(String key, double defaultValue) {
		return this.getEclipsePreferences().getDouble(key, defaultValue);
	}
	public byte[] getByteArry(String key, byte[] defaultValue) {
		return this.getEclipsePreferences().getByteArray(key, defaultValue);
	}
	
}
