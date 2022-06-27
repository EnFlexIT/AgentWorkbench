package de.enflexit.awb.samples.example03;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Person
* @author ontology bean generator
* @version 2013/12/4, 11:00:31
*/
@SuppressWarnings("unused")
public class Person implements Concept {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
* Protege name: age
   */
   private int age;
   public void setAge(int value) { 
    this.age=value;
   }
   public int getAge() {
     return this.age;
   }

   /**
* Protege name: prename
   */
   private String prename;
   public void setPrename(String value) { 
    this.prename=value;
   }
   public String getPrename() {
     return this.prename;
   }

   /**
* Protege name: surname
   */
   private String surname;
   public void setSurname(String value) { 
    this.surname=value;
   }
   public String getSurname() {
     return this.surname;
   }

}
