package de.enflexit.awb.ws.core.model;

import javax.swing.Icon;

import org.eclipse.jetty.server.Handler;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.BundleHelper;

/**
 * The Class ServerTreeNodeServer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTreeNodeHandler extends AbstractServerTreeNodeObject {

	private Handler handler;
	private AwbWebHandlerService handlerService;
	

	/**
	 * Instantiates a new server tree node object for handler services.
	 *
	 * @param handler the handler
	 * @param handlerService the corresponding handler service
	 */
	public ServerTreeNodeHandler(AwbWebHandlerService handlerService) {
		this(null, handlerService);
	}
	/**
	 * Instantiates a new server tree node object for handler services.
	 *
	 * @param handler the handler
	 * @param handlerService the corresponding handler service
	 */
	public ServerTreeNodeHandler(Handler handler, AwbWebHandlerService handlerService) {
		this.handler = handler;
		this.handlerService = handlerService;
	}
	
	/**
	 * Returns the {@link AwbWebHandlerService}
	 * @return the handler service
	 */
	public AwbWebHandlerService getAwbWebHandlerService() {
		return handlerService;
	}
	/**
	 * Returns the current handler.
	 * @return the handler
	 */
	public Handler getHandler() {
		if (handler==null) {
			handler = this.handlerService.getHandler(); 
		}
		return handler;
	}
	
	/**
	 * Checks if the current server is running.
	 * @return true, if the server is running
	 */
	public boolean isRunningHandler() {
		return this.getHandler().isRunning()==true;
	}
	/**
	 * If available, returns the context path for the current handler.
	 * @return the context path
	 */
	public String getContextPath() {
		return HandlerHelper.getContextPath(this.getHandler());
	}
	
	/**
	 * Returns the source bundle of the service.
	 * @return the source bundle
	 */
	public Bundle getSourceBundle() {
		if (this.handlerService==null) {
			return FrameworkUtil.getBundle(this.handler.getClass());
		}
		return FrameworkUtil.getBundle(this.handlerService.getClass());
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#toString()
	 */
	@Override
	public String toString() {
		
		String displayText = "<html>";
		String contextPath = this.getContextPath();
		if (contextPath.isBlank()==false) {
			displayText += "<b>" + HandlerHelper.getContextPath(this.getHandler()) + ":</b> ";
		}
		displayText += "'";
		if (this.handlerService==null) {
			displayText += this.getHandler().getClass().getSimpleName() ;
		} else {
			displayText += this.handlerService.getClass().getSimpleName(); 
		}
		displayText += "'</html>";
		return displayText;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		String displayText = "[" + this.getSourceBundle().getSymbolicName() + "] ";
		if (this.handlerService==null) {
			displayText += this.handler.getClass().getName();
		} else {
			displayText += this.handlerService.getClass().getName();
		}
		return displayText;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getNodeIcon()
	 */
	@Override
	public Icon getNodeIcon() {
		if (this.isRunningHandler()==true) {
			return BundleHelper.getImageIcon("GearGreen16.png");
		}
		return BundleHelper.getImageIcon("GearRed16.png");
	}

}
