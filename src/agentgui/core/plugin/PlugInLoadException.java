package agentgui.core.plugin;

public class PlugInLoadException extends RuntimeException {

	private static final long serialVersionUID = -3387741205485846804L;

	public PlugInLoadException(Throwable throwable) {
		super(throwable);		
	}
	public PlugInLoadException(String msg) {
		super(msg);		
	}
	
}
