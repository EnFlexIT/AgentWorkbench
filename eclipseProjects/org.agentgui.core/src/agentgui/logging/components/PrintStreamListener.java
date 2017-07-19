package agentgui.logging.components;

import java.io.OutputStream;
import java.io.PrintStream;

public class PrintStreamListener extends PrintStream {

	public final static String SystemOutput = "[SysOut]";
	public final static String SystemError = "[SysErr]";
	
	private SysOutScanner sos = null;
	private String prefix2use = null;
	
	
	/**
	 * Constructor for remote outputs
	 * @param outputStream
	 * @param scanner
	 * @param prefix
	 */
	public PrintStreamListener(OutputStream outputStream, SysOutScanner scanner, String prefix) {
		super(outputStream);
		this.sos = scanner;
		this.prefix2use = prefix;
	}
	
	
	
	@Override
	public void write(byte[] buf, int off, int len) {
		// --- Some Error handling ------------------------
		if (buf == null) {
		    throw new NullPointerException();
		} else if ((off < 0) || (off > buf.length) || (len < 0) ||
			   ((off + len) > buf.length) || ((off + len) < 0)) {
		    throw new IndexOutOfBoundsException();
		} else if (len == 0) {
		    return;
		}
		
		byte[] byteArray = new byte[len]; 
		for (int i = 0 ; i < len ; i++) {
			byteArray[i] = buf[off + i];
		}
		// ------------------------------------------------
		// --- Create the String for the console output ---
		String lineOutput = new String(byteArray);
		lineOutput = this.prefix2use + " " + lineOutput;
		final String lineOutputFinal = lineOutput;

		// --- send to Main-Container ---------------------
		if (sos!=null) {
			synchronized (sos) {
				this.sos.append2Stack(lineOutputFinal);
			}
		}

		// ------------------------------------------------
		// --- Do the normal task -------------------------
		super.write(buf, off, len);
	}
	@Override
	public void write(byte[] b) {
		this.write(b, 0, b.length);
	}
	@Override
	public void write(int b) {
		this.write(new byte[] {(byte) b});
	}	
	
}
