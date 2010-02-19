package monitormas.ontology;

import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimeDetector
* @author ontology bean generator
* @version 2010/02/18, 17:09:44
*/
public class TimeDetector implements Concept, Serializable {
   // bean stuff
   protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

   public void addPropertyChangeListener(PropertyChangeListener pcl) {
     pcs.addPropertyChangeListener(pcl);
   }

   public void removePropertyChangeListener(PropertyChangeListener pcl) {
     pcs.removePropertyChangeListener(pcl);
   }


   /**
* Protege name: Time_Transmit_End
   */
   private float time_Transmit_End;
   public void setTime_Transmit_End(float value) { 
     pcs.firePropertyChange("time_Transmit_End", ""+this.time_Transmit_End, ""+value);
    this.time_Transmit_End=value;
   }
   public float getTime_Transmit_End() {
     return this.time_Transmit_End;
   }

   /**
* Protege name: Time_Transmit_Start
   */
   private float time_Transmit_Start;
   public void setTime_Transmit_Start(float value) { 
     pcs.firePropertyChange("time_Transmit_Start", ""+this.time_Transmit_Start, ""+value);
    this.time_Transmit_Start=value;
   }
   public float getTime_Transmit_Start() {
     return this.time_Transmit_Start;
   }

   /**
* Protege name: Time_Receive_End
   */
   private float time_Receive_End;
   public void setTime_Receive_End(float value) { 
     pcs.firePropertyChange("time_Receive_End", ""+this.time_Receive_End, ""+value);
    this.time_Receive_End=value;
   }
   public float getTime_Receive_End() {
     return this.time_Receive_End;
   }

   /**
* Protege name: Time_Receive_Start
   */
   private float time_Receive_Start;
   public void setTime_Receive_Start(float value) { 
     pcs.firePropertyChange("time_Receive_Start", ""+this.time_Receive_Start, ""+value);
    this.time_Receive_Start=value;
   }
   public float getTime_Receive_Start() {
     return this.time_Receive_Start;
   }

}
