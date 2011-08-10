package agentgui.core.gui.components;

public class ClassElement2Display {

	private Class<?> currClass = null;
	private String additionalText = null;
	
	/**
	 * @param clazz
	 */
	public ClassElement2Display (Class<?> clazz){
		this.currClass=clazz;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		if (this.additionalText!=null) {
			return currClass.getName() + " (" + this.additionalText + ")";
		} else {
			return currClass.getName();	
		}
	}
	/**
	 * @return
	 */
	public Class<?> getElementClass(){
		return currClass;
	}
	
	/**
	 * @param additionalText the additionalText to set
	 */
	public void setAdditionalText(String additionalText) {
		this.additionalText = additionalText;
	}
	/**
	 * @return the additionalText
	 */
	public String getAdditionalText() {
		return additionalText;
	}
}
