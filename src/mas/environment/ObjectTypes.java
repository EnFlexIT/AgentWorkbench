package mas.environment;

import application.Language;



public enum ObjectTypes {
	
	AGENT, OBSTACLE, PLAYGROUND;
	
	public static ObjectTypes getType(sma.ontology.AbstractObject object){
		ObjectTypes type = null;
		
		if(object instanceof sma.ontology.AgentObject){
			type = AGENT;
		}else if(object instanceof sma.ontology.ObstacleObject){
			type = OBSTACLE;
		}else if(object instanceof sma.ontology.PlaygroundObject){
			type = PLAYGROUND;
		}
		
		return type;
	}
	
	/**
	 * Returns the type constant for a given name, or null if there is no corresponding type 
	 * @param name Type name
	 * @return
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
