package de.enflexit.awb.samples.example03;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PingPongMessage
* @author ontology bean generator
* @version 2013/12/4, 11:00:31
*/
public class PingPongMessage implements AgentAction {

   /**
* Protege name: requiresAction
   */
   private boolean requiresAction;
   public void setRequiresAction(boolean value) { 
    this.requiresAction=value;
   }
   public boolean getRequiresAction() {
     return this.requiresAction;
   }

   /**
* Protege name: pingPongCounter
   */
   private int pingPongCounter;
   public void setPingPongCounter(int value) { 
    this.pingPongCounter=value;
   }
   public int getPingPongCounter() {
     return this.pingPongCounter;
   }

   /**
* Protege name: msgText
   */
   private String msgText;
   public void setMsgText(String value) { 
    this.msgText=value;
   }
   public String getMsgText() {
     return this.msgText;
   }

}
