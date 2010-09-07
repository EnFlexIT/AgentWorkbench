package application;

import mas.service.load.LoadMeasureThread;
import jnt.scimark2.Constants;
import jnt.scimark2.Random;
import jnt.scimark2.kernel;

public class BenchmarkMeasurement extends Thread {

	private boolean debug = false;
	
	private double min_time = Constants.RESOLUTION_DEFAULT;

	private int FFT_size = Constants.FFT_SIZE;
	private int SOR_size =  Constants.SOR_SIZE;
	private int Sparse_size_M = Constants.SPARSE_SIZE_M;
	private int Sparse_size_nz = Constants.SPARSE_SIZE_nz;
	private int LU_size = Constants.LU_SIZE;
	
	@Override
	public void run() {
		super.run();
		
		this.setName("SciMark2-Benchmark");
		if (debug) {
			System.out.println("Starting SciMark2-Benchmark ...");	
		}		
		
		double res[] = new double[6];
		Random R = new Random(Constants.RANDOM_SEED);

		res[1] = kernel.measureFFT( FFT_size, min_time, R);
		res[2] = kernel.measureSOR( SOR_size, min_time, R);
		res[3] = kernel.measureMonteCarlo(min_time, R);
		res[4] = kernel.measureSparseMatmult( Sparse_size_M, Sparse_size_nz, min_time, R);
		res[5] = kernel.measureLU( LU_size, min_time, R);

		res[0] = (res[1] + res[2] + res[3] + res[4] + res[5]) / 5;
		
		float result  = (float)Math.round((float)res[0]*100)/100;
		if (debug) {
			System.out.println("Benchmark Result - Composite Score: " + result);	
		}		
		LoadMeasureThread.setCompositeBenchmarkValue(result);
		
	}
	
}
