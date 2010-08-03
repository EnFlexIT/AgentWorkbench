package mas.service.load;

public class LoadUnits {

	public static final int CONVERT2_BIT = 0;
	public static final int CONVERT2_BYTES = 1;
	public static final int CONVERT2_KILO_BYTE = 2;
	public static final int CONVERT2_MEGA_BYTE = 3;
	public static final int CONVERT2_GIGA_BYTE = 4;
	public static final int CONVERT2_TERRA_BYTE = 5;
	
	private static final int calcFactor = 1024;
	
	private static int round2 = 4;
	private static double round2factor = Math.pow(10, round2);
	
	
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
	 * Rounds an incomming double value
	 * @param input
	 * @return
	 */
	private static double doubleRound(double input) {
		return Math.round(input * round2factor) / round2factor;
	}
	
}
