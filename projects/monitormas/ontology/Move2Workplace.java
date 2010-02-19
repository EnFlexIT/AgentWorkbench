package monitormas.ontology;

import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Move2Workplace
* @author ontology bean generator
* @version 2010/02/18, 17:09:44
*/
public class Move2Workplace implements AgentAction, Serializable {
   // bean stuff
   protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

   public void addPropertyChangeListener(PropertyChangeListener pcl) {
     pcs.addPropertyChangeListener(pcl);
   }

   public void removePropertyChangeListener(PropertyChangeListener pcl) {
     pcs.removePropertyChangeListener(pcl);
   }


}
