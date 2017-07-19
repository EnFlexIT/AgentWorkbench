package gasmas.resourceallocation;

import java.util.ArrayList;
import java.util.List;

public class AllocDataNeed extends AllocData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8444188864448105835L;
	protected List<String> asked = new ArrayList<String>();
	protected boolean prio = false;
	protected int reducedby = 0;

	public int getReducedby() {
		return reducedby;
	}

	public void setReducedby(int reducedby) {
		this.reducedby = reducedby;
	}

	public AllocDataNeed(AllocData data) {
		this.cap = data.cap;
		this.way = data.way;
	}

	public AllocDataNeed(AllocData data, boolean prio) {
		this.cap = data.cap;
		this.way = data.way;
		this.prio = prio;
	}

	public AllocDataNeed() {
		super();
	}

	public void addAsked(String asked) {
		this.asked.add(asked);
	}

	public List<String> getAsked() {
		return this.asked;
	}

	public void setAsked(List<String> asked) {
		this.asked = asked;
	}

	public void setPrio(boolean prio) {
		this.prio = prio;
	}

}
