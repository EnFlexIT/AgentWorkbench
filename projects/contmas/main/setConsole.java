/**
 * @author Hanno - Felix Wagner, 21.03.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

class setConsole{
	public setConsole(final JTextArea textArea){
		OutputStream myOutputStream=new TextAreaOutputStream(textArea);
		System.setOut(new PrintStream(myOutputStream));
		System.setErr(new PrintStream(myOutputStream));
	}

	class TextAreaOutputStream extends OutputStream{
		private JTextArea textArea;

		TextAreaOutputStream(JTextArea textArea){
			super();
			this.textArea=textArea;
		}

		@Override
		public void write(byte[] b,int off,int len) throws IOException{
			this.textArea.append(new String(b,off,len));
		}

		@Override
		public void write(byte[] b) throws IOException{
			this.write(b,0,b.length);
		}

		@Override
		public void write(int b) throws IOException{
			this.write(new byte[] {(byte) b});
		}
	}
}