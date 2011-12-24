package agentgui.envModel.p2Dsvg.imageProcessing;

import java.io.Serializable;

import agentgui.envModel.p2Dsvg.ontology.Position;

public class AgentWithNameDirection implements Serializable {
	String name;
	int directionToCollision;
	int movingDirection;
	Position pos;
	public AgentWithNameDirection(String name, int directionToCollision, int movingDirection, Position pos) {
		super();
		this.name = name;
		this.directionToCollision = directionToCollision;
		this.pos=pos;
		this.movingDirection=movingDirection;

	}
	
	public String getName() {
		return name;
	}


	public int getDirectionToCollision() {
		return directionToCollision;
	}

	public Position getPos() {
		return pos;
	}

	public int getMovingDirection() {
		return movingDirection;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	
	
}
