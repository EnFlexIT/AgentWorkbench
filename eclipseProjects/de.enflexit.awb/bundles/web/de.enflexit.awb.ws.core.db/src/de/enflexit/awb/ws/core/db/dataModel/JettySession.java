package de.enflexit.awb.ws.core.db.dataModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

/**
 * The Class JettySessions.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "web_sessions")
public class JettySession {
	
	private String sessionId;
	private String contextPath;
	private String virtualHost;	
	private String lastNode;
	
	private long accessTime;
	private long lastAccessTime;
	private long createTime;
	private long cookieTime;
	private long lastSavedTime;
	private long expiryTime;
	private long maxInterval;
	
	private Object map;

	
	/**
	 * Gets the session id.
	 * @return the sessionId
	 */
	@Id
	@Column(nullable=false)
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * Sets the session id.
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Gets the context path.
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}
	/**
	 * Sets the context path.
	 * @param contextPath the contextPath to set
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * Gets the virtual host.
	 * @return the virtualHost
	 */
	public String getVirtualHost() {
		return virtualHost;
	}
	/**
	 * Sets the virtual host.
	 * @param virtualHost the virtualHost to set
	 */
	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	/**
	 * Gets the last node.
	 * @return the lastNode
	 */
	public String getLastNode() {
		return lastNode;
	}
	/**
	 * Sets the last node.
	 * @param lastNode the lastNode to set
	 */
	public void setLastNode(String lastNode) {
		this.lastNode = lastNode;
	}

	/**
	 * Gets the access time.
	 * @return the accessTime
	 */
	public long getAccessTime() {
		return accessTime;
	}
	/**
	 * Sets the access time.
	 * @param accessTime the accessTime to set
	 */
	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}

	/**
	 * Gets the last access time.
	 * @return the lastAccessTime
	 */
	public long getLastAccessTime() {
		return lastAccessTime;
	}
	/**
	 * Sets the last access time.
	 * @param lastAccessTime the lastAccessTime to set
	 */
	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	/**
	 * Gets the creates the time.
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}
	/**
	 * Sets the creates the time.
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * Gets the cookie time.
	 * @return the cookieTime
	 */
	public long getCookieTime() {
		return cookieTime;
	}
	/**
	 * Sets the cookie time.
	 * @param cookieTime the cookieTime to set
	 */
	public void setCookieTime(long cookieTime) {
		this.cookieTime = cookieTime;
	}

	/**
	 * Gets the last saved time.
	 * @return the lastSavedTime
	 */
	public long getLastSavedTime() {
		return lastSavedTime;
	}
	/**
	 * Sets the last saved time.
	 * @param lastSavedTime the lastSavedTime to set
	 */
	public void setLastSavedTime(long lastSavedTime) {
		this.lastSavedTime = lastSavedTime;
	}

	/**
	 * Gets the expiry time.
	 * @return the expiryTime
	 */
	public long getExpiryTime() {
		return expiryTime;
	}
	/**
	 * Sets the expiry time.
	 * @param expiryTime the expiryTime to set
	 */
	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;
	}

	/**
	 * Gets the max interval.
	 * @return the maxInterval
	 */
	public long getMaxInterval() {
		return maxInterval;
	}
	/**
	 * Sets the max interval.
	 * @param maxInterval the maxInterval to set
	 */
	public void setMaxInterval(long maxInterval) {
		this.maxInterval = maxInterval;
	}

	/**
	 * Gets the map.
	 * @return the map
	 */
	@Lob
	@Column(name = "map", columnDefinition="BLOB")
	public Object getMap() {
		return map;
	}
	/**
	 * Sets the map.
	 * @param map the map to set
	 */
	public void setMap(Object map) {
		this.map = map;
	}
	
}
