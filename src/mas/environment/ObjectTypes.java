package mas.environment;

import application.Language;

/**
 * Supported types of ontology objects
 * @author Nils
 *
 */
public enum ObjectTypes {
	
	AGENT, OBSTACLE, PLAYGROUND;
	
	/**
	 * Returns the type constant for the given object, or null if it is not a supported type 
	 * @param object The object to be checked
	 * @return The type constant or null
	 */
	public static ObjectTypes getType(mas.display.ontology.AbstractObject object){
		ObjectTypes type = null;
		
		if(object instanceof mas.display.ontology.AgentObject){
			type = AGENT;
		}else if(object instanceof mas.display.ontology.ObstacleObject){
			type = OBSTACLE;
		}else if(object instanceof mas.display.ontology.PlaygroundObject){
			type = PLAYGROUND;
		}
		
		return type;
	}
	
	/**
	 * Returns the type constant for a given name, or null if there is no corresponding type 
	 * @param name Type name
	 * @return The type constant or null
	 */
	public static ObjectTypes getType(String name){
		ObjectTypes type = null;
		
		if(name.equalsIgnoreCase("agent")){
			type = AGENT;
		}else if(name.equalsIgnoreCase("obstacle")){
			type = OBSTACLE;
		}else if(name.equalsIgnoreCase("playground")){
			type = PLAYGROUND;
		}
		return type;
	}
	
	/**
	 * Returns the type constant for a given translated name, , or null if there is no corresponding type  
	 * @param name The translated type name
	 * @return The type constant or null
	 */
	public static ObjectTypes getTranslatedType(String name){
		ObjectTypes type = null;
		if(name.equalsIgnoreCase(Language.translate("Agent"))){
			type = AGENT;
		}else if(name.equalsIgnoreCase(Language.translate("Hindernis"))){
			type = OBSTACLE;
		}else if(name.equalsIgnoreCase(Language.translate("Sub-Umgebung"))){
			type = PLAYGROUND;
		}
		return type;
	}
	
	

}
