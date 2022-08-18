package de.enflexit.common.featureEvaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;

import de.enflexit.common.featureEvaluation.featureXML.Feature;
import de.enflexit.common.featureEvaluation.featureXML.Import;
import de.enflexit.common.featureEvaluation.featureXML.Includes;
import de.enflexit.common.featureEvaluation.featureXML.Plugin;
import de.enflexit.common.featureEvaluation.featureXML.Requires;


/**
 * The singleton class FeatureEvaluator provides help methods to check feature contents.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class FeatureEvaluator {

	private enum ElementType {
		Bundle,
		Feature
	}
	
	private boolean isDevelopmentMode = false;
	private String baseDirectoryPath = null;
	
	private HashMap<String, Feature> featureMap;
	private HashMap<String, Plugin> pluginMap;
	
	private HashMap<String, ArrayList<String>> featureToPluginListMap;
	private HashMap<String, ArrayList<String>> pluginToFeatureListMap;
	private HashMap<String, ArrayList<String>> includedFeaturesForFeatureMap;
	private HashMap<String, ArrayList<String>> importedFeaturesForFeatureMap;
	
	private String productFeatureID;
	private String productBundleID;
	private ArrayList<String> productFeatures;
	
	private ArrayList<String> errMessages;
	
	
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
		}
		return featureEvaluatorInstance;
	}
	
	/**
	 * Update the feature information in an own thread.
	 */
	public void evaluateFeatureInformationInThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				evaluateFeatureInformation();
			}
		}).start();
	}
	/**
	 * Update the feature information.
	 */
	public void evaluateFeatureInformation() {
		
		// --- Clear all reminder ---------------------------------------------
		this.getFeatureMap().clear();
		this.getPluginMap().clear();
		this.getFeatureToPluginListMap().clear();
		this.getPluginToFeatureListMap().clear();
		
		String symbolicBundleNameProduct = this.getProductBundleID();
		
		// --- Evaluate the feature directory ---------------------------------
		List<Feature> featureList = this.getFeatureListFromFeatureDirectory(); 
		for (Feature feature : featureList) {
			// --- Remind the feature ----------------------------------------- 
			this.getFeatureMap().put(feature.getId(), feature);

			// --- Get included features for that feature -
			ArrayList<String> includedFeatures = this.getIncludedFeatures(feature);
			this.getIncludedFeaturesForFeatureMap().put(feature.getId(), includedFeatures);
			
			// --- Get included features for that feature ---------------------
			ArrayList<String> importedFeatures = this.getRequiredImportedFeatures(feature);
			this.getImportedFeaturesForFeatureMap().put(feature.getId(), importedFeatures);
			
			// --- Store further cross information ----------------------------
			ArrayList<String> pluginNameList = new ArrayList<>();
			List<Plugin> pluginList = this.getFeaturePlugins(feature);
			for (Plugin plugin : pluginList ) {
				pluginNameList.add(plugin.getId());
				this.getPluginMap().put(plugin.getId(), plugin);
				// --- Did we find the product bundle? ------------------------
				if (symbolicBundleNameProduct!=null && plugin.getId().equals(symbolicBundleNameProduct)==true) {
					this.setProductFeatureID(feature.getId());
				}
				
				// --- Do we have already a feature for this bundle? ---------- 
				ArrayList<String> featureIDs = this.getPluginToFeatureListMap().get(plugin.getId()); 
				if (featureIDs==null) {
					featureIDs = new ArrayList<>();
					featureIDs.add(feature.getId());
					this.getPluginToFeatureListMap().put(plugin.getId(), featureIDs);
				} else {
					if (featureIDs.contains(feature.getId())==false) {
						featureIDs.add(feature.getId());
					}
				}
				
			} // end for
			// --- Put plugins of current feature in the reminder -------------
			this.getFeatureToPluginListMap().put(feature.getId(), pluginNameList);
		}
		
		if (this.isDevelopmentMode==true) {
			this.printFeatureInformation();
		}
	}
	
	/**
	 * Returns the error messages.
	 * @return the error messages
	 */
	private ArrayList<String> getErrorMessages() {
		if (errMessages==null) {
			errMessages = new ArrayList<>();
		}
		return errMessages;
	}
	/**
	 * Prints the error messages.
	 */
	private void printErrorMessages() {
		
		if (this.getErrorMessages().size()==0) return;

		System.err.println();
		System.err.println("=> Found " + this.getErrorMessages().size() + " error messages:");
		System.err.println();
		for (String error : this.getErrorMessages()) {
			System.err.println(error);
		}
	}
	
	/**
	 * Prints the available feature information.
	 */
	public void printFeatureInformation() {
		
		ArrayList<String> featureIDs = new ArrayList<>( this.getFeatureMap().keySet());
		Collections.sort(featureIDs);
		
		// --- Print product information ----------------------------
		String productID = Platform.getProduct().getId();
		String productName = Platform.getProduct().getName();
		String productFeature = this.getProductFeatureID();
		String productBundle = this.getProductBundleID();
		int nFeatures = this.getFeatureMap().size(); 
		int nBundles = this.getPluginMap().size();
		
		System.out.println("Product '" + productName + "' (" + productID + ")");
		System.out.println("Defined by feature '" + productFeature + "' and bundle '" + productBundle + "'" );
		System.out.println();
		System.out.println("No. of defined feature: " + nFeatures);
		System.out.println("No. of defined plugin/bundles: " + nBundles);
		System.out.println();
		System.out.println("The character '#' marks elements that belong to the base installation of the product!");
		System.out.println();
		
		// --- Print feature and plugin information -----------------
		for (int i = 0; i < featureIDs.size(); i++) {
			
			String featureName = featureIDs.get(i);
			Feature feature = this.getFeatureMap().get(featureName);
			List<String> pluginList = this.getFeatureToPluginListMap().get(featureName);
			String baseMarkerFeature = this.getMarkerForBaseInstallationElement(feature.getId(), ElementType.Feature);
			System.out.println("=> " + baseMarkerFeature + "Found feature " + feature.getId() + " [version " + feature.getVersion() + "] with " + pluginList.size() + " plugin");
			
			List<String> includeList = this.getIncludedFeaturesForFeatureMap().get(featureName);
			if (includeList.size()==0) {
				System.out.println("   No includes features!");
			} else {
				System.out.println("   Included features:");
				for (String include : includeList) {
					System.out.println("   - " + this.getMarkerForBaseInstallationElement(include, ElementType.Feature) + include);
				}
			}
			
			List<String> importedList = this.getImportedFeaturesForFeatureMap().get(featureName);
			if (importedList.size()==0) {
				System.out.println("   No imported features!");
			} else {
				System.out.println("   Imported features:");
				for (String imported : importedList) {
					System.out.println("   - " + this.getMarkerForBaseInstallationElement(imported, ElementType.Feature) + imported);
				}
			}
			
			System.out.println("   Bundles:");
			for (String pluginName : pluginList ) {
				String marker4PartOfProductFeature = "";
				if (this.isBundleOfProductFeature(pluginName)) marker4PartOfProductFeature =" (Product part)";
				if (pluginName.equals(this.getProductBundleID())) marker4PartOfProductFeature =" (# Product defining bundle)";
				String baseMarkerBundle = this.getMarkerForBaseInstallationElement(feature.getId(), ElementType.Feature);
				
				Plugin plugin = this.getPluginMap().get(pluginName);
				System.out.println("   - " + baseMarkerBundle + plugin.getId() + " [version " + plugin.getVersion() + "]" + marker4PartOfProductFeature);
			}
			System.out.println();
		}
		
		// --- Print error messages, if any -------------------------
		this.printErrorMessages();
	}
	/**
	 * Return a marker for product included element.
	 *
	 * @param elementID the element ID
	 * @param type the type
	 * @return the marker for product included element
	 */
	private String getMarkerForBaseInstallationElement(String elementID, ElementType type) {
		
		String marker = "";
		boolean mark = false;
		switch (type) {
		case Bundle:
			mark = this.isBundleOfBaseInstallation(elementID);
			break;
		case Feature:
			mark = this.isFeatureOfBaseInstallation(elementID);
			break;
		}
		if (mark) marker = "# ";
		return marker;
	}
	
	/**
	 * Gets the base directory.
	 * @return the base directory
	 */
	private File getBaseDirectory() {
		File baseDirectory = null;
		if (this.isDevelopmentMode==true && this.baseDirectoryPath!=null && this.baseDirectoryPath.isEmpty()==false) {
			// --- This is just a development and debug case --------
			baseDirectory = new File(this.baseDirectoryPath);
		} else {
			// --- The regular case --------------------------------- 
			File installDir = null;
			try {
				installDir = new File(Platform.getInstallLocation().getURL().toURI());
			} catch (URISyntaxException uriEx) {
				//uriEx.printStackTrace();
				installDir = new File(Platform.getInstallLocation().getURL().getPath());
			}
			
			// --- An older backup solution -------------------------
			if (installDir!=null && installDir.exists()==false) {
				installDir = new File(System.getProperty("eclipse.launcher")).getParentFile();
			}
			baseDirectory = installDir;	
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
	 * Returns the list of bundles that belong to the specified {@link Feature}.
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
	/**
	 * Returns the list of features that belong to the specified {@link Feature}.
	 *
	 * @param featureIU the installable unit
	 * @return the list of bundle that belong to the in
	 */
	private ArrayList<String> getIncludedFeatures(Feature feature) {
		ArrayList<String> includeList = new ArrayList<>();
		if (feature!=null) {
			for (Object info : feature.getInstallHandlerOrDescriptionOrCopyrightOrLicenseOrUrlOrIncludesOrRequiresOrPluginOrData()) {
				if (info instanceof Includes) {
					Includes include = (Includes) info;
					includeList.add(include.getId());
				}
			}
		}
		return includeList;
	}
	/**
	 * Return the required features for the specified feature.
	 *
	 * @param feature the feature
	 * @return the required features
	 */
	private ArrayList<String> getRequiredImportedFeatures(Feature feature) {
		ArrayList<String> requiresList = new ArrayList<>();
		if (feature!=null) {
			for (Object info : feature.getInstallHandlerOrDescriptionOrCopyrightOrLicenseOrUrlOrIncludesOrRequiresOrPluginOrData()) {
				if (info instanceof Requires) {
					Requires requires = (Requires) info;
					for (Import imported : requires.getImport()) {
						if (imported.getFeature()!=null) {
							requiresList.add(imported.getFeature());
						}
					}
				}
			}
		}
		return requiresList;
	}
	
	/**
	 * Returns the feature map that describes the relation between a {@link Feature} and its ID.
	 * @return the feature map
	 */
	private HashMap<String, Feature> getFeatureMap() {
		if (featureMap==null) {
			featureMap = new HashMap<>();
		}
		return featureMap;
	}
	/**
	 * Returns the plugin map that describes the relation between a {@link Plugin} and its ID..
	 * @return the plugin map
	 */
	private HashMap<String, Plugin> getPluginMap() {
		if (pluginMap==null) {
			pluginMap = new HashMap<>();
		}
		return pluginMap;
	}
	/**
	 * Returns the feature plugins map that stores a list of bundle IDs for each feature.
	 * @return the feature plugins map
	 */
	private HashMap<String, ArrayList<String>> getFeatureToPluginListMap() {
		if (featureToPluginListMap==null) {
			featureToPluginListMap = new HashMap<>();
		}
		return featureToPluginListMap;
	}
	/**
	 * Returns the plugin in feature map that describes in which features a specific plugin can be found.
	 * @return the plugin in feature map
	 */
	private HashMap<String, ArrayList<String>> getPluginToFeatureListMap() {
		if (pluginToFeatureListMap==null) {
			pluginToFeatureListMap = new HashMap<>();
		}
		return pluginToFeatureListMap;
	}
	/**
	 * Returns the included features for feature map that stores the included features for a feature.
	 * @return the included features for feature map
	 */
	private HashMap<String, ArrayList<String>> getIncludedFeaturesForFeatureMap() {
		if (includedFeaturesForFeatureMap==null) {
			includedFeaturesForFeatureMap = new HashMap<>();
		}
		return includedFeaturesForFeatureMap;
	}
	/**
	 * Returns the imported features for feature map that stores the imported features for a feature.
	 * @return the imported features for feature map
	 */
	private HashMap<String, ArrayList<String>> getImportedFeaturesForFeatureMap() {
		if (importedFeaturesForFeatureMap==null) {
			importedFeaturesForFeatureMap = new HashMap<>();
		}
		return importedFeaturesForFeatureMap;
	}
	/**
	 * Returns the product feature ID.
	 * @return the product feature ID
	 */
	public String getProductFeatureID() {
		if (productFeatureID==null) {
			ArrayList<String> featuresFound = this.getPluginToFeatureListMap().get(this.getProductBundleID());
			if (featuresFound==null || featuresFound.size()==0) {
				productFeatureID = "# UNKNOWN #";
			} else  if (featuresFound.size()==1) {
				productFeatureID = featuresFound.get(0);
			} else {
				productFeatureID = "# UNKNOWN #";
			}
		}
		return productFeatureID;
	}
	/**
	 * Sets the product feature ID.
	 * @param productFeatureID the new product feature ID
	 */
	private void setProductFeatureID(String productFeatureID) {
		this.productFeatureID = productFeatureID;
	}
	/**
	 * Gets the product bundle ID.
	 * @return the product bundle ID
	 */
	public String getProductBundleID() {
		if (productBundleID==null) {
			if (Platform.getProduct()!=null && Platform.getProduct().getDefiningBundle()!=null) {
				productBundleID =  Platform.getProduct().getDefiningBundle().getSymbolicName();
			}
		}
		return productBundleID;
	}
	/**
	 * Returns the list of features that are provided by the product.
	 * @return the product features
	 */
	public ArrayList<String> getProductFeatures() {
		if (productFeatures==null) {
			productFeatures = new ArrayList<>();
			productFeatures.add(this.getProductFeatureID());
			productFeatures.addAll(this.getImportedFeaturesForFeatureMap().get(this.getProductFeatureID()));
			productFeatures.addAll(this.getIncludedFeaturesForFeatureMap().get(this.getProductFeatureID()));
		}
		return productFeatures;
	}
	
	/**
	 * Returns the IDs of the providing features of the specified bundle/plugin.
	 *
	 * @param symbolicBundlename the symbolic bundle name
	 * @return the providing feature
	 */
	public ArrayList<String> getFeaturesOfBundle(String symbolicBundlename) {
		if (symbolicBundlename!=null && symbolicBundlename.isEmpty()==false) {
			return this.getPluginToFeatureListMap().get(symbolicBundlename);
		}
		return null;
	}
	/**
	 * Checks if the specified bundle (bundle-ID) is a bundle of product feature.
	 *
	 * @param bundleID the bundle ID
	 * @return true, if the bundle is element of product feature
	 */
	public boolean isBundleOfProductFeature(String bundleID) {
		if (bundleID!=null && bundleID.isEmpty()==false) {
			ArrayList<String> featureIDs = this.getFeaturesOfBundle(bundleID);
			for (String featureID : featureIDs) {
				if (featureID.equals(this.getProductFeatureID())==true) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Checks if the specified feature is part of the base installation.
	 *
	 * @param featureID the feature ID
	 * @return true, if the feature is part of the AWB base installation
	 */
	public boolean isFeatureOfBaseInstallation(String featureID) {
		if (featureID!=null && featureID.isEmpty()==false) {
			List<String> productFeatures = this.getProductFeatures();
			return (productFeatures.contains(featureID));
		}
		return false;
	}
	/**
	 * Checks if the specified bundle is part of the base installation.
	 *
	 * @param bundleID the bundle ID
	 * @return true, if is bundle of base installation
	 */
	public boolean isBundleOfBaseInstallation(String bundleID) {
		if (bundleID!=null && bundleID.isEmpty()==false) {
			ArrayList<String> featuresOfBundle = this.getFeaturesOfBundle(bundleID);
			if (featuresOfBundle!=null && featuresOfBundle.size()>0) {
				for (String featureID : this.getFeaturesOfBundle(bundleID)) {
					if (this.isFeatureOfBaseInstallation(featureID)==true) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Return the feature from the specified installable unit.
	 *
	 * @param installableUnit the installable unit
	 * @return the feature from installable unit
	 */
	private Feature getFeatureFromInstallableUnit(IInstallableUnit installableUnit) {
		
		if (installableUnit==null) return null;
		
		// --- Try to find the feature file info ---------- 
		Feature feature = null;
		String iuID = installableUnit.getId();
		while (iuID.contains(".")==true) {
			// --- try to get the feature -----------------
			feature = this.getFeatureMap().get(iuID);
			if (feature!=null) break;
			// --- Shorten the ID from the end ------------
			iuID = iuID.substring(0, iuID.lastIndexOf("."));
		}
		return feature;
	}
	
	/**
	 * Checks if the feature, specified by an installable unit, is a feature of the base installation.
	 *
	 * @param installableUnit the installable unit
	 * @return true, if the feature is from the base installation
	 */
	public boolean isFeatureOfBaseInstallation(IInstallableUnit installableUnit) {
		
		boolean isBaseFeature = false;
		Feature feature = this.getFeatureFromInstallableUnit(installableUnit);
		if (feature!=null) {
			isBaseFeature = this.isFeatureOfBaseInstallation(feature.getId());
		}
		return isBaseFeature;
	}
	/**
	 * Returns, based on the available feature descriptions a description for an {@link IInstallableUnit}.
	 *
	 * @param installableUnit the installable unit
	 * @return the description of the installable unit
	 */
	public String getIInstallableUnitDescription(IInstallableUnit installableUnit) {
		
		if (installableUnit==null) return null;
		
		// --- Try to find the feature file info ---------- 
		String description = "";
		Feature feature = this.getFeatureFromInstallableUnit(installableUnit);
		if (feature!=null) {
			
			String baseElementMarker = getMarkerForBaseInstallationElement(feature.getId(), ElementType.Feature);
			String featureLabel = feature.getLabel();
			if (featureLabel==null || featureLabel.equals("%featureName")==true) {
				featureLabel = feature.getId();
			}
			description += featureLabel + " [" + feature.getId() + " " + feature.getVersion() + "] " + baseElementMarker;
		}
		return description.trim();
	}
	
}
