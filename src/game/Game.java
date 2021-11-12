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
	public static int blanc = 0, bleu = 1, rouge = 2; // si en jvj, bleu = joueur 1 et rouge = joueur 2

	private Model model;

	public Game() {
	}

	/*
	 * fonction lançant une partie du jeu.
	 */
	public void play() {
		this.loadFiles();
		this.parametreOfGame();
		this.playJvJ();
	}

	/*
	 * fonction permettant le chargement d'un fichier txt afin de repmplir le
	 * plateau, on peut aussi décider de ne pas joindre de fichier et nous devrons
	 * dans ce cas donner un k
	 */
	private void loadFiles() {
		Scanner scan = new Scanner(System.in);

		boolean choose = true;
		boolean preload = false;

		while (choose) {
			System.out.print("Voulez vous charger un plateau (y/n) : ");
			String resp = scan.next();
			System.out.println();

			switch (resp) {
			case "y":
				preload = true;
				choose = false;
				break;
			case "n":
				preload = false;
				choose = false;
				break;
			default:
				System.out.println("Reponse non valide, seuls 'y' et 'n' sont valides");
				break;
			}
		}

		if (preload) { // donc on load un fichier

			System.out.print("entrez un nom de fichier : ");
			String name = scan.next();
			try {
				readTextFile(name);
			} catch (IOException e) {
				System.out.println("Fichier n'existe pas");
				e.printStackTrace();
			}

		} else { // on load rien
			System.out.print("entrez la taille du plateau k (dans la formule 3 * 2 ^ k) : ");
			String k = scan.next();

			this.model = new Model(Integer.parseInt(k));
		}

	}

	/*
	 * fonction nous permettant de parametrer tout les parametres pour une partie
	 * càd, la version du jeu, la version de l'ia
	 */
	private void parametreOfGame() {
		System.out.println("Le jeu commence !");
		System.out.println("Vous pouvez jouer en version Brave ou Téméraire que choisissez vous? (0/1)");
		Scanner scan = new Scanner(System.in);
		int version = -1;
		version = scan.nextInt();
		while (version != 0 && version != 1) {
			System.out.print("Veuillez entrez la valeur 0 pour la version Brave et 1 pour la version Téméraire.");
			version = scan.nextInt();
		}
		if (version == 0) {
			System.out.println("Vous avez donc choisi la version Brave !");
			this.setBrave(true);
		} else {
			System.out.println("Vous avez donc choisi la version Téméraire !");
			this.setBrave(false);
		}
		/*
		 * System.out.
		 * println("L'IA peut jouer de deux façon différentes, Gloutonne ou Inteligente, que choisissez vous? (0/1)"
		 * ); int versionIA = -1; while(versionIA != 0 && versionIA != 1) { System.out.
		 * print("Veuillez entrez la valeur 0 pour la version Gloutonne et 1 pour la version Intelligente."
		 * ); versionIA = scan.nextInt(); } if (versionIA== 0){
		 * System.out.println("Vous avez donc choisi la version Gloutonne !");
		 * this.setGloutonne(true); } else {
		 * System.out.println("Vous avez donc choisi la version Intelligente !");
		 * this.setGloutonne(false); }
		 */
	}

	/*
	 * Dessine sur le terminal le plateau de jeu
	 */
	private void to_string() {
		this.model.afficher();
	}
	
	private void endOfGameToString() {
		System.out.println("Fin de la partie !");
	}

	/*
	 * fonction lançant une partie Joueur versus Joueur
	 */
	public void playJvJ() {
		boolean playing = true;
		Scanner scan = new Scanner(System.in);
		while (playing) {
			this.to_string();

			System.out.println("JOUEUR 1 : ");
			int[] coord = chooseCoordinate();
			boolean play = this.model.colorationBrave(coord[1], coord[0], bleu);
			while (!play) {
				System.out.println(
						"La case que vous avez selectionnée est déjà colorié, veuillez choisir une autre case.");
				coord = chooseCoordinate();
				play = this.model.colorationBrave(coord[1], coord[0], bleu);
			}

			this.to_string();
			System.out.println("JOUEUR 2 : ");
			coord = chooseCoordinate();
			play = this.model.colorationBrave(coord[1], coord[0], rouge);
			while (!play) {
				System.out.println("La case que vous avez selectionnée est déjà colorié, veuillez choisir une autre case.");
				coord = chooseCoordinate();
				play = this.model.colorationBrave(coord[1], coord[0], rouge);
			}
			System.out.println("uncolored nb: " + this.model.getPlateau().uncolored_nb);
			if (this.model.estTerminee()) {
				playing = false;
			}
		}
		scan.close();

	}

	void playIAvJ() {
		// TO-DO
		// choisir qui commence
	}

	void plauIAvIA() {
		// TO-DO

	}

	/*
	 * fonction permettant le choix, par l'utilisateur des coordonnées de son
	 * prochain coup
	 * 
	 * @return int[], le tableau contenant les 2 coordonnées choisie du joueur
	 */
	private int[] chooseCoordinate() {
		int[] res = new int[2];
		Scanner scan = new Scanner(System.in);
		System.out.print("Veuillez entrez un numero de colonne :");
		res[0] = scan.nextInt();
		while (res[0] > model.getSize() - 1 || res[0] < 0) {
			System.out.println(
					"Veuillez indiquez un numéro de colonne compris entre 0 et" + model.getSize() + " exclus.");
			res[0] = scan.nextInt();
		}
		System.out.print("Veuillez entrez un numero de colonne :");
		res[1] = scan.nextInt();
		while (res[1] > model.getSize() - 1 || res[1] < 0) {
			System.out
					.println("Veuillez indiquez un numéro de colonne ligne entre 0 et" + model.getSize() + " exclus.");
			res[1] = scan.nextInt();
		}
		System.out.println("Vous avez choisi le point en (" + res[0] + "," + res[1] + ")");
		return res;
	}

	/*
	 * Set le boolean Brave
	 * 
	 * @param brave, si la version du jeu est brave
	 */
	private void setBrave(boolean brave) {
		isBrave = brave;
	}

	/*
	 * fonction permettant de lire un fichier txt et de remplir notre plateau avec
	 * les données contenu dans ce fichier
	 */
	public void readTextFile(String filename) throws IOException {
		String dir = System.getProperty("user.dir");

		File file = new File(dir + "\\" + filename);
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String str;
		str = reader.readLine();

		int a = Integer.parseInt(str);
		int b = 0;
		while (a != 3) {
			a = a / 2;
			b++;
		}

		this.model = new Model(b);
		int ligne = 0;

		while ((str = reader.readLine()) != null) {
			for (int col = 0; col < str.length(); col++) {
				switch (str.charAt(col)) {
				case 'R':
					this.model.coloration(ligne, col, 2);
					break;
				case 'B':
					this.model.coloration(ligne, col, 1);
					break;
				default:
					break;
				}
			}
			ligne++;
		}
		reader.close();
	}

}
