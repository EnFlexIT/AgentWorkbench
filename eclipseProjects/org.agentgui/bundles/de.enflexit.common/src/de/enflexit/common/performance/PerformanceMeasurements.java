package de.enflexit.common.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The singleton PerformanceMeasurements can be used by any instance in the AWB runtime environment to 
 * measure the performance of specific repetitive parts (e.g. simulation cycles and others).
 * <p>
 * For this, use the static access method of this class and add a {@link PerformanceMeasurement} to the 
 * stack of this class OR simply start a {@link PerformanceMeasurement} (this will create a measurement 
 * with default values automatically).
 * .<p>
 * Measurements are identified by a task description (a string). Individual measurements 
 * can be added by the {@link #addPerformanceMeasurement(String, Integer)} method.
 * A {@link PerformanceMeasurement} contains the descriptor and the number of measurements that
 * are to be used for calculating average values.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class PerformanceMeasurements {

	private HashMap<String, PerformanceMeasurement> measurementHashMap;
	private HashMap<String, PerformanceGroup> groupHashMap;
	
	
	private static PerformanceMeasurements thisInstance;
	/**
	 * Private constructor for the PerformanceMeasurements.
	 */
	private PerformanceMeasurements() { }
	/**
	 * Gets the single instance of PerformanceMeasurements.
	 * @return single instance of PerformanceMeasurements
	 */
	public static PerformanceMeasurements getInstance() {
		if (thisInstance==null) {
			thisInstance = new PerformanceMeasurements();
		}
		return thisInstance;
	}

	
	/**
	 * Returns the measurements HashMap.
	 * @return the measurement HashMap
	 */
	public HashMap<String, PerformanceMeasurement> getMeasurementHashMap() {
		if (measurementHashMap==null) {
			measurementHashMap = new HashMap<>();
		}
		return measurementHashMap;
	}
	/**
	 * Returns the group HashMap.
	 * @return the group HashMap
	 */
	public HashMap<String, PerformanceGroup> getGroupHashMap() {
		if (groupHashMap==null) {
			groupHashMap = new HashMap<>();
		}
		return groupHashMap;
	}
	
	/**
	 * Checks if the performance measurement belongs to a group.
	 * @param pm the PerformanceMeasurement
	 */
	private void checkGroupOfPerformanceMeasurement(PerformanceMeasurement pm) {
		List<PerformanceGroup> pgList = new ArrayList<>(this.getGroupHashMap().values());
		for (int i = 0; i < pgList.size(); i++) {
			pgList.get(i).addGroupMemberIfIsGroupMember(pm);
		}
	}
	
	/**
	 * Adds a PerformanceMeasurement to the know local measurements. If a measurement with the
	 * same descriptor is already there, it will be overwritten.
	 *
	 * @param taskDescriptor the task descriptor
	 * @return the simulation measurement added
	 */
	public PerformanceMeasurement addPerformanceMeasurement(String taskDescriptor) {
		return this.addPerformanceMeasurement(taskDescriptor, null);
	}
	/**
	 * Adds a PerformanceMeasurement to the know local measurements. If a measurement with the
	 * same descriptor is already there, it will be overwritten.
	 *
	 * @param taskDescriptor the task descriptor
	 * @param maxNumberForAverage the max number for average
	 * @return the simulation measurement added
	 */
	public PerformanceMeasurement addPerformanceMeasurement(String taskDescriptor, Integer maxNumberForAverage) {
		PerformanceMeasurement pMeasure = new PerformanceMeasurement(taskDescriptor, maxNumberForAverage);
		this.getMeasurementHashMap().put(taskDescriptor, pMeasure);
		this.checkGroupOfPerformanceMeasurement(pMeasure);
		return pMeasure;
	}

	/**
	 * Sets the specified measurement started.
	 * @param taskDescriptor the new measurement started
	 */
	public void setMeasurementStarted(String taskDescriptor) {
		
		PerformanceMeasurement simMeasure = this.getMeasurementHashMap().get(taskDescriptor);
		if (simMeasure==null) {
			simMeasure = addPerformanceMeasurement(taskDescriptor);
		}
		simMeasure.setMeasurementStarted();
	}
	/**
	 * Sets the specified measurement finalized.
	 * @param taskDescriptor the new measurement finalized
	 */
	public void setMeasurementFinalized(String taskDescriptor) {
		PerformanceMeasurement simMeasure = this.getMeasurementHashMap().get(taskDescriptor);
		if (simMeasure!=null) {
			simMeasure.setMeasurementFinalized();
		}
	}
	/**
	 * Returns the measurement interval average for the specified task.
	 *
	 * @param taskDescriptor the task descriptor
	 * @return the measurement interval average
	 */
	public Long getMeasurementIntervalAverage(String taskDescriptor) {
		PerformanceMeasurement simMeasure = this.getMeasurementHashMap().get(taskDescriptor);
		if (simMeasure!=null) {
			return simMeasure.getMeasurementIntervalAverage();
		}
		return null;
	}
	
	
	/**
	 * Adds a performance group to the PerformanceMeasurements.
	 *
	 * @param groupName the group name
	 * @param groupTaskDescriptorArray the group task descriptor array
	 */
	public void addPerformanceGroup(String groupName, String[] groupTaskDescriptorArray) {
		this.addPerformanceGroup(new PerformanceGroup(groupName, groupTaskDescriptorArray));
	}
	/**
	 * Adds a performance group to the PerformanceMeasurements.
	 * @param pg the PerformanceGroup to add
	 */
	public void addPerformanceGroup(PerformanceGroup pg) {
		this.getGroupHashMap().put(pg.getGroupName(), pg);
	}
	
}
