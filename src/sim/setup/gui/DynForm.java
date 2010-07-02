package sim.setup.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.sun.xml.internal.bind.v2.TODO;

import mas.onto.OntologySingleClassDescription;
import mas.onto.OntologySingleClassSlotDescription;

import application.Project;

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
				this.createGUI(osc, startObjectClassName, mainPanel, 0);
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
		submitButton.setBounds(new Rectangle(0, actualY + 30, 70, 25));
		submitButton.setText("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateInstances(mainPanel, 0);
			}
		});
		mainPanel.add(submitButton);
	}

	
	private void generateInstances(JPanel pan, int tiefe){
		Component[] components = pan.getComponents();
		for (int i = 0; i < components.length; i++) {
			if(tiefe == 0){
				if(components[i] instanceof JPanel){
					tiefe++;
					generateInstances((JPanel)components[i], tiefe);
				}
				else if(components[i] instanceof JLabel){
					String className = ((JLabel)components[i]).getText();
					System.out.println("Here we have class: "+className);
					try {
						Class cl = Class.forName(this.startObjectPackage+"."+className);
						for (Method m : cl.getMethods()) {
							System.out.println(m.getReturnType().toString() + " - " + m.getName().toString());
							for (Class paramClass: m.getParameterTypes()) {
								System.out.print(" "+paramClass.getName());
							}
						}
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else{
				if(components[i] instanceof JPanel)
				{
					tiefe++;
					generateInstances((JPanel)components[i], tiefe);
				}
				else if(components[i] instanceof JLabel && components[i+1] !=null && 
						components[i+1] instanceof JLabel)
					System.out.println("Here we have (innerclass) "+((JLabel)components[i+1]).getText());
				else{
					if (components[i] instanceof JLabel && components[i+1] != null && 
							components[i+1] instanceof JTextField) {
						String variableName = ((JLabel)components[i]).getText();
						String variableValue = ((JTextField)components[i+1]).getText();
						System.out.println("Variable: "+ variableName + " Value: " + variableValue);
					}
				}
			}
		}
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
	public void createGUI(OntologySingleClassDescription osc , String startObjectClassName, JPanel pan, int tiefe){
		if(osc != null){
			// --- if we are on the main panel -> add the class name to it --- //
			// --- TODO create a JPanel in which the class name (JLabel) and its innerclasses
			// --- and/or fields are added instead of mainPanel - class name - innerclasses/fields
			if(tiefe == 0){
				// --- Set the label for the class --- //
				JLabel objectLabelName = new JLabel();
				objectLabelName.setBounds(new Rectangle(einrueckungObersteEbene, actualY , 150, 16));
				objectLabelName.setText(startObjectClassName);
				Font boldFont=new Font(objectLabelName.getFont().getName(),Font.BOLD,objectLabelName.getFont().getSize());
				objectLabelName.setFont(boldFont);
				
				// --- add the name of the class to the mainPanel
				mainPanel.add(objectLabelName);
				
				// --- increment the y-Value by the height of the inserted JLabel
				actualY += 15;
				
				// --- reset the innerObjects and add the actual (outer) class to it
				// --- this is necessary in order to handle self calling infinite recursion
				innerObjects.clear();
				innerObjects.add(this.startObjectPackage+"."+startObjectClassName);
				System.out.println("Level 0: "+startObjectClassName);
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
							this.createInnerElements(dataItemName, dataItemCardinality,  this.startObjectPackage+"."+clazz, tiefe+1, pan);
						}
						else
							System.out.println("Class " + this.startObjectPackage+"."+clazz + " already displayed!");
					}
				}
				// --- here we have a field with a final type (String, int, ...)
				else{
					this.createOuterElements(dataItemName, dataItemCardinality, pan, tiefe);
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
	 */
	public void createInnerElements(String dataItemName, String dataItemCardinality, String startObjectClassName, int tiefe, final JPanel pan){
		
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
		dataPanel.setBounds(new Rectangle(innerX  , actualY , 300, 20));
		dataPanel.setBorder(BorderFactory.createLineBorder (Color.yellow, 1));
		dataPanel.setToolTipText(startObjectClassName + " Inner Panel");
		
		// --- create two JLabels: first displays the field name
		// --- the second displays the inner class name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName);
		valueFieldText.setBounds(new Rectangle(0, 0, 130, 16));
		
		JLabel innerClassName = new JLabel();
		innerClassName.setText(plainStartObjectClassName);
		innerClassName.setBounds(new Rectangle(140, 0, 150, 16));
		Font boldFont=new Font(innerClassName.getFont().getName(),Font.BOLD,innerClassName.getFont().getSize());
		innerClassName.setFont(boldFont);
		
		// --- if the inner class has got a multi cardinality create an add-button
		JButton multipleButton = new JButton("+");
		multipleButton.setBounds(new Rectangle(250, 0, 35, 25));
		multipleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//showPanelComponents(dataPanel);
				addMultiple(dataPanel);
			}
		});
		
		System.out.println("Adding: "+dataItemName);
		
		dataPanel.add(valueFieldText, null);
		dataPanel.add(innerClassName, null);
		if(dataItemCardinality.equals("multiple"))
			dataPanel.add(multipleButton, null);
		
		// --- create the inner fields of the current inner class
		OntologySingleClassDescription osc = currProject.ontologies4Project.getSlots4ClassAsObject(startObjectClassName);
		this.createGUI(osc, startObjectClassName, dataPanel, tiefe);
	
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
		actualY += 50;		
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
				JButton button = new JButton();
				JButton buttonOrig = (JButton) component;
				button.setBounds(buttonOrig.getX(), buttonOrig.getY(), buttonOrig.getWidth(), buttonOrig.getHeight());
				button.setText(buttonOrig.getText());
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addMultiple(newPanel);
					}
				});
				
				newPanel.add(button);
				
				JButton removeComponent = new JButton();
				removeComponent.setBounds(button.getX() + button.getWidth(), button.getY(), button.getWidth(), button.getHeight());
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
	 */
	public void createOuterElements(String dataItemName, String dataItemCardinality, JPanel pan, int tiefe){
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(null);
		int innerX = 0;
		
		// --- this outer element has no parents which are inner classes
		// --- so its added to the mainPanel
		if(tiefe == 0)
		{
			System.out.println("Flat object: " + dataItemName + " tiefe: " + tiefe + " actualY " + actualY);
			dataPanel.setBounds(new Rectangle(10, actualY  , 250, 30));
		}
		else
		{
			innerX = actualX + (einrueckungProUntereEbene*tiefe);
			dataPanel.setBounds(new Rectangle(innerX, this.getCorrectOuterHeight(pan) - 10, 250, 30));
		}
		dataPanel.setBorder(BorderFactory.createLineBorder (Color.black, 1));	
		dataPanel.setToolTipText(dataItemName + "Panel");
		
		// --- add a JLabel to display the field's name
		JLabel valueFieldText = new JLabel();
		valueFieldText.setText(dataItemName);
		valueFieldText.setBounds(new Rectangle(0, 5, 130, 16));
		
		// --- add a JTextField for the value being entered
		// --- TODO check the type of the field and generate the right 
		// --- valueFields (Textfield, Checkbox (for boolean) , ... )
		JTextField valueField = new JTextField();
		valueField.setBounds(new Rectangle(140, 5, 100, 25));
		
		System.out.println("Adding: "+dataItemName);
		
		// --- add both GUI elements to the panel
		dataPanel.add(valueFieldText, null);
		dataPanel.add(valueField);
		
		// --- increment the actual y position according to the height of the
		// --- label / textfield
		actualY += 35;
		
		// --- set the new height (increment the height) for the parent panel of the 
		// --- newly created panel
		Rectangle r = pan.getBounds();
		pan.setBounds(r.x, r.y, r.width + innerX, this.getCorrectOuterHeight(pan) + 30);
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
