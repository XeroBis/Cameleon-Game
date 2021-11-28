package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

import controller.Controller;

public class ParamPanel extends JPanel implements MouseListener{
	
	private View view;
	
	public ParamPanel(View view) {
		super();
		this.setView(view);
		this.setVisible(true);
		addMouseListener(this);
		this.setFocusable(true);
		this.build();
	}
	
	private void build() { // build le menu
		
		JButton Parametres = new JButton("PLAY");
		Parametres.setName("PLAY");
		
		
		// faire les param de la partie, avec des boutons qui envoie des action au controlleur, le controlleur modifie le model
		
		JButton Menu = new JButton("GO BACK TO MENU");
		Menu.setName("MENU");
		
		
		JButton Exit = new JButton("EXIT");
		Exit.setName("EXIT");
		
		
		
		
		this.add(Parametres);
		this.add(Menu);
		this.add(Exit);
		
		Parametres.addActionListener(this.getController());
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

