package agentgui.core.gui.components;

import javax.swing.DefaultListModel;

import agentgui.core.agents.AgentClassElement;
import agentgui.core.application.Application;
import agentgui.core.application.Project;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.jade.ClassSearcherSingle;


public class JListClassSearcher extends JListWithProgressBar {

	private static final long serialVersionUID = 6613469481292382381L;

	private Class<?> currSearchFor = null;
	private ClassSearcherSingle currClassSearcherSingle = null; 
	private Project currProject = null;
	private DefaultListModel currListModel = null;
	
	private String currTextSearch4 = null;
	private DefaultListModel tmpListModel = null;
	
	/**
	 * This constructor was only build to enable the use of the Visual Editor  
	 */
	@Deprecated
	public JListClassSearcher() {
		super();
	}
	
	/**
	 * Constructor for this class in case that we DON'T NEED project-specific
	 * Classes. Needs constants from the class 'ClassSearcher'
	 * @param searchFor
	 */
	public JListClassSearcher(Class<?> searchFor) {
		super();
		this.currSearchFor = searchFor;
		this.setListModel();
	}

	/**
	 * Constructor for this class in case that we NEED project-specific
	 * Classes. Needs constants from the class 'ClassSearcher'
	 * @param searchFor
	 */
	public JListClassSearcher(Class<?> searchFor, Project project) {
		super();
		this.currSearchFor = searchFor;
		this.currProject = project;
		this.setListModel();
	}
	
	/**
	 * Sets the DefaultListModel for the current JList to display
	 */
	private void setListModel() {

		// --- Select the required 'ClassSearcherSingle' --
		if (this.currSearchFor == ClassSearcher.CLASSES_AGENTS) {
			this.currClassSearcherSingle = Application.ClassDetector.csAgents;
		} else if (this.currSearchFor == ClassSearcher.CLASSES_ONTOLOGIES) {
			this.currClassSearcherSingle = Application.ClassDetector.csOntologies;
		} else if (this.currSearchFor == ClassSearcher.CLASSES_BASESERVICE) {
			this.currClassSearcherSingle = Application.ClassDetector.csBaseService;
		}	
		
		// --- Get the needed ListModel -------------------
		if (this.currProject==null) {
			this.currListModel = currClassSearcherSingle.getjListModelClassesFound();
		} else {
			this.currListModel = currClassSearcherSingle.getjListModelClassesFoundProject();
		}
		
		this.currClassSearcherSingle.registerJListWithProgress(this);
		this.setModel(currListModel);
	}

	
	/**
	 * Filters the displayed list, depending on the search String
	 * @param search4
	 */
	public void setModelFiltered(String search4) {
		this.setModelFiltered(search4, null);
	}
	/**
	 * Filters the displayed list, depending on the search String
	 * while excluding the alsoExclude-variable 
	 * @param search4
	 */
	public void setModelFiltered(String search4, String alsoExcludePackage) {
		
		currTextSearch4 = search4;
		if ( (currTextSearch4==null | currTextSearch4.equals("")) && alsoExcludePackage==null ) {
			this.setModel(currListModel);
			return;
		}
		
		// --- Start filtering the Classes ----------------
		tmpListModel = new DefaultListModel();
		for (int i =0; i<currListModel.getSize();i++) {
			Object currObject    = currListModel.get(i);
			Class<?> currClass   = null;
			String currClassName = null;
			if (currSearchFor == ClassSearcher.CLASSES_AGENTS) {
				AgentClassElement ace = (AgentClassElement) currObject;
				currClass = ace.getElementClass();
				currClassName = ace.toString();
			} else if (currSearchFor == ClassSearcher.CLASSES_ONTOLOGIES) {
				ClassElement2Display ce2d = (ClassElement2Display) currObject;
				currClass = ce2d.getElementClass();
				currClassName = currClass.getName();
			} else if (currSearchFor == ClassSearcher.CLASSES_BASESERVICE) {
				ClassElement2Display ce2d = (ClassElement2Display) currObject;
				currClass = ce2d.getElementClass();
				currClassName = currClass.getName();
			}
			
			if (alsoExcludePackage==null) {
				if (currClassName.toLowerCase().contains(currTextSearch4.toLowerCase()) ) {
					currClassSearcherSingle.add2ListModel(tmpListModel, currClass);
				}	
			} else {
				if (currClassName.toLowerCase().startsWith(alsoExcludePackage.toLowerCase())==false) {
					if (currClassName.toLowerCase().contains(currTextSearch4.toLowerCase()) ) {
						currClassSearcherSingle.add2ListModel(tmpListModel, currClass);
					}	
				}
			}
					
		}// end for
		this.setModel(tmpListModel);
	}
	
	public void refreshModel() {
		if (currTextSearch4!=null && currTextSearch4.equals("")==false) {
			this.setModelFiltered(this.currTextSearch4);
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
