package gasmas.resourceallocation;

import java.io.Serializable;

public class StatusData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 827774764094560195L;

	/** Shows the actual step */
	int phase = 0;

	/** Shows the name of a cluster, in which the receiver is */
	String clusterName = "";

	public int getPhase() {
		return phase;
	}

	public String getClusterName() {
		return clusterName;
	}

	public StatusData(int step) {
		this.phase = step;
	}

	public StatusData(String clusterName) {
		this.phase = -1;
		this.clusterName = clusterName;
	}

}
