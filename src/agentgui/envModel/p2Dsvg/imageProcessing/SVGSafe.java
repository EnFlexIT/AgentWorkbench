/**
 * The class can safe SVG Documents in the JPG or SVG format.
 * 
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

public class SVGSafe {
    protected TranscodingHints hints = new TranscodingHints();
    
	/**
	 * @param svgFile The Filename
	 * @param svgDoc  The document
	 * @throws Exception
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
	 * @param filename The filename  of the SVG File
	 * @return Returns the filename of JPG File
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public synchronized String writeJPG(String filename) throws Exception
	{
		   System.out.println("Write JPG is called!");
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