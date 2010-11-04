package agentgui.core.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopier {
	
	/**
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public void copy( InputStream in, OutputStream out ) throws IOException {
		
		byte[] buffer = new byte[ 0xFFFF ];     
		for ( int len; (len = in.read(buffer))!=-1; )       
			out.write( buffer, 0, len );
		
	}
	
	/**
	 * @param src
	 * @param dest
	 */
	public void copyFile(String src, String dest) {
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			fis = new FileInputStream(src);
			fos = new FileOutputStream(dest);
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