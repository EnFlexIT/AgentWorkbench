package de.enflexit.awb.ws.core.session;

import org.eclipse.jetty.ee10.servlet.SessionHandler;
import org.eclipse.jetty.http.HttpCookie.SameSite;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.session.AbstractSessionCacheFactory;
import org.eclipse.jetty.session.DatabaseAdaptor;
import org.eclipse.jetty.session.DefaultSessionCacheFactory;
import org.eclipse.jetty.session.JDBCSessionDataStore.SessionTableSchema;
import org.eclipse.jetty.session.JDBCSessionDataStoreFactory;
import org.eclipse.jetty.session.NullSessionCacheFactory;
import org.eclipse.jetty.session.SessionCache;

import de.enflexit.awb.ws.core.JettyAttribute;
import de.enflexit.awb.ws.core.JettySessionSettings;

/**
 * The Class AWBSessionHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AWBSessionHandler extends SessionHandler {

	public static final String SESSION_TABLE_NAME = "web_sessions";

	/**
	 * Instantiates a new AWB session handler.
	 * @param sessionSettings the session settings
	 */
	public AWBSessionHandler(JettySessionSettings sessionSettings) {
		if (sessionSettings.isUseIndividualSettings()==true) {
			this.applySessionSettings(sessionSettings);
		}
	}
	/**
	 * Applies the specified JettySessionSettings.
	 * @param sessionSettings the session settings
	 */
	private void applySessionSettings(JettySessionSettings sessionSettings) {
		
		// --- setCheckingRemoteSessionIdEncoding() ---------------------------
		JettyAttribute<?> setCheckingRemoteSessionIdEncoding = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_CHECKING_REMOTE_SESSION_ID_ENCODING);
		if (setCheckingRemoteSessionIdEncoding!=null && setCheckingRemoteSessionIdEncoding.getValue()!=null) {
			this.setCheckingRemoteSessionIdEncoding((Boolean)setCheckingRemoteSessionIdEncoding.getValue());
		}
		
		// --- setMaxInactiveInterval() ---------------------------------------
		JettyAttribute<?> setMaxInactiveInterval = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_MAX_INACTIVE_INTERVAL);
		if (setMaxInactiveInterval!=null && setMaxInactiveInterval.getValue()!=null) {
			this.setMaxInactiveInterval((int)setMaxInactiveInterval.getValue());
		}
		
		// --- setCheckingRemoteSessionIdEncoding() ---------------------------
		JettyAttribute<?> setHttpOnly = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_HTTP_ONLY);
		if (setHttpOnly!=null && setHttpOnly.getValue()!=null) {
			this.setHttpOnly((Boolean)setHttpOnly.getValue());
		}
		
		// --- setRefreshCookieAge() ------------------------------------------
		JettyAttribute<?> setRefreshCookieAge = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_REFRESH_COOKIE_AGE);
		if (setRefreshCookieAge!=null && setRefreshCookieAge.getValue()!=null) {
			this.setRefreshCookieAge((Integer)setRefreshCookieAge.getValue());
		}

		// --- setSameSite() --------------------------------------------------
		JettyAttribute<?> setSameSite = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_SAME_SITE);
		if (setSameSite!=null && setSameSite.getValue()!=null) {
			SameSite sSite = SameSite.from(setSameSite.getValue().toString());
			if (sSite!=null) this.setSameSite(sSite);
		}
		
		// --- setSecureRequestOnly() -----------------------------------------
		JettyAttribute<?> setSecureRequestOnly = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_SECURE_REQUEST_ONLY);
		if (setSecureRequestOnly!=null && setSecureRequestOnly.getValue()!=null) {
			this.setSecureRequestOnly((Boolean)setSecureRequestOnly.getValue());
		}

		// --- setSessionCookie() ---------------------------------------------
		JettyAttribute<?> setSessionCookie = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_SESSION_COOKIE);
		if (setSessionCookie!=null && setSessionCookie.getValue()!=null) {
			this.setSessionCookie((String)setSessionCookie.getValue());
		}
		
		// --- setSessionCookie() ---------------------------------------------
		JettyAttribute<?> setSessionIdPathParameterName = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_SESSION_ID_PATH_PARAMETER_NAME);
		if (setSessionIdPathParameterName!=null && setSessionIdPathParameterName.getValue()!=null) {
			this.setSessionIdPathParameterName((String)setSessionIdPathParameterName.getValue());
		}

		// --- setSessionTrackingModes() --------------------------------------
		this.setSessionTrackingModes(DEFAULT_SESSION_TRACKING_MODES);
		
		// --- setUsingCookies() ----------------------------------------------
		JettyAttribute<?> setUsingCookies = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_USING_COOKIES);
		if (setUsingCookies!=null && setUsingCookies.getValue()!=null) {
			this.setUsingCookies((Boolean)setUsingCookies.getValue());
		}

		// --- setMaxCookieAge() ----------------------------------------------
		JettyAttribute<?> setMaxCookieAge = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_MAX_COOKIE_AGE);
		if (setMaxCookieAge!=null && setMaxCookieAge.getValue()!=null) {
			this.setMaxCookieAge((Integer)setMaxCookieAge.getValue());
		}
		
		// --- setSessionDomain() ---------------------------------------------
		JettyAttribute<?> setSessionDomain = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_SESSION_DOMAIN);
		if (setSessionDomain!=null && setSessionDomain.getValue()!=null) {
			this.setSessionDomain((String)setSessionDomain.getValue());
		}
		
		// --- setSessionPath() ----------------------------------------------
		JettyAttribute<?> setSessionPath = sessionSettings.getSessionAttribute(JettySessionSettings.KEY_SET_SESSION_PATH);
		if (setSessionPath!=null && setSessionPath.getValue()!=null) {
			this.setSessionPath((String)setSessionPath.getValue());
		}
		
	}
	
	
	// --------------------------------------------------------------	
	// --- From here, static configuration methods ------------------
	// --------------------------------------------------------------
	/**
	 * Adds the session cache factory.
	 * @param server the server instance
	 */
	public static void addBeanSessionCacheFactory(Server server) {
		
		boolean isUseNullSessionCache = false;
		
		AbstractSessionCacheFactory cacheFactory = null;
		if (isUseNullSessionCache==true) {
			cacheFactory = new NullSessionCacheFactory();
		} else {
			cacheFactory = new DefaultSessionCacheFactory();
		}

		cacheFactory.setEvictionPolicy(SessionCache.NEVER_EVICT);
		//Only useful with the EVICT_ON_INACTIVE policy
		cacheFactory.setSaveOnInactiveEviction(true);
		cacheFactory.setSaveOnCreate(true);

		cacheFactory.setRemoveUnloadableSessions(true);
		cacheFactory.setInvalidateOnShutdown(false);
		cacheFactory.setFlushOnResponseCommit(true);

		server.addBean(cacheFactory);
	}
	/**
	 * Adds the session data store factory.
	 * @param server the server instance
	 */
	public static void addBeanSessionDataStoreFactory(Server server) {
		
		// --- Create the DatabaseAdaptor --------------------------- 
		DatabaseAdaptor driverAdaptor = new DatabaseAdaptor();
		driverAdaptor.setDatasource(new AwbDatabaseAdaptorDataSource());
		
		// --- Create the SessionTableSchema ------------------------ 
		SessionTableSchema sts = new SessionTableSchema();
		sts.setTableName(SESSION_TABLE_NAME);
		
		// --- Create the data store factory ------------------------
		JDBCSessionDataStoreFactory jdbcSessionDataStoreFactory = new JDBCSessionDataStoreFactory();
		jdbcSessionDataStoreFactory.setSavePeriodSec(0);
		jdbcSessionDataStoreFactory.setGracePeriodSec(3600);
		jdbcSessionDataStoreFactory.setDatabaseAdaptor(driverAdaptor);
		jdbcSessionDataStoreFactory.setSessionTableSchema(sts);
		
		server.addBean(jdbcSessionDataStoreFactory);
		
	}
	
}
