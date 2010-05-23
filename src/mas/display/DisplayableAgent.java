package mas.display;

import mas.display.ontology.Position;
import mas.display.ontology.Size;
import mas.display.ontology.Speed;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.core.AID;
/**
 * This interface has to be implemented by all agents using the display functionality. A default implementation is provided in mas.movement.MovingAgent
 * @author Nils
 *
 */
public interface DisplayableAgent {
	public Position getPosition();
	public Size getSize();
	public AID getAID();
	public void setPosition(Position position);
	public Speed getCurrentSpeed();
	public Speed getMaxSpeed();
	public Codec getDisplayCodec();
	public Ontology getDisplayOntology();
	public AID getUpdateReceiver();
	public void setMoving(boolean moving);
	public boolean isMoving();
}
