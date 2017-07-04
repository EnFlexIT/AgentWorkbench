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
package gasmas.physics;

/**
 * The Class CalcFunctions contains some static functions and 
 * methods, which can be called within the project.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class CalcFunctions {

	/**
	 * Gets the exponential scale of a double value to the base of 10. 
	 * For example
	 * 40000 will return 4,
	 * 5000 will return 3,
	 * 823 will return 2,
	 * 10 will return 1,
	 * 4 will return 0,
	 * 0.1 will return -1,
	 * 0.023 will return -2 and so on
	 * 
	 * @param doubleValue the double value to evaluate
	 * @return the exponential scale of double to the base of 10
	 */
	public static double getExponentialScaleOfDouble(double doubleValue) {
		
		double scale = 0;
		double doubleValueTmp = Math.abs(doubleValue);
		double doubleScaler = 0;
		
		if (doubleValueTmp==0 || (doubleValueTmp>=1 && doubleValueTmp<10) ) {
			return 0;
		} else if (doubleValueTmp>1) { 
			doubleScaler = 0.1;
		} else {
			doubleScaler = 10;
		}

		while (doubleValueTmp<1 || doubleValueTmp>10) {
			doubleValueTmp = doubleValueTmp * doubleScaler;
			scale++;
		}
		
		if (Math.abs(doubleValue) < 1) {
			scale = scale * (-1);
		}
		return scale;
	}
	
	
	/**
	 * Rounds a double value to the given precision.
	 * For example: <br> 
	 * <code> round(3.14159 26535, 2)</code> will deliver 3.14
	 *
	 * @param doubleValue the double value
	 * @param precision the precision
	 * @return the double
	 */
	public static double round(double doubleValue, double precision) {
		return Math.round(doubleValue * Math.pow(10, precision)) / Math.pow(10, precision);
	}
	
}
