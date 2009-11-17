package mas.display;

import java.awt.Dimension;
import java.awt.Point;

public interface DisplayableAgent {
	public String getAgentType();
	public Point getPosition();
	public Dimension getSize();
}
