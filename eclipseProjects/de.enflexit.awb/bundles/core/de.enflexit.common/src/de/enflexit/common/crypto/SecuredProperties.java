package de.enflexit.common.crypto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

/**
 * The Class SecuredProperties.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SecuredProperties {

	private Path secureStoragePath;
	private ISecurePreferences securedProperties;
	
	/**
	 * Instantiates a new secured properties.
	 * @param secureStoragePath the secure storage path
	 */
	public SecuredProperties(Path secureStoragePath) {
		this.secureStoragePath =  secureStoragePath;
		if (this.secureStoragePath==null) {
			throw new NullPointerException("The storage path is not allowed to be null!");
		}
	}
	
	/**
	 * Returns the secured AWB properties that are basically {@link ISecurePreferences}.
	 * @return the secured properties
	 */
	public ISecurePreferences getSecuredProperties() {
		if (securedProperties==null) {
			try {
				
				URL propURL = this.secureStoragePath.toUri().toURL();
				securedProperties = SecurePreferencesFactory.open(propURL, null);
				if (secureStoragePath.toFile().exists()==false) {
					securedProperties.flush();
				}
				
			} catch (MalformedURLException mUrlEx) {
				mUrlEx.printStackTrace();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
		return securedProperties;
	}
	
	/**
	 * Saves the secured properties .
	 */
	public void save() {
		try {
			this.getSecuredProperties().flush();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
	
	
	/**
	 * Will put the specified String to the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null
	 * @param key the key
	 * @param value the value
	 */
	public void putString(String nodePath, String key, String value) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				this.getSecuredProperties().put(key, value, true);
			} else {
				this.getSecuredProperties().node(nodePath).put(key, value, true);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
	}
	/**
	 * Will put the specified Boolean to the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null
	 * @param key the key
	 * @param value the value
	 */
	public void putBoolean(String nodePath, String key, boolean value) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				this.getSecuredProperties().putBoolean(key, value, value);
			} else {
				this.getSecuredProperties().node(nodePath).putBoolean(key, value, true);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
	}
	/**
	 * Will put the specified integer to the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null
	 * @param key the key
	 * @param value the value
	 */
	public void putInt(String nodePath, String key, int value) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				this.getSecuredProperties().putInt(key, value, true);
			} else {
				this.getSecuredProperties().node(nodePath).putInt(key, value, true);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
	}
	/**
	 * Will put the specified Long to the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null
	 * @param key the key
	 * @param value the value
	 */
	public void putLong(String nodePath, String key, long value) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				this.getSecuredProperties().putLong(key, value, true);
			} else {
				this.getSecuredProperties().node(nodePath).putLong(key, value, true);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
	}
	/**
	 * Will put the specified Float to the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null
	 * @param key the key
	 * @param value the value
	 */
	public void putFloat(String nodePath, String key, float value) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				this.getSecuredProperties().putFloat(key, value, true);
			} else {
				this.getSecuredProperties().node(nodePath).putFloat(key, value, true);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
	}
	/**
	 * Will put the specified Double to the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null
	 * @param key the key
	 * @param value the value
	 */
	public void putDouble(String nodePath, String key, double value) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				this.getSecuredProperties().putDouble(key, value, true);
			} else {
				this.getSecuredProperties().node(nodePath).putDouble(key, value, true);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
	}
	/**
	 * Will put the specified Byte Array to the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null
	 * @param key the key
	 * @param value the value
	 */
	public void putByteArry(String nodePath, String key, byte[] value) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				this.getSecuredProperties().putByteArray(key, value, true);
			} else {
				this.getSecuredProperties().node(nodePath).putByteArray(key, value, true);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Returns the stored String from the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null.
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the value found or the specified default value 
	 */
	public String getString(String nodePath, String key, String defaultValue) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				return this.getSecuredProperties().get(key, defaultValue);
			} else {
				return this.getSecuredProperties().node(nodePath).get(key, defaultValue);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns the stored Boolean from the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null.
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the value found or the specified default value 
	 */
	public Boolean getBoolean(String nodePath, String key, boolean defaultValue) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				return this.getSecuredProperties().getBoolean(key, defaultValue);
			} else {
				return this.getSecuredProperties().node(nodePath).getBoolean(key, defaultValue);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns the stored Integer from the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null.
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the value found or the specified default value 
	 */
	public Integer getInt(String nodePath, String key, int defaultValue) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				return this.getSecuredProperties().getInt(key, defaultValue);
			} else {
				return this.getSecuredProperties().node(nodePath).getInt(key, defaultValue);
			}
		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns the stored Long from the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null.
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the value found or the specified default value 
	 */
	public Long getLong(String nodePath, String key, long defaultValue) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				return this.getSecuredProperties().getLong(key, defaultValue);
			} else {
				return this.getSecuredProperties().node(nodePath).getLong(key, defaultValue);
			}

		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns the stored Float from the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null.
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the value found or the specified default value 
	 */
	public Float getFloat(String nodePath, String key, float defaultValue) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				return this.getSecuredProperties().getFloat(key, defaultValue);
			} else {
				return this.getSecuredProperties().node(nodePath).getFloat(key, defaultValue);
			}

		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns the stored Double from the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null.
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the value found or the specified default value 
	 */
	public Double getDouble(String nodePath, String key, double defaultValue) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				return this.getSecuredProperties().getDouble(key, defaultValue);
			} else {
				return this.getSecuredProperties().node(nodePath).getDouble(key, defaultValue);
			}

		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns the stored Byte Array from the secured properties.
	 *
	 * @param nodePath the optional node path. Can be null.
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the value found or the specified default value 
	 */
	public byte[] getByteArry(String nodePath, String key, byte[] defaultValue) {
		try {
			if (nodePath==null || nodePath.isBlank()==true) {
				return this.getSecuredProperties().getByteArray(key, defaultValue);
			} else {
				return this.getSecuredProperties().node(nodePath).getByteArray(key, defaultValue);
			}

		} catch (StorageException sEx) {
			sEx.printStackTrace();
		}
		return null;
	}
	
}
