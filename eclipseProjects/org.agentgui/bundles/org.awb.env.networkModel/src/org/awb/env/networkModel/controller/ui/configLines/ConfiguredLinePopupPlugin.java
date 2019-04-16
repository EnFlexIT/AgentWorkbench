package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGui;

import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * The Class ConfiguredLinePopupPlugin.
 */
public class ConfiguredLinePopupPlugin extends AbstractPopupGraphMousePlugin implements Observer {

	private BasicGraphGui basicGraphGui = null;
	private GraphEnvironmentController graphController = null;

	
	/**
     * Creates a new instance of ConfiguredLinePopupPlugin.
     * @param basicGraphGui the instance of the parent {@link BasicGraphGui}
     */
    public ConfiguredLinePopupPlugin(BasicGraphGui basicGraphGui) {
        super(MouseEvent.BUTTON3_MASK);
        this.basicGraphGui = basicGraphGui;
        this.graphController = this.basicGraphGui.getGraphEnvironmentController();
        this.graphController.addObserver(this);
    }
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin#handlePopup(java.awt.event.MouseEvent)
	 */
	@Override
	protected void handlePopup(MouseEvent me) {

		
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {

		
	}

}
