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

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

import agentgui.envModel.graph.GraphGlobals;

/**
 * The ImageFileView class shows a different icon for each type of image accepted by the image filter.
 * Used by the FileChooser
 * 
 * @author Satyadeep - CSE - Indian Institute of Technology, Guwahati
 */
public class ImageFileView extends FileView {
	
	private final String pathImage = GraphGlobals.getPathImages(); 
	
    private ImageIcon jpgIcon = ImageUtils.createImageIcon(pathImage +  "ImageIconJPG.gif");
    private ImageIcon gifIcon = ImageUtils.createImageIcon(pathImage +  "ImageIconGIF.gif");
    private ImageIcon pngIcon = ImageUtils.createImageIcon(pathImage +  "ImageIconPNG.gif");

    public String getName(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getDescription(File f) {
        return null; //let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getTypeDescription(File f) {
        String extension = ImageUtils.getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals(ImageUtils.jpeg) ||
                extension.equals(ImageUtils.jpg)) {
                type = "JPEG Image";
            } else if (extension.equals(ImageUtils.gif)){
                type = "GIF Image";
            } else if (extension.equals(ImageUtils.png)){
                type = "PNG Image";
            }
        }
        return type;
    }

    public Icon getIcon(File f) {
        String extension = ImageUtils.getExtension(f);
        Icon icon = null;

        if (extension != null) {
            if (extension.equals(ImageUtils.jpeg) ||
                extension.equals(ImageUtils.jpg)) {
                icon = jpgIcon;
            } else if (extension.equals(ImageUtils.gif)) {
                icon = gifIcon;
            } else if (extension.equals(ImageUtils.png)) {
                icon = pngIcon;
            }
        }
        return icon;
    }
}

