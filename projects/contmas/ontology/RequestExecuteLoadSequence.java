package contmas.ontology;

import jade.content.AgentAction;

/**
* Protege name: RequestExecuteLoadSequence
* @author ontology bean generator
* @version 2010/04/12, 23:13:31
*/
public class RequestExecuteLoadSequence implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID= -3887276117946064387L;
	/**
	* Protege name: next_step
	   */
	private LoadList next_step;

	public void setNext_step(LoadList value){
		this.next_step=value;
	}

	public LoadList getNext_step(){
		return this.next_step;
	}

}
