package org.agentgui.bundle.evaluation;

import jade.content.onto.Ontology;

/**
 * The Filter For {@link Ontology} classes.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FilterForOntology extends AbstractBundleClassFilter {

	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.AbstractBundleClassFilter#getFilterScope()
	 */
	@Override
	public String getFilterScope() {
		return jade.content.onto.Ontology.class.getName();
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.AbstractBundleClassFilter#isFilterCriteria(java.lang.Class)
	 */
	@Override
	public boolean isFilterCriteria(Class<?> clazz) {
		return (clazz==Ontology.class);
	}
	
	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.AbstractBundleClassFilter#isInFilterScope(java.lang.Class)
	 */
	@Override
	public boolean isInFilterScope(Class<?> clazz) {
		return Ontology.class.isAssignableFrom(clazz) && Ontology.class.equals(clazz)==false;
	}

	

}
