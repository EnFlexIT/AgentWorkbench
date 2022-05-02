package de.enflexit.awb.ws.ui.server;

import javax.swing.Icon;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.HandlerHelper;

/**
 * The Class ServerTreeNodeServerService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTreeNodeHandlerService extends ServerTreeNodeObject {

	private AwbWebHandlerService handlerService;
	
	/**
	 * Instantiates a new server tree node object for handler services.
	 * @param handlerService the handler service
	 */
	public ServerTreeNodeHandlerService(AwbWebHandlerService handlerService) {
		this.handlerService = handlerService;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#toString()
	 */
	@Override
	public String toString() {
		return "<html><b>" + HandlerHelper.getContextPath(this.handlerService.getHandler()) + ":</b> '" + this.handlerService.getClass().getSimpleName() + "'</html>";
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getToolTipText()
	 */
	@Override
	protected String getToolTipText() {
		return "[" + this.getSourceBundle().getSymbolicName() + "] " + this.handlerService.getClass().getName();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getNodeIcon()
	 */
	@Override
	protected Icon getNodeIcon() {
		if (this.isRunningHandler()==true) {
			return BundleHelper.getImageIcon("GearGreen16.png");
		}
		return BundleHelper.getImageIcon("GearRed16.png");
	}

	/**
	 * Returns the source bundle of the service.
	 * @return the source bundle
	 */
	private Bundle getSourceBundle() {
		return FrameworkUtil.getBundle(this.handlerService.getClass());
	}
	/**
	 * Checks if the current server is running.
	 * @return true, if the server is running
	 */
	private boolean isRunningHandler() {
		return this.handlerService!=null && this.handlerService.getHandler()!=null && this.handlerService.getHandler().isRunning()==true;
	}
	
}
