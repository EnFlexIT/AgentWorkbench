 
package org.agentgui.core.handler;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.e4.ui.di.UISynchronize;

public class UpdateHandler {

	private static final String REPOSITORY_LOCATION = "file:///D:/git/AgentWorkbench/eclipseProjects/org.agentgui/releng/org.agentgui.update/target/repository";
	
	@Execute
	public void execute(final IProvisioningAgent agent, final Shell shell, final UISynchronize sync, @Optional final IWorkbench workbench) {
		
		System.out.println((this.getClass().getSimpleName() + " called"));

		Job updateJob = new Job("Update Job") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				return checkForUpdates(agent, shell, sync, workbench, monitor);
			}
		};
		updateJob.schedule();
	}
	
	private IStatus checkForUpdates(final IProvisioningAgent agent, final Shell shell, final UISynchronize sync, final IWorkbench workbench, IProgressMonitor monitor) {
		
		final ProvisioningSession session = new ProvisioningSession(agent);
		final UpdateOperation operation = new UpdateOperation(session);
		configureUpdate(operation);
		
		final IStatus status = operation.resolveModal(monitor);
		
		if(status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			showMessage(shell, sync);
			return Status.CANCEL_STATUS;
		}
		
		final ProvisioningJob provisioningJob = operation.getProvisioningJob(monitor);
		
		if(provisioningJob == null) {
			System.err.println("Trying to update from the Eclipse IDE? This won't work!");
			return Status.CANCEL_STATUS;
		}
		
		configureProvisioningJob(provisioningJob, shell, sync, workbench);
		provisioningJob.schedule();
		return Status.OK_STATUS;
		
	}
	
	private void configureProvisioningJob(ProvisioningJob provisioningJob, final Shell shell, final UISynchronize sync, final IWorkbench workbench) {
		
		provisioningJob.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				if(event.getResult().isOK()) {
					sync.syncExec(new Runnable() {
						
						@Override
						public void run() {
							boolean restart = MessageDialog.openQuestion(shell, "Updates installed, restart?", "Updates have been installed. Do you want to restart?");
							if(restart) {
								workbench.restart();
							}
						}
					});
				}
				super.done(event);
			}
			
		});
	}
	
	private void showMessage(final Shell parent, final UISynchronize sync) {
		sync.syncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageDialog.openWarning(parent, "No update", "No updates for the current installation have been found.");
			}
			
		});
	}
	
	
	private UpdateOperation configureUpdate(final UpdateOperation operation) {
		URI uri = null;
		
		try {
			uri = new URI(REPOSITORY_LOCATION);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}

		operation.getProvisioningContext().setArtifactRepositories(new URI[] {uri});
		operation.getProvisioningContext().setMetadataRepositories(new URI[] {uri});
		return operation;
	}
		
}