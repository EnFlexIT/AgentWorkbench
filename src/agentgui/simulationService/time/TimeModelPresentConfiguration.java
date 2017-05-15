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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import agentgui.core.application.Language;
import agentgui.core.project.Project;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelContinuousConfiguration;

/**
 * The Class TimeModelPresentConfiguration is the JPanel used for configuration of the TimeModelPresent.
 */
public class TimeModelPresentConfiguration extends TimeModelContinuousConfiguration implements ChangeListener {

    private JPanel jPanelWidthSettings = null;

    private static final long serialVersionUID = -7897380427330081422L;

    /**
     * Instantiates a new time model present configuration.
     *
     * @param project the project
     */
    public TimeModelPresentConfiguration(Project project) {
        super(project);
    }

    /**
     * This method initializes this.
     */
    @Override
    protected void initialize() {

        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.gridx = 1;
        gridBagConstraints21.anchor = GridBagConstraints.WEST;
        gridBagConstraints21.insets = new Insets(10, 5, 0, 0);
        gridBagConstraints21.gridy = 6;
        GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
        gridBagConstraints20.gridx = 0;
        gridBagConstraints20.insets = new Insets(15, 10, 0, 0);
        gridBagConstraints20.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints20.gridy = 6;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.insets = new Insets(5, 7, 5, 20);
        gridBagConstraints4.gridwidth = 2;
        gridBagConstraints4.gridy = 7;
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.gridx = 0;
        gridBagConstraints9.anchor = GridBagConstraints.WEST;
        gridBagConstraints9.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints9.gridwidth = 2;
        gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints9.gridy = 9;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.fill = GridBagConstraints.BOTH;
        gridBagConstraints7.weightx = 1.0;
        gridBagConstraints7.weighty = 1.0;
        gridBagConstraints7.gridwidth = 2;
        gridBagConstraints7.gridy = 11;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.insets = new Insets(0, 10, 0, 0);
        gridBagConstraints6.anchor = GridBagConstraints.WEST;
        gridBagConstraints6.gridwidth = 2;
        gridBagConstraints6.weightx = 0.0;
        gridBagConstraints6.fill = GridBagConstraints.NONE;
        gridBagConstraints6.gridy = 1;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.anchor = GridBagConstraints.WEST;
        gridBagConstraints5.insets = new Insets(10, 10, 5, 0);
        gridBagConstraints5.gridwidth = 2;
        gridBagConstraints5.gridy = 0;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;

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

        this.setSize(new Dimension(615, 367));
        this.setLayout(new GridBagLayout());
        this.add(jLabelHeader1, gridBagConstraints5);
        this.add(jLabelHeader2, gridBagConstraints6);
        this.add(getJPanelWidthSettings(), gridBagConstraints9);
        this.add(getJPanelDummy(), gridBagConstraints7);
        this.add(getJPanelDivider(), gridBagConstraints4);
        this.add(jLabelDateFormat, gridBagConstraints20);
        this.add(getJPanelTimeFormater(), gridBagConstraints21);
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

        this.enabledChangeListener = false;

        // --- Settings for the time format -------------------------
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

    /**
     * This method initializes jPanelWidthSettings.
     *
     * @return javax.swing.JPanel
     */
    @Override
    protected JPanel getJPanelWidthSettings() {
        if (jPanelWidthSettings == null) {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 2;
            gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.weightx = 1.0;
            gridBagConstraints12.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints12.gridy = 0;

            JLabel jLabelDummy = new JLabel();
            jLabelDummy.setText(" ");

            jPanelWidthSettings = new JPanel();
            jPanelWidthSettings.setLayout(new GridBagLayout());
            jPanelWidthSettings.add(jLabelDummy, gridBagConstraints12);
        }
        return jPanelWidthSettings;
    }
}
