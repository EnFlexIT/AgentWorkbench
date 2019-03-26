package agentgui.envModel.graph.visualisation;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import jade.lang.acl.ACLMessage;

/**
 * This interface can be implemented by {@link NetworkComponentAdapter}s to handle ACLMessages that are forwarded from the {@link DisplayAgent}
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 */
public interface ACLMessageForwardingListener {
	
	/**
	 * Method for handling ACL messages that have been forwarded from the {@link DisplayAgent}. 
	 * @param forwardedMessage the forwarded message
	 */
	public void forwardACLMessage(ACLMessage forwardedMessage);
	
}
