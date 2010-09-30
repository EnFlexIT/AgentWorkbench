package mas.service.sensoring;

import java.util.Observable;

public class ServiceActuator extends Observable {

	public void setChangedAndNotify(String topicWhichChanged) {
		this.setChanged();
		this.notifyObservers(topicWhichChanged);		
	}
	
	
	
}
