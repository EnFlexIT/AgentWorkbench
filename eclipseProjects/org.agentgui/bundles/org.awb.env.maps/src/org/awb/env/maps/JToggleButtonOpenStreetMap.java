package org.awb.env.maps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.toolbar.AbstractCustomToolbarComponent;

import de.enflexit.geography.BundleHelper;

/**
 * The Class JToggleButtonOpenStreetMap.
 */
public class JToggleButtonOpenStreetMap extends AbstractCustomToolbarComponent implements ActionListener {

	private JToggleButton jToogleButtonOSM;
	
	/**
	 * Instantiates a new j toggle button open street map.
	 * @param graphController the graph controller
	 */
	public JToggleButtonOpenStreetMap(GraphEnvironmentController graphController) {
		super(graphController);
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.toolbar.AbstractCustomToolbarComponent#getCustomComponent()
	 */
	@Override
	public JComponent getCustomComponent() {
		return this.getJToggleButtonOSM();
	}
	
	private JToggleButton getJToggleButtonOSM() {
		if (jToogleButtonOSM==null) {
			jToogleButtonOSM = new JToggleButton();
			jToogleButtonOSM.setIcon(BundleHelper.getImageIcon("MapsOSM.png"));
			jToogleButtonOSM.setToolTipText("Use Open Street Map");
			jToogleButtonOSM.addActionListener(this);
		}
		return jToogleButtonOSM;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (this.getJToggleButtonOSM().isSelected()==true) {
			// --- Selected OSM button --------------------
			this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_MapRendering_ON));
		} else {
			// --- Unselected OSM button ------------------			
			this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_MapRendering_OFF));
		}
		
	}

}
