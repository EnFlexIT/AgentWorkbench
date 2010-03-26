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

import java.util.Random;

import contmas.main.MatchAgentAction;
import contmas.ontology.*;

public class RandomGeneratorAgent extends ContainerAgent{
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
			if(content instanceof RequestRandomBayMap){
				//			    	echoStatus("content instanceof RequestRandomBayMap");

				RequestRandomBayMap input=(RequestRandomBayMap) content;
				reply.setPerformative(ACLMessage.INFORM);
				Integer width, length, height;
				Random RandomGenerator=new Random();
				width=RandomGenerator.nextInt(input.getX_dimension()) + 1;
				length=RandomGenerator.nextInt(input.getY_dimension()) + 1;
				height=RandomGenerator.nextInt(input.getZ_dimension()) + 1;
				BayMap LoadBay=new BayMap();
				LoadBay.setX_dimension(width);
				LoadBay.setY_dimension(length);
				LoadBay.setZ_dimension(height);
				ProvideBayMap act=new ProvideBayMap();
				act.setProvides(LoadBay);
				((ContainerAgent) this.myAgent).fillMessage(reply,act);
				return reply;
			}
			return null;
		} // end prepareResponse() 

		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
			return null;
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
			//	    	echoStatus("populateBayMap handleRequest start");
			ACLMessage reply=request.createReply();
			AgentAction content;
			content=((ContainerAgent) this.myAgent).extractAction(request);
			if(content instanceof RequestPopulatedBayMap){

				BayMap LoadBay=((RequestPopulatedBayMap) content).getPopulate_on();

				reply.setPerformative(ACLMessage.INFORM);

				Integer width, length, height;
				Random RandomGenerator=new Random();
				String containerName;
				Container c;
				BlockAddress ba;

				//old
				width=LoadBay.getX_dimension();
				length=LoadBay.getY_dimension();
				height=LoadBay.getZ_dimension();
				//System.out.println("width:"+width+"length:"+length+"height:"+height);
				for(int z=0;z < height;z++){
					for(int y=0;y < length;y++){
						for(int x=0;x < width;x++){
							//System.out.println("runde");
							//								if(RandomGenerator.nextInt(2)==1 && (z==0 || 1==2)){ //TODO Abfrage, ob unterer Container schon vorhanden (keine Container "in die Luft" stellen)
							//								if(RandomGenerator.nextInt(2)==1){ 
							if(true){

								//System.out.println("zufällig");

								c=new Container();
								containerName="ABC " + RandomGenerator.nextInt(65000);
								c.setBic_code(containerName);
								ba=new BlockAddress();
								//ba.setAddresses_within(LoadBay);
								ba.setLocates(c);
								ba.setX_dimension(x);
								ba.setY_dimension(y);
								ba.setZ_dimension(z);
								//c.setOccupies(ba);
								//c.

								LoadBay.addIs_filled_with(ba);

							}
						}
					}
				}
				//end old

				ProvidePopulatedBayMap act=new ProvidePopulatedBayMap();
				act.setProvides(LoadBay);

				((ContainerAgent) this.myAgent).fillMessage(reply,act);

			}else{
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
				reply.setContent("Fehler populated");
			}
			return reply;
		}

		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
			return response;

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
		//create filter for incoming messages

		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(this,new RequestRandomBayMap())));
		addBehaviour(new createRandomBayMap(this,mt));


		mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(this,new RequestPopulatedBayMap())));

		this.addBehaviour(new populateBayMap(this,mt));

	}

}
