package mas.display;

import java.awt.Dimension;
import java.awt.Point;

public interface DisplayableAgent {
	
	public String getAgentType();
	public int getX();
	public int getY();
	public void setPos(int x, int y);
	public int getWidth();
	public int getHeight();
	public int getPlaygroundWidth();
	public int getPlaygroundHeight();
}
