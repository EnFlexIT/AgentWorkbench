package de.enflexit.awb.core.bundleEvaluation;

import java.lang.reflect.Modifier;

import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;
import jade.core.Agent;

/**
 * The Filter For Agent classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForAgent extends AbstractBundleClassFilter {

	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#getFilterScope()
	 */
	@Override
	public String getFilterScope() {
		return jade.core.Agent.class.getName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isFilterCriteria(java.lang.Class)
	 */
	@Override
	public boolean isFilterCriteria(Class<?> clazz) {
		return (clazz==Agent.class);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isInFilterScope(java.lang.Class)
	 */
	@Override
	public boolean isInFilterScope(Class<?> clazz) {
		return Agent.class.isAssignableFrom(clazz) && Agent.class.equals(clazz)==false && Modifier.isAbstract(clazz.getModifiers())==false;
	}

}
