package agentgui.simulationService.distribution;

import java.util.ArrayList;
import java.util.List;

import agentgui.simulationService.agents.ServerMasterAgent;
import de.enflexit.awb.bgSystem.db.BgSystemDatabaseHandler;
import de.enflexit.awb.bgSystem.db.dataModel.BgSystemPlatform;

/**
 * The Class PlatformStore is used by the {@link ServerMasterAgent} to organize
 * the available platforms.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PlatformStore {

	private List<BgSystemPlatform> platformList; 
	private BgSystemDatabaseHandler dbHandler;
	
	
	/**
	 * Returns the current platform list.
	 * @return the platform list
	 */
	public List<BgSystemPlatform> getPlatformList() {
		if (platformList==null) {
			// --- Try to get the database entries --------
			platformList = this.dbGetPlatformList();
			if (platformList==null) {
				platformList = new ArrayList<>();
			}
		}
		return platformList;
	}
	
	/**
	 * Adds the specified platform.
	 * @param newPlatform the platform
	 */
	public void addPlatform(BgSystemPlatform newPlatform) {
		
		if (newPlatform!=null && this.getPlatformList().contains(newPlatform)==false) {
			this.getPlatformList().add(newPlatform);
			this.dbSynchronizePlatformList();
		}
	}
	/**
	 * Will add or update the specified platform information in the local store (basically, it 
	 * will update the database if possible).
	 * @param platform the platform
	 */
	public void addOrUpdatePlatform(BgSystemPlatform platform) {
		if (platform==null) return;
		if (this.getPlatformList().contains(platform)==false) {
			this.getPlatformList().add(platform);
		}
		this.dbSynchronizePlatformList();
	}
	/**
	 * Removes the specified background system platform.
	 * @param platform the platform to add
	 */
	public void removePlatform(BgSystemPlatform platform) {
		this.removePlatform(platform, true);
	}
	/**
	 * Removes the specified background system platform.
	 * @param platform the platform to remove
	 * @param isSynchronizeDatabase the indicator to synchronize the database
	 */
	private void removePlatform(BgSystemPlatform platform, boolean isSynchronizeDatabase) {
		if (platform!=null && this.getPlatformList().contains(platform)==true) {
			this.getPlatformList().remove(platform);
			if (isSynchronizeDatabase==true) {
				this.dbSynchronizePlatformList();
			}
		}
	}

	/**
	 * Returns the platform of the specified contact agent {derived by agents complete name}.
	 *
	 * @param contactAgent the contact agent
	 * @return the platform
	 */
	public BgSystemPlatform getPlatform(String contactAgent) {
		
		for (BgSystemPlatform platform : this.getPlatformList()) {
			if (platform.getContactAgent().equals(contactAgent)==true) {
				return platform;
			}
		}
		return null;
	}
	
	/**
	 * Removes the out dated platforms.
	 * @param boundaryTime the boundary time at which information will be kept
	 */
	public void removeOutDatedPlatforms(long boundaryTime) {
		
		List<BgSystemPlatform> toDelete = new ArrayList<>();
		
		// --- Check all available platforms --------------
		for (BgSystemPlatform platform : this.getPlatformList()) {
			if (platform.getTimeLastContact().getTimeInMillis() < boundaryTime) {
				toDelete.add(platform);
			}
		}
		
		// --- Remove the platforms found to be deleted ---
		if (toDelete.size()>0) {
			for (BgSystemPlatform platform : toDelete) {
				this.removePlatform(platform, false);
			}
		}
		// --- Synchronize database -----------------------
		this.dbSynchronizePlatformList();
	}
	
	
	// --------------------------------------------------------------
	// --- From here, methods to interact with the database ---------
	// --------------------------------------------------------------	
	/**
	 * Returns the database handler.
	 * @return the database handler
	 */
	private BgSystemDatabaseHandler getDatabaseHandler() {
		if (dbHandler==null) {
			BgSystemDatabaseHandler dbHandlerToCheck = new BgSystemDatabaseHandler();
			if (dbHandlerToCheck.getSession()!=null && dbHandlerToCheck.getSession().isConnected()==true) {
				dbHandler = dbHandlerToCheck;
			}
		}
		return dbHandler;
	}
	/**
	 * Checks if the database connection is available.
	 * @return true, if is available database connection
	 */
	private boolean isAvailableDatabaseConnection() {
		return (this.getDatabaseHandler()!=null);
	}
	
	/**
	 * Synchronizes DB platform list.
	 */
	private void dbSynchronizePlatformList() {
		
		if (this.isAvailableDatabaseConnection()==false) return;
		try {
			this.getDatabaseHandler().setBgSystemPlatformList(this.getPlatformList());
		} catch (Exception ex) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error while synchronizing background system list into database:");
			ex.printStackTrace();
		}
	}
	/**
	 * Returns the list of available background system platforms from DB.
	 * @return the platform list from DB
	 */
	private List<BgSystemPlatform> dbGetPlatformList() {
		
		if (this.isAvailableDatabaseConnection()==false) return null;
		List<BgSystemPlatform> platformList = null;
		try {
			platformList = this.getDatabaseHandler().getBgSystemPlatformList();
		} catch (Exception ex) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error while aquireing background system list from database:");
			ex.printStackTrace();
		}
		return platformList;
	}

}
