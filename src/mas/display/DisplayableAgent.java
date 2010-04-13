package mas.display;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.core.AID;
import sma.ontology.Point;
import sma.ontology.Position;
import sma.ontology.Speed;
/**
 * This interface has to be implemented by all agents using the display functionality. A default implementation is provided in mas.movement.MovingAgent
 * @author Nils
 *
 */
public interface DisplayableAgent {
	public Position getPosition();
	public void setPosition(Position position);
	public Speed getCurrentSpeed();
	public Speed getMaxSpeed();
	public Codec getDisplayCodec();
	public Ontology getDisplayOntology();
	public AID getUpdateReceiver();
	public void setMoving(boolean moving);
	public boolean isMoving();
}
