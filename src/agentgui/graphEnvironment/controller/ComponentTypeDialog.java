package agentgui.graphEnvironment.controller;

import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

/**
 * Additional GUI window for setting component types
 * @author Nils
 *
 */
public class ComponentTypeDialog extends JFrame {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2371556469030758361L;
	/**
	 * Table showing the components and their types
	 */
	private JTable compTypeTable = null;
	/**
	 * ComboBox for component type selection
	 */
	private JComboBox cbCompTypeEditor = null;
	/**
	 * The GraphEnvironmentControllerGUI this dialog belongs to
	 */
	GraphEnvironmentControllerGUI parent;
	/**
	 * Constructor	
	 * @param data Vector containing the ID and type of each component
	 * @param parent The GraphEnvironmentControllerGUI that opened this ComponentTypeDialog
	 */
	public ComponentTypeDialog(Vector<Vector<String>> data, GraphEnvironmentControllerGUI parent){
		this.parent = parent;
		this.setContentPane(new JScrollPane(getTable(data)));
		this.pack();		
	}
	/**
	 * This method builds the table
	 * @param data Vector containing the ID and type of each component
	 * @return The table
	 */
	private JTable getTable(Vector<Vector <String>> data){
			if(compTypeTable == null){
				// Column titles
				Vector<String> titles = new Vector<String>();
				titles.add("ID");
				titles.add("Typ");
				
				compTypeTable = new JTable(data, titles);
				
				// Set the JComboBox for editing the type column
				TableColumn typeColumn = compTypeTable.getColumnModel().getColumn(1);
				typeColumn.setCellEditor(new DefaultCellEditor(getCbTypeEditor()));
				
				// The SelectionListener tells parent to mark the corresponding SVG element when a row is selected  
				compTypeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent e) {
	//					System.out.println(table.getModel().getValueAt(e.getFirstIndex(), 0));
						String compID = (String) compTypeTable.getModel().getValueAt(e.getFirstIndex(), 0);
						parent.setSelectedElementByComponentID(compID);
					}
				});
			}
			return compTypeTable;
		}

	/**
	 * Initialize the JComboBox for type selection
	 * @return The cbTypeCellEditor
	 */
	JComboBox getCbTypeEditor(){
		if(cbCompTypeEditor == null){
			GasGridElements[] types = GasGridElements.values();
			Vector<String> classes = new Vector<String>();
			
			for(int i=0; i<types.length; i++){
				classes.add(GasGridElements.getClassName(types[i]));
			}
			
			cbCompTypeEditor = new JComboBox(classes);
			cbCompTypeEditor.addActionListener(parent);
		}
		return cbCompTypeEditor;
	}
}
