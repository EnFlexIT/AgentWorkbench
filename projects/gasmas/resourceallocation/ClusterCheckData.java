package gasmas.resourceallocation;

import java.io.Serializable;
import java.util.HashSet;

public class ClusterCheckData implements Serializable {

	private static final long serialVersionUID = -3498150685713525063L;

	/** String, which holds the initiator of the actual asking round */
	private String initiator = "";

	/** String, which holds the initiator of the initial request */
	private String urInitiator = "";

	/** HashSet, which holds the way, how the message went */
	private HashSet<String> way = new HashSet<String>();

	/** Shows, if the message is an answer */
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

	public void setUrInitiator(String urInitiator) {
		this.urInitiator = urInitiator;
	}

	public String getUrInitiator() {
		return urInitiator;
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
		this.urInitiator = initiator;
	}

	public ClusterCheckData(String initiator, HashSet<String> way, String urInitiator) {
		this.initiator = initiator;
		this.way = way;
		this.urInitiator = urInitiator;
	}

	public ClusterCheckData(String initiator, boolean answer, String urInitiator) {
		this.initiator = initiator;
		this.answer = answer;
		this.urInitiator = urInitiator;
	}
}
