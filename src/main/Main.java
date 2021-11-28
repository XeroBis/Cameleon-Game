package main;

import game.Game;
import view.View;

import javax.swing.SwingUtilities;

public class Main
{
	public static void main(String[] args) {
		//Game g = new Game();
		//g.play(true); // on lance le jeu avec cette commmande
		//System.out.print(System.getProperty("user.dir"));
		
		
		
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	new View("JEU", 1000, 1000);
            }
        });
		
		/*
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.setVisible(true);
            }
        });
        */
		
		
	}
	/*
	public static void createAndShowGUI() throws Exception {
        new View("JEU", 1000, 1000);
    }
    */
}
