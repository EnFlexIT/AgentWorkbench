package agentgui.core.config.auth;

import agentgui.core.config.auth.OIDCAuthorization.URLProcessor;

public interface OIDCResourceAvailabilityHandler {
	public void onResourceAvailable(URLProcessor urlProcessor);
}
