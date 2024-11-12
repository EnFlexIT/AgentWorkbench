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
package agentgui.simulationService.time;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.event.ChangeListener;

import de.enflexit.language.Language;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;

/**
 * The Class TimeModelPresentConfiguration is the JPanel used for configuration of the TimeModelPresent.
 */
public class TimeModelPresentConfiguration extends TimeModelContinuousConfiguration implements ChangeListener {

    private static final long serialVersionUID = -7897380427330081422L;

    
    /**
     * Instantiates a new time model present configuration.
     *
     * @param project the project
     * @param timeModelController the current {@link TimeModelController}
     */
    public TimeModelPresentConfiguration(Project project, TimeModelController timeModelController) {
        super(project, timeModelController);
    }
    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelContinuousConfiguration#initialize()
     */
    @Override
    protected void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		this.setLayout(gridBagLayout);
		this.setSize(728, 400);

		GridBagConstraints gbcHeader1 = new GridBagConstraints();
		gbcHeader1.gridx = 0;
		gbcHeader1.gridy = 0;
		gbcHeader1.gridwidth = 2;
		gbcHeader1.anchor = GridBagConstraints.WEST;
		gbcHeader1.insets = new Insets(10, 10, 5, 0);

		GridBagConstraints gbcHeader2 = new GridBagConstraints();
		gbcHeader2.gridx = 0;
		gbcHeader2.gridy = 1;
		gbcHeader2.gridwidth = 2;
		gbcHeader2.insets = new Insets(0, 10, 0, 0);
		gbcHeader2.anchor = GridBagConstraints.WEST;
		gbcHeader2.weightx = 0.0;
		gbcHeader2.fill = GridBagConstraints.NONE;

		GridBagConstraints gbcLableDateFormat = new GridBagConstraints();
		gbcLableDateFormat.gridx = 0;
		gbcLableDateFormat.gridy = 2;
		gbcLableDateFormat.insets = new Insets(15, 10, 0, 0);
		gbcLableDateFormat.anchor = GridBagConstraints.NORTHWEST;

		GridBagConstraints gbcTimeFormarter = new GridBagConstraints();
		gbcTimeFormarter.gridx = 1;
		gbcTimeFormarter.gridy = 2;
		gbcTimeFormarter.anchor = GridBagConstraints.WEST;
		gbcTimeFormarter.insets = new Insets(10, 5, 0, 0);

		JLabel jLabelHeader1 = new JLabel();
		jLabelHeader1.setText("TimeModelPresent");
		jLabelHeader1.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JLabel jLabelHeader2 = new JLabel();
		jLabelHeader2.setText("Gegenwarts-Zeit.");
		jLabelHeader2.setText(Language.translate(jLabelHeader2.getText()));

		JLabel jLabelDateFormat = new JLabel();
		jLabelDateFormat.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelDateFormat.setText("Ansicht");
		jLabelDateFormat.setText(Language.translate(jLabelDateFormat.getText()) + ":");

		this.add(jLabelHeader1, gbcHeader1);
		this.add(jLabelHeader2, gbcHeader2);
		this.add(jLabelDateFormat, gbcLableDateFormat);
		this.add(getJPanelTimeFormater(), gbcTimeFormarter);
    }

    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelContinuousConfiguration#setTimeModel(agentgui.simulationService.time.TimeModel)
     */
    @Override
    public void setTimeModel(TimeModel timeModel) {
        TimeModelPresent timeModelPresent = null;
        if (timeModel == null) {
            timeModelPresent = new TimeModelPresent();
        } else {
            timeModelPresent = (TimeModelPresent) timeModel;
        }

        // --- Settings for the time format -------------------------
        this.enabledChangeListener = false;
        this.getJPanelTimeFormater().setTimeFormat(timeModelPresent.getTimeFormat());
        this.enabledChangeListener = true;
    }

    /* (non-Javadoc)
     * @see agentgui.simulationService.time.TimeModelContinuousConfiguration#getTimeModel()
     */
    @Override
    public TimeModel getTimeModel() {
        // --- Getting the time format ------------------------------
        String timeFormat = this.getJPanelTimeFormater().getTimeFormat();

        // --- Set TimeModel ----------------------------------------
        TimeModelPresent tmp = new TimeModelPresent();
        tmp.setTimeFormat(timeFormat);
        return tmp;
    }

//    /* (non-Javadoc)
//     * @see agentgui.simulationService.time.TimeModelContinuousConfiguration#getJPanelWidthSettings()
//     */
//    @Override
//    protected JPanel getJPanelWidthSettings() {
//        if (jPanelWidthSettings == null) {
//            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
//            gridBagConstraints12.gridx = 2;
//            gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
//            gridBagConstraints12.weightx = 1.0;
//            gridBagConstraints12.insets = new Insets(0, 5, 0, 0);
//            gridBagConstraints12.gridy = 0;
//
//            JLabel jLabelDummy = new JLabel();
//            jLabelDummy.setText(" ");
//
//            jPanelWidthSettings = new JPanel();
//            jPanelWidthSettings.setLayout(new GridBagLayout());
//            jPanelWidthSettings.add(jLabelDummy, gridBagConstraints12);
//        }
//        return jPanelWidthSettings;
//    }
}
