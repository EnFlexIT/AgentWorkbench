package gasmas.resourceallocation;

public class FindDirData extends GenericMesssageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3438711840630347490L;

	/** String, which holds the way, how the message went */
	protected String way = "";
	
	/** String, which holds the initiator of the actual asking round */
	protected String flow;

	public void setFlow(String reason) {
		this.flow = reason;
	}

	public String getFlow() {
		return flow;
	}

	public FindDirData(String way, String reason) {
		this.way = way;
		this.flow = reason;
	}

	public FindDirData(String reason) {
		this.flow = reason;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

}
