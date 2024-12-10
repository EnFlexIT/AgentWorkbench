package de.enflexit.awb.simulation.transaction;

import jade.core.AID;

import java.io.Serializable;

import de.enflexit.awb.simulation.SimulationService;

/**
 * This class is used as generalized notification to agents, that are using 
 * the {@link SimulationService}. 
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentNotification implements Serializable {

	private static final long serialVersionUID = -5953628202535502036L;

	private AID sender;
	private boolean comingFromManager;
	
	private Object notification;
	private ProcessingInstruction processingInstruction;
	
	/**
	 * Instantiates a new environment notification.
	 *
	 * @param senderAID the senders AID
	 * @param comingFromManager true, if this message comes from the manager of a simulation
	 * @param notification the generalized notification
	 */
	public EnvironmentNotification(AID senderAID, boolean comingFromManager, Object notification) {
		this.sender = senderAID;
		this.comingFromManager = comingFromManager;
		this.notification = notification;
	}
	
	/**
	 * Sets the sender.
	 * @param sender the sender to set
	 */
	public void setSender(AID sender) {
		this.sender = sender;
	}
	/**
	 * Gets the sender.
	 * @return the sender
	 */
	public AID getSender() {
		return sender;
	}
	
	/**
	 * Sets that the notification is the coming from the manager.
	 * @param comingFromManager the commingFromManager to set
	 */
	public void setComingFromManager(boolean comingFromManager) {
		this.comingFromManager = comingFromManager;
	}
	/**
	 * Checks if the notification is coming from the manager.
	 * @return the commingFromManager
	 */
	public boolean isComingFromManager() {
		return comingFromManager;
	}

	/**
	 * Sets the notification.
	 * @param notification the notification to set
	 */
	public void setNotification(Object notification) {
		this.notification = notification;
	}
	/**
	 * Gets the notification.
	 * @return the notification
	 */
	public Object getNotification() {
		return notification;
	}
	
	
	/**
	 * Returns the processing instruction for the further handling of the notification.
	 * @return the processing instruction
	 */
	public ProcessingInstruction getProcessingInstruction() {
		if (this.processingInstruction==null) {
			this.processingInstruction = new ProcessingInstruction();
		}
		return this.processingInstruction;
	}
	/**
	 * Resets the processing instruction.
	 */
	public void resetProcessingInstruction() {
		this.processingInstruction=null;
	}
	
	/**
	 * Sets the notification to be delete after proceeding.
	 */
	public void delete(){
		this.getProcessingInstruction().setDelete(true);
		this.getProcessingInstruction().setBlock(false);
		this.getProcessingInstruction().setBlockPeriod(0);
		this.getProcessingInstruction().setMoveLast(false);
	}
	
	/**
	 * Sets the notification to be blocked for a time period in milliseconds.
	 * @param period the period
	 */
	public void block(long period) {
		this.getProcessingInstruction().setDelete(false);
		this.getProcessingInstruction().setBlock(true);
		this.getProcessingInstruction().setBlockPeriod(period);
		this.getProcessingInstruction().setMoveLast(false);
	}
	
	/**
	 * Sets the notification to be moved to the last position of the notification queue.
	 * In case that this is already the last notification, the notification will
	 * be block for a certain amount of time in milliseconds.
	 *
	 * @param period the time period in millisecond to block this {@link EnvironmentNotification}
	 */
	public void moveLastOrBlock(long period) {
		this.getProcessingInstruction().setDelete(false);
		this.getProcessingInstruction().setBlock(true);
		this.getProcessingInstruction().setBlockPeriod(period);
		this.getProcessingInstruction().setMoveLast(true);
	}
	
	/**
	 * The Class ProcessingInstruction can be used in order to set 
	 * the further handling with the {@link EnvironmentNotification}.
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	public class ProcessingInstruction implements Serializable {
		
		private static final long serialVersionUID = -4811282760719541477L;

		private boolean delete = true;
		private boolean block = false;
		private long blockPeriod = 100;
		private boolean moveLast = false;
		
		/**
		 * Sets the delete.
		 * @param delete the new delete
		 */
		public void setDelete(boolean delete) {
			this.delete = delete;
		}
		/**
		 * Checks if is delete.
		 * @return true, if is delete
		 */
		public boolean isDelete() {
			return delete;
		}

		/**
		 * Sets the block.
		 * @param block the new block
		 */
		public void setBlock(boolean block) {
			this.block = block;
		}
		/**
		 * Checks if is block.
		 * @return true, if is block
		 */
		public boolean isBlock() {
			return block;
		}
		
		/**
		 * Sets the block period.
		 * @param blockPeriod the new block period
		 */
		public void setBlockPeriod(long blockPeriod) {
			this.blockPeriod = blockPeriod;
		}
		/**
		 * Gets the block period.
		 * @return the block period
		 */
		public long getBlockPeriod() {
			return blockPeriod;
		}
		
		/**
		 * Sets the move last.
		 * @param moveLast the new move last
		 */
		public void setMoveLast(boolean moveLast) {
			this.moveLast = moveLast;
		}
		/**
		 * Checks if is move last.
		 * @return true, if is move last
		 */
		public boolean isMoveLast() {
			return moveLast;
		}
		
	}
	
}
