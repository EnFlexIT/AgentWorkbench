package mas;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.TreeMap;

public class CustomClassFinder extends ClassFinder{
	protected ClassLoader cl = null;
	protected File jarFile = null;

	public CustomClassFinder(String jarFilePath) {
		super();
		if(jarFilePath != null){
			setJarFile(jarFilePath);
			refreshLocations();
		}
	}
	
	protected void setJarFile(String jarFilePath){
		jarFile=new File(jarFilePath);
		URLClassLoader clazzLoader=null;
		try{
			URL url=jarFile.toURI().toURL();
			clazzLoader=new URLClassLoader(new URL[]{url});
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		setClassLoader(clazzLoader);
	}
	
	@Override
	public final Map getClasspathLocations() {
		Map map = null;
		if(jarFile== null){
			map = super.getClasspathLocations();
		} else {
			map = new TreeMap(URL_COMPARATOR);
			include(null, jarFile, map);
		}
		return map;
	}

	@Override
	protected Class callClassForName(String classname) throws ClassNotFoundException {
		return Class.forName(classname, false, getClassLoader());
	}

	protected ClassLoader getClassLoader(){
		if(cl==null){
			cl=getClass().getClassLoader();
		}
		return cl;
	}
	
	protected void setClassLoader(ClassLoader cl){
		this.cl=cl;
	}
}