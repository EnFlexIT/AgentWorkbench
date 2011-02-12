package agentgui.core.sim.setup.gui;

import javax.swing.JTextField;

public class DynType {
	
	public static final String typeClass = "class";
	public static final String typeInnerClass = "innerClass";
	public static final String typeInnerClassType = "innerClassType";
	public static final String typeRawType = "rawType";
	
	String typeName = "";
	String className = "";
	String fieldName = "";
	String innerClassType = "";

	JTextField fieldValue;
		
	public DynType(String typeName, String fieldOrClassName){
		this.typeName = typeName;
		if(typeName.equalsIgnoreCase(typeRawType) || typeName.equalsIgnoreCase(typeInnerClassType))
			this.fieldName = fieldOrClassName;
		else
			this.className = fieldOrClassName;
	}
	
	public DynType(String typeName, String fieldName, JTextField fieldValue) {
		this.typeName = typeName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getInnerClassType() {
		return innerClassType;
	}

	public void setInnerClassType(String innerClassType) {
		this.innerClassType = innerClassType;
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
	
	public boolean isInnerClass(){
		return typeName.equalsIgnoreCase(typeInnerClass);
	}
	
	public boolean isInnerClassType(){
		return typeName.equalsIgnoreCase(typeInnerClassType);
	}
	
	public boolean isRawType(){
		return typeName.equalsIgnoreCase(typeRawType);
	}
}
