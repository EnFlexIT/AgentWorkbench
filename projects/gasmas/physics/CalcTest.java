package gasmas.physics;

import gasmas.physics.pipes.CompressibilityNumber;
import gasmas.physics.pipes.FluidVelocity;
import gasmas.physics.pipes.GasDensity;
import gasmas.physics.pipes.PipeFrictionCoefficient;
import gasmas.physics.pipes.PipePressureLoss;
import gasmas.physics.pipes.Reynolds;
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
		
		try {
		
			CalcExpression normGasDensity = new CalcConstant(0.829);
			CalcExpression initialPressure = new CalcConstant(21e5);
			CalcExpression temperature = new CalcConstant(278.15);
			CalcExpression pipeDiameter = new CalcConstant(0.2101);
			CalcExpression normVolumeFlow = new CalcConstant(16000);
			CalcExpression pipeRoughness = new CalcConstant(0.2e-3);
			CalcExpression pipeLength = new CalcConstant(8950);
			CalcExpression dynamicViscosity = new CalcConstant(11.62e-6);
			
			CompressibilityNumber compressibilityNumber = new CompressibilityNumber(initialPressure);
			FluidVelocity fluidVelocity = new FluidVelocity(pipeDiameter, normVolumeFlow, initialPressure, temperature);
			GasDensity gasDensity = new GasDensity(normGasDensity, initialPressure, temperature);
			
			CalcExpression kinematicViscosity = new CalcConstant(dynamicViscosity.getValue()/gasDensity.getValue());
			Reynolds reynolds = new Reynolds(fluidVelocity, pipeDiameter, kinematicViscosity);
			PipeFrictionCoefficient pipeFrictionCoefficient = new PipeFrictionCoefficient(reynolds, pipeRoughness, pipeDiameter);
			
			PipePressureLoss pipePressureLoss = new PipePressureLoss();
			pipePressureLoss.setInitialPressure(initialPressure);
			pipePressureLoss.setPipeDiameter(pipeDiameter);
			pipePressureLoss.setPipeLength(pipeLength);
			pipePressureLoss.setCompressibilityNumber(compressibilityNumber);
			pipePressureLoss.setFluidVelocity(fluidVelocity);
			pipePressureLoss.setRealGasDensity(gasDensity);
			pipePressureLoss.setPipeFrictionCoefficient(pipeFrictionCoefficient);
		
			System.out.println("d/k " + pipeDiameter.getValue() / pipeRoughness.getValue());
			System.out.println("Reynolds " + reynolds.getValue());
			System.out.println("Lambda " + pipeFrictionCoefficient.getValue());
			System.out.println("Pressure Loss " + pipePressureLoss.getValue());
			
		} catch (CalcExeption e) {
			e.printStackTrace();
		}
		

	}

}
