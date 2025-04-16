package org.agentgui.bundle.evaluation;

import java.lang.reflect.Modifier;

import agentgui.simulationService.time.TimeModel;
import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;

/**
 * The Filter For TimeModel classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForTimeModel extends AbstractBundleClassFilter {

	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#getFilterScope()
	 */
	@Override
	public String getFilterScope() {
		return TimeModel.class.getName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isFilterCriteria(java.lang.Class)
	 */
	@Override
	public boolean isFilterCriteria(Class<?> clazz) {
		return (clazz==TimeModel.class);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isInFilterScope(java.lang.Class)
	 */
	@Override
	public boolean isInFilterScope(Class<?> clazz) {
		return TimeModel.class.isAssignableFrom(clazz) && TimeModel.class.equals(clazz)==false && Modifier.isAbstract(clazz.getModifiers())==false;
	}

}
