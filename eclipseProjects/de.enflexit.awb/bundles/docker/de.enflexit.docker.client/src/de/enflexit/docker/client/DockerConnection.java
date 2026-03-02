package de.enflexit.docker.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.EventType;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

/**
 * This is basically a wrapper class for the docker-java library, providing some methods
 * for convenient use and easy integration in AWB-based software tools.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DockerConnection {
	
	private static final String EXTRACT_PORT_NUMBER_REGEX = "\\d+\\.\\d+\\.\\d+\\.\\d+:(\\d+)";
	
	private Properties dockerProperties;
	
	private DockerClientConfig clientConfig;
	private DockerHttpClient httpClient;
	
	private DockerClient dockerClient;
	
	private boolean debug = true;
	
	/**
	 * Instantiates a new docker connection with the default settings (local docker engine, e.g. docker desktop).
	 */
	public DockerConnection() {}

	/**
	 * Instantiates a new docker connection configured with the provided properties.
	 * @param dockerProperties the docker properties
	 */
	public DockerConnection(Properties dockerProperties) {
		this.dockerProperties = dockerProperties;
	}
	
	/**
	 * Gets the client configuration.
	 * @return the client configuration
	 */
	private DockerClientConfig getClientConfiguration() {
		if (this.dockerProperties==null) {
			// --- Use the default configuration ----------------
			clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
		} else {
			// --- Use the provided properties ------------------
			clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withProperties(this.dockerProperties).build();
		}
		return clientConfig;
	}
	
	/**
	 * Gets the HTTP client for interacting with the actual rest API.
	 * @return the HTTP client
	 */
	private DockerHttpClient getHttpClient() {
		if (httpClient==null) {
			httpClient = new ApacheDockerHttpClient.Builder().dockerHost(this.getClientConfiguration().getDockerHost()).build();
		}
		return httpClient;
	}
	
	/**
	 * Gets the docker client for direct API interaction. It is
	 * recommended to use the provided interaction methods instead!  
	 * @return the docker client
	 */
	public DockerClient getDockerClient() {
		if (dockerClient==null) {
			dockerClient = DockerClientImpl.getInstance(this.getClientConfiguration(), this.getHttpClient());
		}
		return dockerClient;
	}
	
	/**
	 * Checks if the docker runtime is available.
	 * @return true, if is available
	 */
	public boolean isAvailable() {
		try {
			// --- Ping command successful -> available -------------
			this.getDockerClient().pingCmd().exec();
			return true;
		} catch (Throwable e) {
			// --- Ping command failed with an error -> not available
			return false;
		}
	}
	
	
	// --- Image management methods -----------------------
	
	/**
	 * Gets the local images list.
	 * @return the local images list
	 */
	public List<Image> getLocalImagesList(){
		if (this.isAvailable()==false) {
			return null;
		} else {
			return this.getDockerClient().listImagesCmd().exec();
		}
	}
	
	/**
	 * Finds the local image with the specified tag.
	 * @param imageTag the image tag
	 * @return the image
	 */
	public Image findLocalImage(String imageTag) {
		
		// --- If no version is specified, assume latest as default
		if (imageTag.contains(":")==false) {
			imageTag = imageTag + ":latest";
		}
		
		List<Image> imagesList = this.getLocalImagesList();
		if (imagesList==null || imagesList.size()==0) {
			return null;
		} else {
			for (Image image : imagesList) {
				if (Arrays.asList(image.getRepoTags()).contains(imageTag)){
					return image;
				}
			}
		}
		return null;
	}
	
//	/**
//	 * Checks if the specified image is locally available.
//	 * @param imageTag the image tag
//	 * @return true, if is image locally available
//	 */
//	private boolean isImageLocallyAvailable(String imageTag) {
//		try {
//			Object result = this.getDockerClient().inspectImageCmd(imageTag);
//			return true;
//		} catch (Exception ex) {
//			return false;
//		}
//	}
	
	private boolean isImageLocallyAvailable(String imageTag) {
		return (this.findLocalImage(imageTag)!=null);
	}
	
	
	/**
	 * Pulls the specified image.
	 * @param imageTag the image tag
	 * @return true, if successful
	 */
	public boolean pullImage(String imageTag) {
		try {
			this.debugPrint("Pulling image " + imageTag + "... ");
			long pullStart = System.currentTimeMillis();
			this.getDockerClient().pullImageCmd(imageTag).start().awaitCompletion();
			long pullEnd = System.currentTimeMillis();
			this.debugPrint("Image pull done after " + ((pullEnd-pullStart)/1000) + " seconds");
			return true;
		} catch (InterruptedException e) {
			this.debugPrint("Error pulling image " + imageTag, true);
			e.printStackTrace();
			return false;
		}
	}

	
	// ----------------------------------------------------
	// --- Image building methods -------------------------
	
	/**
	 * Builds an image based on the specified docker file.
	 * @param dockerFilePath the docker file path
	 * @param imageTag the image tag
	 * @return the image ID
	 */
	public String buildImage(String dockerFilePath, String imageTag) {
		return this.buildImage(new File(dockerFilePath), imageTag, false);
	}
	
	/**
	 * Builds an image based on the specified docker file.
	 * @param dockerFilePath the docker file path
	 * @param imageTag the image tag
	 * @param debugMode if true, the build outputs from docker will be written to the console.
	 * @return the string
	 */
	public String buildImage(String dockerFilePath, String imageTag, boolean debugMode) {
		return this.buildImage(new File(dockerFilePath), imageTag, debugMode);
	}
	
	/**
	 * Builds the image.
	 * @param dockerFile the docker file
	 * @param imageTag the image tag
	 * @return the string
	 */
	public String buildImage(File dockerFile, String imageTag) {
		return this.buildImage(dockerFile, imageTag, false);
	}
	
	/**
	 * Builds an image based on the provided docker file.
	 *
	 * @param dockerFile the docker file
	 * @param imageTag the image tag
	 * @param debugMode if true, the build outputs from docker will be written to the console.
	 * @return the string
	 */
	public String buildImage(File dockerFile, String imageTag, boolean debugMode) {
		
		if (dockerFile.exists()==false) {
			throw new IllegalArgumentException("The specified docker file does not exist!");
		}
		
		Set<String> imageTags = Set.of(imageTag);
		
		this.debugPrint("Starting image build... ");
		long buildStart = System.currentTimeMillis();
		
		BuildImageResultCallback resultCallback = (debugMode==true ? new ConsoleLogBuildImageResultCallback() : new BuildImageResultCallback());
		
		String imageID = this.getDockerClient().buildImageCmd(dockerFile).withPull(true).withNoCache(false).withTags(imageTags).exec(resultCallback).awaitImageId();
		long buildEnd = System.currentTimeMillis();
		this.debugPrint("Image build done after " + ((buildEnd-buildStart)/1000) + " seconds");
		
		return imageID;
	}

	// ----------------------------------------------------
	// --- Container management methods -------------------
	
	/**
	 * Gets the local containers list.
	 * @return the local containers list
	 */
	public List<Container> getContainersList(){
		if (this.isAvailable()==false) {
			return null;
		} else {
			return this.getDockerClient().listContainersCmd().withShowAll(true).exec();
		}
	}
	
	/**
	 * Gets the container by name.
	 * @param containerName the container name
	 * @return the container, null if not found
	 */
	public Container getContainerByName(String containerName) {
		for (Container container : this.getContainersList()) {
			if (Arrays.asList(container.getNames()).contains("/" + containerName)) {
				return container;
			}
		}
		return null;
	}
	
	/**
	 * Gets the local containers that are based on the specified image.
	 * @param imageTag the image tag
	 * @return the containers by image
	 */
	public List<Container> getContainersByImage(String imageTag){
		ArrayList<Container> containers = new ArrayList<Container>();
		for (Container container : this.getContainersList()) {
			if (container.getImage().equals(imageTag)) {
				containers.add(container);
			}
		}
		return containers;
	}
	
	/**
	 * Creates a container.
	 * @param containerConfiguration the container configuration
	 * @return the container ID
	 */
	private String createContainer(String imageTag, String containerName, List<PortBinding> portMappings, List<Bind> volumeMappings, List<String> environmentVariables) {
		HostConfig hostConfig = new HostConfig();
		if (portMappings.size()>0) {
			hostConfig.withPortBindings(portMappings);
		}
		if (volumeMappings.size()>0) {
			hostConfig.withBinds(volumeMappings);
		}
		
		try {
			CreateContainerResponse containerResponse = this.getDockerClient().createContainerCmd(imageTag).withHostConfig(hostConfig).withEnv(environmentVariables).withName(containerName).exec();
			return containerResponse.getId();
		} catch (Exception ex) {
			this.debugPrint("Error creating container " + containerName + " from image " + imageTag, true);
			ex.printStackTrace();
			return null;
		}
	}
	
	// ----------------------------------------------------
	// --- Container starting methods ---------------------
	
	/**
	 * Starts a new container based on the specified image.
	 * @param imageTag the image tag
	 */
	public boolean startContainer(String imageTag) {
		ContainerStartConfiguration startConfiguration = new ContainerStartConfiguration();
		startConfiguration.setImageTag(imageTag);;
		return this.startContainer(startConfiguration);
	}
	
	/**
	 * Starts a new container with the specified name based on the specified image.
	 * @param imageTag the image tag
	 * @param containerName the container name
	 * @return true, if successful
	 */
	public boolean startContainer(String imageTag, String containerName) {
		ContainerStartConfiguration startConfiguration = new ContainerStartConfiguration();
		startConfiguration.setImageTag(imageTag);;
		startConfiguration.setContainerName(containerName);
		return this.startContainer(startConfiguration);
	}
	
	/**
	 * Starts a container based on the provided {@link ContainerStartConfiguration}.
	 * @param containerConfiguration the start configuration
	 * @return true, if successful
	 */
	public boolean startContainer(ContainerStartConfiguration containerConfiguration) {
		
		String imageTag = containerConfiguration.getImageTag();
		
		// --- Pull image if not locally available ------------------
		if (this.isImageLocallyAvailable(imageTag)==false) {
			this.pullImage(imageTag);
		} else {
			this.debugPrint("Image " + imageTag + " locally available");
		}
		
		// --- Still not available -> pulling failed! ---------------
		if (this.isImageLocallyAvailable(imageTag)==false) {
			this.debugPrint("Cannot start container, unable to find image " + imageTag, true);
			return false;
		}

		// --- Check and handle name conflicts ----------------------
		if (containerConfiguration.getContainerName()!=null && containerConfiguration.getContainerName().isBlank()==false) {
			Container existingContainer = this.getContainerByName(containerConfiguration.getContainerName());
			if (existingContainer!=null) {
				switch(containerConfiguration.getNameConflictHandling()) {
				case FAIL:
					// --- No automatic conflict resolution -------------------
					System.err.println("[" + this.getClass().getSimpleName() + "] A container with the name " + containerConfiguration.getContainerName() + " already exists!");
					return false;
				case DELETE:
					// --- Delete the previous container with the same name --- 
					this.debugPrint("A container with the name " + containerConfiguration.getContainerName() + " already exists - deleting!");
					try {
						this.getDockerClient().removeContainerCmd(existingContainer.getId()).exec();
					} catch(ConflictException ce) {
						System.err.println("[" + this.getClass().getName() + "] Unable to delete, probably the container is currently running!");
						return false;
					}
					break;
				case SUFFIX:
					// --- Append a numerical suffix to solve the conflict ----
					String newName = this.appendSuffix(containerConfiguration.getContainerName());
					this.debugPrint("A container with the name " + containerConfiguration.getContainerName() + " already exists - using " + newName + " instead!");
					containerConfiguration.setContainerName(newName);
					break;
				}
			}
		}
		
		// --- Create the container ---------------------------------
		String containerID = this.createContainer(imageTag, containerConfiguration.getContainerName(), containerConfiguration.getPortMappings(),containerConfiguration.getVolumeMappings(), containerConfiguration.getEnvironmentVariablesAsStringList());
		
		// --- Register delete on terminate callback if configured --
		if (containerID!=null) {
			if (containerConfiguration.isDeleteOnTerminate()==true) {
				this.getDockerClient().eventsCmd().withContainerFilter(containerID).exec(new DeleteOnTerminateCallbackAdapter());
			}
			
			// --- Start the container ----------------------------------
			try {
				this.getDockerClient().startContainerCmd(containerID).exec();
			} catch (InternalServerErrorException isee) {
				// --- Container start failed -----------------------
				System.err.print("[" + this.getClass().getSimpleName() + "] Unable to start container: ");
				
				// --- Check error message to determine the cause ---
				if (isee.getMessage().contains("port is already allocated")) {
					// --- Port mapping failed ----------------------
					int hostPort = this.extractPortNumberFromErrorMessage(isee.getMessage());
					System.err.println("Port mapping failed, host port " + hostPort + " already in use!");
				} else {
					// --- Other ------------------------------------
					System.err.println("Unexpected error: " + isee.getMessage());
				}
				
				// --- Remove already created container -------------
				this.getDockerClient().removeContainerCmd(containerID).exec();
				return false;
			}
			this.debugPrint("Container " + containerConfiguration.getContainerName() + " successfully started");
		} else {
			this.debugPrint("Error creating container " + containerConfiguration.getContainerName() + " based on image " + containerConfiguration.getImageTag(), true);
		}
		
		return true;
	}
	
	/**
	 * Extracts the port number from the error message if port bindings failed.
	 * @param errorMessage the error message
	 * @return the port number, -1 if extraction failed
	 */
	private int extractPortNumberFromErrorMessage(String errorMessage) {
		Pattern pattern = Pattern.compile(EXTRACT_PORT_NUMBER_REGEX);
		Matcher matcher = pattern.matcher(errorMessage);
		if (matcher.find()==true) {
			String portString = matcher.group(1);
			return Integer.parseInt(portString);
		} else {
			return -1;
		}
	}
	
	/**
	 * Appends a numeric suffix to a container name to resolve name conflicts.
	 * @param containerName the container name
	 * @return the string
	 */
	private String appendSuffix(String containerName) {
		
		// --- No name conflict -> no suffix needed
		if (this.getContainerByName(containerName)==null) return containerName;
		
		// --- Increment the suffix until there is no conflicting container
		int suffix = 1;
		while (this.getContainerByName(containerName + "_" + String.format("%02d", suffix))!=null) {
			suffix++;
		}
		
		return containerName + "_" + String.format("%02d", suffix);
	}
	
	
	// ------------------------------------------------------------------------
	// --- Callback implementations for handling different docker events ------
	
	/**
	 * This ResultCallback.Adapter will delete containers after termination. This is supposed to be used 
	 * for single containers. Use eventsCmd().withContainerFilter(containerID)... when applying it yourself, 
	 * or just rely on the startContainer method to apply it properly by setting deleteOnTerminate to true
	 * in the {@link ContainerStartConfiguration}.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class DeleteOnTerminateCallbackAdapter extends ResultCallback.Adapter<Event> {
		@Override
		public void onNext(Event event) {
			if (event.getType() == EventType.CONTAINER && event.getAction().equals("die")) {
				DockerConnection.this.debugPrint("Container " + event.getActor().getId() + " terminated");
				DockerConnection.this.getDockerClient().removeContainerCmd(event.getActor().getId()).exec();
				DockerConnection.this.debugPrint("Container " + event.getActor().getId() + " deleted");
				try {
					this.close();
				} catch (IOException e) {
					DockerConnection.this.debugPrint("Error closing callback adapter for container " + event.getActor().getId(), true);
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * This BuildImageResultCallback subclass will print the
	 * log outputs to the console for build debugging purposes.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class ConsoleLogBuildImageResultCallback extends BuildImageResultCallback{
		@Override
		public void onNext(BuildResponseItem item) {
			if (item.getStream() != null) {
	            System.out.print(item.getStream());
	        }
	        super.onNext(item);
		}
	}
	
	private void debugPrint(String message) {
		this.debugPrint(message, false);
	}
	
	private void debugPrint(String message, boolean isError) {
		if (this.debug==true) {
			if (isError==false) {
				System.out.println("[" + this.getClass().getSimpleName() + "] " + message);
			} else {
				System.err.println("[" + this.getClass().getSimpleName() + "] " + message);
			}
		}
	}
	
}
