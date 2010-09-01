package mas.service.load;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Hashtable;

public class LoadMeasureJVM{

	private static final long serialVersionUID = -5202880756096638141L;
	
	private long jvmMemoFree = 0;			// Byte 
	private long jvmMemoTotal = 0;			// Byte
	private long jvmMemoMax = 0;			// Byte
   
	private MemoryMXBean memXB = ManagementFactory.getMemoryMXBean();
    private long jvmHeapUsed = 0;  			// Byte
    private long jvmHeapCommited = 0;  		// Byte
    private long jvmHeapMax = 0;  			// Byte
    private long jvmHeapInit = 0;  			// Byte
	
    private ThreadMXBean threadXB = ManagementFactory.getThreadMXBean();
    boolean threadMonitoringSupported = threadXB.isThreadContentionMonitoringSupported();
    boolean threadCpuTimeSupported = threadXB.isThreadCpuTimeSupported(); 
    private int jvmThreadCount = 0;
    private Hashtable<String, Long> jvmThreadTimes = null;
    
    public LoadMeasureJVM() {
    	
    	if (threadMonitoringSupported) {
			threadXB.setThreadContentionMonitoringEnabled(true);
			//System.out.println("ThreadContentionMonitoring: enabled");
		}
    	if (threadCpuTimeSupported) {
    		threadXB.setThreadCpuTimeEnabled(true);
    		//System.out.println("ThreadCpuTimeMonitoring: enabled");
    	}
    }
	
    /**
     * This method measures the current load of th system 
     */
	public void measureLoadOfSystem() {

		jvmMemoFree  = Runtime.getRuntime().freeMemory();
		jvmMemoTotal = Runtime.getRuntime().totalMemory();
		jvmMemoMax   = Runtime.getRuntime().maxMemory();
		
		jvmHeapInit     = memXB.getHeapMemoryUsage().getInit();
		jvmHeapMax      = memXB.getHeapMemoryUsage().getMax();
		jvmHeapUsed     = memXB.getHeapMemoryUsage().getUsed();
		jvmHeapCommited = memXB.getHeapMemoryUsage().getCommitted();    
		
		if (threadXB.isObjectMonitorUsageSupported()) {
			
			jvmThreadCount = threadXB.getThreadCount();

			long[] jvmThreadIDs = threadXB.getAllThreadIds();
			ThreadInfo[] jvmThreadInfo = threadXB.getThreadInfo(jvmThreadIDs);
			jvmThreadTimes = new Hashtable<String, Long>();
			for (int i = 0; i < jvmThreadInfo.length; i++) {
				String threadName = jvmThreadInfo[i].getThreadName();
				long threadID = jvmThreadInfo[i].getThreadId();
				jvmThreadTimes.put(threadName, threadXB.getThreadCpuTime(threadID));
			}
		}
		
	}

	/**
	 * This method return treu/false if a Thread, given by it's name exists or not
	 * @param threadName2LookAt
	 * @return boolean
	 */
	public boolean threadExists(String threadName2LookAt) {
		
		boolean exists = false;
		
		long[] jvmThreadIDs = threadXB.getAllThreadIds();
		ThreadInfo[] jvmThreadInfo = threadXB.getThreadInfo(jvmThreadIDs);
		for (int i = 0; i < jvmThreadInfo.length; i++) {
			String threadName = jvmThreadInfo[i].getThreadName();
			if (threadName.equalsIgnoreCase(threadName2LookAt)) {
				exists = true;
				break;
			}
		}
		return exists;		
	}
	
	/**
	 * @return the jvmMemoFree
	 */
	public long getJvmMemoFree() {
		return jvmMemoFree;
	}
	/**
	 * @param jvmMemoFree the jvmMemoFree to set
	 */
	public void setJvmMemoFree(long jvmMemoFree) {
		this.jvmMemoFree = jvmMemoFree;
	}

	/**
	 * @return the jvmMemoTotal
	 */
	public long getJvmMemoTotal() {
		return jvmMemoTotal;
	}
	/**
	 * @param jvmMemoTotal the jvmMemoTotal to set
	 */
	public void setJvmMemoTotal(long jvmMemoTotal) {
		this.jvmMemoTotal = jvmMemoTotal;
	}

	/**
	 * @return the jvmMemoMax
	 */
	public long getJvmMemoMax() {
		return jvmMemoMax;
	}
	/**
	 * @param jvmMemoMax the jvmMemoMax to set
	 */
	public void setJvmMemoMax(long jvmMemoMax) {
		this.jvmMemoMax = jvmMemoMax;
	}

	/**
	 * @return the jvmHeapUsed
	 */
	public long getJvmHeapUsed() {
		return jvmHeapUsed;
	}
	/**
	 * @param jvmHeapUsed the jvmHeapUsed to set
	 */
	public void setJvmHeapUsed(long jvmHeapUsed) {
		this.jvmHeapUsed = jvmHeapUsed;
	}

	/**
	 * @return the jvmHeapCommited
	 */
	public long getJvmHeapCommited() {
		return jvmHeapCommited;
	}
	/**
	 * @param jvmHeapCommited the jvmHeapCommited to set
	 */
	public void setJvmHeapCommited(long jvmHeapCommited) {
		this.jvmHeapCommited = jvmHeapCommited;
	}

	/**
	 * @return the jvmHeapMaxt
	 */
	public long getJvmHeapMax() {
		return jvmHeapMax;
	}
	/**
	 * @param jvmHeapMaxt the jvmHeapMaxt to set
	 */
	public void setJvmHeapMax(long jvmHeapMax) {
		this.jvmHeapMax = jvmHeapMax;
	}

	/**
	 * @return the jvmHeapInit
	 */
	public long getJvmHeapInit() {
		return jvmHeapInit;
	}
	/**
	 * @param jvmHeapInit the jvmHeapInit to set
	 */
	public void setJvmHeapInit(long jvmHeapInit) {
		this.jvmHeapInit = jvmHeapInit;
	}

	/**
	 * @return the jvmThreadCount
	 */
	public int getJvmThreadCount() {
		return jvmThreadCount;
	}
	/**
	 * @param jvmThreadCount the jvmThreadCount to set
	 */
	public void setJvmThreadCount(int jvmThreadCount) {
		this.jvmThreadCount = jvmThreadCount;
	}

	/**
	 * @return the jvmThreadTimes
	 */
	public Hashtable<String, Long> getJvmThreadTimes() {
		return jvmThreadTimes;
	}
	/**
	 * @param jvmThreadTimes the jvmThreadTimes to set
	 */
	public void setJvmThreadTimes(Hashtable<String, Long> jvmThreadTimes) {
		this.jvmThreadTimes = jvmThreadTimes;
	}

	

}
