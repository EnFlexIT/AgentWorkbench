package de.enflexit.docker.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.PortBinding;

/**
 * This class contains the relevant options to start a docker container.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ContainerStartConfiguration {
	
	/**
	 * This enumeration is used to specify how to handle container name conflicts.
	 * - DELETE: Will delete the previous container with the same name before starting a new one
	 * - FAIL: The container will not be started
	 * - SUFFIX: A numerical suffix will be appended to the name of the new container  
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	public enum NameConflictHandling {
		DELETE, FAIL, SUFFIX
	}
	
	private String imageTag;
	private String containerName;
	
	private ArrayList<PortBinding> portMappings;
	private ArrayList<Bind> volumeMappings;
	
	private HashMap<String, String> environmentVariables;
	
	private boolean deleteOnTerminate;
	
	// --- Do not automatically resolve name conflicts by default
	private NameConflictHandling nameConflictHandling = NameConflictHandling.FAIL;

	/**
	 * Gets the image tag.
	 * @return the image tag
	 */
	public String getImageTag() {
		return imageTag;
	}
	/**
	 * Sets the image tag.
	 * @param imageTag the new image tag
	 */
	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
	}

	/**
	 * Gets the container name.
	 * @return the container name
	 */
	public String getContainerName() {
		return containerName;
	}

	/**
	 * Sets the container name.
	 * @param containerName the new container name
	 */
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	/**
	 * Checks if is delete on terminate.
	 * @return true, if is delete on terminate
	 */
	public boolean isDeleteOnTerminate() {
		return deleteOnTerminate;
	}

	/**
	 * Sets the delete on terminate.
	 * @param deleteOnTerminate the new delete on terminate
	 */
	public void setDeleteOnTerminate(boolean deleteOnTerminate) {
		this.deleteOnTerminate = deleteOnTerminate;
	}
	
	/**
	 * Gets the name conflict handling strategy.
	 * @return the name conflict handling strategy
	 */
	public NameConflictHandling getNameConflictHandling() {
		return nameConflictHandling;
	}
	
	/**
	 * Sets the name conflict handling strategy.
	 * @param nameConflictHandling the new name conflict handling strategy
	 */
	public void setNameConflictHandling(NameConflictHandling nameConflictHandling) {
		this.nameConflictHandling = nameConflictHandling;
	}
	/**
	 * Gets the port mappings.
	 * @return the port mappings
	 */
	public ArrayList<PortBinding> getPortMappings() {
		if (portMappings==null) {
			portMappings = new ArrayList<PortBinding>();
		}
		return portMappings;
	}
	
	/**
	 * Adds a port mapping to the configuration.
	 * @param portMapping the port mapping
	 */
	public void addPortMapping(PortBinding portMapping) {
		this.getPortMappings().add(portMapping);
	}
	
	/**
	 * Adds a port mapping to the configuration.
	 * @param containerPort the container port
	 * @param hostPort the host port
	 */
	public void addPortMapping(int containerPort, int hostPort) {
		this.addPortMapping(PortBinding.parse(String.valueOf(hostPort) + ":" + String.valueOf(containerPort)));
	}
	
	/**
	 * Gets the volume mappings.
	 * @return the volume mappings
	 */
	public ArrayList<Bind> getVolumeMappings() {
		if (volumeMappings==null) {
			volumeMappings = new ArrayList<Bind>();
		}
		return volumeMappings;
	}
	/**
	 * Adds a volume mapping to the configuration.
	 * @param volumeMapping the volume mapping
	 */
	public void addVolumeMapping(Bind volumeMapping) {
		this.getVolumeMappings().add(volumeMapping);
	}
	
	/**
	 * Adds a volume mapping to the configuration.
	 * @param hostPath the host path
	 * @param containerPath the container path
	 */
	public void addVolumeMapping(String hostPath, String containerPath) {
		this.addVolumeMapping(Bind.parse(hostPath + ":" + containerPath));
	}
	
	/**
	 * Gets the environment variables.
	 * @return the environment variables
	 */
	public HashMap<String, String> getEnvironmentVariables() {
		if (environmentVariables==null) {
			environmentVariables = new HashMap<String, String>();
		}
		return environmentVariables;
	}
	
	/**
	 * Adds the environment variable.
	 * @param name the name
	 * @param value the value
	 */
	public void addEnvironmentVariable(String name, String value) {
		this.getEnvironmentVariables().put(name, value);
	}
	
	/**
	 * Gets the environment variables as list of Strings "NAME=VALUE",
	 *  as required when starting containers.
	 * @return the environment variables as string list
	 */
	public ArrayList<String> getEnvironmentVariablesAsStringList(){
		ArrayList<String> envVarsList = new ArrayList<String>();
		for (String varName : this.getEnvironmentVariables().keySet()) {
			String varValue = this.getEnvironmentVariables().get(varName);
			envVarsList.add(varName + "=" + varValue);
		}
		return envVarsList;
	}
	

}
