package agas.agents.components;

import static agas.agents.components.ConversationId.EXIT_PROPOSAL;
import gasmas.ontology.Exit;

/**
 * The agent class for an {@link Exit}.
 * 
 * @author Oleksandr Turchyn - University of Duisburg - Essen
 */
public class ExitAgent extends GenericEntryExitAgent {

	private static final long serialVersionUID = -1436530491532584084L;

	@Override
	protected ConversationId getConversationId() {
		return EXIT_PROPOSAL;
	}

	@Override
	protected String getAgentType() {
		return "ExitAgent";
	}
}
