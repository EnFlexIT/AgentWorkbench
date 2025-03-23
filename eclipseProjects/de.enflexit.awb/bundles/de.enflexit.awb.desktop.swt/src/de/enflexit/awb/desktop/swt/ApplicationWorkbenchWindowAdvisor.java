package de.enflexit.awb.desktop.swt;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.core.Application;

/**
 * The Class ApplicationWorkbenchWindowAdvisor.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private Runnable postWindowOpenRunnable;
	
    /**
     * Instantiates a new application workbench window advisor.
     *
     * @param configurer the current IWorkbenchWindowConfigurer
     * @param postWindowOpenExecuter the post window open executer
     */
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer, Runnable postWindowOpenRunnable) {
        super(configurer);
        this.postWindowOpenRunnable = postWindowOpenRunnable;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
     */
    @Override
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
        configurer.setTitle(Application.getGlobalInfo().getApplicationTitle());
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
     */
    @Override
    public void postWindowOpen() {
    	// --- Do the things required after system start --
    	if (this.postWindowOpenRunnable!=null) {
    		try {
    			this.postWindowOpenRunnable.run();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
    	}
    	this.postWindowOpenRunnable = null;
    }
  
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowShellClose()
     */
    @Override
    public boolean preWindowShellClose() {
    	
    	boolean isSwtBundleEclipseUI = this.isProductOfThisBundle();
    	if (isSwtBundleEclipseUI==false) {
    		// --- Used with Swing-UI - Just hide the workbench window --------
			AwbIApplicationSWT.setVisibleEclipseUi(false);
    		return false;
    	}
    	return true;
    }
    
    /**
     * Checks if the current product is of this bundle.
     * @return true, if is product of this bundle
     */
    private boolean isProductOfThisBundle() {
    	String bundleNameProduct = Platform.getProduct().getDefiningBundle().getSymbolicName();
    	String bundleNameSwtBundle = FrameworkUtil.getBundle(this.getClass()).getSymbolicName();
    	return bundleNameProduct.equals(bundleNameSwtBundle);
    }
    
    
}
