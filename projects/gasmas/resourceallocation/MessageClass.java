package gasmas.resourceallocation;

import java.io.Serializable;

public class MessageClass implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -836605615950400256L;

	String reason;
	AllocData data;
	
	public MessageClass(String reason, AllocData data) {
		this.reason = reason;
		this.data = data;
	}
	
	
}

