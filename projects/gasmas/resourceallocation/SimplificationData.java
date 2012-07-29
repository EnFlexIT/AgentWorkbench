package gasmas.resourceallocation;

import java.io.Serializable;
import java.util.HashSet;

public class SimplificationData implements Serializable {

	private static final long serialVersionUID = 827774764094560195L;

	private String initiator = "";

	private String urInitiator = "";

	private boolean answer = false;

	private HashSet<String> way = new HashSet<String>();

	private int sessionID = 0;

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public int getSessionID() {
		return sessionID;
	}

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

	public SimplificationData(String initiator, HashSet<String> way, boolean b, String urInitiator, int SID) {
		this.initiator = initiator;
		this.way = way;
		this.answer = b;
		this.urInitiator = urInitiator;
		this.sessionID = SID;
	}

}
