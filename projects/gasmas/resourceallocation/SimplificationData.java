package gasmas.resourceallocation;

import java.util.HashSet;

public class SimplificationData extends GenericMesssageData {

	private static final long serialVersionUID = 827774764094560195L;

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

	public void setUrInitiator(String urInitiator) {
		this.urInitiator = urInitiator;
	}

	public String getUrInitiator() {
		return urInitiator;
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

	public SimplificationData(String initiator, String station) {
		this.initiator = initiator;
		this.way.add(station);
	}

	public SimplificationData(String initiator, boolean b) {
		this.initiator = initiator;
		if (b) {
			this.urInitiator = initiator;
		}
	}

	public SimplificationData(String initiator, HashSet<String> way, boolean b, String urInitiator) {
		this.initiator = initiator;
		this.way = way;
		this.answer = b;
		this.urInitiator = urInitiator;
	}

}
