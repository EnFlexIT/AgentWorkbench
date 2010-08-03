package mas.service.load;

import java.util.ArrayList;

public class LoadMeasureAvgJVM {


	private Integer useAVGCounter = 0;//maximum length of List
	private ArrayList<LoadMeasureJVM> measureList = new ArrayList<LoadMeasureJVM>();
	
	private long jvmMemoFree = 0;			// KB 
	private long jvmMemoTotal = 0;			// KB
	private long jvmMemoMax = 0;			// KB
   
    private long jvmHeapUsed = 0;  			// KB
    private long jvmHeapCommited = 0;  		// KB            
    private long jvmHeapMax = 0;  			// KB
    private long jvmHeapInit = 0;  			// KB

	public LoadMeasureAvgJVM(Integer avgCounter) {
		useAVGCounter = avgCounter;                     //maximum length of List
	}

	public void put(LoadMeasureJVM currentLoadMeasure) {

		if (measureList.size() >= useAVGCounter) {
			measureList.remove(0);
		}
		measureList.add(currentLoadMeasure); //add new object in the list
		this.calculateLoadAverage();
	}
	
	/**
	 * 
	 */
	private void calculateLoadAverage() {
      
		int size = measureList.size();
		
		long tmpJvmMemoFree = 0;	 
		long tmpJvmMemoTotal = 0;	
		long tmpJvmMemoMax = 0;	
	   
	    long tmpJvmHeapUsed = 0;  	
	    long tmpJvmHeapCommited = 0;  		            
	    long tmpJvmHeapMax = 0;  			
	    long tmpJvmHeapInit = 0;  			

	    
		//calculating JVM memory average value
		for (int i = 0; i < size; i++) {

			tmpJvmMemoFree += measureList.get(i).getJvmMemoFree();
			tmpJvmMemoTotal += measureList.get(i).getJvmMemoTotal();
			tmpJvmMemoMax += measureList.get(i).getJvmMemoMax();

			tmpJvmHeapUsed += measureList.get(i).getJvmHeapUsed();
			tmpJvmHeapCommited += measureList.get(i).getJvmHeapCommited();
			tmpJvmHeapMax += measureList.get(i).getJvmHeapMax();
			tmpJvmHeapInit += measureList.get(i).getJvmHeapInit();
			
		}	
	    
		jvmMemoFree = tmpJvmMemoFree / size;
		jvmMemoTotal = tmpJvmMemoTotal / size;
		jvmMemoMax = tmpJvmMemoMax / size;

		jvmHeapUsed = tmpJvmHeapUsed / size;
		jvmHeapCommited = tmpJvmHeapCommited / size;
		jvmHeapMax = tmpJvmHeapMax / size;
		jvmHeapInit = tmpJvmHeapInit / size;

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
	 * @return the jvmHeapMax
	 */
	public long getJvmHeapMax() {
		return jvmHeapMax;
	}
	/**
	 * @param jvmHeapMax the jvmHeapMax to set
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


}
