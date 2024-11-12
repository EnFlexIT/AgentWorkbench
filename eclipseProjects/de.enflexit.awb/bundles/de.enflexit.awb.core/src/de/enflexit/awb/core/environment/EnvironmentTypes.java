package de.enflexit.awb.core.environment;

import java.util.Iterator;
import java.util.Vector;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;

/**
 * This extended Vector holds all known AWB-environment types.<br>
 * Its concrete instance is stored in the class GlobalInfo 
 * (accessible at runtime by using Application.RunInfo)
 *
 * @see Application#getGlobalInfo()
 * @see GlobalInfo
 * @see GlobalInfo#getKnownEnvironmentTypes()
 * @see EnvironmentType
 *   
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentTypes extends Vector<EnvironmentType>{

	private static final long serialVersionUID = 1887651840189227293L;

	/**
	 * Constructor of this class
	 */
	public EnvironmentTypes() {
		super();
	}
	
	/**
	 * This method returns the EnvironmentType instance searched by its key
	 * @param key the unique identifier for the environment model type
	 * @return The instance of the EnvironmentType
	 * @see EnvironmentType	
	 */
	public EnvironmentType getEnvironmentTypeByKey(String key) {
		
		for (Iterator<EnvironmentType> it = this.iterator(); it.hasNext();) {
			EnvironmentType envTyp = it.next();
			if (envTyp.getInternalKey().equals(key)) {
				return envTyp;
			}
		}
		return this.get(0); // --- the default value ---
	}
	
}
