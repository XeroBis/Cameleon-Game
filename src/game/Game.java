package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import model.Model;

public class Game {
    private int scoreBleu;
    private int scoreRouge;
    private boolean isBrave;
    private boolean isGloutonne;
    public static char blanc = 'A', bleu = 'B', rouge = 'R';

    private Model model;

    public Game(int n){
        this.scoreBleu = 0;
        this.scoreRouge = 0;
        parametreOfGame(n);
    }

    private void parametreOfGame(int n){
        System.out.println("Le jeu commence !");
        System.out.println("Vous pouvez jouer en version Brave ou Téméraire que choisissez vous? (0/1)");
        Scanner scan = new Scanner(System.in);
        int version = -1;
        version = scan.nextInt();
        while(version != 0 && version != 1) {
            System.out.print("Veuillez entrez la valeur 0 pour la version Brave et 1 pour la version Téméraire.");
            version = scan.nextInt();
        }
        if (version == 0){
            System.out.println("Vous avez donc choisi la version Brave !");
            this.setBrave(true);
            this.setModel(new Model(n, true, true));
        } else {
            System.out.println("Vous avez donc choisi la version Téméraire !");
            this.setBrave(false);
            this.setModel(new Model(n, false, true));
        }
        System.out.println("L'IA peut jouer de deux façon différentes, Gloutonne ou Inteligente, que choisissez vous? (0/1)");
        int versionIA = -1;
        while(versionIA != 0 && versionIA != 1) {
            System.out.print("Veuillez entrez la valeur 0 pour la version Gloutonne et 1 pour la version Intelligente.");
            versionIA = scan.nextInt();
        }
        if (versionIA== 0){
            System.out.println("Vous avez donc choisi la version Gloutonne !");
            this.setGloutonne(true);
        } else {
            System.out.println("Vous avez donc choisi la version Intelligente !");
            this.setGloutonne(false);
        }
        
        //scan.close();
    }

    private void to_string(){
        System.out.println("SCORE :");
        System.out.println("bleu : " + scoreBleu + " , rouge : " + scoreRouge);
        this.model.to_string();
    }

    public void play() {
        int playing = 0;
        Scanner scan = new Scanner(System.in);
        while(playing==0){
            this.to_string();

            System.out.println("JOUEUR 1 : ");
            int[] coord = chooseCoordinate();
            int[] res = model.changeValue(rouge,coord[0],coord[1]);
            while(res[0]!= 0){
                System.out.println("La case que vous avez selectionnée est déjà colorié, veuillez choisir une autre case.");
                coord = chooseCoordinate();
                res = model.changeValue(rouge,coord[0],coord[1]);
            }
            //System.out.println("score en plus : "+res[2]);

            changeScore(res[1], res[2]);

            System.out.println("Tour de L'IA: ");
            if(this.isGloutonne){

                int[] coordIA = this.model.IAGlouton();
                System.out.println("coord IA " + coordIA[0]+ ", " +coordIA[1]);

                int[] resIA = this.model.changeValue(bleu, coordIA[0], coordIA[1]);
                changeScore(resIA[1], resIA[2]);

            } 
        }
        scan.close();

    }

    void playIA(){

    }

    private int[] chooseCoordinate(){
        int[] res = new int[2];
        Scanner scan = new Scanner(System.in);
        System.out.print("Veuillez entrez un numero de ligne :");
        res[0] = scan.nextInt();
        while(res[0] > model.getSide()-1 || res[0] < 0){
            System.out.println("Veuillez indiquez un numéro de ligne compris entre 0 et" + model.getSide()+ " exclus.");
            res[0] = scan.nextInt();
        }
        System.out.print("Veuillez entrez un numero de colonne :");
        res[1] = scan.nextInt();
        while(res[1] > model.getSide()-1 || res[1] < 0){
            System.out.println("Veuillez indiquez un numéro de colonne compris entre 0 et" + model.getSide()+ " exclus.");
            res[1] = scan.nextInt();
        }
        System.out.println("Vous avez choisi le point en ("+res[0] +","+res[1]+")");
        return res;
    }
    
    private void changeScore(int scoreAddBleu, int scoreAddRouge){
        this.scoreRouge += scoreAddRouge;
        this.scoreBleu += scoreAddBleu;
    }

    private void setBrave(boolean brave) {
        isBrave = brave;
    }

    private Model getModel(){ return this.model;}
    private void setModel(Model model){

        this.model = model;
    }
    
    private void setGloutonne(boolean g) {
    	this.isGloutonne = g;
    }
    
    public void readTextFile(String filename) throws IOException
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
    
}
