package agentgui.core.ontologies;

import java.util.ArrayList;

public class OntologySingleClassDescription {

	private Class<?> clazz = null;
	private String reference = null;
	private String className = null;
	private String packageName = null;
	
	public ArrayList<OntologySingleClassSlotDescription> osdArr = new ArrayList<OntologySingleClassSlotDescription>();
	
	
	/**
	 * Constructor of this class
	 */
	public OntologySingleClassDescription() {

	}
	
	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	/**
	 * @return the clazz
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @return the reference
	 */
	public String getClassReference() {
		return reference;
	}
	/**
	 * @param reference the reference to set
	 */
	public void setClassReference(String ref) {
		this.reference = ref;
		String Package = ref.substring(0, ref.lastIndexOf("."));
		String ClassName = ref.substring(ref.lastIndexOf(".") + 1, ref.length());
		this.setPackageName(Package);
		this.setClassName(ClassName);
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	

}
