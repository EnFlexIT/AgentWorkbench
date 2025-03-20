package de.enflexit.awb.desktop.swt.extensions;

import de.enflexit.awb.core.project.plugins.PlugIn;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.di.annotations.Execute;

public class EvaluateContributionsHandler {

	private static final String PLUGIN_ID = "de.enflexit.awb.desktop.swt.project.plugin";
   
	
	/**
	 * Gets the extension registry.
	 * @return the extension registry
	 */
	private IExtensionRegistry getExtensionRegistry() {
		return Platform.getExtensionRegistry();
	}
	
	/**
	 * Execute.
	 */
	@Execute
    public void execute() {
        
    	System.out.println("Start evaluating available extensions");
    	IConfigurationElement[] config = this.getExtensionRegistry().getConfigurationElementsFor(PLUGIN_ID);
    	
        try {
        	
            for (IConfigurationElement e : config) {
                System.out.println("Evaluating extension " + e.getName());
                final Object object = e.createExecutableExtension("class");
                if (object instanceof PlugIn) {
                    executeExtension(object);
                }
            }
        } catch (CoreException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Execute extension.
     * @param object the object instance found 
     */
    private void executeExtension(final Object object) {
       
    	ISafeRunnable runnable = new ISafeRunnable() {
            @Override
            public void handleException(Throwable e) {
                System.out.println("Exception in client");
            }

            @Override
            public void run() throws Exception {
                System.err.println(object.toString());
            }
        };
        SafeRunner.run(runnable);
    }
    
    
}
	
