package de.enflexit.docker.client;

import java.util.ArrayList;

import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.PortBinding;

/**
 * This class contains the relevant options to start a docker container.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ContainerStartConfiguration {
	
	private String imageTag;
	private String containerName;
	
	private ArrayList<PortBinding> portMappings;
	private ArrayList<Bind> volumeMappings;
	
	private boolean deleteOnTerminate;

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

}
