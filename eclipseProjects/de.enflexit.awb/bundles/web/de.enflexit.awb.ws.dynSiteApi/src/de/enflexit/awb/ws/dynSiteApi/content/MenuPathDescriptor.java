package de.enflexit.awb.ws.dynSiteApi.content;

/**
 * The Class MenuPathDescriptor.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MenuPathDescriptor {

	private String pathID;
	private String pathCaption;
	private String pathPosition;
	private int depth;
	
	
	/**
	 * Returns the path ID.
	 * @return the pathID
	 */
	public String getPathID() {
		return pathID;
	}
	/**
	 * Sets the path ID.
	 * @param pathID the pathID to set
	 */
	public void setPathID(String pathID) {
		this.pathID = pathID;
	}
	
	/**
	 * Returns the path caption.
	 * @return the pathCaption
	 */
	public String getPathCaption() {
		return pathCaption;
	}
	/**
	 * Sets the path caption.
	 * @param pathCaption the pathCaption to set
	 */
	public void setPathCaption(String pathCaption) {
		this.pathCaption = pathCaption;
	}
	
	/**
	 * Returns the path position.
	 * @return the pathPosition
	 */
	public String getPathPosition() {
		return pathPosition;
	}
	/**
	 * Sets the path position.
	 * @param pathPosition the pathPosition to set
	 */
	public void setPathPosition(String pathPosition) {
		this.pathPosition = pathPosition;
	}
	
	/**
	 * Returns the depth.
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}
	/**
	 * Sets the depth.
	 * @param depth the new depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
}
