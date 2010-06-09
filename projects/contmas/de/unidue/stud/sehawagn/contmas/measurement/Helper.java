package contmas.de.unidue.stud.sehawagn.contmas.measurement;

import jade.core.AID;
import jade.core.Agent;
import contmas.agents.ContainerAgent;

public class Helper {
	private static final String MEASURE_TOPIC_NAME="container-harbour-measuring";
	public static final String STATUS_PICKED_UP = "Picked up ";
	public static final String STATUS_DROPPED = "Dropped ";
	
	private static AID measureTopic;

	public static AID getMeasureTopic(Agent a){
		if(measureTopic == null){
			measureTopic=ContainerAgent.createTopic(a, MEASURE_TOPIC_NAME);
		}
		return measureTopic;
	}
}
