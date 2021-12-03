package main;

import game.Game;


public class Main
{
	public static void main(String[] args) {
		Game g = new Game();
		System.out.println(System.getProperty("user.dir"));
		g.play(); // on lance le jeu avec cette commmande
		
	}
}
