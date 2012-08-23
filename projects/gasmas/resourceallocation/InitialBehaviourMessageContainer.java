package gasmas.resourceallocation;

import java.io.Serializable;

public class InitialBehaviourMessageContainer implements Serializable {

	private static final long serialVersionUID = 1326135126924738223L;
	
	protected Object data;
	
	public Object getData() {
		return data;
	}

	public InitialBehaviourMessageContainer(Object data) {
		this.data = data;
	}
	
	
}

