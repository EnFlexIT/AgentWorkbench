package de.enflexit.awb.ws.restapi.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.enflexit.awb.ws.core.util.WebApplicationUpdate;
import de.enflexit.awb.ws.core.util.WebApplicationVersion;
import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.VersionApiService;
import de.enflexit.awb.ws.restapi.gen.model.SoftwareComponent;
import de.enflexit.awb.ws.restapi.gen.model.SoftwareComponentList;
import de.enflexit.awb.ws.restapi.gen.model.SoftwareComponentType;
import de.enflexit.awb.ws.restapi.gen.model.Version;
import de.enflexit.common.featureEvaluation.FeatureEvaluator;
import de.enflexit.common.featureEvaluation.featureXML.Feature;
import de.enflexit.common.featureEvaluation.featureXML.Plugin;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The Class VersionApiImpl.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class VersionApiImpl extends VersionApiService {

	private final boolean isProductionUsage = true;
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.restapi.gen.VersionApiService#versionGet(de.enflexit.awb.ws.restapi.gen.model.SoftwareComponentType, java.lang.String, java.lang.Boolean, jakarta.ws.rs.core.SecurityContext)
	 */
	@Override
	public Response versionGet(SoftwareComponentType type, String filter, Boolean isShowSource, SecurityContext securityContext) throws NotFoundException {
		
		// --- Check who is the user --------------------------------
		if (this.isProductionUsage==true) {
			Principal principal = securityContext.getUserPrincipal();
			if (principal==null) {
				return Response.status(Status.FORBIDDEN).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Permission denied!")).build();
			}
		}
		
		// --- Ensure at least one specific response ----------------
		if (type==null) type=SoftwareComponentType.WEBAPP;
		boolean isShowSourceComponents = isShowSource==null ? false : isShowSource;
		
		// --- Create response 'SoftwareComponentList' --------------
		SoftwareComponentList scList = null;
		switch (type) {
		case NULL:
		case WEBAPP: 
			scList = this.getSoftwareComponentListForWebApp(scList);
			break;
		case FEATURE:
			scList = this.getSoftwareComponentListForFeatures(scList, isShowSourceComponents);
			break;
		case BUNDLE:
			scList = this.getSoftwareComponentListForBundles(scList, isShowSourceComponents);
			break;
		case BUNDLE_OF_FEATURE:
			scList = this.getSoftwareComponentListForBundlesInFeature(scList, filter, isShowSourceComponents);
			break;
		}

		// --- Return error, if nothing was found -------------------
		if (scList==null || scList.getSoftwareComponentList()==null || scList.getSoftwareComponentList().size()==0) {
			return Response.status(Status.NOT_FOUND).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "No Software Component of type '" + type.name() + "' could be found!")).build();
		}
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(scList).build();
	}

	/**
	 * Returns the software component list for the WebApp (which is just one entry).
	 *
	 * @param scList the current SoftwareComponentList
	 * @return the software component list
	 */
	private SoftwareComponentList getSoftwareComponentListForWebApp(SoftwareComponentList scList) {
	
		// --- Get current WebApplicationVersion ----------
		WebApplicationVersion webAppVersion = WebApplicationUpdate.getCurrentWebApplicationVersion();
		if (webAppVersion==null) return null;
		
		// --- Define SoftwareComponent -------------------
		SoftwareComponent sc = new SoftwareComponent();
		sc.setComponentType(SoftwareComponentType.WEBAPP);
		sc.setID(webAppVersion.getUpdateURL());
		sc.setName(webAppVersion.getFileName());
		sc.setVersion(this.toApiVersion(webAppVersion.getVersion()));

		// --- Add to result list -------------------------
		if (scList==null) scList = new SoftwareComponentList();
		scList.addSoftwareComponentListItem(sc);
		
		return scList;
	}
	
	/**
	 * Returns the software component list for installed Features.
	 *
	 * @param scList the current SoftwareComponentList
	 * @param isShowSource the indicator to show source feature
	 * @return the software component list
	 */
	private SoftwareComponentList getSoftwareComponentListForFeatures(SoftwareComponentList scList, boolean isShowSource) {
	
		// --- Get the FeatureEvaluator instance --------------------
		HashMap<String, Feature> fiMap = FeatureEvaluator.getInstance().getFeatureMap();
		if (fiMap==null || fiMap.size()==0) return null;
		
		// --- Create result list -----------------------------------
		if (scList==null) scList = new SoftwareComponentList();

		// --- Create sorted list of feature IDs --------------------
		List<String> featureIDs = new ArrayList<>(fiMap.keySet());
		Collections.sort(featureIDs);
		for (String featureID : featureIDs) {
			
			// --- Skip source feature? -----------------------------
			if (isShowSource==false && featureID.contains(".source")==true) continue;

			// --- Get the actual Feature ---------------------------
			Feature feature = fiMap.get(featureID);
			
			// --- Create SoftwareComponent -------------------------
			SoftwareComponent sc = new SoftwareComponent();
			sc.setComponentType(SoftwareComponentType.FEATURE);
			sc.setID(feature.getId());
			sc.setName(feature.getLabel());
			sc.setVersion(this.toApiVersion(org.osgi.framework.Version.parseVersion(feature.getVersion())));

			scList.addSoftwareComponentListItem(sc);
		}
		return scList;
	}
	
	/**
	 * Returns the software component list for installed Bundles.
	 *
	 * @param scList the current SoftwareComponentList
	 * @param isShowSource the indicator to show source bundle
	 * @return the software component list
	 */
	private SoftwareComponentList getSoftwareComponentListForBundles(SoftwareComponentList scList, boolean isShowSource) {
	
		// --- Get the FeatureEvaluator instance --------------------
		HashMap<String, Plugin> piMap = FeatureEvaluator.getInstance().getPluginMap();
		if (piMap==null || piMap.size()==0) return null;
		
		// --- Create result list -----------------------------------
		if (scList==null) scList = new SoftwareComponentList();

		// --- Create sorted list of feature IDs --------------------
		List<String> pluginIDs = new ArrayList<>(piMap.keySet());
		Collections.sort(pluginIDs);
		for (String pluginID : pluginIDs) {
			
			// --- Skip source feature? -----------------------------
			if (isShowSource==false && pluginID.contains(".source")==true) continue;
			
			// --- Get the actual Feature ---------------------------
			Plugin plugin = piMap.get(pluginID);

			// --- Create SoftwareComponent -------------------------
			SoftwareComponent sc = new SoftwareComponent();
			sc.setComponentType(SoftwareComponentType.BUNDLE);
			sc.setID(plugin.getId());
			sc.setName(pluginID);
			sc.setVersion(this.toApiVersion(org.osgi.framework.Version.parseVersion(plugin.getVersion())));

			scList.addSoftwareComponentListItem(sc);
		}
		return scList;
	}
	
	/**
	 * Returns the software component list for installed Bundles of a specific feature.
	 *
	 * @param scList the current SoftwareComponentList
	 * @param searchFor the search for
	 * @param isShowSource the indicator to show source bundle
	 * @return the software component list
	 */
	private SoftwareComponentList getSoftwareComponentListForBundlesInFeature(SoftwareComponentList scList, String filterFor, boolean isShowSource) {

		if (filterFor==null || filterFor.isBlank()==true || filterFor.length()<=3) {
			return null;
		}
		
		// --- Derive from all features -----------------------------
		HashMap<String, Feature> fiMap = FeatureEvaluator.getInstance().getFeatureMap();
		if (fiMap==null || fiMap.size()==0) return null;
		
		List<String> featureIDsFound = new ArrayList<>();
		List<String> featureIDs = new ArrayList<>(fiMap.keySet());
		Collections.sort(featureIDs);

		for (String featureID : featureIDs) {
			// --- Skip source feature? -----------------------------
			if (isShowSource==false && featureID.contains(".source")==true) continue;
			// --- Does feature ID match the filter phrase? ---------
			if (featureID.toLowerCase().contains(filterFor.toLowerCase())==true) {
				featureIDsFound.add(featureID);
			}
		}
		if (featureIDsFound.size()==0) return null;
		
		// --- Create result list -----------------------------------
		if (scList==null) scList = new SoftwareComponentList();
		
		// --- Consider features found --------------------------
		HashMap<String, Plugin> piMap = FeatureEvaluator.getInstance().getPluginMap();
		for (String featureID : featureIDsFound) {
			
			// --- Add the feature to the result list ---------------
			Feature feature = fiMap.get(featureID);
			
			// --- Create SoftwareComponent -------------------------
			SoftwareComponent sc = new SoftwareComponent();
			sc.setComponentType(SoftwareComponentType.FEATURE);
			sc.setID(feature.getId());
			sc.setName(feature.getLabel());
			sc.setVersion(this.toApiVersion(org.osgi.framework.Version.parseVersion(feature.getVersion())));

			scList.addSoftwareComponentListItem(sc);
			
			
			// --- Get all bundles that belong to the feature -------
			ArrayList<String> pluginIDList = FeatureEvaluator.getInstance().getFeatureToPluginListMap().get(featureID);
			Collections.sort(pluginIDList);
			if (pluginIDList==null || pluginIDList.size()==0) continue;
			
			for (String pluginID : pluginIDList) {
				// --- Skip source bundle? --------------------------
				if (isShowSource==false && pluginID.contains(".source")==true) continue;
				
				// --- Get the actual Feature ---------------------------
				Plugin plugin = piMap.get(pluginID);

				// --- Create SoftwareComponent -------------------------
				sc = new SoftwareComponent();
				sc.setComponentType(SoftwareComponentType.BUNDLE);
				sc.setID(plugin.getId());
				sc.setName(pluginID);
				sc.setVersion(this.toApiVersion(org.osgi.framework.Version.parseVersion(plugin.getVersion())));

				scList.addSoftwareComponentListItem(sc);
			}
			
		}
		return scList;
	}
	
	/**
	 * Converts an OSGI Version instance to an API Version instance.
	 *
	 * @param osgiVersion the osgi version
	 * @return the version
	 */
	private Version toApiVersion(org.osgi.framework.Version osgiVersion) {
		
		if (osgiVersion==null) return null;
		
		Version version = new Version();
		version.setMajor(osgiVersion.getMajor());
		version.setMinor(osgiVersion.getMinor());
		version.setMicro(osgiVersion.getMicro());
		version.setQualifier(osgiVersion.getQualifier());
		return version;
	}
	
}
