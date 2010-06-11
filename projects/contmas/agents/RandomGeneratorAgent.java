/**
 * @author Hanno - Felix Wagner Copyright 2010 Hanno - Felix Wagner This file is
 *         part of ContMAS. ContMAS is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation, either version
 *         3 of the License, or (at your option) any later version. ContMAS is
 *         distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *         License for more details. You should have received a copy of the GNU
 *         Lesser General Public License along with ContMAS. If not, see
 *         <http://www.gnu.org/licenses/>.
 */

package contmas.agents;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.util.Random;

import contmas.main.MatchAgentAction;
import contmas.ontology.*;

public class RandomGeneratorAgent extends ContainerAgent{
	private Random RandomGenerator=new Random();

	public class createRandomBayMap extends AchieveREResponder{

		private static final long serialVersionUID=1611868636574647177L;

		public createRandomBayMap(Agent a,MessageTemplate mt){
			super(a,mt);
		}

		@Override
		protected ACLMessage handleRequest(ACLMessage request){
			ACLMessage reply=request.createReply();
			Concept content;
			content=((ContainerAgent) this.myAgent).extractAction(request);
			RequestRandomBayMap input=(RequestRandomBayMap) content;
			reply.setPerformative(ACLMessage.INFORM);
			Integer width, length, height;
			width=RandomGeneratorAgent.this.RandomGenerator.nextInt(input.getX_dimension()) + 1;
			length=RandomGeneratorAgent.this.RandomGenerator.nextInt(input.getY_dimension()) + 1;
			height=RandomGeneratorAgent.this.RandomGenerator.nextInt(input.getZ_dimension()) + 1;
			BayMap LoadBay=new BayMap();
			LoadBay.setX_dimension(width);
			LoadBay.setY_dimension(length);
			LoadBay.setZ_dimension(height);
			ProvideBayMap act=new ProvideBayMap();
			act.setProvides(LoadBay);
			((ContainerAgent) this.myAgent).fillMessage(reply,act);
			return reply;
		}
	}

	public class populateBayMap extends AchieveREResponder{

		/**
		 * 
		 */
		private static final long serialVersionUID=7430057892062817948L;

		public populateBayMap(Agent a,MessageTemplate mt){
			super(a,mt);
		}

		@Override
		protected ACLMessage handleRequest(ACLMessage request){
			ACLMessage reply=request.createReply();
			AgentAction content=((ContainerAgent) this.myAgent).extractAction(request);

			BayMap loadBay=((RequestPopulatedBayMap) content).getPopulate_on();

			reply.setPerformative(ACLMessage.INFORM);

			Integer width, length, height;
			String containerName;
			Container c;
			BlockAddress ba;
			List population=new ArrayList();
			TOCHasState tocState;
			Administered state;

			width=loadBay.getX_dimension();
			length=loadBay.getY_dimension();
			height=loadBay.getZ_dimension();
			for(int z=0;z < height;z++){
				for(int y=0;y < length;y++){
					for(int x=0;x < width;x++){
//						if(RandomGenerator.nextInt(2)==1 && (z==0 || 1==2)){ //TODO Abfrage, ob unterer Container schon vorhanden (keine Container "in die Luft" stellen)
//						if(RandomGenerator.nextInt(2)==1){ 
						if(true){
							c=new Container();
							containerName="ABC " + RandomGeneratorAgent.this.RandomGenerator.nextInt(65000);
							c.setBic_code(containerName);
							ba=new BlockAddress();
							ba.setLocates(c);
							ba.setX_dimension(x);
							ba.setY_dimension(y);
							ba.setZ_dimension(z);

							tocState=new TOCHasState();
							state=new Administered();
							state.setAt_address(ba);
							tocState.setState(state);
							tocState.setSubjected_toc(new TransportOrderChain());
							tocState.getSubjected_toc().setTransports(c);

							population.add(tocState);
						}
					}
				}
			}

			ProvidePopulatedBayMap act=new ProvidePopulatedBayMap();
			act.setProvides(loadBay);
			act.setProvides_population(population);

			((ContainerAgent) this.myAgent).fillMessage(reply,act);

			return reply;
		}
	}

	public class createRandomLoadSequence extends AchieveREResponder{

		private static final long serialVersionUID=1611868636574647177L;

		public createRandomLoadSequence(Agent a,MessageTemplate mt){
			super(a,mt);
		}

		private final Integer CONT_AMOUNT_PER_LOADLIST=4;

		@Override
		protected ACLMessage handleRequest(ACLMessage request){
			ACLMessage reply=request.createReply();
			Concept content;
			content=((ContainerAgent) this.myAgent).extractAction(request);
			RequestRandomLoadSequence input=(RequestRandomLoadSequence) content;

			Iterator allCont=input.getAllProvides_population();
			ProvideRandomLoadSequence act=new ProvideRandomLoadSequence();
			LoadList curLoadList=null;
			LoadList lastLoadList=null;

			Integer contNr=0;

			while(allCont.hasNext()){
				TOCHasState curTOCState = (TOCHasState) allCont.next();
				TransportOrderChain curTOC=curTOCState.getSubjected_toc();
				Holding curState=(Holding) curTOCState.getState();
				
//				TransportOrderChain curTOC=new TransportOrderChain();
//				BlockAddress curCont=curState.getAt_address();
//				curTOC.setTransports(curCont.getLocates());
				

				if((contNr % CONT_AMOUNT_PER_LOADLIST) == 0){
					curLoadList=new LoadList();
				}
				curLoadList.addConsists_of(curTOC);
				
				if((contNr % CONT_AMOUNT_PER_LOADLIST) == 0){

					if(lastLoadList == null){ //this is the first run
						act.setNext_step(curLoadList);
					}else{ //all later runs
						lastLoadList.setNext_step(curLoadList);
					}
				}
				lastLoadList=curLoadList;
				contNr++;
			}

			reply.setPerformative(ACLMessage.INFORM);
			((ContainerAgent) this.myAgent).fillMessage(reply,act);
			return reply;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID=1856819498877850473L;

	public RandomGeneratorAgent(){
		super("random-generation");
	}

	@Override
	protected void setup(){
		super.setup();

		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(this,new RequestRandomBayMap())));
		this.addBehaviour(new createRandomBayMap(this,mt));

		mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(this,new RequestPopulatedBayMap())));
		this.addBehaviour(new populateBayMap(this,mt));

		mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(this,new RequestRandomLoadSequence())));
		this.addBehaviour(new createRandomLoadSequence(this,mt));

	}
}