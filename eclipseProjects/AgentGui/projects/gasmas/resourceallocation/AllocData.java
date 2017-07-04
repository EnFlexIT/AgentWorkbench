package gasmas.resourceallocation;

import gasmas.initialProcess.GenericMesssageData;

import java.util.ArrayList;
import java.util.List;

public class AllocData extends GenericMesssageData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3439599732296039356L;

	protected List<String> way = new ArrayList<String>();
	protected int cap;
	protected String reason;
	protected int performative = 1;

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setPerformative(int performative) {
		this.performative = performative;
	}

	public int getPerformative() {
		return performative;
	}

	public String getReason() {
		return reason;
	}

	public AllocData(int cap, String[] way, int performative, String reason) {
		this.cap = cap;
		for (int i = 0; i > way.length; i++) {
			this.way.add(way[i]);
		}
		this.performative = performative;
		this.reason = reason;
	}

	public AllocData() {
		this.cap = 0;
	}

	public AllocData(int cap, String route, int performative, String reason) {
		this.cap = cap;
		this.way.add(route);
		this.performative = performative;
		this.reason = reason;
	}

	public void addRoute(String route) {
		this.way.add(route);
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public int getCap() {
		return this.cap;
	}

	public List<String> getWay() {
		return way;
	}

	public void setWay(List<String> way) {
		this.way = way;
	}

	public void deleteRouteWith(String station) {
		if (station.contains(station)) {
			way.remove(station);
		}
	}

}
