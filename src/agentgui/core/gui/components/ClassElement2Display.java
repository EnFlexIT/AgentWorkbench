package agentgui.core.gui.components;

public class ClassElement2Display {

	Class<?> currClass = null;
	
	public ClassElement2Display (Class<?> clazz){
		this.currClass=clazz;
	}
	@Override
	public String toString(){
		return currClass.getName();
	}
	public Class<?> getElementClass(){
		return currClass;
	}
}
