package agentgui.core.environment;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

public class EnvironmentTypes extends Vector<EnvironmentType>{

	private static final long serialVersionUID = 1887651840189227293L;

	public EnvironmentTypes() {
		super();
	}
	
	/**
	 * This method returns the EnvironmentType instance searched by its key
	 * @param key
	 * @return
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
	
	/**
	 * This method returns this as a DefaultComboBoxModel
	 * @return
	 */
	public DefaultComboBoxModel getComboBoxModel() {
		
		DefaultComboBoxModel cbm = new DefaultComboBoxModel();
		for (Iterator<EnvironmentType> it = this.iterator(); it.hasNext();) {
			EnvironmentType envTyp = it.next();
			cbm.addElement(envTyp);
		}
		return cbm;
	}
	
}
