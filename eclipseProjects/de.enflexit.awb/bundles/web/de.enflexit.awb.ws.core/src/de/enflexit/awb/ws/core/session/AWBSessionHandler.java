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

/**
 * The Class AWBSessionHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AWBSessionHandler extends SessionHandler {

	public static final String SESSION_TABLE_NAME = "web_sessions";
	
	public AWBSessionHandler() {
		
		this.setDefaults();
		
	}
	
	/**
	 * Sets the defaults.
	 */
	private void setDefaults() {
		
		this.setCheckingRemoteSessionIdEncoding(false);
		this.setMaxInactiveInterval(600);
		
		this.setHttpOnly(false);
		this.setRefreshCookieAge(-1);
		
		this.setSameSite(SameSite.STRICT);
		
		this.setSecureRequestOnly(true);
		
		this.setSessionCookie("AWB_SESSIONID");
		this.setSessionIdPathParameterName("awb_sessionid");
		this.setSessionTrackingModes(DEFAULT_SESSION_TRACKING_MODES);

		this.setUsingCookies(true);
		
		this.setMaxCookieAge(600);
		
		this.setSessionDomain(__DefaultSessionDomain);
		this.setSessionPath("/");
	}
	
	
	// --------------------------------------------------------------	
	// --- From here, static configuration methods ------------------
	// --------------------------------------------------------------
	/**
	 * Adds the session cache factory.
	 * @param server the server instance
	 */
	public static void addBeanSessionCacheFactory(Server server) {
		
		boolean isUseNullSessionCache = true;
		
		AbstractSessionCacheFactory cacheFactory = null;
		if (isUseNullSessionCache==true) {
			cacheFactory = new NullSessionCacheFactory();
		} else {
			cacheFactory = new DefaultSessionCacheFactory();
		}

		cacheFactory.setEvictionPolicy(SessionCache.NEVER_EVICT);
		//Only useful with the EVICT_ON_INACTIVE policy
		cacheFactory.setSaveOnInactiveEviction(true);
		
		cacheFactory.setFlushOnResponseCommit(true);
		cacheFactory.setInvalidateOnShutdown(false);
		cacheFactory.setRemoveUnloadableSessions(true);
		cacheFactory.setSaveOnCreate(true);

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
