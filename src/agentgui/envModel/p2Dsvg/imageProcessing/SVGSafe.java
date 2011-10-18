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
package agentgui.envModel.p2Dsvg.imageProcessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;

/**
 * This class can save a document in the SVG file and convert a SVG file into a JPG.
 * @author Tim Lewen - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class SVGSafe {
    
    /** The hints. */
    protected TranscodingHints hints = new TranscodingHints();
    
	/**
	 * Write.
	 *
	 * @param svgFile The Filename
	 * @param svgDoc  The document
	 * @throws Exception the exception
	 */
	public synchronized void write(String svgFile,Document svgDoc) throws Exception
	{
	FileWriter fw = new FileWriter(svgFile);
	PrintWriter writer = new PrintWriter(fw);
	writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	writer.write("<!DOCTYPE svg PUBLIC '");
	writer.write(SVGConstants.SVG_PUBLIC_ID);
	writer.write("' '");
	writer.write(SVGConstants.SVG_SYSTEM_ID);
	writer.write("'>\n\n");
	SVGTranscoder t = new SVGTranscoder();
	t.transcode(new TranscoderInput(svgDoc), new TranscoderOutput(writer));
	writer.close();
	fw.close();
	}
	
	/**
	 * Write jpg.
	 *
	 * @param filename The filename  of the SVG File
	 * @return Returns the filename of JPG File
	 * @throws Exception the exception
	 */
	@SuppressWarnings("rawtypes")
	public synchronized String writeJPG(String filename) throws Exception
	{
		
		   JPEGTranscoder transcoder = new JPEGTranscoder();
		   TranscoderInput input = new TranscoderInput(new FileInputStream(new File(filename)));
		   String newFileName=filename.replace("svg", "jpg");
		   OutputStream ostream = new FileOutputStream(newFileName);
		   TranscoderOutput output = new TranscoderOutput(ostream);
		   hints.put(JPEGTranscoder.KEY_QUALITY, new Float(1.0f));
		   transcoder.setTranscodingHints((Map)hints);
		   if(input==null|| output==null)
		   {
			   System.out.println("Fehler!");
			   
			   System.exit(0);
		   }
	       transcoder.transcode(input, output);
		   ostream.close();
		   
		   return newFileName;		
	}
}