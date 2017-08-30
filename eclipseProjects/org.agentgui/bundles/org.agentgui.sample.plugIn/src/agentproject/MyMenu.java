package agentproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MyMenu extends JMenu {

	private static final long serialVersionUID = -4923829351652611906L;

	private JMenuItem jMenuItemDoThisFirst = null; 
	private JMenuItem jMenuItemDoThisSecond = null;
	private JMenuItem jMenuItemDoThisThird =  null;
	
	public MyMenu(String name) {
		super(name);
		this.initialize();
	}

	private void initialize() {
		
		this.add(this.getjMenuItemDoThisFirst());
		this.add(this.getjMenuItemDoThisSecond());
		this.addSeparator();
		this.add(this.getjMenuItemDoThisThird());
	}
	
	private JMenuItem getjMenuItemDoThisFirst() {
		if (jMenuItemDoThisFirst==null) {
			jMenuItemDoThisFirst = new JMenuItem("First menu item");
			jMenuItemDoThisFirst.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("OK, I do this first");
				}
			});
		}
		return jMenuItemDoThisFirst;
	}
	
	private JMenuItem getjMenuItemDoThisSecond() {
		if (jMenuItemDoThisSecond==null) {
			jMenuItemDoThisSecond = new JMenuItem("Second menu item");
			jMenuItemDoThisSecond.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("OK, I do this as second step");
				}
			});
		}
		return jMenuItemDoThisSecond;
	}
	
	private JMenuItem getjMenuItemDoThisThird() {
		if (jMenuItemDoThisThird==null) {
			jMenuItemDoThisThird = new JMenuItem("Third menu item");
			jMenuItemDoThisThird.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("OK, I do this as third step");
				}
			});
		}
		return jMenuItemDoThisThird;
	}
	
}
