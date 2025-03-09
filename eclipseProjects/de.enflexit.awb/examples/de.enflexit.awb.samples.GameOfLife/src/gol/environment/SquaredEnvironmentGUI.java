package gol.environment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import de.enflexit.awb.core.environment.EnvironmentPanel;
import de.enflexit.awb.core.project.DistributionSetup;
import de.enflexit.common.Observable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;


public class SquaredEnvironmentGUI extends EnvironmentPanel implements ActionListener {

	private static final long serialVersionUID = 5898167188370830883L;
	
	private int currGeneration = 0;
	
	private SquaredEnvironmentPlayGround currentPlayGround = null;
	
	private JLabel jLabelHeaderColumn = null;
	private JTextField jTextFieldNumberColumns = null;
	private JLabel jLabelHeaderRow = null;
	private JTextField jTextFieldNumberRows = null;
	private JButton jButtonApply = null;

	private JLabel jLabelNoOfAgents = null;
	private JTextField jTextFieldNumberAgents = null;
	private JLabel jLabelNumberofContainer = null;
	private JTextField jTextFieldNumberContainer = null;
	private JButton jButtonApplyAgents = null;

	private JButton jButtonClear = null;
	
	private JPanel jPanelField = null;
	private JLabel jLabelGenerations = null;

	/**
	 * Instantiates a new squared gol.environment gui.
	 * @param squaredEnvironmentController the squared gol.environment controller
	 */
	public SquaredEnvironmentGUI(SquaredEnvironmentController envController) {
		super(envController);
		initialize();
	}
	
	/**
	 * Returns the graph gol.environment controller.
	 * @return the graph gol.environment controller
	 */
	public SquaredEnvironmentController getSquaredController() {
		return (SquaredEnvironmentController) this.environmentController;
	}
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
        GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
        gridBagConstraints32.gridx = 0;
        gridBagConstraints32.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints32.insets = new Insets(5, 10, 0, 10);
        gridBagConstraints32.gridy = 7;
        GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
        gridBagConstraints22.fill = GridBagConstraints.BOTH;
        gridBagConstraints22.gridy = 8;
        gridBagConstraints22.weightx = 0.0;
        gridBagConstraints22.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints22.gridx = 0;
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        gridBagConstraints13.gridx = 0;
        gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints13.insets = new Insets(20, 10, 0, 10);
        gridBagConstraints13.anchor = GridBagConstraints.NORTH;
        gridBagConstraints13.gridy = 11;
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.gridx = 0;
        gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints12.anchor = GridBagConstraints.NORTH;
        gridBagConstraints12.insets = new Insets(30, 10, 0, 10);
        gridBagConstraints12.gridy = 10;
        GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
        gridBagConstraints31.gridx = 0;
        gridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints31.anchor = GridBagConstraints.NORTH;
        gridBagConstraints31.insets = new Insets(5, 10, 0, 10);
        gridBagConstraints31.gridy = 9;
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints21.gridy = 6;
        gridBagConstraints21.weightx = 0.0;
        gridBagConstraints21.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints21.gridx = 0;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.insets = new Insets(20, 10, 0, 10);
        gridBagConstraints11.anchor = GridBagConstraints.WEST;
        gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints11.gridy = 5;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.fill = GridBagConstraints.BOTH;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.weighty = 1.0;
        gridBagConstraints5.gridheight = 12;
        gridBagConstraints5.gridy = 0;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.insets = new Insets(5, 10, 0, 10);
        gridBagConstraints4.gridy = 4;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.weightx = 0.0;
        gridBagConstraints3.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints3.gridx = 0;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.anchor = GridBagConstraints.WEST;
        gridBagConstraints2.insets = new Insets(5, 10, 0, 10);
        gridBagConstraints2.gridy = 2;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.weightx = 0.0;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 0, 10);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        
        jLabelHeaderColumn = new JLabel();
        jLabelHeaderColumn.setText("Number of columns");
        jLabelHeaderColumn.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelHeaderColumn.setFont(new Font("Dialog", Font.BOLD, 12));
        jLabelHeaderRow = new JLabel();
        jLabelHeaderRow.setText("Number of rows");
        jLabelHeaderRow.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelHeaderRow.setFont(new Font("Dialog", Font.BOLD, 12));
        jLabelNoOfAgents = new JLabel();
        jLabelNoOfAgents.setText("Number of gol.SimService.agents");
        jLabelNoOfAgents.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelNoOfAgents.setFont(new Font("Dialog", Font.BOLD, 12));
        jLabelNumberofContainer = new JLabel();
        jLabelNumberofContainer.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelNumberofContainer.setText("Number of Container");
        jLabelNumberofContainer.setFont(new Font("Dialog", Font.BOLD, 12));
        jLabelGenerations = new JLabel();
        jLabelGenerations.setText("Gen.:");
        jLabelGenerations.setFont(new Font("Dialog", Font.BOLD, 12));
        
    	this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(658, 414));
        this.add(jLabelHeaderColumn, gridBagConstraints);
        this.add(getJTextFieldNumberColumns(), gridBagConstraints1);
        this.add(jLabelHeaderRow, gridBagConstraints2);
        this.add(getJTextFieldNumberRows(), gridBagConstraints3);
        this.add(getJButtonApply(), gridBagConstraints4);
        this.add(getJPanelField(), gridBagConstraints5);
        this.add(jLabelNoOfAgents, gridBagConstraints11);
        this.add(getJTextFieldNumberAgents(), gridBagConstraints21);
        this.add(getJButtonApplyAgents(), gridBagConstraints31);
        this.add(getJButtonClear(), gridBagConstraints12);
        this.add(jLabelGenerations, gridBagConstraints13);
        this.add(getJTextFieldNumberContainer(), gridBagConstraints22);
        this.add(jLabelNumberofContainer, gridBagConstraints32);
        
        
        if (this.getSquaredController().getProject()==null) {
        	this.getJTextFieldNumberColumns().setEditable(false);
        	this.getJTextFieldNumberRows().setEditable(false);
        	this.getJTextFieldNumberAgents().setEditable(false);
        	this.getJTextFieldNumberContainer().setEditable(false);
        	this.getJButtonApply().setEnabled(false);
        	this.getJButtonApplyAgents().setEnabled(false);
        	
        } else {
        	Integer noContainer = this.getSquaredController().getProject().getDistributionSetup().getNumberOfContainer();
        	this.getJTextFieldNumberContainer().setText(noContainer.toString());
        }
			
        this.displayCurrentEnvironmentModel();
        
        
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentPanel#dispose()
	 */
	@Override
	public void dispose() {
	}

	/**
	 * This method initializes jTextFieldNumberColumns	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNumberColumns() {
		if (jTextFieldNumberColumns == null) {
			jTextFieldNumberColumns = new JTextField();
			jTextFieldNumberColumns.setPreferredSize(new Dimension(10, 26));
			jTextFieldNumberColumns.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String SngChar = Character.toString(charackter);
					// --- Numbers only !!! ------
					if ( SngChar.matches( "[0-9]" ) == false ) {
						kT.consume();	
						return;
					}
				 }				 
			});
		}
		return jTextFieldNumberColumns;
	}
	/**
	 * This method initializes jTextFieldNumberRows	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNumberRows() {
		if (jTextFieldNumberRows == null) {
			jTextFieldNumberRows = new JTextField();
			jTextFieldNumberRows.setPreferredSize(new Dimension(10, 26));
			jTextFieldNumberRows.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String SngChar = Character.toString(charackter);
					// --- Numbers only !!! ------
					if ( SngChar.matches( "[0-9]" ) == false ) {
						kT.consume();	
						return;
					}
				 }				 
			});
		}
		return jTextFieldNumberRows;
	}
	/**
	 * This method initializes jButtonApply	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton();
			jButtonApply.setText("Apply");
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(new Color(0, 153, 0));
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	/**
	 * This method initializes jPanelField	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelField() {
		if (jPanelField == null) {
			jPanelField = new JPanel();
			jPanelField.setLayout(new BorderLayout());
			jPanelField.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return jPanelField;
	}
	/**
	 * This method initializes jTextFieldNumberAgents	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNumberAgents() {
		if (jTextFieldNumberAgents == null) {
			jTextFieldNumberAgents = new JTextField();
			jTextFieldNumberAgents.setPreferredSize(new Dimension(10, 26));
			jTextFieldNumberAgents.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String SngChar = Character.toString(charackter);
					// --- Numbers only !!! ------
					if ( SngChar.matches( "[0-9]" ) == false ) {
						kT.consume();	
						return;
					}
				 }				 
			});
		}
		return jTextFieldNumberAgents;
	}
	/**
	 * This method initializes jTextFieldNUmberContainer	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNumberContainer() {
		if (jTextFieldNumberContainer == null) {
			jTextFieldNumberContainer = new JTextField();
			jTextFieldNumberContainer.setPreferredSize(new Dimension(10, 26));
			jTextFieldNumberContainer.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String SngChar = Character.toString(charackter);
					// --- Numbers only !!! ------
					if ( SngChar.matches( "[0-9]" ) == false ) {
						kT.consume();	
						return;
					}
				 }				 
			});
		}
		return jTextFieldNumberContainer;
	}
	/**
	 * This method initializes jButtonApplyAgents	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonApplyAgents() {
		if (jButtonApplyAgents == null) {
			jButtonApplyAgents = new JButton();
			jButtonApplyAgents.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApplyAgents.setText("Apply");
			jButtonApplyAgents.setForeground(new Color(0, 153, 0));
			jButtonApplyAgents.addActionListener(this);
		}
		return jButtonApplyAgents;
	}
	
	/**
	 * This method initializes jButtonClear	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonClear() {
		if (jButtonClear == null) {
			jButtonClear = new JButton();
			jButtonClear.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonClear.setText("Clear");
			jButtonClear.setForeground(new Color(0, 0, 153));
			jButtonClear.addActionListener(this);
		}
		return jButtonClear;
	}
	
	/**
	 * Sets the generation.
	 * @param generation the new generation
	 */
	public void setGeneration(int generation) {
		this.currGeneration = generation;
		this.jLabelGenerations.setText("Gen.: " + this.currGeneration);
	}
	/**
	 * Gets the generation.
	 * @return the generation
	 */
	public int getGeneration() {
		return this.currGeneration;
	}
	
	/**
	 * Gets the play ground.
	 * @return the play ground
	 */
	private void createPlayGround(GameOfLifeDataModel golModel) {
		
		if (golModel.getNumberOfAgents()==0) {
			this.getJPanelField().removeAll();
			this.currentPlayGround = null;
			
		} else {
			SquaredEnvironmentPlayGround playground = new SquaredEnvironmentPlayGround(golModel);
			this.getJPanelField().removeAll();
			this.getJPanelField().add(playground, BorderLayout.CENTER);
			this.currentPlayGround = playground;
			
		}
		this.getJPanelField().validate();
		this.getJPanelField().repaint();
		
	}
	
	
	/**
	 * Gets the gol.environment model from controller.
	 * @return the gol.environment model from controller
	 */
	private GameOfLifeDataModel getDataModelFromController() {
		GameOfLifeDataModel sqEnvModel = (GameOfLifeDataModel) this.environmentController.getDisplayEnvironmentModel();
		return sqEnvModel;
	}
	
	public GameOfLifeDataModel getDataModelFromGUI() {
		if (this.currentPlayGround!=null) {
			return this.currentPlayGround.getGolModel();
		} else {
			return null;
		}
	}
	
	/**
	 * Display current gol.environment model.
	 */
	public void displayCurrentEnvironmentModel() {
		
		GameOfLifeDataModel sqEnvModel = this.getDataModelFromController();
		int nColumn = sqEnvModel.getNumberOfColumns();
		int nRows   = sqEnvModel.getNumberOfRows();
		int nAgents = sqEnvModel.getNumberOfAgents();
		
		this.getJTextFieldNumberColumns().setText(String.valueOf(nColumn));
		this.getJTextFieldNumberRows().setText(String.valueOf(nRows));
		this.getJTextFieldNumberAgents().setText(String.valueOf(nAgents));
		
		if (nColumn!=0 && nRows!=0) {
			this.createPlayGround(sqEnvModel);	
		} else {
			this.currentPlayGround = null;
			this.getJPanelField().removeAll();
			this.getJPanelField().validate();
			this.getJPanelField().repaint();
		}
		
		if (this.currentPlayGround!=null) {
			this.currentPlayGround.setGolModel(sqEnvModel);
		}
		
	}
	
	/**
	 * Update current gol.environment model.
	 */
	public void updateCurrentEnvironmentModel() {
		if (this.currentPlayGround!=null) {
			GameOfLifeDataModel sqEnvModel = this.getDataModelFromController();
			this.currentPlayGround.setGolModel(sqEnvModel);
		}		
	}
	
	/**
	 * Save current gol.environment model.
	 */
	public void saveCurrentEnvironmentModel() {
		GameOfLifeDataModel golDataModel = this.getDataModelFromGUI();
		if (golDataModel==null) {
			this.environmentController.setEnvironmentModel(null);
		} else {
			this.environmentController.setDisplayEnvironmentModel(this.getDataModelFromGUI());	
		}
		// --- Set the number of container --------------
		Integer noContainer = this.getSquaredController().getProject().getDistributionSetup().getNumberOfContainer();
    	this.getJTextFieldNumberContainer().setText(noContainer.toString());
	}
	
	/**
	 * Checks if is edited.
	 * @return true, if is edited
	 */
	public boolean isInEditing() {
		if (this.currentPlayGround!=null) {
			return this.currentPlayGround.isInEditing();	
		} else {
			return false;
		}
	}
	/**
	 * Sets the edited flag.
	 * @param edited the new edited
	 */
	public void setInEditing(boolean inEditing) {
		if (this.currentPlayGround!=null) {
			this.currentPlayGround.setInEditing(inEditing);	
		} 
	}
	
	/**
	 * Checks if is edited.
	 * @return true, if is edited
	 */
	public boolean wasEdited() {
		if (this.currentPlayGround!=null) {
			return this.currentPlayGround.wasEdited();	
		} else {
			return false;
		}
	}
	/**
	 * Sets the edited.
	 * @param edited the new edited
	 */
	public void setEdited(boolean edited) {
		if (this.currentPlayGround!=null) {
			this.currentPlayGround.setEdited(edited);	
		} 
	}
	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentPanel#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object arg) {
	
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		GameOfLifeDataModel sqEnvModel = this.getDataModelFromController();
		
		Object trigger = ae.getSource();
		if (trigger==jButtonApply) {
			// --- Build the playground -------------------
			int nColumn = Integer.parseInt(this.getJTextFieldNumberColumns().getText());
			int nRows   = Integer.parseInt(this.getJTextFieldNumberRows().getText());
			int nAgents = nColumn*nRows;
			
			sqEnvModel.setNumberOfColumns(nColumn);
			sqEnvModel.setNumberOfRows(nRows);
			sqEnvModel.setNumberOfAgents(nAgents);		
			sqEnvModel.setGolHash(new HashMap<String, Integer>());
			
			this.createPlayGround(sqEnvModel);
			this.getJTextFieldNumberAgents().setText(String.valueOf(nAgents));
			
			
		} else if (trigger==jButtonApplyAgents) {
			// --- Build the playground -------------------
			int nAgents = Integer.parseInt(this.getJTextFieldNumberAgents().getText());
			int stretch_factor = (int) Math.round( Math.sqrt(nAgents/2) );
			int nColumn = stretch_factor * 2;
			int nRows   = stretch_factor * 1;
			nAgents 	= nColumn * nRows;
			
			if (nAgents!=sqEnvModel.getNumberOfAgents() && nColumn!=sqEnvModel.getNumberOfColumns() ) {
				
				sqEnvModel.setNumberOfColumns(nColumn);
				sqEnvModel.setNumberOfRows(nRows);
				sqEnvModel.setNumberOfAgents(nAgents);		
				sqEnvModel.setGolHash(new HashMap<String, Integer>());
				
				this.createPlayGround(sqEnvModel);
				
				this.getJTextFieldNumberColumns().setText(String.valueOf(nColumn));
				this.getJTextFieldNumberRows().setText(String.valueOf(nRows));
				this.getJTextFieldNumberAgents().setText(String.valueOf(nAgents));
			}
			
			if (this.getSquaredController().getProject()!=null) {
				Integer noContainer = Integer.valueOf(this.getJTextFieldNumberContainer().getText());
				if (noContainer==null || noContainer==0) {
					noContainer = 1;
					this.getJTextFieldNumberContainer().setText(noContainer.toString());
					
				} else {
					DistributionSetup disSetup = this.getSquaredController().getProject().getDistributionSetup();
					disSetup.setNumberOfContainer(noContainer);
					this.getSquaredController().getProject().setDistributionSetup(disSetup);	
					
				}
			}
			
		} else if (trigger==jButtonClear) {
			if (this.currentPlayGround!=null) {
				this.currentPlayGround.clear();	
			}
			
		}
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
