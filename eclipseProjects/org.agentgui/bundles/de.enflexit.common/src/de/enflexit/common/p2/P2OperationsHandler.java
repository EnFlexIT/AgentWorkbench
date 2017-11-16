package de.enflexit.common.p2;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProfileChangeOperation;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * This class handles p2-based update and install operations.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class P2OperationsHandler {

	private ProvisioningSession provisioningSession;
	private IProvisioningAgent provisioningAgent;

	private UpdateOperation updateOperation;

	/**
	 * Gets the provisioning session.
	 *
	 * @return the provisioning session
	 */
	private ProvisioningSession getProvisioningSession() {
		if (provisioningSession == null) {
			provisioningSession = new ProvisioningSession(getProvisioningAgent());
		}
		return provisioningSession;
	}

	/**
	 * Gets the provisioning agent.
	 *
	 * @return the provisioning agent
	 */
	private IProvisioningAgent getProvisioningAgent() {
		if (provisioningAgent == null) {
			BundleContext bundleContext = FrameworkUtil.getBundle(P2OperationsHandler.class).getBundleContext();
			ServiceReference<?> serviceReference = bundleContext.getServiceReference(IProvisioningAgent.SERVICE_NAME);

			if (serviceReference != null) {
				provisioningAgent = (IProvisioningAgent) bundleContext.getService(serviceReference);
			}
		}
		return provisioningAgent;
	}

	/**
	 * Gets a {@link IProgressMonitor} for p2 operations. Currently always returns a new {@link NullProgressMonitor}.
	 * 
	 * @return the progress monitor
	 */
	private IProgressMonitor getProgressMonitor() {
		// TODO Figure out how to handle other progress monitors for graphical visualization
		return new NullProgressMonitor();
	}

	/**
	 * Gets the update operation.
	 *
	 * @return the update operation
	 */
	private UpdateOperation getUpdateOperation() {
		if (updateOperation == null) {
			updateOperation = new UpdateOperation(this.getProvisioningSession());
		}
		return updateOperation;
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
		IMetadataRepositoryManager metadataRepositoryManager = (IMetadataRepositoryManager) this.getProvisioningAgent().getService(IMetadataRepositoryManager.SERVICE_NAME);
		metadataRepositoryManager.addRepository(repositoryURI);
		IArtifactRepositoryManager artifactRepositoryManager = (IArtifactRepositoryManager) this.getProvisioningAgent().getService(IArtifactRepositoryManager.SERVICE_NAME);
		artifactRepositoryManager.addRepository(repositoryURI);
	}

	/**
	 * Installs an {@link IInstallableUnit} from a p2 repository.
	 * 
	 * @param installableUnitID the ID of the IU to be installed
	 * @param repositoryURI the repository ID
	 * @return true if successful
	 */
	public boolean installIU(String installableUnitID, URI repositoryURI) {

		// --- Make sure the repository is known and enabled ---------
		this.addRepository(repositoryURI);

		IMetadataRepository metadataRepository = null;
		try {
			IMetadataRepositoryManager metadataRepositoryManager = (IMetadataRepositoryManager) this.getProvisioningAgent().getService(IMetadataRepositoryManager.SERVICE_NAME);
			metadataRepository = metadataRepositoryManager.loadRepository(repositoryURI, this.getProgressMonitor());

		} catch (ProvisionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		if (metadataRepository != null) {
			IQueryResult<IInstallableUnit> queryResult = metadataRepository.query(QueryUtil.createIUQuery(installableUnitID), this.getProgressMonitor());

			if (queryResult.isEmpty() == true) {
				String errorMessage = "Installable unit " + installableUnitID + " could not be found in the repositoty " + repositoryURI;
				System.err.println(errorMessage);
				return false;
			}

			InstallOperation installOperation = new InstallOperation(this.getProvisioningSession(), queryResult.toSet());
			IStatus result = this.performOperation(installOperation);

			return result.isOK();

		} else {
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
	 * 
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
		ProvisioningJob provisioningJob = this.getUpdateOperation().getProvisioningJob(this.getProgressMonitor());
		if (provisioningJob == null) {
			System.err.println("Trying to update from the Eclipse IDE? This won't work!");
			return Status.CANCEL_STATUS;
		}

		return provisioningJob.runModal(this.getProgressMonitor());
	}

}
