package agentgui.core.common;

import java.io.File;

public class PathHandling {

	
	public static String getPathName4LocalFileSystem(String zipName) {
		
		String corrected = "";
		for (int i = 0; i < zipName.length(); i++) {
			String	chara = Character.toString(zipName.charAt(i));
			if (chara.equals("\\") || chara.equals("/")) {
				chara = File.separator;
			}
			corrected = corrected + chara;
		}
		return corrected;
	}
	
	
	
	
}
