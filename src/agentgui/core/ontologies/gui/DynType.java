package agentgui.core.ontologies.gui;

import javax.swing.JTextField;

import agentgui.core.ontologies.OntologySingleClassSlotDescription;

public class DynType {
	
	public static final String typeClass = "class";
	public static final String typeCyclic = "cyclicType";
	public static final String typeRawType = "rawType";
	public static final String typeInnerClassType = "innerClassType";
	
	OntologySingleClassSlotDescription oscsd = null;
	
	String typeName = "";
	String className = "";

	String fieldName = "";
	JTextField fieldValue;
		
	public DynType(OntologySingleClassSlotDescription curOSCSD, String typeName, String className, String fieldOrClassName){
		this.oscsd = curOSCSD;
		this.typeName = typeName;
		this.className = className;
		if(typeName.equalsIgnoreCase(typeRawType) || typeName.equalsIgnoreCase(typeInnerClassType)) {
			this.fieldName = fieldOrClassName;
		}			
	}
	
	public DynType(OntologySingleClassSlotDescription curOSCSD, String typeName, String className, String fieldName, JTextField fieldValue) {
		this.oscsd = curOSCSD;
		this.typeName = typeName;
		this.className = className;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public String toString() {
		String retVal = this.typeName + " - " + this.className + " - " + this.fieldName;
		return retVal;
	}
	
	public OntologySingleClassSlotDescription getOSCD() {
		return oscsd;
	}
	public void setOscd(OntologySingleClassSlotDescription curOSCD) {
		this.oscsd = curOSCD;
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public JTextField getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(JTextField fieldValue) {
		this.fieldValue = fieldValue;
	}

	public boolean isClass(){
		return typeName.equalsIgnoreCase(typeClass);
	}
	public boolean isInnerClassType(){
		return typeName.equalsIgnoreCase(typeInnerClassType);
	}
	public boolean isRawType(){
		return typeName.equalsIgnoreCase(typeRawType);
	}
}
