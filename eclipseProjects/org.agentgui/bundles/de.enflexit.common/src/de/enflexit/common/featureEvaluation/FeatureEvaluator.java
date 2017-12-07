package de.enflexit.common.featureEvaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.enflexit.common.featureEvaluation.featureXML.Feature;
import de.enflexit.common.featureEvaluation.featureXML.Plugin;


/**
 * The singleton class FeatureEvaluator provides help methods to check feature contents.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FeatureEvaluator {

	private boolean isDevelopmentMode = true;
	private String baseDirectoryPath = "D:\\AgentGui";
	
	private HashMap<String, Feature> featureMap;
	private HashMap<String, Plugin> pluginMap;
	
	private HashMap<String, String> pluginInFeatureMap;
	
	
	/** The singleton bundle evaluator instance. */
	private static FeatureEvaluator featureEvaluatorInstance;
	/** 
	 * Private, singleton constructor. 
	 */
	private FeatureEvaluator() {
	}
	/**
	 * Gets the single instance of BundleEvaluator.
	 * @return single instance of BundleEvaluator
	 */
	public static FeatureEvaluator getInstance() {
		if (featureEvaluatorInstance==null) {
			featureEvaluatorInstance = new FeatureEvaluator();
			featureEvaluatorInstance.updateFeatureInformation(); 
		}
		return featureEvaluatorInstance;
	}

	
	/**
	 * Update the feature information.
	 */
	public void updateFeatureInformation() {
		this.updateFeatureInformation(false);
	}
	/**
	 * Updates the feature information.
	 * @param printResults the print results
	 */
	public void updateFeatureInformation(boolean printResults) {
		
		// --- Clear all reminder -------------------------
		this.getFeatureMap().clear();
		this.getPluginMap().clear();
		this.getPluginInFeatureMap().clear();
		
		if (printResults) System.out.println("Examine installed features ...");
		List<Feature> featureList = this.getFeatureListFromFeatureDirectory(); 
		for (Feature feature : featureList) {
			// --- Remind the feature --------------------- 
			this.getFeatureMap().put(feature.getId(), feature);

			List<Plugin> pluginList = this.getFeaturePlugins(feature);
			if (printResults) System.out.println("=> Found feature " + feature.getId() + " [version " + feature.getVersion() + "] with " + pluginList.size() + " plugin");
			for (Plugin plugin : pluginList ) {
				this.getPluginMap().put(plugin.getId(), plugin);
				this.getPluginInFeatureMap().put(plugin.getId(), feature.getId());
				if (printResults) System.out.println("   " + plugin.getId() + " [version " + plugin.getVersion() + "]");
			}
			if (printResults) System.out.println();
		}
			
	}

	
	/**
	 * Returns the feature map that describes the relation between a {@link Feature} and its ID.
	 * @return the feature map
	 */
	public HashMap<String, Feature> getFeatureMap() {
		if (featureMap==null) {
			featureMap = new HashMap<>();
		}
		return featureMap;
	}
	/**
	 * Returns the plugin map that describes the relation between a {@link Plugin} and its ID..
	 * @return the plugin map
	 */
	public HashMap<String, Plugin> getPluginMap() {
		if (pluginMap==null) {
			pluginMap = new HashMap<>();
		}
		return pluginMap;
	}
	/**
	 * Gets the plugin in feature map that describes in which feature a specific plugin can be found.
	 * @return the plugin in feature map
	 */
	public HashMap<String, String> getPluginInFeatureMap() {
		if (pluginInFeatureMap==null) {
			pluginInFeatureMap = new HashMap<>();
		}
		return pluginInFeatureMap;
	}
	
	
	
	/**
	 * Gets the base directory.
	 * @return the base directory
	 */
	private File getBaseDirectory() {
		File baseDirectory = null;
		if (this.isDevelopmentMode==true && this.baseDirectoryPath!=null && this.baseDirectoryPath.isEmpty()==false) {
			baseDirectory = new File(this.baseDirectoryPath);
		} else {
			File launcherfile = new File(System.getProperty("eclipse.launcher"));
			baseDirectory = new File(launcherfile.getParentFile().getAbsolutePath());
		}
		if (baseDirectory.exists()==false) {
			System.err.println(this.getClass().getSimpleName() + ": Could not find base directory '" + baseDirectory.getAbsolutePath() + "'");
		}
		return baseDirectory;
	}
	/**
	 * Returns the features directory.
	 * @return the features directory
	 */
	private File getFeaturesDirectory() {
		File featureDirectory = new File(this.getBaseDirectory() + File.separator + "features");
		if (featureDirectory.exists()==false) {
			System.err.println(this.getClass().getSimpleName() + ": Could not find feature directory '" + featureDirectory + "'");
			return null;
		}
		return featureDirectory;
	}
	/**
	 * Returns the feature list from all feature.xml files that can be found in the feature directory.
	 * @return the {@link Feature}s found
	 */
	private ArrayList<Feature> getFeatureListFromFeatureDirectory() {
		
		ArrayList<Feature> featureList = new ArrayList<>();
		File featuresDirectory = this.getFeaturesDirectory();
		if (featuresDirectory!=null) {
			// --- Filter feature directories for the feature-ID ----
			File[] dirsFound = featuresDirectory.listFiles();
			for (int i = 0; i < dirsFound.length; i++) {
				// --- Read the feature.xml -------------------------
				String xmlFileName = dirsFound[i].getAbsolutePath() + File.separator + "feature.xml";
				Feature feature = this.loadFeatureFile(xmlFileName);
				if (feature!=null) {
					featureList.add(feature);
				}
			}
			
		}
		return featureList;
	}
	/**
	 * Loads the specified feature.xml.
	 *
	 * @param xmlFileName the xml file name
	 * @return the feature
	 */
	private Feature loadFeatureFile(String xmlFileName) {
		
		Feature feature = null;
		File xmlFile = new File(xmlFileName);
		if (xmlFile.exists()==true) {

			FileReader fileReader = null;
			try {
				fileReader = new FileReader(xmlFile);
				JAXBContext pc = JAXBContext.newInstance(Feature.class);
				Unmarshaller um = pc.createUnmarshaller();
				feature = (Feature) um.unmarshal(fileReader);

			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				return null;
			} catch (JAXBException ex) {
				ex.printStackTrace();
				return null;
			} finally {
				try {
					if (fileReader!=null) fileReader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
	
		}
		return feature;
	}
	
	/**
	 * Returns the list of bundles that belong to the specified installable unit.
	 *
	 * @param featureIU the installable unit
	 * @return the list of bundle that belong to the in
	 */
	private List<Plugin> getFeaturePlugins(Feature feature) {
		ArrayList<Plugin> pluginList = new ArrayList<>();
		if (feature!=null) {
			for (Object info : feature.getInstallHandlerOrDescriptionOrCopyrightOrLicenseOrUrlOrIncludesOrRequiresOrPluginOrData()) {
				if (info instanceof Plugin) {
					pluginList.add((Plugin) info);
				}
			}
		}
		return pluginList;
	}
	
}
