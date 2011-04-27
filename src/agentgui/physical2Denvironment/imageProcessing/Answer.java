package agentgui.physical2Denvironment.imageProcessing;

import java.util.ArrayList;

import agentgui.physical2Denvironment.ontology.Position;

public class Answer implements java.io.Serializable
{	
	
	private Object speed;
	private ArrayList<Position> wayToDestination;
	private Position nextPosition;
	private int index;
	
	
	
	
	public Position getNextPosition() {
		return nextPosition;
	}
	public void setNextPosition(Position nextPosition) {
		this.nextPosition = nextPosition;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Object getSpeed() {
		return speed;
	}
	public void setSpeed(Object speed) {
		this.speed = speed;
	}
	public ArrayList<Position> getWayToDestination() {
		return wayToDestination;
	}
	public void setWayToDestination(ArrayList<Position> wayToDestination) {
		this.wayToDestination = wayToDestination;
	}
	
	
	
	

}
