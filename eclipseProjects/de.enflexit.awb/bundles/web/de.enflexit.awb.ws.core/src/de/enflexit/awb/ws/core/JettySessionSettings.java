package de.enflexit.awb.ws.core;

import java.io.Serializable;
import java.util.TreeMap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The Class JettySessionSettings.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JettySessionSettings", propOrder = {
	    "useIndividualSettings",
		"settings"
})
public class JettySessionSettings implements Serializable {

	private static final long serialVersionUID = -7243999870329375788L;
	
	// --- see https://jetty.org/docs/jetty/12/programming-guide/server/session.html#handler for further explanations 
	public static final String KEY_SET_SESSION_COOKIE = "a) Session Cookie (the session identifier for cookies)";
	public static final String KEY_SET_SESSION_ID_PATH_PARAMETER_NAME = "b) Session ID Path Parameter Name (the session identifier for path parameter)";

	public static final String KEY_SET_USING_COOKIES = "c) Using Cookies (determines whether the SessionHandler uses cookies)";
	
	public static final String KEY_SET_MAX_COOKIE_AGE = "d) Max Cookie Age (seconds that the session cookie will be valid)";
	public static final String KEY_SET_MAX_INACTIVE_INTERVAL = "e) Max Inactive Interval (seconds after which an unused session may be scavenged)";
	public static final String KEY_SET_REFRESH_COOKIE_AGE = "f) Refresh Cookie Age (seconds to reset the session cookie, if 'MaxCookieAge' is non-zero)";

	public static final String KEY_SET_SESSION_DOMAIN = "g) Session Domain";
	public static final String KEY_SET_SESSION_PATH = "h) Session Path";
	
	public static final String KEY_SET_SAME_SITE = "i) Same Site (NONE, STRICT or LAX)";
	public static final String KEY_SET_SECURE_REQUEST_ONLY = "j) Secure Request Only";
	public static final String KEY_SET_CHECKING_REMOTE_SESSION_ID_ENCODING = "k) Checking Remote Session ID Encoding";
	public static final String KEY_SET_HTTP_ONLY = "l) Http Only (session cookie will not be exposed to client-side)";
	
	private boolean useIndividualSettings;
	private TreeMap<String, JettyAttribute<?>> settings;
	
	private transient TreeMap<String, JettyAttribute<?>> defaultSessionSettings;
	
	
	/**
	 * Checks if is use individual session settings.
	 * @return true, if is use individual session settings
	 */
	public boolean isUseIndividualSettings() {
		return useIndividualSettings;
	}
	/**
	 * Sets the use individual session settings.
	 * @param useIndividualSettings the new use individual session settings
	 */
	public void setUseIndividualSettings(boolean useIndividualSettings) {
		this.useIndividualSettings = useIndividualSettings;
	}
	
	
	/**
	 * Returns the security configuration tree map.
	 * @return the security configuration tree map
	 */
	public TreeMap<String, JettyAttribute<?>> getSettings() {
		if (settings==null) {
			settings = new TreeMap<>();
		}
		if (settings.size()!=this.getDefaultSessionSettings().size()) {
			this.addMissingSessionSettings(settings);
		}
		return settings;
	}
	/**
	 * Will add missing session settings.
	 */
	private void addMissingSessionSettings(TreeMap<String, JettyAttribute<?>> currentSettings) {
		TreeMap<String, JettyAttribute<?>> defaults = this.getDefaultSessionSettings();
		for (String key : defaults.keySet()) {
			if (currentSettings.get(key)==null) {
				currentSettings.put(key, defaults.get(key));
			}
		}
	}
	
	/**
	 * Returns the session attribute for the specified key.
	 *
	 * @param key the key
	 * @return the session attribute
	 */
	public JettyAttribute<?> getSessionAttribute(String key) {
		return this.getSettings().get(key);
	}
	/**
	 * Sets the attribute.
	 * @param attribute the new attribute
	 */
	public void setSessionAttribute(JettyAttribute<?> attribute) {
		this.getSettings().put(attribute.getKey(), attribute);
	}
	
	
	/**
	 * Returns the the default session settings.
	 * @return the default session settings
	 */
	public TreeMap<String, JettyAttribute<?>> getDefaultSessionSettings() {
		if (defaultSessionSettings==null) {
			defaultSessionSettings = new TreeMap<>();
		
			this.addAttribute(defaultSessionSettings, new JettyAttribute<Boolean>(KEY_SET_CHECKING_REMOTE_SESSION_ID_ENCODING, false));
			this.addAttribute(defaultSessionSettings, new JettyAttribute<Boolean>(KEY_SET_HTTP_ONLY, false));
			
			this.addAttribute(defaultSessionSettings, new JettyAttribute<Integer>(KEY_SET_MAX_INACTIVE_INTERVAL, -1));
			this.addAttribute(defaultSessionSettings, new JettyAttribute<Integer>(KEY_SET_REFRESH_COOKIE_AGE, -1));
			this.addAttribute(defaultSessionSettings, new JettyAttribute<Integer>(KEY_SET_MAX_COOKIE_AGE, -1));
			
			this.addAttribute(defaultSessionSettings, new JettyAttribute<String>(KEY_SET_SAME_SITE, ""));
			this.addAttribute(defaultSessionSettings, new JettyAttribute<Boolean>(KEY_SET_SECURE_REQUEST_ONLY, true));
			
			this.addAttribute(defaultSessionSettings, new JettyAttribute<String>(KEY_SET_SESSION_COOKIE, "AWB_SESSIONID"));
			this.addAttribute(defaultSessionSettings, new JettyAttribute<String>(KEY_SET_SESSION_ID_PATH_PARAMETER_NAME, "awb_sessionid"));
			
			this.addAttribute(defaultSessionSettings, new JettyAttribute<Boolean>(KEY_SET_USING_COOKIES, true));
			
			this.addAttribute(defaultSessionSettings, new JettyAttribute<String>(KEY_SET_SESSION_DOMAIN, ""));
			this.addAttribute(defaultSessionSettings, new JettyAttribute<String>(KEY_SET_SESSION_PATH, ""));
		}
		return defaultSessionSettings;
	}
	/**
	 * Adds an attribute to the specified settings TreeMap.
	 * @param settings the settings
	 * @param attribute the attribute
	 */
	private void addAttribute(TreeMap<String, JettyAttribute<?>> settings, JettyAttribute<?> attribute) {
		if (settings==null || attribute==null) return;
		settings.put(attribute.getKey(), attribute);
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null || (! (compObj instanceof JettySessionSettings))) return false;
		if (compObj==this) return true;

		JettySessionSettings jssComp = (JettySessionSettings) compObj;
		
		if (jssComp.isUseIndividualSettings()!=this.isUseIndividualSettings()) return false;
		if (jssComp.getSettings().equals(this.getSettings())==false) return false;
		
		return true;
	}
	
}
