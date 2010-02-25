package mas.onto;

public class OntologyClassTreeObject extends Object {

	public Class<?> OntoClass = null;
	
	public OntologyClassTreeObject (Class<?> Clazz) {
		OntoClass = Clazz;
	}
	public String toString() {
		return getClassTextSimple( OntoClass.getName() );		
	}
	private String getClassTextSimple( String Reference ) {
		return Reference.substring( Reference.lastIndexOf(".")+1 );
	}
	public Class<?> getOntoClass() {
		return OntoClass;
	}
}
