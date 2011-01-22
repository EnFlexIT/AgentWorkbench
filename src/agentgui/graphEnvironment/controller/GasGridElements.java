package agentgui.graphEnvironment.controller;

public enum GasGridElements {
	BRANCH, COMPRESSOR, PIPE, ENTRY, EXIT, STORAGE, VALVE, UNKNOWN;
	
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
}
