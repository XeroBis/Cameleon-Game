package model;

import java.util.ArrayList;

public class Plateau {
	private ArrayList<ArrayList<Integer>> tableau;
	private int uncolored_nb;
	public static int blanc = 0, bleu = 1, rouge = 2;


	/*
	 * Constructeur de Plateau
	 * 
	 * 
	 * @param k, le k qui permet de savoir la taille du plateau
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

	public void afficherPlateau() {
		for (int i = 0; i < tableau.size(); i++) {
			System.out.print("[");

			ArrayList<Integer> ligne = tableau.get(i);

			for (int j = 0; j < ligne.size() - 1; j++) {
				System.out.print((int) (ligne.get(j)));
				System.out.print(" , ");
			}
			System.out.print((int) (ligne.get(ligne.size() - 1)));
			System.out.println(']');
		}
	}

	public void changerValeur(int ligne, int col, int color) {
		this.tableau.get(ligne).set(col, color);
	}

	public int couleurCase(int ligne, int col) {
		return this.tableau.get(ligne).get(col);
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
