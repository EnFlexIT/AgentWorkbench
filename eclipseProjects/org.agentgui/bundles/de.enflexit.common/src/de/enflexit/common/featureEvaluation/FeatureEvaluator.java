package de.enflexit.common.featureEvaluation;

import org.eclipse.core.runtime.IBundleGroup;
import org.eclipse.core.runtime.IBundleGroupProvider;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class FeatureEvaluator {

	
	/**
	 * Prints the list of features and bundles.
	 */
	public static void printListOfFeaturesAndBundles() {
		
		for (IBundleGroupProvider provider : Platform.getBundleGroupProviders()) {
			for (IBundleGroup feature : provider.getBundleGroups()) {
				String featureId = feature.getIdentifier();
				String fProviderName = feature.getProviderName();
				String fVersion = feature.getVersion();
				System.out.println("=> Found feature '" + featureId + "' (version: " + fVersion + ") provided by " + fProviderName);
				
				for (Bundle bundle : feature.getBundles()) {
					String bSymbollicame = bundle.getSymbolicName();
					String bVersion = bundle.getVersion().toString();
					System.out.println("- Feature '" + featureId + "' found bundle '" + bSymbollicame + "' (version: " + bVersion + ")");
				}
			}
		}

	}

}
