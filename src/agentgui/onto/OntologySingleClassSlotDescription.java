package mas.onto;

import java.lang.reflect.Method;
import java.util.Hashtable;

public class OntologySingleClassSlotDescription {

	private String slotName = null;
	
	private String slotCardinality = null;
	private boolean slotCardinalityIsMultiple = false;
	
	private String slotVarType = null;
	private String slotOtherFacts = null;
	
	private Hashtable<String, Method> methodList = null;
	
	/**
	 * Default Constructor of this class
	 */
	public OntologySingleClassSlotDescription() {
		
	}	
	/**
	 * Constructor of this class
	 * @param currSlotName
	 * @param currSlotCardinality
	 * @param currSlotVarType
	 * @param otherFacts
	 */
	public OntologySingleClassSlotDescription(String currSlotName, String currSlotCardinality, String currSlotVarType, String otherFacts) {
		this.slotName = currSlotName;
		this.slotCardinality = currSlotCardinality;
		if (this.slotCardinality.equalsIgnoreCase("multiple")) {
			this.slotCardinalityIsMultiple = true;
		} else {
			this.slotCardinalityIsMultiple = false;
		}		
		this.slotVarType = currSlotVarType;
		this.slotOtherFacts = otherFacts;
	}

	
	/**
	 * @return the slotName
	 */
	public String getSlotName() {
		return slotName;
	}
	/**
	 * @param slotName the slotName to set
	 */
	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}

	/**
	 * @return the slotCardinality
	 */
	public String getSlotCardinality() {
		return slotCardinality;
	}
	/**
	 * @param slotCardinality the slotCardinality to set
	 */
	public void setSlotCardinality(String slotCardinality) {
		this.slotCardinality = slotCardinality;
		if (this.slotCardinality.equalsIgnoreCase("multiple")) {
			this.slotCardinalityIsMultiple = true;
		} else {
			this.slotCardinalityIsMultiple = false;
		}	
	}

	/**
	 * @return the slotCardinalityIsMultiple
	 */
	public boolean isSlotCardinalityIsMultiple() {
		return slotCardinalityIsMultiple;
	}
	/**
	 * @param slotCardinalityIsMultiple the slotCardinalityIsMultiple to set
	 */
	public void setSlotCardinalityIsMultiple(boolean slotCardinalityIsMultiple) {
		this.slotCardinalityIsMultiple = slotCardinalityIsMultiple;
		if (slotCardinalityIsMultiple==true) {
			this.slotCardinality = "multiple";
		} else {
			this.slotCardinality = "single";
		}
	}

	/**
	 * @return the slotVarType
	 */
	public String getSlotVarType() {
		return slotVarType;
	}
	/**
	 * @param slotVarType the slotVarType to set
	 */
	public void setSlotVarType(String slotVarType) {
		this.slotVarType = slotVarType;
	}

	/**
	 * @return the slotOtherFacts
	 */
	public String getSlotOtherFacts() {
		return slotOtherFacts;
	}
	/**
	 * @param slotOtherFacts the slotOtherFacts to set
	 */
	public void setSlotOtherFacts(String slotOtherFacts) {
		this.slotOtherFacts = slotOtherFacts;
	}
	
	/**
	 * @param methodList the methodList to set
	 */
	public void setSlotMethodList(Hashtable<String, Method> methodList) {
		this.methodList = methodList;
	}
	/**
	 * @return the methodList
	 */
	public Hashtable<String, Method> getSlotMethodList() {
		return methodList;
	}
	
}
