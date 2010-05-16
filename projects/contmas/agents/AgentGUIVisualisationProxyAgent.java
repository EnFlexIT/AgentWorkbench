package contmas.agents;

import java.util.Vector;

import sma.ontology.Position;
import sma.ontology.Speed;
import mas.display.DisplayableAgent;
import mas.movement.MoveToPointBehaviour;
import mas.movement.MovingAgent;
import contmas.behaviours.listenForLogMessage;
import contmas.behaviours.listenForPositionUpdate;
import contmas.behaviours.subscribeToDF;
import contmas.interfaces.DFSubscriber;
import contmas.interfaces.Logger;
import contmas.interfaces.PositionPlotter;
import contmas.ontology.Phy_Position;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

public class AgentGUIVisualisationProxyAgent extends MovingAgent implements Logger,DFSubscriber,PositionPlotter{

	private static final long serialVersionUID= -2081699287513185474L;

	List allActors=new ArrayList();

	public AgentGUIVisualisationProxyAgent(){
		super();
	}

	@Override
	public void setup(){
		super.setup();
		
		if(this.self!=null){
			this.self.setCurrentSpeed(new Speed());
			this.self.getCurrentSpeed().setSpeed(1.0f);
		}
		
		ContainerAgent.enableForCommunication(this);
		addBehaviour(new listenForLogMessage(this));
		addBehaviour(new subscribeToDF(this,"container-handling"));
		addBehaviour(new listenForPositionUpdate(this));
		
		this.doWait(1000);
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.Logger#processLogMsg(java.lang.String)
	 */
	@Override
	public void processLogMsg(String logMsg){
//		echo("[VisualisationAgent says] "+logMsg);
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.DFSubscriber#processSubscriptionUpdate(jade.util.leap.List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents,Boolean remove){
		if( !remove){
			ContainerAgent.addToList(allActors,updatedAgents);
		}else{
			ContainerAgent.removeFromList(allActors,updatedAgents);
		}
		if( !updatedAgents.isEmpty()){
			String msg="";
			msg+="[VisualisationAgent says] Agents ";
			if(remove){
				msg+="disappeared: ";
			}else{
				msg+="emerged: ";
			}
			Iterator iter=updatedAgents.iterator();
			while(iter.hasNext()){
				AID actor=(AID) iter.next();
				msg+=actor.getLocalName();
			}
			msg+="\n";
//			echo(msg);
		}
	}

	public void processPositionUpdate(Phy_Position in,AID from){
		echo("processing PositionUpdate from " + from.getLocalName() + ", new position is: " + StraddleCarrierAgent.positionToString(in));
		Position outPos=new Position();
		outPos.setX(in.getPhy_x());
		outPos.setY(in.getPhy_y());
		Speed speed=new Speed();
		speed.setSpeed(1.0F); //(10.0F seems to be slower than 1.0F)
//		speed=this.getCurrentSpeed();
		Position destPos=outPos;
		Vector<Position> waypoints=new Vector<Position>();
		waypoints.add(destPos);
		Behaviour b;

		if(self != null){
//			b=new MoveToPointBehaviour(from.getLocalName()+"Shadow",this,destPos,speed);

			b=new MoveToPointBehaviour(from.getLocalName()+"Shadow",this,this.getPosition(),destPos,speed);
//		b=new MoveToPointBehaviour(this,waypoints,speed);
			addBehaviour(b);
		}
	}

	public void echo(String msg){
		System.out.println("["+this.getLocalName()+"] "+ msg);
	}

}
