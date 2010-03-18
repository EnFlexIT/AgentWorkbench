package mas.display;


import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.batik.dom.svg.SVGDOMImplementation;

import application.Application;
import application.Language;
import application.Project;

/**
 * GUI for DisplayAgent
 * @author nils
 *
 */
public class DisplayAgentGUI extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Currrent Agent-Project of AgentGUI
	Project currentProject = null;
	
	// SVG Namespace
	public static final String svgNs=SVGDOMImplementation.SVG_NAMESPACE_URI;
	
	// DisplayAgent controlling this GUI
		
	// Pre-start components
	JButton btnStart = null;
	
	
	/**
	 * This constructor is called when the GUI is created first (embedded mode)
	 */
	public DisplayAgentGUI( Project CP ){
		
		this.currentProject = CP;
		
		final int btnWidth = 150;
		final int btnHeight = 25;
		
		this.setLayout(null);

				
		btnStart = new JButton();
		btnStart.setText(Language.translate("Simulation starten"));
		btnStart.addActionListener(new ActionListener(){
			// Starting a display agent and enabling the GUI

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( Application.JadePlatform.jadeMainContainerIsRunning(true)) {
					// Entferne Label und Button
					DisplayAgentGUI.this.remove(btnStart);
					
					// Starte DisplayAgent
					String agentNameBase = "DA";
					int agentNameSuffix = 1;
					String agentName = agentNameBase+agentNameSuffix;
					
					while( Application.JadePlatform.jadeAgentIsRunning(agentName)){
						agentNameSuffix++;
						agentName = agentNameBase + agentNameSuffix;
					}	
					System.out.println("Agent name "+agentNameBase);	
					
					// Markierungen vor dem Start entfernen
//					currentProject.getEnvironmentController().setSelectedElement(null);
//								
//					Object[] args = new Object[3];
//					args[0] = currentProject.getEnvironmentController().getMainPlayground();
//					args[1] = currentProject.getEnvironmentController().getSvgDoc();
//					args[2] = DisplayAgentGUI.this;
//					Application.JadePlatform.jadeAgentStart(agentName, "mas.display.DisplayAgent", args, currentProject.getProjectFolder() );
//					
//					// Starte in der Umgebung definierte Agenten
//					currentProject.getEnvironmentController().startSimulation();
				}
			}
			
		});
		this.add(btnStart, null);
		this.addComponentListener(new ComponentAdapter(){
			// Fit button position to pane size
			public void componentResized(ComponentEvent ce){
				int btnX = (DisplayAgentGUI.this.getWidth()/2-btnWidth/2);
				int btnY = (DisplayAgentGUI.this.getHeight()/2-btnHeight/2);
				btnStart.setBounds(new Rectangle(btnX, btnY, btnWidth, btnHeight));
				
			}
		
			
		});
		this.setVisible(true);
	}
			
}
