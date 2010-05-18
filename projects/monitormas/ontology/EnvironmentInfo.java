package monitormas.ontology;

import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EnvironmentInfo
* @author ontology bean generator
* @version 2010/05/16, 22:23:22
*/
public class EnvironmentInfo implements AgentAction, Serializable {
   // bean stuff
   protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

   public void addPropertyChangeListener(PropertyChangeListener pcl) {
     pcs.addPropertyChangeListener(pcl);
   }

   public void removePropertyChangeListener(PropertyChangeListener pcl) {
     pcs.removePropertyChangeListener(pcl);
   }


}
