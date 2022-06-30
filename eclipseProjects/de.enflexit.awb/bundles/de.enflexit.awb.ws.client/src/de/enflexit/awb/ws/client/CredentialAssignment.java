package de.enflexit.awb.ws.client;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class CredentialAssignment implements Serializable {

	private static final long serialVersionUID = -7854620281999217458L;
	
	private Integer id;
	private int idServerURL;
	private int idApiRegistration;
	private int idCredetnial;
	
	/**
	 * Returns the id of a credential.
	 * @return the credential id
	 */
	public Integer getID() {
		if (id==null) {
			// --- Randomize an ID ------------------
			int min = 1000000;
			int max = Integer.MAX_VALUE;
			id = ThreadLocalRandom.current().nextInt(min, max);
		}
		return id;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {

		// TODO
		
		return super.equals(compObj);
	}
	
	
}
