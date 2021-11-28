package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import view.View;

public class Controller implements ActionListener{

	private View view;
	private int state; // 0 sur menu, 1 sur param, 2 sur pour les règles, 3 pour exit et 4 pour le jeux
	
	public Controller(View view){
		super();
		this.view = view;
		this.setState(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String o = ((JButton)e.getSource()).getName();
		
		if(o == "MENU") {
			this.setState(0);
			this.view.buildContent();
			
		} else if (o == "PARAM") {
			this.setState(1);
			this.view.buildContent();
			
		} else if (o == "RULES") {
			this.setState(2);
			this.view.buildContent();
			
		} else if (o == "EXIT"){
			System.exit(0);
			
		} else if(o == "PLAY") {
			this.setState(4);
			this.view.buildContent();
			
		} else if(o == "JvJ") {
			this.view.setVariante(0);
			this.view.buildContent();
			
		} else if(o == "JvIA") {
			this.view.setVariante(1);
			this.view.buildContent();
			
		} else if(o == "IAvIA") {
			System.out.println("passe par la");
			this.view.setVariante(2);
			this.view.buildContent();
			
		} else if (o == "BRAVE") {
			this.view.setBrave(true);
			this.view.buildContent();
			
		} else if(o == "TEMERAIRE") {
			this.view.setBrave(false);
			this.view.buildContent();
			
		}
		System.out.println(o);
		
		
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
}
