package org.agentgui.bundle.evaluation;

import java.lang.reflect.Modifier;

import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;
import jade.core.BaseService;

/**
 * The Filter for BasseService classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForBaseService extends AbstractBundleClassFilter {

	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#getFilterScope()
	 */
	@Override
	public String getFilterScope() {
		return jade.core.BaseService.class.getName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isFilterCriteria(java.lang.Class)
	 */
	@Override
	public boolean isFilterCriteria(Class<?> clazz) {
		return (clazz==BaseService.class);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isInFilterScope(java.lang.Class)
	 */
	@Override
	public boolean isInFilterScope(Class<?> clazz) {
		return BaseService.class.isAssignableFrom(clazz) && BaseService.class.equals(clazz)==false && Modifier.isAbstract(clazz.getModifiers())==false;
	}

}
