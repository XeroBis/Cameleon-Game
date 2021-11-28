package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

public class RulePanel extends JPanel implements MouseListener{

	private View view;
	
	public RulePanel(View view) {
		super();
		this.setView(view);
		this.setVisible(true);
		addMouseListener(this);
		this.setFocusable(true);
		this.build();
	}
	
	private void build() { // build le menu

		JTextField Rules = new JTextField("RULES");
		Rules.setName("RULES");
		

		// text pour les règles
		
		
		JButton Menu = new JButton("GO BACK TO MENU");
		Menu.setName("MENU");
		
		JButton Exit = new JButton("EXIT");
		Exit.setName("EXIT");
		
		this.add(Rules);
		this.add(Menu);
		this.add(Exit);
		
		Rules.addActionListener(this.getController());
		Menu.addActionListener(this.getController());
		Exit.addActionListener(this.getController());
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
	public Controller getController() {
		return this.view.getController();
	}

}
