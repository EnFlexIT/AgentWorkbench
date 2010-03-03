/**
 * @author Hanno - Felix Wagner Copyright 2010 Hanno - Felix Wagner This file is
 *         part of ContMAS. ContMAS is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation, either version
 *         3 of the License, or (at your option) any later version. ContMAS is
 *         distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *         License for more details. You should have received a copy of the GNU
 *         Lesser General Public License along with ContMAS. If not, see
 *         <http://www.gnu.org/licenses/>.
 */

package contmas.main;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ContMASContainer extends JFrame{

	private static final long serialVersionUID=1L;
	private JPanel jContentPane=null;
	private JDesktopPane jDesktopPane=null;

	/**
	 * This is the default constructor
	 */
	public ContMASContainer(){
		super();
		this.initialize();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane(){
		if(this.jContentPane == null){
			this.jContentPane=new JPanel();
			this.jContentPane.setLayout(null);
			this.jContentPane.add(this.getJDesktopPane(),null);
		}
		return this.jContentPane;

	}

	/**
	 * This method initializes jDesktopPane
	 * 
	 * @return javax.swing.JDesktopPane
	 */
	public JDesktopPane getJDesktopPane(){
		if(this.jDesktopPane == null){
			this.jDesktopPane=new JDesktopPane();
			this.jDesktopPane.setBounds(new Rectangle(0,0,307,294));
		}
		return this.jDesktopPane;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(){
		this.setSize(315,322);
		this.setFont(new Font("Dialog",Font.PLAIN,12));
		this.setContentPane(this.getJContentPane());
		this.setTitle("ContMAS");
		this.setVisible(true);
	}

} //  @jve:decl-index=0:visual-constraint="30,15"
