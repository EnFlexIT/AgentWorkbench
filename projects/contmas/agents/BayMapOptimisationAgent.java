package contmas.agents;

import java.util.Random;

import contmas.behaviours.listenForBlockAddressRequests;
import contmas.behaviours.listenForLogMessage;
import contmas.behaviours.subscribeToDF;
import contmas.interfaces.DFSubscriber;
import contmas.interfaces.Logger;
import contmas.ontology.*;
import jade.core.AID;
import jade.core.Agent;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

public class BayMapOptimisationAgent extends ContainerAgent{

	private static final long serialVersionUID= -2081698387513185474L;
	private Random RandomGenerator=new Random();
	
	public BayMapOptimisationAgent(){
		super("optimizing-bay-maps");
	}

	@Override
	protected void setup(){
		super.setup();
		echoStatus("I setup, therefore I am",ContainerAgent.LOGGING_INFORM);
		addBehaviour(new listenForBlockAddressRequests(this));
	}
	
	public BlockAddress getEmptyBlockAddress(BayMap loadBay, List allContainers){
		Integer tries=0;
		Integer maxTries=getBayMapCapacity(loadBay);
		Integer x, y, z;
		while(tries < maxTries){
			x=RandomGenerator.nextInt(loadBay.getX_dimension());
			y=RandomGenerator.nextInt(loadBay.getY_dimension());
			BlockAddress upmostContainer=this.getUpmostOccupiedBlockAddress(x,y,loadBay,allContainers);
			z=0;
			if(upmostContainer != null){
				if(upmostContainer.getZ_dimension() < loadBay.getZ_dimension()){ //there is room on top
					z=upmostContainer.getZ_dimension() + 1;
				}else{
					tries++;
					continue;
				}
			}
			BlockAddress emptyBA=new BlockAddress();
			emptyBA.setX_dimension(x);
			emptyBA.setY_dimension(y);
			emptyBA.setZ_dimension(z);
			return emptyBA;
		}
		return null;
	}
	public BlockAddress getEmptyBlockAddress(BayMap loadBay, TransportOrderChain subject, List allContainers){
		BlockAddress solution=getEmptyBlockAddress(loadBay,allContainers);
		echoStatus("When asked for an EmptyBlockAddress, I say: Random!",ContainerAgent.LOGGING_INFORM);
		return solution;
	}
	
	
	public Integer getBayMapCapacity(BayMap loadBay){
		Integer baySize=loadBay.getX_dimension() * loadBay.getY_dimension() * loadBay.getZ_dimension();
		return baySize;
	}
	
	public BlockAddress getUpmostOccupiedBlockAddress(Integer x,Integer y, BayMap loadBay, List allContainers){
		BlockAddress upmostContainer=null;
		Iterator allConts=allContainers.iterator();
		while(allConts.hasNext()){ //alle geladenen Container überprüfen 
			Holding curState=(Holding) ((TOCHasState) allConts.next()).getState();
			BlockAddress curContainer=(BlockAddress) curState.getAt_address();
			if((curContainer.getX_dimension() == x) && (curContainer.getY_dimension() == y)){ //betrachteter Container steht im stapel auf momentaner koordinate
				if((upmostContainer == null) || (upmostContainer.getZ_dimension() < curContainer.getZ_dimension())){
					upmostContainer=curContainer;
				}
			}
		} //end while
		return upmostContainer;
	}
}
