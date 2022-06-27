package de.enflexit.common.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The Class PerformanceGroup bundles several {@link PerformanceMeasurement} instances and organizes 
 * the result output according to the specified task description array.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class PerformanceGroup {

	private String groupName;
	private String[] groupTaskDescriptorArray;
	
	
	private List<PerformanceMeasurement> pMeasurementList;
	private List<PerformanceMeasurement> pMeasurementResultList;
	
	/**
	 * Instantiates a new performance group thats consolidates different measurements and it order when printing.
	 *
	 * @param groupName the group name
	 * @param groupTaskDescriptorArray the group task descriptor array
	 * @param removePreviousMeasurements the indicator to remove / reset previous measurements from the global stack first
	 */
	public PerformanceGroup(String groupName, String[] groupTaskDescriptorArray, boolean removePreviousMeasurements) {
		this.groupName = groupName;
		this.groupTaskDescriptorArray = groupTaskDescriptorArray;
		if (removePreviousMeasurements==true) {
			this.removeGroupMember();
		} else {
			this.collectGroupMember();
		}
	}
	/**
	 * Returns the group name.
	 * @return the group name
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * Returns the group task descriptor array.
	 * @return the group task descriptor array
	 */
	public String[] getGroupTaskDescriptorArray() {
		return groupTaskDescriptorArray;
	}
	
	/**
	 * Returns the list of performance measurement that is defined by this group.
	 * @return the performance measurement group
	 */
	private List<PerformanceMeasurement> getPerformanceMeasurementListOfGroup() {
		if (pMeasurementList==null) {
			pMeasurementList = new ArrayList<>();
		}
		return pMeasurementList;
	}
	/**
	 * Returns the performance measurement with the specified task description or <code>null</code>.
	 *
	 * @param taskDescription the task description
	 * @return the performance measurement
	 */
	private PerformanceMeasurement getPerformanceMeasurement(String taskDescription) {
		for (int i = 0; i < this.getPerformanceMeasurementListOfGroup().size(); i++) {
			PerformanceMeasurement pm = this.getPerformanceMeasurementListOfGroup().get(i);
			if (pm.getTaskDescriptor().equals(taskDescription)==true) {
				return pm;
			}
		}
		return null;
	}
	/**
	 * Sort performance measurement list.
	 */
	private void sortPerformanceMeasurementList() {
		Collections.sort(this.getPerformanceMeasurementListOfGroup(), new Comparator<PerformanceMeasurement>() {
			@Override
			public int compare(PerformanceMeasurement pm1, PerformanceMeasurement pm2) {
				String desc1 = pm1.getTaskDescriptor();
				String desc2 = pm2.getTaskDescriptor();
				return desc1.compareTo(desc2);
			}
		});
	}
	
	
	/**
	 * Removes (and thus resets) all group member from the global instance.
	 */
	private void removeGroupMember() {
		PerformanceMeasurements pms = PerformanceMeasurements.getInstance();
		List<PerformanceMeasurement> knownPmList = new ArrayList<>(pms.getMeasurementHashMap().values());
		for (int i = 0; i < knownPmList.size(); i++) {
			PerformanceMeasurement pm = knownPmList.get(i);
			if (this.isGroupMember(pm)==true) {
				pms.getMeasurementHashMap().remove(pm.getTaskDescriptor());
			}
		}
	}
	
	/**
	 * Collects all available group member.
	 */
	private void collectGroupMember() {
		PerformanceMeasurements pms = PerformanceMeasurements.getInstance();
		List<PerformanceMeasurement> knownPmList = new ArrayList<>(pms.getMeasurementHashMap().values());
		for (int i = 0; i < knownPmList.size(); i++) {
			this.addGroupMemberIfIsGroupMember(knownPmList.get(i), false);
		}
		this.sortPerformanceMeasurementList();
	}
	/**
	 * Adds the specified PerformanceMeasurement as group member if the corresponding check is positive.
	 * 
	 * @param pm the PerformanceMeasurement to check and possibly to add to this group
	 */
	public void addGroupMemberIfIsGroupMember(PerformanceMeasurement pm) {
		this.addGroupMemberIfIsGroupMember(pm, true);
	}
	/**
	 * Adds the specified PerformanceMeasurement as group member if the corresponding check is positive.
	 *
	 * @param pm the PerformanceMeasurement to check and possibly to add to this group
	 * @param sortList the sort list
	 */
	private void addGroupMemberIfIsGroupMember(PerformanceMeasurement pm, boolean sortList) {
		if (this.isGroupMember(pm)==true && this.getPerformanceMeasurement(pm.getTaskDescriptor())==null) {
			pm.setPerformanceGroup(this);
			this.getPerformanceMeasurementListOfGroup().add(pm);
			if (sortList==true) {
				this.sortPerformanceMeasurementList();
			}
		}
	}
	/**
	 * Checks if the specified PerformanceMeasurement belongs to this group.
	 * @param pm the PerformanceMeasurement to check
	 */
	private boolean isGroupMember(PerformanceMeasurement pm) {
		if (pm!=null) {
			String descriptor = pm.getTaskDescriptor();
			for (int i = 0; i < groupTaskDescriptorArray.length; i++) {
				String groupMemberDescriptor = groupTaskDescriptorArray[i];
				if (groupMemberDescriptor==null || groupMemberDescriptor.isEmpty()) continue;
				
				if (descriptor.equals(groupMemberDescriptor)==true || descriptor.startsWith(groupMemberDescriptor)==true) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	/**
	 * Returns the performance measurement result list.
	 * @return the performance measurement result list
	 */
	private List<PerformanceMeasurement> getPerformanceMeasurementResultList() {
		if (pMeasurementResultList==null) {
			pMeasurementResultList = new ArrayList<>();
		}
		return pMeasurementResultList;
	}
	/**
	 * Adds that a new average was calculated by the specified PerformanceMeasurement.
	 * If all measurements that belong to the current group have reported a new result, 
	 * all current states will be printed.
	 *  
	 * @param performanceMeasurement the performance measurement
	 */
	public void addNewAverageCalculated(PerformanceMeasurement performanceMeasurement) {
		
		// --- Add pm to the list that reported results -------------
		if (this.getPerformanceMeasurementResultList().contains(performanceMeasurement)==false) {
			this.getPerformanceMeasurementResultList().add(performanceMeasurement);
		}
		// --- If list complete, print group result -----------------
		if (this.getPerformanceMeasurementResultList().size()>=this.getPerformanceMeasurementListOfGroup().size()) {
			System.out.println("[" + this.getClass().getSimpleName() + "][" + this.getGroupName() + "]");
			for (int i = 0; i < this.getPerformanceMeasurementListOfGroup().size(); i++) {
				PerformanceMeasurement pm = this.getPerformanceMeasurementListOfGroup().get(i);
				System.out.println(pm.getMeasurementStatusInformation());
			}
			System.out.println();
			this.getPerformanceMeasurementResultList().clear();
		}
	}
	
	
}
