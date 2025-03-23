package de.enflexit.awb.desktop.swt;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * The Class ApplicationWorkbenchAdvisor.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private Runnable postWindowOpenExecuter;

	/**
	 * Instantiates a new application workbench advisor.
	 * @param postWindowOpenExecuter the post window open executer
	 */
	public ApplicationWorkbenchAdvisor(Runnable postWindowOpenExecuter) {
		this.postWindowOpenExecuter = postWindowOpenExecuter;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
	 */
	@Override
	public String getInitialWindowPerspectiveId() {
		return "de.enflexit.awb.desktop.swt.perspective.main";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
	 */
	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {

		WorkbenchWindowAdvisor wwAdvisor = new ApplicationWorkbenchWindowAdvisor(configurer, this.postWindowOpenExecuter);
		this.postWindowOpenExecuter = null;
		return wwAdvisor;
	}
	
}
