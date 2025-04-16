package org.agentgui.bundle.evaluation;

import java.lang.reflect.Modifier;

import agentgui.simulationService.balancing.DynamicLoadBalancingBase;
import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;

/**
 * The Filter for DynamicLoadBalancing classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForDynamicLoadBalancing extends AbstractBundleClassFilter {

	private Class<DynamicLoadBalancingBase> filterclass = DynamicLoadBalancingBase.class;
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#getFilterScope()
	 */
	@Override
	public String getFilterScope() {
		return this.filterclass.getName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isFilterCriteria(java.lang.Class)
	 */
	@Override
	public boolean isFilterCriteria(Class<?> clazz) {
		return (clazz==this.filterclass);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter#isInFilterScope(java.lang.Class)
	 */
	@Override
	public boolean isInFilterScope(Class<?> clazz) {
		return this.filterclass.isAssignableFrom(clazz) && this.filterclass.equals(clazz)==false && Modifier.isAbstract(clazz.getModifiers())==false;
	}

}
