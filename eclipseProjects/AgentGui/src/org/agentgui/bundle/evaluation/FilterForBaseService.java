package org.agentgui.bundle.evaluation;

import jade.core.BaseService;

/**
 * The Filter for BasseService classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForBaseService extends AbstractBundleClassFilter {

	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.AbstractBundleClassFilter#getFilterScope()
	 */
	@Override
	public String getFilterScope() {
		return jade.core.BaseService.class.getName();
	}

	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.AbstractBundleClassFilter#isFilterCriteria(java.lang.Class)
	 */
	@Override
	public boolean isFilterCriteria(Class<?> clazz) {
		return (clazz==BaseService.class);
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.AbstractBundleClassFilter#isInFilterScope(java.lang.Class)
	 */
	@Override
	public boolean isInFilterScope(Class<?> clazz) {
		return BaseService.class.isAssignableFrom(clazz);
	}

	

}
