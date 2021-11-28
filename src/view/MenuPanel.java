package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

import controller.Controller;

public class MenuPanel extends JPanel implements MouseListener{

	private View view;
	
	
	public MenuPanel(View view) {
		super();
		this.setView(view);
		this.setVisible(true);
		addMouseListener(this);
		this.setFocusable(true);
		this.build();
	}
	
	private void build() { // build le menu
		
		JButton Start = new JButton("PLAY");
		Start.setName("PARAM");
		
		JButton Rules = new JButton("RULES");
		Rules.setName("RULES");
		
		JButton Exit = new JButton("EXIT");
		Exit.setName("EXIT");
		
		this.add(Start);
		this.add(Rules);
		this.add(Exit);
		
		Start.addActionListener(this.getController());
		Rules.addActionListener(this.getController());
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
