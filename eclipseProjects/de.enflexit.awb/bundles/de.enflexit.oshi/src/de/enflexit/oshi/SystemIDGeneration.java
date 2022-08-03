package de.enflexit.oshi;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.Constants;

/**
 * The Class SystemIDGeneration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SystemIDGeneration {

	/**
	 * <p>
	 * main.
	 * </p>
	 *
	 * @param args an array of {@link java.lang.String} objects.
	 */
	public static void main(String[] args) {
		
		String unknownHash = getUnknownIdentifier();

		System.out.println("Here's a unique (?) id for your computer.");
		System.out.println(getSystemIdentifier());
		System.out.println("If any field is " + unknownHash + " then I couldn't find a serial number or uuid, and running as sudo might change this.");
	}

	/**
	 * Generates a Computer Identifier, which may be part of a strategy to construct
	 * a license key. (The identifier may not be unique as in one case hashcode
	 * could be same for multiple values, and the result may differ based on whether
	 * the program is running with sudo/root permission.) The identifier string is
	 * based upon the processor serial number, vendor, processor identifier, and
	 * total processor count.
	 *
	 * @return A string containing four hyphen-delimited fields representing the
	 *         processor; the first 3 are 32-bit hexadecimal values and the last one
	 *         is an integer value.
	 */
	public static String getSystemIdentifier() {
		
		SystemInfo systemInfo = new SystemInfo();
		OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
		HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
		CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
		ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();

		String vendor = operatingSystem.getManufacturer();
		String processorSerialNumber = computerSystem.getSerialNumber();
		String uuid = computerSystem.getHardwareUUID();
		String processorIdentifier = centralProcessor.getProcessorIdentifier().getIdentifier();
		int processors = centralProcessor.getLogicalProcessorCount();

		String delimiter = "-";

		String identifier = "";
		identifier += String.format("%08x", vendor.hashCode()) + delimiter;
		identifier += String.format("%08x", processorSerialNumber.hashCode()) + delimiter;
		identifier += String.format("%08x", uuid.hashCode()) + delimiter;
		identifier += String.format("%08x", processorIdentifier.hashCode()) + delimiter + processors;
		return identifier;
	}

	/**
	 * Returns the unknown identifier hash code.
	 * @return the unknown identifier
	 */
	public static String getUnknownIdentifier() {
		return String.format("%08x", Constants.UNKNOWN.hashCode());
	}
}
