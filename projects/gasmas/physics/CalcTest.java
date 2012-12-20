package gasmas.physics;

import gasmas.physics.pipes.CompressibilityNumber;
import gasmas.physics.pipes.FluidVelocity;
import gasmas.physics.pipes.GasDensity;
import gasmas.physics.pipes.PipePreasureLoss;
import agentgui.math.calculation.CalcConstant;
import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcExpression;

/**
 * Temporary class for the first CalcExpression tests.
 * @author Nils
 */
public class CalcTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		CalcConstant fluidVelocity = new CalcConstant(10.0);
//		CalcConstant pipeDiameter = new CalcConstant(0.6);
//		CalcConstant kinematicViscosity = new CalcConstant(0.0000002135);
//		CalcConstant pipeRoughness = new CalcConstant(0.02);
//		
//		Reynolds reynolds = new Reynolds(fluidVelocity, pipeDiameter, kinematicViscosity);
//		PipeFrictionCoefficient pfc0 = new PipeFrictionCoefficient(reynolds, pipeRoughness, pipeDiameter);
//
//		try {
//			System.out.println( System.currentTimeMillis());
//			System.out.println("Reynolds " + reynolds.getValue() + "	" + System.currentTimeMillis());
//			System.out.println("Lambda " + pfc0.getValue() + "			" + System.currentTimeMillis());
//			
//		} catch (CalcExeption e) {
//			e.printStackTrace();
//		}
		
		
		CalcExpression normGasDensity = new CalcConstant(0.829);
//		CalcExpression initialPreasure = new CalcConstant(21e5);
		CalcExpression initialPreasure = new CalcConstant(21);
		CalcExpression temperature = new CalcConstant(278.15);
		CalcExpression diameter = new CalcConstant(0.2101);
		CalcExpression normVolumeFlow = new CalcConstant(16000);
		CalcExpression dynamicViscosity = new CalcConstant(11.62e-6);
		CalcExpression pipeRoughness = new CalcConstant(0.2);
		CalcExpression pipeLength = new CalcConstant(8950);
		
		CompressibilityNumber compressibilityNumber = new CompressibilityNumber(initialPreasure);
		FluidVelocity fluidVelocity = new FluidVelocity(diameter, normVolumeFlow, initialPreasure, temperature);
		GasDensity gasDensity = new GasDensity(normGasDensity, initialPreasure, temperature);
		
		CalcExpression pipeFrictionCoefficient = new CalcConstant(0.02);
	
		PipePreasureLoss pipePreasureLoss = new PipePreasureLoss();
		pipePreasureLoss.setInitialPreasure(initialPreasure);
		pipePreasureLoss.setPipeDiameter(diameter);
		pipePreasureLoss.setPipeLength(pipeLength);
		pipePreasureLoss.setCompressibilityNumber(compressibilityNumber);
		pipePreasureLoss.setFluidVelocity(fluidVelocity);
		pipePreasureLoss.setRealGasDensity(gasDensity);
		pipePreasureLoss.setPipeFrictionCoefficient(pipeFrictionCoefficient);
		
		try {
			System.out.println(pipePreasureLoss.getValue());
		} catch (CalcExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
