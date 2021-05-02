package de.enflexit.awb.samples.example03;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: FootballTeam
* @author ontology bean generator
* @version 2013/12/4, 11:00:31
*/
public class FootballTeam implements Concept {

   /**
* Protege name: player
   */
   private List player = new ArrayList();
   public void addPlayer(Person elem) { 
     List oldList = this.player;
     player.add(elem);
   }
   public boolean removePlayer(Person elem) {
     List oldList = this.player;
     boolean result = player.remove(elem);
     return result;
   }
   public void clearAllPlayer() {
     List oldList = this.player;
     player.clear();
   }
   public Iterator getAllPlayer() {return player.iterator(); }
   public List getPlayer() {return player; }
   public void setPlayer(List l) {player = l; }

}
