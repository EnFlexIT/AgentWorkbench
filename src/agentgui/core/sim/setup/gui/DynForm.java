package agentgui.core.sim.setup.gui;

import java.awt.BorderLayout;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import agentgui.core.application.Project;
import agentgui.core.ontologies.OntologySingleClassDescription;
import agentgui.core.ontologies.OntologySingleClassSlotDescription;

public class DynForm extends JPanel{

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

	private Project currProject = null;
	private String currAgentReference = null;
	JPanel mainPanel = new JPanel();
	int frameHeight = 600;
	int frameWidht = 600;
	String startObjectPackage = "";
	public int actualX = 0;
	public int actualY = 0;
	public int einrueckungObersteEbene = 10;
	public int einrueckungProUntereEbene = 10;
	List<String> innerObjects = new ArrayList<String>();
	JPanel superPanel = new JPanel();
	JFrame testFrame = new JFrame();
	DefaultTreeModel objectTree = new DefaultTreeModel(new DefaultMutableTreeNode("Root"));
	
	public DynForm(Project project, String agentReference) {
		
		currProject = project;
		currAgentReference = agentReference;
	
		// --- Set the preferences for the Super Panel --- //
		superPanel.setPreferredSize(new Dimension(this.frameWidht, this.frameHeight));
		superPanel.setLayout(new BorderLayout());
	
		// --- Set the preferences for the Main Panel -- //
		mainPanel.setLayout(null);
		mainPanel.setPreferredSize(new Dimension(this.frameWidht, this.frameHeight));
		
		// --- Set the preferences for the testFrame -- //
		testFrame.setSize(this.frameWidht,this.frameHeight);
		testFrame.setVisible(true);
		
		// --- Set the preferences for the scrollPane -- //
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(superPanel);
		
		// --- Add Items --- //
		superPanel.add(mainPanel);
		testFrame.add(scrollPane);
	
		// --- Start building the GUI --- //
		this.start(agentReference);
		this.objectTreePrint();
	}
	
	/**
	 * This class starts building the GUI
	 * @param agentReference
	 */
	public void start(String agentReference){
		// --- Find Agent in AgentConfig --- //
		if ( currProject.AgentConfig.containsKey(currAgentReference)== false) {
			// TODO: Hier ein leeres JPanle zurückgeben ---!!!
			return;
		}
		
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
			
			System.out.println("Creating GUI");
			
			if(osc!=null)
				this.createGUI(osc, startObjectClassName, mainPanel, 0, (DefaultMutableTreeNode) objectTree.getRoot());
			else
				System.out.println("Could not get OntologySingleClassDescription for "+startObjectClass);
		}
		// --- Add the Submit Button in order to create the corresponding start object instances --- ///
		this.addSubmitButton();
	}
		
	/**
	 * This method adds the Submit Button to the end of the mainPanel
	 */
	private void addSubmitButton() {
		
		JButton submitButton = new JButton();
		submitButton.setBounds(new Rectangle(10, actualY + 30, 70, 25));
		submitButton.setText("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				objectTreePrint();
				//generateInstances(mainPanel, 0, ((DefaultMutableTreeNode)objectTree.getRoot()).getNextNode());
			}
		});
		mainPanel.add(submitButton);
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
	
	private void objectTreePrint(){
		DefaultTreeModel dtm = objectTree;
		if(objectTree != null)
		{
			objectTreePrintIt((DefaultMutableTreeNode) objectTree.getRoot());
			
		}
	}
	
	
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
		if(tiefe == 0)
		{
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) objectTree.getRoot();
			root.add(new DefaultMutableTreeNode(className));
		}
			
	}

	private void createAndFillObject(String className) {
		OntologySingleClassDescription oscd = getOntoSingleClsDesc(className);
		Class cl = oscd.getClazz();
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

	public int getActualYValue(JPanel pan){
		int value = 0;
		Component[] components = pan.getComponents();
		if(components.length>0)
			value = ((Component)components[components.length-1]).getY();
		return value;
	}
	
	/**
	 * This method adds the class to a panel and initiates to add
	 * possible inner classes and fields also to the panel or its subpanels
	 * @param osc
	 * @param startObjectClassName
	 * @param pan
	 * @param tiefe
	 */
	public void createGUI(OntologySingleClassDescription osc , String startObjectClassName, JPanel pan, int tiefe, DefaultMutableTreeNode node){
		if(osc != null){
			// --- if we are on the main panel -> add the class name to it --- //
			// --- TODO create a JPanel in which the class name (JLabel) and its innerclasses
			// --- and/or fields are added instead of mainPanel - class name - innerclasses/fields
			if(tiefe == 0){
				// --- Set the label for the class --- //
				actualY += 5;
				JLabel objectLabelName = new JLabel();
				objectLabelName.setBounds(new Rectangle(einrueckungObersteEbene, actualY , 150, 16));
				objectLabelName.setText(startObjectClassName);
				Font boldFont=new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize());
				objectLabelName.setFont(boldFont);
				
				// --- add the name of the class to the mainPanel
				mainPanel.add(objectLabelName);
				
				// --- increment the y-Value by the height of the inserted JLabel
				actualY += 20;
				
				// --- reset the innerObjects and add the actual (outer) class to it
				// --- this is necessary in order to handle self calling infinite recursion
				innerObjects.clear();
				innerObjects.add(this.startObjectPackage+"."+startObjectClassName);
				//System.out.println("Level 0: "+startObjectClassName);
				DynType dynType = new DynType("class",this.startObjectPackage+"."+startObjectClassName);
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
				node.add(newNode);
				node = newNode;
			}
			else{
				actualY += 10;
				JLabel objectLabelName = new JLabel();
				objectLabelName.setBounds(new Rectangle(10, 28 , 150, 16));
				String clsName = startObjectClassName.substring(startObjectClassName.lastIndexOf(".")+1);
				objectLabelName.setText(clsName);
				Font boldFont=new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize());
				objectLabelName.setFont(boldFont);
				pan.add(objectLabelName);
				
				// --- increment the y-Value by the height of the inserted JLabel
				//actualY += 25;
			}
			
			// --- go through each field / inner class ---
			Iterator<OntologySingleClassSlotDescription> iterator = osc.osdArr.iterator();
			while (iterator.hasNext()) {
				OntologySingleClassSlotDescription osd = iterator.next();
				String dataItemCardinality = osd.getSlotCardinality();
				String dataItemName = osd.getSlotName();
				String dataItemValue = osd.getSlotVarType();
				
				// --- if we have a field to which an object is assigned --> inner class
				if(isSpecialType(dataItemValue)){
					if(dataItemValue.matches("Instance of (.)*")){
						String clazz = dataItemValue.substring(12);
						if(objectAlreadyDisplayed(this.startObjectPackage+"."+clazz) == false)
						{
							innerObjects.add(this.startObjectPackage+"."+clazz);
							//DynType dynType = new DynType("innerClass",this.startObjectPackage+"."+clazz);
							//DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
							//node.add(newNode);
							//node = newNode;
							this.createInnerElements(dataItemName, dataItemValue , dataItemCardinality,  this.startObjectPackage+"."+clazz, tiefe+1, pan, node);
						}
						else
							System.out.println("Class " + this.startObjectPackage+"."+clazz + " already displayed!");
					}
				}
				// --- here we have a field with a final type (String, int, ...)
				else{
					this.createOuterElements(dataItemName, dataItemValue , dataItemCardinality, pan, tiefe, node);
				}
			}
		}
		else{
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
	public void createInnerElements(String dataItemName,String dataItemValue ,String dataItemCardinality, String startObjectClassName, int tiefe, final JPanel pan, DefaultMutableTreeNode node){
		DynType dynType = new DynType("innerClassType",dataItemName);
		dynType.setInnerClassType(dataItemValue);
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dynType);
		node.add(newNode);
		node = newNode;
		
		int lastDot = startObjectClassName.lastIndexOf(".");
		String plainStartObjectClassName = startObjectClassName.substring(lastDot + 1);
		
		// --- create a JPanel which will contain further inner classes and fields
		final JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX  = 0;

		// set the value of how much the panel shall be shifted (Einrückung)
		if(tiefe > 1)
			innerX= actualX + (einrueckungProUntereEbene*(tiefe-1));
		else
			innerX= actualX + (einrueckungProUntereEbene*(tiefe));
		dataPanel.setBounds(new Rectangle(innerX  , actualY , 270, 20));
		dataPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		dataPanel.setToolTipText(startObjectClassName + " Inner Panel");
		
		// --- create two JLabels: first displays the field name
		// --- the second displays the inner class name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName + " ["+dataItemValue+"]");
		valueFieldText.setBounds(new Rectangle(10, 5, 250, 16));
		
		
		
//		JLabel innerClassName = new JLabel();
//		innerClassName.setText(plainStartObjectClassName);
//		innerClassName.setBounds(new Rectangle(140, 0, 150, 16));
//		Font boldFont=new Font(innerClassName.getFont().getName(),Font.BOLD,innerClassName.getFont().getSize());
//		innerClassName.setFont(boldFont);
		
		// --- if the inner class has got a multi cardinality create an add-button
		JButton multipleButton = new JButton("+");
		multipleButton.setBounds(new Rectangle(250, 2, 35, 25));
		multipleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//showPanelComponents(dataPanel);
				addMultiple(dataPanel);
			}
		});
		
		//System.out.println("Adding: "+dataItemName);
		
		dataPanel.add(valueFieldText, null);
//		dataPanel.add(innerClassName, null);
		if(dataItemCardinality.equals("multiple"))
			dataPanel.add(multipleButton, null);
		
		
		// --- create the inner fields of the current inner class
		OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(startObjectClassName);
		this.createGUI(osc, startObjectClassName, dataPanel, tiefe, node);
	
		// --- set the correct height of the parent of this inner class according to the
		// --- inner class's height
		Rectangle r = pan.getBounds();
		if(dataPanel.getWidth() > r.width)
			pan.setBounds(r.x, r.y, dataPanel.getWidth(), r.height + 25);
		else
			pan.setBounds(r.x, r.y, r.width, r.height + 25);
		
		// --- finally add the inner class to its parent panel
		pan.add(dataPanel);
		
		// --- increment the actual Y value for further panels
		actualY += 45;		
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
		if(mainPanel.getHeight() > superPanel.getSize().height)
			superPanel.setPreferredSize(new Dimension(superPanel.getSize().width, mainPanel.getHeight()));
		
		JPanel dataPanelCopy = new JPanel();
		dataPanelCopy.setLayout(null);
		dataPanelCopy.setBorder(dataPanel.getBorder());
		
		// --- now create the copy
		this.createPanelCopy(dataPanelCopy,dataPanel);
		dataPanelCopy.setBounds(dataPanel.getX(), dataPanel.getY() + dataPanelHeight, dataPanel.getWidth(), dataPanel.getHeight());
		
		// --- add the created copy
		pan.add(dataPanelCopy);
		
		// --- refresh the GUI
		testFrame.validate();
	}
	
	/**
	 * This method tries to create a copy of the original Panel
	 * @param newPanel
	 * @param originalPanel
	 */
	private void createPanelCopy(final JPanel newPanel, JPanel originalPanel) {
		Component[] c = originalPanel.getComponents();
		for (Component component : c) {
			if(component instanceof JPanel)
			{
				JPanel pan = new JPanel();
				pan.setLayout(null);
				JPanel panOrig = (JPanel) component;
				pan.setBounds(panOrig.getX(), panOrig.getY(), panOrig.getWidth(), panOrig.getHeight());
				pan.setBorder(panOrig.getBorder());
				newPanel.add(pan);
				this.createPanelCopy(pan, (JPanel) component);
			}
			else if(component instanceof JLabel){
				JLabel label = new JLabel();
				JLabel origLabel = (JLabel) component;
				label.setBounds(origLabel.getX(), origLabel.getY(), origLabel.getWidth(), origLabel.getHeight());
				label.setText(origLabel.getText());
				label.setFont(origLabel.getFont());
				newPanel.add(label);
			}
			else if (component instanceof JTextField) {
				JTextField text = new JTextField();
				JTextField origText = (JTextField) component;
				text.setBounds(origText.getX(), origText.getY(), origText.getWidth(), origText.getHeight());
				newPanel.add(text);
			}
			else if (component instanceof JButton) {
//				JButton button = new JButton();
				JButton buttonOrig = (JButton) component;
//				button.setBounds(buttonOrig.getX(), buttonOrig.getY(), buttonOrig.getWidth(), buttonOrig.getHeight());
//				button.setText(buttonOrig.getText());
//				button.addActionListener(new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						addMultiple(newPanel);
//					}
//				});
//				
//				newPanel.add(button);
				
				JButton removeComponent = new JButton();
				removeComponent.setBounds(buttonOrig.getX(), buttonOrig.getY(), buttonOrig.getWidth(), buttonOrig.getHeight());
				removeComponent.setText("-");
				removeComponent.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JPanel parent = (JPanel) newPanel.getParent();
						newPanel.setVisible(false);
						moveOtherPanels4Multiple(newPanel, false);
						parent.remove(newPanel);
						parent.setBounds(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight() - newPanel.getHeight());
						parent.validate();
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
		if(dataPanel != null && dataPanel.getParent() instanceof JPanel){
			Component[] panChilds = ((JPanel) dataPanel.getParent()).getComponents();
			boolean flag = false;
			// --- go through the surrounding panels of the datapanel and move it
			// --- either to + Y-Dir (the panel is added) or to the - Y-Dir (the panel) 
			// --- shall be removed
			for (Component component : panChilds) {
				if(component == dataPanel)
					flag = true;
				if(add){
					if(flag && component != dataPanel){
						component.setBounds(component.getX(), component.getY() + dataPanel.getHeight(), component.getWidth(), component.getHeight());
					}
				}
				else
				{
					if(component.getY() > dataPanel.getY())
						component.setBounds(component.getX(), component.getY() - dataPanel.getHeight(), component.getWidth(), component.getHeight());
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
	public void createOuterElements(String dataItemName, String dataItemValue,  String dataItemCardinality, JPanel pan, int tiefe, DefaultMutableTreeNode node){
		
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX = 0;
		
		// --- this outer element has no parents which are inner classes
		// --- so its added to the mainPanel
		if(tiefe == 0)
		{
			//System.out.println("Flat object: " + dataItemName + " tiefe: " + tiefe + " actualY " + actualY);
			dataPanel.setBounds(new Rectangle(10, actualY  , 250, 30));
		}
		else
		{
			innerX = actualX + (einrueckungProUntereEbene*tiefe);
			dataPanel.setBounds(new Rectangle(innerX, this.getCorrectOuterHeight(pan) - 10, 250, 30));
		}
		//dataPanel.setBorder(BorderFactory.createLineBorder (Color.black, 1));	
		dataPanel.setToolTipText(dataItemName + "Panel");
		
		// --- add a JLabel to display the field's name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName + " ["+dataItemValue+"]");
		valueFieldText.setBounds(new Rectangle(0, 5, 130, 16));
		
		// --- add a JTextField for the value being entered
		// --- TODO check the type of the field and generate the right 
		// --- valueFields (Textfield, Checkbox (for boolean) , ... )
		JTextField valueField = new JTextField();
		valueField.setBounds(new Rectangle(140, 2, 100, 25));
		
		//System.out.println("Adding: "+dataItemName);
		
		// --- add both GUI elements to the panel
		dataPanel.add(valueFieldText, null);
		dataPanel.add(valueField);
		
		
		DynType dynType = new DynType("rawType",dataItemName, valueField);
		node.add(new DefaultMutableTreeNode(dynType));
		
		
		// --- increment the actual y position according to the height of the
		// --- label / textfield
		actualY += 30;
		
		// --- set the new height (increment the height) for the parent panel of the 
		// --- newly created panel
		Rectangle r = pan.getBounds();
		pan.setBounds(r.x, r.y, r.width + innerX, this.getCorrectOuterHeight(pan) + 25);
		pan.add(dataPanel);
		
		if(actualY > superPanel.getSize().height)
			superPanel.setPreferredSize(new Dimension(superPanel.getSize().width, actualY));
		
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
		}
		else if(valueType.equals("float")){
			flag = false;
		}
		else if(valueType.equals("int")){
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
