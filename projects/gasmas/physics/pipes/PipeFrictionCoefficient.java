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
package gasmas.physics.pipes;

import gasmas.physics.CalcFunctions;
import agentgui.math.calculation.CalcConstant;
import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcExpression;
import agentgui.math.calculation.CalcFormula;
import agentgui.math.calculation.CalcParameterNotSetException;

/**
 * The class calculates the PipeFrictionCoefficient (Cerbe, Board 4.3).
 */
public class PipeFrictionCoefficient extends CalcFormula {

	private static final long serialVersionUID = -5819657506014062429L;

	private CalcExpression reynolds;
	private CalcExpression diameter;
	private CalcExpression pipeRoughness = new CalcConstant(0);
	private CalcExpression precision = new CalcConstant(5);
	
	/**
	 * Instantiates a new pipe friction coefficient based on the
	 * reynold number for hydraulically smooth pipes.
	 *
	 * @param reynolds the reynolds number
	 */
	public PipeFrictionCoefficient(CalcExpression reynolds) {
		this.setReynolds(reynolds);
	}
	
	/**
	 * Instantiates a new pipe friction coefficient.
	 *
	 * @param reynolds the reynolds
	 * @param precision the precision
	 */
	public PipeFrictionCoefficient(CalcExpression reynolds, CalcExpression precision) {
		this.setReynolds(reynolds);
		this.setPrecision(precision);
	}
	
	/**
	 * Instantiates a new pipe friction coefficient based on the
	 * reynold number for pipes with a given pipeRoughness.
	 *
	 * @param reynolds the reynolds number
	 * @param pipeRoughness the pipe roughness
	 * @param pipeDiameter the pipe diameter
	 */
	public PipeFrictionCoefficient(CalcExpression reynolds, CalcExpression pipeRoughness, CalcExpression pipeDiameter) {
		this.setReynolds(reynolds);
		this.setPipeRoughness(pipeRoughness);
		this.setPipeDiameter(pipeDiameter);
	}
	
	/**
	 * Instantiates a new pipe friction coefficient based on the
	 * reynold number for pipes with a given pipeRoughness.
	 * Precision determines the number of digits after the decimal point
	 *
	 * @param reynolds the reynolds number
	 * @param pipeRoughness the pipe roughness
	 * @param pipeDiameter the pipe diameter
	 * @param precision the number of digits after the decimal point
	 */
	public PipeFrictionCoefficient(CalcExpression reynolds, CalcExpression pipeRoughness, CalcExpression pipeDiameter, CalcExpression precision) {
		this.setReynolds(reynolds);
		this.setPipeRoughness(pipeRoughness);
		this.setPipeDiameter(pipeDiameter);
		this.setPrecision(precision);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.math.calculation.CalcFormula#getValue()
	 */
	@Override
	public double getValue() throws CalcExeption {
	
		if(reynolds == null){
			throw new CalcParameterNotSetException();
		}
		
		double pipeFrictionCoefficient = 1; // --- default value ---
		
		double dblReynolds = this.reynolds.getValue();
		double dblDiameter = this.diameter.getValue();
		double dblPipeRoughness = this.pipeRoughness.getValue();
		double dblPrecision = this.precision.getValue();
		
		if (dblReynolds<2320) {
			// --- laminar flow, use 'Hagen-Poisseuille' ------------
			pipeFrictionCoefficient = pipeFrictionCoefficient_Hagen_Poisseuille(dblReynolds);
		
		} else {
			// --- turbulent flow -----------------------------------
			if (dblPipeRoughness==0) {
				// --- hydraulically smooth ------------------------- 
				if (dblReynolds>=2320 && dblReynolds<100000) {
					// --- turbulent flow, use 'Blasiuis' -----------
					pipeFrictionCoefficient = pipeFrictionCoefficient_Blasius(dblReynolds);
				} else if (dblReynolds>=100000 && dblReynolds<2500000) {
					// --- turbulent flow, use 'Nikuradse' ----------
					pipeFrictionCoefficient = pipeFrictionCoefficient_Nikuradse(dblReynolds);
				} else if (dblReynolds>=2500000) {
					// --- turbulent flow, use Prandtl and Karman ---
					pipeFrictionCoefficient = pipeFrictionCoefficient_Prandtl_Karman(dblReynolds, dblPrecision);
				}

			} else {
				// --- hydraulically rough --------------------------
				pipeFrictionCoefficient = pipeFrictionCoefficient_Colebroke_White(dblReynolds, dblDiameter, dblPipeRoughness, dblPrecision);
				
			}
		}		
				
		pipeFrictionCoefficient = Math.round(pipeFrictionCoefficient * Math.pow(10, this.precision.getValue())) / Math.pow(10, this.precision.getValue());
		return pipeFrictionCoefficient;
	}
	
	
	/**
	 * Pipe friction coefficient for Hagen-Poisseuille.
	 * Applies for Re<2320 (laminar flow)
	 *
	 * @param reynoldsNumber the reynolds number
	 * @return the double
	 */
	public double pipeFrictionCoefficient_Hagen_Poisseuille(double reynoldsNumber) {
		return 64 / reynoldsNumber;
	}
	
	/**
	 * Pipe friction coefficient for Blasius.
	 * Applies for Re>=2320 and Re<10^5 (turbulent flow) in hydraulically smooth pipes
	 *
	 * @param reynoldsNumber the reynolds number
	 * @return the double
	 */
	public double pipeFrictionCoefficient_Blasius(double reynoldsNumber) {
		return 0.3164 * Math.pow(reynoldsNumber, -0.25);
	}
	
	/**
	 * Pipe friction coefficient for Nikuradse.
	 * Applies for Re>=10^5 and Re<2.5*10^6 (turbulent flow) in hydraulically smooth pipes
	 * 
	 * @param reynoldsNumber the reynolds number
	 * @return the double
	 */
	public double pipeFrictionCoefficient_Nikuradse(double reynoldsNumber) {
		return 0.0032 + (0.221 * Math.pow(reynoldsNumber, -0.237));
	}
	
	/**
	 * Pipe friction coefficient for Prandtl & Karman.
	 * Applies for Re>=2.5*10^6 (turbulent flow) in hydraulically smooth pipes
	 *
	 * @param reynoldsNumber the reynolds number
	 * @param precision the precision
	 * @return the double
	 */
	public double pipeFrictionCoefficient_Prandtl_Karman(double reynoldsNumber, double precision) {
		
		// --- Set accuracy of interpolation ---- 
		double diffTerminateAt = Math.pow(10, (-1) * precision);
		
		// --- Define start value ---------------
		double lambda = pipeFrictionCoefficient_Nikuradse(reynoldsNumber);
		// --- Get an idea about the scale ------
		double lambdaScale = CalcFunctions.getExponentialScaleOfDouble(lambda);
		double lambdaStep = Math.pow(10, lambdaScale);
		// --- Round start value with scale ----- 
		lambda = Math.round(lambda / Math.pow(10, lambdaScale)) * Math.pow(10, lambdaScale);

		// --- Iterate --------------------------
		double diff = 100;
		double diffOld = 10;
		while (Math.abs(diff) > diffTerminateAt) {
			
			double f1 = Math.pow(lambda, -0.5);
			double f2 = 2 * Math.log10(reynoldsNumber * Math.pow(lambda, 0.5)) - 0.8;
			diff = f1-f2;
			
			// --- Did the sign change ? --------
			if (Math.signum(diff)!=Math.signum(diffOld)) {
				lambdaScale--;
				lambdaStep = Math.pow(10, lambdaScale);
			}
			
			if (diff>0) {
				// --- positive value ---
				lambda = lambda + lambdaStep;
			} else {
				// --- negative value ---
				lambda = lambda - lambdaStep;
			}
			diffOld = diff;
		}
		return lambda;
	}
	
	
	/**
	 * Pipe friction coefficient according to Colebroke-White.
	 *
	 * @param reynoldsNumber the reynolds number
	 * @param diameter the diameter
	 * @param pipeRoughness the pipe roughness
	 * @return the double
	 */
	public double pipeFrictionCoefficient_Colebroke_White(double reynoldsNumber, double diameter, double pipeRoughness, double precision) {

		// --- Set accuracy of interpolation ---- 
		double diffTerminateAt = Math.pow(10, (-1) * precision);

		// --- Define start value ---------------
		double lambda = 0.01;
		// --- Get an idea about the scale ------
		double lambdaScale = -2.0;

		double xValue1 = CalcFunctions.round(Math.pow(lambda, 0.5), 2.0);
		double xValue2 = 0.0;
		double xValueStep = Math.pow(10, lambdaScale);
		
		double diff = 100;
		double diffOld = -10;
		
		while (Math.abs(diff) > diffTerminateAt) {
			
			xValue2 = -0.5 * (1 / Math.log10((2.51/(reynoldsNumber*xValue1)) + (pipeRoughness/(3.71*diameter))));
			diff = xValue1-xValue2;
			
			// --- Did the sign change ? --------
			if (Math.signum(diff)!=Math.signum(diffOld)) {
				lambdaScale--;
				xValueStep = Math.pow(10, lambdaScale);
			}
			
			if (diff<0) {
				// --- positive value ---
				xValue1 = xValue1 + xValueStep;
			} else {
				// --- negative value ---
				xValue1 = xValue1 - xValueStep;
			}
			diffOld = diff;
		}
		
		lambda = Math.pow(xValue1, 2);
		return lambda;
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.math.calculation.CalcFormula#addParameter(java.lang.String, agentgui.math.calculation.CalcExpression)
	 */
	public void addParameter(String name, CalcExpression expression) {
		super.addParameter(name, expression);
		if (name.equals("reynolds")){
			this.reynolds=expression;
		} else if (name.equals("pipeDiameter")){
			this.diameter = expression;
		} else if (name.equals("pipeRoughness")){
			this.pipeRoughness=expression;
		} else if (name.equals("precision")){
			this.precision=expression;
		}
	}
	
	/**
	 * Sets the CalcExpression for the reynolds number.
	 * @param reynolds the new reynolds
	 */
	public void setReynolds(CalcExpression reynolds) {
		this.addParameter("reynolds", reynolds);
	}
	/**
	 * Sets the CalcExpression for the diameter of the pipe.
	 * @param diameter the new diameter
	 */
	public void setPipeDiameter(CalcExpression diameter) {
		this.addParameter("pipeDiameter", diameter);
	}
	/**
	 * Sets the CalcExpression for the pipe roughness.
	 * @param pipeRoughness the new pipe roughness
	 */
	public void setPipeRoughness(CalcExpression pipeRoughness) {
		this.addParameter("pipeRoughness", pipeRoughness);
	}
	/**
	 * Sets the CalcExpression for the precisions of the calculations.
	 * @param precision the new precision
	 */
	public void setPrecision(CalcExpression precision) {
		this.addParameter("precision", precision); 
	}
	
}
