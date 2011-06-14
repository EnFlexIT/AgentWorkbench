/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.benchmark;

import java.net.InetAddress;
import java.net.UnknownHostException;

import agentgui.core.application.Application;
import agentgui.core.jade.ClassSearcher;
import agentgui.simulationService.load.LoadMeasureThread;
import jnt.scimark2.Constants;
import jnt.scimark2.Random;
import jnt.scimark2.kernel;

/**
 * This thread is doing the actual benchmark measurements, which 
 * results to the capability of a computer system to crunch numbers.<br> 
 * This will be comparable through a benchmark value in 
 * Mflops (Millions of floating point operations per second).<br><br>
 * 
 * The benchmark value can be obtained by using the method<br>  
 * 'Application.RunInfo.getBenchValue();'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
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
	
	/**
	 * The constructor of this class.<br> 
	 * If <i>forceBenchmark</i> is set to true the benchmark will be executed regardless of 
	 * the settings in the property file or if the system was executed in the same network
	 * already or not.
	 * @param forceBenchmark
	 */
	public BenchmarkMeasurement(boolean forceBenchmark) {
		this.forceBench = forceBenchmark;	
	}
	/**
	 * Depending on the FileProperties this will measure the ability> of this computer to crunch numbers.<br>
	 * The results will be given in Mflops (Millions of floating point operations per second)  
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
		if ( this.benchValueOld>0 && this.nowExecOn.equalsIgnoreCase(this.benchExecOn) && this.benchAllwaysSkip==true && forceBench==false) {
			// --- Nach Agent-, Ontology- und BaseService - Classes suchen ----
			Application.benchmarkIsRunning = false;
			if (Application.ClassDetector == null) {
				Application.ClassDetector = new ClassSearcher();
			}			
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
		Application.Properties.save();
		
		// --- Anzeige für den Fortschritt --- OFF --------
		benchGUI.setBenchmarkValue(result);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException sleepE) {
			sleepE.printStackTrace();
		}
		this.closeGUI();
		Application.benchmarkIsRunning = false;
		
		// --- Nach Agent-, Ontology- und BaseService - Classes suchen ----
		if (Application.ClassDetector == null) {
			Application.ClassDetector = new ClassSearcher();
		}
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
