/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
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
package agentgui.simulationService.load;

public class LoadUnits {

	public static final int CONVERT2_BIT = 0;
	public static final int CONVERT2_BYTES = 1;
	public static final int CONVERT2_KILO_BYTE = 2;
	public static final int CONVERT2_MEGA_BYTE = 3;
	public static final int CONVERT2_GIGA_BYTE = 4;
	public static final int CONVERT2_TERRA_BYTE = 5;
	
	private static final int calcFactor = 1024;
	
	private static int round2 = 2;
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
