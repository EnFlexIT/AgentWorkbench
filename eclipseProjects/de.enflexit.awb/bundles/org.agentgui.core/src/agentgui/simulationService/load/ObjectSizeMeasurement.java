package agentgui.simulationService.load;

import org.openjdk.jol.info.GraphLayout;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * The Class ObjectSizeMeasurement.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ObjectSizeMeasurement {

	/**
	 * Returns the byte size of the specified object.
	 *
	 * @param instance the instance
	 * @return the byte size of object
	 */
	public static Long getByteSizeOfObject(Object instance) {
		
		if (instance==null) return null;
		
		Long sizeInByte = null;
		try {
			sizeInByte = GraphLayout.parseInstance(instance).totalSize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sizeInByte;
	}
	
	/**
	 * Returns the size of an object as SI-String (1K = 1000).
	 *
	 * @param instance the instance
	 * @return the size of object SI
	 */
	public static String getSizeOfObjectSI(Object instance) {
		
		if (instance==null) return null;
		
		Long sizeInByte = getByteSizeOfObject(instance);
		if (sizeInByte!=null) {
			return humanReadableByteCountSI(sizeInByte);
		}
		return "Unknown size of instance => class: " + instance.getClass().getName();
	}
	
	/**
	 * Returns the size of an object as BIN-String (1K = 1024).
	 *
	 * @param instance the instance
	 * @return the size of object bin
	 */
	public static String getSizeOfObjectBin(Object instance) {
		
		if (instance==null) return null;
		
		Long sizeInByte = getByteSizeOfObject(instance);
		if (sizeInByte!=null) {
			return humanReadableByteCountBin(sizeInByte);
		}
		return "Unknown size of instance => class: " + instance.getClass().getName();
	}
	
	
	/**
	 * Human readable byte count SI (1K = 1000).
	 *
	 * @param bytes the bytes
	 * @return the string
	 */
	public static String humanReadableByteCountSI(long bytes) {
	    if (-1000 < bytes && bytes < 1000) {
	        return bytes + " B";
	    }
	    CharacterIterator ci = new StringCharacterIterator("kMGTPE");
	    while (bytes <= -999_950 || bytes >= 999_950) {
	        bytes /= 1000;
	        ci.next();
	    }
	    return String.format("%.1f %cB", bytes / 1000.0, ci.current());
	}
	/**
	 * Human readable byte count bin (1K = 1024).
	 *
	 * @param bytes the bytes
	 * @return the string
	 */
	public static String humanReadableByteCountBin(long bytes) {
	    long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
	    if (absB < 1024) {
	        return bytes + " B";
	    }
	    long value = absB;
	    CharacterIterator ci = new StringCharacterIterator("KMGTPE");
	    for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
	        value >>= 10;
	        ci.next();
	    }
	    value *= Long.signum(bytes);
	    return String.format("%.1f %ciB", value / 1024.0, ci.current());
	}
}
