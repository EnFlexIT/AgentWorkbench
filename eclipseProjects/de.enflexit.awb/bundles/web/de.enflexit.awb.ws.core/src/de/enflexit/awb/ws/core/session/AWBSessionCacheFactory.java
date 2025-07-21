package de.enflexit.awb.ws.core.session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.jetty.session.AbstractSessionCacheFactory;
import org.eclipse.jetty.session.DefaultSessionCache;
import org.eclipse.jetty.session.ManagedSession;
import org.eclipse.jetty.session.SessionCache;
import org.eclipse.jetty.session.SessionManager;

/**
 * A factory for creating AWBSessionCache objects.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AWBSessionCacheFactory extends AbstractSessionCacheFactory {

	private ConcurrentMap<String, ManagedSession> sessionTreeMap;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.session.AbstractSessionCacheFactory#newSessionCache(org.eclipse.jetty.session.SessionManager)
	 */
	@Override
	public SessionCache newSessionCache(SessionManager manager) {
		return new DefaultSessionCache(manager, this.getSessionTreeMap());
	}
	/**
	 * Returns the session tree map.
	 * @return the session tree map
	 */
	private ConcurrentMap<String, ManagedSession>  getSessionTreeMap() {
		if (sessionTreeMap==null) {
			sessionTreeMap = new ConcurrentHashMap<>();
		}
		return sessionTreeMap;
	}
	
}
