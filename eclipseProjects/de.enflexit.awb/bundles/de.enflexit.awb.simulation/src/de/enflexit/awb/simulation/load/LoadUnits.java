package de.enflexit.awb.simulation.load;

/**
 * This class allows the conversion of amounts of data, given in Byte, to
 * a different unit like KB, MB or GB. The inherent methods are accessible in a 
 * static way. 
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadUnits {

	/** Constant for the conversion */
	public static final int CONVERT2_BIT = 0;
	/** Constant for the conversion */
	public static final int CONVERT2_BYTES = 1;
	/** Constant for the conversion */
	public static final int CONVERT2_KILO_BYTE = 2;
	/** Constant for the conversion */
	public static final int CONVERT2_MEGA_BYTE = 3;
	/** Constant for the conversion */
	public static final int CONVERT2_GIGA_BYTE = 4;
	/** Constant for the conversion */
	public static final int CONVERT2_TERRA_BYTE = 5;
	
	/** factor for the calculations */
	private static final int calcFactor = 1024;
	
	/** precision for rounding  */
	private static int round2 = 2;
	/** round factor */
	private static double round2factor = Math.pow(10, round2);
	
	
	/**
	 * Converts Bytes to a specified degree like MB or GB.
	 *
	 * @param bytes the bytes
	 * @param convert2Constant a constant as specified in this class 
	 * @return the double
	 */
	public static double bytes2(Long bytes, int convert2Constant) {
		
		double result = 0;
		
		switch (convert2Constant) {
		case CONVERT2_BIT:
			result = bytes * 8;
			break;
		
		case CONVERT2_BYTES:
			result = bytes;
			break;

		case CONVERT2_KILO_BYTE:
			result = bytes / Math.pow(calcFactor,1);
			break;

		case CONVERT2_MEGA_BYTE:
			result = bytes / Math.pow(calcFactor,2);
			break;
			
		case CONVERT2_GIGA_BYTE:
			result = bytes / Math.pow(calcFactor,3);
			break;
			
		case CONVERT2_TERRA_BYTE:
			result = bytes / Math.pow(calcFactor,4);
			break;
			
		default:
			result = bytes; 
			break;
		}
		return doubleRound(result);
	}
	
	/**
	 * Rounds an incoming double value.
	 *
	 * @param input the input value
	 * @return the double rounded
	 */
	private static double doubleRound(double input) {
		return Math.round(input * round2factor) / round2factor;
	}
	
}
