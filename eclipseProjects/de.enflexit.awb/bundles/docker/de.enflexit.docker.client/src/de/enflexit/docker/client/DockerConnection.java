package de.enflexit.docker.client;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
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
	
	private Properties dockerProperties;
	
	private DockerClientConfig clientConfig;
	private DockerHttpClient httpClient;
	
	private DockerClient dockerClient;
	
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
	
	/**
	 * Checks if the specified image is locally available.
	 * @param imageTag the image tag
	 * @return true, if is image locally available
	 */
	private boolean isImageLocallyAvailable(String imageTag) {
		try {
			this.getDockerClient().inspectImageCmd(imageTag);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	
	/**
	 * Pulls the specified image.
	 * @param imageTag the image tag
	 * @return true, if successful
	 */
	public boolean pullImage(String imageTag) {
		try {
			System.out.print("[" + this.getClass().getSimpleName() + "] Pulling image " + imageTag + "... ");
			long pullStart = System.currentTimeMillis();
			this.getDockerClient().pullImageCmd(imageTag).start().awaitCompletion();
			long pullEnd = System.currentTimeMillis();
			System.out.println("done after " + ((pullEnd-pullStart)/1000) + " seconds");
			return true;
		} catch (InterruptedException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error pulling image " + imageTag);
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
		
		System.out.print("[" + this.getClass().getSimpleName() + "] Starting image build... ");
		long buildStart = System.currentTimeMillis();
		
		BuildImageResultCallback resultCallback = (debugMode==true ? new ConsoleLogBuildImageResultCallback() : new BuildImageResultCallback());
		
		String imageID = this.getDockerClient().buildImageCmd(dockerFile).withPull(true).withNoCache(false).withTags(imageTags).exec(resultCallback).awaitImageId();
		long buildEnd = System.currentTimeMillis();
		System.out.println("done after " + ((buildEnd-buildStart)/1000) + " seconds");
		
		return imageID;
	}

	// ----------------------------------------------------
	// --- Container management methods -------------------
	
	/**
	 * Gets the local containers list.
	 * @return the local containers list
	 */
	public List<Container> getLocalContainersList(){
		if (this.isAvailable()==false) {
			return null;
		} else {
			return this.getDockerClient().listContainersCmd().withShowAll(true).exec();
		}
	}
	
	/**
	 * Creates a container.
	 * @param containerConfiguration the container configuration
	 * @return the container ID
	 */
	private String createContainer(String imageTag, String containerName, List<PortBinding> portMappings, List<Bind> volumeMappings) {
		HostConfig hostCOnfig = new HostConfig();
		if (portMappings.size()>0) {
			hostCOnfig.withPortBindings(portMappings);
		}
		if (volumeMappings.size()>0) {
			hostCOnfig.withBinds(volumeMappings);
		}
		
		try {
			CreateContainerResponse containerResponse = this.getDockerClient().createContainerCmd(imageTag).withHostConfig(hostCOnfig).withName(containerName).exec();
			return containerResponse.getId();
		} catch (Exception ex) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error creating container " + containerName + " from image " + imageTag);
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
	 * @param startConfiguration the start configuration
	 * @return true, if successful
	 */
	public boolean startContainer(ContainerStartConfiguration startConfiguration) {
		
		String imageTag = startConfiguration.getImageTag();
		
		// --- Pull image if not locally available ------------------
		if (this.isImageLocallyAvailable(imageTag)==false) {
			this.pullImage(imageTag);
		} else {
			System.out.println("[" + this.getClass().getSimpleName() + "] Image " + imageTag + " locally available");
		}
		
		// --- Still not available -> pulling failed! ---------------
		if (this.isImageLocallyAvailable(imageTag)==false) {
			System.err.println("[" + this.getClass().getName() + "] Cannot start container, unable to find image " + imageTag);
			return false;
		}
		
		// --- Create the container ---------------------------------
		String containerID = this.createContainer(imageTag, startConfiguration.getContainerName(), startConfiguration.getPortMappings(),startConfiguration.getVolumeMappings());
		
		// --- Register delete on terminate callback if configured --
		if (containerID!=null) {
			if (startConfiguration.isDeleteOnTerminate()==true) {
				this.getDockerClient().eventsCmd().withContainerFilter(containerID).exec(new DeleteOnTerminateCallbackAdapter());
			}
		}
		
		// --- Start the container ----------------------------------
		this.getDockerClient().startContainerCmd(containerID).exec();
		
		return true;
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
				System.out.println("Container " + event.getActor().getId() + " terminated");
				DockerConnection.this.getDockerClient().removeContainerCmd(event.getActor().getId()).exec();
				System.out.println("Container " + event.getActor().getId() + " deleted");
				try {
					this.close();
				} catch (IOException e) {
					System.err.println("Error closing callback adapter for container " + event.getActor().getId());
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
	
}
