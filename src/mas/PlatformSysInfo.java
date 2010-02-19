package mas;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.MemoryMXBean;

public class PlatformSysInfo {
    
	public static void getLoadAverage() {
        final OperatingSystemMXBean osStats = ManagementFactory.getOperatingSystemMXBean();
        final double loadAverage = osStats.getSystemLoadAverage();
        //System.out.println(String.format("load average: %f", loadAverage));
        //getJavaInfo();
    }
	
	public static void getJavaInfo() {
		
		final MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
		/*System.out.println( mxb.getObjectPendingFinalizationCount());
		System.out.println( mxb.getHeapMemoryUsage().getInit() );
		System.out.println( mxb.getHeapMemoryUsage().getUsed() );
		System.out.println( mxb.getHeapMemoryUsage().getMax() );
		*/
		System.out.println("freeMemory(): " + Runtime.getRuntime().freeMemory());
		System.out.println("totalMemory(): " + Runtime.getRuntime().totalMemory());
		System.out.println("maxMemory(): " + Runtime.getRuntime().maxMemory());
		System.out.println("totalMemory() - freeMemory(): "     + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		System.out.println();

		System.out.println("getUsed(): " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed());
		System.out.println("getCommitted(): " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted());
		System.out.println("getMax(): " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax());
		System.out.println("getInit(): " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getInit());

		
	}
	
}
