package org.agentgui.bundle.evaluation;

import jade.core.Agent;

/**
 * The Filter For Agent classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForAgent extends AbstractBundleClassFilter {

	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.AbstractBundleClassFilter#getFilterScope()
	 */
	@Override
	public String getFilterScope() {
		return jade.core.Agent.class.getName();
	}

	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.AbstractBundleClassFilter#isFilterCriteria(java.lang.Class)
	 */
	@Override
	public boolean isFilterCriteria(Class<?> clazz) {
		return (clazz==Agent.class);
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.AbstractBundleClassFilter#isInFilterScope(java.lang.Class)
	 */
	@Override
	public boolean isInFilterScope(Class<?> clazz) {
		return Agent.class.isAssignableFrom(clazz);
	}


}
