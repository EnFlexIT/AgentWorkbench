package agentgui.core.sim.setup.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.ontologies.OntologySingleClassDescription;
import agentgui.core.ontologies.OntologySingleClassSlotDescription;

public class DynForm extends JPanel {

	// === Frame + Panel Description === //
	/*
	 * 				testFrame (Frame)
	 * 				  |
	 * 		scrollPane(JScrollPane)	[ superPanel (JPanel + BorderLayout) ]
	 * 				  |
	 * 				mainPanel (JPanel)
	 * 				  |
	 * 		Class A (JLabel) - Fields of Class A (JPanel) -  Class B (JLabel) - Fields of Class B (JPanel) - ...
	 * 								    |													...
	 * 							 Class X / Field A (JTextField) - Field B - (JTextField) ...
	 */
	// === Frame + Panel Description === //
	
	private static final long serialVersionUID = 7942028680794127910L;

	private boolean debug = false;
	
	private Project currProject = null;
	private String currAgentReference = null;
	
	private String startObjectPackage = "";

	private JButton submitButton = null;
	private int einrueckungProUntereEbene = 5;
	private List<String> innerObjects = new ArrayList<String>();

	private DefaultTreeModel objectTree = new DefaultTreeModel(new DefaultMutableTreeNode("Root"));
	
	/**
	 * Constructor of this class
	 * @param project
	 * @param agentReference
	 */
	public DynForm(Project project, String agentReference) {
		
		super();
		currProject = project;
		currAgentReference = agentReference;
	
		// --- Set the preferences for the Main Panel ---------------
		this.setLayout(null);
		
		// --- Prevent errors through empty agent references --------
		if (currAgentReference!=null) {
			// --- Find Agent in AgentConfig ------------------------
			if (currProject.AgentConfig.containsKey(currAgentReference)==true) {
				// --- Start building the GUI -----------------------
				this.buildGUI();
				// --- If wanted show some debug informations -------
				if (debug==true) 
					this.objectTreePrint();	
			}
		}

	}
	
	/**
	 * This class starts building the GUI
	 * @param agentReference
	 */
	public void buildGUI(){
		
		// --- Which Start-Objects are configured for the Agent? ---- 
		TreeMap<Integer,String> startObjectList = currProject.AgentConfig.getReferencesAsTreeMap(currAgentReference);
		Vector<Integer> v = new Vector<Integer>(startObjectList.keySet()); 
		Collections.sort(v);
		
		// --- Iterate over the available Start-Objects --- //
		Iterator<Integer> it = v.iterator();
		while (it.hasNext()) {
			
			Integer startPosition = it.next();
			String startObjectClass = startObjectList.get(startPosition);
			startObjectPackage = startObjectClass.substring(0, startObjectClass.lastIndexOf("."));
			String startObjectClassName = startObjectClass.substring(startObjectClass.lastIndexOf(".") + 1, startObjectClass.length());
			
			// --- Get the Infos about the slots -------------------
			OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(startObjectClass);
			if (debug==true) {
				System.out.println("Creating GUI");	
			}	
			
			if(osc!=null) {
				this.createGUI(osc, startObjectClassName, this, 0, (DefaultMutableTreeNode) objectTree.getRoot());
			} else {
				System.out.println("Could not get OntologySingleClassDescription for "+startObjectClass);
			}
				
		}
		// --- Add the Submit Button in order to create the corresponding start object instances ---
		this.addSubmitButton();
		// --- Justify the Preferred Size of this Panle ---
		this.setPreferredSize(new Dimension(this.getBounds().width + 10, this.getBounds().height + 10));
	}
		
	/**
	 * This method adds the Submit Button to the end of this Panel
	 */
	private void addSubmitButton() {
		
		submitButton = new JButton();
		submitButton.setBounds(new Rectangle(5, this.getHeight() + 10, 100, 25));
		submitButton.setText(Language.translate("Speichern"));
		submitButton.setFont(new Font(submitButton.getFont().getName(),Font.BOLD,submitButton.getFont().getSize()));
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				objectTreePrint();
				//generateInstances(mainPanel, 0, ((DefaultMutableTreeNode)objectTree.getRoot()).getNextNode());
			}
		});
		this.add(submitButton);
		this.setPanelBounds(this);
	}

	/**
	 * This Method sets the PreferredSize of this Panel according 
	 * to the Position of the 'submitButton'
	 */
	private void setPreferredSize() {
		int newHeight = submitButton.getBounds().y + submitButton.getBounds().height + 10; 
		this.setPreferredSize(new Dimension(this.getBounds().width, newHeight));
	}
	
	
	private void generateInstances(JPanel pan, int tiefe, DefaultMutableTreeNode node){
		Component[] components = pan.getComponents();
		if(node!=null){
			for (int i = 0; i < components.length; i++) {
				if(tiefe == 0){
					if(components[i] instanceof JPanel){
						tiefe++;
						System.out.println("- Going deeper - tiefe " + tiefe);
						generateInstances((JPanel)components[i], tiefe, node);
						tiefe--;
						node = node.getNextNode();
					}
					else if(components[i] instanceof JLabel){
						
						if(((DynType) node.getUserObject()).isInnerClass())
							System.out.println("innerClass");
						else if(((DynType) node.getUserObject()).isClass())
							System.out.println("class");
						else if(((DynType) node.getUserObject()).isRawType())
							System.out.println("raw: " + ((DynType) node.getUserObject()).getFieldName());
						else if(((DynType) node.getUserObject()).isInnerClassType())
							System.out.println("innerClassT");
						else
							System.out.println("dunno");
						
						String className = ((JLabel)components[i]).getText();
						System.out.println("Here we have class: "+className);
						//addNewObjectClass(className, 0);	
						System.out.println("Class " + ((DynType) node.getUserObject()).getClassName());
						node = node.getNextNode();
					}
				}
				else{
					if(components[i] instanceof JPanel)
					{
						tiefe++;
						System.out.println("Going deeper - tiefe " + tiefe);
						generateInstances((JPanel)components[i], tiefe, node);
						tiefe--;
						node = node.getNextNode();
					}
					// Inner Class
					else if(components[i] instanceof JLabel && components[i+1] !=null && 
							components[i+1] instanceof JLabel)
					{
						
						System.out.println("InnerClass: " + ((DynType) node.getUserObject()).getFieldValue());
						System.out.println("Here we have (innerclass) "+((JLabel)components[i+1]).getText());
						node = node.getNextNode();
					}
					// Raw Field
					else{
						if (components[i] instanceof JLabel && components[i+1] != null && 
								components[i+1] instanceof JTextField) {
							String variableName = ((JLabel)components[i]).getText();
							String variableValue = ((JTextField)components[i+1]).getText();
							System.out.println("Node: " + ((DynType)node.getUserObject()).getFieldName() + " - " + variableName);
							System.out.println("Variable: "+ variableName + " Value: " + variableValue);
							node = node.getNextNode();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Call-method to print the content of the 'objectTree'
	 */
	private void objectTreePrint(){
		if(objectTree != null)		{
			objectTreePrintIt((DefaultMutableTreeNode) objectTree.getRoot());
		}
	}
	
	/**
	 * Method to print the content of a node (including sub-nodes)
	 * @param node
	 */
	private void objectTreePrintIt(DefaultMutableTreeNode node){
		
		String spacer = "";
		if(node != null){
			for(int i=0; i<node.getLevel(); i++)
				spacer += "*";
			if(node.getUserObject() instanceof DynType){
				DynType dynType = (DynType) node.getUserObject();
				if(dynType.isRawType() )
					System.out.println(spacer + " Field: " + dynType.getFieldName() + " Value: " + dynType.getFieldValue().getText());
				else if(dynType.isInnerClassType()){
					System.out.println(spacer + " Field: " + dynType.getFieldName() + " Value(InnerClass): " + dynType.getInnerClassType());
				}
				else
					System.out.println(spacer+" "+ dynType.getClassName());
			}
			objectTreePrintIt(node.getNextNode());
		}
	}
	
	private void addNewObjectClass(String className, int tiefe) {
		if(tiefe == 0) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) objectTree.getRoot();
			root.add(new DefaultMutableTreeNode(className));
		}
			
	}

	private void createAndFillObject(String className) {
		OntologySingleClassDescription oscd = getOntoSingleClsDesc(className);
		Class<?> cl = oscd.getClazz();
		Iterator<OntologySingleClassSlotDescription> iterator = oscd.osdArr.iterator();
		while (iterator.hasNext()) {
			OntologySingleClassSlotDescription osd = iterator.next();
			Hashtable<String, Method> meth = osd.getSlotMethodList();
			Iterator<String> methIT = meth.keySet().iterator();
			while (methIT.hasNext()) {
				String methodeName = methIT.next();
				Method methodeHimSelf = meth.get(methodeName);
			}
			
		}

		
	}

	private OntologySingleClassDescription getOntoSingleClsDesc(String className){
		return currProject.ontologies4Project.getSlots4ClassAsObject(this.startObjectPackage+"."+className);
	}
	
	private void showClassInformation(String className) {
		OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(this.startObjectPackage+"."+className);
		if(osc!=null){
			System.out.println( "---------------------------------------------------");
			System.out.println( "---------------------------------------------------");
			System.out.println( "Class<?>-Object: " + osc.getClazz() );
			System.out.println( "Klassenreferenz: " + osc.getClassReference() );
			System.out.println( "Package-Name   : " + osc.getPackageName() );
			System.out.println( "Klassen-Name   : " + osc.getClassName() );
			
			Iterator<OntologySingleClassSlotDescription> iterator = osc.osdArr.iterator();
			while (iterator.hasNext()) {
				OntologySingleClassSlotDescription osd = iterator.next();
				System.out.println( "---------------------------------------------------");
				System.out.println( "Slot-Name   : " + osd.getSlotName() );
				System.out.println( "Kardinalität: " + osd.getSlotCardinality() + " => CardinalityIsMultiple [bool] " + osd.isSlotCardinalityIsMultiple() );
				System.out.println( "VarTyp      : " + osd.getSlotVarType() );
				System.out.println( "Other Facts : " + osd.getSlotOtherFacts() );
				
				System.out.println( "Methoden    : " + osd.getSlotOtherFacts() );
				Hashtable<String, Method> meth = osd.getSlotMethodList();
				Iterator<String> methIT = meth.keySet().iterator();
				while (methIT.hasNext()) {
					String methodeName = methIT.next();
					Method methodeHimSelf = meth.get(methodeName);
					System.out.println( " - " + methodeName + " <=> " + methodeHimSelf);
				}
			}
		}
		else
			System.out.println("[Error] OntologySingleClassDescription is null");
		
	}

	public void setPanelBounds(JPanel pan){
		
		int maxX = 0;
		int maxY = 0;
		
		Component[] components = pan.getComponents();
		for (int i = 0; i < components.length; i++) {
			
			int currXMax = components[i].getX() + components[i].getWidth();
			int currYMax = components[i].getY() + components[i].getHeight();

			if (currXMax > maxX) maxX = currXMax;
			if (currYMax > maxY) maxY = currYMax;
		}
		
		maxX += 10;
		maxY += 2;
		pan.setBounds(new Rectangle(maxX, maxY));

	}
	
	/**
	 * This method adds the class to a panel and initiates to add
	 * possible inner classes and fields also to the panel or its subpanels
	 * @param osc
	 * @param startObjectClassName
	 * @param pan
	 * @param tiefe
	 */
	public void createGUI(OntologySingleClassDescription osc, String startObjectClassName, JPanel pan, int tiefe, DefaultMutableTreeNode node){
		
		JLabel objectLabelName = new JLabel();
		
		if (osc != null) {
			// --- if we are on the main panel -> add the class name to it !
			// --- Create a JPanel in which the class name (JLabel) and its innerclasses
			// --- and/or fields are added instead of mainPanel - class name - innerclasses/fields
			
			if(tiefe == 0){
				// --- Set the label for the class --- //
				objectLabelName.setBounds(new Rectangle(10, pan.getHeight()+5 , 200, 16));
				objectLabelName.setText(startObjectClassName);
				objectLabelName.setFont(new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize()));
				
				// --- reset the innerObjects and add the actual (outer) class to it
				// --- this is necessary in order to handle self calling infinite recursion
				innerObjects.clear();
				innerObjects.add(this.startObjectPackage+"."+startObjectClassName);
				
				DynType dynType = new DynType("class",this.startObjectPackage+"."+startObjectClassName);
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
				node.add(newNode);
				node = newNode;
				
			} else {
				// --- Set the label for the class --- //
				objectLabelName.setBounds(new Rectangle(10, 23 , 200, 16));
				objectLabelName.setText(startObjectClassName.substring(startObjectClassName.lastIndexOf(".")+1));
				objectLabelName.setFont(new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize()));
				
			}

			// --- add the name to the mainPanel
			pan.add(objectLabelName);
			this.setPanelBounds(pan);
			
			// --- go through each field / inner class ---
			Iterator<OntologySingleClassSlotDescription> iterator = osc.osdArr.iterator();
			while (iterator.hasNext()) {
				OntologySingleClassSlotDescription osd = iterator.next();
				String dataItemCardinality = osd.getSlotCardinality();
				String dataItemName = osd.getSlotName();
				String dataItemVarType = osd.getSlotVarType();
				
				// --- if we have a field to which an object is assigned --> inner class
				if(isSpecialType(dataItemVarType)){
					
					if(dataItemVarType.matches("Instance of (.)*")){
						String clazz = dataItemVarType.substring(12);
						if(objectAlreadyDisplayed(this.startObjectPackage+"."+clazz) == false) {
							
							innerObjects.add(this.startObjectPackage+"."+clazz);
							//DynType dynType = new DynType("innerClass",this.startObjectPackage+"."+clazz);
							//DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
							//node.add(newNode);
							//node = newNode;
							this.createInnerElements(dataItemName, dataItemVarType, dataItemCardinality,  this.startObjectPackage+"."+clazz, tiefe+1, pan, node);
							
						} else {
							if (debug==true) {
								System.out.println("Class " + this.startObjectPackage+"."+clazz + " already displayed!");	
							}							
						}
					}
					
				} else {
					// --- here we have a field with a final type (String, int, ...)
					this.createOuterElements(dataItemName, dataItemVarType, dataItemCardinality, pan, tiefe, node);
				}
			}
		
		} else {
			System.out.println("Could not create DefaultTableModel ("+startObjectClassName+")");
		}
		
	}	

	public boolean objectAlreadyDisplayed(String objectClass){
		String objects = "";
		for (String elem : innerObjects) {
			objects +=  " | " + elem;
			if(elem.equals(objectClass))
				return true;
		}
		return false;
	}

	/**
	 * This method creates inner elements (for inner classes)
	 * @param dataItemName
	 * @param dataItemCardinality
	 * @param startObjectClassName
	 * @param tiefe
	 * @param pan
	 * @param node 
	 */
	public void createInnerElements(String dataItemName, String dataItemVarType, String dataItemCardinality, String startObjectClassName, int tiefe, final JPanel pan, DefaultMutableTreeNode node){
		
		DynType dynType = new DynType("innerClassType",dataItemName);
		dynType.setInnerClassType(dataItemVarType);
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
		node.add(newNode);
		node = newNode;
		
		// --- create a JPanel which will contain further inner classes and fields
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX  = 0;

		// set the value of how much the panel shall be shifted (Einrückung)
		if(tiefe > 1)
			innerX = (einrueckungProUntereEbene*(tiefe-1));
		else
			innerX = (einrueckungProUntereEbene*(tiefe));
		dataPanel.setBounds(new Rectangle(innerX, 0 , 270, 20));
		dataPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		dataPanel.setToolTipText(startObjectClassName + " Inner Panel");
		
		// --- create two JLabels: first displays the field name
		// --- the second displays the inner class name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName + " ["+dataItemVarType+"]");
		valueFieldText.setBounds(new Rectangle(10, 5, 300, 16));
		
		dataPanel.add(valueFieldText, null);
		this.setPanelBounds(dataPanel);

		// --- if the inner class has got a multi cardinality create an add-button		
		if(dataItemCardinality.equals("multiple")) {

			JButton multipleButton = new JButton("+");
			multipleButton.setBounds(new Rectangle(310, 2, 35, 25));
			multipleButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//showPanelComponents(dataPanel);
					addMultiple(dataPanel);
				}
			});	
			dataPanel.add(multipleButton, null);
			this.setPanelBounds(dataPanel);
		}
		
		// --- create the inner fields of the current inner class
		OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(startObjectClassName);
		this.createGUI(osc, startObjectClassName, dataPanel, tiefe, node);
		
		// --- set the correct height of the parent of this inner class according to the
		// --- inner class's height
		Rectangle r = dataPanel.getBounds();
		dataPanel.setBounds(10, pan.getHeight(), r.width, r.height);
		
		// --- finally add the inner class to its parent panel
		pan.add(dataPanel);
		this.setPanelBounds(pan);
		
	}
	
	/**
	 * This method creates a copy of the passed JPanel and adds it
	 * @param dataPanel
	 */
	public void addMultiple(JPanel dataPanel){
		
		JPanel pan = (JPanel) dataPanel.getParent();
		int dataPanelHeight = dataPanel.getHeight();
		pan.setBounds(pan.getX(), pan.getY(), pan.getWidth(), pan.getHeight() + dataPanelHeight);
		
		// --- move surrounding panels into the y-direction by the the height of the
		// --- JPanel copy
		this.moveOtherPanels4Multiple(dataPanel, true);
		this.setPreferredSize();
		
		JPanel dataPanelCopy = new JPanel();
		dataPanelCopy.setLayout(null);
		dataPanelCopy.setBorder(dataPanel.getBorder());
		
		// --- now create the copy
		this.createPanelCopy(dataPanelCopy,dataPanel);
		dataPanelCopy.setBounds(dataPanel.getX(), dataPanel.getY() + dataPanelHeight, dataPanel.getWidth(), dataPanel.getHeight());
		
		// --- add the created copy
		pan.add(dataPanelCopy);
		
		// --- refresh the GUI
		this.validate();
	}
	
	/**
	 * This method removes the passed JPanel 
	 * @param dataPanel
	 */
	public void removeMultiple(JPanel dataPanel){
		
		JPanel parent = (JPanel) dataPanel.getParent();
		dataPanel.setVisible(false);
		moveOtherPanels4Multiple(dataPanel, false);
		parent.remove(dataPanel);
		parent.setBounds(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight() - dataPanel.getHeight());
		parent.validate();

		this.setPreferredSize();
		
	}
	
	/**
	 * This method tries to create a copy of the original Panel
	 * @param newPanel
	 * @param originalPanel
	 */
	private void createPanelCopy(final JPanel newPanel, JPanel originalPanel) {
		
		Component[] c = originalPanel.getComponents();
		for (Component component : c) {
			if(component instanceof JPanel) {
				JPanel pan = new JPanel();
				pan.setLayout(null);
				JPanel panOrig = (JPanel) component;
				pan.setBounds(panOrig.getX(), panOrig.getY(), panOrig.getWidth(), panOrig.getHeight());
				pan.setBorder(panOrig.getBorder());
				newPanel.add(pan);
				this.createPanelCopy(pan, (JPanel) component);
				
			} else if(component instanceof JLabel) {
				JLabel label = new JLabel();
				JLabel origLabel = (JLabel) component;
				label.setBounds(origLabel.getX(), origLabel.getY(), origLabel.getWidth(), origLabel.getHeight());
				label.setText(origLabel.getText());
				label.setFont(origLabel.getFont());
				newPanel.add(label);
				
			} else if (component instanceof JTextField) {
				JTextField text = new JTextField();
				JTextField origText = (JTextField) component;
				text.setBounds(origText.getX(), origText.getY(), origText.getWidth(), origText.getHeight());
				newPanel.add(text);
				
			} else if (component instanceof JButton) {
				JButton buttonOrig = (JButton) component;
				JButton removeComponent = new JButton();
				removeComponent.setBounds(buttonOrig.getX(), buttonOrig.getY(), buttonOrig.getWidth(), buttonOrig.getHeight());
				removeComponent.setText("-");
				removeComponent.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removeMultiple(newPanel);
					}
				});
				newPanel.add(removeComponent);
				
			}
		}
	}

	/**
	 * This method moves the surrounding panels into the + Y-Direction or into 
	 * the - Y-Direction
	 * @param dataPanel
	 * @param add
	 */
	public void moveOtherPanels4Multiple(JPanel dataPanel, boolean add){
		
		if (dataPanel!=null && dataPanel.getParent() instanceof JPanel) {
			Component[] panChilds = ((JPanel) dataPanel.getParent()).getComponents();
			boolean flag = false;
			// --- go through the surrounding panels of the datapanel and move it
			// --- either to + Y-Dir (the panel is added) or to the - Y-Dir (the panel) 
			// --- shall be removed
			for (Component component : panChilds) {
				if(component == dataPanel) {
					flag = true;
				}
				if(add){
					if(flag && component != dataPanel){
						component.setBounds(component.getX(), component.getY() + dataPanel.getHeight(), component.getWidth(), component.getHeight());
					}
					
				} else {
					if(component.getY() > dataPanel.getY()) {
						component.setBounds(component.getX(), component.getY() - dataPanel.getHeight(), component.getWidth(), component.getHeight());
					}
					
				}
			}
			
			if(dataPanel.getParent() instanceof JPanel)
				moveOtherPanels4Multiple((JPanel)dataPanel.getParent(), add);
		}	
		
	}
	
	
	public void showPanelComponents(JPanel pan){
		Component [] c = pan.getComponents();
		for (Component component : c) {
			if(component instanceof JPanel)
				showPanelComponents((JPanel)component);
			else if(component instanceof JLabel)
				System.out.println("JLabel: " + ((JLabel) component).getText());
		}
	}


	public int getCorrectOuterHeight(JPanel pan){
		int height = 0;
		Component[] c = pan.getComponents();
		for(int i = 0; i<c.length; i++){
			height += c[i].getHeight();
		}
		return height;
	}

	/**
	 * This method creates the panels for fields which have no inner classes
	 * @param dataItemName
	 * @param dataItemCardinality
	 * @param pan
	 * @param tiefe
	 * @param node 
	 */
	public void createOuterElements(String dataItemName, String dataItemVarType, String dataItemCardinality, JPanel pan, int tiefe, DefaultMutableTreeNode node){
		
		// --- this outer element has no parents which are inner classes
		// --- so its added to the mainPanel
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		dataPanel.setToolTipText(dataItemName + "-Panel");
		
		// --- add a JLabel to display the field's name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName + " ["+dataItemVarType+"]");
		valueFieldText.setBounds(new Rectangle(0, 4, 130, 16));
		
		// --- add a JTextField for the value being entered
		// --- TODO check the type of the field and generate the right 
		// --- valueFields (Textfield, Checkbox (for boolean) , ... )
		JTextField valueField = new JTextField();
		valueField.setBounds(new Rectangle(140, 0, 100, 25));
		
		// --- add both GUI elements to the panel
		dataPanel.add(valueFieldText, null);
		dataPanel.add(valueField);
		this.setPanelBounds(dataPanel);
		
		DynType dynType = new DynType("rawType", dataItemName, valueField);
		node.add(new DefaultMutableTreeNode(dynType));
		
		// --- set the new position (increment the height) for the parent panel of the 
		// --- newly created panel
		Rectangle pos = dataPanel.getBounds();
		pos.x = 10;//tiefe * einrueckungProUntereEbene;
		pos.y = pan.getHeight();
		dataPanel.setBounds(pos);

		pan.add(dataPanel);
		this.setPanelBounds(pan);
		
	}
	
	/**
	 * This method checks if the type of the field is a raw type
	 * (String, int, float, ...)
	 * @param valueType
	 * @return
	 */
	private boolean isSpecialType(String valueType){
		boolean flag = true;
		if(valueType.equals("String")){
			flag = false;
		} else if(valueType.equals("float")){
			flag = false;
		} else if(valueType.equals("int")){
			flag = false;
		} else if(valueType.equals("boolean")){
			flag = false;
		}
		return flag;
	}
	
	
	private void setDataValueField(String valueType, JPanel dataItemPanel) {
		if(valueType.equals("String")){
			JTextField dataValueField = new JTextField();
			dataValueField.setMaximumSize(new Dimension(100, 30));
			dataValueField.setAlignmentX(RIGHT_ALIGNMENT);
			dataItemPanel.add(dataValueField);
		}
		else if(valueType.equals("float")){
			JTextField dataValueField = new JTextField();
			dataValueField.setMaximumSize(new Dimension(100, 30));
			dataValueField.setAlignmentX(RIGHT_ALIGNMENT);
			dataItemPanel.add(dataValueField);
		}
		else if(valueType.equals("int")){
			JTextField dataValueField = new JTextField();
			dataValueField.setMaximumSize(new Dimension(100, 30));
			dataValueField.setAlignmentX(RIGHT_ALIGNMENT);
			dataItemPanel.add(dataValueField);
		}
		else{
			JLabel dataValueField = new JLabel();
			dataValueField.setText("Unknown Type");
			dataValueField.setMaximumSize(new Dimension(100, 30));
			dataValueField.setAlignmentX(RIGHT_ALIGNMENT);
			System.out.println("Unknown: "+valueType);
			if(valueType.matches("Instance of (.)*")){
				
				String clazz = valueType.substring(12);
				System.out.println("The class: "+clazz + " Package: " + this.startObjectPackage);
				System.out.println("+++++++++++++++ Start deep search +++++++++++++++++++++");
			}
			else
			{
				dataItemPanel.add(dataValueField);
			}
		}
		

		System.out.println("Type: "+valueType);
		
	}	
}
