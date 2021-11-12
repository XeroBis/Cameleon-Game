package model;

public class Model {

	private Plateau plateau;
	private QuadTree quadTree;
	private Tree tree;
	int scoreBleu;
	int scoreRouge;
	public static char blanc = 'A', bleu = 'B', rouge = 'R';
	boolean isBrave;
	private int side;
	boolean isTree;

	public Model(int n, boolean isBrave, boolean isTree) {
		this.scoreBleu = 0;
		this.scoreRouge = 0;
		this.isBrave = isBrave;
		this.plateau = new Plateau(n);
		if (isTree) {
			this.tree = new Tree(n);
			this.tree = tree.createFamily(null, n);
			this.side = this.tree.getSide();
			this.isTree = isTree;
		} else {
			if (!isBrave) {
				this.quadTree = new QuadTree(n);
			}
			this.side = this.plateau.getTaille();
		}
	}

	public int[] changeValue(char color, int x, int y) {
		if(this.isTree) {
			return this.tree.changeValue(tree, color, x, y, this.isBrave);
		}else {
			if (this.isBrave) {
				return changeValueBrave(color, x, y);
			} else {
				return changeValueTemeraire(color, x, y);
			}
		}
		
	}

	public int[] changeValueBrave(char color, int x, int y) {
		int[] score = new int[3];
		score = this.plateau.changeValue(color, x, y, false);
		if (score[0] == 0) {
			int[][] scoreV = new int[8][3];
			scoreV[0] = this.plateau.changeValue(color, x + 1, y + 1, true);
			scoreV[1] = this.plateau.changeValue(color, x + 1, y, true);
			scoreV[2] = this.plateau.changeValue(color, x + 1, y - 1, true);
			scoreV[3] = this.plateau.changeValue(color, x, y + 1, true);
			scoreV[4] = this.plateau.changeValue(color, x, y - 1, true);
			scoreV[5] = this.plateau.changeValue(color, x - 1, y + 1, true);
			scoreV[6] = this.plateau.changeValue(color, x - 1, y, true);
			scoreV[7] = this.plateau.changeValue(color, x - 1, y - 1, true);
			for (int i = 0; i < 8; i++) {
				if (scoreV[i][0] == 0) {
					score[1] += scoreV[i][1];
					score[2] += scoreV[i][2];
				}
			}
			return score;
		} else {
			score[0] = -1;
			return score;
		}
	}

	public int[] changeValueTemeraire(char color, int x, int y) {
		int[] score = new int[3];
		score = this.plateau.changeValue(color, x, y, false);
		if (score[0] == 0) {
			this.quadTree.addValue(x, y, false);
			// tous les voisins :
			int[][] scoreV = new int[8][3];
			scoreV[0] = changeValueTemeraireVoisin(color, x + 1, y + 1);
			scoreV[1] = changeValueTemeraireVoisin(color, x + 1, y - 1);
			scoreV[2] = changeValueTemeraireVoisin(color, x + 1, y);
			scoreV[3] = changeValueTemeraireVoisin(color, x, y + 1);
			scoreV[4] = changeValueTemeraireVoisin(color, x, y - 1);
			scoreV[5] = changeValueTemeraireVoisin(color, x - 1, y + 1);
			scoreV[6] = changeValueTemeraireVoisin(color, x - 1, y);
			scoreV[7] = changeValueTemeraireVoisin(color, x - 1, y - 1);
			for (int i = 0; i < 8; i++) {
				if (scoreV[i][0] == 0) {
					score[1] += scoreV[i][1];
					score[2] += scoreV[i][2];
				}
			}
			return score;
		} else {
			score[0] = -1;
			return score;
		}
	}

	public int[] changeValueTemeraireVoisin(char color, int x, int y) {
		int[] scoreV = new int[3];
		if (this.quadTree.addValue(x, y, true) == 0) {
			scoreV = this.plateau.changeValue(color, x, y, true);
			if (scoreV[0] == 0) {
				this.quadTree.addValue(x, y, false);
			}
		}
		return scoreV;
	}

	public int getSide() {
		return this.side;
	}

	private int IAGloutonTestValue(int color, int x, int y) {
		int pts = 0;
		char[] colors = new char[9];
		int indice = 0;
		for (int i = x - 1; i < x + 2; i++) {
			if (i >= 0 && i < this.getSide()) {
				for (int j = y - 1; j < y + 2; j++) {
					if (j >= 0 && j < this.getSide()) {
						if (i != x || j != y) {
							if (!this.isBrave) {
								// System.out.print("peut ajouteer" + this.quadTree.addValue(i, j, true));
								if (this.quadTree.addValue(i, j, true) == 0) {
									if (this.quadTree.getisFull()) {

									}
									colors[indice] = this.plateau.getValue(i, j);
								}
							} else {
								colors[indice] = this.plateau.getValue(i, j);
							}
						}
						indice++;
					}
				}
			}
		}

		for (int i = 0; i < 9; i++) {
			if (colors[i] == rouge) {
				pts++;
			}
		}
		return pts;

	}

	public int[] IAGlouton() {
		int[] res = new int[2];
		int maxScore = 0;
		for (int i = 0; i < this.getSide(); i++) {
			for (int j = 0; j < this.getSide(); j++) {
				if (this.plateau.getValue(i, j) == blanc) {
					int pts = this.IAGloutonTestValue(bleu, i, j);
					if (pts > maxScore) {
						maxScore = pts;
						res[0] = i;
						res[1] = j;
					}
				}
			}
		}
		return res;
	}

	public void to_string() {
		this.plateau.to_string();
	}

}
