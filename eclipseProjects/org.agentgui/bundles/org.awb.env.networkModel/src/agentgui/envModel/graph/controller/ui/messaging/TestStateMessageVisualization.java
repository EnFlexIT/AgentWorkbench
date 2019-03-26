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
package agentgui.envModel.graph.controller.ui.messaging;

import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Insets;

/**
 * The Class TestStateMessageVisualization.
 */
public class TestStateMessageVisualization extends GraphUIStateMessagePanel {
	
	private static final long serialVersionUID = 7465778017804935724L;

	private JLabel lblNewLabel;
	private JTextArea textArea;
	
	/**
	 * Instantiates a new test state message visualization.
	 */
	public TestStateMessageVisualization() {
		this.initilize();
	}
	private void initilize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{46, 0};
		gridBagLayout.rowHeights = new int[]{14, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(getLblNewLabel(), gbc_lblNewLabel);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(5, 0, 0, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		add(getTextArea(), gbc_textArea);
	}
	
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Test for a StateMessagePanel");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return lblNewLabel;
	}
	private JTextArea getTextArea() {
		if (textArea == null) {
			textArea = new JTextArea();
			textArea.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return textArea;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.ui.messaging.AbstractStateMessageVisualization#addMessage(agentgui.envModel.graph.controller.ui.messaging.GraphUIStateMessage)
	 */
	@Override
	public void addMessage(GraphUIStateMessage stateMessage) {
		TestStateMessage tsm = (TestStateMessage) stateMessage;
		this.getTextArea().setText(tsm.getMessage());
	}
}
