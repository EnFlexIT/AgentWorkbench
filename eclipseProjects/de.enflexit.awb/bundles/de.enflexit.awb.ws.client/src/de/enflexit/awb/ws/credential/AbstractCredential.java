package de.enflexit.awb.ws.credential;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The Class AbstractCredential.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractCredential implements Serializable {

	private static final long serialVersionUID = -5179570740645614521L;

	private Integer id;
	private String name;
	
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
	
	/**
	 * Returns a name (short description) for the credential.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty(){	
		if(name!=null) {
			return true;
		}else {
			return false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = true;
		if (obj instanceof AbstractCredential) {
			AbstractCredential abstCred = (AbstractCredential) obj;
			if (abstCred.getID() != this.getID()) {
				equals = false;
			}

			if (!abstCred.getName().equals(this.getName())) {
				equals = false;
			}
		} else {
			equals = false;
		}

		return equals;
	}
}
