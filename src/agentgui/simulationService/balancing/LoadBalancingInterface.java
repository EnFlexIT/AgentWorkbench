package agentgui.simulationService.balancing;
import jade.core.Agent;

/**
 * This interface is to extend an agent with methods needed for balancing 
 * especially metric an cpu-usage
 * 
 * @see Agent
 * @see PredictiveStaticLoadBalancing
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg - Essen
 */
public interface LoadBalancingInterface{

	public double getPredictMetricCPU();
	public void setPredictMetricCPU(double predictMetricCPU) ;
	public double getPredictMetricMEM() ;
	public void setPredictMetricMEM(double predictMetricMEM) ;
	
	public double getRealMetricMEM() ;
	public void setRealMetricMEM(double realMetricMEM) ;
	public double getRealMetricCPU() ;
	public void setRealMetricCPU(double realMetricCPU) ;

}
