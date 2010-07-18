package load;

import jade.domain.introspection.AddedBehaviour;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;


public class LoadMeasureJVM{

	private static final long serialVersionUID = -5202880756096638141L;
	
	// JVM Memory information
	// ////////////////////////////////
	private MemoryMXBean memXB = null;
	private long totalMemoryUsageJVM;               
    private long freeMemorryUsageJVM;            
    private long currentHeapInitJVM;  
    private long maximumHeapSizeJVM;            
    private long currentHeapSizeJVM;
    private long currentHeapFreeJVM;
	
	public LoadMeasureJVM() {

		//JVM information
		////////////////////////////
		memXB = ManagementFactory.getMemoryMXBean();
		totalMemoryUsageJVM =0;               
		freeMemorryUsageJVM = 0;            
	    currentHeapInitJVM =0;  
	    maximumHeapSizeJVM = 0;            
		currentHeapSizeJVM =0;
		setCurrentHeapFreeJVM(0);
				
	}
	

	
		//JVM Memory information
	public void measureLoadOfSystem() {

		long heapUse = memXB.getHeapMemoryUsage().getInit();
		long heapInit =  memXB.getHeapMemoryUsage().getUsed();
		long heapMax  = memXB.getHeapMemoryUsage().getMax();
		long heapFree  = heapMax-(heapInit+heapUse);
		setcurrentHeapInitJVM(heapUse/1048576);
		setCurrentHeapSizeJVM(heapInit/1048576);
		setMaximumHeapSizeJVM(heapMax/1048576);
		setCurrentHeapFreeJVM(heapFree/1048576);
		
		System.out.println(" init "+heapInit/1048576);
		System.out.println(" use "+heapUse/1048576);
		System.out.println(" max "+heapMax/1048576);
		System.out.println(" FreeHeap "+heapFree/1048576);
		
	}

		/**
		 * @param totalMemoryUsageJVM the totalMemoryUsageJVM to set
		 */
		public void setTotalMemoryUsageJVM(long totalMemoryUsageJVM) {
			this.totalMemoryUsageJVM = totalMemoryUsageJVM;
		}

		/**
		 * @return the totalMemoryUsageJVM
		 */
		public long getTotalMemoryUsageJVM() {
			return totalMemoryUsageJVM;
		}

		/**
		 * @param freeMemorryUsageJVM the freeMemorryUsageJVM to set
		 */
		public void setFreeMemorryUsageJVM(long freeMemorryUsageJVM) {
			this.freeMemorryUsageJVM = freeMemorryUsageJVM;
		}

		/**
		 * @return the freeMemorryUsageJVM
		 */
		public long getFreeMemorryUsageJVM() {
			return freeMemorryUsageJVM;
		}

		/**
		 * @param currentHeapInitJVM the currentHeapInitJVM to set
		 */
		public void setcurrentHeapInitJVM(long currentHeapInitJVM) {
			this.currentHeapInitJVM = currentHeapInitJVM;
		}

		/**
		 * @return the currentHeapInitJVM
		 */
		public long getcurrentHeapInitJVM() {
			return currentHeapInitJVM;
		}

		/**
		 * @param maximumHeapSizeJVM the maximumHeapSizeJVM to set
		 */
		public void setMaximumHeapSizeJVM(long maximumHeapSizeJVM) {
			this.maximumHeapSizeJVM = maximumHeapSizeJVM;
		}

		/**
		 * @return the maximumHeapSizeJVM
		 */
		public long getMaximumHeapSizeJVM() {
			return maximumHeapSizeJVM;
		}

		/**
		 * @param currentHeapSizeJVM the currentHeapSizeJVM to set
		 */
		public void setCurrentHeapSizeJVM(long currentHeapSizeJVM) {
			this.currentHeapSizeJVM = currentHeapSizeJVM;
		}

		/**
		 * @return the currentHeapSizeJVM
		 */
		public long getCurrentHeapSizeJVM() {
			return currentHeapSizeJVM;
		}

		/**
		 * @param currentHeapFreeJVM the currentHeapFreeJVM to set
		 */
		public void setCurrentHeapFreeJVM(long currentHeapFreeJVM) {
			this.currentHeapFreeJVM = currentHeapFreeJVM;
		}

		/**
		 * @return the currentHeapFreeJVM
		 */
		public long getCurrentHeapFreeJVM() {
			return currentHeapFreeJVM;
		}

}
