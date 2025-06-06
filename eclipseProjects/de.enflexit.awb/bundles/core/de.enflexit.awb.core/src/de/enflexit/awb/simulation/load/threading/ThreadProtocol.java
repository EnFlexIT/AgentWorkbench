package de.enflexit.awb.simulation.load.threading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Vector;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Transport Protocol class for recorded Thread-Times
 * Holds information about the measurement of all threads on 
 * a machine at a given time.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
@XmlRootElement
public class ThreadProtocol implements Serializable {
	
	private static final long serialVersionUID = 7906666593362238059L;
	
	private long timestamp;
	
	private float loadCPU;
	private double mflops;
	
	private String processID;

	private String containerName;
	private String jvmName;
	private String machineName;
	
	private Vector<ThreadDetail> threadDetails;
	
	
	/**
	 * Instantiates a new thread protocol.
	 */
	public ThreadProtocol() { }
	
	/**
	 * Instantiates a new thread protocol.
	 *
	 * @param timestamp the time stamp
	 * @param loadCPU the load CPU
	 */
	public ThreadProtocol(long timestamp, float loadCPU) {
		this.setTimestamp(timestamp);
		this.setLoadCPU(loadCPU);
	}

	/**
	 * Instantiates a new thread protocol.
	 *
	 * @param timestamp the time stamp
	 * @param processID the process id
	 * @param containerName the container name
	 * @param loadCPU the load CPU
	 * @param threadDetails the thread details
	 */
	public ThreadProtocol(long timestamp, String processID, String containerName, float loadCPU, Vector<ThreadDetail> threadDetails) {
		this.containerName = containerName;
		this.loadCPU = loadCPU;
		this.processID = processID;
		this.timestamp = timestamp;
		this.threadDetails = threadDetails;
	}

	/**
	 * Gets the time stamp.
	 * @return the time stamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Sets the time stamp.
	 * @param timestamp the new time stamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Sets the load CPU.
	 * @param loadCPU the new load CPU
	 */
	public void setLoadCPU(float loadCPU) {
		this.loadCPU = loadCPU;
	}
	
	/**
	 * Gets the load CPU.
	 * @return the load CPU
	 */
	public float getLoadCPU() {
		return loadCPU;
	}
	
	/**
	 * Gets the thread details.
	 * @return the thread details
	 */
	public Vector<ThreadDetail> getThreadDetails() {
		if (threadDetails==null) {
			threadDetails = new Vector<ThreadDetail>();
		}
		return threadDetails;
	}
	
	/**
	 * Sets the thread details.
	 * @param threadDetails the new thread details
	 */
	public void setThreadDetails(Vector<ThreadDetail> threadDetails) {
		this.threadDetails = threadDetails;
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
	 * Gets the machine name.
	 * @return the process ID
	 */
	public String getProcessID() {
		return processID;
	}
	
	/**
	 * Sets the processID .
	 * @param processID the process ID
	 */
	public void setProcessID(String processID) {
		this.processID = processID;
	}
	
	/**
	 * Save.
	 *
	 * @param file2Save the file2 save
	 * @return true, if successful
	 */
	public boolean save(File file2Save) {
		
		boolean saved = true;
		try {			
			JAXBContext pc = JAXBContext.newInstance(this.getClass()); 
			Marshaller pm = pc.createMarshaller(); 
			pm.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
			pm.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 

			Writer pw = new FileWriter(file2Save);
			pm.marshal(this, pw);
			pw.close();

		} catch (Exception e) {
			System.out.println("XML-Error while saving Setup-File!");
			e.printStackTrace();
			saved = false;
		}		
		return saved;		
	}
	
	/**
	 * Adds the specified ThreadProtocol to the current if the time stamps of the protocols are equal.
	 * @param threadProtocol2Add the thread protocol to add
	 */
	public void addThreadProtocol(ThreadProtocol threadProtocol2Add) {
		if (threadProtocol2Add.getTimestamp()==this.getTimestamp()) {
			this.getThreadDetails().addAll(threadProtocol2Add.getThreadDetails());
		}
	}
	
	/**
	 * Load.
	 *
	 * @param file2Read the file2 read
	 * @return true, if successful
	 */
	public boolean load(File file2Read) {
		
		boolean done = true;
		ThreadProtocol tp = null;
		try {
			JAXBContext pc = JAXBContext.newInstance(this.getClass());
			Unmarshaller um = pc.createUnmarshaller();
			FileReader fr = new FileReader(file2Read);
			tp = (ThreadProtocol) um.unmarshal(fr);
			fr.close();
			
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
			return false;
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if (tp!=null) {
			this.setContainerName(tp.getContainerName());
			this.setLoadCPU(tp.getLoadCPU());
			this.setProcessID(tp.getProcessID());
			this.setTimestamp(tp.getTimestamp());
			this.setThreadDetails(tp.getThreadDetails());
		}
		return done;
	}

	/**
	 * Sets the JVM name.
	 * @param jvmName the new JVM name
	 */
	public void setJVMName(String jvmName) {
		this.jvmName = jvmName;
	}

	/**
	 * Sets the machine name.
	 * @param machineName the new machine name
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	
	/**
	 * Gets the JVM name.
	 * @return the JVM name
	 */
	public String getJVMName() {
		return jvmName;
	}

	/**
	 * Gets the machine name.
	 * @return the machine name
	 */
	public String getMachineName() {
		return machineName;
	}

	/**
	 * @return the MFLOPS
	 */
	public double getMflops() {
		return mflops;
	}

	/**
	 * @param mflops the MFLOPS to set
	 */
	public void setMflops(double mflops) {
		this.mflops = mflops;
	}
}
