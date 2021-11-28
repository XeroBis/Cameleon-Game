package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.Model;
import view.View;

public class Controller implements ActionListener{
	
	private Model model;
	private View view;
	private int state; // 0 sur menu, 1 sur param, 2 sur pour les règles, 3 pour exit et 4 pour le jeux
	
	public Controller(Model model, View view){
		super();
		this.model = model;
		this.view = view;
		this.setState(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String o = ((JButton)e.getSource()).getName();
		System.out.print(o);
		
		if(o == "MENU") {
			this.setState(0);
			this.view.build();
		} else if (o == "PARAM") {
			this.setState(1);
			this.view.build();
		} else if (o == "RULES") {
			this.setState(2);
			this.view.build();
		} else if (o == "EXIT"){
			System.exit(0);
		} else if(o == "PLAY") {
			this.setState(4);
			this.view.build();
		}
		System.out.println("STATE : " + this.getState());
		// TODO Auto-generated method stub
		
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
}
