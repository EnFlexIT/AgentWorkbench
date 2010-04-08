package mas.environment;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class GeomTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Line2D.Float line = new Line2D.Float(new Point(0,0), new Point(100,20));
		Rectangle2D.Float rect = new Rectangle2D.Float(40, 40, 20, 20);
		
		System.out.println(line.intersects(rect));
	}

}
