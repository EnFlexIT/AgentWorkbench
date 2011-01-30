package agentgui.graphEnvironment.controller;

public enum GasGridElements {
	BRANCH, COMPRESSOR, PIPE, ENTRY, EXIT, STORAGE, VALVE, UNKNOWN;
	
	/**
	 * Gets the corresponding enumeration element for the type strings from the graphML data field 
	 * @param type The type String
	 * @return The enumeration element
	 */
	public static GasGridElements getElement(String type){
		if(type.equals("branch")){
			return BRANCH;
		}else if(type.equals("compressor")){
			return COMPRESSOR;
		}else if(type.equals("pipe")){
			return PIPE;
		}else if(type.equals("exit")){
			return EXIT;
		}else if(type.equals("entry")){
			return ENTRY;
		}else if(type.equals("storage")){
			return STORAGE;
		}else if(type.equals("valve")){
			return VALVE;
		}else{
			return UNKNOWN;
		}
	}
	
	/**
	 * Gets the corresponding class name for each enumeration element 
	 * @param element The enumeration element
	 * @return The class name
	 */
	public static String getClassName(GasGridElements element){
		switch (element) {
		case BRANCH:
			return "Branch";
		case COMPRESSOR:
			return "Compressor";
		case ENTRY:
			return "Entry";
		case EXIT:
			return "Exit";
		case PIPE:
			return "Pipe";
		case STORAGE:
			return "Storage";
		case VALVE:
			return "Valve";
		default:
			return null;

		}
	}
}
