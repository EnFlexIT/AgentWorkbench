package de.enflexit.awb.ws.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.core.JettyServerInstances;

/**
 * The Class HandlerHelper provides some static help methods to work with {@link Handler}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class HandlerHelper {

	/**
	 * Returns the context path of the specified Handler.
	 *
	 * @param handler the handler
	 * @return the context path
	 */
	public static String getContextPath(Handler handler) {
		
		String contextPath = "";
		try {
			// --- Get the handler ------------------------
			if (handler instanceof ContextHandler) {
				ContextHandler contHandler = (ContextHandler) handler;
				contextPath = contHandler.getContextPath();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contextPath;
	}

	/**
	 * Will sort the specified list of handler by their context path if available.
	 *
	 * @param handlerList the handler list
	 * @return the list
	 */
	public static List<Handler> sort(List<Handler> handlerList) {
		if (handlerList==null) return null;
		Collections.sort(handlerList, new Comparator<Handler>() {
			@Override
			public int compare(Handler h1, Handler h2) {
				String cp1 = HandlerHelper.getContextPath(h1);
				String cp2 = HandlerHelper.getContextPath(h2);
				return cp1.compareTo(cp2);
			}
		});
		return handlerList;
	}
	
	/**
	 * Returns the corresponding handler service for the specified handler.
	 *
	 * @param handlerServiceList the handler service list to search in
	 * @param handler the handler to search for
	 * @return the handler service
	 */
	public static AwbWebHandlerService getHandlerService(List<AwbWebHandlerService> handlerServiceList, Handler handler) {
		
		if (handlerServiceList==null || handlerServiceList.size()==0) return null;
		if (handler==null) return null;
		
		AwbWebHandlerService handlerServiceFound = null;
		for (int i = 0; i < handlerServiceList.size(); i++) {
			AwbWebHandlerService handlerService = handlerServiceList.get(i);
			if (handlerService.getHandler().equals(handler)==true) {
				handlerServiceFound = handlerService;
				break;
			}
		}
		return handlerServiceFound;
	}
	
	
	/**
	 * Return the handler tree for the actual handler and the running handler services on the specified JettyInstance.
	 *
	 * @param jettyServerInstances the jetty server instances
	 * @param handlerServiceList the handler service list
	 * @return the list of handler trees
	 */
	public static List<DefaultMutableTreeNode> getHandlerTrees(JettyServerInstances jettyServerInstances, List<AwbWebHandlerService> handlerServiceList) {
		if (jettyServerInstances==null || jettyServerInstances.getServerHandlerList()==null || jettyServerInstances.getServerHandlerList().size()==0) {
			// --- Server is not running yet --------------
			return HandlerHelper.getHandlerTree(handlerServiceList);
		} else {
			// --- Server is running ----------------------
			return HandlerHelper.getHandlerTrees(HandlerHelper.sort(jettyServerInstances.getServerHandlerList()), handlerServiceList, null);
		}
	}
	/**
	 * Return the handler tree for a running server instance.
	 *
	 * @param handlerServiceList the handler service list
	 * @param jettyServerInstances the jetty server instances
	 * @param parentNode the parent node
	 * @return the handler tree
	 */
	private static List<DefaultMutableTreeNode> getHandlerTrees(List<Handler> serverHandlerList, List<AwbWebHandlerService> handlerServiceList, DefaultMutableTreeNode parentNode) {

		// --- Define result list ---------------------------------------------
		List<DefaultMutableTreeNode> handlerNodes = new ArrayList<>();

		// --- Get the list of server handler ---------------------------------
		for (int i = 0; i < serverHandlerList.size(); i++) {
			// --- Get single handler, its service and create node ------------
			Handler handler = serverHandlerList.get(i);
			AwbWebHandlerService handlerService = HandlerHelper.getHandlerService(handlerServiceList, handler);
			DefaultMutableTreeNode handlerNode = new DefaultMutableTreeNode(new ServerTreeNodeHandler(handler, handlerService));
			handlerNodes.add(handlerNode);
			
			if (handler instanceof HandlerWrapper) {
				HandlerWrapper handlerWrapper = (HandlerWrapper)handler;
				List<Handler> subHandlerList = HandlerHelper.sort(Arrays.asList(handlerWrapper.getHandlers()));
				List<DefaultMutableTreeNode> subHandlerNodes = HandlerHelper.getHandlerTrees(subHandlerList, handlerServiceList, handlerNode);
				HandlerHelper.addSubNodes(handlerNode, subHandlerNodes);
				
			} else if (handler instanceof HandlerCollection) {
				HandlerCollection handlerCollection = (HandlerCollection) handler;
				List<Handler> subHandlerList = HandlerHelper.sort(Arrays.asList(handlerCollection.getHandlers()));
				List<DefaultMutableTreeNode>subHandlerNodes = HandlerHelper.getHandlerTrees(subHandlerList, handlerServiceList, handlerNode);
				HandlerHelper.addSubNodes(handlerNode, subHandlerNodes);
			}
		}
		return handlerNodes;
	}
	
	/**
	 * Adds the specified list of sub nodes to the specified parent node.
	 *
	 * @param parentNode the parent node
	 * @param subNodeList the sub node list
	 */
	private static void addSubNodes(DefaultMutableTreeNode parentNode, List<DefaultMutableTreeNode> subNodeList) {
		if (parentNode==null || subNodeList==null) return;
		for (DefaultMutableTreeNode subHandlerNode : subNodeList) {
			parentNode.add(subHandlerNode);
		}
	}
	
	
	/**
	 * Return the handler tree for server that is not yet executed.
	 *
	 * @param handlerServiceList the handler service list
	 * @param parentNode the parent node
	 * @return the handler tree
	 */
	private static List<DefaultMutableTreeNode> getHandlerTree(List<AwbWebHandlerService> handlerServiceList) {
		List<DefaultMutableTreeNode> handlerNodes = new ArrayList<>();
		for (AwbWebHandlerService handlerService : handlerServiceList) {
			handlerNodes.add(new DefaultMutableTreeNode(new ServerTreeNodeHandler(handlerService)));
		}
		return handlerNodes;
	}
	
	
}
