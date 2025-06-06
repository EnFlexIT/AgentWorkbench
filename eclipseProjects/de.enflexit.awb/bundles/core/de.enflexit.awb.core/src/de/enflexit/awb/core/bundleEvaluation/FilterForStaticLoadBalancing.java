package de.enflexit.awb.core.bundleEvaluation;

import java.lang.reflect.Modifier;

import de.enflexit.awb.simulation.balancing.StaticLoadBalancingBase;
import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;

/**
 * The Filter for StaticLoadBalancingBase classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForStaticLoadBalancing extends AbstractBundleClassFilter {

	private Class<StaticLoadBalancingBase> filterclass = StaticLoadBalancingBase.class;
	
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
