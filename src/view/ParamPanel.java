package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;	

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
		this.setLayout(new GridLayout(4, 1));
		JButton Play = new JButton("PLAY");
		Play.setName("PLAY");
		
		
		// faire les param de la partie, avec des boutons qui envoie des action au controlleur, le controlleur modifie le model
		
		JButton Menu = new JButton("GO BACK TO MENU");
		Menu.setName("MENU");
		
		
		// tout les param d'une partie : 
		
		// BRAVE ou TEMERAIRE
		//GAMEMODE : 
		JPanel gamemode = new JPanel();
		gamemode.setLayout(new GridLayout(1,2));
		JButton brave = new JButton("BRAVE");
		brave.setName("BRAVE");
		
		JButton temeraire = new JButton("TEMERAIRE");
		temeraire.setName("TEMERAIRE");
		
		boolean game = this.view.getModel().isBrave();
		System.out.println("game : " + game);
		if(game) {
			brave.setBackground(Color.GREEN);
		} else {
			temeraire.setBackground(Color.GREEN);
		}
		
		ButtonGroup gm = new ButtonGroup();
		gm.add(brave);
		gm.add(temeraire);
		
		JPanel gmPanel = new JPanel();
		gmPanel.add(brave);
		gmPanel.add(temeraire);
		gamemode.add(gmPanel);
		// FIN GAMEMODE
		
		// VARIANTE DU JEU :
		// JvJ / JvIA / IAvIA
		
		JPanel variant = new JPanel();
		variant.setLayout(new GridLayout(1,3));
		JButton JvJ = new JButton("JvJ");
		JvJ.setName("JvJ");
		JvJ.setBackground(Color.GREEN);
		JButton JvIA = new JButton("JvIA");
		JvIA.setName("JvIA");
		JButton IAvIA = new JButton("IAvIA");
		IAvIA.setName("IAvIA");
		int variante = this.view.getModel().getVariante();
		if(variante == 0) {
			JvJ.setBackground(Color.GREEN);
			JvIA.setBackground(Color.RED);
			IAvIA.setBackground(Color.RED);
		}else if (variante == 1) {
			JvJ.setBackground(Color.RED);
			JvIA.setBackground(Color.GREEN);
			IAvIA.setBackground(Color.RED);
		} else if (variante == 2) {
			JvJ.setBackground(Color.RED);
			JvIA.setBackground(Color.RED);
			IAvIA.setBackground(Color.GREEN);
		}
		System.out.println("variante : "+ variante);
		
		ButtonGroup vr = new ButtonGroup();
		vr.add(JvJ);
		vr.add(JvIA);
		vr.add(IAvIA);
		
		JPanel vrPanel = new JPanel();
		vrPanel.add(JvJ);
		vrPanel.add(JvIA);
		vrPanel.add(IAvIA);
		
		variant.add(vrPanel);
		// si dans choix au dessus on demande qu'elle ia : 
		// gloutonne ou intelligente (pas de choix si en brave)
		
		
		JButton Exit = new JButton("EXIT");
		Exit.setName("EXIT");
		
		
		
		JPanel action = new JPanel();
		action.add(Menu);
		action.add(Play);
		action.add(Exit);
		
		this.add(action);
		this.add(gamemode);
		this.add(variant);
		
		
		
		//ActionListener
		brave.addActionListener(this.getController());
		temeraire.addActionListener(this.getController());
		JvJ.addActionListener(this.getController());
		JvIA.addActionListener(this.getController());
		IAvIA.addActionListener(this.getController());
		Play.addActionListener(this.getController());
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

