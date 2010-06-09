package sim.setup.gui;

import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import application.Project;

public class DynForm extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7942028680794127910L;

	private Project currProject = null;
	private String currAgentReference = null;
	
	public DynForm(Project project, String agentReference) {
		
		currProject = project;
		currAgentReference = agentReference;
		
		// --- Find Agent in AgentConfig ----------------------------
		if ( currProject.AgentConfig.containsKey(currAgentReference)== false) {
			// TODO: Hier ein leeres JPanle zurückgeben ---!!!
			return;
		}
		// --- Which Start-Objects are configured for the Agent? ---- 
		TreeMap<Integer,String> startObjectList = currProject.AgentConfig.getReferencesAsTreeMap(currAgentReference);
		Vector<Integer> v = new Vector<Integer>(startObjectList.keySet()); 
		Collections.sort(v);
		
		Iterator<Integer> it = v.iterator();
		while (it.hasNext()) {
			Integer startPosition = it.next();
			String startObjectClass = startObjectList.get(startPosition);
			String startObjectPackage = startObjectClass.substring(0, startObjectClass.lastIndexOf("."));
			System.out.println( startPosition + ": " + startObjectClass + " Package: " + startObjectPackage);
			
			// --- Get the Infos about the slots -------------------
			DefaultTableModel tm = currProject.ontologies4Project.getSlots4Class(startObjectClass);
			System.out.println(tm.toString());
			// --- Now, the GUI can be build ------------------------
			
			
			
		}
		
		
	}
	
	public static void main(String[] args) {
		
		JDialog jd = new JDialog();
		//jd.add(new DynForm(args));
		
	}
	
	
	
}
