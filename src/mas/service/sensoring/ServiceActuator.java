package mas.service.sensoring;

import java.util.Vector;

import mas.service.environment.EnvironmentModel;

public class ServiceActuator {

	private Vector<ServiceSensor> serviceSensors = new Vector<ServiceSensor>();
		
	public void plugIn(ServiceSensor currSensor) {
		serviceSensors.addElement(currSensor);		
	}
	public void plugOut(ServiceSensor currSensor) {
		serviceSensors.removeElement(currSensor);
	}
	public void notifySensors(EnvironmentModel currEnvironmentModel, boolean aSynchron) {
		Object[] arrLocal = serviceSensors.toArray();
		for (int i = arrLocal.length-1; i>=0; i--) {
			((ServiceSensor)arrLocal[i]).putEnvironmentModel(currEnvironmentModel, aSynchron);
		}
	}

}
