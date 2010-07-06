package sim.setup.gui;

import javax.swing.JTextField;

public class DynType {
	String typeName = "";
	String className = "";
	String fieldName = "";
	String innerClassType = "";
	public String getInnerClassType() {
		return innerClassType;
	}

	public void setInnerClassType(String innerClassType) {
		this.innerClassType = innerClassType;
	}

	JTextField fieldValue;
		
	public DynType(String typeName, String fieldOrClassName){
		this.typeName = typeName;
		if(typeName.equalsIgnoreCase("rawType") || typeName.equalsIgnoreCase("innerClassType"))
			this.fieldName = fieldOrClassName;
		else
			this.className = fieldOrClassName;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public DynType(String typeName, String fieldName, JTextField fieldValue) {
		this.typeName = typeName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
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
		return typeName.equalsIgnoreCase("class");
	}
	
	public boolean isInnerClass(){
		return typeName.equalsIgnoreCase("innerClass");
	}
	
	public boolean isInnerClassType(){
		return typeName.equalsIgnoreCase("innerClassType");
	}
	
	public boolean isRawType(){
		return typeName.equalsIgnoreCase("rawType");
	}
}
