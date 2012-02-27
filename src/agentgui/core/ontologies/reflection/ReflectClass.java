/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.ontologies.reflection;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import agentgui.core.ontologies.OntologyClassTreeObject;

/**
 * This class is used for reflective access to classes of ontologies, which were build by Protégé.
 * It is used within the {@link OntologyClassTreeObject} in order to provide the detailed
 * information about the structure of a class.  
 * 
 * @see OntologyClassTreeObject
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ReflectClass extends Object {

	private Class<?> currClass = null;
	private Hashtable<String, String> currClassSlots = null;
	private Slot[] currSlotList;
		
	/**
	 * Constructor for this class.  
	 * @param clazz the class out of the ontology
	 * @param clazzSlots the list of slots defined in the vocabulary of an ontology
	 */
	public ReflectClass(Class<?> clazz, Hashtable<String, String> clazzSlots ){
		
		currClass = clazz;
		currClassSlots = clazzSlots;
		currSlotList = new Slot[currClassSlots.size()];
		int currSlotListCounter = -1;
		
		Vector<String> v = new Vector<String>(currClassSlots.keySet());
	    Collections.sort(v);
		Iterator<String> it = v.iterator();
	    while (it.hasNext()) {
	    	// --- Slot der Klasse ermitteln und untersuchen ----- 
	    	String key = it.next();
	    	String word = currClassSlots.get(key);
	    	
	    	Slot currSlot = new Slot(word);
	    	currSlotListCounter++;
	    	currSlotList[currSlotListCounter] = currSlot;
	    }
	}
	
	/**
	 * Returns the slot description, specified by its name.
	 *
	 * @param slotName the slot name
	 * @return The Slot, as specified by its name
	 */
	public Slot getSlot(String slotName) {
		Slot resSlot = null;
		for (int i = 0; i < currSlotList.length; i++) {
			if ( currSlotList[i].toString().toLowerCase().equalsIgnoreCase(slotName) ) {
				resSlot = currSlotList[i];
			}
		}
		return resSlot;
	}

	// ------------------------------------------------------------------------
	// --- Start Sub-Class ----------------------------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Sub-Class Slot: Represents a single local variable and the methods of the 
	 * current ontology class.
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	public class Slot extends Object{
		
		/** The slot name. */
		private String slotName;
		/** The method list, stored in a Hashtable. */
		public Hashtable<String, Method> MethodList = new Hashtable<String, Method>();
		/** The number of the methods found. */
		public int N_Methods;
		/** The Cardinality ("single" or "multiple"). */
		public String Cardinality = null;
		/** The type of the variable. */
		public String VarType = null;
		/** The Other facts, which are usually free, but displayed in Protégé. */
		public String OtherFacts = null;
		
		
		/**
		 * Initialise this Sub-Class.
		 * @param currSlot the curr slot
		 */
		public Slot(String currSlot) {
			slotName = currSlot;
			this.initialize();			
		}
		
		/**
		 * returns the name of the current slot.
		 * @return the string
		 */
		public String toString(){
			return slotName;
		}		

		/**
		 * Starts the evaluation of the current slot variable.
		 */
		private void initialize() {
			
			// --- Die betreffende Methoden herausfiltern -----------
			this.setMethods4Slot();
			
			// --- Beschreibungsparameter ermitteln -----------------
			N_Methods = MethodList.size();
			Method singleMethod;
			if ( N_Methods <2 ) {
				Cardinality = "empty";
				VarType = "";
				
			} else  if ( N_Methods == 2 ) {
				// --- "einfache" Variablen mit Getter und Setter ---
				Cardinality = "single";
				singleMethod = MethodList.get( "get" + slotName.toLowerCase() );				
				Class<?> returnType = singleMethod.getReturnType();
				VarType = getClassType( returnType );
				
			} else {
				// --- "komplexere" Variable / ArrayListen ----------
				Cardinality = "multiple";
				singleMethod = MethodList.get( "add" + slotName.toLowerCase() );
				Class<?>[] paraClass = singleMethod.getParameterTypes();
				VarType = "";
				for (int i = 0; i < paraClass.length; i++) {
					if ( VarType.equalsIgnoreCase("") ) {
						VarType = getClassType( paraClass[i] );
					} else {
						VarType = VarType + ", " + getClassType( paraClass[i] );
					}
				}
			}
		}	
		
		/**
		 * Returns a description of a Class-Reference.
		 *
		 * @param clazz the clazz
		 * @return the class type
		 */
		private String getClassType(Class<?> clazz) {
			
			String CurrClass = clazz.getSimpleName();
			if ( ! (CurrClass.equalsIgnoreCase("boolean") || CurrClass.equalsIgnoreCase("String") ||
					CurrClass.equalsIgnoreCase("int") || CurrClass.equalsIgnoreCase("float") ) ) 
			{
				CurrClass = "Instance of " + CurrClass;
			};
			return CurrClass;
		}
		
		/**
		 * Filter the methods, which belonging to the Filter-Term.
		 *
		 */
		private void setMethods4Slot() {
			// --- Mehoden heraussuchen, die den aktuelle Slot betreffen  ----- 
			String CurrSlotNa = slotName.toLowerCase();
			Method[] theMethods = currClass.getMethods();
			for (int i = 0; i < theMethods.length; i++) {
				String methodName = theMethods[i].getName().toLowerCase();
				if ( methodName.equalsIgnoreCase("wait") == false &&
					 methodName.equalsIgnoreCase("equals") == false &&
					 methodName.equalsIgnoreCase("tostring") == false &&
					 methodName.equalsIgnoreCase("hashcode") == false &&
					 methodName.equalsIgnoreCase("getclass") == false &&
					 methodName.equalsIgnoreCase("notify") == false &&
				  	 methodName.equalsIgnoreCase("notifyall") == false &&
				 	 methodName.endsWith( CurrSlotNa ) ) {
					// ---------------------------------------------------------
					// --- Wenn die aktuelle Methode schon mit 'get' oder    ---
					// --- 'set' anfängt, dann sollte der Name auch komplett ---
					// --- stimmen !!!										 ---
					// ---------------------------------------------------------
					// --- 95% Sicherheit, aber, aber ... 
					String GeSeTest = methodName.substring(0, 3);
					if ( GeSeTest.equals("get") || GeSeTest.equals("set")  ) {
						if ( methodName.equals(GeSeTest + CurrSlotNa) ) {
							MethodList.put(methodName, theMethods[i]);
						}
					} else {
						MethodList.put(methodName, theMethods[i]);
					}
					// ---------------------------------------------------------
				}
			} // --- end for		
		}// --- End Method
		
	}
	// ------------------------------------------------------------------------	
	// --- End Sub-Class ------------------------------------------------------
	// ------------------------------------------------------------------------

	
} // --- End Class
