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

import javax.swing.filechooser.FileFilter;

/**
 * ImageFilter implements the accept method so that it accepts all directories 
 * and any file that has a .png, .jpg, .jpeg, .gif, .tif, or .tiff filename extension.
 * 
 * @author Satyadeep - CSE - Indian Institute of Technology, Guwahati
 */
public class ImageFilterAll extends FileFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        
    	if (f.isDirectory()) {
            return true;
        }
        String extension = ImageUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(ImageUtils.gif) ||
                extension.equals(ImageUtils.jpeg) ||
                extension.equals(ImageUtils.jpg) ||
                extension.equals(ImageUtils.png)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Images (*.png, *.jpg, *.jpeg, *.gif)";
    }
}
