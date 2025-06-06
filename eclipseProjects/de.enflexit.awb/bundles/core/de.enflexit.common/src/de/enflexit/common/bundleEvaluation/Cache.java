package de.enflexit.common.bundleEvaluation;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;

import de.enflexit.common.classLoadService.BaseClassLoadServiceUtility;

/**
 * The Class Cache represents the persistable cache for previous search results
 * of the bundle evaluation process. The aim of this cache is to accelerate the overall search 
 * and reduce the system usage during the start of Agent.Workbench. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
@XmlRootElement(name = "BundleEvaluationCache")
@XmlType(name = "Cache", propOrder = {
    "bundleResultList"
})
public class Cache {

	private static final String FILE_ENCODING = "UTF-8";
	
	@XmlElementWrapper(name = "BundleResultList")
	@XmlElement(name = "BundleResult")
	private ArrayList<CacheBundleResult> bundleResultList; 
	
	private transient TreeMap<String, CacheBundleResult> bundleResults;
	
	
	/**
	 * Returns the bundle result list.
	 * @return the bundle result list
	 */
	@XmlTransient
	protected ArrayList<CacheBundleResult> getBundleResultList() {
		return bundleResultList;
	}
	/**
	 * Sets the bundle result list.
	 * @param bundleResultList the new bundle result list
	 */
	protected void setBundleResultList(ArrayList<CacheBundleResult> bundleResultList) {
		this.bundleResultList = bundleResultList;
	}
	
	
	/**
	 * Returns the cache tree map.
	 * @return the cache tree map
	 */
	private TreeMap<String, CacheBundleResult> getBundleResultTreeMap() {
		if (bundleResults==null) {
			bundleResults = new TreeMap<>();
		}
		return bundleResults;
	}
	/**
	 * Returns the available CacheBundleResult for the specified bundle.
	 *
	 * @param symbolicBundleName the symbolic bundle name of the bundle
	 * @return the bundle description
	 */
	public CacheBundleResult getBundleResult(String symbolicBundleName) {
		return this.getBundleResultTreeMap().get(symbolicBundleName);
	}
	
	/**
	 * Gets the or creates a bundle description for the specified bundle.
	 *
	 * @param bundle the bundle
	 * @return the or create bundle description
	 */
	public CacheBundleResult createBundleResult(Bundle bundle) {
		return this.createBundleResult(bundle.getSymbolicName(), bundle.getVersion(), this.getLastModified(bundle));
	}
	/**
	 * Return the time stamp of the last modification of the current bundle. If we are in 
	 * a development environment, were the bundle is available as folder structure, the file 
	 * with the latest time stamp will be used to determine the last modification date.
	 *
	 * @param bundle the bundle
	 * @return the time stamp last modified
	 */
	private long getLastModified(Bundle bundle) {
		
		long lastModifiedBundle = bundle.getLastModified();
		long lastModifiedDetail = lastModifiedBundle;
		
		String bLocation = bundle.getLocation();
		if (bLocation.equals("System Bundle")==true || bLocation.startsWith("System Bundle")) return lastModifiedBundle;
		if (bLocation.startsWith("initial@reference:file:")) return lastModifiedBundle;
		
		try {
			
			// --- Get the file object of the bundle ----------------
			File bLocationFile = null;
			if (bLocation.startsWith("reference:file:")) {
				bLocation = bLocation.substring(("reference:file:".length()), bLocation.length());
				bLocationFile = new File(bLocation);
			
			} else {
				URL bundleLocationURL = new URI(bundle.getLocation()).toURL();
				bLocationFile = new File(bundleLocationURL.toURI());	
			}
			
			// --- Update the last modified date --------------------
			if (bLocationFile!=null && bLocationFile.exists()==true && bLocationFile.isDirectory()==true) {
				lastModifiedDetail = this.getLastModifiedOfClasses(bLocationFile);
			}
			
		} catch (MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		}

		// --- Do we have a different last modified date? -----------
		if (lastModifiedDetail!=lastModifiedBundle) {
			boolean debugDateChange=false;
			if (debugDateChange==true) {
				SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				System.out.println("[" + sdf.format(new Date(lastModifiedBundle)) + " => " + sdf.format(new Date(lastModifiedDetail)) + "] for '" + bundle.getSymbolicName() + "'");
			}
			lastModifiedBundle = lastModifiedDetail;
		}
		return lastModifiedBundle;
	}
	/**
	 * Return the last modified date from the specified file instance were only 
	 * *.class- and *.java-files will be considered in the search.
	 *
	 * @param fileOrDirectory the file or directory
	 * @return the last modified from directory
	 */
	private long getLastModifiedOfClasses(File fileOrDirectory) {
		
		if (fileOrDirectory==null) return -1;
		if (fileOrDirectory.isDirectory()==false) return fileOrDirectory.lastModified();
		
		long methodLastModified = 0;
		
		// --- Define file filter -------------------------
		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File fileToAccept) {
				// --- For directories --------------------
				if (fileToAccept.isDirectory()) return true;
				// --- For files --------------------------
				String fileName = fileToAccept.getName(); 
				String extension = null;
				int i = fileName.lastIndexOf('.');
				if (i > 0) {
				    extension = fileName.substring(i+1);
				}
				if (extension!=null && (extension.equals("java") || extension.equals("class"))) {
					return true;
				}
				return false;
			}
		};
		
		// --- List files ---------------------------------
		File[] fileList = fileOrDirectory.listFiles(fileFilter);
		for (int i = 0; i < fileList.length; i++) {
			// --- Consider file object -------------------
			File file = fileList[i];
			long fileLastModified = file.lastModified();
			if (fileLastModified > methodLastModified) methodLastModified = fileLastModified;
			// --- Consider directory? --------------------
			if (file.isDirectory()==true) {
				fileLastModified = this.getLastModifiedOfClasses(file);
				if (fileLastModified > methodLastModified) methodLastModified = fileLastModified;
			}
		}
		return methodLastModified;
	}
	
	
	/**
	 * Gets the or creates a bundle description.
	 *
	 * @param symbolicBundleName the symbolic bundle name
	 * @param version the version
	 * @param lastModified the last modified
	 * @return the or create bundle description
	 */
	public CacheBundleResult createBundleResult(String symbolicBundleName, Version version, long lastModified) {
		return new CacheBundleResult(symbolicBundleName, version.toString(), lastModified);
	}
	
	/**
	 * Gets, creates or re-creates a CacheBundleResult that explicitly corresponds to the specified bundle.
	 * If a new CacheBundleResult is required, the cache will be updated.
	 *
	 * @param bundle the bundle
	 * @return the or re create bundle description
	 */
	public CacheBundleResult getOrReCreateBundleResult(Bundle bundle) {
		if (bundle==null) return null;
		CacheBundleResult bd = this.getBundleResult(bundle.getSymbolicName());
		CacheBundleResult bdTemp = this.createBundleResult(bundle);
		if (bdTemp.equals(bd)==false) {
			bd = bdTemp;
			this.getBundleResultTreeMap().put(bundle.getSymbolicName(), bd);
		}
		return bd;
	}
	
	/**
	 * Based on the local information, updates the result of the specified bundle class filter.
	 * @param bundleClassFilter the bundle class filter
	 */
	public void updateClassFilterResult(AbstractBundleClassFilter bundleClassFilter) {
		
		if (bundleClassFilter==null) return;
		
		// --- Get the currently installed bundles ------------------ 
		Bundle[] bundleArray = BundleEvaluator.getInstance().getBundles();
		
		// --- Get the current state of the Cache as list -----------
		List<CacheBundleResult> cacheBundleResultList = new ArrayList<>(this.getBundleResultTreeMap().values());
		for (int i = 0; i < cacheBundleResultList.size(); i++) {
			
			// --- Get the single CacheBundleResult ----------------- 
			CacheBundleResult bundleResult = cacheBundleResultList.get(i);
			// --- Check if the bundle is at least installed --------
			if (this.getBundle(bundleArray,  bundleResult.getSymbolicBundleName())!=null) {

				// --- Get the specific filter result ---------------
				CacheClassFilterResult filterResult = bundleResult.getClassFilterResult(bundleClassFilter.getFilterScope());
				if (filterResult!=null) {
					// --- Get list of filtered classes -------------
					List<String> filterResultClassList = filterResult.getFilteredClassesNotNull();
					for (int j = 0; j < filterResultClassList.size(); j++) {
				
						// --- Check if the class can be resolved ---
						try {
							String classNameFound = filterResultClassList.get(j);
							Class<?> classFound = BaseClassLoadServiceUtility.forName(classNameFound);
							if (classFound!=null) {
								bundleClassFilter.addClassFound(classNameFound, bundleResult.getSymbolicBundleName());
							}
							
						} catch (ClassNotFoundException | NoClassDefFoundError ex) {
							// ex.printStackTrace();
						}
					}
				}
			}
		}
	}
	/**
	 * Returns the bundle with the specified bundle name or <code>null</code>.
	 *
	 * @param bundleArray the bundle array to check
	 * @param symbolicBundleName the symbolic bundle name
	 * @return the bundle
	 */
	private Bundle getBundle(Bundle[] bundleArray, String symbolicBundleName) {
		if (bundleArray!=null) {
			for (int i = 0; i < bundleArray.length; i++) {
				if (bundleArray[i].getSymbolicName().equals(symbolicBundleName)) {
					return bundleArray[i];
				}
			}
		}
		return null;
	}
	
	
	// ----------------------------------------------------------------------------------
	// --- From here, methods for saving and loading the cache can be found -------------
	// ----------------------------------------------------------------------------------
	/**
	 * Returns the cache file instance.
	 * @return the cache file
	 */
	private File getCacheFile() {
		
		// --- Get the applications configuration directory ---------
		File configDirectory = null;
		// --- Catch possible NullPointer in ConfigurationScope -----
		try {
			IScopeContext scopeContext = ConfigurationScope.INSTANCE;
			configDirectory = scopeContext.getLocation().toFile();
		} catch (Exception ex) { }
		
		if (configDirectory==null) return null;
		
		// --- Return the cache file -------------------------------- 
		Bundle myBundle = FrameworkUtil.getBundle(this.getClass());
		String cacheFilePath = configDirectory.getAbsolutePath() + File.separator + myBundle.getSymbolicName() + File.separator + "BundleEvaluationCache.xml";
		return new File(cacheFilePath); 
	}
	
	/**
	 * Loads the cached bundle evaluation results and returns the current instance of this Cache.
	 * @return the loaded instance of the cache or <code>null</code>, if the load process failed 
	 */
	public Cache load() {

		File cacheFile = this.getCacheFile();
		if (cacheFile==null || cacheFile.exists()==false) return this;
		
		Cache  cache = null;
		InputStream inputStream = null;
		InputStreamReader isReader = null;
		try {
			
			JAXBContext context = JAXBContext.newInstance(Cache.class);
			Unmarshaller unMarsh = context.createUnmarshaller();
			
			inputStream = new FileInputStream(cacheFile);
			isReader  = new InputStreamReader(inputStream, FILE_ENCODING);
			
			Object jaxbObject = unMarsh.unmarshal(isReader);
			if (jaxbObject!=null && jaxbObject instanceof Cache) {
				cache = (Cache)jaxbObject;
				// --- Fill the local model -----
				if (cache!=null) {
					for (int i = 0; i < cache.getBundleResultList().size(); i++) {
						CacheBundleResult bundleResult = cache.getBundleResultList().get(i);
						this.getBundleResultTreeMap().put(bundleResult.getSymbolicBundleName(), bundleResult);
					}
				}
			}
			
		} catch (Exception ex) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Error while loading cache from XML file:");
			ex.printStackTrace();
		} finally {
			try {
				if (isReader!=null) isReader.close();
				if (inputStream!=null) inputStream.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}	
		}
		return this;
	}

	/**
	 * Saves the cached bundle evaluation results.
	 */
	public synchronized void save() {

		FileWriter fileWriter = null;
		try {
			
			// --- Check if the directory exists ----------
			File cacheFile = this.getCacheFile();
			if (cacheFile==null) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Could not detect cache file description. Cache save action will be canceled.");
				return;
			}
			File cacheDir  = cacheFile.getParentFile();
			if (cacheDir.exists()==false) {
				boolean created = cacheFile.getParentFile().mkdirs();
				if (created==false) {
					System.err.println("[" + this.getClass().getSimpleName() + "] The directory for the cache could not be created (" + cacheDir.getAbsolutePath() + ").");
					return;
				}
			}
			
			// --- Define the JAXB context ----------------
			JAXBContext pc = JAXBContext.newInstance(Cache.class);
			Marshaller pm = pc.createMarshaller();
			pm.setProperty(Marshaller.JAXB_ENCODING, FILE_ENCODING);
			pm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// --- Temporary set the ArrayList ------------
			this.setFilteredClassesToNullIfEmpty();
			this.setBundleResultList(new ArrayList<>(this.getBundleResultTreeMap().values()));
			// --- Write to file ---------------------------
			fileWriter = new FileWriter(cacheFile);
			pm.marshal(this, fileWriter);
			// --- Clear the temporary the ArrayList ------- 
			this.setBundleResultList(null);
			
		} catch (Exception ex) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Error while saving cache as XML file:");
			ex.printStackTrace();
		} finally {
			try {
				if (fileWriter!=null) fileWriter.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}				
		}
	}

	/**
	 * Sets the filtered classes to null if they are empty.
	 */
	private void setFilteredClassesToNullIfEmpty() {
		ArrayList<CacheBundleResult> bundleResultList = new ArrayList<>(this.getBundleResultTreeMap().values());
		for (int i = 0; i < bundleResultList.size(); i++) {
			bundleResultList.get(i).setFilteredClassesToNullIfEmpty();
		}
	}
	
}
