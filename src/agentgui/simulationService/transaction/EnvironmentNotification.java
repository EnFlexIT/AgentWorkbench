package agentgui.simulationService.transaction;

import jade.core.AID;

import java.io.Serializable;

public class EnvironmentNotification implements Serializable {

	private static final long serialVersionUID = -5953628202535502036L;

	private AID sender;
	private boolean commingFromManager;
	private Object notification;
	
	public EnvironmentNotification(AID senderAID, boolean commingFromManager, Object notification) {
		this.sender = senderAID;
		this.commingFromManager = commingFromManager;
		this.notification = notification;
	}
	
	/**
	 * @param sender the sender to set
	 */
	public void setSender(AID sender) {
		this.sender = sender;
	}
	/**
	 * @return the sender
	 */
	public AID getSender() {
		return sender;
	}
	
	/**
	 * @param commingFromManager the commingFromManager to set
	 */
	public void setCommingFromManager(boolean commingFromManager) {
		this.commingFromManager = commingFromManager;
	}
	/**
	 * @return the commingFromManager
	 */
	public boolean isCommingFromManager() {
		return commingFromManager;
	}

	/**
	 * @param notification the notification to set
	 */
	public void setNotification(Object notification) {
		this.notification = notification;
	}
	/**
	 * @return the notification
	 */
	public Object getNotification() {
		return notification;
	}
	
}
