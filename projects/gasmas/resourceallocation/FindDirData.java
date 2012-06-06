package gasmas.resourceallocation;

public class FindDirData extends GenericMesssageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3438711840630347490L;

	protected String way = "";
	protected String reason;

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public FindDirData(String way, String reason) {
		this.way = way;
		this.reason = reason;
	}

	public FindDirData(String reason) {
		this.reason = reason;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

}
