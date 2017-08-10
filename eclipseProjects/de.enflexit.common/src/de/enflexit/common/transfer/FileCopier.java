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
package de.enflexit.common.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class can be used in order to copy one file from a source to
 * a destination location.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class FileCopier {
	
	/**
	 * This method does the actual copying process
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private void copy( InputStream in, OutputStream out ) throws IOException {
		byte[] buffer = new byte[ 0xFFFF ];     
		for ( int len; (len = in.read(buffer))!=-1; )       
			out.write( buffer, 0, len );
	}
	
	/**
	 * This method allows to copy a file from one location to another one
	 * 
	 * @param srcPath
	 * @param destPath
	 */
	public void copyFile(String srcPath, String destPath) {
		
		File checkFile = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		checkFile = new File(srcPath);
		if (checkFile.exists()==false) {
			return;
		}
		
		try {
			fis = new FileInputStream(srcPath);
			fos = new FileOutputStream(destPath);
			copy(fis, fos);
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
				}
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
				}
		}
	}

}