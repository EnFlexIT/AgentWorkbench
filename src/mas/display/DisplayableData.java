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
	 * The agent's position  
	 */
	Point position = new Point();
	/**
	 * The agent's size
	 */
	Dimension agentSize = new Dimension();
	/**
	 * Size of the area the agent is moving in
	 */
	Dimension playgroundSize = new Dimension();
	/**
	 * Type definition, used to map agents to SVG representations
	 */
	String agentType = "";
	
	DisplayableAgent myAgent;
	
	public DisplayableData(DisplayableAgent agent){
		myAgent = agent;		
	}
	
	public Point getPosition() {
		return position;
	}	
	public void setPosition(Point position) {
		this.position = position;
	}
	public Dimension getAgentSize() {
		return agentSize;
	}
	public void setAgentSize(Dimension agentSize) {
		this.agentSize = agentSize;
	}
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public Dimension getPlaygroundSize() {
		return playgroundSize;
	}
	public void setPlaygroundSize(Dimension playgroundSize){
		this.playgroundSize = playgroundSize;
	}
}
