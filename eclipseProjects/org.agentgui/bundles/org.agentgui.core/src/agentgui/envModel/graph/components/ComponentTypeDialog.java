/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.graph.components;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.imaging.MissingIcon;
import agentgui.core.project.Project;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS.ComponentSorting;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS.EdgeShape;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
import de.enflexit.common.swing.AwbBasicTabbedPaneUI;
import jade.core.Agent;

/**
 * Dialog to configure general settings, domains and types od {@link NetworkComponent}s.
 *  
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ComponentTypeDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private boolean currSnap2Grid = true;
	private double currSnapRaster = 5; 
	private EdgeShape currEdgeShape; 
	private ComponentSorting currComponentSorting;
	
	private Project currProject;
	private boolean canceled;

	private JTabbedPane jTabbedPane;

	private JPanel jContentPane;
	private ComponentTypeDialogDomains jPanelDomains;
	private ComponentTypeDialogComponents jPanelComponents;
	
	private JPanel jPanelGeneralSettings;
	private JPanel jPanelButtonOkCancel;

	private JLabel jLabelGridHeader;
	private JLabel jLabelGuideGridWidth;
	private JCheckBox jCheckBoxSnap2Grid;
	private JSpinner jSpnnerGridWidth;

	private JButton jButtonOk;
	private JButton jButtonCancel;

	private JLabel jLabelListSorting;
	private JComboBox<ComponentSorting> jComboBoxListSorting;
	private DefaultComboBoxModel<ComponentSorting> comboBoxModelComponentSorting;
	
	private JLabel jLabelEdgeShape;
	private JComboBox<EdgeShape> jComboBoxEdgeShapes;
	private DefaultComboBoxModel<EdgeShape> comboBoxModelEdgeShapes;
	
	private TableCellEditor4ClassSelector agentClassesCellEditor;  		
	private TableCellEditor4ClassSelector prototypeClassesCellEditor;  	
	private TableCellEditor4ClassSelector adapterClassesCellEditor;  
	

	/**
	 * Instantiates a new component type dialog.
	 *
	 * @param ownerWindow the owner window
	 * @param graphSettings the graph settings
	 * @param project the project
	 */
	public ComponentTypeDialog(Window ownerWindow, GeneralGraphSettings4MAS graphSettings, Project project) {
		super(ownerWindow);
		this.setStartArguments(graphSettings, project);
		this.initialize();
	}

	/**
	 * Sets the start arguments to the local variables.
	 *
	 * @param graphSettings the graph settings
	 * @param project the project
	 */
	private void setStartArguments(GeneralGraphSettings4MAS graphSettings, Project project) {
		this.currProject = project;
		this.getJPanelDomains().setDomainSettings(graphSettings.getDomainSettings());
		this.getJPanelComponents().setComponentTypeSettings(graphSettings.getCurrentCTS());
		this.currSnap2Grid = graphSettings.isSnap2Grid();
		this.currSnapRaster = graphSettings.getSnapRaster();
		this.currEdgeShape = graphSettings.getEdgeShape();
		this.currComponentSorting = graphSettings.getComponentSorting();
	}
	/**
	 * This method initialises this.
	 */
	private void initialize() {
		
		this.setTitle(Language.translate("Komponententyp-Definition"));
		this.setModal(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
		this.registerEscapeKeyStroke();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
	    int width  = (int) (screenSize.getWidth() * 0.7);
	    int height = (int) (screenSize.getHeight() * 0.7);
	    this.setSize(width, height);
	    this.setLocationRelativeTo(null);
	    
	    this.setContentPane(this.getJContentPane());
	    this.setGeneralConfiguration();
	    
	    // --- In case that we're in an executed MAS ------
	    if (this.currProject==null) {
	    	this.getJPanelDomains().setEnabled(false);
	    	this.getJPanelComponents().setEnabled(false);
	    	this.getJButtonOk().setEnabled(false);
	    }
	    
	}
	
    /**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent ae) {
            	// --- Stop cell editing, if required ----- 
				if (ComponentTypeDialog.this.getJPanelDomains().isStopCellEditing()) return;
				if (ComponentTypeDialog.this.getJPanelComponents().isStopCellEditing()) return;
            	// --- Close dialog -----------------------
				String title = Language.translate("Schließen") + " ?";
				String message = Language.translate("Dialog schließen") + " ?";
				if (JOptionPane.showConfirmDialog(ComponentTypeDialog.this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
					canceled = true;
	    			setVisible(false);	
				}
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * This will set the current configuration for the nodes (propagation points) to the current view.
	 */
	private void setGeneralConfiguration() {
		// --- Get the Guide grid configuration -------------------------------
		this.getJCheckBoxSnap2Grid().setSelected(this.currSnap2Grid);
		this.getJSpinnerGridWidth().setValue(this.currSnapRaster);
		this.getJComboBoxEdgeShapes().setSelectedItem(this.currEdgeShape);
		this.getJComboBoxListSorting().setSelectedItem(this.currComponentSorting);
	}
	
	/**
	 * Returns the current project instance.
	 * @return the project
	 */
	public Project getProject() {
		return currProject;
	}
	
	/**
	 * Returns all component type settings.
	 * @return the ComponentTypeSettings
	 */
	public TreeMap<String, ComponentTypeSettings> getComponentTypeSettings() {
		return this.getJPanelComponents().getComponentTypeSettings();
	}
	/**
	 * Returns the domain settings.
	 * @return the DomainSettings
	 */
	public TreeMap<String, DomainSettings> getDomainSettings() {
		return this.getJPanelDomains().getDomainSettings();
	}
	
	/**
	 * Checks if is snap2 grid.
	 * @return true, if is snap2 grid
	 */
	public boolean isSnap2Grid() {
		return this.currSnap2Grid;
	}
	/**
	 * Gets the snap raster.
	 * @return the snap raster
	 */
	public double getSnapRaster(){
		return this.currSnapRaster;
	}
	/**
	 * Gets the edge shape.
	 * @return the edge shape
	 */
	public EdgeShape getEdgeShape() {
		return this.currEdgeShape;
	}
	/**
	 * Gets the component sorting.
	 * @return the component sorting
	 */
	public ComponentSorting getComponentSorting() {
		return this.currComponentSorting;
	}
	/**
	 * Returns the GeneralGraphSettings4MAS.
	 * @return the GeneralGraphSettings4MAS for the graph environment
	 */
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		GeneralGraphSettings4MAS genSettings = new GeneralGraphSettings4MAS();
		genSettings.setCurrentCTS(this.getComponentTypeSettings());
		genSettings.setDomainSettings(this.getDomainSettings());
		genSettings.setSnap2Grid(this.isSnap2Grid());
		genSettings.setSnapRaster(this.getSnapRaster());
		genSettings.setEdgeShape(this.getEdgeShape());
		genSettings.setComponentSorting(this.getComponentSorting());
		return genSettings;
	}
	
	/**
	 * This method initializes jContentPane.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridheight = 3;
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 1;
			gridBagConstraints21.insets = new Insets(15, 15, 20, 15);
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			gridBagConstraints110.fill = GridBagConstraints.BOTH;
			gridBagConstraints110.weighty = 1.0;
			gridBagConstraints110.gridx = 0;
			gridBagConstraints110.gridy = 0;
			gridBagConstraints110.insets = new Insets(15, 15, 0, 15);
			gridBagConstraints110.weightx = 1.0;
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJTabbedPane(), gridBagConstraints110);
			jContentPane.add(getJPanelButtonOkCancel(), gridBagConstraints21);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jTabbedPane	.
	 *
	 * @return javax.swing.JTabbedPane
	 */
	public JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setUI(new AwbBasicTabbedPaneUI());
			jTabbedPane.setFont(new Font("Dialog", Font.BOLD, 13));
			jTabbedPane.addTab("Teilnetze", null, getJPanelDomains(), null);
			jTabbedPane.addTab("Netzwerk-Komponenten", null, getJPanelComponents(), null);
			jTabbedPane.addTab("Allgemein", null, getJPanelGeneralSettings(), null);
			
			jTabbedPane.setTitleAt(0, Language.translate("Teilnetze"));
			jTabbedPane.setTitleAt(1, Language.translate("Netzwerk-Komponenten"));
			jTabbedPane.setTitleAt(2, Language.translate("Allgemein"));
			jTabbedPane.setSelectedIndex(1);

			jTabbedPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ce) {
					ComponentTypeDialog.this.getJPanelDomains().isStopCellEditing();
					ComponentTypeDialog.this.getJPanelComponents().isStopCellEditing();
					setTableCellEditor4DomainsInComponents(null);
				}
			});
			
		}
		return jTabbedPane;
	}

	
	/**
	 * This method initializes jPanelDomains	.
	 * @return javax.swing.JPanel
	 */
	private ComponentTypeDialogDomains getJPanelDomains() {
		if (jPanelDomains == null) {
			jPanelDomains = new ComponentTypeDialogDomains(this);
		}
		return jPanelDomains;
	}
	/**
	 * Return the current domain vector.
	 * @return the domain vector
	 */
	public Vector<String> getDomainVector() {
		return this.getJPanelDomains().getDomainVector();
	}
	
	
	/**
	 * This method initializes jPanelComponents	.
	 * @return javax.swing.JPanel
	 */
	private ComponentTypeDialogComponents getJPanelComponents() {
		if (jPanelComponents == null) {
			jPanelComponents = new ComponentTypeDialogComponents(this);
		}
		return jPanelComponents;
	}
	
	
	/**
	 * This method initializes jPanelGeneralSettings	.
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelGeneralSettings() {
		if (jPanelGeneralSettings == null) {
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
			gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
			gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			
			
			GridBagConstraints gbc_jLabelListSorting = new GridBagConstraints();
			gbc_jLabelListSorting.insets = new Insets(20, 20, 0, 0);
			gbc_jLabelListSorting.gridx = 0;
			gbc_jLabelListSorting.gridy = 0;
			GridBagConstraints gbc_jComboBoxListSorting = new GridBagConstraints();
			gbc_jComboBoxListSorting.insets = new Insets(20, 5, 0, 0);
			gbc_jComboBoxListSorting.gridwidth = 2;
			gbc_jComboBoxListSorting.fill = GridBagConstraints.HORIZONTAL;
			gbc_jComboBoxListSorting.gridx = 1;
			gbc_jComboBoxListSorting.gridy = 0;
			
			GridBagConstraints gbc_JComboBoxEdgeShapes = new GridBagConstraints();
			gbc_JComboBoxEdgeShapes.fill = GridBagConstraints.HORIZONTAL;
			gbc_JComboBoxEdgeShapes.gridy = 3;
			gbc_JComboBoxEdgeShapes.gridwidth = 2;
			gbc_JComboBoxEdgeShapes.insets = new Insets(20, 5, 0, 0);
			gbc_JComboBoxEdgeShapes.gridx = 1;
			GridBagConstraints gbc_JLabelEdgeShape = new GridBagConstraints();
			gbc_JLabelEdgeShape.gridx = 0;
			gbc_JLabelEdgeShape.anchor = GridBagConstraints.WEST;
			gbc_JLabelEdgeShape.insets = new Insets(20, 20, 0, 0);
			gbc_JLabelEdgeShape.gridy = 3;
			GridBagConstraints gbc_getJSpinnerGridWidth = new GridBagConstraints();
			gbc_getJSpinnerGridWidth.anchor = GridBagConstraints.WEST;
			gbc_getJSpinnerGridWidth.insets = new Insets(5, 5, 5, 0);
			gbc_getJSpinnerGridWidth.gridx = 2;
			gbc_getJSpinnerGridWidth.gridy = 2;
			gbc_getJSpinnerGridWidth.fill = GridBagConstraints.NONE;
			GridBagConstraints gbc_JLabelGuideGridWidth = new GridBagConstraints();
			gbc_JLabelGuideGridWidth.anchor = GridBagConstraints.WEST;
			gbc_JLabelGuideGridWidth.gridx = 1;
			gbc_JLabelGuideGridWidth.gridy = 2;
			gbc_JLabelGuideGridWidth.insets = new Insets(5, 10, 5, 0);
			GridBagConstraints gbc_JCheckBoxSnap2Grid = new GridBagConstraints();
			gbc_JCheckBoxSnap2Grid.anchor = GridBagConstraints.WEST;
			gbc_JCheckBoxSnap2Grid.gridwidth = 2;
			gbc_JCheckBoxSnap2Grid.gridx = 1;
			gbc_JCheckBoxSnap2Grid.gridy = 1;
			gbc_JCheckBoxSnap2Grid.insets = new Insets(20, 10, 5, 0);
			GridBagConstraints gbc_JLabelGridHeader = new GridBagConstraints();
			gbc_JLabelGridHeader.anchor = GridBagConstraints.WEST;
			gbc_JLabelGridHeader.gridwidth = 1;
			gbc_JLabelGridHeader.gridx = 0;
			gbc_JLabelGridHeader.gridy = 1;
			gbc_JLabelGridHeader.insets = new Insets(20, 20, 5, 5);
			
			jLabelGridHeader = new JLabel();
			jLabelGridHeader.setText("Hilfs-Raster");
			jLabelGridHeader.setText(Language.translate(jLabelGridHeader.getText()) + ":");
			jLabelGridHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelGuideGridWidth = new JLabel();
			jLabelGuideGridWidth.setText("Raster-Breite");
			jLabelGuideGridWidth.setText(Language.translate(jLabelGuideGridWidth.getText()));
			
			jLabelEdgeShape = new JLabel();
			jLabelEdgeShape.setText("Kanten-Typ");
			jLabelEdgeShape.setText(Language.translate(jLabelEdgeShape.getText()) + ":");
			jLabelEdgeShape.setFont(new Font("Dialog", Font.BOLD, 12));
			
			
			jPanelGeneralSettings = new JPanel();
			jPanelGeneralSettings.setLayout(gridBagLayout);
			jPanelGeneralSettings.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			
			jPanelGeneralSettings.add(getJLabelListSorting(), gbc_jLabelListSorting);
			jPanelGeneralSettings.add(getJComboBoxListSorting(), gbc_jComboBoxListSorting);
			jPanelGeneralSettings.add(jLabelGridHeader, gbc_JLabelGridHeader);
			jPanelGeneralSettings.add(getJCheckBoxSnap2Grid(), gbc_JCheckBoxSnap2Grid);
			jPanelGeneralSettings.add(jLabelGuideGridWidth, gbc_JLabelGuideGridWidth);
			jPanelGeneralSettings.add(getJSpinnerGridWidth(), gbc_getJSpinnerGridWidth);
			jPanelGeneralSettings.add(jLabelEdgeShape, gbc_JLabelEdgeShape);
			jPanelGeneralSettings.add(getJComboBoxEdgeShapes(), gbc_JComboBoxEdgeShapes);
			
		}
		return jPanelGeneralSettings;
	}
	private JLabel getJLabelListSorting() {
		if (jLabelListSorting == null) {
			jLabelListSorting = new JLabel();
			jLabelListSorting.setText("Listen-Sortierung");
			jLabelListSorting.setText(Language.translate(jLabelListSorting.getText()) + ":");
			jLabelListSorting.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelListSorting;
	}
	private JComboBox<ComponentSorting> getJComboBoxListSorting() {
		if (jComboBoxListSorting == null) {
			jComboBoxListSorting = new JComboBox<>(this.getComboBoxModel4ComponentSorting());
		}
		return jComboBoxListSorting;
	}
	private DefaultComboBoxModel<ComponentSorting> getComboBoxModel4ComponentSorting() {
		if (comboBoxModelComponentSorting==null) {
			comboBoxModelComponentSorting = new DefaultComboBoxModel<>();
			for (ComponentSorting csElement : ComponentSorting.values()) {
				comboBoxModelComponentSorting.addElement(csElement);
			}
		}
		return comboBoxModelComponentSorting;
	}
	
	private JCheckBox getJCheckBoxSnap2Grid() {
		if (jCheckBoxSnap2Grid == null) {
			jCheckBoxSnap2Grid = new JCheckBox();
			jCheckBoxSnap2Grid.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jCheckBoxSnap2Grid.setText("Raster verwenden");
			jCheckBoxSnap2Grid.setText(Language.translate(jCheckBoxSnap2Grid.getText()));
		}
		return jCheckBoxSnap2Grid;
	}
	private JSpinner getJSpinnerGridWidth() {
		if (jSpnnerGridWidth == null) {
			jSpnnerGridWidth = new JSpinner(new SpinnerNumberModel(5.0, 0.1, 100.0, 0.1));
			jSpnnerGridWidth.setPreferredSize(new Dimension(60, 26));
		}
		return jSpnnerGridWidth;
	}
	
	private JComboBox<EdgeShape> getJComboBoxEdgeShapes() {
		if (jComboBoxEdgeShapes == null) {
			jComboBoxEdgeShapes = new JComboBox<>(this.getComboBoxModel4EdgeShapes());
			jComboBoxEdgeShapes.setMaximumRowCount(12);
		}
		return jComboBoxEdgeShapes;
	}
	private DefaultComboBoxModel<EdgeShape> getComboBoxModel4EdgeShapes() {
		if (comboBoxModelEdgeShapes==null) {
			comboBoxModelEdgeShapes = new DefaultComboBoxModel<EdgeShape>();
			List<EdgeShape> shapes = new ArrayList<EdgeShape>(Arrays.asList(EdgeShape.values()));
			for (int i = 0; i < shapes.size(); i++) {
				comboBoxModelEdgeShapes.addElement(shapes.get(i));
			}
		}
		return comboBoxModelEdgeShapes;
	}
	
	private JPanel getJPanelButtonOkCancel() {
		if (jPanelButtonOkCancel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.EAST;
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.weightx = 0.0;
			gridBagConstraints8.insets = new Insets(0, 0, 0, 30);
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(0, 30, 0, 0);
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.anchor = GridBagConstraints.CENTER;
			gridBagConstraints9.gridx = 1;
			jPanelButtonOkCancel = new JPanel();
			jPanelButtonOkCancel.setLayout(new GridBagLayout());
			jPanelButtonOkCancel.add(this.getJButtonCancel(), gridBagConstraints9);
			jPanelButtonOkCancel.add(this.getJButtonOk(), gridBagConstraints8);
		}
		return jPanelButtonOkCancel;
	}
	/**
	 * This method initializes jButtonOk	.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton();
			jButtonOk.setText("Übernehmen");
			jButtonOk.setText(Language.translate(jButtonOk.getText()));
			jButtonOk.setPreferredSize(new Dimension(120, 28));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	/**
	 * This method initializes jButtonCancel	.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Abbrechen");
			jButtonCancel.setText(Language.translate(jButtonCancel.getText()));
			jButtonCancel.setPreferredSize(new Dimension(120, 28));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	
	/**
	 * Returns the ClassSelectionDialog cell editor for Agent classes.
	 * @return cell editor for the class selection
	 */
	public TableCellEditor4ClassSelector getAgentClassesCellEditor(){
		if(agentClassesCellEditor == null){
			agentClassesCellEditor = new TableCellEditor4ClassSelector(Application.getMainWindow(), Agent.class, "", "", Language.translate("Agenten"), true);
		}
		return agentClassesCellEditor;
	}
	/**
	 * Returns the ClassSelectionDialog cell editor for NetworkComponentAdapter classes.
	 * @return cell editor for the class selection
	 */
	public TableCellEditor4ClassSelector getAdapterClassesCellEditor(){
		if(adapterClassesCellEditor == null){
			adapterClassesCellEditor = new TableCellEditor4ClassSelector(Application.getMainWindow(), NetworkComponentAdapter.class, "", "", Language.translate("Erweiterungsadapter für Netzwerkkomponenten"), true);
		}
		return adapterClassesCellEditor;
	}
	/**
	 * Returns the ClassSelectionDialog cell editor for classes of GraphElementPrototype.
	 * @return cell editor for the class selection
	 */
	public TableCellEditor4ClassSelector getPrototypeClassesCellEditor(){
		if(prototypeClassesCellEditor == null){
			prototypeClassesCellEditor = new TableCellEditor4ClassSelector(Application.getMainWindow(), GraphElementPrototype.class, "", "", Language.translate("Graph-Prototypen"), false);
		}
		return prototypeClassesCellEditor;
	}
	
	/**
	 * Gets the check box edge width.
	 * @return the check box edge width
	 */
	public JCheckBox getCheckBoxEdgeWidth() {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);
		return checkBox;
	}
	
	
	/**
	 * Sets the table CellEditor for domains in components.
	 * @param domainVector the string vector of the current domains
	 */
	public void setTableCellEditor4DomainsInComponents(Vector<String> domainVector){
		this.getJPanelComponents().setTableCellEditor4DomainsInComponents(domainVector);
	}
	
	/**
	 * Returns an ImageIcon, or a default MissingIcon(a red X) if image not found.
	 *
	 * @param path the path
	 * @param description the description
	 * @return ImageIcon
	 */
	public static ImageIcon createImageIcon(String path, String description) {
		if (path!=null ){		
			if (path.equals("MissingIcon")) {
				return new MissingIcon(description);
			} else {
			    ImageIcon imageIcon = GraphGlobals.getImageIcon(path);
				if (imageIcon!=null) {
					return imageIcon;
				} else {
					System.err.println("Couldn't find file: " + path);
			        return new MissingIcon(description);
				}
			}
		} else {
		    return new MissingIcon(description);		    
		}
	}
	
	/**
	 * Rename domain in components.
	 *
	 * @param oldDomainName the old domain name
	 * @param newDomainName the new domain name
	 */
	public void renameDomainInComponents(String oldDomainName, String newDomainName) {
		this.getJPanelComponents().renameDomainInComponents(oldDomainName, newDomainName);
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJButtonOk()) {

			// --- Check for errors in the DomainSettings -----------
			ComponentTypeError ctError = this.getJPanelDomains().hasDomainSettingError();
			if (ctError!=null) {
				JOptionPane.showMessageDialog(this, ctError.getMessage(), ctError.getTitle(), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// --- Check for errors in the DomainSettings -----------
			ctError = this.getJPanelComponents().hasComponentTypeSettingError();
			if (ctError!=null) {
				JOptionPane.showMessageDialog(this, ctError.getMessage(), ctError.getTitle(), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			this.currSnap2Grid = this.jCheckBoxSnap2Grid.isSelected();
			this.currSnapRaster = (Double)this.jSpnnerGridWidth.getValue(); 
			this.currEdgeShape = (EdgeShape) this.jComboBoxEdgeShapes.getSelectedItem();
			this.currComponentSorting = (ComponentSorting) this.getJComboBoxListSorting().getSelectedItem();
			
			this.canceled = false;
			this.setVisible(false);
			
		} else if(ae.getSource()==this.getJButtonCancel()) {
			// --- Canceled, discard changes --------------
			this.canceled = true;
			this.setVisible(false);
			
		}
	}


} 
