package agentgui.core.common;


import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLoaderUtil {

    // Log object
   

    // Parameters
    private static final Class<? extends Object>[] parameters = new Class[]{URL.class};

    /**
     * Add file to CLASSPATH
     * @param s File name
     * @throws IOException  IOException
     */
    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }
    
    public static void removeFile(String [] files) throws RuntimeException, NoSuchFieldException, IllegalAccessException
    {
    URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    Field [] fields=null;
    Class sysclass = URLClassLoader.class;
    fields=sysclass.getDeclaredFields();
    java.lang.reflect.Field ucp = sysclass.getDeclaredField("ucp");
    ucp.setAccessible(true);
    Object sun_misc_URLClassPath = ucp.get(sysLoader);
    Class c=  sun_misc_URLClassPath.getClass();
    fields=c.getDeclaredFields();
    Field path=c.getDeclaredField("path");
    path.setAccessible(true);
          
    Field urlsField=c.getDeclaredField("urls");
    Object tmpObject=new Object();
    urlsField.setAccessible(true);
    tmpObject=path.get(sun_misc_URLClassPath);
    Object stack=urlsField.get(sun_misc_URLClassPath);
    Stack myStack= (Stack) stack;
    ArrayList list= (ArrayList) tmpObject;
    //System.out.println("Size:"+list.size());
    ArrayList<URL> urls=new ArrayList<URL>();
    for(int i=0;i<list.size();i++)
    {
       	
    	URL url=(URL) list.get(i); 
     	for(String jarFile : files)
     	{	
     		
     		jarFile=jarFile.replace("\\", "/");
     		jarFile="/"+jarFile;
     		if(url.getPath().equals(jarFile))
     		{
     			
     			urls.add(url);
     		
     		}
     	}
    }
     	
    for(URL url: urls)
    {
      list.remove(url);
      myStack.remove(url);
    }

     	
    for(int i=0;i<list.size();i++)
    {
         URL url=(URL) list.get(i); 
     	
    }
        
        path.set(sun_misc_URLClassPath, list);
        ucp.set(ClassLoader.getSystemClassLoader(),  sun_misc_URLClassPath);
      
}
    
    
    public static void removeFile(String jarFile) throws RuntimeException, NoSuchFieldException, IllegalAccessException
    {
    URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    Field [] fields=null;
    Class sysclass = URLClassLoader.class;
    fields=sysclass.getDeclaredFields();
    java.lang.reflect.Field ucp = sysclass.getDeclaredField("ucp");
    ucp.setAccessible(true);
    Object sun_misc_URLClassPath = ucp.get(sysLoader);
    Class c=  sun_misc_URLClassPath.getClass();
    fields=c.getDeclaredFields();
    Field path=c.getDeclaredField("path");
    path.setAccessible(true);
          
    Field urlsField=c.getDeclaredField("urls");
    Object tmpObject=new Object();
    urlsField.setAccessible(true);
    tmpObject=path.get(sun_misc_URLClassPath);
    Object stack=urlsField.get(sun_misc_URLClassPath);
    Stack myStack= (Stack) stack;
    ArrayList list= (ArrayList) tmpObject;
    //System.out.println("Size:"+list.size());
    ArrayList<URL> urls=new ArrayList<URL>();
    for(int i=0;i<list.size();i++)
    {
       	
    	URL url=(URL) list.get(i); 
     	     		
     		jarFile=jarFile.replace("\\", "/");
     		jarFile="/"+jarFile;
     		if(url.getPath().equals(jarFile))
     		{
     			
     			urls.add(url);
     		
     		}
     	
    }
     	
    for(URL url: urls)
    {
      list.remove(url);
      myStack.remove(url);
    }

     	
    for(int i=0;i<list.size();i++)
    {
         URL url=(URL) list.get(i); 
     	
    }
        
        path.set(sun_misc_URLClassPath, list);
        ucp.set(ClassLoader.getSystemClassLoader(),  sun_misc_URLClassPath);
      
}
        
    
        
    

    /**
     * Add file to CLASSPATH
     * @param f  File object
     * @throws IOException IOException
     */
    public static void addFile(File f) throws IOException {
        addURL(f.toURI().toURL());
    }

    /**
     * Add URL to CLASSPATH
     * @param u URL
     * @throws IOException IOException
     */
    public static void addURL(URL u) throws IOException {

        URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL urls[] = sysLoader.getURLs();
        for (int i = 0; i < urls.length; i++) {
          
        }
        Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysLoader, new Object[]{u});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }
    
    public static Properties getEnvVars() throws Throwable {
    	  Process p = null;
    	  Properties envVars = new Properties();
    	  Runtime r = Runtime.getRuntime();
    	  String OS = System.getProperty("os.name").toLowerCase();
    
    	  if (OS.indexOf("windows 9") > -1) {
    	    p = r.exec( "command.com /c set" );
    	    }
    	  else if ( (OS.indexOf("nt") > -1)
    	         || (OS.indexOf("windows 2000") > -1 )
    	         || (OS.indexOf("windows xp") > -1) 
    	         || (OS.indexOf("windows 7") >-1)) {
    	    // thanks to JuanFran for the xp fix!
    	    p = r.exec( "cmd.exe /c set" );
    	    }
    	  else {
    	    // our last hope, we assume Unix (thanks to H. Ware for the fix)
    	    p = r.exec( "env" );
    	    }
    	  BufferedReader br = new BufferedReader
    	     ( new InputStreamReader( p.getInputStream() ) );
    	  String line;
    	  while( (line = br.readLine()) != null ) {
    	   int idx = line.indexOf( '=' );
    	   String key = line.substring( 0, idx );
    	   String value = line.substring( idx+1 );
    	   
    	   envVars.setProperty( key, value );
    	   //System.out.println( key + " = " + value );
    	   }
    	  return envVars;
    	  }
    public static String adjustPathForLoadin(String selectedJar,String projectFolder,String fullProjectFolderPath)
    {
    	if(selectedJar.contains(projectFolder))
		{
			int index=selectedJar.indexOf("\\");
			int index2=selectedJar.indexOf("\\", index+1);
			System.out.println("Index:"+index);
			System.out.println("Index2:"+index2);
			selectedJar=fullProjectFolderPath+selectedJar.substring(index2+1);
		}
		return selectedJar;
    	
    	
    	
    }
    public static  Vector<AgentController> loadAgentsIntoContainer  ( Vector<String> allClasses ,  AgentContainer container) throws ClassNotFoundException, StaleProxyException
    {
    	  Vector<AgentController> controller=new Vector<AgentController>();
		     
		
		   for(String names:allClasses)
			{
			 // System.out.println("Name:"+names);
			
			   if(names.contains(".agents"))
			   {
				   if(!names.contains("$"))
				   {
				  
					  Class<? extends Object> c=Class.forName(names); //urlClassLoaderUtil.getClassByName(names);
			
				    if(c.getGenericSuperclass().toString().contains("Agent"))
				     {
				    	
				    	 controller.add(container.createNewAgent(names, names, null));
				     }
				   
				   }	
    	
			   }
			}
			 return  controller;
    	
    	
    }
    
    public  static Vector<String> getClassNamesFromJar(URL jar) throws IOException
	{
		
		Vector<String> result=new Vector<String> ();
		jar = new URL("jar:" + jar.toExternalForm() + "!/");
		JarURLConnection conn = (JarURLConnection) jar.openConnection();
		JarFile jarFile= conn.getJarFile();
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
			
			JarEntry entry=e.nextElement();
			
			if(!entry.isDirectory())
			{
				String entryName=entry.getName();
				if(entryName.contains(".class"))
				{
					entryName=entryName.replace("/",".");
					entryName=entryName.replace(".class"," ");
					entryName=entryName.trim();
				
					result.add(entryName);
				}
			}
		
		}
		jarFile.close();
		
		return result;
	}
		
    
    
    
    

}


