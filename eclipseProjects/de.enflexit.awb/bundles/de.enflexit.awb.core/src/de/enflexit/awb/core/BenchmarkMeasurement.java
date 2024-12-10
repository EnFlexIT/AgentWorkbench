package de.enflexit.awb.core;

import javax.swing.SwingUtilities;

import de.enflexit.awb.core.ui.AgentWorkbenchUiManager;
import de.enflexit.awb.core.ui.AwbBenchmarkMonitor;
import de.enflexit.awb.simulation.load.LoadMeasureThread;
import de.enflexit.oshi.SystemIDGeneration;
import jnt.scimark2.Constants;
import jnt.scimark2.Kernel;
import jnt.scimark2.Random;

/**
 * This thread is doing the actual benchmark measurements, which 
 * results to the capability of a computer system to crunch numbers.<br> 
 * This will be comparable through a benchmark value in 
 * Mflops (Million floating point operations per second).<br><br>
 * 
 * The benchmark value can be obtained by using the method<br>  
 * 'Application.RunInfo.getBenchValue();'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BenchmarkMeasurement extends Thread {

	private boolean forceBenchmark = false;
	private boolean isHeadlessOperation = Application.isOperatingHeadless();

	private float benchValueOld = Application.getGlobalInfo().getBenchValue();
	private boolean benchAllwaysSkip = Application.getGlobalInfo().isBenchAlwaysSkip();
	private String benchExecOn = Application.getGlobalInfo().getBenchExecOn();
	private String localSystemID;
	
	private double min_time = Constants.RESOLUTION_DEFAULT;
	private int FFT_size = Constants.FFT_SIZE;
	private int SOR_size =  Constants.SOR_SIZE;
	private int Sparse_size_M = Constants.SPARSE_SIZE_M;
	private int Sparse_size_nz = Constants.SPARSE_SIZE_nz;
	private int LU_size = Constants.LU_SIZE;

	private AwbBenchmarkMonitor monitor;
	
	private Object synchronizationObject;
	
	/**
	 * The constructor of this class.<br>
	 * If <i>forceBenchmark</i> is set to true the benchmark will be executed regardless of
	 * the settings in the property file or if the system was executed in the same network
	 * already or not.
	 *
	 * @param forceBenchmark the force benchmark
	 */
	public BenchmarkMeasurement(boolean forceBenchmark) {
		this.forceBenchmark = forceBenchmark;	
	}
	/**
	 * Depending on the FileProperties this will measure the ability of this computer to crunch numbers.<br>
	 * The results will be given in Mflops (Millions of floating point operations per second)  
	 */
	@Override
	public void run() {

		super.run();
		try {
			// --- Name thread / indicate state ---------------------
			synchronized (this.getSynchronizationObject()) {
				this.setName("SciMark2-Benchmark");
				Application.setBenchmarkRunning(true);
				this.getSynchronizationObject().notify();
			}
			
			// --- Criteria to not execute the benchmark ------------
			if (isBenchmarkRequired()==false && forceBenchmark==false) {
				return;
			}  
			
			// --- Initialize Benchmark-Monitor  --------------------
			if (this.getBenchmarkMonitor()!=null) {
				this.getBenchmarkMonitor().setVisible(true);
			} else {
				System.out.println("Executing Benchmark, please wait ... ");
			}
			
			// --- Start benchmark tests ----------------------------
			double res[] = new double[6];
			Random R = new Random(Constants.RANDOM_SEED);

			this.setBenchmarkProgress(1);
			res[1] = Kernel.measureFFT(FFT_size, min_time, R);
			if (this.isSkipAction()) return;
			
			this.setBenchmarkProgress(2);
			res[2] = Kernel.measureSOR(SOR_size, min_time, R);
			if (this.isSkipAction()) return;
			
			this.setBenchmarkProgress(3);
			res[3] = Kernel.measureMonteCarlo(min_time, R);
			if (this.isSkipAction()) return;
			
			this.setBenchmarkProgress(4);
			res[4] = Kernel.measureSparseMatmult(Sparse_size_M, Sparse_size_nz, min_time, R);
			if (this.isSkipAction()) return;
			
			this.setBenchmarkProgress(5);
			res[5] = Kernel.measureLU(LU_size, min_time, R);
			if (this.isSkipAction()) return;

			this.setBenchmarkProgress(6);
			res[0] = (res[1] + res[2] + res[3] + res[4] + res[5]) / 5;
			
			System.out.println("=> Average Benchmark Result: " + Math.round(res[0]) + " Mflops");
//			System.out.println("FFT:           " + Math.round(res[1]) + " Mflops");
//			System.out.println("SOR:           " + Math.round(res[2]) + " Mflops");
//			System.out.println("MonteCarlo:    " + Math.round(res[3]) + " Mflops");
//			System.out.println("SparseMatmult: " + Math.round(res[4]) + " Mflops");
//			System.out.println("LU:            " + Math.round(res[5]) + " Mflops");
//			System.out.println("A V E R A G E: " + Math.round(res[0]) + " Mflops");

			float result  = (float)Math.round((float)res[0]*100)/100;
			if (this.isSkipAction()) return;

			// --- Store result in LoadMeasurThread -----------------
			LoadMeasureThread.setCompositeBenchmarkValue(result);
			Application.getGlobalInfo().setBenchValue(result);
			Application.getGlobalInfo().setBenchExecOn(this.getLocalSystemID());
			Application.getGlobalInfo().setBenchAlwaysSkip(benchAllwaysSkip);
			Application.getGlobalInfo().doSaveConfiguration();
			
			// --- Progress Display --- OFF -------------------------
			if (this.isHeadlessOperation==false) {
				this.getBenchmarkMonitor().setBenchmarkValue(result);
				Thread.sleep(1000);
				this.closeGUI();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			Application.setBenchmarkRunning(false);	
		}
	}
	
	/**
	 * Returns the visual BenchmarkMonitor (if the application is not running headless).
	 * @return the benchmark monitor
	 */
	private AwbBenchmarkMonitor getBenchmarkMonitor() {
		if (monitor==null && this.isHeadlessOperation==false) {
			// --- Initialize BenchmarkMonitor ------------ 
			monitor = AgentWorkbenchUiManager.getInstance().getBenchmarkMonitor();
			// --- Set user buttons -----------------------
			if (this.benchValueOld>0 && this.getLocalSystemID().equalsIgnoreCase(benchExecOn)) {
				monitor.setEnableSkipButton(true);
				if (forceBenchmark==true) {
					monitor.setEnableSkipAlwaysButton(false);
				} else {
					monitor.setEnableSkipAlwaysButton(true);
				}
			} else {
				monitor.setEnableSkipButton(false);
				monitor.setEnableSkipAlwaysButton(false);
			}
			
			// --- Progress Display --- ON ----------------
			monitor.setBenchmarkValue(this.benchValueOld);
			monitor.setProgressMinimum(0);
			monitor.setProgressMaximum(6);
			monitor.setProgressValue(0);
			
		}
		return monitor;
	}
	
	/**
	 * Sets the benchmark progress.
	 * @param n the new benchmark progress
	 */
	private void setBenchmarkProgress(final int n) {
		if (this.getBenchmarkMonitor()!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					getBenchmarkMonitor().setProgressValue(n);
				}
			});	
		}
	}
	
	/**
	 * This Method checks if a skip-button was pressed on the Monitor-GUI.
	 * @return true, if is skip action
	 */
	private boolean isSkipAction() {
		if (this.getBenchmarkMonitor()!=null) {
			if (this.getBenchmarkMonitor().isSkipAlways()==true) {
				Application.getGlobalInfo().setBenchAlwaysSkip(true);
				this.closeGUI();
				Application.setBenchmarkRunning(false);
				return true;
			}
			if (this.getBenchmarkMonitor().isSkip()==true) {
				this.closeGUI();
				Application.setBenchmarkRunning(false);
				return true;
			}	
		}
		return false;
	}
	
	/**
	 * This method closes the benchmark monitor GUI.
	 */
	private void closeGUI() {
		if (this.getBenchmarkMonitor()!=null) {
			this.getBenchmarkMonitor().setVisible(false);
			this.monitor=null;	
		}
	}

	/**
	 * Returns the MAC-Address local 'CanonicalHostName' to identify
	 * on which computer this measurement were executed.
	 * 
	 * @return an identifier for the local system
	 */
	private String getLocalSystemID() {
		if (localSystemID==null) {
			localSystemID = SystemIDGeneration.getSystemIdentifier();
		}
		return localSystemID;
	}
	
	
	
	/**
	 * Checks if the benchmark is required.
	 * @return true, if is benchmark required
	 */
	public static boolean isBenchmarkRequired() {
		
		float benchValueOld = Application.getGlobalInfo().getBenchValue();
		boolean benchAllwaysSkip = Application.getGlobalInfo().isBenchAlwaysSkip();
		String benchExecOn = Application.getGlobalInfo().getBenchExecOn();
		String localSystemID = SystemIDGeneration.getSystemIdentifier();
		
		boolean isRequired = true;
		if (benchValueOld>0 && localSystemID.equalsIgnoreCase(benchExecOn) && benchAllwaysSkip==true) {
			isRequired = false;
		}  
		return isRequired;
	}
	
	/**
	 * Gets the synchronization object, which can be used to wait until the benchmark thread has properly started
	 * @return the synchronization object
	 */
	public Object getSynchronizationObject() {
		if(synchronizationObject == null) {
			synchronizationObject = new Object();
		}
		return synchronizationObject;
	}
	
}
