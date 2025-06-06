package de.enflexit.awb.core.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

import de.enflexit.common.featureEvaluation.FeatureEvaluator;
import de.enflexit.common.featureEvaluation.FeatureInfo;

/**
 * The Class BundleFeatureMapper.
 */
public class BundleFeatureMapper {

	
	/**
	 * Returns the required features for the specified project.
	 * 
	 * @param project the project
	 * @return the required features for project
	 */
	public static Vector<FeatureInfo> getRequiredFeaturesForProject(Project project) {
		if (project==null) return null;
		return getRequiredFeaturesForBundleVector(project.getProjectBundleLoader().getBundleVector());
	}
	
	/**
	 * Returns the required features for the specified vector of bundles.
	 *
	 * @param bundleVector the bundle vector
	 * @return the required features for the bundle vector
	 */
	public static Vector<FeatureInfo> getRequiredFeaturesForBundleVector(Vector<Bundle> bundleVector) {
		
		if (bundleVector==null) return null;
		
		// --- Define interim list --------------------------------------------
		ArrayList<FeatureInfo> featureInfoList = new ArrayList<>();
		
		// --- Get the bundles that belong to the project ---------------------
		for (int i = 0; i < bundleVector.size(); i++) {
			// --- Get FeatureInfo for required features for each bundle ------
			Bundle bundle = bundleVector.get(i);
			Vector<FeatureInfo> reqFeatrues = getRequiredFeaturesForBundle(bundle); 
			for (int j = 0; j < reqFeatrues.size(); j++) {
				if (featureInfoList.contains(reqFeatrues.get(j))==false) {
					featureInfoList.add(reqFeatrues.get(j));					
				}
			}
		} 

		// --- Define and sort the result vector ------------------------------
		Vector<FeatureInfo> featureVector = new Vector<>(featureInfoList);
		Collections.sort(featureVector);
		return featureVector;
	}
	
	/**
	 * Returns the required features for the specified bundle.
	 *
	 * @param bundle the bundle
	 * @return the required features for the bundle
	 */
	public static Vector<FeatureInfo> getRequiredFeaturesForBundle(Bundle bundle) {
		
		if (bundle==null) return null;
		
		// --- Define interim list ----------------------------------
		ArrayList<FeatureInfo> featureInfoList = new ArrayList<>();
		FeatureEvaluator fe = FeatureEvaluator.getInstance();

		// --- Get the list of required bundles -----------------
		ArrayList<String> reqBundle = getRequiredBundleIDs(bundle);
		for (int j = 0; j < reqBundle.size(); j++) {
			
			String bundleID = reqBundle.get(j);
			boolean isOfBaseInstallation = fe.isBundleOfBaseInstallation(bundleID);
			ArrayList<String> featureIDs = fe.getFeaturesOfBundle(bundleID);
			if (isOfBaseInstallation==false && featureIDs!=null) {
				for (String featureID : featureIDs) {
					// --- Found required feature ---------------
					FeatureInfo fInfo = FeatureInfo.createFeatureInfoFromFeatureID(featureID);
					if (fInfo!=null && featureInfoList.contains(fInfo)==false) {
						featureInfoList.add(fInfo);
					}
				}
			}
			
		}
		
		// --- Define and sort the result vector --------------------
		Vector<FeatureInfo> featureVector = new Vector<>(featureInfoList);
		Collections.sort(featureVector);
		return featureVector;
	}
	
	
	/**
	 * Gets the ID of all required bundles for the specified bundle.
	 *
	 * @param bundle the bundle to evaluate
	 * @return the required bundle ID's
	 */
	public static ArrayList<String> getRequiredBundleIDs(Bundle bundle) {
		
		ArrayList<String> requiredBundles = new ArrayList<>();

		String reqBundles = bundle.getHeaders().get(Constants.REQUIRE_BUNDLE);
		if (reqBundles!=null && reqBundles.isEmpty()==false) {
			String[] reqBundlesArray =  reqBundles.split(",");
			for (int i = 0; i < reqBundlesArray.length; i++) {
				String reqLine = reqBundlesArray[i];
				if (reqLine.contains(";")==true) {
					reqLine = reqLine.substring(0, reqLine.indexOf(";"));
				}
				requiredBundles.add(reqLine);
			}
		}
		return requiredBundles;
	}
	
}
