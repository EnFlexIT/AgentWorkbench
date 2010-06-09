package contmas.de.unidue.stud.sehawagn.contmas.measurement;

import contmas.ontology.TransportOrderChain;
import jade.core.AID;

public interface StatusUpdater {
	public void sendStatusUpdate(String status, TransportOrderChain subject);
	
	public AID getStatusUpdateTopic();

}
