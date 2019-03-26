package agentgui.envModel.graph.classFilter;

import java.util.Vector;

import org.agentgui.bundle.evaluation.BundleClassFilterService;

import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;

/**
 * The Class BundleClassFilterDefinifion provides the class filter definitions
 * that are required by graph environment.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class BundleClassFilterDefinifion implements BundleClassFilterService {

	private Vector<AbstractBundleClassFilter> bundleClassFilter;
	
	/* (non-Javadoc)
	 * @see org.agentgui.bundle.evaluation.BundleClassFilterService#getVectorOfBundleClassFilter()
	 */
	@Override
	public Vector<AbstractBundleClassFilter> getVectorOfBundleClassFilter() {
		if (bundleClassFilter==null) {
			bundleClassFilter = new Vector<>();
			bundleClassFilter.add(new FilterForGraphElementPrototype());
			bundleClassFilter.add(new FilterForNetworkComponentAdapter());
		}
		return bundleClassFilter;
	}

}
