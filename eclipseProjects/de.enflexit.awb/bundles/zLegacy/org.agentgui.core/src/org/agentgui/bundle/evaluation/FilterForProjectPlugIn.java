package org.agentgui.bundle.evaluation;

import java.lang.reflect.Modifier;

import agentgui.core.plugin.PlugIn;
import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;

/**
 * The Filter For PlugIn classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForProjectPlugIn extends AbstractBundleClassFilter {

	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#getFilterScope()
	 */
	@Override
	public String getFilterScope() {
		return PlugIn.class.getName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isFilterCriteria(java.lang.Class)
	 */
	@Override
	public boolean isFilterCriteria(Class<?> clazz) {
		return (clazz==PlugIn.class);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isInFilterScope(java.lang.Class)
	 */
	@Override
	public boolean isInFilterScope(Class<?> clazz) {
		return PlugIn.class.isAssignableFrom(clazz) && PlugIn.class.equals(clazz)==false && Modifier.isAbstract(clazz.getModifiers())==false;
	}

}
