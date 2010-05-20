/**
 * @author Hanno - Felix Wagner, 20.05.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.main;

import contmas.agents.ContainerAgent;
import contmas.ontology.Phy_Position;
import sma.ontology.DisplayOntology;
import sma.ontology.Position;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class AgentGUIHelper{
	/**
	 * The language codec
	 */
	private static Codec codec=null;
	/**
	 * The ontology
	 */
	private static Ontology ontology=null;
	private static AID posTopic=null;
	private static final String POSITION_TOPIC_NAME="position";

	public static Ontology getDisplayOntology(){
		if(ontology == null){
			ontology=DisplayOntology.getInstance();
		}
		return ontology;
	}

	public static Codec getDisplayCodec(){
		if(codec == null){
			codec=new SLCodec();
		}
		return codec;
	}

	public static AID getDisplayTopic(Agent a){
		if(posTopic == null){
			try{
				TopicManagementHelper tmh=(TopicManagementHelper) a.getHelper(TopicManagementHelper.SERVICE_NAME);
				posTopic=tmh.createTopic(POSITION_TOPIC_NAME);
				tmh.register(posTopic);
			}catch(ServiceException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return posTopic;
	}
	
	public static Position convertPosition(Phy_Position originalPos){
		Position convertedPos=new Position();
		convertedPos.setX(originalPos.getPhy_x());
		convertedPos.setY(originalPos.getPhy_y());

		return convertedPos;
	}

	public static Phy_Position convertPosition(Position originalPos){
		Phy_Position convertedPos=new Phy_Position();
		convertedPos.setPhy_x(originalPos.getX());
		convertedPos.setPhy_y(originalPos.getY());

		return convertedPos;
	}
	
	public static void enableForCommunication(Agent toBeEnabled){
		toBeEnabled.getContentManager().registerLanguage(getDisplayCodec());
		toBeEnabled.getContentManager().registerOntology(getDisplayOntology());
	}
}
