/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.core.gui.imaging;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;

/**
 * The "missing icon" is a white box with a black border and a red x.
 * It's used to display something when there are issues loading an
 * icon from an external location.
 *
 * @author Satyadeep - CSE - Indian Institute of Technology, Guwahati
 */
public class MissingIcon extends ImageIcon{
    
	private static final long serialVersionUID = 7912176274161834855L;

	private int width = 16;
    private int height = 16;
    
    private BasicStroke stroke = new BasicStroke(4);
    
    public MissingIcon() {

	}
    /**
     * Constructor for setting the description of the ImageIcon
     * @param description
     */
    public MissingIcon(String description){
    	setDescription(description);
    }
    
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x +1 ,y + 1,width -2 ,height -2);
        
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x +1 ,y + 1,width -2 ,height -2);
        
        g2d.setColor(Color.RED);
        
        g2d.setStroke(stroke);
        g2d.drawLine(x +10, y + 10, x + width -10, y + height -10);
        g2d.drawLine(x +10, y + height -10, x + width -10, y + 10);
        
        g2d.dispose();
    }
    
    public int getIconWidth() {
        return width;
    }
    
    public int getIconHeight() {
        return height;
    }
}