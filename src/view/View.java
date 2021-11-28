package view;

import javax.swing.JFrame;

import controller.Controller;
import model.Model;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class View extends JFrame implements KeyListener  {


	private MenuPanel menuPanel;	// une pour le menu
	private RulePanel rulePanel;	// une pour les règles
	private ParamPanel paramPanel;	// une pour les paramètres de la partie
	private GamePanel gamePanel;	// une pour jouer la partie


	
	private Controller controller;
	private Model model;
	
	public View(String title, int width, int height) {
		super(title);
		this.model = new Model();
		this.controller = new Controller(this);
		setPreferredSize(new Dimension(width, height));
		this.build();
		this.setFocusable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void build() {
		
		setLocationRelativeTo(null);
		setResizable(false);
		this.buildContent();
		pack();
		setVisible(true);
		
	}
	public void buildContent() {
		// 0 sur menu, 1 sur param, 2 sur pour les règles, 3 pour exit et 4 pour le jeux
		if(this.controller.getState()==0) {
			this.menuPanel = new MenuPanel(this);
			this.menuPanel.setFocusable(true);
			
			this.menuPanel.setVisible(true);
			
			if(this.paramPanel != null) {
				this.paramPanel.setVisible(false);
			}
			
			if(this.rulePanel != null) {
				this.rulePanel.setVisible(false);
			}
			if(this.gamePanel != null) {
				this.gamePanel.setVisible(false);
			}
			
			getContentPane().add(menuPanel);
			
		} else if(this.controller.getState() == 1) {
			this.paramPanel = new ParamPanel(this);
			this.paramPanel.setFocusable(true);
			getContentPane().add(paramPanel);
			
			this.paramPanel.setVisible(true);
			
			if(this.menuPanel != null) {
				this.menuPanel.setVisible(false);
			}
			
			if(this.rulePanel != null) {
				this.rulePanel.setVisible(false);
			}
			if(this.gamePanel != null) {
				this.gamePanel.setVisible(false);
			}
			
			
			
			
		} else if(this.controller.getState() == 2) {
			this.rulePanel = new RulePanel(this);
			this.rulePanel.setFocusable(true);
			getContentPane().add(rulePanel);
			
			this.rulePanel.setVisible(true);
			
			if(this.menuPanel != null) {
				this.menuPanel.setVisible(false);
			}
			
			if(this.paramPanel != null) {
				this.paramPanel.setVisible(false);
			}
			if(this.gamePanel != null) {
				this.gamePanel.setVisible(false);
			}
			
			
			
		} else if(this.controller.getState() == 4) {
			this.gamePanel = new GamePanel(this);
			this.gamePanel.setFocusable(true);
			getContentPane().add(gamePanel);
			
			this.gamePanel.setVisible(true);
			
			if(this.menuPanel != null) {
				this.menuPanel.setVisible(false);
			}
			
			if(this.paramPanel != null) {
				this.paramPanel.setVisible(false);
			}
			if(this.rulePanel != null) {
				this.rulePanel.setVisible(false);
			}
			
			
			
		}
	}
	
	
	
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public Controller getController() {
		return this.controller;
	}
	public Model getModel() {
		return this.model;
	}
	
	public void setBrave(boolean b) {
		this.model.setBrave(b);
	}
	public void setVariante(int v) {
		this.model.setVariante(v);
	}

}
