package contmas.agents;

import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;

import java.util.HashMap;
import java.util.Vector;

import mas.movement.MoveToPointBehaviour;
import mas.movement.MovingAgent;
import sma.ontology.AgentObject;
import sma.ontology.Position;
import sma.ontology.Speed;
import contmas.behaviours.listenForPositionUpdate;
import contmas.interfaces.PositionPlotter;
import contmas.ontology.Phy_Position;

public class AgentGUIVisualisationProxyAgent extends MovingAgent implements PositionPlotter{

	private static final long serialVersionUID= -2081699287513185474L;

	private static final String SHADOW_SUFFIX="Shadow";

	private static final String POSITION_TOPIC_NAME="position";

	private static final Float SPEED_VALUE=100.0F;

	private HashMap<String, Position> allActorsPos=new HashMap<String, Position>();
	private TickerBehaviour movingBehaviour;
	private Position turningPoint=null;

	public AgentGUIVisualisationProxyAgent(){
		super();
	}

	@Override
	public void setup(){
		super.setup();

		this.self=new AgentObject();
		this.self.setPosition(getDefaultPosition());

		ContainerAgent.enableForCommunication(this);
		addBehaviour(new listenForPositionUpdate(this));

//		this.doWait(1000);
	}

	private static Position getDefaultPosition(){
		Position defPos=new Position();
		defPos.setX(0.0F);
		defPos.setY(0.0F);
		return defPos;
	}

	private Position getCurPosition(String reporter){
		Position curPos=allActorsPos.get(reporter);
		return curPos;
	}

	private void setCurrentPosition(String reporter,Position curPos){
		allActorsPos.put(reporter,curPos);
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.PositionPlotter#processPositionUpdate(jade.util.leap.List, jade.core.AID)
	 */
	@Override
	public void processPositionUpdate(Phy_Position in,AID from,Phy_Position turningPoint){
		Position turPoPos=new Position();
		turPoPos.setX(turningPoint.getPhy_x());
		turPoPos.setY(turningPoint.getPhy_y());
		this.turningPoint=turPoPos;
		processPositionUpdate(in,from);
	}

	public void processPositionUpdate(Phy_Position in,AID from){
		String reporter=from.getLocalName();

		echo("processing PositionUpdate from " + reporter + ", new position is: " + StraddleCarrierAgent.positionToString(in));

		Position destPos=new Position();
		destPos.setX(in.getPhy_x());
		destPos.setY(in.getPhy_y());

		if(movingBehaviour == null){
			movingBehaviour=addMove(reporter,destPos);
		}else{
			if(movingBehaviour.done()){
				movingBehaviour=addMove(reporter,destPos);
			}else{
				echo("doing nothing");
			}
		}
	}

	private MoveToPointBehaviour addMove(String reporter,Position destPos){
		Position curPos=getCurPosition(reporter);
		if(curPos == null){ //if this is the first update for the specific agent, the shadow has to be initially placed
			curPos=destPos;
		}

		return addMove(reporter,curPos,destPos);
	}

	private MoveToPointBehaviour addMove(String reporter,Position curPos,Position destPos){
		setCurrentPosition(reporter,destPos);
		Speed speed=new Speed();
		speed.setSpeed(SPEED_VALUE);
		MoveToPointBehaviour movingBehaviour;

		if(turningPoint==null){
			echo("turningPoint==null, curPosX="+curPos.getX()+" destPosX="+destPos.getX());
			movingBehaviour=new MoveToPointBehaviour(reporter + SHADOW_SUFFIX,this,curPos,destPos,speed);
		} else {
			echo("curPosX="+curPos.getX()+" destPosX="+destPos.getX()+" turningPointX="+turningPoint.getX());

			Vector<Position> wp=new Vector<Position>();
			wp.add(destPos);
			wp.add(turningPoint);

			movingBehaviour=new MoveToPointBehaviour(reporter + SHADOW_SUFFIX,this,curPos,wp,speed);

		}
		addBehaviour(movingBehaviour);
		return movingBehaviour;
	}

	@Override
	public AID getUpdateReceiver(){
		if(posTopic == null){
			try{
				TopicManagementHelper tmh=(TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
				this.posTopic=tmh.createTopic(POSITION_TOPIC_NAME);
				tmh.register(posTopic);
			}catch(ServiceException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return posTopic;
	}

	public void echo(String msg){
		System.out.println("[" + this.getLocalName() + "] " + msg);
	}

}
