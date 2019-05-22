package org.awb.env.maps;

import java.util.ArrayList;
import java.util.List;

import org.awb.env.networkModel.controller.ui.BasicGraphGui.ToolBarSurrounding;
import org.awb.env.networkModel.controller.ui.BasicGraphGui.ToolBarType;
import org.awb.env.networkModel.controller.ui.toolbar.CustomToolbarComponentDescription;
import org.awb.env.networkModel.controller.ui.toolbar.CustomToolbarComponentExtension;

public class NetworkModelToolbarExtension implements CustomToolbarComponentExtension {

	public NetworkModelToolbarExtension() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.toolbar.CustomToolbarComponentExtension#getCustomToolbarComponentDescriptionList()
	 */
	@Override
	public List<CustomToolbarComponentDescription> getCustomToolbarComponentDescriptionList() {
		
		List<CustomToolbarComponentDescription> ctbElements = new ArrayList<CustomToolbarComponentDescription>();
		
		ctbElements.add(new CustomToolbarComponentDescription(ToolBarType.LayoutControl, ToolBarSurrounding.Both, JToggleButtonOpenStreetMap.class, 3, true));
		
		return ctbElements;
	}

}
