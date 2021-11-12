package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import model.Model;

public class Game {
    private boolean isBrave;
    private boolean isGloutonne;
    public static int blanc = 0, bleu = 1, rouge = 2;

    private Model model;

    public Game() throws IOException {
       loadFiles();
    }

    
    private void loadFiles() throws IOException {
    	Scanner scan = new Scanner(System.in);
    	
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
		
		if(preload) { // donc on load un fichier
			
			System.out.print("entrez un nom de fichier : ");
			String name = scan.next();
			
			System.out.println(System.getProperty("user.dir"));
			readTextFile(name);
			
			
		} else { // on load rien
			System.out.print("entrez la taille du plateau k (dans la formule 3 * 2 ^ k) : ");
			String k = scan.next();
			
			this.model.setNewK(Integer.parseInt(k));
		}
		
		
		
		
    }
    
    
    public void play() throws IOException {
    	this.loadFiles();
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
            this.setModel(new Model(n));
        } else {
            System.out.println("Vous avez donc choisi la version Téméraire !");
            this.setBrave(false);
            this.setModel(new Model(n));
        }
        /*
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
        */
        
        //scan.close();
    }

    private void to_string(){
        this.model.afficher();
    }
    
    

    public void playJvJ() {
        int playing = 0;
        Scanner scan = new Scanner(System.in);
        while(playing==0){
            this.to_string();

            System.out.println("JOUEUR 1 : ");
            int[] coord = chooseCoordinate();
            boolean play = this.model.colorationBrave(coord[1], coord[0], rouge);
            while(!play){
                System.out.println("La case que vous avez selectionnée est déjà colorié, veuillez choisir une autre case.");
                coord = chooseCoordinate();
                play = this.model.colorationBrave(coord[1],coord[0], rouge);
            }
            
            this.to_string();
            System.out.println("JOUEUR 2 : ");
            coord = chooseCoordinate();
            play = this.model.colorationBrave(coord[1], coord[0], bleu);
            while(!play){
                System.out.println("La case que vous avez selectionnée est déjà colorié, veuillez choisir une autre case.");
                coord = chooseCoordinate();
                play = this.model.colorationBrave(coord[1],coord[0], bleu);
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
        while(res[0] > model.getSize()-1 || res[0] < 0){
            System.out.println("Veuillez indiquez un numéro de ligne compris entre 0 et" + model.getSize()+ " exclus.");
            res[0] = scan.nextInt();
        }
        System.out.print("Veuillez entrez un numero de colonne :");
        res[1] = scan.nextInt();
        while(res[1] > model.getSize()-1 || res[1] < 0){
            System.out.println("Veuillez indiquez un numéro de colonne compris entre 0 et" + model.getSize()+ " exclus.");
            res[1] = scan.nextInt();
        }
        System.out.println("Vous avez choisi le point en ("+res[0] +","+res[1]+")");
        return res;
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
		
		this.model.setNewK(b);
		int ligne = 0;
		
		while ((str = reader.readLine()) != null)
		{
			for(int col = 0; col < str.length(); col++)
			{
				switch(str.charAt(col))
				{
				case 'R':
					this.model.coloration(ligne, col, 2);
				break;
				case 'B':
					this.model.coloration(ligne,  col,  1);
				break;
				default:
				break;
				}
			}
		ligne++;
		}
	}
    
    
    
}
