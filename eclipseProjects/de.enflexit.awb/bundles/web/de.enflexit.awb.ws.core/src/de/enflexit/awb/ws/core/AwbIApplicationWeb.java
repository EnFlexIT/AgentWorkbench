package de.enflexit.awb.ws.core;

import de.enflexit.awb.core.AwbIApplicationCore;
import de.enflexit.awb.core.config.GlobalInfo.AWBProduct;

/**
 * The Class AwbIApplicationWeb.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbIApplicationWeb extends AwbIApplicationCore {

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.AwbIApplicationCore#getAwbProduct()
	 */
	@Override
	public AWBProduct getAwbProduct() {
		return AWBProduct.WEB;
	}
	
}
