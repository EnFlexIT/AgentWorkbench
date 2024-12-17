package de.enflexit.awb.core.project.plugins;

import javax.swing.JComponent;
import javax.swing.JMenu;

import de.enflexit.awb.core.project.Project;

/**
 * This abstract class is the root for customized Swing plug-in's, which can
 * be loaded to extend an individual agent project.<br>
 * Classes which inherit from this class can be registered to an
 * agent project by using the "Resources"-Tab in the project
 * configuration. 
 *   
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class PlugIn extends AbstractPlugIn<JMenu, JComponent, JComponent> {

	public PlugIn(Project currProject) {
		super(currProject);
	}
}