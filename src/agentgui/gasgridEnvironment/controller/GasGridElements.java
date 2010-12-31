package agentgui.gasgridEnvironment.controller;

public enum GasGridElements {
	BRANCH, COMPRESSOR, PIPE, SINK, SOURCE, STORAGE, VALVE, UNKNOWN;
	
	public static GasGridElements getElement(String type){
		if(type.equals("branch")){
			return BRANCH;
		}else if(type.equals("compressor")){
			return COMPRESSOR;
		}else if(type.equals("pipe")){
			return PIPE;
		}else if(type.equals("sink")){
			return SINK;
		}else if(type.equals("source")){
			return SOURCE;
		}else if(type.equals("storage")){
			return STORAGE;
		}else if(type.equals("valve")){
			return VALVE;
		}else{
			return UNKNOWN;
		}
	}
}
