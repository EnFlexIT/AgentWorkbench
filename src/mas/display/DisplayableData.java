package mas.display;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Storing the additional information needed to make agents displayable 
 * @author Nils
 *
 */
public class DisplayableData {
	/**
	 * Agent's position
	 */
	private int x, y;
	
	/**
	 * Agent's size
	 */
	private int agentWidth, agentHeight;
	
	/**
	 * Area Size
	 */
	private int playgroundWidth, playgroundHeight;
	
	String agentType = "";
	DisplayableAgent myAgent;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setPos(int x, int y){
		this.x=x;
		this.y=y;
	}
	public int getAgentWidth() {
		return agentWidth;
	}
	public void setAgentWidth(int agentWidth) {
		this.agentWidth = agentWidth;
	}
	public int getAgentHeight() {
		return agentHeight;
	}
	public void setAgentHeight(int agentHeight) {
		this.agentHeight = agentHeight;
	}
	public void setAgentSize(int width, int height){
		this.agentWidth = width;
		this.agentHeight = height;
	}
	public int getPlaygroundWidth() {
		return playgroundWidth;
	}
	public void setPlaygroundWidth(int playgroundWidth) {
		this.playgroundWidth = playgroundWidth;
	}
	public int getPlaygroundHeight() {
		return playgroundHeight;
	}
	public void setPlaygroundHeight(int playgroundHeight) {
		this.playgroundHeight = playgroundHeight;
	}
	public void setPlaygroundSize(int width, int height){
		this.playgroundWidth = width;
		this.playgroundHeight = height;
	}
	
	
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public DisplayableAgent getMyAgent() {
		return myAgent;
	}
	
	
	
}
