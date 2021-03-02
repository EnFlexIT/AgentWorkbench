package org.awb.env.maps;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class JxMapAffineTransform extends AffineTransform {

	private static final long serialVersionUID = 395752064146592446L;


	@Override
	public Point2D inverseTransform(Point2D ptSrc, Point2D ptDst) throws NoninvertibleTransformException {
		// TODO Auto-generated method stub
		
		Point2D pCalculated = super.transform(ptSrc, ptDst);
		if (pCalculated.equals(ptSrc)==false) {
			System.out.println("Invers View: " + ptSrc.toString() + " to " + pCalculated.toString());
		}
		
//		pCalculated = new Point2D.Double(-100, -100);
		
		return pCalculated;
	}
	
	
	@Override
	public Point2D transform(Point2D ptSrc, Point2D ptDst) {
		
		Point2D pCalculated = super.transform(ptSrc, ptDst);
		if (pCalculated.equals(ptSrc)==false) {
			System.out.println("Transform View: " + ptSrc.toString() + " to " + pCalculated.toString());
		}
		
		
		return pCalculated;
	}
	
	
}
