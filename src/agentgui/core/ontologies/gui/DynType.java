package agentgui.core.ontologies.gui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import agentgui.core.ontologies.OntologySingleClassSlotDescription;

public class DynType {
	
	public static final String typeClass = "class";
	public static final String typeCyclic = "cyclicType";
	public static final String typeRawType = "rawType";
	public static final String typeInnerClassType = "innerClassType";
	
	private OntologySingleClassSlotDescription oscsd = null;
	
	private String typeName = "";
	private String className = "";

	private String fieldName = "";
	private JPanel panel = null;
	private JComponent fieldDisplay = null;
	
	
	public DynType(OntologySingleClassSlotDescription curOSCSD, String typeName, String className, JPanel locatedOnPanel, String fieldOrClassName){
		this.oscsd = curOSCSD;
		this.typeName = typeName;
		this.className = className;
		this.panel = locatedOnPanel;
		if(typeName.equalsIgnoreCase(typeRawType) || typeName.equalsIgnoreCase(typeInnerClassType)) {
			this.fieldName = fieldOrClassName;
		}			
	}
	
	public DynType(OntologySingleClassSlotDescription curOSCSD, String typeName, String className, JPanel locatedOnPanel, String fieldName, JComponent fieldValue) {
		this.oscsd = curOSCSD;
		this.typeName = typeName;
		this.className = className;
		this.panel = locatedOnPanel;
		this.fieldName = fieldName;
		this.fieldDisplay = fieldValue;
	}
	
	public String toString() {
		String retVal = null;
		if (this.oscsd==null) {
			retVal = "[base] " + this.typeName + " - " + this.className + " - " + this.fieldName;
		} else {
			retVal = "[" + this.oscsd.getSlotCardinality() + "] " + this.typeName + " - " + this.className + " - " + this.fieldName;	
		}
		return retVal;
	}
	
	@Override
	public boolean equals(Object object) {
	
		if (object instanceof DynType) {
			
			DynType compare = (DynType) object;
			if (this.oscsd.equals(compare.oscsd)==false) {
				return false;
			}
			if (this.typeName.equals(compare.typeName)==false) {
				return false;
			}
			if (this.className.equals(compare.className)==false) {
				return false;
			}
			if (this.fieldName.equals(compare.fieldName)==false) {
				return false;
			}			
			return true;
			
		} else {
			return false;
		}
	}
	
	public OntologySingleClassSlotDescription getOSCSD() {
		return oscsd;
	}
	public void setOSCSD(OntologySingleClassSlotDescription curOSCSD) {
		this.oscsd = curOSCSD;
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

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	public JPanel getPanel() {
		return panel;
	}

	public JComponent getFieldDisplay() {
		return fieldDisplay;
	}
	public void setFieldDisplay(JComponent fieldValue) {
		this.fieldDisplay = fieldValue;
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
