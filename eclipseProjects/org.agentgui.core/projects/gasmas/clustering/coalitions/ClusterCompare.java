/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package gasmas.clustering.coalitions;

import java.util.HashSet;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;

/**
 * The Class ClusterCompare, compares different ClusterComponents with each other
 */
public class ClusterCompare {

	/** The EQUAL. */
	public static final int EQUAL = 1;

	/** The BETTER. */
	public static final int BETTER = 2;

	/** The WORSE. */
	public static final int WORSE = 3;

	/** The PAR t_ o f_ suggested. */
	public static final int PART_OF_SUGGESTED = 4;

	/** The PAR t_ o f_ new. */
	public static final int PART_OF_NEW = 5;

	/** The DRO p_ both. */
	public static final int DROP_BOTH = 6;
	
	/**
	 * Check agent cluster.
	 *
	 * @param clusterNC the cluster nc
	 * @param suggestedCNC the suggested cnc
	 * @return the int
	 */
	public static int compareClusters(ClusterNetworkComponent clusterNC, ClusterNetworkComponent suggestedCNC) {
		HashSet<String> clusterNCs = new HashSet<String>(clusterNC.getClusterNetworkModel().getNetworkComponents().keySet());
		HashSet<String> suggestedClusterNCs = new HashSet<String>(suggestedCNC.getClusterNetworkModel().getNetworkComponents().keySet());
		if (clusterNCs.size() == suggestedClusterNCs.size()) {
			if (clusterNCs.containsAll(suggestedClusterNCs)) {
				return ClusterCompare.EQUAL;
			}
		}
		if (clusterNCs.size() > suggestedClusterNCs.size()) {
			if (clusterNCs.containsAll(suggestedClusterNCs)) {
				return ClusterCompare.PART_OF_NEW;
			}
		}
		if (clusterNCs.size() < suggestedClusterNCs.size()) {
			if (suggestedClusterNCs.containsAll(clusterNCs)) {
				return ClusterCompare.PART_OF_SUGGESTED;
			}
		}
		return findBetterCluster(clusterNC, suggestedCNC);
	}

	/**
	 * Find better cluster.
	 *
	 * @param cluster the cluster
	 * @param suggestedCluster the suggested cluster
	 * @return the int
	 */
	protected static int findBetterCluster(ClusterNetworkComponent cluster, ClusterNetworkComponent suggestedCluster) {
		// analyze amount of connections of the Cluster
		int clusterConnections = cluster.getConnectionNetworkComponents().size();
		int suggestedClusterConnections = suggestedCluster.getConnectionNetworkComponents().size();
		if (clusterConnections < suggestedClusterConnections) {
			return ClusterCompare.BETTER;
		} else if (suggestedClusterConnections < clusterConnections) {
			return ClusterCompare.WORSE;
		}
		// analyze size of the Clusters
		int clusterNCs = cluster.getClusterNetworkModel().getNetworkComponents().size();
		int suggestedClusterNCs = suggestedCluster.getClusterNetworkModel().getNetworkComponents().size();
		if (clusterNCs < suggestedClusterNCs) {
			return ClusterCompare.BETTER;
		} else if (suggestedClusterNCs > clusterNCs) {
			return ClusterCompare.WORSE;
		}
		return ClusterCompare.DROP_BOTH;
	}

}
