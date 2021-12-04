package model;

import java.util.ArrayList;

public class Plateau {
	private ArrayList<ArrayList<Integer>> tableau;
	private int uncolored_nb;
	public static int blanc = 0, bleu = 1, rouge = 2;

	public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
	
	
	/*
	 * @purpose Constructeur de Plateau
	 * @param k, le k qui permet de savoir la taille du plateau
	 * @complexity O(n^2), avec n = 3 * 2^k  
	 */
	public Plateau(int k) {
		int dim = (int) (3 * Math.pow(2, k));
		this.tableau = new ArrayList<ArrayList<Integer>>();

		for (int i = 0; i < dim; i++) {
			tableau.add(i, new ArrayList<Integer>());

			for (int j = 0; j < dim; j++) {
				tableau.get(i).add(j, blanc);
			}
		}
		this.uncolored_nb = dim * dim;
	}

	/*
	 * @purpose fonction permettant d'afficher le plateau dans le terminal
	 * @complexity O(n^2), n �tant la longueur du cot�
	 */
	public void afficherPlateau() {	
		
		for (int i = 0; i < tableau.size(); i++) {
			System.out.print("[");

			ArrayList<Integer> ligne = tableau.get(i);

			for (int j = 0; j < ligne.size()-1; j++) {
				if(ligne.get(j) == bleu) {
					System.out.print(ANSI_BLUE + "B" + ANSI_RESET);
				} else if(ligne.get(j) == rouge) {
					System.out.print(ANSI_RED + "R" + ANSI_RESET);
				} else if(ligne.get(j) == blanc) {
					System.out.print("A");
				}
				System.out.print(",");
			}
			if(ligne.get(ligne.size()-1) == bleu) {
				System.out.print(ANSI_BLUE + "B" + ANSI_RESET);
			} else if(ligne.get(ligne.size()-1) == rouge) {
				System.out.print(ANSI_RED + "R" + ANSI_RESET);
			} else if(ligne.get(ligne.size()-1) == blanc) {
				System.out.print("A");
			}
			System.out.println(']');
		}
	}

	/*
	 * @purpose change la valeur d'une case dans le plateau en la valeur donn�e par le param�tre color.
	 * @param ligne, col, les coordon�es de la case qu'il faut modifier, color, la couleur qu'il faut mettre � cet emplacement.
	 * @complexity O(1)
	 */
	public void changerValeur(int ligne, int col, int color) {
		this.tableau.get(ligne).set(col, color);
	}

	/*
	 * @purpose retourne la couleur d'une case se situant
	 * @param ligne, col : les coordon�es du point d'ou l'oon veut la couleur
	 * @return int, la couleur de la case
	 * @complexity O(1)
	 */
	public int couleurCase(int ligne, int col) {
		if ((ligne < 0) || (ligne  >= tableau.size()) || (col < 0) || (col >= tableau.size())) {
			return -1;
		}
		else {
			return this.tableau.get(ligne).get(col);
		}
	}

	public void decrementerUncoloredNb() {
		this.uncolored_nb = getUncolored_nb() - 1;
	}

	public boolean estEntierementColorie() {
		return getUncolored_nb() == 0;
	}

	public int getUncolored_nb() {
		return uncolored_nb;
	}
}
