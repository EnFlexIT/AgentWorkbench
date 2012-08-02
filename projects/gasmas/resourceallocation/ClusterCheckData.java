package gasmas.resourceallocation;

import java.io.Serializable;
import java.util.HashSet;

public class ClusterCheckData implements Serializable {

	private static final long serialVersionUID = -3498150685713525063L;

	private String initiator = "";

	private HashSet<String> way = new HashSet<String>();

	private boolean answer = false;

	public boolean isAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;

	}

	public String getInitiator() {
		return initiator;
	}

	public HashSet<String> getWay() {
		return way;
	}

	public void addStation(String station) {
		this.way.add(station);
	}

	public void addAnotherWay(HashSet<String> otherWay) {
		this.way.addAll(otherWay);
	}

	public ClusterCheckData(String initiator, String station) {
		this.initiator = initiator;
		this.way.add(station);
	}

	public ClusterCheckData(String initiator) {
		this.initiator = initiator;
	}

	public ClusterCheckData(String initiator, HashSet<String> way) {
		this.initiator = initiator;
		this.way = way;
	}

	public ClusterCheckData(String initiator, boolean answer) {
		this.initiator = initiator;
		this.answer = answer;
	}
}
