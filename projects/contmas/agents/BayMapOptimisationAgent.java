package contmas.agents;

import jade.util.leap.List;

import java.util.Random;

import contmas.behaviours.listenForBlockAddressRequests;
import contmas.main.BlockedBecausePlanningException;
import contmas.ontology.BayMap;
import contmas.ontology.BlockAddress;
import contmas.ontology.Holding;
import contmas.ontology.TransportOrderChain;
import contmas.ontology.TransportOrderChainState;

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

	public BlockAddress getEmptyBlockAddress(BayMap loadBay,List allContainers){
		BlockAddress emptyBA=null;
		
		if(allContainers.size()==getBayMapCapacity(loadBay)){ //bay map is already full
			return emptyBA;
		}

		TransportOrderChainState[][][] bayMapArray=ContainerHolderAgent.convertToBayMapArray(loadBay, allContainers);
		
		Integer x, y, z=0;
		while(emptyBA==null){
			x=RandomGenerator.nextInt(loadBay.getX_dimension());
			y=RandomGenerator.nextInt(loadBay.getY_dimension());
			BlockAddress upmostContainer;
			try {
				upmostContainer = this.getUpmostOccupiedBlockAddress(x,y,bayMapArray);
			} catch (BlockedBecausePlanningException e) {
				echoStatus("Stack at current coordinate blocked because of pending plans, skipping.",ContainerAgent.LOGGING_INFORM); //leads to containers hanging in the air
				continue;
			}
	
			if(upmostContainer == null || (upmostContainer.getZ_dimension() < loadBay.getZ_dimension()-1)){ //there is room on top
				if(upmostContainer != null){
					z=upmostContainer.getZ_dimension() + 1;
				}
				
				emptyBA=new BlockAddress();
				emptyBA.setX_dimension(x);
				emptyBA.setY_dimension(y);
				emptyBA.setZ_dimension(z);

			}
		}
		return emptyBA;
	}

	public BlockAddress getEmptyBlockAddress(BayMap loadBay,TransportOrderChain subject,List allContainers){
		BlockAddress solution=getEmptyBlockAddress(loadBay,allContainers);
		echoStatus("When asked for an EmptyBlockAddress, I say: Random!",ContainerAgent.LOGGING_INFORM);
		return solution;
	}

	public Integer getBayMapCapacity(BayMap loadBay){
		Integer baySize=loadBay.getX_dimension() * loadBay.getY_dimension() * loadBay.getZ_dimension();
		return baySize;
	}

	public BlockAddress getUpmostOccupiedBlockAddress(Integer x,Integer y,TransportOrderChainState[][][] bayMapArray) throws BlockedBecausePlanningException{
		BlockAddress upmostOcc=null;
		TransportOrderChainState[] stack=bayMapArray[x][y];
		
		for (Integer i = 0; i < stack.length; i++) {
			if(stack[i]!=null){ //there is a container at this position
				if(stack[i] instanceof Holding){
					upmostOcc=stack[i].getAt_address(); //memorize it
				} else {
					throw new BlockedBecausePlanningException();
				}
			} else { //no container at this position
				if(upmostOcc!=null){ //already found some container, that one is the upmost
					break;
				}
			}
		}
		return upmostOcc;
	}
}
