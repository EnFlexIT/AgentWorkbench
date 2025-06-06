package de.enflexit.awb.bgSystem.classLoadService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import de.enflexit.common.classLoadService.BaseClassLoadService;
import jade.content.onto.Ontology;

/**
 * The Class BaseClassLoadServiceImpl represents the .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BaseClassLoadServiceImpl implements BaseClassLoadService {

	/* (non-Javadoc)
	 * @see energy.classLoadService.ClassLoadService#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException, NoClassDefFoundError {
		return Class.forName(className);
	}
	
	/* (non-Javadoc)
	 * @see energy.classLoadService.ClassLoadService#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoClassDefFoundError {
		//https://stackoverflow.com/questions/46393863/what-to-use-instead-of-class-newinstance
		return this.forName(className).getDeclaredConstructor().newInstance();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.BaseClassLoadService#getOntologyInstance(java.lang.String)
	 */
	@Override
	public Ontology getOntologyInstance(String ontologyClassName) throws ClassNotFoundException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		Class<?> currOntoClass = Class.forName(ontologyClassName);
		Method method = currOntoClass.getMethod("getInstance", new Class[0]);
		return (Ontology) method.invoke(currOntoClass, new Object[0]);
	}

}
