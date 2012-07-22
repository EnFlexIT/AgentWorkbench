package gasmas.resourceallocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimplificationData implements Serializable {

	private static final long serialVersionUID = 827774764094560195L;

	private String initiator = "";

	private boolean answer = false;

	public boolean isAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	private List<String> way = new ArrayList<String>();

	public String getInitiator() {
		return initiator;
	}
	
	public List<String> getWay() {
		return way;
	}

	public void addStation(String station) {
		this.way.add(station);
	}

	public void addAnotherWay(List<String> otherWay) {
		this.way.addAll(otherWay);
	}

	public SimplificationData(String initiator, String station) {
		this.initiator = initiator;
		this.way.add(station);
	}

	public SimplificationData(String initiator) {
		this.initiator = initiator;
	}

	public SimplificationData(String initiator, List<String> way, boolean b) {
		this.initiator = initiator;
		this.way = way;
		this.answer = b;
	}

}
