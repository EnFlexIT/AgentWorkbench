package de.enflexit.common.fileConfiguration;

import java.util.HashMap;
import java.util.List;

import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.common.ServiceFinder;

/**
 * The Class FileConfigurationServiceManager.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileConfigurationServiceManager {

	static Logger LOGGER = LoggerFactory.getLogger(FileConfigurationServiceManager.class);

	private static FileConfigurationServiceManager instance;
	/**
	 * Instantiates a new file configuration service manager.
	 */
	private FileConfigurationServiceManager() {}
	
	/**
	 * Returns the single instance of FileConfigurationServiceManager.
	 * @return single instance of FileConfigurationServiceManager
	 */
	public static FileConfigurationServiceManager getInstance() {
		if (instance == null) {
			instance = new FileConfigurationServiceManager();
			instance.getPerformativeServiceHashMap();
			instance.startFileConfigurationServiceTracker();
		}
		return instance;
	}
	

	private static FileConfigurationServiceTracker fcsTracker;
	private HashMap<String, FileConfigurationService> performativeServiceHashMap;
	
	/**
	 * Returns the performative service hash map.
	 * 
	 * @return the performative service hash map
	 */
	private synchronized HashMap<String, FileConfigurationService> getPerformativeServiceHashMap() {
		if (performativeServiceHashMap == null) {
			performativeServiceHashMap = new HashMap<String, FileConfigurationService>();
			this.updatePerformativeServiceHashMap();
		}
		return performativeServiceHashMap;
	}
	
	/**
	 * Adds all currently registered FileConfiguration services to
	 * the performativeServiceHashMap.
	 */
	private void updatePerformativeServiceHashMap() {
		List<FileConfigurationService> serviceList = ServiceFinder.findServices(FileConfigurationService.class);
		serviceList.forEach(fcs -> this.addFileConfigurationService(fcs));
	}
	
	/**
	 * Adds the file configuration service to the performativeServiceHashmap
	 * @param service2Add the service to add
	 */
	public void addFileConfigurationService(FileConfigurationService service2Add) {
		
		if (service2Add == null) return;
		this.getPerformativeServiceHashMap().put(service2Add.getPerformativeNotNull(), service2Add);
	}
	
	public void removeFileConfigurationService(FileConfigurationService service2Remove) {
		if (service2Remove == null) return;
		this.getPerformativeServiceHashMap().remove(service2Remove.getPerformative());
	}
	
	/**
	 * Start file configuration service tracker.
	 */
	private  void startFileConfigurationServiceTracker() {
		if (fcsTracker == null)	{
			fcsTracker = new FileConfigurationServiceTracker(FrameworkUtil.getBundle(this.getClass()).getBundleContext());
			fcsTracker.open();
		}
	}
	
	/**
	 * Searches for a known service with corresponding performative and 
	 * returns the result of the services processing operation. 
	 *
	 * @param performative the performative
	 * @param file2Process the file 2 process
	 * @return the result of the processing operation
	 */
	public FileProcessingResult processFile(String performative, UploadedFile file2Process) {
		
		
		// --- Look for a corresponding service -----------------------------------------
		FileConfigurationService fcs = this.getFileConfigurationService(performative);
		FileProcessingResult fileProcessingResult = new FileProcessingResult();
		if (fcs == null) {
			fileProcessingResult.setMessage("No service was found for the performative " + performative);
			return fileProcessingResult;
		}
		// --- Process the file and return the result -----------------------------------
		fileProcessingResult = fcs.processFile(file2Process);
		return fileProcessingResult;
	}
	
	/**
	 * Returns the current configuration file for the specified performative
	 *
	 * @param performative the performative to identify the service
	 * @return the current configuration file
	 */
	public FileDownload getCurrentConfigurationFile(String performative) {
		
		// --- Look for a corresponding service -----------------------------------------
		FileConfigurationService fcs = this.getFileConfigurationService(performative);
		if (fcs != null) {
			return fcs.getCurrentConfigurationFile();

		}
		return null;
	}
	
	/**
	 * Returns the file configuration service identified by the specified performative.
	 *
	 * @param performative the performative
	 * @return the file configuration service
	 */
	public FileConfigurationService getFileConfigurationService(String performative) {
		
		FileConfigurationService fcs = this.getPerformativeServiceHashMap().get(performative.trim().toLowerCase());
		if (fcs == null) {
			LOGGER.error("No service was found for the performative " + performative);
			return null;
		}
		return fcs;
	}
	
}