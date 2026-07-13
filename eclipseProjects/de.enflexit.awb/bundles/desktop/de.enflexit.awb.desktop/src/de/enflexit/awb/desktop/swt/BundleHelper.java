package de.enflexit.awb.desktop.swt;

import org.eclipse.swt.graphics.Image;

import de.enflexit.awb.core.config.GlobalInfo;


public class BundleHelper {
	
	/**
	 * Returns one of the internal images as an´SWT image instance.
	 *
	 * @param imageFileName the image file name
	 * @return the internal SWT image
	 */
	public static Image getInternalSWTImage(String imageFileName) {
		return SWTResourceManager.getImage(GlobalInfo.class, GlobalInfo.getPathImageIntern() + imageFileName);
	}
}
