package agentgui.core.benchmark;

import java.net.InetAddress;
import java.net.UnknownHostException;

import agentgui.core.application.Application;
import agentgui.simulationService.load.LoadMeasureThread;
import jnt.scimark2.Constants;
import jnt.scimark2.Random;
import jnt.scimark2.kernel;

public class BenchmarkMeasurement extends Thread {

	private BenchmarkMonitor benchGUI = null;
	
	private float benchValueOld = Application.RunInfo.getBenchValue();
	private boolean benchAllwaysSkip = Application.RunInfo.isBenchAllwaysSkip();
	private boolean forceBench = false;
	private String benchExecOn = Application.RunInfo.getBenchExecOn();
	private String nowExecOn = null;
	
	private double min_time = Constants.RESOLUTION_DEFAULT;

	private int FFT_size = Constants.FFT_SIZE;
	private int SOR_size =  Constants.SOR_SIZE;
	private int Sparse_size_M = Constants.SPARSE_SIZE_M;
	private int Sparse_size_nz = Constants.SPARSE_SIZE_nz;
	private int LU_size = Constants.LU_SIZE;
	
	public BenchmarkMeasurement(boolean forceBenchmark) {
		this.forceBench = forceBenchmark;	
	}
	/**
	 * Depending on the FileProperties this will measure the abillity 
	 * of this computer to crunch numbers. The results will be given
	 * in Mflops (Millions of floating point operations per second)  
	 */
	@Override
	public void run() {
		super.run();
		this.setName("SciMark2-Benchmark");

		// --- Startwert = Alter Wert ---------------------  
		LoadMeasureThread.setCompositeBenchmarkValue(benchValueOld);
		
		// --- FileProperties berücksichtigen -------------
		this.nowExecOn = this.getLocalComputerName();

		// --- Kriterium für einen vorzeitigen Ausstieg ---
		if ( benchValueOld>0 && nowExecOn.equalsIgnoreCase(benchExecOn) && benchAllwaysSkip==true && forceBench==false) {
			Application.benchmarkIsRunning = false;
			return;
		}  
		
		// --- Benchmark-Monitor initialisieren -----------
		if (Application.MainWindow==null) {
			benchGUI = new BenchmarkMonitor(null);	
		} else {
			benchGUI = new BenchmarkMonitor(Application.MainWindow);	
		}		
		
		// --- Eingriffsmöglichkeit für den Nutzer --------
		if ( benchValueOld>0 && nowExecOn.equalsIgnoreCase(benchExecOn)) {
			benchGUI.jButtonSkip.setEnabled(true);
			if (forceBench==true) {
				benchGUI.jButtonSkipAllways.setEnabled(false);
			} else {
				benchGUI.jButtonSkipAllways.setEnabled(true);
			}
		} else {
			benchGUI.jButtonSkip.setEnabled(false);
			benchGUI.jButtonSkipAllways.setEnabled(false);
		}
		// ------------------------------------------------
		
		
		// --- Anzeige für den Fortschritt --- ON ---------
		benchGUI.setBenchmarkValue(benchValueOld);
		benchGUI.setVisible(true);
		benchGUI.jProgressBarBenchmark.setMinimum(0);
		benchGUI.jProgressBarBenchmark.setMaximum(6);
		benchGUI.jProgressBarBenchmark.setValue(0);

		// --- Test durchführen ---------------------------
		double res[] = new double[6];
		Random R = new Random(Constants.RANDOM_SEED);

		benchGUI.jProgressBarBenchmark.setValue(1);
		res[1] = kernel.measureFFT( FFT_size, min_time, R);
		if (this.isSkipAction()) return;
		
		benchGUI.jProgressBarBenchmark.setValue(2);
		res[2] = kernel.measureSOR( SOR_size, min_time, R);
		if (this.isSkipAction()) return;
		
		benchGUI.jProgressBarBenchmark.setValue(3);
		res[3] = kernel.measureMonteCarlo(min_time, R);
		if (this.isSkipAction()) return;
		
		benchGUI.jProgressBarBenchmark.setValue(4);
		res[4] = kernel.measureSparseMatmult( Sparse_size_M, Sparse_size_nz, min_time, R);
		if (this.isSkipAction()) return;
		
		benchGUI.jProgressBarBenchmark.setValue(5);
		res[5] = kernel.measureLU( LU_size, min_time, R);
		if (this.isSkipAction()) return;

		benchGUI.jProgressBarBenchmark.setValue(6);
		res[0] = (res[1] + res[2] + res[3] + res[4] + res[5]) / 5;
		float result  = (float)Math.round((float)res[0]*100)/100;
		if (this.isSkipAction()) return;

		// --- Ergebnis im LoadMeasurThread speichern -----
		LoadMeasureThread.setCompositeBenchmarkValue(result);
		Application.RunInfo.setBenchValue(result);
		Application.RunInfo.setBenchExecOn(nowExecOn);
		Application.RunInfo.setBenchAllwaysSkip(benchAllwaysSkip);
		
		// --- Anzeige für den Fortschritt --- OFF --------
		benchGUI.setBenchmarkValue(result);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException sleepE) {
			sleepE.printStackTrace();
		}
		this.closeGUI();
		Application.benchmarkIsRunning = false;
		
	}
	
	/**
	 * This Method checks if a skip-button was pressed on the Monitor-GUI
	 * @return
	 */
	private boolean isSkipAction() {
		
		if (benchGUI.actionSkipAllways == true) {
			Application.RunInfo.setBenchAllwaysSkip(true);
			this.closeGUI();
			Application.benchmarkIsRunning = false;
			return true;
		}
		if (benchGUI.actionSkip==true) {
			this.closeGUI();
			Application.benchmarkIsRunning = false;
			return true;
		}		
		return false;
	}
	
	/**
	 * This method closes teh Monitor-GUI
	 */
	private void closeGUI() {
		
		benchGUI.setVisible(false);
		benchGUI.dispose();
		benchGUI = null;
	}
	
	/**
	 * Returns the local 'CanonicalHostName' to identify 
	 * on which computer this measurement were executed
	 * @return
	 */
	private String getLocalComputerName() {

		String nowExecOn = null;
		try {
			nowExecOn = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException inetE) {
			inetE.printStackTrace();
		}
		return nowExecOn;
	}
	
}
