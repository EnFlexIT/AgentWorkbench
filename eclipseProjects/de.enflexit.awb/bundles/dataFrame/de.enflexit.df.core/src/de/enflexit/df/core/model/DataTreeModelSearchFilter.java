package de.enflexit.df.core.model;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The Interface DataTreeModelSearchFilter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface DataTreeModelSearchFilter {

	/**
	 * Returns, if the specified tree node matches the filter criteria.
	 *
	 * @param treeNode the tree node
	 * @return true, if successful
	 */
	public boolean matchesFilterCriteria(DefaultMutableTreeNode treeNode);
	
}
