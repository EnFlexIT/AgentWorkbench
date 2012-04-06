package gasmas.transfer.zib;

public class TypeDescription {

	private String className = null;
	private String mapToComponent = null;
	private int classOccurrence = 1;

	
	public TypeDescription(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassOccurrence(int classOccurrence) {
		this.classOccurrence = classOccurrence;
	}
	public void stepClassOccurrence() {
		this.classOccurrence++;
	}
	public int getClassOccurrence() {
		return classOccurrence;
	}

	public void setMapToComponent(String mapToComponent) {
		this.mapToComponent = mapToComponent;
	}
	public String getMapToComponent() {
		return mapToComponent;
	}
	
}
