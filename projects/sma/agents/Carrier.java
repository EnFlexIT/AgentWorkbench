package sma.agents;

import java.util.Vector;

import agentgui.physical2Denvironment.ontology.Position;
import jade.core.Agent;

public class Carrier extends Agent {
	
	private Vector<String> containerNames;
	
	private Vector<Position> storagePositions;
	
	private Position wp1;
	
	private Position wp2;
	
	public void setup(){
		String name = this.getLocalName();
		containerNames = new Vector<String>();
		if(name.equals("carrier1")){
			containerNames.add("container1");
			containerNames.add("container2");
			containerNames.add("container5");
			containerNames.add("container6");
		}else if(name.equals("carrier2")){
			containerNames.add("container8");
			containerNames.add("container7");
			containerNames.add("container4");
			containerNames.add("container3");
		}
		
		Position pos1 = new Position();
		Position pos2 = new Position();
		Position pos3 = new Position();
		Position pos4 = new Position();
		
		storagePositions = new Vector<Position>();
		if(name.equals("carrier1")){
			pos1.setXPos(40);
			pos1.setYPos(12);
			
			pos2.setXPos(43);
			pos2.setYPos(12);
			
			pos3.setXPos(46);
			pos3.setYPos(12);
			
			pos4.setXPos(49);
			pos4.setYPos(12);
		}else if(name.equals("carrier2")){
			pos1.setXPos(40);
			pos1.setYPos(22);
			
			pos2.setXPos(43);
			pos2.setYPos(22);
			
			pos3.setXPos(46);
			pos3.setYPos(22);
			
			pos4.setXPos(49);
			pos4.setYPos(22);
		}
		
		storagePositions.add(pos1);
		storagePositions.add(pos2);
		storagePositions.add(pos3);
		storagePositions.add(pos4);
		
		wp1.setXPos(30);
		wp1.setYPos(17);
		
		wp2.setXPos(39);
		wp2.setYPos(17);
	}

}
