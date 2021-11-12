package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import controller.Controller;
import model.Model;
import view.View;

import game.Game;

public class Main
{
	public static Scanner scan = new Scanner(System.in);
	public static Model test = new Model();
	
	public static void main(String[] args) {
		Game g = new Game(1);
		g.playJvJ();
	}
	

	/*
	public static void readTextFile(String filename) throws IOException
	{
		String dir = System.getProperty("user.dir");
		
		File file = new File(dir + "\\" + filename);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String str;
		str = reader.readLine();
		
		
		int a = Integer.parseInt(str);
		int b = 0;
		while (a != 3)
		{
			a = a / 2;
			b++;
		}
		
		test.setNewK(b);
		int ligne = 0;
		
		while ((str = reader.readLine()) != null)
		{
			for(int col = 0; col < str.length(); col++)
			{
				switch(str.charAt(col))
				{
				case 'R':
					test.coloration(ligne, col, 2);
				break;
				case 'B':
					test.coloration(ligne,  col,  1);
				break;
				default:
				break;
				}
			}
		ligne++;
		}
	}

	
	
	public static void main (String args[]) throws IOException
	{
		boolean choose = true;
		boolean preload = false;
		
		while(choose)
		{
			System.out.print("Voulez vous charger un plateau (y/n) : ");
			String resp = scan.next();
			System.out.println();
			
			switch (resp)
			{
			case "y" :
				preload = true;
				choose = false;
			break;
			case "n" :
				preload = false;
				choose = false;
			break;
			default :
				System.out.println("Reponse non valide, seuls 'y' et 'n' sont valides");
			break;
			}
		}
		
		if (preload)
		{
			System.out.print("entrez un nom de fichier : ");
			String name = scan.next();
			
			System.out.println(System.getProperty("user.dir"));
			readTextFile(name);
		}
		else
		{
			System.out.print("entrez la taille du plateau k (dans la formule 3 * 2 ^ k) : ");
			String k = scan.next();
			
			test.setNewK(Integer.parseInt(k));
		}
		
		int a = 1;
		
		while(!test.estTerminee())
		{
			test.afficherScores();
			playerTurnV2(a);
			if(a==1) {a=2;}
			else {a=1;}
		}
	}*/
}
