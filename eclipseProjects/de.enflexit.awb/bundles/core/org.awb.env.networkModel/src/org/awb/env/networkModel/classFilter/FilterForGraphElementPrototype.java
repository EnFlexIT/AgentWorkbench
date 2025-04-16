package org.awb.env.networkModel.classFilter;

import java.lang.reflect.Modifier;

import org.awb.env.networkModel.prototypes.AbstractGraphElementPrototype;

import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;

/**
 * The Filter for AbstractGraphElementPrototype classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForGraphElementPrototype extends AbstractBundleClassFilter {

	private Class<AbstractGraphElementPrototype> filterclass = AbstractGraphElementPrototype.class;
	
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
