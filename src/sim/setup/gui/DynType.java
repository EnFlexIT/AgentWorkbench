package sim.setup.gui;

public class DynType {
	String typeName = "";
	String className = "";
	String fieldName = "";
	String fieldValue = "";
		
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

	public DynType(String typeName, String fieldName, String fieldValue) {
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

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
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
