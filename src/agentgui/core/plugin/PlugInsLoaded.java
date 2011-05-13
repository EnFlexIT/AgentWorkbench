package agentgui.core.plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import agentgui.core.application.Language;
import agentgui.core.application.Project;

/**
 * 
 * @author Christian Derksen 
 */
public class PlugInsLoaded extends Vector<PlugIn> {

	private static final long serialVersionUID = 8827995777943963298L;
	
	/**
	 * This method will load a PlugIn given by its class-reference 
	 * @param plugInRefernce
	 */
	public PlugIn loadPlugin(Project project, String plugInReference) throws PlugInLoadException {
		
		PlugIn plugIn = null;
		Class<?> plugInClass = null;

		// ----------------------------------------------------------
		// --- Try to get the class for the PlugIn Reference --------
		try {
			plugInClass = (Class<?>) Class.forName(plugInReference);
		} catch (ClassNotFoundException e) {
			throw new PlugInLoadException(e);
		}
		if (plugInClass==null) return null; 
		
		// ----------------------------------------------------------
		// --- If the plugIn-class WAS found  -----------------------
		try {
			
			// --- look for the right constructor parameter ---------
			Class<?>[] conParameter = new Class[1];
			conParameter[0] = Project.class;
		
			// --- Get the constructor ------------------------------	
			Constructor<?> plugInClassConstructor = plugInClass.getConstructor(conParameter);
			
			// --- Define the argument for the newInstance call ----- 
			Object[] args = new Object[1];
			args[0] = project;
			
			plugIn = (PlugIn) plugInClassConstructor.newInstance(args);
			
		} catch (SecurityException e) {
			throw new PlugInLoadException(e);
		} catch (NoSuchMethodException e) {
			throw new PlugInLoadException(e);
		} catch (IllegalArgumentException e) {
			throw new PlugInLoadException(e);
		} catch (InstantiationException e) {
			throw new PlugInLoadException(e);
		} catch (IllegalAccessException e) {
			throw new PlugInLoadException(e);
		} catch (InvocationTargetException e) {
			throw new PlugInLoadException(e);
		}
		if (plugIn==null) return null;
		
		
		// ----------------------------------------------------------
		// --- Check some configurations of the PlugIn -------------- 
		if (plugIn.getName()==null) {
			throw new PlugInLoadException(Language.translate("Es wurde kein Name für das PlugIn konfiguriert"));
		} else if (plugIn.getName().equals("")) {
			throw new PlugInLoadException(Language.translate("Es wurde kein Name für das PlugIn konfiguriert"));
		}
		
		// --- Remind the classReference in the PlugIn --------------
		plugIn.setClassReference(plugInReference);
		// --- Call the onPlugIn() method ---------------------------
		plugIn.onPlugIn();
		// --- add the PlugIn to the local Vector -------------------
		this.add(plugIn);		
		return plugIn;
	}
	
	/**
	 * This will unload and remove a single ProjectPlugin from the current project
	 * @param plugIn
	 */
	public void removePlugIn(PlugIn plugIn) {
		// --- Call the onPlugOut()method ---------------------------
		plugIn.onPlugOut();
		// --- Call the afterPlugOut()method ------------------------
		plugIn.afterPlugOut();
		// --- remove the PlugIn from the local Vector --------------
		this.remove(plugIn);
	}
	
	
	/**
	 * This method returns a single PlugIn Instance  
	 * given by its name or its class reference 
	 * 
	 * @param plugInName_or_plugInReference
	 * @return
	 */
	public PlugIn getPlugIn(String plugInName_or_plugInReference) {

		String search = plugInName_or_plugInReference;
		for (int i = 0; i < this.size(); i++) {
			PlugIn pi = this.get(i);
			if (pi.getName().equals(search)) {
				return pi;
			} else if (pi.getClassReference().equals(search)) {
				return pi;
			}
		}
		return null;
	}
	
	/**
	 * This method checks if a PlugInClass is already loaded 
	 * @param PlugInNameOrReference
	 * @return
	 */
	public boolean isLoaded(String plugInName_or_plugInReference) {

		String search = plugInName_or_plugInReference;
		for (int i = 0; i < this.size(); i++) {
			PlugIn pi = this.get(i);
			if (pi.getName().equals(search)) {
				return true;
			} else if (pi.getClassReference().equals(search)) {
				return true;
			}
		}
		return false;
	}

}
