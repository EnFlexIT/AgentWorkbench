package de.enflexit.common.p2;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.engine.EngineActivator;
import org.eclipse.equinox.internal.p2.metadata.OSGiVersion;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProfileChangeOperation;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import de.enflexit.common.SystemEnvironmentHelper;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;

/**
 * This class handles p2-based update and install operations.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
@SuppressWarnings("restriction")
public class P2OperationsHandler {
	
	private static final String DEFAULT_REPO_URI = "https://p2.enflex.it/awb/latest/";
	
	private boolean isDevelopmentMode = false;
	private File p2Directory = null;

	private ProvisioningSession provisioningSession;
	private IProvisioningAgent provisioningAgent;

	private IProgressMonitor progressMonitor;
	
	private UpdateOperation updateOperation;

	private IMetadataRepositoryManager metadataRepositoryManager;
	private IArtifactRepositoryManager artifactRepositoryManager;

	private  List<IInstallableUnit> iuList;
	
	private boolean debug = false;
	
	private Vector<String> updateRelevantBundleNames;
	private Vector<Bundle> installedBundles;
	
	private HashMap<URI, IMetadataRepository> metadataRepositories;
	
	private static P2OperationsHandler thisInstance;
	/**
	 * Returns the single instance of P2OperationsHandler.
	 * @return single instance of P2OperationsHandler
	 */
	public static P2OperationsHandler getInstance() {
		if (thisInstance==null) {
			thisInstance = new P2OperationsHandler();
		}
		return thisInstance;
	}
	/**
	 * Instantiates a new P2OperationsHandler.
	 */
	private P2OperationsHandler() { 
		if (this.isDevelopmentMode==true && (this.p2Directory==null || this.p2Directory.exists()==false || p2Directory.isDirectory()==false) ) {
			this.isDevelopmentMode = false;
			this.p2Directory = null;
			
		}
	}
	
	/**
	 * Gets the provisioning session.
	 * @return the provisioning session
	 */
	private ProvisioningSession getProvisioningSession() {
		if (provisioningSession == null) {
			provisioningSession = new ProvisioningSession(this.getProvisioningAgent());
		}
		return provisioningSession;
	}

	/**
	 * Gets the provisioning agent.
	 * @return the provisioning agent
	 */
	private IProvisioningAgent getProvisioningAgent() {
		if (provisioningAgent==null) {
			
			// ----------------------------------------------------------------
			// --- Access external p2 directory for developments ? ------------
			// ----------------------------------------------------------------
			if (this.isDevelopmentMode==true && this.p2Directory!=null && this.p2Directory.exists()==true && this.p2Directory.isDirectory()==true) {
				// --- This should provide the agent for developments -----
				BundleContext bc = BundleEvaluator.getInstance().getBundleContext();
				ServiceReference<?> sr = bc.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
				if (sr!=null) {
					IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider) bc.getService(sr);
					try {
						provisioningAgent = agentProvider.createAgent(this.p2Directory.toURI());
					} catch (ProvisionException pOrUriEx) {
						pOrUriEx.printStackTrace();
						this.isDevelopmentMode = false;
					}
				}
				
			} else {
				this.isDevelopmentMode = false;
			}
			
			if (provisioningAgent==null) {
				// --- This should provide the agent for product execution ----
				BundleContext bundleContext = FrameworkUtil.getBundle(P2OperationsHandler.class).getBundleContext();
				ServiceReference<?> serviceReference = bundleContext.getServiceReference(IProvisioningAgent.SERVICE_NAME);
				if (serviceReference!=null) {
					provisioningAgent = (IProvisioningAgent) bundleContext.getService(serviceReference);
				}
			}
			
		}
		return provisioningAgent;
	}
	
	/**
	 * Sets the progress monitor for the used processes.
	 * @param progressMonitor the new progress monitor
	 */
	public void setProgressMonitor(IProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}
	/**
	 * Gets a {@link IProgressMonitor} for p2 operations. Currently always returns a new {@link NullProgressMonitor}.
	 * @return the progress monitor
	 */
	private IProgressMonitor getProgressMonitor() {
		if (progressMonitor==null) {
			progressMonitor = new NullProgressMonitor();
		}
		return progressMonitor;
	}

	/**
	 * Gets the update operation.
	 * @return the update operation
	 */
	private UpdateOperation getUpdateOperation() {
		if (updateOperation == null) {
			updateOperation = new UpdateOperation(this.getProvisioningSession());
		}
		return updateOperation;
	}

	/**
	 * Gets the metadata repository manager.
	 * @return the metadata repository manager
	 */
	private IMetadataRepositoryManager getMetadataRepositoryManager() {
		if (metadataRepositoryManager == null) {
			metadataRepositoryManager = (IMetadataRepositoryManager) this.getProvisioningAgent().getService(IMetadataRepositoryManager.SERVICE_NAME);
		}
		return metadataRepositoryManager;
	}
	/**
	 * Gets the artifact repository manager.
	 * @return the artifact repository manager
	 */
	private IArtifactRepositoryManager getArtifactRepositoryManager() {
		if (artifactRepositoryManager == null) {
			artifactRepositoryManager = (IArtifactRepositoryManager) this.getProvisioningAgent().getService(IArtifactRepositoryManager.SERVICE_NAME);
		}
		return artifactRepositoryManager;
	}

	/**
	 * Checks if a specific {@link IInstallableUnit} (IU) is installed
	 * 
	 * @param installableUnitID The ID of the IU of interest
	 * @return true if the IU is installed
	 */
	public boolean checkIfInstalled(String installableUnitID) {

		// --- Query the p2 profile for the InstallableUnit of interest -----------
		IProfileRegistry profileRegistry = (IProfileRegistry) this.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
		IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
		IQuery<IInstallableUnit> query = QueryUtil.createIUQuery(installableUnitID);
		IQueryResult<IInstallableUnit> queryResult = profile.query(query, this.getProgressMonitor());

		return !(queryResult.isEmpty());
	}

	/**
	 * Adds a new p2 repository
	 * 
	 * @param repositoryURI the {@link URI} of the repository to add
	 */
	public void addRepository(URI repositoryURI) {
		this.addRepository(repositoryURI, null);
	}
	/**
	 * Adds a new p2 repository.
	 *
	 * @param repositoryURI the repository URI
	 * @param repositoryName the repository name
	 */
	public void addRepository(URI repositoryURI, String repositoryName) {
		this.getMetadataRepositoryManager().addRepository(repositoryURI);
		this.getArtifactRepositoryManager().addRepository(repositoryURI);
		if (repositoryName!=null) {
			this.getMetadataRepositoryManager().setRepositoryProperty(repositoryURI, IRepository.PROP_NICKNAME, repositoryName);
		}
	}
	
	/**
	 * Checks if the specified repository is already known.
	 *
	 * @param repositoryURI the repository URI
	 * @return true, if is known repository
	 */
	public boolean isKnownRepository(URI repositoryURI) {
		return this.getMetadataRepositoryManager().contains(repositoryURI) && this.getArtifactRepositoryManager().contains(repositoryURI);
	}
	
	/**
	 * Installs an {@link IInstallableUnit} from a p2 repository.
	 * 
	 * @param installableUnitID the ID of the IU to be installed
	 * @param repositoryURI the repository ID
	 * @return true if successful
	 */
	public boolean installIU(String installableUnitID, URI repositoryURI) {
		
		// --- Make sure the repository is known and enabled ------------------
		this.addRepository(repositoryURI);

		// --- Query the repository for the IU of interest --------------------
		IQueryResult<IInstallableUnit> queryResult = this.queryRepositoryForInstallableUnit(repositoryURI, installableUnitID);

		if (queryResult.isEmpty() == false) {

			// --- If there is more than one result, take the newest one ------
			Set<IInstallableUnit> resultSet = queryResult.toSet();
			if (resultSet.size()>1) {
				IInstallableUnit iuToInstall = this.getNewestUnit(resultSet);
				resultSet.clear();
				resultSet.add(iuToInstall);
			}

			// --- If found, trigger an InstallOperation ----------------------
			InstallOperation installOperation = new InstallOperation(this.getProvisioningSession(), resultSet);
			installOperation.getProvisioningContext().setMetadataRepositories(repositoryURI);
			installOperation.getProvisioningContext().setArtifactRepositories(repositoryURI);
			IStatus result = this.performOperation(installOperation);
			return result.isOK();

		} else {

			// --- If not, print an error message -----------------------
			String errorMessage = "Installable unit " + installableUnitID + " could not be found in the repositoty " + repositoryURI;
			System.err.println(errorMessage);
			return false;

		}

	}

	/**
	 * Performs a {@link ProfileChangeOperation}
	 * 
	 * @param operation the operation
	 * @return the result status
	 */
	private IStatus performOperation(ProfileChangeOperation operation) {
		IStatus status = operation.resolveModal(this.getProgressMonitor());
		if (status.isOK()) {
			ProvisioningJob provisioningJob = operation.getProvisioningJob(this.getProgressMonitor());

			if (provisioningJob == null) {
				System.err.println("Trying to install from the Eclipse IDE? This won't work!");
				return Status.CANCEL_STATUS;
			}

			status = provisioningJob.runModal(this.getProgressMonitor());
		}
		return status;
	}

	/**
	 * Checks for updates.
	 * @return the result status
	 */
	public IStatus checkForUpdates() {
		return this.getUpdateOperation().resolveModal(this.getProgressMonitor());
	}

	/**
	 * Installs available updates from all repositories.
	 *
	 * @return the result status
	 */
	public IStatus installAvailableUpdates() {
		
		// --- Set policy to always accept unsigned IUs if operating headless -----------
		if (SystemEnvironmentHelper.isHeadlessOperation()==true) {
			//TODO Remove when proper signing of bundles is implemented!
			System.getProperties().setProperty(EngineActivator.PROP_UNSIGNED_POLICY, EngineActivator.UNSIGNED_ALLOW);
		}
		
		ProvisioningJob provisioningJob = this.getUpdateOperation().getProvisioningJob(this.getProgressMonitor());
		if (provisioningJob == null) {
			System.err.println("Trying to update from the Eclipse IDE? This won't work!");
			return Status.CANCEL_STATUS;
		}

		return provisioningJob.runModal(this.getProgressMonitor());
	}

	/**
	 * Gets the repository for the {@link IInstallableUnit} (IU) with the given ID.
	 *
	 * @param installableUnitID the ID of the IU of interest
	 * @return the repository, null if no known repository contains the IU
	 */
	public URI getRepositoryForInstallableUnit(String installableUnitID) {

		try {
			// --- Get a list of all known repositories ---------------------------------
			IMetadataRepositoryManager metadataRepositoryManager = (IMetadataRepositoryManager) this.getProvisioningAgent().getService(IMetadataRepositoryManager.SERVICE_NAME);
			URI[] knownRepositories = metadataRepositoryManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);
			
			// --- Check if the repository contains an IU with the requested ID ---------
			for (URI repository : knownRepositories) {
				IQueryResult<IInstallableUnit> queryResult = this.queryRepositoryForInstallableUnit(repository, installableUnitID);
				if (queryResult != null && queryResult.isEmpty() == false) {
					return repository;
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// --- No repository containing the IU could be found ---------------------------
		return null;
	}

	/**
	 * Queries a repository for a specific {@link IInstallableUnit} (IU).
	 *
	 * @param repositoryURI the repository URI
	 * @param installableUnitID the ID of the IU
	 * @return the {@link IQueryResult}
	 */
	private IQueryResult<IInstallableUnit> queryRepositoryForInstallableUnit(URI repositoryURI, String installableUnitID) {

		// --- Load the repository ------------
		IQueryResult<IInstallableUnit> queryResult = null;
		try {
			IMetadataRepository metadataRepository = this.getMetadataRepositoryManager().loadRepository(repositoryURI, this.getProgressMonitor());
			// --- Query for the IU of interest -----
			if (metadataRepository != null) {
				queryResult = metadataRepository.query(QueryUtil.createIUQuery(installableUnitID), this.getProgressMonitor());
			}
			
		} catch (ProvisionException | OperationCanceledException e) {
			System.err.println("Error loading the repository at " + repositoryURI);
			e.printStackTrace();
		}

		return queryResult;
	}

	/**
	 * Gets the repository name.
	 *
	 * @param repositoryURI the repository URI
	 * @return the repository name
	 */
	public String getRepositoryName(URI repositoryURI) {
		return this.getMetadataRepositoryManager().getRepositoryProperty(repositoryURI, IRepository.PROP_NICKNAME);
	}

	/**
	 * Returns the installed features.
	 *
	 * @return the installed features
	 * @throws Exception the exception
	 */
	public List<IInstallableUnit> getInstalledFeatures() throws Exception {
		if (this.iuList==null) {
			
			IProfileRegistry profileRegistry = (IProfileRegistry) this.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
			IProfile profile = null;
			if (this.isDevelopmentMode==true) {
				if (profileRegistry.getProfiles().length>0) {
					profile = profileRegistry.getProfiles()[0];
				}
				
			} else {
				profile = profileRegistry.getProfile(IProfileRegistry.SELF);
			}
			if (profile==null) {
				throw new Exception("Unable to access p2 profile - This is not possible when starting the application from the IDE!");
			}
			
			// --- Create the IU list -------------------------------
			this.iuList = new ArrayList<IInstallableUnit>();
			IQuery<IInstallableUnit> query = QueryUtil.createIUGroupQuery();
			IQueryResult<IInstallableUnit> queryResult = profile.query(query, null);
			
			for (IInstallableUnit feature : queryResult) {
				if (QueryUtil.isProduct(feature) == false) {
					iuList.add(feature);
				}
			}
			
		}
		return iuList;
	}

	
	/**
	 * Gets the newest from a collection of {@link IInstallableUnit}s
	 * @param installableUnits the installable units
	 * @return the newest unit
	 */
	private IInstallableUnit getNewestUnit(Collection<IInstallableUnit> installableUnits) {
		IInstallableUnit newest = null;
		List<IInstallableUnit> unitsList = new ArrayList<>(installableUnits);
		for (int i=0; i<unitsList.size(); i++) {
			if (newest==null) {
				newest = unitsList.get(i);
			} else {
				if (newest.getVersion().compareTo(unitsList.get(i).getVersion())<0) {
					newest = unitsList.get(i);
				}
			}
		}
		return newest;
	}
	
	/**
	 * Checks if newer versions of installed bundles or features are available in the p2 repository.
	 * @return true, if there is at least one newer version available
	 */
	public boolean checkForNewerBundles() {
		
		// --- Check if the default repository is configured, add if not
		try {
			URI defaultRepo = new URI(DEFAULT_REPO_URI);
			if (this.isKnownRepository(defaultRepo)==false) {
				this.addRepository(defaultRepo);
			}
		} catch (URISyntaxException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Invalid URI syntax: " + DEFAULT_REPO_URI);
			e.printStackTrace();
		}
		
		URI[] knownRepos = this.getMetadataRepositoryManager().getKnownRepositories(IRepositoryManager.REPOSITORIES_ALL);

		for (int i=0; i<this.getInstalledBundles().size(); i++) {
			Bundle bundle = this.getInstalledBundles().get(i);
			
			if (this.getUpdateRelevantBundleNames().contains(bundle.getSymbolicName())) {
				for (int j=0; j<knownRepos.length; j++) {
					IMetadataRepository repo = this.getMetadataRepository(knownRepos[j]);
					if (repo!=null) {
						boolean updateAvailable = this.queryRepoForNewerVersion(bundle, repo);
						if (updateAvailable==true) {
							if (this.debug==true) {
								System.out.println("[" + this.getClass().getSimpleName() + "] Update found for bundle " + bundle.getSymbolicName());
							}
							return true;
						}
					}
				}
			}
		}
		
		// --- If not returned yet, no newer versions are available -------
		if (this.debug==true) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Your target platform is up to date!");
		}
		return false;
	}
	
	/**
	 * Gets the list of installed bundles.
	 * @return the installed bundles
	 */
	private Vector<Bundle> getInstalledBundles(){
		if (installedBundles==null) {
			Bundle[] bundlesArray = FrameworkUtil.getBundle(this.getClass()).getBundleContext().getBundles();
			installedBundles = this.removeOlderVersions(bundlesArray);
		}
		return installedBundles;
	}
	
	/**
	 * Gets the list of update relevant bundle names, i.e. the names of 
	 * those bundles that should be considered when checking for updates.
	 *
	 * @return the update relevant bundle names
	 */
	private Vector<String> getUpdateRelevantBundleNames() {
		if (updateRelevantBundleNames==null) {
			updateRelevantBundleNames = new Vector<>();
			updateRelevantBundleNames.add("de.enflexit.awb.core");
			updateRelevantBundleNames.add("de.enflexit.awb.baseUI");
			updateRelevantBundleNames.add("de.enflexit.awb.desktop");
			updateRelevantBundleNames.add("de.enflexit.awb.ws.core");
			updateRelevantBundleNames.add("de.enflexit.awb.ws.restapi");
			updateRelevantBundleNames.add("org.agentgui.lib.jung");
			updateRelevantBundleNames.add("org.awb.env.maps");
			updateRelevantBundleNames.add("org.awb.env.networkModel");
			updateRelevantBundleNames.add("de.enflexit.jade.phonebook");
			updateRelevantBundleNames.add("de.enflexit.api");
			updateRelevantBundleNames.add("de.enflexit.common");
			updateRelevantBundleNames.add("de.enflexit.geography");
			updateRelevantBundleNames.add("de.enflexit.oidc");
			updateRelevantBundleNames.add("de.enflexit.db.hibernate");
			updateRelevantBundleNames.add("de.enflexit.db.mariaDB");
			updateRelevantBundleNames.add("de.enflexit.db.mySQL");
			updateRelevantBundleNames.add("de.enflexit.db.postgres");
		}
		return updateRelevantBundleNames;
	}
	
	/**
	 * Query repo for newer version.
	 *
	 * @param bundle the bundle
	 * @param repo the repo
	 * @return true, if successful
	 */
	private boolean queryRepoForNewerVersion(Bundle bundle, IMetadataRepository repo) {
		org.osgi.framework.Version bv = bundle.getVersion();
		Version bundleVersion = new OSGiVersion(bv.getMajor(), bv.getMinor(), bv.getMicro(), bv.getQualifier());
		
		IQuery<IInstallableUnit> query = QueryUtil.createIUQuery(bundle.getSymbolicName());
		IQueryResult<IInstallableUnit> result = repo.query(query, new NullProgressMonitor());
		
		if (result.isEmpty()==false) {
			IInstallableUnit iu = this.getNewestUnit(result.toSet());
			if (iu.getVersion().compareTo(bundleVersion)>0) {
				return true;
			}
		}
		
		return false;
	}
	
	private HashMap<URI, IMetadataRepository> getMetadataRepositories() {
		if (metadataRepositories==null) {
			metadataRepositories = new HashMap<>();
		}
		return metadataRepositories;
	}
	
	private IMetadataRepository getMetadataRepository(URI uri) {
		IMetadataRepository metadataRepository = this.getMetadataRepositories().get(uri);
		if (metadataRepository==null) {
			try {
				metadataRepository = this.getMetadataRepositoryManager().loadRepository(uri, new NullProgressMonitor());
				this.getMetadataRepositories().put(uri, metadataRepository);
			} catch (ProvisionException | OperationCanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return metadataRepository;
	}
	
	/**
	 * Removes older versions of the same bundle from the provided array of bundles.  
	 * @param bundleList the bundle list
	 * @return the vector
	 */
	private Vector<Bundle> removeOlderVersions(Bundle[] bundleList){
		
		HashMap<String, Bundle> bundlesBySymbolicName = new HashMap<>();
		
		for(int i=0; i<bundleList.length; i++) {
			Bundle bundleFromList = bundleList[i];
			Bundle bundleFromHash = bundlesBySymbolicName.get(bundleFromList.getSymbolicName());
			if (bundleFromHash==null || bundleFromList.getVersion().compareTo(bundleFromHash.getVersion())>0) {
				bundlesBySymbolicName.put(bundleFromList.getSymbolicName(), bundleFromList);
			}
		}
		
		return new Vector<>(bundlesBySymbolicName.values());
	}
	
	
}
