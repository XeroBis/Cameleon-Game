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
	public static int JvJ = 0, JvIA = 1, IAvIA = 2;
	private int variant;
	public static int blanc = 0, bleu = 1, rouge = 2; // si en jvj, bleu = joueur 1 et rouge = joueur 2

	private Model model;

	public Game() {
	}

	/*
	 * fonction lançant une partie du jeu.
	 */
	public void play(boolean test) {
		this.loadFiles();
		if (test) {
			this.fastParamBrave();
		} else {
			this.parametreOfGame();
		}
		if (this.variant == JvIA) {
			this.playIAvJ();
		} else if (this.variant == JvJ) {
			this.playJvJ();
		} else if (this.variant == IAvIA) {
			this.plauIAvIA();
		}
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

	private void fastParamBrave() {
		this.isBrave = false;
		this.isGloutonne = true;
		this.variant = IAvIA; // IA
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

		int ia = 0;
		System.out.println("Voulez-vous jouer avec un joueur? 0 , contre une IA? 1, ou laisser deux IA jouer? 2");
		ia = scan.nextInt();
		if (ia != 0) {
			//this.variant = this.JvIA;
			System.out.println(
					"L'IA peut jouer de deux façon différentes, Gloutonne ou Inteligente, que choisissez vous? (0/1)");
			int versionIA = -1;
			while (versionIA != 0 && versionIA != 1) {
				System.out.print(
						"Veuillez entrez la valeur 0 pour la version Gloutonne et 1 pour la version Intelligente.");
				versionIA = scan.nextInt();
			}
			if (versionIA == 0) {
				System.out.println("Vous avez donc choisi la version Gloutonne !");
				this.setGloutonne(true);
			} else {
				System.out.println("Vous avez donc choisi la version Intelligente !");
				this.setGloutonne(false);
			}
		} else {
			System.out.println("Vous avez donc choisi de jouer en Joueur versus Joueur.");
		}
		if (ia == 0) {
			this.variant = JvJ;
		} else if (ia == 2) {
			this.variant = this.IAvIA;
		} else if (ia == 1) {
			this.variant = this.JvIA;
		}

	}

	private void setGloutonne(boolean b) {
		this.isGloutonne = b;
	}

	/*
	 * Dessine sur le terminal le plateau de jeu
	 */
	private void to_string() {
		this.model.afficher();
	}

	private void endOfGame() {
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
			boolean play;
			play = this.playMove(coord[1], coord[0], bleu);
			while (!play) {
				System.out.println(
						"La case que vous avez selectionnée est déjà colorié, veuillez choisir une autre case.");
				coord = chooseCoordinate();
				play = this.playMove(coord[1], coord[0], bleu);
			}
			if (this.model.estTerminee()) {
				playing = false;
			} else {
				this.to_string();
				System.out.println("JOUEUR 2 : ");
				coord = chooseCoordinate();
				play = this.playMove(coord[1], coord[0], rouge);
				while (!play) {
					System.out.println(
							"La case que vous avez selectionnée est déjà colorié, veuillez choisir une autre case.");
					coord = chooseCoordinate();
					play = this.playMove(coord[1], coord[0], rouge);
				}
				System.out.println("uncolored nb: " + this.model.getPlateau().getUncolored_nb());
			}
			if (this.model.estTerminee()) {
				playing = false;
				this.to_string();
			}
		}
		scan.close();
		this.endOfGame();

	}

	/*
	 * Fonction permettant à un joueur de jouer contre une IA
	 */
	public void playIAvJ() {
		boolean playing = true;
		Scanner scan = new Scanner(System.in);
		this.to_string();
		while (playing) {

			System.out.println("Tour du Joueur : ");
			int[] coord = chooseCoordinate();
			boolean play = this.playMove(coord[1], coord[0], bleu);
			while (!play) {
				System.out.println(
						"La case que vous avez selectionnée est déjà colorié, veuillez choisir une autre case.");
				coord = chooseCoordinate();
				play = this.playMove(coord[1], coord[0], bleu);
			}
			if (this.model.estTerminee()) {
				playing = false;
			} else {
				this.to_string();
				System.out.println("Tour de l'IA : ");
				if (this.isBrave) {
					this.model.botBraveGlouton(rouge);
				} else if (!this.isBrave && this.isGloutonne) {
					this.model.botTemeraireGlouton(rouge);
				}
				this.to_string();
				if (this.model.estTerminee()) {
					playing = false;
				}
			}

		}
		this.to_string();
	}

	void plauIAvIA() {
		boolean playing = true;
		while (playing) {
			if (this.isBrave) {
				System.out.println("Tour de l'IA 1 : ");
				this.model.botBraveGlouton(bleu);
				this.to_string();
				if (this.model.estTerminee()) {
					playing = false;
				} else {
					System.out.println("Tour de l'IA 2 : ");
					this.model.botBraveGlouton(rouge);
					this.to_string();
					if (this.model.estTerminee()) {
						playing = false;
					}
				}
			} else if (!this.isBrave && this.isGloutonne) {
				System.out.println("Tour de l'IA 1 : ");
				this.model.botTemeraireGlouton(bleu);
				this.to_string();
				if (this.model.estTerminee()) {
					playing = false;
				} else {
					System.out.println("Tour de l'IA 2 : ");
					this.model.botTemeraireGlouton(rouge);
					
					if (this.model.estTerminee()) {
						playing = false;
					} else {
						this.to_string();
					}
				}
			}
		}
		this.to_string();

	}

	/*
	 * Fonction permettant au joueur de jouer un tour
	 */
	public boolean playMove(int i, int j, int couleur) {
		if (this.isBrave) {
			return this.model.colorationBrave(i, j, couleur);
		} else {
			return this.model.colorationTemeraire(i, j, couleur);
		}
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
		System.out.print("Veuillez entrez un numero de ligne :");
		res[1] = scan.nextInt();
		while (res[1] > model.getSize() - 1 || res[1] < 0) {
			System.out.println("Veuillez indiquez un numéro de ligne entre 0 et" + model.getSize() + " exclus.");
			res[1] = scan.nextInt();
		}
		System.out.println("Vous avez choisi le point en (" + res[0] + "," + res[1] + ")");
		return res;
	}

	/*
	 * change la valeur du boolean Brave
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
					this.model.RemplirTableau(ligne, col, 2);
					break;
				case 'B':
					this.model.RemplirTableau(ligne, col, 1);
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
