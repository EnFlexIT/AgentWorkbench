package reflection;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class ReflectClass extends Object {

	private Class<?> CurrClass = null;
	private Hashtable<String, String> CurrClassSlots = null;
	private Slot[] CurrSlotList;
		
	public ReflectClass ( Class<?> Clazz, Hashtable<String, String> ClazzSlots ){
		
		CurrClass = Clazz;
		CurrClassSlots = ClazzSlots;
		CurrSlotList = new Slot[CurrClassSlots.size()];
		int CurrSlotListCounter = -1;
		
		Vector<String> v = new Vector<String>( CurrClassSlots.keySet() );
	    Collections.sort(v);
		Iterator<String> it = v.iterator();
	    while (it.hasNext()) {
	    	// --- Slot der Klasse ermitteln und untersuchen ----- 
	    	String Key = it.next();
	    	String Word = CurrClassSlots.get(Key);
	    	
	    	Slot CurrSlot = new Slot( Word );
	    	CurrSlotListCounter++;
	    	CurrSlotList[CurrSlotListCounter] = CurrSlot;
	    }
	}
	/**
	 * Returns the SlotDescription by the Slot-Object
	 * @param SlotName
	 * @return
	 */
	public Slot getSlot(String SlotName) {
		Slot ResSlot = null;
		for (int i = 0; i < CurrSlotList.length; i++) {
			if ( CurrSlotList[i].toString().toLowerCase().equalsIgnoreCase(SlotName) ) {
				ResSlot = CurrSlotList[i];
			}
		}
		return ResSlot;
	}

	// ------------------------------------------------------------------------
	// --- Start Sub-Class ----------------------------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Sub-Class Slot: Represents a single local variable of the 'CurrClass'
	 * @author CDerksen
	 */
	public class Slot extends Object{
		
		private String SlotName;
		public Hashtable<String, Method> MethodList = new Hashtable<String, Method>();
		public int N_Methods;
		public String Cardinality = null;
		public String VarType = null;
		public String OtherFacts = null;
		
		/**
		 * Initilize this Sub-Class
		 */
		public Slot(String CurrSlot) {
			SlotName = CurrSlot;
			this.initialize();			
		}
		/**
		 * retruns the name of the current slot
		 */
		public String toString(){
			return SlotName;
		}		
		// ----------------------------------------------------------
		// --- Im Folgenden nur PRIVATE Methoden --------------------
		private void initialize() {
			
			// --- Die betreffende Methoden herausfiltern -----------
			this.setMethods4Slot();
			
			// --- Beschreibungsparameter ermitteln -----------------
			N_Methods = MethodList.size();
			Method SingleMethod;
			if ( N_Methods == 2 ) {
				// --- "einfache" Variablen mit Getter und Setter ---
				Cardinality = "single";
				SingleMethod = MethodList.get( "get" + SlotName.toLowerCase() );				
				Class<?> ReturnType = SingleMethod.getReturnType();
				VarType = getClassType( ReturnType );
				
			} else {
				// --- "komplexere" Variable / ArrayListen ----------
				Cardinality = "multiple";
				SingleMethod = MethodList.get( "add" + SlotName.toLowerCase() );
				Class<?>[] ParaClass = SingleMethod.getParameterTypes();
				VarType = "";
				for (int i = 0; i < ParaClass.length; i++) {
					if ( VarType.equalsIgnoreCase("") ) {
						VarType = getClassType( ParaClass[i] );
					} else {
						VarType = VarType + ", " + getClassType( ParaClass[i] );
					}
				}
			}
		}	
		
		/**
		 * Returns a description of a Class-Reference
		 * @param Clazz
		 * @return
		 */
		private String getClassType( Class<?> Clazz ) {
			
			String CurrClass = Clazz.getSimpleName();
			if ( ! (CurrClass.equalsIgnoreCase("boolean") || CurrClass.equalsIgnoreCase("String") ||
					CurrClass.equalsIgnoreCase("int") || CurrClass.equalsIgnoreCase("float") ) ) 
			{
				CurrClass = "Instance of " + CurrClass;
			};
			return CurrClass;
		}
		
		/**
		 * Filter the methods, which belonging to the Filter-Term
		 * @param Filter4
		 * @return
		 */
		private void setMethods4Slot() {
			// --- Mehoden heraussuchen, die den aktuelle Slot betreffen  ----- 
			String CurrSlotNa = SlotName.toLowerCase();
			Method[] theMethods = CurrClass.getMethods();
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
